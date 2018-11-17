package com.zdmoney.credit.operation.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.operation.dao.pub.IBasePublicAccountInfoDao;
import com.zdmoney.credit.operation.domain.BasePublicAccountInfo;

@Repository
public class BasePublicAccountInfoDaoImpl extends BaseDaoImpl<BasePublicAccountInfo> implements IBasePublicAccountInfoDao {

    public List<Map<String, Object>> findPublicAccountReceiveInfo(BasePublicAccountInfo basePublicAccountInfo) {
        String sqlId = getIbatisMapperNameSpace(SQLMAP_SUFFIX_NAME) + ".findPublicAccountReceiveInfo";
        return getSqlSession().selectList(sqlId,basePublicAccountInfo);
    }

    public List<Map<String, Object>> findPublicAccountRepayReceiveInfo(
            BasePublicAccountInfo basePublicAccountInfo) {
        String sqlId = getIbatisMapperNameSpace(SQLMAP_SUFFIX_NAME) + ".findPublicAccountRepayReceiveInfo";
        return getSqlSession().selectList(sqlId,basePublicAccountInfo);
    }

    public List<BasePublicAccountInfo> findQueryResultList(BasePublicAccountInfo basePublicAccountInfo) {
        String sqlId = getIbatisMapperNameSpace() + ".findQueryResultList";
        return getSqlSession().selectList(sqlId,basePublicAccountInfo);
    }

    public int updateAccountInfoForCancel(BasePublicAccountInfo basePublicAccountInfo) {
        String sqlId = getIbatisMapperNameSpace() + ".updateAccountInfoForCancel";
        return getSqlSession().update(sqlId, basePublicAccountInfo);
    }

    public int updateAccountInfoForExport(Map<String, Object> params) {
        String sqlId = getIbatisMapperNameSpace() + ".updateAccountInfoForExport";
        return getSqlSession().update(sqlId, params);
    }

    @Override
    public List<Map<String, Object>> findQueryResultMapList(BasePublicAccountInfo basePublicAccountInfo) {
        String sqlId = getIbatisMapperNameSpace() + ".findQueryResultMapList";
        return getSqlSession().selectList(sqlId,basePublicAccountInfo);
    }
}
