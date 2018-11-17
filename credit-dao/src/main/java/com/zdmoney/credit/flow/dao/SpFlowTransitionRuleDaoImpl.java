package com.zdmoney.credit.flow.dao;

import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.flow.dao.pub.ISpFlowTransitionRuleDao;
import com.zdmoney.credit.flow.domain.SpFlowTransitionRule;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ym10094 on 2017/8/2.
 * 审批流程流转规则 对应一个流程的各个节点组成
 */
@Repository
public class SpFlowTransitionRuleDaoImpl extends BaseDaoImpl<SpFlowTransitionRule> implements ISpFlowTransitionRuleDao {
    @Override
    public List<SpFlowTransitionRule> querySpFlowTransitionRuleByFlowId(Long flowId) {
        Map<String,Object> params = new HashMap<>();
        params.put("flowId",flowId);
        List<SpFlowTransitionRule> spFlowTransitionRules = getSqlSession().selectList(getIbatisMapperNameSpace() + ".querySpFlowTransitionRuleByFlowId", params);
        if (CollectionUtils.isEmpty(spFlowTransitionRules)) {
            return Collections.emptyList();
        }
        return spFlowTransitionRules;
    }

    @Override
    public SpFlowTransitionRule queryFlowNodeRuleByFlowIdStartNodeId(Long flowId, Long startNodeId) {
        Map<String,Object> params = new HashMap<>();
        params.put("flowId",flowId);
        params.put("startNodeId",startNodeId);
        SpFlowTransitionRule rule =getSqlSession().selectOne(getIbatisMapperNameSpace() + ".queryFlowNodeRuleByFlowIdStartNodeId",params);
        return rule;
    }
}
