package com.zdmoney.credit.person.dao.pub;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.person.domain.PersonEntrepreneurInfo;

/**
 * 借款人企业经营信息Dao接口定义
 * @author Ivan
 *
 */
public interface IPersonEntrepreneurInfoDao extends IBaseDao<PersonEntrepreneurInfo>{
	/**
	 * 根据personId查询
	 * @param personId
	 * @return
	 */
	public PersonEntrepreneurInfo findByPersonId(Long personId);
}
