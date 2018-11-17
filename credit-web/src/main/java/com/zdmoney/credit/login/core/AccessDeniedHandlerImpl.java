package com.zdmoney.credit.login.core;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.vo.ResponseInfo;
import com.zdmoney.credit.framework.controller.BaseController;

public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

	private String deniedUrl;

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		if (BaseController.isAjaxRequest(request)) {
			ResponseInfo responseInfo = new ResponseInfo(ResponseEnum.SYS_ErrorActionCode);
			response.setContentType("application/json");
			response.getWriter().print(BaseController.toResponseJSON(responseInfo));
		} else {
			String path = request.getContextPath();
			String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
					+ path + "/";
			response.sendRedirect(basePath + deniedUrl);
		}
	}

	public String getDeniedUrl() {
		return deniedUrl;
	}

	public void setDeniedUrl(String deniedUrl) {
		this.deniedUrl = deniedUrl;
	}

}
