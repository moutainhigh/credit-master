package com.zdmoney.credit.payment.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.payment.dao.pub.IThirdLineOfferTransactionDao;
import com.zdmoney.credit.payment.domain.ThirdLineOfferTransaction;

@Repository
public class ThirdLineOfferTransactionDaoImpl extends BaseDaoImpl<ThirdLineOfferTransaction> implements IThirdLineOfferTransactionDao {

    public List<ThirdLineOfferTransaction> findHaTwoOfferTransactionByBatchId(Long batchId) {
        return getSqlSession().selectList(getIbatisMapperNameSpace()+".findHaTwoOfferTransactionByBatchId", batchId);
    }

    public ThirdLineOfferTransaction findHaTwoOfferTransactionByMap(Map<String, Object> paramMap) {
        return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".findHaTwoOfferTransactionByMap", paramMap);
    }
    
    public List<ThirdLineOfferTransaction> findOfferTransactionListByMap(Map<String, Object> params) {
        return getSqlSession().selectList(getIbatisMapperNameSpace() + ".findOfferTransactionListByMap", params);
    }
}
