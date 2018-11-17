package com.zdmoney.credit.loan.dao;

import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.loan.dao.pub.ISpecialRepaymentApplyDao;
import com.zdmoney.credit.loan.domain.SpecialRepaymentApply;
import com.zdmoney.credit.loan.domain.SpecialRepaymentRelation;
import com.zdmoney.credit.loan.vo.ReliefQueryReportVo;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Created by ym10094 on 2017/5/15.
 */
@Repository
public class SpecialRepaymentApplyDaoImpl extends BaseDaoImpl<SpecialRepaymentApply> implements ISpecialRepaymentApplyDao {
    @Override
    public List<SpecialRepaymentApply> querySpecialRepaymentApplyByApplyApplicationStatus(long loanId, String applicationStatus) {
        Map<String,Object> params = new HashMap<>();
        params.put("loanId",loanId);
        params.put("applicationStatus",applicationStatus);
        List<SpecialRepaymentApply> specialRepaymentApplies = this.findListByMap(params);
        if (CollectionUtils.isEmpty(specialRepaymentApplies)) {
            return Collections.emptyList();
        }
        return specialRepaymentApplies;
    }

    @Override
    public List<SpecialRepaymentApply> querySpecialRepaymentApplyByApply(Map<String, Object> params) {
        List<SpecialRepaymentApply> specialRepaymentApplies = this.findListByMap(params);
        if (CollectionUtils.isEmpty(specialRepaymentApplies)) {
            return Collections.emptyList();
        }
        return specialRepaymentApplies;
    }

    @Override
    public Pager queryApplyReliefPage(Map<String, Object> params) {
        Pager pager = (Pager) params.get("pager");
        if (pager == null) {
            pager = new Pager();
        }
        pager.setSearchDataSqlMapId(getIbatisMapperNameSpace() + ".queryApplyReliefInfosDetail");
        pager.setSearchCountSqlMapId(getIbatisMapperNameSpace() + ".queryApplyReliefInfosCount");
        return doPager(pager,params);
    }

    @Override
    public SpecialRepaymentApply querySpecialRepaymentApplyByEffectiveid(Long effectiveId) {
        Map<String,Object> parmas = new HashMap<>();
        parmas.put("effectiveId",effectiveId);
        return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".querySpecialRepaymentApplyByEffectiveid",parmas);
    }

    @Override
    public Map<String, Object> queryEffectiveLoanSpecialRepayment(Map<String, Object> params) {
        return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".queryEffectiveLoanSpecialRepayment",params);
    }

    @Override
    public Pager queryReliefInfoPage(Map<String, Object> params) {
        Pager pager = (Pager) params.get("pager");
        if (pager == null) {
            pager = new Pager();
        }
        pager.setSearchDataSqlMapId(getIbatisMapperNameSpace() + ".queryReliefInfosDetail");
        pager.setSearchCountSqlMapId(getIbatisMapperNameSpace() + ".queryReliefInfosCount");
        return doPager(pager,params);
    }

    @Override
    public List<ReliefQueryReportVo> queryReliefQueryReport(Map<String, Object> params) {
        return getSqlSession().selectList(getIbatisMapperNameSpace() + ".queryReliefQueryReport",params);
    }

    @Override
    public Map<String, Object> queryAccount(String tradeNo) {
        Map<String,Object> params = new HashMap<>();
        params.put("tradeNo", Strings.isEmpty(tradeNo) ? "" : tradeNo);
        return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".queryAccount",params);
    }
}
