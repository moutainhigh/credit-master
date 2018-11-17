package com.zdmoney.credit.common.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;

@SuppressWarnings("rawtypes")
public class HttpUtils {
	
    private static int connTimeOut = 10000;
    private static int readTimeOut = 20000;
    private static PoolingHttpClientConnectionManager connMgr;
    private static RequestConfig requestConfig;
    private static final int MAX_TIMEOUT = 120000;

    static {
        // 设置连接池
        connMgr = new PoolingHttpClientConnectionManager();
        // 设置连接池大小
        connMgr.setMaxTotal(100);
        connMgr.setDefaultMaxPerRoute(connMgr.getMaxTotal());

        RequestConfig.Builder configBuilder = RequestConfig.custom();
        // 设置连接超时
        configBuilder.setConnectTimeout(MAX_TIMEOUT);
        // 设置读取超时
        configBuilder.setSocketTimeout(MAX_TIMEOUT);
        // 设置从连接池获取连接实例的超时
        configBuilder.setConnectionRequestTimeout(MAX_TIMEOUT);
        // 在提交请求之前 测试连接是否可用
        configBuilder.setStaleConnectionCheckEnabled(true);
        requestConfig = configBuilder.build();
    }

    public static StringBuffer URLGet(String strUrl, Map parameterMap) throws IOException {
        String strTotalURL = "";
        strTotalURL = getTotalURL(strUrl, parameterMap);
        URL url = new URL(strTotalURL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setUseCaches(false);
        con.setConnectTimeout(connTimeOut);
        con.setReadTimeout(readTimeOut);
        HttpURLConnection.setFollowRedirects(true);
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        return getStringBufferFormBufferedReader(in);
    }

    public static StringBuffer URLGet(String strUrl, String content) throws IOException {
        String strTotalURL = "";
        strTotalURL = getTotalURL(strUrl, content);
        URL url = new URL(strTotalURL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setUseCaches(false);
        con.setConnectTimeout(connTimeOut);
        con.setReadTimeout(readTimeOut);
        HttpURLConnection.setFollowRedirects(true);
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        return getStringBufferFormBufferedReader(in);
    }
    
    public static InputStream URLGet(String strUrl) throws IOException {
        URL url = new URL(strUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setUseCaches(false);
        con.setConnectTimeout(connTimeOut);
        con.setReadTimeout(readTimeOut);
        HttpURLConnection.setFollowRedirects(true);
        return con.getInputStream();
    }
    
    public static StringBuffer URLGetJson(String strUrl, Map parameterMap) throws IOException {
    	String strTotalURL = "";
    	strTotalURL = getTotalURL(strUrl, parameterMap);
        URL url = new URL(strTotalURL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setUseCaches(false);
        con.setRequestProperty("Accept", "application/json;charset=UTF-8");
        con.setConnectTimeout(connTimeOut);
        con.setReadTimeout(readTimeOut);
        HttpURLConnection.setFollowRedirects(true);
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        return getStringBufferFormBufferedReader(in);
    }
    
    public static StringBuffer URLPost(String strUrl, Map map) throws IOException {
        String content = getContentURL(map);
        URL url = new URL(strUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setAllowUserInteraction(false);
        con.setUseCaches(false);
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        con.setConnectTimeout(connTimeOut);
        con.setReadTimeout(readTimeOut);
        BufferedWriter bout = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
        bout.write(content);
        bout.flush();
        bout.close();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));
        return getStringBufferFormBufferedReader(in);
    }
    
    public static StringBuffer URLPostGBK(String strUrl, Map map) throws IOException {
    	String content = getContentURL(map);
    	URL url = new URL(strUrl);
    	HttpURLConnection con = (HttpURLConnection) url.openConnection();
    	con.setDoInput(true);
    	con.setDoOutput(true);
    	con.setAllowUserInteraction(false);
    	con.setUseCaches(false);
    	con.setRequestMethod("POST");
    	con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=GBK");
    	con.setConnectTimeout(connTimeOut);
    	con.setReadTimeout(readTimeOut);
    	BufferedWriter bout = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
    	bout.write(content);
    	bout.flush();
    	bout.close();
    	BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(),"GBK"));
    	return getStringBufferFormBufferedReader(in);
    }

    public static StringBuffer URLPost(String strUrl, String content) throws IOException {
        URL url = new URL(strUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setAllowUserInteraction(false);
        con.setUseCaches(false);
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        con.setConnectTimeout(connTimeOut);
        con.setReadTimeout(readTimeOut);
        BufferedWriter bout = new BufferedWriter(new OutputStreamWriter(con.getOutputStream(),"UTF-8"));
        bout.write(content);
        bout.flush();
        bout.close();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));
        return getStringBufferFormBufferedReader(in);
    }
    
