package com.zdmoney.credit.loan.service.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.loan.domain.LoanEvaluateInfo;
import com.zdmoney.credit.loan.domain.LoanTransferInfo;

/**
 * @author YM10112 2017年10月17日 下午4:27:05
 */
public interface ILoanTransferInfoService {
	/**
	 * 创建债权转让实体类
	 * @param map
	 * @return
	 */
	public LoanTransferInfo bulidLoanTransferInfo(Map<String, String> map);
	
	
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
	 * 判断债权是否进行过转让
	 * @param params
	 * @return
	 */
	public boolean isLoanTransfer(Long personId,Long loanId);
	
	/**
	 * 根据债权Id得到转让对象
	 * @param loanId
	 * @return
	 */
	public LoanTransferInfo findLoanTransferInfoByLoanId(Long loanId);
}
