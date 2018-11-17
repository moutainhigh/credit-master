package com.zdmoney.credit.loan.service.pub;

import com.zdmoney.credit.loan.domain.LoanSettleInfo;
import com.zdmoney.credit.loan.domain.LoanTransferInfo;

import java.util.List;
import java.util.Map;

/**
 *
 */
public interface ILoanSettleInfoService {
	/**
	 * 创建债权转让实体类
	 * @param map
	 * @return
	 */
	public LoanSettleInfo bulidLoanSettleInfo(Map<String, String> map);
	/**
	 * 根据债权Id得到转让对象
	 * @param loanId
	 * @return
	 */
	public LoanSettleInfo findLoanSettleInfoByLoanId(Long loanId);
}
