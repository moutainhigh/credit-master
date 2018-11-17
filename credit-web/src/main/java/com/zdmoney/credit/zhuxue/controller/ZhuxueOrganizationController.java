package com.zdmoney.credit.zhuxue.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.common.constant.OrgTypeEnum;
import com.zdmoney.credit.common.constant.SysActionLogTypeEnum;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.loan.service.pub.IAfterLoanService;
import com.zdmoney.credit.offer.service.pub.IOfferFlowService;
import com.zdmoney.credit.zhuxue.domain.ZhuxueOrganization;
import com.zdmoney.credit.zhuxue.service.pub.IZhuxueOrganizationService;
import com.zdmoney.credit.zhuxue.vo.ZhuxueOrganizationAccountCardVo;

@Controller
@RequestMapping("/zhuxue")
public class ZhuxueOrganizationController extends BaseController {
	
	@Autowired
	IZhuxueOrganizationService zhuxueOrganizationService;
	@Autowired
	IOfferFlowService offerFlowService;
	@Autowired
	IAfterLoanService afterLoanService;
	
	
	/**
     * 初始化页面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/zhuxueOrganization")
	public ModelAndView viewZhuxueOrganizationPage(HttpServletRequest request, HttpServletResponse response) {
    	this.createLog(request, SysActionLogTypeEnum.查询, "助学贷_渠道机构管理", "加载页面处理");
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("zhuxue/zhuxueOrganizationList");
		modelAndView.addObject("OrgTypes", OrgTypeEnum.values());
		return modelAndView;
	}
    
    /**
     * 查询处理
     * @param zhuxueOrganization
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/searchZhuxueOrganization")
    @ResponseBody
    public String search(ZhuxueOrganization zhuxueOrganization, int rows, int page, HttpServletRequest request, HttpServletResponse response) {
    	this.createLog(request, SysActionLogTypeEnum.查询, "助学贷_渠道机构管理", "执行查询操作");
    	Map<String, Object> paramsMap = new HashMap<String, Object>();
    	
    	Pager pager = new Pager();
		pager.setRows(rows);
		pager.setPage(page);
		pager.setSidx("ID");
		pager.setSort(Pager.DIRECTION_ASC);
		paramsMap.put("code", zhuxueOrganization.getCode());
		paramsMap.put("orgType", zhuxueOrganization.getOrgType());
		paramsMap.put("name", zhuxueOrganization.getName());
		paramsMap.put("pager", pager);
    	
		pager = zhuxueOrganizationService.findWithPgByMap(paramsMap);
		
    	return toPGJSONWithCode(pager);
    }
    
    /**
     * 机构帐卡信息
     * @param zhuxueOrganization
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/zhuxueOrganizationCard/{id}")
    @ResponseBody
    public ModelAndView ViewZhuxueOrganizationCard(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) {
    	this.createLog(request, SysActionLogTypeEnum.查询, "助学贷_渠道机构管理", "加载机构帐卡信息页面");
    	ZhuxueOrganization zhuxueOrganization = zhuxueOrganizationService.findById(id);
    	
    	/**查询机构总收入参数封装**/
    	Map<String, Object> totalIncomeMap = new HashMap<String, Object>();
    	totalIncomeMap.put("tradeCode1", Const.TRADE_CODE_OPENACC_ASC);
    	totalIncomeMap.put("tradeCode2", Const.TRADE_CODE_ORGIN);
    	totalIncomeMap.put("tradeCode3", Const.TRADE_CODE_STDIN);
    	totalIncomeMap.put("acctTitle", Const.ACCOUNT_TITLE_OTHER_EXP);
    	totalIncomeMap.put("appoacct", id);
    	
    	/**机构总收入**/
    	BigDecimal totalIncome = offerFlowService.getZhuxueOrganizationTotalIncome(totalIncomeMap).setScale(2, BigDecimal.ROUND_HALF_UP);
    	
    	/**查询机构总支出参数封装**/
    	Map<String, Object> totalPayMap = new HashMap<String, Object>();
    	totalPayMap.put("acctTitle", Const.ACCOUNT_TITLE_AMOUNT);
    	totalPayMap.put("dorc", "C");
    	totalPayMap.put("account", id);
    	totalPayMap.put("tradeCode1", Const.TRADE_CODE_ORGOUT);
    	totalPayMap.put("tradeCode2", Const.TRADE_CODE_STDOUT);
    	
    	/**机构总支出**/
    	BigDecimal totalPay = offerFlowService.getZhuxueOrganizationTotalPay(totalPayMap).setScale(2, BigDecimal.ROUND_HALF_UP);
    	
    	/**挂账金额**/
    	BigDecimal totalAmount = afterLoanService.getAccAmount(id).setScale(2, BigDecimal.ROUND_HALF_UP);
    	
    	ModelAndView modelAndView = new ModelAndView();
    	modelAndView.addObject("zhuxueOrganization", zhuxueOrganization);
    	modelAndView.addObject("totalIncome", totalIncome);
    	modelAndView.addObject("totalPay", totalPay);
    	modelAndView.addObject("totalAmount", totalAmount);
		modelAndView.setViewName("zhuxue/zhuxueOrganizationCard");
		return modelAndView;
    }
    
    /**
     * 查询处理机构帐卡信息
     * @param zhuxueOrganization
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/searchZhuxueOrganizationAccountInfo")
    @ResponseBody
    public String searchAccountInfo(ZhuxueOrganizationAccountCardVo accountCardVo, HttpServletRequest request, HttpServletResponse response) {
    	this.createLog(request, SysActionLogTypeEnum.查询, "助学贷_渠道机构管理", "查询处理机构帐卡信息");
    	
    	Map<String, Object> paramsMap = new HashMap<String, Object>();
    	
    	Pager pager = new Pager();
		pager.setRows(10000);
		pager.setPage(1);
		paramsMap.put("id", accountCardVo.getZhuxueOrganizationId().toString());
		paramsMap.put("startDate", accountCardVo.getStartDate());
		paramsMap.put("endDate", accountCardVo.getEndDate());
		paramsMap.put("pager", pager);
		
		pager = zhuxueOrganizationService.getZhuxueOrganizationAccountCard(paramsMap);
		
		return toPGJSONWithCode(pager);
    }
}
