package com.zdmoney.credit.core.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.app.dao.pub.IAppEmployeeManagerDao;
import com.zdmoney.credit.app.dao.pub.IAppManagerOrganizationDao;
import com.zdmoney.credit.app.domain.AppEmployeeManager;
import com.zdmoney.credit.app.domain.AppOrganizationManager;
import com.zdmoney.credit.common.constant.AppManagerEnum;
import com.zdmoney.credit.common.constant.ComOrganizationEnum;
import com.zdmoney.credit.common.constant.EmployeeTypeEnum;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.util.coreUtil.Constants;
import com.zdmoney.credit.common.vo.core.LoginCheckVo;
import com.zdmoney.credit.common.vo.core.LoginErrorVo;
import com.zdmoney.credit.core.service.pub.ILoginCheckCoreService;
import com.zdmoney.credit.system.dao.pub.IComEmployeeDao;
import com.zdmoney.credit.system.dao.pub.IComEmployeeRoleDao;
import com.zdmoney.credit.system.dao.pub.IComOrganizationDao;
import com.zdmoney.credit.system.domain.ComEmployee;
import com.zdmoney.credit.system.domain.ComEmployeeRole;
import com.zdmoney.credit.system.domain.ComOrganization;

@Service
public class LoginCheckCoreServiceImpl implements ILoginCheckCoreService {

	private static final Logger logger = Logger.getLogger(LoginCheckCoreServiceImpl.class);

	@Autowired
	@Qualifier("comEmployeeDaoImpl")
	IComEmployeeDao comEmployeeDaoImpl;

	@Autowired
	@Qualifier("comOrganizationDaoImpl")
	IComOrganizationDao comOrganizationDaoImpl;

	@Autowired
	IAppManagerOrganizationDao appManagerOrganizationDao;

	@Autowired
	IAppEmployeeManagerDao appEmployeeManagerDao;
	
	@Autowired
	IComEmployeeRoleDao comEmployeeRoleDao;
	// 存放用户名和失败次数
	private static Map<String, LoginErrorVo> loginUserMap = new ConcurrentHashMap<String, LoginErrorVo>();

