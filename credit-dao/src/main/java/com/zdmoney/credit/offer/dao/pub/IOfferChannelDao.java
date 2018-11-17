package com.zdmoney.credit.offer.dao.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.offer.domain.OfferChannel;

public interface IOfferChannelDao extends IBaseDao<OfferChannel>{
	public OfferChannel findOfferChannelByPaySysNoAndLoanBelong(Map<String,Object> paramMap);
	
	public OfferChannel findOfferChannelById(Long id);
	
	public OfferChannel findOfferChannelListBycontractNum(Map<String,Object> paramMap);
	
	public List<OfferChannel> findOfferChannelList(Map<String,Object> paramMap);
	
	
}
