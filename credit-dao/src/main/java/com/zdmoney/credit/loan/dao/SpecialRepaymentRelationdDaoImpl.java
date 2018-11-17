package com.zdmoney.credit.loan.dao;

import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.loan.dao.pub.ISpecialRepaymentRelationdDao;
import com.zdmoney.credit.loan.domain.SpecialRepaymentRelation;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ym10094 on 2017/5/15.
 */
@Repository
public class SpecialRepaymentRelationdDaoImpl extends BaseDaoImpl<SpecialRepaymentRelation> implements ISpecialRepaymentRelationdDao {
    @Override
    public List<SpecialRepaymentRelation> querySpecialRepaymentRelation(Long loanId, String applicationStatus) {
        Map<String,Object> params = new HashMap<>();
        params.put("loanId",loanId);
        params.put("applicationStatus",applicationStatus);
        List<SpecialRepaymentRelation> specialRepaymentRelations = getSqlSession().selectList(getIbatisMapperNameSpace() + ".querySpecialRepaymentRelation",params);
        if (CollectionUtils.isEmpty(specialRepaymentRelations)) {
            return Collections.emptyList();
        }
        return specialRepaymentRelations;
    }

    @Override
    public void deleteByEffectiveId(Long effectiveId) {
        this.getSqlSession().update(getIbatisMapperNameSpace() + ".deleteByEffectiveId", effectiveId);

    }
}
