package com.zdmoney.credit.loan.vo;

import java.util.Date;

/**
 * Created by ym10094 on 2017/6/22.
 */
public class ApplyReliefVo {
    private static final long serialVersionUID = -803649233994769919L;

    private Long  applyId;

    private  Long loanId;

    private String contractNum;

    private String name;

    private String idNum;

    private String applyReliefType;

    private String salesDepartmentId;

    private Date startApplyDate;

    private Date endApplyDate;

    private String applicationStatus;

    private String nodeId;

    private String specialReliefFlag = "0";

    public Long getApplyId() {
        return applyId;
    }

    public void setApplyId(Long applyId) {
        this.applyId = applyId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdNum() {
        return idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }

    public String getApplyReliefType() {
        return applyReliefType;
    }

    public void setApplyReliefType(String applyReliefType) {
        this.applyReliefType = applyReliefType;
    }

    public String getSalesDepartmentId() {
        return salesDepartmentId;
    }

    public void setSalesDepartmentId(String salesDepartmentId) {
        this.salesDepartmentId = salesDepartmentId;
    }

    public Date getStartApplyDate() {
        return startApplyDate;
    }

    public void setStartApplyDate(Date startApplyDate) {
        this.startApplyDate = startApplyDate;
    }

    public Date getEndApplyDate() {
        return endApplyDate;
    }

    public void setEndApplyDate(Date endApplyDate) {
        this.endApplyDate = endApplyDate;
    }

    public String getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(String applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getSpecialReliefFlag() {
        return specialReliefFlag;
    }

    public void setSpecialReliefFlag(String specialReliefFlag) {
        this.specialReliefFlag = specialReliefFlag;
    }
}
