package com.zdmoney.credit.login.core;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

/**
 * 单点认证失败回调
 * @author Ivan
 *
 */
public class LoginAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler{
	
	protected static Log logger = LogFactory.getLog(LoginAuthenticationFailureHandler.class);
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
		super.onAuthenticationFailure(request, response, exception);
		logger.error("authenticationFailure:" + exception,exception);
	}
}
