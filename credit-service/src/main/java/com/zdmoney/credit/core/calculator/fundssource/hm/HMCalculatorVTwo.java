package com.zdmoney.credit.core.calculator.fundssource.hm;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zdmoney.credit.common.constant.LoanTypeEnum;
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
 * 海门小贷 计算器 版本 v2.0
 * 
 * @author 00234770
 *
 */
@Component("FS_00011_v2")
public class HMCalculatorVTwo extends HMCalculatorBase implements ICalculator {
	@Autowired
	ISequencesService sequencesService;
	@Autowired
	ILoanRepaymentDetailDao loanRepaymentDetailDao;
	
	@Override
	public BigDecimal getOnetimeRepaymentAmount(Long loanId, Date currDate, List<LoanRepaymentDetail> repayList) {
		System.out.println("海门小贷 计算器 版本 v2.0");
		return super.getOnetimeRepaymentAmountV1(loanId, currDate, repayList);
	}

	@Override
	public BigDecimal getPenalty(Long loanId, List<LoanRepaymentDetail> repayList, VLoanInfo vLoanInfo) {
		System.out.println("海门小贷 计算器 版本 v2.0");
		return super.getPenaltyV1(loanId, repayList, vLoanInfo);
	}

	@Override
	public void updateRate(LoanBase loanBase, LoanInitialInfo loanInitialInfo,
			LoanProduct loanProduct,ProdCreditProductTerm prodCreditProductTerm) {
		
		super.updateRate(loanBase, loanInitialInfo, loanProduct, prodCreditProductTerm);
		
		// risk以下可以抽象出来
		
		/**薪生贷的风险金计算方式与其他的不一样；薪生贷 ：风险金 = 合同金额 * 0.06；其他的为：风险金 = 合同金额 * 0.15  **/
		if (loanInitialInfo.getLoanType().equals(LoanTypeEnum.薪生贷.getValue())) {
			loanProduct.setRisk(loanProduct.getPactMoney().multiply(BigDecimal.valueOf(0.06)).setScale(2, BigDecimal.ROUND_HALF_UP));// 风险金
		} else {
			if(loanProduct.getTime() <= 12){
				loanProduct.setRisk(loanProduct.getPactMoney().multiply(BigDecimal.valueOf(0.15)).setScale(2, BigDecimal.ROUND_HALF_UP));// 风险金
			}else{
				loanProduct.setRisk(loanProduct.getPactMoney().multiply(BigDecimal.valueOf(0.17)).setScale(2, BigDecimal.ROUND_HALF_UP));// 风险金
			}
		}
		
		loanProduct.setRateSum(loanProduct.getPactMoney().subtract(loanInitialInfo.getMoney()).subtract(loanProduct.getRisk()));// 收入总和
		
		/*if ((loanProduct.getTime() <= 12 && !loanInitialInfo.getLoanType().equals(LoanTypeEnum.薪生贷.getValue())) || 
				(loanProduct.getTime() == 18 && loanInitialInfo.getLoanType().equals(LoanTypeEnum.学历贷.getValue()))) {
			loanProduct.setManageRateForPartyC(BigDecimal.ZERO);// 丙方管理费
		} else if ((LoanTypeEnum.薪生贷.getValue().equals(loanInitialInfo.getLoanType())) || 
				((LoanTypeEnum.网购达人贷A.getValue().equals(loanInitialInfo.getLoanType()) || LoanTypeEnum.网购达人贷B.getValue().equals(loanInitialInfo.getLoanType()) ||
				  LoanTypeEnum.淘宝商户贷.getValue().equals(loanInitialInfo.getLoanType()) || LoanTypeEnum.保单贷.getValue().equals(loanInitialInfo.getLoanType()) || 
				  LoanTypeEnum.公积金贷.getValue().equals(loanInitialInfo.getLoanType())) && (loanProduct.getTime() == 18))) {
			loanProduct.setManageRateForPartyC(loanProduct.getPactMoney().multiply(new BigDecimal("0.04")).setScale(2, BigDecimal.ROUND_HALF_UP));// 丙方管理费
		} else {
			loanProduct.setManageRateForPartyC(loanProduct.getPactMoney().multiply(new BigDecimal("0.08")).setScale(2, BigDecimal.ROUND_HALF_UP));// 丙方管理费
		}*/
		
		loanProduct.setManageRateForPartyC(BigDecimal.ZERO);// 丙方管理费
		BigDecimal tempRate = loanProduct.getRateSum().subtract(loanProduct.getManageRateForPartyC());
		loanProduct.setReferRate(tempRate.multiply(new BigDecimal("0.45")).setScale(2, BigDecimal.ROUND_HALF_UP));//咨询费
		loanProduct.setEvalRate(tempRate.multiply(new BigDecimal("0.45")).setScale(2, BigDecimal.ROUND_HALF_UP));// 评估费
		loanProduct.setManageRate(tempRate.subtract(loanProduct.getReferRate()).subtract(loanProduct.getEvalRate()).setScale(2, BigDecimal.ROUND_HALF_UP));// 管理费
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
