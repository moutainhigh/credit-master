package com.zdmoney.credit.loan.domain;

import java.sql.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class LoanFilesInfo extends BaseDomain{

	private static final long serialVersionUID = -3575863542963781391L;
	
	private Long id;
	private Long version;
	private String addressCertification;
	private Long addressCertificationCount;
	private String addressCertificationOther;
	private String assetCertification;
	private Long assetCertificationCount;
	private String assetCertificationOther;
	private Long autidingFilesCount;
	private Long bankCardFilesCount;
	private String borrowerDocFlowNum;
	private String businessCertification;
	private Long businessCertificationCount;
	private String businessCertificationOther;
	private Date dateCreated;
	private String idCertification;
	private Long idCertificationCount;
	private String idCertificationOther;
	private String incomeCertification;
	private Long incomeCertificationCount;
	private String incomeCertificationOther;
	private Long loanId;
	private String memo;
	private Long operatorId;
	private String otherCertification;
	private Long otherCertificationCount;
	private String otherCertificationOther;
	private Long personalCreditReportsCount;
	private Long requestFilesCount;
	private Long serviceFilesCount;
	private String workCertification;
	private Long workCertificationCount;
	private String workCertificationOther;
	private String mark;

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getVersion() {
		return version;
	}
	public void setVersion(Long version) {
		this.version = version;
	}
	public String getAddressCertification() {
		return addressCertification;
	}
	public void setAddressCertification(String addressCertification) {
		this.addressCertification = addressCertification;
	}
	public Long getAddressCertificationCount() {
		return addressCertificationCount;
	}
	public void setAddressCertificationCount(Long addressCertificationCount) {
		this.addressCertificationCount = addressCertificationCount;
	}
	public String getAddressCertificationOther() {
		return addressCertificationOther;
	}
	public void setAddressCertificationOther(String addressCertificationOther) {
		this.addressCertificationOther = addressCertificationOther;
	}
	public String getAssetCertification() {
		return assetCertification;
	}
	public void setAssetCertification(String assetCertification) {
		this.assetCertification = assetCertification;
	}
	public Long getAssetCertificationCount() {
		return assetCertificationCount;
	}
	public void setAssetCertificationCount(Long assetCertificationCount) {
		this.assetCertificationCount = assetCertificationCount;
	}
	public String getAssetCertificationOther() {
		return assetCertificationOther;
	}
	public void setAssetCertificationOther(String assetCertificationOther) {
		this.assetCertificationOther = assetCertificationOther;
	}
	public Long getAutidingFilesCount() {
		return autidingFilesCount;
	}
	public void setAutidingFilesCount(Long autidingFilesCount) {
		this.autidingFilesCount = autidingFilesCount;
	}
	public Long getBankCardFilesCount() {
		return bankCardFilesCount;
	}
	public void setBankCardFilesCount(Long bankCardFilesCount) {
		this.bankCardFilesCount = bankCardFilesCount;
	}
	public String getBorrowerDocFlowNum() {
		return borrowerDocFlowNum;
	}
	public void setBorrowerDocFlowNum(String borrowerDocFlowNum) {
		this.borrowerDocFlowNum = borrowerDocFlowNum;
	}
	public String getBusinessCertification() {
		return businessCertification;
	}
	public void setBusinessCertification(String businessCertification) {
		this.businessCertification = businessCertification;
	}
	public Long getBusinessCertificationCount() {
		return businessCertificationCount;
	}
	public void setBusinessCertificationCount(Long businessCertificationCount) {
		this.businessCertificationCount = businessCertificationCount;
	}
	public String getBusinessCertificationOther() {
		return businessCertificationOther;
	}
	public void setBusinessCertificationOther(String businessCertificationOther) {
		this.businessCertificationOther = businessCertificationOther;
	}
	public Date getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	public String getIdCertification() {
		return idCertification;
	}
	public void setIdCertification(String idCertification) {
		this.idCertification = idCertification;
	}
	public Long getIdCertificationCount() {
		return idCertificationCount;
	}
	public void setIdCertificationCount(Long idCertificationCount) {
		this.idCertificationCount = idCertificationCount;
	}
	public String getIdCertificationOther() {
		return idCertificationOther;
	}
	public void setIdCertificationOther(String idCertificationOther) {
		this.idCertificationOther = idCertificationOther;
	}
	public String getIncomeCertification() {
		return incomeCertification;
	}
	public void setIncomeCertification(String incomeCertification) {
		this.incomeCertification = incomeCertification;
	}
	public Long getIncomeCertificationCount() {
		return incomeCertificationCount;
	}
	public void setIncomeCertificationCount(Long incomeCertificationCount) {
		this.incomeCertificationCount = incomeCertificationCount;
	}
	public String getIncomeCertificationOther() {
		return incomeCertificationOther;
	}
	public void setIncomeCertificationOther(String incomeCertificationOther) {
		this.incomeCertificationOther = incomeCertificationOther;
	}
	public Long getLoanId() {
		return loanId;
	}
	public void setLoanId(Long loanId) {
		this.loanId = loanId;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public Long getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}
	public String getOtherCertification() {
		return otherCertification;
	}
	public void setOtherCertification(String otherCertification) {
		this.otherCertification = otherCertification;
	}
	public Long getOtherCertificationCount() {
		return otherCertificationCount;
	}
	public void setOtherCertificationCount(Long otherCertificationCount) {
		this.otherCertificationCount = otherCertificationCount;
	}
	public String getOtherCertificationOther() {
		return otherCertificationOther;
	}
	public void setOtherCertificationOther(String otherCertificationOther) {
		this.otherCertificationOther = otherCertificationOther;
	}
	public Long getPersonalCreditReportsCount() {
		return personalCreditReportsCount;
	}
	public void setPersonalCreditReportsCount(Long personalCreditReportsCount) {
		this.personalCreditReportsCount = personalCreditReportsCount;
	}
	public Long getRequestFilesCount() {
		return requestFilesCount;
	}
	public void setRequestFilesCount(Long requestFilesCount) {
		this.requestFilesCount = requestFilesCount;
	}
	public Long getServiceFilesCount() {
		return serviceFilesCount;
	}
	public void setServiceFilesCount(Long serviceFilesCount) {
		this.serviceFilesCount = serviceFilesCount;
	}
	public String getWorkCertification() {
		return workCertification;
	}
	public void setWorkCertification(String workCertification) {
		this.workCertification = workCertification;
	}
	public Long getWorkCertificationCount() {
		return workCertificationCount;
	}
	public void setWorkCertificationCount(Long workCertificationCount) {
		this.workCertificationCount = workCertificationCount;
	}
	public String getWorkCertificationOther() {
		return workCertificationOther;
	}
	public void setWorkCertificationOther(String workCertificationOther) {
		this.workCertificationOther = workCertificationOther;
	}
}
