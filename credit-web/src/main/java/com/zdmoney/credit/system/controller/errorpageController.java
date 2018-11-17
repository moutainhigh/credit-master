package com.zdmoney.credit.system.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.vo.ResponseInfo;
import com.zdmoney.credit.framework.controller.BaseController;

@Controller
@RequestMapping("/errorpage")
public class errorpageController extends BaseController {
	
	@RequestMapping(value = "/permissionError")
	public String list(HttpServletRequest request, HttpServletResponse response){
		return "/error/permissionError";
		
	}
	@RequestMapping(value = "/permissionErrorCode")
	@ResponseBody
	public String listCode(HttpServletRequest request, HttpServletResponse response){
		logger.debug("没有操作权限");
		return toResponseJSON(new ResponseInfo(ResponseEnum.SYS_ErrorActionCode.getCode(),ResponseEnum.SYS_ErrorActionCode.getDesc()));
	}
}