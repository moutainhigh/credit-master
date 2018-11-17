package com.zdmoney.credit.test.dao;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

/**
 * 减免本金或利息信息
 * @author 10098  2016年11月11日 下午2:16:09
 */
public class XdFeeReduceEntity {

	/**批次编号**/
	@NotBlank(message="批次编号不能为空")
	private String batNo;
	/**合同号**/
	@NotBlank(message="合同号不能为空")
	private String pactNo;
	/**减免本金**/
	@NotNull(message="减免本金不能为空")
	private BigDecimal refAmt;
	/**减免利息**/
	@NotNull(message="减免利息不能为空")
	private BigDecimal refInte;
	/**减免罚息**/
	@NotNull(message="减免罚息不能为空")
	private BigDecimal refOver;
	/**减免费用**/
	@NotNull(message="减免费用不能为空")
	private BigDecimal refFee;
	/**处理状态**/
	@NotBlank(message="处理状态不能为空")
	private String dealSts;
	/**处理说明**/
	private String dealDesc;
	
	public String getBatNo() {
		return batNo;
	}
	public void setBatNo(String batNo) {
		this.batNo = batNo;
	}
	public String getPactNo() {
		return pactNo;
	}
	public void setPactNo(String pactNo) {
		this.pactNo = pactNo;
	}
	public BigDecimal getRefAmt() {
		return refAmt;
	}
	public void setRefAmt(BigDecimal refAmt) {
		this.refAmt = refAmt;
	}
	public BigDecimal getRefInte() {
		return refInte;
	}
	public void setRefInte(BigDecimal refInte) {
		this.refInte = refInte;
	}
	public BigDecimal getRefOver() {
		return refOver;
	}
	public void setRefOver(BigDecimal refOver) {
		this.refOver = refOver;
	}
	public BigDecimal getRefFee() {
		return refFee;
	}
	public void setRefFee(BigDecimal refFee) {
		this.refFee = refFee;
	}
	public String getDealSts() {
		return dealSts;
	}
	public void setDealSts(String dealSts) {
		this.dealSts = dealSts;
	}
	public String getDealDesc() {
		return dealDesc;
	}
	public void setDealDesc(String dealDesc) {
		this.dealDesc = dealDesc;
	}
}
