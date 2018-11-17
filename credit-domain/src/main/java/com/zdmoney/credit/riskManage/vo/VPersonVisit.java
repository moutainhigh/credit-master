package com.zdmoney.credit.riskManage.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.zdmoney.credit.riskManage.domain.PersonVisit;

public class VPersonVisit extends PersonVisit{
    
    private static final long serialVersionUID = -6079271218930118292L;

    /**
     * 客户姓名
     */
    private String name;
    
    /**
     * 客户电话
     */
    private String mobile;
    
    /**
     * 客户身份证号
     */
    private String idNum;

    /**
     * 回访日期（开始日期）
     */
    private String startDate;
    
    /**
     * 回访日期（截止日期）
     */
    private String endDate;
    
    /**
     * 借款类型
     */
    private String loanType;
    
    /**
     * 放款营业部名称
     */
    private String signsalesdepName;
    
    /**
     * 放款营业部区域匹配码
     */
    private String signsalesdepCode;
    
    /**
     * 管理营业部名称
     */
    private String salesdepartmentName;
    
    /**
     * 借款人Id
     */
    private Long borrowerId;
    
    /**
     * 借款人姓名
     */
    private String borrowerName;
    
    /**
     * 客户经理姓名
     */
    private String salesmanName;
    
    /**
     * 回访日期
     */
    private String visitDate;
    
    /**
     * 申请金额
     */
    private BigDecimal requestMoney;
    
    /**
     * 申请期限
     */
    private Long requestTime;
    
    /**
     * 申请日期
     */
    private Date requestDate;
    
    /**
     * 签约日期
     */
    private Date signDate;
    
    /**
     * 审批金额
     */
    private BigDecimal money;
    
    /**
     * 借款期限
     */
    private Long time;
    
    /**
     * 放款日期
     */
    private Date grantMoneyDate;
    
    /**
     * 回访客户数量
     */
    private Long visitNum;
    
    /**
     * 借款状态
     */
    private String[] loanStates;
    
    /**
     * 合同编号
     */
    private String  contractNum;
    
    /**
     * 拨号方式（0：手动输入拨号，空白：选择拨号）
     */
    private String dialMode;
    
    /**
     * 手动输入的电话号码
     */
    private String phone;
    
    /**
     * 分案状态（0：可分案、1：不可分案）
     */
    private String assignState;
    
    /**
     * 是否转让
     * @return
     */
    
    private String transferBatch;
    
    public String getTransferBatch() {
		return transferBatch;
	}

	public void setTransferBatch(String transferBatch) {
		this.transferBatch = transferBatch;
	}

	public String getAssignState() {
		return assignState;
	}

	public void setAssignState(String assignState) {
		this.assignState = assignState;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIdNum() {
        return idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getSignsalesdepName() {
        return signsalesdepName;
    }

    public void setSignsalesdepName(String signsalesdepName) {
        this.signsalesdepName = signsalesdepName;
    }

    public String getSignsalesdepCode() {
        return signsalesdepCode;
    }

    public void setSignsalesdepCode(String signsalesdepCode) {
        this.signsalesdepCode = signsalesdepCode;
    }

    public String getSalesdepartmentName() {
        return salesdepartmentName;
    }

    public void setSalesdepartmentName(String salesdepartmentName) {
        this.salesdepartmentName = salesdepartmentName;
    }

    public Long getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(Long borrowerId) {
        this.borrowerId = borrowerId;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }

    public String getSalesmanName() {
        return salesmanName;
    }

    public void setSalesmanName(String salesmanName) {
        this.salesmanName = salesmanName;
    }

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

    public BigDecimal getRequestMoney() {
        return requestMoney;
    }

    public void setRequestMoney(BigDecimal requestMoney) {
        this.requestMoney = requestMoney;
    }

    public Long getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Long requestTime) {
        this.requestTime = requestTime;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public Date getSignDate() {
        return signDate;
    }

    public void setSignDate(Date signDate) {
        this.signDate = signDate;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Date getGrantMoneyDate() {
        return grantMoneyDate;
    }

    public void setGrantMoneyDate(Date grantMoneyDate) {
        this.grantMoneyDate = grantMoneyDate;
    }

    public Long getVisitNum() {
        return visitNum;
    }

    public void setVisitNum(Long visitNum) {
        this.visitNum = visitNum;
    }

    public String[] getLoanStates() {
        return loanStates;
    }

    public void setLoanStates(String[] loanStates) {
        this.loanStates = loanStates;
    }

    public String getContractNum() {
        return contractNum;
    }

    public void setContractNum(String contractNum) {
        this.contractNum = contractNum;
    }

    public String getDialMode() {
        return dialMode;
    }

    public void setDialMode(String dialMode) {
        this.dialMode = dialMode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}