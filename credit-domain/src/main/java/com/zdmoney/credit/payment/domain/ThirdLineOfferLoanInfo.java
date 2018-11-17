package com.zdmoney.credit.payment.domain;

import java.math.BigDecimal;

public class ThirdLineOfferLoanInfo extends ThirdLineOffer{

    private static final long serialVersionUID = -866547214241348178L;

    /** 客户姓名 **/
    private String name;

    /** 客户性别 **/
    private String sex;

    /** 身份证号 **/
    private String idnum;

    /** 借款类型 **/
    private String loanType;
    
    /** 合同来源 **/
    private String fundsSource;

    /** 合同金额 **/
    private BigDecimal pactMoney;

    /** 所属银行 **/
    private String belongBankName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIdnum() {
        return idnum;
    }

    public void setIdnum(String idnum) {
        this.idnum = idnum;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getFundsSource() {
        return fundsSource;
    }

    public void setFundsSource(String fundsSource) {
        this.fundsSource = fundsSource;
    }

    public BigDecimal getPactMoney() {
        return pactMoney;
    }

    public void setPactMoney(BigDecimal pactMoney) {
        this.pactMoney = pactMoney;
    }

    public String getBelongBankName() {
        return belongBankName;
    }

    public void setBelongBankName(String belongBankName) {
        this.belongBankName = belongBankName;
    }
}