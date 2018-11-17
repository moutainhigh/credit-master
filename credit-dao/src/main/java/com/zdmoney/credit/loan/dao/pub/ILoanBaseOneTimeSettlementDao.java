package com.zdmoney.credit.loan.dao.pub;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.loan.domain.LoanBaseOneTimeSettlement;

import java.util.List;

/**
 * Created by ym10094 on 2016/11/17.
 */
public interface ILoanBaseOneTimeSettlementDao extends IBaseDao<LoanBaseOneTimeSettlement> {
    /**
     * 根据aapNo和申请状态 获取外贸2 一次性结清申请
     * @param appNo
     * @param applyStates
     * @return
     */
    public List<LoanBaseOneTimeSettlement> findOneTimeSettlementByAppNoAndState(String appNo, String[] applyStates);
    /**
     * 根据aapNo和申请状态 获取外贸2 一次性结清申请
     * @param appNo
     * @param applyState
     * @return
     */
    public List<LoanBaseOneTimeSettlement> findOneTimeSettlementByAppNoAndState(String appNo, String applyState);
    /**
     * loanId 获取外贸2 一次性结清申请
     * @param loanId
     * @param applyStates
     * @return
     */
    public List<LoanBaseOneTimeSettlement> findOneTimeSettlementByLoanIdAndState(Long loanId, String[] applyStates);
    /**
     * loanId 获取外贸2 一次性结清申请
     * @param loanId
     * @param applyState
     * @return
     */
    public List<LoanBaseOneTimeSettlement> findOneTimeSettlementByLoanIdAndState(Long loanId, String applyState);
    /**
     * contractNum 和申请状态  获取外贸2 一次性结清申请
     * @param contractNum
     * @param applyStates
     * @return
     */
    public List<LoanBaseOneTimeSettlement> findOneTimeSettlementBycontractNumAndState(String contractNum, String[] applyStates);
    /**
     * 根据contractNum 和申请状态 获取外贸2 一次性结清申请
     * @param contractNum
     * @param applyState
     * @return
     */
    public List<LoanBaseOneTimeSettlement> findOneTimeSettlementBycontractNumAndState(String contractNum, String applyState);

}
