package com.zdmoney.credit.loan.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.zdmoney.credit.common.constant.SysActionLogTypeEnum;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.util.NumberToChinese;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.vo.AttachmentResponseInfo;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.loan.domain.LoanContract;
import com.zdmoney.credit.loan.domain.LoanSpecialRepayment;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.ILoanContractService;
import com.zdmoney.credit.loan.service.pub.ILoanSpecialRepaymentService;
import com.zdmoney.credit.loan.service.pub.ILoanTransferInfoService;
import com.zdmoney.credit.offer.domain.OfferRepayInfo;
import com.zdmoney.credit.offer.service.pub.IOfferRepayInfoService;

@Controller
@RequestMapping("/system/loan")
public class LoanAdvanceRepaymentController extends BaseController {
	@Autowired
	@Qualifier("loanSpecialRepaymentServiceImpl")
	ILoanSpecialRepaymentService loanSpecialRepaymentServiceImpl;
	@Autowired
	@Qualifier("loanContractServiceImpl")
	ILoanContractService loanContractServiceImpl;
	@Autowired
	@Qualifier("offerRepayInfoServiceImpl")
	IOfferRepayInfoService offerRepayInfoServiceImpl;
	@Autowired 
	ILoanTransferInfoService loanTransferInfoServiceImpl;
	
	
	/**
     * 撤销权限url
     */
    private static final String CANCEL_PERMISSION= "/operation/cancelPermission";
    
    
    /**
     * 申请权限url
     */
    private static final String APPLY_PERMISSION = "/operation/applyPermission";
	
