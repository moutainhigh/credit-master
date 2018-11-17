package com.zdmoney.credit.offer.domain;

import com.zdmoney.credit.framework.domain.BaseDomain;

/**
 * 报盘配置实体类
 * @author 00236640
 *
 */
public class OfferConfig extends BaseDomain {
    
    private static final long serialVersionUID = 7173134386909569957L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 执行类型（0：生成正常还款报盘，1：生成逾期还款报盘，2：生成特殊还款报盘，3：发送报盘）
     */
    private String execType;

    /**
     * 执行日（1~31）
     */
    private Long execDay;

    /**
     * 执行时间
     */
    private String execTime;

    /**
     * 划扣类型
     */
    private String debitType;

    /**
     * 划扣描述
     */
    private String debitDesc;

    /**
     * 备注
     */
    private String memo;

    /**
     * 是否有效（t:有效，f:无效）
     */
    private String isValid;
    
    /**
     * 划扣合同来源，多个以逗号隔开
     */
    private String fundsSources;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExecType() {
        return execType;
    }

    public void setExecType(String execType) {
        this.execType = execType;
    }

    public Long getExecDay() {
        return execDay;
    }

    public void setExecDay(Long execDay) {
        this.execDay = execDay;
    }

    public String getExecTime() {
        return execTime;
    }

    public void setExecTime(String execTime) {
        this.execTime = execTime;
    }

    public String getDebitType() {
        return debitType;
    }

    public void setDebitType(String debitType) {
        this.debitType = debitType;
    }

    public String getDebitDesc() {
        return debitDesc;
    }

    public void setDebitDesc(String debitDesc) {
        this.debitDesc = debitDesc;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getIsValid() {
        return isValid;
    }

    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }

    public String getFundsSources() {
        return fundsSources;
    }

    public void setFundsSources(String fundsSources) {
        this.fundsSources = fundsSources;
    }
}