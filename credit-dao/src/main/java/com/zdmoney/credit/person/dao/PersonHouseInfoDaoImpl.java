package com.zdmoney.credit.person.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.person.dao.pub.IPersonHouseInfoDao;
import com.zdmoney.credit.person.domain.PersonHouseInfo;

/**
 * 借款人房产信息Dao接口层(实现)
 * @author Ivan
 *
 */
@Repository
public class PersonHouseInfoDaoImpl extends BaseDaoImpl<PersonHouseInfo> implements IPersonHouseInfoDao {

	/**
	 * 根据personId查找
	 * @param personId
	 * @return
	 */
	@Override
	public PersonHouseInfo findByPersonId(Long personId) {
		PersonHouseInfo personHouseInfo = new PersonHouseInfo();
		personHouseInfo.setPersonId(personId);
		List<PersonHouseInfo> houses = findListByVo(personHouseInfo);
		PersonHouseInfo house = null;
		if (null != houses && !houses.isEmpty() && houses.size() > 0) {
			house = houses.get(0);
		}
		return house;
	}
	
}