	/**
	 * 门店结算证明
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/listPage")
	public ModelAndView listPage(Model model) {
		ModelAndView modelAndView = new ModelAndView(
				"/loan/LoanAdvanceRepayment");
		User user = UserContext.getUser();		
		// 是否有撤销权限
        boolean isCanConfirmReceive = user.ifAnyGranted(CANCEL_PERMISSION);
        // 是否有申请权限
        boolean isApplyConfirmReceive = user.ifAnyGranted(APPLY_PERMISSION);
        modelAndView.addObject("isCanConfirmReceive", isCanConfirmReceive);
        modelAndView.addObject("isApplyConfirmReceive", isApplyConfirmReceive);
		return modelAndView;
	}

	/**
	 * 门店结算证明
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/listLoanSpecialRepaymentPage")
	@ResponseBody
	public String listLoanSpecialRepaymentPage(VLoanInfo vloanInfo,int rows, int page) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		User user = UserContext.getUser();
		paramMap.put("code", user.getOrgCode());
		Pager pager = new Pager();
		pager.setPage(page);
		pager.setRows(rows);
		pager.setSort("ASC");
		paramMap.put("name", vloanInfo.getPersonInfo().getName());
		paramMap.put("idNum", vloanInfo.getPersonInfo().getIdnum());
		paramMap.put("contractNum", vloanInfo.getContractNum());
		paramMap.put("pager", pager);
		try {
			pager = loanSpecialRepaymentServiceImpl
					.findSpecialRepaymentList(paramMap);
			List list = pager.getResultList();
			List<Map> valueList = new ArrayList<Map>();
			for (int i = 0; i < list.size(); i++) {
				VLoanInfo v = (VLoanInfo) list.get(i);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("name", v.getPersonInfo().getName());
				map.put("idnum", v.getPersonInfo().getIdnum());
				// 借款类型
				map.put("loanType", v.getLoanType());
				// 状态
				map.put("loanState", v.getLoanState());
				map.put("signDate", v.getSignDate());
				map.put("loanid", v.getId());
				map.put("money", v.getMoney());
				map.put("personId", v.getPersonInfo().getId());
				//合同编号
				map.put("contractNum", v.getContractNum());
				int count = loanSpecialRepaymentServiceImpl
						.findSpecialRepaymentByLoanId(v.getId());
				if (count == 0) {
					map.put("flage", true);
				} else {
					map.put("flage", false);
				}
				valueList.add(map);
			}

			pager.setResultList(valueList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return toPGJSONWithCode(pager);
	}

	/**
	 * 申请
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/insertLoanSpecialRepayment/{id}")
	@ResponseBody
	public String insertLoanSpecialRepayment(@PathVariable String id,
			HttpServletRequest request, HttpServletResponse response) {
		AttachmentResponseInfo<Object> attachmentResponseInfo = new AttachmentResponseInfo<Object>();
		User user = UserContext.getUser();
		String ids[] = id.split(",");
		String loanId = ids[0];
		String personId = ids[1];
		String money = ids[2];
		
		//检查借款是否已转让过
		boolean flag = loanTransferInfoServiceImpl.isLoanTransfer(null,Long.parseLong(loanId));
		if(!flag){
			attachmentResponseInfo.setResMsg(Strings.errorMsg);
			return toResponseJSON(attachmentResponseInfo);
		}
		
		// 根据申请人的id和 合同状态为结清或者预结清查询，，如果有结清的合同，，没有则--尚存在未结清借款，不允许申请！
		int count = loanSpecialRepaymentServiceImpl
				.findLoanByUserIdAndLoanState(personId);
		if (count < 1) {
			attachmentResponseInfo.setResMsg("尚存在未结清借款，不允许申请!");
			return toResponseJSON(attachmentResponseInfo);
		}

		// 判断合同是否（loanid +SpecialRepaymentState=申请结算单） --结清证明已申请，请勿重复申请！
		LoanSpecialRepayment loanspe = new LoanSpecialRepayment();
		loanspe.setLoanId(Long.parseLong(loanId));
		loanspe = loanSpecialRepaymentServiceImpl
				.findLoanSpecialRepaymentByVO(loanspe);
		if (loanspe != null) {
			if (loanspe.getSpecialRepaymentState().equals("申请结算单")) {
				attachmentResponseInfo.setResMsg("结清证明已申请，请勿重复申请！");
				return toResponseJSON(attachmentResponseInfo);
			}
		}
		if (loanspe == null) {
			loanspe = new LoanSpecialRepayment();
		}
		loanspe.setLoanId(Long.parseLong(loanId));
		loanspe.setMemo("由" + user.getName() + "申请结算单");
		loanspe.setSpecialRepaymentState("申请结算单");
		loanspe.setSpecialRepaymentType("结算单");
		loanspe.setProposerId(user.getId());
		loanspe.setRequestDate(new Date());
		loanspe.setAmount(BigDecimal.valueOf(Long.parseLong(money)));
		loanspe.setCreator(user.getName());
		loanSpecialRepaymentServiceImpl.insert(loanspe);
		attachmentResponseInfo.setResCode(ResponseEnum.SYS_SUCCESS.getCode());
		attachmentResponseInfo.setResMsg(ResponseEnum.SYS_SUCCESS.getDesc());
		attachmentResponseInfo.setResMsg("申请成功");
		this.createLog(request, SysActionLogTypeEnum.新增, "结清证明", "结清证明申请");
		return toResponseJSON(attachmentResponseInfo);
	}

	/**
	 * 撤销
	 */
	@RequestMapping("/cxLoanSpecialRepayment/{loanId}")
	@ResponseBody
	public String cxLoanSpecialRepayment(@PathVariable String loanId,
			HttpServletRequest request, HttpServletResponse response) {
		AttachmentResponseInfo<Object> attachmentResponseInfo = new AttachmentResponseInfo<Object>();
		User user = UserContext.getUser();
		// 判断合同是否（loanid +SpecialRepaymentState=申请结算单） --结清证明已申请，请勿重复申请！
		LoanSpecialRepayment loanspe = new LoanSpecialRepayment();
		loanspe.setLoanId(Long.parseLong(loanId));
		loanspe.setSpecialRepaymentState("申请结算单");
		loanspe.setSpecialRepaymentType("结算单");
		loanspe = loanSpecialRepaymentServiceImpl
				.findLoanSpecialRepaymentByVO(loanspe);
		if (loanspe == null) {
				attachmentResponseInfo.setResMsg("结清证明可能已撤销！无需重复操作！");
				return toResponseJSON(attachmentResponseInfo);
		}
		
		loanspe.setLoanId(Long.parseLong(loanId));
		loanspe.setSpecialRepaymentState("申请结算单");
		loanspe.setSpecialRepaymentType("结算单");
		loanspe = loanSpecialRepaymentServiceImpl
				.findLoanSpecialRepaymentByVO(loanspe);
		
		loanspe.setMemo("由" + user.getName() + "撤销结算单");
		loanspe.setSpecialRepaymentState("撤销结算单");
		loanspe.setRequestDate(new Date());
		loanSpecialRepaymentServiceImpl.update(loanspe);
		this.createLog(request, SysActionLogTypeEnum.更新, "结清证明", "结清证明撤销");
		attachmentResponseInfo.setResCode(ResponseEnum.SYS_SUCCESS.getCode());
		attachmentResponseInfo.setResMsg(ResponseEnum.SYS_SUCCESS.getDesc());
		attachmentResponseInfo.setResMsg("撤销成功");
		return toResponseJSON(attachmentResponseInfo);
	}

