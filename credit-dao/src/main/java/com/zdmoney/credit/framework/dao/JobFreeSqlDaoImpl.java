package com.zdmoney.credit.framework.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.common.util.exceldata.JimuData;
import com.zdmoney.credit.framework.dao.pub.IJobFreeSqlDao;
import com.zdmoney.credit.framework.domain.JobFreeSqlDomain;
@Repository
public class JobFreeSqlDaoImpl extends BaseDaoImpl<JobFreeSqlDomain> implements IJobFreeSqlDao{

	@Override
	public void overdueDetailsJobDelete() {
		getSqlSession().delete(getIbatisMapperNameSpace() + ".overdueDetailsJobDelete");
		
	}

	@Override
	public void overdueDetailsJobInsert() {
		getSqlSession().insert(getIbatisMapperNameSpace() + ".overdueDetailsJobInsert");
	}

	@Override
	public void overdueStatJobDelete(String dateStr) {
		getSqlSession().delete(getIbatisMapperNameSpace() + ".overdueStatJobDelete", dateStr);
		
	}

	@Override
	public void overdueStatJobInsert() {
		getSqlSession().insert(getIbatisMapperNameSpace() + ".overdueStatJobInsert");
		
	}

	@Override
	public void overdueRatioStatJobDelete(String dateStr) {
		getSqlSession().delete(getIbatisMapperNameSpace() + ".overdueRatioStatJobDelete", dateStr);
		
	}

	@Override
	public List<Map<String, Object>> orsjSelectData() {
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".orsjSelectData");
	}

	@Override
	public List<Long> orsjSelectLoanId(Map<String, Object> params) {
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".orsjSelectData", params);
	}

	@Override
	public List<Date> orsjSelectReturnDate(Map<String, Object> params) {
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".orsjSelectReturnDate", params);
	}

	@Override
	public String orsjSelectOverdueSum(Map<String, Object> params) {
		return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".orsjSelectOverdueSum", params);
	}

	@Override
	public List<Integer> orsjSelectCheck() {
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".orsjSelectCheck");
	}

	@Override
	public void orsjInsertRatioNew() {
		getSqlSession().insert(getIbatisMapperNameSpace() + ".orsjInsertRatioNew");
	}

	@Override
	public List<Long> getDrawJimuRiskData(Map<String, Object> param) {
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".getDrawJimuRiskData", param);
	}

	@Override
	public List<Map<String, Object>> jimuMailData(Map<String, Object> param) {
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".jimuMailData", param);
	}

	@Override
	public List<JimuData> jimuNotifyData(Map<String, Object> param) {
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".jimuNotifyData", param);
	}

	@Override
	public List<Map<String, Object>> jimuReturnRiskData(
			Map<String, Object> param) {
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".jimuReturnRiskData", param);
	}

	@Override
	public List<Long> jimuFillRiskData(Map<String, Object> param) {
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".jimuFillRiskData", param);
	}

	@Override
	public List<LinkedHashMap<String, Object>> getExportBorrower(Map<String, Object> params) {
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".getExportBorrower", params);
	}

	@Override
	public List<LinkedHashMap<String, Object>> getExportLoan(Map<String, Object> params) {
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".getExportLoan", params);
	}

	@Override
	public List<LinkedHashMap<String, Object>> getExportContact(Map<String, Object> params) {
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".getExportContact", params);
	}

	@Override
	public List<LinkedHashMap<String, Object>> getExportTel(Map<String, Object> params) {
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".getExportTel", params);
	}

	@Override
	public List<LinkedHashMap<String, Object>> getExportAddress(Map<String, Object> params) {
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".getExportAddress", params);
	}

	@Override
	public List<LinkedHashMap<String, Object>> getExportRepaymentDetial(
			Map<String, Object> params) {
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".getExportRepaymentDetial", params);
	}

	@Override
	public List<LinkedHashMap<String, Object>> getExportRepayInfo(
			Map<String, Object> params) {
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".getExportRepayInfo", params);
	}

	@Override
	public List<LinkedHashMap<String, Object>> getExportFlow(
			Map<String, Object> params) {
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".getExportFlow", params);
	}

	@Override
	public List<LinkedHashMap<String, Object>> getExportPersonTotal(Map<String, Object> params) {
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".getExportPersonTotal", params);
	}

	@Override
	public List<LinkedHashMap<String, Object>> getExportDepartment(Map<String, Object> params) {
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".getExportDepartment", params);
	}

	@Override
	public void residualPactMoneyDetailJobDelete(Date date) {
		getSqlSession().delete(getIbatisMapperNameSpace() + ".residualPactMoneyDetailJobDelete", date);
	}

	@Override
	public void residualPactMoneyDetailJobInsert() {
		getSqlSession().insert(getIbatisMapperNameSpace() + ".residualPactMoneyDetailJobInsert");
	}

	@Override
	public String updateOverdueHistory(String dateStr) {
		return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".updateOverdueHistory", dateStr);
	}

	@Override
	public void organizationMarginJobDelete() {
		getSqlSession().delete(getIbatisMapperNameSpace() + ".organizationMarginJobDelete");
	}

	@Override
	public void organizationMarginJobInsert() {
		getSqlSession().insert(getIbatisMapperNameSpace() + ".organizationMarginJobInsert");
	}

	@Override
	public List<LinkedHashMap<String, Object>> exportLoanStatusDetail(Map<String, Object> params) {
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".exportLoanStatusDetail", params);
	}

	@Override
	public List<LinkedHashMap<String, Object>> exportLoanStatusDetailTrimester(Map<String, Object> params) {
		return getSqlSession().selectList(getIbatisMapperNameSpace()+".exportLoanStatusDetailTrimester",params);
	}

	@Override
	public List<LinkedHashMap<String, Object>> exportLoanCustomers(
			Map<String, Object> params) {
		return getSqlSession().selectList(getIbatisMapperNameSpace()+".exportLoanCustomers",params);
	}

	@Override
	public List<LinkedHashMap<String, Object>> exportTrainLoanCustomers(
			Map<String, Object> params) {
		return getSqlSession().selectList(getIbatisMapperNameSpace()+".exportTrainLoanCustomers",params);
	}

	@Override
	public void dayReportSnapshotJobInsert() {
		Calendar  date = Calendar.getInstance();
		String day= String.valueOf(date.get(Calendar.DAY_OF_MONTH));
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("day", day);
		getSqlSession().insert(getIbatisMapperNameSpace() + ".dayReportSnapshotJobInsert", params);
	}
}
