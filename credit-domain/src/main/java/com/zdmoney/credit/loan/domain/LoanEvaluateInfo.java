package com.zdmoney.credit.loan.domain;

import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class LoanEvaluateInfo extends BaseDomain{
    /**
	 * 
	 */
	private static final long serialVersionUID = 4462053079191906162L;

	private Long id;

    private Long loanId;

    private String contractNum;

    private String customerName;

    private String idNum;

    private String isEval;

    private String creator;

    private Date createTime;

    private String updator;

    private Date updateTime;

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

    public String getContractNum() {
        return contractNum;
    }

    public void setContractNum(String contractNum) {
        this.contractNum = contractNum == null ? null : contractNum.trim();
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName == null ? null : customerName.trim();
    }

    public String getIdNum() {
        return idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum == null ? null : idNum.trim();
    }

    public String getIsEval() {
        return isEval;
    }

    public void setIsEval(String isEval) {
        this.isEval = isEval == null ? null : isEval.trim();
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdator() {
        return updator;
    }

    public void setUpdator(String updator) {
        this.updator = updator == null ? null : updator.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}