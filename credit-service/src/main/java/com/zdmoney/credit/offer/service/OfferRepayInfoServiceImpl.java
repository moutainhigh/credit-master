package com.zdmoney.credit.offer.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zdmoney.credit.job.RepayResultDisposeBsyhJob;
import com.zdmoney.credit.loan.vo.RelieCalculateAmountParamVo;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.common.constant.GatewayFuncIdEnum;
import com.zdmoney.credit.common.constant.LoanStateEnum;
import com.zdmoney.credit.common.constant.LoanTypeEnum;
import com.zdmoney.credit.common.constant.OfferTransactionStateEnum;
import com.zdmoney.credit.common.constant.SpecialRepaymentStateEnum;
import com.zdmoney.credit.common.constant.SpecialRepaymentTypeEnum;
import com.zdmoney.credit.common.constant.TradeKindEnum;
import com.zdmoney.credit.common.constant.TradeTypeEnum;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.constant.tpp.TppPaySysNoEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.gateway.GatewayUtils;
import com.zdmoney.credit.common.util.Assert;
import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.util.TPPHelper;
import com.zdmoney.credit.common.vo.core.FinanceVo;
import com.zdmoney.credit.core.calculator.factory.CalculatorFactoryImpl;
import com.zdmoney.credit.core.calculator.pub.ICalculator;
import com.zdmoney.credit.debit.dao.pub.IDebitBaseInfoDao;
import com.zdmoney.credit.debit.dao.pub.IDebitOfflineOfferInfoDao;
import com.zdmoney.credit.debit.domain.DebitBaseInfo;
import com.zdmoney.credit.debit.domain.DebitOfflineOfferInfo;
import com.zdmoney.credit.debit.domain.DebitTransaction;
import com.zdmoney.credit.debit.service.pub.ISplitQueueLogService;
import com.zdmoney.credit.fee.service.pub.ILoanFeeInfoService;
import com.zdmoney.credit.framework.vo.common.BaseParamVo;
import com.zdmoney.credit.framework.vo.wm3.entity.DeductMoneyDetailListEntity;
import com.zdmoney.credit.framework.vo.wm3.entity.PaidInMoneyListEntity;
import com.zdmoney.credit.framework.vo.wm3.input.WM3_2312Vo;
import com.zdmoney.credit.loan.dao.pub.IVLoanInfoDao;
import com.zdmoney.credit.loan.domain.LoanLedger;
import com.zdmoney.credit.loan.domain.LoanRepaymentDetail;
import com.zdmoney.credit.loan.domain.LoanSpecialRepayment;
import com.zdmoney.credit.loan.domain.SpecialRepaymentApply;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.IAfterLoanService;
import com.zdmoney.credit.loan.service.pub.ILoanLogService;
import com.zdmoney.credit.loan.service.pub.ILoanSpecialRepaymentService;
import com.zdmoney.credit.loan.service.pub.ISpecialRepaymentApplyService;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import com.zdmoney.credit.loan.vo.ReliefAmountCalculateVo;
import com.zdmoney.credit.offer.dao.pub.IOfferRepayInfoDao;
import com.zdmoney.credit.offer.dao.pub.IOfflineRepayRelationDao;
import com.zdmoney.credit.offer.domain.OfferInfo;
import com.zdmoney.credit.offer.domain.OfferRepayInfo;
import com.zdmoney.credit.offer.domain.OfferTransaction;
import com.zdmoney.credit.offer.domain.OfflineRepayRelation;
import com.zdmoney.credit.offer.service.pub.IOfferInfoService;
import com.zdmoney.credit.offer.service.pub.IOfferRepayInfoService;
import com.zdmoney.credit.offer.vo.OfferRepayInfoVo;
import com.zdmoney.credit.offer.vo.RepaymentInputVo;
import com.zdmoney.credit.system.domain.ComEmployee;
import com.zdmoney.credit.system.service.pub.ISequencesService;
import com.zendaimoney.thirdpp.vo.enums.ThirdPartyType;

@Service
public class OfferRepayInfoServiceImpl implements IOfferRepayInfoService{
	
	private static final Logger logger = Logger.getLogger(OfferRepayInfoServiceImpl.class);
	
	@Autowired
	IOfferRepayInfoDao offerRepayInfoDao;

	@Autowired
	IAfterLoanService afterLoanService;
	
	@Autowired
	IOfferInfoService offerInfoService;
	
	@Autowired
    private  IVLoanInfoService loanService;
	
	@Autowired
    private  IVLoanInfoDao loanInfoDao;
	
	@Autowired
	private ILoanLogService loanLogService;
	
	@Autowired @Qualifier("sequencesServiceImpl")
	ISequencesService sequencesServiceImpl;
	
	@Autowired @Qualifier("loanSpecialRepaymentServiceImpl")
	ILoanSpecialRepaymentService loanSpecialRepaymentServiceImpl;
	
	@Autowired @Qualifier("afterLoanServiceImpl")
	IAfterLoanService afterLoanServiceImpl;
	
