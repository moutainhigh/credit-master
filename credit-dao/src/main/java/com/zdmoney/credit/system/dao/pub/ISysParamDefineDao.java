package com.zdmoney.credit.system.dao.pub;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.system.domain.SysParamDefine;

public interface ISysParamDefineDao extends IBaseDao<SysParamDefine>{

	/**
	 * 
	 * @param paramId
	 * @param magicId
	 * @param b
	 * @return
	 */
	public String getSysParamValue(String magicType, String param_key, boolean b);
	
	public int updateByMagictypeAndKey(SysParamDefine param);

}
