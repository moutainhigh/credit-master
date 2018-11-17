package com.zdmoney.credit.person.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zdmoney.credit.common.constant.ContactEnum;
import com.zdmoney.credit.common.constant.ContactEnum.AddressType;
import com.zdmoney.credit.common.constant.ContactEnum.Priority;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.loan.service.pub.ILoanTransferInfoService;
import com.zdmoney.credit.person.dao.pub.IPersonAddressInfoDao;
import com.zdmoney.credit.person.dao.pub.IPersonContactInfoDao;
import com.zdmoney.credit.person.domain.PersonAddressInfo;
import com.zdmoney.credit.person.domain.PersonContactInfo;
import com.zdmoney.credit.person.service.pub.IPersonAddressInfoService;
import com.zdmoney.credit.system.dao.pub.IPersonInfoDao;
import com.zdmoney.credit.system.domain.PersonInfo;
import com.zdmoney.credit.system.service.pub.ISequencesService;

/**
 * 借款人地址变更记录Service业务封装层
 * @author Ivan
 *
 */
@Service
public class PersonAddressInfoServiceImpl implements IPersonAddressInfoService{
	
	@Autowired @Qualifier("personAddressInfoDaoImpl")
	IPersonAddressInfoDao personAddressInfoDaoImpl;
	
	@Autowired @Qualifier("sequencesServiceImpl")
	ISequencesService sequencesServiceImpl;
	
	@Autowired @Qualifier("personInfoDaoImpl")
	IPersonInfoDao personInfoDaoImpl;
	
	@Autowired @Qualifier("personContactInfoDaoImpl")
	IPersonContactInfoDao personContactInfoDaoImpl;
	
	@Autowired 
	ILoanTransferInfoService loanTransferInfoServiceImpl;
	/**
	 * 跟据编号查询实体
	 * @param id
	 * @return
	 */
	@Override
	public PersonAddressInfo findById(Long id) {
		return personAddressInfoDaoImpl.get(id);
	}
	
	/**
	 * 分页查询
	 * @param personTelInfo
	 * @return
	 */
	@Override
	public Pager findWithPg(PersonAddressInfo personAddressInfo) {
		return personAddressInfoDaoImpl.findWithPg(personAddressInfo);
	}
	
	/**
	 * 修改客户家庭地址、公司地址记录变更流水
	 * @param oldPersonAddressInfo 老地址信息
	 * @param newPersonAddressInfo 新地址信息
	 */
	@Override
	public void addAddressInfo(PersonAddressInfo oldPersonAddressInfo,PersonAddressInfo newPersonAddressInfo) {
		/** 如果存在老地址信息将有效性更改成无效 查询条件：地址+地址类型+ObjectId **/
		if (oldPersonAddressInfo != null) {
			List<PersonAddressInfo> oldAddress = personAddressInfoDaoImpl.findListByVo(oldPersonAddressInfo);
			/** 数据正常情况下只返回一条数据，出于数据正常性考虑，采用循环方式更新 **/
			for (int i = 0;i < oldAddress.size();i++) {
				oldPersonAddressInfo = oldAddress.get(i);
				oldPersonAddressInfo.setValid("f");
				personAddressInfoDaoImpl.update(oldPersonAddressInfo);
			}
		}
		
		/** 查询是否存在此实体信息数据 **/
		List<PersonAddressInfo> address = personAddressInfoDaoImpl.findListByVo(newPersonAddressInfo);
		if (address.size() > 0) {
			/** 存在 更新有效性为有效 **/
			/** 数据正常情况下只返回一条数据，出于数据正常性考虑，采用循环方式更新 **/
			for (int i = 0;i < address.size();i++) {
				newPersonAddressInfo = address.get(i);
				newPersonAddressInfo.setValid("t");
				personAddressInfoDaoImpl.update(newPersonAddressInfo);
			}
		} else {
			/** 不存在 插入新数据 **/
			Long seqId = sequencesServiceImpl.getSequences(SequencesEnum.PERSON_ADDRESS_INFO);
			newPersonAddressInfo.setId(seqId);
			newPersonAddressInfo.setValid("t");
			personAddressInfoDaoImpl.insert(newPersonAddressInfo);
		}
	}
	
