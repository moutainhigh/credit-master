package com.zdmoney.credit.fee.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.zdmoney.credit.common.constant.FundsSourcesTypeEnum;
import com.zdmoney.credit.common.constant.LoanFeeStateEnum;
import com.zdmoney.credit.common.constant.OfferStateEnum;
import com.zdmoney.credit.common.constant.OfferTypeEnum;
import com.zdmoney.credit.common.constant.SysActionLogTypeEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.util.Assert;
import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.vo.ResponseInfo;
import com.zdmoney.credit.fee.domain.LoanFeeInfo;
import com.zdmoney.credit.fee.domain.LoanFeeOffer;
import com.zdmoney.credit.fee.offer.LoanFeeUtil;
import com.zdmoney.credit.fee.service.pub.ILoanFeeInfoService;
import com.zdmoney.credit.fee.service.pub.ILoanFeeOfferService;
import com.zdmoney.credit.fee.vo.CreateFeeOfferVo;
import com.zdmoney.credit.fee.vo.LoanFeeOfferVo;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.loan.service.pub.ILoanBaseService;
import com.zdmoney.credit.system.service.pub.ISysDictionaryService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;
import com.zendaimoney.thirdpp.common.enums.ThirdType;

/**
 * 相关 借款收费报盘 逻辑处理 接收前端请求
 * 
 * @author Ivan
 *
 */
@Controller
@RequestMapping("/fee/loanFeeOffer")
public class LoanFeeOfferController extends BaseController {

	protected static Log logger = LogFactory.getLog(LoanFeeOfferController.class);

	@Autowired
	@Qualifier("loanFeeOfferServiceImpl")
	ILoanFeeOfferService loanFeeOfferServiceImpl;

	@Autowired
	@Qualifier("loanFeeUtil")
	LoanFeeUtil loanFeeUtil;

	@Autowired
	@Qualifier("sysParamDefineServiceImpl")
	ISysParamDefineService sysParamDefineServiceImpl;

	@Autowired
	@Qualifier("loanFeeInfoServiceImpl")
	ILoanFeeInfoService loanFeeInfoServiceImpl;

	@Autowired
	@Qualifier("sysDictionaryServiceImpl")
	ISysDictionaryService sysDictionaryServiceImpl;
	@Autowired
	@Qualifier("loanBaseServiceImpl")
	ILoanBaseService loanBaseServiceImpl;
	/**
	 * 跳转服务费报盘页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/jumpSearchOfferPage")
	public ModelAndView jumpSearchOfferPage(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("/fee/searchOffer");
		String[] stateArr = new String[] { OfferStateEnum.未报盘.getValue(), OfferStateEnum.已报盘.getValue(),
				OfferStateEnum.已回盘.getValue(), OfferStateEnum.已关闭.getValue() };
		String[] loanFeeStateArr = new String[] { LoanFeeStateEnum.未收取.getValue(),  LoanFeeStateEnum.部分收取.getValue(), 
				LoanFeeStateEnum.已收取.getValue()};
		/** 划扣通道 **/
		modelAndView.addObject("paySysNos", ThirdType.values());
		/** 服务费收取状态 **/
		modelAndView.addObject("loanFeeStates", loanFeeStateArr);
		modelAndView.addObject("states", stateArr);
		modelAndView.addObject("fundsSources", new String[] { FundsSourcesTypeEnum.龙信小贷.getValue(),
				FundsSourcesTypeEnum.外贸信托.getValue(),FundsSourcesTypeEnum.外贸2.getValue(),FundsSourcesTypeEnum.包商银行.getValue(),FundsSourcesTypeEnum.渤海2.getValue(),
				FundsSourcesTypeEnum.华瑞渤海.getValue(),FundsSourcesTypeEnum.外贸3.getValue(),FundsSourcesTypeEnum.陆金所.getValue()});
		return modelAndView;
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
		createLog(request, SysActionLogTypeEnum.查询, "服务费报盘", "查询服务费报盘");
		/** 登陆者 **/
		User user = UserContext.getUser();
		/** 收集参数信息 **/
		Map<String, Object> params = new HashMap<String, Object>();
		/** 姓名 **/
		params.put("name", Strings.parseString(request.getParameter("name")));
		/** 身份证号 **/
		params.put("idNum", Strings.parseString(request.getParameter("idNum")));
		/** 合同编号 **/
		params.put("contractNum", Strings.parseString(request.getParameter("contractNum")));
		/** 合同来源 **/
		String fundsSources = Strings.parseString(request.getParameter("fundsSources"));
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
		String paySysNo = Strings.parseString(request.getParameter("paySysNo"));
		if (!Strings.isEmpty(paySysNo)) {
			params.put("paySysNo", paySysNo);
		}
		/** 开户银行 **/
		String bankCode = Strings.defaultValue(request.getParameter("bankCode"), "0", "");
		if (!Strings.isEmpty(bankCode)) {
			params.put("bankCode", bankCode);
		}

