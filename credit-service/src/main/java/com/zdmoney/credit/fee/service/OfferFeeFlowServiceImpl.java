package com.zdmoney.credit.fee.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.fee.dao.pub.IOfferFeeFlowDao;
import com.zdmoney.credit.fee.domain.OfferFeeFlow;
import com.zdmoney.credit.fee.service.pub.IOfferFeeFlowService;
import com.zdmoney.credit.system.service.pub.ISequencesService;
@Service
public class OfferFeeFlowServiceImpl implements IOfferFeeFlowService {

	@Autowired
	IOfferFeeFlowDao offerFeeFlowDao;
	@Autowired
	ISequencesService sequencesServiceImpl;
	
	@Override
	public OfferFeeFlow saveOfferFeeFlow(OfferFeeFlow offerFeeFlow) {
		if(Strings.isEmpty(offerFeeFlow.getId())){
			offerFeeFlow.setId(sequencesServiceImpl.getSequences(SequencesEnum.OFFER_FEE_FLOW));
		}
		return offerFeeFlowDao.insert(offerFeeFlow);
	}

}
