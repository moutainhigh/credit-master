package com.zdmoney.credit.payment.dao;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.payment.dao.pub.IOfferExportInfoDao;
import com.zdmoney.credit.payment.domain.OfferExportInfo;
import com.zdmoney.credit.xintuo.domain.XintuoguominDataDomain;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ym10094 on 2016/8/15.
 */
@Repository
public class OfferExportInfoDaoImpl extends BaseDaoImpl<OfferExportInfo> implements IOfferExportInfoDao {
    @Override
    public List<OfferExportInfo> getGuoMinXinTuoThirdLine() {
        List<OfferExportInfo> result = getSqlSession().selectList(this.getIbatisMapperNameSpace() + ".getGuoMinXinTuoThirdLine");
        return result;
    }

    @Override
    public BigDecimal getGuoMinXinTuoThirdLineTotalAmount() {
        Map<String, Object> param = new HashMap<String, Object>();
        BigDecimal totalAmount=(BigDecimal)getSqlSession().selectOne(getIbatisMapperNameSpace() + ".getGuoMinXinTuoThirdLineTotalAmount", param);
        return  totalAmount;
    }
}
