package com.zdmoney.credit.bsyh.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;
/**
 * 放款对账明细
 * @author YM10104
 *
 */
public class GrantRecDetail extends BaseDomain{
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String orderNo;
	private String chanlNo;
	private BigDecimal amt;
	private Date grantDate;
	private Long totalTerm;
	private String productNo;
	private Date latestRepayDate;//最迟还款日
	private Long totalNum;
	private BigDecimal totalAmt;
	private String fileName;
	private Date createDate;
	private String state;
	private BigDecimal grantMoney;
	
	public BigDecimal getGrantMoney() {
		return grantMoney;
	}
	public void setGrantMoney(BigDecimal grantMoney) {
		this.grantMoney = grantMoney;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
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
	public String getChanlNo() {
		return chanlNo;
	}
	public void setChanlNo(String chanlNo) {
		this.chanlNo = chanlNo;
	}
	public BigDecimal getAmt() {
		return amt;
	}
	public void setAmt(BigDecimal amt) {
		this.amt = amt;
	}
	public Date getGrantDate() {
		return grantDate;
	}
	public void setGrantDate(Date grantDate) {
		this.grantDate = grantDate;
	}
	public Long getTotalTerm() {
		return totalTerm;
	}
	public void setTotalTerm(Long totalTerm) {
		this.totalTerm = totalTerm;
	}
	public String getProductNo() {
		return productNo;
	}
	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}
	public Date getLatestRepayDate() {
		return latestRepayDate;
	}
	public void setLatestRepayDate(Date latestRepayDate) {
		this.latestRepayDate = latestRepayDate;
	}
	public Long getTotalNum() {
		return totalNum;
	}
	public void setTotalnNum(Long totalNum) {
		this.totalNum = totalNum;
	}
	public BigDecimal getTotalAmt() {
		return totalAmt;
	}
	public void setTotalAmt(BigDecimal totalAmt) {
		this.totalAmt = totalAmt;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	
}
