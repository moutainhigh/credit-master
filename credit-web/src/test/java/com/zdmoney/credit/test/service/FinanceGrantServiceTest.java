package com.zdmoney.credit.test.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zdmoney.credit.payment.service.pub.IFinanceGrantService;

@RunWith(SpringJUnit4ClassRunner.class)
//使用junit4进行测试
@ContextConfiguration({ "/spring/*.xml" })
//加载配置文件
//@Transactional
public class FinanceGrantServiceTest {
    
    @Autowired
    private IFinanceGrantService financeGrantService;
    
    @Test
    public void testTppGrantResultQuery(){
        financeGrantService.tppGrantResultQuery();
        System.out.println("执行完毕");
    }
    
    @Test
    public void testBatchRequestFinanceGrantApply(){
        String[] loanIds = {"150000608","150000609","150000610"};
        financeGrantService.batchRequestFinanceGrantApply(loanIds);
    }
}
