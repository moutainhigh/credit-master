package com.zdmoney.credit.specialRepayment.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.zdmoney.credit.bsyh.dao.pub.IRepayBusLogDao;
import com.zdmoney.credit.bsyh.domain.RepayBusLog;
import com.zdmoney.credit.common.constant.FundsSourcesTypeEnum;
import com.zdmoney.credit.common.constant.LoanStateEnum;
import com.zdmoney.credit.common.constant.SpecialRepaymentStateEnum;
import com.zdmoney.credit.common.constant.SpecialRepaymentTypeEnum;
import com.zdmoney.credit.common.constant.SysActionLogTypeEnum;
import com.zdmoney.credit.common.constant.system.LogLevel;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.util.Assert;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.util.excel.ExcelTemplet;
import com.zdmoney.credit.common.util.excel.ExcelUtil;
import com.zdmoney.credit.common.util.file.UploadFileUtil;
import com.zdmoney.credit.common.util.file.vo.UploadFile;
import com.zdmoney.credit.common.vo.AttachmentResponseInfo;
import com.zdmoney.credit.common.vo.ResponseInfo;
import com.zdmoney.credit.core.calculator.factory.CalculatorFactoryImpl;
import com.zdmoney.credit.core.calculator.pub.ICalculator;
import com.zdmoney.credit.fee.service.pub.ILoanFeeInfoService;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.loan.domain.LoanRepaymentDetail;
import com.zdmoney.credit.loan.domain.LoanSpecialRepayment;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.IAfterLoanService;
import com.zdmoney.credit.loan.service.pub.ILoanRepaymentDetailService;
import com.zdmoney.credit.loan.service.pub.ILoanSpecialRepaymentService;
import com.zdmoney.credit.loan.service.pub.ILoanTransferInfoService;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import com.zdmoney.credit.system.domain.ComEmployee;
import com.zdmoney.credit.system.domain.ComMessageIn;
import com.zdmoney.credit.system.domain.PersonInfo;
import com.zdmoney.credit.system.service.pub.IComEmployeeService;
import com.zdmoney.credit.system.service.pub.IComMessageInService;
import com.zdmoney.credit.system.service.pub.IPersonInfoService;

/**
 * 申请提前结清、申请减免Controller
 * 
 * @author Ivan
 *
 */
@Controller
@RequestMapping("/specialRepayment")
public class SpecialRepaymentController extends BaseController {

    @Autowired
    @Qualifier("afterLoanServiceImpl")
    IAfterLoanService afterLoanServiceImpl;

    @Autowired
    @Qualifier("VLoanInfoServiceImpl")
    IVLoanInfoService vLoanInfoServiceImpl;

    @Autowired
    @Qualifier("loanSpecialRepaymentServiceImpl")
    ILoanSpecialRepaymentService loanSpecialRepaymentServiceImpl;
    
    @Autowired
    @Qualifier("comMessageInServiceImpl")
    IComMessageInService comMessageInServiceImpl;
    
    @Autowired
    @Qualifier("personInfoServiceImpl")
    IPersonInfoService personInfoServiceImpl;
    
    @Autowired
    @Qualifier("comEmployeeServiceImpl")
    IComEmployeeService comEmployeeServiceImpl;
    
    @Autowired
    private ILoanFeeInfoService iLoanFeeInfoService;
    
    @Autowired
    private ILoanRepaymentDetailService loanRepaymentDetailService;
    
    @Autowired
    private IRepayBusLogDao repayBusLogDao;

    @Autowired 
    ILoanTransferInfoService loanTransferInfoServiceImpl;
    /**
     * 跳转到提前结清申请页面
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/jumpAllCleanPage")
    public ModelAndView jumpAllCleanPage(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("/specialRepayment/allCleanApplyList");
		return modelAndView;
    }
    
    /**
     * 跳转到提前扣款申请页面
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/jumpCurCleanPage")
    public ModelAndView jumpCurCleanPage(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("/specialRepayment/curCleanApplyList");
		return modelAndView;
    }
    
    /**
     * 跳转到罚息减免申请页面
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/jumpReliefPenaltyPage")
    public ModelAndView jumpReliefPenaltyPage(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("/specialRepayment/reliefPenaltyList");
		return modelAndView;
    }

    /**
     * 查询借款数据(提前还款申请 模块)
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "searchRepaymentLoanToPre")
    @ResponseBody
    public String searchRepaymentLoanToPre(PersonInfo personInfo, int rows, int page, HttpServletRequest request, HttpServletResponse response) {
		ResponseInfo responseInfo = null;
		try {
			this.createLog(request, SysActionLogTypeEnum.查询, "提前还款申请", "查询客户数据");
			String salesMan = Strings.convertValue(request.getParameter("salesMan"), String.class);
		    Map<String, Object> paramMap = new HashMap<String, Object>();
		    /** LoanState 定死查询这3类 **/
		    paramMap.put("inLoanState", new String[] { LoanStateEnum.正常.name(), LoanStateEnum.逾期.name(), LoanStateEnum.坏账.name() });
		    /** 获取登陆者信息 **/
		    User user = UserContext.getUser();
		    paramMap.put("orgCode", user.getOrgCode());
		    paramMap.put("idNum", personInfo.getIdnum());
		    paramMap.put("mphone", personInfo.getMphone());
		    paramMap.put("name", personInfo.getName());
		    paramMap.put("salesMan", salesMan);
		    paramMap.put("contractNum", request.getParameter("contractNum"));
	
