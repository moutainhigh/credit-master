package com.zdmoney.credit.flow.dao;

import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.flow.dao.pub.ISpFlowApplyConditionDao;
import com.zdmoney.credit.flow.domain.SpFlowApplyCondition;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ym10094 on 2017/8/2.
 * 审批流程适应条件
 */
@Repository
public class SpFlowApplyConditionDaoImpl extends BaseDaoImpl<SpFlowApplyCondition> implements ISpFlowApplyConditionDao {
    @Override
    public SpFlowApplyCondition querySpFlowApplyCondition(String conditionType, String condition) {
        Map<String,Object> params = new HashMap<>();
        params.put("conditionType",conditionType);
        params.put("condition",condition);
        List<SpFlowApplyCondition> spFlowApplyConditions = findListByMap(params);
        if (CollectionUtils.isEmpty(spFlowApplyConditions)) {
            return  null;
        }
        return spFlowApplyConditions.get(0);
    }
}
