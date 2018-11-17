package com.zdmoney.credit.system.service.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.debit.domain.DebitQueueLog;
import com.zdmoney.credit.loan.domain.LoanReturnRecord;
import com.zdmoney.credit.loan.domain.VLoanInfo;

/**
 * @author 10098  2017年3月7日 下午6:25:13
 */
public interface ILoanReturnRecordService {

	/**
	 * @param lrr
	 * @return
	 */
	public List<LoanReturnRecord> findListByVo(LoanReturnRecord lrr);
	
	/**
	 * 根据map参数查询实体类
	 * @param params
	 * @return
	 */
    public List<LoanReturnRecord> findListByMap(Map<String, Object> params);

	/**
	 * @param vLoanInfo
	 * @param map
	 */
	public void saveBuyBackLoanAndUpdateLoan(VLoanInfo vLoanInfo, Map<String, String> map);

	/**
	 * 生成陆金所一次性回购记录
	 * @param debitQueueLog
	 */
	public void createLoanReturnRecord4Lufax(VLoanInfo vLoanInfo, DebitQueueLog debitQueueLog);
}
