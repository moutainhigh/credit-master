package com.zdmoney.credit.common.util.exceldata;


import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by 00232949 on 2015/4/30.
 */
public class XintuoguominData implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2932257378921090303L;
	String name;
    String sex;
    String idnum;
    String mphone;
    String address;
    String borrower_name;
    String account;
    String bank_full_name;
    BigDecimal pact_money;
    Integer time;
    String purpose;
    BigDecimal repayment_all;
    String contract_num;
    BigDecimal rate_sum;
    String loan_type;
    
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
	public String getMphone() {
		return mphone;
	}
	public void setMphone(String mphone) {
		this.mphone = mphone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getBorrower_name() {
		return borrower_name;
	}
	public void setBorrower_name(String borrower_name) {
		this.borrower_name = borrower_name;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getBank_full_name() {
		return bank_full_name;
	}
	public void setBank_full_name(String bank_full_name) {
		this.bank_full_name = bank_full_name;
	}
	public BigDecimal getPact_money() {
		return pact_money;
	}
	public void setPact_money(BigDecimal pact_money) {
		this.pact_money = pact_money;
	}
	public Integer getTime() {
		return time;
	}
	public void setTime(Integer time) {
		this.time = time;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public BigDecimal getRepayment_all() {
		return repayment_all;
	}
	public void setRepayment_all(BigDecimal repayment_all) {
		this.repayment_all = repayment_all;
	}
	public String getContract_num() {
		return contract_num;
	}
	public void setContract_num(String contract_num) {
		this.contract_num = contract_num;
	}
	public BigDecimal getRate_sum() {
		return rate_sum;
	}
	public void setRate_sum(BigDecimal rate_sum) {
		this.rate_sum = rate_sum;
	}
	public String getLoan_type() {
		return loan_type;
	}
	public void setLoan_type(String loan_type) {
		this.loan_type = loan_type;
	}

    
}
