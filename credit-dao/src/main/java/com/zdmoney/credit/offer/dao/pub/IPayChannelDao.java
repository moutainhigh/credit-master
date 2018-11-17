package com.zdmoney.credit.offer.dao.pub;


import java.util.Map;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.offer.domain.OfferChannel;
import com.zdmoney.credit.offer.domain.PayChannel;

public interface IPayChannelDao extends IBaseDao<PayChannel>{
	public PayChannel findPayChannel(Map<String,Object> paramMap);
}
