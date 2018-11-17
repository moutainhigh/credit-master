package com.zdmoney.credit.operation.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.loan.dao.pub.ILoanBaseDao;
import com.zdmoney.credit.loan.domain.LoanBase;
import com.zdmoney.credit.operation.dao.pub.ILoanCsAdminInfoDao;
import com.zdmoney.credit.operation.domain.LoanCsAdminInfo;
import com.zdmoney.credit.operation.service.pub.ILoanCsAdminInfoService;

@Service
public class LoanCsAdminInfoServiceImpl implements ILoanCsAdminInfoService {

    @Autowired
    private ILoanCsAdminInfoDao loanCsAdminInfoDao;
    
    @Autowired
    private ILoanBaseDao loanBaseDao;
    
    public Pager findWithPg(LoanCsAdminInfo loanCsAdminInfo) {
        return loanCsAdminInfoDao.findWithPg(loanCsAdminInfo);
    }

    public Pager findWithPgByMap(Map<String, Object> params) {
        return loanCsAdminInfoDao.findWithPgByMap(params);
    }

    public List<Map<String, Object>> getCrmOptionInfo(Map<String, Object> params) {
        return loanCsAdminInfoDao.getCrmOptionInfo(params);
    }

    public int updateLoanContractCrmInfo(LoanBase loanBase) {
        return loanBaseDao.update(loanBase);
        
    }

    public List<LoanCsAdminInfo> findListByVo(LoanCsAdminInfo loanCsAdminInfo) {
        return loanCsAdminInfoDao.findListByVo(loanCsAdminInfo);
    }

	
	public Long getSalesDepartmentId(Long newCrmId) {
		return  loanBaseDao.getSalesDepartmentId(newCrmId);
		
	}

	@Override
	public Pager getCollectors(Map<String,Object> params) {
		return loanCsAdminInfoDao.getCollectors(params);
	}
}
