package com.zdmoney.credit.person.dao;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.person.dao.pub.IPersonBankMapDao;
import com.zdmoney.credit.person.domain.PersonBankMap;

/**
 * 客户、银行映射信息
 * @author 00234770
 *
 */
@Repository
public class PersonBankMapDaoImpl extends BaseDaoImpl<PersonBankMap>implements IPersonBankMapDao {

}
