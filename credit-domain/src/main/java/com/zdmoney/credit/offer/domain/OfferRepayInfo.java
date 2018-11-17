package com.zdmoney.credit.offer.domain;


import java.math.BigDecimal;
import java.util.Date;

import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.framework.domain.BaseDomain;
import com.zdmoney.credit.loan.domain.LoanSpecialRepayment;

/**
 * 凭证表，记录一个债权的所有资金变动
 * @author 00232949
 *
 */
public class OfferRepayInfo extends BaseDomain{
    /**
	 * 
	 */
	private static final long serialVersionUID = -1857617632775534927L;

	private Long id;
	
	private Long LoanId;

    private BigDecimal amount;

    private String delegationTeller;

    private String memo;

    private Long offerid;

    private String organ;

    private String reversedNo;

    private String teller;

    private String tradeCode;

    private Date tradeDate;

    private String tradeKind;

    private String tradeNo;

    private String tradeType;

    private String voucherCode;

    private String voucherKind;

    private Date tradeTime;

    private Date updateTime;

    private Date createTime;
    
    
    
    //==========以下非数据库字段===================
    private LoanSpecialRepayment reductionSpecialRepayment;//特殊还款,存放减免的特殊还款

    private boolean isOverdue = false; //用于信托分账 是否逾期标示
    private boolean isHistory = false; //用于信托分账，是否历史还款记录
    private boolean isRelief = false;//用于信托分账，是否减免
    
    /**
     * 是否生成划扣队列数据（陆金所使用）
     */
    private boolean isCreateDebitQueue = true;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDelegationTeller() {
        return delegationTeller;
    }

    public void setDelegationTeller(String delegationTeller) {
        this.delegationTeller = delegationTeller == null ? null : delegationTeller.trim();
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    public Long getOfferid() {
        return offerid;
    }

    public void setOfferid(Long offerid) {
        this.offerid = offerid;
    }

    public String getOrgan() {
        return organ;
    }

    public void setOrgan(String organ) {
        this.organ = organ == null ? null : organ.trim();
    }

    public String getReversedNo() {
        return reversedNo;
    }

    public void setReversedNo(String reversedNo) {
        this.reversedNo = reversedNo == null ? null : reversedNo.trim();
    }

    public String getTeller() {
        return teller;
    }

    public void setTeller(String teller) {
        this.teller = teller == null ? null : teller.trim();
    }

    public String getTradeCode() {
        return tradeCode;
    }

    public void setTradeCode(String tradeCode) {
        this.tradeCode = tradeCode == null ? null : tradeCode.trim();
    }

    public Date getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(Date tradeDate) {
    	if(tradeDate!=null){
    		this.tradeDate = Dates.parse(Dates.getDateTime(tradeDate, Dates.DEFAULT_DATE_FORMAT), Dates.DEFAULT_DATE_FORMAT);
    		this.tradeTime = tradeDate;
    	}
    	
    }

    public String getTradeKind() {
        return tradeKind;
    }

    public void setTradeKind(String tradeKind) {
        this.tradeKind = tradeKind == null ? null : tradeKind.trim();
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo == null ? null : tradeNo.trim();
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType == null ? null : tradeType.trim();
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode == null ? null : voucherCode.trim();
    }

    public String getVoucherKind() {
        return voucherKind;
    }

    public void setVoucherKind(String voucherKind) {
        this.voucherKind = voucherKind == null ? null : voucherKind.trim();
    }

    public Date getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(Date tradeTime) {
        this.tradeTime = tradeTime;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getLoanId() {
		return LoanId;
	}

	public void setLoanId(Long loanId) {
		LoanId = loanId;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public LoanSpecialRepayment getReductionSpecialRepayment() {
		return reductionSpecialRepayment;
	}

	public void setReductionSpecialRepayment(LoanSpecialRepayment loanSpecialRepayment) {
		this.reductionSpecialRepayment = loanSpecialRepayment;
	}

    public boolean isOverdue() {
        return isOverdue;
    }

    public void setIsOverdue(boolean isOverdue) {
        this.isOverdue = isOverdue;
    }

    public boolean isHistory() {
        return isHistory;
    }

    public void setIsHistory(boolean isHistory) {
        this.isHistory = isHistory;
    }

    public boolean isRelief() {
        return isRelief;
    }

    public void setIsRelief(boolean isRelief) {
        this.isRelief = isRelief;
    }

	public boolean isCreateDebitQueue() {
		return isCreateDebitQueue;
	}

	public void setCreateDebitQueue(boolean isCreateDebitQueue) {
		this.isCreateDebitQueue = isCreateDebitQueue;
	}
}