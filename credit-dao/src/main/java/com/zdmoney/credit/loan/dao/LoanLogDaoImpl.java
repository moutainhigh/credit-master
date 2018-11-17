package com.zdmoney.credit.loan.dao;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.loan.dao.pub.ILoanLogDao;
import com.zdmoney.credit.loan.domain.LoanLog;


@Repository
public class LoanLogDaoImpl extends BaseDaoImpl<LoanLog> implements ILoanLogDao{

}
