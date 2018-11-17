package com.zdmoney.credit.person.domain;


import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class PersonThirdPartyAccount extends BaseDomain{
    /**
	 * 
	 */
	private static final long serialVersionUID = 208200597853061916L;

	private Long id;

    private String account;

    private String bankName;

    private String fullBankName;

    private String bankCode;

    private String branchBankCode;

    private String login;

    private String password;

    private String thirdPartyId;

    private String name;

    private String idnum;

    private String mphone;

    private String type;

    private Date createTime;

    private Date updateTime;

    private String creator;
    
    private String creditUserTpp;

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

    public String getFullBankName() {
        return fullBankName;
    }

    public void setFullBankName(String fullBankName) {
        this.fullBankName = fullBankName == null ? null : fullBankName.trim();
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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login == null ? null : login.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getThirdPartyId() {
        return thirdPartyId;
    }

    public void setThirdPartyId(String thirdPartyId) {
        this.thirdPartyId = thirdPartyId == null ? null : thirdPartyId.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getIdnum() {
        return idnum;
    }

    public void setIdnum(String idnum) {
        this.idnum = idnum == null ? null : idnum.trim();
    }

    public String getMphone() {
        return mphone;
    }

    public void setMphone(String mphone) {
        this.mphone = mphone == null ? null : mphone.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCreditUserTpp() {
		return creditUserTpp;
	}

	public void setCreditUserTpp(String creditUserTpp) {
		this.creditUserTpp = creditUserTpp;
	}
}