package com.zdmoney.credit.loan.dao;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.loan.dao.pub.ILoanBaseOneTimeSettlementDao;
import com.zdmoney.credit.loan.domain.LoanBaseOneTimeSettlement;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ym10094 on 2016/11/17.
 */
@Repository
public class LoanBaseOneTimeSettlementDaoImpl extends BaseDaoImpl<LoanBaseOneTimeSettlement> implements ILoanBaseOneTimeSettlementDao {
    @Override
    public List<LoanBaseOneTimeSettlement> findOneTimeSettlementByAppNoAndState(String appNo, String[] applyStates) {
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("appNo",appNo);
        paramMap.put("applyStates",applyStates);
        List<LoanBaseOneTimeSettlement> loanBaseOneTimeSettlements = this.findListByMap(paramMap);
        return loanBaseOneTimeSettlements;
    }

    @Override
    public List<LoanBaseOneTimeSettlement> findOneTimeSettlementByAppNoAndState(String appNo, String applyState) {
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("appNo",appNo);
        paramMap.put("applyState",applyState);
        List<LoanBaseOneTimeSettlement> loanBaseOneTimeSettlements = this.findListByMap(paramMap);
        return loanBaseOneTimeSettlements;
    }

    @Override
    public List<LoanBaseOneTimeSettlement> findOneTimeSettlementByLoanIdAndState(Long loanId, String[] applyStates) {
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("loanId",loanId);
        paramMap.put("applyStates",applyStates);
        List<LoanBaseOneTimeSettlement> loanBaseOneTimeSettlements = this.findListByMap(paramMap);
        return loanBaseOneTimeSettlements;
    }

    @Override
    public List<LoanBaseOneTimeSettlement> findOneTimeSettlementByLoanIdAndState(Long loanId, String applyState) {
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("loanId",loanId);
        paramMap.put("applyState",applyState);
        List<LoanBaseOneTimeSettlement> loanBaseOneTimeSettlements = this.findListByMap(paramMap);
        return loanBaseOneTimeSettlements;
    }

    @Override
    public List<LoanBaseOneTimeSettlement> findOneTimeSettlementBycontractNumAndState(String contractNum, String[] applyStates) {
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("contractNum",contractNum);
        paramMap.put("applyStates",applyStates);
        List<LoanBaseOneTimeSettlement> loanBaseOneTimeSettlements = this.findListByMap(paramMap);
        return loanBaseOneTimeSettlements;
    }

    @Override
    public List<LoanBaseOneTimeSettlement> findOneTimeSettlementBycontractNumAndState(String contractNum, String applyState) {
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("contractNum",contractNum);
        paramMap.put("applyState",applyState);
        List<LoanBaseOneTimeSettlement> loanBaseOneTimeSettlements = this.findListByMap(paramMap);
        return loanBaseOneTimeSettlements;
    }
}
