package com.zdmoney.credit.offer.service;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.offer.dao.pub.IPayChannelDao;
import com.zdmoney.credit.offer.domain.PayChannel;
import com.zdmoney.credit.offer.service.pub.IPayChannelService;
import com.zdmoney.credit.system.service.pub.ISequencesService;

@Service
public class PayChannelServiceImpl implements IPayChannelService {
	@Autowired
	IPayChannelDao payChannelDao;
	@Autowired
	ISequencesService sequencesService;
	@Override
	public Pager findWithPgByMap(Map<String, Object> params) {
		return payChannelDao.findWithPgByMap(params);
	}


	@Override
	public PayChannel findPayChannel(Map<String, Object> params) {
		return payChannelDao.findPayChannel(params);
	}
	
	@Override
	public void saveOrUpdatePayChannel(PayChannel payChannel) {
		if(payChannel.getId() != null){
			payChannelDao.update(payChannel);
		}else{
			payChannel.setId(sequencesService.getSequences(SequencesEnum.PAY_CHANNEL));
			payChannelDao.insert(payChannel);
		}
	}

	
}
