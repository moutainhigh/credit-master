package com.zdmoney.credit.system.domain;

import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class SysFieldMapper extends BaseDomain {

	private static final long serialVersionUID = 1L;
	
	private Long id;

    private String coreNameJava;

    private String apsNameJava;

    private String ctsNameSql;

    private String dictName;

    private String apsNameJson;

    private String coreNameJson;

    private String domainName;

    private String type;

    private String memo;

    private Date createTime;

    private Date updateTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCoreNameJava() {
		return coreNameJava;
	}

	public void setCoreNameJava(String coreNameJava) {
		this.coreNameJava = coreNameJava;
	}

	public String getApsNameJava() {
		return apsNameJava;
	}

	public void setApsNameJava(String apsNameJava) {
		this.apsNameJava = apsNameJava;
	}

	public String getCtsNameSql() {
		return ctsNameSql;
	}

	public void setCtsNameSql(String ctsNameSql) {
		this.ctsNameSql = ctsNameSql;
	}

	public String getDictName() {
		return dictName;
	}

	public void setDictName(String dictName) {
		this.dictName = dictName;
	}

	public String getApsNameJson() {
		return apsNameJson;
	}

	public void setApsNameJson(String apsNameJson) {
		this.apsNameJson = apsNameJson;
	}

	public String getCoreNameJson() {
		return coreNameJson;
	}

	public void setCoreNameJson(String coreNameJson) {
		this.coreNameJson = coreNameJson;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}
