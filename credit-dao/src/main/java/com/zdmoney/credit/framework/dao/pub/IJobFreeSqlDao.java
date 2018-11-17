package com.zdmoney.credit.framework.dao.pub;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.zdmoney.credit.common.util.exceldata.JimuData;
import com.zdmoney.credit.framework.domain.JobFreeSqlDomain;

public interface IJobFreeSqlDao extends IBaseDao<JobFreeSqlDomain>{

	public void overdueDetailsJobDelete();
	public void overdueDetailsJobInsert();
	
	/**
	 * 按cur_date日期删除
	 * @param dateStr yyyy-MM-dd日期格式字符串
	 */
	public void overdueStatJobDelete(String dateStr);
	
	public void overdueStatJobInsert();
	
	/**
	 * OverdueRatioStatJob  删除数据
	 * @param dateStr
	 */
	public void overdueRatioStatJobDelete(String dateStr);
	
	/**
	 * 对应老系统OverdueRatioStatJob中取数据的Hql
	 * @return
	 */
	public List<Map<String, Object>> orsjSelectData();
	
	/**
	 * OverdueRatioStatJob取loan_id
	 * @param params
	 * @return
	 */
	public List<Long> orsjSelectLoanId(Map<String, Object> params);
	
	public List<Date> orsjSelectReturnDate(Map<String, Object> params);
	
	/**
	 * 按逾期天数求逾期金额总和的查询
	 * @param params
	 * @return 字符串形式的金额数字
	 */
	public String orsjSelectOverdueSum(Map<String, Object> params);
	
	/**
	 * 对应OverdueRatioStatJob else分支的check
	 * @return
	 */
	public List<Integer> orsjSelectCheck();
	
	public void orsjInsertRatioNew();
	
	public List<Long> getDrawJimuRiskData(Map<String, Object> param);
	
	public List<Map<String, Object>> jimuMailData(Map<String, Object> param);
	
	public List<JimuData> jimuNotifyData(Map<String, Object> param);
	
	public List<Map<String, Object>> jimuReturnRiskData(Map<String, Object> param);
	
	public List<Long> jimuFillRiskData(Map<String, Object> param);
	
	public List<LinkedHashMap<String, Object>> getExportBorrower(Map<String, Object> params);
	
	public List<LinkedHashMap<String, Object>> getExportLoan(Map<String, Object> params);
	
	public List<LinkedHashMap<String, Object>> getExportContact(Map<String, Object> params);
	
	public List<LinkedHashMap<String, Object>> getExportTel(Map<String, Object> params);
	
	public List<LinkedHashMap<String, Object>> getExportAddress(Map<String, Object> params);
	
	public List<LinkedHashMap<String, Object>> getExportRepaymentDetial(Map<String, Object> params);
	
	public List<LinkedHashMap<String, Object>> getExportRepayInfo(Map<String, Object> params);
	
	public List<LinkedHashMap<String, Object>> getExportFlow(Map<String, Object> params);
	
	public List<LinkedHashMap<String, Object>> getExportPersonTotal(Map<String, Object> params);
	
	public List<LinkedHashMap<String, Object>> getExportDepartment(Map<String, Object> params);
	
	public void residualPactMoneyDetailJobDelete(Date date);
	
	public void residualPactMoneyDetailJobInsert();
	
	public String updateOverdueHistory(String dateStr);

	public void organizationMarginJobDelete();

	public void organizationMarginJobInsert();
	
	public List<LinkedHashMap<String, Object>> exportLoanStatusDetail(Map<String, Object> params);
	
	public List<LinkedHashMap<String, Object>> exportLoanStatusDetailTrimester(Map<String, Object> params);
	
	public List<LinkedHashMap<String, Object>> exportLoanCustomers(Map<String, Object> params);
	
	public List<LinkedHashMap<String, Object>> exportTrainLoanCustomers(Map<String, Object> params);
	
	public void dayReportSnapshotJobInsert();
}
