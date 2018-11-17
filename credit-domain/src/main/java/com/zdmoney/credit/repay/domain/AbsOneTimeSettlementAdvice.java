package com.zdmoney.credit.repay.domain;

import com.zdmoney.credit.framework.domain.BaseDomain;

import java.math.BigDecimal;
import java.util.Date;
/**
 * 通知数信一次性结清表
 * @author 10098  2016年11月30日 晚上 20:51
 */
public class AbsOneTimeSettlementAdvice  extends BaseDomain {
    private static final long serialVersionUID = -6172303000281796662L;

    private Long id;

    private Long loanId;

    private String adviceState;

    private String serialno;

    private BigDecimal payover = new BigDecimal("0.00");

    private BigDecimal repaytotal = new BigDecimal("0.00");

    private BigDecimal repayamt = new BigDecimal("0.00");

    private BigDecimal repayinte = new BigDecimal("0.00");

    private BigDecimal repayover = new BigDecimal("0.00");

    private Date createTime;

    private Date updateTime;

    private String creator;

    private String updator;

    private String remark;

    private Integer lastTerm;

    private Integer startTerm;

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

    public String getAdviceState() {
        return adviceState;
    }

    public void setAdviceState(String adviceState) {
        this.adviceState = adviceState;
    }

    public String getSerialno() {
        return serialno;
    }

    public void setSerialno(String serialno) {
        this.serialno = serialno;
    }

    public BigDecimal getPayover() {
        return payover;
    }

    public void setPayover(BigDecimal payover) {
        this.payover = payover;
    }

    public BigDecimal getRepaytotal() {
        return repaytotal;
    }

    public void setRepaytotal(BigDecimal repaytotal) {
        this.repaytotal = repaytotal;
    }

    public BigDecimal getRepayamt() {
        return repayamt;
    }

    public void setRepayamt(BigDecimal repayamt) {
        this.repayamt = repayamt;
    }

    public BigDecimal getRepayinte() {
        return repayinte;
    }

    public void setRepayinte(BigDecimal repayinte) {
        this.repayinte = repayinte;
    }

    public BigDecimal getRepayover() {
        return repayover;
    }

    public void setRepayover(BigDecimal repayover) {
        this.repayover = repayover;
    }

    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public Date getUpdateTime() {
        return updateTime;
    }

    @Override
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String getCreator() {
        return creator;
    }

    @Override
    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Override
    public String getUpdator() {
        return updator;
    }

    @Override
    public void setUpdator(String updator) {
        this.updator = updator;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getLastTerm() {
        return lastTerm;
    }

    public void setLastTerm(Integer lastTerm) {
        this.lastTerm = lastTerm;
    }

    public Integer getStartTerm() {
        return startTerm;
    }

    public void setStartTerm(Integer startTerm) {
        this.startTerm = startTerm;
    }
}