package com.zdmoney.credit.fee.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.zdmoney.credit.common.constant.FundsSourcesTypeEnum;
import com.zdmoney.credit.common.constant.OfferStateEnum;
import com.zdmoney.credit.common.constant.SysActionLogTypeEnum;
import com.zdmoney.credit.common.constant.tpp.ReturnCodeEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.util.Assert;
import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.ExcelExportUtil;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.vo.ResponseInfo;
import com.zdmoney.credit.fee.domain.LoanFeeTransaction;
import com.zdmoney.credit.fee.service.pub.ILoanFeeTransactionService;
import com.zdmoney.credit.fee.vo.SearchFeeOfferTransactionVo;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zendaimoney.thirdpp.common.enums.ThirdType;

/**
 * 相关 借款收费回盘 逻辑处理 接收前端请求
 * 
 * @author Ivan
 *
 */
@Controller
@RequestMapping("/fee/loanFeeTransaction")
public class LoanFeeTransactionController extends BaseController {

	protected static Log logger = LogFactory.getLog(LoanFeeTransactionController.class);

	private static final String CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

	@Autowired
	@Qualifier("loanFeeTransactionServiceImpl")
	ILoanFeeTransactionService loanFeeTransactionServiceImpl;

	/**
	 * 跳转服务费回盘页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/jumpSearchTransactionPage")
	public ModelAndView jumpSearchTransactionPage(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("/fee/searchOfferTransaction");
		String[] stateArr = new String[] { OfferStateEnum.未报盘.getValue(), OfferStateEnum.已报盘.getValue(),
				OfferStateEnum.已回盘.getValue(), OfferStateEnum.已关闭.getValue() };
		/** 划扣通道 **/
		modelAndView.addObject("paySysNos", ThirdType.values());
		/** 划扣状态 枚举 **/
		modelAndView.addObject("returnCodes", ReturnCodeEnum.values());

