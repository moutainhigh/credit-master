package com.zdmoney.credit.loan.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.loan.dao.pub.ILoanManageSalesDepLogDao;
import com.zdmoney.credit.loan.domain.LoanManageSalesDepLog;
import com.zdmoney.credit.loan.service.pub.ILoanManageSalesDepLogService;
import com.zdmoney.credit.system.service.pub.ISequencesService;

@Service
public class LoanManageSalesDepLogServiceImpl implements ILoanManageSalesDepLogService {

    @Autowired
    private ILoanManageSalesDepLogDao loanManageSalesDepLogDao;
    
    @Autowired 
    private ISequencesService sequencesService;
    
    public int updateLastManageSalesDeptLog(LoanManageSalesDepLog loanManageSalesDepLog) {
        return loanManageSalesDepLogDao.updateLastManageSalesDeptLog(loanManageSalesDepLog);
    }

    public LoanManageSalesDepLog insertLoanManageSalesDeptLog(LoanManageSalesDepLog loanManageSalesDepLog) {
        Long id = sequencesService.getSequences(SequencesEnum.LOAN_MANAGE_SALES_DEP_LOG);
        loanManageSalesDepLog.setId(id);
        return loanManageSalesDepLogDao.insert(loanManageSalesDepLog);
    }

    /**
     * 通过loanId查询
     * @param loanId
     * @return
     */
	public LoanManageSalesDepLog findByLoanId(Long loanId) {
		return loanManageSalesDepLogDao.findByLoanId(loanId);
	}
}
