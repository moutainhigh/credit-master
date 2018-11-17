package com.zdmoney.credit.loan.domain;


import java.math.BigDecimal;
import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;
import com.zdmoney.credit.system.domain.PersonInfo;

public class LoanBase extends BaseDomain{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -203649233994769919L;

	private Long id;

    private String contractNum;

    private String loanState;

    private String loanFlowState;

    private Long giveBackBankId;

    private Long grantBankId;

    private Long borrowerId;

    private Long crmId;

    private Long salesDepartmentId;

    private Long salesmanId;

    private Long salesTeamId;

    private Long signSalesDepId;

    private Long applySalesDepId;

    private String endOfMonthOpened;

    private Date createTime;

    private Date updateTime;

    private String loanNum;

    private String fundsSources;

    private String isOffer;
    
    private String trustGrantState;
    
    private String batchNum;
    
    private Long planId;
    
    //债权回接时债权归属的合同来源
    private String loanBelong;
    
    private String applyType;
    private String applyInputFlag;
    
    private String appInputFlag;//进件平台（空代表否   app_input代表是）
    
    //张倩倩
    private LoanContract loanContract;
    private LoanInitialInfo loanInitialInfo;
    private LoanProduct loanProduct;
    private LoanLedger loanLedger;
    private LoanSpecialRepayment loanSpecialRepayment;
    private PersonInfo personInfo;
    //剩余利息
    private BigDecimal remainingInterest;
    //剩余本息和
    private BigDecimal  bxcount;
    //违约金
    private  BigDecimal penalty;
    //当前期数
    private Long currentTerm;
    //是否申请
    private String isApply;
   //挂账总额
    private BigDecimal accAmount;
    
	public BigDecimal getAccAmount() {
		return accAmount;
	}

	public void setAccAmount(BigDecimal accAmount) {
		this.accAmount = accAmount;
	}

	public String getIsApply() {
		return isApply;
	}

	public void setIsApply(String isApply) {
		this.isApply = isApply;
	}

	public Long getCurrentTerm() {
		return currentTerm;
	}

	public void setCurrentTerm(Long currentTerm) {
		this.currentTerm = currentTerm;
	}

	public BigDecimal getBxcount() {
		return bxcount;
	}

	public void setBxcount(BigDecimal bxcount) {
		this.bxcount = bxcount;
	}

	public BigDecimal getPenalty() {
		return penalty;
	}

	public void setPenalty(BigDecimal penalty) {
		this.penalty = penalty;
	}

	public BigDecimal getRemainingInterest() {
		return remainingInterest;
	}

	public void setRemainingInterest(BigDecimal remainingInterest) {
		this.remainingInterest = remainingInterest;
	}

     
	public PersonInfo getPersonInfo() {
		return personInfo;
	}

	public void setPersonInfo(PersonInfo personInfo) {
		this.personInfo = personInfo;
	}

	public LoanSpecialRepayment getLoanSpecialRepayment() {
		return loanSpecialRepayment;
	}

	public void setLoanSpecialRepayment(LoanSpecialRepayment loanSpecialRepayment) {
		this.loanSpecialRepayment = loanSpecialRepayment;
	}

	public LoanLedger getLoanLedger() {
		return loanLedger;
	}

	public void setLoanLedger(LoanLedger loanLedger) {
		this.loanLedger = loanLedger;
	}

	public LoanContract getLoanContract() {
		return loanContract;
	}

	public void setLoanContract(LoanContract loanContract) {
		this.loanContract = loanContract;
	}

	public LoanInitialInfo getLoanInitialInfo() {
		return loanInitialInfo;
	}

	public void setLoanInitialInfo(LoanInitialInfo loanInitialInfo) {
		this.loanInitialInfo = loanInitialInfo;
	}

	public LoanProduct getLoanProduct() {
		return loanProduct;
	}

	public void setLoanProduct(LoanProduct loanProduct) {
		this.loanProduct = loanProduct;
	}

	

    private String isSend;
    
    private Long signCrmId;
    
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

	public String getIsOffer() {
		return isOffer;
	}

	public void setIsOffer(String isOffer) {
		this.isOffer = isOffer;
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

	public String getIsSend() {
		return isSend;
	}

	public void setIsSend(String isSend) {
		this.isSend = isSend;
	}

	public Long getSignCrmId() {
		return signCrmId;
	}

	public void setSignCrmId(Long signCrmId) {
		this.signCrmId = signCrmId;
	}

	public Long getPlanId() {
		return planId;
	}

	public void setPlanId(Long planId) {
		this.planId = planId;
	}
	
	public String getLoanBelong() {
		return loanBelong;
	}

	public void setLoanBelong(String loanBelong) {
		this.loanBelong = loanBelong;
	}
	
	public String getApplyType() {
        return applyType;
    }

    public void setApplyType(String applyType) {
        this.applyType = applyType;
    }

	public String getApplyInputFlag() {
		return applyInputFlag;
	}

	public void setApplyInputFlag(String applyInputFlag) {
		this.applyInputFlag = applyInputFlag;
	}

	public String getAppInputFlag() {
		return appInputFlag;
	}

	public void setAppInputFlag(String appInputFlag) {
		this.appInputFlag = appInputFlag;
	}
	
}