package com.zdmoney.credit.debit.dao;


import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import com.zdmoney.credit.debit.dao.pub.IDebitOfflineOfferInfoDao;
import com.zdmoney.credit.debit.domain.DebitOfflineOfferInfo;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;

@Repository
public class DebitofflineOfferInfoDaoImpl extends BaseDaoImpl<DebitOfflineOfferInfo> implements	IDebitOfflineOfferInfoDao {

	@Override
	public List<DebitOfflineOfferInfo> findDistinctBatNoList(Map<String,Object> map){
		List<DebitOfflineOfferInfo> list = getSqlSession().selectList(getIbatisMapperNameSpace() + ".findDistinctBatNoList",map);
		return list;
	}

	@Override
	public int updateById(Long id, String state) {
		DebitOfflineOfferInfo debitOfflineOfferInfo = new DebitOfflineOfferInfo();
		debitOfflineOfferInfo.setId(id);
		debitOfflineOfferInfo.setState(state);
		return super.update(debitOfflineOfferInfo);
	}
}