	@Autowired
	private ILoanFeeInfoService loanFeeInfoService;
	
	@Autowired
	private IDebitOfflineOfferInfoDao debitOfflineOfferInfoDao;
	
	@Autowired
	private IOfflineRepayRelationDao offlineRepayRelationDao;
	
	@Autowired
	private ISplitQueueLogService splitQueueLogService;

	@Autowired
	ISpecialRepaymentApplyService specialRepaymentApplyService;
	
	@Autowired
	private IDebitBaseInfoDao debitBaseInfoDao;
	
	@Autowired
    private RepayResultDisposeBsyhJob job;
	
	@Override
	public OfferRepayInfo builderRepayInfo(OfferTransaction offerTransaction) {
		OfferRepayInfo repayInfo = new OfferRepayInfo();
		repayInfo.setId(sequencesServiceImpl.getSequences(SequencesEnum.OFFER_REPAY_INFO));
		repayInfo.setLoanId(offerTransaction.getLoanId());
		repayInfo.setTradeDate(offerTransaction.getReqTime());
		boolean isOneTime = afterLoanService.isOneTimeRepayment(offerTransaction.getLoanId());
		repayInfo.setTradeCode(isOneTime ? Const.TRADE_CODE_ONEOFF : Const.TRADE_CODE_NORMAL);
		repayInfo.setAmount(offerTransaction.getActualAmount());//1.0接口不会有部分成功，直接拿报盘金额
		repayInfo.setTradeType(getPaySysNoString(offerTransaction.getPaySysNoReal()));//这个方法还没实现
		repayInfo.setTradeNo(afterLoanService.getTradeFlowNo(offerTransaction.getLoanId()));//这个方法从新放位置
		repayInfo.setTradeKind(TradeKindEnum.正常交易.getValue());
		repayInfo.setTeller(TPPHelper.TPP_HANDLER_TELLER);
		repayInfo.setOrgan(TPPHelper.TPP_HANDLER_ORGANIZATION_CODE);
		repayInfo.setOfferid(offerTransaction.getLoanId());
		repayInfo.setCreateTime(new Date());
		offerRepayInfoDao.insert(repayInfo);
		return repayInfo;
		
	}

	/**
	 * 根据thirdPartyType 返回中文名称
	 * @param thirdPartyType
	 * @return
	 */
	private String formatThirdPartyType(String thirdPartyType) {
		String result = "";
		if(ThirdPartyType.Fuiou.name().equals(thirdPartyType)){
			result = "富友代扣";
		}else if(ThirdPartyType.Allinpay.name().equals(thirdPartyType)){
			result = "通联代扣";
		}else if(ThirdPartyType.SHUnionpay.name().equals(thirdPartyType)){
			 result = "上海银联代扣";
		}else{
			result = thirdPartyType.toString();
		}
        return result;
	}
	
	/**
	 * 划扣通道转中文
	 * @param PaySysNo
	 * @return
	 */
	private String getPaySysNoString(String PaySysNo){
        return TppPaySysNoEnum.get(PaySysNo).getValue();
	}
	
	/**
     * 返回第三方平台  Allinpay(通联)/Fuiou(富友)/THIRD_PARTY_TYPE_02(汇付)
     * @author zhangho
     * @date 2013-10-16
     * @param offer
     * @return
     */
	private String getThirdPartyType(OfferTransaction offerTransaction) {
		//查找报盘文件
		OfferInfo offerInfo = offerInfoService.findOfferById(offerTransaction.getOfferId());
		
		if(Strings.isNotEmpty(offerInfo.getPaySysNoReal())){
			return offerInfo.getPaySysNoReal();
		}
        else{
        	return offerInfo.getBankCode().equals(TPPHelper.ICBC) ? ThirdPartyType.Fuiou.name() : ThirdPartyType.Allinpay.name();
        }
            
	}


	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void createAccountingByRepaymentDayDeal(LoanLedger loanLedger,
			Calendar tradeDate) {
		//生成流水
   	 	OfferRepayInfo repayInfoInstance = new OfferRepayInfo();
   	 	repayInfoInstance.setId(sequencesServiceImpl.getSequences(SequencesEnum.OFFER_REPAY_INFO));
		repayInfoInstance.setLoanId(loanLedger.getLoanId());
        repayInfoInstance.setAmount(new BigDecimal("0"));//这里不能输入金额，还款处理方法中自动获取
        repayInfoInstance.setMemo("挂账日终自动处理");
        
        VLoanInfo loanInfo = loanService.findByLoanId(loanLedger.getLoanId());
        repayInfoInstance.setOrgan(loanInfo!=null?String.valueOf(loanInfo.getSalesDepartmentId()):"999"); //  riOrgan=Loan.get(Long.parseLong(row.lrAccount))?.salesDepartment?.id?.toString()?: "999";
        repayInfoInstance.setTeller(Const.ENDOFDAY_TELLER);
        repayInfoInstance.setTradeCode(afterLoanService.isOneTimeRepayment(loanLedger.getLoanId())?Const.TRADE_CODE_ONEOFF:Const.TRADE_CODE_NORMAL);//riTradeCode=afterLoanService.isOneTimeRepayment(Long.parseLong(row.lrAccount))?Const.TRADE_CODE_ONEOFF:Const.TRADE_CODE_NORMAL;
        repayInfoInstance.setTradeDate(tradeDate.getTime());
        
        repayInfoInstance.setTradeKind(TradeKindEnum.正常交易.getValue());
        repayInfoInstance.setTradeType(TradeTypeEnum.挂账.getValue());
        repayInfoInstance.setTradeNo(afterLoanService.getTradeFlowNo(loanLedger.getLoanId()));//riTradeNo=afterLoanService.getTradeFlowNo(row.lrAccount);
        repayInfoInstance.setCreateTime(new Date());
        offerRepayInfoDao.insert(repayInfoInstance);
        try
        {
       	 	afterLoanService.repayDeal(repayInfoInstance);
        }
        catch(PlatformException ex)
        {
        	if(ex.getResponseCode().name().equals(ResponseEnum.SYS_WARN.name())){
        		throw ex;   //可以忽略的异常，不打印，直接抛出，回滚
        	}else{
        		logger.error("日终处理：还款日挂账部分处理flow异常！loanId="+loanLedger.getLoanId()+ex.getMessage(),ex);
        		throw new RuntimeException("日终处理：还款日挂账部分处理flow异常！"+ex.getMessage());  
        	}
        }
        catch(Exception ex)
        {
	       	 logger.error("日终处理：还款日挂账部分处理flow异常！loanId="+loanLedger.getLoanId()+ex.getMessage(),ex);
	       	 //写日志
	       	 loanLogService.createLog("EndOfDay", "error", "日终处理：还款日挂账部分处理flow异常!loanId="+loanLedger.getLoanId()+ex.getMessage(), "SYSTEM");
	         throw new RuntimeException("日终处理：还款日挂账部分处理flow异常！"+ex.getMessage());   
        }
	}
	
