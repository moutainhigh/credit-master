package com.zdmoney.credit.loan.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.zdmoney.credit.common.constant.SpecialRepaymentStateEnum;
import com.zdmoney.credit.common.constant.SpecialRepaymentTypeEnum;
import com.zdmoney.credit.common.constant.SysActionLogTypeEnum;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.util.Assert;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.vo.AttachmentResponseInfo;
import com.zdmoney.credit.core.calculator.factory.CalculatorFactoryImpl;
import com.zdmoney.credit.core.calculator.pub.ICalculator;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.loan.domain.LoanBase;
import com.zdmoney.credit.loan.domain.LoanContract;
import com.zdmoney.credit.loan.domain.LoanProcessHistory;
import com.zdmoney.credit.loan.domain.LoanRepaymentDetail;
import com.zdmoney.credit.loan.domain.LoanSpecialRepayment;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.IAfterLoanService;
import com.zdmoney.credit.loan.service.pub.ILoanBaseService;
import com.zdmoney.credit.loan.service.pub.ILoanProcessHistoryService;
import com.zdmoney.credit.loan.service.pub.ILoanSpecialRepaymentService;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import com.zdmoney.credit.offer.service.pub.IOfferCreateService;
import com.zdmoney.credit.system.domain.ComEmployeeRole;
import com.zdmoney.credit.system.domain.ComRole;
import com.zdmoney.credit.system.service.pub.IComEmployeeRoleService;
import com.zdmoney.credit.system.service.pub.IComRoleService;

