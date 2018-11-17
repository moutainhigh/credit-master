package com.zdmoney.credit.core.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.common.constant.IsSucCoreEnum;
import com.zdmoney.credit.common.constant.OfferStateEnum;
import com.zdmoney.credit.common.constant.RepayTypeEnum;
import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.util.ValidateUtil;
import com.zdmoney.credit.common.util.coreUtil.Constants;
import com.zdmoney.credit.common.util.coreUtil.JSONUtil;
import com.zdmoney.credit.common.util.coreUtil.MessageUtil;
import com.zdmoney.credit.common.vo.core.CreditVo;
import com.zdmoney.credit.common.vo.core.OfferParamsVo;
import com.zdmoney.credit.common.vo.core.RepayTrailParamsVO;
import com.zdmoney.credit.core.service.pub.IOfferCoreService;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import com.zdmoney.credit.offer.service.pub.IOfferCreateService;
import com.zdmoney.credit.offer.service.pub.IOfferInfoService;
import com.zdmoney.credit.system.domain.ComEmployee;
import com.zdmoney.credit.system.domain.ComOrganization;
import com.zdmoney.credit.system.service.pub.IComEmployeeService;
import com.zdmoney.credit.system.service.pub.IComOrganizationService;

/**
 * 
 * @author 00235304
 * @since 2015-8-17
 */
@Controller
@RequestMapping("/core/offerCore")
public class OfferCoreController extends BaseController {
	private static final Logger logger = Logger.getLogger(OfferCoreController.class);

	@Autowired
	IOfferInfoService offerInfoService;
	
	@Autowired
	IComEmployeeService comEmployeeService;
	
	@Autowired
	IVLoanInfoService vLoanInfoService;
	
	@Autowired
	private IOfferCreateService offerCreateService;

	@Autowired
	@Qualifier("comOrganizationServiceImpl")
	IComOrganizationService comOrganizationServiceImpl;
	
	@Autowired
	IOfferCoreService offerCoreService;

