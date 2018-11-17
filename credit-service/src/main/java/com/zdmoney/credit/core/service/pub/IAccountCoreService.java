package com.zdmoney.credit.core.service.pub;

import java.util.Map;

import com.zdmoney.credit.common.vo.core.AccountVo;

public interface IAccountCoreService {

	/**
	 * 绑卡验证接口 (征审系统发送将卡号信息发送到财务核心系统进项验证，返回账号信息)
	 * @param params
	 * @return
	 */
	Map<String, Object> createYYAcount(AccountVo params);

}
