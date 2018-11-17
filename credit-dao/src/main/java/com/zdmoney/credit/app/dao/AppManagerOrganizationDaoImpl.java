package com.zdmoney.credit.app.dao;


import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.app.dao.pub.IAppManagerOrganizationDao;
import com.zdmoney.credit.app.domain.AppOrganizationManager;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;
@Repository
public class AppManagerOrganizationDaoImpl extends BaseDaoImpl<AppOrganizationManager> implements
		IAppManagerOrganizationDao {


	@Override
	public Pager findAppManagerOrganizationListWithPg(
			Map<String, Object> paramMap) {
		Pager pager = (Pager) paramMap.get("pager");
		if(pager == null){
			pager = new Pager();
		}
		pager.setSearchDataSqlMapId(getIbatisMapperNameSpace() + ".findListWithPGByMap");
		pager.setSearchCountSqlMapId(getIbatisMapperNameSpace() + ".findCountByMap");
		return doPager(pager,paramMap);
	}
	/**
	 * 根据营业部ID查询APP配置信息
	 * @param id
	 * @return
	 */
	@Override
	public  AppOrganizationManager selectAppOrganizationManagerByOrgId(Long id){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("orgId", id);
		return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".selectAppOrganizationManagerByOrgId", paramMap);
	}

	
}
