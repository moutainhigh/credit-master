package com.zdmoney.credit.api.app.vo;

import java.io.Serializable;

/**
 * Created by ym10094 on 2016/11/17.
 */
public class AllRepaymentResponse implements Serializable {
    private static final long serialVersionUID = 3163417642778559126L;
    /**
     * 合同编号
     */
    private String pactNo;
    /**
     * 1 成功 2 失败
     */
    private String status;
    private String msg;

    public String getPactNo() {
        return pactNo;
    }

    public void setPactNo(String pactNo) {
        this.pactNo = pactNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
