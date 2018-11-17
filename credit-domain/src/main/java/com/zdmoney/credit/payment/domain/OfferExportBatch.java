package com.zdmoney.credit.payment.domain;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;


/**
 *
 */
public class OfferExportBatch extends BaseDomain {
	private static final long serialVersionUID = -8010611323390367493L;

	private Long id;

	private BigInteger totalNumber;

	private BigDecimal totalAmount;

	private Date exportTime;

	private String seqNo;

	private Date createTime;

	private String creator;

	/**
	 * 商户号
	 */
	private String merchantId;
	/**
	 * 交易标志
	 */
	private String tradeMark;
	/**
	 * 业务类型
	 */
	private String businessType;

	/**
	 * 当前日期
	 * 
	 * @return
	 */
	private String currentDay;// yyyyMMdd

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigInteger getTotalNumber() {
		return totalNumber;
	}

	public void setTotalNumber(BigInteger totalNumber) {
		this.totalNumber = totalNumber;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Date getExportTime() {
		return exportTime;
	}

	public void setExportTime(Date exportTime) {
		this.exportTime = exportTime;
	}

	public String getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo == null ? null : seqNo.trim();
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator == null ? null : creator.trim();
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getTradeMark() {
		return tradeMark;
	}

	public void setTradeMark(String tradeMark) {
		this.tradeMark = tradeMark;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getCurrentDay() {
		return currentDay;
	}

	public void setCurrentDay(String currentDay) {
		this.currentDay = currentDay;
	}
}