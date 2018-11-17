package com.zdmoney.credit.person.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.loan.vo.VloanPersonOrg;
import com.zdmoney.credit.person.dao.pub.IPersonTelInfoDao;
import com.zdmoney.credit.person.domain.PersonTelInfo;

/**
 * 借款人号码变更记录Dao接口层(实现)
 * @author Ivan
 *
 */
@Repository
public class PersonTelInfoDaoImpl extends BaseDaoImpl<PersonTelInfo> implements IPersonTelInfoDao {

	@Override
	public List<PersonTelInfo> findByPhone(String phone) {
		// TODO Auto-generated method stub
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".findByPhone", phone);	 
	}

	@Override
	public List<VloanPersonOrg> findByPersonId(Long borrowerId) {
		// TODO Auto-generated method stub
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".findByPersonId", borrowerId);	
	}
	
}
