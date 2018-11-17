package com.zdmoney.credit.loan.dao.pub;

import java.util.List;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.framework.vo.abs.entity.PaidInEntity;
import com.zdmoney.credit.loan.domain.PaidInEntityMemo;

public interface IPaidInEntityDao extends IBaseDao<PaidInEntityMemo> {

    List<PaidInEntity> queryPaidEntityList();

    void insertRequestParam(PaidInEntityMemo pe);

    String selectLoanId(String pactNo);

}
