package com.zdmoney.credit.loan.service.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.loan.domain.LoanEvaluateInfo;

/**
 * @author YM10112 2017年10月17日 下午4:27:05
 */
public interface ILoanEvaluateInfoService {
	
	
	/**
	 * 创建债权评估实体类
	 * @param map
	 * @return
	 */
	public LoanEvaluateInfo bulidLoanEvaluateInfo(Map<String, String> map);
	
	/**
	 * 得到债权评估对象
	 * @param loanId
	 * @return
	 */
	public LoanEvaluateInfo findLoanEvaluateInfo(Map<String, Object> params);
	
	
	/**
	 * 查询债权评估的具体信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> findLoanEvaluateInfoList(Map<String, Object> params);
	
	
	public Map<String, Object>  updateLoanEvaluateSign(String loanId);
	
	
}
