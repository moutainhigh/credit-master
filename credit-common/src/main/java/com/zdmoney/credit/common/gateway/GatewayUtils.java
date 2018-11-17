package com.zdmoney.credit.common.gateway;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zdmoney.credit.common.constant.GatewayConstant;
import com.zdmoney.credit.common.constant.GatewayFuncIdEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.util.Assert;
import com.zdmoney.credit.common.util.HttpUtils;
import com.zdmoney.credit.common.util.MD5Util;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.util.coreUtil.JSONUtil;
import com.zdmoney.credit.common.vo.RequestInfo;

/**
 * Created by ym10094 on 2016/11/18.
 * 网关接口 工具类
 */
@Component
public class GatewayUtils {
	
	private static Logger logger = Logger.getLogger(GatewayUtils.class);
	
    public static String gatewayInterfaceUrl;
    
    @Value("${gateway.interface.url}")
	public void setGatewayInterfaceUrl(String url) {
		gatewayInterfaceUrl = url;
	}

	/**
     *获取发送网关的签名串
     * @param funcId
     * @param params
     * @param key
     * @param secretKey
     * @param <T>
     * @return
     * @throws UnsupportedEncodingException
     */
    public static <T> String getSign(String funcId ,T params,String key,String secretKey ) throws UnsupportedEncodingException {
        String encryptStr = "";
        Assert.notNull(funcId, ResponseEnum.FULL_MSG,"业务功能号不能为空");
        Assert.notNull(params, ResponseEnum.FULL_MSG, "加密参数不能为空");
        Assert.notNull(key, ResponseEnum.FULL_MSG, "key值不能为空");
        Assert.notNull(secretKey, ResponseEnum.FULL_MSG, "密钥不能为空");
        encryptStr = funcId+ JSONUtil.toJSON(params)+key+secretKey;
        return MD5Util.md5Hex(encryptStr,"UTF-8");
    }

    /**
     * 获取发送网关接口 的vo
     * @param reqParam
     * @param gatewayFuncIdEnum
     * @param <T>
     * @return
     * @throws UnsupportedEncodingException
     */
    public static<T> RequestInfo getSendGatewayRequestVo(T reqParam,GatewayFuncIdEnum gatewayFuncIdEnum) throws UnsupportedEncodingException {
        RequestInfo.GatewayRequest<T> gatewayRequestVo = RequestInfo.getGatewayRequest();
        gatewayRequestVo.setRequestId(GatewayConstant.PROJECT_NO + System.currentTimeMillis());
        gatewayRequestVo.setProjectNo(GatewayConstant.PROJECT_NO);
        gatewayRequestVo.setReqParam(reqParam);
        RequestInfo requestInfoVo = new RequestInfo();
        String funcId = gatewayFuncIdEnum.getCode();
        String key = String.valueOf(System.currentTimeMillis());
        String sign = GatewayUtils.getSign(funcId, gatewayRequestVo, key, GatewayConstant.SECRET_KEY);
        System.out.println("签名串：" + sign);
        requestInfoVo.setParams(gatewayRequestVo);
        requestInfoVo.setSign(sign);
        requestInfoVo.setFuncId(funcId);
        requestInfoVo.setKey(key);
        return requestInfoVo;
    }
    
    /**
     * 调用网关接口
     * @param requestVo 请求参数
     * @param funcId	接口功能号
     * @return
     */
    public static JSONObject callCateWayInterface(Object requestVo, String funcId){
    	JSONObject jsonObject = new JSONObject();
    	GatewayFuncIdEnum gatewayEnums = getGatewayEnumByCode(funcId);
    	if(gatewayEnums == null){
    		logger.debug("未找到功能号为："+funcId+" 的接口");
    		throw new PlatformException(ResponseEnum.FULL_MSG,"未找到该接口号");
    	}	
    	logger.info(gatewayEnums.getValue()+"接口参数："+JSONUtil.toJSON(requestVo));
		RequestInfo requestInfoVo = null;
		String rsultStr = "";
		String url = GatewayUtils.gatewayInterfaceUrl;
		if (Strings.isEmpty(url)) {
			throw new PlatformException(ResponseEnum.FULL_MSG,"接口请求路径不存在");
		}
		try {
			requestInfoVo = GatewayUtils.getSendGatewayRequestVo(requestVo, gatewayEnums);
		}catch (UnsupportedEncodingException e){
			e.printStackTrace();
			throw new PlatformException(ResponseEnum.FULL_MSG,"生成调用网关的接口签名异常");
		}catch (Exception e){
			e.printStackTrace();
			throw new PlatformException(ResponseEnum.FULL_MSG,"生成调用网关的接口的Vo异常");
		}
		try{
			logger.info("请求网关--"+gatewayEnums.getValue()+"接口url:"+url+"参数："+JSONUtil.toJSON(requestInfoVo));
			rsultStr = HttpUtils.doPost(url, JSONUtil.toJSON(requestInfoVo));
			rsultStr = URLDecoder.decode(rsultStr, "UTF-8");
			logger.info("请求网关--"+gatewayEnums.getValue()+"接口url:"+url+"响应："+rsultStr);
			JSONObject jsonResponse =  JSON.parseObject(rsultStr);
			if(GatewayConstant.RESPONSE_SUCCESS.equals(jsonResponse.getString("resCode"))){
				if(jsonResponse.containsKey("infos")){
					return jsonResponse.getJSONObject("infos");
				}
			}else{
				throw new PlatformException(ResponseEnum.FULL_MSG,"网关返回结果，"+jsonResponse.getString("respDesc"));
			}
		}catch(PlatformException e){
			throw new PlatformException(ResponseEnum.FULL_MSG,e.getMessage());
		}catch (Exception e){
			e.printStackTrace();
			throw new PlatformException(ResponseEnum.FULL_MSG,"调用网关接口异常");
		}
		return jsonObject;
    }
    
