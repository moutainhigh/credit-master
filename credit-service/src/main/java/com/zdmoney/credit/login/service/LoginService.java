package com.zdmoney.credit.login.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.tree.EasyUITree;
import com.zdmoney.credit.common.util.Assert;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.system.domain.ComEmployee;
import com.zdmoney.credit.system.domain.ComOrganization;
import com.zdmoney.credit.system.domain.ComPermission;
import com.zdmoney.credit.system.domain.ComRole;
import com.zdmoney.credit.system.service.pub.IComEmployeeService;
import com.zdmoney.credit.system.service.pub.IComOrganizationService;
import com.zdmoney.credit.system.service.pub.IComPermissionService;
import com.zdmoney.credit.system.service.pub.IComRoleService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;

/**
 * 业务系统核心登陆业务层
 * 
 * @author Ivan
 *
 */
@Service
public class LoginService {

	protected static Log logger = LogFactory.getLog(LoginService.class);

	/** 各业务系统菜单标识 **/
	public static String MENU_SYSTEM_FLAG = "system.thirdparty|system.credit.core.url";

	@Autowired
	@Qualifier("comEmployeeServiceImpl")
	IComEmployeeService comEmployeeServiceImpl;

	@Autowired
	@Qualifier("comOrganizationServiceImpl")
	IComOrganizationService comOrganizationServiceImpl;

	@Autowired
	@Qualifier("comRoleServiceImpl")
	IComRoleService comRoleServiceImpl;

	@Autowired
	@Qualifier("comPermissionServiceImpl")
	IComPermissionService comPermissionServiceImpl;

	@Autowired
	@Qualifier("sysParamDefineServiceImpl")
	ISysParamDefineService sysParamDefineServiceImpl;

	/**
	 * 获取登陆者Vo信息
	 * 
	 * @param userId
	 *            登陆者编号
	 * @return
	 */
	public User getLoginUserInfo(Long userId) {
		ComEmployee comEmployee = comEmployeeServiceImpl.get(userId);
		Assert.notNull(comEmployee, "未获取员工信息");
		Assert.notNull(comEmployee.getId(), "未获取员工信息");
		Assert.notNullAndEmpty(comEmployee.getUsercode(), "未获取到员工工号信息");
		Assert.notNull(comEmployee.getOrgId(), "未获取到员工营业网点信息");
		Assert.notNullAndEmpty(comEmployee.getPassword(), "未获取到员工密码信息");

		/** 营业网点编号（PK） **/
		Long orgId = comEmployee.getOrgId();
		ComOrganization comOrganization = comOrganizationServiceImpl.get(orgId);

		Assert.notNull(comOrganization, "员工营业网点信息为空");
		Assert.notNullAndEmpty(comOrganization.getOrgCode(), "未获取到员工营业网点代码信息");
		Assert.notNullAndEmpty(comOrganization.getName(), "未获取到员工营业网点名称信息");

		User user = new User();
		/** 员工编号（PK） **/
		user.setId(comEmployee.getId());
		/** 员工姓名 **/
		user.setName(comEmployee.getName());
		/** 员工工号 **/
		user.setUserCode(comEmployee.getUsercode());
		/** 员工密码 **/
		user.setPassword(comEmployee.getPassword());
		/** 营业网点编号（PK） **/
		user.setOrgId(comEmployee.getOrgId());
		/** 营业网点代码 **/
		user.setOrgCode(comOrganization.getOrgCode());
		/** 营业网点名称 **/
		user.setOrgName(comOrganization.getName());
		user.setEmployeeType(comEmployee.getEmployeeType());
		/** 登陆时间 **/
		user.setLoginTime(new Date());
		user.setDepartment_type(null == comOrganization.getDepartmentType() ? "" : comOrganization.getDepartmentType());
		return user;
	}

