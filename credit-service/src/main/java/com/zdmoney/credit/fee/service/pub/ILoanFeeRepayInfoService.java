package com.zdmoney.credit.fee.service.pub;

import java.util.List;

import com.zdmoney.credit.fee.domain.LoanFeeRepayInfo;
import com.zdmoney.credit.fee.vo.RepayDealVo;

/**
 * 借款收费记账表 Service层接口，定义一些与系统业务相关的方法
 * 
 * @author Ivan
 *
 */
public interface ILoanFeeRepayInfoService {

	/**
	 * PK换实体
	 * 
	 * @param id
	 */
	public LoanFeeRepayInfo findById(Long id);

	/**
	 * 跟据实体类查询
	 * 
	 * @param loanFeeRepayInfo
	 */
	public List<LoanFeeRepayInfo> findListByVo(LoanFeeRepayInfo loanFeeRepayInfo);

	/**
	 * 服务费记账
	 * 
	 * @param repayDealVo
	 *            记账相关信息
	 * @return
	 */
	public LoanFeeRepayInfo repayDeal(RepayDealVo repayDealVo);

}
