package com.zdmoney.credit.person.dao;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.person.dao.pub.IPersonAddressInfoDao;
import com.zdmoney.credit.person.domain.PersonAddressInfo;

/**
 * 借款人地址变更记录Dao接口层(实现)
 * @author Ivan
 *
 */
@Repository
public class PersonAddressInfoDaoImpl extends BaseDaoImpl<PersonAddressInfo> implements IPersonAddressInfoDao {
	
}
