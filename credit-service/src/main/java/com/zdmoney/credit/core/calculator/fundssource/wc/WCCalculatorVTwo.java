package com.zdmoney.credit.core.calculator.fundssource.wc;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.zdmoney.credit.core.calculator.pub.ICalculator;
import com.zdmoney.credit.loan.domain.LoanBase;
import com.zdmoney.credit.loan.domain.LoanInitialInfo;
import com.zdmoney.credit.loan.domain.LoanProduct;
import com.zdmoney.credit.loan.domain.LoanRepaymentDetail;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.offer.domain.OfferRepayInfo;
import com.zdmoney.credit.system.domain.ProdCreditProductTerm;

/**
 * 挖财2 计算器 版本 v2.0
 * 
 * @author Ivan
 *
 */
@Component("FS_00008_v2")
public class WCCalculatorVTwo extends WCCalculatorBase implements ICalculator {

	@Override
	public BigDecimal getOnetimeRepaymentAmount(Long loanId, Date currDate, List<LoanRepaymentDetail> repayList) {
		System.out.println("挖财2 计算器 版本 v2.0");
		return super.getOnetimeRepaymentAmountV1(loanId, currDate, repayList);
	}

	@Override
	public BigDecimal getPenalty(Long loanId, List<LoanRepaymentDetail> repayList, VLoanInfo vLoanInfo) {
		System.out.println("挖财2 计算器 版本 v2.0");
		return super.getPenaltyV1(loanId, repayList, vLoanInfo);
	}
	
	@Override
	public void updateRate(LoanBase loanBase, LoanInitialInfo loanInitialInfo,
			LoanProduct loanProduct,ProdCreditProductTerm prodCreditProductTerm) {

		super.updateRate(loanBase, loanInitialInfo, loanProduct,prodCreditProductTerm);
		
	}
	
	@Override
	public Map<String, Object> createLoanRepaymentDetail(LoanBase loanBase, LoanInitialInfo loanInitialInfo, LoanProduct loanProduct) {
		
		return super.createLoanRepaymentDetailV2(loanBase, loanInitialInfo, loanProduct);
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
