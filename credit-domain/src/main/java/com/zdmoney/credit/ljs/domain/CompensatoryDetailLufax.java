/**
 * 
 */
package com.zdmoney.credit.ljs.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

/**
 * @ClassName:     CompensatoryDetailLufax.java
 * @Description:   
 * @author         柳云华
 * @version        V1.0  
 * @Since          JDK 1.7
 * @Date           2017年6月9日 下午6:39:24
 */
public class CompensatoryDetailLufax extends BaseDomain{

    private static final long serialVersionUID = 1L;
    
    private Long id;
    
    private Long loanId;
    
    private Integer term;
    
    private String type;
    
    private Date tradeDate;
    
    private BigDecimal totalAmount;
    
    private BigDecimal corpusAmount;
    
    private BigDecimal accrualAmount;
    
    private BigDecimal penaltyAmount;
    
    private BigDecimal cleanAmount;
    
    private Long debitQueueId;
    
    private String memo;
    
    private Date createTime;
    
    private String creator;
    
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

    public Integer getTerm() {
        return term;
    }

    public void setTerm(Integer term) {
        this.term = term;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(Date tradeDate) {
        this.tradeDate = tradeDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getCorpusAmount() {
        return corpusAmount;
    }

    public void setCorpusAmount(BigDecimal corpusAmount) {
        this.corpusAmount = corpusAmount;
    }

    public BigDecimal getAccrualAmount() {
        return accrualAmount;
    }

    public void setAccrualAmount(BigDecimal accrualAmount) {
        this.accrualAmount = accrualAmount;
    }

    public BigDecimal getPenaltyAmount() {
        return penaltyAmount;
    }

    public void setPenaltyAmount(BigDecimal penaltyAmount) {
        this.penaltyAmount = penaltyAmount;
    }

    public BigDecimal getCleanAmount() {
        return cleanAmount;
    }

    public void setCleanAmount(BigDecimal cleanAmount) {
        this.cleanAmount = cleanAmount;
    }

    public Long getDebitQueueId() {
        return debitQueueId;
    }

    public void setDebitQueueId(Long debitQueueId) {
        this.debitQueueId = debitQueueId;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
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
        this.creator = creator;
    }

}
