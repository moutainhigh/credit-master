package com.zdmoney.credit.repay.vo;

import com.zdmoney.credit.framework.domain.BaseDomain;

import java.math.BigDecimal;
import java.util.Date;

public class ApplyBookInfo extends BaseDomain {
    private static final long serialVersionUID = 9017446798934975960L;

    private long id;

    private String fileName;

    private String batchNum;

    private BigDecimal grantMoney;

    private int loanTimes;

    private BigDecimal diffMoney;

    private BigDecimal applyMoney;

    private String remark;

    private String mark1;

    private String mark2;

    private Date createTime;

    private Date updateTime;

    private String creator;

    private String updator;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName == null ? null : fileName.trim();
    }

    public String getBatchNum() {
        return batchNum;
    }

    public void setBatchNum(String batchNum) {
        this.batchNum = batchNum == null ? null : batchNum.trim();
    }

    public BigDecimal getGrantMoney() {
        return grantMoney;
    }

    public void setGrantMoney(BigDecimal grantMoney) {
        this.grantMoney = grantMoney;
    }

    public int getLoanTimes() {
        return loanTimes;
    }

    public void setLoanTimes(int loanTimes) {
        this.loanTimes = loanTimes;
    }

    public BigDecimal getDiffMoney() {
        return diffMoney;
    }

    public void setDiffMoney(BigDecimal diffMoney) {
        this.diffMoney = diffMoney;
    }

    public BigDecimal getApplyMoney() {
        return applyMoney;
    }

    public void setApplyMoney(BigDecimal applyMoney) {
        this.applyMoney = applyMoney;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getMark1() {
        return mark1;
    }

    public void setMark1(String mark1) {
        this.mark1 = mark1 == null ? null : mark1.trim();
    }

    public String getMark2() {
        return mark2;
    }

    public void setMark2(String mark2) {
        this.mark2 = mark2 == null ? null : mark2.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public String getUpdator() {
        return updator;
    }

    public void setUpdator(String updator) {
        this.updator = updator == null ? null : updator.trim();
    }
}