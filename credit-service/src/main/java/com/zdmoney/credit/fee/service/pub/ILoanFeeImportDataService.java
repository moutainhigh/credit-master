package com.zdmoney.credit.fee.service.pub;

import java.util.List;

import com.zdmoney.credit.fee.domain.LoanFeeImportData;

/**
 * 借款收费导入数据 Service层接口，定义一些与系统业务相关的方法
 * 
 * @author Ivan
 *
 */
public interface ILoanFeeImportDataService {

	/**
	 * 跟据实体类查询
	 * 
	 * @param loanFeeImportData
	 */
	public List<LoanFeeImportData> findListByVo(LoanFeeImportData loanFeeImportData);
	/**
	 * 新增放款导入日志
	 * @param loanFeeImportData
	 * @return
	 */
	public LoanFeeImportData save(LoanFeeImportData loanFeeImportData);
}
