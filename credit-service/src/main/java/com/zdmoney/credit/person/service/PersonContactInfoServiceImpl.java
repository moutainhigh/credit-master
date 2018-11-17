package com.zdmoney.credit.person.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zdmoney.credit.common.constant.ContactEnum;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.person.dao.pub.IPersonContactInfoDao;
import com.zdmoney.credit.person.domain.PersonAddressInfo;
import com.zdmoney.credit.person.domain.PersonContactInfo;
import com.zdmoney.credit.person.domain.PersonTelInfo;
import com.zdmoney.credit.person.service.pub.IPersonAddressInfoService;
import com.zdmoney.credit.person.service.pub.IPersonContactInfoService;
import com.zdmoney.credit.person.service.pub.IPersonTelInfoService;
import com.zdmoney.credit.system.service.pub.ISequencesService;

/**
 * 联系人信息Service业务封装层
 * @author Ivan
 *
 */
@Service
public class PersonContactInfoServiceImpl implements IPersonContactInfoService {
	
	@Autowired @Qualifier("personContactInfoDaoImpl")
	IPersonContactInfoDao personContactInfoDaoImpl;
	
	@Autowired @Qualifier("sequencesServiceImpl")
	ISequencesService sequencesServiceImpl;
	
	@Autowired @Qualifier("personTelInfoServiceImpl")
	IPersonTelInfoService personTelInfoServiceImpl;
	
	@Autowired @Qualifier("personAddressInfoServiceImpl")
	IPersonAddressInfoService personAddressInfoServiceImpl;
	
	/**
	 * 通过实体查询结果
	 * @param personContactInfo
	 * @return
	 */
	public List<PersonContactInfo> findListByVo(PersonContactInfo personContactInfo) {
		return personContactInfoDaoImpl.findListByVo(personContactInfo);
	}
	
	/**
	 * 带分页查询
	 * @param personContactInfo 条件实体对象
	 * @return
	 */
	public Pager findWithPg(PersonContactInfo personContactInfo) {
		return personContactInfoDaoImpl.findWithPg(personContactInfo);
	}
	
	/**
	 * 通过编号获取实体
	 * @param id
	 * @return
	 */
	@Override
	public PersonContactInfo findById(Long id) {
		return personContactInfoDaoImpl.get(id);
	}

