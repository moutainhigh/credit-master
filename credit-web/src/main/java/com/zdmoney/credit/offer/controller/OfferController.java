package com.zdmoney.credit.offer.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.zdmoney.credit.common.constant.OfferStateEnum;
import com.zdmoney.credit.common.constant.SysActionLogTypeEnum;
import com.zdmoney.credit.common.constant.tpp.TppPaySysNoEnum;
import com.zdmoney.credit.common.constant.tpp.TppRealPaySysNoEnum;
import com.zdmoney.credit.common.exception.OfferBackException;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.util.BeanUtils;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.ExcelExportUtil;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.util.TPPHelper;
import com.zdmoney.credit.common.vo.AttachmentResponseInfo;
import com.zdmoney.credit.common.vo.ResponseInfo;
import com.zdmoney.credit.common.vo.TppCallBackData;
import com.zdmoney.credit.common.vo.TppCallBackData20;
import com.zdmoney.credit.common.vo.core.OfferParamsVo;
import com.zdmoney.credit.fee.service.pub.ILoanFeeTransactionService;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.ILoanBaseService;
import com.zdmoney.credit.loan.service.pub.ILoanTransferInfoService;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import com.zdmoney.credit.offer.domain.OfferChannel;
import com.zdmoney.credit.offer.domain.OfferInfo;
import com.zdmoney.credit.offer.domain.OfferTransaction;
import com.zdmoney.credit.offer.service.pub.IOfferChannelService;
import com.zdmoney.credit.offer.service.pub.IOfferCreateService;
import com.zdmoney.credit.offer.service.pub.IOfferInfoService;
import com.zdmoney.credit.offer.service.pub.IOfferTransactionService;
import com.zdmoney.credit.offer.vo.OfferRealTimeDeduct;
import com.zdmoney.credit.system.domain.ComMessageIn;
import com.zdmoney.credit.system.domain.SysDictionary;
import com.zdmoney.credit.system.service.pub.IComMessageInService;
import com.zdmoney.credit.system.service.pub.IComOrganizationService;
import com.zdmoney.credit.system.service.pub.IPersonInfoService;
import com.zdmoney.credit.system.service.pub.ISysDictionaryService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;

/**
 * 报回盘针对tpp1.0的交互Controller
 * 
 * @author 00232949
 *
 */
@Controller
@RequestMapping("/offer")
public class OfferController extends BaseController {
	private static final Logger logger = Logger.getLogger(OfferController.class);

	private static final String CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	private static final String DIRECT_DEDUCT_ROLE ="信贷-实时划扣-指定划扣通道";
	
	@Autowired
	ISysParamDefineService sysParamDefineService;
	@Autowired
	IComMessageInService comMessageInService;
	@Autowired
	IOfferTransactionService offerTransactionService;

	@Autowired
	IVLoanInfoService vLoanInfoService;
	
	@Autowired
	ILoanBaseService loanBaseService;

	@Autowired
	IOfferInfoService offerInfoService;
	@Autowired
	IOfferCreateService offerCreateService;
	
	@Autowired
	IOfferChannelService offerChannelService;

	@Autowired
	@Qualifier("personInfoServiceImpl")
	IPersonInfoService personInfoServiceImpl;

	@Autowired
	@Qualifier("loanFeeTransactionServiceImpl")
	ILoanFeeTransactionService loanFeeTransactionServiceImpl;

	@Autowired
	@Qualifier("sysDictionaryServiceImpl")
	ISysDictionaryService sysDictionaryServiceImpl;

	@Autowired
	ILoanTransferInfoService loanTransferInfoService;
	
