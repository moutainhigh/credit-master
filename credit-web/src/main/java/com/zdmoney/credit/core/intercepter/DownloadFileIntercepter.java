package com.zdmoney.credit.core.intercepter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.zdmoney.credit.common.util.UrlUtil;

/**
 * 下载文件完成处理
 * @author 00236633
 *
 */
public class DownloadFileIntercepter implements HandlerInterceptor {
	
    protected static Log logger = LogFactory.getLog(DownloadFileIntercepter.class);

	@Override
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler) throws Exception {
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,HttpServletResponse response, Object handler,ModelAndView modelAndView) throws Exception {
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request,HttpServletResponse response, Object handler, Exception ex)throws Exception {
		try {
			if((request.getParameter("isBigFile")+"").equalsIgnoreCase("true")){
				request.getSession().setAttribute(request.getAttribute("htmlDownloadFileToken")+"", true);
			}
		} catch (Exception e) {
			logger.error("afterCompletion path:"+UrlUtil.getUri(),e);
		}
	}

}