    /**
     * 通过code 获取 网关接口枚举类
     * @param code
     * @return
     */
    public static GatewayFuncIdEnum getGatewayEnumByCode(String code){
    	GatewayFuncIdEnum []gatewayEnums = GatewayFuncIdEnum.values();
    	for(GatewayFuncIdEnum gatewayEnum:gatewayEnums){
    		if(gatewayEnum.getCode().equals(code)){
    			return gatewayEnum;
    		}
    	}
    	return null;
    }
    
    /**
     * 校验错误列表
     * @param jsonArray
     * @param field
     * @param value
     * @return
     */
    public static boolean checkErrListByField(JSONArray jsonArray, String field, String value){
    	boolean res = false;
    	if(jsonArray == null || jsonArray.size() == 0){
    		res = true;
    	}else{
    		for(int i=0; i<jsonArray.size(); i++){
    			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
    			if(jsonObject.containsKey(field)){
    				if(value.equals(jsonObject.getString(field))){
    					res = false;
    					break;
    				}
    			}
    		}
    	}
    	return res;
    }

    /**
     * 检查是status SUCCESS/FAIL
     * @param jsonResponse 
     */
	public static boolean checkResultStatus(JSONObject jsonResponse) {
		boolean res = false;
		if(jsonResponse.containsKey("status")){
			String status = jsonResponse.getString("status");
			if("SUCCESS".equals(status)){
				return true;
			}else{
				if(jsonResponse.containsKey("msg")){
					String errMsg = jsonResponse.getString("msg");
					throw new PlatformException(ResponseEnum.FULL_MSG, "数信方响应,接口调用失败:"+errMsg);
				}
			}
		}
		return res;
	}
	
	/**
	 * 获取返回数据错误列表
	 * @param jsonObject
	 * @return
	 */
	public static JSONArray getErrJSONArray(JSONObject jsonObject){
		JSONArray jsonArray = new JSONArray();
		if(jsonObject.containsKey("listErr")){
			jsonArray = jsonObject.getJSONArray("listErr");
		}
		return jsonArray;
	}

	/**
	 * 获取外贸3网关调用结果信息
	 * @param jsonObject
	 * @return
	 */
	public static JSONObject getReponseContentJSONObject(JSONObject jsonObject, boolean hasInfo) {
		if(hasInfo){
	        String resCode =  (String)jsonObject.get("resCode");
	        if (!GatewayConstant.RESPONSE_SUCCESS.equals(resCode)) {
	            String respDesc = (String)jsonObject.get("respDesc");
	            throw new PlatformException(ResponseEnum.FULL_MSG, "核心调网关："+respDesc);
	        }
	         jsonObject = jsonObject.getJSONObject("infos");			
		}
        if(!GatewayConstant.RESPONSE_SUCCESS.equals((String)jsonObject.get("respCode"))){
        	throw new PlatformException(ResponseEnum.FULL_MSG, "网关调外贸3："+(String)jsonObject.get("respDesc"));
        }
        JSONObject content = JSONObject.parseObject((String)jsonObject.get("content"));
        return content;
	}
	
	/**
	 * 获取外贸3网关调用结果信息
	 * @param jsonObject 
	 * @return
	 */
	public static JSONArray getReponseContentJSONArray(JSONObject jsonObject, boolean hasInfo) {
		if(hasInfo){
	        String resCode =  (String)jsonObject.get("resCode");
	        if (!GatewayConstant.RESPONSE_SUCCESS.equals(resCode)) {
	            String respDesc = (String)jsonObject.get("respDesc");
	            throw new PlatformException(ResponseEnum.FULL_MSG, "核心调网关："+respDesc);
	        }
	        jsonObject = jsonObject.getJSONObject("infos");			
		}

        if(!GatewayConstant.RESPONSE_SUCCESS.equals((String)jsonObject.get("respCode"))){
        	throw new PlatformException(ResponseEnum.FULL_MSG, "网关调外贸3："+(String)jsonObject.get("respDesc"));
        }
        JSONArray content = JSONArray.parseArray((String)jsonObject.get("content"));
        return content;
	}
}