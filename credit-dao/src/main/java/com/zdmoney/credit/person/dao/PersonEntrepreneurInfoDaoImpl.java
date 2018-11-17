package com.zdmoney.credit.person.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.person.dao.pub.IPersonEntrepreneurInfoDao;
import com.zdmoney.credit.person.domain.PersonEntrepreneurInfo;

/**
 * 借款人企业经营信息Dao接口层(实现)
 * @author Ivan
 *
 */
@Repository
public class PersonEntrepreneurInfoDaoImpl extends BaseDaoImpl<PersonEntrepreneurInfo> implements IPersonEntrepreneurInfoDao {

	/**
	 * 根据personId查询
	 * @param personId
	 * @return
	 */
	@Override
	public PersonEntrepreneurInfo findByPersonId(Long personId) {
		PersonEntrepreneurInfo personEntrepreneurInfo = new PersonEntrepreneurInfo();
		personEntrepreneurInfo.setPersonId(personId);
		List<PersonEntrepreneurInfo> entrepreneurs = findListByVo(personEntrepreneurInfo);
		PersonEntrepreneurInfo entrepreneur = null;
		if (null != entrepreneurs && !entrepreneurs.isEmpty() && entrepreneurs.size() > 0) {
			entrepreneur = entrepreneurs.get(0);
		}
		
		return entrepreneur;
	}
	
}
