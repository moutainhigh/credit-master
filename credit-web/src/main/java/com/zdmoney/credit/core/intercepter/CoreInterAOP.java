package com.zdmoney.credit.core.intercepter;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;

import com.zdmoney.credit.common.login.UserContext;

/**
 * 核心接口拦截器
 * 
 * @author Ivan
 *
 */
@Component
public class CoreInterAOP {

	/**
	 * 被调用接口 执行前后进行拦截
	 * 
	 * @param point
	 * @return
	 * @throws Throwable
	 */
	public Object onAround(ProceedingJoinPoint joinPoint) throws Throwable {
		try {
			Object resultValue = joinPoint.proceed();
			return resultValue;
		} finally {
			/** 方法结束后清空相关内容 **/
			UserContext.clear();
		}

	}

}
