package com.zdmoney.credit.loan.domain;

import com.zdmoney.credit.framework.domain.BaseDomain;

/**
 * @author 10098  2017年3月1日 上午11:02:06
 */
public class UploadLoanDataInfo extends BaseDomain{

	private static final long serialVersionUID = 8947726067981515331L;

	private Long id;
	
	private Long loanId;
	
	private String contractNum;
	
	private String idNum;
	
	private String uploadStatus;
	
	private String appNo;
	
	private String remark;
	
	private String uploadAddress;

	private String batchNum;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getLoanId() {
		return loanId;
	}

	public void setLoanId(Long loanId) {
		this.loanId = loanId;
	}

	public String getContractNum() {
		return contractNum;
	}

	public void setContractNum(String contractNum) {
		this.contractNum = contractNum;
	}

	public String getIdNum() {
		return idNum;
	}

	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}

	public String getUploadStatus() {
		return uploadStatus;
	}

	public void setUploadStatus(String uploadStatus) {
		this.uploadStatus = uploadStatus;
	}

	public String getAppNo() {
		return appNo;
	}

	public void setAppNo(String appNo) {
		this.appNo = appNo;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUploadAddress() {
		return uploadAddress;
	}

	public void setUploadAddress(String uploadAddress) {
		this.uploadAddress = uploadAddress;
	}

	public String getBatchNum() {
		return batchNum;
	}

	public void setBatchNum(String batchNum) {
		this.batchNum = batchNum;
	}
	
	
}
