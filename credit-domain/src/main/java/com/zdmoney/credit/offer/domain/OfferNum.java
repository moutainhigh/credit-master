package com.zdmoney.credit.offer.domain;

import java.util.Date;
import com.zdmoney.credit.framework.domain.BaseDomain;

public class OfferNum extends BaseDomain{
    private Long id;
    
    private String name;
    
    private String userCode;

	private String offerCount;

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


    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getOfferCount() {
		return offerCount;
	}

	public void setOfferCount(String offerCount) {
		this.offerCount = offerCount;
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
		this.creator = creator;
	}

	public String getUpdator() {
		return updator;
	}

	public void setUpdator(String updator) {
		this.updator = updator;
	}

}