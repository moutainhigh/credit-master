package com.zdmoney.credit.test.service;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.common.constant.TradeKindEnum;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.util.TPPHelper;
import com.zdmoney.credit.loan.service.pub.IAfterLoanService;
import com.zdmoney.credit.offer.domain.OfferRepayInfo;
import com.zdmoney.credit.offer.domain.OfferTransaction;
import com.zdmoney.credit.offer.service.pub.IOfferRepayInfoService;
import com.zdmoney.credit.system.service.pub.ISequencesService;

@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试  
@ContextConfiguration   
({"/spring/*.xml"}) //加载配置文件  
public class AfterLoanServiceTest {
	
	@Autowired 
	IAfterLoanService afterLoanService;
	@Autowired
	private IOfferRepayInfoService offerRepayInfoService;
	
	@Autowired
	ISequencesService sequencesServiceImpl;
	
	
	
	@Test
	public void testRepayDeal () {
		
		//回盘记账测试======================================
		Long loanid = 150001152L;
		OfferRepayInfo repayInfo = new OfferRepayInfo();
		repayInfo.setId(sequencesServiceImpl.getSequences(SequencesEnum.OFFER_REPAY_INFO));
		repayInfo.setLoanId(loanid);

		repayInfo.setTradeDate(new Date());
		boolean isOneTime = afterLoanService.isOneTimeRepayment(loanid);
		repayInfo.setTradeCode(isOneTime ? Const.TRADE_CODE_ONEOFF : Const.TRADE_CODE_NORMAL);
		repayInfo.setAmount(new BigDecimal(15471.33));//1.0接口不会有部分成功，直接拿报盘金额
		repayInfo.setTradeType("挂账");//这个方法还没实现
		repayInfo.setTradeKind(TradeKindEnum.正常交易.getValue());
		repayInfo.setTradeNo(afterLoanService.getTradeFlowNo(loanid));//这个方法从新放位置
		repayInfo.setTeller(TPPHelper.TPP_HANDLER_TELLER);
		repayInfo.setOrgan(TPPHelper.TPP_HANDLER_ORGANIZATION_CODE);
		repayInfo.setOfferid(loanid);
		repayInfo.setCreateTime(new Date());
		repayInfo.setTradeCode("3001");
		offerRepayInfoService.save(repayInfo);
		afterLoanService.repayDeal(repayInfo);
		
		
	}
}
