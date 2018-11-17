package com.zdmoney.credit.fee.dao;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.fee.dao.pub.ILoanFeeRepayInfoDao;
import com.zdmoney.credit.fee.domain.LoanFeeRepayInfo;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;

/**
 * 借款收费记账表 Dao实现层，定义一些与系统业务无关的方法
 * 
 * @author Ivan
 */
@Repository
public class LoanFeeRepayInfoDaoImpl extends BaseDaoImpl<LoanFeeRepayInfo> implements ILoanFeeRepayInfoDao {

}
