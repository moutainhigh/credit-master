package com.zdmoney.credit.framework.domain;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONType;
import com.zdmoney.credit.common.util.Pager;

/**
 * 实体基类，所有的业务实体必须继承该实体
 *
 * @author Ivan
 */
@JSONType(ignores = {"pager"})
public class BaseDomain implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3163417642726959621L;
	
	/** 分页对象 **/ 
	private Pager pager = new Pager();
	
	/** 创建人 **/
	private String creator;
	/** 更新人 **/
	private String updator;
	/** 创建时间 **/
	private Date createTime;
	/** 更新时间 **/
	private Date updateTime;

	public Pager getPager() {
		return pager;
	}

	public void setPager(Pager pager) {
		this.pager = pager;
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
	
}
