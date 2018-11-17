package com.zdmoney.credit.flow.dao;

import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.flow.dao.pub.ISpFlowInstanceDao;
import com.zdmoney.credit.flow.domain.SpFlowInstance;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ym10094 on 2017/8/2.
 * 审批流程实例
 */
@Repository
public class SpFlowInstanceDaoImpl extends BaseDaoImpl<SpFlowInstance> implements ISpFlowInstanceDao {
    @Override
    public SpFlowInstance querySpFlowInstanceByApply(Long applyId) {
        Map<String,Object> params = new HashMap<>();
        params.put("applyId",applyId);
        List<SpFlowInstance> spFlowInstances = findListByMap(params);
        if (CollectionUtils.isEmpty(spFlowInstances)) {
            return null;
        }
        return spFlowInstances.get(0);
    }
}