	/**
	 * 查询登陆者角色及菜单资源信息
	 * 
	 * @param user
	 * @param request
	 */
	public List<GrantedAuthority> queryResource(User user) {
		// , HttpServletRequest request
		/** 保存登陆者所拥有的URL权限 **/
		List<GrantedAuthority> authoritys = new ArrayList<GrantedAuthority>();

		// 保存登陆者拥有的权限资源ID
		List<Long> myResource = new ArrayList<Long>();

		// 角色封装MAP
		Map<String, Object> roleMap = new HashMap<String, Object>();
		// 查找用户的角色
		List<ComRole> roles = comRoleServiceImpl.getRolesByUser(user.getId());
		// 通过角色查找拥有的权限
		for (ComRole role : roles) {
			roleMap.put(role.getId() + "", role.getRoleName());
			List<ComPermission> permListByRole = comPermissionServiceImpl.getPermissionByRole(role.getId());
			for (ComPermission perm : permListByRole) {
				/** 将拥有权限及资源放到集合中，最终提交到Spring Security 进行权限校验 **/
				toAuthority(authoritys, perm.getPermUrl());
				myResource.add(perm.getId());
				logger.debug("角色查找权限：" + perm.getPermName() + ">>" + perm.getPermUrl() + "  System_Flag: "
						+ perm.getSystemFlag() + " <<< type>>" + perm.getPermType() + ":" + perm.getSort());
			}
		}
		// 设置用户角色
		user.setRoleMap(roleMap);
		// 通过人员查找拥有的权限
		List<ComPermission> permListBycomEmployee = comPermissionServiceImpl.getPermissionByComEmployee(user.getId());
		for (ComPermission perm : permListBycomEmployee) {
			/** 将拥有权限及资源放到集合中，最终提交到Spring Security 进行权限校验 **/
			toAuthority(authoritys, perm.getPermUrl());
			myResource.add(perm.getId());
			logger.debug("人员查找权限：" + perm.getPermName() + ">>" + perm.getPermUrl() + "  System_Flag: "
					+ perm.getSystemFlag() + " <<< type>>" + perm.getPermType() + ":" + perm.getSort());
		}

		logger.debug("【用户" + user.getName() + "的权限】:" + myResource);

		/** 取员工所有菜单（有权限） **/
		EasyUITree easyUITree = new EasyUITree();
		easyUITree.setId("0");
		easyUITree.setText("Root");
		loadUserAllMenuSource(easyUITree, myResource);
		user.setMenuResource(easyUITree.getChildren());
		return authoritys;
	}

	/**
	 * 将权限URL生成SimpleGrantedAuthority实例
	 * 
	 * @param container
	 * @param permUrl
	 * @return
	 */
	public SimpleGrantedAuthority toAuthority(List<GrantedAuthority> container, String permUrl) {
		SimpleGrantedAuthority simpleGrantedAuthority = null;
		if (permUrl.startsWith("#")) {

		} else {
			simpleGrantedAuthority = new SimpleGrantedAuthority(permUrl);
			container.add(simpleGrantedAuthority);
		}
		return simpleGrantedAuthority;
	}

	/**
	 * 生成菜单URL地址
	 * 
	 * @param comPermission
	 *            菜单对象
	 * @return
	 */
	public String getResourceUrl(ComPermission comPermission) {
		String url = Strings.convertValue(comPermission.getPermUrl(), String.class);
		if (url.startsWith("#")) {
			return "#";
		}
		/** 外部系统标识 （空=本系统，非空=外部系统，详细见SYS_PARAM_DEFINE表定义） **/
		String systemFlag = comPermission.getSystemFlag();
		if (Strings.isNotEmpty(systemFlag)) {
			/** 此菜单链接到外部系统 **/

			/** 获取外部系统域名地址 **/
			String[] flag = systemFlag.split("[|]");
			if (flag.length == 2) {
				/** 从数据字典获取外部域名地址 **/
				String externalUrl = sysParamDefineServiceImpl.getSysParamValueCache(flag[0], flag[1]);
				if (!Strings.isEmpty(externalUrl)) {
					url = externalUrl + url;
				}
				return url;
			}
			return "#";
		} else {
			/** 此菜单链接到本系统 **/
			return url;
		}
	}

	/**
	 * 递归查询某员工所有菜单（权限已过滤）
	 * 
	 * @param easyUITree
	 * @param myResource
	 */
	public void loadUserAllMenuSource(EasyUITree easyUITree, List<Long> myResource) {
		/** 跟据父结点编号查询子菜单 **/
		ComPermission comPermission = new ComPermission();
		comPermission.setParentId(Strings.convertValue(easyUITree.getId(), Long.class));
		comPermission.setPermType("1");
		/** 加载显示的菜单 **/
		comPermission.setIsDisplay(1L);
		List<ComPermission> comPermissionList = comPermissionServiceImpl.findComPermission(comPermission);
		List<EasyUITree> resources = convertMenuResourceList(comPermissionList, myResource);
		if (CollectionUtils.isNotEmpty(resources)) {
			easyUITree.getChildren().addAll(resources);
			for (int i = 0; i < resources.size(); i++) {
				EasyUITree menuResourceInner = resources.get(i);
				loadUserAllMenuSource(menuResourceInner, myResource);
			}
		}
	}

	/**
	 * 将ComPermission 转换成 EasyUITree 同时过滤掉无权限的菜单
	 * 
	 * @param comPermissionList
	 * @param myResource
	 * @return
	 */
	public List<EasyUITree> convertMenuResourceList(List<ComPermission> comPermissionList, List<Long> myResource) {
		List<EasyUITree> resources = new ArrayList<EasyUITree>();
		for (int i = 0; i < comPermissionList.size(); i++) {
			ComPermission comPermission = comPermissionList.get(i);
			if (myResource.contains(comPermission.getId())) {
				/** 生成菜单实际URL地址 **/
				String url = getResourceUrl(comPermission);

				EasyUITree easyUITree = new EasyUITree();
				easyUITree.setId(comPermission.getId() + "");
				easyUITree.setIconCls(comPermission.getIcon());
				easyUITree.getAttributes().put("url", url);
				easyUITree.setText(comPermission.getPermName());
				resources.add(easyUITree);
			}
		}
		return resources;
	}

}
