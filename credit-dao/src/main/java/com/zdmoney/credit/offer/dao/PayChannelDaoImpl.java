package com.zdmoney.credit.offer.dao;


import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.offer.dao.pub.IPayChannelDao;
import com.zdmoney.credit.offer.domain.PayChannel;

@Repository
public class PayChannelDaoImpl extends BaseDaoImpl<PayChannel> implements IPayChannelDao {

	@Override
	public PayChannel findPayChannel(Map<String, Object> paramMap) {
		return getSqlSession().selectOne(getIbatisMapperNameSpace()+".findPayChannel", paramMap);
	}
	
}
