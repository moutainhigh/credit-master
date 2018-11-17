package com.zdmoney.credit.loan.service.pub;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.zdmoney.credit.debit.domain.DebitTransaction;
import com.zdmoney.credit.loan.domain.LoanRepaymentDetail;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.offer.domain.OfferRepayInfo;
import com.zdmoney.credit.offer.domain.OfferTransaction;

/**
 * loan的综合计算serice ，包含所有底层计算规则
 * @author 00232949
 *
 */
public interface IAfterLoanService {
	
	/**
	 * 获取客户每期的还款金额
	 * @param currDate
	 * @param loanId
	 * @return
	 */
	public BigDecimal getPerReapyAmount(Date currDate, Long loanId);
	
	/**
     * 获取客户当期的剩余欠款
     * @param currDate
     * @param loanId
     * @return
     */
    public BigDecimal getCurrentDeficitAmount(Date currDate, Long loanId);
    
    /**
     * 获取当期未结清的记录
     * @param loanId
     * @return
     */
    public LoanRepaymentDetail getLast (Date currDate, Long loanId);

	/**
	 * 获取客户报盘总额
	 * @param currDate
	 * @param id
	 * @return
	 */
	public BigDecimal getAmount(Date currDate, Long id);

	/**
	 * 获取逾期总额 不包含罚息
	 * @param repayList
	 * @param currDate
	 * @return
	 */
	public BigDecimal getOverdueAmount(List<LoanRepaymentDetail> repayList,
			Date currDate);

	/**
	 * 根据当前还款日期、获取逾期罚息
	 * @param repayList
	 * @param currDate
	 * @return
	 */
	public BigDecimal getFine(List<LoanRepaymentDetail> repayList, Date currDate);

	/**
	 * 获取当期总额
	 * @param repayList
	 * @param currDate
	 * @return
	 */
	public BigDecimal getCurrAmount(List<LoanRepaymentDetail> repayList, Date currDate);

	/**
	 * 根据罚息起始期,剩余本息和
	 * @param repayList
	 * @return
	 */
	public BigDecimal getRemnant(List<LoanRepaymentDetail> repayList);
	
	/**
	 * 返回指定日期、指定账号的所有未还（要还）的本息记录 逾期，有可能是当期（当前日期为还款日）
   		包含每期的期数、本息合计、一次性还款金额,退费，剩余本金 等
	 * @param currDate
	 * @param id
	 * @return
	 */
	public List<LoanRepaymentDetail> getAllInterestOrLoan(Date currDate, Long id);

	/**
	 * 根据借款ID判断是否申请过一次性还款，或者是否处于指定状态
	 * @param id
	 * @return
	 */
	public boolean isOneTimeRepayment(Long id);

	/**
	 * 获取该账户中余额（即挂账部分）
	 * @param id
	 * @return
	 */
	public BigDecimal getAccAmount(Long id);

	/**
	 * 获取申请减免罚息金额
	 * @param currDate
	 * @param id
	 * @return
	 */
	public BigDecimal getReliefOfFine(Date currDate, Long id);

	/**
	 * 根据交易日期、约定还款日取得当期的还款日
	 * @param currDate
	 * @param promiseReturnDate
	 * @return
	 */
	public Date getCurrTermReturnDate(Date currDate, Long promiseReturnDate);

	/**
	 * 获取一次性还款金额
	 * @param repayList
	 * @param currDate
	 * @return
	 */
	public BigDecimal getOnetimeRepaymentAmountTmp(List<LoanRepaymentDetail> repayList,Date currDate);
	
	/**
	 * 计算至当前所有应还总和(逾期本息，当前日期问还款日时包含本期本息)注意 不含挂账的金额 ,罚息
	 * @param repayList
	 * @param currDate
	 * @return
	 */
	public BigDecimal sumDeficit(List<LoanRepaymentDetail> repayList,Date currDate);

	/**
	 *  获得罚息天数
	 *  参数 逾期/当期应还款信息 ，交易日期
	 * @param repayList
	 * @param currDate
	 * @return
	 */
	public int getOverdueDay(List<LoanRepaymentDetail> repayList,Date currDate);
	
	
	/**
	 * 根据交易记账
	 * @param offerTransaction
	 * @return
	 */
	public void accountingByTransaction(OfferTransaction offerTransaction);

