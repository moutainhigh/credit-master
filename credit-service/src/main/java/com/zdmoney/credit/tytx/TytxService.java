package com.zdmoney.credit.tytx;



import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.util.coreUtil.Constants;
import com.zdmoney.credit.common.vo.Result;
import com.zdmoney.credit.tytx.domain.CreditSmsHistory;
import com.zdmoney.credit.tytx.pub.ICreditSmsHistoryService;
import com.zdmoney.credit.tytx.vo.CustInfoVo;
import com.zdmoney.credit.tytx.vo.MsgBodyVo;
import com.zdmoney.credit.tytx.vo.SendMsgVo;
import com.zendaimoney.ice.uc.client.service.IDataSendService;
import com.zendaimoney.ice.uc.client.service.impl.ShortSendService;

/**
 * 统一通信平台相关操作
 * @author fuhongxing
 * @date 2016年6月28日
 * @version v1.0.0
 */
@Service
public class TytxService {
	
	private static Logger logger = LoggerFactory.getLogger(TytxService.class);
	
	@Autowired
	private ICreditSmsHistoryService iCreditSmsHistoryService;
	
	/**
	 * 发送短信
	 * @param mobile
	 * @param content
	 * @return
	 */
	public Result<String> sendMsg(Map<String, Object> map) {
		return sendMsg(map,"");
	}
	/**
	 * 发送短信
	 * @param map 参数
	 * @param mType 信息类别
	 * @return
	 */
	public Result<String> sendMsg(Map<String, Object> map,String mType) {
		Result<String> results = new Result<String>(Result.Type.SUCCESS);
		//发送短信记录
		CreditSmsHistory smsHistory = new CreditSmsHistory();
		smsHistory.setMobile(map.get("mobile").toString());
		smsHistory.setSender("systemjob");
		smsHistory.setCreateTime(new Date());
		smsHistory.setType("leaveOfficeEmployeeSendMessage");
		smsHistory.setCreateTime(new Date());
		smsHistory.setRequestDate(new Date());
		smsHistory.setEmployeeId(map.get("employeeId").toString());
		SendMsgVo sendMsgVo = new SendMsgVo();
		if (!Strings.isEmpty(mType)) {
			sendMsgVo.setMtype(mType); //信息类别编码
		}
		CustInfoVo custInfoVo = new CustInfoVo();
		custInfoVo.setMarkInfo(map.get("mobile").toString());
		sendMsgVo.getuInfo().add(custInfoVo);
		//消息体
		MsgBodyVo msgBodyVo = new MsgBodyVo();
		msgBodyVo.setBodyFormat("text");
		msgBodyVo.setBody(map.get("content").toString());
		sendMsgVo.setMsgBody(msgBodyVo);
		smsHistory.setRequestContent(JSONObject.toJSONString(sendMsgVo));//请求内容
		smsHistory.setOperationMethod("调用统一通信平台 shortSendService.sendMsg()");
		/** 调用统一通讯接口 **/
		JSONObject result = this.sendMsg(sendMsgVo);
		smsHistory.setResponseDate(new Date());
		
		//900000为统一通讯平台成功代码
		try {
			//设置响应内容
			if(result !=null && (Constants.TYTX_SUCCESS_CODE).equals(result.getString("ucCode"))){
				logger.info("短信发送成功。"+result.toJSONString());
				results.addMessage("短信发送成功");
			}else{
				logger.info("短信发送失败" + result.toJSONString());
				results.setType(Result.Type.FAILURE);
				results.addMessage("短信发送失败");
			}
		} catch (Exception e) {
			smsHistory.setRemark("短信发送异常!" + e.getMessage());
			results.setType(Result.Type.WARNING);
			logger.error("发送短息异常", e);
		}finally{
			smsHistory.setStatus(result.getString("ucCode"));
			smsHistory.setResponseContent(result.toJSONString());
			iCreditSmsHistoryService.save(smsHistory);
		}
		return results;
	}
	
	
	/**
	 * 签收系统发送短信
	 * @param mobile
	 * @param content
	 * @return
	 */
	public Result<String> signValidSendMsg(Map<String, Object> map) {
		Result<String> results = new Result<String>(Result.Type.SUCCESS);
		//发送短信记录
		CreditSmsHistory smsHistory = new CreditSmsHistory();
		smsHistory.setMobile(map.get("mobile").toString());
		smsHistory.setSender("signSystemPort");
		smsHistory.setCreateTime(new Date());
		smsHistory.setType("signSystemSendValidateCode");
		smsHistory.setCreateTime(new Date());
		smsHistory.setRequestDate(new Date());
		smsHistory.setEmployeeId(map.get("employeeId").toString());
		SendMsgVo sendMsgVo = new SendMsgVo();
		sendMsgVo.setMtype("10000002"); //信息类别编码
		CustInfoVo custInfoVo = new CustInfoVo();
		custInfoVo.setMarkInfo(map.get("mobile").toString());
		sendMsgVo.getuInfo().add(custInfoVo);
		//消息体
		MsgBodyVo msgBodyVo = new MsgBodyVo();
		msgBodyVo.setBodyFormat("text");
		msgBodyVo.setBody(map.get("content").toString());
		sendMsgVo.setMsgBody(msgBodyVo);
		smsHistory.setRequestContent(JSONObject.toJSONString(sendMsgVo));//请求内容
		smsHistory.setOperationMethod("调用统一通信平台 shortSendService.sendMsg()");
		/** 调用统一通讯接口 **/
		JSONObject result = this.sendMsg(sendMsgVo);
		smsHistory.setResponseDate(new Date());
		
		//900000为统一通讯平台成功代码
		try {
			//设置响应内容
			if(result !=null && (Constants.TYTX_SUCCESS_CODE).equals(result.getString("ucCode"))){
				logger.info("签名系统短信发送成功。"+result.toJSONString());
				results.addMessage("签名系统短信发送成功");
			}else{
				logger.info("签名系统短信发送失败" + result.toJSONString());
				results.setType(Result.Type.FAILURE);
				results.addMessage("签名系统短信发送失败");
			}
		} catch (Exception e) {
			smsHistory.setRemark("签名系统短信发送异常!" + e.getMessage());
			results.setType(Result.Type.WARNING);
			logger.error("签名系统发送短息异常", e);
		}finally{
			smsHistory.setStatus(result.getString("ucCode"));
			smsHistory.setResponseContent(result.toJSONString());
			iCreditSmsHistoryService.save(smsHistory);
		}
		return results;
	}
	
