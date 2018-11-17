package com.zdmoney.credit.trustOffer.service.pub;

import java.util.Map;

/**
 * @author YM10112 2018年1月8日 下午5:35:09
 */
public interface IBhTrustCommonService {
	/**
	 * 构建上传渤海2的实体类，上传还款状态信息
	 * @param params
	 * @return
	 */
	public Map<String, Object> createRepayStateDetail(Map<String, Object> params);
}
