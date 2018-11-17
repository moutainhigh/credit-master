package com.zdmoney.credit.operation.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.loan.dao.pub.ILoanBaseDao;
import com.zdmoney.credit.loan.domain.LoanBase;
import com.zdmoney.credit.operation.dao.pub.IPerformanceBelongInfoDao;
import com.zdmoney.credit.operation.domain.PerformanceBelongInfo;
import com.zdmoney.credit.operation.service.pub.IPerformanceBelongInfoService;

@Service
public class PerformanceBelongInfoServiceImpl implements IPerformanceBelongInfoService {

    @Autowired
    private IPerformanceBelongInfoDao performanceBelongInfoDao;
    
    @Autowired
    private ILoanBaseDao loanBaseDao;
    
    public Pager findWithPg(PerformanceBelongInfo performanceBelongInfo) {
        return performanceBelongInfoDao.findWithPg(performanceBelongInfo);
    }

    public List<Map<String, Object>> getSalesManOptionInfo(Map<String, Object> params) {
        return performanceBelongInfoDao.getSalesManOptionInfo(params);
    }

    public List<Map<String, Object>> getSalesTeamOptionInfo(Map<String, Object> params) {
        return performanceBelongInfoDao.getSalesTeamOptionInfo(params);
    }

    public int updatePerformanceBelongInfo(LoanBase loanBase) {
        return loanBaseDao.update(loanBase);
    }
}
