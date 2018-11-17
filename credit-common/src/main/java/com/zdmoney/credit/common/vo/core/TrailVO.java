package com.zdmoney.credit.common.vo.core;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 还款试算接口返回结果类型VO
 * 
 * @author 00235304
 * 
 */
public class TrailVO {

	private Long loanId; 			// 债权编号
	private String name; 			// 姓名
	private String idNum; 			// 身份证号
	private String mphone; 			// 手机号码
	private String loanType; 		// 借款类型
	private String loanState; 		// 还款状态
	private BigDecimal pactMoney=BigDecimal.ZERO; 		// 合同金额
	private BigDecimal overInterest=BigDecimal.ZERO;	// 逾期利息
	private BigDecimal overCorpus=BigDecimal.ZERO; 		// 逾期本金
	private int overTerm=0; 							// 逾期期数
	private BigDecimal fine=BigDecimal.ZERO; 			// 罚息
	private BigDecimal overDueAmount=BigDecimal.ZERO;	// 逾期本息和
	private BigDecimal currInterest=BigDecimal.ZERO; 	// 当期利息
	private BigDecimal currCorpus=BigDecimal.ZERO; 		// 当期本金
	private BigDecimal currAmount=BigDecimal.ZERO; 		// 当期应还
	private BigDecimal accAmount=BigDecimal.ZERO; 		// 挂账
	private BigDecimal penalty=BigDecimal.ZERO; 		// 违约金
	private BigDecimal backAmount=BigDecimal.ZERO; 		// 退费
	private BigDecimal principal=BigDecimal.ZERO; 		// 剩余本金
	private BigDecimal currAMT=BigDecimal.ZERO; 		// 当期应还金额
	private BigDecimal interest=BigDecimal.ZERO; 		// 利息
	private BigDecimal remnant=BigDecimal.ZERO; 		// 剩余本息和
	private BigDecimal oneTimeRepayAmt=BigDecimal.ZERO; // 一次性还款金额
	private BigDecimal repaymentAmount=BigDecimal.ZERO; // 应还金额
	private BigDecimal overDueAllAmt=BigDecimal.ZERO; 	
	private BigDecimal nextTimeRepayment=BigDecimal.ZERO; //下期金额
	private int currTime; 	// 当前期数
	private int time; 	 	// 期数
	private int residualTerm;//剩余期数
	private BigDecimal returneTerm;//每期还款金额
	private Date overdueStartDate;	//逾期起始日 
	private int overDueDays;		//逾期天数
	
	public int getResidualTerm() {
		return residualTerm;
	}
	public void setResidualTerm(int residualTerm) {
		this.residualTerm = residualTerm;
	}
	public Long getLoanId() {
		return loanId;
	}
	public void setLoanId(Long loanId) {
		this.loanId = loanId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdNum() {
		return idNum;
	}
	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}
	public String getMphone() {
		return mphone;
	}
	public void setMphone(String mphone) {
		this.mphone = mphone;
	}
	public String getLoanType() {
		return loanType;
	}
	public void setLoanType(String loanType) {
		this.loanType = loanType;
	}
	public String getLoanState() {
		return loanState;
	}
	public void setLoanState(String loanState) {
		this.loanState = loanState;
	}
	public BigDecimal getPactMoney() {
		return pactMoney;
	}
	public void setPactMoney(BigDecimal pactMoney) {
		this.pactMoney = pactMoney;
	}
	public BigDecimal getOverInterest() {
		return overInterest;
	}
	public void setOverInterest(BigDecimal overInterest) {
		this.overInterest = overInterest;
	}
	public BigDecimal getOverCorpus() {
		return overCorpus;
	}
	public void setOverCorpus(BigDecimal overCorpus) {
		this.overCorpus = overCorpus;
	}
	public BigDecimal getFine() {
		return fine;
	}
	public void setFine(BigDecimal fine) {
		this.fine = fine;
	}
	public BigDecimal getOverDueAmount() {
		return overDueAmount;
	}
	public void setOverDueAmount(BigDecimal overDueAmount) {
		this.overDueAmount = overDueAmount;
	}
	public BigDecimal getCurrInterest() {
		return currInterest;
	}
	public void setCurrInterest(BigDecimal currInterest) {
		this.currInterest = currInterest;
	}
	public BigDecimal getCurrCorpus() {
		return currCorpus;
	}
	public void setCurrCorpus(BigDecimal currCorpus) {
		this.currCorpus = currCorpus;
	}
	public BigDecimal getCurrAmount() {
		return currAmount;
	}
	public void setCurrAmount(BigDecimal currAmount) {
		this.currAmount = currAmount;
	}
	public BigDecimal getAccAmount() {
		return accAmount;
	}
	public void setAccAmount(BigDecimal accAmount) {
		this.accAmount = accAmount;
	}
	public BigDecimal getPenalty() {
		return penalty;
	}
	public void setPenalty(BigDecimal penalty) {
		this.penalty = penalty;
	}
	public BigDecimal getBackAmount() {
		return backAmount;
	}
	public void setBackAmount(BigDecimal backAmount) {
		this.backAmount = backAmount;
	}
	public BigDecimal getPrincipal() {
		return principal;
	}
	public void setPrincipal(BigDecimal principal) {
		this.principal = principal;
	}
	public BigDecimal getCurrAMT() {
		return currAMT;
	}
	public void setCurrAMT(BigDecimal currAMT) {
		this.currAMT = currAMT;
	}
	public BigDecimal getInterest() {
		return interest;
	}
	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}
	public BigDecimal getRemnant() {
		return remnant;
	}
	public void setRemnant(BigDecimal remnant) {
		this.remnant = remnant;
	}
	public BigDecimal getOneTimeRepayAmt() {
		return oneTimeRepayAmt;
	}
	public void setOneTimeRepayAmt(BigDecimal oneTimeRepayAmt) {
		this.oneTimeRepayAmt = oneTimeRepayAmt;
	}
	public BigDecimal getRepaymentAmount() {
		return repaymentAmount;
	}
	public void setRepaymentAmount(BigDecimal repaymentAmount) {
		this.repaymentAmount = repaymentAmount;
	}
	public BigDecimal getOverDueAllAmt() {
		return overDueAllAmt;
	}
	public void setOverDueAllAmt(BigDecimal overDueAllAmt) {
		this.overDueAllAmt = overDueAllAmt;
	}
	public int getCurrTime() {
		return currTime;
	}
	public void setCurrTime(int currTime) {
		this.currTime = currTime;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public BigDecimal getNextTimeRepayment() {
		return nextTimeRepayment;
	}
	public void setNextTimeRepayment(BigDecimal nextTimeRepayment) {
		this.nextTimeRepayment = nextTimeRepayment;
	}
	public Date getOverdueStartDate() {
		return overdueStartDate;
	}
	public void setOverdueStartDate(Date overdueStartDate) {
		this.overdueStartDate = overdueStartDate;
	}
	public int getOverDueDays() {
		return overDueDays;
	}
	public void setOverDueDays(int overDueDays) {
		this.overDueDays = overDueDays;
	}
	public int getOverTerm() {
		return overTerm;
	}
	public void setOverTerm(int overTerm) {
		this.overTerm = overTerm;
	}
	public BigDecimal getReturneTerm() {
		return returneTerm;
	}
	public void setReturneTerm(BigDecimal returneTerm) {
		this.returneTerm = returneTerm;
	}
	
}
