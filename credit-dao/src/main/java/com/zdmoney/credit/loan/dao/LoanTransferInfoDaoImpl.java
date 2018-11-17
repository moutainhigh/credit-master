package com.zdmoney.credit.loan.dao;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.loan.dao.pub.ILoanTransferInfoDao;
import com.zdmoney.credit.loan.domain.LoanTransferInfo;

/**
 * @author YM10112 2017年10月17日 下午4:36:57
 */
@Repository
public class LoanTransferInfoDaoImpl extends BaseDaoImpl<LoanTransferInfo> implements ILoanTransferInfoDao{
	
	@Override
	public List<Map<String, Object>> findLoanTransferBatchList() {
		List<Map<String, Object>> result = getSqlSession().selectList(getIbatisMapperNameSpace() + ".findLoanTransferBatchList");
		return result;
	}

	@Override
	public List<Map<String, Object>> findLoanTransferInfoList(Map<String, Object> params) {
		List<Map<String, Object>> result = getSqlSession().selectList(getIbatisMapperNameSpace() + ".findLoanTransferInfoList",params);
		return result;
	}

	@Override
	public LoanTransferInfo findLoanTransferInfoByLoanId(Long loanId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("loanId",loanId);
		return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".findLoanTransferInfoByLoanId",params);
	}
	
}
