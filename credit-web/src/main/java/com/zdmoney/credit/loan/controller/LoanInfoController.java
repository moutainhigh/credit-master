package com.zdmoney.credit.loan.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zdmoney.credit.common.constant.*;
import com.zdmoney.credit.common.util.*;
import com.zdmoney.credit.loan.domain.*;
import com.zdmoney.credit.loan.service.pub.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.vo.AttachmentResponseInfo;
import com.zdmoney.credit.common.vo.ResponseInfo;
import com.zdmoney.credit.common.vo.core.PublicStoreAccountVo;
import com.zdmoney.credit.core.AccountCard;
import com.zdmoney.credit.core.service.pub.IRepaymentDetialCoreService;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.loan.util.OfferFlowUtil;
import com.zdmoney.credit.loan.util.OfferRepayUtil;
import com.zdmoney.credit.offer.domain.OfferFlow;
import com.zdmoney.credit.offer.domain.OfferRepayInfo;
import com.zdmoney.credit.offer.service.pub.IOfferFlowService;
import com.zdmoney.credit.offer.service.pub.IOfferRepayInfoService;
import com.zdmoney.credit.offer.vo.OfferRepayInfoVo;
import com.zdmoney.credit.person.service.pub.IPersonCarInfoService;
import com.zdmoney.credit.person.service.pub.IPersonEntrepreneurInfoService;
import com.zdmoney.credit.person.service.pub.IPersonHouseInfoService;
import com.zdmoney.credit.system.domain.ComEmployee;
import com.zdmoney.credit.system.domain.ComOrganization;
import com.zdmoney.credit.system.domain.PersonInfo;
import com.zdmoney.credit.system.service.pub.IComEmployeeService;
import com.zdmoney.credit.system.service.pub.IComOrganizationService;
import com.zdmoney.credit.system.service.pub.IPersonInfoService;

/**
 * 借款数据 Controller
 * 
 * @author Ivan
 */
@Controller
@RequestMapping("/loanInfo")
public class LoanInfoController extends BaseController {

    @Autowired
    @Qualifier("personInfoServiceImpl")
    IPersonInfoService personInfoServiceImpl;

    @Autowired
    @Qualifier("personHouseInfoServiceImpl")
    IPersonHouseInfoService personHouseInfoServiceImpl;

    @Autowired
    @Qualifier("personCarInfoServiceImpl")
    IPersonCarInfoService personCarInfoServiceImpl;

    @Autowired
    @Qualifier("personEntrepreneurInfoServiceImpl")
    IPersonEntrepreneurInfoService personEntrepreneurInfoServiceImpl;

    @Autowired
    @Qualifier("comOrganizationServiceImpl")
    IComOrganizationService comOrganizationServiceImpl;

    @Autowired
    @Qualifier("VLoanInfoServiceImpl")
    IVLoanInfoService vLoanInfoServiceImpl;

    @Autowired
    @Qualifier("loanBankServiceImpl")
    ILoanBankService loanBankServiceImpl;

    @Autowired
    @Qualifier("comEmployeeServiceImpl")
    IComEmployeeService comEmployeeServiceImpl;

    @Autowired
    @Qualifier("loanRepaymentDetailServiceImpl")
    ILoanRepaymentDetailService loanRepaymentDetailServiceImpl;

    @Autowired
    @Qualifier("offerRepayInfoServiceImpl")
    IOfferRepayInfoService offerRepayInfoServiceImpl;

    @Autowired
    @Qualifier("loanSpecialRepaymentServiceImpl")
    ILoanSpecialRepaymentService loanSpecialRepaymentServiceImpl;

    @Autowired
    @Qualifier("afterLoanServiceImpl")
    IAfterLoanService afterLoanServiceImpl;
    
    @Autowired
    @Qualifier("repaymentDetialCoreServiceImpl")
    IRepaymentDetialCoreService repaymentDetialCoreServiceImpl;
    
    @Autowired
    @Qualifier("offerFlowServiceImpl")
    IOfferFlowService offerFlowServiceImpl;
    
    @Autowired
    ILoanRepaymentDetailService LoanRepaymentDetailService;

	@Autowired
	ISpecialRepaymentApplyService specialRepaymentApplyService;
	@Autowired
	ILoanTransferInfoService loanTransferInfoService;
	@Autowired
	ILoanSettleInfoService loanSettleInfoService;

