package com.zdmoney.credit.loan.domain;


import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class LoanBank extends BaseDomain{
    /**
	 * 
	 */
	private static final long serialVersionUID = -5846166837762460131L;

	private Long id;

    private String account;

    private String bankName;

    private String fullName;

    private String bankCode;

    private String branchBankCode;

    private String bankDicType;

    private Date createTime;

    private Date updateTime;

    private Long objectId;
    
    public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName == null ? null : bankName.trim();
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName == null ? null : fullName.trim();
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode == null ? null : bankCode.trim();
    }

    public String getBranchBankCode() {
        return branchBankCode;
    }

    public void setBranchBankCode(String branchBankCode) {
        this.branchBankCode = branchBankCode == null ? null : branchBankCode.trim();
    }

    public String getBankDicType() {
        return bankDicType;
    }

    public void setBankDicType(String bankDicType) {
        this.bankDicType = bankDicType == null ? null : bankDicType.trim();
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
}