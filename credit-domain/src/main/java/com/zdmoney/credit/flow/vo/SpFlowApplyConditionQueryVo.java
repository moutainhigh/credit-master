package com.zdmoney.credit.flow.vo;

import java.io.Serializable;

/**
 * Created by ym10094 on 2017/8/3.
 */
public class SpFlowApplyConditionQueryVo implements Serializable {
    private static final long serialVersionUID = -764780840755058677L;

    private String conditionType;

    private String condition;

    public SpFlowApplyConditionQueryVo() {}

    public SpFlowApplyConditionQueryVo(String conditionType, String condition) {
        this.conditionType = conditionType;
        this.condition = condition;
    }


    public String getConditionType() {
        return conditionType;
    }

    public void setConditionType(String conditionType) {
        this.conditionType = conditionType;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}
