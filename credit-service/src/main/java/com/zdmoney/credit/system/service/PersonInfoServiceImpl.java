package com.zdmoney.credit.system.service;

import java.util.List;
import java.util.Map;

import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.zdmoney.credit.common.constant.ContactEnum;
import com.zdmoney.credit.common.constant.GatewayFuncIdEnum;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.gateway.GatewayUtils;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.framework.vo.bsb.input.Bsb100019Vo;
import com.zdmoney.credit.loan.dao.pub.IVLoanInfoDao;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.person.dao.pub.IPersonCarInfoDao;
import com.zdmoney.credit.person.dao.pub.IPersonEntrepreneurInfoDao;
import com.zdmoney.credit.person.dao.pub.IPersonHouseInfoDao;
import com.zdmoney.credit.person.domain.PersonAddressInfo;
import com.zdmoney.credit.person.domain.PersonCarInfo;
import com.zdmoney.credit.person.domain.PersonEntrepreneurInfo;
import com.zdmoney.credit.person.domain.PersonHouseInfo;
import com.zdmoney.credit.person.domain.PersonTelInfo;
import com.zdmoney.credit.person.service.pub.IPersonAddressInfoService;
import com.zdmoney.credit.person.service.pub.IPersonTelInfoService;
import com.zdmoney.credit.person.vo.PersonVo;
import com.zdmoney.credit.system.dao.pub.IPersonInfoDao;
import com.zdmoney.credit.system.domain.PersonInfo;
import com.zdmoney.credit.system.service.pub.IPersonInfoService;
import com.zdmoney.credit.system.service.pub.ISequencesService;

@Service
public class PersonInfoServiceImpl implements IPersonInfoService {

	@Autowired
	@Qualifier("sequencesServiceImpl")
	ISequencesService sequencesServiceImpl;

	@Autowired
	IPersonInfoDao personInfoDao;

	@Autowired
	@Qualifier("personCarInfoDaoImpl")
	IPersonCarInfoDao personCarInfoDaoImpl;

	@Autowired
	@Qualifier("personHouseInfoDaoImpl")
	IPersonHouseInfoDao personHouseInfoDaoImpl;

	@Autowired
	@Qualifier("personEntrepreneurInfoDaoImpl")
	IPersonEntrepreneurInfoDao personEntrepreneurInfoDaoImpl;

	@Autowired
	@Qualifier("personTelInfoServiceImpl")
	IPersonTelInfoService personTelInfoServiceImpl;

	@Autowired
	@Qualifier("personAddressInfoServiceImpl")
	IPersonAddressInfoService personAddressInfoServiceImpl;
	
	@Autowired
	IVLoanInfoDao vloanInfoDao;

	@Override
	public PersonInfo findById(Long borrowerId) {
		return personInfoDao.get(borrowerId);
	}

	/**
	 * 带分页查询
	 * 
	 * @param personInfo
	 *            条件实体对象
	 * @return
	 */
	@Override
	public Pager findWithPg(PersonInfo personInfo) {
		return personInfoDao.findWithPg(personInfo);
	}

