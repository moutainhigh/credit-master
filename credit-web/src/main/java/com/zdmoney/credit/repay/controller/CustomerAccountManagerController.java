package com.zdmoney.credit.repay.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zdmoney.credit.common.constant.SysActionLogTypeEnum;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.util.UrlUtil;
import com.zdmoney.credit.common.util.ValidatorErrorUtil;
import com.zdmoney.credit.common.vo.AttachmentResponseInfo;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.loan.domain.LoanLog;
import com.zdmoney.credit.loan.service.pub.ILoanLogService;
import com.zdmoney.credit.repay.service.pub.ICustomerAccountManagerService;
import com.zdmoney.credit.repay.vo.VCustomerAccountManagerInfo;
import com.zdmoney.credit.repay.vo.VCustomerAccountManagerList;
import com.zdmoney.credit.repay.vo.VCustomerAccountManagerPrestore;
import com.zdmoney.credit.repay.vo.VCustomerAccountManagerRepaymentEarnestMoney;
import com.zdmoney.credit.repay.vo.VCustomerAccountManagerWithdrawDeposits;


/**
 * 前端请求处理（客户账户管理）
 * @author 00236633
 * @since 2015-8-10
 */

@Controller
@RequestMapping("/repay")
public class CustomerAccountManagerController extends BaseController{

	@Autowired 
	@Qualifier("customerAccountManagerService")
	private ICustomerAccountManagerService customerAccountManagerService;
	
	@Autowired
	ILoanLogService loanLogService;
    
	/**
	 * 加载客户账号管理列表页面
	 * @author 00236633
	 * @return
	 */
	@RequestMapping(value = "/customerAccountManagerListPage", method = RequestMethod.GET)
	public void listPage(Model model,HttpServletRequest request) {
    	this.createLog(request, SysActionLogTypeEnum.查询, "客户账号管理", "加载客户账号管理列表页面");
		Map<String,Object> response = new HashMap<String,Object>();
		response.put("resCode",ResponseEnum.SYS_SUCCESS.getCode());
		response.put("resMsg",ResponseEnum.SYS_SUCCESS.getDesc());
		
		model.addAllAttributes(response);
	}
	
	/**
	 * 查询客户账号管理列表
	 * @author 00236633
	 * @return
	 */
	@RequestMapping(value = "/customerAccountManagerList", method = RequestMethod.POST)
	@ResponseBody
	public String list(@Valid VCustomerAccountManagerList vCustomerAccountManagerList, BindingResult error,HttpServletRequest request) {
    	this.createLog(request, SysActionLogTypeEnum.查询, "客户账号管理", "查询客户账号管理列表");
    	AttachmentResponseInfo<Map<String,Object>> response = new AttachmentResponseInfo<Map<String,Object>>();
		Map<String,Object> data = new HashMap<String,Object>();
		response.setAttachment(data);
		
		try {
			if(!error.hasErrors()){
				Map<String,Object> serviceResult = customerAccountManagerService.list(vCustomerAccountManagerList);
				data.put("rows", serviceResult.get("customerAccountManagerList"));
				data.put("total", serviceResult.get("customerAccountManagerCount"));
				response.setResCode(ResponseEnum.SYS_SUCCESS.getCode());
				response.setResMsg(ResponseEnum.SYS_SUCCESS.getDesc());
			}else{
				response.setResCode(ResponseEnum.SYS_FAILD.getCode());
				String errorInfo = ValidatorErrorUtil.getErrorInfoString(error.getAllErrors());
				if(errorInfo.length()>0){
					response.setResMsg(errorInfo);
				}else{
					response.setResMsg("查询参数不合法");
				}
			}
		} catch (Exception e) {
			logger.error("path:"+UrlUtil.getUri(),e);
			response.setResCode(ResponseEnum.SYS_FAILD.getCode());
			response.setResMsg("查询失败");
		}
		
		return toResponseJSON(response);
	}
    
