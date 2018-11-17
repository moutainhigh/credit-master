package com.zdmoney.credit.repay.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class VRepaymentDetail extends BaseDomain {
    
    private static final long serialVersionUID = 9062667806591268552L;

    /**
     * 借款人姓名
     */
    private String name;

    /**
     * 借款人身份证号
     */
    private String idnum;

    /**
     * 交易金额
     */
    private BigDecimal amount;

    /**
     * 罚息
     */
    private BigDecimal fx;

    /**
     * 减免金额
     */
    private BigDecimal jmfx;

    /**
     * 风险金
     */
    private BigDecimal fxj;

    /**
     * 利息
     */
    private BigDecimal lx;

    /**
     * 本金
     */
    private BigDecimal bj;

    /**
     * 交易日期
     */
    private Date tradeDate;

    /**
     * 管理营业部Id
     */
    private Long salesDepartmentId;

    /**
     * 借款状态
     */
    private String loanState;

    /**
     * 借款类型
     */
    private String loanType;

    /**
     * 交易代码
     */
    private String tradeCode;

    /***
     * 债权Id
     */
    private Long loanId;
    
    /**
     * 导出最大件数
     */
    private Long max;
    
    /**
     * 合同编号
     * 
     */
    private String contractNum;
    
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getFx() {
        return fx;
    }

    public void setFx(BigDecimal fx) {
        this.fx = fx;
    }

    public BigDecimal getJmfx() {
        return jmfx;
    }

    public void setJmfx(BigDecimal jmfx) {
        this.jmfx = jmfx;
    }

    public BigDecimal getFxj() {
        return fxj;
    }

    public void setFxj(BigDecimal fxj) {
        this.fxj = fxj;
    }

    public BigDecimal getLx() {
        return lx;
    }

    public void setLx(BigDecimal lx) {
        this.lx = lx;
    }

    public BigDecimal getBj() {
        return bj;
    }

    public void setBj(BigDecimal bj) {
        this.bj = bj;
    }

    public Date getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(Date tradeDate) {
        this.tradeDate = tradeDate;
    }

    public Long getSalesDepartmentId() {
        return salesDepartmentId;
    }

    public void setSalesDepartmentId(Long salesDepartmentId) {
        this.salesDepartmentId = salesDepartmentId;
    }

    public String getLoanState() {
        return loanState;
    }

    public void setLoanState(String loanState) {
        this.loanState = loanState;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getTradeCode() {
        return tradeCode;
    }

    public void setTradeCode(String tradeCode) {
        this.tradeCode = tradeCode;
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
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