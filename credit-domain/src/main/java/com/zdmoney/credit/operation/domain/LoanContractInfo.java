package com.zdmoney.credit.operation.domain;

import java.math.BigDecimal;
import java.util.List;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class LoanContractInfo extends BaseDomain {

    private static final long serialVersionUID = 4019705873556833829L;
    
    /**
     * 债权Id
     */
    private Long loanId;
    /**
     * 借款人姓名
     */
    private String name;
    
    /**
     * 身份证号码
     */
    private String idNum;
    
    /**
     * 手机号码
     */
    private String mobile;
    
    /**
     * 借款类型
     */
    private String loanType;
    
    /**
     * 申请金额
     */
    private BigDecimal requestMoney;
    
    /**
     * 审批金额
     */
    private BigDecimal money;
    
    /**
     * 合同金额
     */
    private BigDecimal pactMoney;
    
    /**
     * 合同金额
     */
    private BigDecimal grantMoney;
    
    /**
     * 借款期限（单位：月）
     */
    private Long time;
    
    /**
     * 申请日期
     */
    private String requestDate;
    
    /**
     * 借款状态
     */
    private String loanState;
    
    /**
     * 当前客服Id
     */
    private Long crmId;
    
    /**
     * 当前客服姓名
     */
    private String crmName;
    
    /**
     * 是否在职（是：在职，否：不在职）
     */
    private String inActive;
    
    /**
     * 借款状态
     */
    private List<String> loanStates;
    
    /**
     * 
     */
    private String empNum;
    
    /**
     * Excel导出最大条数
     */
    private Long max;
    
    /**
     * 
     * 合同编号
     */
    private String contractNum;

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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public BigDecimal getRequestMoney() {
        return requestMoney;
    }

    public void setRequestMoney(BigDecimal requestMoney) {
        this.requestMoney = requestMoney;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public BigDecimal getPactMoney() {
        return pactMoney;
    }

    public void setPactMoney(BigDecimal pactMoney) {
        this.pactMoney = pactMoney;
    }

    public BigDecimal getGrantMoney() {
        return grantMoney;
    }

    public void setGrantMoney(BigDecimal grantMoney) {
        this.grantMoney = grantMoney;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public String getLoanState() {
        return loanState;
    }

    public void setLoanState(String loanState) {
        this.loanState = loanState;
    }

    public Long getCrmId() {
        return crmId;
    }

    public void setCrmId(Long crmId) {
        this.crmId = crmId;
    }

    public String getCrmName() {
        return crmName;
    }

    public void setCrmName(String crmName) {
        this.crmName = crmName;
    }

    public String getInActive() {
        return inActive;
    }

    public void setInActive(String inActive) {
        this.inActive = inActive;
    }

    public List<String> getLoanStates() {
        return loanStates;
    }

    public void setLoanStates(List<String> loanStates) {
        this.loanStates = loanStates;
    }

    public String getEmpNum() {
        return empNum;
    }

    public void setEmpNum(String empNum) {
        this.empNum = empNum;
    }

    public Long getMax() {
        return max;
    }

    public void setMax(Long max) {
        this.max = max;
    }

	public String getContractNum() {
		return contractNum;
	}

	public void setContractNum(String contractNum) {
		this.contractNum = contractNum;
	}
    
}
