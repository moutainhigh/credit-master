package com.zdmoney.credit.loan.domain;

import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class LoanManageSalesDepLog extends BaseDomain {

    private static final long serialVersionUID = 1656828603235154426L;
    
    /**
     * Id编号
     */
    private Long id;
    
    /**
     * 变更时间
     */
    private Date beginDate;
    
    /**
     * 更新时间
     */
    private Date endDate;
    
    /**
     * 债权Id
     */
    private Long loanId;
    
    /**
     * 新客服Id
     */
    private Long newCrmId;
    
    /**
     * 旧客服Id
     */
    private Long oldCrmId;
    
    /**
     * 操作客服Id
     */
    private Long operatorId;
    
    /**
     * 管理部门机构Id
     */
    private Long salesDepId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public Long getNewCrmId() {
        return newCrmId;
    }

    public void setNewCrmId(Long newCrmId) {
        this.newCrmId = newCrmId;
    }

    public Long getOldCrmId() {
        return oldCrmId;
    }

    public void setOldCrmId(Long oldCrmId) {
        this.oldCrmId = oldCrmId;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public Long getSalesDepId() {
        return salesDepId;
    }

    public void setSalesDepId(Long salesDepId) {
        this.salesDepId = salesDepId;
    }
}
