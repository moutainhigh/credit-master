package com.zdmoney.credit.flow.dao.pub;

import com.zdmoney.credit.flow.domain.SpFlowApplyCondition;
import com.zdmoney.credit.framework.dao.pub.IBaseDao;

/**
 * Created by ym10094 on 2017/8/2.
 * 审批流程适应条件
 */
public interface ISpFlowApplyConditionDao extends IBaseDao<SpFlowApplyCondition> {

    public SpFlowApplyCondition querySpFlowApplyCondition(String conditionType,String condition);
}
