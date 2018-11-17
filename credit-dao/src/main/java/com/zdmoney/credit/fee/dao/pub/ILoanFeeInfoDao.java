package com.zdmoney.credit.fee.dao.pub;

import com.zdmoney.credit.fee.domain.LoanFeeInfo;
import com.zdmoney.credit.framework.dao.pub.IBaseDao;

/**
 * 借款收费主表 Dao层接口，定义一些与系统业务无关的方法
 * 
 * @author Ivan
 */
public interface ILoanFeeInfoDao extends IBaseDao<LoanFeeInfo> {
	/**
	 * 根据债权编号查询借款收费信息
	 * 
	 * @param paramMap
	 * @return
	 */
	public LoanFeeInfo findLoanFeeInfoByLoanId(Long loanId);

	/**
	 * 判断债权编号是否存在
	 * 
	 * @param loanId
	 *            债权编号
	 * @return
	 */
	public boolean isExistsByLoanId(Long loanId);
}