/**
 * 费用减免申请
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/system/loan")
public class LoanBaseController extends BaseController {
	protected static Log logger = LogFactory
			.getLog(LoanFilesInfoController.class);

	@Autowired
	@Qualifier("loanSpecialRepaymentServiceImpl")
	ILoanSpecialRepaymentService loanSpecialRepaymentServiceImpl;

	@Autowired
	@Qualifier("comEmployeeRoleServiceImpl")
	IComEmployeeRoleService comEmployeeRoleServiceImpl;

	@Autowired
	@Qualifier("loanBaseServiceImpl")
	ILoanBaseService loanBaseServiceImpl;

	@Autowired
	@Qualifier("loanProcessHistoryServiceImpl")
	ILoanProcessHistoryService loanProcessHistoryServiceImpl;

	@Autowired
	@Qualifier("afterLoanServiceImpl")
	IAfterLoanService afterLoanServiceImpl;
	
	@Autowired
	@Qualifier("comRoleServiceImpl")
	IComRoleService comRoleServiceImpl;
	
	@Autowired
	IVLoanInfoService vLoanInfoServiceImpl;
	
	@Autowired
	private IOfferCreateService offerCreateService;
	
	@RequestMapping("/findLoanBase")
	public ModelAndView findLoanBase(Model model) {
		ModelAndView modelAndView = new ModelAndView("/loan/loanFYJM");

		return modelAndView;
	}

	/**
	 * 费用减免申请
	 * 
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/findLoanBaseList")
	@ResponseBody
	public String findLoanBaseList(LoanBase loanBase, int rows, int page) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		User user = UserContext.getUser();
		paramMap.put("code", user.getOrgCode());
		LoanContract contract=loanBase.getLoanContract();
		if(contract!=null){
			paramMap.put("borrowerName", loanBase.getLoanContract().getBorrowerName());
			paramMap.put("idnum", loanBase.getLoanContract().getIdnum());
		}
		paramMap.put("salesMan", loanBase.getSalesmanId());
		paramMap.put("mphone", loanBase.getPersonInfo().getMphone());
		paramMap.put("contractNum", loanBase.getContractNum());
		Pager pager = new Pager();
		pager.setPage(page);
		pager.setRows(rows);
		pager.setSort("ASC");
		paramMap.put("pager", pager);
		try {
			pager = loanBaseServiceImpl.findLoanBaseList(paramMap);
			List<LoanBase> list = pager.getResultList();
			for (int i = 0; i < list.size(); i++) {
				LoanBase loanBaseVo = (LoanBase) list.get(i);
				Long loanId = loanBaseVo.getId();
				VLoanInfo vLoanInfo = vLoanInfoServiceImpl.findByLoanId(loanId);
				Assert.notNull(vLoanInfo, ResponseEnum.FULL_MSG, "未找到债权数据loanId:" + loanId);
				
				/** 获取计算器实例 **/
				ICalculator calculatorInstace = CalculatorFactoryImpl.createCalculator(vLoanInfo);
				
				//查询剩余利息
				BigDecimal remaining=afterLoanServiceImpl.getRemainingInterest(vLoanInfo,new Date());
				loanBaseVo.setRemainingInterest(remaining);
				
				/** 获取违约金 **/
				List<LoanRepaymentDetail> repayList= afterLoanServiceImpl.getAllInterestOrLoan(new Date(),loanBaseVo.getId());
				BigDecimal penalty = calculatorInstace.getPenalty(loanId, repayList, vLoanInfo);
				loanBaseVo.setPenalty(penalty);
				//剩余本息和
				BigDecimal remain=	loanBaseVo.getRemainingInterest();
				BigDecimal pact=loanBaseVo.getLoanProduct().getResidualPactMoney();
				loanBaseVo.setBxcount(remain.add(pact));
				//获取挂账总额
				loanBaseVo.setAccAmount(afterLoanServiceImpl.getAccAmount(loanBaseVo.getId()));
				/*Long currentTerm=	loanBaseVo.getCurrentTerm();
				if(currentTerm==null){currentTerm=0l;}
				//当前期数
				loanBaseVo.setCurrentTerm((loanBaseVo.getLoanProduct().getTime()-currentTerm)+1);*/
				//是否申请过
				int count = loanSpecialRepaymentServiceImpl
						.findLoanSpecialRepaymentByStateAndLoanId(loanBaseVo.getId());
				if (count > 0) {
					loanBaseVo.setIsApply("已申请");
				}else{ 
					loanBaseVo.setIsApply("未申请");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return toPGJSONWithCode(pager);
	}

	//申请之前判断是否同一天
	@RequestMapping("/findYearsqFYJM/{loanId}")
	@ResponseBody
	public String findYearsqFYJM(@PathVariable String loanId, LoanSpecialRepayment loanspe, HttpServletRequest request, HttpServletResponse response) {
		AttachmentResponseInfo<Object> attachmentResponseInfo = new AttachmentResponseInfo<Object>();
//		User user = UserContext.getUser();
		//判断该借款人当天是否有在审批中的记录，如果有不能再发起
		boolean b=loanSpecialRepaymentServiceImpl.findSpecialRepaymentByUserAndDate(loanId);
		if(b){
			attachmentResponseInfo.setResCode(ResponseEnum.SYS_SUCCESS.getCode());
			attachmentResponseInfo.setResMsg(ResponseEnum.SYS_SUCCESS.getDesc());
			attachmentResponseInfo.setResMsg("同一借款人当天只能有一个减免申请！");
			return toResponseJSON(attachmentResponseInfo);
		}
		return toResponseJSON(attachmentResponseInfo);
	}

	/**
	 * 申请提交
	 * @param model
	 * @return
	 */
	@SuppressWarnings("static-access")
	@RequestMapping("/insertLoanSpecialRepaymentFYJM/{loanId}")
	@ResponseBody
	public String insertLoanSpecialRepaymentFYJM(@PathVariable String loanId,
			LoanSpecialRepayment loanspe, HttpServletRequest request,
			HttpServletResponse response) {
		AttachmentResponseInfo<Object> attachmentResponseInfo = new AttachmentResponseInfo<Object>();
		User user = UserContext.getUser();
		//判断该借款人当天是否有在审批中的记录，如果有不能再发起
		boolean b=loanSpecialRepaymentServiceImpl.findSpecialRepaymentByUserAndDate(loanId);
		if(b){
			attachmentResponseInfo.setResCode(ResponseEnum.FILE_ERROR_TYPE
					.getCode());
			attachmentResponseInfo
					.setResMsg(ResponseEnum.FILE_ERROR_TYPE.getDesc());
			attachmentResponseInfo.setResMsg("同一借款人当天只能有一个减免申请！");
			return toResponseJSON(attachmentResponseInfo);
		}
	
		loanspe.setLoanId(Long.parseLong(loanId));
		loanspe.setSpecialRepaymentState(SpecialRepaymentStateEnum.区域总审批.getValue());
		loanspe.setSpecialRepaymentType(SpecialRepaymentTypeEnum.正常费用减免.getValue());
		loanspe.setProposerId(user.getId());
		loanspe.setRequestDate(new Date());
		loanspe.setCreator(user.getName());
		try {
			loanSpecialRepaymentServiceImpl.insert(loanspe);
			// 写入日志
			LoanProcessHistory loanProcessHistory = new LoanProcessHistory();
			loanProcessHistory.setLoanId(Long.parseLong(loanId));
			loanProcessHistory.setLoanState(SpecialRepaymentTypeEnum.正常费用减免.getValue());
			loanProcessHistory.setLoanFlowState(SpecialRepaymentStateEnum.区域总审批.getValue());
			loanProcessHistory.setContent("提交减免申请，金额:" + loanspe.getAmount());
			loanProcessHistoryServiceImpl.insert(loanProcessHistory);
			this.createLog(request, SysActionLogTypeEnum.新增, "费用减免申请", "费用减免申请");
			attachmentResponseInfo.setResCode(ResponseEnum.SYS_SUCCESS.getCode());
			attachmentResponseInfo.setResMsg(ResponseEnum.SYS_SUCCESS.getDesc());
			attachmentResponseInfo.setResMsg("申请成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return toResponseJSON(attachmentResponseInfo);
	}

	/**
	 * 取消
	 * 
	 * @param model
	 * @return
	 */
	@SuppressWarnings("static-access")
	@RequestMapping("/updateLoanSpecialRepaymentFYJM/{loanId}")
	@ResponseBody
	public String updateLoanSpecialRepaymentFYJM(@PathVariable String loanId, HttpServletRequest request, HttpServletResponse response) {
		AttachmentResponseInfo<Object> attachmentResponseInfo = new AttachmentResponseInfo<Object>();
		User user = UserContext.getUser();
		try {
			LoanSpecialRepayment loanspe = new LoanSpecialRepayment();
			loanspe.setLoanId(Long.parseLong(loanId));
			loanspe = loanSpecialRepaymentServiceImpl.findSpecialRepaymentCount(loanspe);
			if (loanspe == null) {
				attachmentResponseInfo.setResCode(ResponseEnum.SYS_EXIST.getCode());
				attachmentResponseInfo.setResMsg(ResponseEnum.SYS_EXIST.getDesc());
				attachmentResponseInfo.setResMsg("合同还没有申请过，请重新选择");
				return toResponseJSON(attachmentResponseInfo);
			}
			// 根据id查询费用减免记录。修改state和修改人，时间
			loanspe.setSpecialRepaymentState("取消");
			loanspe.setSpecialRepaymentType("正常费用减免");
			loanspe.setProposerId(user.getId());
			loanspe.setRequestDate(new Date());
			loanSpecialRepaymentServiceImpl.update(loanspe);
			this.createLog(request, SysActionLogTypeEnum.新增, "费用减免申请", "费用减免取消");
			// 写入日志
			LoanProcessHistory loanProcessHistory = new LoanProcessHistory();
			loanProcessHistory.setLoanId(Long.parseLong(loanId));
			loanProcessHistory.setLoanState("正常费用减免");
			loanProcessHistory.setLoanFlowState("取消");
			loanProcessHistory.setContent("取消申请");
			loanProcessHistoryServiceImpl.insert(loanProcessHistory);
			attachmentResponseInfo.setResCode(ResponseEnum.SYS_SUCCESS.getCode());
			attachmentResponseInfo.setResMsg(ResponseEnum.SYS_SUCCESS.getDesc());
			attachmentResponseInfo.setResMsg("取消成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return toResponseJSON(attachmentResponseInfo);
	}

	/**
	 * 日志
	 */
	@SuppressWarnings("static-access")
	@RequestMapping("/logList/{loanId}")
	@ResponseBody
	public String logList(@PathVariable String loanId, int rows, int page, HttpServletRequest request, HttpServletResponse response) {
		AttachmentResponseInfo<Object> attachmentResponseInfo = new AttachmentResponseInfo<Object>();
		Pager pager = new Pager();
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			pager.setPage(page);
			pager.setRows(rows);
			pager.setSort("ASC");
			paramMap.put("loanId", loanId);
			paramMap.put("loanState", "正常费用减免");
			paramMap.put("pager", pager);
			pager = loanProcessHistoryServiceImpl.searchLoanProcessHistoryWithPg(paramMap);
			this.createLog(request, SysActionLogTypeEnum.新增, "费用减免申请", "费用减免日志查看");
			attachmentResponseInfo.setResCode(ResponseEnum.SYS_SUCCESS.getCode());
			attachmentResponseInfo.setResMsg(ResponseEnum.SYS_SUCCESS.getDesc());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return toPGJSONWithCode(pager);
	}

	/**
	 * 日志
	 */
	@RequestMapping("/log/{loanId}")
	public ModelAndView log(@PathVariable String loanId, HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("/loan/loanLog");
		request.setAttribute("loanId", loanId);
		return modelAndView;
	}

	/**
	 * 审批
	 */
	@RequestMapping("/approval")
	public ModelAndView approval(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("/loan/loanApproval");
		return modelAndView;
	}

	/**
	 * 费用减免审核 根据当前登陆的人查询角色
	 */
	@SuppressWarnings({ "static-access", "unchecked" })
	@RequestMapping("/approvalList")
	@ResponseBody
	public String approvalList(int rows, int page, HttpServletRequest request,
			HttpServletResponse response, LoanBase loanBase) {
		AttachmentResponseInfo<Object> attachmentResponseInfo = new AttachmentResponseInfo<Object>();
		Pager pager = new Pager();
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			User user = UserContext.getUser();
			ComEmployeeRole comEmployeeRole = new ComEmployeeRole();
			comEmployeeRole.setEmployeeId(user.getId());
			List<ComEmployeeRole> list = comEmployeeRoleServiceImpl
					.findComEmployeeRoleListByEmpId(comEmployeeRole);
			// 得到用户的角色。判断是否存在区域总审批，信贷综合管理部审批，信贷综合管理部经理审批，分管领导审批，总经理审批角色
			List<String> stateList = new ArrayList<String>();
			for (int i = 0; i < list.size(); i++) {
				ComEmployeeRole comEmpRole = list.get(i);
				ComRole role=comRoleServiceImpl.get(comEmpRole.getRoleId());
				if (role.getRoleName().equals("减免-区域总")) {
					stateList.add(SpecialRepaymentStateEnum.区域总审批.getValue());
				} else if (role.getRoleName().equals("减免-综管部")) {
					stateList.add(SpecialRepaymentStateEnum.信贷综合管理部审批.getValue());
				} else if (role.getRoleName().equals("减免-综管部经理")) {
					stateList.add(SpecialRepaymentStateEnum.信贷综合管理部经理审批.getValue());
				} else if (role.getRoleName().equals("减免-分管领导")){
					stateList.add("分管领导审批");
				} else if (role.getRoleName().equals("减免-总经理")) {
					stateList.add("总经理审批");
				}
			}
			if (stateList != null && stateList.size() > 0) {// 登录角色存在减免审核权限
				pager.setPage(page);
				pager.setRows(rows);
				pager.setSort("ASC");
				LoanContract contract=loanBase.getLoanContract();
				if(contract!=null){
					paramMap.put("borrowerName", loanBase.getLoanContract().getBorrowerName());
					paramMap.put("idnum", loanBase.getLoanContract().getIdnum());
				}
				paramMap.put("code", user.getOrgCode());
				paramMap.put("salesMan", loanBase.getSalesmanId());
				paramMap.put("mphone", loanBase.getPersonInfo() != null ? loanBase.getPersonInfo().getMphone() : "");
				paramMap.put("specialRepaymentState", stateList);
				paramMap.put("pager", pager);
				pager = loanBaseServiceImpl.searchapprovalWithPg(paramMap);
				List<LoanBase> listPager = pager.getResultList();
				for (int i = 0; i < listPager.size(); i++) {
					LoanBase loanBaseVo=(LoanBase)listPager.get(i);
					Long loanId = loanBaseVo.getId();
					VLoanInfo vLoanInfo = vLoanInfoServiceImpl.findByLoanId(loanId);
					Assert.notNull(vLoanInfo, ResponseEnum.FULL_MSG, "未找到债权数据loanId:" + loanId);
					/** 获取计算器实例 **/
					ICalculator calculatorInstace = CalculatorFactoryImpl.createCalculator(vLoanInfo);
					
					//查询剩余利息
					BigDecimal remaining=afterLoanServiceImpl.getRemainingInterest(vLoanInfo,new Date());
					loanBaseVo.setRemainingInterest(remaining);
					
					/** 获取违约金 **/
					List<LoanRepaymentDetail> repayList = afterLoanServiceImpl.getAllInterestOrLoan(new Date(),loanBaseVo.getId());
					BigDecimal penalty = calculatorInstace.getPenalty(loanId, repayList, vLoanInfo);
					loanBaseVo.setPenalty(penalty);
					//剩余本息和
					BigDecimal remain=	loanBaseVo.getRemainingInterest();
					BigDecimal pact=loanBaseVo.getLoanProduct().getResidualPactMoney();
					loanBaseVo.setBxcount(remain.add(pact));
					/*Long currentTerm=	loanBaseVo.getCurrentTerm();
				if(currentTerm==null){currentTerm=0l;}
				loanBaseVo.setCurrentTerm((loanBaseVo.getLoanProduct().getTime())-currentTerm+1);*/
					
				}
				this.createLog(request, SysActionLogTypeEnum.查询, "费用减免审核", "费用减免审核");
				attachmentResponseInfo.setResCode(ResponseEnum.SYS_SUCCESS.getCode());
				attachmentResponseInfo.setResMsg(ResponseEnum.SYS_SUCCESS.getDesc());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return toPGJSONWithCode(pager);
	}

	/**
	 * 审核通过
	 * 
	 * @param model
	 * @return
	 */
	@SuppressWarnings("static-access")
	@RequestMapping("/approvalSuccess/{speId}")
	@ResponseBody
	public String approvalSuccess(@PathVariable String speId,
			LoanSpecialRepayment loanspe, HttpServletRequest request,
			HttpServletResponse response) {
		AttachmentResponseInfo<Object> attachmentResponseInfo = new AttachmentResponseInfo<Object>();
		User user = UserContext.getUser();
		loanspe=loanSpecialRepaymentServiceImpl.findById(Long.parseLong(speId));
		LoanProcessHistory loanProcessHistory = new LoanProcessHistory();
		String state = loanspe.getSpecialRepaymentState();
		if (state.equals(SpecialRepaymentStateEnum.区域总审批.getValue())) {
			loanspe.setSpecialRepaymentState(SpecialRepaymentStateEnum.信贷综合管理部审批.getValue());
			loanProcessHistory.setLoanFlowState(SpecialRepaymentStateEnum.信贷综合管理部审批.getValue());
			loanProcessHistory.setContent("区域总审批审核通过，等待信贷综合管理部审批");
		} else if (state.equals(SpecialRepaymentStateEnum.信贷综合管理部审批.getValue())) {
			loanspe.setSpecialRepaymentState(SpecialRepaymentStateEnum.信贷综合管理部经理审批.getValue());
			loanProcessHistory.setLoanFlowState(SpecialRepaymentStateEnum.信贷综合管理部经理审批.getValue());
			loanProcessHistory.setContent("信贷综合管理部审批通过，等待信贷综合管理部经理审批");
		} else if (state.equals(SpecialRepaymentStateEnum.信贷综合管理部经理审批.getValue())) {
			loanspe.setSpecialRepaymentState(SpecialRepaymentStateEnum.分管领导审批.getValue());
			loanProcessHistory.setLoanFlowState(SpecialRepaymentStateEnum.分管领导审批.getValue());
			loanProcessHistory.setContent("信贷综合管理部经理审批通过，等待分管领导审批审批");
		} else if (state.equals(SpecialRepaymentStateEnum.分管领导审批.getValue())) {
			loanspe.setSpecialRepaymentState(SpecialRepaymentStateEnum.总经理审批.getValue());
			loanProcessHistory.setLoanFlowState(SpecialRepaymentStateEnum.总经理审批.getValue());
			loanProcessHistory.setContent("分管领导审批通过，等待总经理审批审批");
		} else if (state.equals(SpecialRepaymentStateEnum.总经理审批.getValue())) {
			loanspe.setSpecialRepaymentState(SpecialRepaymentStateEnum.申请.getValue());
			loanProcessHistory.setLoanFlowState(SpecialRepaymentStateEnum.申请.getValue());
			loanProcessHistory.setContent("总经理审批通过，申请已成功");
		}
//		loanspe.setSpecialRepaymentType("正常费用减免");
		loanspe.setProposerId(user.getId());
//		loanspe.setRequestDate(new Date());
//		loanspe.setCreator(user.getName());
		try {
			loanSpecialRepaymentServiceImpl.update(loanspe);
			if(SpecialRepaymentStateEnum.申请.getValue().equals(loanspe.getSpecialRepaymentState())){
				//这里调用报盘接口，调整报盘金额
				offerCreateService.createOfferInfoBySpecialRepay(loanspe.getId());
			}
			this.createLog(request, SysActionLogTypeEnum.查询, "费用减免审核", "费用减免审核通过");
			// 写入日志
			loanProcessHistory.setLoanId(loanspe.getLoanId());
			loanProcessHistory.setLoanState("正常费用减免");
			loanProcessHistoryServiceImpl.insert(loanProcessHistory);
			attachmentResponseInfo.setResCode(ResponseEnum.SYS_SUCCESS.getCode());
			attachmentResponseInfo.setResMsg(ResponseEnum.SYS_SUCCESS.getDesc());
			attachmentResponseInfo.setResMsg("审核成功");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return toResponseJSON(attachmentResponseInfo);
	}

	/**
	 * 审核拒绝
	 * @param model
	 * @return
	 */
	@SuppressWarnings("static-access")
	@RequestMapping("/approvalNO/{speId}")
	@ResponseBody
	public String approvalNO(@PathVariable String speId, LoanSpecialRepayment loanspe, HttpServletRequest request, HttpServletResponse response) {
		AttachmentResponseInfo<Object> attachmentResponseInfo = new AttachmentResponseInfo<Object>();
		User user = UserContext.getUser();
		loanspe = loanSpecialRepaymentServiceImpl.findById(Long.parseLong(speId));
		LoanProcessHistory loanProcessHistory = new LoanProcessHistory();
		// String state = loanspe.getSpecialRepaymentState();
		loanspe.setSpecialRepaymentState("拒绝");
		loanProcessHistory.setLoanFlowState("拒绝");
		loanProcessHistory.setContent("申请被操作人拒绝");
		loanspe.setProposerId(user.getId());
		// loanspe.setRequestDate(new Date());
		// loanspe.setCreator(user.getName());
		try {
			loanSpecialRepaymentServiceImpl.update(loanspe);
			this.createLog(request, SysActionLogTypeEnum.查询, "费用减免审核", "费用减免审核拒绝");
			// 写入日志
			loanProcessHistory.setLoanId(loanspe.getLoanId());
			loanProcessHistory.setLoanState(SpecialRepaymentTypeEnum.正常费用减免.getValue());
			loanProcessHistoryServiceImpl.insert(loanProcessHistory);
			attachmentResponseInfo.setResCode(ResponseEnum.SYS_SUCCESS.getCode());
			attachmentResponseInfo.setResMsg(ResponseEnum.SYS_SUCCESS.getDesc());
			attachmentResponseInfo.setResMsg("拒绝成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return toResponseJSON(attachmentResponseInfo);
	}
}
