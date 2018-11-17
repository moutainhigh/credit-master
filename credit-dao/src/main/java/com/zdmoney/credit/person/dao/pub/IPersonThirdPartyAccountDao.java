package com.zdmoney.credit.person.dao.pub;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.person.domain.PersonThirdPartyAccount;

public interface IPersonThirdPartyAccountDao extends IBaseDao<PersonThirdPartyAccount>{

	/**
	 * 根据account查询
	 * @param account
	 * @return
	 */
	public PersonThirdPartyAccount findByAccount(String account);

	/**
	 * 根据身份证查询
	 * @param idnum
	 * @return
	 */
	public PersonThirdPartyAccount findByIdnum(String idnum);
}
