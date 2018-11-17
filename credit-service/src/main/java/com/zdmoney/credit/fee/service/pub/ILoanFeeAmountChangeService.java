package com.zdmoney.credit.fee.service.pub;

import com.zdmoney.credit.fee.domain.LoanFeeAmountChange;

/**
 * 记录服务费记账之后 实收和未收历史数据 Service层接口，定义一些与系统业务相关的方法
 * 
 * @author Ivan
 *
 */
public interface ILoanFeeAmountChangeService {
	/**
	 * 新增
	 * 
	 * @param loanFeeAmountChange
	 * @return
	 */
	public LoanFeeAmountChange insert(LoanFeeAmountChange loanFeeAmountChange);
}
