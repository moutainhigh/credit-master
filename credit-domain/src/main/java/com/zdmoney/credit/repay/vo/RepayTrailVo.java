package com.zdmoney.credit.repay.vo;

import java.math.BigDecimal;
import java.util.Date;

public class RepayTrailVo {
	  /**
	 * 
	 */
	String ID;
      String name="";
      String idType="";
      String idNum="";
      String mphone="";
      String area="";
      String loanType="";
      BigDecimal pactMoney;
      int time=0;
      String loanState="";
      Date overDueDate  ;          //逾期日期
      int overDueDay=0 ;          //逾期天数
      int overDueTerm=0   ;       //逾期期数
      BigDecimal overInterest=new BigDecimal(0.0) ;   //逾期利息
      BigDecimal overCorpus=new BigDecimal(0.0);     //逾期本金
      Date fineDate   ; //罚息日期
      int fineDay=0  ; //罚息天数
      BigDecimal Remnant=new BigDecimal(0.0);
      BigDecimal fine  =new BigDecimal(0.0)  ;   //罚息
      BigDecimal OverdueAmount=new BigDecimal(0.0)  ; //逾期应还
      Date currDate;
      int currTerm=0;
      BigDecimal currInterest =new BigDecimal(0.0);    //当期利息
      BigDecimal currCorpus =new BigDecimal(0.0) ;    //当期本金
      BigDecimal currAmount=new BigDecimal(0.0);     //当期应还
      BigDecimal accAmount =new BigDecimal(0.0) ;     //挂账
      BigDecimal penalty=new BigDecimal(0.0); //违约金
      BigDecimal giveBackRate=new BigDecimal(0.0); //退费
      Date tradeDate;
      BigDecimal oneTimeRepayment =new BigDecimal(0.0) ;//一次性还款金额合计
      BigDecimal residualPactMoney=new BigDecimal(0.0) ;//剩余本金
      String requestState ; //申请状态  包含：申请和取消申请
      String specialRepaymentRequestId ; //特殊申请ID
      String contractNum;//合同编号
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
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
		return Remnant;
	}
	public void setRemnant(BigDecimal remnant) {
		Remnant = remnant;
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
	public int getCurrTerm() {
		return currTerm;
	}
	public void setCurrTerm(int currTerm) {
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
	public String getContractNum() {
		return contractNum;
	}
	public void setContractNum(String contractNum) {
		this.contractNum = contractNum;
	}
    
      
      
}
