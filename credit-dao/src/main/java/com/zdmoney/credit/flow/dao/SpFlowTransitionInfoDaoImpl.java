package com.zdmoney.credit.flow.dao;

import com.zdmoney.credit.common.constant.flow.FlowNodeTransitionStatusEnum;
import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.flow.dao.pub.ISpFlowTransitionInfoDao;
import com.zdmoney.credit.flow.domain.SpFlowTransitionInfo;
import com.zdmoney.credit.flow.vo.SpFlowOperateInfoVo;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Created by ym10094 on 2017/8/2.
 * 审批流程流转信息
 */
@Repository
public class SpFlowTransitionInfoDaoImpl extends BaseDaoImpl<SpFlowTransitionInfo> implements ISpFlowTransitionInfoDao {
    @Override
    public SpFlowTransitionInfo queryProcessingSpFlowTransitionInfo(Long flowInstanceId) {
        Map<String,Object> params = new HashMap<>();
        params.put("flowInstanceId",flowInstanceId);
        params.put("status", FlowNodeTransitionStatusEnum.审批中.getCode());
        List<SpFlowTransitionInfo> spFlowTransitionInfos =  findListByMap(params);
        if (CollectionUtils.isEmpty(spFlowTransitionInfos)) {
            return null;
        }
        return spFlowTransitionInfos.get(0);
    }

    @Override
    public List<SpFlowOperateInfoVo> querySpFlowTransitionInfosByFlowInstanceId(Long flowInstanceId) {
        Map<String,Object> params = new HashMap<>();
        params.put("flowInstanceId",flowInstanceId);
        List<SpFlowOperateInfoVo> spFlowTransitionInfos = getSqlSession().selectList(getIbatisMapperNameSpace() + ".querySpFlowTransitionInfosByFlowInstanceId",params);
        if (CollectionUtils.isEmpty(spFlowTransitionInfos)) {
            return Collections.emptyList();
        }
        return spFlowTransitionInfos;
    }

    @Override
    public int updateStatusCancel(Long flowInstanceId) {
        return  getSqlSession().update(getIbatisMapperNameSpace() + ".updateStatusCancel", flowInstanceId);
    }
}
