package com.zdmoney.credit.fee.controller;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zdmoney.credit.common.constant.LoanFeeStateEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.util.Assert;
import com.zdmoney.credit.common.vo.AttachmentResponseInfo;
import com.zdmoney.credit.fee.domain.LoanFeeInfo;
import com.zdmoney.credit.fee.service.pub.ILoanFeeInfoService;
import com.zdmoney.credit.framework.controller.BaseController;

/**
 * 相关 借款收费 逻辑处理 接收前端请求
 * 
 * @author Ivan
 *
 */
@Controller
@RequestMapping("/fee/loanFeeInfo")
public class LoanFeeInfoController extends BaseController {

	protected static Log logger = LogFactory.getLog(LoanFeeInfoController.class);

	@Autowired
	@Qualifier("loanFeeInfoServiceImpl")
	ILoanFeeInfoService loanFeeInfoServiceImpl;

	/**
	 * 跳转主页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/jumpPage")
	public String jumpPage(HttpServletRequest request, HttpServletResponse response) {
		return "xxx";
	}

	/**
	 * 实时查询未收服务费
	 * 
	 * @param feeId
	 *            服务费编号
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryFeeAmount")
	@ResponseBody
	public String queryFeeAmount(Long feeId, HttpServletRequest request, HttpServletResponse response) {
		AttachmentResponseInfo<Object> attachmentResponseInfo = null;
		try {
			LoanFeeInfo loanFeeInfo = loanFeeInfoServiceImpl.findById(feeId);
			Assert.notNull(loanFeeInfo, ResponseEnum.FULL_MSG, "FeeId：" + feeId + " 未找到记录");
			if (LoanFeeStateEnum.已收取.getValue().equals(loanFeeInfo.getState())) {
				throw new PlatformException(ResponseEnum.FULL_MSG, "服务费已结清");
			}
			BigDecimal amount = loanFeeInfo.getUnpaidAmount();
			attachmentResponseInfo = new AttachmentResponseInfo<Object>(ResponseEnum.SYS_SUCCESS);
			attachmentResponseInfo.setAttachment(amount);
		} catch (PlatformException ex) {
			ex.printStackTraceExt(logger);
			attachmentResponseInfo = ex.toAttachmentResponseInfo();
		} catch (Exception ex) {
			/** 系统忙 **/
			logger.error(ex, ex);
			attachmentResponseInfo = new AttachmentResponseInfo<Object>(ResponseEnum.SYS_FAILD);
		}
		return toResponseJSON(attachmentResponseInfo);
	}
}
