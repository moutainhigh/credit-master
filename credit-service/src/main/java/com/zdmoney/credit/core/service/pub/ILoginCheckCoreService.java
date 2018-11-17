package com.zdmoney.credit.core.service.pub;

import java.util.Map;

import com.zdmoney.credit.common.vo.core.LoginCheckVo;

public interface ILoginCheckCoreService {

	/**
	 * 登录校验接口
	 * 
	 * @param params 登陆业务参数
	 * @param projectNo App来源 1.征信查询App  2.录单App
	 * @return
	 */
	Map<String, Object> loginCkeck(LoginCheckVo params, String projectNo);

}
