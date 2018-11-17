package com.zdmoney.credit.test.service;

import java.math.BigDecimal;
import java.util.Calendar;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.debit.dao.pub.IDebitBaseInfoDao;
import com.zdmoney.credit.debit.dao.pub.IDebitDetailInfoDao;
import com.zdmoney.credit.debit.dao.pub.IDebitTransactionDao;
import com.zdmoney.credit.debit.domain.DebitBaseInfo;
import com.zdmoney.credit.debit.domain.DebitDetailInfo;
import com.zdmoney.credit.debit.domain.DebitTransaction;
import com.zdmoney.credit.debit.service.pub.IDebitBaseInfoService;
import com.zdmoney.credit.framework.vo.common.BaseParamVo;
import com.zdmoney.credit.loan.domain.LoanLedger;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import com.zdmoney.credit.offer.service.pub.IOfferRepayInfoService;
import com.zdmoney.credit.system.service.pub.ISequencesService;

//使用junit4进行测试
@RunWith(SpringJUnit4ClassRunner.class)
//加载配置文件
@ContextConfiguration({ "/spring/*.xml" })
@Transactional
public class OfferRepayInfoServiceTest {

    @Autowired
    private IOfferRepayInfoService offerRepayInfoService;
    
    @Autowired
    private IDebitBaseInfoService debitBaseInfoService;
    
    @Autowired
    private IVLoanInfoService vLoanInfoService;
    
    @Autowired
    private ISequencesService sequencesService;
    
    @Autowired
    private IDebitBaseInfoDao debitBaseInfoDao; 
    
    @Autowired
    private IDebitTransactionDao debitTransactionDao;
    
    @Autowired
    private IDebitDetailInfoDao debitDetailInfoDao;

    @Test
    //@Rollback(value = false)
    public void testCreateAccountingByRepaymentDayDeal() {
        LoanLedger loanLedger = new LoanLedger();
        loanLedger.setLoanId(150000263l);
        loanLedger.setAmount(new BigDecimal(21164.11));
        Calendar tradeDate = Calendar.getInstance();
        offerRepayInfoService.createAccountingByRepaymentDayDeal(loanLedger, tradeDate);
    }
    
    @Test
    @Rollback(value = false)
    public void testCreateThirdOffer() {
        Long loanId = 150000263L;
        VLoanInfo loan = vLoanInfoService.findByLoanId(loanId);
        debitBaseInfoService.createThirdOffer(loan, new BigDecimal(2164.11));
    }
    
    @Test
    @Rollback(value = false)
    public void testCreateCallbackThirdOffer() {
        Long loanId = 150000263L;
        VLoanInfo loan = vLoanInfoService.findByLoanId(loanId);
        debitBaseInfoService.createCallbackThirdOffer(loan, new BigDecimal(2164.11), true, "扣款成功");
    }
    
    @Test
    @Rollback(value = false)
    public void testDebitBaseInfoUpdate() {
        Long id = 146L;
        DebitBaseInfo debitBaseInfo = debitBaseInfoDao.get(id);
        debitBaseInfo.setState("未报盘");
        debitBaseInfoDao.update(debitBaseInfo);
    }
    
    @Test
    @Rollback(value = false)
    public void testDebitTransactionUpdate() {
        Long id = 73L;
        DebitTransaction debitTransaction = debitTransactionDao.get(id);
        debitTransaction.setState("扣款失败");
        debitTransactionDao.update(debitTransaction);
    }
    
    @Test
    @Rollback(value = false)
    public void testDebitDetailInfoInsert(){
        Long loanId = 150000263L;
        VLoanInfo loan = vLoanInfoService.findByLoanId(loanId);
        //第三方划扣明细信息表
        DebitDetailInfo debitDetailInfo = new DebitDetailInfo();
        debitDetailInfo.setId(sequencesService.getSequences(SequencesEnum.DEBIT_DETAIL_INFO));
        debitDetailInfo.setDebitId(146L);
        debitDetailInfo.setAccountNo(BaseParamVo.SYS_SOURCE_WM3+"_"+loan.getContractNum());
        debitDetailInfo.setBrNo(BaseParamVo.SYS_SOURCE_WM3);
        debitDetailInfoDao.insert(debitDetailInfo);
        
        debitDetailInfo.setCity("changsha");
        debitDetailInfoDao.update(debitDetailInfo);
    }
    
    @Test
    @Rollback(value = false)
    public void testDebitDetailInfoUpdate(){
        Long id = 83L;
        DebitDetailInfo debitDetailInfo = debitDetailInfoDao.get(id);
        /*DebitDetailInfo debitDetailInfo = new DebitDetailInfo();
        debitDetailInfo.setId(id);*/
        debitDetailInfo.setCity("changsha");
        debitDetailInfoDao.update(debitDetailInfo);
    }
}
