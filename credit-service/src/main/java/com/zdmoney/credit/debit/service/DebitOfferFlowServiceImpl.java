package com.zdmoney.credit.debit.service;

import java.math.BigDecimal;
import java.util.Map;

import com.zdmoney.credit.common.constant.wm.WMDebitDeductStateEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.debit.dao.pub.IDebitOfferFlowDao;
import com.zdmoney.credit.debit.domain.DebitOfferFlow;
import com.zdmoney.credit.debit.service.pub.IDebitOfferFlowService;
import com.zdmoney.credit.system.service.pub.ISequencesService;

/**
 * @author 10098  2017年3月30日 下午4:04:44
 */
@Service
public class DebitOfferFlowServiceImpl implements IDebitOfferFlowService {

	@Autowired
	IDebitOfferFlowDao debitOfferFlowDao;
	@Autowired
	ISequencesService sequencesService;

	@Override
	public DebitOfferFlow createDebitOfferFlow(Map<String, Object> map, String batNo, String subjType,BigDecimal amount, String tradeDate, String accNo, String currentTerm, boolean isAdvanceRepay) {
		DebitOfferFlow debitOfferFlow = new DebitOfferFlow();
		debitOfferFlow.setId(sequencesService.getSequences(SequencesEnum.DEBIT_OFFER_FLOW));
		debitOfferFlow.setAcNo(accNo);
		debitOfferFlow.setBatNo(batNo);
		debitOfferFlow.setCardChn("");//CL0001中金支付 CL0002广银联  待确定
		debitOfferFlow.setCnt(Strings.isEmpty(currentTerm)?0:Integer.valueOf(currentTerm));
		debitOfferFlow.setLoanId(Long.parseLong(map.get("loanId").toString()));
		debitOfferFlow.setPactNo(map.get("contractNum").toString());
		debitOfferFlow.setRepayAmt(new BigDecimal(map.get("repayAmt").toString()));
		if(isAdvanceRepay){
			debitOfferFlow.setRepyType("02"); //01-正常扣款 02-提前清贷 03-溢缴款充值
		}else{
			debitOfferFlow.setRepyType("01"); //01-正常扣款 02-提前清贷 03-溢缴款充值
		}
		debitOfferFlow.setSerialNo(debitOfferFlow.getId().toString());
		debitOfferFlow.setState(WMDebitDeductStateEnum.未发送.getValue()); //1-待发送
		debitOfferFlow.setSubjAmt(amount);
		debitOfferFlow.setSubjType(subjType);
		debitOfferFlow.setTradeDate(Dates.getDateByString(tradeDate, Dates.DEFAULT_DATE_FORMAT));
		return debitOfferFlowDao.insert(debitOfferFlow);
	}
}