	/***
	 * 跟据Map查询结果集
	 * @param map 参数集合
	 * @return
	 */
	@Override
	public List<OfferRepayInfo> findListByMap(Map paramMap) {
		return offerRepayInfoDao.findListByMap(paramMap);
	}

	@Override
	public void builderRepayInfoFinancing(VLoanInfo loan, ComEmployee employee) {
		OfferRepayInfo repayInfo = new OfferRepayInfo();
		repayInfo.setId(sequencesServiceImpl.getSequences(SequencesEnum.OFFER_REPAY_INFO));
        repayInfo.setAmount(loan.getPactMoney());
        repayInfo.setLoanId(loan.getId());
        repayInfo.setOrgan(String.valueOf(employee.getOrgId()));
        repayInfo.setTeller(employee.getUsercode());
        if (loan.getLoanType().equals(LoanTypeEnum.助学贷)) {
        	repayInfo.setTradeCode(Const.TRADE_CODE_OPENACC_ASC);
        }
        else{
        	repayInfo.setTradeCode(Const.TRADE_CODE_OPENACC);
        }
        repayInfo.setTradeKind(TradeKindEnum.正常交易.getValue());
        repayInfo.setTradeType(TradeTypeEnum.现金.getValue());
        repayInfo.setTradeDate(new Date());
        repayInfo.setCreateTime(new Date());
        repayInfo.setTradeNo(afterLoanService.getTradeFlowNo(loan.getId()));
        offerRepayInfoDao.insert(repayInfo);
        try
        {
       	 	afterLoanService.repayDeal(repayInfo);
        }
        catch(Exception ex)
        {
	       	 logger.error("财务放款：repayDeal处理异常！"+ex.getMessage(),ex);
	         throw new RuntimeException("财务放款：repayDeal处理异常！"+ex.getMessage());   
        }
	}

	@Override
	public List<OfferRepayInfo> findListByFinanceVo(FinanceVo params) {
		
		return offerRepayInfoDao.findListByFinanceVo(params);
	}
	
