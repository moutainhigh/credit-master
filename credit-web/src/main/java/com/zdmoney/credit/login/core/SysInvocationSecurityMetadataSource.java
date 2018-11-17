package com.zdmoney.credit.login.core;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.expression.Expression;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.intercept.aopalliance.MethodSecurityInterceptor;
import org.springframework.security.access.vote.AbstractAccessDecisionManager;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.ExpressionBasedFilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.access.intercept.DefaultFilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.login.service.LoginService;
import com.zdmoney.credit.system.domain.ComPermission;
import com.zdmoney.credit.system.service.pub.IComPermissionService;

/**
 * 动态设置Spring Secuirty 权限数据
 * 
 * @author user
 *
 */
@Service
public class SysInvocationSecurityMetadataSource extends LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> {

	public static final String ROLE_PRE_FIX = "";

	protected static Log logger = LogFactory.getLog(SysInvocationSecurityMetadataSource.class);

	@Autowired
	@Qualifier("org.springframework.security.web.access.intercept.FilterSecurityInterceptor#0")
	FilterSecurityInterceptor filterSecurityInterceptor;

	@Autowired
	MethodSecurityInterceptor methodSecurityInterceptor;

	@Autowired
	@Qualifier("comPermissionServiceImpl")
	IComPermissionService comPermissionServiceImpl;

	/**
	 * 从配置文件中加载静态权限数据，添加到Spring Secuirty MetadataSource 容器中
	 */
	public void appendFileDefineMetadataSource() {
		logger.debug("！！！！！！！！！！Loadding File MetadataSource To Spring Secuirty");
		// 验证是否启用表达式
		boolean useExpressions = false;
		AffirmativeBased affirmativeBased = (AffirmativeBased) filterSecurityInterceptor.getAccessDecisionManager();
		List<AccessDecisionVoter> decisionVoters = affirmativeBased.getDecisionVoters();
		for (AccessDecisionVoter voter : decisionVoters) {
			if (voter.getClass().equals(WebExpressionVoter.class)) {
				useExpressions = true;
			}
		}
		// 获取系统中的权限拦截配置元
		DefaultFilterInvocationSecurityMetadataSource securityMetadataSource = (DefaultFilterInvocationSecurityMetadataSource) filterSecurityInterceptor
				.getSecurityMetadataSource();
		Map<RequestMatcher, Collection<ConfigAttribute>> requestMapTemp = new HashMap<RequestMatcher, Collection<ConfigAttribute>>();
		try {
			Field field = DefaultFilterInvocationSecurityMetadataSource.class.getDeclaredField("requestMap");
			field.setAccessible(true);
			Field modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
			requestMapTemp = (Map<RequestMatcher, Collection<ConfigAttribute>>) field.get(securityMetadataSource);
		} catch (NoSuchFieldException e1) {
			e1.printStackTrace();
		} catch (SecurityException e1) {
			e1.printStackTrace();
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		}

		LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap = new LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>>();
		if (useExpressions == true) {
			// 将系统中拦截配置设置到新的容器中
			for (Entry<RequestMatcher, Collection<ConfigAttribute>> entry : requestMapTemp.entrySet()) {
				Object attribute = entry.getValue().toArray(new ConfigAttribute[1])[0];
				try {
					Field field = attribute.getClass().getDeclaredField("authorizeExpression");
					field.setAccessible(true);
					Field modifiersField = Field.class.getDeclaredField("modifiers");
					modifiersField.setAccessible(true);
					modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
					Expression authorizeExpression = (Expression) field.get(attribute);
					requestMap
							.put(entry.getKey(), SecurityConfig.createList(authorizeExpression.getExpressionString()));
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			// 加载自定义权限拦截
			// setSecurityUrlConfig(requestMap);
			securityMetadataSource = new ExpressionBasedFilterInvocationSecurityMetadataSource(requestMap,
					new DefaultWebSecurityExpressionHandler());
		} else {
			// 将系统中拦截配置设置到新的容器中
			requestMap.putAll(requestMapTemp);
			// 加载自定义权限拦截
			// setSecurityUrlConfig(requestMap);
			securityMetadataSource = new DefaultFilterInvocationSecurityMetadataSource(requestMap);
		}
		this.putAll(requestMapTemp);
	}

	public String toResource(String perm) {
		return ROLE_PRE_FIX + perm;
	}

	/**
	 * 从COM_PERMISSION加载所有菜单及权限数据，添加到Spring Secuirty MetadataSource 容器中
	 */
	public void appendDataBaseDefineMetadataSource() {
		logger.debug("！！！！！！！！！！Loadding COM_PERMISSION MetadataSource To Spring Secuirty");
		ComPermission comPermissionQuery = new ComPermission();
		comPermissionQuery.setSystemFlag(LoginService.MENU_SYSTEM_FLAG);
		List<ComPermission> comPermissionList = comPermissionServiceImpl.findComPermission(comPermissionQuery);
		// List<ComPermission> comPermissionList =
		// comPermissionServiceImpl.findAll();
		for (ComPermission comPermission : comPermissionList) {
			String url = comPermission.getPermUrl();
			this.put(new AntPathRequestMatcher(url), SecurityConfig.createListFromCommaDelimitedString(toResource(url)));
		}
	}

	@PostConstruct
	public void afterPropertiesSet() throws Exception {
		/** 变更Spring Security 枚举器 将第三方RoleVoter 替换成 RoleVoterImpl **/
		List<AccessDecisionVoter> accessDecisionVoterList = ((AbstractAccessDecisionManager) methodSecurityInterceptor
				.getAccessDecisionManager()).getDecisionVoters();
		for (AccessDecisionVoter voter : accessDecisionVoterList) {
			if (voter instanceof RoleVoter) {
				accessDecisionVoterList.remove(voter);
				accessDecisionVoterList.add(new RoleVoterImpl());
				break;
			}
		}
		/** 变更Spring Security 枚举器 将第三方RoleVoter 替换成 RoleVoterImpl **/

		appendDataBaseDefineMetadataSource();
		appendFileDefineMetadataSource();
	}

}
