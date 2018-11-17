package com.zdmoney.credit.flow.dao;

import com.zdmoney.credit.common.constant.flow.NodeEmployeeEnum;
import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.flow.dao.pub.ISpNodeEmployeeDao;
import com.zdmoney.credit.flow.domain.SpNodeEmployee;
import com.zdmoney.credit.flow.vo.EmployeeRoleVo;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ym10094 on 2017/8/2.
 */
@Repository
public class SpNodeEmployeeDaoImpl extends BaseDaoImpl<SpNodeEmployee> implements ISpNodeEmployeeDao {

    @Override
    public List<SpNodeEmployee> querySpNodeEmployeeByEmployeeId(Long employeeId) {
        Map<String,Object> params = new HashMap<>();
        params.put("employeeId",employeeId);
        List<SpNodeEmployee> spNodeEmployees = findListByMap(params);
        if (CollectionUtils.isEmpty(spNodeEmployees)) {
            return Collections.emptyList();
        }
        return spNodeEmployees;
    }

    @Override
    public Pager queryNodeEmployeePage(Map<String, Object> params) {
        Pager pager = (Pager) params.get("pager");
        if (pager == null) {
            pager = new Pager();
        }
        pager.setSearchDataSqlMapId(getIbatisMapperNameSpace() + ".queryNodeEmployeeInfosDetail");
        pager.setSearchCountSqlMapId(getIbatisMapperNameSpace() + ".queryNodeEmployeeInfosCount");
        return doPager(pager,params);
    }

    @Override
    public List<SpNodeEmployee> queryApplySpNodeEmployeeByEmployeeId(Long employeeId) {
        return getSqlSession().selectList(getIbatisMapperNameSpace() + ".queryApplySpNodeEmployeeByEmployeeId",employeeId);
    }

    @Override
    public SpNodeEmployee querySpNodeEmployee(Long nodeId, Long employeeId) {
        Map<String,Object> params = new HashMap<>();
        params.put("nodeId",nodeId);
        params.put("employeeId",employeeId);
        params.put("status", NodeEmployeeEnum.有效.getCode());
        List<SpNodeEmployee> spNodeEmployees = findListByMap(params);
        if (CollectionUtils.isEmpty(spNodeEmployees)) {
            return null;
        }
        return spNodeEmployees.get(0);
    }

    @Override
    public List<EmployeeRoleVo> queryEmployeeRoleInfos() {
        return getSqlSession().selectList(getIbatisMapperNameSpace() + ".queryEmployeeRoleInfos");
    }

    public void batchDeleteEmployeeRole(){
        this.getSqlSession().update(getIbatisMapperNameSpace() + ".batchDeleteEmployeeRole");
    }
}
