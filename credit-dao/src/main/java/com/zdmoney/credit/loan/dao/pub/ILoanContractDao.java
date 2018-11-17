package com.zdmoney.credit.loan.dao.pub;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.loan.domain.LoanContract;

/**
 * 生成合同操作DAO接口定义
 * @author 00234770
 * @date 2015年8月27日 下午6:12:46 
 *
 */
public interface ILoanContractDao extends IBaseDao<LoanContract> {

	/**
	 * 通过loanId查找合同信息
	 * @param loanId
	 * @return
	 */
	public LoanContract findByLoanId(Long loanId);
}
