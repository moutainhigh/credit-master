package com.zdmoney.credit.bsyh.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;
/**
 * 放款对账明细
 * @author YM10104
 *
 */
public class RepayRecDetail extends BaseDomain{
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String orderNo;
	private String chanlNo;
	private Long term;
	private BigDecimal tradeAmt;
	private BigDecimal totalInterest;
	private BigDecimal totalFeeAmt;
	private String repayType;
	private Date tradeDate;
	private String fileName;
	private Date createDate;
	private Long totalNum;
	private BigDecimal totalAmt;
	private String state;
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
	public Long getTerm() {
		return term;
	}
	public void setTerm(Long term) {
		this.term = term;
	}
	public BigDecimal getTradeAmt() {
		return tradeAmt;
	}
	public void setTradeAmt(BigDecimal tradeAmt) {
		this.tradeAmt = tradeAmt;
	}
	public BigDecimal getTotalInterest() {
		return totalInterest;
	}
	public void setTotalInterest(BigDecimal totalInterest) {
		this.totalInterest = totalInterest;
	}
	public BigDecimal getTotalFeeAmt() {
		return totalFeeAmt;
	}
	public void setTotalFeeAmt(BigDecimal totalFeeAmt) {
		this.totalFeeAmt = totalFeeAmt;
	}
	public String getRepayType() {
		return repayType;
	}
	public void setRepayType(String repayType) {
		this.repayType = repayType;
	}
	public Date getTradeDate() {
		return tradeDate;
	}
	public void setTradeDate(Date tradeDate) {
		this.tradeDate = tradeDate;
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
	public Long getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(Long totalNum) {
		this.totalNum = totalNum;
	}
	public BigDecimal getTotalAmt() {
		return totalAmt;
	}
	public void setTotalAmt(BigDecimal totalAmt) {
		this.totalAmt = totalAmt;
	}
	
	
}
