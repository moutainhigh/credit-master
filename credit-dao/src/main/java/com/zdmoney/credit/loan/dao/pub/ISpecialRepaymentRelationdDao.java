package com.zdmoney.credit.loan.dao.pub;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.loan.domain.SpecialRepaymentRelation;

import java.util.List;

/**
 * Created by ym10094 on 2017/5/15.
 */
public interface ISpecialRepaymentRelationdDao extends IBaseDao<SpecialRepaymentRelation> {
    /**
     * 查询减免申请 与减免 生效的关联关系
     * @param loanId
     * @param applicationStatus
     * @return
     */
    public List<SpecialRepaymentRelation> querySpecialRepaymentRelation(Long loanId,String applicationStatus);

    /**
     * 根据生效减免iD
     * @param effectiveId
     */
    public void deleteByEffectiveId(Long effectiveId);
}