	/***
	 * 查询还款汇总信息
	 * @return
	 */
	@Override
	public OfferRepayInfoVo getRepayInfo(Long loanId,Date tradeDate) {
		OfferRepayInfoVo offerRepayInfoVo = new OfferRepayInfoVo();
		
		VLoanInfo vLoanInfo = loanService.findByLoanId(loanId);
		Assert.notNull(vLoanInfo, ResponseEnum.FULL_MSG, "未找到债权数据loanId:" + loanId);
		
		/** 获取计算器实例 **/
		ICalculator calculatorInstace = CalculatorFactoryImpl.createCalculator(vLoanInfo);
		
		List<LoanRepaymentDetail> repayList = afterLoanService.getAllInterestOrLoan(tradeDate,loanId);
		BigDecimal accAmount = afterLoanService.getAccAmount(loanId);
		if (accAmount == null) {
			accAmount = new BigDecimal(0);
		}
		offerRepayInfoVo.setAccAmount(accAmount);
		
		if (CollectionUtils.isNotEmpty(repayList)) {
			/** 以下获取逾期还款信息 **/
			/** 获取逾期的总期数 **/
			int overDueTerm = afterLoanService.getOverdueTermCount(repayList,tradeDate);
			/** 逾期本息和 不包含罚息 **/
			BigDecimal overAmount = afterLoanService.getOverdueAmount(repayList,tradeDate);
			/** 逾期本金 **/
			BigDecimal overCorpus = afterLoanService.getOverdueCorpus(repayList,tradeDate);
			/** 逾期利息 **/
			BigDecimal overInterest = afterLoanService.getOverdueInterest(repayList,tradeDate);
			
			/** 逾期日期 **/
			Date overDueDate = null;
			if (overDueTerm > 0) {
				overDueDate = repayList.get(0).getReturnDate();
			}
			offerRepayInfoVo.setOverDueTerm(overDueTerm);
			offerRepayInfoVo.setOverCorpus(overCorpus);
			offerRepayInfoVo.setOverInterest(overInterest);
			offerRepayInfoVo.setOverDueDate(overDueDate);
			offerRepayInfoVo.setOverAmount(overAmount);
			
			/** 以下获取逾期罚息相关信息 **/
			/** 获得罚息天数 **/
			int fineDay = afterLoanService.getOverdueDay(repayList,tradeDate);
			/** 根据当前还款日期、获取逾期罚息 **/
			BigDecimal fine = afterLoanService.getFine(repayList,tradeDate);
			if (fine == null) {
				fine = new BigDecimal(0); 
			}
			/** 罚息日期 **/
			Date fineDate = null;
			if (fineDay > 0) {
				fineDate = repayList.get(0).getPenaltyDate();
			}
			offerRepayInfoVo.setFineDay(fineDay);
			offerRepayInfoVo.setFine(fine);
			offerRepayInfoVo.setFineDate(fineDate);
            
			/** 以下获取当期的还款信息 **/
			/** 当期还款日 **/
			Date currDate = repayList.get(repayList.size() - 1).getReturnDate();
			/** 当期期数 **/
			Long currTerm = repayList.get(repayList.size() - 1).getCurrentTerm();
			/** 当期总额 **/
			BigDecimal currAmount = afterLoanService.getCurrAmount(repayList,tradeDate);
			/** 当期本金 **/
			BigDecimal currCorpus = afterLoanService.getCurrCorpus(repayList,tradeDate);
			/** 当期利息 **/
			BigDecimal currInterest = afterLoanService.getCurrInterest(repayList,tradeDate);
			
			offerRepayInfoVo.setCurrDate(currDate);
			offerRepayInfoVo.setCurrTerm(currTerm);
			offerRepayInfoVo.setCurrAmount(currAmount);
			offerRepayInfoVo.setCurrCorpus(currCorpus);
			offerRepayInfoVo.setCurrInterest(currInterest);
			
			/** 申请状态  包含：申请和取消申请 **/
			String requestState = afterLoanService.isOneTimeRepayment(loanId) ? "已申请" : "未申请";
			offerRepayInfoVo.setRequestState(requestState);
			
			/** 申请减免罚息金额 **/
			BigDecimal relief = afterLoanService.getReliefOfFine(tradeDate,loanId);
			offerRepayInfoVo.setRelief(relief);
			
			/** 根据罚息起始期,剩余本息和 **/
			BigDecimal remnant = afterLoanService.getRemnant(repayList);
			offerRepayInfoVo.setRemnant(remnant);
			
			/** 获取一次性结清金额 **/
			BigDecimal oneTimeRepayment = calculatorInstace.getOnetimeRepaymentAmount(loanId, tradeDate, repayList);
			
			offerRepayInfoVo.setOneTimeRepayment(oneTimeRepayment);
			
			/** 一次性还清总金额 **/
			BigDecimal allAmount = null;
			allAmount = overAmount.add(fine).add(oneTimeRepayment).subtract(accAmount).subtract(relief);
			if (allAmount.compareTo(BigDecimal.ZERO) == -1) {
				allAmount = BigDecimal.ZERO;
			}
			offerRepayInfoVo.setAllAmount(allAmount);
			
			/** 总金额 **/
			if (requestState.equalsIgnoreCase("已申请")) {
				
				/** 应还总额（不含当期） **/
				BigDecimal overdueAmount = new BigDecimal(0);
				/** 应还总额（含当期） **/
				BigDecimal currAllAmount = new BigDecimal(0);
				
				overdueAmount = overAmount.add(fine).subtract(accAmount).subtract(relief);
				if (overdueAmount.compareTo(BigDecimal.ZERO) == -1) {
					overdueAmount = BigDecimal.ZERO;
				}
				
				currAllAmount = overAmount.add(fine).add(oneTimeRepayment).subtract(accAmount).subtract(relief);
				if (currAllAmount.compareTo(BigDecimal.ZERO) == -1) {
					currAllAmount = BigDecimal.ZERO;
				}
				
				offerRepayInfoVo.setOverdueAmount(overdueAmount);
				offerRepayInfoVo.setCurrAllAmount(currAllAmount);
			} else {
				
				/** 应还总额（不含当期） **/
				BigDecimal overdueAmount = new BigDecimal(0);
				/** 应还总额（含当期） **/
				BigDecimal currAllAmount = new BigDecimal(0);
				
				overdueAmount = overAmount.add(fine).subtract(accAmount).subtract(relief);
				if (overdueAmount.compareTo(BigDecimal.ZERO) == -1) {
					overdueAmount = BigDecimal.ZERO;
				}
				currAllAmount = overAmount.add(fine).add(currAmount).subtract(accAmount).subtract(relief);
				if (currAllAmount.compareTo(BigDecimal.ZERO) == -1) {
					currAllAmount = BigDecimal.ZERO;
				}
				
				offerRepayInfoVo.setOverdueAmount(overdueAmount);
				offerRepayInfoVo.setCurrAllAmount(currAllAmount);
			}
			/**剩余本金**/
			offerRepayInfoVo.setResidualPactMoney(vLoanInfo.getResidualPactMoney());
			offerRepayInfoVo.setTradeDate(tradeDate);
			return offerRepayInfoVo;
		} else {
			logger.warn("借款编号：" + loanId + " 未获取到有效的还款计划信息");
			return null;
		}
	}

