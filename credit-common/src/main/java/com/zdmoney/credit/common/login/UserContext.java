package com.zdmoney.credit.common.login;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.login.vo.UserCoreDetails;

/**
 * 保存登陆者信息
 * 
 * @author Ivan
 */
public class UserContext {

	private static final ThreadLocal<User> session = new ThreadLocal<User>();

	/**
	 * 获取登陆者信息
	 * 
	 * @return
	 */
	public static User getUser() {
		/** 获取登陆者信息 **/
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			/**
			 * Spring Security 容器不存在已认证的员工 从本地线程获取 一般发起HttpClient请求时，通过此方法获取员工信息
			 **/
			return session.get();
		}
		Object curUser = authentication.getPrincipal();
		if (curUser != null && curUser instanceof UserCoreDetails) {
			UserCoreDetails userCoreDetails = (UserCoreDetails) curUser;
			if (userCoreDetails != null) {
				return userCoreDetails.getUser();
			}
		} else {
			/** anonymous 代表匿名身份 **/

		}
		return null;
	}

	/**
	 * 绑定登陆者信息
	 * 
	 * @param user
	 */
	public static void setUser(User user) {
		session.set(user);
	}

	/**
	 * 清空登陆者信息
	 */
	public static void clear() {
		session.set(null);
	}
}