	/**
	 * 更新联系人信息（联系人基本信息、手机和地址变更记录）
	 * @param personContactInfo 
	 * @return
	 */
	@Transactional
	public void saveOrUpdate(PersonContactInfo personContactInfo) {
		Long id = personContactInfo.getId();
		PersonContactInfo personContactInfoDB = personContactInfoDaoImpl.get(id);
		personContactInfo.setIsKnowLoan(Strings.defaultValue(personContactInfo.getIsKnowLoan(), "f"));
		if (Strings.isEmpty(id)) {
			/** 新增联系人 **/
			id = sequencesServiceImpl.getSequences(SequencesEnum.PERSON_CONTACT_INFO);
			personContactInfo.setId(id);
			personContactInfoDaoImpl.insert(personContactInfo);
		} else {
			/** 修改联系人 **/
			if (personContactInfoDB == null) {
				/** 客户信息不存在  **/
				throw new PlatformException(ResponseEnum.VALIDATE_ISNULL,new Object[]{"客户编号:" + id});
			}
			personContactInfoDaoImpl.update(personContactInfo);
		}
		
		/** Begin 保存手机号变更记录  **/
		if (Strings.isNotEmpty(personContactInfo.getMphone())) {
			/** 手机号不为空 保存到变更记录表 **/
			PersonTelInfo personTelInfoDB = null;
			if (personContactInfoDB != null) {
				/** 新增操作 不需要和数据库手机号比较 **/
				String dbMphone = Strings.convertValue(personContactInfoDB.getMphone(), String.class);
				String mphone = Strings.convertValue(personContactInfo.getMphone(), String.class);
				if (dbMphone.equalsIgnoreCase(mphone)) {
					
				} else {
					if (Strings.isNotEmpty(dbMphone)) {
						personTelInfoDB = new PersonTelInfo();
						personTelInfoDB.setContent(dbMphone);
						personTelInfoDB.setTelType(ContactEnum.TelType.手机.name());
						personTelInfoDB.setObjectId(id);
					}
				}
			}
			PersonTelInfo personTelInfo = new PersonTelInfo();
			personTelInfo.setContent(personContactInfo.getMphone());
			personTelInfo.setTelType(ContactEnum.TelType.手机.name());
			personTelInfo.setObjectId(id);
			personTelInfo.setPriority(ContactEnum.Priority.高.name());
			personTelInfo.setClassName(ContactEnum.ClassName.Contact.getName());
			personTelInfoServiceImpl.addTelInfo(personTelInfoDB,personTelInfo);
		}
		
		if (Strings.isNotEmpty(personContactInfo.getTel())) {
			/** 家庭电话不为空 保存到变更记录表 **/
			PersonTelInfo personTelInfoDB = null;
			if (personContactInfoDB != null) {
				/** 新增操作 不需要和数据库家庭电话比较 **/
				String dbTel = Strings.convertValue(personContactInfoDB.getTel(), String.class);
				String tel = Strings.convertValue(personContactInfo.getTel(), String.class);
				if (dbTel.equalsIgnoreCase(tel)) {
					
				} else {
					if (Strings.isNotEmpty(dbTel)) {
						personTelInfoDB = new PersonTelInfo();
						personTelInfoDB.setContent(dbTel);
						personTelInfoDB.setTelType(ContactEnum.TelType.家庭电话.name());
						personTelInfoDB.setObjectId(id);
					}
				}
			}
			PersonTelInfo personTelInfo = new PersonTelInfo();
			personTelInfo.setContent(personContactInfo.getTel());
			personTelInfo.setTelType(ContactEnum.TelType.家庭电话.name());
			personTelInfo.setObjectId(id);
			personTelInfo.setPriority(ContactEnum.Priority.高.name());
			personTelInfo.setClassName(ContactEnum.ClassName.Contact.getName());
			personTelInfoServiceImpl.addTelInfo(personTelInfoDB,personTelInfo);
		}
		
		if (Strings.isNotEmpty(personContactInfo.getCtel())) {
			/** 公司电话不为空 保存到变更记录表 **/
			PersonTelInfo personTelInfoDB = null;
			if (personContactInfoDB != null) {
				/** 新增操作 不需要和数据库公司电话比较 **/
				String dbCTel = Strings.convertValue(personContactInfoDB.getCtel(), String.class);
				String cTel = Strings.convertValue(personContactInfo.getCtel(), String.class);
				if (dbCTel.equalsIgnoreCase(cTel)) {
					
				} else {
					if (Strings.isNotEmpty(dbCTel)) {
						personTelInfoDB = new PersonTelInfo();
						personTelInfoDB.setContent(dbCTel);
						personTelInfoDB.setTelType(ContactEnum.TelType.公司电话.name());
						personTelInfoDB.setObjectId(id);
					}
				}
			}
			PersonTelInfo personTelInfo = new PersonTelInfo();
			personTelInfo.setContent(personContactInfo.getCtel());
			personTelInfo.setTelType(ContactEnum.TelType.公司电话.name());
			personTelInfo.setObjectId(id);
			personTelInfo.setPriority(ContactEnum.Priority.高.name());
			personTelInfo.setClassName(ContactEnum.ClassName.Contact.getName());
			personTelInfoServiceImpl.addTelInfo(personTelInfoDB,personTelInfo);
		}
		/** End 保存手机号变更记录  **/
		
		
		/** Begin 保存地址信息变更记录  **/
		if (Strings.isNotEmpty(personContactInfo.getAddress())) {
			/** 家庭地址不为空 保存到变更记录表 **/
			PersonAddressInfo personAddressInfoDB = null;
			if (personContactInfoDB != null) {
				/** 新增操作 不需要和数据库家庭地址比较 **/
				String dbAddress = Strings.convertValue(personContactInfoDB.getAddress(), String.class);
				String address = Strings.convertValue(personContactInfo.getAddress(), String.class);
				if (dbAddress.equalsIgnoreCase(address)) {
					
				} else {
					if (Strings.isNotEmpty(dbAddress)) {
						personAddressInfoDB = new PersonAddressInfo();
						personAddressInfoDB.setContent(dbAddress);
						personAddressInfoDB.setAddressType(ContactEnum.AddressType.家庭.name());
						personAddressInfoDB.setObjectId(id);
					}
				}
			}
			PersonAddressInfo personAddressInfo = new PersonAddressInfo();
			personAddressInfo.setContent(personContactInfo.getAddress());
			personAddressInfo.setAddressType(ContactEnum.AddressType.家庭.name());
			personAddressInfo.setObjectId(id);
			personAddressInfo.setPriority(ContactEnum.Priority.高.name());
			personAddressInfo.setClassName(ContactEnum.ClassName.Contact.getName());
			personAddressInfoServiceImpl.addAddressInfo(personAddressInfoDB,personAddressInfo);
		}
		/** End 保存地址信息变更记录  **/
		
	}

	/**
	 * 保存或者更新联系人信息
	 * @param contacts
	 * @return
	 */
	@Override
	@Transactional
	public List<PersonContactInfo> saveOrUpdateCore(List<PersonContactInfo> contacts) {
		for (PersonContactInfo contact : contacts) {
			Long id = contact.getId();
			
			if (Strings.isEmpty(id)) {
				/**保存联系人**/
				contact.setId(sequencesServiceImpl.getSequences(SequencesEnum.PERSON_CONTACT_INFO));
				personContactInfoDaoImpl.insert(contact);
			} else {
				/**更新联系人**/
				personContactInfoDaoImpl.update(contact);
			}
		}
		return contacts;
	}


	@Override
	public List<Map<String, Object>> findContactInfo4LXXD(Map<String, Object> params) {
		return personContactInfoDaoImpl.findContactInfo4LXXD(params);
	}
	
}