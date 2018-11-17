package com.zdmoney.credit.app.dao;


import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.app.dao.pub.IAppEmployeeManagerDao;
import com.zdmoney.credit.app.domain.AppEmployeeManager;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;

@Repository
public class AppEmployeeManagerDaoImpl extends BaseDaoImpl<AppEmployeeManager> implements IAppEmployeeManagerDao {


	@Override
	public Pager findAppEmployeeManagerListWithPg(Map<String, Object> paramMap) {
		Pager pager = (Pager) paramMap.get("pager");
		if(pager == null){
			pager = new Pager();
		}
		pager.setSearchDataSqlMapId(getIbatisMapperNameSpace() + ".findListWithPGByMap");
		pager.setSearchCountSqlMapId(getIbatisMapperNameSpace() + ".findCountByMap");
		return doPager(pager,paramMap);
	}

	/**
	 * 根据员工id查询App登录配置信息
	 */
	@Override
	public AppEmployeeManager selectAppEmployeeManagerByEmployeeId(Long id){
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("employeeId", id);
		return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".selectAppEmployeeManagerByEmployeeId", params);
	}
	

}
