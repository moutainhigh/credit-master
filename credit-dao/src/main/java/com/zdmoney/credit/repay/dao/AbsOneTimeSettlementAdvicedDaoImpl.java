package com.zdmoney.credit.repay.dao;

import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.repay.dao.pub.IAbsOneTimeSettlementAdvicedDao;
import com.zdmoney.credit.repay.domain.AbsOneTimeSettlementAdvice;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ym10094 on 2016/12/2.
 */
@Repository
public class AbsOneTimeSettlementAdvicedDaoImpl extends BaseDaoImpl<AbsOneTimeSettlementAdvice> implements IAbsOneTimeSettlementAdvicedDao  {
    @Override
    public void insertBatch(List<AbsOneTimeSettlementAdvice> list) {
        if (CollectionUtils.isEmpty(list))
            return;
        this.getSqlSession().insert(getIbatisMapperNameSpace() + ".insertBatch", list);
    }

    @Override
    public List<AbsOneTimeSettlementAdvice> queryAbsOneTimeSettlementAdviceByStates(String[] adviceStates) {
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("adviceStates",adviceStates);
        return this.findListByMap(paramMap);
    }

}
