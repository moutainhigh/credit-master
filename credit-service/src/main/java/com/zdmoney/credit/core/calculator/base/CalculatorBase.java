package com.zdmoney.credit.core.calculator.base;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.zdmoney.credit.common.constant.CalculatorVersionEnum;
import com.zdmoney.credit.common.constant.FundsSourcesTypeEnum;
import com.zdmoney.credit.common.constant.LoanTypeEnum;
import com.zdmoney.credit.common.constant.RepaymentStateEnum;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.util.Assert;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.ToolUtils;
import com.zdmoney.credit.loan.dao.pub.ILoanRepaymentDetailDao;
import com.zdmoney.credit.loan.domain.LoanBase;
import com.zdmoney.credit.loan.domain.LoanInitialInfo;
import com.zdmoney.credit.loan.domain.LoanProduct;
import com.zdmoney.credit.loan.domain.LoanRepaymentDetail;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import com.zdmoney.credit.system.dao.pub.ISysParamDefineDao;
import com.zdmoney.credit.system.domain.ProdCreditProductTerm;
import com.zdmoney.credit.system.service.pub.ISequencesService;

/**
 * 计算器 父类 (不区分合同来源)
 * 
 * 用于实现计算器公共方法(不区分合同来源及计算器版本)
 * 
 * @author Ivan
 *
 */
public class CalculatorBase {

	@Autowired
	IVLoanInfoService loanInfoServiceImpl;
	@Autowired
	ISysParamDefineDao sysParamDefineDao;//系统参数操作DAO
	@Autowired
	ISequencesService sequencesService;
	@Autowired
	ILoanRepaymentDetailDao loanRepaymentDetailDao;

	/**
	 * 获取一次性还款金额（不包含罚息和逾期本息）
	 * 
	 * 适用于 1.0版本
	 * 
	 * @param loanId
	 *            债权编号
	 * @param currDate
	 *            当前时间
	 * @param repayList
	 *            （可选）还款计划列表（通过此列表计算一次性还款金额）
	 * @return
	 */
	public BigDecimal getOnetimeRepaymentAmountV1(Long loanId, Date currDate, List<LoanRepaymentDetail> repayList) {
		BigDecimal bigDecimal = new BigDecimal("0.00");
		LoanRepaymentDetail loanRepaymentDetail = getLast(repayList);
		Assert.notNull(loanRepaymentDetail, ResponseEnum.FULL_MSG, "获取一次性结清金额异常，未得到loanRepaymentDetail实例。loanId:"
				+ loanId);
		boolean isBf = loanRepaymentDetail.getReturnDate().compareTo(currDate) >= 0;
		if (isBf) {
			BigDecimal bd = loanRepaymentDetail.getRepaymentAll().subtract(loanRepaymentDetail.getReturneterm())
					.add(loanRepaymentDetail.getDeficit());
			bigDecimal = bd.compareTo(new BigDecimal("0.00")) > -1 ? bd : new BigDecimal("0.00");
		}
		return bigDecimal;
	}

	/**
	 * 获取一次性还款金额（不包含罚息和逾期本息）
	 * 
	 * 适用于 2.0版本 (暂时不用)
	 * 
	 * @param loanId
	 *            债权编号
	 * @param currDate
	 *            当前时间
	 * @param repayList
	 *            （可选）还款计划列表（通过此列表计算一次性还款金额）
	 * @return
	 */
	public BigDecimal getOnetimeRepaymentAmountV2(Long loanId, Date currDate, List<LoanRepaymentDetail> repayList) {
		BigDecimal onetimeRepaymentAmount = null;
		/** 获取当期还款计划，债权过期后得到的是最后一期还款计划 **/
		LoanRepaymentDetail loanRepaymentDetail = getLast(repayList);
		Assert.notNull(loanRepaymentDetail, ResponseEnum.FULL_MSG, "获取一次性结清金额异常，未得到loanRepaymentDetail实例。loanId:"
				+ loanId);
		/** 判断债权是否过期 只要过期一次性还款金额始终返回0元 **/
		boolean isExpire = loanRepaymentDetail.getReturnDate().compareTo(currDate) >= 0;
		if (isExpire) {
			/** 债权未到期 **/
			/** 当期一次性结清金额 **/
			BigDecimal repaymentAll = loanRepaymentDetail.getRepaymentAll();
			repaymentAll = repaymentAll.subtract(loanRepaymentDetail.getReturneterm()).add(
					loanRepaymentDetail.getDeficit());
			/** 当期违约金 **/
			BigDecimal penalty = loanRepaymentDetail.getPenalty();
			/** 当期退费 **/
			BigDecimal giveBackRate = loanRepaymentDetail.getGiveBackRate();
			/** 计算 期末剩余本金 公式 当期一次性结清金额 - 当期违约金 + 当期退费 **/
			BigDecimal principal = repaymentAll.subtract(penalty).add(giveBackRate);

			/** 获取违约金2.0版本 取连续逾期最早那一期 **/
			BigDecimal overduePenalty = getPenaltyV2(loanId, repayList, null);

			/** 计算一次性结清金额 **/
			onetimeRepaymentAmount = principal.add(overduePenalty).subtract(giveBackRate);
		}
		if (onetimeRepaymentAmount == null || onetimeRepaymentAmount.compareTo(BigDecimal.ZERO) == -1) {
			onetimeRepaymentAmount = new BigDecimal("0.00");
		}
		return onetimeRepaymentAmount;
	}

