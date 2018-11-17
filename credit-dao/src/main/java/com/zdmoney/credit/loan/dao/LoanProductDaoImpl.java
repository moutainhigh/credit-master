package com.zdmoney.credit.loan.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.loan.dao.pub.ILoanProductDao;
import com.zdmoney.credit.loan.domain.LoanProduct;

@Repository
public class LoanProductDaoImpl  extends BaseDaoImpl<LoanProduct> implements ILoanProductDao{

	@Override
	public LoanProduct findByLoanId(Long loanId) {
		LoanProduct loanProduct = new LoanProduct();
		loanProduct.setLoanId(loanId);
		List<LoanProduct> list = findListByVo(loanProduct);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}

}