	@Autowired
	IComOrganizationService comOrganizationService;
	/**
	 * tpp1.0的回盘接收
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/tppback/realtimeDeductBack")
	public void realtimeDeductBack(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");
		// 验证合法性
		if (!checkReqValidity(request)) {
			response.getWriter().write("请求地址不符合要求!");
			return;
		}

		// 获取request中的请求xml
		String[] tppResponseResult = readXmlByRequest(request, "1.0");
		// 判断返回解析是否正确
		if (tppResponseResult == null) {
			logger.error("TppCall--tpp回盘推送...解析失败或者没有响应数据");
			response.getWriter().write("没有响应数据!");
			return;
		}
		// 处理开始
		// 解析xml转换成对象
		List<TppCallBackData> callBackDataList;
		try {
			callBackDataList = TPPHelper.parseRealtimeDeductBackData(tppResponseResult);
		} catch (Exception e) {
			logger.error("TppCall--tpp回盘推送...解析数据异常", e);
			response.getWriter().write("ERROR");
			return;
		}
		response.getWriter().write("SUCCESS"); // 这里先给tpp成功回执，防止处理时间过长，链接超时

		// 解析TPP（结算平台）系统回调的划扣结果信息
		logger.info("TppCall--tpp回盘推送...开始处理数据");
		// 处理TPP系统划扣的结果信息
		int success = 0;
		long s = System.currentTimeMillis();
		for (TppCallBackData objects : callBackDataList) {
			try {
				Boolean isSucc = offerTransactionService.executeOfferBack(objects);
				if (isSucc) {// 记录成功条数
					success++;
				}
			} catch (OfferBackException e) {
				logger.error("TppCall--tpp回盘推送...处理数据异常！ orderNo:" + objects.getOrderNo() + e.getMessage());
			} catch (Exception e) {
				logger.error("TppCall--tpp回盘推送...处理数据异常！ orderNo:" + objects.getOrderNo(), e);
			}
		}
		long e = System.currentTimeMillis();
		logger.info("[" + Dates.getDateTime() + "]  处理回盘逻辑结束,处理笔数:" + callBackDataList.size() + ",共计用时:" + (e - s)
				+ "毫秒,成功:" + success + "笔,失败:" + (callBackDataList.size() - success) + "笔!");
		logger.info("TppCall--tpp回盘推送...处理数据完成");
		// response.getWriter().write("SUCCESS");

	}

	/**
	 * tpp2.0的回盘接收
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/tppback/realtimeDeductBack20")
	public void realtimeDeductBack20(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");
		// 验证合法性
		/*
		 * if(!checkReqValidity(request)){
		 * response.getWriter().write("请求地址不符合要求!"); return; }
		 */

		// 获取request中的请求xml
		String[] tppResponseResult = readXmlByRequest(request, "2.0");
		// 判断返回解析是否正确
		if (tppResponseResult == null) {
			logger.error("TppCall--tpp20回盘推送...解析失败或者没有响应数据");
			response.getWriter().write("没有响应数据!");
			return;
		}
		// 处理开始
		// 解析xml转换成对象
		List<TppCallBackData20> callBackDataList;
		try {
			callBackDataList = TPPHelper.parseRealtimeDeductBackData20(tppResponseResult);
		} catch (Exception e) {
			logger.error("TppCall--tpp回盘推送...解析数据异常", e);
			response.getWriter().write("ERROR");
			return;
		}

