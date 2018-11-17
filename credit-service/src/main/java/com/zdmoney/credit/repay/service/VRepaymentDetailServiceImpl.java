package com.zdmoney.credit.repay.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.repay.dao.pub.IVRepaymentDetailDao;
import com.zdmoney.credit.repay.service.pub.IVRepaymentDetailService;
import com.zdmoney.credit.repay.vo.VRepaymentDetail;

@Service
public class VRepaymentDetailServiceImpl implements IVRepaymentDetailService{

    @Autowired
    private IVRepaymentDetailDao vRepaymentDetailDao;
    
    public Pager findWithPg(VRepaymentDetail repaymentDetail) {
        return vRepaymentDetailDao.findWithPg(repaymentDetail);
    }

    public List<VRepaymentDetail> queryRepaymentDetailList(VRepaymentDetail repaymentDetail) {
        return vRepaymentDetailDao.queryRepaymentDetailList(repaymentDetail);
    }
}
