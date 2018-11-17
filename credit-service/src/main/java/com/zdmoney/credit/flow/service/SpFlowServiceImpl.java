package com.zdmoney.credit.flow.service;

import com.zdmoney.credit.common.constant.SpecialRepaymentApplyStatus;
import com.zdmoney.credit.common.constant.SpecialRepaymentApplyTeyps;
import com.zdmoney.credit.common.constant.flow.*;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.util.*;
import com.zdmoney.credit.flow.dao.pub.*;
import com.zdmoney.credit.flow.domain.*;
import com.zdmoney.credit.flow.service.pub.ISpFlowService;
import com.zdmoney.credit.flow.vo.EmployeeRoleVo;
import com.zdmoney.credit.flow.vo.SpFlowApplyConditionQueryVo;
import com.zdmoney.credit.flow.vo.SpFlowInstanceRequestParamVo;
import com.zdmoney.credit.flow.vo.SpFlowOperateInfoVo;
import com.zdmoney.credit.loan.dao.pub.ISpecialRepaymentApplyDao;
import com.zdmoney.credit.loan.domain.SpecialRepaymentApply;
import com.zdmoney.credit.loan.service.pub.ISpecialRepaymentApplyService;
import com.zdmoney.credit.offer.service.pub.IOfferRepayInfoService;
import com.zdmoney.credit.offer.vo.RepaymentInputVo;
import com.zdmoney.credit.system.service.pub.ISequencesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by ym10094 on 2017/8/2.
 */
@Service
public class SpFlowServiceImpl implements ISpFlowService {
    public static final Logger logger = LoggerFactory.getLogger(SpFlowServiceImpl.class);
    @Autowired
    private ISpFlowApplyConditionDao spFlowApplyConditionDao;
    @Autowired
    private ISpFlowApplyRuleDao spFlowApplyRuleDao;
    @Autowired
    private ISpNodeEmployeeDao spNodeEmployeeDao;
    @Autowired
    private ISpFlowTransitionRuleDao spFlowTransitionRuleDao;
    @Autowired
    private ISpFlowInstanceDao spFlowInstanceDao;
    @Autowired
    private ISpFlowTransitionInfoDao spFlowTransitionInfoDao;
    @Autowired
    private ISpecialRepaymentApplyDao specialRepaymentApplyDao;
    @Autowired
    private ISpNodeInfoDao spNodeInfoDao;
    @Autowired
    private ISpNodeRoleDao spNodeRoleDao;
    @Autowired
    private ISequencesService sequencesService;
    @Autowired
    private ISpecialRepaymentApplyService specialRepaymentApplyService;
    @Autowired
    private IOfferRepayInfoService offerRepayInfoService;

    @Override
    public List<SpFlowApplyCondition> queryFlowApplyConditions(List<SpFlowApplyConditionQueryVo> queryVos) {
        List<SpFlowApplyCondition> spFlowApplyConditions = new ArrayList<>();
        for (SpFlowApplyConditionQueryVo queryVo:queryVos){
            SpFlowApplyCondition spFlowApplyCondition = spFlowApplyConditionDao.querySpFlowApplyCondition(queryVo.getConditionType(),queryVo.getCondition());
            spFlowApplyConditions.add(spFlowApplyCondition);
        }
        return spFlowApplyConditions;
    }

