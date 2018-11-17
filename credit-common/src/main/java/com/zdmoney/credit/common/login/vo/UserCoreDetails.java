package com.zdmoney.credit.common.login.vo;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/***
 * Spring Security 预登陆者信息
 * @author Ivan
 *
 */
public class UserCoreDetails extends User {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3745962005875108506L;
	
//	private ComEmployee comEmployee;
	
	private com.zdmoney.credit.common.login.vo.User user;
	
	public UserCoreDetails(String username, String password,
			Collection<? extends GrantedAuthority> authorities,
			com.zdmoney.credit.common.login.vo.User user) {
		super(username, password, true, true, true,true, authorities);
//		this.comEmployee = comEmployee;
		this.user = user;
	}
	
	public UserCoreDetails(String username, String password, boolean enabled,
			boolean accountNonExpired, boolean credentialsNonExpired,
			boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities,
			com.zdmoney.credit.common.login.vo.User user) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired,
				accountNonLocked, authorities);
//		this.comEmployee = comEmployee;
		this.user = user;
	}

//	public ComEmployee getComEmployee() {
//		return comEmployee;
//	}
//
//	public void setComEmployee(ComEmployee comEmployee) {
//		this.comEmployee = comEmployee;
//	}

	public com.zdmoney.credit.common.login.vo.User getUser() {
		return user;
	}

	public void setUser(com.zdmoney.credit.common.login.vo.User user) {
		this.user = user;
	}

	
	
}