	/**
	 * 加载客户账号管理提现页面
	 * @author 00236633
	 * @return
	 */
	@RequestMapping(value = "/customerAccountManagerWithdrawDepositsPage", method = RequestMethod.GET)
	public void withdrawDepositsPage(@Valid VCustomerAccountManagerInfo vCustomerAccountManagerInfo, BindingResult error,Model model,HttpServletRequest request) {
    	this.createLog(request, SysActionLogTypeEnum.查询, "客户账号管理", "加载客户账号管理提现页面");
    	Map<String,Object> response = new HashMap<String,Object>();
		if(!error.hasErrors()){
			Map<String,Object> serviceResult = customerAccountManagerService.withdrawDepositsPage(vCustomerAccountManagerInfo);
			response.putAll(serviceResult);
			response.put("zhuxuedaiOrg", vCustomerAccountManagerInfo.getZhuxuedaiOrg());
			if((Boolean)serviceResult.get("success")){
				response.put("resCode",ResponseEnum.SYS_SUCCESS.getCode());
				response.put("resMsg",ResponseEnum.SYS_SUCCESS.getDesc());
			}else{
				response.put("resCode",ResponseEnum.SYS_FAILD.getCode());
				response.put("resMsg",serviceResult.get("message")+"");
			}
		}else{
			response.put("resCode",ResponseEnum.SYS_FAILD.getCode());
			String errorInfo = ValidatorErrorUtil.getErrorInfoString(error.getAllErrors());
			if(errorInfo.length()>0){
				response.put("resMsg",errorInfo);
			}else{
				response.put("resMsg","打开客户账号提现页面参数不合法");
			}
		}
		
		model.addAllAttributes(response);
	}
	
	/**
	 * 提交客户账号管理提现操作
	 * @author 00236633
	 * @return
	 */
	@RequestMapping(value = "/customerAccountManagerWithdrawDeposits", method = RequestMethod.POST)
	@ResponseBody
	public String withdrawDeposits(@Valid VCustomerAccountManagerWithdrawDeposits vCustomerAccountManagerWithdrawDeposits, BindingResult error,HttpServletRequest request) {
    	this.createLog(request, SysActionLogTypeEnum.查询, "客户账号管理", "提交客户账号管理提现操作");
    	AttachmentResponseInfo<Map<String,Object>> response = new AttachmentResponseInfo<Map<String,Object>>();
		Map<String,Object> data = new HashMap<String,Object>();
		response.setAttachment(data);
		if(!error.hasErrors()){
			try {
				Map<String,Object> serviceResult = customerAccountManagerService.withdrawDeposits(vCustomerAccountManagerWithdrawDeposits);
				if((Boolean)serviceResult.get("success")){
					response.setResCode(ResponseEnum.SYS_SUCCESS.getCode());
					response.setResMsg(ResponseEnum.SYS_SUCCESS.getDesc());
				}else{
					response.setResCode(ResponseEnum.SYS_FAILD.getCode());
					response.setResMsg(serviceResult.get("message")+"");
				}
			} catch (Exception e) {
				logger.error("path:"+UrlUtil.getUri(),e);
				logger.error(vCustomerAccountManagerWithdrawDeposits);
				try {
					LoanLog loanLog = new LoanLog();
					loanLog.setCreator(UserContext.getUser().getId()+"");
					loanLog.setCreateTime(new Date());
					loanLog.setContent( "操作员:" + UserContext.getUser().getName() + ",执行客户账户管理[客户提现]操作,操作金额:" + vCustomerAccountManagerWithdrawDeposits.getAmount());
					loanLog.setLogName("customerAccountManagerWithdrawDeposits");
					loanLog.setLogType("info");
					loanLogService.createLog(loanLog);
				} catch (Exception e1) {
					logger.debug(e1);
				}
            	response.setResCode(ResponseEnum.SYS_FAILD.getCode());
				response.setResMsg("记账出错，请重新执行提现操作!");
			}
		}else{
			response.setResCode(ResponseEnum.SYS_FAILD.getCode());
			String errorInfo = ValidatorErrorUtil.getErrorInfoString(error.getAllErrors());
			if(errorInfo.length()>0){
				response.setResMsg(errorInfo);
			}else{
				response.setResMsg("提现参数不合法");
			}
		}
		return toResponseJSON(response);
	}
    
	/**
	 * 加载客户账号管理预存界面
	 * @author 00236633
	 * @return
	 */
	@RequestMapping(value = "/customerAccountManagerPrestorePage", method = RequestMethod.GET)
	public void prestorePage(@Valid VCustomerAccountManagerInfo vCustomerAccountManagerInfo, BindingResult error,Model model,HttpServletRequest request) {
    	this.createLog(request, SysActionLogTypeEnum.查询, "客户账号管理", "加载客户账号管理预存界面");
    	Map<String,Object> response = new HashMap<String,Object>();
		
		if(!error.hasErrors()){
			Map<String,Object> serviceResult = customerAccountManagerService.prestorePage(vCustomerAccountManagerInfo);
			response.putAll(serviceResult);
			if((Boolean)serviceResult.get("success")){
				response.put("resCode",ResponseEnum.SYS_SUCCESS.getCode());
				response.put("resMsg",ResponseEnum.SYS_SUCCESS.getDesc());
			}else{
				response.put("resCode",ResponseEnum.SYS_FAILD.getCode());
				response.put("resMsg",serviceResult.get("message")+"");
			}
		}else{
			response.put("resCode",ResponseEnum.SYS_FAILD.getCode());
			String errorInfo = ValidatorErrorUtil.getErrorInfoString(error.getAllErrors());
			if(errorInfo.length()>0){
				response.put("resMsg",errorInfo);
			}else{
				response.put("resMsg","打开客户账号预存页面参数不合法");
			}
		}
		
		model.addAllAttributes(response);
	}
	
