package com.zdmoney.credit.system.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.common.util.Assert;
import com.zdmoney.credit.common.util.BeanUtils;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.system.dao.pub.SysDictionaryDao;
import com.zdmoney.credit.system.domain.SysDictionary;

/**
 * 
 * @author Ivan
 *
 */
@Repository
public class SysDictionaryDaoImpl extends BaseDaoImpl<SysDictionary> implements
		SysDictionaryDao {

	/**
	 * 数据库基本操作，对应*mapper.xml中的id
	 */
	private static final String FINDSYSDICTIONARYALLLIST = ".findSysDictionaryAllList";
	private static final String FINDBYCODETYPEANDCODENAME = ".findByCodeTypeAndCodeName";

	/**
	 * 根据codeType和codeName查找
	 * 
	 * @param codeType
	 * @param codeName
	 * @return
	 */
	@Override
	public SysDictionary findByCodeTypeAndCodeName(String codeType, String codeName) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("codeType", codeType);
		paramMap.put("codeName", codeName);
		List<SysDictionary> dictionaries = getSqlSession().selectList(getIbatisMapperNameSpace() + FINDBYCODETYPEANDCODENAME, paramMap);
		SysDictionary dic = null;
		if (null != dictionaries && !dictionaries.isEmpty()
				&& dictionaries.size() == 1) {
			dic = dictionaries.get(0);
		}

		return dic;
	}

	/**
	 * 查找全部
	 * 
	 * @return
	 */
	@Override
	public List<SysDictionary> findSysDictionaryAllList() {
		return getSqlSession().selectList(
				getIbatisMapperNameSpace() + FINDSYSDICTIONARYALLLIST);
	}

	@Override
	public SysDictionary getIsThere(SysDictionary sysDictionary) {
		// TODO Auto-generated method stub
		return getSqlSession().selectOne(
				getIbatisMapperNameSpace() + ".getIsThere", sysDictionary);
	}

	/**
	 * 根据codeType
	 * 
	 * @param codeType
	 * @author 00236633
	 * @return
	 */
	@Override
	public List<SysDictionary> findByCodeType(String codeType) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("codeType", codeType);
		List<SysDictionary> dictionaries =  getSqlSession().selectList(getIbatisMapperNameSpace() + ".findDictionaryListByMap", paramMap);
		return dictionaries;
	}

	@Override
	public Pager findWithPg(SysDictionary sysDictionary) {
		Pager pager = sysDictionary.getPager();
		Assert.notNull(pager, "分页参数");
		if(sysDictionary.getFlage().equals("f")){
			
			pager.setSearchDataSqlMapId(getIbatisMapperNameSpace() + ".findWithPG");
			pager.setSearchCountSqlMapId(getIbatisMapperNameSpace() + ".count");
		}else
		{
			pager.setSearchDataSqlMapId(getIbatisMapperNameSpace() + ".findWithPGParent");
			pager.setSearchCountSqlMapId(getIbatisMapperNameSpace() + ".countParent");
		}
		
		return doPager(pager, BeanUtils.toMap(sysDictionary));
	}

	public List<SysDictionary> findDictionaryListByVo(SysDictionary sysDictionary) {
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".findDictionaryListByVo",sysDictionary);
	}
}
