package com.zdmoney.credit.repay.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.repay.dao.pub.ISalesDeptRepayInfoDao;
import com.zdmoney.credit.repay.domain.SalesDeptRepayInfo;

@Repository
public class SalesDeptRepayInfoDaoImpl extends BaseDaoImpl<SalesDeptRepayInfo> implements ISalesDeptRepayInfoDao {

    public List<Map<String, Object>> getSalesDeptInfo(Map<String, Object> params) {
        return getSqlSession().selectList(getIbatisMapperNameSpace() + ".getSalesDeptInfo",params);
    }
}
