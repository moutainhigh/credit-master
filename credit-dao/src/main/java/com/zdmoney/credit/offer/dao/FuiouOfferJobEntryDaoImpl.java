package com.zdmoney.credit.offer.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.offer.dao.pub.IFuiouOfferJobEntryDao;
import com.zdmoney.credit.offer.domain.FuiouOfferJobEntry;

@Repository
public class FuiouOfferJobEntryDaoImpl extends BaseDaoImpl<FuiouOfferJobEntry> implements IFuiouOfferJobEntryDao{

	@Override
	public List<FuiouOfferJobEntry> getFuiouOffer22(String dateStr) {
		Map<String,Object> map =new HashMap<String,Object>();
		map.put("dateStr", dateStr);
		
		List<FuiouOfferJobEntry> list = getSqlSession().selectList(getIbatisMapperNameSpace() + ".getFuiouOffer22", map);
		return list;
	}

	@Override
	public List<FuiouOfferJobEntry> getFuiouOffer(Map<String, Object> params) {
		List<FuiouOfferJobEntry> list = getSqlSession().selectList(getIbatisMapperNameSpace() + ".getFuiouOffer", params);
		return list;
	}

}
