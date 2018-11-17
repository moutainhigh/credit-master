package com.zdmoney.credit.test.service;

import com.zdmoney.credit.common.constant.SpecialRepaymentApplyTeyps;
import com.zdmoney.credit.common.constant.flow.ConditionTypeEnum;
import com.zdmoney.credit.common.constant.flow.DepartmentTypeEnum;
import com.zdmoney.credit.common.constant.flow.OverDueGradeEnum;
import com.zdmoney.credit.common.util.coreUtil.JSONUtil;
import com.zdmoney.credit.flow.dao.pub.ISpFlowTransitionRuleDao;
import com.zdmoney.credit.flow.domain.SpFlowApplyCondition;
import com.zdmoney.credit.flow.domain.SpFlowTransitionRule;
import com.zdmoney.credit.flow.service.pub.ISpFlowService;
import com.zdmoney.credit.flow.vo.SpFlowApplyConditionQueryVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ym10094 on 2017/8/2.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/spring/*.xml"})
public class SpFlowServiceTest {
    private final Logger logger = LoggerFactory.getLogger(SpFlowServiceTest.class);
    @Autowired
    private ISpFlowTransitionRuleDao spFlowTransitionRuleDao;
    @Autowired
    private ISpFlowService spFlowService;

    @Test
    public void queryFlowTransitionRule(){
        List<SpFlowTransitionRule> spFlowTransitionRules = spFlowTransitionRuleDao.querySpFlowTransitionRuleByFlowId(1L);
        System.out.println(JSONUtil.toJSON(spFlowTransitionRules));
        for (SpFlowTransitionRule rule:spFlowTransitionRules){
            System.out.println("节点id："+ rule.getStartNodeId()+"-----------节点名字:"+rule.getStartNodeName());
        }
    }

    @Test
    public void queryFlowApplyConditions(){
        List<SpFlowApplyConditionQueryVo> queryVos = new ArrayList<>();
        SpFlowApplyConditionQueryVo queryVo1 = new SpFlowApplyConditionQueryVo(ConditionTypeEnum.逾期等级.getCode(),OverDueGradeEnum.A.getCode());
        SpFlowApplyConditionQueryVo queryVo2 = new SpFlowApplyConditionQueryVo(ConditionTypeEnum.营业部门.getCode(), DepartmentTypeEnum.营业部.getValue());
        SpFlowApplyConditionQueryVo queryVo3 = new SpFlowApplyConditionQueryVo(ConditionTypeEnum.减免类型.getCode(), SpecialRepaymentApplyTeyps.一般减免.getCode());
        SpFlowApplyConditionQueryVo queryVo4 = new SpFlowApplyConditionQueryVo(ConditionTypeEnum.流程首节点.getCode(),"1");
        queryVos.add(queryVo1);
        queryVos.add(queryVo2);
        queryVos.add(queryVo3);
        queryVos.add(queryVo4);
        List<SpFlowApplyCondition> spFlowApplyConditions = spFlowService.queryFlowApplyConditions(queryVos);
        Long flowId = spFlowService.querySpFlowId(spFlowApplyConditions);
        System.out.println("启动流程：" + flowId);
    }
}
