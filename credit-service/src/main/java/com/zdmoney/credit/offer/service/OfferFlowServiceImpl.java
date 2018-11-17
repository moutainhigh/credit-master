package com.zdmoney.credit.offer.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.common.constant.LoanStateEnum;
import com.zdmoney.credit.common.constant.LoanTypeEnum;
import com.zdmoney.credit.common.constant.RepaymentStateEnum;
import com.zdmoney.credit.common.constant.SpecialRepaymentStateEnum;
import com.zdmoney.credit.common.constant.SpecialRepaymentTypeEnum;
import com.zdmoney.credit.common.constant.TradeTypeEnum;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.util.ToolUtils;
import com.zdmoney.credit.fortune.service.pub.IFortuneSurportService;
import com.zdmoney.credit.loan.dao.pub.ILoanBaseDao;
import com.zdmoney.credit.loan.dao.pub.ILoanProductDao;
import com.zdmoney.credit.loan.domain.LoanLedger;
import com.zdmoney.credit.loan.domain.LoanPreSettle;
import com.zdmoney.credit.loan.domain.LoanProduct;
import com.zdmoney.credit.loan.domain.LoanRepaymentDetail;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.IAfterLoanService;
import com.zdmoney.credit.loan.service.pub.ILoanLedgerService;
import com.zdmoney.credit.loan.service.pub.ILoanPreSettleService;
import com.zdmoney.credit.loan.service.pub.ILoanRepaymentDetailService;
import com.zdmoney.credit.loan.service.pub.ILoanSpecialRepaymentService;
import com.zdmoney.credit.loan.service.pub.ISpecialRepaymentApplyService;
import com.zdmoney.credit.offer.dao.pub.IOfferFlowDao;
import com.zdmoney.credit.offer.domain.OfferFlow;
import com.zdmoney.credit.offer.domain.OfferRepayInfo;
import com.zdmoney.credit.offer.service.pub.IOfferFlowService;
import com.zdmoney.credit.repay.vo.RepayStateDetail;
import com.zdmoney.credit.system.domain.BaseMessage;
import com.zdmoney.credit.system.domain.PersonInfo;
import com.zdmoney.credit.system.domain.SysDictionary;
import com.zdmoney.credit.system.service.pub.IBaseMessageService;
import com.zdmoney.credit.system.service.pub.IComEmployeeService;
import com.zdmoney.credit.system.service.pub.IPersonInfoService;
import com.zdmoney.credit.system.service.pub.ISequencesService;
import com.zdmoney.credit.system.service.pub.ISysDictionaryService;
import com.zdmoney.credit.zhuxue.domain.ZhuxueOrganization;
import com.zdmoney.credit.zhuxue.service.pub.IZhuxueOrganizationService;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.owasp.esapi.util.CollectionsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OfferFlowServiceImpl implements IOfferFlowService{
	private static final Logger logger = Logger.getLogger(OfferFlowServiceImpl.class);
	@Autowired
	private IOfferFlowDao offerFlowDao;
	@Autowired
	private ILoanLedgerService loanLedgerServiceImpl;
	@Autowired
	private IAfterLoanService afterLoanService;
	@Autowired 
	private ILoanRepaymentDetailService loanRepaymentDetailServiceImpl;
	@Autowired
	private ILoanBaseDao loanBaseDao;
	@Autowired 
	private ILoanSpecialRepaymentService loanSpecialRepaymentServiceImpl;
	@Autowired
	private ILoanProductDao loanProductDao;
	@Autowired
	private ILoanPreSettleService loanPreSettleService;
	@Autowired
	private IBaseMessageService baseMessageService;
	
	@Autowired
	private ISysDictionaryService sysDictionaryService;
	@Autowired
	private IComEmployeeService comEmployeeService;
	@Autowired
	private IPersonInfoService personInfoService;
	
	@Autowired
	private IFortuneSurportService fortuneSurportService;
	
	@Autowired
	private ISequencesService sequencesService;
	
	@Autowired
	private IZhuxueOrganizationService zhuxueOrganizationService;

    @Autowired
    private ISpecialRepaymentApplyService specialRepaymentApplyService ;
	
	@Override
	public void save(OfferFlow flowInstance) {
		offerFlowDao.insert(flowInstance);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void openAccountDeal(OfferRepayInfo repay, VLoanInfo loanInfo) {
		//非助学贷 风险金
        OfferFlow flowInstance=FlowBuild(repay, loanInfo.getRisk(), Const.ACCOUNT_TITLE_RISK_EXP,"","D","C");
        if(loanInfo.getFundsSources() .equals("积木盒子")) {
            flowInstance.setAppoAcct(Const.ACCOUNT_JIMUHEZIRISK);
        }
        if (!Accounting(flowInstance)){
        	//风险金
        	throw new PlatformException("新开户风险金处理失败，交易未完成！");
        }
		//管理费
        if (!Accounting(FlowBuild(repay,loanInfo.getManageRate(),Const.ACCOUNT_TITLE_MANAGE_EXP,"","D","C")))//管理费
            throw new PlatformException("新开户管理费处理失败，交易未完成！");
        //TODO:积木盒子：代码注释
        if (!Accounting(FlowBuild(repay,loanInfo.getManageRateForPartyC(),Const.ACCOUNT_TITLE_MANAGEC_EXP,"","D","C")))//管理费
            throw new PlatformException("新开户丙方管理费处理失败，交易未完成！");
        //咨询费
        if (!Accounting(FlowBuild(repay,loanInfo.getReferRate(),Const.ACCOUNT_TITLE_CONSULT_EXP,"","D","C")))//咨询费
            throw new PlatformException("新开户咨询费处理失败，交易未完成！");
        //评估费
        if (!Accounting(FlowBuild(repay,loanInfo.getEvalRate(),Const.ACCOUNT_TITLE_APPRAISAL_EXP,"","D","C")))//评估费
            throw new PlatformException("新开户评估费处理失败，交易未完成！");
        //合同金额
        if (!Accounting(FlowBuild(repay,loanInfo.getPactMoney(),Const.ACCOUNT_TITLE_LOAN_AMOUNT,"","C","D")))//合同金额
            throw new PlatformException("新开户贷款本金记账处理失败，交易未完成！");
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void accountBalancePayDeal(OfferRepayInfo repay, BigDecimal amount) {
		 if (!Accounting(FlowBuild(repay,amount,Const.ACCOUNT_TITLE_AMOUNT,"挂账部分销账","C","C"))){
         	//挂账部分销账
         	throw new PlatformException("挂账部分销账处理失败，交易未完成！");
         }
		
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void accountDepositDeal(OfferRepayInfo repay) {
		OfferFlow flowInstance=FlowBuild(repay,repay.getAmount(),Const.ACCOUNT_TITLE_AMOUNT,"","D","D");
        flowInstance.setEndAmount((afterLoanService.getAccAmount(repay.getLoanId()).add(repay.getAmount())).setScale(2,RoundingMode.HALF_UP));
        if (!Accounting(flowInstance)){
        	// 现金挂账
        	throw new PlatformException("现金挂账处理失败，交易未完成！");
        }
		
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void bondAccountDepositDeal(OfferRepayInfo repay) {
		OfferFlow flowInstance=FlowBuild(repay,repay.getAmount(),Const.ACCOUNT_TITLE_AMOUNT,"","D","");
        flowInstance.setAppoAcct("");
        flowInstance.setAppoAcctTitle("");
        flowInstance.setEndAmount((afterLoanService.getAccAmount(repay.getLoanId()).add(repay.getAmount())).setScale(2,RoundingMode.HALF_UP));
        if(!Accounting(flowInstance)){
        	throw new PlatformException("保证金账户挂账处理失败，交易未完成！");
        }
		
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void accountWithdrawDeal(OfferRepayInfo repay) {
		 if(repay.getAmount().compareTo(afterLoanService.getAccAmount(repay.getLoanId())) >0 )
         {
         	throw new PlatformException("账户取款金额大于账户余额，交易未完成！");
         }
         OfferFlow flowInstance=FlowBuild(repay,repay.getAmount(),Const.ACCOUNT_TITLE_AMOUNT,"挂账部分销账","C","C");
         flowInstance.setEndAmount((afterLoanService.getAccAmount(repay.getLoanId()).subtract(repay.getAmount())).setScale(2,RoundingMode.HALF_UP));
         if (!Accounting(flowInstance))//挂账部分销账
             throw new PlatformException("挂账部分销账处理失败，交易未完成！");
		
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void bondAccountWithdrawDeal(OfferRepayInfo repay) {
		if(repay.getAmount().compareTo(afterLoanService.getAccAmount(repay.getLoanId()))>0)
        {
        	throw new PlatformException("保证金账户取款金额大于账户余额，交易未完成！");
        }
        OfferFlow flowInstance=FlowBuild(repay,repay.getAmount(),Const.ACCOUNT_TITLE_AMOUNT,"挂账部分销账","C","");//保证金账户挂账部分销账
        flowInstance.setAppoAcct("");
        flowInstance.setAppoAcctTitle("");
        flowInstance.setEndAmount((afterLoanService.getAccAmount(repay.getLoanId()).subtract(repay.getAmount())).setScale(2,RoundingMode.HALF_UP));
        if(!Accounting(flowInstance)){
        	throw new PlatformException("保证金账户销账处理失败，交易未完成！");
        }
		
	}
	
	/*@Override
	public void accountRechargeDeal(OfferRepayInfo repay) {
		 if(repay.getAmount().compareTo(new BigDecimal("0.01"))<0)
         {
         	throw new PlatformException("未还款项小于0.01元，交易未完成！");
         }
         if (!Accounting(FlowBuild(repay,repay.getAmount(),Const.ACCOUNT_TITLE_AMOUNT,"","D","D" ))){//多余挂账
         	throw new PlatformException("多余现金挂账处理失败，交易未完成！");
         }
		
	}*/
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void accountRechargeDeal(OfferRepayInfo repay, BigDecimal countAMT) {
		if(countAMT.compareTo(new BigDecimal("0.01"))<0)
        {
			logger.warn("金额挂账accountRechargeDeal,loanid:"+repay.getLoanId()+",挂账款项小于0.01元，交易未完成！");
        	throw new PlatformException(ResponseEnum.SYS_WARN,"挂账款项小于0.01元，交易未完成！");
        }
		if(!Accounting(FlowBuild(repay,countAMT,Const.ACCOUNT_TITLE_AMOUNT,"","D","D")) ){//多余挂账
			logger.error("金额挂账accountRechargeDeal,loanid:"+repay.getLoanId()+",多余现金挂账处理失败,交易未完成！");
        	throw new PlatformException("多余现金挂账处理失败，交易未完成！");
        }
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public OfferFlow reductionPenaltyDeal(OfferRepayInfo repay, BigDecimal relief) {
		OfferFlow flow = new OfferFlow();
		flow.setId(sequencesService.getSequences(SequencesEnum.OFFER_FLOW));
        RepayToFlow(repay,flow);
        flow.setAccount(Const.ACCOUNT_GAINS);
        flow.setAcctTitle(Const.ACCOUNT_TITLE_FINE_EXP);
        flow.setAppoAcct(String.valueOf(repay.getLoanId()));//因新系统吧loanid放在了新字段里，这里是loanid
        flow.setAppoAcctTitle(Const.ACCOUNT_TITLE_FINE_INCOME);
        flow.setTradeAmount(relief);
        flow.setDorc("D");
        flow.setAppoDorc("C");
        flow.setMemo3("罚息减免流水");
        flow.setCreateTime(new Date());
        if (!Accounting(flow)){
        	logger.error("逾期罚息流水记账处理失败，交易未完成！");
        	//记账出错时
        	throw new PlatformException("逾期罚息流水记账处理失败，交易未完成！");
        }
            
        //更新特殊还款表状态
        if(repay.getReductionSpecialRepayment()!=null){
        	repay.getReductionSpecialRepayment().setSpecialRepaymentState( SpecialRepaymentStateEnum.结束.getValue());
        	repay.setUpdateTime(new Date());
            loanSpecialRepaymentServiceImpl.update(repay.getReductionSpecialRepayment());
        }else{
        	logger.error("记账更新特殊还款表时，未找到特殊还款记录!  loanid="+repay.getLoanId());
        }
        
		return flow;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int fullRepaymentDeal(OfferRepayInfo repay, VLoanInfo loanInfo,BigDecimal countAMT, List<LoanRepaymentDetail> overDateList) {
		if(!loanInfo.getFundsSources().equals("积木盒子") &&  repay.getTradeDate().compareTo(loanInfo.getEndrdate()) <= 0){
            Date curDay  = Dates.getCurrDate();
            if(curDay.compareTo(repay.getTradeDate()) > 0){
                Date nextRepayDate = ToolUtils.getNextRepayDateIncludeToday(repay.getTradeDate());
                DateTime dateTime = new DateTime(nextRepayDate);
                if(dateTime.getDayOfMonth() != loanInfo.getPromiseReturnDate()){
                    nextRepayDate = ToolUtils.getNextRepayDate(nextRepayDate);
                }
                if(curDay.compareTo(nextRepayDate) <= 0){
                    if(!(repay.getTradeType().equals(TradeTypeEnum.挂账.getValue())))
                    {
                        if(!Accounting(FlowBuild(repay,countAMT,Const.ACCOUNT_TITLE_AMOUNT,"","D","D"))){
                        	//非日终处理时，一次性还款金额不足时挂账
                        	throw new PlatformException("一次性还款金额足额挂账处理失败，交易未完成！");
                        }
                        
                        loanPreSettleService.creartPreSettle(loanInfo,repay.getTradeDate());
                        loanBaseDao.updateLoanState(loanInfo.getId(),LoanStateEnum.预结清.getValue());
                        
                        if(repay.getTradeDate().compareTo(loanInfo.getEndrdate()) <= 0 ){
                        	 if(Strings.isNotEmpty(loanInfo.getBatchNum())){
                                 if(loanInfo.getBatchNum().substring(0,2).indexOf("XL")>-1 || loanInfo.getBatchNum().substring(0,2).indexOf("WC")>-1){
                                     passMessage(loanInfo, "预结清通知");
                                 }
                             }
                        }
                        return 1;
                    }else{
                        List<LoanPreSettle> list=loanPreSettleService.findByLoanId(loanInfo.getId());
                        if (CollectionUtils.isEmpty(list)){
                            loanPreSettleService.creartPreSettlePromise(loanInfo, repay.getTradeDate());
                        }else{
                            for (int i = 0; i < list.size(); i++) {
                                LoanPreSettle loanPreSettle=list.get(i);
                                loanPreSettle.setRealSettleDate(repay.getTradeDate());
                                loanPreSettleService.updateLoanPreSettle(loanPreSettle);
                            }
                        }
                    }
                }
            }else{
                if(!(repay.getTradeType().equals(TradeTypeEnum.挂账.getValue())))
                {
                    if(!Accounting(FlowBuild(repay,countAMT,Const.ACCOUNT_TITLE_AMOUNT,"","D","D"))){
                    	//非日终处理时，一次性还款金额不足时挂账
                    	throw new PlatformException("一次性还款金额足额挂账处理失败，交易未完成！");
                    }
                    loanPreSettleService.creartPreSettle(loanInfo,repay.getTradeDate());
                    loanBaseDao.updateLoanState(loanInfo.getId(),LoanStateEnum.预结清.getValue());
                    if(repay.getTradeDate().compareTo(loanInfo.getEndrdate()) <= 0){
                        if(Strings.isNotEmpty(loanInfo.getBatchNum())){
                            if(loanInfo.getBatchNum().substring(0,2).indexOf("XL")>-1 || loanInfo.getBatchNum().substring(0,2).indexOf("WC")>-1){
                                passMessage(loanInfo, "预结清通知");
                            }
                        }
                    }
                    return 1;
                }else{
                    List<LoanPreSettle> list=loanPreSettleService.findByLoanId(loanInfo.getId());
                    if (CollectionUtils.isEmpty(list)){
                        loanPreSettleService.creartPreSettlePromise(loanInfo, repay.getTradeDate());
                    }else{
                        for (int i = 0; i < list.size(); i++) {
                            LoanPreSettle loanPreSettle=list.get(i);
                            loanPreSettle.setRealSettleDate(repay.getTradeDate());
                            loanPreSettleService.updateLoanPreSettle(loanPreSettle);
                        }
                    }
                }
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(loanInfo.getEndrdate());
            cal.add(Calendar.MONTH, -1);
            if(repay.getTradeDate().compareTo(cal.getTime()) <= 0){
            //    if(loanInfo.getFundsSources().equals("证大P2P")){//证大P2P
               if(loanInfo.getLoanBelong().equals("证大P2P") || loanInfo.getLoanBelong().equals("挖财") || loanInfo.getLoanBelong().equals("新浪")){ //20160202同步1.0	
                    passDataToFortune(loanInfo,repay);
                }
                if(loanInfo.getFundsSources().equals("证大爱特")){//证大爱特、挖财、新浪提前结清消息通知
                    passMessage(loanInfo, "提前结清通知");
                }else if(Strings.isNotEmpty(loanInfo.getBatchNum())){
                    if(loanInfo.getBatchNum().substring(0,2).indexOf("XL")>-1 || loanInfo.getBatchNum().substring(0,2).indexOf("WC")>-1){
                        passMessage(loanInfo, "提前结清通知");
                    }
                }
            }
        }
		/** 上面的非挂账交易，只做现金入账处理和发预结清通知，分账只会是在交易类型为【挂账】的时候才会做 ，也就是【日终】的时候**/
        /** 下面的代码才是一次性结清日终的时候挂账交易要做分账的代码 **/
		int result = realtimeFullRepaymentDeal(repay, loanInfo,countAMT, overDateList);
        return result;
	}
	
	@Override
    @Transactional(propagation=Propagation.REQUIRED)
	public int realtimeFullRepaymentDeal(OfferRepayInfo repay, VLoanInfo loanInfo,BigDecimal countAMT, List<LoanRepaymentDetail> overDateList){
	    //更新还款计划表，贷款基本信息表                  
        loanRepaymentDetailServiceImpl.updateYCXJQAllStateToSettlementByLoanId(loanInfo.getId(),repay.getTradeDate());
        loanBaseDao.updateLoanState(loanInfo.getId(),LoanStateEnum.结清.getValue());
        //更新剩余本金
        LoanProduct loanProduct = loanProductDao.findByLoanId(loanInfo.getId());
        loanProduct.setResidualPactMoney(BigDecimal.ZERO);
        loanProduct.setUpdateTime(new Date());
        loanProductDao.update(loanProduct);  
        
        //2013-09-26 添加，申请一次还款且已过最后一期还款日
        if(overDateList.get(overDateList.size()-1).getReturnDate().compareTo(repay.getTradeDate())<0)
        {
            if (!Accounting(FlowBuild(repay,countAMT,Const.ACCOUNT_TITLE_AMOUNT,"","D","D"))){
                throw new PlatformException("一次性还款后剩余现金挂账处理失败，交易未完成！");
            }
            
            loanSpecialRepaymentServiceImpl.updateSpecialRepaymentToEnd(loanInfo.getId(), SpecialRepaymentTypeEnum.一次性还款.name(),  "申请");
            return 1;
        }

        //记账，这里是否只需还的总本金，利息，违约金拆开即可（三笔）
        BigDecimal amount = loanInfo.getPactMoney();//合同金额
        BigDecimal lixi = BigDecimal.ZERO;
        LoanRepaymentDetail lastRep = overDateList.get(overDateList.size()-1);
        
        if ((loanInfo.getLoanType().equals(LoanTypeEnum.助学贷.getValue()))){
            Date d602 = Dates.parse("2014-6-19", "yyyy-MM-dd");  //上线当天用以前的规则，当天上线后不允许生成新的合同。
            //违约金
            BigDecimal temp = lastRep.getPenalty();
            if (!Accounting(FlowBuild(repay,temp,Const.ACCOUNT_TITLE_PENALTY_EXP,lastRep.getCurrentTerm().toString(),"D","C"))){
                //违约金支出
                throw new PlatformException("违约金支出记账处理失败，交易未完成！");
            }
                
            lixi = lixi.add(temp);
            lixi = lixi.subtract(lastRep.getGiveBackRate()) ;

            //咨询费(退费)
            if(loanInfo.getSignDate().compareTo(d602)<0 ){
                temp = (lastRep.getGiveBackRate().multiply(loanInfo.getReferRate()).divide((loanInfo.getPactMoney().subtract(loanInfo.getGrantMoney())),2,RoundingMode.HALF_UP)).setScale(2,RoundingMode.HALF_UP);
            } else {
                temp = loanInfo.getReferRate();
            }
            
            if (!Accounting(FlowBuild(repay,temp,Const.ACCOUNT_TITLE_CONSULT_EXP,lastRep.getCurrentTerm().toString(),"C","D"))){
                throw new PlatformException("一次性还款退咨询费记账处理失败，交易未完成！");
            }
                
            amount = temp;
            
            //评估费(退费)
            if(loanInfo.getSignDate().compareTo(d602)<0 ) {
                temp = (lastRep.getGiveBackRate().multiply(loanInfo.getEvalRate()).divide((loanInfo.getPactMoney().subtract(loanInfo.getGrantMoney())),2,RoundingMode.HALF_UP)).setScale(2,RoundingMode.HALF_UP);
            } else {
                temp = loanInfo.getReferRate();
            }
            
            if (!Accounting(FlowBuild(repay,temp,Const.ACCOUNT_TITLE_APPRAISAL_EXP,lastRep.getCurrentTerm().toString(),"C","D"))){
                throw new PlatformException("一次性还款退评估费记账处理失败，交易未完成！");
            }
                
            amount = amount.add(temp);
            
            //管理费(退费)
            if(loanInfo.getSignDate().compareTo(d602)<0 ){
                temp = (lastRep.getGiveBackRate().multiply(loanInfo.getManageRate()).divide((loanInfo.getPactMoney().subtract(loanInfo.getGrantMoney())),2,RoundingMode.HALF_UP)).setScale(2,RoundingMode.HALF_UP);
            } else {//一次性还款 当期退费-评估费支出-咨询费支出
                temp = lastRep.getGiveBackRate().subtract(loanInfo.getEvalRate()).subtract(loanInfo.getReferRate()) ;
            }
            
            if (!Accounting(FlowBuild(repay,temp,Const.ACCOUNT_TITLE_MANAGE_EXP,lastRep.getCurrentTerm().toString(),"C","D"))){
                throw new PlatformException("一次性还款管理费记账处理失败，交易未完成！");
            }
                
            amount = amount.add(temp);
        }
        else { //个贷
            BigDecimal temp = BigDecimal.ZERO;
            Date d602 = Dates.parse("2014-6-19", "yyyy-MM-dd"); //上线当天用以前的规则，当天上线后不允许生成新的合同。
            /** 当期违约金 **/
            BigDecimal penalty = lastRep.getPenalty();
            if (penalty == null) {
                penalty = BigDecimal.ZERO;
            }
            
            if (penalty.compareTo(BigDecimal.ZERO) > 0) 
            {
                temp = penalty;
                if (!Accounting(FlowBuild(repay,temp,Const.ACCOUNT_TITLE_PENALTY_EXP,lastRep.getCurrentTerm().toString(),"D","C"))){
                    //违约金支出
                    throw new PlatformException("违约金支出记账处理失败，交易未完成！");
                }
                lixi = lixi.add(temp);
            }
            lixi = lixi.subtract(lastRep.getGiveBackRate());
            //退咨询费
            //temp=(overDateList[-1].giveBackRate*Const.PENALTY_BACK_CONSULT).setScale(2,RoundingMode.HALF_UP);
            if(loanInfo.getSignDate().compareTo(d602)<0 ){
                temp = (lastRep.getGiveBackRate().multiply(loanInfo.getReferRate()).divide((loanInfo.getPactMoney().subtract(loanInfo.getGrantMoney())),2,RoundingMode.HALF_UP)).setScale(2,RoundingMode.HALF_UP);
            } else{
                temp = (lastRep.getGiveBackRate().multiply(loanInfo.getReferRate()).divide((loanInfo.getRateSum().subtract(loanInfo.getManageRateForPartyC())),2,RoundingMode.HALF_UP)).setScale(2,RoundingMode.HALF_UP);
            }
            
            if (!Accounting(FlowBuild(repay,temp,Const.ACCOUNT_TITLE_CONSULT_EXP,lastRep.getCurrentTerm().toString(),"C","D"))){
                throw new PlatformException("一次性还款退咨询费记账处理失败，交易未完成！");
            }
                
            amount = temp;
            //退评估费
            //temp=(overDateList[-1].giveBackRate*Const.PENALTY_BACK_APPRAISAL).setScale(2,RoundingMode.HALF_UP);
            if(loanInfo.getSignDate().compareTo(d602)<0 ){
                temp = (lastRep.getGiveBackRate().multiply(loanInfo.getEvalRate()).divide((loanInfo.getPactMoney().subtract(loanInfo.getGrantMoney())),2,RoundingMode.HALF_UP)).setScale(2,RoundingMode.HALF_UP);
            } else {
                temp = (lastRep.getGiveBackRate().multiply(loanInfo.getEvalRate()).divide((loanInfo.getRateSum().subtract(loanInfo.getManageRateForPartyC())),2,RoundingMode.HALF_UP)).setScale(2,RoundingMode.HALF_UP);
            }
            
            if (!Accounting(FlowBuild(repay,temp,Const.ACCOUNT_TITLE_APPRAISAL_EXP,lastRep.getCurrentTerm().toString(),"C","D"))){
                throw new PlatformException("一次性还款退评估费记账处理失败，交易未完成！");
            }
                
            amount = amount.add(temp);
             //退丙方管理费
            if(loanInfo.getSignDate().compareTo(d602)<0 ){
                temp = (lastRep.getGiveBackRate().multiply(loanInfo.getManageRateForPartyC()).divide((loanInfo.getPactMoney().subtract(loanInfo.getGrantMoney())),2,RoundingMode.HALF_UP)).setScale(2,RoundingMode.HALF_UP);
            } else {
                temp = BigDecimal.ZERO;
            }
            
            if (!Accounting(FlowBuild(repay,temp,Const.ACCOUNT_TITLE_MANAGEC_EXP,lastRep.getCurrentTerm().toString(),"C","D"))){
                throw new PlatformException("一次性还款退丙方管理费记账处理失败，交易未完成！");
            }
                
            amount = amount.add(temp);
            //退乙方管理费(退费误差在此体现)
            temp = lastRep.getGiveBackRate().subtract(amount);
            if (!Accounting(FlowBuild(repay,temp,Const.ACCOUNT_TITLE_MANAGE_EXP,lastRep.getCurrentTerm().toString(),"C","D"))){
                throw new PlatformException("一次性还款退管理费记账处理失败，交易未完成！");
            }
        }
        
        BigDecimal benji = lastRep.getRepaymentAll().subtract(lixi).subtract(lastRep.getCurrentAccrual());
        lixi = lastRep.getCurrentAccrual();
        
        //特殊情况，当期已部分还款后，再做一次性还款
        amount = lastRep.getReturneterm().subtract(lastRep.getDeficit());
        if (amount.compareTo(BigDecimal.ZERO)>0) {
            lixi = lixi.subtract(amount).compareTo(BigDecimal.ZERO) > 0 ? lixi.subtract(amount): new BigDecimal(0.00);
            benji = lixi.subtract(amount).compareTo(BigDecimal.ZERO) <0 ? benji.add(lixi).subtract(amount): benji;
        }

        if (!Accounting(FlowBuild(repay,lixi,Const.ACCOUNT_TITLE_INTEREST_EXP,lastRep.getCurrentTerm().toString(),"D","C"))){
            //利息流水
            throw new PlatformException("一次性还款利息记账处理失败，交易未完成！");
        }
            
        //剩余本金+当前本金
        if (!Accounting(FlowBuild(repay,benji,Const.ACCOUNT_TITLE_LOAN_AMOUNT,lastRep.getCurrentTerm().toString(),"D","C"))){
            //本金流水
            throw new PlatformException("一次性还款本金记账处理失败，交易未完成！");
        }
            
        //将剩余？？？
        amount = countAMT.subtract(lastRep.getRepaymentAll());
        if (amount.compareTo(BigDecimal.ZERO)>0)
        {
            if (!Accounting(FlowBuild(repay,amount,Const.ACCOUNT_TITLE_AMOUNT,"","D","D"))){
                //多余挂账
                throw new PlatformException("一次性还款后剩余现金挂账处理失败，交易未完成！");
            }
        }
                
        //还款计划表剩余金额清零
        loanRepaymentDetailServiceImpl.updateYCXJQAllDeficitToZeroByLoanId(loanInfo.getId());
        //更新一次性还款申请到结束
        loanSpecialRepaymentServiceImpl.updateSpecialRepaymentToEnd(loanInfo.getId(), SpecialRepaymentTypeEnum.一次性还款.getValue(), "申请");

        return 0;
	}
	
	/**
	 * 记账处理<br>
	 * s
	 * @param flowInstance
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean Accounting(OfferFlow flowInstance) {
		if(flowInstance==null){
			return true;
		}
		boolean result= true;
		if (Strings.isNotEmpty(flowInstance.getAccount()) && Strings.isNotEmpty(flowInstance.getAcctTitle())) { //帐号不为空
			result &= AcctTitleProcess(flowInstance.getAccount(),
					flowInstance.getAcctTitle(), flowInstance.getDorc(),
					flowInstance.getTradeAmount());
		}
		if (Strings.isNotEmpty(flowInstance.getAppoAcct()) && (Strings.isNotEmpty(flowInstance.getAppoAcctTitle()))) { //对方帐号不为空
			result &= AcctTitleProcess(flowInstance.getAppoAcct(),
					flowInstance.getAppoAcctTitle(),
					flowInstance.getAppoDorc(),
					flowInstance.getTradeAmount());
		}
		offerFlowDao.insert(flowInstance);
		return result;
	}
	/**
	 * 流水帐生成<br>
	 * 原来默认：String dOrc='D',String appo_dORc='C'
	 * @param repay
	 * @param amount
	 * @param acctTitle
	 * @param memo2
	 * @param dOrc
	 * @param appo_dORc
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public OfferFlow FlowBuild(OfferRepayInfo repay, BigDecimal amount,
			String acctTitle, String memo2, String dOrc,
			String appo_dORc) {

		if (amount.compareTo(new BigDecimal("0.01")) < 0) {
			return null;
		}
		
		OfferFlow result = new OfferFlow();
		result.setId(sequencesService.getSequences(SequencesEnum.OFFER_FLOW));
		
        if (acctTitle.equals(Const.ACCOUNT_TITLE_AMOUNT))//现金
        {
            result.setAppoAcct(Const.ACCOUNT_REPAYMENT);
            result.setAppoAcctTitle(Const.ACCOUNT_TITLE_AMOUNT);
        }
        else if(acctTitle.equals(Const.ACCOUNT_TITLE_MANAGE_EXP))//管理费支出
        {
            result.setAppoAcct(Const.ACCOUNT_INVESTMENT);
            result.setAppoAcctTitle(Const.ACCOUNT_TITLE_MANAGE_INCOME);
        }
        else if(acctTitle.equals(Const.ACCOUNT_TITLE_MANAGEC_EXP))//丙方管理费支出
        {
            result.setAppoAcct(Const.ACCOUNT_FORTUNE);
            result.setAppoAcctTitle(Const.ACCOUNT_TITLE_MANAGE_INCOME);
        }
        else if(acctTitle.equals(Const.ACCOUNT_TITLE_APPRAISAL_EXP))//评估费支出
        {
            result.setAppoAcct(Const.ACCOUNT_FORTUNE);
            result.setAppoAcctTitle(Const.ACCOUNT_TITLE_APPRAISAL_INCOME);
        }
        else if(acctTitle.equals(Const.ACCOUNT_TITLE_CONSULT_EXP))  //咨询费支出
        {
            result.setAppoAcct(Const.ACCOUNT_FORTUNE);
            result.setAppoAcctTitle(Const.ACCOUNT_TITLE_CONSULT_INCOME);
        }
        else if (acctTitle.equals(Const.ACCOUNT_TITLE_FINE_EXP)) //罚息支出
        {
            result.setAppoAcct(Const.ACCOUNT_GAINS);
            result.setAppoAcctTitle(Const.ACCOUNT_TITLE_FINE_INCOME);
        }
        else if(acctTitle.equals(Const.ACCOUNT_TITLE_PENALTY_EXP))      //违约金支出
        {
            result.setAppoAcct(Const.ACCOUNT_GAINS);
            result.setAppoAcctTitle(Const.ACCOUNT_TITLE_PENALTY_INCOME);
        }
        else if(acctTitle.equals(Const.ACCOUNT_TITLE_LOAN_AMOUNT)) ///贷款本金
        {
            result.setAppoAcct(Const.ACCOUNT_INTERJACENT);
            result.setAppoAcctTitle(Const.ACCOUNT_TITLE_FINANCING);
        }
        else if(acctTitle.equals(Const.ACCOUNT_TITLE_INTEREST_EXP))    //利息支出
        {
            result.setAppoAcct(Const.ACCOUNT_INTERJACENT);
            result.setAppoAcctTitle(Const.ACCOUNT_TITLE_INTEREST_INCOME);
        }
        else if (acctTitle.equals(Const.ACCOUNT_TITLE_RISK_EXP))//风险金支出
        {
            if(!IsInternalAcct(String.valueOf(repay.getLoanId()))) //放款时
            {
                result.setAppoAcct(Const.ACCOUNT_RISK);
                result.setAppoAcctTitle(Const.ACCOUNT_TITLE_RISK_INCOME);
            }
        }
        else if(acctTitle.equals(Const.ACCOUNT_TITLE_OTHER_PAYABLE))   //其他应付款
        {

        }
        else if(acctTitle.equals(Const.ACCOUNT_TITLE_OTHER_EXP))   //其他营业支出
        {

        }
		
		RepayToFlow(repay, result);
		result.setAcctTitle(acctTitle);
		result.setDorc(dOrc);
		result.setAppoDorc(appo_dORc);
		result.setTradeAmount(amount);
		result.setMemo2(memo2);
				
		return result;
	}
	
	/*根据科目号，进行记账*/
	@Transactional(propagation=Propagation.REQUIRED)
	private boolean AcctTitleProcess(String account,String acctTitle,String  dorC,BigDecimal amount)
    {
        LoanLedger led= loanLedgerServiceImpl.findByAccountForUpdate(account);
        if(led==null) //帐户不存在时 ,新建账户
        {
            led=new LoanLedger();
            led.setId(sequencesService.getSequences(SequencesEnum.LOAN_LEDGER));
            led.setAccount(account);
            
            if(IsInternalAcct(account)){
                led.setType(Const.ACCOUNT_TYPE_IN);
            }
            else{
            	led.setLoanId(Long.parseLong(account));
                led.setType(Const.ACCOUNT_TYPE_LA);
            }
            loanLedgerServiceImpl.saveNow(led);
        }
        if(acctTitle.equals(Const.ACCOUNT_TITLE_AMOUNT))//现金
        {
            if (dorC==Const.DorC.D.toString()){
            	BigDecimal sum = led.getAmount().add(amount);
            	led.setAmount(sum);
            }
            else{
            	BigDecimal sum = led.getAmount().subtract(amount);
            	led.setAmount(sum);
            }
        }
        else if(acctTitle.equals(Const.ACCOUNT_TITLE_INTEREST_RECEI))//应收利息
        {
            if (dorC==Const.DorC.D.toString()){
            	BigDecimal sum = led.getInterestReceivable().add(amount);
            	led.setInterestReceivable(sum);
            }
            else{
            	BigDecimal sum = led.getInterestReceivable().subtract(amount);
            	led.setInterestReceivable(sum);
            }
        }
        else if(acctTitle.equals(Const.ACCOUNT_TITLE_FINE_RECEI))//应收罚息
        {
            if (dorC==Const.DorC.D.toString()){
            	BigDecimal sum = led.getFineReceivable().add(amount);
            	led.setFineReceivable(sum);
            }
            else{
            	BigDecimal sum = led.getFineReceivable().subtract(amount);
            	led.setFineReceivable(sum);
            }
        }
        else if(acctTitle.equals(Const.ACCOUNT_TITLE_LOAN_AMOUNT))//借款本金
        {
        	if (dorC==Const.DorC.D.toString()){
            	BigDecimal sum = led.getLoanAmount().subtract(amount);
            	led.setLoanAmount(sum);
            }
            else{
            	BigDecimal sum = led.getLoanAmount().add(amount);
            	led.setLoanAmount(sum);
            }
        }
        else if(acctTitle.equals(Const.ACCOUNT_TITLE_FINANCING))//还本金 理财户 居间人
        {
        	if (dorC==Const.DorC.D.toString()){
            	BigDecimal sum = led.getAmount().subtract(amount);
            	led.setAmount(sum);
            }
            else{
            	BigDecimal sum = led.getAmount().add(amount);
            	led.setAmount(sum);
            }
        }
        else if(acctTitle.equals(Const.ACCOUNT_TITLE_INTEREST_PAYABLE))//应付利息
        {
            if (dorC==Const.DorC.D.toString()){
            	BigDecimal sum = led.getInterestPayable().subtract(amount);
            	led.setInterestPayable(sum);
            }else{
            	BigDecimal sum = led.getInterestPayable().add(amount);
            	led.setInterestPayable(sum);
            }
        }
        else if(acctTitle.equals(Const.ACCOUNT_TITLE_FINE_PAYABLE))//应付罚息
        {
        	
        	if (dorC==Const.DorC.D.toString()){
            	BigDecimal sum = led.getFinePayable().subtract(amount);
            	led.setFinePayable(sum);
            }else{
            	BigDecimal sum = led.getFinePayable().add(amount);
            	led.setFinePayable(sum);
            }
        }
        else if(acctTitle.equals(Const.ACCOUNT_TITLE_INTEREST_INCOME))//利息收入
        {
        	if (dorC==Const.DorC.D.toString()){
            	BigDecimal sum = led.getInterestIncome().subtract(amount);
            	led.setInterestIncome(sum);
            }else{
            	BigDecimal sum = led.getInterestIncome().add(amount);
            	led.setInterestIncome(sum);
            }
        }
        else if(acctTitle.equals(Const.ACCOUNT_TITLE_FINE_INCOME))//逾期罚息收入
        {
        	if (dorC==Const.DorC.D.toString()){
            	BigDecimal sum = led.getFineIncome().subtract(amount);
            	led.setFineIncome(sum);
            }else{
            	BigDecimal sum = led.getFineIncome().add(amount);
            	led.setFineIncome(sum);
            }
        }
        else if(acctTitle.equals(Const.ACCOUNT_TITLE_CONSULT_INCOME))//咨询费收入
        {
        	if (dorC==Const.DorC.D.toString()){
            	BigDecimal sum = led.getConsultIncome().subtract(amount);
            	led.setConsultIncome(sum);
            }else{
            	BigDecimal sum = led.getConsultIncome().add(amount);
            	led.setConsultIncome(sum);
            }
        }
        else if(acctTitle.equals(Const.ACCOUNT_TITLE_APPRAISAL_INCOME))//评估费收入
        {
        	if (dorC==Const.DorC.D.toString()){
            	BigDecimal sum = led.getAppraisalIncome().subtract(amount);
            	led.setAppraisalIncome(sum);
            }else{
            	BigDecimal sum = led.getAppraisalIncome().add(amount);
            	led.setAppraisalIncome(sum);
            }
        }
        else if(acctTitle.equals(Const.ACCOUNT_TITLE_MANAGE_INCOME))//管理费收入
        {
        	if (dorC==Const.DorC.D.toString()){
            	BigDecimal sum = led.getManageIncome().subtract(amount);
            	led.setManageIncome(sum);
            }else{
            	BigDecimal sum = led.getManageIncome().add(amount);
            	led.setManageIncome(sum);
            }
        }
        else if(acctTitle.equals(Const.ACCOUNT_TITLE_PENALTY_INCOME))//违约金收入
        {
        	if (dorC==Const.DorC.D.toString()){
            	BigDecimal sum = led.getPenaltyIncome().subtract(amount);
            	led.setPenaltyIncome(sum);
            }else{
            	BigDecimal sum = led.getManageIncome().add(amount);
            	led.setManageIncome(sum);
            }
        }
        else if(acctTitle.equals(Const.ACCOUNT_TITLE_OTHER_INCOME))//其他营业收入
        {
        	if (dorC==Const.DorC.D.toString()){
            	BigDecimal sum = led.getOtherIncome().subtract(amount);
            	led.setOtherIncome(sum);
            }else{
            	BigDecimal sum = led.getOtherIncome().add(amount);
            	led.setOtherIncome(sum);
            }
        }
        else if(acctTitle.equals(Const.ACCOUNT_TITLE_NONOPERAT_INCOME))//营业外收入
        {
        	if (dorC==Const.DorC.D.toString()){
            	BigDecimal sum = led.getNonOperatIncome().subtract(amount);
            	led.setNonOperatIncome(sum);
            }else{
            	BigDecimal sum = led.getNonOperatIncome().add(amount);
            	led.setNonOperatIncome(sum);
            }
        }
        else if(acctTitle.equals(Const.ACCOUNT_TITLE_INTEREST_EXP))//利息支出
        {
        	if (dorC==Const.DorC.D.toString()){
            	BigDecimal sum = led.getInterestExp().add(amount);
            	led.setInterestExp(sum);
            }else{
            	BigDecimal sum = led.getInterestExp().subtract(amount);
            	led.setInterestExp(sum);
            }
        }
        else if(acctTitle.equals(Const.ACCOUNT_TITLE_FINE_EXP))//逾期罚息支出
        {
        	if (dorC==Const.DorC.D.toString()){
            	BigDecimal sum = led.getFineExp().add(amount);
            	led.setFineExp(sum);
            }else{
            	BigDecimal sum = led.getFineExp().subtract(amount);
            	led.setFineExp(sum);
            }
        }
        else if(acctTitle.equals(Const.ACCOUNT_TITLE_CONSULT_EXP))//咨询费支出
        {
        	if (dorC==Const.DorC.D.toString()){
            	BigDecimal sum = led.getConsultExp().add(amount);
            	led.setConsultExp(sum);
            }else{
            	BigDecimal sum = led.getConsultExp().subtract(amount);
            	led.setConsultExp(sum);
            }
        }
        else if(acctTitle.equals(Const.ACCOUNT_TITLE_APPRAISAL_EXP))//评估费支出
        {
        	if (dorC==Const.DorC.D.toString()){
            	BigDecimal sum = led.getAppraisalExp().add(amount);
            	led.setAppraisalExp(sum);
            }else{
            	BigDecimal sum = led.getAppraisalExp().subtract(amount);
            	led.setAppraisalExp(sum);
            }
        }
        else if(acctTitle.equals(Const.ACCOUNT_TITLE_MANAGE_EXP) || acctTitle.equals(Const.ACCOUNT_TITLE_MANAGEC_EXP))//管理费支出 || 丙方管理费支出
        {
        	if (dorC==Const.DorC.D.toString()){
            	BigDecimal sum = led.getManageExp().add(amount);
            	led.setManageExp(sum);
            }else{
            	BigDecimal sum = led.getManageExp().subtract(amount);
            	led.setManageExp(sum);
            }
        }
        else if(acctTitle.equals(Const.ACCOUNT_TITLE_PENALTY_EXP))//违约金支出
        {
        	if (dorC==Const.DorC.D.toString()){
            	BigDecimal sum = led.getPenaltyExp().add(amount);
            	led.setPenaltyExp(sum);
            }else{
            	BigDecimal sum = led.getPenaltyExp().subtract(amount);
            	led.setPenaltyExp(sum);
            }
        }
        else if(acctTitle.equals(Const.ACCOUNT_TITLE_OTHER_EXP))//其他营业支出
        {
        	if (dorC==Const.DorC.D.toString()){
            	BigDecimal sum = led.getOtherExp().add(amount);
            	led.setOtherExp(sum);
            }else{
            	BigDecimal sum = led.getOtherExp().subtract(amount);
            	led.setOtherExp(sum);
            }
        }
        else if(acctTitle.equals(Const.ACCOUNT_TITLE_NONOPERAT_EXP))//营业外支出
        {
        	if (dorC==Const.DorC.D.toString()){
            	BigDecimal sum = led.getNonOperatExp().add(amount);
            	led.setNonOperatExp(sum);
            }else{
            	BigDecimal sum = led.getNonOperatExp().subtract(amount);
            	led.setNonOperatExp(sum);
            }
        }
        else if(acctTitle.equals(Const.ACCOUNT_TITLE_OTHER_PAYABLE))//其他应付款
        {
        	if (dorC==Const.DorC.D.toString()){
            	BigDecimal sum = led.getOtherPayable().subtract(amount);
            	led.setOtherPayable(sum);
            }else{
            	BigDecimal sum = led.getOtherPayable().add(amount);
            	led.setOtherPayable(sum);
            }
        }
        else if (acctTitle.equals(Const.ACCOUNT_TITLE_RISK_INCOME))//风险金收入
        {
        	if (dorC==Const.DorC.D.toString()){
            	BigDecimal sum = led.getOtherIncome().add(amount);
            	led.setOtherIncome(sum);
            }else{
            	BigDecimal sum = led.getOtherIncome().subtract(amount);
            	led.setOtherIncome(sum);
            }
        }
        else if(acctTitle.equals(Const.ACCOUNT_TITLE_RISK_EXP))//风险金支出
        {
        	if (dorC==Const.DorC.D.toString()){
            	BigDecimal sum = led.getOtherExp().add(amount);
            	led.setOtherExp(sum);
            }else{
            	BigDecimal sum = led.getOtherExp().subtract(amount);
            	led.setOtherExp(sum);
            }
        }

        loanLedgerServiceImpl.update(led);
        return true;
    }
	
	private void RepayToFlow(OfferRepayInfo repay,OfferFlow flow)
    {
        flow.setAccount(repay.getLoanId().toString());
        flow.setLoanId(repay.getLoanId());
        flow.setTradeKind(repay.getTradeKind());
        flow.setTradeCode(repay.getTradeCode());
        flow.setTradeType(repay.getTradeType());
        flow.setTradeNo(repay.getTradeNo());
        flow.setTradeDate(repay.getTradeDate());
        flow.setTeller(repay.getTeller());
        flow.setDelegationTeller(repay.getDelegationTeller());
        flow.setOrgan(repay.getOrgan());
        flow.setMemo(repay.getMemo());
        flow.setReversedNo(repay.getReversedNo());
        flow.setVoucherCode(repay.getVoucherCode());
        flow.setVoucherKind(repay.getVoucherKind());
    }
	
	/*判断帐号是否为内部帐号
    True： 内部帐号
    False：不是内部帐号*/
    private boolean IsInternalAcct(String Account)
    {
        if(Account.length()>10 && Account.substring(0, 1)=="ZD"){
            return  true;
        }
        else if(Account.length()>10 && Account.substring(0, 1)=="JM"){
            return  true;
        }
        else{
            return false;
        }
    }

	

	/**
	 * 发送消息
	 * @param loanInfo
	 * @param string
	 */
    @Transactional(propagation=Propagation.REQUIRED)
	private void passMessage(VLoanInfo loanInfo, String title) {
		SysDictionary sysDictionary = new SysDictionary();
		sysDictionary.setCodeType("message_receiver");
		List<SysDictionary> list = sysDictionaryService.findListByVo(sysDictionary);
		
//		List<String> list = DictionaryUtils.getList("message_receiver");
        if(list.size()>0){
            String[] receivers = list.get(0).getCodeTitle().split(",");
            for(String s : receivers){
                BaseMessage message = new BaseMessage();
                message.setSendTime(new Date());
                message.setSender(comEmployeeService.findByUserCode("admin").getId()); 
                message.setState("1");//未读
                message.setType("0");
                PersonInfo personInfo = personInfoService.findById(loanInfo.getBorrowerId());
                message.setContent("客户"+personInfo.getName()+"，身份证号："+personInfo.getIdnum()+"，债权编号："+loanInfo.getId()+
                		",批次号："+(Strings.isEmpty(loanInfo.getBatchNum())?"无":loanInfo.getBatchNum()));
                message.setTitle(title);
                message.setReceiver(comEmployeeService.findByUserCode(s).getId());
                baseMessageService.inserBaseMessage(message);
            }
        }
		
	}

	/**
	 * 发送数据给fortune
	 * @param loanInfo
	 * @param repay
	 */
    @Transactional(propagation=Propagation.REQUIRED)
	private void passDataToFortune(VLoanInfo loanInfo, OfferRepayInfo repay) {
		Date nextRepayDate = ToolUtils.getNextRepayDateIncludeToday(repay.getTradeDate());
		DateTime dateTime = new DateTime(nextRepayDate);
        if(dateTime.getDayOfMonth() != loanInfo.getPromiseReturnDate()){
            nextRepayDate = ToolUtils.getNextRepayDate(nextRepayDate);
        }
        fortuneSurportService.createFortuneSurport(loanInfo,nextRepayDate);
        
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Map<String ,Object> penaltyDeal(OfferRepayInfo repay, BigDecimal fineAmount, BigDecimal countAMT,
			BigDecimal relief, List<LoanRepaymentDetail> overDateList, OfferFlow reliefflow) {
		Map<String ,Object> map = new HashMap<String ,Object>();//返回容容器
		map.put("countAMT", countAMT);
		
		if(countAMT.compareTo(fineAmount)>=0) //已交金额>罚息
        {
        	for(LoanRepaymentDetail repaymentDetail:overDateList){
        		if(repaymentDetail.getReturnDate().before(repay.getTradeDate()))
                {
        			repaymentDetail.setPenaltyDate(repay.getTradeDate());
        			repaymentDetail.setRepaymentState(RepaymentStateEnum.未还款.name());
        			loanRepaymentDetailServiceImpl.update(repaymentDetail);
                }
        	}
          
            //记账
            if (!Accounting(FlowBuild(repay,fineAmount,Const.ACCOUNT_TITLE_FINE_EXP,overDateList.get(0).getCurrentTerm().toString(),"D","C")))//记账出错时
            {
            	throw new PlatformException("逾期罚息记账处理失败，交易未完成！");
            }
            
            countAMT=countAMT.subtract(fineAmount);
            map.put("countAMT", countAMT);
        }
        else
        {
            //记录已缴罚息，更新还款计划表（计算可缴几天的罚息）没有这一步
        	for(LoanRepaymentDetail repaymentDetail:overDateList){
        		 if(repaymentDetail.getReturnDate().before(repay.getTradeDate()))
                 {
        			 repaymentDetail.setRepaymentState(RepaymentStateEnum.不足罚息.name());
        			 loanRepaymentDetailServiceImpl.update(repaymentDetail);
                 }
        	}

            //存在减免罚息时,撤销罚息减免流水
            if (relief.compareTo(new BigDecimal("0.01"))>=0)
            {
            	//收益账户
                LoanLedger led=loanLedgerServiceImpl.findByAccountForUpdate(Const.ACCOUNT_GAINS);
                if (led !=null){
                	BigDecimal sum = led.getFineExp().subtract(relief);
                    led.setFineExp(sum);
                    loanLedgerServiceImpl.update(led);
                }
                //债权
                LoanLedger ledloan = loanLedgerServiceImpl.findByLoanIdForUpdate(repay.getLoanId());
                if(ledloan!=null){
                	ledloan.setFineIncome(ledloan.getFineIncome().subtract(relief));//lrFineIncome-=relief
                	loanLedgerServiceImpl.update(ledloan);
                }
                if (reliefflow!=null){
                	//存在减免罚息时,撤销罚息减免流水
                	offerFlowDao.deleteById(reliefflow.getId());
                }
                countAMT=countAMT.subtract(relief);
                map.put("countAMT", countAMT);
                repay.getReductionSpecialRepayment().setSpecialRepaymentState(SpecialRepaymentStateEnum.取消.getValue());
                loanSpecialRepaymentServiceImpl.update(repay.getReductionSpecialRepayment());
                //罚息不足 已生效的减免置为通过
                specialRepaymentApplyService.releifNotEnoughFine(repay.getReductionSpecialRepayment().getId());
//                SpecialRepayment.executeUpdate("update SpecialRepayment s set s.specialRepaymentState=:state where s.loan.id=:loanid and s.specialRepaymentType=:type and s.requestDate=:date1 and specialRepaymentState=:state1 ",[state: SpecialRepaymentState.申请,loanid:loanInfo.id,type: SpecialRepaymentType.减免,date1:repay.riTradeDate,state1: SpecialRepaymentState.结束])
            }

            //罚息不够时挂账
            if (!Accounting(FlowBuild(repay,countAMT,Const.ACCOUNT_TITLE_AMOUNT,"","D","D"))){
            	//罚息不够时挂账
            	throw new PlatformException("罚息不够挂账处理失败，交易未完成！");
            }
            map.put("type", 1);//返回不再处理后面的
            return map;
        }
		map.put("type", 0);
		return map;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public BigDecimal overdueRepaymentDeal(OfferRepayInfo repay,LoanRepaymentDetail para,
			VLoanInfo loanInfo, BigDecimal countAMT) {
		
		para.setRepaymentState(RepaymentStateEnum.结清.name());//更新状态   Loan 中的状态有可能需要更新，最一期逾期的情况未考虑
        para.setFactReturnDate(repay.getTradeDate());
        para.setBackTime(Dates.format(new Date(),Dates.DATAFORMAT_SLASHDATE));
        if (para.getCurrentTerm() !=loanInfo.getTime()){
        	LoanProduct loanProduct = loanProductDao.findByLoanId(loanInfo.getId());
        	loanProduct.setResidualPactMoney(para.getPrincipalBalance());
        	loanProduct.setUpdateTime(new Date());
        	loanProductDao.update(loanProduct);
//        	zdsys.Loan.executeUpdate("update Loan set residualPactMoney=:shamount,residualTime=residualTime-1 where id=:loanid ",[shamount:para.principalBalance,loanid:loanInfo.id]);
            
        }else{
        	UpdateLoanStateEnd(para.getCurrentTerm(),loanInfo.getId());//最后一期 更新贷款表中状态
        }
            

        //记账
        BigDecimal amount = para.getDeficit().subtract(para.getReturneterm().subtract(para.getCurrentAccrual()));
        if (amount.compareTo(BigDecimal.ZERO)>0)//利息未还时
        {
            if (!Accounting(FlowBuild(repay,amount,Const.ACCOUNT_TITLE_INTEREST_EXP,para.getCurrentTerm().toString() ,"D","C"))){
            	//利息流水
            	throw new PlatformException("逾期利息记账处理失败，交易未完成！");
            }
        } else{
        	 amount = BigDecimal.ZERO;
        }
        
        if (!Accounting(FlowBuild(repay,para.getDeficit().subtract(amount),Const.ACCOUNT_TITLE_LOAN_AMOUNT,para.getCurrentTerm().toString(),"D","C"))){
        	//本金流水
        	throw new PlatformException("逾期本金记账处理失败，交易未完成！");
        }

        countAMT=countAMT.subtract(para.getDeficit());
        para.setDeficit(BigDecimal.ZERO);
        loanRepaymentDetailServiceImpl.update(para);
		return countAMT;
	}

	/**
	 * 还款结束时、更新贷款表中状态
	 * @param currentTerm
	 * @param loanId
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	private void UpdateLoanStateEnd(Long currentTerm, Long loanId) {
		loanBaseDao.updateLoanState(loanId, LoanStateEnum.结清.getValue());
		
		LoanProduct loanProduct = loanProductDao.findByLoanId(loanId);
		loanProduct.setResidualPactMoney(BigDecimal.ZERO);
		loanProduct.setTime(currentTerm);
		loanProduct.setUpdateTime(new Date());
		loanProductDao.update(loanProduct);
//		 zdsys.Loan.executeUpdate("update Loan set loanState=:state,residualPactMoney=0,residualTime=0 where id=:loanid and time=:cTerm",[state:zdsys.LoanState.结清, loanid:lid,cTerm:currTerm]);
		  
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void overdueInsufficientRepaymentDeal(OfferRepayInfo repay,
			LoanRepaymentDetail para, VLoanInfo loanInfo, BigDecimal countAMT) {
		para.setRepaymentState(RepaymentStateEnum.不足额还款.name()); //
        //记账 更新，退出
		BigDecimal amount = para.getDeficit().subtract(para.getReturneterm().subtract(para.getCurrentAccrual()));//
        if(amount.compareTo(BigDecimal.ZERO)>0)
        {
           if(countAMT.compareTo(amount)<0){
        	   amount = countAMT;
           }
        }
        else{
        	 amount = BigDecimal.ZERO;
        }
           
        if (!Accounting(FlowBuild(repay,amount,Const.ACCOUNT_TITLE_INTEREST_EXP,para.getCurrentTerm().toString() ,"D","C"))){
        	//利息流水
        	 throw new PlatformException("逾期利息记账处理失败，交易未完成！");
        }
           
        if (!Accounting(FlowBuild(repay,countAMT.subtract(amount),Const.ACCOUNT_TITLE_LOAN_AMOUNT,para.getCurrentTerm().toString() ,"D","C" ))){
        	//本金流水
        	throw new PlatformException("逾期本金记账处理失败，交易未完成！");
        }
        
        LoanProduct loanProduct = loanProductDao.findByLoanId(loanInfo.getId());
    	loanProduct.setResidualPactMoney(loanProduct.getResidualPactMoney().subtract(countAMT.subtract(amount)));
    	loanProductDao.update(loanProduct);  
    	
//        zdsys.Loan.executeUpdate("update Loan set residualPactMoney=residualPactMoney-:shamount where id=:loanid ",[shamount:countAMT-amount,loanid:loanInfo.id]);
        para.setDeficit(para.getDeficit().subtract(countAMT));;//只还一部分
        loanRepaymentDetailServiceImpl.update(para);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public BigDecimal normalRepaymentDeal(OfferRepayInfo repay,VLoanInfo loanInfo,
			LoanRepaymentDetail lastRep, BigDecimal countAMT) {
	    /** 交易日期必须【早于等于】当期还款日期*/
		if (lastRep.getReturnDate().compareTo(repay.getTradeDate()) >= 0)
        {
            if (countAMT.compareTo(lastRep.getDeficit()) >= 0)
            {
                //注意这里有可能最后一期结清的可能
            	lastRep.setRepaymentState(RepaymentStateEnum.结清.name());
            	lastRep.setFactReturnDate(repay.getTradeDate());
            	lastRep.setBackTime(Dates.format(new Date(),Dates.DATAFORMAT_SLASHDATE));//回盘日期 取当天
                if (lastRep.getCurrentTerm() != loanInfo.getTime()){
                	LoanProduct loanProduct = loanProductDao.findByLoanId(loanInfo.getId());
                	loanProduct.setResidualPactMoney(lastRep.getPrincipalBalance());
                	loanProduct.setUpdateTime(new Date());
                	loanProductDao.update(loanProduct);  
                }
                else{
                	UpdateLoanStateEnd(lastRep.getCurrentTerm(),loanInfo.getId());//最后一期 更新贷款表中状态
                }
                     
                //记账
                BigDecimal amount = lastRep.getDeficit().subtract((lastRep.getReturneterm().subtract(lastRep.getCurrentAccrual())));//本次还的利息
                if (amount.compareTo(BigDecimal.ZERO) > 0)
                {
                    if (!Accounting(FlowBuild(repay,amount,Const.ACCOUNT_TITLE_INTEREST_EXP,lastRep.getCurrentTerm().toString(),"D","C" ))){
                    	//利息流水
                    	throw new PlatformException("正常还款利息记账处理失败，交易未完成！");
                    }
                }
                else{
                	amount = BigDecimal.ZERO;
                }
                    
                if (!Accounting(FlowBuild(repay,lastRep.getDeficit().subtract(amount),Const.ACCOUNT_TITLE_LOAN_AMOUNT,lastRep.getCurrentTerm().toString(),"D","C" ))){
                	//本金流水
                	throw new PlatformException("正常还款本金记账处理失败，交易未完成！");
                }

                countAMT = countAMT.subtract(lastRep.getDeficit());
                if (!Accounting(FlowBuild(repay,countAMT,Const.ACCOUNT_TITLE_AMOUNT,"","D","D" ))){
                	//多余挂账
                	throw new PlatformException("正常还款后剩余现金挂账处理失败，交易未完成！");
                }
                lastRep.setDeficit(BigDecimal.ZERO);
            }
            else //正常还款中 部分还款
            {
                if(countAMT.compareTo(new BigDecimal("0.01"))<0){
                	return countAMT;
                }
                    
                //将countAMT 还掉一部分 //更新还款计划表
                lastRep.setRepaymentState(RepaymentStateEnum.不足额还款.name());
                //记账 更新，退出
                BigDecimal amount = lastRep.getDeficit().subtract((lastRep.getReturneterm().subtract(lastRep.getCurrentAccrual())));
                if(amount.compareTo(BigDecimal.ZERO)>0)
                {
                    if(countAMT.compareTo(amount)<0){
                    	amount = countAMT;
                    }
                }
                else{
                	 amount = BigDecimal.ZERO;
                }
                   
                if (!Accounting(FlowBuild(repay,amount,Const.ACCOUNT_TITLE_INTEREST_EXP,lastRep.getCurrentTerm().toString(),"D","C" ))){
                	//利息流水
                	 throw new PlatformException("正常还款利息记账处理失败，交易未完成！");
                }
                   
                if((countAMT.compareTo(amount))>0)
                {
                    if (!Accounting(FlowBuild(repay,countAMT.subtract(amount),Const.ACCOUNT_TITLE_LOAN_AMOUNT,lastRep.getCurrentTerm().toString(),"D","C" ))){
                    	//本金流水
                    	throw new PlatformException("正常还款本金记账处理失败，交易未完成！");
                    }
                    LoanProduct loanProduct = loanProductDao.findByLoanId(loanInfo.getId());
                	loanProduct.setResidualPactMoney(loanProduct.getResidualPactMoney().subtract(countAMT.subtract(amount)));
                	loanProduct.setUpdateTime(new Date());
                	loanProductDao.update(loanProduct);  
                }
                lastRep.setDeficit(lastRep.getDeficit().subtract(countAMT));
                // 剩余金额清零
                countAMT = new BigDecimal("0");
            }
            loanRepaymentDetailServiceImpl.update(lastRep);
            //存在申请减免生效状态置为完成
            specialRepaymentApplyService.reliefRepayDealFinish(repay);
        }
		return countAMT;
	}

	@Override
	public void studentBondRepayment(OfferRepayInfo repay, VLoanInfo loanInfo) {
		if (repay.getAmount().compareTo(new BigDecimal("0.01"))<0)  //还款额小于1分钱时
        {
        	throw new PlatformException("学生还保证金,金额不足0.01，交易未完成！");
        }
       //这里可以做校验   ,后期可能会增加 还多少保证金，目前会造成实际还的保证金>应还
       OfferFlow flowInstance=FlowBuild(repay,repay.getAmount(),Const.ACCOUNT_TITLE_OTHER_PAYABLE,"","D","D");
       
       ZhuxueOrganization zhuxueOrganization =  zhuxueOrganizationService.findByPlanId(loanInfo.getPlanId());
       
       flowInstance.setAppoAcct(zhuxueOrganization.getId().toString());
       flowInstance.setAppoAcctTitle(Const.ACCOUNT_TITLE_AMOUNT);//现金
       flowInstance.setEndAmount(afterLoanService.getAccAmount(repay.getLoanId()));
       
       if(!Accounting(flowInstance)){
    	   throw new PlatformException("学生还保证金记账失败，交易未完成！");
       }
		
	}

	/**
	 * 助学贷机构总收入
	 * @param param
	 * @return
	 */
	@Override
	public BigDecimal getZhuxueOrganizationTotalIncome(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return offerFlowDao.getZhuxueOrganizationTotalIncome(param);
	}

	/**
	 * 助学贷机构总支出
	 * @param param
	 * @return
	 */
	@Override
	public BigDecimal getZhuxueOrganizationTotalPay(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return offerFlowDao.getZhuxueOrganizationTotalPay(param);
	}
	
	/**
	 * 跟据交易流水查询凭证明细数据
	 * @param tradeNo 交易流水
	 * @return
	 */
	@Override
	public List<OfferFlow> findByTradeNo(String tradeNo) {
		List<OfferFlow> offerFlowList = new ArrayList<OfferFlow>();
		if (Strings.isNotEmpty(tradeNo)) {
			OfferFlow offerFlow = new OfferFlow();
			offerFlow.setTradeNo(tradeNo);
			offerFlowList = offerFlowDao.findListByVo(offerFlow);
		}
		return offerFlowList;
	}
	
    @Transactional(propagation = Propagation.REQUIRED)
    public int realtimeFullRepaymentDealLufax(OfferRepayInfo repay,VLoanInfo loanInfo, BigDecimal countAMT, 
            List<LoanRepaymentDetail> overDateList, BigDecimal onetimeRepaymentAmount) {
        // 更新还款计划表，贷款基本信息表
        loanRepaymentDetailServiceImpl.updateYCXJQAllStateToSettlementByLoanId(loanInfo.getId(), repay.getTradeDate());
        loanBaseDao.updateLoanState(loanInfo.getId(),LoanStateEnum.结清.getValue());
        // 更新剩余本金
        LoanProduct loanProduct = loanProductDao.findByLoanId(loanInfo.getId());
        loanProduct.setResidualPactMoney(BigDecimal.ZERO);
        loanProduct.setUpdateTime(new Date());
        loanProductDao.update(loanProduct);
        
        LoanRepaymentDetail lastRep = overDateList.get(overDateList.size() - 1);
        // 2013-09-26 添加，申请一次还款且已过最后一期还款日
        if (lastRep.getReturnDate().compareTo(repay.getTradeDate()) < 0) {
            if (!Accounting(FlowBuild(repay, countAMT,Const.ACCOUNT_TITLE_AMOUNT, "", "D", "D"))) {
                throw new PlatformException("一次性还款后剩余现金挂账处理失败，交易未完成！");
            }
            loanSpecialRepaymentServiceImpl.updateSpecialRepaymentToEnd(loanInfo.getId(), SpecialRepaymentTypeEnum.一次性还款.name(), "申请");
            return 1;
        }
        //插入结清记录表
        loanPreSettleService.creartPreSettlePromise(loanInfo,repay.getTradeDate());
        // 记账，这里是否只需还的总本金，利息，违约金拆开即可（三笔）
        BigDecimal amount = BigDecimal.ZERO;
        // 个贷
        BigDecimal temp = BigDecimal.ZERO;
        // 当期期数
        String currentTerm = lastRep.getCurrentTerm().toString();
        
        // 当期违约金 
        BigDecimal penalty = lastRep.getPenalty();
        if (penalty == null) {
            penalty = BigDecimal.ZERO;
        }
        // 违约金记账
        if (penalty.compareTo(BigDecimal.ZERO) > 0) {
            if (!Accounting(FlowBuild(repay, penalty, Const.ACCOUNT_TITLE_PENALTY_EXP, currentTerm, "D", "C"))) {
                // 违约金支出
                throw new PlatformException("违约金支出记账处理失败，交易未完成！");
            }
        }
        
        // 退咨询费
        temp = (lastRep.getGiveBackRate().multiply(loanInfo.getReferRate())
                .divide((loanInfo.getRateSum().subtract(loanInfo.getManageRateForPartyC())), 2, RoundingMode.HALF_UP))
                .setScale(2, RoundingMode.HALF_UP);
        if (!Accounting(FlowBuild(repay, temp, Const.ACCOUNT_TITLE_CONSULT_EXP, currentTerm, "C", "D"))) {
            throw new PlatformException("一次性还款退咨询费记账处理失败，交易未完成！");
        }
        amount = temp;
        
        // 退评估费
        temp = (lastRep.getGiveBackRate().multiply(loanInfo.getEvalRate())
                .divide((loanInfo.getRateSum().subtract(loanInfo.getManageRateForPartyC())), 2, RoundingMode.HALF_UP))
                .setScale(2, RoundingMode.HALF_UP);
        if (!Accounting(FlowBuild(repay, temp, Const.ACCOUNT_TITLE_APPRAISAL_EXP, currentTerm, "C", "D"))) {
            throw new PlatformException("一次性还款退评估费记账处理失败，交易未完成！");
        }
        amount = amount.add(temp);
        
        // 退乙方管理费(退费误差在此体现)
        temp = lastRep.getGiveBackRate().subtract(amount);
        if (!Accounting(FlowBuild(repay, temp, Const.ACCOUNT_TITLE_MANAGE_EXP, currentTerm, "C", "D"))) {
            throw new PlatformException("一次性还款退管理费记账处理失败，交易未完成！");
        }

        // 结清本金 = 一次性结清金额 + 退费  - 违约金 - 当期利息
        BigDecimal benji = lastRep.getRepaymentAll().add(lastRep.getGiveBackRate()).subtract(penalty).subtract(lastRep.getCurrentAccrual());
        // 当期利息
        BigDecimal lixi = lastRep.getCurrentAccrual();

        // 特殊情况，当期已部分还款后，再做一次性还款
        amount = lastRep.getReturneterm().subtract(lastRep.getDeficit());
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            lixi = lixi.subtract(amount).compareTo(BigDecimal.ZERO) > 0 ? lixi.subtract(amount) : new BigDecimal(0.00);
            benji = lastRep.getCurrentAccrual().subtract(amount).compareTo(BigDecimal.ZERO) < 0 ? benji.add(lastRep.getCurrentAccrual()).subtract(amount) : benji;
        }

        if (!Accounting(FlowBuild(repay, lixi,Const.ACCOUNT_TITLE_INTEREST_EXP, currentTerm, "D", "C"))) {
            // 利息流水
            throw new PlatformException("一次性还款利息记账处理失败，交易未完成！");
        }

        // 剩余本金+当前本金
        if (!Accounting(FlowBuild(repay, benji, Const.ACCOUNT_TITLE_LOAN_AMOUNT, currentTerm, "D", "C"))) {
            // 本金流水
            throw new PlatformException("一次性还款本金记账处理失败，交易未完成！");
        }

        // 实际还款金额大于一次性结清金额，剩余的金额挂账
        amount = countAMT.subtract(onetimeRepaymentAmount);
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            if (!Accounting(FlowBuild(repay, amount, Const.ACCOUNT_TITLE_AMOUNT, "", "D", "D"))) {
                // 多余挂账
                throw new PlatformException("一次性还款后剩余现金挂账处理失败，交易未完成！");
            }
        }

        // 还款计划表剩余金额清零
        loanRepaymentDetailServiceImpl.updateYCXJQAllDeficitToZeroByLoanId(loanInfo.getId());
        // 更新一次性还款申请到结束
        loanSpecialRepaymentServiceImpl.updateSpecialRepaymentToEnd(loanInfo.getId(), SpecialRepaymentTypeEnum.一次性还款.getValue(),null);
        //存在申请减免生效状态置为完成
        specialRepaymentApplyService.reliefRepayDealFinish(repay);
        return 0;
    }

    @Override
    public List<OfferFlow> findOverDueOfferFlow4Lufax(Map<String, Object> map) {
        return offerFlowDao.findOverDueOfferFlow4Lufax(map);
    }

	@Override
	public RepayStateDetail getRepayStateDetailRealValue(Map<String, Object> paramMap) {
		return offerFlowDao.getRepayStateDetailRealValue(paramMap);
	}
    
    /**
     * 查询某笔还款分账的当期本金
     * @param tradeNo
     * @param detail
     * @return
     */
    public BigDecimal queryNormalPrincipalAmount(String tradeNo, LoanRepaymentDetail detail) {
        BigDecimal amount = BigDecimal.ZERO;
        // 跟据交易流水查询分账明细数据
        List<OfferFlow> flowList = this.findByTradeNo(tradeNo);
        if (CollectionUtils.isEmpty(flowList)) {
            return amount;
        }
        // 当前还款期数
        Long currentTerm = detail.getCurrentTerm();
        for (OfferFlow flow : flowList) {
            if (Const.ACCOUNT_TITLE_LOAN_AMOUNT.equals(flow.getAcctTitle())) {
                if(Long.parseLong(flow.getMemo2()) == currentTerm){
                	amount = amount.add(flow.getTradeAmount());
                }
            }
        }
        return amount;
    }
    
    /**
     * 查询某笔还款分账的当期利息
     * @param tradeNo
     * @param detail
     * @return
     */
    public BigDecimal queryNormalInterestAmount(String tradeNo, LoanRepaymentDetail detail) {
        BigDecimal amount = BigDecimal.ZERO;
        // 跟据交易流水查询分账明细数据
        List<OfferFlow> flowList = this.findByTradeNo(tradeNo);
        if (CollectionUtils.isEmpty(flowList)) {
            return amount;
        }
        // 当前还款期数
        Long currentTerm = detail.getCurrentTerm();
        for (OfferFlow flow : flowList) {
            if (Const.ACCOUNT_TITLE_INTEREST_EXP.equals(flow.getAcctTitle())) {
                if(Long.parseLong(flow.getMemo2()) == currentTerm){
                	amount = amount.add(flow.getTradeAmount());
                }
            }
        }
        return amount;
    }
}
