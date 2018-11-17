package com.zdmoney.credit.loan.service.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.loan.domain.LoanLedger;

public interface ILoanLedgerService {

	/**
	 * 得到该还款日在账户中存在挂账的loan
	 * @param promiseReturnDate
	 * @return
	 */
	public List<LoanLedger> getLoanLedgerHasBalanceByDate(int promiseReturnDate);

	

	/**
	 * 根据债权ID查找账务表信息
	 * @param id
	 * @return
	 */
	public LoanLedger findByLoanId(Long id);



	/**
	 * 根据账户号查询账务表信息
	 * @param account
	 * @return
	 */
	public LoanLedger findByAccount(String account);



	/**
	 * 更新
	 * @param led
	 */
	public int update(LoanLedger led);



	/**
	 * 立即保存
	 * @param led
	 */
	public void saveNow(LoanLedger led);


	/**
	 * 根据债权ID查找账务表信息(带悲观锁)
	 * @param id
	 * @return
	 */
	public LoanLedger findByLoanIdForUpdate(Long id);


	/**
	 * 根据账户号查询账务表信息(带悲观锁)
	 * @param account
	 * @return
	 */
	public LoanLedger findByAccountForUpdate(String account);
	
	/**
	 * 根据债权去向查询挂账金额信息
	 * @return
	 */
	public List<LoanLedger> getLoanLedgerInfoByLoanBelong(Map<String,Object> params);


	public int updateOtherPayableByLoanId(LoanLedger loanLedger);


	/**
	 * 处理大额挂账金额
	 */
	public void dealBigAmount();

}
