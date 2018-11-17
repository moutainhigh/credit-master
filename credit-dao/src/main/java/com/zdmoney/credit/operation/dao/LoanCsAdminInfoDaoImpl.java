package com.zdmoney.credit.operation.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.operation.dao.pub.ILoanContractInfoDao;
import com.zdmoney.credit.operation.dao.pub.ILoanCsAdminInfoDao;
import com.zdmoney.credit.operation.domain.LoanContractInfo;
import com.zdmoney.credit.operation.domain.LoanCsAdminInfo;

@Repository
public class LoanCsAdminInfoDaoImpl extends BaseDaoImpl<LoanCsAdminInfo> implements ILoanCsAdminInfoDao {

    public List<Map<String, Object>> getCrmOptionInfo(Map<String, Object> params) {
        return getSqlSession().selectList(getIbatisMapperNameSpace() + ".getCrmOptionInfo",params);
    }

	@Override
	public Pager getCollectors(Map<String,Object> params) {
		Pager pager = (Pager)params.get("pager");
        if (pager == null) {
            pager = new Pager();
        }
        pager.setSearchDataSqlMapId(getIbatisMapperNameSpace() + ".getCollectors");
        pager.setSearchCountSqlMapId(getIbatisMapperNameSpace() + ".countByCollectorsMap");
        return doPager(pager,params);
	}
}
