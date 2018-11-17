package com.zdmoney.credit.system.dao.pub;

/**
 * 存储过程dao
 * @author 00232949
 *
 */
public interface IStoredProcedureDao {

	
	/**
	 * 放款撤销存储过程
	 * @param loanId
	 * @param userCode
	 * @return
	 */
	int rollbackGrantloan(Long loanId, String userCode);

}
