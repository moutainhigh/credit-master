package com.zdmoney.credit.common.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.w3c.dom.Text;

import com.zdmoney.credit.common.vo.TppCallBackData;
import com.zdmoney.credit.common.vo.TppCallBackData20;

public class TPPHelper {
	// public static final String TPP_IP_ADDRESS = "192.16.6.188";
    public static final String TPP_IP_ADDRESS = "192.16.10.10";

    public static final String RESPONSE_TPP_SYSTEM_PREFIX = "TPP_REC_";

    public static final String[] FuiouBanks = {"","","","",""}; //富有报盘包含的银行,工商银行

    public static final String[] AllinpayBanks = {"","","","",""};//通联报盘包含的银行,

    public static final String ICBC = "102";

    public static final String FUIOU_SUCCESS_CODE = "000000"; //富有成功接收划扣消息返回的代码

    public static final String ALLINPAY_SUCCESS_CODE = "0000";//通联成功接收划扣消息返回的代码

    public static final String RESULT_SUCCESS_CODE = "000000";//TPP回执成功
    public static final String RESULT_FAIL_CODE = "111111";//TPP回执失败
    public static final String RESULT_BFSUCCESS_CODE = "444444";//TPP回执部分成功

    public static final String TPP_HANDLER_TELLER = "A0002"; //结算平台处理时的柜员号

    public static final String TPP_HANDLER_ORGANIZATION_CODE = "01"; //结算平台处理时的机构号

    public static final String OFFER_FILE_TYPE_OFFER = "1";  //报盘

    public static final String OFFER_FILE_TYPE_COUNTEROFFER = "2";  //回盘

    public static final String CALL_BACK_DATA_STATE_YIJIESHOU = "已接收";

    public static final String CALL_BACK_DATA_STATE_YICHULI = "已处理";

    public static final String TPP_RECEIVE_SUCCESS_CODE = "000001";//TPP接收报盘数据成功Code
    
    public static final String TPP20_RECEIVE_SUCCESS_CODE = "000000";//TPP接收报盘数据成功Code
    
    public static final String TPP20_RECEIVE_WORKING_CODE = "222222";//TPP接收报盘数据处理中code
    /**
     * 配置表中tpp2.0版本标示
     */
    public static final String TPP_VER20 = "2";
    /**
     * 获取客户端请求的IP地址
     * @param request
     * @return ip地址
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("http_client_ip");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        // 如果是多级代理，那么取第一个ip为客户ip
        if (ip != null && ip.indexOf(",") != -1) {
            ip = ip.substring(ip.lastIndexOf(",") + 1, ip.length()).trim();
        }
        return ip;
    }

    /**
     * 是否为合法的IP地址、
     * @author zhanghao
     * @date 2013-10-23
     * @param ipAddress 客户端请求的IP地址
     * @return 是否合法
     */
    public static boolean isLegitimateIPAddress(String ipAddress){
        return ipAddress.equals(TPP_IP_ADDRESS);
    }

    /**
     * 解析TPP（结算平台）系统请求过来的划扣结果
     * @author  zhanghao
     * @date 2013-10-30
     * @param inputStream
     * @return 响应的XML字符串
     * @throws IOException 
     */
    public static String parseResponseResult(ServletInputStream inputStream) throws IOException {
        StringBuilder builder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
        String line = null;
        while((line = bufferedReader.readLine())!=null){
            builder.append(line);
        }
        bufferedReader.close();

        return  builder.toString();
    }