    /**
     * 跳转到客户借款主页面（单笔借款信息查询）
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/viewPersonLoanDetailPage/{id}")
    public ModelAndView viewPersonLoanDetailPage(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
	ModelAndView modelAndView = new ModelAndView("/person/personLoan");
	modelAndView.addObject("id", id);
	return modelAndView;
    }

    /**
     * 查询借款信息(单笔)
     * 
     * @param id
     *            借款编号
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "loadLoanInfoData/{id}")
    public ModelAndView loadLoanInfoData(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
	ModelAndView modelAndView = new ModelAndView("/person/personLoan_info");

	VLoanInfo vLoanInfo = vLoanInfoServiceImpl.findByLoanId(id);
	/** 还款银行 **/
	LoanBank giveloanBank = loanBankServiceImpl.findById(vLoanInfo.getGiveBackBankId());
	/** 放款银行 **/
	LoanBank grantloanBank = loanBankServiceImpl.findById(vLoanInfo.getGrantBankId());
	/** 营业网点 **/
	ComOrganization comOrganization = comOrganizationServiceImpl.get(vLoanInfo.getSalesDepartmentId());
	/** 客户经理 **/
	ComEmployee salesMan = comEmployeeServiceImpl.get(vLoanInfo.getSalesmanId());
	/** 客服 **/
	ComEmployee crmMan = comEmployeeServiceImpl.get(vLoanInfo.getCrmId());
	/** 借款人信息 **/
	PersonInfo personInfo = personInfoServiceImpl.findById(vLoanInfo.getBorrowerId());
	/**是否委外**/
	Long isOutSourcing=vLoanInfoServiceImpl.getOutSourcing(id);
	/**是否转让**/
	LoanTransferInfo loanTransfer = loanTransferInfoService.findLoanTransferInfoByLoanId(id);
	/**转让状态**/
	LoanSettleInfo loanSettleInfo = loanSettleInfoService.findLoanSettleInfoByLoanId(id);
	/**
	 * 根据合同来源和债权去向查询对公账户信息
	 */
	Map<String,Object> params = new HashMap<String, Object>();
	params.put("fundsSources", vLoanInfo.getFundsSources());
	params.put("loanBelong",vLoanInfo.getLoanBelong());
	PublicStoreAccountVo accountDetail = vLoanInfoServiceImpl.getPublicStoreAccountDetail(params);
	
