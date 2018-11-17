package com.zdmoney.credit.offer.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.offer.dao.pub.IOfferChannelDao;
import com.zdmoney.credit.offer.domain.OfferChannel;
import com.zdmoney.credit.offer.service.pub.IOfferChannelService;
import com.zdmoney.credit.system.service.pub.ISequencesService;
@Service
public class OfferChannelServiceImpl implements IOfferChannelService {

	@Autowired
	IOfferChannelDao offerChannelDao;
	
	@Autowired
	ISequencesService sequencesService;
	
	/**
	 * 通过划扣通道和债权去向查询划扣配置信息
	 */
	@Override
	public OfferChannel findOfferChannelByPaySysNoAndLoanBelong(
			Map<String,Object> paramMap) {
		return offerChannelDao.findOfferChannelByPaySysNoAndLoanBelong(paramMap);
	}
	
	/**
	 * 插入或修改划扣通道配置信息
	 */
	@Override
	public void saveOrUpdateOfferChannel(OfferChannel offerChannel) {
		if(offerChannel.getId() != null){
			offerChannelDao.update(offerChannel);
		}else{
			offerChannel.setId(sequencesService.getSequences(SequencesEnum.OFFER_CHANNEL));
			offerChannelDao.insert(offerChannel);
		}
		
	}
	
	/**
	 * 分页查询划扣通道配置信息表
	 */
	@Override
	public Pager findWithPgByMap(Map<String, Object> params) {
		return offerChannelDao.findWithPgByMap(params);
	}
	/**
	 * 根据主键查询划扣通道配置信息
	 * @param id
	 * @return
	 */
	public OfferChannel findOfferChannelById(Long id){
		return offerChannelDao.findOfferChannelById(id);
	}
	
	/**
	 * 通过合同编号查询划扣通道配置信息
	 */
	@Override
	public OfferChannel findOfferChannelListBycontractNum(
			Map<String, Object> paramMap) {
		return offerChannelDao.findOfferChannelListBycontractNum(paramMap);
	}

	@Override
	public List<OfferChannel> findOfferChannelList(Map<String, Object> paramMap) {
		return offerChannelDao.findOfferChannelList(paramMap);
	}

}
