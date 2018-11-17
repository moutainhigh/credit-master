package com.zdmoney.credit.app.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.zdmoney.credit.app.domain.AppEmployeeManager;
import com.zdmoney.credit.app.domain.AppOrganizationManager;
import com.zdmoney.credit.app.service.pub.IAppEmployeeManagerService;
import com.zdmoney.credit.app.service.pub.IAppOrganizationManagerService;
import com.zdmoney.credit.common.constant.ComOrganizationEnum;
import com.zdmoney.credit.common.constant.SysActionLogTypeEnum;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.vo.AttachmentResponseInfo;
import com.zdmoney.credit.common.vo.core.AppManagerVO;
import com.zdmoney.credit.common.vo.core.RepayMarkVo;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.system.domain.ComEmployee;
import com.zdmoney.credit.system.domain.ComOrganization;
import com.zdmoney.credit.system.service.pub.IComEmployeeService;
import com.zdmoney.credit.system.service.pub.IComOrganizationService;

@Controller
@RequestMapping(value = "/appManager")
public class AppEmployeeManagerController extends BaseController {

	@Autowired
	IAppEmployeeManagerService appEmployeeManagerService;

	@Autowired
	IComEmployeeService comEmployeeService;

	@Autowired
	IAppOrganizationManagerService appOrganizationManagerService;

	@Autowired
	IComOrganizationService comOrganizationServiceImpl;

	/**
	 * 查询app登录配置信息-员工
	 * 
	 * @param request
	 * @param response
	 * @param appManagerVO
	 * @param rows
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/appEmployeeManagerPage")
	@ResponseBody
	public String findAppEmployeeManagerListWithPg(HttpServletRequest request, HttpServletResponse response,
			AppManagerVO appManagerVO, int rows, int page) {
		this.createLog(request, SysActionLogTypeEnum.查询, "APP登录管理", "查询配置信息");
		Map<String, Object> params = new HashMap<String, Object>();
		if (StringUtils.isNotEmpty(appManagerVO.getName())) {
			params.put("name", appManagerVO.getName());
		}
		if (StringUtils.isNotEmpty(appManagerVO.getUserCode())) {
			params.put("userCode", appManagerVO.getUserCode());
		}
		if (appManagerVO.getOrgId() != null) {
			params.put("orgId", appManagerVO.getOrgId());
		}
		Pager pager = new Pager();
		pager.setRows(rows);
		pager.setPage(page);
		// pager.setSidx("LOAN_ID");
		// pager.setSort("ASC");
		params.put("pager", pager);
		pager = appEmployeeManagerService.findAppEmployeeManagerListWithPg(params);
		return toPGJSONWithCode(pager);
	}

	/**
	 * 保存app登录配置信息
	 * 
	 * @param request
	 * @param response
	 * @param appManagerVO
	 * @return
	 */
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/saveAppEmployeeManager")
	@ResponseBody
	public String saveAppEmployeeManager(HttpServletRequest request, HttpServletResponse response,
			AppManagerVO appManagerVO) {
		this.createLog(request, SysActionLogTypeEnum.新增, "app登录配置", "保存app登录配置信息");
		AttachmentResponseInfo<RepayMarkVo> attachmentResponseInfo = null;
		if (StringUtils.isEmpty(appManagerVO.getUserCode())) {
			attachmentResponseInfo = new AttachmentResponseInfo<RepayMarkVo>(
					ResponseEnum.EMPLOYEE_USERCODE_NOT_EXIST.getCode(),
					ResponseEnum.EMPLOYEE_USERCODE_NOT_EXIST.getDesc(), "");
			return toResponseJSON(attachmentResponseInfo);
		}
		try {
			ComEmployee employee = comEmployeeService.findByUserCode(appManagerVO.getUserCode());
			if (employee == null) {
				attachmentResponseInfo = new AttachmentResponseInfo<RepayMarkVo>(
						ResponseEnum.EMPLOYEE_USERCODE_NOT_WRONG.getCode(),
						ResponseEnum.EMPLOYEE_USERCODE_NOT_WRONG.getDesc(), "");
				return toResponseJSON(attachmentResponseInfo);
			}
			AppEmployeeManager appEmployeeManager = null;
			if ("1".equals(appManagerVO.getSaveOrUpdate())) {
				appEmployeeManager = appEmployeeManagerService.selectAppEmployeeManagerByEmployeeId(employee.getId());
				if (appEmployeeManager != null) {
					attachmentResponseInfo = new AttachmentResponseInfo<RepayMarkVo>(
							ResponseEnum.EMPLOYEE_EXIST.getCode(), ResponseEnum.EMPLOYEE_EXIST.getDesc(), "");
					return toResponseJSON(attachmentResponseInfo);
				}
			}
			User user = UserContext.getUser();
			appEmployeeManager = new AppEmployeeManager();
			appEmployeeManager.setUpdatetime(new Date());
			appEmployeeManager.setId(appManagerVO.getId());
			if (user != null) {
				appEmployeeManager.setOperator(user.getUserCode());
			}
			appEmployeeManager.setState(appManagerVO.getState());
			appEmployeeManager.setEmployeeId(employee.getId());
			appEmployeeManagerService.saveAppEmployeeManager(appEmployeeManager);
			attachmentResponseInfo = new AttachmentResponseInfo<RepayMarkVo>(ResponseEnum.SYS_SUCCESS.getCode(),
					ResponseEnum.SYS_SUCCESS.getDesc(), "");
		} catch (Exception ex) {
			/** 系统忙 **/
			logger.error(ex, ex);
			attachmentResponseInfo = new AttachmentResponseInfo<RepayMarkVo>(ResponseEnum.SYS_FAILD.getCode(),
					ResponseEnum.SYS_FAILD.getDesc(), "");
		}

		return toResponseJSON(attachmentResponseInfo);
	}

