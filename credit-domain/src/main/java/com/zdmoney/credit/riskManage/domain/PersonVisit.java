package com.zdmoney.credit.riskManage.domain;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class PersonVisit extends BaseDomain{
    
    private static final long serialVersionUID = 1776662414392240659L;

    /**
     * 主键Id
     */
    private Long id;
    
    /**
     * 额外费用 
     */
    private String additionalCharges;
    
    /**
     * 渠道
     */
    private String channel;
    
    /**
     * 债权Id
     */
    private Long loanId;
    
    /**
     * 备注
     */
    private String memo;
    
    /**
     * 操作人Id
     */
    private Long creatorId;
    
    /**
     * 服务态度 
     */
    private String sAttitude;
    
    /**
     * 电话
     */
    private String tel;
    
    /**
     * 建议
     */
    private String advice;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdditionalCharges() {
        return additionalCharges;
    }

    public void setAdditionalCharges(String additionalCharges) {
        this.additionalCharges = additionalCharges;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getsAttitude() {
        return sAttitude;
    }

    public void setsAttitude(String sAttitude) {
        this.sAttitude = sAttitude;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }
}