package com.zdmoney.credit.system.dao.pub;

import java.util.List;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.system.domain.SysDictionary;


public interface SysDictionaryDao extends IBaseDao<SysDictionary>{
	
	/**
	 * 根据codeType和codeName查找
	 * @param codeType
	 * @param codeName
	 * @return
	 */
	public SysDictionary findByCodeTypeAndCodeName(String codeType, String codeName);
	
	/**
	 * 查找全部
	 * @return
	 */
	public List<SysDictionary> findSysDictionaryAllList();

	public SysDictionary getIsThere(SysDictionary sysDictionary);
	public Pager findWithPg(SysDictionary sysDictionary);
	/**
	 * 根据codeType
	 * @param codeType
	 * @author 00236633
	 * @return
	 */
	public List<SysDictionary> findByCodeType(String codeType);
	
	/**
	 * 根据指定条件查询字典数据
	 * @param sysDictionary
	 * @return
	 */
	public List<SysDictionary> findDictionaryListByVo(SysDictionary sysDictionary);
}