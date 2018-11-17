package com.zdmoney.credit.test.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zdmoney.credit.common.json.JackJsonUtil;
import com.zdmoney.credit.ems.service.pub.IEMSService;
import com.zdmoney.credit.offer.service.pub.IOfferCreateService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;

@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试  
@ContextConfiguration   
({"/spring/*.xml"}) //加载配置文件 
public class EMSServiceTest {
	
	@Autowired 
	IEMSService EMSService;
	@Autowired
	ISysParamDefineService sysParamDefineService;
	private CloseableHttpClient httpClient ;
	@Test
	public void testSendByPost () {
		httpClient = HttpClients.createDefault();
		try {
			sendByPost(httpClient,"zdtest1022003","test11111","18616576616");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String sendByPost(HttpClient httpClient, String token, String msg, String mobiles) throws Exception{
        HttpPost post = new HttpPost(sysParamDefineService.getSysParamValueCache("sms", "sms.serverUrl"));

        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("cmd", "sendSm"));
        params.add(new BasicNameValuePair("username", sysParamDefineService.getSysParamValueCache("sms", "sms.uid")));
        params.add(new BasicNameValuePair("password", sysParamDefineService.getSysParamValueCache("sms", "sms.pwd")));
//        String mobs = formatMobileArray(mobiles);
        params.add(new BasicNameValuePair("smMtMessage", buildSendJson(
        		token,  mobiles, msg, sysParamDefineService.getSysParamValueCache("sms", "beginDateStr"), "0")));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();//设置请求和传输超时时间
        post.setConfig(requestConfig);
        post.setEntity(entity);

        HttpResponse response  = httpClient.execute(post);
        String result = EntityUtils.toString(response.getEntity());
        System.out.println("========================="+result);
        return result;
//      
       
    }
	
	 //如果支持定时？那么可以半夜发送？？
    private static String buildSendJson(String sendId, String mobsStr, String content,String beginDateStr, String dailyBeginTimeStr) {
        Map<String, String> m = new HashMap<String, String>();
        m.put("token", sendId);
        m.put("mobiles", mobsStr);
        m.put("content", content);
//        m.put("dailyBeginTime", dailyBeginTimeStr);
//        m.put("beginDate", beginDateStr);
//        Gson gson = new Gson();
        
        String json = "";
		try {
			json = JackJsonUtil.objToStr(m);
		} catch (JsonGenerationException e) {
		} catch (JsonMappingException e) {
		} catch (IOException e) {
		}

        return json;
    }
	
}
