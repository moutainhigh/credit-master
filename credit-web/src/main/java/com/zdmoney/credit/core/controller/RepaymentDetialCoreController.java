package com.zdmoney.credit.core.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.util.coreUtil.Constants;
import com.zdmoney.credit.common.util.coreUtil.JSONUtil;
import com.zdmoney.credit.common.util.coreUtil.MessageUtil;
import com.zdmoney.credit.common.vo.core.RepaymentDetailParamVO;
import com.zdmoney.credit.core.service.pub.IRepaymentDetialCoreService;
import com.zdmoney.credit.framework.controller.BaseController;

/**
 * 还款计划接口Web层
 * @author 00235304
 *
 */
@Controller
@RequestMapping("/core/repaymentDetialCore")
public class RepaymentDetialCoreController extends BaseController {

	private static final Logger logger = Logger.getLogger(RepaymentDetialCoreController.class);
	
	@Autowired
	IRepaymentDetialCoreService repaymentDetialCoreService;
	
	/**
	 * 还款计划表接口：合同签约时调用账务核心系统获得还款计划
	 * @param request 请求信息对象
	 * @param response 响应信息对象
	 * @param paramVo 请求参数VO
	 * @throws IOException
	 */
	@RequestMapping("/queryRepaymentDetail")
	@ResponseBody
	public void queryRepaymentDetail(HttpServletRequest request,HttpServletResponse response,RepaymentDetailParamVO paramVo) throws IOException{
		logger.info("还款计划表接口接收的入参："+paramVo.toString());
		Map<String,Object> json=new HashMap<String,Object>();
		Map<String,Object> paramsMap=new HashMap<String,Object>();
		
		String userCode=paramVo.getUserCode();
		Long loanId=paramVo.getLoanId();
		Long max=paramVo.getMax();
		Long offset=paramVo.getOffset();
		if( loanId==null && StringUtils.isBlank(userCode)){
			json=MessageUtil.returnErrorMessage("失败：缺少必要参数！");
			response.getWriter().write(JSONUtil.toJSON(json));
			return;
		}
		paramVo.setMax(Math.min((max!=null && max.intValue()>0 ? max:10),100));
		paramVo.setOffset(Math.min((offset!=null && offset.intValue()>=0 ? offset:0),100));
		
		Pager pager = new Pager();
		pager.setRows(paramVo.getMax().intValue());
		pager.setPage(paramVo.getOffset().intValue());
		pager.setSidx("ID");
		pager.setSort("DESC");
		paramsMap.put("pager", pager);
		paramsMap.put("loanId", loanId);
		
		json=repaymentDetialCoreService.queryRepaymentDetail(paramsMap);
		response.getWriter().write(JSONUtil.toJSON(json));
		return;
	}
	
	/**
	 * 还款明细表接口 (征审系统请求账务核心系统返回客户还款明细表)
	 * @param request 请求信息对象
	 * @param response 响应信息对象
	 * @param paramVo 请求参数VO
	 * @throws IOException
	 */
	@RequestMapping("/queryflow")
	@ResponseBody
	public void queryflow(HttpServletRequest request,HttpServletResponse response,RepaymentDetailParamVO paramVo) throws IOException{
		logger.info("还款明细表接口接收的入参："+paramVo.toString());
		Map<String,Object> json = null;
		Map<String,Object> paramMap=new HashMap<String,Object>();
		
		if(Strings.isEmpty(paramVo.getLoanId()) || Strings.isEmpty(paramVo.getUserCode())){
			json=MessageUtil.returnErrorMessage("失败：缺少必要参数！");
			response.getWriter().write(JSONUtil.toJSON(json));
			return;
		}
		Long max = paramVo.getMax();
		Long offset = paramVo.getOffset();
		paramVo.setMax(Math.min((max!=null && max.intValue()>0 ? max:10),100));
		paramVo.setOffset(Math.min((offset!=null && offset.intValue()>=0 ? offset:0),100));
		
		Pager pager = new Pager();
		pager.setRows(paramVo.getMax().intValue());
		pager.setPage(paramVo.getOffset().intValue());
		pager.setSidx("TRADE_NO");
		pager.setSort(Pager.DIRECTION_ASC);
		paramMap.put("pager", pager);
		paramMap.put("loanId", paramVo.getLoanId());
		
		try {
			json=repaymentDetialCoreService.queryFlow(paramMap);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("queryflow方法：失败:"+e.getMessage());
			
			json = new HashMap<String, Object>();
			json.put("code",Constants.DATA_ERR_CODE);
			json.put("message","失败：未知错误");
		}
		response.getWriter().write(JSONUtil.toJSON(json));
		return;
	}
	
	/**
	 * 查看还款汇总信息接口
	 * @param params
	 * @param request
	 * @param response
	 */
	@RequestMapping("/queryRepaymentSummary")
	public void queryRepaymentSummary(HttpServletRequest request,HttpServletResponse response,RepaymentDetailParamVO paramVo) throws Exception {
		logger.info("queryRepaymentSummary接口 :接收到的参数:"+paramVo.toString());
	    
	    Map<String, Object> json = null;
	    
	    if (Strings.isEmpty(paramVo.getLoanId()) || Strings.isEmpty(paramVo.getUserCode())) {
	    	logger.error("queryRepaymentSummary:失败:缺少必要参数loanId或者userCode");
	    	json = MessageUtil.returnErrorMessage("失败:缺少必要参数");
	    	response.getWriter().write(JSONUtil.toJSON(json));
	    	return;
	    }
	    
	    try {
	    	json = repaymentDetialCoreService.queryRepaymentSummary(paramVo);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("queryRepaymentSummary方法：失败:"+e.getMessage());
			
			json = new HashMap<String, Object>();
			json.put("code",Constants.DATA_ERR_CODE);
			json.put("message","失败：查看还款汇总信息失败");
		}
	    
	    response.getWriter().write(JSONUtil.toJSON(json)); 
	    return;
	}
	
	
	/**
	 * 查看还款详细信息接口
	 * @param params
	 * @param request
	 * @param response
	 */
	@RequestMapping("/queryRepaymentDetailAll")
	public void queryRepaymentDetailAll(HttpServletRequest request,HttpServletResponse response,RepaymentDetailParamVO paramVo) throws Exception {
		logger.info("queryRepaymentDetailAll接口 :接收到的参数:"+paramVo.toString());
	    
	    Map<String, Object> json = null;
	    
	    if (Strings.isEmpty(paramVo.getLoanId()) || Strings.isEmpty(paramVo.getUserCode())) {
	    	logger.error("queryRepaymentDetailAll:失败:缺少必要参数loanId或者userCode");
	    	json = MessageUtil.returnErrorMessage("失败:缺少必要参数");
	    	response.getWriter().write(JSONUtil.toJSON(json));
	    	return;
	    }
	    
	    try {
			Map<String,Object> paramsMap=new HashMap<String,Object>();
	    	
			Pager pager = new Pager();
			pager.setRows(Integer.MAX_VALUE);
			pager.setPage(1);
			pager.setSidx("ID");
			pager.setSort(Pager.DIRECTION_ASC);
			paramsMap.put("pager", pager);
			paramsMap.put("loanId", paramVo.getLoanId());
	    	json = repaymentDetialCoreService.queryRepaymentDetailAll(paramsMap);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("queryRepaymentDetailAll方法：失败:"+e.getMessage());
			
			json = new HashMap<String, Object>();
			json.put("code",Constants.DATA_ERR_CODE);
			json.put("message","失败：未知错误");
		}
	    
	    response.getWriter().write(JSONUtil.toJSON(json)); 
	    return;
	}
}
