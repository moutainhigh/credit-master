package com.zdmoney.credit.fee.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

/**
 * 借款收费报盘表
 * 
 * @author Ivan
 *
 */
public class LoanFeeOffer extends BaseDomain {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7748048181266406823L;
	/** 主键 **/
	private Long id;
	/** 债权编号 **/
	private Long loanId;
	/** 收费主表编号 **/
	private Long feeId;
	/** 合同编号 **/
	private String contractNum;
	/** 姓名 **/
	private String name;
	/** 身份证号 **/
	private String IdNum;
	/** 手机号 **/
	private String mobile;
	/** 报盘应收金额 **/
	private BigDecimal amount;
	/** 报盘已收金额 **/
	private BigDecimal receiveAmount;
	/** 报盘未收金额 **/
	private BigDecimal unpaidAmount;
	/** 银行账号 **/
	private String bankAcct;
	/** 银行名称 **/
	private String bankName;
	/** 银行代码 **/
	private String bankCode;
	/** 货币类型 **/
	private String currencyType;
	/** 划扣类型（自动划扣，实时划扣） **/
	private String type;
	/** 报盘日期 **/
	private Date offerDate;
	/** 信息类别编码（TPP2.0平台提供统一分配） **/
	private String infoCategoryCode;
	/** 业务系统编码（由TPP2.0平台提供统一分配） **/
	private String bizSysNo;
	/** 第三方通道编码（由TPP2.0系统统一提供 0 通联支付 2富友支付 4 上海银联支付 6 用友支付 8 上海银联支付-实名认证 ） **/
	private String paySysNo;
	/** 网点号 **/
	private String organ;
	/** 报盘状态（未报盘、已报盘、已回盘全额、已回盘非全额、已关闭） **/
	private String state;
	/** 预留备注信息 **/
	private String memo;
	/** 第三方通道编码（由TPP2.0系统统一提供 0 通联支付 2富友支付 4 上海银联支付 6 用友支付 8 上海银联支付-实名认证 ） 18 快捷通 **/
	private String paySysNoReal;

	public String getPaySysNoReal() {
		return paySysNoReal;
	}

	public void setPaySysNoReal(String paySysNoReal) {
		this.paySysNoReal = paySysNoReal;
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

	public Long getFeeId() {
		return feeId;
	}

	public void setFeeId(Long feeId) {
		this.feeId = feeId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getReceiveAmount() {
		return receiveAmount;
	}

	public void setReceiveAmount(BigDecimal receiveAmount) {
		this.receiveAmount = receiveAmount;
	}

	public BigDecimal getUnpaidAmount() {
		return unpaidAmount;
	}

	public void setUnpaidAmount(BigDecimal unpaidAmount) {
		this.unpaidAmount = unpaidAmount;
	}

	public String getBankAcct() {
		return bankAcct;
	}

	public void setBankAcct(String bankAcct) {
		this.bankAcct = bankAcct == null ? null : bankAcct.trim();
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName == null ? null : bankName.trim();
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode == null ? null : bankCode.trim();
	}

	public String getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType == null ? null : currencyType.trim();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type == null ? null : type.trim();
	}

	public String getInfoCategoryCode() {
		return infoCategoryCode;
	}

	public void setInfoCategoryCode(String infoCategoryCode) {
		this.infoCategoryCode = infoCategoryCode == null ? null : infoCategoryCode.trim();
	}

	public String getBizSysNo() {
		return bizSysNo;
	}

	public void setBizSysNo(String bizSysNo) {
		this.bizSysNo = bizSysNo == null ? null : bizSysNo.trim();
	}

	public String getPaySysNo() {
		return paySysNo;
	}

	public void setPaySysNo(String paySysNo) {
		this.paySysNo = paySysNo == null ? null : paySysNo.trim();
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state == null ? null : state.trim();
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo == null ? null : memo.trim();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdNum() {
		return IdNum;
	}

	public void setIdNum(String idNum) {
		IdNum = idNum;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getContractNum() {
		return contractNum;
	}

	public void setContractNum(String contractNum) {
		this.contractNum = contractNum;
	}

	public Date getOfferDate() {
		return offerDate;
	}

	public void setOfferDate(Date offerDate) {
		this.offerDate = offerDate;
	}

	public String getOrgan() {
		return organ;
	}

	public void setOrgan(String organ) {
		this.organ = organ;
	}

}