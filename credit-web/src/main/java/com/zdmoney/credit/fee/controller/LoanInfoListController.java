package com.zdmoney.credit.fee.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.zdmoney.credit.common.constant.FundsSourcesTypeEnum;
import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.fee.service.pub.ILoanFeeInputService;
import com.zdmoney.credit.framework.controller.BaseController;

/**
 * 收费查询 Controller
 * 
 * @author fudewan
 */
@Controller
@RequestMapping("/fee/LoanFeeInfolist")
public class LoanInfoListController extends BaseController {

	@Autowired
    private ILoanFeeInputService loanFeeInputService;
    /**
     * 跳转到收费查询主页面
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/loanFeeInfoList")
    public ModelAndView viewPersonLoanDetailPage( HttpServletRequest request, HttpServletResponse response) {
	ModelAndView modelAndView = new ModelAndView("fee/loanFeeInfoList");
		String[] fundsSources = { FundsSourcesTypeEnum.外贸信托.getValue(),
				FundsSourcesTypeEnum.龙信小贷.getValue(),
				FundsSourcesTypeEnum.外贸2.getValue(),
				FundsSourcesTypeEnum.包商银行.getValue(),
				FundsSourcesTypeEnum.渤海2.getValue(),
				FundsSourcesTypeEnum.华瑞渤海.getValue(),
				FundsSourcesTypeEnum.外贸3.getValue(),
				FundsSourcesTypeEnum.陆金所.getValue() };
	modelAndView.addObject("fundsSources", fundsSources);
	return modelAndView;
    }
    
    /**
     * 加载收费数据
     * @param name
     * @param idnum
     * @param contractNum
     * @param grantMoneyDate
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/searchLoanFeeListInfo")
    @ResponseBody
    public String searchLoanFeeListInfo(String name,String idNum,String contractNum,String grantMoneyDate,String state,String fundsSources,int rows, int page, HttpServletRequest request, HttpServletResponse response) {
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("name", name);
    	params.put("idNum",idNum);
    	params.put("contractNum", contractNum);
    	params.put("grantMoneyDate",grantMoneyDate);
    	params.put("state", state);
    	params.put("fundsSources", fundsSources);
    	User user = UserContext.getUser();
    	params.put("orgCode", user.getOrgCode());
    	Pager pager = new Pager();
		pager.setRows(rows);
		pager.setPage(page);
		params.put("pager", pager);
		pager.setSidx("GRANT_MONEY_DATE");
		pager.setSort("DESC");
		pager = loanFeeInputService.findFeeListWithPg(params);
	    return toPGJSONWithCode(pager);
    }
}
