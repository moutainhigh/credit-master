package com.zdmoney.credit.debit.service.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.debit.domain.DebitOfflineOfferInfo;
/**
 * 线下实收记录
 * @author YM10112
 *
 */

public interface IDebitOfflineOfferInfoService {
	public List<DebitOfflineOfferInfo> findDistinctBatNoList(Map<String,Object> map);

	public DebitOfflineOfferInfo createDebitOfflineOfferInfo(DebitOfflineOfferInfo debitOfflineOfferInfo);
	
	public List<DebitOfflineOfferInfo> findListByMap(Map<String,Object> map);

	public void pushOfflineDebit4WM3();
}
