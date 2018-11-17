package com.zdmoney.credit.person.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.zdmoney.credit.common.constant.ContactEnum;
import com.zdmoney.credit.common.constant.GatewayFuncIdEnum;
import com.zdmoney.credit.common.constant.ContactEnum.Priority;
import com.zdmoney.credit.common.constant.ContactEnum.TelType;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.gateway.GatewayUtils;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.framework.vo.bsb.input.Bsb100019Vo;
import com.zdmoney.credit.loan.dao.pub.IVLoanInfoDao;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.ILoanTransferInfoService;
import com.zdmoney.credit.loan.vo.VloanPersonOrg;
import com.zdmoney.credit.person.dao.pub.IPersonContactInfoDao;
import com.zdmoney.credit.person.dao.pub.IPersonTelInfoDao;
import com.zdmoney.credit.person.domain.PersonContactInfo;
import com.zdmoney.credit.person.domain.PersonTelInfo;
import com.zdmoney.credit.person.service.pub.IPersonTelInfoService;
import com.zdmoney.credit.system.dao.pub.IPersonInfoDao;
import com.zdmoney.credit.system.domain.PersonInfo;
import com.zdmoney.credit.system.service.pub.ISequencesService;

/**
 * 借款人号码变更记录Service业务封装层
 * @author Ivan
 *
 */
@Service
public class PersonTelInfoServiceImpl implements IPersonTelInfoService {
	
	@Autowired @Qualifier("personTelInfoDaoImpl")
	IPersonTelInfoDao personTelInfoDaoImpl;
	
	@Autowired @Qualifier("sequencesServiceImpl")
	ISequencesService sequencesServiceImpl;
	
	@Autowired @Qualifier("personInfoDaoImpl")
	IPersonInfoDao personInfoDaoImpl;
	
	@Autowired @Qualifier("personContactInfoDaoImpl")
	IPersonContactInfoDao personContactInfoDaoImpl;
	
	@Autowired
	IPersonInfoDao personInfoDao;//人员信息操作DAO
	
	@Autowired
	IVLoanInfoDao vloanInfoDao;
	
	@Autowired 
	ILoanTransferInfoService loanTransferInfoServiceImpl;
	
	/**
	 * 分页查询
	 * @param personTelInfo
	 * @return
	 */
	@Override
	public Pager findWithPg(PersonTelInfo personTelInfo) {
		return personTelInfoDaoImpl.findWithPg(personTelInfo);
	}
	
	/**
	 * 跟据编号查询实体
	 * @param id
	 * @return
	 */
	@Override
	public PersonTelInfo findById(Long id) {
		return personTelInfoDaoImpl.get(id);
	}
	
	
	/**
	 * 修改客户手机号、家庭电话、公司电话记录变更流水
	 * @param oldPersonTelInfo 老号码信息
	 * @param newPersonTelInfo 新号码信息
	 */
	@Override
	public void addTelInfo(PersonTelInfo oldPersonTelInfo,PersonTelInfo newPersonTelInfo){
		/** 如果存在老号码信息将有效性更改成无效 查询条件：号码+电话类型+ObjectId **/
		if (oldPersonTelInfo != null) {
			List<PersonTelInfo> oldTels = personTelInfoDaoImpl.findListByVo(oldPersonTelInfo);
			/** 数据正常情况下只返回一条数据，出于数据正常性考虑，采用循环方式更新 **/
			for (int i = 0;i < oldTels.size();i++) {
				oldPersonTelInfo = oldTels.get(i);
				oldPersonTelInfo.setValid("f");
				personTelInfoDaoImpl.update(oldPersonTelInfo);
			}
		}
		
		/** 查询是否存在此实体信息数据 **/
		List<PersonTelInfo> tels = personTelInfoDaoImpl.findListByVo(newPersonTelInfo);
		if (tels.size() > 0) {
			/** 存在 更新有效性为有效 **/
			/** 数据正常情况下只返回一条数据，出于数据正常性考虑，采用循环方式更新 **/
			for (int i = 0;i < tels.size();i++) {
				newPersonTelInfo = tels.get(i);
				newPersonTelInfo.setValid("t");
				personTelInfoDaoImpl.update(newPersonTelInfo);
			}
		} else {
			/** 不存在 插入新数据 **/
			Long seqId = sequencesServiceImpl.getSequences(SequencesEnum.PERSON_TEL_INFO);
			newPersonTelInfo.setId(seqId);
			newPersonTelInfo.setValid("t");
			personTelInfoDaoImpl.insert(newPersonTelInfo);
		}
	}
	
