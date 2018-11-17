package com.zdmoney.credit.loan.vo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 债权导出
 * @author Anfq
 *
 * 2015年9月12日
 */
public class VloanPersonInfo {
	private Long id;

    private String contractNum;

    private String loanState; //状态

    private String loanFlowState;

    private Long giveBackBankId;

    private Long grantBankId;

    private Long borrowerId; //

    private Long crmId; //客服ID employee

    private Long salesDepartmentId;//所属网点(管理营业部)

    private Long salesmanId;//销售员

    private Long salesTeamId;//销售团队

    private Long signSalesDepId; //放款营业部 com_or

    private Long applySalesDepId;//申请营业部

    private String endOfMonthOpened;

    private Date createTime;

    private Date updateTime;

    private String loanNum;

    private String fundsSources;
    
    private String trustGrantState;
    
    private String batchNum;

    private Long assessorId;

    private String loanType;

    private BigDecimal money;

    private String purpose;

    private Date requestDate;

    private BigDecimal restoreem;

    private BigDecimal auditingMoney;

    private BigDecimal requestMoney;

    private Date grantMoneyDate;

    private Long grantMoneyOperatorId;

    private Date signDate;

    private String requestLoanType;

    private Long requestTime;

    private Date auditDate;

    private BigDecimal approveMoney;

    private Long approveTime;

    private Long lastAssessorId;

    private Date startrdate;

    private Date endrdate;

    private BigDecimal evalRate;

    private BigDecimal manageRate;

    private BigDecimal pactMoney;

    private Long promiseReturnDate;

    private BigDecimal rateem;

    private BigDecimal rateey;

    private BigDecimal rateSum;

    private BigDecimal referRate;

    private Long time;

    private BigDecimal grantMoney;

    private BigDecimal margin;

    private BigDecimal risk;

    private BigDecimal penaltyRate;
    
    private String isOffer;
    
    private int currentTerm;
    
    
    private String appNo;
    
    private String name;
    private String idnum;
    private String orgCode;
    private String changeFlag;
    