	/**
	 * 更新客户资料信息（基本信息、车辆信息、房屋信息、企业信息、手机和地址变更记录）
	 * 
	 * @param personInfo
	 * @return
	 */
	@Transactional
	public void saveOrUpdate(PersonInfo personInfo) {
		final Long personId = personInfo.getId();
		final PersonInfo personInfoDB = personInfoDao.get(personId);
		PersonCarInfo personCarInfo = personInfo.getPersonCarInfo();
		PersonHouseInfo personHouseInfo = personInfo.getPersonHouseInfo();
		PersonEntrepreneurInfo personEntrepreneurInfo = personInfo.getPersonEntrepreneurInfo();
		if (Strings.isEmpty(personId)) {
			/** 缺少客户编号 **/
			throw new PlatformException(ResponseEnum.VALIDATE_ISNULL, new Object[] { "客户编号" });
		}
		if (personInfoDB == null) {
			/** 客户信息不存在 **/
			throw new PlatformException(ResponseEnum.VALIDATE_ISNULL, new Object[] { "客户编号:" + personId });
		}

		if (personCarInfo == null || Strings.isEmpty(personCarInfo.getId())) {
			/** 缺少车辆编号 **/
			throw new PlatformException(ResponseEnum.VALIDATE_ISNULL, new Object[] { "车辆编号" });
		}
		if (personHouseInfo == null || Strings.isEmpty(personHouseInfo.getId())) {
			/** 缺少房屋编号 **/
			throw new PlatformException(ResponseEnum.VALIDATE_ISNULL, new Object[] { "房屋编号" });
		}
		if (personEntrepreneurInfo == null || Strings.isEmpty(personEntrepreneurInfo.getId())) {
			/** 缺少企业信息编号 **/
			throw new PlatformException(ResponseEnum.VALIDATE_ISNULL, new Object[] { "企业信息编号" });
		}

		personInfo.setHasLoan(Strings.defaultValue(personInfo.getHasLoan(), "f"));
		personInfo.setHasCreditCard(Strings.defaultValue(personInfo.getHasCreditCard(), "f"));
		personHouseInfo.setHasLoan(Strings.defaultValue(personHouseInfo.getHasLoan(), "f"));
		personCarInfo.setHasLoan(Strings.defaultValue(personCarInfo.getHasLoan(), "f"));

		/** Begin 保存手机号变更记录 **/
		if (Strings.isNotEmpty(personInfo.getMphone())) {
			/** 手机号不为空 保存到变更记录表 **/
			PersonTelInfo personTelInfoDB = null;
			String dbMphone = Strings.convertValue(personInfoDB.getMphone(), String.class);
			String mphone = Strings.convertValue(personInfo.getMphone(), String.class);
			if (dbMphone.equalsIgnoreCase(mphone)) {

			} else {
				if (Strings.isNotEmpty(dbMphone)) {
					personTelInfoDB = new PersonTelInfo();
					personTelInfoDB.setContent(dbMphone);
					personTelInfoDB.setTelType(ContactEnum.TelType.手机.name());
					personTelInfoDB.setObjectId(personId);
				}
			}
			PersonTelInfo personTelInfo = new PersonTelInfo();
			personTelInfo.setContent(personInfo.getMphone());
			personTelInfo.setTelType(ContactEnum.TelType.手机.name());
			personTelInfo.setObjectId(personId);
			personTelInfo.setPriority(ContactEnum.Priority.高.name());
			personTelInfo.setClassName(ContactEnum.ClassName.Borrower.getName());
			personTelInfoServiceImpl.addTelInfo(personTelInfoDB, personTelInfo);
			new Thread(){
	            public void run() {
	            	List<VLoanInfo> list = vloanInfoDao.findById(personId);
	            	if (list!=null && list.size()>0) {
	            		PersonInfo personInfo = personInfoDao.get(personId);
		    			Bsb100019Vo vo =new Bsb100019Vo();
		    			vo.setChangeMsgType("01");
		    			vo.setCustName(personInfo.getName());
		    			vo.setIdNo(personInfo.getIdnum());
		    			vo.setIdType("01");
		    			vo.setNewMobNo(personInfo.getMphone());
		    			vo.setOldMobNo(personInfoDB.getMphone());
		    			vo.setTxncd("BYXY0019");
		    			JSONObject object = GatewayUtils.callCateWayInterface(vo, GatewayFuncIdEnum.电话号码变更.getCode());
					}else{
						return;
					}
	            }
	        }.start();
		}

		if (Strings.isNotEmpty(personInfo.getTel())) {
			/** 家庭电话不为空 保存到变更记录表 **/
			PersonTelInfo personTelInfoDB = null;
			String dbTel = Strings.convertValue(personInfoDB.getTel(), String.class);
			String tel = Strings.convertValue(personInfo.getTel(), String.class);
			if (dbTel.equalsIgnoreCase(tel)) {

			} else {
				if (Strings.isNotEmpty(dbTel)) {
					personTelInfoDB = new PersonTelInfo();
					personTelInfoDB.setContent(dbTel);
					personTelInfoDB.setTelType(ContactEnum.TelType.家庭电话.name());
					personTelInfoDB.setObjectId(personId);
				}
			}
			PersonTelInfo personTelInfo = new PersonTelInfo();
			personTelInfo.setContent(personInfo.getTel());
			personTelInfo.setTelType(ContactEnum.TelType.家庭电话.name());
			personTelInfo.setObjectId(personId);
			personTelInfo.setPriority(ContactEnum.Priority.高.name());
			personTelInfo.setClassName(ContactEnum.ClassName.Borrower.getName());
			personTelInfoServiceImpl.addTelInfo(personTelInfoDB, personTelInfo);
		}

		if (Strings.isNotEmpty(personInfo.getcTel())) {
			/** 公司电话不为空 保存到变更记录表 **/
			PersonTelInfo personTelInfoDB = null;
			String dbCTel = Strings.convertValue(personInfoDB.getcTel(), String.class);
			String cTel = Strings.convertValue(personInfo.getcTel(), String.class);
			if (dbCTel.equalsIgnoreCase(cTel)) {

			} else {
				if (Strings.isNotEmpty(dbCTel)) {
					personTelInfoDB = new PersonTelInfo();
					personTelInfoDB.setContent(dbCTel);
					personTelInfoDB.setTelType(ContactEnum.TelType.公司电话.name());
					personTelInfoDB.setObjectId(personId);
				}
			}
			PersonTelInfo personTelInfo = new PersonTelInfo();
			personTelInfo.setContent(personInfo.getcTel());
			personTelInfo.setTelType(ContactEnum.TelType.公司电话.name());
			personTelInfo.setObjectId(personId);
			personTelInfo.setPriority(ContactEnum.Priority.高.name());
			personTelInfo.setClassName(ContactEnum.ClassName.Borrower.getName());
			personTelInfoServiceImpl.addTelInfo(personTelInfoDB, personTelInfo);
		}

		if (Strings.isNotEmpty(personInfo.getFax())) {
			/** 传真号码不为空 保存到变更记录表 **/
			PersonTelInfo personTelInfoDB = null;
			String dbFax = Strings.convertValue(personInfoDB.getFax(), String.class);
			String fax = Strings.convertValue(personInfo.getFax(), String.class);
			if (dbFax.equalsIgnoreCase(fax)) {

			} else {
				if (Strings.isNotEmpty(dbFax)) {
					personTelInfoDB = new PersonTelInfo();
					personTelInfoDB.setContent(dbFax);
					personTelInfoDB.setTelType(ContactEnum.TelType.传真.name());
					personTelInfoDB.setObjectId(personId);
				}
			}
			PersonTelInfo personTelInfo = new PersonTelInfo();
			personTelInfo.setContent(personInfo.getFax());
			personTelInfo.setTelType(ContactEnum.TelType.传真.name());
			personTelInfo.setObjectId(personId);
			personTelInfo.setPriority(ContactEnum.Priority.高.name());
			personTelInfo.setClassName(ContactEnum.ClassName.Borrower.getName());
			personTelInfoServiceImpl.addTelInfo(personTelInfoDB, personTelInfo);
		}
		/** End 保存手机号变更记录 **/

		/** Begin 保存地址信息变更记录 **/
		if (Strings.isNotEmpty(personInfo.getAddress())) {
			/** 家庭地址不为空 保存到变更记录表 **/
			PersonAddressInfo personAddressInfoDB = null;
			String dbAddress = Strings.convertValue(personInfoDB.getAddress(), String.class);
			String address = Strings.convertValue(personInfo.getAddress(), String.class);
			if (dbAddress.equalsIgnoreCase(address)) {

			} else {
				if (Strings.isNotEmpty(dbAddress)) {
					personAddressInfoDB = new PersonAddressInfo();
					personAddressInfoDB.setContent(dbAddress);
					personAddressInfoDB.setAddressType(ContactEnum.AddressType.家庭.name());
					personAddressInfoDB.setObjectId(personId);
				}
			}
			PersonAddressInfo personAddressInfo = new PersonAddressInfo();
			personAddressInfo.setContent(personInfo.getAddress());
			personAddressInfo.setAddressType(ContactEnum.AddressType.家庭.name());
			personAddressInfo.setObjectId(personId);
			personAddressInfo.setPriority(ContactEnum.Priority.高.name());
			personAddressInfo.setClassName(ContactEnum.ClassName.Borrower.getName());
			personAddressInfoServiceImpl.addAddressInfo(personAddressInfoDB, personAddressInfo);
		}
		if (Strings.isNotEmpty(personInfo.getcAddress())) {
			/** 公司地址不为空 保存到变更记录表 **/
			PersonAddressInfo personAddressInfoDB = null;
			String dbCAddress = Strings.convertValue(personInfoDB.getcAddress(), String.class);
			String cAddress = Strings.convertValue(personInfo.getcAddress(), String.class);
			if (dbCAddress.equalsIgnoreCase(cAddress)) {

			} else {
				if (Strings.isNotEmpty(dbCAddress)) {
					personAddressInfoDB = new PersonAddressInfo();
					personAddressInfoDB.setContent(dbCAddress);
					personAddressInfoDB.setAddressType(ContactEnum.AddressType.公司.name());
					personAddressInfoDB.setObjectId(personId);
				}
			}
			PersonAddressInfo personAddressInfo = new PersonAddressInfo();
			personAddressInfo.setContent(personInfo.getcAddress());
			personAddressInfo.setAddressType(ContactEnum.AddressType.公司.name());
			personAddressInfo.setObjectId(personId);
			personAddressInfo.setPriority(ContactEnum.Priority.高.name());
			personAddressInfo.setClassName(ContactEnum.ClassName.Borrower.getName());
			personAddressInfoServiceImpl.addAddressInfo(personAddressInfoDB, personAddressInfo);
		}
		/** End 保存地址信息变更记录 **/

		/** 更新客户主表数据 **/
		personInfoDao.update(personInfo);

		/** 更新车辆表数据 **/
		personCarInfoDaoImpl.update(personCarInfo);

		/** 更新房屋表数据 **/
		personHouseInfoDaoImpl.update(personHouseInfo);

		/** 更新企业表数据 **/
		personEntrepreneurInfoDaoImpl.update(personEntrepreneurInfo);
		
	}

