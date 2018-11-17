package com.zdmoney.credit.core.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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
import com.zdmoney.credit.common.vo.core.LoginCheckVo;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.system.service.pub.IComEmployeeService;

@Controller
@RequestMapping("/core/findLeaveEmployee")
public class FindLeaveEmployeeController extends BaseController {
	private static final Logger logger = Logger.getLogger(FindLeaveEmployeeController.class);

	@Autowired
	IComEmployeeService comEmployeeService;
	
	@RequestMapping("/findLeaveEmployee")
	@ResponseBody
	public void findLeaveEmployee(LoginCheckVo params, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		logger.info("findLeaveEmployee接口 :接收到的参数:" + params.toString());
		Map<String, Object> json = null;
		
		if (Strings.isEmpty(params.getOrgCode()) ) {
			json = MessageUtil.returnErrorMessage("失败：缺少orgCode");
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write(JSONUtil.toJSON(json));
			return;
		}
		try {
			List<Map<String, String>> list = comEmployeeService.findLeaveEmployeeList(params.getOrgCode());
			json = new HashMap<String, Object>();
			json.put("employeeList", list);
			json.put("resCode", Constants.SUCCESS_CODE);
			json.put("resMsg", "正常");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("findLeaveEmployee接口：失败：" + e.getMessage(),e);
			json = new HashMap<String, Object>();
			json.put("resCode", Constants.DATA_ERR_CODE);
			json.put("resMsg", "失败：查询失败");
		}
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(JSONUtil.toJSON(json));
		return;
	}
}
