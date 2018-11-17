package com.zdmoney.credit.repay.dao.pub;

import java.util.List;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.repay.vo.VRepaymentDetail;

/**
 * 线上还款明细DAO接口类
 * @author 00236640
 * @version $Id: IVRepaymentDetailDao.java, v 0.1 2015年9月11日 下午3:41:04 00236640 Exp $
 */
public interface IVRepaymentDetailDao extends IBaseDao<VRepaymentDetail>{
    
    /**
     * 查询线上还款明细信息，最大件数不超过20000件
     * @param repaymentDetail
     * @return
     */
    public List<VRepaymentDetail> queryRepaymentDetailList(VRepaymentDetail repaymentDetail);
}
