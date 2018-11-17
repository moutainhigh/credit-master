package com.zdmoney.credit.core.controller;

import com.zdmoney.credit.common.constant.SpecialRepaymentApplyTeyps;
import com.zdmoney.credit.common.constant.SpecialRepaymentStateCoreEnum;
import com.zdmoney.credit.common.constant.SpecialRepaymentTypeCoreEnum;
import com.zdmoney.credit.common.constant.system.LogLevel;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.util.coreUtil.Constants;
import com.zdmoney.credit.common.util.coreUtil.JSONUtil;
import com.zdmoney.credit.common.util.coreUtil.MessageUtil;
import com.zdmoney.credit.common.vo.AttachmentResponseInfo;
import com.zdmoney.credit.common.vo.core.SpecialRepayParamsVO;
import com.zdmoney.credit.common.vo.core.SpecialRepaymentVO;
import com.zdmoney.credit.core.service.pub.ISpecialRepaymentCoreService;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.IAfterLoanService;
import com.zdmoney.credit.loan.service.pub.ISpecialRepaymentApplyService;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import com.zdmoney.credit.loan.vo.MaxReliefAmountParamVo;
import com.zdmoney.credit.loan.vo.ReliefAmountCalculateVo;
import com.zdmoney.credit.system.domain.ComEmployee;
import com.zdmoney.credit.system.service.pub.IComEmployeeService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 特殊还款相关处理
 * 
 * @author 00235304
 *
 */
@Controller
@RequestMapping("/core/specialRepaymentCore")
public class SpecialRepaymentCoreController extends BaseController {

	private static Log logger = LogFactory.getLog(SpecialRepaymentCoreController.class);

	@Autowired
	ISpecialRepaymentCoreService specialRepaymentCoreService;

	@Autowired
	IComEmployeeService comEmployeeService;

	@Autowired
	IVLoanInfoService vLoanInfoService;

	@Autowired
	private IAfterLoanService afterLoanService;