	/**
	 * 得到凭证号
	 * @param loanId
	 * @return
	 */
	public String getTradeFlowNo(Long loanId);

	/**
	 *  记账核心，处理所有的账务flow操作
	 * @param repayInfoInstance
	 * @return
	 */
	public void repayDeal(OfferRepayInfo repayInfoInstance);

	/**
	 * 提风险金（保证金）流水
	 * 
	 * @param vLoanInfo
	 * @param tradeDate
	 */
	public void drawRiskFee(VLoanInfo vLoanInfo, Calendar tradeDate);
	
	/***
	 * 获取逾期的总期数
	 * @param repayList
	 * @param currDate
	 * @return
	 */
	public int getOverdueTermCount(List<LoanRepaymentDetail> repayList,Date currDate);
	
	/**
	 * 积木盒子的借款，还款日当天早晨，积木风险金垫付全部当期应还
	 * @param loanId
	 * @param currDate
	 */
	public void drawJimuRisk(long loanId, Date currDate);
	
	public void generateFlowForRepayAllJimuPay(long loanId, int curTerm, Date notifyDate);
	
	
	/**
	 * 获取逾期本金
	 * @param repayList 还款明细集合
	 * @param currDate	当前日期
	 * @return
	 */
	public BigDecimal getOverdueCorpus(List<LoanRepaymentDetail> repayList,Date currDate);
	
	
	/**
	 * 获取逾期利息
	 * @param repayList 还款明细集合
	 * @param currDate	当前日期
	 * @return
	 */
	public BigDecimal getOverdueInterest(List<LoanRepaymentDetail> repayList,Date currDate);
	
	/**
	 * 获取当期应还本金
	 * @param repayList 还款明细集合
	 * @param currDate	当前日期
	 * @return
	 */
	public BigDecimal getCurrCorpus(List<LoanRepaymentDetail> repayList,Date currDate);
	
	
	/**
	 * 获取当期应还利息
	 * @param repayList 还款明细集合
	 * @param currDate	当前日期
	 * @return
	 */
	public BigDecimal getCurrInterest(List<LoanRepaymentDetail> repayList,Date currDate);
	
	/**
	 * 获取退费
	 * @param repayList
	 * @return
	 */
	public BigDecimal getGiveBackRate(List<LoanRepaymentDetail> repayList);
	
	
	/**
	 * 获取违约金
	 * @param repayList
	 * @return
	 */
	public BigDecimal getPenaltyTmp(List<LoanRepaymentDetail> repayList,VLoanInfo loan);
	
	public void fillJimuRisk(Long loanId, Date returnDate, Date doFillDate);
	
	public void returnJimuRisk(Long loanId, int currentTerm, BigDecimal pactMoney);
	
	/**
	 * 获取违约金
	 * @param repayList
	 * @return
	 */
	public BigDecimal getPenaltyTmp(List<LoanRepaymentDetail> repayList);

	/**
	 * 获取剩余利息
	 * @param loan
	 * @param currDate
	 * @return
	 */
	public BigDecimal getRemainingInterest(VLoanInfo loan, Date currDate);
	
	/**
	 * 根据债权编号判断是否申请了提前还款
	 * @param loanId
	 * @return
	 */
	public boolean isAdvanceRepayment(Long loanId);
	
	/**
	 * 计算还款计划某一期的罚息
	 * @param detail
	 * @param currDate
	 * @return
	 */
	public BigDecimal getFine(LoanRepaymentDetail detail, Date currDate);
	
	/**
	 * 计算还款计划某一期的罚息
	 * @param detail
	 * @param currDate
	 * @param type（需要把委托还款的本金用来计算罚息的合同来源）
	 * @return
	 */
	public BigDecimal getFine(LoanRepaymentDetail detail, Date currDate, VLoanInfo loanInfo, String type);
	
	/**
	 * 第三方还款减免入账处理
	 *
	 * @param transaction
	 */
	public void accountingByTransaction(DebitTransaction transaction);
}
