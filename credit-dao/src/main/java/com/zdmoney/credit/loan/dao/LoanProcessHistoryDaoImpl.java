package com.zdmoney.credit.loan.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.loan.dao.pub.ILoanProcessHistoryDao;
import com.zdmoney.credit.loan.domain.LoanProcessHistory;

@Repository
public class LoanProcessHistoryDaoImpl extends BaseDaoImpl<LoanProcessHistory>
		implements ILoanProcessHistoryDao {

	@Override
	public Pager searchLoanProcessHistoryWithPg(Map<String, Object> paramMap) {
		Pager pager = (Pager) paramMap.get("pager");
		if (pager == null) {
			pager = new Pager();
		}
		pager.setSearchDataSqlMapId(getIbatisMapperNameSpace()
				+ ".searchLoanProcessHistoryWithPg");
		pager.setSearchCountSqlMapId(getIbatisMapperNameSpace()
				+ ".searchLoanProcessHistoryWithPgCount");
		return doPager(pager, paramMap);
	}

	@Override
	public Pager searchapprovalWithPg(Map<String, Object> paramMap) {
		Pager pager = (Pager) paramMap.get("pager");
		if (pager == null) {
			pager = new Pager();
		}
		pager.setSearchDataSqlMapId(getIbatisMapperNameSpace()
				+ ".searchapprovalWithPg");
		pager.setSearchCountSqlMapId(getIbatisMapperNameSpace()
				+ ".searchapprovalWithPgCount");
		return doPager(pager, paramMap);
	}
}
