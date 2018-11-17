package com.zdmoney.credit.repay.domain;

import java.util.List;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class SalesDeptRepayInfo extends BaseDomain {
    
    private static final long serialVersionUID = -5888497382483918113L;

    /**
     * 借款人姓名
     */
    private String name;
    
    /**
     * 借款人身份证号
     */
    private String idNum;
    
    /**
     * 借款类型
     */
    private String loanType;
    
    /**
     * 签约日期
     */
    private String signDate;
    
    /**
     * 借款状态
     */
    private String loanStatus;
    
    /**
     * 放款营业部编号
     */
    private String signSalesDeptId;
    
    /**
     * 放款营业部
     */
    private String signSalesDeptName;
    
    /**
     * 管理营业部编号
     */
    private String salesDepartmentId;
    
    /**
     * 管理营业部
     */
    private String salesDepartmentName;
    
    /**
     * 用来记录数据权限字段。对于业务团队长和客服，
     * 值为所在小区域或者团队的num值，对于业务员，为自己的num值
     */
    private String empNum;
    
    /**
     * 借款状态
     */
    private List<String> loanStates;
    
    /**
     * 合同编号
     */
    private String contractNum;

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

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getSignDate() {
        return signDate;
    }

    public void setSignDate(String signDate) {
        this.signDate = signDate;
    }

    public String getLoanStatus() {
        return loanStatus;
    }

    public void setLoanStatus(String loanStatus) {
        this.loanStatus = loanStatus;
    }

    public String getSignSalesDeptId() {
        return signSalesDeptId;
    }

    public void setSignSalesDeptId(String signSalesDeptId) {
        this.signSalesDeptId = signSalesDeptId;
    }

    public String getSignSalesDeptName() {
        return signSalesDeptName;
    }

    public void setSignSalesDeptName(String signSalesDeptName) {
        this.signSalesDeptName = signSalesDeptName;
    }

    public String getSalesDepartmentId() {
        return salesDepartmentId;
    }

    public void setSalesDepartmentId(String salesDepartmentId) {
        this.salesDepartmentId = salesDepartmentId;
    }

    public String getSalesDepartmentName() {
        return salesDepartmentName;
    }

    public void setSalesDepartmentName(String salesDepartmentName) {
        this.salesDepartmentName = salesDepartmentName;
    }

    public String getEmpNum() {
        return empNum;
    }

    public void setEmpNum(String empNum) {
        this.empNum = empNum;
    }

    public List<String> getLoanStates() {
        return loanStates;
    }

    public void setLoanStates(List<String> loanStates) {
        this.loanStates = loanStates;
    }

	public String getContractNum() {
		return contractNum;
	}

	public void setContractNum(String contractNum) {
		this.contractNum = contractNum;
	}
    
}
