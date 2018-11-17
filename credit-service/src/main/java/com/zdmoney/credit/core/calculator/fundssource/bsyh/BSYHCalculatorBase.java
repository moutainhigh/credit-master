package com.zdmoney.credit.core.calculator.fundssource.bsyh;

import org.springframework.stereotype.Component;

import com.zdmoney.credit.common.constant.CalculatorVersionEnum;
import com.zdmoney.credit.core.calculator.base.CalculatorBase;

/**
 * 包商银行 计算器 (父类)
 * 
 * 用于实现计算器公共方法
 * @author YM10104
 *
 */
@Component("FS_00018")
public class BSYHCalculatorBase extends CalculatorBase {
	@Override
	public CalculatorVersionEnum getCalculatorVersion() {
		
		return CalculatorVersionEnum.v2;
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
