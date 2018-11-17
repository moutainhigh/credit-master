package com.zdmoney.credit.repay.dao.pub;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.repay.vo.LoanApplyDetailVo;
import com.zdmoney.credit.repay.vo.RequestManagementVo;

/**
 * 申请书管理DAO接口类
 * @author 00236640
 */
public interface IRequestManagementDao extends IBaseDao<RequestManagementVo>{
    
    /**
     * 查询放款申请明细
     * @param params
     * @return
     */
    public List<LoanApplyDetailVo> queryLoanApplyDetailList(Map<String,Object> params);
    
    /**
     * 查询还款计划信息
     * @param params
     * @return
     */
    public List<Map<String,Object>> queryPayPlanList(Map<String,Object> params);
    /**
     * 查询划拨申请书统计信息
     * @param params
     * @return
     */
    public Map<String,Object> queryApplyPdfInfo(Map<String,Object> params);

    public BigDecimal queryAlreadyGrantMoney(List<String> batchNums);
}
