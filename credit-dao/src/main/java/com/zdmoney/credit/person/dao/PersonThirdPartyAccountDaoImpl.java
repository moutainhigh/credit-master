package com.zdmoney.credit.person.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.person.dao.pub.IPersonThirdPartyAccountDao;
import com.zdmoney.credit.person.domain.PersonThirdPartyAccount;

@Repository
public class PersonThirdPartyAccountDaoImpl  extends BaseDaoImpl<PersonThirdPartyAccount> implements IPersonThirdPartyAccountDao{

	/**
	 * 数据库基本操作，对应*mapper.xml中的id
	 */
	private static final String FINDBYACCOUNT = ".findByAccount";
	
	/**
	 * 根据account查询
	 * @param account
	 * @return
	 */
	@Override
	public PersonThirdPartyAccount findByAccount(String account) {
		return getSqlSession().selectOne(getIbatisMapperNameSpace() + FINDBYACCOUNT, account);
	}

	@Override
	public PersonThirdPartyAccount findByIdnum(String idnum) {
		PersonThirdPartyAccount personThirdPartyAccount = new PersonThirdPartyAccount();
		personThirdPartyAccount.setIdnum(idnum);
		List<PersonThirdPartyAccount> list =  this.findListByVo(personThirdPartyAccount);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}

}
