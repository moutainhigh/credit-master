package com.zdmoney.credit.excel.vo;

import java.math.BigDecimal;
import java.util.List;

import com.zdmoney.credit.loan.domain.LoanRepaymentDetail;

public class VExcelInfo {
	/**
	 * 合同金额
	 */
	private BigDecimal pactMoney;
	/**
	 * 风险金
	 */
	private BigDecimal risk;
	/**
	 * 咨询费
	 */
	private BigDecimal referRate;
	/**
	 * 评估费
	 */
	private BigDecimal evalRate;
	/**
	 *管理费 
	 */
	private BigDecimal manageRate;
	/**
	 * 月利率
	 */
	private BigDecimal rateem;
	/**
	 * 年利率
	 */
	private BigDecimal rateey;
	/**
	 * 日利率
	 */
	private BigDecimal rateed;
	/**
	 * 逾期罚息
	 */
	private BigDecimal penaltyRate;
	/**
	 * 还款计划明细
	 */
	private List<LoanRepaymentDetail>  excelDetailList;
	/**
	 * 丙方管理费
	 */
	private BigDecimal  manageRateForPartyC;	
	
	public BigDecimal getManageRateForPartyC() {
		return manageRateForPartyC;
	}

	public void setManageRateForPartyC(BigDecimal manageRateForPartyC) {
		this.manageRateForPartyC = manageRateForPartyC;
	}
	
	public BigDecimal getPenaltyRate() {
		return penaltyRate;
	}

	public void setPenaltyRate(BigDecimal penaltyRate) {
		this.penaltyRate = penaltyRate;
	}

	public List<LoanRepaymentDetail> getExcelDetailList() {
		return excelDetailList;
	}

	public void setExcelDetailList(List<LoanRepaymentDetail> excelDetailList) {
		this.excelDetailList = excelDetailList;
	}

	public BigDecimal getPactMoney() {
		return pactMoney;
	}

	public void setPactMoney(BigDecimal pactMoney) {
		this.pactMoney = pactMoney;
	}

	public BigDecimal getRisk() {
		return risk;
	}

	public void setRisk(BigDecimal risk) {
		this.risk = risk;
	}
	
	public BigDecimal getReferRate() {
		return referRate;
	}

	public void setReferRate(BigDecimal referRate) {
		this.referRate = referRate;
	}

	public BigDecimal getEvalRate() {
		return evalRate;
	}

	public void setEvalRate(BigDecimal evalRate) {
		this.evalRate = evalRate;
	}

	public BigDecimal getManageRate() {
		return manageRate;
	}

	public void setManageRate(BigDecimal manageRate) {
		this.manageRate = manageRate;
	}

	public BigDecimal getRateem() {
		return rateem;
	}

	public void setRateem(BigDecimal rateem) {
		this.rateem = rateem;
	}

	public BigDecimal getRateey() {
		return rateey;
	}

	public void setRateey(BigDecimal rateey) {
		this.rateey = rateey;
	}

	public BigDecimal getRateed() {
		return rateed;
	}

	public void setRateed(BigDecimal rateed) {
		this.rateed = rateed;
	}
	
	
}
