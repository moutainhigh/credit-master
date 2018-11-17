package com.zdmoney.credit.fee.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.fee.dao.pub.ILoanFeeInputDao;
import com.zdmoney.credit.fee.domain.vo.LoanFeeInput;
import com.zdmoney.credit.fee.service.pub.ILoanFeeInputService;

@Service
public class LoanFeeInputServiceImpl implements ILoanFeeInputService {

    @Autowired
    private ILoanFeeInputDao loanFeeInputDao;
    
    public List<LoanFeeInput> findListByVo(LoanFeeInput loanFeeInput) {
        return loanFeeInputDao.findListByVo(loanFeeInput);
    }

    public Pager findWithPg(LoanFeeInput loanFeeInput) {
        return loanFeeInputDao.findWithPg(loanFeeInput);
    }
    
    public Pager findFeeListWithPg(Map<String, Object> paramMap) {
        return loanFeeInputDao.searchVLoanInfoFeeWithPg(paramMap);
    }
}
