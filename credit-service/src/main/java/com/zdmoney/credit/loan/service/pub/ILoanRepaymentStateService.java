package com.zdmoney.credit.loan.service.pub;

import java.util.Date;


/**
 * 历史还款状态
 * @author Charming
 *
 */
public interface ILoanRepaymentStateService {
	/**
	 * 创建历史还款状态
	 */
	public void createLoanRepaymentState(Date currDate) throws Exception;
	
}