	@Override
	public List<OfferRepayInfo> getDrawJimuRiskRepayInfo(
			Map<String, Object> params) {
		return offerRepayInfoDao.getDrawJimuRiskRepayInfo(params);
	}

	@Override
	public void save(OfferRepayInfo repay) {
		offerRepayInfoDao.insert(repay);
	}

	@Override
	public OfferRepayInfo getLoanlastDrawRisk(Long loanId) {
		Map<String, Object> repayParam = new HashMap<String, Object>();
		repayParam.put("loanId", loanId);
		List<String> tradeCodes = new ArrayList<String>();
		tradeCodes.add(Const.TRADE_CODE_DRAWRISK);
		tradeCodes.add(Const.TRADE_CODE_DRAWRISK_STUDENT);
		repayParam.put("tradeCodes", tradeCodes);
		repayParam.put("sort", "trade_date desc");
		return offerRepayInfoDao.getLoanlastDrawRisk(repayParam);
	}

	@Override
	public OfferRepayInfo getLoanLastRepayInfoByTradeCode(Long loanId,
			String tradeCode) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("loanId", loanId);
		param.put("tradeCode", tradeCode);
		
		OfferRepayInfo result = offerRepayInfoDao.getLoanLastRepayInfoByTradeCode(param);
		return result;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public OfferRepayInfo repaymentInputCore(RepaymentInputVo repaymentInputVo) {
		Long loanId = repaymentInputVo.getLoanId();
		VLoanInfo vLoanInfo = loanService.findByLoanId(loanId);
		Assert.notNull(vLoanInfo, ResponseEnum.FULL_MSG,"未找到借款记录");
		if (!repaymentInputVo.isTppSouces()) {
			String loanState = Strings.convertValue(vLoanInfo.getLoanState(),String.class);
			if (loanState.equalsIgnoreCase(LoanStateEnum.正常.name()) || loanState.equalsIgnoreCase(LoanStateEnum.逾期.name())
                    || loanState.equalsIgnoreCase(LoanStateEnum.坏账.name())) {
                /** 可进行还款录入操作 **/
            } else {
                /** 不可进行还款录入操作 **/
                throw new PlatformException(ResponseEnum.FULL_MSG,"该笔借款无法进行还款录入操作[当前借款状态：" + loanState + "]");
            }

			// 针对龙信小贷、外贸信托、外贸2，判断是否已经完成划扣服务费，如果没有，则终止后续还款录入操作
			if(!loanFeeInfoService.isAlreadyDebitServiceCharge(loanId)){
				throw new PlatformException(ResponseEnum.FULL_MSG,"该笔借款没有完成划扣服务费，无法进行还款录入操作！");
			}
			
			// 针对陆金所，如果存在划扣中或者分账中的记录，不允许做还款录入操作
			/*if(!splitQueueLogService.isCanRepayment(loanId, new String[] { DebitRepayTypeEnum.一次性回购.getCode() })){
				throw new PlatformException(ResponseEnum.FULL_MSG,"该笔借款目前存在划扣中或者分账中的记录，暂不能进行还款录入操作！");
			}*/
		}
		
		OfferRepayInfo offerRepayInfo = new OfferRepayInfo();
		offerRepayInfo.setId(sequencesServiceImpl.getSequences(SequencesEnum.OFFER_REPAY_INFO));
	    offerRepayInfo.setLoanId(loanId);
	    offerRepayInfo.setAmount(repaymentInputVo.getAmount());
	    offerRepayInfo.setMemo(repaymentInputVo.getMemo());
	    offerRepayInfo.setOrgan(repaymentInputVo.getOrgan());
	    offerRepayInfo.setTeller(repaymentInputVo.getTeller());
	    
	    /** 查询还款状态(正常还款、一次性提前还款) **/
	    LoanSpecialRepayment loanSpecialRepayment = loanSpecialRepaymentServiceImpl.findbyLoanAndType(loanId, SpecialRepaymentTypeEnum.一次性还款.name(),
		    SpecialRepaymentStateEnum.申请.name());
	    if (loanSpecialRepayment != null) {
			/** 已申请一次提前提交还款 **/
			offerRepayInfo.setTradeCode(Const.TRADE_CODE_ONEOFF);
	    } else {
	    	offerRepayInfo.setTradeCode(Const.TRADE_CODE_NORMAL);
	    }

	    //offerRepayInfo.setTradeDate(repaymentInputVo.getTradeDate());
	    //offerRepayInfo.setTradeTime(Strings.isEmpty(repaymentInputVo.getTradeTime()) ? Dates.getNow() : repaymentInputVo.getTradeTime());
	    offerRepayInfo.setTradeDate(Dates.format(Dates.getNow(),Dates.DATAFORMAT_SLASHDATE));
	    offerRepayInfo.setTradeTime(Dates.getNow());
	    offerRepayInfo.setTradeKind(TradeKindEnum.正常交易.name());
	    offerRepayInfo.setTradeType(repaymentInputVo.getTradeType());
	    offerRepayInfo.setTradeNo(afterLoanServiceImpl.getTradeFlowNo(loanId));
	    
	    save(offerRepayInfo);
	    if(Strings.isNotEmpty(repaymentInputVo.getRepayNo())){
		    recordOfflineRepayRelation(offerRepayInfo, repaymentInputVo.getRepayNo());	
	    }
	    afterLoanServiceImpl.repayDeal(offerRepayInfo);
	    
		return offerRepayInfo;
	}

