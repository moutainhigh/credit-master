package com.zdmoney.credit.loan.domain;

import com.zdmoney.credit.framework.domain.BaseDomain;

import java.math.BigDecimal;
import java.util.Date;

public class LoanBaseOneTimeSettlement extends BaseDomain {
    private static final long serialVersionUID = -2036492339785769919L;

    private Long id;

    private Long loanId;

    private Long proposerId;

    private String appNo;

    private Date beginApplyDate;

    private Date endApplyDate;

    private String applyState;

    private Date createTime;

    private Date updateTime;

    private String creator;

    private String updator;

    private String remark;

    private String contractNum;
    /**
     * 扣款流水号
     */
    private String serialno;
    /**
     * 应收罚息
     */
    private BigDecimal payover;
    /**
     * 扣款金额
     */

    private BigDecimal repaytotal;
    /**
     * 扣款本金
     */

    private BigDecimal repayamt;
    /**
     * 扣款利息
     */

    private BigDecimal repayinte;
    /**
     * 扣款罚息
     */

    private BigDecimal repayover;

    public LoanBaseOneTimeSettlement() {
    }

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

    public Long getProposerId() {
        return proposerId;
    }

    public void setProposerId(Long proposerId) {
        this.proposerId = proposerId;
    }

    public String getAppNo() {
        return appNo;
    }

    public void setAppNo(String appNo) {
        this.appNo = appNo == null ? null : appNo.trim();
    }

    public Date getBeginApplyDate() {
        return beginApplyDate;
    }

    public void setBeginApplyDate(Date beginApplyDate) {
        this.beginApplyDate = beginApplyDate;
    }

    public Date getEndApplyDate() {
        return endApplyDate;
    }

    public void setEndApplyDate(Date endApplyDate) {
        this.endApplyDate = endApplyDate;
    }

    public String getApplyState() {
        return applyState;
    }

    public void setApplyState(String applyState) {
        this.applyState = applyState == null ? null : applyState.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public String getUpdator() {
        return updator;
    }

    public void setUpdator(String updator) {
        this.updator = updator == null ? null : updator.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getContractNum() {
        return contractNum;
    }

    public void setContractNum(String contractNum) {
        this.contractNum = contractNum;
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
}