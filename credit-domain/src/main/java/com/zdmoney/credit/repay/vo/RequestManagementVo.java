package com.zdmoney.credit.repay.vo;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class RequestManagementVo extends BaseDomain {
    
    private static final long serialVersionUID = 7539111380223163978L;

    /** 信托项目简码 **/
    private String creditCode = "ZDCF01";
    
    /** 理财机构简码 **/
    private String financeCode;
    
    /** 债权批次号 **/
    private String batchNum;
    
    /** 上传状态 **/
    private String status;

    public String getCreditCode() {
        return creditCode;
    }

    public void setCreditCode(String creditCode) {
        this.creditCode = creditCode;
    }

    public String getFinanceCode() {
        return financeCode;
    }

    public void setFinanceCode(String financeCode) {
        this.financeCode = financeCode;
    }

    public String getBatchNum() {
        return batchNum;
    }

    public void setBatchNum(String batchNum) {
        this.batchNum = batchNum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}