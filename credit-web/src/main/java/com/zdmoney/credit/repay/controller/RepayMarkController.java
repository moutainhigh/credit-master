package com.zdmoney.credit.repay.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.common.constant.FundsSourcesTypeEnum;
import com.zdmoney.credit.common.constant.LoanStateEnum;
import com.zdmoney.credit.common.constant.SysActionLogTypeEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.vo.AttachmentResponseInfo;
import com.zdmoney.credit.common.vo.core.RepayMarkVo;
import com.zdmoney.credit.core.AccountCard;
import com.zdmoney.credit.core.service.pub.IRepaymentDetialCoreService;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.loan.dao.pub.ILoanThirdSourceMemoDao;
import com.zdmoney.credit.loan.domain.LoanThirdSourceMemo;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.ILoanThirdSourceMemoService;
import com.zdmoney.credit.loan.util.OfferFlowUtil;
import com.zdmoney.credit.loan.util.OfferRepayUtil;
import com.zdmoney.credit.offer.domain.OfferFlow;
import com.zdmoney.credit.offer.domain.OfferRepayInfo;
import com.zdmoney.credit.offer.service.pub.IOfferFlowService;
import com.zdmoney.credit.offer.service.pub.IOfferRepayInfoService;
import com.zdmoney.credit.repay.service.pub.IRepayMarkService;
import com.zdmoney.credit.system.domain.ComEmployee;
import com.zdmoney.credit.system.domain.PersonInfo;

/**
 * 还款备注（华奥2、华奥信托、国民信托）
 *
 */

@Controller
@RequestMapping("/repay/repayMark")
public class RepayMarkController extends BaseController {

	@Autowired
	IRepayMarkService repayMarkService;
	
	@Autowired
    IOfferRepayInfoService offerRepayInfoServiceImpl;
	
	@Autowired
    IRepaymentDetialCoreService repaymentDetialCoreServiceImpl;
	
	@Autowired
    IOfferFlowService offerFlowServiceImpl;
	
	@Autowired
	ILoanThirdSourceMemoDao loanThirdSourceMemoDao;
	
	@Autowired
	ILoanThirdSourceMemoService loanThirdSourceMemoService;
	
	/**
	 * 加载还款备注页面
	 * @author 00236633
	 * @return
	 */
	@SuppressWarnings("static-access")
	@RequestMapping(value = "/repayMarkListPage", method = RequestMethod.GET)
	public String listPage(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap) {
		this.createLog(request, SysActionLogTypeEnum.查询, "还款备注", "加载页面处理");
		
		/*String[] fundSourcesArr = new String[] { FundsSourcesTypeEnum.华澳2.getValue(),FundsSourcesTypeEnum.华澳信托.getValue(),
				FundsSourcesTypeEnum.国民信托.getValue()};*/
		String[] fundSourcesArr = new String[] {FundsSourcesTypeEnum.华澳信托.getValue(),
				FundsSourcesTypeEnum.国民信托.getValue(),FundsSourcesTypeEnum.渤海信托.getValue(),FundsSourcesTypeEnum.龙信小贷.getValue(),FundsSourcesTypeEnum.外贸信托.getValue()};
		modelMap.addAttribute("fundsSources", fundSourcesArr);
		
		String[] loanStateArr = new String[] { LoanStateEnum.正常.getValue(),LoanStateEnum.逾期.getValue(),
				LoanStateEnum.结清.getValue(),LoanStateEnum.预结清.getValue()};
		modelMap.addAttribute("loanState", loanStateArr);
		
		return "/repay/repayMarkListPage";
	}
	
	@SuppressWarnings("static-access")
	@RequestMapping("/searchLoanList")
	@ResponseBody
	public String listRepayMark(RepayMarkVo repayMarkVo, int rows, int page, HttpServletRequest request, HttpServletResponse response) {
		this.createLog(request, SysActionLogTypeEnum.查询, "还款备注", "查询债权信息");
		
		Map<String, Object> params=new HashMap<String, Object>();
		
		if(Strings.isNotEmpty(repayMarkVo.getContractNum())){
			params.put("contractNum", repayMarkVo.getContractNum());
		}
		if(Strings.isNotEmpty(repayMarkVo.getFundsSources())){
			params.put("fundsSources", repayMarkVo.getFundsSources());
		}
		if(Strings.isNotEmpty(repayMarkVo.getIdNum())){
			params.put("idNum", repayMarkVo.getIdNum());
		}
		if(Strings.isNotEmpty(repayMarkVo.getLoanState())){
			params.put("loanState", repayMarkVo.getLoanState());
		}
		
		Pager pager = new Pager();
		pager.setPage(page);
		pager.setRows(rows);
		pager.setSidx("LOAN_ID");
		pager.setSort("ASC");
		params.put("pager", pager);	    
		pager =  repayMarkService.searchVLoanInfoList(params);
		
		List<?> list = pager.getResultList();

		@SuppressWarnings("rawtypes")
		List<Map> ss = new ArrayList<Map>();
		for (int i = 0; i < list.size(); i++) {
			VLoanInfo v = (VLoanInfo) list.get(i);
			Map<String,Object> map = new HashMap<String,Object>();
			// 客户
			ComEmployee crmMan = v.getCrmMan();
			if (crmMan != null) {
				// 客户经理
				map.put("salesManName", crmMan.getName());
			} else {
				map.put("salesManName", "");
			}
			// 客服经理
			ComEmployee salesMan = v.getSalesMan();
			if (salesMan != null) {
				// 客服经理
				map.put("personInfoName", salesMan.getName());
			} else {
				map.put("personInfoName", "");
			}
			// 借款人信息
			PersonInfo personInfo = v.getPersonInfo();
			if (personInfo != null) {
				// 借款人
				map.put("name", personInfo.getName());
				// 身份证号码
				map.put("idnum", personInfo.getIdnum());
				// 职业类型
				map.put("profession", personInfo.getProfession());
			} else {
				// 借款人
				map.put("name", "");
				// 身份证号码
				map.put("idnum", "");
				// 职业类型
				map.put("profession", "");
			}
			map.put("borrowerId", v.getBorrowerId());
			map.put("id", v.getId());
			// 借款类型
			map.put("loanType", v.getLoanType());
			// 借款用途
			map.put("purpose", v.getPurpose());
			// 合同金额
			map.put("pactMoney", v.getPactMoney());
			// 审批金额
			map.put("money", v.getMoney());
			// 借款期限
			map.put("time", v.getTime());
			// 状态
			map.put("loanState", v.getLoanState());
			// ftp下载图片用
			map.put("appNo", v.getAppNo());
			//map.put("appNo", "20150331170000001104");
			//合同编号
			map.put("contractNum", v.getContractNum());
			ss.add(map);
		}
		
		pager.setResultList(ss);
		
		return toPGJSONWithCode(pager);
	}
	
