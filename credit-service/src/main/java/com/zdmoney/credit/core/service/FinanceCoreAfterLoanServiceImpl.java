package com.zdmoney.credit.core.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zdmoney.credit.common.constant.LoanFlowStateEnum;
import com.zdmoney.credit.core.service.pub.IFinanceCoreAfterLoanService;
import com.zdmoney.credit.loan.dao.pub.ILoanBaseDao;
import com.zdmoney.credit.loan.dao.pub.ILoanInitialInfoDao;
import com.zdmoney.credit.loan.domain.LoanBase;
import com.zdmoney.credit.loan.domain.LoanInitialInfo;
import com.zdmoney.credit.loan.domain.LoanProcessHistory;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.IAuditingService;
import com.zdmoney.credit.loan.service.pub.ILoanProcessHistoryService;
import com.zdmoney.credit.offer.service.pub.IOfferRepayInfoService;
import com.zdmoney.credit.system.domain.ComEmployee;

/**
 * 处理财务放款入库信息
 * @author 00234770
 * @date 2015年10月20日 上午9:39:59 
 *
 */
@Service
public class FinanceCoreAfterLoanServiceImpl implements IFinanceCoreAfterLoanService {

	@Autowired
	private ILoanProcessHistoryService loanProcessHistoryService;//债权日志操作Service
	@Autowired
	private IAuditingService auditingService;
	@Autowired
	private ILoanInitialInfoDao loanInitialInfoDao;
	@Autowired
	private IOfferRepayInfoService offerRepayInfoService;
	@Autowired
	private ILoanBaseDao loanBaseDao;
	
	/**
	 * 财务放款信息入库
	 * @param employee
	 * @param loan
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void financingGrantLoan(ComEmployee employee, VLoanInfo loan) {
		//记录日志信息
    	LoanProcessHistory loanProcessHistory = new LoanProcessHistory();
    	loanProcessHistory.setContent("放款");
		loanProcessHistory.setLoanId(loan.getId());
		loanProcessHistory.setLoanFlowState(loan.getLoanFlowState());
		loanProcessHistory.setCreator(employee.getId().toString());
		loanProcessHistory.setLoanState(loan.getLoanState());
		loanProcessHistory.setCreateTime(new Date());
		loanProcessHistoryService.insert(loanProcessHistory);
		
        auditingService.setNextFlowState(loan,false);
        if (loan.getLoanFlowState().equals(LoanFlowStateEnum.正常.getValue())){
        	LoanInitialInfo loanInitialInfo = loanInitialInfoDao.findByLoanId(loan.getId());
        	loanInitialInfo.setGrantMoneyDate(new Date());
        	loanInitialInfo.setGrantMoneyOperatorId(employee.getId());
        	boolean isEndOfMonthOpened =  "t".equals(loan.getEndOfMonthOpened()) ? true : false;
        	
        	if(!isEndOfMonthOpened){//开关关闭状态，该日期为放款日期
            	loanInitialInfo.setGrantMoneyDateTtp(new Date());
            }
            loanInitialInfoDao.update(loanInitialInfo);
            
            LoanBase loanBase = loanBaseDao.findByLoanId(loan.getId());
            loanBase.setLoanBelong(loanBase.getFundsSources());
            loanBaseDao.update(loanBase);
        }
        offerRepayInfoService.builderRepayInfoFinancing(loan,employee);
    }
}
