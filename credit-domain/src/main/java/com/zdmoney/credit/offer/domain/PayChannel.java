package com.zdmoney.credit.offer.domain;

import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class PayChannel extends BaseDomain{
    /**
	 * 
	 */
	private static final long serialVersionUID = -6959589527846440112L;

	private Long id;

    private String fundsSources;

    private String paySysNo;

    private String state;

    private Date createTime;

    private Date updateTime;

    private String creator;

    private String updator;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFundsSources() {
        return fundsSources;
    }

    public void setFundsSources(String fundsSources) {
        this.fundsSources = fundsSources == null ? null : fundsSources.trim();
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