	/**
	 * 获取违约金
	 * 
	 * 适用于 1.0版本
	 * 
	 * @param loanId
	 *            债权编号
	 * @param repayList
	 *            还款计划列表
	 * @param vLoanInfo
	 *            债权实例
	 * @return
	 */
	public BigDecimal getPenaltyV1(Long loanId, List<LoanRepaymentDetail> repayList, VLoanInfo vLoanInfo) {
		BigDecimal result = new BigDecimal("0.00");
		if (CollectionUtils.isEmpty(repayList)) {
			return result;
		}
		LoanRepaymentDetail rd = getLast(repayList);
		if (LoanTypeEnum.助学贷.name().equals(vLoanInfo.getLoanType())
				|| LoanTypeEnum.车贷.name().equals(vLoanInfo.getLoanType())) {
			if (rd.getCurrentTerm() != vLoanInfo.getTime()) {// 最后一期没有利息
				result = rd.getRepaymentAll().subtract(rd.getPrincipalBalance()).subtract(rd.getReturneterm());
			}
		} else {// 个贷
//			if (rd.getCurrentTerm() != vLoanInfo.getTime() && (rd.getCurrentTerm() != 1 && rd.getCurrentTerm() != 2)) {
//				result = vLoanInfo.getPactMoney().multiply(Const.PENALTY_INTEREST_RATE)
//						.setScale(2, RoundingMode.HALF_UP);
//			}
			/** 违约金直接到还款计划表取 **/
			result = rd.getPenalty();
		}
		return result;
	}

	/**
	 * 获取违约金
	 * 
	 * 适用于 2.0版本 (暂时不用)
	 * 
	 * @param loanId
	 *            债权编号
	 * @param repayList
	 *            还款计划列表
	 * @param vLoanInfo
	 *            债权实例
	 * @return
	 */
	public BigDecimal getPenaltyV2(Long loanId, List<LoanRepaymentDetail> repayList, VLoanInfo vLoanInfo) {
		LoanRepaymentDetail loanRepaymentDetail = getFirst(repayList);
		Assert.notNull(loanRepaymentDetail, ResponseEnum.FULL_MSG, "获取违约金异常，未得到loanRepaymentDetail实例。loanId:" + loanId);
		BigDecimal penalty = loanRepaymentDetail.getPenalty();
		if (penalty == null) {
			penalty = new BigDecimal("0.00");
		}
		return penalty;
	}

	/**
	 * 获取集合第一项数据
	 * 
	 * @param repayList
	 * @return
	 */
	private LoanRepaymentDetail getFirst(List<LoanRepaymentDetail> repayList) {
		LoanRepaymentDetail loanRepaymentDetail = null;
		if (repayList != null && repayList.size() > 0) {
			loanRepaymentDetail = repayList.get(0);
		}
		return loanRepaymentDetail;
	}

	/**
	 * 获取集合最后一项数据
	 * 
	 * @param repayList
	 * @return
	 */
	private LoanRepaymentDetail getLast(List<LoanRepaymentDetail> repayList) {
		LoanRepaymentDetail loanRepaymentDetail = null;
		if (repayList != null && repayList.size() > 0) {
			int last = repayList.size() - 1;
			loanRepaymentDetail = repayList.get(last);
		}
		return loanRepaymentDetail;
	}
	
