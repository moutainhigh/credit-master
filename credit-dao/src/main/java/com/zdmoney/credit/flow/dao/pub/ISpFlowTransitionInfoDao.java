package com.zdmoney.credit.flow.dao.pub;

import com.zdmoney.credit.flow.domain.SpFlowTransitionInfo;
import com.zdmoney.credit.flow.vo.SpFlowOperateInfoVo;
import com.zdmoney.credit.framework.dao.pub.IBaseDao;

import java.util.List;

/**
 * Created by ym10094 on 2017/8/2.
 * 审批流程流转信息
 */
public interface ISpFlowTransitionInfoDao extends IBaseDao<SpFlowTransitionInfo> {
    /**
     * 查询处在处理中状态的流程节点信息
     * @param flowInstanceId
     * @return
     */
    public SpFlowTransitionInfo queryProcessingSpFlowTransitionInfo(Long flowInstanceId);

    public List<SpFlowOperateInfoVo> querySpFlowTransitionInfosByFlowInstanceId(Long flowInstanceId);

    public int updateStatusCancel(Long flowInstanceId);

}
