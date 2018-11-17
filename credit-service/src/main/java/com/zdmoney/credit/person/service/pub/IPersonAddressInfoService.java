package com.zdmoney.credit.person.service.pub;

import org.springframework.transaction.annotation.Transactional;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.person.domain.PersonAddressInfo;
import com.zdmoney.credit.person.domain.PersonContactInfo;
import com.zdmoney.credit.system.domain.PersonInfo;


/**
 * 借款人地址变更记录Service接口定义
 * @author Ivan
 *
 */
public interface IPersonAddressInfoService {
	
	/**
	 * 添加地址变更记录 
	 * @param oldPersonAddressInfo 老号码信息
	 * @param newPersonAddressInfo 新号码信息
	 */
	public void addAddressInfo(PersonAddressInfo oldPersonAddressInfo,PersonAddressInfo newPersonAddressInfo);
	
	/**
	 * 新增、修改地址数据
	 * @param personTelInfo
	 */
	@Transactional
	public void saveOrUpdate(PersonAddressInfo personAddressInfo);
	
	/**
	 * 分页查询
	 * @param personAddressInfo
	 * @return
	 */
	public Pager findWithPg(PersonAddressInfo personAddressInfo);
	
	/**
	 * 跟据编号查询实体
	 * @param id
	 * @return
	 */
	public PersonAddressInfo findById(Long id);
	
	/**
	 * 核心接口中保存借款人的住址信息
	 * @param person
	 */
	public void saveCorePersonAddress(PersonInfo person);
	
	/**
	 * 核心接口中保存借款人的联系人的住址信息
	 * @param contact
	 */
	public void saveCorePersonContactAddress(PersonContactInfo contact);
}
