package com.zdmoney.credit.xingtuo.dao.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.xintuo.domain.XintuoDataDomain;

public interface IXintuoDataDao extends IBaseDao<XintuoDataDomain>{
	
	public List<XintuoDataDomain> getXintuoDataJob(Map<String, Object> params);


}
