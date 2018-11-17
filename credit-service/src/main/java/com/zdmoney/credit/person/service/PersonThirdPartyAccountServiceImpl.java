package com.zdmoney.credit.person.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.person.dao.pub.IPersonThirdPartyAccountDao;
import com.zdmoney.credit.person.domain.PersonThirdPartyAccount;
import com.zdmoney.credit.person.service.pub.IPersonThirdPartyAccountService;

@Service
public class PersonThirdPartyAccountServiceImpl implements IPersonThirdPartyAccountService{

	@Autowired
	private IPersonThirdPartyAccountDao personThirdPartyAccountDao;
	
	@Override
	public PersonThirdPartyAccount findById(Long thirdPartyAccountId) {
		return personThirdPartyAccountDao.get(thirdPartyAccountId);
	}

	@Override
	public PersonThirdPartyAccount findByIdnum(String idnum) {
		return personThirdPartyAccountDao.findByIdnum(idnum);
	}

}
