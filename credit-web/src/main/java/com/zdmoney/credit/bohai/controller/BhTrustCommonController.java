package com.zdmoney.credit.bohai.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.util.coreUtil.Constants;
import com.zdmoney.credit.common.util.coreUtil.JSONUtil;
import com.zdmoney.credit.common.util.coreUtil.MessageUtil;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.trustOffer.service.pub.IBhTrustCommonService;

@Controller
@RequestMapping(value = "/bhTrustCommon")
public class BhTrustCommonController extends BaseController {
	private static final Logger logger = Logger.getLogger(BhTrustCommonController.class);
    @Autowired
    private IBhTrustCommonService bhTrustCommonService;
    
    /**
     * 根据所传日期上传上个月的数据
     * @param date 格式为201801 得到的数据为 201712
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/uploadRepayStateDetail")
	public void uploadRepayStateDetail(String date, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("uploadRepayStateDetail上传还款状态文件到渤海FTP服务器接口 :接收到的参数:"+date);
    	Map<String, Object> json = new HashMap<String, Object>();
		if (Strings.isEmpty(date)) {
			logger.error("uploadRepayStateDetail上传还款状态文件到渤海FTP服务器接口:失败:缺少必要参数date");
            json = MessageUtil.returnErrorMessage("失败:缺少必要的参数date！");
            response.getWriter().write(JSONUtil.toJSON(json));
            return;
		}
		Date formatDate = Dates.parse(date, Dates.DATAFORMAT_YYYYMM);
		String dateStr = Dates.getDateTime(formatDate, Dates.DATAFORMAT_YYYYMM);
		if(!date.equals(dateStr)){
			json = new HashMap<String, Object>();
			json.put("code", Constants.DATA_ERR_CODE);
			json.put("message", "失败：上传还款状态文件到渤海失败：日期格式不对，应为yyyyMM");
			response.getWriter().write(JSONUtil.toJSON(json));
            return;
		}
		try {
			json.put("date", date);
			json =  bhTrustCommonService.createRepayStateDetail(json);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("uploadRepayStateDetail方法：失败:" + e);
			json = new HashMap<String, Object>();
			json.put("code", Constants.DATA_ERR_CODE);
			json.put("message", "失败：上传还款状态文件到渤海失败:" + e.getMessage());
		}
		response.getWriter().write(JSONUtil.toJSON(json)); 
        return;
	}
}
