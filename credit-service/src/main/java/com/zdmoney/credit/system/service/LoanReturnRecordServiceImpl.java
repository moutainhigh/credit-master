package com.zdmoney.credit.system.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zdmoney.credit.debit.domain.DebitQueueLog;
import com.zdmoney.credit.ljs.dao.pub.ICompensatoryDetailLufaxDao;
import com.zdmoney.credit.ljs.domain.CompensatoryDetailLufax;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zdmoney.credit.common.constant.FundsSourcesTypeEnum;
import com.zdmoney.credit.common.constant.RepaymentStateEnum;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.loan.domain.LoanBase;
import com.zdmoney.credit.loan.domain.LoanLedger;
import com.zdmoney.credit.loan.domain.LoanRepaymentDetail;
import com.zdmoney.credit.loan.domain.LoanReturn;
import com.zdmoney.credit.loan.domain.LoanReturnRecord;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.ILoanBaseService;
import com.zdmoney.credit.loan.service.pub.ILoanLedgerService;
import com.zdmoney.credit.loan.service.pub.ILoanRepaymentDetailService;
import com.zdmoney.credit.system.dao.pub.ILoanReturnRecordDao;
import com.zdmoney.credit.system.service.pub.ILoanReturnRecordService;
import com.zdmoney.credit.system.service.pub.ILoanReturnService;
import com.zdmoney.credit.system.service.pub.ISequencesService;

@Service
public class LoanReturnRecordServiceImpl implements ILoanReturnRecordService{
	
	@Autowired
	private ISequencesService sequencesServiceImpl;
	@Autowired
	private ILoanReturnRecordDao loanReturnRecordDao;
	@Autowired
	private ILoanBaseService loanBaseService;
	@Autowired
	private ILoanLedgerService loanLedgerService;
	@Autowired 
	private ILoanRepaymentDetailService loanRepaymentDetailServiceImpl;
	@Autowired
	private ILoanReturnService loanReturnService;
	@Autowired
	private ICompensatoryDetailLufaxDao compensatoryDetailLufaxDao;
	
	@Override
	public List<LoanReturnRecord> findListByVo(LoanReturnRecord lrr) {
		return loanReturnRecordDao.findListByVo(lrr);
	}


	@Override
	public List<LoanReturnRecord> findListByMap(Map<String, Object> params) {
		return loanReturnRecordDao.findListByMap(params);
	}
	
	/**
     * 插入债权回购记录信息， 更新债权
     * @param vloanPersonInfo
     * @param map
     */
    @Transactional
	public void saveBuyBackLoanAndUpdateLoan(VLoanInfo vloanInfo,Map<String, String> map) {
		LoanReturnRecord loanReturnRecord = new LoanReturnRecord();
		loanReturnRecord.setAmount(new BigDecimal(map.get("amount")));
		loanReturnRecord.setBatchNum(vloanInfo.getBatchNum());
		loanReturnRecord.setBuyBackTime(Dates.getDateByString(map.get("buyBackTime"), Dates.DEFAULT_DATE_FORMAT));
		loanReturnRecord.setFundsSources(map.get("fundsSources"));
		loanReturnRecord.setLoanId(vloanInfo.getId());
		Map<String, Object> lrParams = new HashMap<String, Object>();
		lrParams.put("repaymentStates", new String[]{RepaymentStateEnum.未还款.name(),RepaymentStateEnum.不足额还款.name()
				,RepaymentStateEnum.不足罚息.name()});
		lrParams.put("loanId", vloanInfo.getId());
		List<LoanRepaymentDetail> lrList = loanRepaymentDetailServiceImpl.findByLoanIdAndRepaymentState(lrParams);
		if(CollectionUtils.isEmpty(lrList)){
			//没有逾期记录 记录当期
			loanReturnRecord.setCurrentTerm(Long.valueOf(vloanInfo.getCurrentTerm()));	
		}else{
			loanReturnRecord.setCurrentTerm(lrList.get(0).getCurrentTerm());
		}
		loanReturnRecord.setId(sequencesServiceImpl.getSequences(SequencesEnum.LOAN_RETURN_RECORD));
		//保存loanReturnRecord
		loanReturnRecordDao.insert(loanReturnRecord);
	    //更新loan_base
	    LoanBase loanBase=new LoanBase();
	    loanBase.setId(vloanInfo.getId());
	    String loanBelongs = map.get("loanBelongs");
	    if(Strings.isEmpty(loanBelongs)){
	    	loanBelongs = FundsSourcesTypeEnum.证大P2P.getValue();
	    }
	    loanBase.setLoanBelong(loanBelongs);
	    loanBaseService.updateLoanBaseByBuyBackLoan(loanBase);
	    //更新loan_ledger 
	    LoanLedger loanLedger = new LoanLedger();
	    loanLedger.setLoanId(vloanInfo.getId());
	    Map<String, Object> ldParams = new HashMap<String, Object>();
	    ldParams.put("loanId", vloanInfo.getId());
	    ldParams.put("state", "结清");
	    ldParams.put("currDate",Dates.getDateByString(map.get("buyBackTime"), Dates.DEFAULT_DATE_FORMAT));
	    ldParams.put("minDate", Dates.addDay(vloanInfo.getStartrdate(), -1));
	    BigDecimal drawRiskSumDeficit = loanRepaymentDetailServiceImpl.getDrawRiskSumDeficit(ldParams);
	    if (drawRiskSumDeficit==null) {
	    	loanLedger.setOtherPayable(new BigDecimal(0));
		}else{
			 loanLedger.setOtherPayable(drawRiskSumDeficit);
		}
	    loanLedgerService.updateOtherPayableByLoanId(loanLedger);
	    Date currDate = Dates.getCurrDate();
	    if(!currDate.after(vloanInfo.getEndrdate())){
	    	LoanReturn loanReturn = new LoanReturn();
			loanReturn.setLoanId(vloanInfo.getId());
	    	int count = loanReturnService.countLoanReturn(loanReturn);
	    	if(count > 0){
	    		return;
	    	}
			loanReturn.setAmount(loanReturnRecord.getAmount());
			loanReturn.setBatchNum(vloanInfo.getBatchNum());
			loanReturn.setFundsSources(loanReturnRecord.getFundsSources());
			loanReturn.setImportReason("债权回购导入");
			loanReturn.setCurrentTerm(Long.valueOf(vloanInfo.getCurrentTerm()));
	    	loanReturnService.insertLoanReturn(loanReturn);
	    }
	}

