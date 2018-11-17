package com.zdmoney.credit.common.redis;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

/**
 * Redis客户端 工具
 * 
 * @author Ivan
 *
 */
@Component
public class RedisClientUtil {

	protected static Log logger = LogFactory.getLog(RedisClientUtil.class);

	@Autowired(required = false)
	RedisTemplate redisTemplate;

	@Value("${redis.validCode.dbIndex:0}")
	private int dbIndex;

	public RedisSerializer<String> getStringSerializer() {
		return redisTemplate.getStringSerializer();
	}

	/**
	 * 保存字符串Key Value数据
	 * 
	 * @param key
	 * @param seconds
	 *            过期时间（秒）
	 * @param value
	 * @return
	 */
	public void setValue(final String key, final long seconds, final String value) {
		redisTemplate.execute(new RedisCallback() {
			public Long doInRedis(RedisConnection connection) throws DataAccessException {
				connection.select(dbIndex);
				RedisSerializer<String> serializer = getStringSerializer();
				byte[] redisKey = serializer.serialize(key);
				byte[] redisValue = serializer.serialize(value);
				connection.setEx(redisKey, seconds, redisValue);
				return 1L;
			}
		});
	}

	/**
	 * 获取key对应的Value
	 * 
	 * @param key
	 * @return
	 */
	public String getValue(final String key) {
		return (String) redisTemplate.execute(new RedisCallback() {
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				connection.select(dbIndex);
				RedisSerializer<String> serializer = getStringSerializer();
				byte[] redisValue = connection.get(serializer.serialize(key));
				return serializer.deserialize(redisValue);
			}
		});
	}

}