	/**
	 * 新增、修改地址数据
	 * @param personTelInfo
	 */
	@Transactional
	@Override
	public void saveOrUpdate(PersonAddressInfo personAddressInfo) {
		/** 电话记录编号 **/
		Long id = personAddressInfo.getId();
		/** 客户或联系人编号  **/
		Long objectId = personAddressInfo.getObjectId();
		personAddressInfo.setValid(Strings.defaultValue(personAddressInfo.getValid(), "f"));
		ContactEnum.AddressType addressType = ContactEnum.AddressType.get(personAddressInfo.getAddressType());
		ContactEnum.ClassName className = ContactEnum.ClassName.get(personAddressInfo.getClassName());
		/** 家庭地址、公司地址内容 **/
		String content = "";
		
		if (addressType == null || className == null) {
			/** 枚举数据匹配异常 **/
			throw new PlatformException(ResponseEnum.VALIDATE_ENUM_ERROR, new String[]{""});
		}
		if (Strings.isEmpty(objectId)) {
			/** 缺少客户或联系人编号 **/
			throw new PlatformException(ResponseEnum.VALIDATE_ISNULL, new String[]{""});
		}
		
		switch(className){
			/** 客户 **/
			case Borrower:
				PersonInfo personInfo = personInfoDaoImpl.get(objectId);
				if ( personInfo == null) {
					throw new PlatformException(ResponseEnum.VALIDATE_RESULT_ISNULL,new Object[]{"客户编号:" + objectId});
				}
				//检查该借款人是否有已转让过的债权
				boolean flag = loanTransferInfoServiceImpl.isLoanTransfer(personInfo.getId(),null);
				if(!flag){
					throw new PlatformException(ResponseEnum.FULL_MSG,Strings.errorMsg);
				}
				break;
			/** 联系人 **/
			case Contact:
				PersonContactInfo personContactInfo = personContactInfoDaoImpl.get(objectId);
				if(personContactInfo == null){
					throw new PlatformException(ResponseEnum.VALIDATE_RESULT_ISNULL,new Object[]{"联系人编号:" + objectId});
				}
				Long personId = personContactInfo.getPersonId();
				//检查该借款人是否有已转让过的债权
				boolean flagg = loanTransferInfoServiceImpl.isLoanTransfer(personId,null);
				if(!flagg){
					throw new PlatformException(ResponseEnum.FULL_MSG,Strings.errorMsg);
				}
				break;
		}
		
		
		if (Strings.isEmpty(id)) {
			/**
			 * 新增操作 更新对应客户或联系人手机号、家庭电话和公司电话信息 
			 * **/
			Long seqId = sequencesServiceImpl.getSequences(SequencesEnum.PERSON_ADDRESS_INFO);
			personAddressInfo.setId(seqId);
			personAddressInfoDaoImpl.insert(personAddressInfo);
			content = Strings.parseString(personAddressInfo.getContent());
		} else {
			/** 
			 * 修改操作 当前状态为有效时，直接更新对应客户或联系人手机号、家庭电话和公司电话信息
			 * 无效时，随机取出有效的信息来更新 
			 * **/
			personAddressInfoDaoImpl.update(personAddressInfo);
			if (personAddressInfo.getValid().equalsIgnoreCase("t")) {
				/** 有效性=有效 **/
				content = Strings.parseString(personAddressInfo.getContent());
			} else {
				/** 
				 * 有效性=无效
				 * 随机取出有效的数据 
				 * **/
				
				PersonAddressInfo personAddressInfoTmp = new PersonAddressInfo();
				personAddressInfoTmp.setObjectId(objectId);
				personAddressInfoTmp.setAddressType(addressType.getName());
				personAddressInfoTmp.setClassName(className.getName());
				personAddressInfoTmp.setValid("t");
				
				List<PersonAddressInfo> list = personAddressInfoDaoImpl.findListByVo(personAddressInfoTmp);
				if (list.size() > 0) {
					content = Strings.parseString(list.get(0).getContent());
				}
			}
		}
		
		switch(className) {
			/** 客户 **/
			case Borrower:
				if (personInfoDaoImpl.get(objectId) == null) {
					throw new PlatformException(ResponseEnum.VALIDATE_RESULT_ISNULL,new Object[]{"客户编号:" + objectId});
				}
				PersonInfo personInfo = new PersonInfo();
				personInfo.setId(objectId);
				
				switch(addressType) {
					case 家庭 :
						if (Strings.isEmpty(content)) {
							throw new PlatformException(ResponseEnum.FULL_MSG, new Object[] { "家庭地址不允许为空"});
						}
						personInfo.setAddress(content);
						break;
					case 公司 :
						if (Strings.isEmpty(content)) {
							throw new PlatformException(ResponseEnum.FULL_MSG, new Object[] { "公司地址不允许为空"});
						}
						personInfo.setcAddress(content);
						break;
					default :
						personInfo = null;
						break;
				}
				if (personInfo != null) {
					personInfoDaoImpl.update(personInfo);
				}
				break;
			/** 联系人 **/
			case Contact:
				if (personContactInfoDaoImpl.get(objectId) == null) {
					throw new PlatformException(ResponseEnum.VALIDATE_RESULT_ISNULL,new Object[]{"联系人编号:" + objectId});
				}
				PersonContactInfo personContactInfo = new PersonContactInfo();
				personContactInfo.setId(objectId);
				switch(addressType) {
					case 家庭 :
						personContactInfo.setAddress(content);
						break;
					default :
						personContactInfo = null;
						break;
				}
				if (personContactInfo != null) {
					personContactInfoDaoImpl.update(personContactInfo);
				}
			break;
		}
	}

