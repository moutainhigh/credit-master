package com.zdmoney.credit.fee.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.fee.dao.pub.ILoanFeeImportDataDao;
import com.zdmoney.credit.fee.domain.LoanFeeImportData;
import com.zdmoney.credit.fee.service.pub.ILoanFeeImportDataService;

/**
 * 借款收费导入数据 Service实现层，定义一些与系统业务相关的方法
 * 
 * @author Ivan
 *
 */
@Service
public class LoanFeeImportDataServiceImpl implements ILoanFeeImportDataService {

	protected static Log logger = LogFactory.getLog(LoanFeeImportDataServiceImpl.class);

	@Autowired
	@Qualifier("loanFeeImportDataDaoImpl")
	ILoanFeeImportDataDao loanFeeImportDataDaoImpl;

	@Override
	public List<LoanFeeImportData> findListByVo(LoanFeeImportData loanFeeImportData) {
		return loanFeeImportDataDaoImpl.findListByVo(loanFeeImportData);
	}

	@Override
	public LoanFeeImportData save(LoanFeeImportData loanFeeImportData) {
		return loanFeeImportDataDaoImpl.insert(loanFeeImportData);
	}

}
