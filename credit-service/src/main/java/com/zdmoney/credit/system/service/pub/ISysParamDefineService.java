package com.zdmoney.credit.system.service.pub;

public interface ISysParamDefineService {
	
	/**
	 * 获取系统参数实时
	 * @param paramId
	 * @param magicId
	 * @return
	 */
	public String getSysParamValue(String magicType, String param_key);

	/**
	 * 获取带缓存的系统参数
	 * @param magicType
	 * @param param_key
	 * @return
	 */
	public String getSysParamValueCache(String magicType, String param_key);

	/**
	 * 更新系统参数
	 * @param magicType
	 * @param param_key
	 * @param param_value
	 * @return
	 */
	public int updateSysParamValue(String magicType, String param_key, String param_value);
}
