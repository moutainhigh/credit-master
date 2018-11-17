package com.zdmoney.credit.flow.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by ym10094 on 2017/8/11.
 */
public class SpFlowOperateInfoVo implements Serializable {
    private static final long serialVersionUID = 5201078022840138460L;

    private String usercode;

    private String operateName;

    private Date operateDate;

    private String operateStatus;

    private String memo;

    public String getUsercode() {
        return usercode;
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode;
    }

    public String getOperateName() {
        return operateName;
    }

    public void setOperateName(String operateName) {
        this.operateName = operateName;
    }

    public Date getOperateDate() {
        return operateDate;
    }

    public void setOperateDate(Date operateDate) {
        this.operateDate = operateDate;
    }

    public String getOperateStatus() {
        return operateStatus;
    }

    public void setOperateStatus(String operateStatus) {
        this.operateStatus = operateStatus;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
