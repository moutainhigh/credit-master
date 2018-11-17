package com.zdmoney.credit.person.domain;

import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class PersonNfcsWorkerCall extends BaseDomain {
	private static final long serialVersionUID = 1L;

	private Long id;

    private String callBackinfo;

    private Date handleTime;

    private String idnum;

    private Long loan;

    private String searchCode;

    private String name;

    private Date createTime;

    private String creator;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCallBackinfo() {
		return callBackinfo;
	}

	public void setCallBackinfo(String callBackinfo) {
		this.callBackinfo = callBackinfo;
	}

	public Date getHandleTime() {
		return handleTime;
	}

	public void setHandleTime(Date handleTime) {
		this.handleTime = handleTime;
	}

	public String getIdnum() {
		return idnum;
	}

	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}

	public Long getLoan() {
		return loan;
	}

	public void setLoan(Long loan) {
		this.loan = loan;
	}

	public String getSearchCode() {
		return searchCode;
	}

	public void setSearchCode(String searchCode) {
		this.searchCode = searchCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}
}