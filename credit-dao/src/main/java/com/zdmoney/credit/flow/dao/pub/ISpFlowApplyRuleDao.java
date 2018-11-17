package com.zdmoney.credit.flow.dao.pub;

import com.zdmoney.credit.flow.domain.SpFlowApplyRule;
import com.zdmoney.credit.framework.dao.pub.IBaseDao;

/**
 * Created by ym10094 on 2017/8/2.
 * 审批流程适应规则
 */
public interface ISpFlowApplyRuleDao extends IBaseDao<SpFlowApplyRule> {
    /**
     * 根据条件组合字符串(2,5,6)查询流程ID
     * @param condition
     * @return
     */
    public Long queryFlowIdByCondition(String condition);
}
