package com.zdmoney.credit.common.login.vo;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.zdmoney.credit.common.tree.EasyUITree;
import com.zdmoney.credit.common.util.Strings;

/**
 * 登陆者信息(存入内存)
 * 
 * @author Ivan
 *
 */
public class User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2952730115112984605L;
	/** 员工编号（PK） **/
	private Long id;
	/** 营业网点编号（PK） **/
	private Long orgId;
	/** 员工姓名 **/
	private String name;
	/** 员工工号 **/
	private String userCode;
	/**
	 * 员工类型
	 */
	private String employeeType;

	/**
	 * 员工密码
	 */
	private String password;
	/** 营业网点代码 **/
	private String orgCode;
	/** 营业网点名称 **/
	private String orgName;
	/** 登陆时间 **/
	private Date loginTime;

	private String department_type;
	/** 用户角色 **/
	private Map<String, Object> roleMap;
	/** 首页左侧菜单树信息 **/
	private List<EasyUITree> menuResource;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserCode() {
		return userCode;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getDepartment_type() {
		return department_type;
	}

	public void setDepartment_type(String department_type) {
		this.department_type = department_type;
	}

	public String getEmployeeType() {
		return employeeType;
	}

	public void setEmployeeType(String employeeType) {
		this.employeeType = employeeType;
	}

	public Map<String, Object> getRoleMap() {
		return roleMap;
	}

	public void setRoleMap(Map<String, Object> roleMap) {
		this.roleMap = roleMap;
	}

	public List<EasyUITree> getMenuResource() {
		return menuResource;
	}

	public void setMenuResource(List<EasyUITree> menuResource) {
		this.menuResource = menuResource;
	}

	/**
	 * 判断用户是否拥有某项权限
	 * 
	 * @param url
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean ifAnyGranted(String url) {
		if (Strings.isEmpty(url)) {
			return false;
		}
		// 获取用户具有的所有权限
		Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) SecurityContextHolder.getContext()
				.getAuthentication().getAuthorities();
		Iterator<GrantedAuthority> ite = authorities.iterator();
		GrantedAuthority authority = null;
		while (ite.hasNext()) {
			authority = (GrantedAuthority) ite.next();
			if (url.equals(authority.getAuthority())) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 判断用户是否有某项角色
	 * @param string
	 * @return
	 */
	public boolean ifAnyRole(String roleName) {
		if (Strings.isEmpty(roleName)) {
			return false;
		}
		//获取用户的所有角色
		Collection<Object> values = roleMap.values();
		if(!CollectionUtils.isEmpty(values)){
			for (Object value : values) {
				if(roleName.equals(value.toString())){
					return true;
				}
			}			
		}
		return false;
	}
}
