package com.zdmoney.credit.person.dao.pub;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.person.domain.PersonHouseInfo;

/**
 * 借款人房产信息Dao接口定义
 * @author Ivan
 *
 */
public interface IPersonHouseInfoDao extends IBaseDao<PersonHouseInfo>{

	/**
	 * 根据personId查找
	 * @param personId
	 * @return
	 */
	public PersonHouseInfo findByPersonId(Long personId);
}