	/**
	 * 调用统一通信平台发送短信接口
	 * 
	 * @return
	 */
	public JSONObject sendMsg(SendMsgVo sendMsgVo) {
		logger.info("=============调用统一通信平台发送短信==============");
		logger.info("请求参数==" + JSON.toJSONString(sendMsgVo));
		JSONObject jsonObj = null;
		try {
			IDataSendService dataSendService = new ShortSendService();
			//获取消息体 
			MsgBodyVo msgBodyVo = sendMsgVo.getMsgBody();
			//将消息报文转换成JSON 字符串 
			String msgBody = JSONObject.toJSONString(msgBodyVo);
			//将用户信息转为json格式
			JSONObject fastjson = new JSONObject();
			fastjson.put("custInfo", sendMsgVo.getuInfo());
			//调用统一通讯接口 
			String result = dataSendService.sendMsg(sendMsgVo.getSysUID(), sendMsgVo.getEmpId(), fastjson.toJSONString(),
					sendMsgVo.getPolicy(), sendMsgVo.getChannel(), sendMsgVo.getTemplateId(), sendMsgVo.getSpecialId(),
					sendMsgVo.getMtype(), msgBody, sendMsgVo.getDelaySendTime(), sendMsgVo.getLateSendTime(), null,
					sendMsgVo.getEncoding());
			jsonObj = JSONObject.parseObject(result);
			return jsonObj;
		} catch (Exception e) {
			logger.error("短信发送异常.", e);
		}
		return jsonObj;
	}
	
	
}
