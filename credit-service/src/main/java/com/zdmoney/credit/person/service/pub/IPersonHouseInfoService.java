package com.zdmoney.credit.person.service.pub;

import java.util.List;

import com.zdmoney.credit.person.domain.PersonHouseInfo;

/**
 * 借款人房产信息Service接口定义
 * @author Ivan
 *
 */
public interface IPersonHouseInfoService {
	/**
	 * 通过实体信息查询
	 * @param personHouseInfo
	 * @return
	 */
	public List<PersonHouseInfo> findListByVo(PersonHouseInfo personHouseInfo);
	
	/**
	 * 根据personId查找
	 * @param personId
	 * @return
	 */
	public PersonHouseInfo findByPersonId(Long personId);
	
	/**
	 * 保存或者更新
	 * @param personHouseInfo
	 */
	public void saveOrUpdate(PersonHouseInfo personHouseInfo);
}
