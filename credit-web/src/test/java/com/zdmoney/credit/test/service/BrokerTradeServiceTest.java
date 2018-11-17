package com.zdmoney.credit.test.service;


import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zendaimoney.thirdpp.common.enums.BizSys;
import com.zendaimoney.thirdpp.common.enums.ThirdType;
import com.zendaimoney.thirdpp.common.vo.Response;
import com.zendaimoney.thirdpp.trade.pub.service.IBrokerTradeService;
import com.zendaimoney.thirdpp.trade.pub.vo.req.biz.CollectReqVo;
import com.zendaimoney.thirdpp.trade.pub.vo.req.biz.RequestVo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring/*.xml"})
public class BrokerTradeServiceTest {

	@Autowired
    private IBrokerTradeService brokerTradeConsumer;

	@Test
	public void asyncCollect() {
		RequestVo vo = new RequestVo();
		vo.setBizSysNo(BizSys.ZENDAI_2004_SYS.getCode());
		vo.setInfoCategoryCode("10007");
		CollectReqVo collectReqVo = null;
		System.out.println("======start" + new Date());
		for (int i = 1; i <= 10000; i++) {
			collectReqVo = new CollectReqVo();
			collectReqVo.setPaySysNo(ThirdType.ALLINPAY.getCode());
			collectReqVo.setBizSysAccountNo("");
			collectReqVo.setZengdaiAccountNo("10000001");
			collectReqVo.setReceiverAccountNo("10000010"); // 收款方账户编号
			collectReqVo.setReveiverAccountName("证大财富");// 收款方姓名
			collectReqVo.setPayerName("张三");// 付款方姓名
			collectReqVo.setPayerBankCardNo("9558801001114990713");// 付款方银行卡号
			collectReqVo.setPayerBankCardType("1");// 付款方银行卡类型 1.借记卡，2信用卡
			collectReqVo.setPayerIdType("1");// 付款方证件类型
			collectReqVo.setPayerId("421023198609091827");// 付款方证件号
			collectReqVo.setPayerBankCode("10000");// 付款方银行编码
			collectReqVo.setPayerSubBankCode("10023942");// 付款方银行支行行号
			collectReqVo.setCurrency("0");// 币种(0人民币)
			collectReqVo.setAmount(new BigDecimal("1000"));// 金额
			collectReqVo.setBizFlow(String.valueOf(new Date().getTime()) + i+i+i);// 业务流水号
			collectReqVo.setIsNeedSpilt(1); //报盘是否需要拆单(限额超过后)0不需要拆单1需要拆单
			vo.getList().add(collectReqVo);
		}

		
		Response response = brokerTradeConsumer.asynCollect(vo);
		System.out.println("======end" + new Date());
		System.out.println("response-------------:" + response.getCode()
				+ ",msg:" + response.getMsg() + "flowid:" + response.getFlowId());
	}
	
	@Test
	public void syncCollect() {
		/*RequestVo vo = new RequestVo();
		vo.setBizSysNo(BizSys.ZENDAI_2004_SYS.getCode());
		vo.setInfoCategoryCode("10000");
		CollectReqVo collectReqVo = null;
		for (int i = 1; i <= 1; i++) {
			collectReqVo = new CollectReqVo();
			collectReqVo.setPaySysNo(ThirdType.ALLINPAY.getCode());
			collectReqVo.setBizSysAccountNo("");
			collectReqVo.setZengdaiAccountNo("10000001");
			collectReqVo.setReceiverAccountNo("10000010"); // 收款方账户编号
			collectReqVo.setReveiverAccountName("证大财富");// 收款方姓名
			collectReqVo.setPayerName("张三");// 付款方姓名
			collectReqVo.setPayerBankCardNo("9558801001114990713");// 付款方银行卡号
			collectReqVo.setPayerBankCardType("1");// 付款方银行卡类型 1.借记卡，2信用卡
			collectReqVo.setPayerIdType("0");// 付款方证件类型
			collectReqVo.setPayerId("421023198609091827");// 付款方证件号
			collectReqVo.setPayerBankCode("10000");// 付款方银行编码
			collectReqVo.setPayerSubBankCode("10023942");// 付款方银行支行行号
			collectReqVo.setCurrency("0");// 币种(0人民币)
			collectReqVo.setAmount(new BigDecimal("11"));// 金额
			collectReqVo.setBizFlow(String.valueOf(new Date().getTime()) + i);// 业务流水号
			collectReqVo.setIsNeedSpilt(1); //报盘是否需要拆单(限额超过后)0不需要拆单1需要拆单
			vo.getList().add(collectReqVo);
		}

		Response response = brokerTradeConsumer.syncCollect(vo);
		System.out.println("response-------------:" + response.getCode()
				+ ",msg:" + response.getMsg() + "flowid:" + response.getFlowId());*/
	}

}
