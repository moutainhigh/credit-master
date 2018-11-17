package com.zdmoney.credit.loan.vo;

import com.zdmoney.credit.framework.domain.BaseDomain;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by ym10094 on 2017/10/9.
 */
public class ReliefQueryReportVo extends BaseDomain {
    private static final long serialVersionUID = -3771488856287405068L;

    private String contractNum;

    private String salesDepartmentName;

    private String name;

    private String loanType;

    private BigDecimal repayAmount;

    private BigDecimal reliefAmonut;

    private String applyType;

    private String applySource;

    private String isSpecial;

    private String flag;

    private Date releifDate;

    private BigDecimal releifFine;

    private BigDecimal reliefPenalty;

    private BigDecimal reliefInterest;

    private BigDecimal releifPrincipal;

    private String grantType;

    private Date grantDate;

    private BigDecimal pactMoney;

    private String tradeNo;

    private String idNum;

    private Long time;
    public String getContractNum() {
        return contractNum;
    }

    public void setContractNum(String contractNum) {
        this.contractNum = contractNum;
    }

    public String getSalesDepartmentName() {
        return salesDepartmentName;
    }

    public void setSalesDepartmentName(String salesDepartmentName) {
        this.salesDepartmentName = salesDepartmentName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public BigDecimal getRepayAmount() {
        return repayAmount;
    }

    public void setRepayAmount(BigDecimal repayAmount) {
        this.repayAmount = repayAmount;
    }

    public BigDecimal getReliefAmonut() {
        return reliefAmonut;
    }

    public void setReliefAmonut(BigDecimal reliefAmonut) {
        this.reliefAmonut = reliefAmonut;
    }

    public String getApplyType() {
        return applyType;
    }

    public void setApplyType(String applyType) {
        this.applyType = applyType;
    }

    public String getApplySource() {
        return applySource;
    }

    public void setApplySource(String applySource) {
        this.applySource = applySource;
    }

    public String getIsSpecial() {
        return isSpecial;
    }

    public void setIsSpecial(String isSpecial) {
        this.isSpecial = isSpecial;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Date getReleifDate() {
        return releifDate;
    }

    public void setReleifDate(Date releifDate) {
        this.releifDate = releifDate;
    }

    public BigDecimal getReleifFine() {
        return releifFine;
    }

    public void setReleifFine(BigDecimal releifFine) {
        this.releifFine = releifFine;
    }

    public BigDecimal getReliefPenalty() {
        return reliefPenalty;
    }

    public void setReliefPenalty(BigDecimal reliefPenalty) {
        this.reliefPenalty = reliefPenalty;
    }

    public BigDecimal getReliefInterest() {
        return reliefInterest;
    }

    public void setReliefInterest(BigDecimal reliefInterest) {
        this.reliefInterest = reliefInterest;
    }

    public BigDecimal getReleifPrincipal() {
        return releifPrincipal;
    }

    public void setReleifPrincipal(BigDecimal releifPrincipal) {
        this.releifPrincipal = releifPrincipal;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public Date getGrantDate() {
        return grantDate;
    }

    public void setGrantDate(Date grantDate) {
        this.grantDate = grantDate;
    }

    public BigDecimal getPactMoney() {
        return pactMoney;
    }

    public void setPactMoney(BigDecimal pactMoney) {
        this.pactMoney = pactMoney;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getIdNum() {
        return idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
