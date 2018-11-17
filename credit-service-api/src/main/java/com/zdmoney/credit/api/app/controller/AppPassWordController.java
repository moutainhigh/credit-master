package com.zdmoney.credit.api.app.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zdmoney.credit.api.app.vo.ModifyLoginPasswordVo;
import com.zdmoney.credit.api.app.vo.SendMobileCodeVo;
import com.zdmoney.credit.api.app.vo.ValidMobileCodeVo;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.json.FastJsonUtil;
import com.zdmoney.credit.common.redis.RedisClientUtil;
import com.zdmoney.credit.common.regex.RegexUtil;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.vo.AttachmentResponseInfo;
import com.zdmoney.credit.common.vo.ResponseInfo;
import com.zdmoney.credit.common.vo.Result;
import com.zdmoney.credit.system.domain.ComEmployee;
import com.zdmoney.credit.system.service.pub.IComEmployeeService;
import com.zdmoney.credit.tytx.TytxService;

@Controller
@RequestMapping(value = "/app/passWord")
public class AppPassWordController {

	protected static Log logger = LogFactory.getLog(AppPassWordController.class);

	@Autowired
	IComEmployeeService comEmployeeServiceImpl;

	@Autowired
	RedisClientUtil redisClientUtil;

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
	@RequestMapping(value = "/sendMobileCode")
	@ResponseBody
	public String sendMobileCode(SendMobileCodeVo sendMobileCodeVo, HttpServletRequest request,
			HttpServletResponse response) {
		AttachmentResponseInfo<Map> attachmentResponseInfo = null;
		try {
			/** 1.工号 2.手机号 **/
			String dataType = Strings.parseString(sendMobileCodeVo.getDataType());
			String mobile = "";
			switch (dataType) {
			case "1":
				/** 工号换手机号 **/
				String userCode = Strings.parseString(sendMobileCodeVo.getDataInfo());
				ComEmployee comEmployee = comEmployeeServiceImpl.findByUserCode(userCode);
				if (comEmployee == null) {
					throw new PlatformException(ResponseEnum.FULL_MSG, "工号不存在" + userCode);
				}
				mobile = Strings.parseString(comEmployee.getMobile());
				break;
			case "2":
				mobile = Strings.parseString(sendMobileCodeVo.getDataInfo());
				break;
			default:
				throw new PlatformException(ResponseEnum.FULL_MSG, "数据类型无效" + dataType);
			}

			if (!RegexUtil.isMobile(mobile)) {
				/** 手机号格式有误 **/
				throw new PlatformException(ResponseEnum.FULL_MSG, "手机号格式有误" + mobile);
			}

			/** 随机生成4位数字 **/
			Random random = new Random();
			int num = (random.nextInt(9999 - 1000 + 1) + 1000);
			logger.info("手机 验证码：" + num);
			/** 将4位数字存储到Redis服务端 **/
			String redisKey = UUID.randomUUID().toString();
			String redisValue = Strings.parseString(num);
			/** 保存到Redis(60秒验证码自动失效) **/
			redisClientUtil.setValue(redisKey, (60 * 5), redisValue);

			/** 调用统一通讯平台发送短信 **/
			String smsContent = "手机验证码：" + redisValue;
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("type", "");
			paramMap.put("mobile", mobile);
			paramMap.put("employeeId", "");
			paramMap.put("content", smsContent);
			String mType = "10000002";
			Result<String> result = tytxService.sendMsg(paramMap,mType);
			if (Result.Type.SUCCESS == result.getType()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("token", redisKey);
				attachmentResponseInfo = new AttachmentResponseInfo<Map>(ResponseEnum.SYS_SUCCESS);
				attachmentResponseInfo.setAttachment(map);
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

	/**
	 * 校验手机验证码接口
	 * 
	 * @param request
	 * @param response
	 * @param validMobileCodeVo
	 * @return
	 */
	@RequestMapping(value = "/validMobileCode")
	@ResponseBody
	public String validMobileCode(ValidMobileCodeVo validMobileCodeVo, HttpServletRequest request,
			HttpServletResponse response) {
		ResponseInfo responseInfo = null;
		try {
			/** 手机验证码Token **/
			String token = validMobileCodeVo.getToken();
			/** 从Redis 服务端获取手机验证码 **/
			String redisMobileCode = "";
			/** 拿手机验证码参数对比Redis中验证码 **/
			String code = Strings.parseString(validMobileCodeVo.getCode());
			/** 从Redis中获取手机验证码 **/
			redisMobileCode = redisClientUtil.getValue(token);
			if (Strings.isEmpty(redisMobileCode)) {
				throw new PlatformException(ResponseEnum.FULL_MSG, "验证码已失效或不存在");
			}
			if (redisMobileCode.equals(code)) {
				responseInfo = new ResponseInfo(ResponseEnum.SYS_SUCCESS);
			} else {
				throw new PlatformException(ResponseEnum.FULL_MSG, "验证码错误");
			}
		} catch (PlatformException ex) {
			ex.printStackTraceExt(logger);
			responseInfo = ex.toResponseInfo();
		} catch (Exception ex) {
			logger.error(ex, ex);
			responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
		}
		return FastJsonUtil.toJSONString(responseInfo);
	}

	/**
	 * 修改登陆密码接口
	 * 
	 * @param request
	 * @param response
	 * @param modifyLoginPasswordVo
	 * @return
	 */
	@RequestMapping(value = "/modifyLoginPassword")
	@ResponseBody
	public String modifyLoginPassword(ModifyLoginPasswordVo modifyLoginPasswordVo, HttpServletRequest request,
			HttpServletResponse response) {
		ResponseInfo responseInfo = null;
		try {
			String userCode = modifyLoginPasswordVo.getUserCode();
			String newPassWord = modifyLoginPasswordVo.getPassword();
			String validType = Strings.parseString(modifyLoginPasswordVo.getValidType());

			ComEmployee comEmployee = comEmployeeServiceImpl.findByUserCode(userCode);
			if (comEmployee == null) {
				throw new PlatformException(ResponseEnum.FULL_MSG, userCode + "工号不存在");
			}
			if (comEmployee.getPassword().equals(newPassWord)) {
				throw new PlatformException(ResponseEnum.FULL_MSG, "当前密码与原密码一致，请重新输入密码");
			}

			switch (validType) {
			case "1":
				/** 手机验证码 **/
				String mobileCode = Strings.parseString(modifyLoginPasswordVo.getValidData());
				String token = Strings.parseString(modifyLoginPasswordVo.getToken());
				if (Strings.isEmpty(token)) {
					throw new PlatformException(ResponseEnum.FULL_MSG, "缺少验证码唯一标识参数");
				}
				/** 从Redis中获取手机验证码 **/
				String redisMobileCode = redisClientUtil.getValue(token);
				if (Strings.isEmpty(redisMobileCode)) {
					throw new PlatformException(ResponseEnum.FULL_MSG, "验证码已失效或不存在");
				}
				if (redisMobileCode.equals(mobileCode)) {

				} else {
					throw new PlatformException(ResponseEnum.FULL_MSG, "验证码错误");
				}
				break;
			case "2":
				/** 原密码 **/
				String oldPassword = modifyLoginPasswordVo.getValidData();
				if (!comEmployee.getPassword().equals(oldPassword)) {
					throw new PlatformException(ResponseEnum.FULL_MSG, "原密码有误");
				}
				break;
			default:
				throw new PlatformException(ResponseEnum.FULL_MSG, "验证类型码错误");
			}
			if (Strings.isEmpty(newPassWord)) {
				throw new PlatformException(ResponseEnum.FULL_MSG, "缺少新密码参数");
			}
			/** 保存新密码 **/
			comEmployee.setPassword(newPassWord);
			/** 更新成 非强制修改密码 **/
			comEmployee.setIsFirst("f");
			comEmployeeServiceImpl.saveOrUpdate(comEmployee, true,true, false);
			responseInfo = new ResponseInfo(ResponseEnum.SYS_SUCCESS);
		} catch (PlatformException ex) {
			ex.printStackTraceExt(logger);
			responseInfo = ex.toResponseInfo();
		} catch (Exception ex) {
			logger.error(ex, ex);
			responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
		}
		return FastJsonUtil.toJSONString(responseInfo);
	}

}
