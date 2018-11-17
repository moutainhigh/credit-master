package com.zdmoney.credit.flow.dao.pub;

import com.zdmoney.credit.flow.domain.SpFlowTransitionRule;
import com.zdmoney.credit.framework.dao.pub.IBaseDao;

import java.util.List;

/**
 * Created by ym10094 on 2017/8/2.
 * 审批流程流转规则 配置一个流程节点
 */

public interface ISpFlowTransitionRuleDao extends IBaseDao<SpFlowTransitionRule> {

    public List<SpFlowTransitionRule> querySpFlowTransitionRuleByFlowId(Long flowId);

    public SpFlowTransitionRule queryFlowNodeRuleByFlowIdStartNodeId(Long flowId,Long startNodeId);
}
