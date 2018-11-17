package com.zdmoney.credit.ljs.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.bsyh.dao.pub.IEarlyRepayCalculateDao;
import com.zdmoney.credit.bsyh.domain.EarlyRepayCalculate;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.ljs.dao.pub.IPublicAccountDetailDao;
import com.zdmoney.credit.ljs.domain.PublicAccountDetail;

@Repository
public class PublicAccountDetailDaoImpl extends BaseDaoImpl<PublicAccountDetail> implements IPublicAccountDetailDao {

	@Override
	public PublicAccountDetail getLastAccountDetail(String accountType) {
		
		 return super.getSqlSession().selectOne(getIbatisMapperNameSpace() + ".getLastAccountDetail" , accountType);
	}

	@Override
	public List<PublicAccountDetail> findByMap(Map<String, Object> paramMap) {
		return super.getSqlSession().selectList(getIbatisMapperNameSpace() + ".findByMap" , paramMap);
	}

	@Override
	public void updateAccDetailByBatchNo(PublicAccountDetail publicAccountDetail) {
		super.getSqlSession().update(getIbatisMapperNameSpace() + ".updateAccDetailByBatchNo", publicAccountDetail);		
	}

	@Override
	public PublicAccountDetail findByBatchNo(String batchNo) {
		 return super.getSqlSession().selectOne(getIbatisMapperNameSpace() + ".findByBatchNo" , batchNo);
	}

}
