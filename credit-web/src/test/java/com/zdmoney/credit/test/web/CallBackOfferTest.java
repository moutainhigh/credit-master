package com.zdmoney.credit.test.web;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.junit.Test;

public class CallBackOfferTest {

	@Test
	public void offer() throws HttpException, IOException {
		PostMethod post = new PostMethod("http://my.creditweb:8080/credit-web/offer/tppback/realtimeDeductBack20");
		String offerParam = "";
		offerParam = "<trans><tran><requestId></requestId><taskId></taskId><orderNo>LX_FEE_2016071800000035</orderNo><tradeFlow></tradeFlow>"
				+ "<returnCode>000000</returnCode><returnInfo>交易成功</returnInfo><successAmount>9.00</successAmount>"
				+ " <failReason>交易成功</failReason></tran></trans>";

		ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(offerParam.getBytes());

		// 设置请求的内容直接从文件中读取
		post.setRequestBody(tInputStringStream);
		// post.setRequestContentLength(tInputStringStream.);

		// 指定请求内容的类型
		post.setRequestHeader("Content-type", "text/xml; charset=UTF-8");
		HttpClient httpclient = new HttpClient();
		int result = httpclient.executeMethod(post);
		System.out.println("Response status code: " + result);
		System.out.println("Response body: ");
		System.out.println(post.getResponseBodyAsString());
		post.releaseConnection();
	}
}
