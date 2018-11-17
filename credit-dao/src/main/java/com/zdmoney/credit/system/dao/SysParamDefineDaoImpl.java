package com.zdmoney.credit.system.dao;


import java.util.List;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.system.dao.pub.ISysParamDefineDao;
import com.zdmoney.credit.system.domain.SysParamDefine;
@Repository
public class SysParamDefineDaoImpl extends BaseDaoImpl<SysParamDefine> implements ISysParamDefineDao{

	@Override
	public String getSysParamValue(String magicType, String paramKey, boolean b) {

		SysParamDefine sysParamDefine = new SysParamDefine();
		sysParamDefine.setMagicType(magicType);
		sysParamDefine.setParamKey(paramKey);
		
		List<SysParamDefine> list = findListByVo(sysParamDefine);
		if(list!=null && list.size()>0){
			return list.get(0).getParamValue();
		}
		
		return null;
	}

	@Override
	public int updateByMagictypeAndKey(SysParamDefine param) {
		return getSqlSession().update(getIbatisMapperNameSpace() + ".updateByMagictypeAndKey", param);
	}

	

}
