package com.zdmoney.credit.loan.service.pub;

import java.util.Map;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.loan.domain.LoanBank;

/**
 * 
 * @author 00232949
 *
 */
public interface ILoanBankService {

	/**
	 * 根据id查询
	 * @param giveBackBankId
	 * @return
	 */
	public LoanBank findById(Long giveBackBankId);
	
	/**
	 * 跟据Map 查询结果集
	 * @param paramMap 查询参数
	 * @return
	 */
	public Pager findWithPgByMap(Map<String,Object> paramMap);
	
	
	/**
	 * 新增或更新操作
	 * @param loanBank
	 * @return
	 */
	public LoanBank saveOrUpdate(LoanBank loanBank);
	
	/**
	 * 核心接口中新增或更新操作
	 * @param loanBank
	 * @return
	 */
	public LoanBank saveOrUpdateCore(LoanBank loanBank);
	
	/**
	 * 根据loanLd查询
	 * 
	 * 
	 */
	public String findNumByLoanId(Long loanId);
}
