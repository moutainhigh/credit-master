package com.zdmoney.credit.flow.service.pub;

import com.zdmoney.credit.common.constant.flow.FlowNodeTransitionStatusEnum;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.flow.domain.*;
import com.zdmoney.credit.flow.vo.EmployeeRoleVo;
import com.zdmoney.credit.flow.vo.SpFlowApplyConditionQueryVo;
import com.zdmoney.credit.flow.vo.SpFlowInstanceRequestParamVo;
import com.zdmoney.credit.flow.vo.SpFlowOperateInfoVo;

import java.util.List;
import java.util.Map;

/**
 * Created by ym10094 on 2017/8/2.
 * 审批流程service
 */
public interface ISpFlowService {
    /**
     * 查询流程适配条件实例
     * @param queryVos
     * @return
     */
    public List<SpFlowApplyCondition> queryFlowApplyConditions(List<SpFlowApplyConditionQueryVo> queryVos);

    /**
     * 把流程适配条件集合Id转换成字符串用","拼接
     * @param spFlowApplyConditions
     * @return
     */
    public String SpFlowApplyConditionIdFormatString(List<SpFlowApplyCondition> spFlowApplyConditions);
    /**
     * 获取流程id
     * @param spFlowApplyConditions
     * @return
     */
    public Long querySpFlowId(List<SpFlowApplyCondition> spFlowApplyConditions);

    /**
     * 获取流程节点
     * @param paramVo
     * @return
     */
    public List<SpFlowTransitionRule> queryFlowNode(SpFlowInstanceRequestParamVo paramVo);

    /**
     * 启动流程实例
     * @param applyId
     * @param flowId
     */
    public void startFlowInstance(Long applyId,Long flowId);

    /**
     * 流转（移动）节点实例动作
     * @param flowInstanceId
     * @param nodeStatus
     */
    public void moveNodeInstanceAction(Long flowInstanceId,FlowNodeTransitionStatusEnum nodeStatus,String memo);

    /**
     * 审批通过时 调用申请减免是否零元入账处理
     * @param applyId
     */
    public void appleReliefPassInvokingRepayIuput(Long applyId);

    public SpFlowInstance querySpFlowInstanceByApplyId(Long applyId);

    /**
     * 获取某一个申请减免的审批流程
     * @param applyId
     * @return
     */
    public List<SpFlowTransitionRule> querySpFlowTransitionRulesByApplyId(Long applyId);

    /**
     * 查询流程审批操作日志
     * @param applyId
     * @param flowInstanceId
     * @return
     */
    public List<SpFlowOperateInfoVo> queryOperateInfos(Long applyId,Long flowInstanceId);

    public void cancelReliefApplyFlowInstanceByApplyId(Long applyId);

    /**
     * 申请减免分页
     * @param params
     * @return
     */
    public Pager queryNodeEmployeePage(Map<String,Object> params);

    public List<SpNodeInfo> querySpNodeInfos();

    public List<SpNodeInfo> querySpApplyNodeInfos();

    public void insertNodeEmployee(Long employeeId,Long nodeId);

    public void deleteNodeEmployee(Long id);

    public SpNodeEmployee querySpNodeEmployee(Long nodeId,Long employeeId);

    public void updateNodeEmployee(EmployeeRoleVo employeeRoleVo);
}
