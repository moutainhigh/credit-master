package com.zdmoney.credit.test.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zdmoney.credit.common.constant.RepaymentStateEnum;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.core.service.pub.ILoanCoreService;
import com.zdmoney.credit.loan.dao.pub.ILoanInitialInfoDao;
import com.zdmoney.credit.loan.dao.pub.ILoanProductDao;
import com.zdmoney.credit.loan.dao.pub.ILoanRepaymentDetailDao;
import com.zdmoney.credit.loan.dao.pub.ILoanRepaymentDetailLufaxDao;
import com.zdmoney.credit.loan.domain.LoanRepaymentDetail;
import com.zdmoney.credit.loan.domain.LoanRepaymentDetailLufax;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.IAfterLoanService;
import com.zdmoney.credit.loan.service.pub.ILoanBaseService;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/spring/*.xml" })
public class LoanCoreServiceTest {

	@Autowired
	ILoanCoreService loanCoreService;
	
	@Autowired
	ILoanBaseService loanBaseService;
	
	@Autowired
	ILoanInitialInfoDao loanInitialInfoDao;
	
	@Autowired
	ILoanProductDao LoanProductDao;
	@Autowired
	ILoanRepaymentDetailLufaxDao loanRepaymentDetailLufaxDao;
	@Autowired
	IVLoanInfoService vLoanInfoService;
	@Autowired
	private IAfterLoanService afterLoanService;
	@Autowired
	private ILoanRepaymentDetailDao loanRepaymentDetailDao;
	public static void main(String[] args) {
		 int day = DateTime.now().minusDays(1).getDayOfMonth();
		 System.out.println(day);
		 System.out.println(Dates.getDateTime(Dates.getBeforeDays(1), Dates.DATAFORMAT_YYYYMMDDHHMMSS));
		 System.out.println(RepaymentStateEnum.未还款);
	}
	
	@Test
	public void aaa(){
		long loanId = 150053544;
		VLoanInfo vLoanInfo = vLoanInfoService.findByLoanId(loanId);
		Date currReturnDate = afterLoanService.getCurrTermReturnDate(Dates.getBeforeDays(1), vLoanInfo.getPromiseReturnDate());
		System.out.println(Dates.getDateTime(currReturnDate, Dates.DATAFORMAT_YYYYMMDDHHMMSS));
		Map<String, Object> params = new HashMap<>();
		params.put("loanId", vLoanInfo.getId());
		params.put("repaymentStates", new String[]{RepaymentStateEnum.未还款.getValueName(),RepaymentStateEnum.不足额还款.getValueName()});
		params.put("CurrTermReturnDate",currReturnDate);
		List<LoanRepaymentDetail> list = loanRepaymentDetailDao.findListByMap(params);
		
	}
	
	@Test
	public void ccc(){
		long loanId = 150000808;
		Map<String, Object> repayPlanparams = new HashMap<String, Object>();
        repayPlanparams.put("loanId",loanId);
        repayPlanparams.put("orderByCurrentTerm","true");
        List<LoanRepaymentDetailLufax>  loanRepaymentDetailLufaxList = loanRepaymentDetailLufaxDao.findListByMap(repayPlanparams);
    	int lastTerm = loanRepaymentDetailLufaxList.size()-1;
    	System.out.println(lastTerm);
    	LoanRepaymentDetailLufax last = loanRepaymentDetailLufaxList.get(lastTerm);
    	System.out.println("============"+last.getId()+"======"+last.getCurrentTerm()+"---"+last.getCurrentAccrual());
    	String due_date = Dates.getDateTime(last.getReturnDate(),Dates.DATAFORMAT_YYYYMMDDHHMMSS);
    	System.out.println("========="+due_date);
	}


}
