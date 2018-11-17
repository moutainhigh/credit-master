package com.zdmoney.credit.fee.dao;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.fee.dao.pub.ILoanFeeImportDataDao;
import com.zdmoney.credit.fee.domain.LoanFeeImportData;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;

/**
 * 借款收费导入数据 Dao实现层，定义一些与系统业务无关的方法
 * 
 * @author Ivan
 */
@Repository
public class LoanFeeImportDataDaoImpl extends BaseDaoImpl<LoanFeeImportData> implements ILoanFeeImportDataDao {

}