	@Override
	public Map<String, Object> loginCkeck(LoginCheckVo params, String projectNo) {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		/** 传过来的密码已进行MD5加密 **/
		String password = params.getPassWord();
		// password = MD5Util.md5Hex(password);
		/** 用户名 **/
		String userCode = params.getUserCode();
		/** 根据工号查找用户 **/
		ComEmployee comEmployee = comEmployeeDaoImpl.findEmployeeByUserCode(userCode);
		if (loginUserMap.size() > 0) {

			LoginErrorVo loginVo = (LoginErrorVo) loginUserMap.get(userCode);
			if (loginVo != null && loginVo.getLockStartTime() != null) {
				long curren = System.currentTimeMillis();
				long time = (curren - loginVo.getLockStartTime().getTime());
				if (time < 5 * 60 * 1000) {
					resultMap.put("resCode", Constants.DATA_ERR_CODE);
					resultMap.put("resMsg", "密码输入错误超出2次，请于5分钟后再登录！");
					return resultMap;
				}
			}
		}
		if (comEmployee != null && comEmployee.getPassword().equals(password)) {
			LoginCheckVo loginCheckVo = new LoginCheckVo();
			loginCheckVo.setId(comEmployee.getId());
			loginCheckVo.setUserCode(comEmployee.getUsercode());
			loginCheckVo.setName(comEmployee.getName());
			loginCheckVo.setIsFirst(comEmployee.getIsFirst());
			if (StringUtils.isNotEmpty(comEmployee.getInActive()) && "f".endsWith(comEmployee.getInActive().trim())) {
				resultMap.put("resCode", Constants.DATA_ERR_CODE);
				resultMap.put("resMsg", "该员工已离职!");
			} else {
				if ("2".equals(projectNo)) {
					/** 录单App登陆 **/
					AppOrganizationManager appOrganizationManager = appManagerOrganizationDao
							.selectAppOrganizationManagerByOrgId(comEmployee.getOrgId());
					AppEmployeeManager appEmployeeManager = appEmployeeManagerDao
							.selectAppEmployeeManagerByEmployeeId(comEmployee.getId());
					resultMap = loginPermission(appOrganizationManager, appEmployeeManager,params.getNewOrOld(),comEmployee);

					/** 登陆成功之后 将首次登陆状态 更新成 非首次登陆状态 **/
					if (Constants.SUCCESS_CODE.equals(resultMap.get("resCode"))) {
						/** 查询客户经理所在区域/分部 营业部以及所属业务组 信息 **/
						Map map = comOrganizationDaoImpl.queryPartOrgName(comEmployee.getOrgId());
						/** 区域/分部 **/
						String areaInfo = "";
						/** 营业部 **/
						String branchInfo = "";
						/** 所属业务组 **/
						String groupInfo = "";
						if (map != null) {
							String vLevel = Strings.parseString(map.get("V_LEVEL"));
							String name1 = Strings.parseString(map.get("NAME1"));
							String name2 = Strings.parseString(map.get("NAME2"));
							String name3 = Strings.parseString(map.get("NAME3"));
							String name4 = Strings.parseString(map.get("NAME4"));
							String name5 = Strings.parseString(map.get("NAME5"));
							String name6 = Strings.parseString(map.get("NAME6"));
							/** 转换所需数据 **/
							if (ComOrganizationEnum.Level.V105.getName().equals(vLevel)) {
								groupInfo = name6;
								branchInfo = name5;
								areaInfo = name2 + "/" + name3;
							} else if (ComOrganizationEnum.Level.V104.getName().equals(vLevel)) {
								branchInfo = name6;
								areaInfo = name3 + "/" + name4;
							} else if (ComOrganizationEnum.Level.V103.getName().equals(vLevel)) {
								areaInfo = name4 + "/" + name5;
							} else if (ComOrganizationEnum.Level.V102.getName().equals(vLevel)) {
								areaInfo = name5 + "/" + name6;
							} else if (ComOrganizationEnum.Level.V101.getName().equals(vLevel)) {
								areaInfo = name6;
							}
							//设置orgCode和userType
							ComOrganization comOrganization = comOrganizationDaoImpl.get(comEmployee.getOrgId());
							//判断是否有 信贷-营业部-业务主任 这个角色
							Map<String, Object> maps = new HashMap<String, Object>();
							maps.put("usercode", comEmployee.getUsercode());
							maps.put("roleName", "信贷-营业部-业务主任");
							int num = comEmployeeRoleDao.findComEmployeeRoleByUserCodeAndRoleName(maps);
							if(num>0){//存在该角色
								loginCheckVo.setOrgCode(comOrganization.getOrgCode());
								loginCheckVo.setUserType("4");//1.客户经理 2.客服 3.未知 4.业务主任
							}else{
								if(comEmployee.getEmployeeType().equals(EmployeeTypeEnum.客服.getValue())){
									if(ComOrganizationEnum.Level.V105.getName().equals(vLevel)){
										//客服在小组内，取上一级的orgCode
										String parentOrgCode = comOrganizationDaoImpl.get(Long.valueOf(comOrganization.getParentId())).getOrgCode();
										loginCheckVo.setOrgCode(parentOrgCode);
										loginCheckVo.setUserType("2");//1.客户经理 2.客服 3.未知 4.业务主任
									}else if(ComOrganizationEnum.Level.V104.getName().equals(vLevel)){
										//客服在营业部下，取orgCode
										loginCheckVo.setOrgCode(comOrganization.getOrgCode());
										loginCheckVo.setUserType("2");//1.客户经理 2.客服 3.未知 4.业务主任									
									}else{
										resultMap.put("resCode", Constants.DATA_ERR_CODE);
										resultMap.put("resMsg", "该客服不在营业部或小组下，无法登陆");
										return resultMap;
									}
								}else if(comEmployee.getEmployeeType().equals(EmployeeTypeEnum.业务员.getValue())){//客户经理
									//去当前的orgCode
									loginCheckVo.setOrgCode(comOrganization.getOrgCode());
									loginCheckVo.setUserType("1");//1.客户经理 2.客服 3.未知 4.业务主任
								}else{
									loginCheckVo.setOrgCode("");
									loginCheckVo.setUserType("3");//1.客户经理 2.客服 3.未知 4.业务主任
								}								
							}
						}
						/** 区域/分部 **/
						loginCheckVo.setAreaInfo(areaInfo);
						/** 营业部 **/
						loginCheckVo.setBranchInfo(branchInfo);
						/** 所属业务组 **/
						loginCheckVo.setGroupInfo(groupInfo);
						String mobile = Strings.parseString(comEmployee.getMobile());
						if (mobile.length() >= 11) {
							mobile = mobile.substring(0, 3) + "****" + mobile.substring(7);
						} else {
							mobile = "***";
						}
						loginCheckVo.setMobile(mobile);						
						resultMap.put("loginCheckVo", loginCheckVo);
					}
				} else if ("1".equals(projectNo)) {
					/** 征信查询App登陆 **/
					resultMap.put("loginCheckVo", loginCheckVo);
					resultMap.put("resCode", Constants.SUCCESS_CODE);
					resultMap.put("resMsg", "成功");
				}

				// resultMap.put("resCode", Constants.SUCCESS_CODE);
				// resultMap.put("resMsg", "成功");
			}
			loginUserMap.remove(userCode);

		} else {
			/** 初始化失败次数 **/
			int error = 1;

			/** 查询当前用户是否是初次登录 **/
			if (null == loginUserMap.get(userCode)) {
				/** 初次登录失败 **/
				LoginErrorVo loginErrorVo = new LoginErrorVo();
				loginErrorVo.setErrorCount(error);
				loginErrorVo.setUserCode(userCode);
				loginErrorVo.setRecentLoginTime(new Date());
				loginUserMap.put(userCode, loginErrorVo);
				resultMap.put("resMsg", "用户名或密码错误");
			} else {
				LoginErrorVo loginErrorVo = loginUserMap.get(userCode);
				int oldUserError = loginErrorVo.getErrorCount();
				/** 间隔15分钟之内登录失败，失败次数累加 **/
				long curren = System.currentTimeMillis();
				long time = (curren - loginErrorVo.getRecentLoginTime().getTime());
				if (time < 5 * 60 * 1000) {
					oldUserError++;
					loginErrorVo.setErrorCount(oldUserError);
				}

				/** 最后一次失败设置开始锁定时间 **/
				if (oldUserError == 2) {
					loginErrorVo.setLockStartTime(new Date());
				}
				loginUserMap.put(userCode, loginErrorVo);
				resultMap.put("resMsg", "密码输入错误超出2次，请于5分钟后再登录！");
			}
			resultMap.put("resCode", Constants.PARAM_ERR_CODE);
			// resultMap.put("resMsg", "用户名或密码错误");
		}

		return resultMap;
	}