	/**
	 * 提交客户账号管理预存操作
	 * @author 00236633
	 * @return
	 */
	@RequestMapping(value = "/customerAccountManagerPrestore", method = RequestMethod.POST)
	@ResponseBody
	public String prestore(@Valid VCustomerAccountManagerPrestore vCustomerAccountManagerPrestore, BindingResult error,HttpServletRequest request) {
    	this.createLog(request, SysActionLogTypeEnum.查询, "客户账号管理", "提交客户账号管理预存操作");
    	AttachmentResponseInfo<Map<String,Object>> response = new AttachmentResponseInfo<Map<String,Object>>();
		Map<String,Object> data = new HashMap<String,Object>();
		response.setAttachment(data);
	
		if(!error.hasErrors()){
			try {
				Map<String,Object> serviceResult = customerAccountManagerService.prestore(vCustomerAccountManagerPrestore);
				if((Boolean)serviceResult.get("success")){
					response.setResCode(ResponseEnum.SYS_SUCCESS.getCode());
					response.setResMsg(ResponseEnum.SYS_SUCCESS.getDesc());
				}else{
					response.setResCode(ResponseEnum.SYS_FAILD.getCode());
					response.setResMsg(serviceResult.get("message")+"");
				}
			} catch (Exception e) {
				logger.error("path:"+UrlUtil.getUri(),e);
				logger.error(vCustomerAccountManagerPrestore);
				try {
					LoanLog loanLog = new LoanLog();
					loanLog.setCreator(UserContext.getUser().getId()+"");
					loanLog.setCreateTime(new Date());
					loanLog.setContent( "操作员:" + UserContext.getUser().getName() + ",执行客户账户管理[客户预存]操作,操作金额:" + vCustomerAccountManagerPrestore.getAmount());
					loanLog.setLogName("customerAccountManagerPrestore");
					loanLog.setLogType("info");
					loanLogService.createLog(loanLog);
				} catch (Exception e1) {
					logger.debug(e1);
				}
				response.setResCode(ResponseEnum.SYS_FAILD.getCode());
				response.setResMsg("记账出错，请重新执行预存操作!");
			}
		}else{
			response.setResCode(ResponseEnum.SYS_FAILD.getCode());
			String errorInfo = ValidatorErrorUtil.getErrorInfoString(error.getAllErrors());
			if(errorInfo.length()>0){
				response.setResMsg(errorInfo);
			}else{
				response.setResMsg("预存参数不合法");
			}
		}
		
		return toResponseJSON(response);
	}
    /**
	 * 加载客户账号管理还保证金界面
	 * @author 00236633
	 * @return
	 */
	@RequestMapping(value = "/customerAccountManagerRepaymentEarnestMoneyPage", method = RequestMethod.GET)
	public void repaymentEarnestMoneyPage(@Valid VCustomerAccountManagerInfo vCustomerAccountManagerInfo, BindingResult error,Model model,HttpServletRequest request) {
    	this.createLog(request, SysActionLogTypeEnum.查询, "客户账号管理", "加载客户账号管理还保证金界面");
    	Map<String,Object> response = new HashMap<String,Object>();
		
		if(!error.hasErrors()){
			Map<String,Object> serviceResult = customerAccountManagerService.repaymentEarnestMoneyPage(vCustomerAccountManagerInfo);
			response.putAll(serviceResult);
			if((Boolean)serviceResult.get("success")){
				response.put("resCode",ResponseEnum.SYS_SUCCESS.getCode());
				response.put("resMsg",ResponseEnum.SYS_SUCCESS.getDesc());
			}else{
				response.put("resCode",ResponseEnum.SYS_FAILD.getCode());
				response.put("resMsg",serviceResult.get("message")+"");
			}
		}else{
			response.put("resCode",ResponseEnum.SYS_FAILD.getCode());
			String errorInfo = ValidatorErrorUtil.getErrorInfoString(error.getAllErrors());
			if(errorInfo.length()>0){
				response.put("resMsg",errorInfo);
			}else{
				response.put("resMsg","打开客户账号还保证金页面参数不合法");
			}
		}
		
		model.addAllAttributes(response);
	}
	
