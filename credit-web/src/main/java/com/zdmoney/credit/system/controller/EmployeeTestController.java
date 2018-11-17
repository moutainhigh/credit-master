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
@RequestMapping("/system/userTest")
public class EmployeeTestController extends BaseController {
	
	@RequestMapping("/employeeTestList")
	public String employeeList(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("员工列表");
		return "/employeeTestList";
		
	}
	
	@RequestMapping("/list")
	@ResponseBody	
	public String listEmployee(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("员工列表查询");
		return toResponseJSON(new AttachmentResponseInfo<Object>(ResponseEnum.SYS_SUCCESS.getCode(),ResponseEnum.SYS_SUCCESS.getDesc(),""));
		
	}
	
	@RequestMapping("/update")
	@ResponseBody	
	public String updateEmployee(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("员工列表修改");
		return toResponseJSON(new AttachmentResponseInfo<Object>(ResponseEnum.SYS_SUCCESS.getCode(),ResponseEnum.SYS_SUCCESS.getDesc(),""));
		
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	public String deleteEmployee(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("员工列表删除");
		return toResponseJSON(new AttachmentResponseInfo<Object>(ResponseEnum.SYS_SUCCESS.getCode(),ResponseEnum.SYS_SUCCESS.getDesc(),""));
		
	}
	
	@RequestMapping("/insert")
	@ResponseBody	
	public String insertEmployee(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("员工列表新增");
		return toResponseJSON(new AttachmentResponseInfo<Object>(ResponseEnum.SYS_SUCCESS.getCode(),ResponseEnum.SYS_SUCCESS.getDesc(),""));
		
	}

}
