package com.zdmoney.credit.offer.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zdmoney.credit.common.constant.FundsSourcesTypeEnum;
import com.zdmoney.credit.common.constant.SysActionLogTypeEnum;
import com.zdmoney.credit.common.constant.tpp.ReturnCodeEnum;
import com.zdmoney.credit.common.constant.tpp.TppPaySysNoEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.util.Assert;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.ExcelExportUtil;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.vo.ResponseInfo;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.loan.service.pub.ILoanTransferInfoService;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import com.zdmoney.credit.offer.service.pub.IOfferInfoService;
import com.zdmoney.credit.offer.service.pub.IOfferTransactionService;
import com.zdmoney.credit.offer.vo.OfferReturnVo;
import com.zdmoney.credit.system.domain.SysDictionary;
import com.zdmoney.credit.system.service.pub.ISysDictionaryService;
import com.zendaimoney.thirdpp.common.enums.ThirdType;

/**
 * 委托代扣回盘
 * 
 * @author Anfq 2015年8月27日
 */
@Controller
@RequestMapping("/offer")
public class OfferReturnController extends BaseController {

	private static final String CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	
	@Autowired
	private IOfferInfoService offerInfoService;

	@Autowired
	private IOfferTransactionService offerTransactionService;

	@Autowired
	IVLoanInfoService vLoanInfoService;

	@Autowired
	@Qualifier("sysDictionaryServiceImpl")
	ISysDictionaryService sysDictionaryServiceImpl;
	
	@Autowired
	private ILoanTransferInfoService loanTransferInfoServiceImpl;

	/**
	 * 委托代扣回盘页面初始化
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/offerReturn/offerReturnList")
	public String jumpPage(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		createLog(request, SysActionLogTypeEnum.查询, "委托代扣回盘", "加载委托代扣回盘页面");
		Date sysdate = new Date();
		model.addAttribute("sysdate", sysdate);
		/** 划扣状态 枚举 **/
		model.addAttribute("returnCodes", ReturnCodeEnum.values());
		// 合同来源
		model.addAttribute("fundsSources", FundsSourcesTypeEnum.values());
		/** 划扣通道枚举 **/
		model.addAttribute("paySysNos", TppPaySysNoEnum.values());

