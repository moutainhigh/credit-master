package com.zdmoney.credit.common;

import org.springframework.beans.factory.config.PreferencesPlaceholderConfigurer;

import com.zdmoney.credit.common.constant.system.Environment;

/***
 * 系统环境变更
 * @author Ivan
 *
 */
public class SystemConfig extends PreferencesPlaceholderConfigurer {
	
	private static SystemConfig this_;
	
	public Environment environment = Environment.DEVELOP;
	
	public SystemConfig(String environment) {
		this_ = this;
		this.environment = Environment.valueOf(environment);
	}
	
	public static SystemConfig get() {
		return this_;
	}

	

}
