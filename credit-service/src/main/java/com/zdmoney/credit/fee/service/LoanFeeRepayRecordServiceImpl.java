package com.zdmoney.credit.fee.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.fee.dao.pub.ILoanFeeRepayRecordDao;
import com.zdmoney.credit.fee.dao.pub.IOfferFeeFlowDao;
import com.zdmoney.credit.fee.domain.LoanFeeRepayInfo;
import com.zdmoney.credit.fee.domain.LoanFeeRepayRecord;
import com.zdmoney.credit.fee.domain.OfferFeeFlow;
import com.zdmoney.credit.fee.service.pub.ILoanFeeRepayRecordService;
import com.zdmoney.credit.system.service.pub.ISequencesService;

@Service
public class LoanFeeRepayRecordServiceImpl implements
		ILoanFeeRepayRecordService {
	protected static Log logger = LogFactory.getLog(LoanFeeRepayRecordServiceImpl.class);
	
	@Autowired
	ILoanFeeRepayRecordDao loanFeeRepayRecordDao;
	
	@Autowired
	IOfferFeeFlowDao offerFeeFlowDao;
	
	@Autowired
	ISequencesService sequencesServiceImpl;
	
	/**
	 * 
	 */
	@Override
	public LoanFeeRepayRecord saveLoanFeeRepayRecord(
			LoanFeeRepayRecord loanFeeRepayRecord) {
		if(Strings.isEmpty(loanFeeRepayRecord.getId())){
			loanFeeRepayRecord.setId(sequencesServiceImpl.getSequences(SequencesEnum.LOAN_FEE_REPAY_RECORD));
		}
		return loanFeeRepayRecordDao.insert(loanFeeRepayRecord);
	}
	
	
	/**
	 * 服务费分账 ,收取的服务费先补足风险金，然后为评估费、咨询费、乙方管理费。
	 */
	public void saveLoanFeeSplit(LoanFeeRepayInfo loanFeeRepayInfo){
		logger.info("服务费分账入参：feeId："+loanFeeRepayInfo.getFeeId()+"SerialNo:"+loanFeeRepayInfo.getSerialNo()+"amount:"+loanFeeRepayInfo.getAmount());
		String [] account_titles = {Const.ACCOUNT_TITLE_RISK_EXP,Const.ACCOUNT_TITLE_APPRAISAL_EXP,Const.ACCOUNT_TITLE_CONSULT_EXP,Const.ACCOUNT_TITLE_MANAGE_EXP};
		BigDecimal amount =  loanFeeRepayInfo.getAmount();
		for(String accountTitle : account_titles){
			System.out.println("-----------------------amount");
			if(amount.compareTo(BigDecimal.ZERO) <= 0){
				logger.info("费用不足还服务费");
				break;
			}
			Map<String,Object> paramMap = new HashMap<String, Object>();
			paramMap.put("feeId", loanFeeRepayInfo.getFeeId());
			paramMap.put("acctTitle", accountTitle);
			LoanFeeRepayRecord loanFeeRepayRecord = loanFeeRepayRecordDao.selectLoanFeeRepayRecordByFeeIdAndAcctTitle(paramMap);
			/**判断剩余未还金额是否大于零，大于零则更新服务费分账表，插入分账扣费记录 */
			if(loanFeeRepayRecord != null && loanFeeRepayRecord.getRepayamount().compareTo(BigDecimal.ZERO) == 1){
				OfferFeeFlow offerFeeFlow = new OfferFeeFlow();
				//够还全换不够换只还部分
				if(amount.compareTo(loanFeeRepayRecord.getRepayamount()) >=0){
					amount = amount.subtract(loanFeeRepayRecord.getRepayamount());
					offerFeeFlow.setTradeAmount(loanFeeRepayRecord.getRepayamount());
					loanFeeRepayRecord.setRepayamount(BigDecimal.ZERO);
				}else{
					loanFeeRepayRecord.setRepayamount(loanFeeRepayRecord.getRepayamount().subtract(amount));
					offerFeeFlow.setTradeAmount(amount);
					amount = BigDecimal.ZERO;
				}
				offerFeeFlow.setAcctTitle(accountTitle);
				switch (accountTitle) {
				case Const.ACCOUNT_TITLE_RISK_EXP:
					offerFeeFlow.setAppoAcct("ZD0000001090000003");
					offerFeeFlow.setAppoAcctTitle("445");
					break;
				case Const.ACCOUNT_TITLE_APPRAISAL_EXP:
					offerFeeFlow.setAppoAcct("ZD0000001090000005");
					offerFeeFlow.setAppoAcctTitle("442");
					break;	
				case Const.ACCOUNT_TITLE_CONSULT_EXP:
					offerFeeFlow.setAppoAcct("ZD0000001090000005");
					offerFeeFlow.setAppoAcctTitle("441");
					break;	
				case Const.ACCOUNT_TITLE_MANAGE_EXP:
					offerFeeFlow.setAppoAcct("ZD0000001090000004");
					offerFeeFlow.setAppoAcctTitle("443");
					break;	
				default:
					break;
				}
				offerFeeFlow.setAppoDorc("C");
				offerFeeFlow.setCreateTime(new Date());
				offerFeeFlow.setDorc("D");
				offerFeeFlow.setCreator(loanFeeRepayInfo.getCreator());
				offerFeeFlow.setfeeId(loanFeeRepayInfo.getFeeId());
				offerFeeFlow.setLoanId(loanFeeRepayInfo.getLoanId());
				offerFeeFlow.setTradeCode("1001");
				offerFeeFlow.setTradeDate(loanFeeRepayInfo.getTradeTime());
				offerFeeFlow.setTradeKind("正常交易");
				offerFeeFlow.setSerialNo(loanFeeRepayInfo.getSerialNo());
				offerFeeFlow.setTradeType(loanFeeRepayInfo.getTradeType());
				offerFeeFlow.setId(sequencesServiceImpl.getSequences(SequencesEnum.OFFER_FEE_FLOW));
				loanFeeRepayRecordDao.update(loanFeeRepayRecord);
				offerFeeFlowDao.insert(offerFeeFlow);
			}else{
				/**执行下一个类型的服务费逻辑*/
				continue;
			}
			
		}
	}
}
