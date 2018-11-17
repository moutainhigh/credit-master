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
@RequestMapping("/system/logViewTest")
public class LogViewTestController extends  BaseController {
	
	@RequestMapping("/logView")
	public String changePassWD(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("系统日志");
		return "/logView";
		
	}
	

	@RequestMapping("/dellog")
	@ResponseBody
	public String dellog(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("删除日志");
		return toResponseJSON(new AttachmentResponseInfo<Object>(ResponseEnum.SYS_SUCCESS.getCode(),ResponseEnum.SYS_SUCCESS.getDesc(),""));
		
	}
	
	@RequestMapping("/viewlog")
	@ResponseBody
	public String viewlog(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("查看日志");
		return toResponseJSON(new AttachmentResponseInfo<Object>(ResponseEnum.SYS_SUCCESS.getCode(),ResponseEnum.SYS_SUCCESS.getDesc(),""));
		
	}
	
	
}