	private Map<String, Object> loginPermission(AppOrganizationManager appOrganizationManager,
			AppEmployeeManager appEmployeeManager,String newOrOld, ComEmployee comEmployee) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//业务主任、门店副理、门店经理角色 新系统无法登录 。  门店副理、门店经理角色老系统无法登陆
		Map<String, Object> maps = new HashMap<String, Object>();
		maps.put("usercode", comEmployee.getUsercode());
		maps.put("roleName", "信贷-营业部-业务主任");
		int num1 = comEmployeeRoleDao.findComEmployeeRoleByUserCodeAndRoleName(maps);
		maps.put("roleName", "信贷-营业部-经理");
		int num2 = comEmployeeRoleDao.findComEmployeeRoleByUserCodeAndRoleName(maps);
		maps.put("roleName", "信贷-营业部-副理");
		int num3 = comEmployeeRoleDao.findComEmployeeRoleByUserCodeAndRoleName(maps);
		if(num1>0){//业务主任角色  新系统无法登录
			if("n".equals(newOrOld)){//登陆新系统
				resultMap.put("resCode", Constants.LOGIN_PERMISSION_NOT);
				resultMap.put("resMsg", "业务主任功能尚未开通");
				return resultMap;				
			}else{//老系统可以登陆
				resultMap.put("resCode", Constants.SUCCESS_CODE);
				resultMap.put("resMsg", "成功");
				return resultMap;
			}
		}
		if(num2>0){//门店副理、门店经理角色 新老都不能登录
			resultMap.put("resCode", Constants.LOGIN_PERMISSION_NOT);
			resultMap.put("resMsg", "门店经理功能尚未开通");
			return resultMap;
		}else if(num3>0){
			resultMap.put("resCode", Constants.LOGIN_PERMISSION_NOT);
			resultMap.put("resMsg", "门店副理功能尚未开通");
			return resultMap;
		}		
		
