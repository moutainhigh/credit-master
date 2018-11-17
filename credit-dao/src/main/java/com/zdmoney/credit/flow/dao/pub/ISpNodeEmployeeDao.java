package com.zdmoney.credit.flow.dao.pub;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.flow.domain.SpNodeEmployee;
import com.zdmoney.credit.flow.vo.EmployeeRoleVo;
import com.zdmoney.credit.framework.dao.pub.IBaseDao;

import java.util.List;
import java.util.Map;

/**
 * Created by ym10094 on 2017/8/2.
 */
public interface ISpNodeEmployeeDao extends IBaseDao<SpNodeEmployee> {

    public List<SpNodeEmployee> querySpNodeEmployeeByEmployeeId(Long employeeId);

    public Pager queryNodeEmployeePage(Map<String,Object> params);

    /**
     * 查询员工分配有哪些申请环节
     * @param employeeId
     * @return
     */
    public List<SpNodeEmployee> queryApplySpNodeEmployeeByEmployeeId(Long employeeId);

    public SpNodeEmployee querySpNodeEmployee(Long nodeId,Long employeeId);

    public List<EmployeeRoleVo> queryEmployeeRoleInfos();

    public void batchDeleteEmployeeRole();
}
