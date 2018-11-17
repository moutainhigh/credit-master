package com.zdmoney.credit.repay.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.zdmoney.credit.common.util.CollectionUtils;
import org.olap4j.impl.ArrayMap;
import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.repay.dao.pub.IRequestManagementDao;
import com.zdmoney.credit.repay.vo.LoanApplyDetailVo;
import com.zdmoney.credit.repay.vo.RequestManagementVo;

@Repository
public class RequestManagementDaoImpl extends BaseDaoImpl<RequestManagementVo> implements IRequestManagementDao {

    public List<LoanApplyDetailVo> queryLoanApplyDetailList(Map<String, Object> params) {
        return getSqlSession().selectList(getIbatisMapperNameSpace() + ".queryLoanApplyDetailList",params);
    }

    public List<Map<String, Object>> queryPayPlanList(Map<String, Object> params) {
        return getSqlSession().selectList(getIbatisMapperNameSpace() + ".queryPayPlanList",params);
    }

    public Map<String, Object> queryApplyPdfInfo(Map<String, Object> params) {
        return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".queryApplyPdfInfo",params);
    }

    @Override
    public BigDecimal queryAlreadyGrantMoney(List<String> batchNums) {
        if (CollectionUtils.isEmpty(batchNums)) {
            return new BigDecimal("0.00");
        }
        Map<String, Object> param = new ArrayMap<>();
        param.put("batchNums", batchNums);
        return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".queryAlreadyGrantMoney", param);
    }
}