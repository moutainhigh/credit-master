package com.zdmoney.credit.loan.dao.pub;


import java.util.List;
import java.util.Map;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.loan.domain.LoanEvaluateInfo;
import com.zdmoney.credit.loan.domain.LoanTransferInfo;

/**
 * @author YM10112 2017年11月14日 下午4:43:14
 */
 
public interface ILoanEvaluateInfoDao extends IBaseDao<LoanEvaluateInfo> {
	
	/**
	 * 得到债权评估对象
	 * @param loanId
	 * @return
	 */
	public LoanEvaluateInfo findLoanEvaluateInfo(Map<String, Object> params);
	
	/**
	 * 查询债权评估list数据的具体信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> findLoanEvaluateInfoList(Map<String, Object> params);
	
	
}
