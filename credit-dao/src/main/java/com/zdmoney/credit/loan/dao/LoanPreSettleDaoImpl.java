package com.zdmoney.credit.loan.dao;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.loan.dao.pub.ILoanPreSettleDao;
import com.zdmoney.credit.loan.domain.LoanPreSettle;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Repository
public class LoanPreSettleDaoImpl extends BaseDaoImpl<LoanPreSettle> implements ILoanPreSettleDao{

}