	@Override
	public void createLoanReturnRecord4Lufax(VLoanInfo vLoanInfo, DebitQueueLog debitQueueLog) {
		CompensatoryDetailLufax vo = new CompensatoryDetailLufax();
		vo.setLoanId(debitQueueLog.getLoanId());
		vo.setDebitQueueId(debitQueueLog.getId());
		vo.setType("02");//垫付类型（01：逾期代偿、02：一次性回购）
		CompensatoryDetailLufax compensatoryDetailLufax = compensatoryDetailLufaxDao.findListByVo(vo).get(0);
		LoanReturnRecord loanReturnRecord = new LoanReturnRecord();
		loanReturnRecord.setAmount(compensatoryDetailLufax.getTotalAmount());
		loanReturnRecord.setBatchNum(vLoanInfo.getBatchNum());
		loanReturnRecord.setBuyBackTime(compensatoryDetailLufax.getTradeDate());
		loanReturnRecord.setFundsSources(vLoanInfo.getFundsSources());
		loanReturnRecord.setLoanId(debitQueueLog.getLoanId());
		loanReturnRecord.setCurrentTerm(Long.parseLong(String.valueOf(compensatoryDetailLufax.getTerm())));
		loanReturnRecord.setId(sequencesServiceImpl.getSequences(SequencesEnum.LOAN_RETURN_RECORD));
		//保存loanReturnRecord
		loanReturnRecordDao.insert(loanReturnRecord);
		//更新loan_base
		LoanBase loanBase=new LoanBase();
		loanBase.setId(vLoanInfo.getId());
		loanBase.setLoanBelong(FundsSourcesTypeEnum.证大P2P.getValue());
		loanBaseService.updateLoanBaseByBuyBackLoan(loanBase);
		Date currDate = Dates.getCurrDate();
		if(!currDate.after(vLoanInfo.getEndrdate())){
			LoanReturn loanReturn = new LoanReturn();
			loanReturn.setLoanId(vLoanInfo.getId());
			int count = loanReturnService.countLoanReturn(loanReturn);
			if(count > 0){
				return;
			}
			loanReturn.setAmount(loanReturnRecord.getAmount());
			loanReturn.setBatchNum(vLoanInfo.getBatchNum());
			loanReturn.setFundsSources(loanReturnRecord.getFundsSources());
			loanReturn.setImportReason("债权回购导入");
			loanReturn.setCurrentTerm(Long.valueOf(vLoanInfo.getCurrentTerm()));
			loanReturnService.insertLoanReturn(loanReturn);
		}
	}
}
