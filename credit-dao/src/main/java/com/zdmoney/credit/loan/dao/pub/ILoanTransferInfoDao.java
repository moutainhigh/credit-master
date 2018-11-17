package com.zdmoney.credit.loan.dao.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.loan.domain.LoanTransferInfo;

/**
 * @author YM10112 2017年10月17日 下午4:37:05
 */
public interface ILoanTransferInfoDao extends IBaseDao<LoanTransferInfo> {
	/**
	 * 查询不重复的债权转让批次号
	 * @return
	 */
	public List<Map<String, Object>> findLoanTransferBatchList();
	
	/**
	 * 查询债权转让的具体信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> findLoanTransferInfoList(Map<String, Object> params);
	
	/**
	 * 根据债权Id得到数据
	 * @param loanId
	 * @return
	 */
	public LoanTransferInfo findLoanTransferInfoByLoanId(Long loanId);
}
