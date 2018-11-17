package com.zdmoney.credit.common.vo.core;


/**
 * 
 */
public class CreateFailVO {

    Long loanId;             //失败的债权ID
    String errorMessage;    //失败信息

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