    /**
     * 解析TPP（结算平台）系统请求过来的划扣结果
     * @author  zhanghao
     * @date 2013-10-23
     * @param xml[]  0:xml  1:msgin的id
     * @return
     */
    public static List<TppCallBackData> parseRealtimeDeductBackData(String[] xml){

        StringReader reader = new StringReader(xml[0]);

        SAXReader saxReader = new SAXReader();

        List<TppCallBackData> parseResult = new ArrayList<TppCallBackData>();

        try {
            // 解析xml文件
            Document doc = saxReader.read(reader);
            // 获得xml根元素
            Element root = doc.getRootElement();

            List<Element> elements = root.elements("tran");
            for (Element element : elements){
            	TppCallBackData objects = new TppCallBackData();
            	objects.setOrderNo(element.element("orderNo").getText());
            	objects.setReturnCode(element.element("returnCode").getText());
            	objects.setReturnInfo(element.element("returnInfo").getText());
            	objects.setRequestCode(element.element("requestId").getText());
            	objects.setMsgInId(xml[1]);
            	
                parseResult.add(objects);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return parseResult;
    }
    
    /**
     * 解析TPP2.0（结算平台）系统请求过来的划扣结果
     * @param xml[]  0:xml  1:msgin的id
     * @return
     */
    public static List<TppCallBackData20> parseRealtimeDeductBackData20(String[] xml){

        StringReader reader = new StringReader(xml[0]);

        SAXReader saxReader = new SAXReader();

        List<TppCallBackData20> parseResult = new ArrayList<TppCallBackData20>();

        try {
            // 解析xml文件
            Document doc = saxReader.read(reader);
            // 获得xml根元素
            Element root = doc.getRootElement();

            List<Element> elements = root.elements("tran");
            for (Element element : elements){
            	TppCallBackData20 objects = new TppCallBackData20();
            	objects.setFailReason(element.element("failReason").getText());
            	objects.setOrderNo(element.element("orderNo").getText());
            	objects.setReturnCode(element.element("returnCode").getText());
            	objects.setReturnInfo(element.element("returnInfo").getText());
            	objects.setRequestId(element.element("requestId").getText());
            	objects.setTaskId(element.element("taskId").getText());
            	objects.setSuccessAmount(element.element("successAmount").getText());
            	objects.setTradeFlow(element.element("tradeFlow").getText());
            	objects.setPaySysNo(element.element("paySysNo").getText());
            	objects.setMerId(element.element("merId").getText());
            	objects.setMsgInId(xml[1]);
            	
                parseResult.add(objects);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return parseResult;
    }

    /**
     * 解析TPP（结算平台）系统请求过来的划扣结果
     * @author  zhanghao
     * @date 2013-10-23
     * @param xml
     * @return
     */
    public static String parseRealtimeDeductBackRequestCode(String xml){
        StringReader reader = new StringReader(xml);

        SAXReader saxReader = new SAXReader();

        String requestCode = "";
        try {
            // 解析xml文件
            Document doc = saxReader.read(reader);
            // 获得xml根元素
            Element root = doc.getRootElement();

            requestCode = root.element("requestCode").getText();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return requestCode;

    }

    /**
     * 生成xml字符串的处理结果
     * @author zhanghao
     * @date 2013-11-14
     * @param responseResultList
     * @return
     */
    public static String generateXml(List<Object[]> responseResultList){
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = dbf.newDocumentBuilder();
        } catch (Exception e) {
            e.printStackTrace();
        }
        org.w3c.dom.Document doc = builder.newDocument();
        org.w3c.dom.Element root = doc.createElement("orders");
        // 将根元素添加到文档上
        doc.appendChild(root);
        // 循环添加处理结果
        for(Object[] responseResult : responseResultList){
            org.w3c.dom.Element order = doc.createElement("order");
            root.appendChild(order);

            org.w3c.dom.Element orderNo = doc.createElement("orderNo");
            Text orderNoText = doc.createTextNode((String) responseResult[0]);
            orderNo.appendChild(orderNoText);

            org.w3c.dom.Element handleResult = doc.createElement("handleResult");
            Text handleResultText = doc.createTextNode((String) responseResult[1]);
            handleResult.appendChild(handleResultText);

            order.appendChild(orderNo);
            order.appendChild(handleResult);
        }
        try {
            String result = callWriteXmlString(doc, "UTF-8");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 将Document内容 写入XML字符串并返回
     * @author zhanghao
     * @date 2013-11-14
     * @param doc
     * @param encoding
     * @return
     */
    public static String callWriteXmlString(org.w3c.dom.Document doc, String encoding) {

        try {
            Source source = new DOMSource(doc);
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            OutputStreamWriter write = new OutputStreamWriter(outStream);
            Result result = new StreamResult(write);

            Transformer xformer = TransformerFactory.newInstance().newTransformer();
            xformer.setOutputProperty(OutputKeys.ENCODING, encoding);

            xformer.transform(source, result);
            return outStream.toString();

        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
            return null;
        } catch (TransformerException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 发送处理结果
     * @author zhanghao
     * @date 2013-11-04
     * @param responseResultXmlString 处理结果字符串
     * @param url 发送地址
     * @return 是否发送成功
     */
    public static boolean sendHandleResult(String responseResultXmlString, String url){
		return false;
//        String result = HttpClientHelper.post(url, null, new InputStreamEntity(stringConvertInputStream(responseResultXmlString)));
//
//        System.out.println("往结算平台发送信贷系统回盘处理结果消息::::" + result)
    }

    /**
     * 将一个字符串转化为输入流
     * @author zhanghao
     * @date 2013-11-05
     * @param val
     * @return
     */
    public static InputStream stringConvertInputStream(String val){
        if (!StringUtils.isBlank(val)){
            try{
                ByteArrayInputStream inputStream = new ByteArrayInputStream(val.getBytes());
                return inputStream;
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 将一个输入流转换为字符串
     * @author zhanghao
     * @date 2013-11-05
     * @param inputStream
     * @return
     */
    public static String inputStreamConvertString(InputStream inputStream){
        if (inputStream != null){
            try{
                BufferedReader tBufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer tStringBuffer = new StringBuffer();
                String line = new String("");
                while ((line = tBufferedReader.readLine()) != null){
                    tStringBuffer.append(line);
                }
                return tStringBuffer.toString();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 构建业务系统响应给TPP系统的划扣结果的信息
     * @author  zhanghao
     * @date 2013-10-23
     * @param requestCode TPP系统本次的请求编号
     * @param localHandleResult 本地处理结果
     * @return 响应给TPP系统的字符串
     */
    public static String builderTPPSystemResponseInfo(String requestCode, boolean localHandleResult){
        return localHandleResult ? RESPONSE_TPP_SYSTEM_PREFIX + requestCode + "_TRUE" : RESPONSE_TPP_SYSTEM_PREFIX + requestCode + "_FALSE";
    }

    /**
     * 截取结算平台的订单号,转换成信贷系统里的报盘数据ID
     * @author zhanghao
     * @date 2013-11-01
     * @param orderNo 订单号
     * @return 报盘数据的ID（Offer表的主键ID）
     */
    public static Long subOrderNo(String orderNo){
        return Long.parseLong(orderNo.substring(9,orderNo.length()));
    }


    /**
     * 报文转义
     * @param xml  待转义报文
     * @return
     */
    public static String replaceXml(String xml) {
        if (xml == null || xml.trim().equals("") || xml.length() <= 0 || xml.indexOf("<trans>")<0)
            return xml;
        StringBuffer newXml = new StringBuffer();
        int start = 0;
        int end = 0;
        String center = "";

        newXml.append(xml.substring(0, xml.indexOf("<trans>") + 7));

        while (xml.indexOf("<tran>")>=0) {
            start = xml.indexOf("<tran>");
            end = xml.indexOf("</tran>");
            if(start<0 || end<0){
                return xml;
            }
            center = xml.substring(start, end + 7);
            xml = xml.substring(end + 7);
            //替换特殊字符
            center = replace(center);
            newXml.append(center);
        }
        newXml.append("</trans>");
        return newXml.toString();
    }
    /**
     * 替换特殊字符
     * @param xml  待替换xml
     * @return
     */
    private static String replace(String xml) {
        StringBuffer newXml = new StringBuffer();
        String returnInfo = "<returnInfo>";
        String returnInfo_end = "</returnInfo>";
        int start = xml.indexOf(returnInfo) + returnInfo.length();
        int end = xml.indexOf(returnInfo_end);

        if (start > 0 && end > 0 && end > start) {
            String tran = xml.substring(0, start);
            String center = xml.substring(start, end);
            String tran_end = xml.substring(end, xml.length());
            center = center.replaceAll("&", "&amp;");
            center = center.replaceAll("<", "&lt;");
            center = center.replaceAll(">", "&gt;");
            center = center.replaceAll("\"", "&quot;");
            center = center.replaceAll("'", "&apos;");
            newXml.append(tran);
            newXml.append(center);
            newXml.append(tran_end);
        }

        return newXml.toString();
    }
}
