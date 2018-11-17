package com.zdmoney.credit.loan.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.common.constant.DebitNotifyStateEnum;
import com.zdmoney.credit.common.constant.DebitOperateTypeEnum;
import com.zdmoney.credit.common.constant.DebitRepayTypeEnum;
import com.zdmoney.credit.common.constant.DebitResultStateEnum;
import com.zdmoney.credit.common.constant.FundsSourcesTypeEnum;
import com.zdmoney.credit.common.constant.LoanStateEnum;
import com.zdmoney.credit.common.constant.LoanTypeEnum;
import com.zdmoney.credit.common.constant.PayOffTypeEnum;
import com.zdmoney.credit.common.constant.PayPartyEnum;
import com.zdmoney.credit.common.constant.RepaymentStateEnum;
import com.zdmoney.credit.common.constant.SpecialRepaymentStateEnum;
import com.zdmoney.credit.common.constant.SpecialRepaymentTypeEnum;
import com.zdmoney.credit.common.constant.SplitNotifyStateEnum;
import com.zdmoney.credit.common.constant.SplitResultStateEnum;
import com.zdmoney.credit.common.constant.TradeKindEnum;
import com.zdmoney.credit.common.constant.TradeTypeEnum;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.service.pub.IWorkDayInfoService;
import com.zdmoney.credit.common.util.Assert;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.LufaxUtil;
import com.zdmoney.credit.common.util.ToolUtils;
import com.zdmoney.credit.core.calculator.factory.CalculatorFactoryImpl;
import com.zdmoney.credit.core.calculator.pub.ICalculator;
import com.zdmoney.credit.debit.domain.DebitQueueLog;
import com.zdmoney.credit.debit.domain.DebitTransaction;
import com.zdmoney.credit.debit.domain.SplitQueueLog;
import com.zdmoney.credit.debit.service.pub.IDebitQueueLogService;
import com.zdmoney.credit.debit.service.pub.ISplitQueueLogService;
import com.zdmoney.credit.fortune.service.pub.IFortuneSurportService;
import com.zdmoney.credit.ljs.dao.pub.ICompensatoryDetailLufaxDao;
import com.zdmoney.credit.ljs.domain.CompensatoryDetailLufax;
import com.zdmoney.credit.loan.dao.pub.IJimuExtRepayAllDao;
import com.zdmoney.credit.loan.dao.pub.IJimuExtRepayDao;
import com.zdmoney.credit.loan.dao.pub.ILoanBaseDao;
import com.zdmoney.credit.loan.dao.pub.ILoanProductDao;
import com.zdmoney.credit.loan.dao.pub.ILoanRepaymentDetailDao;
import com.zdmoney.credit.loan.dao.pub.IRiskAmountDetailDao;
import com.zdmoney.credit.loan.domain.JimuExtRepay;
import com.zdmoney.credit.loan.domain.JimuExtRepayAll;
import com.zdmoney.credit.loan.domain.LoanLedger;
import com.zdmoney.credit.loan.domain.LoanRepaymentDetail;
import com.zdmoney.credit.loan.domain.LoanSpecialRepayment;
import com.zdmoney.credit.loan.domain.RiskAmountDetail;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.IAfterLoanService;
import com.zdmoney.credit.loan.service.pub.ILoanLedgerService;
import com.zdmoney.credit.loan.service.pub.ILoanPreSettleService;
import com.zdmoney.credit.loan.service.pub.ILoanRepaymentDetailService;
import com.zdmoney.credit.loan.service.pub.ILoanSpecialRepaymentService;
import com.zdmoney.credit.loan.service.pub.ISpecialRepaymentApplyService;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import com.zdmoney.credit.offer.dao.pub.IOfferFlowDao;
import com.zdmoney.credit.offer.domain.OfferFlow;
import com.zdmoney.credit.offer.domain.OfferRepayInfo;
import com.zdmoney.credit.offer.domain.OfferTransaction;
import com.zdmoney.credit.offer.service.pub.IOfferFlowService;
import com.zdmoney.credit.offer.service.pub.IOfferRepayInfoService;
import com.zdmoney.credit.offer.vo.RepaymentInputVo;
import com.zdmoney.credit.system.service.pub.IBaseMessageService;
import com.zdmoney.credit.system.service.pub.IComEmployeeService;
import com.zdmoney.credit.system.service.pub.IPersonInfoService;
import com.zdmoney.credit.system.service.pub.ISequencesService;
import com.zdmoney.credit.system.service.pub.ISysDictionaryService;
import com.zdmoney.credit.system.service.pub.ISysSpecialDayService;
import com.zdmoney.credit.zhuxue.domain.ZhuxueOrganization;
import com.zdmoney.credit.zhuxue.service.pub.IZhuxueOrganizationService;

/**
 * 该类等待石俊实现
 * @author 00232949
 *
 */
@Service
@Transactional
public class AfterLoanServiceImpl implements IAfterLoanService{
	private static final Logger logger = Logger.getLogger(AfterLoanServiceImpl.class);
	
	@Autowired
	private IOfferRepayInfoService offerRepayInfoService;
	
	@Autowired 
	private IVLoanInfoService vLoanInfoServiceImpl;
	
	@Autowired 
	private ILoanRepaymentDetailService loanRepaymentDetailServiceImpl;
	
	@Autowired 
	private ILoanSpecialRepaymentService loanSpecialRepaymentServiceImpl;
	
	@Autowired
	private ILoanLedgerService loanLedgerServiceImpl;
	
	@Autowired
	private ILoanProductDao loanProductDao;
	
	@Autowired
	private ILoanBaseDao loanBaseDao;
	
	@Autowired
	private ILoanPreSettleService loanPreSettleService;
	
	@Autowired
	private IOfferFlowService offerFlowService;
	
	@Autowired
	private IOfferFlowDao offerFlowDao;
	
	@Autowired
	private IJimuExtRepayDao jimuExtRepayDao;
	
	@Autowired
	private IJimuExtRepayAllDao jimuExtRepayAllDao;
	
	@Autowired
	private ILoanRepaymentDetailDao repaymentDetailDao;
	
	@Autowired
	private ISequencesService sequencesService;
	
	@Autowired
	private IComEmployeeService comEmployeeService;
	
	@Autowired
	private IPersonInfoService personInfoService;
	
	@Autowired
	private IBaseMessageService baseMessageService;
	
	@Autowired
	private ISysDictionaryService sysDictionaryService;
	
	@Autowired
	private IFortuneSurportService fortuneSurportService;
	
	@Autowired
	private ISysSpecialDayService sysSpecialDayService;
	
	@Autowired
	private IZhuxueOrganizationService zhuxueOrganizationService;
	
	@Autowired
    private IDebitQueueLogService debitQueueLogService;

	@Autowired
	private ISpecialRepaymentApplyService specialRepaymentApplyService ;
	
	@Autowired
	private ISplitQueueLogService splitQueueLogService;
	
	@Autowired
	private IWorkDayInfoService workDayInfoService;
	@Autowired
	private IRiskAmountDetailDao riskAmountDetailDao;
	
	@Autowired
    private ICompensatoryDetailLufaxDao compensatoryDetailLufaxDao;

	public static ThreadLocal<BigDecimal> transaction = new ThreadLocal<BigDecimal>();
	
	@Override
	public BigDecimal getPerReapyAmount(Date currDate, Long loanId){
		List<LoanRepaymentDetail> repayList = getAllInterestOrLoan(currDate,loanId);
		BigDecimal perReapyAmount = new BigDecimal("0.00");
        if (repayList != null && repayList.size()>0){
        	LoanRepaymentDetail rd = getLast(repayList);
        	perReapyAmount = rd.getReturneterm();
        }
        return perReapyAmount;
	}
	
	@Override
    public BigDecimal getCurrentDeficitAmount(Date currDate, Long loanId){
        List<LoanRepaymentDetail> repayList = getAllInterestOrLoan(currDate,loanId);
        BigDecimal deficitAmount = new BigDecimal("0.00");
        if (repayList != null && repayList.size()>0){
            LoanRepaymentDetail rd = getLast(repayList);
            deficitAmount = rd.getDeficit();
        }
        return deficitAmount;
    }
	
	@Override
	public LoanRepaymentDetail getLast (Date currDate, Long loanId){
	    List<LoanRepaymentDetail> repayList = getAllInterestOrLoan(currDate,loanId);
        LoanRepaymentDetail loanRepaymentDetail = null;
        if(repayList != null && repayList.size()>0){
            int last = repayList.size() - 1;
            loanRepaymentDetail = repayList.get(last);
        }
        return loanRepaymentDetail;
    }
	
	
	@Override
	public BigDecimal getAmount(Date currDate, Long id) {
		BigDecimal result = new BigDecimal("0.00");
        List<LoanRepaymentDetail> list = getAllInterestOrLoan(currDate,id);//所有未还款的记录
        
        VLoanInfo vLoanInfo = vLoanInfoServiceImpl.findByLoanId(id);
        Assert.notNull(vLoanInfo, ResponseEnum.FULL_MSG, "未找到债权数据loanId:" + id);
        
        /** 获取计算器实例 **/
        ICalculator calculatorInstace = CalculatorFactoryImpl.createCalculator(vLoanInfo);

        //一次性
        if(isOneTimeRepayment(id))
        {
        	/** 获取一次性结清金额 **/
        	BigDecimal onetimeRepaymentAmount = calculatorInstace.getOnetimeRepaymentAmount(id, currDate, list);
            result = onetimeRepaymentAmount
            		.add(getFine(list,currDate))
            		.add(getOverdueAmount(list,currDate))
            		.subtract(getAccAmount(id));
        }
        else//正常
        {
        	if(FundsSourcesTypeEnum.包商银行.getValue().equals(vLoanInfo.getLoanBelong())){
        		result = getFine(list,currDate).add(sumDeficit2(list,currDate).subtract(getAccAmount(id)));
        	}else{
        		result = getFine(list,currDate)
        				.add(sumDeficit(list,currDate))
        				.subtract(getAccAmount(id));
        	}
        }
        //获取减免罚息
        if(result.compareTo(new BigDecimal("0.01")) > -1)
        {
        	BigDecimal relie = getReliefOfFine(currDate,id);
            if (relie.compareTo(new BigDecimal("0.01")) > -1){
                result = result.subtract(relie);
            }
        }
        result = result.compareTo(new BigDecimal("0.01")) == -1?new BigDecimal("0.00"):result;
        return  result;
	}

	@Override
	public BigDecimal getOverdueAmount(List<LoanRepaymentDetail> repayList, Date currDate) {
		BigDecimal result = new BigDecimal("0.00");
        if (repayList != null && repayList.size()>0){
        	for(LoanRepaymentDetail it : repayList){
        		if (it.getReturnDate().compareTo(currDate) == -1) {
        			result = result.add(it.getDeficit());
        		}
        	}
        }
        return  result;
	}

    @Override
    public BigDecimal getFine(List<LoanRepaymentDetail> repayList, Date currDate) {
        BigDecimal result = new BigDecimal("0.00");
        if (CollectionUtils.isEmpty(repayList)) {
            return result;
        }
        int overdueDay = getOverdueDay(repayList, currDate);
        LoanRepaymentDetail loanRepaymentDetail = getLast(repayList);
        VLoanInfo vLoanInfo = vLoanInfoServiceImpl.findByLoanId(loanRepaymentDetail.getLoanId());
        // 合同来源
        String fundsSource = vLoanInfo.getFundsSources();
        if (FundsSourcesTypeEnum.包商银行.getValue().equals(fundsSource) || FundsSourcesTypeEnum.陆金所.getValue().equals(fundsSource)) {
            for(LoanRepaymentDetail detail : repayList){
                result = result.add(this.getFine(detail, currDate, vLoanInfo, null));
            }
        } else {
            if (overdueDay > 0) {
                BigDecimal penaltyRate = vLoanInfo.getPenaltyRate();
                if (penaltyRate.equals(Const.PENALTY_FINE_RATE)) { // 早期的罚息计算方式
                    result = getRemnant(repayList).multiply(new BigDecimal(overdueDay)).multiply(Const.PENALTY_FINE_RATE);
                } else {// 新版罚息计算方式
                    result = vLoanInfo.getResidualPactMoney().multiply(new BigDecimal(overdueDay)).multiply(penaltyRate);
                }
            }
        }
        // 四舍五入
        result = result.setScale(2, RoundingMode.HALF_UP);
        return result;
    }

