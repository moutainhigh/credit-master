package com.zdmoney.credit.offer.service.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.offer.domain.OfferChannel;

public interface IOfferChannelService {
	public OfferChannel findOfferChannelByPaySysNoAndLoanBelong(Map<String,Object> paramMap);
	
	
	
	public void saveOrUpdateOfferChannel(OfferChannel offerChannel);
	
	
	public Pager findWithPgByMap(Map<String, Object> params);
	
	public OfferChannel findOfferChannelById(Long id);
	
	public OfferChannel findOfferChannelListBycontractNum(Map<String,Object> paramMap);
	
	public List<OfferChannel> findOfferChannelList(Map<String, Object> paramMap);
}
