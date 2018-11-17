package com.zdmoney.credit.loan.dao.pub;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.loan.domain.SpecialRepaymentApply;
import com.zdmoney.credit.loan.domain.SpecialRepaymentRelation;
import com.zdmoney.credit.loan.vo.ReliefQueryReportVo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by ym10094 on 2017/5/15.
 */
@Repository
public interface ISpecialRepaymentApplyDao extends IBaseDao<SpecialRepaymentApply> {
    public List<SpecialRepaymentApply> querySpecialRepaymentApplyByApplyApplicationStatus(long loanId,String applicationStatus);

    public List<SpecialRepaymentApply> querySpecialRepaymentApplyByApply(Map<String,Object> params);

    public Pager queryApplyReliefPage(Map<String,Object> params);

    /**
     * 根据减免生效ID
     * @param effectiveId
     * @return
     */
    public SpecialRepaymentApply querySpecialRepaymentApplyByEffectiveid(Long  effectiveId);

    /**
     * 获取减免生效后的减免信息
     * @param params
     * @return
     */
    public Map<String,Object> queryEffectiveLoanSpecialRepayment(Map<String, Object> params);

    /**
     * 减免查询分页
     * @param params
     * @return
     */
    public Pager queryReliefInfoPage(Map<String,Object> params);

    public  List<ReliefQueryReportVo> queryReliefQueryReport(Map<String,Object> params);

    public Map<String,Object> queryAccount(String tradeNo);
}
