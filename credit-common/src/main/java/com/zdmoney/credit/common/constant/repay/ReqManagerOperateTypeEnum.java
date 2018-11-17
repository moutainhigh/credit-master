package com.zdmoney.credit.common.constant.repay;

/**
 * Created by ym10094 on 2016/9/1.
 */
public enum ReqManagerOperateTypeEnum {
    downLoad("01"),
    upload("02");
    private String operateType;

     ReqManagerOperateTypeEnum(String operateType) {
        this.operateType = operateType;
    }

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }
}
