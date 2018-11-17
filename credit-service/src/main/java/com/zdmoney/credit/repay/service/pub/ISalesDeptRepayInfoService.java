package com.zdmoney.credit.repay.service.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.loan.domain.LoanBase;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.repay.domain.SalesDeptRepayInfo;

/**
 * 管理门店变更信息Service接口定义
 * @author 00236640
 * @version $Id: ISalesDeptRepayInfoService.java, v 0.1 2015年8月5日 下午4:18:40 00236640 Exp $
 */
public interface ISalesDeptRepayInfoService {
    
    /**
     * 
     * 查询当前用户所属机构下的所有营业网点信息
     * @param params
     * @return
     */
    public List<Map<String,Object>> getSalesDeptInfo(Map<String,Object> params);
    
    /**
	 * 带分页查询
	 * @param salesDeptRepayInfo 条件实体对象
	 * @return
	 */
    public Pager findWithPg(SalesDeptRepayInfo salesDeptRepayInfo);
    
    /**
     * 更新债权信息
     * @param loanBase
     * @return
     */
    public int updateLoanBaseInfo(LoanBase loanBase);
    
    /**
     * 更新借款人债权相关信息
     * @param loanInfo
     * @param oldCrmId
     * @return
     */
    public boolean updateBorrowerLoanInfo(VLoanInfo loanInfo, Long oldCrmId);
}
