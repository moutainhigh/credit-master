package com.zdmoney.credit.offer.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.offer.dao.pub.IOfferChannelDao;
import com.zdmoney.credit.offer.domain.OfferChannel;

@Repository
public class OfferChannelDaoImpl extends BaseDaoImpl<OfferChannel> implements
		IOfferChannelDao {

	/**
	 * 通过划扣通道和债权去向查询划扣配置信息
	 */
	public OfferChannel findOfferChannelByPaySysNoAndLoanBelong(Map<String,Object> paramMap){
		return getSqlSession().selectOne(getIbatisMapperNameSpace()+".findOfferChannelByPaySysNoAndLoanBelong", paramMap);
	}

	@Override
	public OfferChannel findOfferChannelById(Long id) {
		return getSqlSession().selectOne(getIbatisMapperNameSpace()+".selectByPrimaryKey", id);
	}
	/**
	 * 通过合同编号查询划扣通道配置信息
	 */
	@Override
	public OfferChannel findOfferChannelListBycontractNum(
			Map<String,Object> paramMap) {
		return getSqlSession().selectOne(getIbatisMapperNameSpace()+".findOfferChannelListBycontractNum", paramMap);
	}

	@Override
	public List<OfferChannel> findOfferChannelList(Map<String, Object> paramMap) {
		return getSqlSession().selectList(getIbatisMapperNameSpace()+".findOfferChannelList", paramMap);
	}
	
}
