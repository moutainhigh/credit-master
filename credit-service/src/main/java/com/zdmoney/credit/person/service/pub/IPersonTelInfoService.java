package com.zdmoney.credit.person.service.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.loan.vo.VloanPersonOrg;
import com.zdmoney.credit.person.domain.PersonContactInfo;
import com.zdmoney.credit.person.domain.PersonTelInfo;
import com.zdmoney.credit.system.domain.PersonInfo;


/**
 * 借款人号码变更记录Service接口定义
 * @author Ivan
 *
 */
public interface IPersonTelInfoService {
	
	/**
	 * 添加号码变更记录 
	 * @param oldPersonTelInfo 老号码信息
	 * @param newPersonTelInfo 新号码信息
	 */
	public void addTelInfo(PersonTelInfo oldPersonTelInfo,PersonTelInfo newPersonTelInfo);
	
	/**
	 * 新增、修改电话数据
	 * @param personTelInfo
	 */
	public void saveOrUpdate(PersonTelInfo personTelInfo);
	
	/**
	 * 分页查询
	 * @param personTelInfo
	 * @return
	 */
	public Pager findWithPg(PersonTelInfo personTelInfo);
	
	/**
	 * 跟据编号查询实体
	 * @param id
	 * @return
	 */
	public PersonTelInfo findById(Long id);
	
	/**
	 * 核心接口中更新借款人的电话信息
	 * @param person
	 */
	public void saveCorePersonTel(PersonInfo person);
	
	/**
	 * 核心接口中更新借款人的联系人的电话信息
	 * @param contact
	 */
	public void saveCorePersonContactTel(PersonContactInfo contact);
	
	/**
	 * 查询客户电话信息
	 * @param personTelInfo
	 * @return
	 */
	public List<PersonTelInfo> findListByVo(PersonTelInfo personTelInfo);
	
	/**
	 *  通过身份证和姓名查找
	 * @param paramMap
	 * @return
	 */
	 public PersonInfo findPerson(Map<String, String> paramMap);
	 
	 /**
	  * 通过手机号码查询
	  * @param phone
	  * @return
	  */
	 public List<PersonTelInfo> findByPhone(String phone);
	 
	 /**
	  * 通过借款人ID查找债权信息
	  * @param id
	  * @return
	  */
	 public List<VloanPersonOrg> findByPersonId(Long id);
}