	@Autowired
	private ISpecialRepaymentApplyService specialRepaymentApplyService;
	/**
	 * 特殊还款接口方法：用于提前结清、提前扣款、费用减免
	 * 
	 * @param request
	 *            请求信息对象
	 * @param response
	 *            响应信息对象
	 * @throws IOException
	 */
	@RequestMapping("/handleSpecialRepayment")
	@ResponseBody
	public void handleSpecialRepayment(HttpServletRequest request, HttpServletResponse response,
			SpecialRepayParamsVO paramsVo) throws IOException {
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			logger.info("特殊还款接口接收的参数：" + paramsVo.toString());
			Long loanId = paramsVo.getLoanId();
			String userCode = paramsVo.getUserCode();
			String spRepayType = paramsVo.getSpRepayType();
			String spRepayState = paramsVo.getSpRepayState();
			BigDecimal reliefAmount = paramsVo.getReliefAmount();

			// 接口参数校验 begin
			if (loanId == null || StringUtils.isBlank(userCode) || StringUtils.isBlank(spRepayType)
					|| StringUtils.isBlank(spRepayState)) {
				logger.info("特殊还款接口调用失败：缺少必要参数userCode、spRepayType或spRepayState!");
				json = MessageUtil.returnErrorMessage("缺少必要参数!");
				response.getWriter().write(JSONUtil.toJSON(json));
				return;
			}
			if (!(SpecialRepaymentStateCoreEnum.application.name().equals(spRepayState) || SpecialRepaymentStateCoreEnum.cancel
					.name().equals(spRepayState))) {
				logger.info("特殊还款接口调用失败：参数spRepayState传入值有误!");
				json = MessageUtil.returnErrorMessage("参数spRepayState传入值有误!");
				response.getWriter().write(JSONUtil.toJSON(json));
				return;
			}
			if (!(SpecialRepaymentTypeCoreEnum.onetime.name().equals(spRepayType)
					|| SpecialRepaymentTypeCoreEnum.reduction.name().equals(spRepayType) || SpecialRepaymentTypeCoreEnum.advanceDeduct
					.name().equals(spRepayType))) {
				logger.info("特殊还款接口调用失败：参数spRepayType传入值有误!");
				json = MessageUtil.returnErrorMessage("参数spRepayType传入值有误!");
				response.getWriter().write(JSONUtil.toJSON(json));
				return;
			}
			if (SpecialRepaymentTypeCoreEnum.reduction.name().equals(spRepayType) && reliefAmount == null) {
				logger.info("特殊还款接口调用失败：参数spRepayType值为[reduction]时，reliefAmount参数不能为空！");
				json = MessageUtil.returnErrorMessage("参数spRepayType值为[reduction]时，reliefAmount参数不能为空！");
				response.getWriter().write(JSONUtil.toJSON(json));
				return;
			}
			ComEmployee employee = comEmployeeService.findByUserCode(userCode);
			if (employee == null) {
				logger.info("特殊还款接口调用失败：参数userCode对应的员工信息不存在！");
				json = MessageUtil.returnErrorMessage("参数userCode对应的员工信息不存在！");
				response.getWriter().write(JSONUtil.toJSON(json));
				return;
			}
			VLoanInfo loan = vLoanInfoService.findByLoanId(loanId);
			if (loan == null) {
				logger.info("特殊还款接口调用失败：参数loanId对应的借款信息不存在！");
				json = MessageUtil.returnErrorMessage("参数loanId对应的借款信息不存在！");
				response.getWriter().write(JSONUtil.toJSON(json));
				return;
			}
			// 接口参数校验 end
			json = specialRepaymentCoreService.handleSpecialRepayment(paramsVo, employee);
			
			
		} catch (PlatformException ex) {
			ex.printStackTraceExt(logger);
			json = MessageUtil.returnErrorMessage(ex.getMessage());
		} catch (Exception ex) {
			logger.error(ex,ex);
			json = MessageUtil.returnErrorMessage("系统忙");
		}
		response.getWriter().write(JSONUtil.toJSON(json));
		logger.info("特殊还款接口调用完毕！");
		return;
	}

	/**
	 * 提前扣款更新
	 * 
	 * @param request
	 *            请求信息对象
	 * @param response
	 *            响应信息对象
	 * @throws IOException
	 */
	@RequestMapping("/updateSpecialRepayment")
	@ResponseBody
	public void updateSpecialRepayment(HttpServletRequest request, HttpServletResponse response,
			SpecialRepayParamsVO paramsVo) throws IOException {
		logger.info("提前扣款时间更新接口传入参数：" + paramsVo.toString());
		Map<String, Object> json = new HashMap<String, Object>();

		if (StringUtils.isBlank(paramsVo.getUserCode()) || StringUtils.isBlank(paramsVo.getEffectiveDate())
				|| paramsVo.getLoanId() == null) {
			logger.info("提前扣款时间更新接口参数错误：userCode，loanId，effectiveDate参数缺失！");
			json = MessageUtil.returnErrorMessage("失败:缺少必要参数userCode,loanId或effectiveDate");
			response.getWriter().write(JSONUtil.toJSON(json));
			return;
		}

		json = specialRepaymentCoreService.updateSpecialRepayment(paramsVo);
		response.getWriter().write(JSONUtil.toJSON(json));
		return;
	}

	/**
	 * 特殊还款查询接口：提前结清、提前扣款、费用减免
	 * 
	 * @param request
	 *            请求信息对象
	 * @param response
	 *            响应信息对象
	 * @throws IOException
	 */
	@RequestMapping("/querySpecialRepayment")
	@ResponseBody
	public void querySpecialRepayment(HttpServletRequest request, HttpServletResponse response,
			SpecialRepayParamsVO paramsVo) throws IOException {
		logger.info("特殊还款查询接口传入参数：" + paramsVo.toString());
		Map<String, Object> json = new HashMap<String, Object>();
		String userCode = paramsVo.getUserCode();
		String spRepayType = paramsVo.getSpRepayType();
		String name = paramsVo.getName();
		String idnum = paramsVo.getIdnum();
		String mphone = paramsVo.getMphone();
		Long salesmanId = paramsVo.getSalesmanId();
		Long loanId = paramsVo.getLoanId();

		paramsVo.setMax(Math.min(paramsVo.getMax() != null ? paramsVo.getMax() : 10, 100));
		;
		paramsVo.setOffset(Math.min(paramsVo.getOffset() != null ? paramsVo.getOffset() : 0, 100));

		// 参数校验begin
		if (StringUtils.isBlank(userCode) || StringUtils.isBlank(spRepayType)) {
			json = MessageUtil.returnErrorMessage("失败：缺少必要参数！");
			response.getWriter().write(JSONUtil.toJSON(json));
			return;
		}

		List<SpecialRepaymentVO> queryResult = new ArrayList<SpecialRepaymentVO>();
		if (SpecialRepaymentTypeCoreEnum.onetime.name().equals(spRepayType)) {// 一次性还款

			if (StringUtils.isBlank(name) && StringUtils.isBlank(idnum) && StringUtils.isBlank(mphone)
					&& salesmanId == null && loanId == null) {
				json = MessageUtil.returnQuerySuccessMessage(paramsVo.getMax().intValue(), queryResult, 0,
						"specialRepaymentVOs");
				response.getWriter().write(JSONUtil.toJSON(json));
				return;
			}
		} else if (SpecialRepaymentTypeCoreEnum.reduction.name().equals(spRepayType)) {// 费用减免
			if (StringUtils.isBlank(idnum) && loanId == null) {
				json = MessageUtil.returnQuerySuccessMessage(paramsVo.getMax().intValue(), queryResult, 0,
						"specialRepaymentVOs");
				response.getWriter().write(JSONUtil.toJSON(json));
				return;
			}
		} else {
			json = MessageUtil.returnErrorMessage("失败：参数spRepayType接收到的值不正确！");
			response.getWriter().write(JSONUtil.toJSON(json));
			return;
		}

		ComEmployee employee = comEmployeeService.findByUserCode(userCode);
		if (employee == null) {
			json = MessageUtil.returnErrorMessage("参数userCode对应的员工信息不存在！");
			response.getWriter().write(JSONUtil.toJSON(json));
			return;
		}
		// 参数校验end

		// 业务处理
		json = specialRepaymentCoreService.querySpecialRepayment(paramsVo);
		response.getWriter().write(JSONUtil.toJSON(json));
	}

	/**
	 * 罚息减免试算查询接口
	 * 
	 * @throws IOException
	 */
	@RequestMapping("/queryReliefPenalty")
	@ResponseBody
	public void queryReliefPenalty(HttpServletRequest request, HttpServletResponse response,
			SpecialRepayParamsVO paramsVo) throws IOException {

		AttachmentResponseInfo<Map<String, Object>> attachmentResponseInfo = null;

		logger.info("特殊还款查询接口传入参数：" + paramsVo.toString());
		try {
			String userCode = paramsVo.getUserCode();
			BigDecimal reliefAmount = paramsVo.getReliefAmount();
			Long loanId = paramsVo.getLoanId();
			if ((userCode == null) || (reliefAmount == null) || (loanId == null)) {// 判断是否缺少必要参数
				throw new PlatformException(ResponseEnum.FULL_MSG, "缺少必要参数！").applyLogLevel(LogLevel.WARN);
			}

			if (BigDecimal.ZERO.compareTo(reliefAmount) >= 0) {
				throw new PlatformException(ResponseEnum.FULL_MSG, "减免金额必须大于0元！").applyLogLevel(LogLevel.WARN);
			}

			Map<String, Object> data = specialRepaymentCoreService.queryReliefPenalty(paramsVo);
			attachmentResponseInfo = new AttachmentResponseInfo<Map<String, Object>>(ResponseEnum.SYS_SUCCESS);
			attachmentResponseInfo.setAttachment(data);

		} catch (PlatformException ex) {
			ex.printStackTraceExt(logger);
			attachmentResponseInfo = ex.<Map<String, Object>> toAttachmentResponseInfo();
		} catch (Exception ex) {
			logger.error("queryReliefPenalty方法：失败:" + ex.getMessage(), ex);
			attachmentResponseInfo = new AttachmentResponseInfo<Map<String, Object>>(ResponseEnum.SYS_FAILD);
		}
		response.getWriter().write(BaseController.toResponseJSON(attachmentResponseInfo));
	}

	@ResponseBody
	@RequestMapping(value = "/getMaxReliefAmount")
	public String getMaxReliefAmount(SpecialRepayParamsVO specialRepayParamsVO, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> json = new HashMap<String, Object>();
		try {
			logger.info(Strings.format("来着ip：{0}的请求参数：{1}", request.getRemoteHost(), JSONUtil.toJSON(specialRepayParamsVO)));
			if (Strings.isEmpty(specialRepayParamsVO.getLoanId())) {
				json = MessageUtil.returnErrorMessage("参数loanId为空！");
				return JSONUtil.toJSON(json);
			}

			Long loanId = specialRepayParamsVO.getLoanId();
			String reliefType = afterLoanService.isOneTimeRepayment(loanId) ? SpecialRepaymentApplyTeyps.结清减免.getCode() : SpecialRepaymentApplyTeyps.一般减免.getCode();
			MaxReliefAmountParamVo maxReliefAmountParamVo = new MaxReliefAmountParamVo();
			maxReliefAmountParamVo.setLoanId(loanId);
			maxReliefAmountParamVo.setReliefType(reliefType);
			maxReliefAmountParamVo.setTradeDate(Dates.getCurrDate());
			//最大减免金额
			BigDecimal maxReleifAmount = specialRepaymentApplyService.getMaxReliefAmount(maxReliefAmountParamVo);
			json.put("code", Constants.SUCCESS_CODE);
			json.put("message", "成功");
			json.put("maxReleifAmount", maxReleifAmount);
		} catch (PlatformException pe) {
			logger.info("获取最大减免金额错误：{}"+pe.getMessage());
			json = MessageUtil.returnErrorMessage(Strings.format("获取最大减免金额错误{0}",pe.getMessage()));
			return JSONUtil.toJSON(json);
		} catch (Exception e) {
			logger.error("获取最大减免金额异常..."+ e.getMessage());
			json = MessageUtil.returnErrorMessage("系统忙");
			return JSONUtil.toJSON(json);
		}
		return JSONUtil.toJSON(json);
	}
}
