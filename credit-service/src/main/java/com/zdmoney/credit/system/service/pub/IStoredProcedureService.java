package com.zdmoney.credit.system.service.pub;

/**
 * 存储过程service 所有存储过程都在本类调用
 * @author 00232949
 *
 */
public interface IStoredProcedureService {

	/**
	 * 放款撤销存储过程
	 * @param l
	 * @return 
	 */
	int rollbackGrantloan(Long l, String userCode);

}
