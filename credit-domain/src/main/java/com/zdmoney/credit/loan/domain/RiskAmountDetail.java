package com.zdmoney.credit.loan.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class RiskAmountDetail extends BaseDomain{
    /**
	 * 
	 */
	private static final long serialVersionUID = -4364282301895340077L;

	private Long id;

    private Long loanId;

    private Date tradeDate;

    private Long term;

    private BigDecimal corpus;

    private BigDecimal accrual;

    private String memo;

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

    public Date getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(Date tradeDate) {
        this.tradeDate = tradeDate;
    }

    public Long getTerm() {
        return term;
    }

    public void setTerm(Long term) {
        this.term = term;
    }

    public BigDecimal getCorpus() {
        return corpus;
    }

    public void setCorpus(BigDecimal corpus) {
        this.corpus = corpus;
    }

    public BigDecimal getAccrual() {
        return accrual;
    }

    public void setAccrual(BigDecimal accrual) {
        this.accrual = accrual;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
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