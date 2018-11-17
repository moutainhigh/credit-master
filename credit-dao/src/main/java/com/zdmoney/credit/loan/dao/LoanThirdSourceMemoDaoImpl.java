package com.zdmoney.credit.loan.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.loan.dao.pub.ILoanThirdSourceMemoDao;
import com.zdmoney.credit.loan.domain.LoanThirdSourceMemo;

@Repository
public class LoanThirdSourceMemoDaoImpl extends BaseDaoImpl<LoanThirdSourceMemo> implements ILoanThirdSourceMemoDao {

	@Override
	public LoanThirdSourceMemo findByOfferRepayInfoId(Long offerRepayInfoId) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("offerRepayInfoId", offerRepayInfoId);
		
		List<LoanThirdSourceMemo> loanThirdSourceMemos = getSqlSession().selectList(getIbatisMapperNameSpace() + ".findByOfferRepayInfoId", offerRepayInfoId);
		if (loanThirdSourceMemos != null && loanThirdSourceMemos.size() > 0) {
			return loanThirdSourceMemos.get(0);
		}
		return null;
	}

}