		if (appOrganizationManager != null && AppManagerEnum.开启旧.getCode().equals(appOrganizationManager.getState())) {
			if (appEmployeeManager != null && AppManagerEnum.开启旧.getCode().equals(appEmployeeManager.getState())) {
				if("o".equals(newOrOld)){//登录的是旧系统					
					resultMap.put("resCode", Constants.SUCCESS_CODE);
					resultMap.put("resMsg", "成功");					
				}else{
					resultMap.put("resCode", Constants.LOGIN_PERMISSION_NOT);
					resultMap.put("resMsg", "无登录权限");
				}
			}else if(appEmployeeManager != null && AppManagerEnum.开启新.getCode().equals(appEmployeeManager.getState())){
				if("n".equals(newOrOld)){//登录的是新系统
					resultMap.put("resCode", Constants.SUCCESS_CODE);
					resultMap.put("resMsg", "成功");					
				}else{
					resultMap.put("resCode", Constants.LOGIN_PERMISSION_NOT);
					resultMap.put("resMsg", "无登录权限");
				}
			} else if (appEmployeeManager != null && AppManagerEnum.未开启.getCode().equals(appEmployeeManager.getState())) {
				resultMap.put("resCode", Constants.LOGIN_PERMISSION_NOT);
				resultMap.put("resMsg", "无登录权限");
			} else {//appEmployeeManager 为空
				if("o".equals(newOrOld)){
					resultMap.put("resCode", Constants.SUCCESS_CODE);
					resultMap.put("resMsg", "成功");					
				}else{
					resultMap.put("resCode", Constants.LOGIN_PERMISSION_NOT);
					resultMap.put("resMsg", "无登录权限");
				}
			}
		}else if(appOrganizationManager != null && AppManagerEnum.开启新.getCode().equals(appOrganizationManager.getState())){
			if (appEmployeeManager != null && AppManagerEnum.开启旧.getCode().equals(appEmployeeManager.getState())) {
				if("o".equals(newOrOld)){//登录的是旧系统
					resultMap.put("resCode", Constants.SUCCESS_CODE);
					resultMap.put("resMsg", "成功");					
				}else{
					resultMap.put("resCode", Constants.LOGIN_PERMISSION_NOT);
					resultMap.put("resMsg", "无登录权限");
				}
			}else if(appEmployeeManager != null && AppManagerEnum.开启新.getCode().equals(appEmployeeManager.getState())){
				if("n".equals(newOrOld)){//登录的是新系统
					resultMap.put("resCode", Constants.SUCCESS_CODE);
					resultMap.put("resMsg", "成功");					
				}else{
					resultMap.put("resCode", Constants.LOGIN_PERMISSION_NOT);
					resultMap.put("resMsg", "无登录权限");
				}
			} else if (appEmployeeManager != null && AppManagerEnum.未开启.getCode().equals(appEmployeeManager.getState())) {
				resultMap.put("resCode", Constants.LOGIN_PERMISSION_NOT);
				resultMap.put("resMsg", "无登录权限");
			} else {//appEmployeeManager 为空
				if("n".equals(newOrOld)){
					resultMap.put("resCode", Constants.SUCCESS_CODE);
					resultMap.put("resMsg", "成功");					
				}else{
					resultMap.put("resCode", Constants.LOGIN_PERMISSION_NOT);
					resultMap.put("resMsg", "无登录权限");
				}
			}
		} else {
			if (appEmployeeManager != null && AppManagerEnum.开启旧.getCode().equals(appEmployeeManager.getState())) {
				if("o".equals(newOrOld)){//登录的是旧系统
					resultMap.put("resCode", Constants.SUCCESS_CODE);
					resultMap.put("resMsg", "成功");					
				}else{
					resultMap.put("resCode", Constants.LOGIN_PERMISSION_NOT);
					resultMap.put("resMsg", "无登录权限");
				}
			}else if(appEmployeeManager != null && AppManagerEnum.开启新.getCode().equals(appEmployeeManager.getState())){
				if("n".equals(newOrOld)){//登录的是新系统
					resultMap.put("resCode", Constants.SUCCESS_CODE);
					resultMap.put("resMsg", "成功");					
				}else{
					resultMap.put("resCode", Constants.LOGIN_PERMISSION_NOT);
					resultMap.put("resMsg", "无登录权限");
				}
			} else if (appEmployeeManager != null && AppManagerEnum.未开启.getCode().equals(appEmployeeManager.getState())) {
				resultMap.put("resCode", Constants.LOGIN_PERMISSION_NOT);
				resultMap.put("resMsg", "无登录权限");
			} else {
				resultMap.put("resCode", Constants.LOGIN_PERMISSION_NOT);
				resultMap.put("resMsg", "无登录权限");
			}
		}
		return resultMap;
	}
}