		    Pager pager = new Pager();
		    pager.setPage(page);
		    pager.setRows(rows);
		    pager.setSidx("LOAN_ID");
		    pager.setSort("ASC");
		    paramMap.put("pager", pager);
	
		    pager = vLoanInfoServiceImpl.searchRepaymentLoanWithPg(paramMap);
		    List list = pager.getResultList();
	
		    List<Map> dataMap = new ArrayList<Map>();
		    Date currDate = Dates.getCurrDate();
		    for (int i = 0; i < list.size(); i++) {
				VLoanInfo vLoanInfo = (VLoanInfo) list.get(i);
				
				/** 获取计算器实例 **/
				ICalculator calculatorInstace = CalculatorFactoryImpl.createCalculator(vLoanInfo);
				
				Long loanId = vLoanInfo.getId();
				Map<String, Object> map = new HashMap<String, Object>();
		
				map.put("id", loanId);
				map.put("name", vLoanInfo.getPersonInfo().getName());
				map.put("loanType", vLoanInfo.getLoanType());
				map.put("idNum", vLoanInfo.getPersonInfo().getIdnum());
				map.put("pactMoney", vLoanInfo.getPactMoney());
				map.put("time", vLoanInfo.getTime());
				map.put("loanState", vLoanInfo.getLoanState());
				map.put("contractNum", vLoanInfo.getContractNum());
		
				/** 查询未结清的还款计划数据 **/
				List<LoanRepaymentDetail> detailList = afterLoanServiceImpl.getAllInterestOrLoan(currDate, loanId);
				/** 查询逾期本金 **/
				BigDecimal overdueCorpus = afterLoanServiceImpl.getOverdueCorpus(detailList, currDate);
				/** 查询逾期利息 **/
				BigDecimal overdueInterest = afterLoanServiceImpl.getOverdueInterest(detailList, currDate);
				/** 逾期应还 **/
				map.put("overdueAllAmount", overdueCorpus.add(overdueInterest));
				/** 逾期罚息 **/
				BigDecimal fine = afterLoanServiceImpl.getFine(detailList, currDate);
				map.put("fine", fine);
				/** 账户中余额(即挂账金额) **/
				BigDecimal accAmount = afterLoanServiceImpl.getAccAmount(loanId);
				map.put("accAmount", accAmount);
				/** 获取一次性结清金额 **/
				BigDecimal oneTimeRepaymentAmount = calculatorInstace.getOnetimeRepaymentAmount(loanId, currDate, detailList);
				map.put("oneTimeRepaymentAmount", oneTimeRepaymentAmount);
				/** 当期总额 **/
				BigDecimal currAmount = afterLoanServiceImpl.getCurrAmount(detailList, currDate);
				map.put("currAmount", currAmount);
				
				/** 提前结清、提前扣款申请状态显示 **/
				String spState = "未申请";
				/** 查看是否申请(一次性还款) **/
				LoanSpecialRepayment oneTimeState = loanSpecialRepaymentServiceImpl.findbyLoanAndType(loanId, SpecialRepaymentTypeEnum.一次性还款.name(),
					SpecialRepaymentStateEnum.申请.name());
				/** 查看是否申请(提前扣款) **/
				LoanSpecialRepayment noOntTimeState = loanSpecialRepaymentServiceImpl.findbyLoanAndType(loanId, SpecialRepaymentTypeEnum.提前扣款.name(),
					SpecialRepaymentStateEnum.申请.name());
				if (oneTimeState != null && noOntTimeState != null) {
					spState = "申请数据异常";
				} else {
					if (oneTimeState != null) {
						spState = "已申请一次性结清";
					} else if (noOntTimeState != null) {
						spState = "已申请提前扣款";
					}
				}
				map.put("spState", spState);
		
				dataMap.add(map);
		    }
		    pager.setResultList(dataMap);
		    return toPGJSONWithCode(pager);
		} catch (Exception ex) {
		    /** 系统忙 **/
			logger.error(ex,ex);
		    responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(), ResponseEnum.SYS_FAILD.getDesc(), "");
		}
		return toResponseJSON(responseInfo);
    }

    /**
     * 查询提前扣款申请状态
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "loadCurCleanSpecialState/{id}")
    @ResponseBody
    public String loadCurCleanSpecialState(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
		AttachmentResponseInfo<Map<String, Object>> attachmentResponseInfo = null;
		try {
		    VLoanInfo loanInfo = vLoanInfoServiceImpl.findByLoanId(id);
		    Assert.notNull(loanInfo, ResponseEnum.FULL_MSG, "借款数据不存在[" + id + "]");
		    Map<String, Object> map = new HashMap<String, Object>();
		    /** 查看是否申请(提前扣款) **/
		    LoanSpecialRepayment curCleanSpecialState = loanSpecialRepaymentServiceImpl.findbyLoanAndType(id, SpecialRepaymentTypeEnum.提前扣款.name(),
			    SpecialRepaymentStateEnum.申请.name());
	
		    /** 取当期还款日 **/
		    Date currDate = Dates.getCurrDate();
		    List<LoanRepaymentDetail> repayList = afterLoanServiceImpl.getAllInterestOrLoan(currDate, id);
		    Date returnDate = null;
		    if (!CollectionUtils.isEmpty(repayList)) {
				/** 当期还款日 **/
				returnDate = repayList.get(repayList.size() - 1).getReturnDate();
				if (returnDate.compareTo(Dates.getCurrDate()) < 0) {
					/** 自动生效时间晚于最后一期还款日，不能申请提前还款。 **/
					throw new PlatformException("自动生效时间晚于最后一期还款日，不能申请提前还款。loanId:" + id).applyLogLevel(LogLevel.WARN);
				}if (returnDate.compareTo(Dates.getCurrDate()) == 0) {
					/** 自动生效时间晚于最后一期还款日，不能申请提前还款。 **/
					throw new PlatformException("还款日当天，不能申请提前还款。loanId:" + id).applyLogLevel(LogLevel.WARN);
				}
		    }
		    //陆金所，若借款处于逾期状态，则不能申请提前还款
		    if(FundsSourcesTypeEnum.陆金所.getValue().equals(loanInfo.getLoanBelong())){
		    	if("逾期".equals(loanInfo.getLoanState())){
		    		throw new PlatformException("该借款处于逾期阶段，不能申请提前还款。" + id).applyLogLevel(LogLevel.WARN);
		    	}
		    }
		    map.put("id", id);
		    map.put("curCleanApplyState", (curCleanSpecialState == null ? false : true));
		    map.put("currDate", Dates.getDateTime(Dates.DEFAULT_DATE_FORMAT));
		    map.put("requestDate", (curCleanSpecialState == null ? Dates.getDateTime(Dates.DEFAULT_DATE_FORMAT) : curCleanSpecialState.getRequestDate()));
		    /** 当期还款日 **/
		    map.put("curReturnDate", (returnDate == null ? "": Dates.getDateTime(returnDate,Dates.DEFAULT_DATE_FORMAT)));
		    
		    attachmentResponseInfo = new AttachmentResponseInfo<Map<String, Object>>(ResponseEnum.SYS_SUCCESS);
		    attachmentResponseInfo.setAttachment(map);
		} catch (PlatformException ex) {
			ex.printStackTraceExt(logger);
		    attachmentResponseInfo = ex.<Map<String, Object>> toAttachmentResponseInfo();
		} catch (Exception ex) {
			logger.error(ex,ex);
		    attachmentResponseInfo = new AttachmentResponseInfo<Map<String, Object>>(ResponseEnum.SYS_FAILD);
		}
		return toResponseJSON(attachmentResponseInfo);
    }
    
    /**
     * 查询提前一次性结清申请状态
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "loadAllCleanSpecialState/{id}")
    @ResponseBody
    public String loadAllCleanSpecialState(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
		AttachmentResponseInfo<Map<String, Object>> attachmentResponseInfo = null;
		try {
		    VLoanInfo loanInfo = vLoanInfoServiceImpl.findByLoanId(id);
		    Assert.notNull(loanInfo, ResponseEnum.FULL_MSG, "借款数据不存在[" + id + "]");
		    Map<String, Object> map = new HashMap<String, Object>();
		    
		    if("包商银行".equals(loanInfo.getLoanBelong())){
		    	Date currDate = Dates.getCurrDate();
			    List<LoanRepaymentDetail> repayList = afterLoanServiceImpl.getAllInterestOrLoan(currDate, id);
			    Date returnDate = null;
			    if (!CollectionUtils.isEmpty(repayList)) {
					/** 当期还款日 **/
					returnDate = repayList.get(repayList.size() - 1).getReturnDate();
					if (returnDate.compareTo(Dates.getCurrDate()) == 0) {						
						throw new PlatformException("还款日当天，不能申请提前结清。loanId:" + id).applyLogLevel(LogLevel.WARN);
					}
			    }
			}
		    /** 查看是否申请(一次性还款) **/
		    LoanSpecialRepayment allCleanSpecialState = loanSpecialRepaymentServiceImpl.findbyLoanAndType(id, SpecialRepaymentTypeEnum.一次性还款.name(),
			    SpecialRepaymentStateEnum.申请.name());
	
		    map.put("id", id);
		    map.put("allCleanApplyState", (allCleanSpecialState == null ? false : true));
		    map.put("currDate", Dates.getDateTime(Dates.DEFAULT_DATE_FORMAT));
	
		    attachmentResponseInfo = new AttachmentResponseInfo<Map<String, Object>>(ResponseEnum.SYS_SUCCESS);
		    attachmentResponseInfo.setAttachment(map);
		} catch (PlatformException ex) {
			ex.printStackTraceExt(logger);
		    attachmentResponseInfo = ex.<Map<String, Object>> toAttachmentResponseInfo();
		} catch (Exception ex) {
			logger.error(ex,ex);
		    attachmentResponseInfo = new AttachmentResponseInfo<Map<String, Object>>(ResponseEnum.SYS_FAILD);
		}
		return toResponseJSON(attachmentResponseInfo);
    }

    /**
     * 变更一次性结清申请状态
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "changeAllCleanApplyState")
    @ResponseBody
    public String changeAllCleanApplyState(HttpServletRequest request, HttpServletResponse response) {
		ResponseInfo responseInfo = null;
		try {
			this.createLog(request, SysActionLogTypeEnum.更新, "提前一次性结清申请或取消", "提前一次性结清申请或取消");
			Long id = Strings.convertValue(request.getParameter("id"), Long.class);
			Assert.notNull(id, "借款编号");
			
			//检查借款是否有转让过
		    boolean flag = loanTransferInfoServiceImpl.isLoanTransfer(null,id);
		    if(!flag){
		    	throw new PlatformException(ResponseEnum.FULL_MSG,Strings.errorMsg);
		    }
			
			// 针对龙信小贷、外贸信托、外贸2，判断是否已经完成划扣服务费，服务费未结清客户不能申请提前结清			
			if(iLoanFeeInfoService.isAlreadyDebitServiceCharge(id)){
				/** 提前一次性结清 申请状态 on:申请 off:取消 **/
				String allCleanApplyState = Strings.convertValue(request.getParameter("allCleanApplyState"), String.class);
				/** 提前扣款 申请状态 on:申请 off:取消 **/
				String curCleanApplyState = "";
				if ("on".equals(allCleanApplyState)) {
					curCleanApplyState = "off";
				}
				loanSpecialRepaymentServiceImpl.updateSpecialOneTimeState(id, allCleanApplyState, curCleanApplyState, null
						, null,null);
				responseInfo = new ResponseInfo(ResponseEnum.SYS_SUCCESS);
			}else{
				logger.info("服务费未结清，不能变更一次性结清申请");
				responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
				responseInfo.setResMsg("服务费未结清，不能变更一次性结算申请");
			}
		} catch (PlatformException ex) {
			logger.error(ex,ex);
		    responseInfo = ex.toResponseInfo();
		} catch (Exception ex) {
			logger.error(ex,ex);
		    responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
		}
		return toResponseJSON(responseInfo);
    }
    
    /**
     * 变更提前扣款申请状态
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "changeCurCleanApplyState")
    @ResponseBody
    public String changeCurCleanApplyState(HttpServletRequest request, HttpServletResponse response) {
		ResponseInfo responseInfo = null;
		try {
			this.createLog(request, SysActionLogTypeEnum.更新, "提前扣款申请或取消", "提前扣款申请或取消");
			Long id = Strings.convertValue(request.getParameter("id"), Long.class);
			Assert.notNull(id, "借款编号");
			
			//检查借款是否有转让过
		    boolean flag = loanTransferInfoServiceImpl.isLoanTransfer(null,id);
		    if(!flag){
		    	throw new PlatformException(ResponseEnum.FULL_MSG,Strings.errorMsg);
		    }
			
			// 针对龙信小贷、外贸信托、外贸2，判断是否已经完成划扣服务费、服务费未结清客户不能申请提前结清
			if(iLoanFeeInfoService.isAlreadyDebitServiceCharge(id)){
				VLoanInfo laonInfo = vLoanInfoServiceImpl.findByLoanId(id);				
				if("包商银行".equals(laonInfo.getLoanBelong())){
					//查询当期是否进行提前还款
					Date currTermReturnDate=afterLoanServiceImpl.getCurrTermReturnDate(Dates.getCurrDate(),laonInfo.getPromiseReturnDate());
		            LoanRepaymentDetail loanRepaymentDetail = loanRepaymentDetailService.findByLoanAndReturnDate(laonInfo,currTermReturnDate);
		            logger.info("☆☆☆☆☆☆☆☆☆☆☆☆☆☆提前扣款期数："+loanRepaymentDetail.getCurrentTerm()+"☆☆☆☆☆loanId："+id);
		            HashMap<String, Object> paramMap = new HashMap<String, Object>();
		            paramMap.put("loanId", id);
		            paramMap.put("currentTerm", loanRepaymentDetail.getCurrentTerm());//期数
		            paramMap.put("applyState", "t");//申请成功
		            paramMap.put("deductState", "t");//划扣成功
		            paramMap.put("applyType", "1");//类型提前扣款
		            List<RepayBusLog> list = repayBusLogDao.findListByMap(paramMap);
					if(!"正常".equals(laonInfo.getLoanState())){//包银带款状态是不正常，不可申请提前还款
						logger.info("包商银行，借款状态不是正常状态，不可申请提前还款");
						responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
						responseInfo.setResMsg("包商银行，借款状态非正常状态，不可申请提前还款");
						return toResponseJSON(responseInfo);
					}else if(list.size()!=0){//包银已经成功划扣过，不允许再次申请提前还款
						logger.info("包商银行，逾期和当期均已结清，不允许申请提前扣款");
						responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
						responseInfo.setResMsg("逾期和当期均已结清，不允许申请提前扣款");
						return toResponseJSON(responseInfo);
					}
				}
				/** 提前一次性结清 申请状态 on:申请 off:取消 **/
				String allCleanApplyState = "";
				/** 提前扣款 申请状态 on:申请 off:取消 **/
				String curCleanApplyState = Strings.convertValue(request.getParameter("curCleanApplyState"), String.class);
				
				/** 提前扣款 自动生效时间 **/
				Date requestDate = null;
				
				if ("on".equals(curCleanApplyState)) {
					requestDate = Assert.notDate(request.getParameter("requestDate"), "自动生效时间");
					allCleanApplyState = "off";
				}
				
				loanSpecialRepaymentServiceImpl.updateSpecialOneTimeState(id, allCleanApplyState, curCleanApplyState, null
						, null,requestDate);
				responseInfo = new ResponseInfo(ResponseEnum.SYS_SUCCESS);				
			}else{
				logger.info("服务费未结清，不能变更提前扣款申请");
				responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
				responseInfo.setResMsg("服务费未结清，不能变更提前扣款申请");
			}
		} catch (PlatformException ex) {
			logger.error(ex,ex);
		    responseInfo = ex.toResponseInfo();
		} catch (Exception ex) {
			logger.error(ex,ex);
		    responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
		}
		return toResponseJSON(responseInfo);
    }
    
    /**
     * 查询借款数据(罚息减免申请 模块)
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "searchReliefPenalty")
    @ResponseBody
    
    public String searchReliefPenalty(PersonInfo personInfo, int rows, int page, HttpServletRequest request, HttpServletResponse response) {
		ResponseInfo responseInfo = null;
		try {
			this.createLog(request, SysActionLogTypeEnum.查询, "减免申请", "查询客户数据");
			String salesMan = Strings.convertValue(request.getParameter("salesMan"), String.class);
		    Map<String, Object> paramMap = new HashMap<String, Object>();
		    /** LoanState 定死查询这2类 **/
		    paramMap.put("inLoanState", new String[] {LoanStateEnum.正常.name(),LoanStateEnum.逾期.name()});
		    /** 获取登陆者信息 **/
		    User user = UserContext.getUser();
		    paramMap.put("orgCode", user.getOrgCode());
		    paramMap.put("idNum", personInfo.getIdnum());
		    paramMap.put("mphone", personInfo.getMphone());
		    paramMap.put("name", personInfo.getName());
		    paramMap.put("salesMan", salesMan);
		    String contractNum = request.getParameter("contractNum");
		    paramMap.put("contractNum", contractNum);		    
		    Pager pager = new Pager();
		    pager.setPage(page);
		    pager.setRows(rows);
		    pager.setSidx("LOAN_ID");
		    pager.setSort("ASC");
		    paramMap.put("pager", pager);
		    
		    pager = vLoanInfoServiceImpl.searchRepaymentLoanWithPg(paramMap);
		    List list = pager.getResultList();
		    
		    List<Map> dataMap = new ArrayList<Map>();
		    Date currDate = Dates.getCurrDate();
		    for (int i = 0; i < list.size(); i++) {
				VLoanInfo vLoanInfo = (VLoanInfo) list.get(i);
				Long loanId = vLoanInfo.getId();
				Map<String, Object> map = new HashMap<String, Object>();
				
				map.put("id", loanId);
				map.put("name", vLoanInfo.getPersonInfo().getName());
				map.put("loanType", vLoanInfo.getLoanType());
				map.put("idNum", vLoanInfo.getPersonInfo().getIdnum());
				map.put("pactMoney", vLoanInfo.getPactMoney());
				map.put("time", vLoanInfo.getTime());
				map.put("loanState", vLoanInfo.getLoanState());
				map.put("contractNum", vLoanInfo.getContractNum());
				
				/** 查询未结清的还款计划数据 **/
				List<LoanRepaymentDetail> detailList = afterLoanServiceImpl.getAllInterestOrLoan(currDate, loanId);
				/** 查询逾期本金 **/
				BigDecimal overdueCorpus = afterLoanServiceImpl.getOverdueCorpus(detailList, currDate);
				/** 查询逾期利息 **/
				BigDecimal overdueInterest = afterLoanServiceImpl.getOverdueInterest(detailList, currDate);
				map.put("overdueCorpusAndInterest", overdueCorpus.add(overdueInterest));
				/** 逾期罚息 **/
				BigDecimal fine = afterLoanServiceImpl.getFine(detailList, currDate);
				map.put("fine", fine);
				/** 账户中余额(即挂账金额) **/
				BigDecimal accAmount = afterLoanServiceImpl.getAccAmount(loanId);
				map.put("accAmount", accAmount);
				/** 应还总额 **/
				BigDecimal repaymentAmount = overdueCorpus.add(overdueInterest).add(fine).subtract(accAmount);
				if(repaymentAmount.compareTo(new BigDecimal("0"))< 0 ){
					repaymentAmount = new BigDecimal("0");
				}
				map.put("repaymentAmount", repaymentAmount);
				
				/** 查询是否申请过罚息减免 **/
				Map<String,Object> parMap = new HashMap<String,Object>();
				parMap.put("loanId", loanId);
				parMap.put("currDate", Dates.getCurrDate());
				parMap.put("states",new String[]{SpecialRepaymentStateEnum.申请.name()});
				parMap.put("type", SpecialRepaymentTypeEnum.减免.name());
		        /** 罚息减免申请状态 **/
		        LoanSpecialRepayment reliefPenaltyState = loanSpecialRepaymentServiceImpl.findByLoanIdAndDateAndTypeAndState(parMap);
		        String reliefRequestState = "未申请";
		        if (reliefPenaltyState != null) {
		        	reliefRequestState = reliefPenaltyState.getAmount() + "";
		        }
		        map.put("reliefRequestState", reliefRequestState);
				
				dataMap.add(map);
		    }
		    pager.setResultList(dataMap);
		    return toPGJSONWithCode(pager);
		} catch (Exception ex) {
		    /** 系统忙 **/
			logger.error(ex,ex);
		    responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(), ResponseEnum.SYS_FAILD.getDesc(), "");
		}
		return toResponseJSON(responseInfo);
    }
    
    /**
     * 查询罚息减免申请状态
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "loadReliefPenaltyState/{id}")
    @ResponseBody
    public String loadReliefPenaltyState(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
		AttachmentResponseInfo<Map<String, Object>> attachmentResponseInfo = null;
		try {
		    VLoanInfo loanInfo = vLoanInfoServiceImpl.findByLoanId(id);
		    Assert.notNull(loanInfo, ResponseEnum.FULL_MSG, "借款数据不存在[" + id + "]");
		    Map<String, Object> map = new HashMap<String, Object>();
		    
		    map.put("loanId", id);
	        map.put("currDate", Dates.getCurrDate());
	        map.put("states",new String[]{SpecialRepaymentStateEnum.申请.name(),SpecialRepaymentStateEnum.取消.name()});
	        map.put("type", SpecialRepaymentTypeEnum.减免.name());
	        
	        /** 罚息减免申请状态 **/
	        LoanSpecialRepayment reliefPenaltyState = loanSpecialRepaymentServiceImpl.findByLoanIdAndDateAndTypeAndState(map);
	        
		    map.put("id", id);
		    map.put("money", (reliefPenaltyState == null ? "" : reliefPenaltyState.getAmount()));
		    map.put("reliefPenaltyState", (reliefPenaltyState == null ? false : 
		    	reliefPenaltyState.getSpecialRepaymentState().equals(SpecialRepaymentStateEnum.申请.name()) ? true : false));
		    
		    attachmentResponseInfo = new AttachmentResponseInfo<Map<String, Object>>(ResponseEnum.SYS_SUCCESS);
		    attachmentResponseInfo.setAttachment(map);
		} catch (PlatformException ex) {
			logger.error(ex,ex);
		    attachmentResponseInfo = ex.<Map<String, Object>> toAttachmentResponseInfo();
		} catch (Exception ex) {
			logger.error(ex,ex);
		    attachmentResponseInfo = new AttachmentResponseInfo<Map<String, Object>>(ResponseEnum.SYS_FAILD);
		}
		return toResponseJSON(attachmentResponseInfo);
    }
    
    /**
     * 罚息减免申请 状态变更
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "changeReliefPenaltyState")
    @ResponseBody
    public String changeReliefPenaltyState(HttpServletRequest request, HttpServletResponse response) {
		ResponseInfo responseInfo = null;
		try {
			this.createLog(request, SysActionLogTypeEnum.更新, "减免申请", "减免申请/取消");
			Long id = Strings.convertValue(request.getParameter("id"), Long.class);
			Assert.notNull(id, "借款编号");
			
			//检查该借款人是否有已转让过的债权
		    boolean flag = loanTransferInfoServiceImpl.isLoanTransfer(null,id);
		    if(!flag){
		    	throw new PlatformException(ResponseEnum.FULL_MSG, Strings.errorMsg);
		    }
			
			// 针对龙信小贷、外贸信托、外贸2、包商银行，，判断是否已经完成划扣服务费，如果有，则能减免申请
	        if(iLoanFeeInfoService.isAlreadyDebitServiceCharge(id)){
	        	/** 登陆者信息 **/
	        	User user = UserContext.getUser();
	        	
	        	/** 罚息减免申请状态 on:申请 off:取消 **/
	        	String reliefPenaltyState = Strings.convertValue(request.getParameter("reliefPenaltyState"), String.class);
	        	
	        	String money = Strings.convertValue(request.getParameter("money"), String.class);
	        	
	        	if (!"on".equals(reliefPenaltyState) && !"off".equals(reliefPenaltyState)) {
	        		throw new PlatformException(ResponseEnum.FULL_MSG, "无效参数");
	        	}
	        	
	        	VLoanInfo vLoanInfo = vLoanInfoServiceImpl.findByLoanId(id);
			    if(FundsSourcesTypeEnum.包商银行.getValue().equals(vLoanInfo.getLoanBelong())){
		        	// 得到特殊还款记录表信息
		            LoanSpecialRepayment specialRepayment = loanSpecialRepaymentServiceImpl.findByLoanIdAndRequestDateAndTypesAndState(id,
		         				null,
		         				new String[]{SpecialRepaymentTypeEnum.提前扣款.getValue(),SpecialRepaymentTypeEnum.一次性还款.getValue()}, 
		         				SpecialRepaymentStateEnum.申请.getValue());
//		            Map<String, Object> map = new HashMap<String, Object>();
//		            map.put("loanId", id);
//		            map.put("CurrTermReturnDate", Dates.getCurrDate());
//		            List<LoanRepaymentDetail> planlist = loanRepaymentDetailService.findByLoanIdAndRepaymentState(map);
//		            LoanRepaymentDetail last = planlist.get(planlist.size()-1);
		            //获取当期还款明细
		            Date currTermReturnDate=afterLoanServiceImpl.getCurrTermReturnDate(Dates.getCurrDate(),vLoanInfo.getPromiseReturnDate());
		            LoanRepaymentDetail loanRepaymentDetail = loanRepaymentDetailService.findByLoanAndReturnDate(vLoanInfo,currTermReturnDate);
		            //当提前申请并未生效，而且也没有逾期，不能申请减免
		            if(specialRepayment == null && loanRepaymentDetail.getRepaymentState().equals("结清")){
		            	throw new PlatformException("包商银行提前还款/结清申请未生效，而且也没有逾期，不能申请减免").applyLogLevel(LogLevel.ERROR);
		            }
		        }

	        	loanSpecialRepaymentServiceImpl.updateReliefPenaltyState(id, "on".equals(reliefPenaltyState), money
	        			,user.getId(),"");
	        	responseInfo = new ResponseInfo(ResponseEnum.SYS_SUCCESS);
	        	
	        }else{
	        	logger.info("服务费未结清，不能变更提前扣款申请");
				responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
				responseInfo.setResMsg("服务费未结清，不能变更提前扣款申请");
	        }
		} catch (PlatformException ex) {
			logger.error(ex,ex);
		    responseInfo = ex.toResponseInfo();
		} catch (Exception ex) {
			logger.error(ex,ex);
		    responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
		}
		return toResponseJSON(responseInfo);
    }
    
    /**
     * 罚息减免申请 - Excel文件批量导入
     * 
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/importReliefPenaltyStateFile")
    @ResponseBody
    public void importReliefPenaltyStateFile(@RequestParam(value = "uploadFile", required = false) MultipartFile file, HttpServletRequest request,
	    HttpServletResponse response) throws IOException {
		ResponseInfo responseInfo = null;
		try {
			this.createLog(request, SysActionLogTypeEnum.导入, "减免申请", "减免申请导入");
			
			/** 登陆者信息 **/
			User user = UserContext.getUser();
			
		    UploadFile uploadFile = new UploadFile();
		    uploadFile.setFile(file);
		    uploadFile.setFileType(UploadFileUtil.FILE_TYPE_EXCEL);
		    uploadFile.setFileMaxSize(1024 * 1024 * 5);
	
		    UploadFileUtil.valid(uploadFile);
		    String contentType = file.getContentType();
		    String fileName = file.getOriginalFilename();
		    
		    /** 跟据文件名判断是否已导入 **/
		    List<ComMessageIn> checkFile = comMessageInServiceImpl.findByMessageTypeAndFileName("ReliefPenaltyState", fileName);
		    Assert.isCollectionsEmpty(checkFile, ResponseEnum.FULL_MSG, "当前导入文件名:[" + fileName + "]已经存在!");
		    
			Workbook workBook = WorkbookFactory.create(file.getInputStream());
			
			ExcelTemplet excelTemplet = new ExcelTemplet().new ReliefPenaltyStateInputExcel();
			List<Map<String, String>> result = ExcelUtil.getExcelData(workBook, excelTemplet);
			
			Assert.notCollectionsEmpty(result, ResponseEnum.FILE_EMPTY_FILE,"");
			
			comMessageInServiceImpl.createMsgIn("ReliefPenaltyState", user.getName(), fileName,"减免申请批量导入");
			
			for (int i = 0; i < result.size(); i++) {
				reliefPenaltyParseItem(result.get(i));
			}
			
			ExcelUtil.addResultToWorkBook(workBook, result, excelTemplet);
			
