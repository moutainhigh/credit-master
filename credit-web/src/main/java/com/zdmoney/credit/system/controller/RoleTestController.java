package com.zdmoney.credit.system.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.vo.AttachmentResponseInfo;
import com.zdmoney.credit.framework.controller.BaseController;

@Controller
@RequestMapping("/system/roleTest")
public class RoleTestController  extends BaseController {
	@RequestMapping("/rolelist")
	public String roleList(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("角色列表");
		return "/rolelist";
		
	}
	@RequestMapping("/update")
	@ResponseBody
	public String updateRole(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("角色修改");
		return toResponseJSON(new AttachmentResponseInfo<Object>(ResponseEnum.SYS_SUCCESS.getCode(),ResponseEnum.SYS_SUCCESS.getDesc(),""));
		
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	public String deleteRole(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("角色删除");
		return toResponseJSON(new AttachmentResponseInfo<Object>(ResponseEnum.SYS_SUCCESS.getCode(),ResponseEnum.SYS_SUCCESS.getDesc(),""));
		
	}
	
	@RequestMapping("/insert")
	@ResponseBody
	public String insertRole(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("角色新增");
		return toResponseJSON(new AttachmentResponseInfo<Object>(ResponseEnum.SYS_SUCCESS.getCode(),ResponseEnum.SYS_SUCCESS.getDesc(),""));
		
	}
}
