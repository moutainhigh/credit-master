package com.zdmoney.credit.common.util;

import javassist.bytecode.stackmap.BasicBlock;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/***
 * 调用第三方接口 走HTTP协议
 * 
 * @author Ivan
 *
 */
@Component
public class HttpUrlConnection {

	private static Log logger = LogFactory.getLog(HttpUrlConnection.class);

	static MediaType mediaType = MediaType.parseMediaType("application/json; charset=UTF-8");

	static MediaType mediaTypeForm = MediaType.parseMediaType("application/x-www-form-urlencoded; charset=UTF-8");

	@Autowired
	RestTemplate restTemplate;

	/**
	 * 请求第三方接口
	 * 
	 * @param requestUrl
	 *            请求目标地址
	 * @param requestParam
	 *            请求参数
	 * @param type
	 *            响应数据类型
	 * @return
	 */
	public <T> T postForEntity(String requestUrl, JSONObject param, Class<T> type) {
		return postForEntity(requestUrl, param, mediaTypeForm, type);
	}

	/**
	 * 请求第三方接口
	 * 
	 * @param url
	 *            请求目标地址
	 * @param mediaType
	 *            头文件信息
	 * @param param
	 *            请求参数
	 * @param type
	 *            响应数据类型
	 * @return
	 */
	public <T> T postForEntity(String requestUrl, JSONObject param, MediaType mediaType, Class<T> type) {
		Assert.notNullAndEmpty(requestUrl, ResponseEnum.FULL_MSG, "请求地址不存在");
		ResponseEntity<T> responseEntity = null;
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(mediaType);
			HttpEntity<String> request = new HttpEntity<String>(JsonUtil.toUrlParam(param), headers);
			responseEntity = restTemplate.postForEntity(requestUrl, request, type);
			HttpStatus httpStatus = responseEntity.getStatusCode();
			switch (httpStatus) {
			case OK:
				logger.debug("[status:200]" + requestUrl + "参数：" + param);
				/** 正常响应 **/
				T value = responseEntity.getBody();
				if (value instanceof String) {
					return (T) new String(value.toString().getBytes("ISO-8859-1"), "UTF-8");
				} else {
					return value;
				}
			default:
				logger.error("[status:" + httpStatus + "]" + requestUrl + "参数：" + param);
				throw new PlatformException(ResponseEnum.FULL_MSG, "请求地址[" + requestUrl + "]异常，响应码：" + httpStatus);
			}
		} catch (PlatformException ex) {
			logger.error(requestUrl + "参数：" + param, ex);
			throw ex;
		} catch (Exception ex) {
			logger.error(requestUrl + "参数：" + param, ex);
			throw new PlatformException(ResponseEnum.FULL_MSG, "请求地址[" + requestUrl + "]异常，错误信息：" + ex);
		}

	}

	/**
	 * 文件上传至图片系统
	 * @param appNo
	 * @param fileName
	 * @param inputStream
	 * @param url
	 * @return
	 */
	public boolean uploadData2PicSystem(String appNo,String fileName, InputStream inputStream,String url) {
		try {
			// 片文件通过base64编码转换成字符串
			// 封装接口参数
			JSONObject params = new JSONObject();
			params.put("appNo", appNo);
			params.put("iremark", "");
			params.put("sysName", "aps");
			params.put("nodeKey","");
			//params.put("nodeKey", "input-modify");
			params.put("ifPatchBolt", "N");
			params.put("Filename", fileName);

			logger.info("上传结清附件，接口输入参数：" + JSONObject.toJSONString(params));
			byte[] data = null;
			// 读取图片字节数组
			data = new byte[inputStream.available()];
			inputStream.read(data);
			inputStream.close();
			// 对字节数组Base64编码
			Base64 base64 = new Base64();
			String fileString = URLEncoder.encode(base64.encodeToString(data), "UTF-8");
			params.put("fileBytes", fileString);
			// 调用接口上传图片
			String result = this.postForEntity(url, params, String.class);

			if (Strings.isEmpty(result)) {
				throw new PlatformException(ResponseEnum.FULL_MSG, "图片系统上传文件接口响应结果为空！");
			}
			// 返回信息处理
			result = URLDecoder.decode(result, "UTF-8");

			logger.info("上传结清附件，接口响应结果：" + result);
			JSONObject obj = JSONObject.parseObject(result);

			List resultList = (List) obj.get("obj");
			if (CollectionUtils.isEmpty(resultList)) {
				throw new PlatformException(ResponseEnum.FULL_MSG, "图片系统上传接口返回结果数据异常！");
			}
			// 响应结果码信息
			JSONObject json = (JSONObject) resultList.get(0);

			if (!json.containsKey("flag")) {
				throw new PlatformException(ResponseEnum.FULL_MSG, "图片管理系统返回缺少【flag】Key");
			}
			String flag = json.getString("flag");
			if (flag == null || "failure".equals(flag)) {
				throw new PlatformException(ResponseEnum.FULL_MSG, json.getString("message"));
			}
			return true;
		} catch (Exception e) {
			logger.error("调用图片关系系统上传文件接口异常。。。", e);
			return false;
		}
	}
}
