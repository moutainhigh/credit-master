package com.zdmoney.credit.common.vo.core;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 报回盘接口参数VO
 * 
 * @author 00235304
 * 
 */
public class OfferParamsVo {

	/** 姓名 */
	private String name;
	
	/** 身份证号 */
	private String idnum;
	
	/** 债权ID */
	private Long loanId;
	
	/** 报盘状态 */
	private String offerState;
	
	/** 是否成功 */
	private String isSuc;
	
	/** 报盘日期，格式为：yyyy-MM-dd */
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date offerDate;
	
	/** 操作员code */
	private String userCode;
	
	/** 合同来源 */
	private String fundsSources;
	
	/** 每页显示条数，默认为10 */
	private Long max = 10L;
	
	/** 指定的查询页码 */
	private Long offset=0L;
	
	/** 报盘金额 */
	private BigDecimal offerAmount;
	
	/**划扣通道*/
	private String paySysNo;
	
	private boolean isShowPayChannel;

	public String getPaySysNo() {
		return paySysNo;
	}

	public void setPaySysNo(String paySysNo) {
		this.paySysNo = paySysNo;
	}

	public Long getLoanId() {
		return loanId;
	}

	public void setLoanId(Long loanId) {
		this.loanId = loanId;
	}

	public BigDecimal getOfferAmount() {
		return offerAmount;
	}

	public void setOfferAmount(BigDecimal offerAmount) {
		this.offerAmount = offerAmount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdnum() {
		return idnum;
	}

	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}

	public String getOfferState() {
		return offerState;
	}

	public void setOfferState(String offerState) {
		this.offerState = offerState;
	}

	public String getIsSuc() {
		return isSuc;
	}

	public void setIsSuc(String isSuc) {
		this.isSuc = isSuc;
	}

	public Date getOfferDate() {
		return offerDate;
	}

	public void setOfferDate(Date offerDate) {
		this.offerDate = offerDate;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getFundsSources() {
		return fundsSources;
	}

	public void setFundsSources(String fundsSources) {
		this.fundsSources = fundsSources;
	}

	public Long getMax() {
		return max;
	}

	public void setMax(Long max) {
		this.max = max;
	}

	public Long getOffset() {
		return offset;
	}

	public void setOffset(Long offset) {
		this.offset = offset;
	}

	public boolean isShowPayChannel() {
		return isShowPayChannel;
	}

	public void setShowPayChannel(boolean isShowPayChannel) {
		this.isShowPayChannel = isShowPayChannel;
	}

	@Override
	public String toString() {
		return "OfferParamsVo [name=" + name + ", idnum=" + idnum + ", loanid="
				+ loanId + ", offerState=" + offerState + ", isSuc=" + isSuc
				+ ", offerDate=" + offerDate + ", userCode=" + userCode
				+ ", fundsSources=" + fundsSources + ", max=" + max
				+ ", offset=" + offset + "]";
	}

}
