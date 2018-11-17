package com.zdmoney.credit.core.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.util.coreUtil.Constants;
import com.zdmoney.credit.common.util.coreUtil.JSONUtil;
import com.zdmoney.credit.common.util.coreUtil.MessageUtil;
import com.zdmoney.credit.common.vo.core.AccountVo;
import com.zdmoney.credit.core.service.pub.IAccountCoreService;
import com.zdmoney.credit.framework.controller.BaseController;

@Controller
@RequestMapping("/core/accountCore")
public class AccountCoreController extends BaseController {
	private static final Logger logger = Logger.getLogger(AccountCoreController.class);
	
	@Autowired
	private IAccountCoreService accountCoreService;
	
	/**
	 * 绑卡验证接口 (征审系统发送将卡号信息发送到财务核心系统进项验证，返回账号信息)
	 * @param params
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/createYYAcount")
	@ResponseBody
	public void createYYAcount(AccountVo params,HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.info("createYYAcount接口 :接收到的参数:"+params.toString());
		Map<String, Object> json = null;
		
		if (Strings.isEmpty(params.getBankCode()) || Strings.isEmpty(params.getBranchBankCode()) || Strings.isEmpty(params.getBankDicType()) 
			|| Strings.isEmpty(params.getAccount()) || Strings.isEmpty(params.getMphone()) || Strings.isEmpty(params.getIdnum()) 
			|| Strings.isEmpty(params.getSex()) || Strings.isEmpty(params.getName()) || Strings.isEmpty(params.getUserCode()) 
			|| Strings.isEmpty(params.getRemark())) {
			
			json = MessageUtil.returnErrorMessage("失败：缺少绑卡必要参数");
			response.getWriter().write(JSONUtil.toJSON(json));
			return;
		}
		
		String remark = params.getRemark();
		if ("true".equals(remark)) {
			json = MessageUtil.returnHandleSuccessMessage();
	    } else if ("false".equals(remark)) {
	    	try {
	    		json = accountCoreService.createYYAcount(params);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("createYYAcount接口：失败："+e);
				
				json = new HashMap<String, Object>();
				json.put("code",Constants.DATA_ERR_CODE);
				json.put("message","失败：绑卡验证失败");
			}
	    } else {
	        json = MessageUtil.returnErrorMessage("失败:没有输入备注信息！");
	    }
		
		response.getWriter().write(JSONUtil.toJSON(json));
		return;
	}
	
	
}
