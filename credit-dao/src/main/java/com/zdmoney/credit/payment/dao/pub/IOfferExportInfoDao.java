package com.zdmoney.credit.payment.dao.pub;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.payment.domain.OfferExportInfo;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by ym10094 on 2016/8/15.
 */
public interface IOfferExportInfoDao extends IBaseDao<OfferExportInfo> {
    /**
     * 国民信托线下第三方代付查询所有记录
     */
    public List<OfferExportInfo> getGuoMinXinTuoThirdLine();
    /**
     * 国民信托线下第三方代付查询总金额
     */
    public BigDecimal getGuoMinXinTuoThirdLineTotalAmount();
}
