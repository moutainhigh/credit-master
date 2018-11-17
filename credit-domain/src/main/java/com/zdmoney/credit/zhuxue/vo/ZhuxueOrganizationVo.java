package com.zdmoney.credit.zhuxue.vo;

import java.math.BigDecimal;
import java.util.Date;

public class ZhuxueOrganizationVo {
	private Long id;

    private String address;

    private String code;
    
    private Date dateSigned;

    private BigDecimal marginRate;

    private String memo;

    private String name;

    private String orgType;

    private String owner;

    private String ownerIdnum;

    private String tel;

    private String postCode;

    private String contact;

    private String contactTel;

    private String bankAccountType;
    
    private BigDecimal guazhangjine;//挂账金额
    
    private BigDecimal tradeAmount;//开户总金额

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Date getDateSigned() {
		return dateSigned;
	}

	public void setDateSigned(Date dateSigned) {
		this.dateSigned = dateSigned;
	}

	public BigDecimal getMarginRate() {
		return marginRate;
	}

	public void setMarginRate(BigDecimal marginRate) {
		this.marginRate = marginRate;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrgType() {
		return orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getOwnerIdnum() {
		return ownerIdnum;
	}

	public void setOwnerIdnum(String ownerIdnum) {
		this.ownerIdnum = ownerIdnum;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getContactTel() {
		return contactTel;
	}

	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
	}

	public String getBankAccountType() {
		return bankAccountType;
	}

	public void setBankAccountType(String bankAccountType) {
		this.bankAccountType = bankAccountType;
	}

	public BigDecimal getGuazhangjine() {
		return guazhangjine;
	}

	public void setGuazhangjine(BigDecimal guazhangjine) {
		this.guazhangjine = guazhangjine;
	}

	public BigDecimal getTradeAmount() {
		return tradeAmount;
	}

	public void setTradeAmount(BigDecimal tradeAmount) {
		this.tradeAmount = tradeAmount;
	}
}
