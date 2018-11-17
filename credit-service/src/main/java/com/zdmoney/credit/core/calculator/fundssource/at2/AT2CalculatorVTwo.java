package com.zdmoney.credit.core.calculator.fundssource.at2;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.core.calculator.pub.ICalculator;
import com.zdmoney.credit.loan.domain.LoanBase;
import com.zdmoney.credit.loan.domain.LoanInitialInfo;
import com.zdmoney.credit.loan.domain.LoanProduct;
import com.zdmoney.credit.loan.domain.LoanRepaymentDetail;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.offer.domain.OfferRepayInfo;
import com.zdmoney.credit.system.domain.ProdCreditProductTerm;

/**
 * 证大爱特2 计算器 版本 v2.0
 * 
 * @author Ivan
 *
 */
@Component("FS_00006_v2")
public class AT2CalculatorVTwo extends AT2CalculatorBase implements ICalculator {

	@Override
	public BigDecimal getOnetimeRepaymentAmount(Long loanId, Date currDate, List<LoanRepaymentDetail> repayList) {
		System.out.println("证大爱特2 计算器 版本 v2.0");
		return super.getOnetimeRepaymentAmountV1(loanId, currDate, repayList);
	}

	@Override
	public BigDecimal getPenalty(Long loanId, List<LoanRepaymentDetail> repayList, VLoanInfo vLoanInfo) {
		System.out.println("证大爱特2 计算器 版本 v2.0");
		return super.getPenaltyV1(loanId, repayList, vLoanInfo);
	}
	
	@Override
	public void updateRate(LoanBase loanBase, LoanInitialInfo loanInitialInfo,
			LoanProduct loanProduct,ProdCreditProductTerm prodCreditProductTerm) {

		throw new PlatformException(ResponseEnum.FULL_MSG, loanBase.getFundsSources()+"计算器暂不支持");
		
	}
	
	@Override
	public Map<String, Object> createLoanRepaymentDetail(LoanBase loanBase, LoanInitialInfo loanInitialInfo,
			LoanProduct loanProduct) {
		
		throw new PlatformException(ResponseEnum.FULL_MSG, loanBase.getFundsSources()+"计算器暂不支持");
	}

	@Override
	public List<LoanRepaymentDetail> createLoanTrial(LoanBase loanBase, LoanInitialInfo loanInitialInfo,
			LoanProduct loanProduct) {
		// TODO Auto-generated method stub
		return null;
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
