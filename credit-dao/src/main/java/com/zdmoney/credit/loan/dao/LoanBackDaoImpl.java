package com.zdmoney.credit.loan.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.loan.dao.pub.ILoanBackDao;
import com.zdmoney.credit.loan.domain.LoanBack;
/**
 * 待回购信息dao封装
 * @author user
 *
 */
@Repository
public class LoanBackDaoImpl extends BaseDaoImpl<LoanBack> implements ILoanBackDao {


}
