package com.zdmoney.credit.core.calculator.fundssource.wc;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.zdmoney.credit.common.constant.CalculatorVersionEnum;
import com.zdmoney.credit.core.calculator.base.CalculatorBase;
import com.zdmoney.credit.loan.domain.LoanBase;
import com.zdmoney.credit.loan.domain.LoanInitialInfo;
import com.zdmoney.credit.loan.domain.LoanProduct;
import com.zdmoney.credit.system.domain.ProdCreditProductTerm;

/**
 * 挖财2 计算器 (父类)
 * 
 * 用于实现计算器公共方法
 * 
 * @author Ivan
 *
 */
@Component("FS_00008")
public class WCCalculatorBase extends CalculatorBase {
	
	public void updateRate(LoanBase loanBase, LoanInitialInfo loanInitialInfo, LoanProduct loanProduct, ProdCreditProductTerm prodCreditProductTerm) {

		super.updateRate(loanBase, loanInitialInfo, loanProduct, prodCreditProductTerm);

		// risk以下可以抽象出来
		loanProduct.setRisk(loanProduct.getPactMoney().multiply(BigDecimal.valueOf(0.15)).setScale(2, BigDecimal.ROUND_HALF_UP));// 风险金
		loanProduct.setRateSum(loanProduct.getPactMoney().subtract(loanInitialInfo.getMoney()).subtract(loanProduct.getRisk()));// 收入总和
		loanProduct.setManageRateForPartyC(BigDecimal.ZERO);// 丙方管理费为0
		BigDecimal tempRate = loanProduct.getRateSum().subtract(loanProduct.getManageRateForPartyC());
		loanProduct.setReferRate(tempRate.multiply(new BigDecimal("0.45")).setScale(2, BigDecimal.ROUND_HALF_UP));// 咨询费
		loanProduct.setEvalRate(tempRate.multiply(new BigDecimal("0.45")).setScale(2, BigDecimal.ROUND_HALF_UP));// 评估费
		loanProduct.setManageRate(tempRate.subtract(loanProduct.getReferRate()).subtract(loanProduct.getEvalRate()).setScale(2, BigDecimal.ROUND_HALF_UP));// 管理费
	}
	
	@Override
	public CalculatorVersionEnum getCalculatorVersion() {
		
		/**根据当前时间判断执行哪一个版本的计算器**/
		if(isV2Date()){
			return CalculatorVersionEnum.v1;
		} else {
			return CalculatorVersionEnum.v2;
		}
	}
	@Override
	public int getLoanType(String loanTypeStr) {
		
		 int loanType = "随薪贷、保单贷、公积金贷、网购达人贷A、网购达人贷B".indexOf(loanTypeStr) >= 0 ? 1 :
             ("薪生贷".indexOf(loanTypeStr) >= 0 ? 2 :
             ("随房贷、随房贷A、随房贷B、随车贷、淘宝商户贷、学历贷、卡友贷".indexOf(loanTypeStr) >= 0 ? 3 :
             ("随意贷、随意贷A、随意贷B、随意贷C".indexOf(loanTypeStr) >= 0 ? 4 : -1)));
		
		return loanType;
	}
}
