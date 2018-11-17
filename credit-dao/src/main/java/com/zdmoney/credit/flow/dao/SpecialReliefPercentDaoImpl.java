package com.zdmoney.credit.flow.dao;

import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.flow.dao.pub.ISpecialReliefPercentDao;
import com.zdmoney.credit.flow.domain.SpecialReliefPercent;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ym10094 on 2017/8/15.
 */
@Repository
public class SpecialReliefPercentDaoImpl extends BaseDaoImpl<SpecialReliefPercent> implements ISpecialReliefPercentDao {
    @Override
    public BigDecimal queryReliefPercent(String grade, Long currentTerm) {
        Map<String,Object> params = new HashMap<>();
        params.put("grade", Strings.isEmpty(grade) ? "" : grade);
        params.put("currentTerm",currentTerm);
        BigDecimal reliefPercent = getSqlSession().selectOne(getIbatisMapperNameSpace() + ".queryReliefPercent",params);
        if (reliefPercent == null) {
            return new BigDecimal(0.00);
        }
        return reliefPercent;
    }
}
