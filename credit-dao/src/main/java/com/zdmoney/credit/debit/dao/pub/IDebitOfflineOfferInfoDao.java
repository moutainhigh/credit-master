package com.zdmoney.credit.debit.dao.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.debit.domain.DebitOfflineOfferInfo;
import com.zdmoney.credit.framework.dao.pub.IBaseDao;


public interface IDebitOfflineOfferInfoDao  extends IBaseDao<DebitOfflineOfferInfo>{
	public List<DebitOfflineOfferInfo> findDistinctBatNoList(Map<String,Object> map);

	public int updateById(Long id, String state);
}
