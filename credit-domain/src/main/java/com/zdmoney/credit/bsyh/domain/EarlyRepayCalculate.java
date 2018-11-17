package com.zdmoney.credit.bsyh.domain;

import java.math.BigDecimal;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class EarlyRepayCalculate extends BaseDomain{
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String orderNo;
	private String respcd;
	private String resptx;
	private BigDecimal installTotalAmt;
	private BigDecimal repayBaseAmt;
	private BigDecimal repyam;
	private BigDecimal installTotalInterest;
	private BigDecimal disCountInterest;
	private Long repayPeriod;
	private Long remainRepayTimes;
	private BigDecimal liqdaRatio;
	private BigDecimal liqdaAmount;
	private BigDecimal earlyRepaymentAmt;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getRespcd() {
		return respcd;
	}
	public void setRespcd(String respcd) {
		this.respcd = respcd;
	}
	public String getResptx() {
		return resptx;
	}
	public void setResptx(String resptx) {
		this.resptx = resptx;
	}
	public BigDecimal getInstallTotalAmt() {
		return installTotalAmt;
	}
	public void setInstallTotalAmt(BigDecimal installTotalAmt) {
		this.installTotalAmt = installTotalAmt;
	}
	public BigDecimal getRepayBaseAmt() {
		return repayBaseAmt;
	}
	public void setRepayBaseAmt(BigDecimal repayBaseAmt) {
		this.repayBaseAmt = repayBaseAmt;
	}
	public BigDecimal getRepyam() {
		return repyam;
	}
	public void setRepyam(BigDecimal repyam) {
		this.repyam = repyam;
	}
	public BigDecimal getInstallTotalInterest() {
		return installTotalInterest;
	}
	public void setInstallTotalInterest(BigDecimal installTotalInterest) {
		this.installTotalInterest = installTotalInterest;
	}
	public BigDecimal getDisCountInterest() {
		return disCountInterest;
	}
	public void setDisCountInterest(BigDecimal disCountInterest) {
		this.disCountInterest = disCountInterest;
	}
	public Long getRepayPeriod() {
		return repayPeriod;
	}
	public void setRepayPeriod(Long repayPeriod) {
		this.repayPeriod = repayPeriod;
	}
	public Long getRemainRepayTimes() {
		return remainRepayTimes;
	}
	public void setRemainRepayTimes(Long remainRepayTimes) {
		this.remainRepayTimes = remainRepayTimes;
	}
	public BigDecimal getLiqdaRatio() {
		return liqdaRatio;
	}
	public void setLiqdaRatio(BigDecimal liqdaRatio) {
		this.liqdaRatio = liqdaRatio;
	}
	public BigDecimal getLiqdaAmount() {
		return liqdaAmount;
	}
	public void setLiqdaAmount(BigDecimal liqdaAmount) {
		this.liqdaAmount = liqdaAmount;
	}
	public BigDecimal getEarlyRepaymentAmt() {
		return earlyRepaymentAmt;
	}
	public void setEarlyRepaymentAmt(BigDecimal earlyRepaymentAmt) {
		this.earlyRepaymentAmt = earlyRepaymentAmt;
	}
	
	
}
