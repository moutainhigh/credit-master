/**
 * Copyright (c) 2017, lings@yuminsoft.com All Rights Reserved. <br/>
 * Date:2017年6月2日下午5:10:42
 *
 */

package com.zdmoney.credit.operation.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.zdmoney.credit.common.vo.ReturnObject;
import com.zdmoney.credit.operation.service.pub.ILoanCsCloseBusinessInfoService;

/**
 * ClassName:LoanCsCloseBusinessController <br/>
 * Date: 2017年6月2日 下午5:10:42 <br/>
 * 
 * @author lings@yuminsoft.com
 */
@Controller
@RequestMapping("/csclose")
public class LoanCsCloseBusinessController {
	
	@Autowired
	ILoanCsCloseBusinessInfoService closeBusinessService;
	
	/**
	 *	填充关门营业部列表 
	 */
	@RequestMapping("/searchShutShop")
	@ResponseBody
	public String searchShutShop(HttpServletRequest req) {
		List<Map<String, Object>> list = closeBusinessService.searchShutShop();
		return JSONObject.toJSONString(list);
	}

	/**
	 *	删除或新增关店营业部 
	 */
	
	@RequestMapping(value = "/editCloseDepartment", method = RequestMethod.POST)
	@ResponseBody
	public String editCloseDepartment(HttpServletRequest req) {
		ReturnObject ro = new ReturnObject();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("allOrgType", req.getParameter("allOrgType"));
		// 关店的操作和删除关店
		Map<String, Object> resultMap = closeBusinessService
				.editCloseDepartment(map);
		ro.setSuccessMsg(resultMap);
		return JSONObject.toJSONString(ro);
	}
	
	/**
	 *	已关店营业部
	 */
	@RequestMapping("/searchShutedShop")
	@ResponseBody
	public String searchShutedShop(HttpServletRequest req) {
		List<Map<String, Object>> list = closeBusinessService.searchShutedShop();
		return JSONObject.toJSONString(list);
	}
}