	/**
	 * 回盘信息查询接口
	 * @param request 请求信息对象
	 * @param response 响应信息对象
	 * @param paramsVo 封装请求参数
	 * @throws IOException
	 */
	@RequestMapping("/queryOffer")
	@ResponseBody
	public void queryOffer(HttpServletRequest request,HttpServletResponse response,OfferParamsVo paramsVo) throws IOException {
		Map<String,Object> json=new HashMap<String,Object>();
		Map<String,Object> paramsMap=new HashMap<String,Object>();
		/*  参数校验 begin  */
		if(StringUtils.isBlank(paramsVo.getUserCode()) && paramsVo.getOfferDate()==null){
			json=MessageUtil.returnErrorMessage("失败：缺少必要参数！");
			response.getWriter().write(JSONUtil.toJSON(json));
			return;
		}
		String isSuc=paramsVo.getIsSuc();
		if(StringUtils.isNotBlank(isSuc) && !(IsSucCoreEnum.succ.name().equals(isSuc)|| IsSucCoreEnum.fail.name().equals(isSuc))){
			json=MessageUtil.returnErrorMessage("失败：isSuc参数值错误！");
			response.getWriter().write(JSONUtil.toJSON(json));
			return;
		}
		String offerState=paramsVo.getOfferState();
//		if(StringUtils.isNotBlank(offerState) && !(OfferStateEnum.未报盘.name().equals(offerState)|| OfferStateEnum.已报盘.name().equals(offerState) || OfferStateEnum.已回盘.name().equals(offerState))){
//			json=MessageUtil.returnErrorMessage("失败：offerState参数值错误！");
//			response.getWriter().write(JSONUtil.toJSON(json));
//			return;
//		}
		ComEmployee employee=comEmployeeService.findByUserCode(paramsVo.getUserCode());
		if(employee==null){
			json=MessageUtil.returnErrorMessage("userCode参数有误，无法找到该员工信息!");
			response.getWriter().write(JSONUtil.toJSON(json));
			return;
		}
		ComOrganization organization=comOrganizationServiceImpl.get(employee.getOrgId());
		/*  参数校验 end  */
		/*  查询参数准备 begin */
		Long max=paramsVo.getMax();
		Long offset=paramsVo.getOffset();
		paramsVo.setMax(Math.min((max!=null && max.intValue()>0 ? max:10),100));
		paramsVo.setOffset(Math.min((offset!=null && offset.intValue()>=0 ? offset:0),100));
		
		Pager pager = new Pager();
		pager.setRows(paramsVo.getMax().intValue());
		pager.setPage(paramsVo.getOffset().intValue());
		paramsMap.put("pager", pager);
		paramsMap.put("name",paramsVo.getName());
		paramsMap.put("idnum",paramsVo.getIdnum());
		paramsMap.put("loanId",paramsVo.getLoanId());
		paramsMap.put("offerDate",paramsVo.getOfferDate());
		paramsMap.put("fundsSources",paramsVo.getFundsSources());
		paramsMap.put("saleDeptId",organization.getOrgCode());
		if(StringUtils.isNoneBlank(offerState)){
			String tempState=Const.OFFER_STATE.get(offerState);
			tempState=tempState!=null? tempState:offerState;
			String offerStateSql="";
			if(OfferStateEnum.已回盘.name().equals(tempState)){
				offerStateSql="O.RETURN_CODE IS NOT NULL";
			}else{
				offerStateSql="O.STATE LIKE '%"+tempState+"%'";
			}
			paramsMap.put("offerStateSql",offerStateSql);
		}
		/*将这段SQL逻辑改写到XML文件中，尽量不在java代码中拼接SQL
		StringBuffer bufferStr=new StringBuffer();
		if(StringUtils.isNotBlank(isSuc) && IsSucCoreEnum.succ.name().equals(isSuc)){
			bufferStr.append(" ( O.RETURN_CODE='"+Const.RETURNOFFER_SUCCES+"' OR O.RETURN_CODE='"+Const.RETURNOFFER_SUCCES2+"' OR O.RETURN_CODE LIKE '%"+Const.RETURNOFFER_SUCCES_FI+"%')");
		}else if(StringUtils.isNotBlank(isSuc) && IsSucCoreEnum.fail.name().equals(isSuc)){
			bufferStr.append("  O.RETURN_CODE IS NOT NULL AND O.RETURN_CODE!='"+Const.RETURNOFFER_SUCCES+"' AND O.RETURN_CODE!='"+Const.RETURNOFFER_SUCCES2+"' AND O.RETURN_CODE NOT LIKE '%"+Const.RETURNOFFER_SUCCES_FI+"%'");
			bufferStr.append(" AND O.STATE='"+OfferStateEnum.已报盘.name()+"'");
		}
		paramsMap.put("isSucSql",bufferStr.toString());
		*/
		paramsMap.put("isSuc",isSuc);
		paramsMap.put("success",IsSucCoreEnum.succ.name());
		paramsMap.put("failure",IsSucCoreEnum.fail.name());
		paramsMap.put("success1",Const.RETURNOFFER_SUCCES);
		paramsMap.put("success2",Const.RETURNOFFER_SUCCES2);
		paramsMap.put("success3",Const.RETURNOFFER_SUCCES_FI);
		paramsMap.put("offerState",OfferStateEnum.已报盘.name());
		/*  查询参数准备 end */
		json=offerInfoService.queryOffer(paramsMap);
		response.getWriter().write(JSONUtil.toJSON(json));
		return;
	}
	
	
	/**
	 * 实时扣款更新接口
	 * @param request 请求信息对象
	 * @param response 响应信息对象
	 * @param paramsVo 封装请求参数
	 * @throws IOException
	 */
	@RequestMapping("/updateOffer")
	@ResponseBody
	public void updateOffer(HttpServletRequest request,HttpServletResponse response,OfferParamsVo paramsVo) throws IOException {
		Map<String,Object> json=new HashMap<String,Object>();
		
		String userCode=paramsVo.getUserCode();
		Long loanId=paramsVo.getLoanId();
		BigDecimal offerAmount=paramsVo.getOfferAmount();
		logger.info("实时扣款更新接口接收的入参：[ userCode="+userCode+", loanId="+loanId+", offerAmount="+offerAmount.doubleValue()+" ]");
		
		/* 接口入参校验 begin */
		if(StringUtils.isBlank(userCode) || loanId==null || offerAmount==null){
			json=MessageUtil.returnErrorMessage("参数缺失：userCode、loanId或offerAmount!");
			response.getWriter().write(JSONUtil.toJSON(json));
			return;
		}
		
		ComEmployee employee=comEmployeeService.findByUserCode(userCode);
		VLoanInfo loanInfo=vLoanInfoService.findByLoanId(loanId);
		if(employee==null || loanInfo==null){
			json=MessageUtil.returnErrorMessage("userCode或者loanId输入参有误，无法找到对应记录!");
			response.getWriter().write(JSONUtil.toJSON(json));
			return;
		}
		
		if((BigDecimal.ZERO.compareTo(offerAmount)>=0)){
			json=MessageUtil.returnErrorMessage("参数错误：offerAmount必须大于0!");
			response.getWriter().write(JSONUtil.toJSON(json));
			return;
		}
		/* 接口入参校验   end */
		json=offerInfoService.updateOfferInfo(paramsVo);
		response.getWriter().write(JSONUtil.toJSON(json));
		logger.info("实时扣款更新操作完成！");
		return;
	}
	
	
	/**
	 * 实时扣款推送接口
	 * @param request 请求信息对象
	 * @param response 响应信息对象
	 * @param paramsVo 封装请求参数
	 * @throws IOException
	 */
	@RequestMapping("/createOffer")
	@ResponseBody
	public void createOffer(HttpServletRequest request,HttpServletResponse response,OfferParamsVo paramsVo) throws IOException {
		Map<String,Object> json=new HashMap<String,Object>();
		String userCode=paramsVo.getUserCode();
		Long loanId=paramsVo.getLoanId();
		BigDecimal offerAmount=paramsVo.getOfferAmount();
		logger.info("实时扣款推送接口接收的入参：[ userCode="+userCode+", loanId="+loanId+", offerAmount="+(offerAmount!=null? offerAmount.doubleValue():null)+" ]");
		
		//userCode和loanId是必输的参数，进行校验
		if(StringUtils.isBlank(userCode) || loanId==null){
			json=MessageUtil.returnErrorMessage("参数缺失：userCode或loanId!");
			response.getWriter().write(JSONUtil.toJSON(json));
			return;
		}
		
		//userCode对应的操作员必须存在
		ComEmployee employee=comEmployeeService.findByUserCode(userCode);
		if(employee==null){
			json=MessageUtil.returnErrorMessage("userCode参数有误，无法找到该员工信息!");
			response.getWriter().write(JSONUtil.toJSON(json));
			return;
		}
		
		//offerAmount参数是非必输参数，如果有传入，则必须大于0
		if(offerAmount!=null && (BigDecimal.ZERO.compareTo(offerAmount)>=0)){
			json=MessageUtil.returnErrorMessage("参数错误：offerAmount必须大于0!");
			response.getWriter().write(JSONUtil.toJSON(json));
			return;
		}
		
		User user = new User();
		user.setId(employee.getId());
		user.setName(employee.getName());
		user.setUserCode(employee.getUsercode());
		UserContext.setUser(user);
		
		try {
			paramsVo.setShowPayChannel(true);
			json=offerCreateService.createRealtimeOfferInfo(paramsVo);
		} catch (Exception e) {
			json=MessageUtil.returnErrorMessage("实时扣款推送接口异常！"+e.getMessage());
		}
		response.getWriter().write(JSONUtil.toJSON(json));
		logger.info("实时扣款更新操作完成！");
		return;
	}
	
	
	/**
	 * 还款试算接口
	 * @param request 请求信息对象
	 * @param response 响应信息对象
	 * @param paramsVo 封装请求参数
	 * @throws IOException
	 */
	@RequestMapping("/queryTrail")
	@ResponseBody
	public void queryTrail(HttpServletRequest request,HttpServletResponse response,RepayTrailParamsVO paramsVo) throws IOException {
		logger.info("还款试算接口接收到的参数："+paramsVo.toString());

		Map<String,Object> json=new HashMap<String,Object>();
		Date repayDate=paramsVo.getRepayDate();
		String repayType=paramsVo.getRepayType();
		String userCode=paramsVo.getUserCode();
		String name=paramsVo.getName();
		String idnum=paramsVo.getIdnum();
		String mphone=paramsVo.getMphone();
		String[] loanIds=paramsVo.getLoanIds();
		//paramsVo.setMax(Math.min((paramsVo.getMax()!=null? paramsVo.getMax():10), 100));
		//paramsVo.setOffset(Math.min((paramsVo.getOffset()!=null? paramsVo.getOffset():0), 100));
		
		boolean flag=StringUtils.isBlank(name)&& StringUtils.isBlank(idnum) && StringUtils.isBlank(mphone) && loanIds==null; //姓名、身份证号、手机号码、债权编号必须有一个才有返回值
		if( flag || repayDate==null || StringUtils.isBlank(repayType) || StringUtils.isBlank(userCode)){
			json=MessageUtil.returnErrorMessage("失败：缺少必要参数！");
			response.getWriter().write(JSONUtil.toJSON(json));
			return;
		}
		
		if(!(RepayTypeEnum.onetime.name().equals(repayType) || RepayTypeEnum.normal.name().equals(repayType))){//repayType值校验
			json=MessageUtil.returnErrorMessage("失败：参数repayType值错误！");
			response.getWriter().write(JSONUtil.toJSON(json));
			return;
		}
		
		ComEmployee employee=comEmployeeService.findByUserCode(userCode);//userCode对应的操作员必须存在
		if(employee==null){
			json=MessageUtil.returnErrorMessage("userCode参数有误，无法找到该员工信息!");
			response.getWriter().write(JSONUtil.toJSON(json));
			return;
		}
		
		json=offerInfoService.queryTrail(paramsVo);
		response.getWriter().write(JSONUtil.toJSON(json));
	}
	
	
	/**
	 * 征信接口
	 * @param params
	 * @param request
	 * @param response
	 */
	@RequestMapping("/queryCredit")
	public void queryCredit(CreditVo params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("queryCredit接口 :接收到的参数:"+params.toString());
	    
	    Map<String, Object> json = null;
	    
	    if (Strings.isEmpty(params.getIdnum()) || Strings.isEmpty(params.getName()) || Strings.isEmpty(params.getUserCode()) || 
	    		Strings.isEmpty(params.getSearchSource()) || Strings.isEmpty(params.getSearchCode())) {
	    	logger.error("queryCredit:失败:缺少必要参数");
	    	json = MessageUtil.returnErrorMessage("失败:缺少必要参数");
	    	response.getWriter().write(JSONUtil.toJSON(json));
	    	return;
	    }
	    
	    if(!ValidateUtil.isIDcard(params.getIdnum())){
	    	json = MessageUtil.returnErrorMessage("失败:idnum入参有误");
	    	response.getWriter().write(JSONUtil.toJSON(json));
	    	return;
	    }
	    
	    if(!("01".equals(params.getSearchCode()) || "02".equals(params.getSearchCode()))) {
	    	json = MessageUtil.returnErrorMessage("失败:searchCode入参有误");
	    	response.getWriter().write(JSONUtil.toJSON(json));
	    	return;
	    }
	    
	    if(!"01".equals(params.getSearchSource())) {
	    	json = MessageUtil.returnErrorMessage("失败:searchSource入参有误");
	    	response.getWriter().write(JSONUtil.toJSON(json));
	    	return;
	    }
	    
	    try {
	    	json = offerCoreService.queryCredit(params);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("queryCredit方法：失败:"+e.getMessage());
			
			json = new HashMap<String, Object>();
			json.put("code",Constants.DATA_ERR_CODE);
			json.put("message","失败：未知错误");
		}
	    
	    response.getWriter().write(JSONUtil.toJSON(json)); 
	    return;
	}
}