	/**
	 * 记录线下还款关系
	 * @param offerRepayInfo
	 */
	public void recordOfflineRepayRelation(OfferRepayInfo offerRepayInfo, String repayNo) {
		OfflineRepayRelation offlineRepayRelation = new OfflineRepayRelation();
		offlineRepayRelation.setAmount(offerRepayInfo.getAmount());
		offlineRepayRelation.setLoanId(offerRepayInfo.getLoanId());
		offlineRepayRelation.setRepayNo(repayNo);
		offlineRepayRelation.setTradeNo(offerRepayInfo.getTradeNo());
		offlineRepayRelation.setId(sequencesServiceImpl.getSequences(SequencesEnum.OFFLINE_REPAY_RELATION));
		offlineRepayRelationDao.insert(offlineRepayRelation);
	}

	@Override
	public OfferRepayInfo getLoanLastRepayInfoById(Long loanId) {
		Map<String, Object> repayParam = new HashMap<String, Object>();
		repayParam.put("loanId", loanId);
		return offerRepayInfoDao.getLoanLastRepayInfoById(repayParam);
	}
	
	/**
     * 还款录入后  单条录入和批量导入 
     * 调用线下实收 WM3_2312Vo
     * @param amount
     */
	@SuppressWarnings("unused")
	@Override
    public void callOfflineReceiveInterface(List<PaidInMoneyListEntity> paidInMoneyList){
    	if(paidInMoneyList != null && paidInMoneyList.size()>0){//批量导入和单条录入 
    		for(int i=0;i<paidInMoneyList.size();i++){
    	    	List<PaidInMoneyListEntity> list = new ArrayList<PaidInMoneyListEntity>();
    			PaidInMoneyListEntity paidInMoney = paidInMoneyList.get(i);
        		logger.info("【外贸3】还款录入，调用线下实收接口..."+ paidInMoney.toString());
    	    	String batNo = "wm3offline"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    	    	WM3_2312Vo vo = new WM3_2312Vo();
    	    	vo.setBrNo(BaseParamVo.SYS_SOURCE_WM3);//合作机构号
    	    	vo.setBatNo(batNo);//专批次号
    	    	vo.setDataCnt("1");
    	    	paidInMoney.setPactNo(paidInMoney.getPactNo());//合同号
    	    	paidInMoney.setRepayType("03");//实收类型
    	    	paidInMoney.setRepayAmt(paidInMoney.getRepayAmt());//实收金额
    	    	List<DeductMoneyDetailListEntity> deductMoneyList = new ArrayList<DeductMoneyDetailListEntity>();
    	    	DeductMoneyDetailListEntity deductMoney = new DeductMoneyDetailListEntity();
    			deductMoney.setCnt(0); // N 期次   注释：没有填 0
    			deductMoneyList.add(deductMoney);
    			paidInMoney.setListSubj(deductMoneyList);
    	    	list.add(paidInMoney);
    	    	vo.setList(list);
    	    	JSONObject grantResut = null;
    	    	try{
    	    		grantResut =  GatewayUtils.callCateWayInterface(vo, GatewayFuncIdEnum.外贸3线下实收.getCode());
    	    	}catch(Exception e){
    	    		logger.error("【外贸3】线下实收接口调用网关接口失败："+e.getMessage(),e);
    	    		saveDebitOfflineOfferInfo(paidInMoney, batNo, OfferTransactionStateEnum.扣款失败.getValue());
    	    		continue;
    	    	}
    	    	logger.info("【外贸3】线下实收接口,外贸3返回的数据："+grantResut);
    			String respCode = (String)grantResut.get("respCode");//外贸3返回的结果
    			String respDesc = (String)grantResut.get("respDesc");//外贸3返回的描述
    			if("0000".equals(respCode)){
    				String content = (String)grantResut.get("content");
    				JSONObject jsonObjectContent = JSON.parseObject(content);//String 类型
    				logger.info("调用【外贸3】线下实收接口成功，返回信息："+respDesc);
    				JSONArray jsonArray = jsonObjectContent.getJSONArray("list");//失败的记录 []数组类型
    				if(jsonArray.size() > 0){
    					saveDebitOfflineOfferInfo(paidInMoney, batNo, OfferTransactionStateEnum.扣款失败.getValue());
    					continue;
    				}
    				saveDebitOfflineOfferInfo(paidInMoney, batNo, OfferTransactionStateEnum.扣款成功.getValue());
    			}else{
    				logger.error("调用【外贸3】线下实收接口失败：原因："+respDesc);
    				saveDebitOfflineOfferInfo(paidInMoney, batNo, OfferTransactionStateEnum.扣款失败.getValue());
    				continue;
    			}
    		}
    	}
    }
    
