package com.zdmoney.credit.flow.vo;

import com.zdmoney.credit.common.constant.SpecialRepaymentApplyTeyps;
import com.zdmoney.credit.common.constant.flow.DepartmentTypeEnum;
import com.zdmoney.credit.common.constant.flow.OverDueGradeEnum;
import com.zdmoney.credit.common.constant.flow.ReliefAmountGradeEnum;

import java.io.Serializable;

/**
 * Created by ym10094 on 2017/8/4.
 */
public class SpFlowInstanceRequestParamVo implements Serializable{
    private static final long serialVersionUID = -1450224219451963104L;

    private OverDueGradeEnum overDueGradeEnum;

    private DepartmentTypeEnum departmentTypeEnum;

    private SpecialRepaymentApplyTeyps reliefApplyTypes;

    private ReliefAmountGradeEnum reliefAmountGradeEnum;

    private Long firstNodeId;

    public OverDueGradeEnum getOverDueGradeEnum() {
        return overDueGradeEnum;
    }

    public void setOverDueGradeEnum(OverDueGradeEnum overDueGradeEnum) {
        this.overDueGradeEnum = overDueGradeEnum;
    }

    public DepartmentTypeEnum getDepartmentTypeEnum() {
        return departmentTypeEnum;
    }

    public void setDepartmentTypeEnum(DepartmentTypeEnum departmentTypeEnum) {
        this.departmentTypeEnum = departmentTypeEnum;
    }

    public SpecialRepaymentApplyTeyps getReliefApplyTypes() {
        return reliefApplyTypes;
    }

    public void setReliefApplyTypes(SpecialRepaymentApplyTeyps reliefApplyTypes) {
        this.reliefApplyTypes = reliefApplyTypes;
    }

    public Long getFirstNodeId() {
        return firstNodeId;
    }

    public void setFirstNodeId(Long firstNodeId) {
        this.firstNodeId = firstNodeId;
    }

    public ReliefAmountGradeEnum getReliefAmountGradeEnum() {
        return reliefAmountGradeEnum;
    }

    public void setReliefAmountGradeEnum(ReliefAmountGradeEnum reliefAmountGradeEnum) {
        this.reliefAmountGradeEnum = reliefAmountGradeEnum;
    }
}
