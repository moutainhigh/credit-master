package com.zdmoney.credit.grant.domain;

import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class LoanBaseGrant extends BaseDomain {

    private static final long serialVersionUID = 3163417642726959126L;

    private Long id;

    private Long loanId;

    private Date grantApplyDate;

    private Date grantApplyFinishDate;

    private String offerState;

    private String grantState;

    private String remark;

    private Long proposerId;

    private String appNo;

    private String contractNum;

    private String portBatNo;
    
    private String respStatus;
    
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

    public Date getGrantApplyDate() {
        return grantApplyDate;
    }

    public void setGrantApplyDate(Date grantApplyDate) {
        this.grantApplyDate = grantApplyDate;
    }

    public Date getGrantApplyFinishDate() {
        return grantApplyFinishDate;
    }

    public void setGrantApplyFinishDate(Date grantApplyFinishDate) {
        this.grantApplyFinishDate = grantApplyFinishDate;
    }

    public String getOfferState() {
        return offerState;
    }

    public void setOfferState(String offerState) {
        this.offerState = offerState == null ? null : offerState.trim();
    }

    public String getGrantState() {
        return grantState;
    }

    public void setGrantState(String grantState) {
        this.grantState = grantState == null ? null : grantState.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Long getProposerId() {
        return proposerId;
    }

    public void setProposerId(Long proposerId) {
        this.proposerId = proposerId;
    }

    public String getAppNo() {
        return appNo;
    }

    public void setAppNo(String appNo) {
        this.appNo = appNo;
    }

    public String getContractNum() {
        return contractNum;
    }

    public void setContractNum(String contractNum) {
        this.contractNum = contractNum;
    }

	public String getPortBatNo() {
		return portBatNo;
	}

	public void setPortBatNo(String portBatNo) {
		this.portBatNo = portBatNo;
	}

	public String getRespStatus() {
		return respStatus;
	}

	public void setRespStatus(String respStatus) {
		this.respStatus = respStatus;
	}
}