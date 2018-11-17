package com.zdmoney.credit.test.service;


import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zendaimoney.thirdpp.common.vo.AttachmentResponse;
import com.zendaimoney.thirdpp.common.vo.Response;
import com.zendaimoney.thirdpp.trade.pub.service.IAuthService;
import com.zendaimoney.thirdpp.trade.pub.service.IBaseService;
import com.zendaimoney.thirdpp.trade.pub.vo.req.query.BankAreaCityQueryReqVo;
import com.zendaimoney.thirdpp.trade.pub.vo.req.query.BankCardAuthReqVo;
import com.zendaimoney.thirdpp.trade.pub.vo.req.query.BankCardBinQueryReqVo;
import com.zendaimoney.thirdpp.trade.pub.vo.req.query.BankOrganizationQueryReqVo;
import com.zendaimoney.thirdpp.trade.pub.vo.req.query.BaseQueryRequestVo;
import com.zendaimoney.thirdpp.trade.pub.vo.req.query.ThirdPartyPayPlatformSupportBankInfoReqVo;
import com.zendaimoney.thirdpp.trade.pub.vo.req.query.ThirdPartyPayPlatformSupportBankReqVo;
import com.zendaimoney.thirdpp.trade.pub.vo.resp.query.BankAreaResponseVo;
import com.zendaimoney.thirdpp.trade.pub.vo.resp.query.BankOrgResponseVo;
import com.zendaimoney.thirdpp.trade.pub.vo.resp.query.BankResponseVo;
import com.zendaimoney.thirdpp.trade.pub.vo.resp.query.CardBinResponseVo;
import com.zendaimoney.thirdpp.trade.pub.vo.resp.query.ThirdPartyPayPlatformResponseVo;
import com.zendaimoney.thirdpp.trade.pub.vo.resp.query.ThirdPartyPayPlatformSupportBankInfoRespVo;
import com.zendaimoney.thirdpp.trade.pub.vo.resp.query.ThirdPartyPayPlatformSupportBankRespVo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-dubbo.xml" })
public class BaseTradeServiceTest {

	@Autowired
    private IBaseService baseService;

	@Autowired
    private IAuthService authService;


	@Test
	public void getSupportBanks(){
		BaseQueryRequestVo vo = new BaseQueryRequestVo();
		vo.setBizSysNo("004");
		AttachmentResponse<List<BankResponseVo>> bankList = baseService.getSupportedBanks(vo);
		List<BankResponseVo> bankResponseVoList = bankList.getAttachment();
		for (BankResponseVo brVo : bankResponseVoList) {
			System.out.println(brVo.getBankName() + ":" + brVo.getBankCode());
		}
		Assert.assertNotNull(bankResponseVoList);
	}
	
	@Test
	public void getBankAreaProvinces(){
		BaseQueryRequestVo vo = new BaseQueryRequestVo();
		vo.setBizSysNo("004");
		AttachmentResponse<List<BankAreaResponseVo>> bankList = baseService.getBankAreaProvinces(vo);
		List<BankAreaResponseVo> bankAreaResponseVoList = bankList.getAttachment();
		for (BankAreaResponseVo brVo : bankAreaResponseVoList) {
			System.out.println(brVo.getAreaName() + ":" + brVo.getAreaCode());
		}
		Assert.assertNotNull(bankAreaResponseVoList);
	}
	
	@Test
	public void getBankAreaCitysWhenProvinceAreaCodeBlank(){
		BankAreaCityQueryReqVo vo = new BankAreaCityQueryReqVo();
		vo.setBizSysNo("004");
		vo.setBankOrganizationProvinceNo("");
		AttachmentResponse<List<BankAreaResponseVo>> bankList = baseService.getBankAreaCitysByProvince(vo);
		if (bankList.getAttachment() != null) {
			List<BankAreaResponseVo> bankAreaResponseVoList = bankList.getAttachment();
			for (BankAreaResponseVo brVo : bankAreaResponseVoList) {
				System.out.println(brVo.getAreaName() + ":" + brVo.getAreaCode());
			}
		} else {
			System.out.println(bankList.getCode() + ":" + bankList.getMsg());
		}
		
		Assert.assertEquals("000001", bankList.getCode());
	}
	
