package com.zdmoney.credit.test.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.debit.domain.DebitTransaction;
import com.zdmoney.credit.debit.service.pub.IDebitTransactionService;
import com.zdmoney.credit.debit.service.pub.IRechargeSearchService;
import com.zdmoney.credit.framework.vo.lufax.entity.LoanDetailLufax;
import com.zdmoney.credit.ljs.service.pub.ILoanStatusLufaxService;
import com.zdmoney.credit.offer.service.pub.IOfferRepayInfoService;
import com.zdmoney.credit.offer.vo.RepaymentInputVo;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;

@RunWith(SpringJUnit4ClassRunner.class)
//使用junit4进行测试
@ContextConfiguration({ "/spring/*.xml" })
//加载配置文件
//@Transactional
public class RechargeSearchServiceTest {
    
    @Autowired
    private IRechargeSearchService rechargeSearchService;
    
    @Autowired
    private ISysParamDefineService sysParamDefineService;
    
    @Autowired
    private IOfferRepayInfoService offerRepayInfoService;
    
    @Autowired
    private IDebitTransactionService debitTransactionService;
    
    @Autowired
    private ILoanStatusLufaxService loanStatusLufaxService;
    
    @Test
    public void testSearchOnlineDebitResult(){
        rechargeSearchService.searchOnlineDebitResult();
        System.out.println("测试结束！");
    }
    
    @Test
    public void testCardChn(){
        // 扣款渠道（CL0001中金支付、 CL0002广银联）
        String cardChn = sysParamDefineService.getSysParamValue("wm3", "cardChn");
        System.out.println("扣款渠道是：" + cardChn);
    }
    
    @Test
    public void testCreateRepaymentInputVo(){
        String serialNo = "81";
        DebitTransaction transaction = debitTransactionService.findDebitTransactionBySerialNo(serialNo);
        RepaymentInputVo vo = offerRepayInfoService.createRepaymentInputVo(transaction);
        System.out.println("返回参数：" + vo);
    }
    
    @Test
    public void testFindLoanDetailLufaxList2Transmit(){
        Map<String, Object> params = new HashMap<>();
        params.put("currDate", Dates.getDateTime());
        List<LoanDetailLufax> resultList = loanStatusLufaxService.findLoanDetailLufaxList2Transmit(params);
        System.out.println("返回查询件数：" + resultList.size());
    }
}
