package com.zdmoney.credit.api.app.vo;

import com.zdmoney.credit.common.vo.RequestInfo;

import java.io.Serializable;

/**
 * Created by ym10094 on 2016/11/9.
 */
public class FinanceGrantRequest implements Serializable{
    private static final long serialVersionUID = 3163417642726959126L;

    private String prePactNo;

    private String dealSts;

    private String dealDesc;

    public String getPrePactNo() {
        return prePactNo;
    }

    public void setPrePactNo(String prePactNo) {
        this.prePactNo = prePactNo;
    }

    public String getDealSts() {
        return dealSts;
    }

    public void setDealSts(String dealSts) {
        this.dealSts = dealSts;
    }

    public String getDealDesc() {
        return dealDesc;
    }

    public void setDealDesc(String dealDesc) {
        this.dealDesc = dealDesc;
    }
}