	/**
	 * 总部结算证明
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/ZBlistPage")
	public ModelAndView ZBlistPage(Model model) {
		ModelAndView modelAndView = new ModelAndView(
				"/loan/ZBLoanAdvanceRepayment");

		return modelAndView;
	}

	/**
	 * 总部结算证明
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/ZBlistLoanSpecialRepaymentPage")
	@ResponseBody
	public String ZBlistLoanSpecialRepaymentPage(VLoanInfo vloanInfo,int rows, int page) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		User user = UserContext.getUser();
		paramMap.put("code",user.getOrgCode() );
		Pager pager = new Pager();
		pager.setPage(page);
		pager.setRows(rows);
		pager.setSort("ASC");
		paramMap.put("name", vloanInfo.getPersonInfo().getName());
		paramMap.put("idNum", vloanInfo.getPersonInfo().getIdnum());
		paramMap.put("contractNum", vloanInfo.getContractNum());
		paramMap.put("pager", pager);
		try {
			pager = loanSpecialRepaymentServiceImpl
					.ZBfindSpecialRepaymentList(paramMap);
			List list = pager.getResultList();
			List<Map> valueList = new ArrayList<Map>();
			for (int i = 0; i < list.size(); i++) {
				VLoanInfo v = (VLoanInfo) list.get(i);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("name", v.getPersonInfo().getName());
				map.put("idnum", v.getPersonInfo().getIdnum());
				// 借款类型
				map.put("loanType", v.getLoanType());
				// 状态
				map.put("loanState", v.getLoanState());
				map.put("signDate", v.getSignDate());
				map.put("loanid", v.getId());
				map.put("money", v.getMoney());
				map.put("personId", v.getPersonInfo().getId());
				map.put("creator", v.getCreator());
				map.put("contractNum", v.getContractNum());

				int count = loanSpecialRepaymentServiceImpl
						.findSpecialRepaymentByLoanId(v.getId());
				if (count == 0) {
					map.put("flage", true);
				} else {
					map.put("flage", false);
				}
				valueList.add(map);
			}
			pager.setResultList(valueList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return toPGJSONWithCode(pager);
	}

	/**
	 * 打印页面
	 * @param model
	 * @return
	 */
	@RequestMapping("/printPage/{loanId}")
	public ModelAndView printPage(@PathVariable String loanId) {
		ModelAndView modelAndView = new ModelAndView("loan/printLoanAdvanceRepaymentPage");
		modelAndView.addObject("loanId", loanId);
		return modelAndView;
	}
	
	/**
	 * 打印功能
	 * @param model
	 * @return
	 */
	@RequestMapping("/print/{loanId}")
	public ModelAndView print(@PathVariable String loanId) {
		ModelAndView modelAndView = new ModelAndView("loan/printLoanAdvanceRepayment");
		// 根据合同号查询还款信息--还款人，还款时间，还款金额
		// LOAN_CONTRACT --BORROWER_NAME
		// OFFER_REPAY_INFO --AMOUNT TRADE_TIME
		// 根据合同号查询借款信息--借款时间，借款地址合同编号
		// LOAN_CONTRACT SIGN_DATE SIGNING_SITE CONTRACT_NUM
		// LOAN_SPECIAL_REPAYMENT REQUEST_DATE
		LoanContract loanContract = loanContractServiceImpl
				.findLoanContractByLoanId(Long.parseLong(loanId));
		OfferRepayInfo offerRepayInfo = offerRepayInfoServiceImpl
				.getLoanLastRepayInfoById(Long.parseLong(loanId));
		
		
		LoanSpecialRepayment loanSpecialRepayment = loanSpecialRepaymentServiceImpl
				.findbyLoanAndType(Long.parseLong(loanId),null, null);
		modelAndView.addObject("borrowerName", loanContract.getBorrowerName());
		String amount = NumberToChinese.numberToChinese(offerRepayInfo
				.getAmount().longValue());
		Calendar c = Calendar.getInstance();
		c.setTime(offerRepayInfo.getTradeDate());
		modelAndView.addObject("year", c.get(Calendar.YEAR));
		modelAndView.addObject("month",
				((c.get(Calendar.MONTH) + 1 + 100) + "").substring(1));
		modelAndView.addObject("dayOfMonth",
				(((c.get(Calendar.DAY_OF_MONTH) + 100) + "").substring(1)));
		modelAndView.addObject("dxamount", amount);
		modelAndView.addObject("amount", offerRepayInfo.getAmount());
		
		//借款时间
		c.setTime(loanContract.getSignDate());
		modelAndView.addObject("ryear", c.get(Calendar.YEAR));
		modelAndView.addObject("rmonth",
				((c.get(Calendar.MONTH) + 1 + 100) + "").substring(1));
		modelAndView.addObject("rdayOfMonth",
				(((c.get(Calendar.DAY_OF_MONTH) + 100) + "").substring(1)));
		modelAndView.addObject("signingSite", loanContract.getSigningSite());
		modelAndView.addObject("contractNum", loanContract.getContractNum());
		
		//最后时间
		c.setTime(new Date());
		modelAndView.addObject("zyear", c.get(Calendar.YEAR));
		modelAndView.addObject("zmonth",
				((c.get(Calendar.MONTH) + 1 + 100) + "").substring(1));
		modelAndView.addObject("zdayOfMonth",
				(((c.get(Calendar.DAY_OF_MONTH) + 100) + "").substring(1)));
		return modelAndView;
	}

}
