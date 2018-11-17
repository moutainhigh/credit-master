package com.zdmoney.credit.login.core;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.cas.client.util.CommonUtils;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.session.InvalidSessionStrategy;

import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.vo.ResponseInfo;
import com.zdmoney.credit.framework.controller.BaseController;

/**
 * Spring Security 会话超时处理逻辑
 * @author Ivan
 *
 */
public class SessionTimeOutHandle implements InvalidSessionStrategy {
	
	protected static Log logger = LogFactory.getLog(SessionTimeOutHandle.class);
	
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	private boolean createNewSession = true;

	@Override
	public void onInvalidSessionDetected(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		if (BaseController.isAjaxRequest(request)) {
			/** 超时后 返回JSON到前端 **/
			ResponseInfo responseInfo = new ResponseInfo(ResponseEnum.SYS_SessionOutActionCode);
			try {
				response.setContentType("application/json");
				response.getWriter().print(BaseController.toResponseJSON(responseInfo));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			/** 超时后直接跳转到原地址 **/
			String directUrl = CommonUtils.constructServiceUrl(request, response, "", request.getServerName(), "", false);
			logger.debug("Starting new session (if required) and redirecting to '" + directUrl + "'");
	        if (createNewSession) {
	            request.getSession();
	        }
			redirectStrategy.sendRedirect(request, response, directUrl);
		}
	}

	public void setCreateNewSession(boolean createNewSession) {
		this.createNewSession = createNewSession;
	}

}