		/** 查询债权去向数据源 **/
		SysDictionary sysDictionary = new SysDictionary();
		sysDictionary.setCodeType("loan_loanbelongs");
		List<SysDictionary> list = sysDictionaryServiceImpl.findDictionaryListByVo(sysDictionary);
		model.addAttribute("loanBelongs", list);
		/**转让批次**/
		List<Map<String, Object>> transferBatchs = loanTransferInfoServiceImpl.findLoanTransferBatchList();
		model.addAttribute("transferBatchs", transferBatchs);
		return "/offer/offerReturnList";
	}

	/**
	 * 委托代扣回盘页面查询处理
	 * 
	 * @param rows
	 * @param page
	 * @param request
	 * @param response
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("/offerReturn/search")
	@ResponseBody
	public String search(int rows, int page, HttpServletRequest request, HttpServletResponse response)
			throws ParseException {
		createLog(request, SysActionLogTypeEnum.查询, "委托代扣回盘", "查询委托代扣回盘信息");
		User user = UserContext.getUser();
		// 收集参数信息
		Map<String, Object> params = this.getRequestParameter(request, user);
		Pager pager = new Pager();
		pager.setRows(rows);
		pager.setPage(page);
		//pager.setSidx("T1.REQ_TIME,T1.ID");
		//pager.setSort("ASC");
		params.put("pager", pager);
		// 查询报盘信息
		pager = offerTransactionService.searchOfferTransactionInfoWithPgByMap(params);
		List queryList = pager.getResultList();
		if (CollectionUtils.isNotEmpty(queryList)) {
			for (int i = 0; i < queryList.size(); i++) {
				OfferReturnVo offerReturnVo = (OfferReturnVo) queryList.get(i);
				/** 转换TPP划扣通道名称 **/
				try {
					if (offerReturnVo.getPaySysNo()==null) {
						offerReturnVo.setPaySysNo("");
					}else if(Strings.isEmpty(TppPaySysNoEnum.get(offerReturnVo.getPaySysNo()))){
						offerReturnVo.setPaySysNo("");//TppPaySysNoEnum中没有此划扣通道
					}else{
						offerReturnVo.setPaySysNo(TppPaySysNoEnum.get(offerReturnVo.getPaySysNo()).getValue());					
					}
				} catch (Exception ex) {
					offerReturnVo.setPaySysNo("未知");
					logger.warn(ex);
				}
			
				/** 转换TPP划扣状态名称 **/
				try {
					if (Strings.isEmpty(offerReturnVo.getRtnCode())) {
						offerReturnVo.setRtnCode(offerReturnVo.getTrxState());
					} else {
						ReturnCodeEnum returnCodeEnum = ReturnCodeEnum.get(offerReturnVo.getRtnCode());
						offerReturnVo.setRtnCode(returnCodeEnum.getDesc());
					}
				} catch (Exception ex) {
					offerReturnVo.setRtnCode("未知");
					logger.warn(ex);
				}
			}
		}
		return toPGJSONWithCode(pager);
	}

	/**
	 * 导出委托代扣回盘信息
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/offerReturn/exportReturnFile")
	@ResponseBody
	public void exportReturnFile(HttpServletRequest request, HttpServletResponse response) {
		createLog(request, SysActionLogTypeEnum.导出, "委托代扣回盘", "导出委托代扣回盘信息");
		ResponseInfo responseInfo = null;
		User user = UserContext.getUser();
		Workbook workbook = null;
		OutputStream out = null;
		try {
			int maxRecord = 50000;
			// 收集参数信息
			Map<String, Object> params = this.getRequestParameter(request, user);
			Pager pager = new Pager();
			pager.setRows(maxRecord);
			pager.setPage(1);
			pager.setSidx("T1.REQ_TIME,T1.ID");
			pager.setSort("ASC");
			params.put("pager", pager);
			// 查询报盘信息
			pager = offerTransactionService.searchOfferTransactionInfoWithPgByMap(params);
			List<OfferReturnVo> offerReturnVoList = (List<OfferReturnVo>) pager.getResultList();

			// 当前导出条件查询不到数据
			Assert.notCollectionsEmpty(offerReturnVoList, ResponseEnum.FULL_MSG, "暂无数据！");
			// 编辑查询结果
			for (OfferReturnVo offerReturnVo : offerReturnVoList) {
				/** 转换TPP划扣通道名称 **/
				try {
					if (offerReturnVo.getPaySysNo()==null) {
						offerReturnVo.setPaySysNo("");
					}else if(Strings.isEmpty(TppPaySysNoEnum.get(offerReturnVo.getPaySysNo()))){
						offerReturnVo.setPaySysNo("");//TppPaySysNoEnum中没有此划扣通道
					}else{
						offerReturnVo.setPaySysNo(TppPaySysNoEnum.get(offerReturnVo.getPaySysNo()).getValue());					
					}
				} catch (Exception ex) {
					offerReturnVo.setPaySysNo("未知");
					logger.warn(ex);
				}
				/** 转换TPP划扣状态名称 **/
				try {
					if (Strings.isEmpty(offerReturnVo.getRtnCode())) {
						offerReturnVo.setRtnCode(offerReturnVo.getTrxState());
					} else {
						ReturnCodeEnum returnCodeEnum = ReturnCodeEnum.get(offerReturnVo.getRtnCode());
						offerReturnVo.setRtnCode(returnCodeEnum.getDesc());
					}
				} catch (Exception ex) {
					offerReturnVo.setRtnCode("未知");
					logger.warn(ex);
				}
			}
			// excel文件的表头显示字段名
			List<String> labels = this.getLabels();
			// excel文件的数据字段名
			List<String> fields = this.getFields();
			// 文件名
			String fileName = "报回盘查询导出" + Dates.getDateTime(new Date(), "yy-MM-dd") + ".xls";
			// 工作表名称
			String sheetName = "Export";
			// 创建excel工作簿对象
			workbook = ExcelExportUtil.createExcelByVo(fileName, labels, fields, offerReturnVoList, sheetName);
			// 文件下载
			// response.reset();
			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
			response.setContentType(CONTENT_TYPE);
			out = response.getOutputStream();
			workbook.write(out);
			out.flush();
			return;
		} catch (PlatformException e) {
			logger.error("导出委托代扣回盘信息异常：", e);
			responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("导出委托代扣回盘信息异常：", e);
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
				logger.error("导出委托代扣回盘信息异常：", e);
			}
		}
		try {
			response.setContentType("text/html");
			response.getWriter().write(toResponseJSON(responseInfo));
		} catch (IOException e) {
		}
	}

	/**
	 * 收集参数信息
	 * 
	 * @param request
	 * @param user
	 * @return
	 */
	private Map<String, Object> getRequestParameter(HttpServletRequest request, User user) {
		Map<String, Object> params = new HashMap<String, Object>();
		// 速贷
		String memo = request.getParameter("memo");
		if (Strings.isNotEmpty(memo)) {
		    params.put("memo", memo);
		}
		// 姓名
		String name = request.getParameter("name");
		if (Strings.isNotEmpty(name)) {
			params.put("name", name);
		}
		// 身份证号
		String idNum = request.getParameter("idNum");
		if (Strings.isNotEmpty(idNum)) {
			params.put("idNum", idNum);
		}
		// 报盘日期（开始日期）
		String startOfferDate = request.getParameter("startOfferDate");
		if (Strings.isNotEmpty(startOfferDate)) {
			params.put("startOfferDate", startOfferDate);
		}
		// 报盘日期（截止日期）
		String endOfferDate = request.getParameter("endOfferDate");
		if (Strings.isNotEmpty(endOfferDate)) {
			params.put("endOfferDate", endOfferDate);
		}
		// 回盘日期（开始日期）
		String startReceiveDate = request.getParameter("startReceiveDate");
		if (Strings.isNotEmpty(startReceiveDate)) {
			params.put("startReceiveDate", startReceiveDate);
		}
		// 回盘日期（截止日期）
		String endReceiveDate = request.getParameter("endReceiveDate");
		if (Strings.isNotEmpty(endReceiveDate)) {
			params.put("endReceiveDate", endReceiveDate);
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
		// 是否成功
		String isSuc = request.getParameter("isSuc");
		if (Strings.isNotEmpty(isSuc)) {
			params.put("isSuc", isSuc);
		}
		// 合同来源
		String fundsSource = request.getParameter("fundsSource");
		if (Strings.isNotEmpty(fundsSource)) {
			params.put("fundsSource", fundsSource);
		}
		// 合同编号
		String contractNum = request.getParameter("contractNum");
		if (Strings.isNotEmpty(contractNum)) {
			params.put("contractNum", contractNum);
		}
		
		
		/** 划扣通道 **/
		String paySysNo = request.getParameter("paySysNo");
		if (Strings.isNotEmpty(paySysNo)) {
			params.put("paySysNo", paySysNo);
		}
		/** 是否划扣成功 **/
		String returnCode = request.getParameter("returnCode");
		if (Strings.isNotEmpty(returnCode)) {
			params.put("returnCode", returnCode);
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
		
		/** 转让批次**/
		String transferBatch = Strings.parseString(request.getParameter("transferBatch"));
		if (Strings.isNotEmpty(transferBatch)) {
			params.put("transferBatch", transferBatch);
		}
		return params;
	}

	/**
	 * excel文件的表头显示名
	 * 
	 * @return
	 */
	private List<String> getLabels() {
		List<String> labels = new ArrayList<String>();
		labels.add("合同编号");
		labels.add("合同来源");
		labels.add("借款人");
		labels.add("身份证号");
		labels.add("银行");
		labels.add("账号");
		labels.add("报盘日期");
		labels.add("报盘金额");
		labels.add("回盘金额");
		labels.add("回盘日期");
		labels.add("划扣方式");
		labels.add("划扣通道");
		labels.add("划扣商户");
		labels.add("划扣状态");
		labels.add("债权去向");
		labels.add("备注");
		labels.add("营业部备注");
		labels.add("回购日期");
		labels.add("转让批次");
		return labels;
	}

	/**
	 * excel文件的数据字段名
	 * 
	 * @return
	 */
	private List<String> getFields() {
		List<String> fields = new ArrayList<String>();
		fields.add("contractNum");
		fields.add("fundsSources");
		fields.add("name");
		fields.add("idNum");
		fields.add("bankName");
		fields.add("bankAcct");
		fields.add("reqTime");
		fields.add("payAmount");
		fields.add("actualAmount");
		fields.add("rspReceiveTime");
		fields.add("type");
		fields.add("paySysNo");
		fields.add("merId");
		fields.add("rtnCode");
		fields.add("loanBelong");
		fields.add("memo");
		fields.add("orgMemo");
		fields.add("buybackTime");
		fields.add("transferBatch");
		return fields;
	}
}
