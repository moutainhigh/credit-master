package com.zdmoney.credit.test.service;

import java.math.BigDecimal;
import java.util.Calendar;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.debit.service.pub.IDebitQueueLogService;
import com.zdmoney.credit.debit.service.pub.ISplitQueueLogService;
import com.zdmoney.credit.loan.domain.LoanLedger;
import com.zdmoney.credit.loan.service.pub.ILoanLedgerService;
import com.zdmoney.credit.offer.service.pub.IOfferRepayInfoService;

@RunWith(SpringJUnit4ClassRunner.class)
//使用junit4进行测试
@ContextConfiguration({ "/spring/*.xml" })
//加载配置文件
//@Transactional
public class SplitQueueLogServiceTest {
    
    @Autowired
    private ISplitQueueLogService splitQueueLogService;
    
    @Autowired
    private IOfferRepayInfoService offerRepayInfoService;
    
    @Autowired 
    private ILoanLedgerService loanLedgerService;
    
    @Autowired
    private IDebitQueueLogService debitQueueLogService;
    
    @Test
    public void testIsCanRepayment(){
        Long loanId = 150000109L;
        boolean result = splitQueueLogService.isCanRepayment(loanId);
        System.out.println(result);
    }
    
    @Test
    public void testCreateAccountingByRepaymentDayDeal(){
        Long loanId = 150000547L;
        Calendar  tradeDate = Calendar.getInstance();
        tradeDate.set(Calendar.MONTH, tradeDate.get(Calendar.MONTH) + 1);
        tradeDate.set(Calendar.DAY_OF_MONTH, 16);
        String repaymentDate = Dates.getDateTime(tradeDate.getTime(), "yyyy-MM-dd");
        System.out.print(repaymentDate);
        LoanLedger loanLedger = loanLedgerService.findByLoanId(loanId);
        offerRepayInfoService.createAccountingByRepaymentDayDeal(loanLedger, tradeDate);
    }
    
    @Test
    public void testExecuteEntrustDebit(){
        debitQueueLogService.executeEntrustDebit(null);
    }
    
    @Test
    public void testExecuteOverdueRepaymentInfo(){
        splitQueueLogService.executeOverdueRepaymentInfo(null);
    }
    
    @Test
    public void testPerpareOverdueCompensatory(){
        debitQueueLogService.perpareOverdueCompensatory();
        System.out.println("执行完毕！");
    }
    
    @Test
    public void testQueryCompensatoryPenaltyAmount(){
        Long loanId = 150106785L;
        Long term = 7L;
        BigDecimal penaltyAmount = splitQueueLogService.queryCompensatoryPenaltyAmount(loanId, term);
        System.out.println("垫付的罚息金额是："+ penaltyAmount);
    }
    
    @Test
    public void testCreateSplitQueueLog(){
        Long loanId = 150106785L;
        String debitNo = "20171117002000302228261";
        String lufaxNo = "2017080418352100340046";
        String batchId = "B20171117160000156849384";
        BigDecimal frozenAmount = /*new BigDecimal(2311.3)*/ null;
        splitQueueLogService.createSplitQueueLog(loanId, debitNo, lufaxNo, batchId, frozenAmount);
    }
    
    @Test
    public void testExecuteEntrustRepaymentInfo(){
        splitQueueLogService.executeEntrustRepaymentInfo(null);
        System.out.println("执行完毕！");
    }
    
    @Test
    public void testExecuteEntrustDebit_2(){
        debitQueueLogService.executeEntrustDebit(null);
        System.out.println("执行完毕！");
    }
}
