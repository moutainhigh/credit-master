package com.zdmoney.credit.operation.service.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.loan.domain.LoanBase;
import com.zdmoney.credit.operation.domain.LoanContractInfo;

public interface ILoanContractInfoService {
	
    /**
     * 带分页查询
     * @param performanceBelongInfo 条件实体对象
     * @return
     */
    public Pager findWithPg(LoanContractInfo loanContractInfo);
    
    /**
     * 带分页查询
     * @param params 条件map对象
     * @return
     */
    public Pager findWithPgByMap(Map<String,Object> params);
    
    /**
     * 查询当前机构下的客服人员信息
     * @param params
     * @return
     */
    public List<Map<String,Object>> getCrmOptionInfo(Map<String,Object> params);
    
    /**
     * 变更借款合同客服
     * @param loanBase
     * @return
     */
    public int updateLoanContractCrmInfo(LoanBase loanBase);
    
    /**
     * 查询借款合同信息
     * @param loanContractInfo
     * @return
     */
    public List<LoanContractInfo> findListByVo(LoanContractInfo loanContractInfo);
    /**
	 * 通过客服id获取所属网点(管理营业部)
	 * */
	public Long getSalesDepartmentId(Long newCrmId);
    
}
