package com.zdmoney.credit.test.service;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zdmoney.credit.loan.domain.LoanLedger;
import com.zdmoney.credit.loan.service.pub.ILoanLedgerService;

@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试  
@ContextConfiguration   
({"/spring/*.xml"}) //加载配置文件  

@Transactional
public class LoanLedgerServiceTest {
	@Autowired 
	ILoanLedgerService loanLedgerService;
	
	
	@Test
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void testFindByAccountForUpdate () {
		/*LoanLedger loanLedger = loanLedgerService.findByAccountForUpdate("ZD0000001090000005");
		loanLedger.setAmount(loanLedger.getAmount().add(new BigDecimal("1")));
		loanLedgerService.update(loanLedger);
		System.out.println(loanLedger);*/
		
		LoanLedger loanLedger = loanLedgerService.findByLoanIdForUpdate(9999495l);
		loanLedger.setAmount(loanLedger.getAmount().add(new BigDecimal("1")));
		loanLedgerService.update(loanLedger);
		System.out.println(loanLedger);
	}
}
