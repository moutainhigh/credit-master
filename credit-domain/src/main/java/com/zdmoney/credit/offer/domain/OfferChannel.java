package com.zdmoney.credit.offer.domain;

import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class OfferChannel extends BaseDomain{
    private Long id;

    private String paySysNo;

    private String state;

    private String ext1;

    private String ext2;

    private String ext3;

    private String ext4;

    private Date createTime;

    private Date updateTime;

    private String creator;

    private String updator;

    private String loanBelong;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPaySysNo() {
        return paySysNo;
    }

    public void setPaySysNo(String paySysNo) {
        this.paySysNo = paySysNo == null ? null : paySysNo.trim();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    public String getExt1() {
        return ext1;
    }

    public void setExt1(String ext1) {
        this.ext1 = ext1 == null ? null : ext1.trim();
    }

    public String getExt2() {
        return ext2;
    }

    public void setExt2(String ext2) {
        this.ext2 = ext2 == null ? null : ext2.trim();
    }

    public String getExt3() {
        return ext3;
    }

    public void setExt3(String ext3) {
        this.ext3 = ext3 == null ? null : ext3.trim();
    }

    public String getExt4() {
        return ext4;
    }

    public void setExt4(String ext4) {
        this.ext4 = ext4 == null ? null : ext4.trim();
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

    public String getLoanBelong() {
        return loanBelong;
    }

    public void setLoanBelong(String loanBelong) {
        this.loanBelong = loanBelong == null ? null : loanBelong.trim();
    }
}