	public String getChangeFlag() {
		return changeFlag;
	}
	public void setChangeFlag(String changeFlag) {
		this.changeFlag = changeFlag;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getContractNum() {
		return contractNum;
	}
	public void setContractNum(String contractNum) {
		this.contractNum = contractNum;
	}
	public String getLoanState() {
		return loanState;
	}
	public void setLoanState(String loanState) {
		this.loanState = loanState;
	}
	public String getLoanFlowState() {
		return loanFlowState;
	}
	public void setLoanFlowState(String loanFlowState) {
		this.loanFlowState = loanFlowState;
	}
	public Long getGiveBackBankId() {
		return giveBackBankId;
	}
	public void setGiveBackBankId(Long giveBackBankId) {
		this.giveBackBankId = giveBackBankId;
	}
	public Long getGrantBankId() {
		return grantBankId;
	}
	public void setGrantBankId(Long grantBankId) {
		this.grantBankId = grantBankId;
	}
	public Long getBorrowerId() {
		return borrowerId;
	}
	public void setBorrowerId(Long borrowerId) {
		this.borrowerId = borrowerId;
	}
	public Long getCrmId() {
		return crmId;
	}
	public void setCrmId(Long crmId) {
		this.crmId = crmId;
	}
	public Long getSalesDepartmentId() {
		return salesDepartmentId;
	}
	public void setSalesDepartmentId(Long salesDepartmentId) {
		this.salesDepartmentId = salesDepartmentId;
	}
	public Long getSalesmanId() {
		return salesmanId;
	}
	public void setSalesmanId(Long salesmanId) {
		this.salesmanId = salesmanId;
	}
	public Long getSalesTeamId() {
		return salesTeamId;
	}
	public void setSalesTeamId(Long salesTeamId) {
		this.salesTeamId = salesTeamId;
	}
	public Long getSignSalesDepId() {
		return signSalesDepId;
	}
	public void setSignSalesDepId(Long signSalesDepId) {
		this.signSalesDepId = signSalesDepId;
	}
	public Long getApplySalesDepId() {
		return applySalesDepId;
	}
	public void setApplySalesDepId(Long applySalesDepId) {
		this.applySalesDepId = applySalesDepId;
	}
	public String getEndOfMonthOpened() {
		return endOfMonthOpened;
	}
	public void setEndOfMonthOpened(String endOfMonthOpened) {
		this.endOfMonthOpened = endOfMonthOpened;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getLoanNum() {
		return loanNum;
	}
	public void setLoanNum(String loanNum) {
		this.loanNum = loanNum;
	}
	public String getFundsSources() {
		return fundsSources;
	}
	public void setFundsSources(String fundsSources) {
		this.fundsSources = fundsSources;
	}
	public String getTrustGrantState() {
		return trustGrantState;
	}
	public void setTrustGrantState(String trustGrantState) {
		this.trustGrantState = trustGrantState;
	}
	public String getBatchNum() {
		return batchNum;
	}
	public void setBatchNum(String batchNum) {
		this.batchNum = batchNum;
	}
	public Long getAssessorId() {
		return assessorId;
	}
	public void setAssessorId(Long assessorId) {
		this.assessorId = assessorId;
	}
	public String getLoanType() {
		return loanType;
	}
	public void setLoanType(String loanType) {
		this.loanType = loanType;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public Date getRequestDate() {
		return requestDate;
	}
	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}
	public BigDecimal getRestoreem() {
		return restoreem;
	}
	public void setRestoreem(BigDecimal restoreem) {
		this.restoreem = restoreem;
	}
	public BigDecimal getAuditingMoney() {
		return auditingMoney;
	}
	public void setAuditingMoney(BigDecimal auditingMoney) {
		this.auditingMoney = auditingMoney;
	}
	public BigDecimal getRequestMoney() {
		return requestMoney;
	}
	public void setRequestMoney(BigDecimal requestMoney) {
		this.requestMoney = requestMoney;
	}
	public Date getGrantMoneyDate() {
		return grantMoneyDate;
	}
	public void setGrantMoneyDate(Date grantMoneyDate) {
		this.grantMoneyDate = grantMoneyDate;
	}
	public Long getGrantMoneyOperatorId() {
		return grantMoneyOperatorId;
	}
	public void setGrantMoneyOperatorId(Long grantMoneyOperatorId) {
		this.grantMoneyOperatorId = grantMoneyOperatorId;
	}
	public Date getSignDate() {
		return signDate;
	}
	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}
	public String getRequestLoanType() {
		return requestLoanType;
	}
	public void setRequestLoanType(String requestLoanType) {
		this.requestLoanType = requestLoanType;
	}
	public Long getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(Long requestTime) {
		this.requestTime = requestTime;
	}
	public Date getAuditDate() {
		return auditDate;
	}
	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}
	public BigDecimal getApproveMoney() {
		return approveMoney;
	}
	public void setApproveMoney(BigDecimal approveMoney) {
		this.approveMoney = approveMoney;
	}
	public Long getApproveTime() {
		return approveTime;
	}
	public void setApproveTime(Long approveTime) {
		this.approveTime = approveTime;
	}
	public Long getLastAssessorId() {
		return lastAssessorId;
	}
	public void setLastAssessorId(Long lastAssessorId) {
		this.lastAssessorId = lastAssessorId;
	}
	public Date getStartrdate() {
		return startrdate;
	}
	public void setStartrdate(Date startrdate) {
		this.startrdate = startrdate;
	}
	public Date getEndrdate() {
		return endrdate;
	}
	public void setEndrdate(Date endrdate) {
		this.endrdate = endrdate;
	}
	public BigDecimal getEvalRate() {
		return evalRate;
	}
	public void setEvalRate(BigDecimal evalRate) {
		this.evalRate = evalRate;
	}
	public BigDecimal getManageRate() {
		return manageRate;
	}
	public void setManageRate(BigDecimal manageRate) {
		this.manageRate = manageRate;
	}
	public BigDecimal getPactMoney() {
		return pactMoney;
	}
	public void setPactMoney(BigDecimal pactMoney) {
		this.pactMoney = pactMoney;
	}
	public Long getPromiseReturnDate() {
		return promiseReturnDate;
	}
	public void setPromiseReturnDate(Long promiseReturnDate) {
		this.promiseReturnDate = promiseReturnDate;
	}
	public BigDecimal getRateem() {
		return rateem;
	}
	public void setRateem(BigDecimal rateem) {
		this.rateem = rateem;
	}
	public BigDecimal getRateey() {
		return rateey;
	}
	public void setRateey(BigDecimal rateey) {
		this.rateey = rateey;
	}
	public BigDecimal getRateSum() {
		return rateSum;
	}
	public void setRateSum(BigDecimal rateSum) {
		this.rateSum = rateSum;
	}
	public BigDecimal getReferRate() {
		return referRate;
	}
	public void setReferRate(BigDecimal referRate) {
		this.referRate = referRate;
	}
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	public BigDecimal getGrantMoney() {
		return grantMoney;
	}
	public void setGrantMoney(BigDecimal grantMoney) {
		this.grantMoney = grantMoney;
	}
	public BigDecimal getMargin() {
		return margin;
	}
	public void setMargin(BigDecimal margin) {
		this.margin = margin;
	}
	public BigDecimal getRisk() {
		return risk;
	}
	public void setRisk(BigDecimal risk) {
		this.risk = risk;
	}
	public BigDecimal getPenaltyRate() {
		return penaltyRate;
	}
	public void setPenaltyRate(BigDecimal penaltyRate) {
		this.penaltyRate = penaltyRate;
	}
	public String getIsOffer() {
		return isOffer;
	}
	public void setIsOffer(String isOffer) {
		this.isOffer = isOffer;
	}
	public int getCurrentTerm() {
		return currentTerm;
	}
	public void setCurrentTerm(int currentTerm) {
		this.currentTerm = currentTerm;
	}
	public String getAppNo() {
		return appNo;
	}
	public void setAppNo(String appNo) {
		this.appNo = appNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdnum() {
		return idnum;
	}
	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
    
    
}