	/**
     * 债权对应产品初始化
     * 
     * @param loanBase
     * @param loanInitialInfo
     * @param loanProduct
     * @param prodCreditProductInfo
     */
    public void updateRate(LoanBase loanBase, LoanInitialInfo loanInitialInfo, LoanProduct loanProduct
    		,ProdCreditProductTerm prodCreditProductTerm) {
    	
    	loanProduct.setRate(prodCreditProductTerm.getRate());
    	loanProduct.setPenaltyRate(prodCreditProductTerm.getPenaltyRate());
		loanProduct.setAccrualem(prodCreditProductTerm.getAccrualem());
		
    	BigDecimal t = loanInitialInfo.getMoney().multiply(BigDecimal.valueOf(loanProduct.getTime())).multiply(loanProduct.getRate());
    	// 合同金额
    	Double pactMoney = (t.doubleValue() + loanInitialInfo.getMoney().doubleValue()) / (1 + loanProduct.getAccrualem().doubleValue() * loanProduct.getTime().doubleValue()); 
    	loanProduct.setPactMoney(BigDecimal.valueOf(pactMoney).setScale(2, BigDecimal.ROUND_HALF_UP));
    	loanProduct.setGrantMoney(loanInitialInfo.getMoney());
		Double returnETerm = pactMoney * (loanProduct.getAccrualem().doubleValue() + 1 / loanProduct.getTime().doubleValue());
    	loanProduct.setRateem(BigDecimal.valueOf(ToolUtils.rate(pactMoney, returnETerm, loanProduct.getTime().doubleValue())));
		loanProduct.setRateey(BigDecimal.valueOf(Math.pow(1 + loanProduct.getRateem().doubleValue(), 12) - 1));
		loanProduct.setResidualPactMoney(loanProduct.getPactMoney());
    }
    
    /**
     * 根据申请金额 获取合同金额
     * @param requestMoney
     * @param prodCreditProductTerm
     * @return
     */
    public BigDecimal pactMoney(BigDecimal requestMoney, ProdCreditProductTerm prodCreditProductTerm){
    	BigDecimal t = requestMoney.multiply(BigDecimal.valueOf(prodCreditProductTerm.getTerm())).multiply(prodCreditProductTerm.getRate());
    	// 合同金额
    	Double pactMoney = (t.doubleValue() + requestMoney.doubleValue()) / (1 + prodCreditProductTerm.getAccrualem().doubleValue() * prodCreditProductTerm.getTerm().doubleValue());
    	return BigDecimal.valueOf(pactMoney).setScale(2, BigDecimal.ROUND_HALF_UP);
    }
    
    /**
	 * 当期利息
	 * 
	 * @param pri
	 * @param loanProduct
	 * @param currentTerm
	 * @return
	 */
	protected double currentAccrual(LoanRepaymentDetail pri, LoanProduct loanProduct, int currentTerm) {

		double returnETerm = Double.valueOf(loanProduct.getPactMoney().toString())
				* (Double.valueOf(loanProduct.getAccrualem().toString())
						+ 1 / Double.valueOf(loanProduct.getTime().toString()));

		double rateYear = ToolUtils.rate(Double.valueOf(loanProduct.getPactMoney().toString()), returnETerm, loanProduct.getTime());
		if (currentTerm == 1) {
			return rateYear * Double.valueOf(loanProduct.getPactMoney().toString());
		}
		if (currentTerm > 1) {
			double r = Double.valueOf(pri.getPrincipalBalance().toString()) * rateYear;
			if (r > 0) {
				return r;
			} else {
				return 0;
			}
		}
		return 0;
	}
	
	/**
	 * 本金余额
	 * 
	 * @param pri
	 * @param loanProduct
	 * @param currentTerm
	 * @return
	 */
	protected double principalBalance(LoanRepaymentDetail pri, LoanProduct loanProduct, int currentTerm) {

		double returnETerm = Double.valueOf(loanProduct.getPactMoney().toString())
				* (Double.valueOf(loanProduct.getAccrualem().toString()) + 1 / Double.valueOf(loanProduct.getTime()));
		if (currentTerm == 1) {
			return Double.valueOf(loanProduct.getPactMoney().toString()) - returnETerm
					+ currentAccrual(pri, loanProduct, 1);
		}
		if (currentTerm > 1) {
			double r = Double.valueOf(pri.getPrincipalBalance().toString()) - returnETerm
					+ currentAccrual(pri, loanProduct, currentTerm);
			if (r > 0) {
				return r;
			} else {
				return 0;
			}
		}
		return 0;
	}
	
