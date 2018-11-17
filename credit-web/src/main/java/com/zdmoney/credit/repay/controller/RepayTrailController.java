package com.zdmoney.credit.repay.controller;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.core.calculator.factory.CalculatorFactoryImpl;
import com.zdmoney.credit.core.calculator.pub.ICalculator;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.loan.domain.LoanRepaymentDetail;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.IAfterLoanService;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import com.zdmoney.credit.repay.service.pub.IRepayTrailService;
import com.zdmoney.credit.repay.vo.RepayTrailVo;
import com.zdmoney.credit.system.domain.PersonInfo;
import com.zdmoney.credit.system.service.pub.IPersonInfoService;

/**
 * 还款试算
 * @author Anfq
 *
 * 2015年8月10日
 */
@Controller
@RequestMapping("/repay/repayInfo")
public class RepayTrailController extends BaseController{
	@Autowired
	IRepayTrailService repayTrailService;
	
	@Autowired
	IAfterLoanService  afterLoanService;
	@Autowired
	IVLoanInfoService vLoanInfoService;
	@Autowired @Qualifier("personInfoServiceImpl")
	IPersonInfoService personInfoServiceImpl;
	
	
	@RequestMapping("/repayTrail")
	public String repayTrail(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap) {
		String [] repayTypeArr=new String[]{"正常","一次性"};
		modelMap.put("repayTypeArr", repayTypeArr);	
		Date  repayDate=  new Date();
		modelMap.put("repayDate", repayDate);	
		return "/repay/repayTrail";
		
	}

	
	/**
	 * 一次性
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/repayTrailOneTime")
	public String repayTrailOneTime(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap) {
		String [] repayTypeArr=new String[]{"正常","一次性"};
		modelMap.put("repayTypeArr", repayTypeArr);	
		Date  repayDate=  new Date();
		modelMap.put("repayDate", repayDate);	
		return "/repay/repayTrailOneTime";
		
	}
	/**
	 * 正常
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/repayTrailNormal")
	public String repayTrailNormal(HttpServletRequest request, HttpServletResponse response,ModelMap modelMap) {
		String [] repayTypeArr=new String[]{"正常","一次性"};
		modelMap.put("repayTypeArr", repayTypeArr);	
		Date  repayDate=  new Date();
		modelMap.put("repayDate", repayDate);	
		return "/repay/repayTrailNormal";
		
	}
	
	/**
	 * 正常还款试算
	 * @param repayTrailVo
	 * @param rows
	 * @param page
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws ParseException
	 */
	@ResponseBody
	@RequestMapping("/listRepayTrailNormal")	
	public String listRepayTrailNomal(RepayTrailVo repayTrailVo,@RequestParam("grantMoneyDateStart") String grantMoneyDateStart,int rows, int page,HttpServletRequest request, HttpServletResponse response ,ModelMap modelMap) throws UnsupportedEncodingException, ParseException{
		String  repayDateStr=request.getParameter("grantMoneyDateStart");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date  repayDate=  format.parse(repayDateStr);
		String  repayType=request.getParameter("repayType");		
		Map<String, Object> params=new HashMap<String, Object>();
		if(Strings.isNotEmpty(request.getParameter("name"))){
			params.put("name", request.getParameter("name") );//
		}
		if(Strings.isNotEmpty(request.getParameter("idnum"))){
			params.put("idNum", request.getParameter("idnum") );//
		}
		if(Strings.isNotEmpty(request.getParameter("mphone"))){
			params.put("mphone", request.getParameter("mphone") );//
		}
		if(Strings.isNotEmpty(request.getParameter("contractNum"))){
			params.put("contractNum", request.getParameter("contractNum") );//合同编号
		}
		User user=UserContext.getUser();
	    if(Strings.isNotEmpty(user.getOrgCode() )){
	    	params.put("code",user.getOrgCode());
		}	
	    Pager pager = new Pager();
		pager.setPage(page);
		pager.setRows(rows);
		pager.setSidx("ID");
		pager.setSort("ASC");
		params.put("pager", pager);	    
		pager =  vLoanInfoService.searchVLoanInfoWithPg(params);  		
		List<VLoanInfo> list = pager.getResultList();		
		List<RepayTrailVo> repayTrailVoList=new ArrayList<RepayTrailVo>();
		for(VLoanInfo vLoanInfo:list){
			RepayTrailVo repayTrailVos=new RepayTrailVo();				
			repayTrailVos=GetRepaymentInfoForTrail(vLoanInfo,repayDate,repayType);
			repayTrailVoList.add(repayTrailVos);
		}
		pager.setResultList(repayTrailVoList);
        // 将数据对象转换成JSON字符串，返回给前台
        return toPGJSONWithCode(pager);
	}
	
	
	
	
	/**
	 * 一次性还款试算
	 * @param repayTrailVo
	 * @param rows
	 * @param page
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws ParseException
	 */
	@ResponseBody
	@RequestMapping("/listRepayTrailOneTime")	
	public String listRepayTrailOneTime(RepayTrailVo repayTrailVo,int rows, int page,HttpServletRequest request, HttpServletResponse response ,ModelMap modelMap) throws UnsupportedEncodingException, ParseException{
		String  repayDateStr=request.getParameter("grantMoneyDateStart");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date  repayDate=  format.parse(repayDateStr);
		String  repayType=request.getParameter("repayType");		
		Map<String, Object> params=new HashMap<String, Object>();
		if(Strings.isNotEmpty(request.getParameter("name"))){
			params.put("name", request.getParameter("name") );//
		}
		if(Strings.isNotEmpty(request.getParameter("idnum"))){
			params.put("idNum", request.getParameter("idnum") );//
		}
		if(Strings.isNotEmpty(request.getParameter("mphone"))){
			params.put("mphone", request.getParameter("mphone") );//
		}
		if(Strings.isNotEmpty(request.getParameter("contractNum"))){
			params.put("contractNum", request.getParameter("contractNum") );//合同编号
		}
		User user=UserContext.getUser();
	    if(Strings.isNotEmpty(user.getOrgCode() )){
	    	params.put("code",user.getOrgCode());//departmentType.add(user.getOrgCode());
		}	   
	    Pager pager = new Pager();
		pager.setPage(page);
		pager.setRows(rows);
		pager.setSidx("ID");
		pager.setSort("ASC");
		params.put("pager", pager);	    
		pager =  vLoanInfoService.searchVLoanInfoWithPg(params);  		
		List<VLoanInfo> list = pager.getResultList();		
		List<RepayTrailVo> repayTrailVoList=new ArrayList<RepayTrailVo>();
		for(VLoanInfo vLoanInfo:list){
			RepayTrailVo repayTrailVos=new RepayTrailVo();				
			repayTrailVos=GetRepaymentInfoForTrail(vLoanInfo,repayDate,repayType);
			repayTrailVoList.add(repayTrailVos);
		}
		pager.setResultList(repayTrailVoList);
        // 将数据对象转换成JSON字符串，返回给前台
        return toPGJSONWithCode(pager);
	}
	
	
	 private RepayTrailVo GetRepaymentInfoForTrail(VLoanInfo vLoanInfo,Date tradeDate,String repayType){
		 RepayTrailVo repayTrailVo=new RepayTrailVo();
		 Long loanId = vLoanInfo.getId();
		 PersonInfo person=	personInfoServiceImpl.findById(vLoanInfo.getBorrowerId());
		 repayTrailVo.setName(person.getName());
		 repayTrailVo.setIdNum(person.getIdnum());
		 repayTrailVo.setLoanType(vLoanInfo.getLoanType());
		 repayTrailVo.setPactMoney(vLoanInfo.getPactMoney());
		 repayTrailVo.setContractNum(vLoanInfo.getContractNum());
		 List<LoanRepaymentDetail> repayList= afterLoanService.getAllInterestOrLoan(tradeDate, vLoanInfo.getId());
		 repayTrailVo.setAccAmount(afterLoanService.getAccAmount(vLoanInfo.getId()));
		 Calendar cal = Calendar.getInstance();
	      cal.setTime(tradeDate);
		 int promiseReturnDate=cal.get(Calendar.DAY_OF_MONTH);
		 if("一次性".equals(repayType)){
			repayTrailVo.setRequestState("已申请");
		}else{
			repayTrailVo.setRequestState("未申请");
		}
		/** 获取计算器实例 **/
		ICalculator calculatorInstace = CalculatorFactoryImpl.createCalculator(vLoanInfo);
		 //还款状态 
		 if(null!=repayList && repayList.size()>0 ){
			  //以下获取逾期还款信息
			   repayTrailVo.setOverDueTerm(afterLoanService.getOverdueTermCount(repayList,tradeDate));
			   repayTrailVo.setOverCorpus(afterLoanService.getOverdueCorpus(repayList, tradeDate));
			   repayTrailVo.setOverDueDate(repayTrailVo.getOverDueTerm()<1?null:repayList.get(0).getReturnDate());			   
	           repayTrailVo.setOverInterest(afterLoanService.getOverdueInterest(repayList,tradeDate));
	           //以下获取逾期罚息相关信息
	           repayTrailVo.setFineDay(afterLoanService.getOverdueDay(repayList,tradeDate));
	           repayTrailVo.setFine(afterLoanService.getFine(repayList, tradeDate));//afterLoanService.getFine(repayList, tradeDate));
	           repayTrailVo.setFineDate(repayTrailVo.getFineDay()<1?null:repayList.get(0).getPenaltyDate()); 
	           //以下获取当期的还款信息
	           repayTrailVo.setCurrDate(null==repayList.get(repayList.size()-1).getFactReturnDate()?null:repayList.get(repayList.size()-1).getFactReturnDate());
	           repayTrailVo.setCurrTerm(null==repayList.get(repayList.size()-1).getCurrentTerm()?0:repayList.get(repayList.size()-1).getCurrentTerm().intValue());
	        	if(promiseReturnDate==vLoanInfo.getPromiseReturnDate()){
	        			   repayTrailVo.setCurrCorpus(afterLoanService.getCurrCorpus(repayList,tradeDate)); 
	        	}else{
	        			   repayTrailVo.setCurrCorpus(new BigDecimal(0.0)); 
	        	}
	          
	        	repayTrailVo.setCurrInterest((promiseReturnDate==vLoanInfo.getPromiseReturnDate()||"一次性".equals(repayType))?afterLoanService.getCurrInterest(repayList,tradeDate):new BigDecimal(0.0) );

	        	repayTrailVo.setRemnant(afterLoanService.getReliefOfFine(tradeDate,vLoanInfo.getId()));

	        	/** 获取一次性结清金额 **/
	        	BigDecimal oneTimeRepayment = calculatorInstace.getOnetimeRepaymentAmount(loanId, tradeDate, repayList);
	        	repayTrailVo.setOneTimeRepayment(oneTimeRepayment);
	        	repayTrailVo.setResidualPactMoney(vLoanInfoService.findListByVO(vLoanInfo).get(0).getResidualPactMoney());  
	    

	           //获取应还利息、本金、违约金、退费
	        	repayTrailVo.setGiveBackRate(afterLoanService.getGiveBackRate(repayList));
	        	BigDecimal penalty = calculatorInstace.getPenalty(loanId, repayList, vLoanInfo);
	        	repayTrailVo.setPenalty(penalty);
	        	
	        	if("一次性".equals(repayType)){
	        		repayTrailVo.setCurrAmount(repayTrailVo.getOverInterest().add(repayTrailVo.getCurrInterest()).add(repayTrailVo.getResidualPactMoney()).add(repayTrailVo.getFine()).add(repayTrailVo.getPenalty()).subtract(repayTrailVo.getGiveBackRate()).subtract(repayTrailVo.getAccAmount())) ;
	        	}else{
	        		repayTrailVo.setCurrAmount(repayTrailVo.getOverInterest().add(repayTrailVo.getOverCorpus()).add(repayTrailVo.getCurrInterest()).add(repayTrailVo.getCurrCorpus()).add(repayTrailVo.getFine()).subtract(repayTrailVo.getAccAmount())) ;
	        	}
	        	if(repayTrailVo.getCurrAmount().compareTo(new BigDecimal(0))<0){	        		
	        		repayTrailVo.setCurrAmount(new BigDecimal(0.0)); 
	        	}
	        	 
	        	
		 }
		 repayTrailVo.setTradeDate(tradeDate);
		 return repayTrailVo;
		 
	 }
	
	
}
