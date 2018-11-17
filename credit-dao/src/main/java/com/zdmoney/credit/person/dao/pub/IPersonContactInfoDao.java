package com.zdmoney.credit.person.dao.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.person.domain.PersonContactInfo;


/**
 * 联系人信息Dao接口定义
 * @author Ivan
 *
 */
public interface IPersonContactInfoDao extends IBaseDao<PersonContactInfo>{

	/**
	 * 根据 personId删除联系人信息
	 * @param contacts
	 */
	public void deleteByPersonId(Long personId);

	/**
	 * 查找联系人信息
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> findContactInfo4LXXD(Map<String, Object> params);
}