	/**
	 * 跳转到客户借款主页面（单笔借款信息查询）
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/viewPersonLoanDetailPage/{id}")
	public ModelAndView viewPersonLoanDetailPage(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
		ModelAndView modelAndView = new ModelAndView("/person/personLoanSpecial");
		modelAndView.addObject("id", id);
		return modelAndView;
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
		ModelAndView modelAndView = new ModelAndView("/person/personLoan_flowDetailSpecial");
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
    @SuppressWarnings({ "rawtypes", "static-access" })
	@RequestMapping(value = "loadFlowDetailData/{id}")
    @ResponseBody
    public String loadFlowDetailData(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
    	this.createLog(request, SysActionLogTypeEnum.查询, "查询账卡信息", "查询账卡信息");
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
		    	LoanThirdSourceMemo memo = loanThirdSourceMemoDao.findByOfferRepayInfoId(offerRepayInfo.getId());
		    	
		    	item.put("id", offerRepayInfo.getId());
		    	item.put("tradeDate", offerRepayInfo.getTradeDate());
		    	item.put("tradeType", offerRepayInfo.getTradeType());
		    	item.put("tradeCode", OfferRepayUtil.getTradeCodeName(offerRepayInfo.getTradeCode()));
		    	item.put("tradeNo", offerRepayInfo.getTradeNo());
		    	item.put("qichuBalance", accountCard.getQichuBalance());
		    	item.put("income", accountCard.getIncome());
		    	item.put("outlay", accountCard.getOutlay());
		    	
		    	item.put("qimoBalance", accountCard.getQimoBalance());
		    	item.put("memo", offerRepayInfo.getMemo());
		    	item.put("financeMemo", memo != null ? memo.getMemo() : "");
		    	
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
     * 查询单条账卡详细信息
     * 
     * @param tradeNo
     *            交易流水号
     * @param request
     * @param response
     * @return
     */
    @SuppressWarnings({ "rawtypes", "static-access" })
	@RequestMapping(value = "loadSingleFlowDetailData/{tradeNo}")
    @ResponseBody
    public String loadSingleFlowDetailData(@PathVariable String tradeNo, HttpServletRequest request, HttpServletResponse response) {
    	this.createLog(request, SysActionLogTypeEnum.查询, "查询账卡信息", "查询单条账卡详细信息");
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
     * 保存财务备注
     * @param repayMarkVo
     * @param request
     * @param response
     * @return
     */
    @SuppressWarnings("static-access")
	@RequestMapping("/saveOrUpdateMemo")
    @ResponseBody
    public String saveOrUpdateMemo(RepayMarkVo repayMarkVo, HttpServletRequest request, HttpServletResponse response) {
    	this.createLog(request, SysActionLogTypeEnum.查询, "财务备注", "保存财务备注");
    	AttachmentResponseInfo<RepayMarkVo> attachmentResponseInfo = null;
		try {
			LoanThirdSourceMemo loanThirdSourceMemo = loanThirdSourceMemoDao.findByOfferRepayInfoId(repayMarkVo.getId());
			if (loanThirdSourceMemo == null) {
				loanThirdSourceMemo = new LoanThirdSourceMemo();
			}
			
			loanThirdSourceMemo.setOfferRepayInfoId(repayMarkVo.getId());
			loanThirdSourceMemo.setMemo(repayMarkVo.getMemo());
			
		    loanThirdSourceMemoService.saveOrUpdate(loanThirdSourceMemo);
		    attachmentResponseInfo = new AttachmentResponseInfo<RepayMarkVo>(ResponseEnum.SYS_SUCCESS.getCode(),ResponseEnum.SYS_SUCCESS.getDesc(), "");
		} catch (PlatformException ex) {
			logger.error(ex,ex);
		    ResponseEnum responseEnum = ex.getResponseCode();
		    attachmentResponseInfo = new AttachmentResponseInfo<RepayMarkVo>(responseEnum.getCode(), ex.getMessage(), "");
		} catch (Exception ex) {
		    /** 系统忙 **/
			logger.error(ex,ex);
		    attachmentResponseInfo = new AttachmentResponseInfo<RepayMarkVo>(ResponseEnum.SYS_FAILD.getCode(), ResponseEnum.SYS_FAILD.getDesc(), "");
		}
		
		return toResponseJSON(attachmentResponseInfo);
    }
}
