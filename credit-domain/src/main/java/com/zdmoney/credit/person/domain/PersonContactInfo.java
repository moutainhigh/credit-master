package com.zdmoney.credit.person.domain;

import com.zdmoney.credit.framework.domain.BaseDomain;

/**
 * 联系人信息实体
 * @author Ivan
 *
 */
public class PersonContactInfo extends BaseDomain {
    /**
	 * 
	 */
	private static final long serialVersionUID = 3725312112419223743L;
	/** 主键 **/
	private Long id;
	/** 联系人地址 **/
    private String address;
    /** 联系人公司 **/
    private String company;
    /** 联系人类型 **/
    private String contactType;
    /** 联系人部门 **/
    private String department;
    /** 联系人职务 **/
    private String duty;
    /** 身份证 **/
    private String idCard;
    /** 联系人手机号码 **/
    private String mphone;
    /** 姓名 **/
    private String name;
    /** 关系 **/
    private String relation;
    /** 联系人电话号码 **/
    private String tel;
    /** 联系人所属的借款人 **/
    private Long personId;
    /** 公司电话 **/
    private String ctel;
    /** 联系人是否知晓贷款 **/
    private String isKnowLoan;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company == null ? null : company.trim();
    }

    public String getContactType() {
        return contactType;
    }

    public void setContactType(String contactType) {
        this.contactType = contactType == null ? null : contactType.trim();
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department == null ? null : department.trim();
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty == null ? null : duty.trim();
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard == null ? null : idCard.trim();
    }

    public String getMphone() {
        return mphone;
    }

    public void setMphone(String mphone) {
        this.mphone = mphone == null ? null : mphone.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation == null ? null : relation.trim();
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel == null ? null : tel.trim();
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public String getCtel() {
        return ctel;
    }

    public void setCtel(String ctel) {
        this.ctel = ctel == null ? null : ctel.trim();
    }

    public String getIsKnowLoan() {
        return isKnowLoan;
    }

    public void setIsKnowLoan(String isKnowLoan) {
        this.isKnowLoan = isKnowLoan == null ? null : isKnowLoan.trim();
    }
}