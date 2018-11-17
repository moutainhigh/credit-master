package com.zdmoney.credit.system.controller;

import java.util.List;

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
import com.zdmoney.credit.system.domain.ComEmployeePermission;
import com.zdmoney.credit.system.service.pub.IComEmployeePermissionService;

/**
 * 前端请求处理（员工模块）
 * 
 * @author Ivan
 *
 */
@Controller
@RequestMapping("/system/ComEmployeePermission")
public class ComEmployeePermissionController extends BaseController {

	protected static Log logger = LogFactory
			.getLog(ComEmployeeController.class);

	@Autowired
	@Qualifier("comEmployeePermissionServiceImpl")
	IComEmployeePermissionService comEmployeePermissionServiceImpl;

	// private ComEmployeePermission comEmployeePermissionDom;

	

	/**
	 * 根据用户id查询权限信息
	 * 
	 * @param ComEmployee
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "findEmpPerByEId")
	@ResponseBody
	public String findEmpPerByEId(ComEmployeePermission comEmployeePermission,
			HttpServletRequest request, HttpServletResponse response) {
		AttachmentResponseInfo<String> attachmentResponseInfo = null;
		try {
			comEmployeePermission.setEmpId(Long.parseLong(request.getParameter("empId").toString()));
			List list=comEmployeePermissionServiceImpl.findComPermissionIdByEmployeeId(comEmployeePermission);
			String json="";
			for (int i = 0; i < list.size(); i++) {
				comEmployeePermission=(ComEmployeePermission)list.get(i);
				if(i==list.size()-1)
				{
					json+=comEmployeePermission.getPermId();
				}else{
				json+=comEmployeePermission.getPermId()+",";
				}
				//此处缺少判断。根据角色来判断选中置灰菜单
			}
			this.createLog(request, SysActionLogTypeEnum.查询, "权限菜单", "根据用户id查询权限");
			// 正常返回
			attachmentResponseInfo = new AttachmentResponseInfo<String>(
					ResponseEnum.SYS_SUCCESS.getCode(),
					ResponseEnum.SYS_SUCCESS.getDesc(), "");
			attachmentResponseInfo.setResMsg("正常");
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
	@RequestMapping(value = "insertComEmployeePermission")
	@ResponseBody
	public String insertComEmployeePermission(
			ComEmployeePermission comEmployeePermission,
			HttpServletRequest request, HttpServletResponse response) {
		AttachmentResponseInfo<Object> attachmentResponseInfo = null;
		try {
			String pid=request.getParameter("pid").toString();
			comEmployeePermission.setEmpId(Long.parseLong(request.getParameter("empId").toString()));
			//先删除当前用户拥有的。
			comEmployeePermissionServiceImpl.deleteComEmployeePermission(comEmployeePermission, pid);
			this.createLog(request, SysActionLogTypeEnum.新增, "权限菜单", "新增权限");
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

	
				
					/**
					 * 根据用户id查询已经拥有的角色，在根据角色查询有的菜单
					 * 
					 * 
					 */
				@RequestMapping(value = "findEmpPerByEIdAndRole")
				@ResponseBody
				public String findEmpPerByEIdAndRole(ComEmployeePermission comEmployeePermission,
					HttpServletRequest request, HttpServletResponse response) {
				AttachmentResponseInfo<String> attachmentResponseInfo = null;
				try {
					comEmployeePermission.setEmpId(Long.parseLong(request.getParameter("empId").toString()));
				String json=	comEmployeePermissionServiceImpl.findPerIdByEmpId(comEmployeePermission);
					this.createLog(request, SysActionLogTypeEnum.查询, "权限菜单", "根据用户id查询权限");
					// 正常返回
					attachmentResponseInfo = new AttachmentResponseInfo<String>(
							ResponseEnum.SYS_SUCCESS.getCode(),
							ResponseEnum.SYS_SUCCESS.getDesc(), "");
					attachmentResponseInfo.setResMsg("正常");
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


}