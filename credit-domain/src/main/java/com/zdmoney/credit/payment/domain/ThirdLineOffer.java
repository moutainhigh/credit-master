package com.zdmoney.credit.payment.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class ThirdLineOffer extends BaseDomain{
    
    private static final long serialVersionUID = -8010611323390367493L;
    
    private Long id;

    private String tlPaymentNumber;

    private String bankCode;

    private String accountNumberType;

    private String accountNumber;

    private String accountName;

    private String bankProvince;

    private String bankCity;

    private String bankName;

    private String accountType;

    private BigDecimal amount;

    private String currencyType;

    private String protocolNumber;

    private String protocolUserNumber;

    private String certificateType;

    private String licenseNumber;

    private String telNumber;

    private String customUserNumber;

    private String remark;

    private Long loanId;

    private String financialType;

    private String state;

    private Date offerTime;
    
    private String recordNumber;
    
    private String[] states;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTlPaymentNumber() {
        return tlPaymentNumber;
    }

    public void setTlPaymentNumber(String tlPaymentNumber) {
        this.tlPaymentNumber = tlPaymentNumber == null ? null : tlPaymentNumber.trim();
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode == null ? null : bankCode.trim();
    }

    public String getAccountNumberType() {
        return accountNumberType;
    }

    public void setAccountNumberType(String accountNumberType) {
        this.accountNumberType = accountNumberType == null ? null : accountNumberType.trim();
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber == null ? null : accountNumber.trim();
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName == null ? null : accountName.trim();
    }

    public String getBankProvince() {
        return bankProvince;
    }

    public void setBankProvince(String bankProvince) {
        this.bankProvince = bankProvince == null ? null : bankProvince.trim();
    }

    public String getBankCity() {
        return bankCity;
    }

    public void setBankCity(String bankCity) {
        this.bankCity = bankCity == null ? null : bankCity.trim();
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName == null ? null : bankName.trim();
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType == null ? null : accountType.trim();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType == null ? null : currencyType.trim();
    }

    public String getProtocolNumber() {
        return protocolNumber;
    }

    public void setProtocolNumber(String protocolNumber) {
        this.protocolNumber = protocolNumber == null ? null : protocolNumber.trim();
    }

    public String getProtocolUserNumber() {
        return protocolUserNumber;
    }

    public void setProtocolUserNumber(String protocolUserNumber) {
        this.protocolUserNumber = protocolUserNumber == null ? null : protocolUserNumber.trim();
    }

    public String getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType == null ? null : certificateType.trim();
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber == null ? null : licenseNumber.trim();
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber == null ? null : telNumber.trim();
    }

    public String getCustomUserNumber() {
        return customUserNumber;
    }

    public void setCustomUserNumber(String customUserNumber) {
        this.customUserNumber = customUserNumber == null ? null : customUserNumber.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public String getFinancialType() {
        return financialType;
    }

    public void setFinancialType(String financialType) {
        this.financialType = financialType == null ? null : financialType.trim();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    public Date getOfferTime() {
        return offerTime;
    }

    public void setOfferTime(Date offerTime) {
        this.offerTime = offerTime;
    }

    public String getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(String recordNumber) {
        this.recordNumber = recordNumber;
    }

    public String[] getStates() {
        return states;
    }

    public void setStates(String[] states) {
        this.states = states;
    }
}