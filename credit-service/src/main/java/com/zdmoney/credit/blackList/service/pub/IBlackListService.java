package com.zdmoney.credit.blackList.service.pub;

import java.util.Date;
import java.util.List;

import org.dom4j.DocumentException;

import com.zdmoney.credit.blacklist.domain.ComBlacklist;
import com.zdmoney.credit.blacklist.domain.Customer;
import com.zdmoney.credit.blacklist.domain.Enterprise;

public interface IBlackListService{
	
	/**
	 * 
	 * @param string
	 * @param string2
	 * @param string3
	 * @param string4
	 * @param string5
	 * @param string6
	 * @return
	 * @throws DocumentException 
	 */
	List<Customer> getCustomerBlackList(String string, String string2,
			String string3, String string4, String string5, String string6) throws DocumentException;

	/**
	 * 
	 * @param name
	 * @param idCard
	 * @return
	 */
	ComBlacklist findByNameAndIdnum(String name, String idCard);

	/**
	 * 
	 * @param string
	 * @param string2
	 * @param string3
	 * @param string4
	 * @param string5
	 * @param string6
	 * @return
	 * @throws DocumentException 
	 */
	List<Enterprise> getEnterpriseBlackList(String string, String string2,
			String string3, String string4, String string5, String string6) throws DocumentException;

	/**
	 * 创建黑名单：个人
	 * @param customer
	 */
	void createComBlacklist(Customer customer);

	/**
	 * 创建黑名单：企业
	 * @param enterprise
	 */
	void createComBlacklist(Enterprise enterprise);

	/**
	 * 
	 * @param name
	 * @return
	 */
	ComBlacklist findByCompany(String name);

	/**
	 * 
	 * @param customerAddList
	 */
	boolean addCustomerBlackList(List<Customer> customerAddList);

	/**
	 * 
	 * @param enterpriseAddList
	 */
	boolean addEnterpriseBlackList(List<Enterprise> enterpriseAddList);

	/**
	 * 
	 * @param date
	 * @return
	 */
	List<ComBlacklist> findLocalCustomerBlacklist(Date date);

	/**
	 * 
	 * @param date
	 * @return
	 */
	List<ComBlacklist> findLocalEnterpriseBlacklist(Date date);

}
