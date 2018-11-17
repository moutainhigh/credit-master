package com.zdmoney.credit.overdue.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class OverdueRatioNew extends BaseDomain{
    /**
	 * 
	 */
	private static final long serialVersionUID = -3789527819997315089L;

	private BigDecimal id;

    private String depName;

    private String depCode;

    private String departmentNum;

    private String departmentType;

    private BigDecimal did;

    private BigDecimal residualPactMoney;

    private String loanType;

    private BigDecimal residualPactMoneyProduct;

    private BigDecimal residualPactMoneyOverP;

    private BigDecimal overdueStatProduct;

    private Long productCount;

    private Long productCountOverdue;

    private BigDecimal m1ResidualPactMoney;

    private BigDecimal m1OverdueStat;

    private BigDecimal m2ResidualPactMoney;

    private BigDecimal m2OverdueStat;

    private BigDecimal m3ResidualPactMoney;

    private BigDecimal m3OverdueStat;

    private BigDecimal m46ResidualPactMoney;

    private BigDecimal m46OverdueStat;

    private BigDecimal m7GeResidualPactMoney;

    private BigDecimal m7GeOverdueStat;

    private BigDecimal overdueStatDepartment;

    private Date curDate;

    private String zoneCode;

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName == null ? null : depName.trim();
    }

    public String getDepCode() {
        return depCode;
    }

    public void setDepCode(String depCode) {
        this.depCode = depCode == null ? null : depCode.trim();
    }

    public String getDepartmentNum() {
        return departmentNum;
    }

    public void setDepartmentNum(String departmentNum) {
        this.departmentNum = departmentNum == null ? null : departmentNum.trim();
    }

    public String getDepartmentType() {
        return departmentType;
    }

    public void setDepartmentType(String departmentType) {
        this.departmentType = departmentType == null ? null : departmentType.trim();
    }

    public BigDecimal getDid() {
        return did;
    }

    public void setDid(BigDecimal did) {
        this.did = did;
    }

    public BigDecimal getResidualPactMoney() {
        return residualPactMoney;
    }

    public void setResidualPactMoney(BigDecimal residualPactMoney) {
        this.residualPactMoney = residualPactMoney;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType == null ? null : loanType.trim();
    }

    public BigDecimal getResidualPactMoneyProduct() {
        return residualPactMoneyProduct;
    }

    public void setResidualPactMoneyProduct(BigDecimal residualPactMoneyProduct) {
        this.residualPactMoneyProduct = residualPactMoneyProduct;
    }

    public BigDecimal getResidualPactMoneyOverP() {
        return residualPactMoneyOverP;
    }

    public void setResidualPactMoneyOverP(BigDecimal residualPactMoneyOverP) {
        this.residualPactMoneyOverP = residualPactMoneyOverP;
    }

    public BigDecimal getOverdueStatProduct() {
        return overdueStatProduct;
    }

    public void setOverdueStatProduct(BigDecimal overdueStatProduct) {
        this.overdueStatProduct = overdueStatProduct;
    }

    public Long getProductCount() {
        return productCount;
    }

    public void setProductCount(Long productCount) {
        this.productCount = productCount;
    }

    public Long getProductCountOverdue() {
        return productCountOverdue;
    }

    public void setProductCountOverdue(Long productCountOverdue) {
        this.productCountOverdue = productCountOverdue;
    }

    public BigDecimal getM1ResidualPactMoney() {
        return m1ResidualPactMoney;
    }

    public void setM1ResidualPactMoney(BigDecimal m1ResidualPactMoney) {
        this.m1ResidualPactMoney = m1ResidualPactMoney;
    }

    public BigDecimal getM1OverdueStat() {
        return m1OverdueStat;
    }

    public void setM1OverdueStat(BigDecimal m1OverdueStat) {
        this.m1OverdueStat = m1OverdueStat;
    }

    public BigDecimal getM2ResidualPactMoney() {
        return m2ResidualPactMoney;
    }

    public void setM2ResidualPactMoney(BigDecimal m2ResidualPactMoney) {
        this.m2ResidualPactMoney = m2ResidualPactMoney;
    }

    public BigDecimal getM2OverdueStat() {
        return m2OverdueStat;
    }

    public void setM2OverdueStat(BigDecimal m2OverdueStat) {
        this.m2OverdueStat = m2OverdueStat;
    }

    public BigDecimal getM3ResidualPactMoney() {
        return m3ResidualPactMoney;
    }

    public void setM3ResidualPactMoney(BigDecimal m3ResidualPactMoney) {
        this.m3ResidualPactMoney = m3ResidualPactMoney;
    }

    public BigDecimal getM3OverdueStat() {
        return m3OverdueStat;
    }

    public void setM3OverdueStat(BigDecimal m3OverdueStat) {
        this.m3OverdueStat = m3OverdueStat;
    }

    public BigDecimal getM46ResidualPactMoney() {
        return m46ResidualPactMoney;
    }

    public void setM46ResidualPactMoney(BigDecimal m46ResidualPactMoney) {
        this.m46ResidualPactMoney = m46ResidualPactMoney;
    }

    public BigDecimal getM46OverdueStat() {
        return m46OverdueStat;
    }

    public void setM46OverdueStat(BigDecimal m46OverdueStat) {
        this.m46OverdueStat = m46OverdueStat;
    }

    public BigDecimal getM7GeResidualPactMoney() {
        return m7GeResidualPactMoney;
    }

    public void setM7GeResidualPactMoney(BigDecimal m7GeResidualPactMoney) {
        this.m7GeResidualPactMoney = m7GeResidualPactMoney;
    }

    public BigDecimal getM7GeOverdueStat() {
        return m7GeOverdueStat;
    }

    public void setM7GeOverdueStat(BigDecimal m7GeOverdueStat) {
        this.m7GeOverdueStat = m7GeOverdueStat;
    }

    public BigDecimal getOverdueStatDepartment() {
        return overdueStatDepartment;
    }

    public void setOverdueStatDepartment(BigDecimal overdueStatDepartment) {
        this.overdueStatDepartment = overdueStatDepartment;
    }

    public Date getCurDate() {
        return curDate;
    }

    public void setCurDate(Date curDate) {
        this.curDate = curDate;
    }

    public String getZoneCode() {
        return zoneCode;
    }

    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode == null ? null : zoneCode.trim();
    }

}