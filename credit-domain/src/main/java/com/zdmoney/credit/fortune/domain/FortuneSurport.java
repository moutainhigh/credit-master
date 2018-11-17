package com.zdmoney.credit.fortune.domain;


import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class FortuneSurport extends BaseDomain{
    /**
	 * 
	 */
	private static final long serialVersionUID = 480216617379312511L;

	private Long id;

    private Long loanId;

    private String loanType;

    private Date sendTime;

    private Date curReturnDate;

    private Date createTime;

    private Date updateTime;


    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType == null ? null : loanType.trim();
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Date getCurReturnDate() {
        return curReturnDate;
    }

    public void setCurReturnDate(Date curReturnDate) {
        this.curReturnDate = curReturnDate;
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
}