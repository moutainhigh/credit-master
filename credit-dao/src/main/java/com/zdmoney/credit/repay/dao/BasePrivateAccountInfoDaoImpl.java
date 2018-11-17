package com.zdmoney.credit.repay.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.repay.dao.pub.IBasePrivateAccountInfoDao;
import com.zdmoney.credit.repay.domain.BasePrivateAccountInfo;

@Repository
public class BasePrivateAccountInfoDaoImpl extends BaseDaoImpl<BasePrivateAccountInfo> implements IBasePrivateAccountInfoDao {

    public List<Map<String, Object>> findPrivateAccountReceiveInfo(BasePrivateAccountInfo basePrivateAccountInfo) {
        return getSqlSession().selectList(getIbatisMapperNameSpace() + ".findPrivateAccountReceiveInfo",basePrivateAccountInfo);
    }

    public int updateAccountInfoForExport(Map<String, Object> params) {
        return getSqlSession().update(getIbatisMapperNameSpace() + ".updateAccountInfoForExport", params);
    }

    public int updateAccountInfoForCancel(BasePrivateAccountInfo basePrivateAccountInfo) {
        return getSqlSession().update(getIbatisMapperNameSpace() + ".updateAccountInfoForCancel", basePrivateAccountInfo);
    }

    @Override
    public List<Map<String, Object>> findQueryResultMapList(BasePrivateAccountInfo basePrivateAccountInfo) {
        return getSqlSession().selectList(getIbatisMapperNameSpace() + ".findQueryResultMapList",basePrivateAccountInfo);
    }
}
