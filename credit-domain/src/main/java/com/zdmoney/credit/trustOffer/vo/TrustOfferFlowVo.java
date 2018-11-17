package com.zdmoney.credit.trustOffer.vo;

import java.math.BigDecimal;

import com.zdmoney.credit.trustOffer.domain.TrustOfferFlow;

/**
 * Created by ym10094 on 2016/12/2.
 */
public class TrustOfferFlowVo extends TrustOfferFlow {

    /**
     * 本金
     */
    private BigDecimal principal =new BigDecimal("0.00");
    /**
     * 利息
     */
    private BigDecimal interest = new BigDecimal("0.00");
    /**
     * 罚息
     */
    private  BigDecimal fineInterest = new BigDecimal("0.00");
    /**
     * 减免罚息
     */
    private BigDecimal reliefFineInterest = new BigDecimal("0.00");
    /**
     * 总金额
     */
    private BigDecimal amount = new BigDecimal("0.00");
    /**
     * 一次性结清 最后哪一期
     */
    private Integer lastTerm;
    /**
     * 一次性结清 最开始的哪一期
     */
    private Integer startTerm;
    
    private String fundsSources;

    private String loanBelongs;


    public BigDecimal getPrincipal() {
        return principal;
    }

    public void setPrincipal(BigDecimal principal) {
        this.principal = principal;
    }

    public BigDecimal getInterest() {
        return interest;
    }

    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }

    public BigDecimal getFineInterest() {
        return fineInterest;
    }

    public void setFineInterest(BigDecimal fineInterest) {
        this.fineInterest = fineInterest;
    }

    public BigDecimal getReliefFineInterest() {
        return reliefFineInterest;
    }

    public void setReliefFineInterest(BigDecimal reliefFineInterest) {
        this.reliefFineInterest = reliefFineInterest;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getLastTerm() {
        return lastTerm;
    }

    public void setLastTerm(Integer lastTerm) {
        this.lastTerm = lastTerm;
    }

    public Integer getStartTerm() {
        return startTerm;
    }

    public void setStartTerm(Integer startTerm) {
        this.startTerm = startTerm;
    }

	public String getFundsSources() {
		return fundsSources;
	}

	public void setFundsSources(String fundsSources) {
		this.fundsSources = fundsSources;
	}

	public String getLoanBelongs() {
		return loanBelongs;
	}

	public void setLoanBelongs(String loanBelongs) {
		this.loanBelongs = loanBelongs;
	}
}