	/**
	 * 当期退费
	 * 
	 * @param loanBase
	 * @param loanInitialInfo
	 * @param loanProduct
	 * @param currentTeam
	 * @return
	 */
	protected double giveBackRate(LoanBase loanBase, LoanInitialInfo loanInitialInfo, LoanProduct loanProduct, int currentTeam) {
		if (loanProduct.getTime() == 6) {
			return 0;
		}
		if (currentTeam >= 1 && currentTeam <= 2) {
			return 0;
		}

		double y3 = 0.0;
		if (FundsSourcesTypeEnum.积木盒子.getValue().equals(loanBase.getFundsSources())) {
			y3 = loanProduct.getPactMoney().doubleValue() - loanInitialInfo.getMoney().doubleValue()
					- loanProduct.getRisk().doubleValue() - loanProduct.getManageRateForPartyC().doubleValue()
					- loanProduct.getPactMoney().doubleValue() * 0.02;
		} else if (FundsSourcesTypeEnum.华澳信托.getValue().equals(loanBase.getFundsSources())
				|| FundsSourcesTypeEnum.国民信托.getValue().equals(loanBase.getFundsSources())) {
			y3 = loanProduct.getPactMoney().doubleValue() - loanInitialInfo.getMoney().doubleValue()
					- loanProduct.getRisk().doubleValue() - loanProduct.getManageRateForPartyC().doubleValue()
					- loanProduct.getPactMoney().doubleValue() * 0.1;
		} else {
			y3 = loanProduct.getPactMoney().doubleValue() - loanInitialInfo.getMoney().doubleValue()
					- loanProduct.getPactMoney().doubleValue() * 0.1;

			/*
			 * 以下为不用的老系统代码 def d1215 = new
			 * java.text.SimpleDateFormat('yyyy-MM-dd').parse('2014-12-15');
			 * if(loan.signDate != null && loan.signDate < d1215){
			 * y3=loan.pactMoney-loan.money-loan.risk - loan.pactMoney*0.06 }
			 */
		}

		if (currentTeam == 3) {
			return y3 > 0 ? y3 : 0;
		} else if (currentTeam == 4) {
			return (y3 - loanProduct.getPactMoney().doubleValue() * 0.03) > 0
					? (y3 - loanProduct.getPactMoney().doubleValue() * 0.03) : 0;
		} else if (currentTeam > 4) {
			double r = y3 - loanProduct.getPactMoney().doubleValue() * 0.03
					- loanProduct.getPactMoney().doubleValue() * (currentTeam - 4) * 0.01;
			if (r > 0) {
				return r;
			} else {
				return 0;
			}
		} else {
			return 0;
		}
	}

	/**
	 * 一次性还款金额
	 * 
	 * @param detail
	 * @param loanProduct
	 * @param currentTeam
	 * @return
	 */
	protected double repaymentAll(LoanRepaymentDetail detail, LoanProduct loanProduct, int currentTeam) {
		double retrunETerm = loanProduct.getPactMoney().doubleValue()
				* (loanProduct.getAccrualem().doubleValue() + 1 / loanProduct.getTime().doubleValue());
		detail.setPenalty(new BigDecimal(0.00));

		if (currentTeam <= 2) {
			return detail.getPrincipalBalance().doubleValue() + retrunETerm + detail.getGiveBackRate().doubleValue();
		} else if (currentTeam >= 3 && currentTeam <= loanProduct.getTime() - 1) {
			detail.setPenalty(BigDecimal.valueOf(loanProduct.getPactMoney().doubleValue() * 0.01));
			return detail.getPrincipalBalance().doubleValue() + retrunETerm - detail.getGiveBackRate().doubleValue()
					+ loanProduct.getPactMoney().doubleValue() * 0.01;
		} else {
			return retrunETerm;
		}
	}
	
