package com.zdmoney.credit.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.validation.ObjectError;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * 验证错误信息转换
 * @author 00236633
 *
 */
public class ValidatorErrorUtil {
	
	public static final String ERROR_CODE = "errorCode";
	public static final String ERROR_MESSAGE = "errorMessage";
	public static final String ERROR_OBJECT_NAME = "objectName";
	
	/**
	 * 将验证错误信息转换成List
	 * @param fieldErrors
	 * @return
	 */
	public static List<Map<String,String>> getErrorInfoMap(List<ObjectError>  errors){
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();  
		Locale locale = RequestContextUtils.getLocaleResolver(request).resolveLocale(request);  
		ApplicationContext applicationContext=SpringContextUtil.getApplicationContext();  
		for(ObjectError error : errors ){
			String errorMessage = error.getDefaultMessage();
			String errorCode = error.getCode();
			String objectName = error.getObjectName();
			
			for(String code: error.getCodes() ){
				String errorMessageTemp = applicationContext.getMessage(code,null, null, locale); 
				if(errorMessageTemp!=null){
					errorMessage = errorMessageTemp;
					errorCode = code;
					break;
				}
			}
			Map<String,String> map = new HashMap<String,String>();
			map.put(ERROR_CODE, errorCode);
			map.put(ERROR_MESSAGE, errorMessage);
			map.put(ERROR_OBJECT_NAME, objectName);
			list.add(map);
		}
		return list;
	}
	
	/**
	 * 将验证错误信息转换成字符串
	 * @param fieldErrors
	 * @return
	 */
	public static String getErrorInfoString(List<ObjectError>  errors){
		List<Map<String,String>> list = getErrorInfoMap(errors);
		
		StringBuilder sb = new StringBuilder();
		for(Map<String,String> map : list){
			sb.append(map.get(ERROR_MESSAGE)).append(",");
		}
		if(sb.length()>0){
			return sb.substring(0, sb.length()-1);
		}
		return "";
	}
	
}