	/**
	 * 核心接口中更新借款人的住址信息
	 * @param person
	 */
	@Override
	@Transactional
	public void saveCorePersonAddress(PersonInfo person) {
		if (person.getAddress() != null) {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("className", "zdsys.Borrower");
			paramMap.put("objectId", person.getId());
			paramMap.put("content", person.getAddress());
			if (!personAddressInfoDaoImpl.exists(paramMap)) {
				PersonAddressInfo address = new PersonAddressInfo();
				address.setId(sequencesServiceImpl.getSequences(SequencesEnum.PERSON_ADDRESS_INFO));
				address.setClassName("zdsys.Borrower");
				address.setObjectId(person.getId());
				address.setContent(person.getAddress());
				address.setAddressType(AddressType.家庭.getName());
				address.setPriority(Priority.高.getName());
				address.setValid("t");
				personAddressInfoDaoImpl.insert(address);
			}
		}
		
		if (person.getcAddress() != null) {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("className", "zdsys.Borrower");
			paramMap.put("objectId", person.getId());
			paramMap.put("content", person.getcAddress());
			if (!personAddressInfoDaoImpl.exists(paramMap)) {
				PersonAddressInfo address = new PersonAddressInfo();
				address.setId(sequencesServiceImpl.getSequences(SequencesEnum.PERSON_ADDRESS_INFO));
				address.setClassName("zdsys.Borrower");
				address.setObjectId(person.getId());
				address.setContent(person.getcAddress());
				address.setAddressType(AddressType.公司.getName());
				address.setPriority(Priority.高.getName());
				address.setValid("t");
				personAddressInfoDaoImpl.insert(address);
			}
		}
	}

	/**
	 * 核心接口中保存借款人的联系人的住址信息
	 * @param contact
	 */
	@Override
	@Transactional
	public void saveCorePersonContactAddress(PersonContactInfo contact) {
		if (contact.getAddress() != null) {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("className", "zdsys.Contact");
			paramMap.put("objectId", contact.getId());
			paramMap.put("content", contact.getAddress());
			if (!personAddressInfoDaoImpl.exists(paramMap)) {
				PersonAddressInfo address = new PersonAddressInfo();
				address.setId(sequencesServiceImpl.getSequences(SequencesEnum.PERSON_ADDRESS_INFO));
				address.setClassName("zdsys.Contact");
				address.setObjectId(contact.getId());
				address.setContent(contact.getAddress());
				address.setAddressType(AddressType.家庭.getName());
				address.setPriority(Priority.高.getName());
				address.setValid("t");
				personAddressInfoDaoImpl.insert(address);
			}
		}
	}
	
}