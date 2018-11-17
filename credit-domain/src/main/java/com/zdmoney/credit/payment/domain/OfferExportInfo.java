package com.zdmoney.credit.payment.domain;

import com.zdmoney.credit.framework.domain.BaseDomain;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by ym10094 on 2016/8/15.
 */
public class OfferExportInfo extends  BaseDomain{
    private static final long serialVersionUID = -8010611323390367493L;
     /*
     *记录序列
     * */
    private String recSeqId;
    /**
     * 银行编码
     */
    private String bankCode;
    /**
     * 账号类型
     */
    private String accType;
    /**
     * 账号
     */
    private String account;
    /**
     * 账号名
     */
    private String accountName;
    /**
     *开户行名
     */
    private String bankName;
    /**
     * 放款金额
     */
    private BigDecimal grantMoney;
    /**
     * 证件类型
     */
    private String cardType;
    /**
     * 证件号码
     */
    private String cardId;
    /**
     * 总金额
     */
    private BigDecimal totalAmount;

    private String tlPaymentNumber;

    private String accountNumberType;

    private String accountNumber;

    private String bankProvince;

    private String bankCity;

    private String accountType;

    private String currencyType;

    private String protocolNumber;

    private String protocolUserNumber;

    private String certificateType;

    private String telNumber;

    private String customUserNumber;

    private String remark;

    private String state;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRecSeqId() {
        return recSeqId;
    }

    public void setRecSeqId(String recSeqId) {
        this.recSeqId = recSeqId;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getAccType() {
        return accType;
    }

    public void setAccType(String accType) {
        this.accType = accType;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public BigDecimal getGrantMoney() {
        return grantMoney;
    }

    public void setGrantMoney(BigDecimal grantMoney) {
        this.grantMoney = grantMoney;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTlPaymentNumber() {
        return tlPaymentNumber;
    }

    public void setTlPaymentNumber(String tlPaymentNumber) {
        this.tlPaymentNumber = tlPaymentNumber;
    }

    public String getAccountNumberType() {
        return accountNumberType;
    }

    public void setAccountNumberType(String accountNumberType) {
        this.accountNumberType = accountNumberType;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBankProvince() {
        return bankProvince;
    }

    public void setBankProvince(String bankProvince) {
        this.bankProvince = bankProvince;
    }

    public String getBankCity() {
        return bankCity;
    }

    public void setBankCity(String bankCity) {
        this.bankCity = bankCity;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public String getProtocolNumber() {
        return protocolNumber;
    }

    public void setProtocolNumber(String protocolNumber) {
        this.protocolNumber = protocolNumber;
    }

    public String getProtocolUserNumber() {
        return protocolUserNumber;
    }

    public void setProtocolUserNumber(String protocolUserNumber) {
        this.protocolUserNumber = protocolUserNumber;
    }

    public String getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public String getCustomUserNumber() {
        return customUserNumber;
    }

    public void setCustomUserNumber(String customUserNumber) {
        this.customUserNumber = customUserNumber;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
