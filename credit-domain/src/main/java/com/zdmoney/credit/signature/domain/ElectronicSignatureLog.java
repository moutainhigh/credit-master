package com.zdmoney.credit.signature.domain;

import com.zdmoney.credit.framework.domain.BaseDomain;

import java.util.Date;

/**
 * 记录电子签章
 */
public class ElectronicSignatureLog  extends BaseDomain {
    private static final long serialVersionUID = -5286657852000985855L;

    private Long id;

    private String appNo;

    private String contractNum;

    private Long signatureCount;

    private int status;

    private Date createTime;

    private Date updateTime;

    private String creator;

    private String updator;

    private String signatureName;

    private String signatureBelong;

    private String remark;

    private String fileType;

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

    public Long getSignatureCount() {
        return signatureCount;
    }

    public void setSignatureCount(Long signatureCount) {
        this.signatureCount = signatureCount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public String getUpdator() {
        return updator;
    }

    public void setUpdator(String updator) {
        this.updator = updator == null ? null : updator.trim();
    }

    public String getSignatureName() {
        return signatureName;
    }

    public void setSignatureName(String signatureName) {
        this.signatureName = signatureName == null ? null : signatureName.trim();
    }

    public String getSignatureBelong() {
        return signatureBelong;
    }

    public void setSignatureBelong(String signatureBelong) {
        this.signatureBelong = signatureBelong == null ? null : signatureBelong.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}