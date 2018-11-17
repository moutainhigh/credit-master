package com.zdmoney.credit.data.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.util.UrlUtil;
import com.zdmoney.credit.framework.controller.BaseController;

/**
 * 核心页面调用算话controller类
 * @author zhouwq
 * @version 1.0
 */
@Controller
@RequestMapping("/data/cpcs")
public class DataCpcsController extends BaseController {

    protected static Log logger = LogFactory.getLog(DataCpcsController.class);
    @Value("${zhengxin.url.prefix}")
  	private String url;
  	
    //@Value("${zhengxin.exportUrl.prefix}")
  	private String exportUrl;
    /**
     * 跳转主页面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/jumpPage")
    public ModelAndView jumpPage(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("data/dataCpcs");//1
        return mav;
    }
    
    @RequestMapping("/export")
    @ResponseBody
    public String export(HttpServletRequest request, HttpServletResponse response) {
    	logger.info("导出征信报文==================开始");
    	Map<String,String> mapRes = new HashMap<String,String>();
    	mapRes.put("url", "http://localhost:8888/suanhua/export");
        String reqStr = JSON.toJSONString(mapRes);  
    	logger.info("导出征信报文==================结束");
        return reqStr;
    }
    
    @RequestMapping("/upload")
    @ResponseBody
    public String upload(HttpServletRequest request, HttpServletResponse response) {
    	logger.info("=============访问征信===============开始");
    	String countDateStart = request.getParameter("countDateStart");
    	String countDateEnd = request.getParameter("countDateEnd");
    	String state = request.getParameter("state");
    	String monthDate = request.getParameter("monthDate");
    	logger.info("核心页面参数===>"+countDateStart+"============="+countDateEnd+"================="+state+"==============="+monthDate);
    	User user = UserContext.getUser();
    	Map<String,String> map = null;
    	if("1".equals(state)){//数据共享
	    	map = new HashMap<String,String>();
	    	map.put("monthDate", monthDate);
	    	map.put("state", state);
    	}else if("2".equals(state)){//反欺诈
    		map = new HashMap<String,String>();
	    	map.put("countDateStart", countDateStart);
	    	map.put("countDateEnd", countDateEnd);
	    	map.put("state", state);
	    	map.put("userId", user.getId().toString());
    	}
    	String result = UrlUtil.methodPost(url, map);
		Map<String,String> resultMap = new HashMap<String,String>();
		resultMap.put("result", result);
    	logger.info("=============访问征信===============开始");
        return JSON.toJSONString(resultMap);
    }
}
