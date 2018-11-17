package com.zdmoney.credit.fee.dao;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.fee.dao.pub.ILoanFeeAmountChangeDao;
import com.zdmoney.credit.fee.domain.LoanFeeAmountChange;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;

/**
 * 记录服务费记账之后 实收和未收历史数据 Dao实现层，定义一些与系统业务无关的方法
 * 
 * @author Ivan
 */
@Repository
public class LoanFeeAmountChangeDaoImpl extends BaseDaoImpl<LoanFeeAmountChange> implements ILoanFeeAmountChangeDao {

}
