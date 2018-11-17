package com.zdmoney.credit.operation.domain;

import java.math.BigDecimal;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class PerformanceBelongInfo extends BaseDomain {

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
     * 合同金额
     */
    private BigDecimal pactMoney;
    
    /**
     * 借款期限（单位：月）
     */
    private Long time;
    
    /**
     * 当前客户经理Id
     */
    private Long salesManId;
    
    /**
     * 当前客户经理姓名
     */
    private String salesManName;
    
    /**
     * 当前所属团队Id
     */
    private Long salesTeamId;
    
    /**
     * 当前所属团队名称
     */
    private String salesTeamName;
    
    /**
     * 当前所属营业部Id
     */
    private Long salesDeptId;
    
    /**
     * 当前所属营业部名称
     */
    private String salesDeptName;
    
    /**
     * 借款状态
     */
    private String[] loanStates;
    
    /**
     * 
     */
    private String empNum;
    
    /**
     * 
     * 合同编号
     */
    private String contractNum;
    
    
    /**
     * 申请件渠道标识
     * */
    private String  applyInputFlag;
    
  
	public String getApplyInputFlag() {
		return applyInputFlag;
	}

	public void setApplyInputFlag(String applyInputFlag) {
		this.applyInputFlag = applyInputFlag;
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

    public BigDecimal getPactMoney() {
        return pactMoney;
    }

    public void setPactMoney(BigDecimal pactMoney) {
        this.pactMoney = pactMoney;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Long getSalesManId() {
        return salesManId;
    }

    public void setSalesManId(Long salesManId) {
        this.salesManId = salesManId;
    }

    public String getSalesManName() {
        return salesManName;
    }

    public void setSalesManName(String salesManName) {
        this.salesManName = salesManName;
    }

    public Long getSalesTeamId() {
        return salesTeamId;
    }

    public void setSalesTeamId(Long salesTeamId) {
        this.salesTeamId = salesTeamId;
    }

    public String getSalesTeamName() {
        return salesTeamName;
    }

    public void setSalesTeamName(String salesTeamName) {
        this.salesTeamName = salesTeamName;
    }

    public Long getSalesDeptId() {
        return salesDeptId;
    }

    public void setSalesDeptId(Long salesDeptId) {
        this.salesDeptId = salesDeptId;
    }

    public String getSalesDeptName() {
        return salesDeptName;
    }

    public void setSalesDeptName(String salesDeptName) {
        this.salesDeptName = salesDeptName;
    }

    public String[] getLoanStates() {
        return loanStates;
    }

    public void setLoanStates(String[] loanStates) {
        this.loanStates = loanStates;
    }

    public String getEmpNum() {
        return empNum;
    }

    public void setEmpNum(String empNum) {
        this.empNum = empNum;
    }

	public String getContractNum() {
		return contractNum;
	}

	public void setContractNum(String contractNum) {
		this.contractNum = contractNum;
	}
    
}
