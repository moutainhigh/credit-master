package com.zdmoney.credit.system.service;





import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.system.dao.pub.ISysParamDefineDao;
import com.zdmoney.credit.system.domain.SysParamDefine;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;

@Service
public class SysParamDefineServiceImpl implements ISysParamDefineService{
	
	/**
	 * 缓存参数，key为magicType+param_key
	 */
	private static Map<String, String> cacheMap = new HashMap<String, String>();
	
	@Autowired
	ISysParamDefineDao sysParamDefineDao;
	
	/**
	 * 可以缓存一些配置
	 */
    public void init() {
        cacheMap.clear();
       /* List<SysParam> spList = sysParamService.getSysParams(0, 200, "PEPS").getPageItems();
        for (SysParam sp : spList) {
            pepsMap.put(sp.getSysParamKey().getMagicId(), sp.getParamValue());
        }*/
    }

	@Override
	public String getSysParamValue(String magicType, String param_key) {
		
	    return sysParamDefineDao.getSysParamValue(magicType, param_key, false);
	}

	@Override
	public String getSysParamValueCache(String magicType, String param_key) {
		String value = cacheMap.get(magicType+param_key);
		if(Strings.isEmpty(value)){
			String val = sysParamDefineDao.getSysParamValue(magicType, param_key, false);
			cacheMap.put(magicType+param_key, val);
			return val;
		}
		
		return value;
	}

	@Override
	public int updateSysParamValue(String magicType, String param_key,
			String param_value) {
		SysParamDefine domain = new SysParamDefine();
		domain.setMagicType(magicType);
		domain.setParamKey(param_key);
		domain.setParamValue(param_value);
		
		int result = sysParamDefineDao.updateByMagictypeAndKey(domain);
		cacheMap.put(magicType+param_key, param_value);
		return result;
	}
    
	

}
