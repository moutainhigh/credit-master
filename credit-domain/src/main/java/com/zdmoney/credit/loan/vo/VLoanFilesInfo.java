package com.zdmoney.credit.loan.vo;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

/**
 * 客户档案信息
 * @author 00236633
 */
public class VLoanFilesInfo {

//	@NotNull(message="致客户书数量不能为空")
	@Range(min=0,max=99999999l,message="致客户书数量必须大于0,小于1000000000")
	private Long autidingFilesCount;
	
//	@NotNull(message="银行卡复印件数量不能为空")
	@Range(min=0,max=99999999l,message="银行卡复印件数量必须大于0,小于1000000000")
	private Long bankCardFilesCount;
	
//	@NotNull(message="个人信用报告数量不能为空")
	@Range(min=0,max=99999999l,message="个人信用报告数量必须大于0,小于1000000000")
	private Long personalCreditReportsCount;
	
//	@NotNull(message="借款申请表数量不能为空")
	@Range(min=0,max=99999999l,message="借款申请表数量必须大于0,小于1000000000")
	private Long requestFilesCount;
	
//	@NotNull(message="签约确认函数量不能为空")
	@Range(min=0,max=99999999l,message="签约确认函数量必须大于0,小于1000000000")
	private Long serviceFilesCount;
	
	@Length(max=255,message="住址证明最多能输入255个字符")
	private String addressCertification;
	
	@Range(min=0,max=99999999l,message="住址证明数量必须大于0,小于1000000000")
	private Long addressCertificationCount;
	
	@Length(max=255,message="住址证明其他最多能输入255个字符")
	private String addressCertificationOther;
	
	@Length(max=255,message="资产证明最多能输入255个字符")
	private String assetCertification;
	
	@Range(min=0,max=99999999l,message="资产证明数量必须大于0,小于1000000000")
	private Long assetCertificationCount;
	
	@Length(max=255,message="资产证明其他最多能输入255个字符")
	private String assetCertificationOther;

	@Length(max=255,message="经营证明最多能输入255个字符")
	private String businessCertification;
	
	@Range(min=0,max=99999999l,message="经营证明数量必须大于0,小于1000000000")
	private Long businessCertificationCount;
	
	@Length(max=255,message="经营证明其他最多能输入255个字符")
	private String businessCertificationOther;
	
	@Length(max=255,message="身份证明最多能输入255个字符")
	private String idCertification;
	
	@Range(min=0,max=99999999l,message="身份证明数量必须大于0,小于1000000000")
	private Long idCertificationCount;
	
	@Length(max=255,message="身份证明其他最多能输入255个字符")
	private String idCertificationOther;
	
	@Length(max=255,message="收入证明最多能输入255个字符")
	private String incomeCertification;
	
	@Range(min=0,max=99999999l,message="收入证明数量必须大于0,小于1000000000")
	private Long incomeCertificationCount;
	
	@Length(max=255,message="收入证明其他最多能输入255个字符")
	private String incomeCertificationOther;
	
	@Length(max=255,message="其他证明最多能输入255个字符")
	private String otherCertification;
	
	@Range(min=0,max=99999999l,message="其他证明数量必须大于0,小于1000000000")
	private Long otherCertificationCount;
	
	@Length(max=255,message="其他证明其他最多能输入255个字符")
	private String otherCertificationOther;
	
	@Length(max=255,message="工作证明最多能输入255个字符")
	private String workCertification;
	
	@Range(min=0,max=99999999l,message="工作证明数量必须大于0,小于1000000000")
	private Long workCertificationCount;
	
	@Length(max=255,message="工作证明最多能输入255个字符")
	private String workCertificationOther;
	
	private Long id;
	private Long version;
	private String borrowerDocFlowNum;
	private Long operatorId;
	
	@NotNull(message="借款id不能为空")
	@Range(min=1,max=999999999999999999l,message="借款id必须大于零,小于1000000000000000000")
	private Long loanId;
	
	@Length(max=255,message="备注最多只能输入255个字符")
	private String memo;
	/**
	 * 版本号
	 */
	private String mark;

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
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

	public String getBorrowerDocFlowNum() {
		return borrowerDocFlowNum;
	}

	public void setBorrowerDocFlowNum(String borrowerDocFlowNum) {
		this.borrowerDocFlowNum = borrowerDocFlowNum;
	}

	public Long getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
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
	
}
