package com.zdmoney.credit.grant.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 放款--账号信息
 * Created by ym10094 on 2016/11/11.
 */
public class GrantAccountVo implements Serializable {
    private static final long serialVersionUID = 1363417642726789126L;
    /**
     * 账户用途
     */
    private String acUse;
    /**
     *放款金额
     */
    private BigDecimal acAmt = new BigDecimal("0.00");
    /**
     * 账户类型
     */
    private String acType;
    /**
     * 账户号
     */
    private String acno;
    /**
     * 账户户名
     */
    private String acName;
    /**
     * 银行代码
     */
    private String bankCode;
    /**
     * 账户开户省
     */
    private String bankProv;
    /**
     *账户开户市
     */
    private String bankCity;
    /**
     * 开户银行网点
     */
    private String bankSite;
    /**
     * 证件类型
     */
	private String idType;

	/** 证件号码 **/
	private String idNo;

	/** 银行预留手机号 **/
	private String phoneNo;

	/** 信用卡有效期 **/
	private String validDate;

	/** 信用卡CVV2码 **/
	private String cvvNo;

    public String getAcUse() {
        return acUse;
    }

    public void setAcUse(String acUse) {
        this.acUse = acUse;
    }

    public BigDecimal getAcAmt() {
        return acAmt;
    }

    public void setAcAmt(BigDecimal acAmt) {
        this.acAmt = acAmt;
    }

    public String getAcType() {
        return acType;
    }

    public void setAcType(String acType) {
        this.acType = acType;
    }

    public String getAcno() {
        return acno;
    }

    public void setAcno(String acno) {
        this.acno = acno;
    }

    public String getAcName() {
        return acName;
    }

    public void setAcName(String acName) {
        this.acName = acName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankProv() {
        return bankProv;
    }

    public void setBankProv(String bankProv) {
        this.bankProv = bankProv;
    }

    public String getBankCity() {
        return bankCity;
    }

    public void setBankCity(String bankCity) {
        this.bankCity = bankCity;
    }

    public String getBankSite() {
        return bankSite;
    }

    public void setBankSite(String bankSite) {
        this.bankSite = bankSite;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getValidDate() {
        return validDate;
    }

    public void setValidDate(String validDate) {
        this.validDate = validDate;
    }

    public String getCvvNo() {
        return cvvNo;
    }

    public void setCvvNo(String cvvNo) {
        this.cvvNo = cvvNo;
    }
}
