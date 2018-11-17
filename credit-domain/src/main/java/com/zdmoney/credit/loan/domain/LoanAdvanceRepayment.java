package com.zdmoney.credit.loan.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class LoanAdvanceRepayment extends BaseDomain{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7362304333893233817L;
	
    private Long id;

    private Date createTime;

    private Date factReturnDate;

    private Long loanId;

    private Long personId;

    private BigDecimal repaymentAllAmout;

    private String type;

    private String memo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getFactReturnDate() {
        return factReturnDate;
    }

    public void setFactReturnDate(Date factReturnDate) {
        this.factReturnDate = factReturnDate;
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public BigDecimal getRepaymentAllAmout() {
        return repaymentAllAmout;
    }

    public void setRepaymentAllAmout(BigDecimal repaymentAllAmout) {
        this.repaymentAllAmout = repaymentAllAmout;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

}
