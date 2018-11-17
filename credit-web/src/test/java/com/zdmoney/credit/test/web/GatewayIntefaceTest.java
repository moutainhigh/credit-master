package com.zdmoney.credit.test.web;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import com.zdmoney.credit.api.app.vo.FinanceGrantRequest;
import com.zdmoney.credit.common.util.HttpUtils;
import com.zdmoney.credit.common.util.coreUtil.JSONUtil;
import com.zdmoney.credit.test.dao.ButBackEntity;
import com.zdmoney.credit.test.dao.Xdcore100004Vo;

/**
 * Created by ym10094 on 2016/11/9.
 */
public class GatewayIntefaceTest {


    public void disFinanceGrantResult(){
        List<FinanceGrantRequest> list = new ArrayList<>();
        FinanceGrantRequest request = new FinanceGrantRequest();
//        request.setPactNo("1231215");
//        request.setFounsSources("外贸2");
        FinanceGrantRequest request1 = new FinanceGrantRequest();
//        request.setPactNo("1231215asrfesrte");
//        request.setFounsSources("外贸fssere2");
        list.add(request);
        list.add(request1);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String httpStr = null;
        HttpPost httpPost = new HttpPost("http://my.creditweb:8080/credit-web/gateway/disposeFinanceGrantResult");
        CloseableHttpResponse response = null;
        try {
            System.out.println(JSONUtil.toJSON(request));
            StringEntity stringEntity = new StringEntity(JSONUtil.toJSON(list),"UTF-8");//解决中文乱码问题
            stringEntity.setContentEncoding("UTF-8");
            stringEntity.setContentType("application/json");
            httpPost.addHeader("Content-type","application/json; charset=utf-8");
            httpPost.setHeader("Accept", "application/json");
            httpPost.setEntity(stringEntity);
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            System.out.println(response.getStatusLine().getStatusCode());
            httpStr = EntityUtils.toString(entity, "UTF-8");
            System.out.println("sfsdtryr5:" + httpStr);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 推送待回购信息接口测试方法
     */
   // @Test
   public void backLoanTest(){
   	Xdcore100004Vo backLoanRequest = new Xdcore100004Vo();
    	backLoanRequest.setListBuyBack(new ArrayList<ButBackEntity>());
    	backLoanRequest.setRepAmt(new BigDecimal("11"));
    	backLoanRequest.setRepIntst(new BigDecimal("11"));
    	backLoanRequest.setRepTotal(new BigDecimal("11"));
    	ButBackEntity butBack1 = new ButBackEntity();
    	butBack1.setPactNo("ZDB20110012000002");
    	butBack1.setCustName("a");
    	butBack1.setRepAmt(new BigDecimal("11"));
    	butBack1.setRepIntst(new BigDecimal("11"));
    	ButBackEntity butBack2 = new ButBackEntity();
    	butBack2.setPactNo("b");
    	butBack2.setCustName("b");
    	butBack2.setRepAmt(new BigDecimal("22"));
    	butBack2.setRepIntst(new BigDecimal("22"));
    	backLoanRequest.getListBuyBack().add(butBack1);
    	backLoanRequest.getListBuyBack().add(butBack2);
    	String url = "http://my.creditweb:8080/credit-web/gateway/pushBackLoan";
    	String rep = HttpUtils.doPost(url, JSONUtil.toJSON(backLoanRequest));
    	System.out.println(rep);
//    	CloseableHttpClient httpClient = HttpClients.createDefault();
//        String httpStr = null;
//        HttpPost httpPost = new HttpPost("http://my.creditweb:8080/credit-web/gateway/pushBackLoan");
//        CloseableHttpResponse response = null;
//        try {
//            System.out.println(JSONUtil.toJSON(backLoanRequest));
//            
//            StringEntity stringEntity = new StringEntity(JSONUtil.toJSON(backLoanRequest),"UTF-8");//解决中文乱码问题
//            stringEntity.setContentEncoding("UTF-8");
//            stringEntity.setContentType("application/json");
//            httpPost.addHeader("Content-type","application/json; charset=utf-8");
//            httpPost.setHeader("Accept", "application/json");
//            httpPost.setEntity(stringEntity);
//            response = httpClient.execute(httpPost);
//            HttpEntity entity = response.getEntity();
//            System.out.println(response.getStatusLine().getStatusCode());
//
//            httpStr = EntityUtils.toString(entity, "UTF-8");
//            System.out.println("aaa:" + httpStr);
//            
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (response != null) {
//                try {
//                    EntityUtils.consume(response.getEntity());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }
    
    static public String toString(InputStream input, String encoding) throws IOException {
        return (null == encoding) ? toString(new InputStreamReader(input)) : toString(new InputStreamReader(
                input, encoding));
    }
    static public String toString(Reader reader) throws IOException {
        CharArrayWriter sw = new CharArrayWriter();
        copy(reader, sw);
        return sw.toString();
    }
    static public long copy(Reader input, Writer output) throws IOException {
        char[] buffer = new char[1 << 12];
        long count = 0;
        for (int n = 0; (n = input.read(buffer)) >= 0;) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }
    static public String httpPost(String url,String content, String encoding, long readTimeoutMs) throws IOException {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(3000);
            conn.setReadTimeout((int) readTimeoutMs);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
//            setHeaders(conn, headers, encoding);

            conn.getOutputStream().write(content.getBytes());

            int respCode = conn.getResponseCode();
            String resp = null;

            if (HttpURLConnection.HTTP_OK == respCode) {
                resp = toString(conn.getInputStream(), encoding);
            }
            else {
                resp = toString(conn.getErrorStream(), encoding);
            }
            return  "";
        }
        finally {
            if (null != conn) {
                conn.disconnect();
            }
        }
    }
    
    
    public static void main(String[] args){
//        GatewayIntefaceTest test= new GatewayIntefaceTest();
//        test.disFinanceGrantResult();
//        String pactNo = "15454561564_1";
//       System.out.println(pactNo.substring(0, pactNo.indexOf("_")));
//        System.out.println(pactNo.substring(pactNo.indexOf("_")+1));

    }
}
