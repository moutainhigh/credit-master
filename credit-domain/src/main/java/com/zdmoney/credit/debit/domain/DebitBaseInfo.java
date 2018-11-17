package com.zdmoney.credit.debit.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

/**
 * 
 * 第三方划扣信息表
 * 
 * */
public class DebitBaseInfo extends BaseDomain {

    private static final long serialVersionUID = 1448616724824237624L;

    private Long id;
    /** 债权编号 **/
    private Long loanId;
    /** 合同编号 **/
    private String pactNo;
    /** 证件类型 **/
    private String idType;
    /** 证件号码 **/
    private String idNo;
    /** 客户姓名 **/
    private String custName;
    /** 银行代码 **/
    private String bankCode;
    /** 第三方通道编码（00018：包商银行、00019：外贸3） **/
    private String paySysNo;
    /** 划扣类型（自动划扣、实时划扣） **/
    private String type;
    /** 报盘时间 **/
    private Date offerDate;
    /** 应收金额 **/
    private BigDecimal amount;
    /** 报盘金额 **/
    private BigDecimal offerAmount;
    /** 实际划扣金额 **/
    private BigDecimal actualAmount;
    /** 扣款卡号 **/
    private String acctNo;
    /** 账户类型 **/
    private String acctType;
    /** 账户名称 **/
    private String acctName;
    /** 状态（未报盘、已报盘、已回盘全额、已回盘非全额、已关闭） **/
    private String state;

    private String memo;
    private String bankName;

    public String getBankName() {
        return bankName;
    }

    public void setMemo(String memo) {
		this.memo = memo;
	}

	public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getPaySysNo() {
        return paySysNo;
    }

    public void setPaySysNo(String paySysNo) {
        this.paySysNo = paySysNo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getOfferAmount() {
        return offerAmount;
    }

    public void setOfferAmount(BigDecimal offerAmount) {
        this.offerAmount = offerAmount;
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

    public String getPactNo() {
        return pactNo;
    }

    public void setPactNo(String pactNo) {
        this.pactNo = pactNo;
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

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public Date getOfferDate() {
        return offerDate;
    }

    public void setOfferDate(Date offerDate) {
        this.offerDate = offerDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(BigDecimal actualAmount) {
        this.actualAmount = actualAmount;
    }

    public String getAcctNo() {
        return acctNo;
    }

    public void setAcctNo(String acctNo) {
        this.acctNo = acctNo;
    }

    public String getAcctType() {
        return acctType;
    }

    public void setAcctType(String acctType) {
        this.acctType = acctType;
    }

    public String getAcctName() {
        return acctName;
    }

    public void setAcctName(String acctName) {
        this.acctName = acctName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMemo() {
        return memo;
    }
}
