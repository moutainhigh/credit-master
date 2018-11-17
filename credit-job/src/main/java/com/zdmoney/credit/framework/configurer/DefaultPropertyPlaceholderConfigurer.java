package com.zdmoney.credit.framework.configurer;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.zdmoney.credit.common.SystemConfig;
import com.zdmoney.credit.common.constant.system.Environment;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.util.ThreeDes;

/**
 * 读取配置文件（properties） 做二次翻译工作
 * 
 * @author Ivan
 *
 */
public class DefaultPropertyPlaceholderConfigurer
		extends
			PropertyPlaceholderConfigurer {
	
	protected static Log logger = LogFactory.getLog(DefaultPropertyPlaceholderConfigurer.class);
	
	/**
	 * 数据库用户名Key
	 */
	private final static String JDBC_USER_NAME_KEY = "jdbc.username";
	
	/**
	 * 数据库密码Key
	 */
	private final static String JDBC_PASS_WORD_KEY = "jdbc.password";

	boolean ignoreResourceNotFound = false;

	private Resource[] locations;

	private Resource keyLocation;

	private String fileEncoding;

	public void setKeyLocation(Resource keyLocation) {
		this.keyLocation = keyLocation;
	}

	public void setLocations(Resource[] locations) {
		this.locations = locations;
	}

	public void loadProperties(Properties props) throws IOException {
		if (this.locations != null) {
			for (Resource location : this.locations) {
				if (logger.isInfoEnabled()) {
					logger.info("Loading properties file from " + location);
				}
				try {
					PropertiesLoaderUtils.fillProperties(props,
							new EncodedResource(location, this.fileEncoding));
				} catch (IOException ex) {
					if (ignoreResourceNotFound) {
						if (logger.isWarnEnabled()) {
							logger.warn("Could not load properties from "
									+ location + ": " + ex.getMessage());
						}
					} else {
						throw ex;
					}
				}
			}
		}
		/** 获取系统环境（开发、测试及生产） **/
		Environment environment = SystemConfig.get().environment;
		try {
			/** 测试及生产环境进行数据库用户名及密码解密操作 **/
			switch (environment) {
				case DEVELOP :
					/** 开发环境 **/
					break;
				case TEST :
					/** 测试环境 **/
				case PRODUCTION :
					/** 生产环境 **/
					if (Strings.isNotEmpty(props.getProperty(JDBC_USER_NAME_KEY))) {
						String jdbcUserName = props.getProperty(JDBC_USER_NAME_KEY);
						jdbcUserName = new String(ThreeDes.decryptMode(ThreeDes.keyBytes, ThreeDes.hex2byte(jdbcUserName.getBytes())));
						props.setProperty(JDBC_USER_NAME_KEY, jdbcUserName);
					}
					if (Strings.isNotEmpty(props.getProperty(JDBC_PASS_WORD_KEY))) {
						String jdbcPassWord = props.getProperty(JDBC_PASS_WORD_KEY);
						jdbcPassWord = new String(ThreeDes.decryptMode(ThreeDes.keyBytes, ThreeDes.hex2byte(jdbcPassWord.getBytes())));
						props.setProperty(JDBC_PASS_WORD_KEY, jdbcPassWord);
					}
					break;
				default :
					throw new PlatformException("Get System Environment State Error");
			}
		} catch(Exception ex) {
			logger.error("应用启动时数据库用户名或密码解密出现异常",ex);
			throw ex;
		}
	}

}