    @Override
    public String SpFlowApplyConditionIdFormatString(List<SpFlowApplyCondition> spFlowApplyConditions) {
        StringBuffer conditionIds = new StringBuffer();
        Collections.sort(spFlowApplyConditions, new Comparator<SpFlowApplyCondition>() {
            @Override
            public int compare(SpFlowApplyCondition o1, SpFlowApplyCondition o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });
        boolean flag = true;
        for(SpFlowApplyCondition spFlowApplyCondition:spFlowApplyConditions) {
            if (flag) {
                conditionIds.append(spFlowApplyCondition.getId());
                flag = false;
                continue;
            }
            conditionIds.append("-").append(spFlowApplyCondition.getId());
        }
        return conditionIds.toString();
    }

    @Override
    public Long querySpFlowId(List<SpFlowApplyCondition> spFlowApplyConditions) {
        String conditionsStr = SpFlowApplyConditionIdFormatString(spFlowApplyConditions);
        logger.info("流程匹配规则为：{}",conditionsStr);
        Long flowId = spFlowApplyRuleDao.queryFlowIdByCondition(conditionsStr);
        return flowId;
    }

    @Override
    public List<SpFlowTransitionRule> queryFlowNode(SpFlowInstanceRequestParamVo paramVo) {
        User userInfo = UserContext.getUser();
        List<SpNodeEmployee> spNodeEmployees = spNodeEmployeeDao.queryApplySpNodeEmployeeByEmployeeId(userInfo.getId());
        Long flowId = null;
        //计算出适合员工申请的减免审批流程
        for(SpNodeEmployee nodeEmployee:spNodeEmployees){
            paramVo.setFirstNodeId(nodeEmployee.getNodeId());
            List<SpFlowApplyConditionQueryVo> queryVos = getConditionQueryVos(paramVo);
            List<SpFlowApplyCondition> conditions = queryFlowApplyConditions(queryVos);
            flowId = querySpFlowId(conditions);
            if (Strings.isNotEmpty(flowId)) {
                break;
            }
        }
        if (Strings.isEmpty(flowId)) {
            throw new PlatformException(ResponseEnum.FULL_MSG,"找不到对应的流程！");
        }
        List<SpFlowTransitionRule> rules = spFlowTransitionRuleDao.querySpFlowTransitionRuleByFlowId(flowId);
        return rules;
    }

    private List<SpFlowApplyConditionQueryVo> getConditionQueryVos(SpFlowInstanceRequestParamVo paramVo){
        List<SpFlowApplyConditionQueryVo> queryVos = new ArrayList<>();
        //取随机节点 配置的时候强制发起申请的节点 要求 一个员工一个节点
        SpFlowApplyConditionQueryVo queryVo1 = new SpFlowApplyConditionQueryVo(ConditionTypeEnum.流程首节点.getCode(),paramVo.getFirstNodeId().toString());
        SpFlowApplyConditionQueryVo queryVo2 = new SpFlowApplyConditionQueryVo(ConditionTypeEnum.逾期等级.getCode(),paramVo.getOverDueGradeEnum().getCode());
        SpFlowApplyConditionQueryVo queryVo3 = new SpFlowApplyConditionQueryVo(ConditionTypeEnum.减免类型.getCode(),paramVo.getReliefApplyTypes().getCode());
        SpFlowApplyConditionQueryVo queryVo4 = new SpFlowApplyConditionQueryVo(ConditionTypeEnum.营业部门.getCode(),paramVo.getDepartmentTypeEnum().getValue());
        SpFlowApplyConditionQueryVo queryVo5 = new SpFlowApplyConditionQueryVo(ConditionTypeEnum.减免金额等级.getCode(),paramVo.getReliefAmountGradeEnum().getCode());
        if (SpecialRepaymentApplyTeyps.一般减免.getCode().equals(paramVo.getReliefApplyTypes().getCode())) {
            queryVos.add(queryVo2);
        }
        queryVos.add(queryVo1);
        queryVos.add(queryVo3);
        queryVos.add(queryVo4);
        queryVos.add(queryVo5);
        return queryVos;
    }

    @Override
    public void startFlowInstance(Long applyId, Long flowId) {
        List<SpFlowTransitionRule> rules = spFlowTransitionRuleDao.querySpFlowTransitionRuleByFlowId(flowId);
        Long stratNodeId = rules.get(0).getStartNodeId();
        Long nextNodeId = rules.get(0).getStopNodeId();
        SpFlowInstance flowInstance = insertSpFlowInstance(applyId, flowId, stratNodeId);
        startFirstNodeInstance(0L, stratNodeId, nextNodeId,flowInstance.getId());
        SpFlowTransitionRule transitionRule = rules.get(1);
        if (transitionRule != null) {
            if (NodeActionTypeEnum.自动.getCode().equals(transitionRule.getIsAuto())) {
                moveAutoNodeInstanceAction(transitionRule,flowInstance.getId());
                return;
            }
            initNodeInstance(stratNodeId,nextNodeId,transitionRule.getStopNodeId(),flowInstance.getId());
        }
    }

    private SpFlowInstance insertSpFlowInstance(Long applyId,Long flowId,Long nextNodeId){
        SpFlowInstance spFlowInstance = new SpFlowInstance();
        spFlowInstance.setId(sequencesService.getSequences(SequencesEnum.SP_FLOW_INSTANCE));
        spFlowInstance.setApplyId(applyId);
        spFlowInstance.setFlowId(flowId);
        spFlowInstance.setNextNodeId(nextNodeId);
        spFlowInstance.setStatus(FlowInstanceStatusEnum.开启流程.getCode());
        spFlowInstanceDao.insert(spFlowInstance);
        return spFlowInstance;
    }

    private void insertSpFlowTransitionInfo(SpFlowTransitionInfo transitionInfo){
        transitionInfo.setId(sequencesService.getSequences(SequencesEnum.SP_FLOW_TRANSITION_INFO));
        transitionInfo.setUpdateTime(new Date());
        spFlowTransitionInfoDao.insert(transitionInfo);
    }

    private void startFirstNodeInstance(Long  previousNodeId,Long currentNodeId,Long nextNodeId,Long flowInstanceId){
        SpFlowTransitionInfo spFlowTransitionInfo = new SpFlowTransitionInfo();
        spFlowTransitionInfo.setPreviousNodeId(previousNodeId);
        spFlowTransitionInfo.setNodeId(currentNodeId);
        spFlowTransitionInfo.setNextNodeId(nextNodeId == 0L ? 0L : nextNodeId);
        spFlowTransitionInfo.setFlowInstanceId(flowInstanceId);
        spFlowTransitionInfo.setStatus(FlowNodeTransitionStatusEnum.发起申请.getCode());
        User user = UserContext.getUser();
        spFlowTransitionInfo.setProposerId(user.getId());
        insertSpFlowTransitionInfo(spFlowTransitionInfo);
    }
    private void initNodeInstance(Long  previousNodeId,Long currentNodeId,Long nextNodeId,Long flowInstanceId){
        SpFlowTransitionInfo spFlowTransitionInfo = new SpFlowTransitionInfo();
        spFlowTransitionInfo.setPreviousNodeId(previousNodeId == 0L ? 0L : previousNodeId);
        spFlowTransitionInfo.setNodeId(currentNodeId);
        spFlowTransitionInfo.setNextNodeId(nextNodeId == 0L ? 0L : nextNodeId);
        spFlowTransitionInfo.setFlowInstanceId(flowInstanceId);
        spFlowTransitionInfo.setStatus(FlowNodeTransitionStatusEnum.审批中.getCode());
        insertSpFlowTransitionInfo(spFlowTransitionInfo);
    }
    /**
     * 一个流程节点实例执行完成
     * @param transitionInfo
     * @return
     */
    private int nodeInstanceProcessingFinish(SpFlowTransitionInfo transitionInfo){
        User userInfo = UserContext.getUser();
        transitionInfo.setStatus(FlowNodeTransitionStatusEnum.审批通过.getCode());
        transitionInfo.setUpdateTime(new Date());
        transitionInfo.setProposerId(userInfo.getId());
        int count = spFlowTransitionInfoDao.update(transitionInfo);
        return count;
    }
    /**
     * 一个流程节点实例执行失败
     * @param transitionInfo
     * @return
     */
    private int nodeInstanceProcessingFail(SpFlowTransitionInfo transitionInfo){
        User userInfo = UserContext.getUser();
        transitionInfo.setStatus(FlowNodeTransitionStatusEnum.审批拒绝.getCode());
        transitionInfo.setUpdateTime(new Date());
        transitionInfo.setProposerId(userInfo.getId());
        int count = spFlowTransitionInfoDao.update(transitionInfo);
        return count;
    }

    private void flowInstanceFinish(SpFlowInstance flowInstance) {
        flowInstance.setFinishTime(new Date());
        flowInstance.setStatus(FlowInstanceStatusEnum.结束流程.getCode());
        spFlowInstanceDao.update(flowInstance);
    }
    @Override
    @Transactional(propagation= Propagation.REQUIRED)
    public void moveNodeInstanceAction(Long flowInstanceId,FlowNodeTransitionStatusEnum nodeStatus,String memo) {
        SpFlowTransitionInfo transitionInfo = spFlowTransitionInfoDao.queryProcessingSpFlowTransitionInfo(flowInstanceId);
        SpFlowInstance flowInstance = spFlowInstanceDao.get(flowInstanceId);
        if (FlowNodeTransitionStatusEnum.审批拒绝.getCode().equals(nodeStatus.getCode())) {
            nodeInstanceProcessingFail(transitionInfo);
            specialRepaymentApplyService.updateSpecialRepaymentApply(flowInstance.getApplyId(), SpecialRepaymentApplyStatus.拒绝.getCode(),memo);
            return;
        }
        //审批通过成功处理完节点实例
        nodeInstanceProcessingFinish(transitionInfo);
        //最后一个节点审批通过 流程结束 申请减免通过
        if ( 0L == transitionInfo.getNextNodeId()) {
            flowSuccessTransitionFinish(flowInstance);
            return;
        }
        Long currentNodeId = transitionInfo.getNextNodeId();
        Long  previousNodeId = transitionInfo.getNodeId();
        SpFlowTransitionRule transitionRule = spFlowTransitionRuleDao.queryFlowNodeRuleByFlowIdStartNodeId(flowInstance.getFlowId(),currentNodeId);
        Long nextNodeId = transitionRule.getStopNodeId();
        if (NodeActionTypeEnum.自动.getCode().equals(transitionRule.getIsAuto())) {
            moveAutoNodeInstanceAction(transitionRule,flowInstanceId);
            return;
        }
        //初始下一个节点实例
        initNodeInstance(previousNodeId,currentNodeId,nextNodeId,flowInstanceId);
        flowInstance.setNextNodeId(nextNodeId);
        spFlowInstanceDao.update(flowInstance);
    }

    /**
     *节点实例自动移动
     * @param previousNodeId
     * @param currentNodeId
     * @param nextNodeId
     */
    private void nodeInstanceAutoMove(Long  previousNodeId,Long currentNodeId,Long nextNodeId,Long flowInstanceId){
        SpFlowTransitionInfo spFlowTransitionInfo = new SpFlowTransitionInfo();
        spFlowTransitionInfo.setPreviousNodeId(previousNodeId == 0L ? 0L : previousNodeId);
        spFlowTransitionInfo.setNodeId(currentNodeId);
        spFlowTransitionInfo.setNextNodeId(nextNodeId == 0L ? 0L : nextNodeId);
        spFlowTransitionInfo.setFlowInstanceId(flowInstanceId);
        spFlowTransitionInfo.setStatus(FlowNodeTransitionStatusEnum.审批通过.getCode());
        spFlowTransitionInfo.setUpdator("admin");
        spFlowTransitionInfo.setUpdateTime(new Date());
        insertSpFlowTransitionInfo(spFlowTransitionInfo);
    };

    /**
     * 流转自动节点实例
     * @param transitionRule -- 此节点为自动节点
     */
    private void moveAutoNodeInstanceAction(SpFlowTransitionRule transitionRule,Long flowInstanceId){
        List<SpFlowTransitionRule> rules = spFlowTransitionRuleDao.querySpFlowTransitionRuleByFlowId(transitionRule.getFlowId());;
        boolean flag = false;
        SpFlowTransitionRule tempRule = null;
        SpFlowInstance flowInstance = spFlowInstanceDao.get(flowInstanceId);
        for(int i = 0;i < rules.size();i++) {
            SpFlowTransitionRule rule = rules.get(i);
            if (!flag) {
                if (rule.getStartNodeId().equals(transitionRule.getStartNodeId())) {
                    //此节点在为自动节点
                    tempRule = rules.get(i-1);
                    nodeInstanceAutoMove(tempRule.getStartNodeId(),rule.getStartNodeId(),rule.getStopNodeId(),flowInstance.getId());
                    flag = true;
                    if (0L == rule.getStopNodeId()) {
                        //最后一个审批环节节点 为自动节点
                        flowSuccessTransitionFinish(flowInstance);
                        return;
                    }
                }
                continue;
            }
            tempRule = rules.get(i-1);
            if (NodeActionTypeEnum.手动.getCode().equals(rule.getIsAuto())) {
                //初始下一个节点实例
                initNodeInstance(tempRule.getStartNodeId(),rule.getStartNodeId(),rule.getStopNodeId(),flowInstance.getId());
                flowInstance.setNextNodeId(rule.getStopNodeId());
                spFlowInstanceDao.update(flowInstance);
                return;
            }
            nodeInstanceAutoMove(tempRule.getStartNodeId(),rule.getStartNodeId(),rule.getStopNodeId(),flowInstance.getId());
            if (0L == rule.getStopNodeId()) {
                //最后一个审批环节节点 为自动节点
                flowSuccessTransitionFinish(flowInstance);
                return;
            }
        }
    }

    private void flowSuccessTransitionFinish(SpFlowInstance flowInstance){
        flowInstanceFinish(flowInstance);
        specialRepaymentApplyService.updateSpecialRepaymentApply(flowInstance.getApplyId(), SpecialRepaymentApplyStatus.通过.getCode(),"");
        //减免审批通过  O元入账
        appleReliefPassInvokingRepayIuput(flowInstance.getApplyId());
    }
    /**
     * 审批通过时 调用申请减免是否零元入账处理
     * @param applyId
     */
    public void appleReliefPassInvokingRepayIuput(Long applyId){
        User userInfo = UserContext.getUser();
        SpecialRepaymentApply specialRepayment = specialRepaymentApplyService.getSpecialRepaymentApplyById(applyId);
        Long loanId = specialRepayment.getLoanId();
        /** 还款录入Vo **/
        RepaymentInputVo repaymentInputVo = new RepaymentInputVo();
        repaymentInputVo.setLoanId(loanId);
        repaymentInputVo.setAmount(new BigDecimal(0.0));
        repaymentInputVo.setMemo("");
        repaymentInputVo.setOrgan(userInfo.getOrgCode());
        repaymentInputVo.setTeller(userInfo.getUserCode());
        repaymentInputVo.setTradeDate(Dates.getCurrDate());
        repaymentInputVo.setTradeType("现金");
        offerRepayInfoService.dealRepaymentInput(repaymentInputVo);
    }

    @Override
    public SpFlowInstance querySpFlowInstanceByApplyId(Long applyId) {
        return spFlowInstanceDao.querySpFlowInstanceByApply(applyId);
    }

    @Override
    public List<SpFlowTransitionRule> querySpFlowTransitionRulesByApplyId(Long applyId) {
        SpFlowInstance flowInstance = querySpFlowInstanceByApplyId(applyId);
        if (flowInstance == null) {
            return Collections.emptyList();
        }
        List<SpFlowTransitionRule> rules = spFlowTransitionRuleDao.querySpFlowTransitionRuleByFlowId(flowInstance.getFlowId());;
        return rules;
    }

    @Override
    public List<SpFlowOperateInfoVo> queryOperateInfos(Long applyId, Long flowInstanceId) {
        List<SpFlowOperateInfoVo> operateInfoVos = spFlowTransitionInfoDao.querySpFlowTransitionInfosByFlowInstanceId(flowInstanceId);
        for(SpFlowOperateInfoVo operateInfoVo:operateInfoVos) {
            if (FlowNodeTransitionStatusEnum.审批通过.getCode().equals(operateInfoVo.getOperateStatus())) {
                operateInfoVo.setOperateStatus(FlowNodeTransitionStatusEnum.审批通过.getValue());
            }
            if (FlowNodeTransitionStatusEnum.审批拒绝.getCode().equals(operateInfoVo.getOperateStatus())) {
                operateInfoVo.setOperateStatus(FlowNodeTransitionStatusEnum.审批拒绝.getValue());
            }
            if (FlowNodeTransitionStatusEnum.发起申请.getCode().equals(operateInfoVo.getOperateStatus())) {
                operateInfoVo.setOperateStatus(FlowNodeTransitionStatusEnum.发起申请.getValue());
            }
            if (FlowNodeTransitionStatusEnum.审批中.getCode().equals(operateInfoVo.getOperateStatus())) {
                operateInfoVo.setOperateStatus(FlowNodeTransitionStatusEnum.审批中.getValue());
            }
            if (Strings.isEmpty(operateInfoVo.getUsercode())) {
                operateInfoVo.setUsercode("System");
                operateInfoVo.setOperateName("系统");
            }
        }
        SpecialRepaymentApply repaymentApply = specialRepaymentApplyDao.get(applyId);
        if (SpecialRepaymentApplyStatus.生效.getCode().equals(repaymentApply.getApplicationStatus())) {
            SpFlowOperateInfoVo flowOperateInfoVo = new SpFlowOperateInfoVo();
            flowOperateInfoVo.setUsercode("System");
            flowOperateInfoVo.setOperateName("系统");
            flowOperateInfoVo.setOperateDate(repaymentApply.getUpdateTime());
            flowOperateInfoVo.setOperateStatus(SpecialRepaymentApplyStatus.生效.getValue());
            flowOperateInfoVo.setMemo("减免入账");
            operateInfoVos.add(flowOperateInfoVo);
        }
        return operateInfoVos;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED)
    public void cancelReliefApplyFlowInstanceByApplyId(Long applyId) {
        SpFlowInstance flowInstance = querySpFlowInstanceByApplyId(applyId);
        flowInstance.setStatus(FlowInstanceStatusEnum.取消流程.getCode());
        spFlowInstanceDao.update(flowInstance);
        spFlowTransitionInfoDao.updateStatusCancel(flowInstance.getId());
    }

    @Override
    public Pager queryNodeEmployeePage(Map<String, Object> params) {
        return spNodeEmployeeDao.queryNodeEmployeePage(params);
    }

    @Override
    public List<SpNodeInfo> querySpNodeInfos() {
        return spNodeInfoDao.findAllList();
    }

    @Override
    public List<SpNodeInfo> querySpApplyNodeInfos() {
        return spNodeInfoDao.querySpApplyNodeInfos();
    }

    @Override
    public void insertNodeEmployee(Long employeeId, Long nodeId) {
        SpNodeEmployee nodeEmployee = new SpNodeEmployee();
        nodeEmployee.setId(sequencesService.getSequences(SequencesEnum.SP_NODE_EMPLOYEE));
        nodeEmployee.setNodeId(nodeId);
        nodeEmployee.setEmployeeId(employeeId);
        spNodeEmployeeDao.insert(nodeEmployee);
    }

    @Override
    public void deleteNodeEmployee(Long id) {
        SpNodeEmployee nodeEmployee = new SpNodeEmployee();
        nodeEmployee.setId(id);
        nodeEmployee.setStatus(NodeEmployeeEnum.无效.getCode());
        spNodeEmployeeDao.update(nodeEmployee);
    }

    @Override
    public SpNodeEmployee querySpNodeEmployee(Long nodeId, Long employeeId) {
        return spNodeEmployeeDao.querySpNodeEmployee(nodeId,employeeId);
    }

    @Override
    public void updateNodeEmployee(EmployeeRoleVo employeeRoleVo) {
        Long employeeId = employeeRoleVo.getEmployeeId();
        List<SpNodeRole> nodeRoles = spNodeRoleDao.querySpNodeRole(employeeRoleVo.getRoleId());
        if (CollectionUtils.isEmpty(nodeRoles)) {
            return;
        }
        for(SpNodeRole nodeRole:nodeRoles){
            Long nodeId = nodeRole.getNodeId();
           SpNodeEmployee nodeEmployee = spNodeEmployeeDao.querySpNodeEmployee(nodeId, employeeId);
            if (nodeEmployee == null) {
                insertNodeEmployee(employeeId,nodeId);
            }
        }
    }
}
