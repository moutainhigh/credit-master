package com.zdmoney.credit.common.vo.core;

public class UpdateFailVO {

    String appNo;            //失败的债权appNo
    String errorMessage;    //失败信息

    public String getAppNo() {
        return appNo;
    }

    public void setAppNo(String appNo) {
        this.appNo = appNo;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
