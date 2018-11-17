package com.zdmoney.credit.person.service.pub;

import java.util.List;

import com.zdmoney.credit.person.domain.PersonEntrepreneurInfo;

/**
 * 借款人企业经营信息Service接口定义
 * @author Ivan
 *
 */
public interface IPersonEntrepreneurInfoService {
	/**
	 * 通过实体信息查询
	 * @param personEntrepreneurInfo
	 * @return
	 */
	public List<PersonEntrepreneurInfo> findListByVo(PersonEntrepreneurInfo personEntrepreneurInfo);
	
	/**
	 * 根据personID查找
	 * @param personId
	 * @return
	 */
	public PersonEntrepreneurInfo findByPersonId(Long personId);
	
	/**
	 * 保存或更新
	 * @param personEntrepreneurInfo
	 */
	public void saveOrUpdate(PersonEntrepreneurInfo personEntrepreneurInfo);
}
