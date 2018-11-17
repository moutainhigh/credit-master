package com.zdmoney.credit.system.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zdmoney.credit.common.constant.SysActionLogTypeEnum;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.vo.AttachmentResponseInfo;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.system.domain.ComEmployeeRole;
import com.zdmoney.credit.system.service.pub.IComEmployeeRoleService;
import com.zdmoney.credit.system.service.pub.IComRoleService;

/**
 * 前端请求处理（员工模块）
 * 
 * @author Ivan
 *
 */
@Controller
@RequestMapping("/system/ComEmployeeRole")
public class ComEmployeeRoleController extends BaseController {

	protected static Log logger = LogFactory
			.getLog(ComEmployeeController.class);

	
	@Autowired
	@Qualifier("comEmployeeRoleServiceImpl")
	IComEmployeeRoleService comEmployeeRoleServiceImpl;

	@Autowired
	@Qualifier("comRoleServiceImpl")
	IComRoleService comRoleServiceImpl;
	

	/**
	 * 根据用户id查询权限信息
	 * 
	 * @param ComEmployee
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "findEmpRoleByEId")
	@ResponseBody
	public String findEmpRoleByEId(ComEmployeeRole comEmployeeRole,
			HttpServletRequest request, HttpServletResponse response) {
		AttachmentResponseInfo<String> attachmentResponseInfo = null;
		try {
			comEmployeeRole.setEmployeeId(Long.parseLong(request.getParameter("empId").toString()));
			String json=comEmployeeRoleServiceImpl.findComEmployeeRole(comEmployeeRole);
			this.createLog(request, SysActionLogTypeEnum.查询, "查询", "查询");
			attachmentResponseInfo = new AttachmentResponseInfo<String>(
					ResponseEnum.SYS_SUCCESS.getCode(),
					ResponseEnum.SYS_SUCCESS.getDesc(), "");
			attachmentResponseInfo.setResMsg("成功");
			attachmentResponseInfo.setAttachment(json);

		} catch (Exception ex) {
			// 系统忙
			logger.error(ex,ex);
			attachmentResponseInfo = new AttachmentResponseInfo<String>(
					ResponseEnum.SYS_FAILD.getCode(),
					ResponseEnum.SYS_FAILD.getDesc(), "");
		}
		return toResponseJSON(attachmentResponseInfo);
	}

	/**
	 * 新增权限
	 * 
	 * @param ComEmployee
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "insertComEmployeeRole")
	@ResponseBody
	public String insertComEmployeeRole(
			ComEmployeeRole comEmployeeRole,
			HttpServletRequest request, HttpServletResponse response) {
		AttachmentResponseInfo<Object> attachmentResponseInfo = null;
		try {
			String pid=request.getParameter("pid").toString();
			comEmployeeRole.setEmployeeId(Long.parseLong(request.getParameter("empId").toString()));
			//先删除当前用户拥有的。
			comEmployeeRoleServiceImpl.deleteComEmployeeRole(comEmployeeRole,pid);
			this.createLog(request, SysActionLogTypeEnum.新增, "权限菜单", "新增用户角色");
			// 正常返回
			attachmentResponseInfo = new AttachmentResponseInfo<Object>(
					ResponseEnum.SYS_SUCCESS.getCode(),
					ResponseEnum.SYS_SUCCESS.getDesc(), "");
			attachmentResponseInfo.setResMsg("正常");

		} catch (Exception ex) {
			// 系统忙
			logger.error(ex,ex);
			attachmentResponseInfo = new AttachmentResponseInfo<Object>(
					ResponseEnum.SYS_FAILD.getCode(),
					ResponseEnum.SYS_FAILD.getDesc(), "");
		}
		return toResponseJSON(attachmentResponseInfo);
	}


}