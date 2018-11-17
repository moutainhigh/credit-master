package com.zdmoney.credit.loan.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.loan.dao.pub.ILoanOverdueHistoryDao;
import com.zdmoney.credit.loan.domain.LoanOverdueHistory;

@Repository
public class LoanOverdueHistoryDaoImpl extends BaseDaoImpl<LoanOverdueHistory> implements ILoanOverdueHistoryDao {

	@Override
	public LoanOverdueHistory findByLoanId(Long id) {
		LoanOverdueHistory loan= new LoanOverdueHistory();
		loan.setLoanId(id);
		List<LoanOverdueHistory> ls = this.findListByVo(loan);
		if(ls!=null && ls.size()>0){
			return ls.get(0);
		}
		return null;
	}

}