		// 解析TPP（结算平台）系统回调的划扣结果信息
		logger.info("TppCall--tpp回盘推送...开始处理数据");
		// 处理TPP系统划扣的结果信息
		int success = 0;
		long s = System.currentTimeMillis();
		for (TppCallBackData20 objects : callBackDataList) {
			try {
				Boolean isSucc = false;
				/** 业务流水号 **/
				String orderNo = Strings.parseString(objects.getOrderNo());
				if (orderNo.startsWith("LX_FEE_")) {
					/** 龙信小贷 服务费划扣回盘 **/
					isSucc = loanFeeTransactionServiceImpl.callBackTransaction(objects);
				} else {
					isSucc = offerTransactionService.executeOfferBack20(objects);
				}
				if (isSucc) {// 记录成功条数
					success++;
				}
			} catch (OfferBackException e) {
				logger.error("TppCall--tpp回盘推送...处理数据异常！ orderNo:" + objects.getOrderNo() + e.getMessage());
			} catch (Exception e) {
				logger.error("TppCall--tpp回盘推送...处理数据异常！ orderNo:" + objects.getOrderNo(), e);
			}
		}
		long e = System.currentTimeMillis();
		logger.info("[" + Dates.getDateTime() + "]  处理回盘逻辑结束,处理笔数:" + callBackDataList.size() + ",共计用时:" + (e - s)
				+ "毫秒,成功:" + success + "笔,失败:" + (callBackDataList.size() - success) + "笔!");
		logger.info("TppCall--tpp回盘推送...处理数据完成");
		response.getWriter().write("SUCCESS");

	}

	/**
	 * 从流中获取xml，并保存msgin，返回过滤过的xml，如果失败返回null
	 * 
	 * @param request
	 * @param tppver
	 * @return String[] 0:xml 1:msgin的id
	 */
	private String[] readXmlByRequest(HttpServletRequest request, String tppver) {

		String[] tppResponseResult = new String[2];
		try {
			logger.info("TppCall--tpp回盘推送...开始接收数据");
			String tppResponseResultXml = TPPHelper.parseResponseResult(request.getInputStream());
			// 保存原始数据xml到msgin
			ComMessageIn comMessageIn = comMessageInService.createMsgIn("offerBack", "MESSAGE_ID",
					tppResponseResultXml, "TPP", tppver);
			tppResponseResult[1] = String.valueOf(comMessageIn.getId());

			logger.info("TppCall--tpp回盘推送...处理前数据长度:" + tppResponseResultXml.length());
			tppResponseResultXml = TPPHelper.replaceXml(tppResponseResultXml);
			tppResponseResult[0] = tppResponseResultXml;
			logger.info("TppCall--tpp回盘推送...处理后数据长度:" + tppResponseResultXml.length());
		} catch (Exception e) {
			logger.error("TppCall--tpp回盘推送...接收数据异常", e);
			return null;
		}
		return tppResponseResult;
	}

	/**
	 * 检查请求合法性，签名等
	 * 
	 * @param request
	 * @return
	 */
	private boolean checkReqValidity(HttpServletRequest request) {
		String tppCallbackCheck = sysParamDefineService.getSysParamValueCache("codeHelper", "tpp_callback_check");
		if ("0".equals(tppCallbackCheck)) {// 1为需要校验，0不需要
			logger.debug("tpp回盘推送...ip校验关闭，不进行校验");
			return true;
		}
		String reqServer = TPPHelper.getIpAddr(request);
		String tppServer = sysParamDefineService.getSysParamValueCache("codeHelper", "tppServer");// CodeHelper.findAll()[0].tppServer;
		logger.debug("配置的结算平台服务器IP地址:::::::::" + tppServer);
		// 判断发起请求的地址,防止恶意请求
		if (!reqServer.equals(tppServer)) {
			logger.error("tpp回盘推送...请求地址有误,请求服务器IP:" + reqServer + ",配置服务器IP:" + tppServer);
			return false;
		}
		return true;
	}

	/**
	 * 委托代扣报盘
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/offerInfo/offerList")
	public String offerListDefault(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		Date offerDate = new Date();
		modelMap.addAttribute("offerDate", offerDate);
		String[] stateArr = new String[] { OfferStateEnum.未报盘.getValue(), OfferStateEnum.已报盘.getValue(),
				OfferStateEnum.已回盘.getValue(), OfferStateEnum.已关闭.getValue() };
		modelMap.addAttribute("states", stateArr);
		modelMap.addAttribute("isEdits", new String[] { "是", "否" });
		modelMap.addAttribute("orgName", UserContext.getUser().getOrgName());
		modelMap.addAttribute("paySysNos", TppPaySysNoEnum.values());
		/**实时划扣划扣通道**/