	/**
	 * 新增、修改电话数据
	 * @param personTelInfo
	 */
	@Transactional
	public void saveOrUpdate(PersonTelInfo personTelInfo) {
		/** 电话记录编号 **/
		Long id = personTelInfo.getId();
		/** 客户或联系人编号  **/
		Long objectId = personTelInfo.getObjectId();
		personTelInfo.setValid(Strings.defaultValue(personTelInfo.getValid(), "f"));
		
		ContactEnum.TelType telType = ContactEnum.TelType.get(personTelInfo.getTelType());
		ContactEnum.ClassName className = ContactEnum.ClassName.get(personTelInfo.getClassName());
		/** 手机、家庭电话、公司电话内容 **/
		String content = "";
		
		if (telType == null || className == null) {
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
			Long seqId = sequencesServiceImpl.getSequences(SequencesEnum.PERSON_TEL_INFO);
			personTelInfo.setId(seqId);
			personTelInfoDaoImpl.insert(personTelInfo);
			content = Strings.parseString(personTelInfo.getContent());
		} else {
			/** 
			 * 修改操作 当前状态为有效时，直接更新对应客户或联系人手机号、家庭电话和公司电话信息
			 * 无效时，随机取出有效的信息来更新 
			 * **/
			personTelInfoDaoImpl.update(personTelInfo);
			if (personTelInfo.getValid().equalsIgnoreCase("t")) {
				/** 有效性=有效 **/
				content = Strings.parseString(personTelInfo.getContent());
			} else {
				/** 
				 * 有效性=无效
				 * 随机取出有效的数据 
				 * **/
				
				PersonTelInfo personTelInfoTmp = new PersonTelInfo();
				personTelInfoTmp.setObjectId(objectId);
				personTelInfoTmp.setTelType(telType.getName());
				personTelInfoTmp.setClassName(className.getName());
				personTelInfoTmp.setValid("t");
				
				List<PersonTelInfo> list = personTelInfoDaoImpl.findListByVo(personTelInfoTmp);
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
				switch(telType) {
					case 家庭电话 :
						if (Strings.isEmpty(content)) {
							throw new PlatformException(ResponseEnum.FULL_MSG, new Object[] { "家庭电话不允许为空"});
						}
						personInfo.setTel(content);
						break;
					case 手机 :
						if (Strings.isEmpty(content)) {
							/** 业务上客户手机号不能为空  **/
							throw new PlatformException(ResponseEnum.FULL_MSG, new Object[] { "手机号不允许为空"});
						}
						List<VLoanInfo> list =vloanInfoDao.findById(personInfo.getId());
						if (list!=null && list.size()>0) {
							PersonInfo person = personInfoDao.get(personInfo.getId());
			    			Bsb100019Vo vo =new Bsb100019Vo();
			    			vo.setChangeMsgType("01");
			    			vo.setCustName(person.getName());
			    			vo.setIdNo(person.getIdnum());
			    			vo.setIdType("01");
			    			vo.setNewMobNo(personTelInfo.getContent());
			    			vo.setOldMobNo(person.getMphone());
			    			vo.setTxncd("BYXY0019");
			    			JSONObject object = GatewayUtils.callCateWayInterface(vo, GatewayFuncIdEnum.电话号码变更.getCode());
						}
				        personInfo.setMphone(content);
						break;
					case 公司电话 :
						if (Strings.isEmpty(content)) {
							throw new PlatformException(ResponseEnum.FULL_MSG, new Object[] { "公司电话不允许为空"});
						}
						personInfo.setcTel(content);
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
				switch(telType) {
					case 家庭电话 :
						personContactInfo.setTel(content);
						break;
					case 手机 :
						personContactInfo.setMphone(content);
						break;
					case 公司电话 :
						personContactInfo.setCtel(content);
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
	 * 核心接口中更新借款人的电话信息
	 * @param person
	 */
	@Override
	@Transactional
	public void saveCorePersonTel(PersonInfo person) {
		if (person.getTel() != null) {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("className", "zdsys.Borrower");
			paramMap.put("objectId", person.getId());
			paramMap.put("content", person.getTel());
			if (!personTelInfoDaoImpl.exists(paramMap)) {
				PersonTelInfo tel = new PersonTelInfo();
				tel.setId(sequencesServiceImpl.getSequences(SequencesEnum.PERSON_TEL_INFO));
				tel.setClassName("zdsys.Borrower");
				tel.setObjectId(person.getId());
				tel.setContent(person.getTel());
				tel.setTelType(TelType.家庭电话.getName());
				tel.setPriority(Priority.高.getName());
				tel.setValid("t");
				personTelInfoDaoImpl.insert(tel);
			}
		}
		
		if (person.getFax() != null) {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("className", "zdsys.Borrower");
			paramMap.put("objectId", person.getId());
			paramMap.put("content", person.getFax());
			if (!personTelInfoDaoImpl.exists(paramMap)) {
				PersonTelInfo tel = new PersonTelInfo();
				tel.setId(sequencesServiceImpl.getSequences(SequencesEnum.PERSON_TEL_INFO));
				tel.setClassName("zdsys.Borrower");
				tel.setObjectId(person.getId());
				tel.setContent(person.getFax());
				tel.setTelType(TelType.传真.getName());
				tel.setPriority(Priority.高.getName());
				tel.setValid("t");
				personTelInfoDaoImpl.insert(tel);
			}
		}
		
		if (person.getcTel() != null) {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("className", "zdsys.Borrower");
			paramMap.put("objectId", person.getId());
			paramMap.put("content", person.getcTel());
			if (!personTelInfoDaoImpl.exists(paramMap)) {
				PersonTelInfo tel = new PersonTelInfo();
				tel.setId(sequencesServiceImpl.getSequences(SequencesEnum.PERSON_TEL_INFO));
				tel.setClassName("zdsys.Borrower");
				tel.setObjectId(person.getId());
				tel.setContent(person.getcTel());
				tel.setTelType(TelType.公司电话.getName());
				tel.setPriority(Priority.高.getName());
				tel.setValid("t");
				personTelInfoDaoImpl.insert(tel);
			}
		}
		
		if (person.getMphone() != null) {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("className", "zdsys.Borrower");
			paramMap.put("objectId", person.getId());
			paramMap.put("content", person.getMphone());
			if (!personTelInfoDaoImpl.exists(paramMap)) {
				PersonTelInfo tel = new PersonTelInfo();
				tel.setId(sequencesServiceImpl.getSequences(SequencesEnum.PERSON_TEL_INFO));
				tel.setClassName("zdsys.Borrower");
				tel.setObjectId(person.getId());
				tel.setContent(person.getMphone());
				tel.setTelType(TelType.手机.getName());
				tel.setPriority(Priority.高.getName());
				tel.setValid("t");
				personTelInfoDaoImpl.insert(tel);
			}
		}
	}

	/**
	 * 核心接口中更新借款人的联系人的电话信息
	 * @param contact
	 */
	@Override
	@Transactional
	public void saveCorePersonContactTel(PersonContactInfo contact) {
		if (contact.getTel() != null) {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("className", "zdsys.Contact");
			paramMap.put("objectId", contact.getId());
			paramMap.put("content", contact.getTel());
			if (!personTelInfoDaoImpl.exists(paramMap)) {
				PersonTelInfo tel = new PersonTelInfo();
				tel.setId(sequencesServiceImpl.getSequences(SequencesEnum.PERSON_TEL_INFO));
				tel.setClassName("zdsys.Contact");
				tel.setObjectId(contact.getId());
				tel.setContent(contact.getTel());
				tel.setTelType(TelType.家庭电话.getName());
				tel.setPriority(Priority.高.getName());
				tel.setValid("t");
				personTelInfoDaoImpl.insert(tel);
			}
		}
		
		if (contact.getCtel() != null) {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("className", "zdsys.Contact");
			paramMap.put("objectId", contact.getId());
			paramMap.put("content", contact.getCtel());
			if (!personTelInfoDaoImpl.exists(paramMap)) {
				PersonTelInfo tel = new PersonTelInfo();
				tel.setId(sequencesServiceImpl.getSequences(SequencesEnum.PERSON_TEL_INFO));
				tel.setClassName("zdsys.Contact");
				tel.setObjectId(contact.getId());
				tel.setContent(contact.getCtel());
				tel.setTelType(TelType.公司电话.getName());
				tel.setPriority(Priority.高.getName());
				tel.setValid("t");
				personTelInfoDaoImpl.insert(tel);
			}
		}
		
		if (contact.getMphone() != null) {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("className", "zdsys.Contact");
			paramMap.put("objectId", contact.getId());
			paramMap.put("content", contact.getMphone());
			if (!personTelInfoDaoImpl.exists(paramMap)) {
				PersonTelInfo tel = new PersonTelInfo();
				tel.setId(sequencesServiceImpl.getSequences(SequencesEnum.PERSON_TEL_INFO));
				tel.setClassName("zdsys.Contact");
				tel.setObjectId(contact.getId());
				tel.setContent(contact.getMphone());
				tel.setTelType(TelType.手机.getName());
				tel.setPriority(Priority.高.getName());
				tel.setValid("t");
				personTelInfoDaoImpl.insert(tel);
			}
		}
		
		
	}

	public List<PersonTelInfo> findListByVo(PersonTelInfo personTelInfo) {
		return personTelInfoDaoImpl.findListByVo(personTelInfo);
	}

	@Override
	public PersonInfo findPerson(Map<String, String> paramMap) {
		// TODO Auto-generated method stub
		return personInfoDao.findPerson(paramMap);
	}

	@Override
	public List<PersonTelInfo> findByPhone(String phone) {
		// TODO Auto-generated method stub
		return personTelInfoDaoImpl.findByPhone(phone);
	}

	@Override
	public List<VloanPersonOrg> findByPersonId(Long borrowerId) {
		// TODO Auto-generated method stub
		return personTelInfoDaoImpl.findByPersonId(borrowerId);
	}
	
}