	/**
	 * 查询APP登录配置信息--营业部
	 * 
	 * @param request
	 * @param response
	 * @param appManagerVO
	 * @param rows
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/appOrganizationManagerPage")
	@ResponseBody
	public String findAppOrganizationManagerListWithPg(HttpServletRequest request, HttpServletResponse response,
			AppManagerVO appManagerVO, int rows, int page) {
		this.createLog(request, SysActionLogTypeEnum.查询, "APP登录管理", "查询配置信息");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orgId", appManagerVO.getOrgId());
		Pager pager = new Pager();
		pager.setRows(rows);
		pager.setPage(page);
		// pager.setSidx("LOAN_ID");
		// pager.setSort("ASC");
		params.put("pager", pager);
		pager = appOrganizationManagerService.findAppOrganizationManagerListWithPg(params);
		return toPGJSONWithCode(pager);
	}

	/**
	 * 保存app登录配置信息--营业部
	 * 
	 * @param request
	 * @param response
	 * @param appManagerVO
	 * @return
	 */
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/saveAppOrganizationManager")
	@ResponseBody
	public String saveOrUpdateAppOrganizationManager(HttpServletRequest request, HttpServletResponse response,
			AppManagerVO appManagerVO) {
		this.createLog(request, SysActionLogTypeEnum.新增, "app登录配置", "保存app登录配置信息");
		AttachmentResponseInfo<RepayMarkVo> attachmentResponseInfo = null;
		if (appManagerVO.getOrgId() == null) {
			attachmentResponseInfo = new AttachmentResponseInfo<RepayMarkVo>(
					ResponseEnum.ORGANIZATION_NOT_WRONG.getCode(), ResponseEnum.ORGANIZATION_NOT_WRONG.getDesc(), "");
			return toResponseJSON(attachmentResponseInfo);
		}
		try {
			User user = UserContext.getUser();
			AppOrganizationManager appOrganizationManager = null;

			if ("1".equals(appManagerVO.getSaveOrUpdate())) {
				appOrganizationManager = appOrganizationManagerService.selectAppOrganizationManagerByOrgId(appManagerVO
						.getOrgId());
				if (appOrganizationManager != null) {
					attachmentResponseInfo = new AttachmentResponseInfo<RepayMarkVo>(
							ResponseEnum.ORGANIZATION_EXIST.getCode(), ResponseEnum.ORGANIZATION_EXIST.getDesc(), "");
					return toResponseJSON(attachmentResponseInfo);
				}
			}
			appOrganizationManager = new AppOrganizationManager();
			appOrganizationManager.setUpdatetime(new Date());
			appOrganizationManager.setId(appManagerVO.getId());
			if (user != null) {
				appOrganizationManager.setOperator(user.getUserCode());
			}
			appOrganizationManager.setState(appManagerVO.getState());
			appOrganizationManager.setOrgId(appManagerVO.getOrgId());
			appOrganizationManagerService.saveOrUpdateAppOrganizationManager(appOrganizationManager);
			attachmentResponseInfo = new AttachmentResponseInfo<RepayMarkVo>(ResponseEnum.SYS_SUCCESS.getCode(),
					ResponseEnum.SYS_SUCCESS.getDesc(), "");
		} catch (Exception ex) {
			/** 系统忙 **/
			logger.error(ex, ex);
			attachmentResponseInfo = new AttachmentResponseInfo<RepayMarkVo>(ResponseEnum.SYS_FAILD.getCode(),
					ResponseEnum.SYS_FAILD.getDesc(), "");
		}

		return toResponseJSON(attachmentResponseInfo);
	}

	@RequestMapping("/jumpPage")
	public ModelAndView jumpPage(HttpServletRequest request, HttpServletResponse response) {
		ComOrganization comOrganization = new ComOrganization();
		comOrganization.setvLevel(ComOrganizationEnum.Level.V104.name());
		List<ComOrganization> orgList = comOrganizationServiceImpl.findListByVo(comOrganization);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("app/appManager");
		modelAndView.addObject("salesDeptInfoList", orgList);
		return modelAndView;
	}
}
