package com.zdmoney.credit.test.web;

import java.math.BigDecimal;

import com.alibaba.fastjson.JSONObject;
import com.zdmoney.credit.common.gateway.GatewayUtils;
import com.zdmoney.credit.common.util.HttpUtils;
import com.zdmoney.credit.common.util.coreUtil.JSONUtil;
import com.zdmoney.credit.framework.vo.lufax.input.Lufax100015Vo;
import com.zdmoney.credit.framework.vo.xdcore.input.Lufax800011Vo;
import com.zdmoney.credit.framework.vo.xdcore.input.Lufax800013Vo;

public class LujinsuoTest {

	 public static void main(String[] args) throws Exception{			 
//		ResponseInfo responseInfo = null;
//    	Lufax100014Vo lufax100014Vo=new Lufax100014Vo();
//    	lufax100014Vo.setAmount(new BigDecimal("1000"));//充值金额
//    	lufax100014Vo.setAnshuoBatchId("1111111111111111111111111111111");//批次号
//    	lufax100014Vo.setAppId("");//调用方ID
//    	lufax100014Vo.setApplyAt(Dates.getDateTime("yyyy-MM-dd hh:mm:ss"));//调用发起时间
//    	lufax100014Vo.setCompanyUsename("123213");//对公账户陆金所虚拟户名称
//    	lufax100014Vo.setProductType("1000500010");//业务类型
//    	JSONObject msg = GatewayUtils.callCateWayInterface(lufax100014Vo, "lufax100014");
//    	System.out.println(JSON.toJSONString(msg));
    	
//    	Lufax100015Vo lufax100015Vo=new Lufax100015Vo();
//    	lufax100015Vo.setLufaxUserName("123");
//    	lufax100015Vo.setWithdrawAmount(new BigDecimal("1000"));
//    	lufax100015Vo.setWithdrawReqNo("1111111111111111111111111111111");  	
//    	JSONObject msg = GatewayUtils.callCateWayInterface(lufax100015Vo, "lufax100015");
//    	System.out.println(JSONUtil.toJSON(msg));
    	//充值结果通知
    	Lufax800011Vo lufax800011Vo=new Lufax800011Vo();
    	lufax800011Vo.setRecharge_status("fail");
    	lufax800011Vo.setAnshuo_batch_id("123");
    	lufax800011Vo.setActual_amount("200");
    	lufax800011Vo.setError_message(null);
    	JSONObject msg = GatewayUtils.callCateWayInterface(lufax800011Vo, "lufax800011Vo");
    	System.out.println("sfsdtryr5:" + msg);
    	//提现结果通知
//    	Lufax800013Vo lufax800013Vo=new Lufax800013Vo();
//    	lufax800013Vo.setWithdraw_msg("11");
//    	lufax800013Vo.setWithdraw_req_no("321");
//    	lufax800013Vo.setWithdraw_result("1");
//    	String res = HttpUtils.doPost("http://my.creditweb:8080/credit-web/gateway/withdrawResult", JSONUtil.toJSON(lufax800013Vo));  
//    	System.out.println("sfsdtryr5:" + res);
	 }
}
