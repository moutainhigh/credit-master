package com.zdmoney.credit.fee.service.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.fee.domain.vo.LoanFeeInput;

/**
 * 收费录入 Service层接口
 * @author 00236640
 * @version $Id: ILoanFeeInputService.java, v 0.1 2016年7月14日 下午5:52:07 00236640 Exp $
 */
public interface ILoanFeeInputService {

    /**
     * 查询收费录入信息
     * @param loanFeeInput
     * @return
     */
    public List<LoanFeeInput> findListByVo(LoanFeeInput loanFeeInput);
    
    /**
     * 分页查询收费录入信息
     * @param loanFeeInput
     * @return
     */
    public Pager findWithPg(LoanFeeInput loanFeeInput);
    
    /**
     * 分页查询收费信息
     * @param loanFeeInput
     * @return
     */
    public Pager findFeeListWithPg(Map<String, Object> paramMap);
}
