package com.zdmoney.credit.login.core;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.alibaba.fastjson.JSONObject;
import com.zdmoney.credit.common.constant.SysActionLogTypeEnum;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.vo.ResponseInfo;
import com.zdmoney.credit.framework.controller.BaseController;

/**
 * 单点认证成功回调
 * @author Ivan
 *
 */
public class LoginAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	
	protected static Log logger = LogFactory.getLog(LoginAuthenticationSuccessHandler.class);
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        /** 记录登陆留痕 **/
        BaseController.createLog(request, SysActionLogTypeEnum.登陆, "员工登陆", "员工自主登陆");
        if (BaseController.isAjaxRequest(request)) {
        	response.setContentType("application/json");
        	ResponseInfo responseInfo = new ResponseInfo(
					ResponseEnum.SYS_SUCCESS);
            response.getWriter().print(JSONObject.toJSONString(responseInfo));
            return;
        } else {
        	super.onAuthenticationSuccess(request, response, authentication);
        }
    }
}
