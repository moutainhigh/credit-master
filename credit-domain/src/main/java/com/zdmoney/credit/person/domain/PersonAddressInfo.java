package com.zdmoney.credit.person.domain;

import com.zdmoney.credit.framework.domain.BaseDomain;


/**
 * 借款人地址变更记录实体
 * @author Ivan
 *
 */
public class PersonAddressInfo extends BaseDomain {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 5902578227963664918L;
	/** 主键ID **/
	private Long id;
	/** 对象名 **/
    private String className;
    /** 地址类型 **/
    private String addressType;
    /** 地址 **/
    private String content;
    /** 备注 **/
    private String memo;
    /** 对象ID **/
    private Long objectId;
    /** 邮编 **/
    private String postcode;
    /** 地址优先级 **/
    private String priority;
    /** 是否有效 **/
    private String valid;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getAddressType() {
		return addressType;
	}

	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getValid() {
		return valid;
	}

	public void setValid(String valid) {
		this.valid = valid;
	}

    
}