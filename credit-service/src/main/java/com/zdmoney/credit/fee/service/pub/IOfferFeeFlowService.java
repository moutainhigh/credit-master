package com.zdmoney.credit.fee.service.pub;

import com.zdmoney.credit.fee.domain.OfferFeeFlow;

public interface IOfferFeeFlowService {
	
	/**
	 * 保存服务费分账信息
	 */
	public OfferFeeFlow saveOfferFeeFlow(OfferFeeFlow offerFeeFlow);
}
