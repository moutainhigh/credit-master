package com.zdmoney.credit.loan.vo;

import com.zdmoney.credit.loan.domain.SpecialRepaymentApply;

import java.math.BigDecimal;

/**
 * Created by ym10094 on 2017/5/16.
 */
public class SpecialRepaymentApplyVo extends SpecialRepaymentApply {
    private boolean isEffective;
    public boolean isEffective() {
        return isEffective;
    }

    public void setIsEffective(boolean isEffective) {
        this.isEffective = isEffective;
    }
}
