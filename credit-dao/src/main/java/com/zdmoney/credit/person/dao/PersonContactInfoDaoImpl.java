package com.zdmoney.credit.person.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.person.dao.pub.IPersonContactInfoDao;
import com.zdmoney.credit.person.domain.PersonContactInfo;

/**
 * 联系人信息Dao接口层(实现)
 * @author Ivan
 *
 */
@Repository
public class PersonContactInfoDaoImpl extends BaseDaoImpl<PersonContactInfo> implements IPersonContactInfoDao {
	
	/**
	 * 数据库基本操作，对应*mapper.xml中的id
	 */
	private static final String DELETEBYPERSONID = ".deleteByPersonId";
	
	
	/**
	 * 根据 personId删除联系人信息
	 * @param contacts
	 */
	@Override
	public void deleteByPersonId(Long personId) {
		this.getSqlSession().update(getIbatisMapperNameSpace() + DELETEBYPERSONID, personId);
	}


	@Override
	public List<Map<String, Object>> findContactInfo4LXXD(Map<String, Object> params) {
		return getSqlSession().selectList(getIbatisMapperNameSpace()+".findContactInfo4LXXD",params);
	}
}
