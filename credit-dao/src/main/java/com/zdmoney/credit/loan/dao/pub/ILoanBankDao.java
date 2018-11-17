package com.zdmoney.credit.loan.dao.pub;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.loan.domain.LoanBank;

import java.util.Map;

public interface ILoanBankDao extends IBaseDao<LoanBank>{
	
	/**
	 * 根据银行卡号查找
	 * @param account
	 * @return
	 */
	public LoanBank findByAccount(String account);
	
	public String findNumByLoanId(Long loanId);

	/**
	 * 查询银行卡信息
	 * @param params
	 * @return
	 */
	public LoanBank findLoanBankByWm3(Map<String, Object> params);
}
