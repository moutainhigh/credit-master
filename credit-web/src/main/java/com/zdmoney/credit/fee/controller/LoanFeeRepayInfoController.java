package com.zdmoney.credit.fee.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.fee.domain.LoanFeeRepayInfo;
import com.zdmoney.credit.fee.service.pub.ILoanFeeRepayInfoService;
import com.zdmoney.credit.framework.controller.BaseController;

/**
 * 相关 借款收费记账 逻辑处理 接收前端请求
 * 
 * @author Ivan
 *
 */
@Controller
@RequestMapping("/fee/loanFeeRepayInfo")
public class LoanFeeRepayInfoController extends BaseController {

	protected static Log logger = LogFactory.getLog(LoanFeeRepayInfoController.class);

	@Autowired
	@Qualifier("loanFeeRepayInfoServiceImpl")
	ILoanFeeRepayInfoService loanFeeRepayInfoServiceImpl;

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
	 * 查询服务费收费流水数据
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
	public String search(long feeId, HttpServletRequest request, HttpServletResponse response)
			throws ParseException {
		/** 登陆者 **/
		User user = UserContext.getUser();
		LoanFeeRepayInfo loanFeeRepayInfo = new LoanFeeRepayInfo();
		loanFeeRepayInfo.setFeeId(feeId);

		Pager pager = new Pager();
		List repayList = loanFeeRepayInfoServiceImpl.findListByVo(loanFeeRepayInfo);
		pager.setResultList(repayList);
		pager.setTotalCount(repayList.size());
		pager.setPage(0);
		return toPGJSONWithCode(pager);
	}

}
