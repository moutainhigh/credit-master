package com.zdmoney.credit.person.service.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.person.domain.PersonContactInfo;

/**
 * 联系人信息Service接口定义
 * @author Ivan
 *
 */
public interface IPersonContactInfoService {
	
	/**
	 * 通过实体查询结果
	 * @param personContactInfo
	 * @return
	 */
	public List<PersonContactInfo> findListByVo(PersonContactInfo personContactInfo);
	
	/**
	 * 通过编号获取实体
	 * @param id
	 * @return
	 */
	public PersonContactInfo findById(Long id);
	
	/**
	 * 带分页查询
	 * @param personContactInfo 条件实体对象
	 * @return
	 */
	public Pager findWithPg(PersonContactInfo personContactInfo);
	
	/**
	 * 更新联系人信息（联系人基本信息、手机和地址变更记录）
	 * @param personContactInfo 
	 * @return
	 */
	public void saveOrUpdate(PersonContactInfo personContactInfo);
	
	/**
	 * 保存或者更新联系人信息
	 * @param contacts
	 * @return
	 */
	public List<PersonContactInfo> saveOrUpdateCore(List<PersonContactInfo> contacts);

	/**
	 * 查找联系人信息
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> findContactInfo4LXXD( Map<String, Object> params);
	
}