	@Override
	public BigDecimal getCurrAmount(List<LoanRepaymentDetail> repayList, Date currDate) {
		BigDecimal result = new BigDecimal("0.00");
        if (repayList!=null && repayList.size()>0){
        	LoanRepaymentDetail rd = getLast(repayList);
        	if(rd.getReturnDate().compareTo(currDate) >= 0){
        		result = rd.getDeficit();
        	}
        }
        return  result;
	}
	
	@Override
    public BigDecimal getRemnant(List<LoanRepaymentDetail> repayList) {
        BigDecimal result = new BigDecimal("0.00");
        if (repayList!= null && repayList.size()>0){
        	long id = repayList.get(0).getLoanId();
        	Map<String,Object> map = new HashMap<String,Object>();
        	map.put("loanId", id);
        	map.put("state1", RepaymentStateEnum.结清);
        	result = loanRepaymentDetailServiceImpl.findByLoanIdAndNotRepaymentStateInSum(map);
        	
        }
        return  result;
    }

	@Override
	public List<LoanRepaymentDetail> getAllInterestOrLoan(Date currDate, Long id) {
		Map<String,Object> map = new HashMap<String, Object>();
		VLoanInfo vLoanInfo = vLoanInfoServiceImpl.findByLoanId(id);
		Date CurrTermReturnDate = getCurrTermReturnDate(currDate,vLoanInfo.getPromiseReturnDate());
		map.put("CurrTermReturnDate", CurrTermReturnDate);
		map.put("loanId", id);
		map.put("state1", RepaymentStateEnum.未还款.name());
		map.put("state2", RepaymentStateEnum.不足额还款.name());
		map.put("state3", RepaymentStateEnum.不足罚息.name());
		
		List<LoanRepaymentDetail> list = loanRepaymentDetailServiceImpl.findByLoanIdAndRepaymentState(map);
		//债权去向为陆金所，并且当期已经结清分账过
		if(FundsSourcesTypeEnum.陆金所.getValue().equals(vLoanInfo.getLoanBelong()) && CollectionUtils.isEmpty(list)){
		    LoanRepaymentDetail loanRepaymentDetail = loanRepaymentDetailServiceImpl.findByLoanAndReturnDate(vLoanInfo, CurrTermReturnDate);
		    if(null != loanRepaymentDetail){
		    	list = Arrays.asList(loanRepaymentDetail);
		    }
		}
        return list;
	}
	
	@Override
	public boolean isAdvanceRepayment(Long loanId) {
        LoanSpecialRepayment loanSpecialRepayment = loanSpecialRepaymentServiceImpl.findbyLoanAndType(loanId, SpecialRepaymentTypeEnum.提前扣款.name(), SpecialRepaymentStateEnum.申请.name());
        return loanSpecialRepayment != null?true:false;
    }

	@Override
	public boolean isOneTimeRepayment(Long id) {
		LoanSpecialRepayment loanSpecialRepayment = loanSpecialRepaymentServiceImpl.findbyLoanAndType(id, SpecialRepaymentTypeEnum.一次性还款.name(), SpecialRepaymentStateEnum.申请.name());
		return loanSpecialRepayment != null?true:false;
	}

	@Override
	public BigDecimal getAccAmount(Long id) {
		BigDecimal result = new BigDecimal("0.00");
        if(id != null && id >0){
            LoanLedger info = loanLedgerServiceImpl.findByLoanId(id);
            result = info != null?info.getAmount():result;
        }
        return result;
	}

	@Override
	public BigDecimal getReliefOfFine(Date currDate, Long id) {
		
        return getReliefOfFine(currDate,id,null);
	}
	
	/**
	 * 获取申请减免罚息金额,并在OfferRepayInfo中记录特殊还款对象
	 * @param currDate
	 * @param id
	 * @param repay
	 * @return
	 */
	private BigDecimal getReliefOfFine(Date currDate, Long id, OfferRepayInfo repay) {
		
		Map<String,Object> map = new HashMap<String,Object>();
		BigDecimal result = new BigDecimal("0.00");
        if(currDate == null || id == null){
            return  result;
        }
        map.put("loanId", id);
        map.put("currDate", currDate);
        map.put("state",SpecialRepaymentStateEnum.申请.name());
        map.put("types", new String[]{SpecialRepaymentTypeEnum.减免.name(),SpecialRepaymentTypeEnum.正常费用减免.name()});
        LoanSpecialRepayment loanSpecialRepayment = loanSpecialRepaymentServiceImpl.findByLoanIdAndDateAndTypeAndState(map);
        if(loanSpecialRepayment != null){
        	result = loanSpecialRepayment.getAmount();
        	if(repay != null){
        		repay.setReductionSpecialRepayment(loanSpecialRepayment);
        	}
        }
        
        return result;
	}
	
	@Override
	public Date getCurrTermReturnDate(Date currDate, Long promiseReturnDate) {
		Calendar c = Calendar.getInstance();
		c.setTime(currDate);
        if (c.get(Calendar.DATE)>promiseReturnDate){
        	c.add(Calendar.MONTH, 1);
        	c.set(Calendar.DATE, promiseReturnDate.intValue());
        }else{
        	c.set(Calendar.DATE, promiseReturnDate.intValue());
        }
		return c.getTime();
	}

	@Override
	public BigDecimal getOnetimeRepaymentAmountTmp(List<LoanRepaymentDetail> repayList, Date currDate) {
		BigDecimal bigDecimal = new BigDecimal("0.00");
		if(repayList != null && repayList.size()>0){
			LoanRepaymentDetail loanRepaymentDetail = getLast(repayList);
			boolean isBf = loanRepaymentDetail.getReturnDate().compareTo(currDate) >= 0;
		    if (isBf){
		    	BigDecimal bd = loanRepaymentDetail.getRepaymentAll().subtract(loanRepaymentDetail.getReturneterm()).add(loanRepaymentDetail.getDeficit());
	    		bigDecimal = bd.compareTo(new BigDecimal("0.00")) >-1?bd:new BigDecimal("0.00");
		    	
		    }
		}
		return bigDecimal;
    }

	@Override
	public BigDecimal sumDeficit(List<LoanRepaymentDetail> repayList, Date currDate) {
		BigDecimal result = new BigDecimal("0.00");
        if (repayList != null && repayList.size()>0){
        	for(LoanRepaymentDetail item : repayList){
        		if(item.getReturnDate().compareTo(currDate) <= 0){
        			result = result.add(item.getDeficit());
        		}
        	}
        }
        return  result;
	}
	
	public BigDecimal sumDeficit2(List<LoanRepaymentDetail> repayList, Date currDate) {
		BigDecimal result = new BigDecimal("0.00");
        if (repayList != null && repayList.size()>0){
        	for(LoanRepaymentDetail item : repayList){
        		if(item.getReturnDate().compareTo(currDate)<0){//1 8  
        			result = result.add(item.getDeficit());
        		}
        	}
        }
        return  result;
	}

	@Override
	public int getOverdueDay(List<LoanRepaymentDetail> repayList, Date currDate) {
		//判断当期之前是否存在逾期
        int result = 0;
        if (repayList!=null && repayList.size()>0 && currDate != null && repayList.get(0).getPenaltyDate() != null
					&& repayList.get(0).getPenaltyDate().compareTo(currDate) == -1){
        	result = Dates.dateDiff(currDate, repayList.get(0).getPenaltyDate());
        }
        return result<0?0:result;
	}
	