	vLoanInfo.setGiveloanBank(giveloanBank);
	vLoanInfo.setGrantLoanBank(grantloanBank);
	vLoanInfo.setComOrganization(comOrganization);
	vLoanInfo.setSalesMan(salesMan);
	vLoanInfo.setCrmMan(crmMan);
	vLoanInfo.setPersonInfo(personInfo);
	vLoanInfo.setAccountDetail(accountDetail);
	if(0==isOutSourcing){
		vLoanInfo.setIsOutSourcing("否");
	}else{
		vLoanInfo.setIsOutSourcing("是");
	}
	if(loanTransfer == null || (loanTransfer!=null && "未转让".equals(loanTransfer.getTransferBatch()))){
		vLoanInfo.setTransferBatch("");
	}else if(loanTransfer!=null && !"未转让".equals(loanTransfer.getTransferBatch())){
		vLoanInfo.setTransferBatch(loanTransfer.getTransferBatch());
	}
	if (null!=loanSettleInfo){
		vLoanInfo.setTransferState(loanSettleInfo.getTransferState());
	}
	modelAndView.addObject("loan", vLoanInfo);
	return modelAndView;
    }

    /**
     * 查询还款汇总信息(单笔)
     * 
     * @param id
     *            借款编号
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "jumpRepayPage/{id}")
    public ModelAndView jumpRepayPage(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
	ModelAndView modelAndView = new ModelAndView("/person/personLoan_repay");
	ResponseInfo responseInfo = null;
	Date tradeDate = Dates.getCurrDate();
	try {
	    OfferRepayInfoVo offerRepayInfoVo = offerRepayInfoServiceImpl.getRepayInfo(id, tradeDate);
	    if (offerRepayInfoVo == null) {
	    	throw new PlatformException(ResponseEnum.FULL_MSG, "无借款数据或已结清");
	    }
	    VLoanInfo vLoanInfo = vLoanInfoServiceImpl.findByLoanId(id);
	    Assert.notNullAndEmpty(vLoanInfo, "借款");
	    Long borrowerId = vLoanInfo.getBorrowerId();
	    PersonInfo personInfo = personInfoServiceImpl.findById(borrowerId);
	    Assert.notNullAndEmpty(personInfo, "借款人");
	    offerRepayInfoVo.setId(id);
	    offerRepayInfoVo.setName(personInfo.getName());
	    offerRepayInfoVo.setIdNum(personInfo.getIdnum());
	    offerRepayInfoVo.setMphone(personInfo.getMphone());
	    offerRepayInfoVo.setIdType(personInfo.getIdtype());
	    //还款等级
		Map levelMap = new HashMap();
		levelMap.put("loanId",id);
	    String repaymentLevel =  LoanRepaymentDetailService.findRepaymentLevel(levelMap);
		this.setOfferRepayInfoVoTheRelief(offerRepayInfoVo);
		offerRepayInfoVo.setRepaymentLevel(repaymentLevel);
	    modelAndView.addObject("offerRepayInfoVo", offerRepayInfoVo);
	    responseInfo = new ResponseInfo(ResponseEnum.SYS_SUCCESS);
	} catch (PlatformException ex) {
	    logger.error(ex,ex);
	    responseInfo = ex.toResponseInfo();
	} catch (Exception ex) {
		logger.error(ex,ex);
	    responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
	}
	modelAndView.addObject("responseInfo", responseInfo);
	return modelAndView;
    }

    /**
     * 跳转还款详细信息页面（还款计划页面）
     * 
     * @param id
     *            借款编号
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "jumpRepaymentDetailPage/{id}")
    public ModelAndView jumpRepaymentDetailPage(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
	ModelAndView modelAndView = new ModelAndView("/person/personLoan_repaymentDetail");
	return modelAndView;
    }

    /**
     * 查询还款详细信息（还款计划数据）
     * 
     * @param id
     *            借款编号
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "loadRepaymentDetailData/{id}")
    @ResponseBody
    public String loadRepaymentDetailData(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
	AttachmentResponseInfo<Object> attachmentResponseInfo = null;
	try {
	    List<LoanRepaymentDetail> repaymentDetailList = new ArrayList<LoanRepaymentDetail>();
	    VLoanInfo vLoanInfo = vLoanInfoServiceImpl.findByLoanId(id);
	    if (vLoanInfo == null) {
		throw new PlatformException(ResponseEnum.VALIDATE_RESULT_ISNULL, new String[] { "借款编号：" + id });
	    }
	    Long borrowerId = vLoanInfo.getBorrowerId();
	    PersonInfo personInfo = personInfoServiceImpl.findById(borrowerId);
	    if (personInfo == null) {
		throw new PlatformException(ResponseEnum.VALIDATE_RESULT_ISNULL, new String[] { "客户编号：" + borrowerId });
	    }

	    if (Strings.isNotEmpty(id)) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("loanId", id);
		repaymentDetailList = loanRepaymentDetailServiceImpl.findByLoanIdAndRepaymentState(map);
	    }
	    Pager pager = new Pager();
	    pager.setTotalCount(repaymentDetailList.size());
	    pager.setResultList(repaymentDetailList);
	    return toPGJSONWithCode(pager);

	} catch (PlatformException ex) {
		logger.error(ex,ex);
	    ResponseEnum responseEnum = ex.getResponseCode();
	    attachmentResponseInfo = new AttachmentResponseInfo<Object>(responseEnum.getCode(), ex.getMessage(), "");
	} catch (Exception ex) {
	    /** 系统忙 **/
		logger.error(ex,ex);
	    attachmentResponseInfo = new AttachmentResponseInfo<Object>(ResponseEnum.SYS_FAILD.getCode(), ResponseEnum.SYS_FAILD.getDesc(), "");
	}
	return toResponseJSON(attachmentResponseInfo);
    }

    /**
     * 跳转账卡信息页面
     * 
     * @param id
     *            借款编号
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "jumpFlowDetailPage/{id}")
    public ModelAndView jumpFlowDetailPage(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
	ModelAndView modelAndView = new ModelAndView("/person/personLoan_flowDetail");
	return modelAndView;
    }

    /**
     * 查询账卡信息
     * 
     * @param id
     *            借款编号
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "loadFlowDetailData/{id}")
    @ResponseBody
    public String loadFlowDetailData(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
	AttachmentResponseInfo<Object> attachmentResponseInfo = null;
	try {
	    Map<String, Object> paramMap = new HashMap<String, Object>();
	    paramMap.put("loanId", id);
	    paramMap.put("notEqTradeCode", new String[] { Const.TRADE_CODE_OPENACC, Const.TRADE_CODE_OPENACC_ASC, Const.TRADE_CODE_DRAWRISK,
		    Const.TRADE_CODE_DRAWRISK_STUDENT });
	    paramMap.put("sort", "trade_no");
	    List<OfferRepayInfo> repayInfoList = offerRepayInfoServiceImpl.findListByMap(paramMap);
	    
	    List<Map> resultList = new ArrayList<Map>();
	    
	    for (OfferRepayInfo offerRepayInfo : repayInfoList) {
	    	Map<String,Object> item = new HashMap<String,Object>();
	    	AccountCard accountCard = repaymentDetialCoreServiceImpl.toReturnAccountCardVo(offerRepayInfo);
	    	
	    	item.put("tradeDate", offerRepayInfo.getTradeDate());
	    	item.put("tradeType", offerRepayInfo.getTradeType());
	    	item.put("tradeCode", OfferRepayUtil.getTradeCodeName(offerRepayInfo.getTradeCode()));
	    	item.put("tradeNo", offerRepayInfo.getTradeNo());
	    	item.put("qichuBalance", accountCard.getQichuBalance());
	    	item.put("income", accountCard.getIncome());
	    	item.put("outlay", accountCard.getOutlay());
	    	
	    	item.put("qimoBalance", accountCard.getQimoBalance());
	    	item.put("memo", offerRepayInfo.getMemo());
	    	resultList.add(item);
	    }
//	    List<ReturnAccountCardVO> list = repaymentDetialCoreServiceImpl.toReturnAccountCardVo(repayInfoList);
	    
	    Pager pager = new Pager();
	    pager.setTotalCount(resultList.size());
	    pager.setResultList(resultList);
	    return toPGJSONWithCode(pager);
	} catch (PlatformException ex) {
		logger.error(ex,ex);
	    ResponseEnum responseEnum = ex.getResponseCode();
	    attachmentResponseInfo = new AttachmentResponseInfo<Object>(responseEnum.getCode(), ex.getMessage(), "");
	} catch (Exception ex) {
	    /** 系统忙 **/
		logger.error(ex,ex);
	    attachmentResponseInfo = new AttachmentResponseInfo<Object>(ResponseEnum.SYS_FAILD.getCode(), ResponseEnum.SYS_FAILD.getDesc(), "");
	}
	return toResponseJSON(attachmentResponseInfo);
    }
    
    /**
     * 查询单条账卡详细信息
     * 
     * @param tradeNo
     *            交易流水号
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "loadSingleFlowDetailData/{tradeNo}")
    @ResponseBody
    public String loadSingleFlowDetailData(@PathVariable String tradeNo, HttpServletRequest request, HttpServletResponse response) {
		AttachmentResponseInfo<Object> attachmentResponseInfo = null;
		try {
			List<Map> resultList = new ArrayList<Map>();
			List<OfferFlow> flowList = offerFlowServiceImpl.findByTradeNo(tradeNo);
			
		    for (OfferFlow offerFlow : flowList) {
		    	Map<String,Object> item = new HashMap<String,Object>();
		    	item.put("accountTitleName", OfferFlowUtil.getAccountTitle(offerFlow));
		    	item.put("tradeDate", offerFlow.getTradeDate());
		    	String itemCount = Strings.isDecimal(Strings.parseString(offerFlow.getMemo2())) ? offerFlow.getMemo2() : "";
		    	item.put("itemCount", itemCount);
		    	item.put("amount", offerFlow.getTradeAmount());
		    	resultList.add(item);
		    }
		    
		    Pager pager = new Pager();
		    pager.setTotalCount(resultList.size());
		    pager.setResultList(resultList);
		    return toPGJSONWithCode(pager);
		} catch (PlatformException ex) {
			logger.error(ex,ex);
		    ResponseEnum responseEnum = ex.getResponseCode();
		    attachmentResponseInfo = new AttachmentResponseInfo<Object>(responseEnum.getCode(), ex.getMessage(), "");
		} catch (Exception ex) {
		    /** 系统忙 **/
			logger.error(ex,ex);
		    attachmentResponseInfo = new AttachmentResponseInfo<Object>(ResponseEnum.SYS_FAILD.getCode(), ResponseEnum.SYS_FAILD.getDesc(), "");
		}
		return toResponseJSON(attachmentResponseInfo);
    }
    

    /**
     * 跳转还款录入页面
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "jumpRepaymentInputPage")
    public ModelAndView jumpRepaymentInputPage(HttpServletRequest request, HttpServletResponse response) {
	ModelAndView modelAndView = new ModelAndView("/repay/repaymentInput");
	return modelAndView;
    }

    /**
     * 查询借款数据(还款录入模块)
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "searchRepaymentLoan")
    @ResponseBody
    public String searchRepaymentLoan(PersonInfo personInfo, int rows, int page, HttpServletRequest request, HttpServletResponse response) {
	ResponseInfo responseInfo = null;
	try {
		this.createLog(request, SysActionLogTypeEnum.查询, "还款录入", "查询客户数据");
	    Map<String, Object> paramMap = new HashMap<String, Object>();
	    /** LoanState 定死查询这3类 **/
	    paramMap.put("inLoanState", new String[] { LoanStateEnum.正常.name(), LoanStateEnum.逾期.name(), LoanStateEnum.坏账.name() });
	    /** 获取登陆者信息 **/
	    User user = UserContext.getUser();
	    paramMap.put("orgCode", user.getOrgCode());
	    paramMap.put("idNum", personInfo.getIdnum());
	    paramMap.put("name", personInfo.getName());
	    if(Strings.isNotEmpty(request.getParameter("contractNum"))){
	    	paramMap.put("contractNum", request.getParameter("contractNum") );//
		}

	    Pager pager = new Pager();
	    pager.setPage(page);
	    pager.setRows(rows);
	    pager.setSidx("LOAN_ID");
	    pager.setSort("ASC");
	    paramMap.put("pager", pager);

	    pager = vLoanInfoServiceImpl.searchRepaymentLoanWithPg(paramMap);
	    List list = pager.getResultList();

	    List<Map> dataMap = new ArrayList<Map>();
	    for (int i = 0; i < list.size(); i++) {
		VLoanInfo vLoanInfo = (VLoanInfo) list.get(i);
		Long loanId = vLoanInfo.getId();
		Map<String, Object> map = new HashMap<String, Object>();
		ComEmployee crmMan = vLoanInfo.getCrmMan();
		/** 客服所在营业网点 **/
		String crmOrgName = "";
		if (crmMan != null) {
		    Long crmOrgId = crmMan.getOrgId();
		    ComOrganization comOrganization = comOrganizationServiceImpl.get(crmOrgId);
		    if (comOrganization != null) {
			crmOrgName = comOrganization.getName();
		    }
		}
		map.put("id", loanId);
		map.put("crmOrgName", crmOrgName);
		map.put("name", vLoanInfo.getPersonInfo().getName());
		map.put("loanType", vLoanInfo.getLoanType());
		map.put("contractNum", vLoanInfo.getContractNum());
		String idNum = Strings.convertValue(vLoanInfo.getPersonInfo().getIdnum(),String.class);
		if (idNum.length() > 6) {
			idNum = ".." + idNum.substring(idNum.length() - 6);
		}
		map.put("idNum", idNum);
		map.put("pactMoney", vLoanInfo.getPactMoney());
		map.put("time", vLoanInfo.getTime());
		map.put("loanState", vLoanInfo.getLoanState());

		/** 查询是否申请提前还款 **/
		String isOneTime = "未申请";
		LoanSpecialRepayment loanSpecialRepayment = loanSpecialRepaymentServiceImpl.findbyLoanAndType(loanId, SpecialRepaymentTypeEnum.一次性还款.name(),
			SpecialRepaymentStateEnum.申请.name());
		if (loanSpecialRepayment != null) {
		    isOneTime = "已申请";
		}
		map.put("isOneTime", isOneTime);

		/** 查询减免金额 (债权ID、申请日期、类型数组、状态) **/
		Double reliefOfFine = specialRepaymentApplyService.getReleifAmount(loanId).doubleValue();

		map.put("reliefOfFine", reliefOfFine);
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
     * 查询借款 还款汇总信息
     * 
     * @param id
     *            借款编号
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "searchRepayInfo/{id}")
    @ResponseBody
    public String searchRepayInfo(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
	AttachmentResponseInfo<OfferRepayInfoVo> attachmentResponseInfo = null;
	Date tradeDate = Dates.getCurrDate();
	try {
	    OfferRepayInfoVo offerRepayInfoVo = offerRepayInfoServiceImpl.getRepayInfo(id, tradeDate);
	    if (offerRepayInfoVo != null) {
			attachmentResponseInfo = new AttachmentResponseInfo<OfferRepayInfoVo>(ResponseEnum.SYS_SUCCESS.getCode(), ResponseEnum.SYS_SUCCESS.getDesc(),
				Strings.convertValue(id, String.class));
			offerRepayInfoVo.setId(id);
			VLoanInfo vLoanInfo = vLoanInfoServiceImpl.findByLoanId(id);
			Assert.notNullAndEmpty(vLoanInfo, "借款");
			Long borrowerId = vLoanInfo.getBorrowerId();
			PersonInfo personInfo = personInfoServiceImpl.findById(borrowerId);
			Assert.notNullAndEmpty(personInfo, "借款人");
			offerRepayInfoVo.setName(personInfo.getName());
			offerRepayInfoVo.setIdNum(personInfo.getIdnum());
			offerRepayInfoVo.setMphone(personInfo.getMphone());
			offerRepayInfoVo.setContractNum(vLoanInfo.getContractNum());
			this.setOfferRepayInfoVoTheRelief(offerRepayInfoVo);
			attachmentResponseInfo.setAttachment(offerRepayInfoVo);
	    } else {
	    	attachmentResponseInfo = new AttachmentResponseInfo<OfferRepayInfoVo>(ResponseEnum.FULL_MSG,"该客户截止当前无需还款！");
	    }
	} catch (PlatformException ex) {
		logger.error(ex,ex);
	    ResponseEnum responseCode = ex.getResponseCode();
	    attachmentResponseInfo = new AttachmentResponseInfo<OfferRepayInfoVo>(responseCode.getCode(), ex.getMessage(), Strings.convertValue(id,
		    String.class));
	} catch (Exception ex) {
	    /** 系统忙 **/
		logger.error(ex,ex);
	    attachmentResponseInfo = new AttachmentResponseInfo<OfferRepayInfoVo>(ResponseEnum.SYS_FAILD.getCode(), ResponseEnum.SYS_FAILD.getDesc(),
		    Strings.convertValue(id, String.class));
	}
	return toResponseJSON(attachmentResponseInfo);
    }

	/**
	 * 设置减免金额 应还总额（不含当期）-减免 应还总会（包含当期）-减免
	 * @param offerRepayInfoVo
	 */
	private void setOfferRepayInfoVoTheRelief(OfferRepayInfoVo offerRepayInfoVo){
		BigDecimal relief = specialRepaymentApplyService.getReleifAmount(offerRepayInfoVo.getId());
		if (new BigDecimal(0.00).compareTo(relief) == -1) {
			offerRepayInfoVo.setRelief(relief);
			//应还总额（不含当期）
			BigDecimal overdueAmount = offerRepayInfoVo.getOverdueAmount();
			overdueAmount = overdueAmount.subtract(relief);
			offerRepayInfoVo.setOverdueAmount(overdueAmount.compareTo(new BigDecimal(0.00)) == 1 ? overdueAmount : new BigDecimal(0.00));
			//应还总额（包含当期）
			BigDecimal currAllAmount = offerRepayInfoVo.getCurrAllAmount();
			currAllAmount = currAllAmount.subtract(relief);
			offerRepayInfoVo.setCurrAllAmount(currAllAmount.compareTo(new BigDecimal(0.00)) == 1 ? currAllAmount : new BigDecimal(0.00));
		}
	}
}
