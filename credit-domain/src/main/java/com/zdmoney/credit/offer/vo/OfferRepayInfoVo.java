package com.zdmoney.credit.offer.vo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 还款信息Vo
 * @author Ivan
 *
 */
public class OfferRepayInfoVo {
	/** 借款ID **/
	private Long id;
	/** 借款人 **/
    private String name;
    /** 借款人证件类型 **/
    private String idType;
    /** 借款人证件号码 **/
    private String idNum;
    /** 借款人手机号 **/
    private String mphone;
    /** 借款人营业网点 **/
    private String area;
    /** 借款类型 **/
    private String loanType;
    /** 借款合同金额 **/
    private BigDecimal pactMoney = new BigDecimal(0);
    /** 借款合同期限 **/
    private int time = 0;
    /** 借款合同状态 **/
    private String loanState;
    /** 逾期日期 **/
    private Date overDueDate;
    /** 逾期天数 **/
    private int overDueDay = 0;
    /** 逾期期数 **/
    private int overDueTerm = 0;
    /** 逾期利息 **/
    private BigDecimal overInterest = new BigDecimal(0);
    /** 逾期本金 **/
    private BigDecimal overCorpus = new BigDecimal(0);
    /** 逾期本息和 = 逾期本金 + 逾期利息 **/
    private BigDecimal overAmount = new BigDecimal(0);
    /** 罚息日期 **/
    private Date fineDate;
    /** 罚息天数 **/
    private int fineDay = 0;
    private BigDecimal remnant = new BigDecimal(0);
    /** 罚息 **/
    private BigDecimal fine = new BigDecimal(0);
    /** 逾期应还 **/
    private BigDecimal OverdueAmount = new BigDecimal(0);
    /** 当期还款日 **/
    private Date currDate;
    /** 当前期数 **/
    private Long currTerm = 0L;
    /** 当期利息 **/
    private BigDecimal currInterest = new BigDecimal(0);
    /** 当期本金 **/
    private BigDecimal currCorpus = new BigDecimal(0);
    /** 当期应还 **/
    private BigDecimal currAmount = new BigDecimal(0);
    /** 应还总额（含当期） **/
    private BigDecimal currAllAmount = new BigDecimal(0);
    /** 挂账 **/
    private BigDecimal accAmount = new BigDecimal(0);
    /** 违约金 **/
    private BigDecimal penalty = new BigDecimal(0);
    /** 退费 **/
    private BigDecimal giveBackRate = new BigDecimal(0);
    /**  **/
    private Date tradeDate;
    /** 一次性还款金额合计 **/
    private BigDecimal oneTimeRepayment = new BigDecimal(0);
    /** 剩余本金 **/
    private BigDecimal residualPactMoney = new BigDecimal(0);
    /** 申请状态  包含：申请和取消申请 **/
    private String requestState;
    /** 特殊申请ID **/
    private String specialRepaymentRequestId;
    /** 一次性还清总金额 **/
    private BigDecimal allAmount = new BigDecimal(0);
    /** 罚息减免金额 **/
    private BigDecimal relief = new BigDecimal(0);
    /**合同编号**/
    private String contractNum;
    /**还款等级**/
    private String repaymentLevel;
    
	public String getRepaymentLevel() {
		return repaymentLevel;
	}
	public void setRepaymentLevel(String repaymentLevel) {
		this.repaymentLevel = repaymentLevel;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdType() {
		return idType;
	}
	public void setIdType(String idType) {
		this.idType = idType;
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
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getLoanType() {
		return loanType;
	}
	public void setLoanType(String loanType) {
		this.loanType = loanType;
	}
	public BigDecimal getPactMoney() {
		return pactMoney;
	}
	public void setPactMoney(BigDecimal pactMoney) {
		this.pactMoney = pactMoney;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public String getLoanState() {
		return loanState;
	}
	public void setLoanState(String loanState) {
		this.loanState = loanState;
	}
	public Date getOverDueDate() {
		return overDueDate;
	}
	public void setOverDueDate(Date overDueDate) {
		this.overDueDate = overDueDate;
	}
	public int getOverDueDay() {
		return overDueDay;
	}
	public void setOverDueDay(int overDueDay) {
		this.overDueDay = overDueDay;
	}
	public int getOverDueTerm() {
		return overDueTerm;
	}
	public void setOverDueTerm(int overDueTerm) {
		this.overDueTerm = overDueTerm;
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
	public Date getFineDate() {
		return fineDate;
	}
	public void setFineDate(Date fineDate) {
		this.fineDate = fineDate;
	}
	public int getFineDay() {
		return fineDay;
	}
	public void setFineDay(int fineDay) {
		this.fineDay = fineDay;
	}
	public BigDecimal getRemnant() {
		return remnant;
	}
	public void setRemnant(BigDecimal remnant) {
		this.remnant = remnant;
	}
	public BigDecimal getFine() {
		return fine;
	}
	public void setFine(BigDecimal fine) {
		this.fine = fine;
	}
	public BigDecimal getOverdueAmount() {
		return OverdueAmount;
	}
	public void setOverdueAmount(BigDecimal overdueAmount) {
		OverdueAmount = overdueAmount;
	}
	public Date getCurrDate() {
		return currDate;
	}
	public void setCurrDate(Date currDate) {
		this.currDate = currDate;
	}
	public Long getCurrTerm() {
		return currTerm;
	}
	public void setCurrTerm(Long currTerm) {
		this.currTerm = currTerm;
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
	public BigDecimal getGiveBackRate() {
		return giveBackRate;
	}
	public void setGiveBackRate(BigDecimal giveBackRate) {
		this.giveBackRate = giveBackRate;
	}
	public Date getTradeDate() {
		return tradeDate;
	}
	public void setTradeDate(Date tradeDate) {
		this.tradeDate = tradeDate;
	}
	public BigDecimal getOneTimeRepayment() {
		return oneTimeRepayment;
	}
	public void setOneTimeRepayment(BigDecimal oneTimeRepayment) {
		this.oneTimeRepayment = oneTimeRepayment;
	}
	public BigDecimal getResidualPactMoney() {
		return residualPactMoney;
	}
	public void setResidualPactMoney(BigDecimal residualPactMoney) {
		this.residualPactMoney = residualPactMoney;
	}
	public String getRequestState() {
		return requestState;
	}
	public void setRequestState(String requestState) {
		this.requestState = requestState;
	}
	public String getSpecialRepaymentRequestId() {
		return specialRepaymentRequestId;
	}
	public void setSpecialRepaymentRequestId(String specialRepaymentRequestId) {
		this.specialRepaymentRequestId = specialRepaymentRequestId;
	}
	public BigDecimal getAllAmount() {
		return allAmount;
	}
	public void setAllAmount(BigDecimal allAmount) {
		this.allAmount = allAmount;
	}
	public BigDecimal getOverAmount() {
		return overAmount;
	}
	public void setOverAmount(BigDecimal overAmount) {
		this.overAmount = overAmount;
	}
	public BigDecimal getCurrAllAmount() {
		return currAllAmount;
	}
	public void setCurrAllAmount(BigDecimal currAllAmount) {
		this.currAllAmount = currAllAmount;
	}
	public BigDecimal getRelief() {
		return relief;
	}
	public void setRelief(BigDecimal relief) {
		this.relief = relief;
	}
	public String getContractNum() {
		return contractNum;
	}
	public void setContractNum(String contractNum) {
		this.contractNum = contractNum;
	}
    
    
    
}