	public void saveDebitOfflineOfferInfo(PaidInMoneyListEntity paidInMoney, String batNo, String state){
    	DebitOfflineOfferInfo offline = null;
    	Map<String,Object> map = new HashMap<String, Object>();
		String pactNo = paidInMoney.getPactNo();//合同号
    	BigDecimal repayAmt = paidInMoney.getRepayAmt();//实收金额
    	map.put("contractNum", pactNo);
    	VLoanInfo loanInfo = loanInfoDao.findListByMap(map).get(0);
    	offline = new DebitOfflineOfferInfo();
    	offline.setId(sequencesServiceImpl.getSequences(SequencesEnum.DEBIT_OFFLINE_OFFER_INFO));
    	offline.setBatNo(batNo);
    	offline.setTradeDate(Dates.getCurrDate());// 批量导入时，去当前时间 交易日期
    	offline.setLoanId(loanInfo.getId());
    	offline.setPactNo(loanInfo.getContractNum());//合同号
    	offline.setRepyType("03");//实收类型
    	offline.setRepayAmt(repayAmt);//实收金额
    	offline.setMemo("");//备注
    	offline.setState(state);
    	offline.setCreateTime(new Date());
    	offline.setCreator("admin");
    	debitOfflineOfferInfoDao.insert(offline);
	}
	
    /**
     * 保存 线下实收2312（线下溢缴款充值）请求 的收据， 后续通过定时任务根据批次号查询充值(2313接口)结果
     * @param paidInMoneyList
     * @param batNo
     */
    public void saveDebitOfflineOfferInfo(List<PaidInMoneyListEntity> paidInMoneyList,String batNo){
    	DebitOfflineOfferInfo offline = null;
    	Map<String,Object> map = new HashMap<String, Object>();
    	if(paidInMoneyList != null && paidInMoneyList.size()>0){
    		for(int i=0;i<paidInMoneyList.size();i++){
    			PaidInMoneyListEntity paidInMoney = paidInMoneyList.get(i);
    			String pactNo = paidInMoney.getPactNo();//合同号
    	    	BigDecimal repayAmt = paidInMoney.getRepayAmt();//实收金额
    	    	map.put("contractNum", pactNo);
    	    	VLoanInfo loanInfo = loanInfoDao.findListByMap(map).get(0);
    	    	offline = new DebitOfflineOfferInfo();
    	    	offline.setId(sequencesServiceImpl.getSequences(SequencesEnum.DEBIT_OFFLINE_OFFER_INFO));
            	offline.setBatNo(batNo);
            	offline.setBankNo("");
            	offline.setTradeDate(Dates.getCurrDate());// 批量导入时，去当前时间 交易日期
            	offline.setLoanId(loanInfo.getId());
            	offline.setPactNo(loanInfo.getContractNum());//合同号
            	offline.setRepyType("03");//实收类型
            	offline.setRepayAmt(repayAmt);//实收金额
            	//offline.setCnt(1);
            	//offline.setSubjType("");//科目类型
            	//offline.setSubjAmt(new BigDecimal(2));//科目金额
            	offline.setMemo("");//备注
            	offline.setState(OfferTransactionStateEnum.已发送.getValue());
            	offline.setCreateTime(new Date());
            	offline.setCreator("admin");
            	debitOfflineOfferInfoDao.insert(offline);
    		}
    	}
    }
    
