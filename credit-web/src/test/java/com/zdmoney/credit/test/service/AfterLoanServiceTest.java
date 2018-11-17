package com.zdmoney.credit.test.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.loan.domain.LoanRepaymentDetail;
import com.zdmoney.credit.loan.service.pub.IAfterLoanService;
import com.zdmoney.credit.offer.service.pub.IOfferRepayInfoService;
import com.zdmoney.credit.system.service.pub.ISequencesService;

@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试  
@ContextConfiguration   
({"/spring/*.xml"}) //加载配置文件  
@Transactional
public class AfterLoanServiceTest {
    
    @Autowired 
    private IAfterLoanService afterLoanService;
    
    @Autowired
    private IOfferRepayInfoService offerRepayInfoService;
    
    @Autowired
    private ISequencesService sequencesService;
    
    @Test
    @Transactional(propagation=Propagation.REQUIRED)
    public void test1 () {
        for(int i=0;i<3;i++){
            System.out.println(sequencesService.getSequences(SequencesEnum.OFFER_FLOW));
        }
    }
    
    @Test
    public void testGetFine(){
        Long loanId = 150106785L;
        Date currDate = new Date();
        List<LoanRepaymentDetail> detailList =  afterLoanService.getAllInterestOrLoan(currDate, loanId);
        BigDecimal fine = afterLoanService.getFine(detailList, currDate);
        System.out.println(fine);
    }
    
    /*@Test
    public void testRepayDeal () {
        
        //回盘记账测试======================================
        Long loanid = 44527558l;
        OfferRepayInfo repayInfo = new OfferRepayInfo();
        repayInfo.setId(sequencesServiceImpl.getSequences(SequencesEnum.OFFER_REPAY_INFO));
        repayInfo.setLoanId(loanid);
        repayInfo.setTradeDate(new Date());
        boolean isOneTime = afterLoanService.isOneTimeRepayment(loanid);
        repayInfo.setTradeCode(isOneTime ? Const.TRADE_CODE_ONEOFF : Const.TRADE_CODE_NORMAL);
        repayInfo.setAmount(new BigDecimal(1547.33));//1.0接口不会有部分成功，直接拿报盘金额
        repayInfo.setTradeType("通联代扣");//这个方法还没实现
        repayInfo.setTradeNo(afterLoanService.getTradeFlowNo(loanid));//这个方法从新放位置
        repayInfo.setTradeKind(TradeKindEnum.正常交易.getValue());
        repayInfo.setTeller(TPPHelper.TPP_HANDLER_TELLER);
        repayInfo.setOrgan(TPPHelper.TPP_HANDLER_ORGANIZATION_CODE);
        repayInfo.setOfferid(loanid);
        repayInfo.setCreateTime(new Date());
        offerRepayInfoService.save(repayInfo);
        afterLoanService.repayDeal(repayInfo);
    }*/
}
