package com.zdmoney.credit.common;

/**
 * 定义常量信息
 * @author Ivan
 *
 */
public class WebConstants {
	
	/** 登录页面地址 */
	public static final String LOGIN_PAGE_URI = "/system/user/jumpLogin";
	
	/** 404页面地址 */
	public static final String PAGE_404_URI = "/views/error/404.jsp";
	
	/** 500页面地址 */
	public static final String PAGE_500_URI = "/views/error/500.jsp";
	
	/**报表逻辑错误页面*/
	public static final String REPORT_ERROR_PAGE = "error/reportErrorPage"; 
	
	/**
	 * 权限类型：菜单
	 */
	public static final String PERM_TYPE_MENU = "1";
	
	/**
	 * 权限类型：功能
	 */
	public static final String PERM_TYPE_FUNCTION = "2";
	
	/** 存放登陆者ID的KEY值 **/
	public static final String SESSION_USER_ID_KEY = "tmpUserId";
	
	/** 存放登陆者信息的KEY值 **/
	public static final String SESSION_USER_INFO_KEY = "user";
	
}
