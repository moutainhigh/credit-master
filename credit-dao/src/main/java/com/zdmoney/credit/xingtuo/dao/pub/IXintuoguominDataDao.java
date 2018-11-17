package com.zdmoney.credit.xingtuo.dao.pub;

import java.util.List;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.xintuo.domain.XintuoguominDataDomain;

public interface IXintuoguominDataDao extends IBaseDao<XintuoguominDataDomain>{
	
	public List<XintuoguominDataDomain> getXintuoguominDataJob();
	public List<XintuoguominDataDomain> getXintuobohaiDataJob();
}
