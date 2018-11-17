package com.zdmoney.credit.test.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zdmoney.credit.common.constant.FundsSourcesTypeEnum;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.vo.TppCallBackData20;
import com.zdmoney.credit.common.vo.core.FinanceVo;
import com.zdmoney.credit.core.service.pub.IFinanceCoreService;
import com.zdmoney.credit.fee.offer.LoanFeeUtil;
import com.zdmoney.credit.fee.service.pub.ILoanFeeInfoService;
import com.zdmoney.credit.fee.service.pub.ILoanFeeOfferService;
import com.zdmoney.credit.fee.service.pub.ILoanFeeTransactionService;

@RunWith(SpringJUnit4ClassRunner.class)
// 使用junit4进行测试
@ContextConfiguration({ "/spring/*.xml" })
// 加载配置文件
// @Transactional
public class LoanFeeTest {

	@Autowired
	LoanFeeUtil loanFeePack;

	@Autowired
	ILoanFeeInfoService loanFeeInfoServiceImpl;

	@Autowired
	ILoanFeeTransactionService loanFeeTransactionServiceImpl;

	@Autowired
	ILoanFeeOfferService loanFeeOfferServiceImpl;
	
	@Autowired
	IFinanceCoreService financeCoreService;
	
	@Test
	public void a () {
		FinanceVo params = new FinanceVo();
		params.setIds("14974774");
		params.setUserCode("admin");
		financeCoreService.grantLoan(params);
	}

//	 @Test
	// @Transactional(propagation = Propagation.REQUIRED)
	public void createFeeTest() {
		loanFeePack.createFee(53138868L, FundsSourcesTypeEnum.龙信小贷.getValue());
	}

//	 @Test
	// @Transactional(propagation = Propagation.REQUIRED)
	public void callBackTransactionTest() {
		TppCallBackData20 tppCallBackData20 = new TppCallBackData20();
		tppCallBackData20.setMsgInId("123");
		tppCallBackData20.setOrderNo("LX_FEE_2016071600000032");
		tppCallBackData20.setReturnCode("000000");
		tppCallBackData20.setReturnInfo("扣款成功");
		tppCallBackData20.setSuccessAmount("0.55");
		boolean s = loanFeeTransactionServiceImpl.callBackTransaction(tppCallBackData20);
		System.out.println("!!!!!!!!!!!!!!!!!!!!!" + s);
	}

//	@Test
	public void getOfferCountTest() {
		int a = loanFeeOfferServiceImpl.getOfferCount(123L, "实时划扣", Dates.getCurrDate());
		System.out.println("getOfferCount:" + a);
	}

//	 @Test
	public void isOfferSenddingTest() {
		boolean s = loanFeeTransactionServiceImpl.isOfferSendding(501557L);
		System.out.println("isOfferSendding:" + s);
	}

}
