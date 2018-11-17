package com.zdmoney.credit.core.calculator.fundssource.jmhz;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zdmoney.credit.common.constant.RepaymentStateEnum;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.core.calculator.pub.ICalculator;
import com.zdmoney.credit.loan.dao.pub.ILoanRepaymentDetailDao;
import com.zdmoney.credit.loan.domain.LoanBase;
import com.zdmoney.credit.loan.domain.LoanInitialInfo;
import com.zdmoney.credit.loan.domain.LoanProduct;
import com.zdmoney.credit.loan.domain.LoanRepaymentDetail;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.offer.domain.OfferRepayInfo;
import com.zdmoney.credit.system.domain.ProdCreditProductTerm;
import com.zdmoney.credit.system.service.pub.ISequencesService;

/**
 * 积木盒子 计算器 版本 v2.0
 * 
 * @author Ivan
 *
 */
@Component("FS_00007_v2")
public class JMHZCalculatorVTwo extends JMHZCalculatorBase implements ICalculator {

	@Autowired
	ISequencesService sequencesService;
	@Autowired
	ILoanRepaymentDetailDao loanRepaymentDetailDao;
	
	@Override
	public BigDecimal getOnetimeRepaymentAmount(Long loanId, Date currDate, List<LoanRepaymentDetail> repayList) {
		System.out.println("积木盒子 计算器 版本 v2.0");
		return super.getOnetimeRepaymentAmountV1(loanId, currDate, repayList);
	}

	@Override
	public BigDecimal getPenalty(Long loanId, List<LoanRepaymentDetail> repayList, VLoanInfo vLoanInfo) {
		System.out.println("积木盒子 计算器 版本 v2.0");
		return super.getPenaltyV1(loanId, repayList, vLoanInfo);
	}
	
	@Override
	public void updateRate(LoanBase loanBase, LoanInitialInfo loanInitialInfo,
			LoanProduct loanProduct,ProdCreditProductTerm prodCreditProductTerm) {

		super.updateRate(loanBase, loanInitialInfo, loanProduct,prodCreditProductTerm);
		
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public Map<String, Object> createLoanRepaymentDetail(LoanBase loanBase, LoanInitialInfo loanInitialInfo, LoanProduct loanProduct) {
		
		LoanRepaymentDetail pri = new LoanRepaymentDetail();
		
		double payEterm4Jimu = jimuheziCalReapyEterm(loanProduct);
		
		LoanRepaymentDetail c = null;
		Double returnetermSUM = 0.0;
		Double currentAccrualSum = 0.0;
		
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
			double accrualRevise = jimuheziCalAccrualRevise(payEterm4Jimu, c.getReturneterm().doubleValue(), c.getCurrentAccrual().doubleValue());
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
	 * 贷前试算
	 * 
	 * @param loanBase
	 * @param loanInitialInfo
	 * @param loanProduct
	 * @return
	 */
	@Override
	public List<LoanRepaymentDetail> createLoanTrial(LoanBase loanBase, LoanInitialInfo loanInitialInfo,
			LoanProduct loanProduct) {
		
		return super.createLoanTrialV2(loanBase, loanInitialInfo, loanProduct);
	}

	@Override
	public boolean enterAccountAfter(OfferRepayInfo offerRepayInfo) {
		return true;
	}

	@Override
	public boolean enterAccountBefore(OfferRepayInfo offerRepayInfo) {
		return true;
	}

}
