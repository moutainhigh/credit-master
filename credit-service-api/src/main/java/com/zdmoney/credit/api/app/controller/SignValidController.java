package com.zdmoney.credit.api.app.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zdmoney.credit.api.app.vo.SignValidateCodeVo;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.json.FastJsonUtil;
import com.zdmoney.credit.common.vo.AttachmentResponseInfo;
import com.zdmoney.credit.common.vo.Result;
import com.zdmoney.credit.tytx.TytxService;

@Controller
@RequestMapping(value="/signValid")
public class SignValidController {

	protected static Log logger = LogFactory.getLog(SignValidController.class);

	@Autowired
	TytxService tytxService;
	/**
	 * 发送手机验证码接口
	 * 
	 * @param request
	 * @param response
	 * @param sendMobileCodeVo
	 * @return
	 */
	@RequestMapping(value = "/sendValidCode")
	@ResponseBody
	public String sendMobileCode(SignValidateCodeVo signValidateCodeVo, HttpServletRequest request,
			HttpServletResponse response) {
		AttachmentResponseInfo<Map> attachmentResponseInfo = null;
		logger.info("签名系统调用验证码短信发送接口");
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("type", "");
			paramMap.put("mobile", signValidateCodeVo.getMobile());
			paramMap.put("employeeId", "");
			paramMap.put("content", signValidateCodeVo.getContent());
			Result<String> result = tytxService.signValidSendMsg(paramMap);
			if (Result.Type.SUCCESS == result.getType()) {
				attachmentResponseInfo = new AttachmentResponseInfo<Map>(ResponseEnum.SYS_SUCCESS);
			} else {
				throw new PlatformException(ResponseEnum.FULL_MSG, "发送短信异常");
			}
		} catch (PlatformException ex) {
			ex.printStackTraceExt(logger);
			attachmentResponseInfo = ex.toAttachmentResponseInfo();
		} catch (Exception ex) {
			logger.error(ex, ex);
			attachmentResponseInfo = new AttachmentResponseInfo<Map>(ResponseEnum.SYS_FAILD);
		}
		return FastJsonUtil.toJSONString(attachmentResponseInfo);
	}
}
