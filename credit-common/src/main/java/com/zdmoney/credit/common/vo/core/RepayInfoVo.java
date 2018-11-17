package com.zdmoney.credit.common.vo.core;

import java.math.BigDecimal;

public class RepayInfoVo {
	String ID = "";//债权ID
	String name = "";//姓名
    String idType = "";//证件类型
    String idNum = "";//身份证号
    String mphone = "";//手机
    String overDueDate = "";      //逾期日期
    int overDueTerm = 0;          //逾期期数
    BigDecimal overInterest=new BigDecimal(0.00);    //逾期利息
    BigDecimal overCorpus=new BigDecimal(0.00);      //逾期本金
    String fineDate = "";   //罚息日期
    int fineDay = 0;        //罚息天数
    BigDecimal Remnant = new BigDecimal(0.00);//剩余本息和
    BigDecimal fine = new BigDecimal(0.00);          //罚息
    String currDate = "";//当期还款日
    Long currTerm = 0L;//当前期数
    BigDecimal currInterest = new BigDecimal(0.00);  //当期利息
    BigDecimal currCorpus = new BigDecimal(0.00);    //当期本金
    BigDecimal accAmount = new BigDecimal(0.00);     //挂账
    BigDecimal OverdueAmount = new BigDecimal(0.00); //应还总额（不含当期）
    BigDecimal currAmount = new BigDecimal(0.00);    //当期应还
    BigDecimal oneTimeRepayment = new BigDecimal(0.00);  //一次性还款金额合计
    String tradeDate = null;
	
    /**一下字段暂时未用到，先隐去（对应老系统的RepayInfoTemp类）**/
	/*
    String area = "";
    String loanType = "";
    BigDecimal pactMoney = new BigDecimal(0.00);
    int time = 0;
    String loanState = "";
    int overDueDay = 0;           //逾期天数
    BigDecimal penalty = new BigDecimal(0.00);       //违约金
    BigDecimal giveBackRate = new BigDecimal(0.00);  //退费
    BigDecimal residualPactMoney = new BigDecimal(0.00); //剩余本金
    String requestState = "";  //申请状态  包含：申请和取消申请
    String specialRepaymentRequestId = ""; //特殊申请ID
*/	
    
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
	public String getOverDueDate() {
		return overDueDate;
	}
	public void setOverDueDate(String overDueDate) {
		this.overDueDate = overDueDate;
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
	public String getFineDate() {
		return fineDate;
	}
	public void setFineDate(String fineDate) {
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
	public String getCurrDate() {
		return currDate;
	}
	public void setCurrDate(String currDate) {
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
	public BigDecimal getAccAmount() {
		return accAmount;
	}
	public void setAccAmount(BigDecimal accAmount) {
		this.accAmount = accAmount;
	}
	public BigDecimal getOverdueAmount() {
		return OverdueAmount;
	}
	public void setOverdueAmount(BigDecimal overdueAmount) {
		OverdueAmount = overdueAmount;
	}
	public BigDecimal getCurrAmount() {
		return currAmount;
	}
	public void setCurrAmount(BigDecimal currAmount) {
		this.currAmount = currAmount;
	}
	public BigDecimal getOneTimeRepayment() {
		return oneTimeRepayment;
	}
	public void setOneTimeRepayment(BigDecimal oneTimeRepayment) {
		this.oneTimeRepayment = oneTimeRepayment;
	}
	public String getTradeDate() {
		return tradeDate;
	}
	public void setTradeDate(String tradeDate) {
		this.tradeDate = tradeDate;
	}
}
