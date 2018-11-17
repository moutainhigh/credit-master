package com.zdmoney.credit.loan.domain;

import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class UploadFtpFileLog extends BaseDomain {

    private static final long serialVersionUID = -5286657852000900825L;

    private Long id;

    private String appNo;

    private String contractNum;

    private int recordtimes=1;

    private Date createTime;

    private Date updateTime;

    private int  status;

    private String uploadAddress; 
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppNo() {
        return appNo;
    }

    public void setAppNo(String appNo) {
        this.appNo = appNo == null ? null : appNo.trim();
    }

    public String getContractNum() {
        return contractNum;
    }

    public void setContractNum(String contractNum) {
        this.contractNum = contractNum == null ? null : contractNum.trim();
    }

    public int getRecordtimes() {
        return recordtimes;
    }

    public void setRecordtimes(int recordtimes) {
        this.recordtimes = recordtimes;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

	public String getUploadAddress() {
		return uploadAddress;
	}

	public void setUploadAddress(String uploadAddress) {
		this.uploadAddress = uploadAddress;
	}
    
}