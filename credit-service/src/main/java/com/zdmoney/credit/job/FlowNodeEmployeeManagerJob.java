package com.zdmoney.credit.job;

import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.flow.dao.pub.ISpNodeEmployeeDao;
import com.zdmoney.credit.flow.domain.SpNodeRole;
import com.zdmoney.credit.flow.service.pub.ISpFlowService;
import com.zdmoney.credit.flow.vo.EmployeeRoleVo;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ym10094 on 2017/9/6.
 */
@Service
public class FlowNodeEmployeeManagerJob {
    private static final Logger logger = LoggerFactory.getLogger(FlowNodeEmployeeManagerJob.class);

    @Autowired
    private ISysParamDefineService sysParamDefineService;
    @Autowired
    private ISpNodeEmployeeDao nodeEmployeeDao;
    @Autowired
    private ISpFlowService spFlowService;
    public void execute(){
        //开关设置
        String isFlowNodeEmployeeManagerJob = sysParamDefineService.getSysParamValue("sysJob", "isFlowNodeEmployeeManagerJob");
        if (Strings.isEmpty(isFlowNodeEmployeeManagerJob) || "0".equals(isFlowNodeEmployeeManagerJob)) {
            return;
        }
        nodeEmployeeDao.batchDeleteEmployeeRole();
        List<EmployeeRoleVo> employeeRoleVos = nodeEmployeeDao.queryEmployeeRoleInfos();
        for(EmployeeRoleVo employeeRoleVo:employeeRoleVos){
            spFlowService.updateNodeEmployee(employeeRoleVo);
        }
    }
}