    /**
     * 如果合同编号为多条，则合并为一条，repayAmt实收金额汇总
     * @param list
     * @return
     */
    public static List<PaidInMoneyListEntity> mySort(List<PaidInMoneyListEntity> list){
        Map<String,PaidInMoneyListEntity> tempMap = new HashMap<String,PaidInMoneyListEntity>();
        for (PaidInMoneyListEntity paidInMoneyListEntity : list) {
            String key = paidInMoneyListEntity.getPactNo();
            if(tempMap.containsKey(key)){
                PaidInMoneyListEntity tempUser = new PaidInMoneyListEntity(key,tempMap.get(key).getRepayAmt().add(paidInMoneyListEntity.getRepayAmt()));
                tempMap.put(key, tempUser);
            }else{
                tempMap.put(key, paidInMoneyListEntity);
            }
        }
        List<PaidInMoneyListEntity> tempList = new ArrayList<PaidInMoneyListEntity>();
        for(String key : tempMap.keySet()){
            tempList.add(tempMap.get(key));
        }
        return tempList;
    }

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public OfferRepayInfo dealRepaymentInput(RepaymentInputVo repaymentInputVo) {
		OfferRepayInfo offerRepayInfo = new OfferRepayInfo();
		long loanId = repaymentInputVo.getLoanId();
		if(!splitQueueLogService.isCanPublicAccountRepayment(loanId)){
			throw new PlatformException(ResponseEnum.FULL_MSG,"该笔借款目前存在划扣中或者待入账的记录，暂不能进行入账操作！");
		}
		SpecialRepaymentApply specialRepaymentApply = specialRepaymentApplyService.getApplyReliefStatusPass(loanId);
		if (specialRepaymentApply == null) {
			//此借款债权下没有审核通过的减免申请
			/** 调用核心层接口（还款） **/
			offerRepayInfo = this.repaymentInputCore(repaymentInputVo);
			return offerRepayInfo;
		}
		RelieCalculateAmountParamVo relieCalculateAmountParamVo = new RelieCalculateAmountParamVo();
		relieCalculateAmountParamVo.setLoanId(loanId);
		relieCalculateAmountParamVo.setTradeDate(repaymentInputVo.getTradeDate());
		relieCalculateAmountParamVo.setRepayAmount(repaymentInputVo.getAmount());
		relieCalculateAmountParamVo.setIsPecial(specialRepaymentApply.getIsSpecial());
		relieCalculateAmountParamVo.setApplyType(specialRepaymentApply.getApplyType());
		ReliefAmountCalculateVo reliefAmountCalculateVo = specialRepaymentApplyService.getRelieCalculateAmount(relieCalculateAmountParamVo);
		specialRepaymentApply = specialRepaymentApplyService.calculateEffectTotalMoney(reliefAmountCalculateVo,specialRepaymentApply);
		offerRepayInfo = specialRepaymentApplyService.dealReliefMoneyInputAndRepaymentInput(specialRepaymentApply,repaymentInputVo,reliefAmountCalculateVo);
		return offerRepayInfo;
	}

	@Override
	public RepaymentInputVo builderRepaymentInputVo(OfferTransaction offerTransaction){
		RepaymentInputVo repaymentInputVo = new RepaymentInputVo();
		Date tradeDate = Dates.parse(Dates.getDateTime(offerTransaction.getReqTime(), Dates.DEFAULT_DATE_FORMAT), Dates.DEFAULT_DATE_FORMAT);
		repaymentInputVo.setLoanId(offerTransaction.getLoanId());
		repaymentInputVo.setAmount(offerTransaction.getActualAmount());
		repaymentInputVo.setMemo("");
		repaymentInputVo.setOrgan(TPPHelper.TPP_HANDLER_ORGANIZATION_CODE);
		repaymentInputVo.setTeller(TPPHelper.TPP_HANDLER_TELLER);
		repaymentInputVo.setTradeDate(tradeDate);
		repaymentInputVo.setTradeTime(offerTransaction.getReqTime());
		repaymentInputVo.setTradeType(getPaySysNoString(offerTransaction.getPaySysNoReal()));
		repaymentInputVo.setIsTppSouces(true);
		return repaymentInputVo;
	}

	public RepaymentInputVo createRepaymentInputVo(DebitTransaction transaction) {
		RepaymentInputVo repaymentInputVo = new RepaymentInputVo();
		Date tradeDate = Dates.parse(Dates.getDateTime(transaction.getReqTime(), Dates.DEFAULT_DATE_FORMAT), Dates.DEFAULT_DATE_FORMAT);
		repaymentInputVo.setLoanId(transaction.getLoanId());
		repaymentInputVo.setAmount(transaction.getActualAmount());
		repaymentInputVo.setMemo("外贸3扣款结果入账");
		repaymentInputVo.setOrgan(TPPHelper.TPP_HANDLER_ORGANIZATION_CODE);
		repaymentInputVo.setTeller(TPPHelper.TPP_HANDLER_TELLER);
		repaymentInputVo.setTradeDate(tradeDate);
		repaymentInputVo.setTradeTime(transaction.getReqTime());
		DebitBaseInfo debitBaseInfo = debitBaseInfoDao.get(transaction.getDebitId());
		repaymentInputVo.setTradeType(getPaySysNoString(debitBaseInfo.getPaySysNo()));
		repaymentInputVo.setIsTppSouces(true);
		return repaymentInputVo;
	}
}