		/** 报盘状态 **/
		params.put("state", Strings.parseString(request.getParameter("state")));
		/** 收取状态 **/
		params.put("loanFeeState", Strings.parseString(request.getParameter("loanFeeState")));
		/** 网点代码 **/
		params.put("organ", user.getOrgCode());

		Pager pager = new Pager();
		pager.setRows(rows);
		pager.setPage(page);
		pager.setSidx("ID");
		pager.setSort("DESC");
		params.put("pager", pager);
		/** 查询报盘信息 **/
		pager = loanFeeOfferServiceImpl.searchLoanFeeOfferWithPgByMap(params);
		List queryList = pager.getResultList();
		if (CollectionUtils.isNotEmpty(queryList)) {
			for (int i = 0; i < queryList.size(); i++) {
				LoanFeeOfferVo loanFeeOffer = (LoanFeeOfferVo) queryList.get(i);
				String state = Strings.parseString(loanFeeOffer.getState());
				if (state.equals("已回盘全额") || state.equals("已回盘非全额")) {
					state = "已回盘";
				}
				loanFeeOffer.setState(state);
				/** 转换TPP划扣通道名称 **/
				try {
					ThirdType thirdType = ThirdType.get(loanFeeOffer.getPaySysNo());
					loanFeeOffer.setPaySysNo(thirdType.getDesc());
				} catch (Exception ex) {
					loanFeeOffer.setPaySysNo("未知");
					logger.warn(ex);
				}
			}
		}
		return toPGJSONWithCode(pager);
	}

	/**
	 * 服务费实时划扣
	 * 
	 * @param page
	 * @param request
	 * @param response
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("/actualTimeOffer")
	@ResponseBody
	public String actualTimeOffer(CreateFeeOfferVo createFeeOfferVo, HttpServletRequest request,
			HttpServletResponse response) {
		ResponseInfo responseInfo = null;
		try {
			createLog(request, SysActionLogTypeEnum.新增, "服务费实时划扣", "服务费实时划扣");
			Long feeId = createFeeOfferVo.getFeeId();
			Assert.notNull(feeId, ResponseEnum.FULL_MSG, "服务费实时划扣失败：缺少服务费编号参数");
			BigDecimal amount = createFeeOfferVo.getAmount();
			if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
				throw new PlatformException(ResponseEnum.FULL_MSG, "服务费金额不能为空且大于0元");
			}

			LoanFeeInfo loanFeeInfo = loanFeeInfoServiceImpl.findById(feeId);
			Assert.notNull(loanFeeInfo, ResponseEnum.FULL_MSG, "feeId：" + feeId + " 未找到记录");
			if (LoanFeeStateEnum.已收取.getValue().equals(loanFeeInfo.getState())) {
				throw new PlatformException(ResponseEnum.FULL_MSG, "feeId：" + feeId + " 服务费已结清");
			}
			
			/** 同笔服务费当天只能进行N次实时划扣（N配置在数据库） **/
			/** 查询参数配置最大划扣次数 **/
			int maxCount = Strings.convertValue(
					sysParamDefineServiceImpl.getSysParamValue("offer", "fee_realTimeDeductCount"), Long.class)
					.intValue();
			/** 查询该笔服务费当天已划扣次数 **/
			int hasCount = loanFeeOfferServiceImpl.getOfferCount(feeId, OfferTypeEnum.实时划扣.getValue(),
					Dates.getCurrDate());
			if (hasCount >= maxCount) {
				throw new PlatformException(ResponseEnum.FULL_MSG, "实时划扣次数超出每天限额");
			}
			/**获取合同来源*/
			createFeeOfferVo.setFundsSources(loanBaseServiceImpl.findLoanBase(loanFeeInfo.getLoanId()).getFundsSources());
			/** 生成服务费报盘 **/
			LoanFeeOffer loanFeeOffer = loanFeeOfferServiceImpl.createOffer(createFeeOfferVo);

			/** 发送报盘 **/
			List<LoanFeeOffer> loanFeeOfferList = new ArrayList<LoanFeeOffer>();
			loanFeeOfferList.add(loanFeeOffer);
			loanFeeUtil.sendOffer(loanFeeOfferList);

			responseInfo = new ResponseInfo(ResponseEnum.SYS_SUCCESS);
		} catch (PlatformException ex) {
			responseInfo = ex.toResponseInfo();
			ex.printStackTraceExt(logger);
		} catch (Exception ex) {
			responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
			logger.error(ex, ex);
		}
		return toResponseJSON(responseInfo);
	}
}

