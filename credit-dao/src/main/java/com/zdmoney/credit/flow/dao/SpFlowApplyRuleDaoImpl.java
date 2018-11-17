package com.zdmoney.credit.flow.dao;

import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.flow.dao.pub.ISpFlowApplyRuleDao;
import com.zdmoney.credit.flow.domain.SpFlowApplyRule;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ym10094 on 2017/8/2.
 * 审批流程适应规则
 */
@Repository
public class SpFlowApplyRuleDaoImpl extends BaseDaoImpl<SpFlowApplyRule> implements ISpFlowApplyRuleDao {
    @Override
    public Long queryFlowIdByCondition(String condition) {
        Map<String,Object> params = new HashMap<>();
        params.put("condition", Strings.isEmpty(condition) ? "" : condition);
        Long flowId = getSqlSession().selectOne(getIbatisMapperNameSpace()+".queryFlowIdByCondition",params);
        return flowId;
    }
}
