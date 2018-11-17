package com.zdmoney.credit.xintuo.domain;

import java.math.BigDecimal;

import com.zdmoney.credit.common.util.exceldata.XintuoguominData;
import com.zdmoney.credit.framework.domain.BaseDomain;

public class XintuoguominDataDomain extends BaseDomain{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8881654998615718628L;
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
	
	public XintuoguominData toXintuoguominData() {
		XintuoguominData newInstance = new XintuoguominData();
		newInstance.setName(this.name);
		newInstance.setSex(this.sex);
		newInstance.setIdnum(this.idnum);
		newInstance.setMphone(this.mphone);
		newInstance.setAddress(this.address);
		newInstance.setBorrower_name(this.borrower_name);
		newInstance.setAccount(this.account);
		newInstance.setBank_full_name(this.bank_full_name);
		newInstance.setPact_money(this.pact_money);
		newInstance.setTime(this.time);
		newInstance.setPurpose(this.purpose);
		newInstance.setRepayment_all(this.repayment_all);
		newInstance.setContract_num(this.contract_num);
		newInstance.setRate_sum(this.rate_sum);
		newInstance.setLoan_type(this.loan_type);
		
		return newInstance;
	}
}
