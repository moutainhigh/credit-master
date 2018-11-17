package com.zdmoney.credit.framework.dao.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.framework.domain.BaseDomain;

/**
 * 数据库Dao基类，定义一些公共方法（接口层）
 * @author Ivan
 *
 */
public interface IBaseDao<T extends BaseDomain> {
	/** 插入数据：{mapper.xml需要实现} */
	T insert(T object);

	/** 根据id删除数据：{mapper.xml需要实现} */
	void deleteById(Long id);

	/** 根据idList删除数据：{mapper.xml需要实现} */
	void deleteByIdList(BaseDomain baseDomain);

	/** 更新数据{mapper.xml需要实现} */
	int update(BaseDomain baseDomain);
	
	/** 更新特定数据*/
	int updateByPrimaryKeySelective(BaseDomain baseDomain);

	/** 根据id查找数据行：{mapper.xml需要实现} */
	T get(Long id);

	/** 通过查询vo查找符合条件的数据行：{mapper.xml需要实现} */
	List<T> findListByVo(BaseDomain baseDomain);
	
	/** 通过查询Map查找符合条件的数据行：{mapper.xml需要实现} */
	List<T> findListByMap(Map<String,Object> paramMap);

	/** 根据多个字段查询指定数据行是否存在 {需要自己写mapper实现} */
	boolean exists(Map<String, Object> map);

	/** 根据查询条件vo物理分页查询出数据行：{mapper.xml需要实现} */
	Pager findWithPg(BaseDomain baseDomain);
	
	/** 根据查询条件Map物理分页查询出数据行：{mapper.xml需要实现} */
	Pager findWithPgByMap(Map<String,Object> paramMap);
	
	/** 根据vo查询指定数据行是存在：{调用findListByDto方法不需要实现} */
	T get(BaseDomain baseDomain);

	/** 根据id查询指定数据行是否存在：{调用get方法不需要实现} */
	boolean exists(Long id);
	
	/** 查询单结果集(直接传Sql查询) **/
	public Object selectOne(String sql);
	
	/** 跟据实体查询总记录数 {mapper.xml需要实现} */
	public int queryCount(BaseDomain baseDomain);
	
	/** 查询数据库中所有的记录 */
	public List<T> findAllList();
	
	/**
	 * 获取sql语句
	 * @param id
	 * @param params
	 * @return
	 */
	public String getSql(String id,Object params);
}
