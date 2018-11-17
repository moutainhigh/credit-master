package com.zdmoney.credit.loan.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class LoanReturnRecord extends BaseDomain{
	
	private static final long serialVersionUID = -1993078895985475613L;

	private Long id;

    private Long loanId;

    private Long currentTerm;

    private String batchNum;

    private String fundsSources;

    private Date buyBackTime;

    private BigDecimal amount;

    private Date createTime;

    private String creator;

    private Date updateTime;

    private String updator;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public Long getCurrentTerm() {
        return currentTerm;
    }

    public void setCurrentTerm(Long currentTerm) {
        this.currentTerm = currentTerm;
    }

    public String getBatchNum() {
        return batchNum;
    }

    public void setBatchNum(String batchNum) {
        this.batchNum = batchNum == null ? null : batchNum.trim();
    }

    public String getFundsSources() {
        return fundsSources;
    }

    public void setFundsSources(String fundsSources) {
        this.fundsSources = fundsSources == null ? null : fundsSources.trim();
    }

    public Date getBuyBackTime() {
        return buyBackTime;
    }

    public void setBuyBackTime(Date buyBackTime) {
        this.buyBackTime = buyBackTime;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdator() {
        return updator;
    }

    public void setUpdator(String updator) {
        this.updator = updator == null ? null : updator.trim();
    }
}