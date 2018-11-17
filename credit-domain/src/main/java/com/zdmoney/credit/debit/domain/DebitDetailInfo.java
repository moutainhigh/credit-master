package com.zdmoney.credit.debit.domain;

import java.math.BigDecimal;

import com.zdmoney.credit.framework.domain.BaseDomain;

/**
 *第三方划扣明细信息表
 */
public class DebitDetailInfo  extends BaseDomain{

	private static final long serialVersionUID = 1710413778024958723L;

	private Long id;  
	/**债权编号**/
    private Long loanId;
    /**划扣信息表主键ID**/
    private Long debitId ;     
    /**溢缴款账户号**/
    private String accountNo;
    /**减免金额**/
    private BigDecimal reduceAmount;
    /**支付系统渠道（银行）编号**/
    private String  channelNo;
    /**联行号**/
    private String bankDetailNo;
    /**开户行名称**/
    private String bankName;
    /**开户行所在省**/
    private String province;
    /**开户行所在市**/
    private String city;
    /**信用卡安全码**/
    private String cvn2No;
    /**信用卡有效期**/
    private String valDate;
    /**手机号**/
    private String phoneNo;
    /**邮箱**/
    private String email;
    /**机构号**/
    private String brNo;
    
    private String memo;        
    
	public Long getLoanId() {
		return loanId;
	}
	public void setLoanId(Long loanId) {
		this.loanId = loanId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getDebitId() {
		return debitId;
	}
	public void setDebitId(Long debitId) {
		this.debitId = debitId;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public BigDecimal getReduceAmount() {
		return reduceAmount;
	}
	public void setReduceAmount(BigDecimal reduceAmount) {
		this.reduceAmount = reduceAmount;
	}
	public String getChannelNo() {
		return channelNo;
	}
	public void setChannelNo(String channelNo) {
		this.channelNo = channelNo;
	}
	public String getBankDetailNo() {
		return bankDetailNo;
	}
	public void setBankDetailNo(String bankDetailNo) {
		this.bankDetailNo = bankDetailNo;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getCvn2No() {
		return cvn2No;
	}
	public void setCvn2No(String cvn2No) {
		this.cvn2No = cvn2No;
	}
	public String getValDate() {
		return valDate;
	}
	public void setValDate(String valDate) {
		this.valDate = valDate;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getBrNo() {
		return brNo;
	}
	public void setBrNo(String brNo) {
		this.brNo = brNo;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
    
}