	private LoanRepaymentDetail getLast (List<LoanRepaymentDetail> repayList){
		LoanRepaymentDetail loanRepaymentDetail = null;
		if(repayList != null && repayList.size()>0){
			int last = repayList.size() - 1;
			loanRepaymentDetail = repayList.get(last);
		}
		return loanRepaymentDetail;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void accountingByTransaction(OfferTransaction offerTransaction) {
		RepaymentInputVo repaymentInputVo = offerRepayInfoService.builderRepaymentInputVo(offerTransaction);
		offerRepayInfoService.dealRepaymentInput(repaymentInputVo);
	}



	/**
	 * 还风险金（保证金）流水<br>
     * currentAmount 当前金额指 去除罚息后还剩的金额（包含减免金额）
	 * @param loanInfo
	 * @param repay
	 * @param countAMT
	 * @return
	 */
	private boolean repayRiskFee(VLoanInfo loan, OfferRepayInfo repayInfo, BigDecimal currentAmount) {
		 if(loan.getFundsSources().equals("积木盒子")){
	            return jimuRepay(repayInfo,currentAmount,loan);
	        } else{
	            return repayRiskFee(repayInfo,currentAmount,loan);
	        }
	}

	private boolean jimuRepay(OfferRepayInfo repayInfo, BigDecimal currentAmount,VLoanInfo loanInfo) {
		 if (currentAmount.compareTo(new BigDecimal("0.01")) < 0){
			 return  true; 
		 }
		 LoanLedger led = loanLedgerServiceImpl.findByLoanId(repayInfo.getLoanId());
        if (led.getOtherPayable().compareTo(new BigDecimal("0.01")) < 0){
        	//应还金额<0 没有逾期或当期已还 //通常应该不会进入这个if分支
        	return  true;
        }
            
        BigDecimal riskFee = currentAmount.compareTo(led.getOtherPayable())>0?led.getOtherPayable():currentAmount; //获取应还风险金
        riskFee = riskFee.setScale(2,RoundingMode.HALF_UP);

        // led.lrOtherPayable中包含非当期的风险金垫付和当期的风险金垫付，
        // 非当期的更早的积木风险金垫付，需要还入证大风险金，

        // 还入证大风险金的优先级更高
        // 非当期的更早的积木风险金垫付，需要还入证大风险金，
        Date lastRepayDate = ToolUtils.getLastRepayDateIncludeToday(repayInfo.getTradeDate(), loanInfo.getPromiseReturnDate().intValue());
        LoanRepaymentDetail rd = loanRepaymentDetailServiceImpl.findByLoanAndReturnDate(loanInfo,lastRepayDate);
//        RepaymentDetail rd = RepaymentDetail.findByLoanAndReturnDate(loanInfo, lastRepayDate);
        if(rd == null){
        	throw new RuntimeException("积木盒子还款记账失败，未得到还款计划，交易未完成！loanId="+loanInfo.getId());
        }
        BigDecimal lastRepayDeficit = rd.getDeficit();
        BigDecimal overdueRisk = led.getOtherPayable().subtract(lastRepayDeficit) ;

        if(overdueRisk.compareTo(new BigDecimal("0.01")) > 0){
            BigDecimal overdueRiskPayAmount = currentAmount.compareTo(overdueRisk) > 0 ? overdueRisk : currentAmount;
            currentAmount = currentAmount.subtract(overdueRiskPayAmount); 

            OfferFlow flowInstance = buildFlowJimuRepayRisk(repayInfo,overdueRiskPayAmount);
            if(!Accounting(flowInstance))
                throw new RuntimeException("积木盒子还款记账失败，交易未完成！loanId="+loanInfo.getId());
        }

        if(currentAmount.compareTo(new BigDecimal("0.01")) < 0){
        	return  true;
        }
            
        BigDecimal lastRepayTermAmount = currentAmount.compareTo(lastRepayDeficit) > 0 ? lastRepayDeficit : currentAmount ;

        // 针对当期的还款，符合时间条件的部分，应该还入积木还款账户
        //t+0之后的当期还款，直接进入证大风险金
        OfferFlow flowInstance;

        if(repayInfo.getTradeDate().compareTo(lastRepayDate) == 0){
            flowInstance = buildFlowJimuRepayRepay(repayInfo,lastRepayTermAmount);//当期的还款
        } else {
            flowInstance = buildFlowJimuRepayRisk(repayInfo,lastRepayTermAmount);
        }

        if(!Accounting(flowInstance))
            throw new RuntimeException("积木盒子还款记账失败，交易未完成！loanId="+loanInfo.getId());

        return  true;
	}

	private OfferFlow buildFlowJimuRepayRepay(OfferRepayInfo repayInfo,
			BigDecimal lastRepayTermAmount) {
		OfferFlow flowInstance = null;
        flowInstance = FlowBuild(repayInfo,lastRepayTermAmount,Const.ACCOUNT_TITLE_OTHER_PAYABLE,"","D","C");
        flowInstance.setAppoAcct(Const.ACCOUNT_JIMUHEZIREPAY);//积木盒子账户
        flowInstance.setAppoAcctTitle(Const.ACCOUNT_TITLE_OTHER_PAYABLE);
        return flowInstance;
	}

	private OfferFlow buildFlowJimuRepayRisk(OfferRepayInfo repayInfo, BigDecimal amount){
		OfferFlow flowInstance = null;
		flowInstance = FlowBuild(repayInfo,amount,Const.ACCOUNT_TITLE_OTHER_PAYABLE,"","D","C");
        flowInstance.setAppoAcct(Const.ACCOUNT_RISK);//证大风险金账户
        flowInstance.setAppoAcctTitle(Const.ACCOUNT_TITLE_RISK_INCOME); //风险金收入
        return flowInstance;
	}

	private boolean repayRiskFee(OfferRepayInfo repayInfo, BigDecimal currentAmount,VLoanInfo loanInfo) {
/*		if (repayInfo.getAmount().compareTo(new BigDecimal("0.01")) < 0){
			 return  true;
		}*/
		LoanLedger led = loanLedgerServiceImpl.findByLoanId(repayInfo.getLoanId());
        if (led.getOtherPayable().compareTo(new BigDecimal("0.01")) < 0){
        	//应还金额<0不生成还保证金（风险金）流水
        	 return  true;
        }
           
        BigDecimal riskFee = currentAmount.compareTo(led.getOtherPayable())>0?led.getOtherPayable():currentAmount; //获取应还风险金
        riskFee = riskFee.setScale(2,RoundingMode.HALF_UP);
        
        
        OfferFlow flowInstance;
        if (loanInfo.getLoanType().equals(LoanTypeEnum.助学贷.name())) // 还保证金
        {
            flowInstance = FlowBuild(repayInfo,riskFee,Const.ACCOUNT_TITLE_OTHER_PAYABLE,"","D","D");
            ZhuxueOrganization zhuxueOrganization = zhuxueOrganizationService.findByPlanId(loanInfo.getPlanId());
            flowInstance.setAppoAcct(zhuxueOrganization.getId().toString());
            flowInstance.setAppoAcctTitle(Const.ACCOUNT_TITLE_AMOUNT);//现金
        }
        else 
        {
	    	//还风险金
	        flowInstance = FlowBuild(repayInfo,riskFee,Const.ACCOUNT_TITLE_OTHER_PAYABLE,"","D","C");
	        flowInstance.setAppoAcct(Const.ACCOUNT_RISK);//风险金账户
	        flowInstance.setAppoAcctTitle(Const.ACCOUNT_TITLE_RISK_INCOME); //风险金收入
        }
        if(!Accounting(flowInstance)){
        	throw new RuntimeException("还保证金记账失败，交易未完成！");
        }

        return true;
	}

	/**
	 * 记账处理<br>
	 * s
	 * @param flowInstance
	 * @return
	 */
	private boolean Accounting(OfferFlow flowInstance) {
		return offerFlowService.Accounting(flowInstance);
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
	private OfferFlow FlowBuild(OfferRepayInfo repay, BigDecimal amount,
			String acctTitle, String memo2, 
			String dOrc, String appo_dORc) {
		return offerFlowService.FlowBuild(repay, amount, acctTitle, memo2, dOrc, appo_dORc);
	}
	
	@Override
	public String getTradeFlowNo(Long loanId) {
		String result = "TX" + loanId + "D" + Dates.getDateTime("yyyyMMddHHmmssSSS");
		return result;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void drawRiskFee(VLoanInfo vLoanInfo, Calendar currDate) {
		 String loanBelong = vLoanInfo.getLoanBelong();
		if (FundsSourcesTypeEnum.华澳信托.name().equals(loanBelong)
				|| FundsSourcesTypeEnum.国民信托.name().equals(loanBelong)
				|| FundsSourcesTypeEnum.外贸信托.name().equals(loanBelong)
				|| FundsSourcesTypeEnum.渤海2.name().equals(loanBelong)
				|| FundsSourcesTypeEnum.华瑞渤海.name().equals(loanBelong)
				|| FundsSourcesTypeEnum.外贸3.name().equals(loanBelong)) {
			return;
		}
		
		//针对渤海信托做特殊处理 
		if(FundsSourcesTypeEnum.渤海信托.name().equals(loanBelong)){
			LoanRepaymentDetail loanRepaymentDetail = loanRepaymentDetailServiceImpl.findByLoanAndReturnDate(vLoanInfo,Dates.format(currDate.getTime(), Dates.DATAFORMAT_SLASHDATE));
			if(loanRepaymentDetail == null){
				throw new RuntimeException("AfterLoanServiceImpl.drawRiskFee【渤海信托】得到指定还款日期的还款计划为空！");
			}
			BigDecimal defict = loanRepaymentDetail.getDeficit();
			BigDecimal accrual = new BigDecimal(0.0);
			BigDecimal corpus = new BigDecimal(0.0);
			if(defict.compareTo(loanRepaymentDetail.getReturneterm().subtract(loanRepaymentDetail.getCurrentAccrual()))>=0){
				corpus = loanRepaymentDetail.getReturneterm().subtract(loanRepaymentDetail.getCurrentAccrual());
				accrual = defict.subtract(corpus);
			}else{
				corpus = defict;
			}
			RiskAmountDetail riskDetail = new RiskAmountDetail();
			riskDetail.setId(sequencesService.getSequences(SequencesEnum.RISK_AMOUNT_DETAIL));
			riskDetail.setAccrual(accrual);
			riskDetail.setCorpus(corpus);
			riskDetail.setLoanId(vLoanInfo.getId());
			riskDetail.setTerm(loanRepaymentDetail.getCurrentTerm());
			riskDetail.setTradeDate(Dates.format(currDate.getTime(), Dates.DATAFORMAT_SLASHDATE));
			riskAmountDetailDao.insert(riskDetail);
			return;
		}
		
		 OfferRepayInfo repayInfo = offerRepayInfoService.getLoanlastDrawRisk(vLoanInfo.getId());

	       Date lastDrawRiskDate;
	       if (repayInfo != null){
	    	   lastDrawRiskDate = Dates.parse(repayInfo.getMemo(), "yyyy-MM-dd");
	       }
	       else{
	        	//之前没有提过风险金，设置还款起始日期
	        	lastDrawRiskDate = Dates.addDays(currDate.getTime(), -1);
	       }
	           
	       Map<String, Object> repaymentMap = new HashMap<String, Object>();
			repaymentMap.put("loanId", vLoanInfo.getId());
			repaymentMap.put("currDate", currDate.getTime());
			repaymentMap.put("minDate", lastDrawRiskDate);
			repaymentMap.put("state", RepaymentStateEnum.结清.toString());
			BigDecimal amount = loanRepaymentDetailServiceImpl.getDrawRiskSumDeficit(repaymentMap);
	       
//	       BigDecimal amount=RepaymentDetail.executeQuery("select sum(deficit) as sumdeficit from RepaymentDetail r where r.loan.id=:loan_id and r.returnDate<=:current and r.returnDate>:mindate and repaymentState<>:state",[loan_id:loanObj.id,current:currDate,mindate:lastDrawRiskDate, state: RepaymentState.结清])[0]
	       //如果已经过了最后一期还款，这里会是null
		   if(amount == null || amount.compareTo(BigDecimal.ZERO) <= 0){
	    	   return;
	       }
	       OfferRepayInfo repay = new OfferRepayInfo();
	        repay.setId(sequencesService.getSequences(SequencesEnum.OFFER_REPAY_INFO));
	        repay.setLoanId(vLoanInfo.getId());
	        repay.setAmount(amount);
	        repay.setTradeDate(currDate.getTime());
	        repay.setTradeKind(TradeKindEnum.正常交易.name());
	        repay.setOrgan(String.valueOf(vLoanInfo.getSalesDepartmentId()));
	        repay.setTeller(Const.ENDOFDAY_TELLER);
	        repay.setTradeNo(getTradeFlowNo(vLoanInfo.getId()));
	        repay.setMemo(Dates.getDateTime(currDate.getTime(), "yyyy-MM-dd"));
	        repay.setCreateTime(new Date());
	        
	        OfferFlow flowInstance;
	       if (vLoanInfo.getLoanType().equals(LoanTypeEnum.助学贷.getValue()))  //从机构保证金中提
	       {
	            repay.setTradeCode(Const.TRADE_CODE_DRAWRISK_STUDENT);
	            repay.setTradeType(TradeTypeEnum.保证金.getValue());
	            flowInstance = FlowBuild(repay,repay.getAmount(),Const.ACCOUNT_TITLE_AMOUNT,"提保证金","C","C");//保证金账户挂账部分销账
	            ZhuxueOrganization zhuxueOrganization = zhuxueOrganizationService.findByPlanId(vLoanInfo.getPlanId());
	            flowInstance.setAccount(zhuxueOrganization.getId().toString());//fwAccount=loanObj.organization_id.toString()
	            flowInstance.setAppoAcct(String.valueOf(repay.getLoanId()));
	            flowInstance.setAppoAcctTitle(Const.ACCOUNT_TITLE_OTHER_PAYABLE);//其他应付款
	       }
	       else 
	       {
	    	 //从公司风险金账户提
	           repay.setTradeCode(Const.TRADE_CODE_DRAWRISK);
	           repay.setTradeType(TradeTypeEnum.风险金.getValue());
	           flowInstance = FlowBuild(repay,repay.getAmount(),Const.ACCOUNT_TITLE_RISK_EXP,"提风险金","D","C");//保证金账户挂账部分销账
	           flowInstance.setAccount(Const.ACCOUNT_RISK);
	           flowInstance.setAppoAcct(String.valueOf(repay.getLoanId()));
	           flowInstance.setAppoAcctTitle(Const.ACCOUNT_TITLE_OTHER_PAYABLE);//其他应付款
	       }
	           
	       offerRepayInfoService.save(repay);
	       if((!Accounting(flowInstance))){
	    	   throw new RuntimeException(repay.getMemo()+"记账失败，交易未完成！");
	       }
	          
		
	}

	/***
	 * 获取逾期的总期数
	 * @param repayList
	 * @param currDate
	 * @return
	 */
	@Override
	public int getOverdueTermCount(List<LoanRepaymentDetail> repayList,
			Date tradeDate) {
		int result = 0;
        if (repayList == null || repayList.size()<1)
            return result;
        
        if (repayList.get(repayList.size()-1).getReturnDate().compareTo(tradeDate)<0){
        	//逾期
        	result = repayList.size();
        }
        else{
        	//当期
        	result = repayList.size()-1;
        }
            
        return result;
	}
	
	@Override
	public void drawJimuRisk(long loanId, Date currDate) {
		Map<String, Object> repayParam = new HashMap<String, Object>();
		repayParam.put("loanId", loanId);
		List<String> tradeCodes = new ArrayList<String>();
		tradeCodes.add(Const.TRADE_CODE_DRAWRISK);
		tradeCodes.add(Const.TRADE_CODE_DRAWRISK_STUDENT);
		repayParam.put("tradeCodes", tradeCodes);
		repayParam.put("sort", "trade_date desc");
		
		List<OfferRepayInfo> repayList = offerRepayInfoService.getDrawJimuRiskRepayInfo(repayParam);
		
		VLoanInfo loan = vLoanInfoServiceImpl.findByLoanId(loanId);
		
		Date lastDrawRiskDate = null;
		
		if (repayList.size() > 0) {
			lastDrawRiskDate = Dates.parse(repayList.get(0).getMemo(), Dates.DEFAULT_DATE_FORMAT);
		} else {
			lastDrawRiskDate = Dates.getBeforeDays(loan.getStartrdate(), 1);
		}
		
		Map<String, Object> repaymentMap = new HashMap<String, Object>();
		repaymentMap.put("loanId", loan.getId());
		repaymentMap.put("currDate", currDate);
		repaymentMap.put("minDate", lastDrawRiskDate);
		repaymentMap.put("state", RepaymentStateEnum.结清.toString());
		BigDecimal amount = loanRepaymentDetailServiceImpl.getDrawJimuRiskSumDeficit(repaymentMap);
		
		//if amount < 0
		if (null == amount || -1 == amount.signum()) {
			return;
		}
		
		OfferRepayInfo repay = new OfferRepayInfo();
		repay.setId(sequencesService.getSequences(SequencesEnum.OFFER_REPAY_INFO));
		repay.setLoanId(loan.getId());
		repay.setAmount(amount);
		repay.setTradeDate(currDate);
		repay.setTradeKind(TradeKindEnum.正常交易.getValue());
		repay.setOrgan(loan.getSalesDepartmentId() + "");
		repay.setTeller(Const.ENDOFDAY_TELLER);
		repay.setTradeNo(getTradeFlowNo(loan.getId()));
		repay.setMemo(Dates.getDateTime(currDate, Dates.DEFAULT_DATE_FORMAT));
		repay.setTradeCode(Const.TRADE_CODE_DRAWRISK);
		repay.setTradeType(TradeTypeEnum.风险金.toString());
		repay.setCreateTime(new Date());
		
		OfferFlow flowInstance = null;
		flowInstance = FlowBuild(repay, repay.getAmount(), Const.ACCOUNT_TITLE_RISK_EXP, 
				"提风险金", Const.DorC.D.toString(), Const.DorC.C.toString());
		
		flowInstance.setAccount(Const.ACCOUNT_JIMUHEZIRISK);
		flowInstance.setAppoAcct(repay.getLoanId() + "");
		flowInstance.setAppoAcctTitle(Const.ACCOUNT_TITLE_OTHER_PAYABLE);
		
		offerRepayInfoService.save(repay);
		if ((!Accounting(flowInstance)) ) {
			throw new RuntimeException(repay.getMemo() + "记账失败，交易未完成！");
		}
		
		updateELPOnDrawOnDrwRisk(loanId, currDate);
	}
	
    private void updateELPOnDrawOnDrwRisk(Long loanId, Date returnDate){
    	JimuExtRepay elp = new JimuExtRepay();
        
        LoanRepaymentDetail queryCondition = new LoanRepaymentDetail();
        queryCondition.setLoanId(loanId);
        queryCondition.setReturnDate(returnDate);
                
        List<LoanRepaymentDetail> results = repaymentDetailDao.findListByVo(queryCondition);

        if (results.size() == 0) {
        	throw new RuntimeException("未找到loan_id:" + loanId + " return_date:" + returnDate.toString() + "对应的RepaymentDetail" );
        }
        LoanRepaymentDetail tmpDetail = results.get(0);
        elp.setId(sequencesService.getSequences(SequencesEnum.JIMU_EXT_REPAY));
        elp.setLoanId(tmpDetail.getLoanId());
        elp.setPromiseRepayDate(returnDate);
        elp.setCurrentTerm(tmpDetail.getCurrentTerm());
        elp.setPromiseReturneterm(tmpDetail.getReturneterm());
        elp.setAccrualRevise(tmpDetail.getAccrualRevise());
        
        jimuExtRepayDao.insert(elp);
    }

	@Override
	public synchronized void generateFlowForRepayAllJimuPay(long loanId, int curTerm, Date notifyDate) {
        LoanRepaymentDetail queryCondition = new LoanRepaymentDetail();
        queryCondition.setLoanId(loanId);
        queryCondition.setCurrentTerm((long) curTerm);        
                
        List<LoanRepaymentDetail> results = repaymentDetailDao.findListByVo(queryCondition);

        if (results.size() == 0) {
        	return;
        }
        
        LoanRepaymentDetail rd = results.get(0);
        
        VLoanInfo loan = vLoanInfoServiceImpl.findByLoanId(rd.getLoanId());
        
        Date promiseReturnDate = rd.getReturnDate();
        
        BigDecimal repayThisTerm =  rd.getReturneterm();
        BigDecimal repayAllIncludeThisTerm = rd.getRepaymentAll().add(rd.getGiveBackRate());
        repayAllIncludeThisTerm = repayAllIncludeThisTerm.subtract(rd.getPenalty());
        BigDecimal repayAllExcludeThisTerm = repayAllIncludeThisTerm.subtract(repayThisTerm);
        
      //当期垫付的补足流水 积木风险金收入
        JimuExtRepay elrCondition = new JimuExtRepay();
        elrCondition.setLoanId(rd.getLoanId());
        elrCondition.setCurrentTerm(rd.getCurrentTerm());
        List<JimuExtRepay> elrList = jimuExtRepayDao.findListByVo(elrCondition);
        
        boolean curTermHadDrawRisk = elrList.size() != 0;
        
        // 流水，积木风险金收入始终是 还款账户->积木风险金 （一次性含当期）；
        // 需要多出流水，客户应付 -> 还款账户， 即所有对积木风险金的补足的资金来源都来自 还款账户
        // 2种情况，1种是当期还款尚未垫付；1种是当期还款已经发生过垫付
        // 积木风险金 -> 客户应付 10k  客户应付->还款账户 10k 还款账户->积木风险金 10k
        // 积木风险金 -> 客户应付 1k  积木风险金 -> 客户应付 9k 客户应付->还款账户 1k  客户应付->还款账户 9k 还款账户->积木风险金 10k
        OfferFlow flowJIMURiskPay = null;
        OfferFlow flowJIMURiskReceive = null;
        OfferFlow flowJIMUAccountReceive = null;
        if (curTermHadDrawRisk) { //一次性结清（不含当期应还本息）对应的支出和收入流水
            // 1、积木风险金 -> 客户应付 9k  2、客户应付->还款账户 1k （客户还款是已经实时生成） 3、客户应付->还款账户 9k  4、还款账户->积木风险金 10k
        	flowJIMURiskPay = buildFlowJimuRiskPay4RepayAll(loanId,curTerm,repayAllExcludeThisTerm,notifyDate);
        	flowJIMURiskReceive = buildFlowJimuRiskReceive4RepayAll(loanId, curTerm, repayAllIncludeThisTerm, notifyDate);
        	flowJIMUAccountReceive = buildFlowJimuAccountReceive4RepayAll(loanId,curTerm,repayAllExcludeThisTerm,notifyDate);
        	
        	updateELPOnFillRisk(loanId, promiseReturnDate, notifyDate, null, repayThisTerm, null, null);
        } else { //一次性结清（含当期应还本息）对应的支出和收入流水
            // 1、积木风险金 -> 客户应付 10k   2、客户应付->还款账户 10k   3、还款账户->积木风险金 10k
        	flowJIMURiskPay = buildFlowJimuRiskPay4RepayAll(loanId,curTerm,repayAllIncludeThisTerm,notifyDate);
        	flowJIMURiskReceive = buildFlowJimuRiskReceive4RepayAll(loanId,curTerm,repayAllIncludeThisTerm,notifyDate);
        	flowJIMUAccountReceive = buildFlowJimuAccountReceive4RepayAll(loanId,curTerm,repayAllIncludeThisTerm,notifyDate);

        }
        
        if (!Accounting(flowJIMURiskPay)) {
        	throw new RuntimeException("积木盒子风险金一次性结清支出记账失败，交易未完成！");
        }
        if (!Accounting(flowJIMUAccountReceive)) {
            throw new RuntimeException("积木盒子还款账户一次性结清收入记账失败，交易未完成！");
        }
        if (!Accounting(flowJIMURiskReceive)) {
            throw new RuntimeException("积木盒子风险金一次性结清收入记账失败，交易未完成！");
        }
        
        //贴息流水（不经过积木风险金收入+支出中转，投资公司账户  服务费支出 ->积木贴息 罚息收入
//        boolean needAccrualRevise = false; //贴息；通知日晚于约定还款日，就需要贴息； 没有用到？？
        int reviseDay = Dates.dateDiff(notifyDate, promiseReturnDate); //贴息时长，需要贴几天利息；
        if(reviseDay <= 0) {
        	reviseDay = 0;
        }
        BigDecimal accrualRevise = rd.getPrincipalBalance().multiply(loan.getRateey());
        accrualRevise = accrualRevise.multiply(new BigDecimal(reviseDay / 360));//贴息；
        accrualRevise = accrualRevise.setScale(2,RoundingMode.HALF_UP);

        OfferFlow flowAccuralRevise = buildFlowJimuRepayAllAccuralRevise(loanId,curTerm,accrualRevise,notifyDate);
        if (!Accounting(flowAccuralRevise)) {
            throw new RuntimeException("积木盒子风险金一次性结清贴息记账失败，交易未完成！");
        }
        
        //一次性减免金额统计；减免不再额外记流水
        //能找到结清对应的repayInfo吗？最后一笔3001的RepayInfo；找到其中的减免流水和罚息流水，即可计算得出 一次性涉及的减免金额
        OfferRepayInfo repayInfo = offerRepayInfoService.getLoanLastRepayInfoByTradeCode(loanId, Const.TRADE_CODE_ONEOFF);
        
        OfferFlow condition = new OfferFlow();
        condition.setTradeNo(repayInfo.getTradeNo());
        condition.setAcctTitle(Const.ACCOUNT_TITLE_FINE_EXP);
        List<OfferFlow> flows = offerFlowDao.findListByVo(condition);
        
        Date doneRepayAllFactTime = repayInfo.getTradeTime();
        BigDecimal jianmian = BigDecimal.ZERO;
        BigDecimal faxi = BigDecimal.ZERO;
        for(OfferFlow flow : flows){
            if(flow.getAccount().equals(Const.ACCOUNT_GAINS)) {
                jianmian = flow.getTradeAmount();
            }
            if(flow.getLoanId().equals(loanId)) {
            	faxi = flow.getTradeAmount();
            }
        }
        
        updateExtRepayAll(loanId, rd, repayInfo.getTradeDate(), notifyDate, doneRepayAllFactTime, jianmian, faxi, accrualRevise);
	}
	
	/**
	 * 积木风险金 风险金支出 -> 客户其他应付
	 * @param loanId
	 * @param curterm
	 * @param amount
	 * @param tradeDate
	 * @return
	 */
	@Transactional
    private OfferFlow buildFlowJimuRiskPay4RepayAll(long loanId, int curterm, BigDecimal amount, Date tradeDate) { 
        if(amount.compareTo(new BigDecimal("0.01")) < 0) {
            return null;
        }
        OfferFlow flow = new OfferFlow();
        Long id = sequencesService.getSequences(SequencesEnum.OFFER_FLOW);
        flow.setId(id);
        flow.setLoanId(loanId);
        flow.setTradeAmount(amount);
        flow.setMemo("一次性还款");
        flow.setMemo2(curterm + "");
        flow.setMemo3(loanId + "");
        flow.setTradeCode(Const.TRADE_CODE_DRAWRISK);

        flow.setAccount(Const.ACCOUNT_JIMUHEZIRISK);
        flow.setAcctTitle(Const.ACCOUNT_TITLE_RISK_EXP);
        flow.setAppoAcct(loanId + "");
        flow.setAppoAcctTitle(Const.ACCOUNT_TITLE_OTHER_PAYABLE);
        flow.setDorc("D");
        flow.setAppoDorc("C");

        flow.setTradeKind(TradeKindEnum.正常交易.toString());

        flow.setTradeType(TradeTypeEnum.风险金.toString());
        flow.setTradeDate(tradeDate);
        flow.setTeller(Const.JIMU_REPAYALL_TELLER);
        flow.setOrgan("-");

        flow.setTradeNo(getTradeFlowNo(loanId));
        return flow;
    }
    
    /**
     *  积木还款账户 其他应付 -> 积木风险金 风险金收入
     * @param loanId
     * @param curterm
     * @param amount
     * @param tradeDate
     * @return
     */
    private OfferFlow buildFlowJimuRiskReceive4RepayAll (long loanId, int curterm, BigDecimal amount, Date tradeDate) {
        if(amount.compareTo(new BigDecimal("0.01")) < 0) {
            return null;
        }
        OfferFlow flow = new OfferFlow();
        Long id = sequencesService.getSequences(SequencesEnum.OFFER_FLOW);
        flow.setId(id);
        flow.setLoanId(loanId);
        flow.setTradeAmount(amount);
        flow.setMemo("一次性还款");
        flow.setMemo2(curterm + "");
        flow.setMemo3(loanId + "");
        flow.setTradeCode(Const.TRADE_CODE_FILL_RISK);

        flow.setAccount(Const.ACCOUNT_JIMUHEZIREPAY);
        flow.setAcctTitle(Const.ACCOUNT_TITLE_OTHER_PAYABLE);
        flow.setAppoAcct(Const.ACCOUNT_JIMUHEZIRISK);
        flow.setAppoAcctTitle(Const.ACCOUNT_TITLE_RISK_INCOME);
        flow.setDorc("D");
        flow.setAppoDorc("C");

        flow.setTradeKind(TradeKindEnum.正常交易.toString());

        flow.setTradeType(TradeTypeEnum.风险金.toString());
        flow.setTradeDate(tradeDate);
        flow.setTeller(Const.JIMU_REPAYALL_TELLER);
        flow.setOrgan("-");

        flow.setTradeNo(getTradeFlowNo(loanId));

        return flow;
    }
    
    /**
     *  客户 其他应付 -> 积木还款 其他应付
     * @param loanId
     * @param curterm
     * @param amount
     * @param tradeDate
     * @return
     */
    private OfferFlow buildFlowJimuAccountReceive4RepayAll (long loanId, int curterm, BigDecimal amount, Date tradeDate) {
        if(amount.compareTo(new BigDecimal("0.01")) < 0) {
            return null;
        }
        OfferFlow flow = new OfferFlow();
        Long id = sequencesService.getSequences(SequencesEnum.OFFER_FLOW);
        flow.setId(id);
        flow.setLoanId(loanId);
        flow.setTradeAmount(amount);
        flow.setMemo("一次性还款");
        flow.setMemo2(curterm + "");
        flow.setMemo3(loanId + "");
        flow.setTradeCode(Const.TRADE_CODE_FILL_RISK);

        flow.setAccount(loanId + "");
        flow.setAcctTitle(Const.ACCOUNT_TITLE_OTHER_PAYABLE);
        flow.setAppoAcct(Const.ACCOUNT_JIMUHEZIRISK);
        flow.setAppoAcctTitle(Const.ACCOUNT_TITLE_OTHER_PAYABLE);
        flow.setDorc("D");
        flow.setAppoDorc("C");

        flow.setTradeKind(TradeKindEnum.正常交易.toString());

        flow.setTradeType(TradeTypeEnum.现金.toString());
        flow.setTradeDate(tradeDate);
        flow.setTeller(Const.JIMU_REPAYALL_TELLER);
        flow.setOrgan("-");

        flow.setTradeNo(getTradeFlowNo(loanId));

        return flow;
    }
    
    private OfferFlow buildFlowJimuRepayAllAccuralRevise (long loanId, int curterm, BigDecimal amount, Date tradeDate) {
        if(amount.compareTo(new BigDecimal("0.01")) < 0) {
            return null;
        }
        OfferFlow flow = new OfferFlow();
        flow.setId(sequencesService.getSequences(SequencesEnum.OFFER_FLOW));
        flow.setLoanId(loanId);
        flow.setTradeAmount(amount);
        flow.setMemo("一次性还款");
        flow.setMemo2(curterm + "");
        flow.setMemo3(loanId + "");
        flow.setTradeCode(Const.TRADE_CODE_FILL_RISK);

        flow.setAccount(Const.ACCOUNT_GAINS);
        flow.setAcctTitle(Const.ACCOUNT_TITLE_FINE_EXP);
        flow.setAppoAcct(Const.ACCOUNT_JIMUHEZIRISK);
        flow.setAppoAcctTitle(Const.ACCOUNT_TITLE_FINE_INCOME);
        flow.setDorc("D");
        flow.setAppoDorc("C");

        flow.setTradeKind(TradeKindEnum.正常交易.toString());

        flow.setTradeType(TradeTypeEnum.现金.toString());
        flow.setTradeDate(tradeDate);
        flow.setTeller(Const.JIMU_REPAYALL_TELLER);
        flow.setOrgan("-");

        flow.setTradeNo(getTradeFlowNo(loanId));

        return flow;
    }
	
    private void updateExtRepayAll(Long loanId, LoanRepaymentDetail rd, Date tradeDate, 
    		Date notifyDate, Date doneRepayAllFactTime, BigDecimal jianmian, BigDecimal faxi, BigDecimal accrualRevise){

        JimuExtRepayAll condition = new JimuExtRepayAll();
        condition.setLoanId(loanId);
        
        List<JimuExtRepayAll> results = jimuExtRepayAllDao.findListByVo(condition);

        if(results.size() > 0) {
        	throw new RuntimeException("该笔债权已有提前结清记录");
        }

        JimuExtRepayAll elp = new JimuExtRepayAll();
        elp.setId(sequencesService.getSequences(SequencesEnum.JIMU_EXT_REPAY_ALL));
        elp.setLoanId(rd.getLoanId());
        elp.setCurrentTerm(rd.getCurrentTerm().intValue());
        elp.setPromiseRepayDate(rd.getReturnDate());
        elp.setTradeDate(tradeDate);
        elp.setNotifyDate(notifyDate);
        elp.setDoneRepayAllFactTime(doneRepayAllFactTime);

        elp.setPromiseReturneterm(rd.getReturneterm());
        elp.setRepayAll(rd.getRepaymentAll());
        elp.setWeiyuejing(rd.getPenalty());
        elp.setTuifei(rd.getGiveBackRate());

        elp.setJianmian(jianmian);
        elp.setFaxi(faxi);
        elp.setAccrualRevise(accrualRevise);

        jimuExtRepayAllDao.insert(elp);
    }

	@Override
	public BigDecimal getOverdueCorpus(List<LoanRepaymentDetail> repayList,Date currDate) {
		BigDecimal result = BigDecimal.ZERO;
		if(CollectionUtils.isNotEmpty(repayList)){
			for(LoanRepaymentDetail rd: repayList){
				if(rd.getReturnDate().compareTo(currDate) == -1){//还款日期在指定日期之前
					//[每期还款金额-当期利息]和 [当期剩余欠款]2个计算值中的较小值进行累计
					BigDecimal minValue = rd.getDeficit().min(rd.getReturneterm().subtract(rd.getCurrentAccrual()));
					result = result.add(minValue);
				}
			}
		}
		return result;
	}

	@Override
	public BigDecimal getOverdueInterest(List<LoanRepaymentDetail> repayList,Date currDate) {
		BigDecimal result = BigDecimal.ZERO;
		if(CollectionUtils.isNotEmpty(repayList)){
			for(LoanRepaymentDetail rd: repayList){
				if(rd.getReturnDate().compareTo(currDate) == -1){//还款日期在指定日期之前
					BigDecimal tempValue = rd.getDeficit().subtract((rd.getReturneterm().subtract(rd.getCurrentAccrual())));
					if((tempValue.compareTo(BigDecimal.ZERO) >= 0)){
						result = result.add(tempValue);
					}
				}
			}
		}
		return result;
	}

	@Override
	public BigDecimal getCurrCorpus(List<LoanRepaymentDetail> repayList,Date currDate) {
		BigDecimal result = BigDecimal.ZERO;
		
		if(CollectionUtils.isNotEmpty(repayList)){
			LoanRepaymentDetail rd = repayList.get(repayList.size()-1);
			if(rd.getReturnDate().compareTo(currDate) >= 0){
				BigDecimal tempValue = rd.getReturneterm().subtract(rd.getCurrentAccrual());
				tempValue = rd.getDeficit().min(tempValue);
				result = result.add(tempValue);
			}
		}
		return result;
	}

	@Override
	public BigDecimal getCurrInterest(List<LoanRepaymentDetail> repayList,Date currDate) {
		BigDecimal result = BigDecimal.ZERO;
		if(CollectionUtils.isNotEmpty(repayList)){
				LoanRepaymentDetail rd = repayList.get(repayList.size()-1);
				BigDecimal tempValue = rd.getDeficit().subtract(rd.getReturneterm().subtract(rd.getCurrentAccrual()));
				if(rd.getReturnDate().compareTo(currDate) >= 0 && (tempValue.compareTo(BigDecimal.ZERO) >= 0)){
					result = tempValue;
				}
		}
		return result;
	}

	@Override
	public BigDecimal getGiveBackRate(List<LoanRepaymentDetail> repayList) {
		BigDecimal result = BigDecimal.ZERO;
		if(CollectionUtils.isNotEmpty(repayList)){
			result = repayList.get(repayList.size()-1).getGiveBackRate();
		}
		return result;
	}

	@Override
	public synchronized void fillJimuRisk(Long loanId, Date returnDate, Date doFillDate) {
		Date inAccountEndDay = sysSpecialDayService.getBeforeWorkday(doFillDate, 4);
		fillJimuRisk(loanId, returnDate, doFillDate, inAccountEndDay);
	}
	
	private void fillJimuRisk(Long loanId, Date returnDate, Date doFillDate, Date inAccountEndDay) {

        LoanRepaymentDetail queryCondition = new LoanRepaymentDetail();
        queryCondition.setLoanId(loanId);
        queryCondition.setReturnDate(returnDate);
                
        List<LoanRepaymentDetail> results = repaymentDetailDao.findListByVo(queryCondition);

        if (results.size() == 0) {
        	return;
        }
        
        LoanRepaymentDetail rd = results.get(0);
        if(hasFilled(loanId, rd.getCurrentTerm().intValue(), doFillDate)) {
        	return;
        }
        Map<String, Object> param1 = new HashMap<String, Object>();
        param1.put("loan_id", loanId);
        param1.put("appoAccount", Const.ACCOUNT_INTERJACENT);
        param1.put("startDate", returnDate);
        param1.put("endDate", returnDate);
        param1.put("curTerm", rd.getCurrentTerm());
        param1.put("title1", Const.ACCOUNT_TITLE_LOAN_AMOUNT);
        param1.put("title2", Const.ACCOUNT_TITLE_INTEREST_EXP);
        
        BigDecimal paidAmountOnRepayDay = offerFlowDao.getPaidAmountOnRepayDay(param1);
        
        if (null == paidAmountOnRepayDay) {
        	paidAmountOnRepayDay = BigDecimal.ZERO;
        }

        Map<String, Object> param2 = new HashMap<String, Object>();
        param2.put("loan_id", loanId);
        param2.put("appoAccount", Const.ACCOUNT_INTERJACENT);
        param2.put("startDate", returnDate);
        param2.put("endDate", inAccountEndDay);
        param2.put("curTerm", rd.getCurrentTerm());
        param2.put("title1", Const.ACCOUNT_TITLE_LOAN_AMOUNT);
        param2.put("title2", Const.ACCOUNT_TITLE_INTEREST_EXP);
        
        BigDecimal paidAmountBeforeEndDay = offerFlowDao.getPaidAmountBeforeEndDay(param2);
        
        if (null == paidAmountBeforeEndDay) {
        	paidAmountBeforeEndDay = BigDecimal.ZERO;
        }
        
        BigDecimal needZendaiRisk = rd.getReturneterm().subtract(paidAmountOnRepayDay);
        needZendaiRisk = needZendaiRisk.subtract(paidAmountBeforeEndDay);
        
        if (paidAmountOnRepayDay.signum() == 1) {
        	OfferFlow flowPaidOnRepayDay = buildFlowJimuFill(loanId, rd.getCurrentTerm().intValue(), 
        			paidAmountOnRepayDay, Const.ACCOUNT_JIMUHEZIREPAY, Const.ACCOUNT_TITLE_OTHER_PAYABLE,
        			TradeTypeEnum.现金, doFillDate);
            if(!Accounting(flowPaidOnRepayDay)) {
                throw new RuntimeException("积木盒子风险金补足记账失败，交易未完成！");
            }
        }
        
        BigDecimal tmpAmount = rd.getReturneterm().subtract(paidAmountOnRepayDay);
        if (tmpAmount.signum() == 1) {
            OfferFlow flow4RiskAll = buildFlowJimuFill(loanId, rd.getCurrentTerm().intValue(), 
            		tmpAmount, Const.ACCOUNT_RISK, Const.ACCOUNT_TITLE_RISK_EXP, 
            		TradeTypeEnum.风险金, doFillDate);
            if(!Accounting(flow4RiskAll)) {
                throw new RuntimeException("积木盒子风险金补足记账失败，交易未完成！");
            }
        }
        
        updateELPOnFillRisk(loanId, returnDate, doFillDate, inAccountEndDay, paidAmountOnRepayDay,
        		paidAmountBeforeEndDay, needZendaiRisk);
        
	}
	
    private void updateELPOnFillRisk(Long loanId, Date returnDate, Date doFillDate,
    		Date inAccountEndDay,BigDecimal paidAmountOnRepayDay, BigDecimal paidAmountBeforeEndDay, 
    		BigDecimal needZendaiRisk){
        
        JimuExtRepay condition = new JimuExtRepay();
        condition.setLoanId(loanId);
        condition.setPromiseRepayDate(returnDate);
        
        List<JimuExtRepay> results = jimuExtRepayDao.findListByVo(condition);

        if(results.size() == 0) {
        	throw new RuntimeException("意外地没有找到垫付记录");
        }
        
        JimuExtRepay elp = results.get(0);
        if(elp.getFillDate() != null) {
        	throw new RuntimeException("该笔垫付已经进行过补足");
        }

        elp.setPaidOnRepayDay(paidAmountOnRepayDay);
        elp.setPaidBeforeInAccEndDay(paidAmountBeforeEndDay);
        elp.setNotPaidUntilInAccEndDay(needZendaiRisk);
        elp.setInAccountEndDay( inAccountEndDay);
        elp.setFillDate(doFillDate);
        elp.setHasFilled("t");

        jimuExtRepayDao.updateByPrimaryKeySelective(elp);
    }
	
    private OfferFlow buildFlowJimuFill(Long loanId, Integer curterm, BigDecimal amount, String account,String title, TradeTypeEnum tradeType, Date tradeDate){

        OfferFlow flow = new OfferFlow();
        flow.setId(sequencesService.getSequences(SequencesEnum.OFFER_FLOW));
        flow.setLoanId(loanId);
        flow.setTradeAmount(amount);
        flow.setTradeCode(Const.TRADE_CODE_FILL_RISK);//补足风险金
        flow.setAccount(account);
        flow.setAcctTitle(title);
        flow.setDorc("D");
        flow.setAppoDorc("C");
        flow.setAppoAcct(Const.ACCOUNT_JIMUHEZIRISK);//积木盒子账户
        flow.setAppoAcctTitle(Const.ACCOUNT_TITLE_RISK_INCOME); //风险金收入
        flow.setMemo2(curterm+ "");
        flow.setMemo3(loanId + "");

        flow.setTradeKind(TradeKindEnum.正常交易.toString());
        flow.setTradeType(tradeType.toString());
        flow.setTradeDate(tradeDate);
        flow.setTeller(Const.FILLRISK_TELLER);
        flow.setOrgan("-");

        flow.setTradeNo(getTradeFlowNo(loanId));
        return flow;
    }
	
    private boolean hasFilled(Long loanId, Integer curTerm, Date fillDate){
    	OfferFlow condition = new OfferFlow();
    	condition.setTradeDate(fillDate);
    	condition.setTradeCode(Const.TRADE_CODE_FILL_RISK);
    	condition.setMemo2(curTerm + "");
    	condition.setMemo3(loanId + "");
    	
        List<OfferFlow> list =  offerFlowDao.findListByVo(condition);
        
        return list.size() != 0;
     }

	@Override
	public synchronized void returnJimuRisk(Long loanId, int currentTerm,
			BigDecimal pactMoney) {
		BigDecimal amount = pactMoney.divide(
				new BigDecimal(currentTerm), MathContext.DECIMAL128);
		amount = amount.multiply(new BigDecimal("1.5"));

		
		OfferFlow flowPaidOnRepayDay = buildFlowJimuFill(loanId, currentTerm, amount, 
				Const.ACCOUNT_JIMUHEZIRISK, Const.ACCOUNT_TITLE_RISK_EXP, 
				TradeTypeEnum.风险金);
		
		if (!Accounting(flowPaidOnRepayDay)) {
			throw new RuntimeException("积木盒子风险金退回失败，交易未完成！");
		}
		
	}
	
    /**\
     * 重载，用于风险金退回
     * @param loanId
     * @param curterm
     * @param amount
     * @param account
     * @param title
     * @param tradeType
     * @param tradeDate
     * @return
     */
    private OfferFlow buildFlowJimuFill(Long loanId, Integer curterm, BigDecimal amount, 
    		String account,String title, TradeTypeEnum tradeType){

        OfferFlow flow = new OfferFlow();
        flow.setId(sequencesService.getSequences(SequencesEnum.OFFER_FLOW));
        flow.setLoanId(loanId);
        flow.setTradeAmount(amount);
        flow.setTradeCode(Const.TRADE_CODE_RETURN_RISK);//风险金退回
        flow.setAccount(account);
        flow.setAcctTitle(title);
        flow.setDorc("D");
        flow.setAppoDorc("C");
        flow.setAppoAcct(Const.ACCOUNT_RISK);//风险金账户
        flow.setAppoAcctTitle(Const.ACCOUNT_TITLE_RISK_INCOME); //风险金收入
        flow.setMemo2(curterm + "");
        flow.setMemo3(loanId + "");

        flow.setTradeKind(TradeKindEnum.正常交易.toString());
        flow.setTradeType(tradeType.toString());
        flow.setTradeDate(Dates.getCurrDate());
        flow.setTeller(Const.FILLRISK_TELLER);
        flow.setOrgan("-");

        flow.setTradeNo(getTradeFlowNo(loanId));
        return flow;
    }
	
	@Override
	public BigDecimal getPenaltyTmp(List<LoanRepaymentDetail> repayList,VLoanInfo loan) {
		BigDecimal result = new BigDecimal("0.00");
		if (CollectionUtils.isEmpty(repayList))
			return result;
		LoanRepaymentDetail rd = repayList.get(repayList.size() - 1);
		if (LoanTypeEnum.助学贷.name().equals(loan.getLoanType()) 
				|| LoanTypeEnum.车贷.name().equals(loan.getLoanType())) {
			 
			if(rd.getCurrentTerm() != loan.getTime()) {//最后一期没有利息
				 result = rd.getRepaymentAll().subtract(rd.getPrincipalBalance())
							.subtract(rd.getReturneterm());
			 }
		} 
		else{//个贷
			if (rd.getCurrentTerm() != loan.getTime()
					&& (rd.getCurrentTerm() != 1 && rd.getCurrentTerm() != 2)) {
					result = loan.getPactMoney().multiply(Const.PENALTY_INTEREST_RATE).setScale(2, RoundingMode.HALF_UP);
				}
		}
		
		return result;
	}

	@Override
	public BigDecimal getPenaltyTmp(List<LoanRepaymentDetail> repayList) {
		if (CollectionUtils.isEmpty(repayList)){
			return new BigDecimal("0.00");
		}
		LoanRepaymentDetail rd = repayList.get(repayList.size() - 1);
		VLoanInfo loan =  vLoanInfoServiceImpl.findByLoanId(rd.getLoanId());
		
		return getPenaltyTmp(repayList,loan);
	}
	
	@Override
	public BigDecimal getRemainingInterest(VLoanInfo loan,Date currDate) {
        List<LoanRepaymentDetail> repaymentDetail = repaymentDetailDao.findByLoanId(loan.getId());
        BigDecimal result = new BigDecimal("0.00");
        if (repaymentDetail == null || repaymentDetail.size()<1)
            return result;

        if(repaymentDetail != null&&repaymentDetail.size()>0)
        {
            for(int i=0;i<repaymentDetail.size();i++){
                if (repaymentDetail.get(i).getReturnDate().compareTo(currDate) >= 0){
                    result = result.add(repaymentDetail.get(i).getCurrentAccrual());
                }
            }
        }
        return result;
    }
	
    /**
     * modify by liuyh 
     * 存款、取款入账处理 
     */
    private boolean depositAndDrawDeal(OfferRepayInfo repay, VLoanInfo loanInfo){
        // 交易码
        String tradeCode = repay.getTradeCode();
        if (!(Const.TRADE_CODE_ORGOUT.equals(tradeCode) || Const.TRADE_CODE_OUT.equals(tradeCode))) {
            Assert.notNull(loanInfo, ResponseEnum.FULL_MSG, "未找到债权数据loanId:" + repay.getLoanId());
        }
        if (Const.TRADE_CODE_OPENACC.equals(tradeCode) || Const.TRADE_CODE_OPENACC_ASC.equals(tradeCode)) {// 新开户
            offerFlowService.openAccountDeal(repay, loanInfo);
            return Boolean.TRUE;
        }
        if (Const.TRADE_CODE_IN.equals(tradeCode)) {// 账户存款
            offerFlowService.accountDepositDeal(repay);
            return Boolean.TRUE;
        }
        if (Const.TRADE_CODE_ORGIN.equals(tradeCode)) {// 保证金账户存款
            offerFlowService.bondAccountDepositDeal(repay);
            return Boolean.TRUE;
        }
        if (Const.TRADE_CODE_OUT.equals(tradeCode)) {// 账户取款
            offerFlowService.accountWithdrawDeal(repay);
            return Boolean.TRUE;
        }
        if (Const.TRADE_CODE_ORGOUT.equals(tradeCode)) {// 保证金账户取款
            offerFlowService.bondAccountWithdrawDeal(repay);
            return Boolean.TRUE;
        }
        if (Const.TRADE_CODE_STDIN.equals(tradeCode)) {// 学生还保证金
            offerFlowService.studentBondRepayment(repay, loanInfo);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    
    /**
     * modify by liuyh 
     * 历史还款是结清的，当期还款也是结清的，后面再进来的钱直接挂账 
     */
    private boolean unnecessaryRepayDeal(OfferRepayInfo repay, VLoanInfo loanInfo, List<LoanRepaymentDetail> overDateList){
        //没有要还款  ，没到还款日时，多余现金挂账
        if (overDateList == null || overDateList.size()<1) {
            offerFlowService.accountRechargeDeal(repay,repay.getAmount());
            logger.debug("accountRechargeDeal,金额挂账，账户充值的flow账务处理结束1，repayDeal返回,loanId="+loanInfo.getId());
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    
    /** 
     * modify by liuyh
     * 逾期还款处理 
     * 返回处理状态和剩余金额
     */
    private boolean overdueRepayDeal(OfferRepayInfo repay, VLoanInfo loanInfo, List<LoanRepaymentDetail> overDateList, BigDecimal tranAmt){
        //获取挂账金额
        BigDecimal amount = getAccAmount(repay.getLoanId()); 
        
        if (amount.compareTo(new BigDecimal("0.01")) >= 0) {
            offerFlowService.accountBalancePayDeal(repay,amount);
            tranAmt = tranAmt.add(amount);
        }
        
        //获取申请减免罚息金额
        BigDecimal relief = getReliefOfFine(repay.getTradeDate(), repay.getLoanId(), repay); 
        OfferFlow flow = null;
        //存在减免罚息金额
        if(relief.compareTo(new BigDecimal("0.01")) >= 0){
            tranAmt = tranAmt.add(relief);//还款金额加上免罚息金额
            flow = offerFlowService.reductionPenaltyDeal(repay, relief);
        }
        
        //获取罚息
        BigDecimal fineAmount = getFine(overDateList, repay.getTradeDate());
        //逾期罚息存在
        if(fineAmount.compareTo(BigDecimal.ZERO)>0) {
            Map<String ,Object> map = offerFlowService.penaltyDeal(repay, fineAmount, tranAmt, relief, overDateList, flow);
            tranAmt = (BigDecimal)map.get("countAMT");
            if((Integer)map.get("type") == 1){//处理结束，下面的不再处理
               logger.debug("penaltyDeal,罚息的flow账务处理结束，repayDeal返回.loanId=" + loanInfo.getId());
               return Boolean.TRUE;
           }
        }
		//一般减免已经生效 已经完成了
		if (loanInfo.getLoanState().equals(LoanStateEnum.逾期.name())) {
			specialRepaymentApplyService.reliefRepayDealFinish(repay);
		}
        if (tranAmt.compareTo(new BigDecimal("0.01")) < 0){
            //金额处理完
             logger.debug("金额处理完处理结束，repayDeal返回,loanId=" + loanInfo.getId());
             return Boolean.TRUE;
        }

        // 还风险金（保证金）流水
        if (!repayRiskFee(loanInfo, repay, tranAmt)) {
            logger.error("还风险金（保证金）流水失败，交易未完成！loanid=" + loanInfo.getId());
            throw new PlatformException("还风险金（保证金）流水失败，交易未完成！loanId=" + loanInfo.getId());
        }

        // 剩余使用金额
        BigDecimal residueAmount = tranAmt;
        // 对逾期部分需要先扣除
        for (LoanRepaymentDetail para : overDateList) {
            if (para.getReturnDate().before(repay.getTradeDate())) {
                if (para.getDeficit().compareTo(tranAmt) <= 0) {
                    tranAmt = offerFlowService.overdueRepaymentDeal(repay, para, loanInfo, tranAmt);
                    // 创建逾期还款划扣队列、分账队列数据
                    this.createOverdueRepayDebitQueue(repay, loanInfo, para, residueAmount.subtract(tranAmt));
                    residueAmount = tranAmt;
                } else {
                    offerFlowService.overdueInsufficientRepaymentDeal(repay, para, loanInfo, tranAmt);
                    logger.debug("overdueInsufficientRepaymentDeal逾期不足额还款的flow账务处理结束，repayDeal返回.loanId=" + loanInfo.getId());
                    // 创建逾期还款划扣队列、分账队列数据
                    this.createOverdueRepayDebitQueue(repay, loanInfo, para, tranAmt);
                    return Boolean.TRUE;
                }
            }
        }
        // 将逾期已还的贷款改为正常
        loanBaseDao.updatePaidoffOverdueToZC(loanInfo.getId(), repay.getTradeDate());
       //结完逾期，剩余金额
       transaction.set(tranAmt);
       return Boolean.FALSE;
    }
    
	/**
     * modify by liuyh
     * 正常还款处理（提前还款、提前结清、T日正常还款、T日挂账分账）
     */
    private void normalRepayDeal(OfferRepayInfo repay, VLoanInfo loanInfo, List<LoanRepaymentDetail> overDateList){
        //正常还款金额 = 交易金额+挂账金额-逾期入账金额【如果有逾期的情况】
        BigDecimal tranAmt = transaction.get();
        /** 获取计算器实例 **/
        ICalculator calculatorInstace = CalculatorFactoryImpl.createCalculator(loanInfo);
        
        /** 获取一次性结清金额 **/
        BigDecimal onetimeRepaymentAmount = calculatorInstace.getOnetimeRepaymentAmount(loanInfo.getId(), repay.getTradeDate(), overDateList);
        
        //一次性（提前还款）
        if((repay.getTradeCode().equals(Const.TRADE_CODE_ONEOFF))
                &&(tranAmt.compareTo(onetimeRepaymentAmount) >= 0)){
            //一次性结清还款哪怕是T日也不分账，只在【日终】分账
            int i = offerFlowService.fullRepaymentDeal(repay,loanInfo,tranAmt,overDateList);
            if(i==1){//处理结束，下面的不再处理
                logger.debug("fullRepaymentDeal一次性还款的flow账务处理结束，repayDeal返回,loanId="+loanInfo.getId());
                return;
            }
        } else if((repay.getTradeCode().equals(Const.TRADE_CODE_NORMAL)) //正常还款
                ||(repay.getTradeCode().equals(Const.TRADE_CODE_ONEOFF))) {
            //根据交易日期、约定还款日取得当期的还款日
            Date currTermReturnDate = getCurrTermReturnDate(repay.getTradeDate(),loanInfo.getPromiseReturnDate());
            /** 还没到还款日&申请过一次性提前结清&不能够足额结清 **/
            /** 还没到还款日&申请过提前扣款 **/
            /** 到了还款日&申请过一次性提前结清&不能够足额结清&非挂账交易 **/
            if(currTermReturnDate.compareTo(repay.getTradeDate()) > 0 //交易日期【早于】当期还款日（提前还款）
                    //非日终处理时，一次性还款金额不足时挂账
                    || ((repay.getTradeCode().equals(Const.TRADE_CODE_ONEOFF))
                            &&(!(repay.getTradeType().equals(TradeTypeEnum.挂账.getValue()))))
                    )
            {
                //如果记账金额是0，此处直接返回，不然记账方法会报错，老系统中不检测金额，如果0就不记
                if(tranAmt.compareTo(new BigDecimal("0.01")) < 0){
                    logger.debug("正常还款501行记账金额是0，repayDeal返回,loanId="+loanInfo.getId());
                    return;
                }
                offerFlowService.accountRechargeDeal(repay,tranAmt);
                logger.debug("accountRechargeDeal，金额挂账，账户充值的flow账务处理结束2，repayDeal返回,loanId="+loanInfo.getId());
                return ;
             }
            /** 到了还款日&申请过一次性提前结清&挂账交易【一次性结清的分账统一放在日终做】 **/
            /** 到了还款日&正常还款&挂账交易【T日日终要分以前入的账】 **/
            /** 到了还款日&正常还款&非挂账交易 【T日有钱进来就立马分账】**/
            LoanRepaymentDetail lastRep = getLast(overDateList);
             
            tranAmt = offerFlowService.normalRepaymentDeal(repay,loanInfo,lastRep,tranAmt);
             
            // 不足时 是否当作正常还款流程 还是挂账？？？？？
            if (repay.getTradeCode().equals(Const.TRADE_CODE_ONEOFF)) {
                loanSpecialRepaymentServiceImpl.updateSpecialRepaymentToEnd(loanInfo.getId(), SpecialRepaymentTypeEnum.一次性还款.getValue(),"申请");
            }
             
            loanSpecialRepaymentServiceImpl.updateSpecialRepaymentToEnd(loanInfo.getId(), SpecialRepaymentTypeEnum.提前扣款.getValue(), "申请");
            return;
        }
    }
    
    /**
     * modify by liuyh 
     * 【陆金所】的正常还款处理（提前还款、提前结清、T日正常还款）
     */
    private void lufaxRepayDeal(OfferRepayInfo repay, VLoanInfo loanInfo, List<LoanRepaymentDetail> overDateList){
        //正常还款金额 = 交易金额+挂账金额-逾期入账金额【如果有逾期的情况】
        BigDecimal tranAmt = transaction.get();
        /** 获取计算器实例 **/
        ICalculator calculatorInstace = CalculatorFactoryImpl.createCalculator(loanInfo);
        
        /** 获取一次性结清金额 **/
        BigDecimal onetimeRepaymentAmount = calculatorInstace.getOnetimeRepaymentAmount(loanInfo.getId(), repay.getTradeDate(), overDateList);
        
        //获取未结清的当期(这里可能是结清)
        LoanRepaymentDetail lastRep = getLast(overDateList);
        // 是否满足提前结清
        boolean isOneTimeRepayment = (repay.getTradeCode().equals(Const.TRADE_CODE_ONEOFF) || this.isOneTimeRepayment(loanInfo.getId())) 
                && (tranAmt.compareTo(onetimeRepaymentAmount) >= 0);
        //一次性提前还款【并且足额】
        if(isOneTimeRepayment){
            //实时结清分账
            int i = offerFlowService.realtimeFullRepaymentDealLufax(repay, loanInfo, tranAmt, overDateList, onetimeRepaymentAmount);
            if(i==1){//处理结束，下面的不再处理
                logger.debug("陆金所一次性还款实时分账的flow账务处理结束，repayDeal返回,loanId="+loanInfo.getId());
                return;
            }
        } /** 非结清，或者结清金额不足，都需对当期实时分账 【应陆金所特别要求】**/
        else if((repay.getTradeCode().equals(Const.TRADE_CODE_NORMAL)) //正常还款
                ||(repay.getTradeCode().equals(Const.TRADE_CODE_ONEOFF))) {
            //如果记账金额是0，此处直接返回，不然记账方法会报错，老系统中不检测金额，如果0就不记
            if(tranAmt.compareTo(new BigDecimal("0.01")) < 0){
                logger.debug("正常还款501行记账金额是0，repayDeal返回,loanId="+loanInfo.getId());
                return;
            }
            
            //如果当期已结清分账过（结清状态），则不需要再对当期分账，直接挂账即可
            if(RepaymentStateEnum.结清.name().equals(lastRep.getRepaymentState())){
                offerFlowService.accountRechargeDeal(repay,tranAmt);
                logger.debug("accountRechargeDeal,一次性还款金额依然不足，继续挂账,repayDeal返回,loanId="+loanInfo.getId());
                return;
            }
            
            tranAmt = offerFlowService.normalRepaymentDeal(repay,loanInfo,lastRep,tranAmt);
            logger.info("陆金所实时正常分账结束-->{" + "债权Id：" + loanInfo.getId() + ",挂账金额：" + tranAmt + "}");
            
            if(isAdvanceRepayment(loanInfo.getId())){
                loanSpecialRepaymentServiceImpl.updateSpecialRepaymentToEnd(loanInfo.getId(), SpecialRepaymentTypeEnum.提前扣款.getValue(), "申请");
            }
        }
        // 创建是由证大扣款的划扣队列数据
        this.createNormalRepayDebitQueue(repay, loanInfo, lastRep, isOneTimeRepayment);
    }
    
    @Transactional(propagation = Propagation.REQUIRED)
    public void repayDeal(OfferRepayInfo repay) {
    	VLoanInfo loanInfo = vLoanInfoServiceImpl.findByLoanId(repay.getLoanId());
    	boolean isError = false;
    	try {
            // 实际还款金额
            BigDecimal tranAmt = repay.getAmount().setScale(2, RoundingMode.HALF_UP);

            // ①存款、取款入账处理
            if (depositAndDrawDeal(repay, loanInfo)) {
                return;
            }

            /* =========以下是还款流程===================== */
            List<LoanRepaymentDetail> overDateList = getAllInterestOrLoan(repay.getTradeDate(), repay.getLoanId());

            // ②历史还款是结清的，当期还款也是结清的，后面再进来的钱直接挂账
            if (unnecessaryRepayDeal(repay, loanInfo, overDateList)) {
                return;
            }

            // ③逾期还款处理
            if(overdueRepayDeal(repay, loanInfo, overDateList, tranAmt)) {
    			return;
    		}

            if (FundsSourcesTypeEnum.陆金所.getValue().equals(loanInfo.getLoanBelong())) {
                // ④正常还款处理（提前还款、提前结清、T日正常还款）
                lufaxRepayDeal(repay, loanInfo, overDateList);
            } else {            // ④正常还款处理（提前还款、提前结清、T日正常还款、T日挂账分账）

                normalRepayDeal(repay, loanInfo, overDateList);
            }
    		if (repay.getTradeCode().equals(Const.TRADE_CODE_ONEOFF) && repay.getTradeType().equals(TradeTypeEnum.挂账.getValue())) {
    			specialRepaymentApplyService.reliefRepayDealFinish(repay);
    		}
    		transaction.remove();
    	}catch (Exception ex){
    		isError = true;
    		throw ex;
    	}finally {
    		/** 入账（开户、还款、提现、预存等）之后触发特殊业务逻辑（区分债权去向） **/
    		try {
    			if (isError == false) {
    				ICalculator calculator = CalculatorFactoryImpl.createCalculatorByLoanBelong(loanInfo);
        		    calculator.enterAccountAfter(repay);
    			}
    		} catch(Exception ex) {
    			logger.warn("入账（开户、还款、提现、预存等）之后触发特殊业务逻辑 出现异常",ex);
    		}
    	}
    }
    
    /**
     * 查询本次还款入账的本息和
     * @param tradeNo
     * @return
     */
    public BigDecimal queryPrincipalAmount(String tradeNo) {
        BigDecimal principalAmount = BigDecimal.ZERO;
        // 跟据交易流水查询分账明细数据
        List<OfferFlow> flowList = offerFlowService.findByTradeNo(tradeNo);
        if (CollectionUtils.isEmpty(flowList)) {
            return principalAmount;
        }
        for (OfferFlow flow : flowList) {
            if (Const.ACCOUNT_TITLE_INTEREST_EXP.equals(flow.getAcctTitle())
                    || Const.ACCOUNT_TITLE_LOAN_AMOUNT.equals(flow.getAcctTitle())) {
                principalAmount = principalAmount.add(flow.getTradeAmount());
            }
        }
        return principalAmount;
    }
    
    /**
     * 查询某笔还款分账的当期本金和利息之和
     * @param tradeNo
     * @param detail
     * @return
     */
    public BigDecimal queryNormalPrincipalAmount(String tradeNo, LoanRepaymentDetail detail) {
        BigDecimal principalAmount = BigDecimal.ZERO;
        // 跟据交易流水查询分账明细数据
        List<OfferFlow> flowList = offerFlowService.findByTradeNo(tradeNo);
        if (CollectionUtils.isEmpty(flowList)) {
            return principalAmount;
        }
        // 当前还款期数
        Long currentTerm = detail.getCurrentTerm();
        for (OfferFlow flow : flowList) {
            if (Const.ACCOUNT_TITLE_INTEREST_EXP.equals(flow.getAcctTitle()) || Const.ACCOUNT_TITLE_LOAN_AMOUNT.equals(flow.getAcctTitle())) {
                if(Long.parseLong(flow.getMemo2()) == currentTerm){
                    principalAmount = principalAmount.add(flow.getTradeAmount());
                }
            }
        }
        return principalAmount;
    }
    
    /**
     * 查询某笔还款分账的逾期本金和利息之和
     * @param tradeNo
     * @param detail
     * @return
     */
    public BigDecimal queryOverduePrincipalAmount(String tradeNo, LoanRepaymentDetail detail) {
        BigDecimal principalAmount = BigDecimal.ZERO;
        // 跟据交易流水查询分账明细数据
        List<OfferFlow> flowList = offerFlowService.findByTradeNo(tradeNo);
        if (CollectionUtils.isEmpty(flowList)) {
            return principalAmount;
        }
        // 当前还款期数
        Long currentTerm = detail.getCurrentTerm();
        for (OfferFlow flow : flowList) {
            if (Const.ACCOUNT_TITLE_INTEREST_EXP.equals(flow.getAcctTitle()) || Const.ACCOUNT_TITLE_LOAN_AMOUNT.equals(flow.getAcctTitle())) {
                if(Long.parseLong(flow.getMemo2()) < currentTerm){
                    principalAmount = principalAmount.add(flow.getTradeAmount());
                }
            }
        }
        return principalAmount;
    }
    
    /**
     * 创建逾期还款划扣队列、分账队列数据
     * @param repay
     * @param loanInfo
     * @param repaymentDetail
     * @param amount
     */
    private void createOverdueRepayDebitQueue(OfferRepayInfo repay, VLoanInfo loanInfo, LoanRepaymentDetail repaymentDetail, BigDecimal amount) {
        // 债权编号
        Long loanId = loanInfo.getId();
        // 合同来源
        String fundsSource = loanInfo.getFundsSources();
        // 是否创建划扣队列
        boolean isCreateDebitQueue_1 = FundsSourcesTypeEnum.陆金所.getValue().equals(fundsSource)
                && repay.isCreateDebitQueue()
                && amount.compareTo(BigDecimal.ZERO) > 0;
        // 根据债权编号查询此笔债权一次性回购代垫明细记录
        CompensatoryDetailLufax buyBackCompensatoryDetailLufax = debitQueueLogService.queryBuyBackCompensatoryDetailLufax(loanId);
        // 没有发生一次性回购或者发生一次性回购、追偿回购期数之前期数的欠款
        boolean isCreateDebitQueue_2 = buyBackCompensatoryDetailLufax == null
                || buyBackCompensatoryDetailLufax.getTerm().intValue() > repaymentDetail.getCurrentTerm().intValue();
        if (isCreateDebitQueue_1 && isCreateDebitQueue_2) {
            try {
                // 创建划扣队列
                DebitQueueLog debitQueueLog = new DebitQueueLog();
                debitQueueLog.setLoanId(loanInfo.getId());
                debitQueueLog.setTradeNo(repay.getTradeNo());
                debitQueueLog.setBatchId(LufaxUtil.createAnshuoBatchId());
                debitQueueLog.setDebitNo(LufaxUtil.createAnshuoSerialno());
                debitQueueLog.setDebitNotifyState(DebitNotifyStateEnum.通知成功.getCode());
                debitQueueLog.setDebitResultState(DebitResultStateEnum.划扣成功.getCode());
                debitQueueLog.setDebitType(DebitOperateTypeEnum.当期追偿.getCode());
                debitQueueLog.setAmount(amount);
                debitQueueLog.setPayParty(PayPartyEnum.借款人.getCode());
                debitQueueLog.setRepayType(DebitRepayTypeEnum.逾期还回.getCode());
                // 逾期还款对应的期数
                debitQueueLog.setRepayTerm(repaymentDetail.getCurrentTerm());
                debitQueueLogService.saveDebitQueueLog(debitQueueLog);

                SplitQueueLog splitQueueLog = new SplitQueueLog();
                splitQueueLog.setLoanId(loanId);
                splitQueueLog.setDebitNo(debitQueueLog.getDebitNo());
                splitQueueLog.setSplitNotifyState(SplitNotifyStateEnum.待通知.getCode());
                splitQueueLog.setSplitResultState(SplitResultStateEnum.未分账.getCode());
                // 未结算
                splitQueueLog.setPayOffType(PayOffTypeEnum.未结清.getCode());
                // lufax还款请求号
                splitQueueLog.setSplitNo(null);
                // 发送分账时的安硕批次号
                splitQueueLog.setBatchId(debitQueueLog.getBatchId());
                // 冻结金额默认是客户逾期还款的金额
                splitQueueLog.setFrozenAmount(amount);
                splitQueueLogService.createSplitQueueLog(splitQueueLog);
            } catch (Exception e) {
                logger.error("创建逾期还款划扣队列数据异常", e);
            }
        }
    }
    
    /**
     * 创建正常还款给公司的划扣队列、分账队列数据（对公还当期）
     * @param repay
     * @param loanInfo
     * @param repaymentDetail
     * @param isOneTimeRepayment
     */
    private void createNormalRepayDebitQueue(OfferRepayInfo repay, VLoanInfo loanInfo, LoanRepaymentDetail repaymentDetail, boolean isOneTimeRepayment) {
        // 债权去向
        String loanBelong = loanInfo.getLoanBelong();
        // 查询正常还款或一次性结清还款分账的本金和利息之和（不包含逾期还款分账的本金、利息）
        BigDecimal splitAmt = this.queryNormalPrincipalAmount(repay.getTradeNo(), repaymentDetail);
        // 是否创建划扣队列
        boolean isCreateDebitQueue = FundsSourcesTypeEnum.陆金所.getValue().equals(loanBelong)
                && repay.isCreateDebitQueue()
                && splitAmt.compareTo(BigDecimal.ZERO) > 0;
        if(isCreateDebitQueue){
            try {
                // 创建划扣队列
                DebitQueueLog debitQueueLog = new DebitQueueLog();
                debitQueueLog.setLoanId(loanInfo.getId());
                debitQueueLog.setTradeNo(repay.getTradeNo());
                debitQueueLog.setDebitNotifyState(DebitNotifyStateEnum.待通知.getCode());
                debitQueueLog.setDebitResultState(DebitResultStateEnum.未划扣.getCode());
                debitQueueLog.setPayParty(PayPartyEnum.准备金.getCode());
                debitQueueLog.setDebitType(DebitOperateTypeEnum.当期代偿.getCode());
                debitQueueLog.setRepayType(DebitRepayTypeEnum.委托还款.getCode());
                if(isOneTimeRepayment){
                	 debitQueueLog.setPayParty(PayPartyEnum.服务公司.getCode());
                    debitQueueLog.setDebitType(DebitOperateTypeEnum.提前还款代扣.getCode());
                    debitQueueLog.setRepayType(DebitRepayTypeEnum.提前结清.getCode());
                }
                debitQueueLog.setAmount(splitAmt);
                // 还款对应期数
                debitQueueLog.setRepayTerm(repaymentDetail.getCurrentTerm());
                debitQueueLogService.saveDebitQueueLog(debitQueueLog);
            } catch (Exception e) {
                logger.error("创建委托还款或提前结清划扣队列数据异常", e);
            }
        }
    }
    
    public BigDecimal getFine(LoanRepaymentDetail detail, Date currDate) {
        VLoanInfo loanInfo = vLoanInfoServiceImpl.findByLoanId(detail.getLoanId());
        return this.getFine(detail, currDate, loanInfo, FundsSourcesTypeEnum.陆金所.getValue());
    }
    
    public BigDecimal getFine(LoanRepaymentDetail detail, Date currDate, VLoanInfo loanInfo, String type) {
        BigDecimal result = BigDecimal.ZERO;
        // 时间格式化
        currDate = Dates.parse(Dates.getDateTime(currDate, Dates.DEFAULT_DATE_FORMAT), Dates.DEFAULT_DATE_FORMAT);
        if (!FundsSourcesTypeEnum.陆金所.getValue().equals(type) && RepaymentStateEnum.结清.name().equals(detail.getRepaymentState())) {
            return result;
        }
        if (detail.getReturnDate().compareTo(currDate) >= 0) {
            return result;
        }
        // 逾期天数
        int overdueDay = Dates.dateDiff(detail.getPenaltyDate(), currDate);
        if (overdueDay <= 0) {
            return result;
        }
        // 合同来源
        String fundsSource = loanInfo.getFundsSources();
        // 当期剩余本金
        BigDecimal overdueCorpus = BigDecimal.ZERO;
        // 当期应还本金
        BigDecimal shouldRepayCorpus = detail.getReturneterm().subtract(detail.getCurrentAccrual());
        if (detail.getDeficit().compareTo(shouldRepayCorpus) >= 0) {
            overdueCorpus = shouldRepayCorpus;
        } else {
            overdueCorpus = detail.getDeficit();
        }
        // 需要把委托还款的本金用来计算罚息
        if(FundsSourcesTypeEnum.陆金所.getValue().equals(type)){
            // 委托还款垫付本金
            BigDecimal entrustCompensatoryCorpus = this.findEntrustCompensatoryCorpus(loanInfo, detail.getCurrentTerm());
            overdueCorpus = overdueCorpus.add(entrustCompensatoryCorpus);
        }
        // 罚息率
        BigDecimal penaltyRate = loanInfo.getPenaltyRate();
        if (FundsSourcesTypeEnum.包商银行.getValue().equals(fundsSource)) {
            // 通过日利率算出罚息
            penaltyRate = loanProductDao.findByLoanId(loanInfo.getId()).getRateed().multiply(new BigDecimal("0.5"));
        } else if (FundsSourcesTypeEnum.陆金所.getValue().equals(fundsSource)) {
            // 获取陆金所起始预期日到当前时间的逾期天数、剔除掉节假日，用于判断是否在宽限期
            int lufaxOverdueDay = workDayInfoService.getWorkDaysInRegion(detail.getReturnDate(), currDate);
            // 逾期还款还在宽限期内、不需缴纳罚息
            if (lufaxOverdueDay <= 3) {
                return result;
            }
        } 
        result = overdueCorpus.multiply(new BigDecimal(overdueDay)).multiply(penaltyRate);
        // 四舍五入
        result = result.setScale(2, RoundingMode.HALF_UP);
        return result;
    }
    
    /**
     * 查询一笔还款当时所在期数
     * @param loanInfo
     * @param repay
     * @return
     */
    private Long getRepaymentTerm(VLoanInfo loanInfo, OfferRepayInfo repay){
        if(repay.getTradeDate().compareTo(loanInfo.getEndrdate()) > 0){
            return loanInfo.getTime();
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("loanId", repay.getLoanId());
        params.put("repayDate", repay.getTradeDate());
        return debitQueueLogService.getRepaymentCurrentTerm(params);
    }
    
    /**
     * 查询逾期还款对应的最后还款期数，不包含当期
     * @param loanInfo
     * @param repay
     * @return
     */
    public Long getOverdueRepaymentMaxTerm(VLoanInfo loanInfo, OfferRepayInfo repay){
        Long currentTerm = this.getRepaymentTerm(loanInfo, repay);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("tradeNo", repay.getTradeNo());
        if(currentTerm.intValue() == loanInfo.getTime().intValue()){
            params.put("lastTerm", currentTerm);
        } else {
            params.put("currentTerm", currentTerm);
        }
        Map<String, Object> termMap = debitQueueLogService.getOverdueRepaymentTerm(params);
        BigDecimal maxTerm = (BigDecimal) termMap.get("MAX_TERM");
        return maxTerm.longValue();
    }
    
    public void accountingByTransaction(DebitTransaction transaction) {
        RepaymentInputVo repaymentInputVo = offerRepayInfoService.createRepaymentInputVo(transaction);
        offerRepayInfoService.dealRepaymentInput(repaymentInputVo);
    }
    
    /**
     * 委托还款垫付本金
     * @param loanInfo
     * @param currentTerm
     * @return
     */
    private BigDecimal findEntrustCompensatoryCorpus(VLoanInfo loanInfo, Long currentTerm){
        // 委托还款垫付本金
        BigDecimal entrustCompensatoryCorpus = BigDecimal.ZERO;
        if (FundsSourcesTypeEnum.陆金所.getValue().equals(loanInfo.getFundsSources())) {
            // 查询逾期代垫明细记录  
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("loanId", loanInfo.getId());
            params.put("term", currentTerm);
            // 垫付类型是：委托还款垫付
            params.put("type", "03");
            List<CompensatoryDetailLufax> compensatoryDetailList = compensatoryDetailLufaxDao.findListByMap(params);
            if(CollectionUtils.isNotEmpty(compensatoryDetailList)){
                for(CompensatoryDetailLufax compensatoryDetailLufax : compensatoryDetailList){
                    entrustCompensatoryCorpus = entrustCompensatoryCorpus.add(compensatoryDetailLufax.getCorpusAmount());
                }
            }
        }
        return entrustCompensatoryCorpus;
    }
}
