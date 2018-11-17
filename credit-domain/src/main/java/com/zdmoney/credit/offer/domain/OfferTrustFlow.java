package com.zdmoney.credit.offer.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

/**
 * 债权分账信息表
 * @author wangn  2016年10月11日 下午4:48:39
 *
 */
public class OfferTrustFlow extends BaseDomain{


	/**
	 * 
	 */
	private static final long serialVersionUID = 8097586913398571092L;

	private	Long id;
	
	private Long loanId;
	
	private String tradeNo;
	
	private Date tradeDate;
	
	private String tradeType;
	
	private String repaymentType;
	
	private Long currentTerm;
	
	private BigDecimal actualPrincipal;
	
	private BigDecimal reliefPrincipal;
	
	private BigDecimal actualInterest;
	
	private BigDecimal reliefInterest;
	
	private BigDecimal actualFine;
	
	private BigDecimal reliefFine;
	
	private BigDecimal actualPenalty;
	
	private BigDecimal reliefPenalty;
	
	private BigDecimal giveBackService;
	
	private BigDecimal giveBackPrincipal;
	
	private BigDecimal giveBackInterest;
	
	private BigDecimal actualHandling;
	
	private BigDecimal reliefHandling;
	
	private BigDecimal actualGuarantee;
	
	private BigDecimal reliefGuarantee;
	
	private BigDecimal actualService;
	
	private BigDecimal reliefService;
	
	private Date actualTradeDate;
	
	private BigDecimal actualFee1;
	
	private BigDecimal reliefFee1;
	
	private BigDecimal actualFee2;
	
	private BigDecimal reliefFee2;
	
	private BigDecimal actualFee3;
	
	private BigDecimal reliefFee3;
	
	private BigDecimal thirdPaySerial;
	
	private Date returnDate;
	
	private Long overdueDay;
	
	private BigDecimal deficit;

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

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public Date getTradeDate() {
		return tradeDate;
	}

	public void setTradeDate(Date tradeDate) {
		this.tradeDate = tradeDate;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getRepaymentType() {
		return repaymentType;
	}

	public void setRepaymentType(String repaymentType) {
		this.repaymentType = repaymentType;
	}

	public Long getCurrentTerm() {
		return currentTerm;
	}

	public void setCurrentTerm(Long currentTerm) {
		this.currentTerm = currentTerm;
	}

	public BigDecimal getActualPrincipal() {
		return actualPrincipal;
	}

	public void setActualPrincipal(BigDecimal actualPrincipal) {
		this.actualPrincipal = actualPrincipal;
	}

	public BigDecimal getReliefPrincipal() {
		return reliefPrincipal;
	}

	public void setReliefPrincipal(BigDecimal reliefPrincipal) {
		this.reliefPrincipal = reliefPrincipal;
	}

	public BigDecimal getActualInterest() {
		return actualInterest;
	}

	public void setActualInterest(BigDecimal actualInterest) {
		this.actualInterest = actualInterest;
	}

	public BigDecimal getReliefInterest() {
		return reliefInterest;
	}

	public void setReliefInterest(BigDecimal reliefInterest) {
		this.reliefInterest = reliefInterest;
	}

	public BigDecimal getActualFine() {
		return actualFine;
	}

	public void setActualFine(BigDecimal actualFine) {
		this.actualFine = actualFine;
	}

	public BigDecimal getReliefFine() {
		return reliefFine;
	}

	public void setReliefFine(BigDecimal reliefFine) {
		this.reliefFine = reliefFine;
	}

	public BigDecimal getActualPenalty() {
		return actualPenalty;
	}

	public void setActualPenalty(BigDecimal actualPenalty) {
		this.actualPenalty = actualPenalty;
	}

	public BigDecimal getReliefPenalty() {
		return reliefPenalty;
	}

	public void setReliefPenalty(BigDecimal reliefPenalty) {
		this.reliefPenalty = reliefPenalty;
	}

	public BigDecimal getGiveBackService() {
		return giveBackService;
	}

	public void setGiveBackService(BigDecimal giveBackService) {
		this.giveBackService = giveBackService;
	}

	public BigDecimal getGiveBackPrincipal() {
		return giveBackPrincipal;
	}

	public void setGiveBackPrincipal(BigDecimal giveBackPrincipal) {
		this.giveBackPrincipal = giveBackPrincipal;
	}

	public BigDecimal getGiveBackInterest() {
		return giveBackInterest;
	}

	public void setGiveBackInterest(BigDecimal giveBackInterest) {
		this.giveBackInterest = giveBackInterest;
	}

	public BigDecimal getActualHandling() {
		return actualHandling;
	}

	public void setActualHandling(BigDecimal actualHandling) {
		this.actualHandling = actualHandling;
	}

	public BigDecimal getReliefHandling() {
		return reliefHandling;
	}

	public void setReliefHandling(BigDecimal reliefHandling) {
		this.reliefHandling = reliefHandling;
	}

	public BigDecimal getActualGuarantee() {
		return actualGuarantee;
	}

	public void setActualGuarantee(BigDecimal actualGuarantee) {
		this.actualGuarantee = actualGuarantee;
	}

	public BigDecimal getReliefGuarantee() {
		return reliefGuarantee;
	}

	public void setReliefGuarantee(BigDecimal reliefGuarantee) {
		this.reliefGuarantee = reliefGuarantee;
	}

	public BigDecimal getActualService() {
		return actualService;
	}

	public void setActualService(BigDecimal actualService) {
		this.actualService = actualService;
	}

	public BigDecimal getReliefService() {
		return reliefService;
	}

	public void setReliefService(BigDecimal reliefService) {
		this.reliefService = reliefService;
	}

	public Date getActualTradeDate() {
		return actualTradeDate;
	}

	public void setActualTradeDate(Date actualTradeDate) {
		this.actualTradeDate = actualTradeDate;
	}

	public BigDecimal getActualFee1() {
		return actualFee1;
	}

	public void setActualFee1(BigDecimal actualFee1) {
		this.actualFee1 = actualFee1;
	}

	public BigDecimal getReliefFee1() {
		return reliefFee1;
	}

	public void setReliefFee1(BigDecimal reliefFee1) {
		this.reliefFee1 = reliefFee1;
	}

	public BigDecimal getActualFee2() {
		return actualFee2;
	}

	public void setActualFee2(BigDecimal actualFee2) {
		this.actualFee2 = actualFee2;
	}

	public BigDecimal getReliefFee2() {
		return reliefFee2;
	}

	public void setReliefFee2(BigDecimal reliefFee2) {
		this.reliefFee2 = reliefFee2;
	}

	public BigDecimal getActualFee3() {
		return actualFee3;
	}

	public void setActualFee3(BigDecimal actualFee3) {
		this.actualFee3 = actualFee3;
	}

	public BigDecimal getReliefFee3() {
		return reliefFee3;
	}

	public void setReliefFee3(BigDecimal reliefFee3) {
		this.reliefFee3 = reliefFee3;
	}

	public BigDecimal getThirdPaySerial() {
		return thirdPaySerial;
	}

	public void setThirdPaySerial(BigDecimal thirdPaySerial) {
		this.thirdPaySerial = thirdPaySerial;
	}

	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public Long getOverdueDay() {
		return overdueDay;
	}

	public void setOverdueDay(Long overdueDay) {
		this.overdueDay = overdueDay;
	}

	public BigDecimal getDeficit() {
		return deficit;
	}

	public void setDeficit(BigDecimal deficit) {
		this.deficit = deficit;
	}
	
	
}
