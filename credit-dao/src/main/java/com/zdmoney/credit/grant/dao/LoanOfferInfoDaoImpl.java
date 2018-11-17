package com.zdmoney.credit.grant.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.grant.dao.pub.ILoanOfferInfoDao;
import com.zdmoney.credit.grant.domain.LoanOfferInfo;

/**
 * 
 * @author YM10104
 *
 */
@Repository
public class LoanOfferInfoDaoImpl extends BaseDaoImpl<LoanOfferInfo> implements ILoanOfferInfoDao {

	@Override
	public LoanOfferInfo findbySerialNo(String serialNo) {
		
		return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".findbySerialNo",serialNo);
	}

	@Override
	public List<LoanOfferInfo> findInfoList() {
		
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".findInfoList");
	}
	
}