	/**
	 * 当期退费 V2
	 * 
	 * 计算方式：
	 * 	退费=总服务费-总服务费*X/借款期限：
	 * 	总服务费 = 合同金额 - 审批金额；
	 * 	X为第几期，当X等于借款期限时，退费为0，即最后一期没有退费；
	 * 
	 * @param loanBase
	 * @param loanInitialInfo
	 * @param loanProduct
	 * @param currentTeam
	 * @return
	 */
	protected double giveBackRateTwo(LoanInitialInfo loanInitialInfo, LoanProduct loanProduct, int currentTeam) {
		/**
		 
		 */
		
		/**总服务费**/
		double serviceSum = loanProduct.getPactMoney().doubleValue() - loanInitialInfo.getMoney().doubleValue();

		double backRate = serviceSum - (serviceSum * currentTeam /loanProduct.getTime());
		
		if(backRate > 0) {
			return backRate;
		} else {
			return 0;
		}
	}
	
	/**
	 * 计算违约金 V2
	 * 
	 * 计算方式：
	 * 从原来的合同金额*1%，改为:12期之前（含）提前结清的，
	 * 按期末剩余本金*4%，12期之后按期末剩余本金*3%；
	 * 且最后一期没有违约金；
	 * 
	 * @param loanProduct
	 * @param currentTeam
	 * @return
	 */
	protected double penaltyTwo(LoanRepaymentDetail c, LoanProduct loanProduct, int currentTerm) {
		
		double retrunETerm = loanProduct.getPactMoney().doubleValue()
				* (loanProduct.getAccrualem().doubleValue() + 1 / loanProduct.getTime().doubleValue());
		
		double penalty = 0.0;
		
		if(currentTerm <= 12) {
			penalty = (c.getPrincipalBalance().doubleValue() + retrunETerm) * 0.04;
		} else if (currentTerm > 12 && currentTerm < loanProduct.getTime()) {
			penalty = (c.getPrincipalBalance().doubleValue() + retrunETerm) * 0.03;
		}
		
		if(currentTerm == 12 && loanProduct.getTime() == 12L) {
			penalty = 0.0;
		}
		
		
		return penalty;
	}
	
	/**
	 * 一次性还款金额 V2
	 * 
	 * @param detail
	 * @param loanProduct
	 * @param currentTeam
	 * @return
	 */
	protected double repaymentAllTwo(LoanRepaymentDetail detail, LoanProduct loanProduct, int currentTeam) {
		double retrunETerm = loanProduct.getPactMoney().doubleValue()
				* (loanProduct.getAccrualem().doubleValue() + 1 / loanProduct.getTime().doubleValue());

		if (currentTeam <= loanProduct.getTime() - 1) {
			return detail.getPrincipalBalance().doubleValue() + retrunETerm + detail.getPenalty().doubleValue() - detail.getGiveBackRate().doubleValue();
		} else {
			return retrunETerm;
		}
	}
	
