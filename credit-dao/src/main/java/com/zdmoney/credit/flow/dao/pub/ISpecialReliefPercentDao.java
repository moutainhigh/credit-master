package com.zdmoney.credit.flow.dao.pub;

import com.zdmoney.credit.flow.domain.SpecialReliefPercent;
import com.zdmoney.credit.framework.dao.pub.IBaseDao;

import java.math.BigDecimal;

/**
 * Created by ym10094 on 2017/8/15.
 */
public interface ISpecialReliefPercentDao extends IBaseDao<SpecialReliefPercent> {

    public BigDecimal queryReliefPercent(String grade,Long currentTerm);
}