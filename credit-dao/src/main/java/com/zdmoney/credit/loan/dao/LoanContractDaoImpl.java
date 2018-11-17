package com.zdmoney.credit.loan.dao;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.loan.dao.pub.ILoanContractDao;
import com.zdmoney.credit.loan.domain.LoanContract;

/**
 * 生成合同操作
 * @author 00234770
 * @date 2015年8月27日 下午6:11:54 
 *
 */
@Repository
public class LoanContractDaoImpl extends BaseDaoImpl<LoanContract> implements ILoanContractDao {

	/**
	 * 通过loanId查找合同信息
	 * @param loanId
	 * @return
	 */
	@Override
	public LoanContract findByLoanId(Long loanId) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("loanId", loanId);
		List<LoanContract> contracts = findListByMap(paramMap);
		LoanContract contract = null;
		if (contracts != null && !contracts.isEmpty() && contracts.size() == 1) {
			contract = contracts.get(0);
		}
		
		return contract;
	}

}
