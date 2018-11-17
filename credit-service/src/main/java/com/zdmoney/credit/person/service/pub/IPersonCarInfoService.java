package com.zdmoney.credit.person.service.pub;

import java.util.List;

import com.zdmoney.credit.person.domain.PersonCarInfo;

/**
 * 借款人车辆信息Service接口定义
 * @author Ivan
 *
 */
public interface IPersonCarInfoService {
	/**
	 * 通过实体信息查询
	 * @param personCarInfo
	 * @return
	 */
	public List<PersonCarInfo> findListByVo(PersonCarInfo personCarInfo);
	
	/**
	 * 通过personId查找
	 * @param personId
	 * @return
	 */
	public PersonCarInfo findByPersonId(Long personId);
	
	/**
	 * 保存或者更新
	 * @param personCarInfo
	 */
	public void saveOrUpdate(PersonCarInfo personCarInfo);
}
