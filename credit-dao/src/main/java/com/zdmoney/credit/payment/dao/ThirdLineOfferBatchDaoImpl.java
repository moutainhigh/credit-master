package com.zdmoney.credit.payment.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.payment.dao.pub.IThirdLineOfferBatchDao;
import com.zdmoney.credit.payment.domain.ThirdLineOfferBatch;

@Repository
public class ThirdLineOfferBatchDaoImpl extends BaseDaoImpl<ThirdLineOfferBatch> implements IThirdLineOfferBatchDao {

    public List<ThirdLineOfferBatch> findHaTwoOfferBatch() {
        return getSqlSession().selectList(getIbatisMapperNameSpace() + ".findHaTwoOfferBatch");
    }

    public List<ThirdLineOfferBatch> findHaTwoOfferBatchNotExport() {
        return getSqlSession().selectList(getIbatisMapperNameSpace() + ".findHaTwoOfferBatchNotExport");
    }

    public List<ThirdLineOfferBatch> findThirdLineOfferBatch() {
        return getSqlSession().selectList(getIbatisMapperNameSpace() + ".findThirdLineOfferBatch");
    }

    public ThirdLineOfferBatch findOfferBatchId() {
        return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".findOfferBatchId");
    }

    public ThirdLineOfferBatch findLatelyOfferBatchInfo() {
        return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".findLatelyOfferBatchInfo");
    }
}
