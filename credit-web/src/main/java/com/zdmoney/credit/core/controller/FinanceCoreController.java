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
import com.zdmoney.credit.common.vo.core.FinanceVo;
import com.zdmoney.credit.core.service.pub.IFinanceCoreService;
import com.zdmoney.credit.framework.controller.BaseController;


/**
 * 外部系统财务类相关接口
 * @author 00232949
 *
 */
@Controller
@RequestMapping("/core/financeCore")
public class FinanceCoreController extends BaseController {
	private static final Logger logger = Logger.getLogger(FinanceCoreController.class);
	
	@Autowired
	IFinanceCoreService financeCoreService;
	
	/**
	 * 放款推送接口 (征审系统发送放款请求到财务核心系统，进行实时放款.)
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	@RequestMapping("/grantLoan")
	@ResponseBody
	public void grantLoan(FinanceVo params,HttpServletRequest request, HttpServletResponse response) throws IOException  {
		logger.info("grantLoan放款推送接口 :接收到的参数:"+params.toString());
		
		Map<String, Object> json = new HashMap<String, Object>();
        if ((Strings.isEmpty(params.getUserCode())) || (Strings.isEmpty(params.getIds())))    //判断是否缺少必要参数; userCode为放款人代码
        {
        	logger.error("grantLoan放款推送接口 :失败:缺少必要参数userCode或ids");
            json = MessageUtil.returnErrorMessage("失败:缺少必要参数userCode或ids");
            response.getWriter().write(JSONUtil.toJSON(json));
            return;
        }

        try {
        	json = financeCoreService.grantLoan(params);
		} catch (Exception e) {
			logger.error("grantLoan放款推送接口：失败:"+e);
			
			json = new HashMap<String, Object>();
			json.put("code",Constants.DATA_ERR_CODE);
			json.put("message","放款失败");
		}
        response.getWriter().write(JSONUtil.toJSON(json)); 
        return;
	}
	
	/**
     * 放款撤销接口
     * @param params 入参
     * @return 返回放款撤销是否成功的相关信息集合
	 * @throws IOException 
     */
	@RequestMapping("/rollbackGrantLoan")
	@ResponseBody
    public void rollbackGrantLoan(FinanceVo params,HttpServletRequest request, HttpServletResponse response ) throws IOException{
		logger.info("rollbackGrantLoan接口 :接收到的参数:" + params.toString());
		
		Map<String, Object> json = null;
        if ((Strings.isEmpty(params.getUserCode())) || (Strings.isEmpty(params.getLoanId())) ) //判断是否缺少必要参数
        {
        	logger.error("rollbackGrantLoan放款撤销接口 :失败:缺少必要参数userCode或loanId");
            json = MessageUtil.returnErrorMessage("失败:缺少必要参数userCode或loanId");
            response.getWriter().write(JSONUtil.toJSON(json));
            return;
        }
        
        try {
        	json = financeCoreService.rollbackGrantLoan(params);
		} catch (Exception e) {
			logger.error("rollbackGrantLoan放款撤销接口：失败:"+e);
			
			json = new HashMap<String, Object>();
			json.put("code",Constants.DATA_ERR_CODE);
			json.put("message","放款撤销失败");
		}
        response.getWriter().write(JSONUtil.toJSON(json)); 
        return;
    }
}