	@Test
	public void getBankAreaCitysWhenProvinceAreaCodeNotExist(){
		BankAreaCityQueryReqVo vo = new BankAreaCityQueryReqVo();
		vo.setBizSysNo("004");
		vo.setBankOrganizationProvinceNo("000");
		AttachmentResponse<List<BankAreaResponseVo>> bankList = baseService.getBankAreaCitysByProvince(vo);
		if (bankList.getAttachment() != null) {
			List<BankAreaResponseVo> bankAreaResponseVoList = bankList.getAttachment();
			for (BankAreaResponseVo brVo : bankAreaResponseVoList) {
				System.out.println(brVo.getAreaName() + ":" + brVo.getAreaCode());
			}
		} else {
			System.out.println(bankList.getCode() + ":" + bankList.getMsg());
		}
		Assert.assertNull(bankList.getAttachment());
	}
	
	@Test
	public void getBankAreaCitysHappyPass(){
		BankAreaCityQueryReqVo vo = new BankAreaCityQueryReqVo();
		vo.setBizSysNo("004");
		vo.setBankOrganizationProvinceNo("009");
		AttachmentResponse<List<BankAreaResponseVo>> bankList = baseService.getBankAreaCitysByProvince(vo);
		if (bankList.getAttachment() != null) {
			List<BankAreaResponseVo> bankAreaResponseVoList = bankList.getAttachment();
			for (BankAreaResponseVo brVo : bankAreaResponseVoList) {
				System.out.println(brVo.getAreaName() + ":" + brVo.getAreaCode());
			}
		} else {
			System.out.println(bankList.getCode() + ":" + bankList.getMsg());
		}
		Assert.assertNotNull(bankList.getAttachment());
	}
	
	@Test
	public void getBankOrganizationsByBankCodeAndBankAreaNeedNeccessaryFields(){
		BankOrganizationQueryReqVo vo = new BankOrganizationQueryReqVo();
		vo.setBizSysNo("004");
		vo.setBankOrganizationProvinceNo("009");
		AttachmentResponse<List<BankOrgResponseVo>> bankList = baseService.getBankOrganizationsByBankCodeAndBankArea(vo);
		if (bankList.getAttachment() != null) {
			List<BankOrgResponseVo> bankOrgResponseVoList = bankList.getAttachment();
			for (BankOrgResponseVo brVo : bankOrgResponseVoList) {
				System.out.println(brVo.getBankOrgName()+ ":" + brVo.getBankOrgName());
			}
		} else {
			System.out.println(bankList.getCode() + ":" + bankList.getMsg());
		}
		Assert.assertNull(bankList.getAttachment());
	}
	
	@Test
	public void getBankOrganizationsByBankCodeAndBankAreaHappyPass(){
		BankOrganizationQueryReqVo vo = new BankOrganizationQueryReqVo();
		vo.setBizSysNo("004");
		vo.setBankOrganizationProvinceNo("001");
		vo.setBankOrganizationCityNo("001");
		vo.setBankCode("001");
		AttachmentResponse<List<BankOrgResponseVo>> bankList = baseService.getBankOrganizationsByBankCodeAndBankArea(vo);
		if (bankList.getAttachment() != null) {
			List<BankOrgResponseVo> bankOrgResponseVoList = bankList.getAttachment();
			for (BankOrgResponseVo brVo : bankOrgResponseVoList) {
				System.out.println(brVo.getBankOrgName()+ ":" + brVo.getBankLineNo());
			}
		} else {
			System.out.println(bankList.getCode() + ":" + bankList.getMsg());
		}
		Assert.assertNotNull(bankList.getAttachment());
	}
	
	@Test
	public void getBankAreaCitysByProvince(){
		BankAreaCityQueryReqVo vo = new BankAreaCityQueryReqVo();
		vo.setBizSysNo("004");
		vo.setBankOrganizationProvinceNo("001");
		AttachmentResponse<List<BankAreaResponseVo>> bankList = baseService.getBankAreaCitysByProvince(vo);
		if (bankList.getAttachment() != null) {
			List<BankAreaResponseVo> bankOrgResponseVoList = bankList.getAttachment();
			for (BankAreaResponseVo brVo : bankOrgResponseVoList) {
				System.out.println(brVo.getAreaCode());
			}
		} else {
			System.out.println(bankList.getCode() + ":" + bankList.getMsg());
		}
		Assert.assertNotNull(bankList.getAttachment());
	}
	
