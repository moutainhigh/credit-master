package com.zdmoney.credit.debit.service.pub;

import java.math.BigDecimal;
import java.util.Map;

import com.zdmoney.credit.debit.domain.DebitOfferFlow;

/**
 * @author 10098  2017年3月30日 下午4:04:00
 */
public interface IDebitOfferFlowService {

	/**
	 * 创建外贸3 分账流水
	 * @param loanId
	 * @param subjType
	 * @param amount
	 * @return
	 */
	public DebitOfferFlow createDebitOfferFlow(Map<String, Object> map, String batNo, String subjType,BigDecimal amount, String tradeDate, String accNo, String currentTerm, boolean isAdvanceRepay);

}
