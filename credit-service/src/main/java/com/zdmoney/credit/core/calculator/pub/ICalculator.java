package com.zdmoney.credit.core.calculator.pub;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.zdmoney.credit.loan.domain.LoanBase;
import com.zdmoney.credit.loan.domain.LoanInitialInfo;
import com.zdmoney.credit.loan.domain.LoanProduct;
import com.zdmoney.credit.loan.domain.LoanRepaymentDetail;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.offer.domain.OfferRepayInfo;
import com.zdmoney.credit.system.domain.ProdCreditProductTerm;

/**
 * 核心计算器接口
 * 
 * @author Ivan
 *
 */
public interface ICalculator {
	/**
	 * 获取一次性还款金额
	 * 
	 * @param loanId
	 *            债权编号
	 * @param currDate
	 *            当前时间
	 * @param repayList
	 *            （可选）还款计划列表（通过此列表计算一次性还款金额）
	 * @return
	 */
	public BigDecimal getOnetimeRepaymentAmount(Long loanId, Date currDate, List<LoanRepaymentDetail> repayList);

	/**
	 * 获取违约金
	 * 
	 * @param loanId
	 *            债权编号
	 * @param repayList
	 *            还款计划列表
	 * @param vLoanInfo
	 *            债权实例
	 * @return
	 */
	public BigDecimal getPenalty(Long loanId, List<LoanRepaymentDetail> repayList, VLoanInfo vLoanInfo);
	
	/**
	 * 计算合同金额等
	 * 
	 * @param loanBase
	 * @param loanInitialInfo
	 * @param loanProduct
	 * @param prodCreditProductTerm
	 */
	public void updateRate(LoanBase loanBase,LoanInitialInfo loanInitialInfo,LoanProduct loanProduct,ProdCreditProductTerm prodCreditProductTerm);
	
	/**
	 * 创建还款计划
	 * 
	 * @param loanBase
	 * @param loanInitialInfo
	 * @param loanProduct
	 * @return
	 */
	public Map<String, Object> createLoanRepaymentDetail(LoanBase loanBase,LoanInitialInfo loanInitialInfo,LoanProduct loanProduct);
	
	/**
	 * 贷前试算
	 * 
	 * @param loanBase
	 * @param loanInitialInfo
	 * @param loanProduct
	 * @return
	 */
	public List<LoanRepaymentDetail> createLoanTrial(LoanBase loanBase,LoanInitialInfo loanInitialInfo,LoanProduct loanProduct);
	
	/**
	 * 入账（开户、还款、提现、预存等）之后触发特殊业务逻辑
	 * @param offerRepayInfo 入账实体
	 * @return
	 */
	public boolean enterAccountAfter(OfferRepayInfo offerRepayInfo);
	
	/**
	 * 入账（开户、还款、提现、预存等）之前触发特殊业务逻辑
	 * @param offerRepayInfo 入账实体
	 * @return 
	 *     返回假时 不做入账操作
	 */
	public boolean enterAccountBefore(OfferRepayInfo offerRepayInfo);
	
}
