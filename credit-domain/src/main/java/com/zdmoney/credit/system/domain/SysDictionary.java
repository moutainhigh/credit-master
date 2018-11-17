package com.zdmoney.credit.system.domain;

import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class SysDictionary extends BaseDomain{
    private Long id;

    private String codeName;

    private String codeTitle;

    private String codeType;

    private String codeTypeTitle;

    private String memo;

    private Long parentId;

    private Long seqence;

    private String codeValue;

    private String tag;

    private Date createTime;

    private Date updateTime;

    private String operator;

    private String flage;
    
    public String getFlage() {
		return flage;
	}

	public void setFlage(String flage) {
		this.flage = flage;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName == null ? null : codeName.trim();
    }

    public String getCodeTitle() {
        return codeTitle;
    }

    public void setCodeTitle(String codeTitle) {
        this.codeTitle = codeTitle == null ? null : codeTitle.trim();
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType == null ? null : codeType.trim();
    }

    public String getCodeTypeTitle() {
        return codeTypeTitle;
    }

    public void setCodeTypeTitle(String codeTypeTitle) {
        this.codeTypeTitle = codeTypeTitle == null ? null : codeTypeTitle.trim();
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getSeqence() {
        return seqence;
    }

    public void setSeqence(Long seqence) {
        this.seqence = seqence;
    }

    public String getCodeValue() {
        return codeValue;
    }

    public void setCodeValue(String codeValue) {
        this.codeValue = codeValue == null ? null : codeValue.trim();
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag == null ? null : tag.trim();
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

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }
}