	@Test
	public void getAccountVerification(){
		BankCardAuthReqVo vo = new BankCardAuthReqVo();
		vo.setBizSysNo("004");
		vo.setBankCardNo("6226620607792207");
		vo.setBankCardType("1");
		vo.setIdNo("000002198903302089");
		vo.setIdType("1");
		vo.setMobile("18317120735");
		vo.setRealName("刘敏");
		vo.setInfoCategoryCode("011");
		Response response = authService.bankCardAuth(vo);
		System.out.println("code = " + response.getCode());
		System.out.println("msg = " + response.getMsg());
		System.out.println("flowId = " + response.getFlowId());
	}
	
	@Test
	public void getCardBin(){
		BankCardBinQueryReqVo vo = new BankCardBinQueryReqVo();
		vo.setBizSysNo("004");
		vo.setBankCardNo("0006625607792207");
		vo.setInfoCategoryCode("011");
		AttachmentResponse<CardBinResponseVo> response = authService.bankCardBinQuery(vo);
		System.out.println("code = " + response.getCode());
		System.out.println("msg = " + response.getMsg());
		System.out.println("flowId = " + response.getFlowId());
		CardBinResponseVo responseVo = response.getAttachment();
		if (responseVo != null) {
			System.out.println("BankCardBin = " + responseVo.getBankCardBin());
			System.out.println("BankCardNo = " + responseVo.getBankCardNo());
			System.out.println("BankCardLen = " + responseVo.getBankCardLen());
		}
	}
	
	@Test
	public void getSupportedPayPlatforms(){
		BaseQueryRequestVo vo = new BaseQueryRequestVo();
		vo.setBizSysNo("004");
		AttachmentResponse<List<ThirdPartyPayPlatformResponseVo>> response = baseService.getSupportThirdPartyPayPlatforms(vo);
		Assert.assertEquals(Response.SUCCESS_RESPONSE_CODE, response.getCode());
		for (ThirdPartyPayPlatformResponseVo temp : response.getAttachment()) {
			System.out.println(temp.getThirdPartyPayPlatformCode() + ":" + temp.getThirdPartyPayPlatformName());
		}
	}
	
	@Test
	public void queryThirdPartyPayPlatformSupportBanks(){
		ThirdPartyPayPlatformSupportBankReqVo vo = new ThirdPartyPayPlatformSupportBankReqVo();
		vo.setBizSysNo("004");
		vo.setThirdPartyPayPlatformCode("0");
		AttachmentResponse<List<ThirdPartyPayPlatformSupportBankRespVo>> response = baseService.getSupportBanksByThirdPartyPayPlatform(vo);
		Assert.assertEquals(Response.SUCCESS_RESPONSE_CODE, response.getCode());
		Assert.assertNotNull(response.getAttachment());
		for (ThirdPartyPayPlatformSupportBankRespVo temp : response.getAttachment()) {
			System.out.println(temp.getBankCode() + ":" + temp.getBankName());
		}
	}
	
	@Test
	public void getSupportBankInfoByThirdPartyPayPlatformCodeAndBankCodeHappyPass(){
		ThirdPartyPayPlatformSupportBankInfoReqVo vo = new ThirdPartyPayPlatformSupportBankInfoReqVo();
		vo.setBizSysNo("004");
		vo.setThirdPartyPayPlatformCode("0");
		vo.setBankCode("00080015");
		AttachmentResponse<ThirdPartyPayPlatformSupportBankInfoRespVo> response = baseService.getSupportBankInfoByThirdPartyPayPlatformCodeAndBankCode(vo);
		Assert.assertEquals(Response.SUCCESS_RESPONSE_CODE, response.getCode());
		Assert.assertNotNull(response.getAttachment());
		ThirdPartyPayPlatformSupportBankInfoRespVo attach = response.getAttachment();
		System.out.println(attach.getStatus() + ":" +attach.getCollectMaxMoney() + ":" + attach.getPayMaxMoney() + ":" + attach.getQuickPayMaxMoney());
	}
	
	@Test
	public void getSupportBankInfoByThirdPartyPayPlatformCodeAndBankCodeErrorCaseOne(){
		// 测试数据库无记录
		ThirdPartyPayPlatformSupportBankInfoReqVo vo = new ThirdPartyPayPlatformSupportBankInfoReqVo();
		vo.setBizSysNo("004");
		vo.setThirdPartyPayPlatformCode("0");
		vo.setBankCode("00080099");
		AttachmentResponse<ThirdPartyPayPlatformSupportBankInfoRespVo> response = baseService.getSupportBankInfoByThirdPartyPayPlatformCodeAndBankCode(vo);
		Assert.assertEquals(Response.SUCCESS_RESPONSE_CODE, response.getCode());
		Assert.assertNull(response.getAttachment());
	}

}
