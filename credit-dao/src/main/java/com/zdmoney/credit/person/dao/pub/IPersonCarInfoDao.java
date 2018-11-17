package com.zdmoney.credit.person.dao.pub;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.person.domain.PersonCarInfo;

/**
 * 借款人车辆信息Dao接口定义
 * @author Ivan
 *
 */
public interface IPersonCarInfoDao extends IBaseDao<PersonCarInfo>{
	
	/**
	 * 根据personId查询
	 * @param personId
	 * @return
	 */
	public PersonCarInfo findByPersonId(Long personId);

}