//		modelMap.addAttribute("paySysNoAdds", TppRealPaySysNoEnum.values());
		/** 查询债权去向数据源 **/
		SysDictionary sysDictionary = new SysDictionary();
		sysDictionary.setCodeType("loan_loanbelongs");
		List<SysDictionary> list = sysDictionaryServiceImpl.findDictionaryListByVo(sysDictionary);
		modelMap.addAttribute("loanBelongs", list);

		//判断是否走支付路由
		User user = UserContext.getUser();
		boolean flag = this.isShowPayChannel(user);
		modelMap.addAttribute("isShowPayChannel", flag);//true 指定过该角色，显示划扣通道，走通道；false 没有该角色，不显示划扣通道 走路由 999
		return "/offer/offerList";
	}

	
	/**
	 * 根据登录的用户判断是否显示划扣通道
	 * @param user
	 * @return
	 */
	public boolean isShowPayChannel(User user){
		if(user == null){
			return false;
		}
		for(Map.Entry<String, Object> entry : user.getRoleMap().entrySet()) {
			if(DIRECT_DEDUCT_ROLE.equals(entry.getValue())){
				return true;//true 指定过该角色，显示划扣通道，走通道；false 没有该角色，不显示划扣通道 走路由 999
			}
		}
		Long orgId = user.getOrgId();
		Integer count = comOrganizationService.getPaymentRouteDeptCount(orgId);
		if(count > 0){
			return false;//在营业部  不显示划扣通道   走路由 999
		}
		return true;//不在营业部  显示划扣通道   走通道
	}
	
	/**
	 * 委托代扣报盘查询
	 * 
	 * @param offerInfo
	 * @param rows
	 * @param page
	 * @param request
	 * @param response
	 * @return
	 * @throws ParseException
	 */
	@ResponseBody
	@RequestMapping("/offerInfo/search")
	public String offerSearch(OfferInfo offerInfo, int rows, int page, HttpServletRequest request,
			HttpServletResponse response) throws ParseException {

		User user = UserContext.getUser();
		Map<String, Object> params = new HashMap<String, Object>();
		// 是否修改
		String isEdit = request.getParameter("isEdit");
		if (Strings.isNotEmpty(isEdit)) {
			params.put("isEdit", isEdit);
		}
		// 客户姓名
		String name = request.getParameter("name");
		if (Strings.isNotEmpty(name)) {
			params.put("name", name);
		}
		// 报盘日期，默认为系统日期
		params.put("offerDate", Dates.getDateTime(new Date(), "yyyy-MM-dd"));
		String offerDate = request.getParameter("offerDate");
		if (Strings.isNotEmpty(offerDate)) {
			params.put("offerDate", offerDate);
		}
		// 证件号码
		String idNum = request.getParameter("idNum");
		if (Strings.isNotEmpty(idNum)) {
			params.put("idNum", idNum);
		}
		// 机构区域码
		if ("催收员".equals(user.getEmployeeType()) && Strings.isEmpty(idNum)) {
			params.put("teller", user.getOrgCode());
		} else {
			params.put("orgCode", user.getOrgCode());
		}
		// 报盘状态
		String state = request.getParameter("state");
		if (Strings.isNotEmpty(state)) {
			params.put("state", state);
		}
		// 合同编号
		String contractNum = request.getParameter("contractNum");
		if (Strings.isNotEmpty(contractNum)) {
			params.put("contractNum", contractNum);
		}
		String paySysNo = request.getParameter("paySysNo");
		if (Strings.isNotEmpty(paySysNo)) {
			params.put("paySysNo", paySysNo);
		}
		/** 开户银行 **/
		String bankCode = Strings.defaultValue(request.getParameter("bankCode"), "0", "");
		if (Strings.isNotEmpty(bankCode)) {
			params.put("bankCode", bankCode);
		}
		/** 债权去向 **/
		String loanBelong = Strings.parseString(request.getParameter("loanBelong"));
		if (Strings.isNotEmpty(loanBelong)) {
			params.put("loanBelong", loanBelong);
		}

		Pager pager = new Pager();
		pager.setRows(rows);
		pager.setPage(page);
		pager.setSidx("ID");
		pager.setSort("DESC");
		offerInfo.setPager(pager);
		params.put("pager", pager);
		pager = offerTransactionService.searchOfferInfoWithPgByMap(params);
		List querytList = pager.getResultList();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		if (CollectionUtils.isNotEmpty(querytList)) {
			for (int i = 0; i < querytList.size(); i++) {
				offerInfo = (OfferInfo) querytList.get(i);
				// 将bean转化成map
				Map resultMap = BeanUtils.toMap(offerInfo);
				// 债权id
				Long loanId = offerInfo.getLoanId();
				// 根据债权id，查询债权信息
				VLoanInfo vLoanInfo = vLoanInfoService.findByLoanId(loanId);
				// 得到合同编号
				resultMap.put("contractNum", vLoanInfo.getContractNum());
				
				
				/** 转换TPP划扣通道名称 **/
				try {
					if(Strings.isEmpty(TppPaySysNoEnum.get(offerInfo.getPaySysNo()))){
						resultMap.put("paySysNo","");//TppPaySysNoEnum中没有此划扣通道
					}else{
						resultMap.put("paySysNo",TppPaySysNoEnum.get(offerInfo.getPaySysNo()).getValue());			
					}
				} catch (Exception ex) {
					resultMap.put("paySysNo", "未知");
					logger.warn(ex);
				}
				
				list.add(resultMap);
			}
		}
		pager.setResultList(list);
		return toPGJSONWithCode(pager);
	}

	@RequestMapping("/offerInfo/insert")
	@ResponseBody
	public String realtimeDeductPre(OfferRealTimeDeduct offerRealTimeDeduct, HttpServletRequest request,
			HttpServletResponse response) {
		AttachmentResponseInfo<OfferRealTimeDeduct> attachmentResponseInfo = null;
		try {
			User user = UserContext.getUser();
			String code = user.getOrgCode();
			
			// 校验债权人和身份证信息是否与数据库匹配
			Map<String, Object> paramMap = new HashMap<String, Object>();
			Map<String, Object> offerparamMap = new HashMap<String, Object>();
			Map<String, Object> json = new HashMap<String, Object>();
			paramMap.put("name", offerRealTimeDeduct.getBorrowName());
			paramMap.put("idNum", offerRealTimeDeduct.getBorrowIdum());
			String contractNum = request.getParameter("contractNum");
			String paySysNo = request.getParameter("paySysNoAdd");
			paramMap.put("contractNum", contractNum);
			paramMap.put("code", code);
			
			if(Strings.isNotEmpty(paySysNo)){
				offerparamMap.put("contractNum", contractNum);
				offerparamMap.put("paySysNo", paySysNo);
				OfferChannel offerChannel = offerChannelService.findOfferChannelListBycontractNum(offerparamMap);
				if(offerChannel == null){
					attachmentResponseInfo = new AttachmentResponseInfo<OfferRealTimeDeduct>(
							ResponseEnum.REALTIMEDEDUCTPRE_FAILD_ERROR.getCode(), "该债权去向未配置划扣通道，请联系管理员");
					attachmentResponseInfo.setAttachment(offerRealTimeDeduct);
					return toResponseJSON(attachmentResponseInfo);
				}
			}
			List<VLoanInfo> list = vLoanInfoService.searchVLoanInfoList(paramMap);
			if (CollectionUtils.isEmpty(list)) {
				attachmentResponseInfo = new AttachmentResponseInfo<OfferRealTimeDeduct>(
						ResponseEnum.REALTIMEDEDUCTPRE_FAILD_ERROR.getCode(), "输入用户名和合同号不匹配");
				attachmentResponseInfo.setAttachment(offerRealTimeDeduct);
				return toResponseJSON(attachmentResponseInfo);
			}
			
			VLoanInfo loanInfo = list.get(0);
			// 是否显示划扣通道（true:是、false：否）
			String isShowPayChannel = request.getParameter("isShowPayChannel");
			// 显示划扣通道  如果paySysNo为空，则没有配置划扣通道
			if(Boolean.parseBoolean(isShowPayChannel) && Strings.isEmpty(paySysNo)){
				attachmentResponseInfo = new AttachmentResponseInfo<OfferRealTimeDeduct>(
						ResponseEnum.REALTIMEDEDUCTPRE_FAILD_ERROR.getCode(), "该债权去向未配置划扣通道，请联系管理员");
				attachmentResponseInfo.setAttachment(offerRealTimeDeduct);
				return toResponseJSON(attachmentResponseInfo);
			}
			
			/**检查该债权是否进行过转让**/
			boolean flag = loanTransferInfoService.isLoanTransfer(null, loanInfo.getId());
			if(!flag){
				attachmentResponseInfo = new AttachmentResponseInfo<OfferRealTimeDeduct>(
						ResponseEnum.REALTIMEDEDUCTPRE_FAILD_ERROR.getCode(),Strings.errorMsg);
				attachmentResponseInfo.setAttachment(offerRealTimeDeduct);
				return toResponseJSON(attachmentResponseInfo);
			}
			
			OfferParamsVo paramsVo = new OfferParamsVo();
			paramsVo.setUserCode(user.getUserCode());
			paramsVo.setIdnum(offerRealTimeDeduct.getBorrowIdum());
			paramsVo.setLoanId(loanInfo.getId());
			paramsVo.setOfferAmount(offerRealTimeDeduct.getOfferAmount());
			paramsVo.setPaySysNo(paySysNo);
			paramsVo.setShowPayChannel(Boolean.parseBoolean(isShowPayChannel));
			json = offerCreateService.createRealtimeOfferInfo(paramsVo);
			String resCode = (String) json.get("code");
			if (!ResponseEnum.SYS_SUCCESS.getCode().equals(resCode)) {
				attachmentResponseInfo = new AttachmentResponseInfo<OfferRealTimeDeduct>((String) json.get("code"),
						(String) json.get("message"));
				return toResponseJSON(attachmentResponseInfo);
			}
			attachmentResponseInfo = new AttachmentResponseInfo<OfferRealTimeDeduct>(
					ResponseEnum.REALTIMEDEDUCTPRE_SUCCESS, ResponseEnum.REALTIMEDEDUCTPRE_SUCCESS.getDesc());
		} catch (Exception e) {
			logger.error("realtimeDeductPre:" + e.getMessage());
			attachmentResponseInfo = new AttachmentResponseInfo<OfferRealTimeDeduct>(ResponseEnum.FULL_MSG.getCode(),
					e.getMessage());
			attachmentResponseInfo.setAttachment(offerRealTimeDeduct);
		}
		return toResponseJSON(attachmentResponseInfo);
	}

    /**
     * 关闭报盘
     * @param offerId
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/offerInfo/updateOfferInfoState")
    @ResponseBody
    public String updateOfferInfoState(@RequestParam String offerId, HttpServletRequest request, HttpServletResponse response) {
        AttachmentResponseInfo<Object> attachmentResponseInfo = null;
        try {
            Map<String, Object> paramMap = new HashMap<String, Object>();
            User user = UserContext.getUser();
            paramMap.put("offerId", offerId);
            paramMap.put("memo", "该报盘记录已经被" + user.getName() + "关闭");
            OfferInfo offerInfo = offerInfoService.findOfferById(Long.valueOf(offerId));
            if(null == offerInfo){
                throw new PlatformException(ResponseEnum.FULL_MSG,"报盘记录不存在！");
            }
            if(!"自动划扣".equals(offerInfo.getType())){
                throw new PlatformException(ResponseEnum.FULL_MSG,"不能关闭实时划扣生成的报盘！");
            }
            if(!OfferStateEnum.未报盘.getValue().equals(offerInfo.getState()) 
                && !OfferStateEnum.已回盘非全额.getValue().equals(offerInfo.getState())){
                throw new PlatformException(ResponseEnum.FULL_MSG, "该债权系统报盘状态是" + offerInfo.getState() + ",不能进行关闭操作！");
            }
            int num = offerInfoService.updateOfferInfo(paramMap);
            if (num > 0) {
                attachmentResponseInfo = new AttachmentResponseInfo<Object>(ResponseEnum.SYS_SUCCESS.getCode(),ResponseEnum.SYS_SUCCESS.getDesc());
                attachmentResponseInfo.setAttachment(attachmentResponseInfo);
            } else {
                attachmentResponseInfo = new AttachmentResponseInfo<Object>(ResponseEnum.FULL_MSG.getCode(), "操作失败");
                attachmentResponseInfo.setAttachment(attachmentResponseInfo);
            }
        }catch (PlatformException e) {
            attachmentResponseInfo = new AttachmentResponseInfo<Object>(ResponseEnum.FULL_MSG.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("realtimeDeductPre:" + e.getMessage());
            attachmentResponseInfo = new AttachmentResponseInfo<Object>(ResponseEnum.FULL_MSG.getCode(), e.getMessage());
            attachmentResponseInfo.setAttachment(attachmentResponseInfo);
        }
        return toResponseJSON(attachmentResponseInfo);
    }
	
	/**
	 * 跳转到（报盘明细信息查询）
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/offerInfo/offerInfoDetail")
	public ModelAndView listOfferDeatil(@RequestParam String offerId,
			@RequestParam String isThird, @RequestParam String fundsSources,
			HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("/offer/offerDetailList");
		modelAndView.addObject("offerId", offerId);
		modelAndView.addObject("isThird", isThird);
		modelAndView.addObject("fundsSources", fundsSources);
		return modelAndView;
	}

	/**
	 * 委托代扣回盘信息查询
	 * @param offerId
	 * @param isThird
	 * @param fundsSources
	 * @param offerTransaction
	 * @param rows
	 * @param page
	 * @param request
	 * @param response
	 * @return
	 * @throws ParseException
	 */
	@ResponseBody
	@RequestMapping("/offerInfo/searchOfferTransaction")
	public String listOfferDeatilTraction(@RequestParam String offerId,
			@RequestParam String isThird, @RequestParam String fundsSources,
			OfferTransaction offerTransaction, int rows, int page,
			HttpServletRequest request, HttpServletResponse response)
			throws ParseException {
		offerTransaction.setOfferId(Long.valueOf(offerId));
		offerTransaction.setIsThird(isThird);
		offerTransaction.setFundsSources(fundsSources);
		Pager pager = new Pager();
		pager.setRows(rows);
		pager.setPage(page);
		pager.setSidx("ID");
		pager.setSort("DESC");
		offerTransaction.setPager(pager);
		offerTransaction.setIsThird(isThird);
		pager = offerTransactionService.findWithPg(offerTransaction);
		return toPGJSONWithCode(pager);
	}
	
	@RequestMapping("/offerInfo/getPaySysNo")
	@ResponseBody
	public String getPaySysNo(HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("contractNum", request.getParameter("contractNum"));
		OfferChannel offerChannel = new OfferChannel();
		List<OfferChannel> list = offerChannelService.findOfferChannelList(paramMap);
		List<Map> resultList = new ArrayList<Map>();
		for (int i=0;i<list.size();i++) {
			Map map = new HashMap();
			offerChannel = list.get(i);
			map.put("id", offerChannel.getPaySysNo());
			map.put("text", TppRealPaySysNoEnum.get(offerChannel.getPaySysNo()));
			resultList.add(map);
		}
		return JSONObject.toJSONString(resultList);
		
	}
	

	//获取配置文件里面写好的捞财宝url地址和key
	public  String getValue(String key) throws IOException{
		Properties prop = PropertiesLoaderUtils.loadProperties(new ClassPathResource("messages.properties"));
		String value = prop.getProperty(key).trim();
		return value;
	}
	
	 /* 导出委托代扣报盘信息
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/offerInfo/exportOfferFile")
	@ResponseBody
	public void exportReturnFile(HttpServletRequest request, HttpServletResponse response) {
		createLog(request, SysActionLogTypeEnum.导出, "委托代扣报盘", "导出委托代扣报盘信息");
		ResponseInfo responseInfo = null;
		User user = UserContext.getUser();
		Workbook workbook = null;
		OutputStream out = null;
		try {
			int maxRecord = 50000;
			// 收集参数信息
			Map<String, Object> params = new HashMap<String, Object>();
			// 是否修改
			String isEdit = request.getParameter("isEdit");
			if (Strings.isNotEmpty(isEdit)) {
				params.put("isEdit", isEdit);
			}
			// 客户姓名
			String name = request.getParameter("name");
			if (Strings.isNotEmpty(name)) {
				params.put("name", name);
			}
			// 报盘日期，默认为系统日期
			params.put("offerDate", Dates.getDateTime(new Date(), "yyyy-MM-dd"));
			String offerDate = request.getParameter("offerDate");
			if (Strings.isNotEmpty(offerDate)) {
				params.put("offerDate", offerDate);
			}
			// 证件号码
			String idNum = request.getParameter("idNum");
			if (Strings.isNotEmpty(idNum)) {
				params.put("idNum", idNum);
			}
			// 机构区域码
			if ("催收员".equals(user.getEmployeeType()) && Strings.isEmpty(idNum)) {
				params.put("teller", user.getOrgCode());
			} else {
				params.put("orgCode", user.getOrgCode());
			}
			// 报盘状态
			String state = request.getParameter("state");
			if (Strings.isNotEmpty(state)) {
				params.put("state", state);
			}
			// 合同编号
			String contractNum = request.getParameter("contractNum");
			if (Strings.isNotEmpty(contractNum)) {
				params.put("contractNum", contractNum);
			}
			String paySysNo = request.getParameter("paySysNo");
			if (Strings.isNotEmpty(paySysNo)) {
				params.put("paySysNo", paySysNo);
			}
			/** 开户银行 **/
			String bankCode = Strings.defaultValue(request.getParameter("bankCode"), "0", "");
			if (Strings.isNotEmpty(bankCode)) {
				params.put("bankCode", bankCode);
			}
			/** 债权去向 **/
			String loanBelong = Strings.parseString(request.getParameter("loanBelong"));
			if (Strings.isNotEmpty(loanBelong)) {
				params.put("loanBelong", loanBelong);
			}
			// 查询报盘信息
			Pager pager = new Pager();
			pager.setRows(maxRecord);
			pager.setPage(1);
			pager.setSidx("ID");
			pager.setSort("DESC");
			params.put("pager", pager);
			pager = offerTransactionService.searchOfferInfoWithPgByMap(params);
			List querytList = pager.getResultList();
			
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			if (CollectionUtils.isNotEmpty(querytList)) {
				for (int i = 0; i < querytList.size(); i++) {
					OfferInfo offerInfo = (OfferInfo) querytList.get(i);
					// 将bean转化成map
					Map resultMap = BeanUtils.toMap(offerInfo);
					// 债权id
					Long loanId = offerInfo.getLoanId();
					// 根据债权id，查询债权信息
					VLoanInfo vLoanInfo = vLoanInfoService.findByLoanId(loanId);
					// 得到合同编号
					resultMap.put("contractNum", vLoanInfo.getContractNum());
					
					
					/** 转换TPP划扣通道名称 **/
					try {
						if(Strings.isEmpty(TppPaySysNoEnum.get(offerInfo.getPaySysNo()))){
							resultMap.put("paySysNo","");//TppPaySysNoEnum中没有此划扣通道
						}else{
							resultMap.put("paySysNo",TppPaySysNoEnum.get(offerInfo.getPaySysNo()).getValue());			
						}
					} catch (Exception ex) {
						resultMap.put("paySysNo", "未知");
						logger.warn(ex);
					}
					
					list.add(resultMap);
				}
			}
			// excel文件的表头显示字段名
			List<String> labels = this.getLabels();
			// excel文件的数据字段名
			List<String> fields = this.getFields();
			// 文件名
			String fileName = "委托代扣报盘表" + Dates.getDateTime(new Date(), "yyyyMMdd") + ".xls";
			// 工作表名称
			String sheetName = "Export";
			// 创建excel工作簿对象
			workbook = ExcelExportUtil.createExcelByMap(fileName, labels, fields, list, sheetName);
			// 文件下载
			// response.reset();
			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
			response.setContentType(CONTENT_TYPE);
			out = response.getOutputStream();
			workbook.write(out);
			out.flush();
			return;
		} catch (PlatformException e) {
			logger.error("导出委托代扣报盘信息异常：", e);
			responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("导出委托代扣报盘信息异常：", e);
			responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
		} finally {
			try {
				if (null != out) {
					out.close();
				}
				if (null != workbook) {
					workbook.close();
				}
			} catch (IOException e) {
				responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
				logger.error("导出委托代扣报盘信息异常：", e);
			}
		}
		try {
			response.setContentType("text/html");
			response.getWriter().write(toResponseJSON(responseInfo));
		} catch (IOException e) {
		}
	}
	
	/**
	 * excel文件的表头显示名
	 * 
	 * @return
	 */
	private List<String> getLabels() {
		List<String> labels = new ArrayList<String>();
		labels.add("报盘日期");
		labels.add("借款人");
		labels.add("银行");
		labels.add("账号");
		labels.add("报盘金额");
		labels.add("回盘金额");
		labels.add("最后变更日期");
		labels.add("备注");
		labels.add("状态");
		labels.add("划扣方式");
		labels.add("划扣通道");
		labels.add("合同编号");
		labels.add("合同来源");
		return labels;
	}
	/* excel文件的数据字段名
	 * 
	 * @return
	 */
	private List<String> getFields() {
		List<String> fields = new ArrayList<String>();
		fields.add("offerDate");
		fields.add("name");
		fields.add("bankName");
		fields.add("bankAcct");
		fields.add("amount");
		fields.add("actualAmount");
		fields.add("updateTime");
		fields.add("memo");
		fields.add("state");
		fields.add("type");
		fields.add("paySysNo");
		fields.add("contractNum");
		fields.add("fundsSources");
		return fields;
	}
}
