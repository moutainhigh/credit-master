package com.zdmoney.credit.flow.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by ym10094 on 2017/8/16.
 */
public class SpQueryFlowChartParamVo implements Serializable{

    private static final long serialVersionUID = -5073281424479269400L;

    private String OverDueDate;

    private String applyType;

    private boolean isRuleIn;

    private BigDecimal applyReliefAmount;

    private BigDecimal maxReliefAmount;

    private BigDecimal fine;

    private  BigDecimal ruleInMaxReliefAmount;

    public String getOverDueDate() {
        return OverDueDate;
    }

    public void setOverDueDate(String overDueDate) {
        OverDueDate = overDueDate;
    }

    public String getApplyType() {
        return applyType;
    }

    public void setApplyType(String applyType) {
        this.applyType = applyType;
    }

    public boolean isRuleIn() {
        return isRuleIn;
    }

    public void setIsRuleIn(boolean isRuleIn) {
        this.isRuleIn = isRuleIn;
    }

    public BigDecimal getMaxReliefAmount() {
        return maxReliefAmount;
    }

    public void setMaxReliefAmount(BigDecimal maxReliefAmount) {
        this.maxReliefAmount = maxReliefAmount;
    }

    public BigDecimal getApplyReliefAmount() {
        return applyReliefAmount;
    }

    public void setApplyReliefAmount(BigDecimal applyReliefAmount) {
        this.applyReliefAmount = applyReliefAmount;
    }

    public BigDecimal getFine() {
        return fine;
    }

    public void setFine(BigDecimal fine) {
        this.fine = fine;
    }

    public BigDecimal getRuleInMaxReliefAmount() {
        return ruleInMaxReliefAmount;
    }

    public void setRuleInMaxReliefAmount(BigDecimal ruleInMaxReliefAmount) {
        this.ruleInMaxReliefAmount = ruleInMaxReliefAmount;
    }
}
