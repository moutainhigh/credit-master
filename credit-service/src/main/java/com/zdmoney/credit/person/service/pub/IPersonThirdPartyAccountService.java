package com.zdmoney.credit.person.service.pub;

import com.zdmoney.credit.person.domain.PersonThirdPartyAccount;

/**
 * 第三方银行账户service
 * @author 00232949
 *
 */
public interface IPersonThirdPartyAccountService {

	/**
	 * 根据id查找
	 * @param thirdPartyAccountId
	 * @return 
	 */
	PersonThirdPartyAccount findById(Long thirdPartyAccountId);

	/**
	 * 根据身份证查找
	 * @param idnum
	 * @return
	 */
	PersonThirdPartyAccount findByIdnum(String idnum);

}
