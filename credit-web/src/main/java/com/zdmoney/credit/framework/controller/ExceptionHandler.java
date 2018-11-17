package com.zdmoney.credit.framework.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.vo.ResponseInfo;

/**
 * Spring MVC 针对服务端异常进行封闭（Ajax返回JSON格式）
 * 
 * @author Ivan
 *
 */
@Service
public class ExceptionHandler implements HandlerExceptionResolver {

	protected static Log logger = LogFactory.getLog(ExceptionHandler.class);

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		if (ex instanceof AccessDeniedException) {

		} else {
			if (BaseController.isAjaxRequest(request)) {
				ResponseInfo responseInfo = null;
				if (ex instanceof PlatformException) {
					((PlatformException) ex).printStackTraceExt(logger);
					responseInfo = ((PlatformException) ex).toResponseInfo();
				} else {
					logger.error(ex, ex);
				}
				if (responseInfo == null) {
					responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
				}
				try {
					response.getWriter().print(BaseController.toResponseJSON(responseInfo));
					response.getWriter().flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				if (ex instanceof PlatformException) {
					((PlatformException) ex).printStackTraceExt(logger);
					String errMsg = ((PlatformException) ex).getMessage();
					ModelAndView modelAndView = new ModelAndView();
					modelAndView.addObject("errMsg", errMsg);
					modelAndView.setViewName("error/error");
					return modelAndView;
				}
			}
		}
		return null;
	}

}
