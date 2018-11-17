package com.zdmoney.credit.core.service.pub;

import java.util.Map;

import com.zdmoney.credit.common.vo.core.CreditVo;

public interface IOfferCoreService {

	/**
	 * 征信信息查询
	 * @param params
	 * @return
	 */
	public Map<String, Object> queryCredit(CreditVo params) throws Exception;
}