	public void updatePersonInfo(PersonInfo personInfo) {
		personInfoDao.update(personInfo);
	}

	/**
	 * 
	 * 跟據姓名+身份證號查詢
	 * 
	 * @param name
	 * @param idNum
	 */
	public PersonInfo findByIdNumAndName(String name, String idNum) {
		PersonInfo personInfo = new PersonInfo();
		if (Strings.isEmpty(name) || Strings.isEmpty(idNum)) {
			return null;
		}
		personInfo.setName(name);
		personInfo.setIdnum(idNum);
		List<PersonInfo> list = personInfoDao.findListByVo(personInfo);
		if (list.size() == 1) {
			return list.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 核心接口中保存或者更新客户信息
	 * @param personInfo 
	 * @return
	 */
	@Override
	@Transactional
	public PersonInfo saveOrUpdateCore(PersonInfo personInfo) {
		Long personId = personInfo.getId();
		if (Strings.isEmpty(personId)) {
			/**保存客户信息**/
			personInfo.setId(sequencesServiceImpl.getSequences(SequencesEnum.PERSON_INFO));
			personInfoDao.insert(personInfo);
		} else {
			/**更新客户信息**/
			personInfoDao.update(personInfo);
		}
		
		return personInfo;
	}
	
	@Override
	public List<PersonVo> findLeaveOfficeEmployee(Map<String, Object> paramMap) {
		return personInfoDao.findLeaveOfficeEmployee(paramMap);
	}


	@Override
	public List<Map<String, Object>> getCustomerInfo4LXXD(Map<String, Object> paramMap) {
		return personInfoDao.getCustomerInfo4LXXD(paramMap);
	}
}
