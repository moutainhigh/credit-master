package com.zdmoney.credit.core.filter;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * 下载文件 cookie处理（由底层Servlet拦截）
 * @author 00236633
 *
 */
public class DownloadFileFilter implements Filter {
	
    private static final Logger logger = Logger.getLogger(DownloadFileFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,FilterChain chain) throws IOException, ServletException {
		if(HttpServletRequest.class.isAssignableFrom(request.getClass()) && HttpServletResponse.class.isAssignableFrom(response.getClass()) ){
			HttpServletRequest newRequest = (HttpServletRequest)request;
			HttpServletResponse newResponse = (HttpServletResponse)response;
			try {
				String requestUrl = newRequest.getRequestURI().replace(newRequest.getContextPath(), "");
				if(!"/frameWork/downloadBigFile".equalsIgnoreCase(requestUrl)){
					Cookie[] cookies = newRequest.getCookies();
					if(cookies!=null){
						Cookie htmlDownloadFileToken = null;
						for(Cookie cookie : cookies){
							if("htmlDownloadFileToken".equals(cookie.getName())){
								htmlDownloadFileToken = cookie;
								break;
							}
						}
						if(htmlDownloadFileToken != null){
							Cookie cookie = new Cookie(htmlDownloadFileToken.getValue(),(new Date().getTime())+"");
							cookie.setPath(newRequest.getContextPath());
							newResponse.addCookie(cookie);
							htmlDownloadFileToken.setMaxAge(0);
							htmlDownloadFileToken.setPath(newRequest.getContextPath());
							newResponse.addCookie(htmlDownloadFileToken);
							newRequest.setAttribute("htmlDownloadFileToken", htmlDownloadFileToken.getValue());
							if((newRequest.getParameter("isBigFile")+"").equalsIgnoreCase("true")){
								newRequest.getSession().setAttribute(htmlDownloadFileToken.getValue(), false);
								//downloadBigFile
							}else{
								newResponse.setBufferSize(10485760);
							}
						}
					}
				}
			} catch (Exception e) {
				logger.error("下载文件cookie处理出错", e);
			}	
			chain.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {
		
	}
}
