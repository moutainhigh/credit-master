package com.zdmoney.credit.core.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.util.coreUtil.Constants;
import com.zdmoney.credit.common.util.coreUtil.JSONUtil;
import com.zdmoney.credit.common.util.coreUtil.MessageUtil;
import com.zdmoney.credit.common.vo.core.LoginCheckVo;
import com.zdmoney.credit.core.service.pub.ILoginCheckCoreService;
import com.zdmoney.credit.framework.controller.BaseController;

@Controller
@RequestMapping("/core/loginCheckCore")
public class LoginCheckCoreController extends BaseController {
	private static final Logger logger = Logger.getLogger(LoginCheckCoreController.class);

	@Autowired
	private ILoginCheckCoreService loginCheckCoreService;

	/**
	 * 登录校验接口（征信查询App端登陆）
	 * 
	 * @param params
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/loginCheck")
	@ResponseBody
	public void loginCheck(LoginCheckVo params, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		logger.info("loginCheck接口 :接收到的参数:" + params.toString());
		Map<String, Object> json = null;
		if(Strings.isEmpty(params.getNewOrOld())){
			params.setNewOrOld("o");//null则是老系统
		}
		if (Strings.isEmpty(params.getUserCode()) || Strings.isEmpty(params.getPassWord()) || Strings.isEmpty(params.getNewOrOld())) {

			json = MessageUtil.returnErrorMessage("失败：缺少登录必要参数");
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write(JSONUtil.toJSON(json));
			return;
		}
		try {
			json = loginCheckCoreService.loginCkeck(params, "1");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("loginCheck接口：失败：" + e);

			json = new HashMap<String, Object>();
			json.put("resCode", Constants.DATA_ERR_CODE);

			json.put("resMsg", "失败：登录验证失败");
		}
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(JSONUtil.toJSON(json));
		return;
	}

	/**
	 * 登录校验接口（录单App端登陆）
	 * 
	 * @param params
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/loginCheckByLoan")
	@ResponseBody
	public void loginCheckByLoan(LoginCheckVo params, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		logger.info("loginCheck接口 :接收到的参数:" + params.toString());
		Map<String, Object> json = null;
		if(Strings.isEmpty(params.getNewOrOld())){
			params.setNewOrOld("o");//null则是老系统
		}

		if (Strings.isEmpty(params.getUserCode()) || Strings.isEmpty(params.getPassWord())|| Strings.isEmpty(params.getNewOrOld())) {

			json = MessageUtil.returnErrorMessage("失败：缺少登录必要参数");
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write(JSONUtil.toJSON(json));
			return;
		}
		try {
			json = loginCheckCoreService.loginCkeck(params, "2");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("loginCheck接口：失败：" + e);

			json = new HashMap<String, Object>();
			json.put("resCode", Constants.DATA_ERR_CODE);

			json.put("resMsg", "失败：登录验证失败");
		}
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(JSONUtil.toJSON(json));
		return;
	}

}
