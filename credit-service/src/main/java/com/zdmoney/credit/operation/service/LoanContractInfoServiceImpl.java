package com.zdmoney.credit.operation.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.loan.dao.pub.ILoanBaseDao;
import com.zdmoney.credit.loan.domain.LoanBase;
import com.zdmoney.credit.operation.dao.pub.ILoanContractInfoDao;
import com.zdmoney.credit.operation.domain.LoanContractInfo;
import com.zdmoney.credit.operation.service.pub.ILoanContractInfoService;

@Service
public class LoanContractInfoServiceImpl implements ILoanContractInfoService {

    @Autowired
    private ILoanContractInfoDao loanContractInfoDao;
    
    @Autowired
    private ILoanBaseDao loanBaseDao;
    
    public Pager findWithPg(LoanContractInfo loanContractInfo) {
        return loanContractInfoDao.findWithPg(loanContractInfo);
    }

    public Pager findWithPgByMap(Map<String, Object> params) {
        return loanContractInfoDao.findWithPgByMap(params);
    }

    public List<Map<String, Object>> getCrmOptionInfo(Map<String, Object> params) {
        return loanContractInfoDao.getCrmOptionInfo(params);
    }

    public int updateLoanContractCrmInfo(LoanBase loanBase) {
        return loanBaseDao.update(loanBase);
        
    }

    public List<LoanContractInfo> findListByVo(LoanContractInfo loanContractInfo) {
        return loanContractInfoDao.findListByVo(loanContractInfo);
    }

	
	public Long getSalesDepartmentId(Long newCrmId) {
		return  loanBaseDao.getSalesDepartmentId(newCrmId);
		
	}
}
