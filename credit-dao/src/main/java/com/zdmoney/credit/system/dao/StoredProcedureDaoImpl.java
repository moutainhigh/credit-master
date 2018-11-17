package com.zdmoney.credit.system.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.system.dao.pub.IStoredProcedureDao;

@Repository
public class StoredProcedureDaoImpl extends BaseDaoImpl implements IStoredProcedureDao{

	@Override
	public int rollbackGrantloan(Long loanId, String userCode) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
       paramMap.put("procedureName", "rollback_grantloan");
       paramMap.put("loanId", loanId);
       paramMap.put("userCode", userCode);
//		String procedureSeq = "rollback_grantloan("+loanId+")";
		return (int) selectProcedure(paramMap);
	}

}
