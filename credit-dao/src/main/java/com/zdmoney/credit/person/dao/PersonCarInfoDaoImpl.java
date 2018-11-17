package com.zdmoney.credit.person.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.person.dao.pub.IPersonCarInfoDao;
import com.zdmoney.credit.person.domain.PersonCarInfo;

/**
 * 借款人车辆信息Dao接口层(实现)
 * @author Ivan
 *
 */
@Repository
public class PersonCarInfoDaoImpl extends BaseDaoImpl<PersonCarInfo> implements IPersonCarInfoDao {

	/**
	 * 根据personId查询
	 * @param personId
	 * @return
	 */
	@Override
	public PersonCarInfo findByPersonId(Long personId) {
		PersonCarInfo personCarInfo = new PersonCarInfo();
		personCarInfo.setPersonId(personId);
		List<PersonCarInfo> cars = findListByVo(personCarInfo);
		PersonCarInfo car = null;
		if (null != cars && !cars.isEmpty() && cars.size() > 0) {
			car = cars.get(0);
		}
		
		return car;
	}
	
}
