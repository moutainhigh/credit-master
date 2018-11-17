package com.zdmoney.credit.loan.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.loan.dao.pub.ILoanEvaluateInfoDao;
import com.zdmoney.credit.loan.domain.LoanEvaluateInfo;

/**
 * @author YM10112 2017年11月15日 上午10:55:05
 */
 
@Repository
public class LoanEvaluateInfoDaoImpl extends BaseDaoImpl<LoanEvaluateInfo> implements ILoanEvaluateInfoDao{

	@Override
	public LoanEvaluateInfo findLoanEvaluateInfo(Map<String, Object> params) {
		return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".findLoanEvaluateInfo", params);
	}

	@Override
	public List<Map<String, Object>> findLoanEvaluateInfoList(Map<String, Object> params) {
		List<Map<String, Object>> result =  getSqlSession().selectList(getIbatisMapperNameSpace() + ".findLoanEvaluateInfoList", params);
		return result;
	}
	
}