		modelAndView.addObject("states", stateArr);
		modelAndView.addObject("fundsSources", new String[] { FundsSourcesTypeEnum.龙信小贷.getValue(),
				FundsSourcesTypeEnum.外贸信托.getValue(),FundsSourcesTypeEnum.外贸2.getValue(),FundsSourcesTypeEnum.包商银行.getValue(),
				FundsSourcesTypeEnum.渤海2.getValue(),FundsSourcesTypeEnum.华瑞渤海.getValue(),FundsSourcesTypeEnum.外贸3.getValue(),FundsSourcesTypeEnum.陆金所.getValue()});
		return modelAndView;
	}

	/**
	 * 收集服务费回盘查询参数
	 * 
	 * @param request
	 * @return
	 */
	private Map<String, Object> collectTransactionQueryData(HttpServletRequest request) {
		/** 收集参数信息 **/
		Map<String, Object> params = new HashMap<String, Object>();
		/** 姓名 **/
		params.put("name", Strings.parseString(request.getParameter("name")));
		/** 身份证号 **/
		params.put("idNum", Strings.parseString(request.getParameter("idNum")));
		/** 合同编号 **/
		params.put("contractNum", Strings.parseString(request.getParameter("contractNum")));
		/** 速贷**/
		String memo = request.getParameter("memo");
		if (Strings.isNotEmpty(memo)) {
			params.put("memo", memo);
		}
		/** 合同来源 **/
		String fundsSources = Strings.parseString(request.getParameter("fundsSource"));
		if (StringUtils.isNotEmpty(fundsSources)) {
			params.put("fundsSources", fundsSources);
		}
		/** 报盘日期（起） **/
		String offerDateBegin = Strings.parseString(request.getParameter("offerDateBegin"));
		if (!Strings.isEmpty(offerDateBegin)) {
			params.put("offerDateBegin", Dates.parse(offerDateBegin, Dates.DEFAULT_DATE_FORMAT));
		}
		/** 报盘日期（止） **/
		String offerDateEnd = Strings.parseString(request.getParameter("offerDateEnd"));
		if (!Strings.isEmpty(offerDateBegin)) {
			params.put("offerDateEnd", Dates.parse(offerDateEnd, Dates.DEFAULT_DATE_FORMAT));
		}
		/** 划扣通道 **/
		String paySysNoReal = Strings.parseString(request.getParameter("paySysNoReal"));
		if (!Strings.isEmpty(paySysNoReal)) {
			params.put("paySysNoReal", paySysNoReal);
		}
		/** 划扣状态 **/
		String returnCode = Strings.parseString(request.getParameter("returnCode"));
		if (!Strings.isEmpty(returnCode)) {
			params.put("returnCode", returnCode);
		}

		/** 开户银行 **/
		String bankCode = Strings.defaultValue(request.getParameter("bankCode"), "0", "");
		if (!Strings.isEmpty(bankCode)) {
			params.put("bankCode", bankCode);
		}

		/** 报盘状态 **/
		params.put("state", Strings.parseString(request.getParameter("state")));
		return params;
	}

	/**
	 * 查询服务费报盘数据
	 * 
	 * @param rows
	 * @param page
	 * @param request
	 * @param response
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("/search")
	@ResponseBody
	public String search(int rows, int page, HttpServletRequest request, HttpServletResponse response)
			throws ParseException {
		createLog(request, SysActionLogTypeEnum.查询, "服务费回盘", "查询服务费回盘");
		/** 登陆者 **/
		User user = UserContext.getUser();
		/** 收集查询参数 **/
		Map<String, Object> params = collectTransactionQueryData(request);
		/** 网点代码 **/
		params.put("organ", user.getOrgCode());

		Pager pager = new Pager();
		pager.setRows(rows);
		pager.setPage(page);
		pager.setSidx("T2.ID");
		pager.setSort("DESC");
		params.put("pager", pager);
		/** 查询报盘信息 **/
		pager = loanFeeTransactionServiceImpl.searchFeeOfferTransactionList(params);
		List queryList = pager.getResultList();
		if (CollectionUtils.isNotEmpty(queryList)) {
			for (int i = 0; i < queryList.size(); i++) {
				SearchFeeOfferTransactionVo searchFeeOfferTransactionVo = (SearchFeeOfferTransactionVo) queryList
						.get(i);
				/** 转换TPP划扣通道名称 **/
				try {
					if(searchFeeOfferTransactionVo.getPaySysNoReal()==null){
						searchFeeOfferTransactionVo.setPaySysNoReal("");
					}else{
					ThirdType thirdType = ThirdType.get(searchFeeOfferTransactionVo.getPaySysNoReal());
					searchFeeOfferTransactionVo.setPaySysNoReal(thirdType.getDesc());
					}
				} catch (Exception ex) {
					searchFeeOfferTransactionVo.setPaySysNoReal("未知");
					logger.warn(ex);
				}
				/** 转换TPP划扣状态名称 **/
				try {
					if (Strings.isEmpty(searchFeeOfferTransactionVo.getRtnCode())) {
						searchFeeOfferTransactionVo.setRtnCode(searchFeeOfferTransactionVo.getTrxState());
					} else {
						ReturnCodeEnum returnCodeEnum = ReturnCodeEnum.get(searchFeeOfferTransactionVo.getRtnCode());
						searchFeeOfferTransactionVo.setRtnCode(returnCodeEnum.getDesc());
					}
				} catch (Exception ex) {
					searchFeeOfferTransactionVo.setRtnCode("未知");
					logger.warn(ex);
				}
			}
		}
		return toPGJSONWithCode(pager);
	}

	/**
	 * 跟据报盘编号查询回盘情况 （不含分页）
	 * 
	 * @param offerId
	 *            报盘编号
	 * @param request
	 * @param response
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("/searchByOfferId")
	@ResponseBody
	public String searchByOfferId(Long offerId, HttpServletRequest request, HttpServletResponse response)
			throws ParseException {
		createLog(request, SysActionLogTypeEnum.查询, "服务费回盘", "查询服务费回盘数据");
		/** 登陆者 **/
		User user = UserContext.getUser();

		LoanFeeTransaction loanFeeTransaction = new LoanFeeTransaction();
		loanFeeTransaction.setOfferId(offerId);
		List<LoanFeeTransaction> transactionList = loanFeeTransactionServiceImpl.findListByVo(loanFeeTransaction);

		Pager pager = new Pager();
		pager.setResultList(transactionList);
		pager.setTotalCount(transactionList.size());
		return toPGJSONWithCode(pager);
	}

	/**
	 * 导出服务费回盘数据
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/exportFile")
	@ResponseBody
	public void exportFile(HttpServletRequest request, HttpServletResponse response) {
		createLog(request, SysActionLogTypeEnum.导出, "服务费回盘", "导出服务费回盘数据");
		ResponseInfo responseInfo = null;
		/** 登陆者 **/
		User user = UserContext.getUser();
		Workbook workbook = null;
		OutputStream out = null;
		try {
			int maxRecord = 50000;

			/** 收集查询参数 **/
			Map<String, Object> params = collectTransactionQueryData(request);
			/** 网点代码 **/
			params.put("organ", user.getOrgCode());

			// 收集参数信息
			Pager pager = new Pager();
			pager.setRows(maxRecord);
			pager.setPage(1);
			pager.setSidx("T2.ID");
			pager.setSort("DESC");
			params.put("pager", pager);
			// 查询报盘信息
			pager = loanFeeTransactionServiceImpl.searchFeeOfferTransactionList(params);
			List<SearchFeeOfferTransactionVo> queryList = (List<SearchFeeOfferTransactionVo>) pager.getResultList();

			// 当前导出条件查询不到数据
			Assert.notCollectionsEmpty(queryList, ResponseEnum.FULL_MSG, "暂无数据！");
			// 编辑查询结果
			for (SearchFeeOfferTransactionVo searchFeeOfferTransactionVo : queryList) {
				/** 转换TPP划扣通道名称 **/
				try {
					Date offerDate= searchFeeOfferTransactionVo.getOfferDate();
					Date responseTime = searchFeeOfferTransactionVo.getResponseTime();
					if(offerDate != null){
						searchFeeOfferTransactionVo.setRequestTimeStr(Dates.getDateTime(offerDate, Dates.DEFAULT_DATETIME_FORMAT));
					}
					if(responseTime != null){
						searchFeeOfferTransactionVo.setResponseTimeStr(Dates.getDateTime(responseTime, Dates.DEFAULT_DATETIME_FORMAT));
					}
					if(searchFeeOfferTransactionVo.getPaySysNoReal()==null){
						searchFeeOfferTransactionVo.setPaySysNoReal("");
					}else{
					ThirdType thirdType = ThirdType.get(searchFeeOfferTransactionVo.getPaySysNoReal());
					searchFeeOfferTransactionVo.setPaySysNoReal(thirdType.getDesc());
					}
				} catch (Exception ex) {
					searchFeeOfferTransactionVo.setPaySysNoReal("未知");
					logger.warn(ex);
				}
				/** 转换TPP划扣状态名称 **/
				try {
					if (Strings.isEmpty(searchFeeOfferTransactionVo.getRtnCode())) {
						searchFeeOfferTransactionVo.setRtnCode(searchFeeOfferTransactionVo.getTrxState());
					} else {
						ReturnCodeEnum returnCodeEnum = ReturnCodeEnum.get(searchFeeOfferTransactionVo.getRtnCode());
						searchFeeOfferTransactionVo.setRtnCode(returnCodeEnum.getDesc());
					}
				} catch (Exception ex) {
					searchFeeOfferTransactionVo.setRtnCode("未知");
					logger.warn(ex);
				}
			}
			// excel文件的表头显示字段名
			List<String> labels = this.getLabels();
			// excel文件的数据字段名
			List<String> fields = this.getFields();
			// 文件名
			String fileName = "服务费回盘数据" + Dates.getDateTime(new Date(), "yy-MM-dd") + ".xls";
			// 工作表名称
			String sheetName = "Export";
			// 创建excel工作簿对象
			workbook = ExcelExportUtil.createExcelByVo(fileName, labels, fields, queryList, sheetName);
			// 文件下载
			// response.reset();
			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
			response.setContentType(CONTENT_TYPE);
			out = response.getOutputStream();
			workbook.write(out);
			out.flush();
			return;
		} catch (PlatformException e) {
			logger.error("导出服务费回盘数据异常：", e);
			responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("导出服务费回盘数据异常：", e);
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
				logger.error("导出服务费回盘数据异常：", e);
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
		labels.add("营业部备注");
		labels.add("失败原因");
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
		fields.add("requestTimeStr");
		fields.add("amount");
		fields.add("factAmount");
		fields.add("responseTimeStr");
		fields.add("type");
		fields.add("paySysNoReal");
		fields.add("merId");
		fields.add("rtnCode");
		fields.add("orgMemo");
		fields.add("failReason");
		return fields;
	}
}
