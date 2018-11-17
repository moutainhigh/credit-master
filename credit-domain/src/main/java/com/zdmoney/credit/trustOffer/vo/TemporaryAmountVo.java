package com.zdmoney.credit.trustOffer.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;
/**
 * 暂收款报表
 * @author user
 *
 */
public class TemporaryAmountVo extends BaseDomain{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8420750341862161507L;

	private Long id;
	
	private String projectCode;
	
	private String contractNum;
	
	private Date tradeDate;
	
	private BigDecimal temporaryIncome;
	
	private BigDecimal temporaryInterest;
	
	private BigDecimal temporaryPenalty;
	
	private BigDecimal temporaryOtherAmount;
	
	private Date offDate;

	private String tradeDateS;
	
	private String offDateS;
	/**
	 * 备注
	 */
	private String memo;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public String getContractNum() {
		return contractNum;
	}

	public void setContractNum(String contractNum) {
		this.contractNum = contractNum;
	}

	public Date getTradeDate() {
		return tradeDate;
	}

	public void setTradeDate(Date tradeDate) {
		this.tradeDate = tradeDate;
	}

	public BigDecimal getTemporaryIncome() {
		return temporaryIncome;
	}

	public void setTemporaryIncome(BigDecimal temporaryIncome) {
		this.temporaryIncome = temporaryIncome;
	}

	public BigDecimal getTemporaryInterest() {
		return temporaryInterest;
	}

	public void setTemporaryInterest(BigDecimal temporaryInterest) {
		this.temporaryInterest = temporaryInterest;
	}

	public BigDecimal getTemporaryPenalty() {
		return temporaryPenalty;
	}

	public void setTemporaryPenalty(BigDecimal temporaryPenalty) {
		this.temporaryPenalty = temporaryPenalty;
	}

	public BigDecimal getTemporaryOtherAmount() {
		return temporaryOtherAmount;
	}

	public void setTemporaryOtherAmount(BigDecimal temporaryOtherAmount) {
		this.temporaryOtherAmount = temporaryOtherAmount;
	}

	public Date getOffDate() {
		return offDate;
	}

	public void setOffDate(Date offDate) {
		this.offDate = offDate;
	}

	public String getTradeDateS() {
		return tradeDateS;
	}

	public void setTradeDateS(String tradeDateS) {
		this.tradeDateS = tradeDateS;
	}

	public String getOffDateS() {
		return offDateS;
	}

	public void setOffDateS(String offDateS) {
		this.offDateS = offDateS;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}
}
