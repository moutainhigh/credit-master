package com.zdmoney.credit.loan.vo;


import java.math.BigDecimal;
import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;
import com.zdmoney.credit.loan.domain.LoanContract;
import com.zdmoney.credit.loan.domain.LoanInitialInfo;
import com.zdmoney.credit.loan.domain.LoanLedger;
import com.zdmoney.credit.loan.domain.LoanProduct;
import com.zdmoney.credit.loan.domain.LoanSpecialRepayment;
import com.zdmoney.credit.system.domain.PersonInfo;

public class LoanBaseVo {
	
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
    
    //张倩倩
    private LoanContract loanContract;
    private LoanInitialInfo loanInitialInfo;
    private LoanProduct loanProduct;
    private LoanLedger loanLedger;
    private LoanSpecialRepayment loanSpecialRepayment;
    private PersonInfo personInfo;
    
 
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

   
}