	/**
	 * V1版本 生成还款计划
	 * @param loanBase
	 * @param loanInitialInfo
	 * @param loanProduct
	 * @return
	 */
	@SuppressWarnings("deprecation")
	protected Map<String, Object> createLoanRepaymentDetailV1(LoanBase loanBase, LoanInitialInfo loanInitialInfo, LoanProduct loanProduct) {
		LoanRepaymentDetail pri = new LoanRepaymentDetail();
		
		LoanRepaymentDetail c = null;
		Double returnetermSUM = 0.0;
		Double currentAccrualSum = 0.0;
		
		for (int i = 0; i < loanProduct.getTime(); i++) {
			c = new LoanRepaymentDetail();

			c.setId(sequencesService.getSequences(SequencesEnum.LOAN_REPAYMENT_DETAIL));
			c.setCurrentTerm(i + 1L);
			c.setCurrentAccrual(BigDecimal.valueOf(currentAccrual(pri, loanProduct, i + 1)));
			c.setPrincipalBalance(BigDecimal.valueOf(principalBalance(pri, loanProduct, i + 1)));
			c.setGiveBackRate(BigDecimal.valueOf(giveBackRate(loanBase, loanInitialInfo, loanProduct, i + 1)));
			c.setRepaymentAll(BigDecimal.valueOf(repaymentAll(c, loanProduct, i + 1)));
			c.setLoanId(loanBase.getId());
			c.setDeficit(BigDecimal.valueOf(loanProduct.getPactMoney().doubleValue()
					* (loanProduct.getAccrualem().doubleValue() + 1 / loanProduct.getTime().doubleValue())));
			c.setReturneterm(c.getDeficit());
			c.setRepaymentState(RepaymentStateEnum.未还款.name());
			Date dx = (Date) loanProduct.getStartrdate().clone();
			dx.setMonth(dx.getMonth() + i);
			c.setReturnDate(dx);
			c.setPenaltyDate(dx);
			double accrualRevise = 0;
			c.setAccrualRevise(BigDecimal.valueOf(accrualRevise));

			loanRepaymentDetailDao.insert(c);
			pri = c;

			returnetermSUM += c.getReturneterm().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			if (c.getCurrentTerm() < loanProduct.getTime()) {
				currentAccrualSum += c.getCurrentAccrual().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			}
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("loanRepaymentDetail", c);
		map.put("returnetermSUM", returnetermSUM);
		map.put("currentAccrualSum", currentAccrualSum);
		
		return map;
	}
	
	/**
	 * V2版本 生成还款计划
	 * @param loanBase
	 * @param loanInitialInfo
	 * @param loanProduct
	 * @return
	 */
	@SuppressWarnings("deprecation")
	protected Map<String, Object> createLoanRepaymentDetailV2(LoanBase loanBase, LoanInitialInfo loanInitialInfo, LoanProduct loanProduct) {
		LoanRepaymentDetail pri = new LoanRepaymentDetail();
		
		LoanRepaymentDetail c = null;
		BigDecimal returnetermSUM = BigDecimal.ZERO;
		BigDecimal currentAccrualSum = BigDecimal.ZERO;
		
		for (int i = 0; i < loanProduct.getTime(); i++) {
			c = new LoanRepaymentDetail();

			c.setId(sequencesService.getSequences(SequencesEnum.LOAN_REPAYMENT_DETAIL));
			c.setCurrentTerm(i + 1L);
			c.setCurrentAccrual(BigDecimal.valueOf(currentAccrual(pri, loanProduct, i + 1)));
			c.setPrincipalBalance(BigDecimal.valueOf(principalBalance(pri, loanProduct, i + 1)));//计算 本金余额
			c.setGiveBackRate(BigDecimal.valueOf(giveBackRateTwo(loanInitialInfo, loanProduct, i + 1)));//计算 退费
			c.setPenalty(BigDecimal.valueOf(penaltyTwo(c, loanProduct, i + 1)));//计算 违约金
			c.setRepaymentAll(BigDecimal.valueOf(repaymentAllTwo(c, loanProduct, i + 1)));//计算一次性还款金额
			c.setLoanId(loanBase.getId());
			c.setDeficit(BigDecimal.valueOf(loanProduct.getPactMoney().doubleValue()
					* (loanProduct.getAccrualem().doubleValue() + 1 / loanProduct.getTime().doubleValue())));
			c.setReturneterm(c.getDeficit());
			c.setRepaymentState(RepaymentStateEnum.未还款.name());
			Date dx = (Date) loanProduct.getStartrdate().clone();
			dx.setMonth(dx.getMonth() + i);
			c.setReturnDate(dx);
			c.setPenaltyDate(dx);
			double accrualRevise = 0;
			c.setAccrualRevise(BigDecimal.valueOf(accrualRevise));

			loanRepaymentDetailDao.insert(c);
			pri = c;

			returnetermSUM = returnetermSUM.add(c.getReturneterm());
			//returnetermSUM += c.getReturneterm().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			if (c.getCurrentTerm() < loanProduct.getTime()) {
				//currentAccrualSum += c.getCurrentAccrual().setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				currentAccrualSum = currentAccrualSum.add(c.getCurrentAccrual());
			}
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("loanRepaymentDetail", c);
		map.put("returnetermSUM", returnetermSUM.doubleValue());
		map.put("currentAccrualSum", currentAccrualSum.doubleValue());
		
		return map;
	}
	
	/**
	 *  V1版本 贷前试算
	 * @param loanBase
	 * @param loanInitialInfo
	 * @param loanProduct
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public List<LoanRepaymentDetail> createLoanTrialV1(LoanBase loanBase, LoanInitialInfo loanInitialInfo, LoanProduct loanProduct) {
		LoanRepaymentDetail pri = new LoanRepaymentDetail();
		List<LoanRepaymentDetail> repaymentDetails = new ArrayList<LoanRepaymentDetail>();
		
		for (int i = 0; i < loanProduct.getTime(); i++) {
			LoanRepaymentDetail c = new LoanRepaymentDetail();

			c.setCurrentTerm(i + 1L);
			c.setCurrentAccrual(BigDecimal.valueOf(currentAccrual(pri, loanProduct, i + 1)));
			c.setPrincipalBalance(BigDecimal.valueOf(principalBalance(pri, loanProduct, i + 1)));
			c.setGiveBackRate(BigDecimal.valueOf(giveBackRate(loanBase, loanInitialInfo, loanProduct, i + 1)));
			c.setRepaymentAll(BigDecimal.valueOf(repaymentAll(c, loanProduct, i + 1)));
			c.setDeficit(BigDecimal.valueOf(loanProduct.getPactMoney().doubleValue()
							* (loanProduct.getAccrualem().doubleValue() + 1 / loanProduct.getTime().doubleValue())));
			c.setReturneterm(c.getDeficit());
			c.setRepaymentState(RepaymentStateEnum.未还款.name());
			Date dx = (Date) loanProduct.getStartrdate().clone();
			dx.setMonth(dx.getMonth() + i);
			c.setReturnDate(dx);
			c.setPenaltyDate(dx);

			repaymentDetails.add(c);
			pri = c;
		}
		return repaymentDetails;
	}
	
	/**
	 *  V2版本 贷前试算
	 * @param loanBase
	 * @param loanInitialInfo
	 * @param loanProduct
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public List<LoanRepaymentDetail> createLoanTrialV2(LoanBase loanBase, LoanInitialInfo loanInitialInfo, LoanProduct loanProduct) {
		LoanRepaymentDetail pri = new LoanRepaymentDetail();
		List<LoanRepaymentDetail> repaymentDetails = new ArrayList<LoanRepaymentDetail>();
		
		for (int i = 0; i < loanProduct.getTime(); i++) {
			LoanRepaymentDetail c = new LoanRepaymentDetail();

			c.setCurrentTerm(i + 1L);
			c.setCurrentAccrual(BigDecimal.valueOf(currentAccrual(pri, loanProduct, i + 1)));
			c.setPrincipalBalance(BigDecimal.valueOf(principalBalance(pri, loanProduct, i + 1)));//计算 本金余额
			c.setGiveBackRate(BigDecimal.valueOf(giveBackRateTwo(loanInitialInfo, loanProduct, i + 1)));//计算 退费
			c.setPenalty(BigDecimal.valueOf(penaltyTwo(c, loanProduct, i + 1)));//计算 违约金
			c.setRepaymentAll(BigDecimal.valueOf(repaymentAllTwo(c, loanProduct, i + 1)));//计算一次性还款金额
			c.setDeficit(BigDecimal.valueOf(loanProduct.getPactMoney().doubleValue()
							* (loanProduct.getAccrualem().doubleValue() + 1 / loanProduct.getTime().doubleValue())));
			c.setReturneterm(c.getDeficit());
			c.setRepaymentState(RepaymentStateEnum.未还款.name());
			Date dx = (Date) loanProduct.getStartrdate().clone();
			dx.setMonth(dx.getMonth() + i);
			c.setReturnDate(dx);
			c.setPenaltyDate(dx);

			repaymentDetails.add(c);
			pri = c;
		}
		return repaymentDetails;
	}
	
	/**
	 * 判断要执行那本版本的计算器
	 * 
	 * @return
	 */
	protected boolean isV2Date() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Date now = new Date();
		
		String calculatorDate = sysParamDefineDao.getSysParamValue("codeHelper", "calculatorDate", false);
		
		Date vDate = null;
		try {
			vDate = sdf.parse(calculatorDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (vDate == null) {
			return false;
		} else {
			if(now.before(vDate)) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	/**
	 * 获取计算器版本
	 * @return
	 */
	public CalculatorVersionEnum getCalculatorVersion(){
		return null;
	}
	
	/**
	 * 获取借款类型
	 * @return
	 */
	public int getLoanType(String loanType){
		return -1;
	}
	
    

	/**
	 * 罚息计算通用方法（旧版）
	 * @param repayList
	 * @param currDate
	 * @param loanInfo
	 * @return
	 */
	protected BigDecimal getFineV1(List<LoanRepaymentDetail> repayList, Date currDate, VLoanInfo loanInfo) {
        BigDecimal result = BigDecimal.ZERO;
        if (CollectionUtils.isEmpty(repayList)) {
            return result;
        }
        // 逾期天数
        int overdueDay = Dates.dateDiff(repayList.get(0).getPenaltyDate(), currDate);
        if (overdueDay <= 0) {
            return result;
        }
        BigDecimal penaltyRate = loanInfo.getPenaltyRate();
        result = loanInfo.getResidualPactMoney().multiply(new BigDecimal(overdueDay)).multiply(penaltyRate);
        result = result.setScale(2, RoundingMode.HALF_UP);// 四舍五入
        return result;
    }
    
	/**
	 * 罚息计算通用方法（旧版）
	 * @param repayList
	 * @param currDate
	 * @return
	 */
    protected BigDecimal getFineV1(List<LoanRepaymentDetail> repayList, Date currDate) {
        if (CollectionUtils.isEmpty(repayList)) {
            return BigDecimal.ZERO;
        }
        Long loanId = repayList.get(0).getLoanId();
        VLoanInfo loanInfo = loanInfoServiceImpl.findByLoanId(loanId);
        return this.getFineV1(repayList, currDate, loanInfo);
    }
    
    /**
     * 罚息计算通用方法（新版）
     * @param detail
     * @param currDate
     * @param loanInfo
     * @return
     */
    protected BigDecimal getFineV2(LoanRepaymentDetail detail, Date currDate, VLoanInfo loanInfo) {
        BigDecimal result = BigDecimal.ZERO;
        if (null == detail) {
            return result;
        }
        // 当期剩余本金
        BigDecimal overdueCorpus = BigDecimal.ZERO;
        // 当期应还本金
        BigDecimal shouldRepayCorpus = BigDecimal.ZERO;
        if (RepaymentStateEnum.结清.name().equals(detail.getRepaymentState())) {
            return result;
        }
        if (detail.getReturnDate().compareTo(currDate) >= 0) {
            return result;
        }
        // 计算逾期天数
        int overdueDay = Dates.dateDiff(detail.getPenaltyDate(), currDate);
        if (overdueDay <= 0) {
            return result;
        }
        // 当期应还本金
        shouldRepayCorpus = detail.getReturneterm().subtract(detail.getCurrentAccrual());
        if (detail.getDeficit().compareTo(shouldRepayCorpus) >= 0) {
            overdueCorpus = shouldRepayCorpus;
        } else {
            overdueCorpus = detail.getDeficit();
        }
        result = result.add(overdueCorpus.multiply(new BigDecimal(overdueDay)).multiply(loanInfo.getPenaltyRate()));
        // 四舍五入
        result = result.setScale(2, RoundingMode.HALF_UP);
        return result;
    }
    
    /**
     * 罚息计算通用方法（新版）
     * @param detail
     * @param currDate
     * @return
     */
    protected BigDecimal getFineV2(LoanRepaymentDetail detail, Date currDate) {
    	if (null == detail) {
            return BigDecimal.ZERO;
        }
        Long loanId = detail.getLoanId();
        VLoanInfo loanInfo = loanInfoServiceImpl.findByLoanId(loanId);
        return this.getFineV2(detail, currDate, loanInfo);
    }
    
    /**
     * 罚息计算通用方法（新版）
     * @param repayList
     * @param currDate
     * @param loanInfo
     * @return
     */
    protected BigDecimal getFineV2(List<LoanRepaymentDetail> repayList, Date currDate, VLoanInfo loanInfo) {
        BigDecimal result = BigDecimal.ZERO;
        if (CollectionUtils.isEmpty(repayList)) {
            return result;
        }
        for(LoanRepaymentDetail detail : repayList){
            result = result.add(this.getFineV2(detail, currDate, loanInfo));
        }
        // 四舍五入
        result = result.setScale(2, RoundingMode.HALF_UP);
        return result;
    }
    
    /**
     * 罚息计算通用方法（新版）
     * @param repayList
     * @param currDate
     * @return
     */
    protected BigDecimal getFineV2(List<LoanRepaymentDetail> repayList, Date currDate) {
        if (CollectionUtils.isEmpty(repayList)) {
            return BigDecimal.ZERO;
        }
        Long loanId = repayList.get(0).getLoanId();
        VLoanInfo loanInfo = loanInfoServiceImpl.findByLoanId(loanId);
        return this.getFineV2(repayList, currDate, loanInfo);
    }
}
