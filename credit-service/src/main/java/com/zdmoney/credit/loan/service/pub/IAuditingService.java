package com.zdmoney.credit.loan.service.pub;

import com.zdmoney.credit.loan.domain.VLoanInfo;

/**
 * 功能：对于一个借款的流程控制
 * @author 00232949
 *
 */
public interface IAuditingService {

	/**
	 * 设置成下一个状态
	 * @param loan
	 */
	void setNextFlowState(VLoanInfo loan,boolean toAdvanced);

}
