package com.zdmoney.credit.repay.service.pub;

import java.util.List;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.repay.vo.VRepaymentDetail;

/**
 * 线上还款明细Service接口类
 * @author 00236640
 * @version $Id: IVRepaymentDetailService.java, v 0.1 2015年9月11日 下午3:42:00 00236640 Exp $
 */
public interface IVRepaymentDetailService {
    
    /**
     * 分页查询线上还款明细信息
     * @param repaymentDetail
     * @return
     */
    public Pager findWithPg(VRepaymentDetail repaymentDetail);
    
    /**
     * 查询线上还款明细信息，最大件数不超过20000件
     * @param repaymentDetail
     * @return
     */
    public List<VRepaymentDetail> queryRepaymentDetailList(VRepaymentDetail repaymentDetail);
}