	/**
	 * 提交客户账号管理还保证金操作
	 * @author 00236633
	 * @return
	 */
	@RequestMapping(value = "/customerAccountManagerRepaymentEarnestMoney", method = RequestMethod.POST)
	@ResponseBody
	public String repaymentEarnestMoney(@Valid VCustomerAccountManagerRepaymentEarnestMoney vCustomerAccountManagerRepaymentEarnestMoney, BindingResult error,HttpServletRequest request) {
    	this.createLog(request, SysActionLogTypeEnum.查询, "客户账号管理", "提交客户账号管理还保证金操作");
    	AttachmentResponseInfo<Map<String,Object>> response = new AttachmentResponseInfo<Map<String,Object>>();
		Map<String,Object> data = new HashMap<String,Object>();
		response.setAttachment(data);
	
		if(!error.hasErrors()){
			try {
				Map<String,Object> serviceResult = customerAccountManagerService.repaymentEarnestMoney(vCustomerAccountManagerRepaymentEarnestMoney);
				if((Boolean)serviceResult.get("success")){
					response.setResCode(ResponseEnum.SYS_SUCCESS.getCode());
					response.setResMsg(ResponseEnum.SYS_SUCCESS.getDesc());
				}else{
					response.setResCode(ResponseEnum.SYS_FAILD.getCode());
					response.setResMsg(serviceResult.get("message")+"");
				}
			} catch (Exception e) {
				logger.error("path:"+UrlUtil.getUri(),e);
				logger.error(vCustomerAccountManagerRepaymentEarnestMoney);
				try {
					LoanLog loanLog = new LoanLog();
					loanLog.setCreator(UserContext.getUser().getId()+"");
					loanLog.setCreateTime(new Date());
					loanLog.setContent( "操作员:" + UserContext.getUser().getName() + ",执行客户账户管理[还保证金]操作,操作金额:" + vCustomerAccountManagerRepaymentEarnestMoney.getAmount());
					loanLog.setLogName("customerAccountManagerRepaymentEarnestMoney");
					loanLog.setLogType("info");
					loanLogService.createLog(loanLog);
				} catch (Exception e1) {
					logger.debug(e1);
				}
				response.setResCode(ResponseEnum.SYS_FAILD.getCode());
				response.setResMsg("记账出错，请重新执行还保证金操作!");
			}
		}else{
			response.setResCode(ResponseEnum.SYS_FAILD.getCode());
			String errorInfo = ValidatorErrorUtil.getErrorInfoString(error.getAllErrors());
			if(errorInfo.length()>0){
				response.setResMsg(errorInfo);
			}else{
				response.setResMsg("还保证金参数不合法");
			}
		}
		
		return toResponseJSON(response);
	}
    
    
	/**
	 * 查询客户账号信息
	 * @author 00236633
	 * @return
	 */
	@RequestMapping(value = "/findCustomerAccountInfo", method = RequestMethod.POST)
	@ResponseBody
	public String findCustomerAccountInfo(@Valid VCustomerAccountManagerInfo vCustomerAccountManagerInfo, BindingResult error,HttpServletRequest request) {
    	this.createLog(request, SysActionLogTypeEnum.查询, "客户账号管理", "查询客户账号信息");
    	AttachmentResponseInfo<Map<String,Object>> response = new AttachmentResponseInfo<Map<String,Object>>();
		Map<String,Object> data = new HashMap<String,Object>();
		response.setAttachment(data);
	
		if(!error.hasErrors()){
			try {
				Map<String,Object> serviceResult = customerAccountManagerService.findCustomerAccountInfo(vCustomerAccountManagerInfo);
				if((Boolean)serviceResult.get("success")){
					data.putAll(serviceResult);
					response.setResCode(ResponseEnum.SYS_SUCCESS.getCode());
					response.setResMsg(ResponseEnum.SYS_SUCCESS.getDesc());
				}else{
					response.setResCode(ResponseEnum.SYS_FAILD.getCode());
					response.setResMsg(serviceResult.get("message")+"");
				}
			} catch (Exception e) {
				logger.error("path:"+UrlUtil.getUri(),e);
				logger.error(vCustomerAccountManagerInfo);
				response.setResCode(ResponseEnum.SYS_FAILD.getCode());
				response.setResMsg("查询客户账户信息失败！");
			}
		}else{
			response.setResCode(ResponseEnum.SYS_FAILD.getCode());
			String errorInfo = ValidatorErrorUtil.getErrorInfoString(error.getAllErrors());
			if(errorInfo.length()>0){
				response.setResMsg(errorInfo);
			}else{
				response.setResMsg("查询客户账户信息参数不合法");
			}
		}
		
		return toResponseJSON(response);
	}
}
