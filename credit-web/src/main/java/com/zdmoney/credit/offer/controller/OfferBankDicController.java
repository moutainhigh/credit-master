package com.zdmoney.credit.offer.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.offer.domain.OfferBankDic;
import com.zdmoney.credit.offer.service.pub.IOfferBankDicService;


/**
 * 银行总行Controller
 * @author Ivan
 *
 */
@Controller
@RequestMapping("/offer/offerBankDic")
public class OfferBankDicController extends BaseController {
	
	@Autowired @Qualifier("offerBankDicServiceImpl")
	IOfferBankDicService offerBankDicServiceImpl;
	
	/**
	 * 获取总行信息
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/getBankInfo")
	@ResponseBody
	public String getBankInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
		OfferBankDic offerBankDic = new OfferBankDic();
		List<OfferBankDic> offerBankDicList = offerBankDicServiceImpl.findListByVo(offerBankDic);
		List<Map> resultList = new ArrayList<Map>();
		for (int i=0;i<offerBankDicList.size();i++) {
			Map map = new HashMap();
			offerBankDic = offerBankDicList.get(i);
			map.put("id", offerBankDic.getCode());
			map.put("text", offerBankDic.getName());
			resultList.add(map);
		}
		return JSONObject.toJSONString(resultList);
	}
	
}
