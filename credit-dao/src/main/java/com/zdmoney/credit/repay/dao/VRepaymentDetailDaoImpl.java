package com.zdmoney.credit.repay.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.repay.dao.pub.IVRepaymentDetailDao;
import com.zdmoney.credit.repay.vo.VRepaymentDetail;

@Repository
public class VRepaymentDetailDaoImpl extends BaseDaoImpl<VRepaymentDetail> implements IVRepaymentDetailDao{

    public List<VRepaymentDetail> queryRepaymentDetailList(VRepaymentDetail repaymentDetail) {
        return getSqlSession().selectList(getIbatisMapperNameSpace() + ".queryRepaymentDetailList",repaymentDetail);
    }
}