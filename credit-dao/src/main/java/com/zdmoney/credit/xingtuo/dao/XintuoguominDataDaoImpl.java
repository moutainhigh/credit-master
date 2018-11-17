package com.zdmoney.credit.xingtuo.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.xingtuo.dao.pub.IXintuoguominDataDao;
import com.zdmoney.credit.xintuo.domain.XintuoguominDataDomain;

@Repository
public class XintuoguominDataDaoImpl extends BaseDaoImpl<XintuoguominDataDomain> implements IXintuoguominDataDao{

	@Override
	public List<XintuoguominDataDomain> getXintuoguominDataJob() {
		List<XintuoguominDataDomain> result = getSqlSession().selectList(this.getIbatisMapperNameSpace() + ".getXintuoguominDataJob");
		return result;
	}
	
	/**
	 * 渤海信托相关债权信息查询
	 */
	@Override
	public List<XintuoguominDataDomain> getXintuobohaiDataJob() {
		List<XintuoguominDataDomain> result = getSqlSession().selectList(this.getIbatisMapperNameSpace() + ".getXintuobohaiDataJob");
		return result;
	}

}