//			response.reset();
			/** 下载文件名 **/
            fileName = "减免申请导入结果" + Dates.getDateTime(Dates.DEFAULT_DATE_FORMAT) + ".xls";
            String enableFileName = new String(fileName.getBytes("GBK"), "ISO-8859-1");   
            response.setHeader("Content-Disposition", "attachment; filename=" + enableFileName);
            response.setHeader("Content-Type",contentType);
			
			OutputStream outputStream = response.getOutputStream();
			workBook.write(outputStream);
			outputStream.flush();
			return;
		} catch (PlatformException ex) {
			logger.error(ex,ex);
		    responseInfo = ex.toResponseInfo();
		} catch (Exception ex) {
		    /** 系统忙 **/
			logger.error(ex,ex);
		    responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
		}
		response.setContentType("text/html");
		response.getWriter().print(toResponseJSON(responseInfo));
    }

    /**
     * 处理罚息减免申请 Excel文件数据
     * 
     * @param values
     */
    private void reliefPenaltyParseItem(Map<String, String> values) {
		/** 登陆者信息 **/
		User user = UserContext.getUser();
		String feedBackMsg = "";
		try {
		    String name = Strings.convertValue(values.get("name"), String.class);
		    String idNum = Strings.convertValue(values.get("idNum"), String.class);
		    String userCode = Strings.convertValue(values.get("userCode"), String.class);
		    String amountPar = Strings.convertValue(values.get("amount"), String.class);
		    String contractNum = Strings.convertValue(values.get("contractNum"), String.class);
		    
		    Assert.notNullAndEmpty(name, "借款人姓名");
		    Assert.notNullAndEmpty(idNum, "借款人身份证");
		    Assert.notNullAndEmpty(userCode, "申请人工号");
		    Assert.notNullAndEmpty(amountPar, "申请金额");
		    Assert.notNullAndEmpty(contractNum, "合同编号");
		    
		    BigDecimal amount = Assert.notBigDecimal(amountPar, "申请金额");
		    if (amount.compareTo(BigDecimal.ZERO) <= 0) {
		    	throw new PlatformException(ResponseEnum.FULL_MSG, "申请金额必须大于0");
		    }
		    
		    /** 判断借款人是否存在（姓名+身份证号） **/
		    PersonInfo personInfo = personInfoServiceImpl.findByIdNumAndName(name, idNum);
		    Assert.notNull(personInfo, ResponseEnum.FULL_MSG, "借款人不存在,请检查借款人姓名和身份证是否正确!");
		    
		    /** 验证申请人工号是否存在 **/
		    ComEmployee comEmployee = comEmployeeServiceImpl.findByUserCode(userCode);
		    Assert.notNull(comEmployee, ResponseEnum.FULL_MSG,"申请人工号提供有误!");
		    
		    Long borrowerId = personInfo.getId();
		    
		    
		    /** 跟据借款人、借款类型、借款状态、 还款日期查询借款数据 **/
		    /*List<VLoanInfo> loan = vLoanInfoServiceImpl.findByBorrowerAndState(borrowerId,new String[] {
			    LoanStateEnum.逾期.name(),LoanStateEnum.坏账.name(),LoanStateEnum.正常.name() });*/
		    /** 根据借款人、合同编号查询借款数据**/
		    List<VLoanInfo> loan = vLoanInfoServiceImpl.findByBorrowerAndContractNumAndState(borrowerId, contractNum,new String[] {
				    LoanStateEnum.逾期.name(),LoanStateEnum.坏账.name(),LoanStateEnum.正常.name() });
		    Assert.notCollectionsEmpty(loan, ResponseEnum.FULL_MSG, "当前提供的客户信息,不存在可减免的借款!");
		    /*if (loan.size() > 1) {
		    	throw new PlatformException(ResponseEnum.FULL_MSG, "借款人存在存在多笔借款记录,请确认!");
		    }*/
		    //借款状态
		    String loanStatus = loan.get(0).getLoanState();
		    if (!LoanStateEnum.逾期.getValue().equals(loanStatus)
		    		&& !LoanStateEnum.坏账.getValue().equals(loanStatus)
                    &&!LoanStateEnum.正常.getValue().equals(loanStatus) ) {
		    	throw new PlatformException(ResponseEnum.FULL_MSG, "该债权状态为:"+loanStatus+",无需申请!");
            }
		    /** 借款编号 **/
		    Long loanId = loan.get(0).getId();
		    
		    /** 验证当天是否已申请过罚息减免 **/
		    Map<String, Object> map = new HashMap<String, Object>();
		    map.put("loanId", loanId);
	        map.put("currDate", Dates.getCurrDate());
	        map.put("state",SpecialRepaymentStateEnum.申请.name());
	        map.put("type", SpecialRepaymentTypeEnum.减免.name());
	        
	        /** 罚息减免申请状态 **/
	        LoanSpecialRepayment reliefPenaltyState = loanSpecialRepaymentServiceImpl.findByLoanIdAndDateAndTypeAndState(map);
	        Assert.isNull(reliefPenaltyState,ResponseEnum.FULL_MSG, "当前借款已经存在罚息减免!");
	        
	        /** 发起减免申请 **/
	        String memo = "由" + user.getName() + "批量导入，导入日期:" + Dates.getDateTime();
	        loanSpecialRepaymentServiceImpl.updateReliefPenaltyState(loanId, true, amount.toString()
	        		,comEmployee.getId(),memo);
	        
		    feedBackMsg = "申请成功!";
		} catch (PlatformException ex) {
			logger.error(ex,ex);
		    feedBackMsg = ex.getMessage();
		} catch (Exception ex) {
			logger.error(ex,ex);
		    feedBackMsg = "系统忙：申请出错!";
		}
		values.put(ExcelTemplet.FEED_BACK_MSG, feedBackMsg);
    }

}