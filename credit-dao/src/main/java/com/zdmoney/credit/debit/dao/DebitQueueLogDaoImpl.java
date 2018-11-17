package com.zdmoney.credit.debit.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zdmoney.credit.common.util.CollectionUtils;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.debit.dao.pub.IDebitQueueLogDao;
import com.zdmoney.credit.debit.domain.DebitQueueLog;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.repay.vo.DebitQueueManagementVo;

@Repository
public class DebitQueueLogDaoImpl extends BaseDaoImpl<DebitQueueLog> implements	IDebitQueueLogDao {

	@Override
	public void updateDebitQueueLogAfterGetDeductResultNotifyFromLufax(String debitNo, String debitResultState) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("debitNo", debitNo);
		paramMap.put("debitResultState", debitResultState);
		getSqlSession().update(getIbatisMapperNameSpace() + ".updateDebitQueueLogAfterGetDeductResultNotifyFromLufax",paramMap);
	}

	@Override
	public Pager findDebitQueueManagementWithPg(DebitQueueManagementVo debitQueueManagementVo) {
        Pager pager = (Pager) debitQueueManagementVo.getPager();
        if (pager == null) {
            pager = new Pager();
        }
        pager.setSearchDataSqlMapId(getIbatisMapperNameSpace() + ".searchDebitQueueManagementResult");
        pager.setSearchCountSqlMapId(getIbatisMapperNameSpace() + ".searchDebitQueueManagementCount");
        return doPager(pager, debitQueueManagementVo);
	}

	@Override
	public Map<String, Object> findTradeDateByDebitNo(Map<String, Object> map) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		result = getSqlSession().selectList(getIbatisMapperNameSpace() + ".findTradeDateByDebitNo",map);
		if(CollectionUtils.isNotEmpty(result)){
			return result.get(0);
		}else{
			return null;
		}
	}

	public Long getRepaymentCurrentTerm(Map<String, Object> params) {
		return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".getRepaymentCurrentTerm", params);
	}

	public Map<String, Object> getOverdueRepaymentTerm(Map<String, Object> params) {
		return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".getOverdueRepaymentTerm", params);
	}

	@Override
	public List<DebitQueueLog> findEntrustAndOverdueDebit(Map<String, Object> params) {
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".findEntrustAndOverdueDebit", params);
	}

	@Override
	public List<DebitQueueLog> findEntrustAndAdvanceClearDebit() {
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".findEntrustAndAdvanceClearDebit");
	}
}
