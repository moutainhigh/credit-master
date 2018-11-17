package com.zdmoney.credit.core.calculator.fundssource.bh2;

import org.springframework.stereotype.Component;

import com.zdmoney.credit.common.constant.CalculatorVersionEnum;
import com.zdmoney.credit.core.calculator.base.CalculatorBase;
import com.zdmoney.credit.loan.domain.LoanBase;
import com.zdmoney.credit.loan.domain.LoanInitialInfo;
import com.zdmoney.credit.loan.domain.LoanProduct;
import com.zdmoney.credit.system.domain.ProdCreditProductTerm;

/**
 * 渤海2 计算器
 * @author wangn  2016年9月19日16:35:00
 *
 */

@Component("FS_00015")
public class BH2CalculatorBase extends CalculatorBase {
	
	public void updateRate(LoanBase loanBase, LoanInitialInfo loanInitialInfo, LoanProduct loanProduct,
			ProdCreditProductTerm prodCreditProductTerm) {
		super.updateRate(loanBase, loanInitialInfo, loanProduct,prodCreditProductTerm);
	}
	
	@Override
	public CalculatorVersionEnum getCalculatorVersion() {
		return CalculatorVersionEnum.v2;
	}
	
	@Override
	public int getLoanType(String loanTypeStr) {
		 if("随薪贷、保单贷、公积金贷、网购达人贷A、网购达人贷B".indexOf(loanTypeStr) >= 0){
			 return 1;
		 }
		 if("薪生贷".indexOf(loanTypeStr) >= 0){
			 return 2;
		 }
		 if("随房贷、随房贷A、随房贷B、随车贷、淘宝商户贷、学历贷、卡友贷".indexOf(loanTypeStr) >= 0){
			 return 3;
		 }
		 if("随意贷、随意贷A、随意贷B、随意贷C".indexOf(loanTypeStr) >= 0){
			 return 4;
		 }
		return -1;
	}
}
