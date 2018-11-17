package com.zdmoney.credit.login.core;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.vote.RoleVoter;

/**
 * 权限验证器
 * 支持普通字符串验证
 * @author Ivan
 *
 */
public class RoleVoterImpl extends RoleVoter {
	@Override
	public boolean supports(ConfigAttribute attribute) {
		if (attribute instanceof SecurityConfig) {
			return true;
		} else {
			return false;
		}
	}
}
