package com.zdmoney.credit.xingtuo.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.xingtuo.dao.pub.IXintuoDataDao;
import com.zdmoney.credit.xintuo.domain.XintuoDataDomain;
@Repository
public class XintuoDaoImpl extends BaseDaoImpl<XintuoDataDomain> implements IXintuoDataDao{

	@Override
	public List<XintuoDataDomain> getXintuoDataJob(Map<String, Object> params) {
		List<XintuoDataDomain> result = getSqlSession().selectList(this.getIbatisMapperNameSpace() + ".getXintuoDataJob", params);
		return result;
	}


}