    public static StringBuffer crmUrlPost(String strUrl, Map map) throws IOException {
        String content = getContentURL(map);
        URL url = new URL(strUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setAllowUserInteraction(false);
        con.setUseCaches(false);
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        con.setRequestProperty("netmac", "");
        con.setRequestProperty("version", "1.0");
        con.setRequestProperty("token", "");
        con.setRequestProperty("User-Agent", "payCashier");
        con.setConnectTimeout(connTimeOut);
        con.setReadTimeout(readTimeOut);
        BufferedWriter bout = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
        bout.write(content);
        bout.flush();
        bout.close();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        return getStringBufferFormBufferedReader(in);
    }

    private static StringBuffer getStringBufferFormBufferedReader(BufferedReader in)
            throws IOException {
        StringBuffer returnStringBuffer = new StringBuffer();
        char[] tmpbuf = new char[1024];
        int num = in.read(tmpbuf);
        while (num != -1) {
            returnStringBuffer.append(tmpbuf, 0, num);
            num = in.read(tmpbuf);
        }
        in.close();
        return returnStringBuffer;
    }

    public static String getTotalURL(String strUrl, Map parameterMap) {
        String content = getContentURL(parameterMap);
        return getTotalURL(strUrl, content);
    }

    public static String getTotalURL(String strUrl, String content) {
        String totalURL = strUrl;
        if (totalURL.indexOf("?") == -1)
            totalURL = totalURL + "?";
        else
            totalURL = totalURL + "&";

        totalURL = totalURL + content;
        return totalURL;
    }

    public static String getContentURL(Map parameterMap) {
        if ((parameterMap == null) || (parameterMap.keySet().size() == 0))
            return "";
        StringBuffer url = new StringBuffer();
        Set keys = parameterMap.keySet();
        for (Iterator i = keys.iterator(); i.hasNext();) {
            String key = String.valueOf(i.next());
            if (!(parameterMap.containsKey(key)))
                break;
            Object val = parameterMap.get(key);
            String str = (val != null) ? val.toString() : "";
            try {
                str = URLEncoder.encode(str, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            url.append(key).append("=").append(str).append("&");
        }
        String strURL = "";
        strURL = url.toString();
        if ('&' == strURL.charAt(strURL.length() - 1)) {
            strURL = strURL.substring(0, strURL.length() - 1);
        }
        return strURL;
    }
    
    /**
	 * 读取request流
	 * 
	 * @param request
	 * @return
	 */
	public static String readReqStr(HttpServletRequest request) {
		BufferedReader reader = null;
		StringBuilder sb = new StringBuilder();
		try {
			reader = new BufferedReader(new InputStreamReader(
					request.getInputStream(), "utf-8"));
			String line = null;

			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != reader) {
					reader.close();
				}
			} catch (IOException e) {

			}
		}
		return sb.toString();
	}
	
	/**
	 * 获取请求的真实Ip
	 * 
	 * @param request
	 * @return
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
		if (ip!=null && !"".equals(ip) && ip.contains(",")) {
			String[] ips = ip.split(",");
			ip = ips[ips.length - 1];
		}
		return ip;
	}
    /**
     * 发送 GET 请求（HTTP），不带输入数据
     * @param url
     * @return
     */
    public static String doGet(String url) {
        return doGet(url, new HashMap<String, Object>());
    }

    /**
     * 发送 GET 请求（HTTP），K-V形式
     * @param url
     * @param params
     * @return
     */
    public static String doGet(String url, Map<String, Object> params) {
        String apiUrl = url;
        StringBuffer param = new StringBuffer();
        int i = 0;
        for (String key : params.keySet()) {
            if (i == 0)
                param.append("?");
            else
                param.append("&");
            param.append(key).append("=").append(params.get(key));
            i++;
        }
        apiUrl += param;
        String result = null;
        HttpClient httpclient = new DefaultHttpClient();
        try {
            HttpGet httpPost = new HttpGet(apiUrl);
            HttpResponse response = httpclient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();

            System.out.println("执行状态码 : " + statusCode);

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                result = IOUtils.toString(instream, "UTF-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 发送 POST 请求（HTTP），不带输入数据
     * @param apiUrl
     * @return
     */
    public static String doPost(String apiUrl) {
        return doPost(apiUrl, new HashMap<String, Object>());
    }

    /**
     * 发送 POST 请求（HTTP），K-V形式
     * @param apiUrl API接口URL
     * @param params 参数map
     * @return
     */
    public static String doPost(String apiUrl, Map<String, Object> params) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String httpStr = null;
        HttpPost httpPost = new HttpPost(apiUrl);
        CloseableHttpResponse response = null;

        try {
            httpPost.setConfig(requestConfig);
            List<NameValuePair> pairList = new ArrayList<>(params.size());
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry
                        .getValue().toString());
                pairList.add(pair);
            }
            httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName("UTF-8")));
            response = httpClient.execute(httpPost);
            System.out.println(response.toString());
            HttpEntity entity = response.getEntity();
            httpStr = EntityUtils.toString(entity, "UTF-8");
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
        return httpStr;
    }

