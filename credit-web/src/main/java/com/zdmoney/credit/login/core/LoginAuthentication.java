package com.zdmoney.credit.login.core;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.login.vo.UserCoreDetails;
import com.zdmoney.credit.login.service.LoginService;
import com.zdmoney.credit.system.domain.ComEmployee;
import com.zdmoney.credit.system.service.pub.IComEmployeeService;

/***
 * 登陆者信息校验
 * @author Ivan
 *
 */
@Service
public class LoginAuthentication implements AuthenticationUserDetailsService<Authentication>,UserDetailsService {
	
	@Autowired
	@Qualifier("comEmployeeServiceImpl")
	IComEmployeeService comEmployeeServiceImpl;
	
	@Autowired
	@Qualifier("loginService")
	LoginService loginService;

	/***
	 * 自主登陆或单点登陆，验证登陆者信息
	 */
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		try {
			/** 验证用户名账号信息 Begin **/
			/** 员工工号 **/
			String userCode = username;
			ComEmployee comEmployee = comEmployeeServiceImpl.findByUserCode(userCode);
			if (comEmployee == null) {
				throw new UsernameNotFoundException("账号有误!");
			}
			
			Long userId = comEmployee.getId();
			/** 获取登陆者Vo信息 **/
			User user = loginService.getLoginUserInfo(userId);
			/** 查询角色及菜单权限 **/
			/** authoritys 保存登陆者拥有的权限信息 **/
			List<GrantedAuthority> authoritys = loginService.queryResource(user);
			
			/** 验证用户名账号信息 End **/
			return buildUserForAuthentication(comEmployee,authoritys,user);
		} catch(Exception ex) {
			throw new UsernameNotFoundException(ex.getMessage(),ex);
		}
	}
	
	/***
	 * 单点登陆认证通过回调此方法
	 */
	@Override
	public UserDetails loadUserDetails(Authentication token)
			throws UsernameNotFoundException {
		String name = token.getName();
		//name = name.split("\\\\")[1];
		return loadUserByUsername(name);
	}
	
	private UserCoreDetails buildUserForAuthentication(ComEmployee comEmployee,List authorities,User user){
		return new UserCoreDetails(comEmployee.getUsercode(),comEmployee.getPassword()
				,true,true,true,true,authorities,user);
	}

}