package com.zdmoney.credit.loan.dao.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.loan.domain.LoanLedger;

public interface ILoanLedgerDao extends IBaseDao<LoanLedger>{

	/**
	 * 
	 * @param promiseReturnDate
	 * @return
	 */
	List<LoanLedger> getLoanLedgerHasBalanceByDate(int promiseReturnDate);
	
	
	/**
	 * 根据债权ID查找还款账户信息
	 * @param id
	 * @return
	 */
	LoanLedger findByLoanId(Long id);


	/**
	 * 根据账户号查找账务信息
	 * @param account
	 * @return
	 */
	LoanLedger findByAccount(String account);

	/**
	 * 根据债权ID查找还款账户信息(带悲观锁)
	 * @param id
	 * @return
	 */
	LoanLedger findByLoanIdForUpdate(Long id);

	/**
	 * 根据账户号查找账务信息(带悲观锁)
	 * @param account
	 * @return
	 */
	LoanLedger findByAccountForUpdate(String account);
	
	/**
	 * 根据债权去向查询挂账金额信息
	 * @return
	 */
	public List<LoanLedger> getLoanLedgerInfoByLoanBelong(Map<String,Object> params);

	int updateOtherPayableByLoanId(LoanLedger loanLedger);

	/**
	 * 查询挂账金额大于0的数据
	 * @return
	 */
	List<LoanLedger> findMoreThanZero();

}
