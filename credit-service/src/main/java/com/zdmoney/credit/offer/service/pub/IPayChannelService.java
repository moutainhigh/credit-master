package com.zdmoney.credit.offer.service.pub;

import java.util.Map;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.offer.domain.PayChannel;

public interface IPayChannelService {
	public Pager findWithPgByMap(Map<String, Object> params);
	
	public PayChannel findPayChannel(Map<String, Object> params);
	
	public void saveOrUpdatePayChannel(PayChannel payChannel);
}
