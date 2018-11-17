package com.zdmoney.credit.repay.service.pub;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.repay.vo.RepayTrailVo;

import java.util.Date;

public interface IRepayTrailService {
	
	public Pager findWithPg(RepayTrailVo repayTrailVo);
	/**
	 * 获取还款明细信息
	 * @param vLoanInfo
	 * @param tradeDate
	 * @param repayType
	 * @return
	 */
	public RepayTrailVo getRepaymentDetailInfo(VLoanInfo vLoanInfo, Date tradeDate, String repayType);
}