    /**
     * 发送 POST 请求（HTTP），JSON形式
     * @param apiUrl
     * @param json json对象
     * @return
     */
    public static String doPost(String apiUrl, Object json) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String httpStr = null;
        HttpPost httpPost = new HttpPost(apiUrl);
        CloseableHttpResponse response = null;

        try {
            httpPost.setConfig(requestConfig);
            StringEntity stringEntity = new StringEntity(json.toString(),"UTF-8");//解决中文乱码问题
            stringEntity.setContentEncoding("UTF-8");
            stringEntity.setContentType("application/json");
            httpPost.setEntity(stringEntity);
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            System.out.println(response.getStatusLine().getStatusCode());
            if (!(200 == response.getStatusLine().getStatusCode())) {
                throw new PlatformException(ResponseEnum.FULL_MSG,String.format("连接服务器失败！响应码为：%s",response.getStatusLine().getStatusCode()));
            }
            httpStr = EntityUtils.toString(entity, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            throw new PlatformException(ResponseEnum.FULL_MSG,String.format("连接服务器异常！异常为:%s",e.getMessage()));
        } finally {
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return httpStr;
    }

    /**
     * 发送 SSL POST 请求（HTTPS），K-V形式
     * @param apiUrl API接口URL
     * @param params 参数map
     * @return
     */
    public static String doPostSSL(String apiUrl, Map<String, Object> params) {
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory()).setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).build();
        HttpPost httpPost = new HttpPost(apiUrl);
        CloseableHttpResponse response = null;
        String httpStr = null;

        try {
            httpPost.setConfig(requestConfig);
            List<NameValuePair> pairList = new ArrayList<NameValuePair>(params.size());
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry
                        .getValue().toString());
                pairList.add(pair);
            }
            httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName("utf-8")));
            response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                return null;
            }
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                return null;
            }
            httpStr = EntityUtils.toString(entity, "utf-8");
        } catch (Exception e) {
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
        return httpStr;
    }

    /**
     * 发送 SSL POST 请求（HTTPS），JSON形式
     * @param apiUrl API接口URL
     * @param json JSON对象
     * @return
     */
    public static String doPostSSL(String apiUrl, Object json) {
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory()).setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).build();
        HttpPost httpPost = new HttpPost(apiUrl);
        CloseableHttpResponse response = null;
        String httpStr = null;

        try {
            httpPost.setConfig(requestConfig);
            StringEntity stringEntity = new StringEntity(json.toString(),"UTF-8");//解决中文乱码问题
            stringEntity.setContentEncoding("UTF-8");
            stringEntity.setContentType("application/json");
            httpPost.setEntity(stringEntity);
            response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                return null;
            }
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                return null;
            }
            httpStr = EntityUtils.toString(entity, "utf-8");
        } catch (Exception e) {
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
        return httpStr;
    }

    /**
     * 创建SSL安全连接
     *
     * @return
     */
    private static SSLConnectionSocketFactory createSSLConnSocketFactory() {
        SSLConnectionSocketFactory sslsf = null;
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {

                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            sslsf = new SSLConnectionSocketFactory(sslContext, new X509HostnameVerifier() {

                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }

                @Override
                public void verify(String host, SSLSocket ssl) throws IOException {
                }

                @Override
                public void verify(String host, X509Certificate cert) throws SSLException {
                }

                @Override
                public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
                }
            });
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return sslsf;
    }
    
    /**
     * json参数形式的post请求
     * @param url
     * @param json
     * @return
     */
    public static String doJsonPost(String url, String json) {
        // 创建HttpClient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String resultString = "";
        CloseableHttpResponse httpResponse = null;
        try {
            // 创建HttpPost对象
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new StringEntity(json,ContentType.APPLICATION_JSON));

            // 开始执行http请求
            long startTime = System.currentTimeMillis();
            httpResponse = httpclient.execute(httpPost);
            long endTime = System.currentTimeMillis();

            // 获得响应状态码
            int statusCode = httpResponse.getStatusLine().getStatusCode();

            // 取出应答字符串
            HttpEntity httpEntity = httpResponse.getEntity();
            resultString = EntityUtils.toString(httpEntity,Charset.forName("UTF-8"));

            // 判断返回状态是否为200
            if (statusCode != HttpStatus.SC_OK) {
                throw new RuntimeException(String.format("\n\tStatus:%s\n\tError Message:%s", statusCode,resultString));
            }
        } catch (ClientProtocolException e) {
        	e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (httpResponse != null) {
                    httpResponse.close();
                }
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultString;
    }
}