package com.zdmoney.credit.core.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zdmoney.credit.common.domain.LoanContractTrialRepVo;
import com.zdmoney.credit.common.domain.ResidualPactMoneyRepVo;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.util.Assert;
import com.zdmoney.credit.common.util.BeanUtils;
import com.zdmoney.credit.common.util.ExcelExportUtil;
import com.zdmoney.credit.common.util.MD5Util;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.util.ToolUtils;
import com.zdmoney.credit.common.util.coreUtil.Constants;
import com.zdmoney.credit.common.util.coreUtil.JSONUtil;
import com.zdmoney.credit.common.util.coreUtil.MessageUtil;
import com.zdmoney.credit.common.vo.ResponseInfo;
import com.zdmoney.credit.common.vo.core.ExternalDebtVO;
import com.zdmoney.credit.common.vo.core.LoanExportParamsVO;
import com.zdmoney.credit.common.vo.core.LoanTrialVo;
import com.zdmoney.credit.common.vo.core.LoanVo;
import com.zdmoney.credit.common.vo.core.PersonVo;
import com.zdmoney.credit.common.vo.core.UserVo;
import com.zdmoney.credit.core.EnumConvertor;
import com.zdmoney.credit.core.FieldNameMapper.FieldNameType;
import com.zdmoney.credit.core.service.pub.ILoanCoreService;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.loan.dao.pub.ILoanInitialInfoDao;
import com.zdmoney.credit.loan.dao.pub.ILoanProductDao;
import com.zdmoney.credit.loan.domain.LoanBase;
import com.zdmoney.credit.loan.domain.LoanInitialInfo;
import com.zdmoney.credit.loan.domain.LoanProduct;
import com.zdmoney.credit.loan.service.pub.ILoanBaseService;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import com.zdmoney.credit.operation.util.PublicAccountInfoUtil;
import com.zdmoney.credit.system.domain.ComEmployee;
import com.zdmoney.credit.system.domain.ProdCreditProductTerm;
import com.zdmoney.credit.system.service.pub.IComEmployeeService;
import com.zdmoney.credit.system.service.pub.IProdCreditProductTermService;

/**
 * 
 * 外包系统债权相关接口  核心接口
 * @author 00234770
 *
 */
@Controller
@RequestMapping("/core/loanCore")
public class LoanCoreController extends BaseController{

	private static final Logger logger = Logger.getLogger(LoanCoreController.class);
	
	@Autowired
	ILoanCoreService loanCoreService;
	
	@Autowired
	IVLoanInfoService vLoanInfoService;
	
	@Autowired
	IProdCreditProductTermService prodCreditProductTermService;
	
	@Autowired
	ILoanBaseService loanBaseService;
	
	@Autowired
	ILoanInitialInfoDao loanInitialInfoDao;
	
	@Autowired
	ILoanProductDao LoanProductDao;
	
	@Autowired
	IComEmployeeService comEmployeeService;
	
	/**
	 * 保存债权数据接口
	 * @param params
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/importSingleAPSLoan")
	public void importSingleAPSLoan(LoanVo params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("importSingleAPSLoan保存债权数据接口 :接收到的参数:"+params.toString());
		
		Map<String, Object> json = null;
		
		if (Strings.isEmpty(params.getUserCode()) || Strings.isEmpty(params.getJsonStr())) {
			logger.error("importSingleAPSLoan保存债权数据接口:失败:缺少必要参数userCode或jsonStr");
            json = MessageUtil.returnErrorMessage("失败:缺少必要参数userCode或jsonStr");
            response.getWriter().write(JSONUtil.toJSON(json));
            return;
		}
		
		
		
		try {
			
			Map<String,Object> sourceMap = JSON.parseObject(params.getJsonStr());

			//期数
			Long term=Long.parseLong(PropertyUtils.getProperty(sourceMap, "tmAppMain.accTerm").toString());
			
			//转换成中文(产品类型)
			String productCd=EnumConvertor.convertToText("tmAppMain.productCd", PropertyUtils.getProperty(sourceMap, "tmAppMain.productCd").toString(), FieldNameType.aps4json);
			
			//转换成中文(合同来源)
			String contractSource =EnumConvertor.convertToText("tmAppMain.contractSource", PropertyUtils.getProperty(sourceMap, "tmAppMain.contractSource").toString(), FieldNameType.aps4json);
			ProdCreditProductTerm prodCreditProductTerm = new ProdCreditProductTerm();
			prodCreditProductTerm =prodCreditProductTermService.findBymap(term,productCd,contractSource);
			
			if (Strings.isEmpty(prodCreditProductTerm) ) {
				logger.error("importSingleAPSLoan保存债权数据接口:失败:产品类型不存在");
	            json = MessageUtil.returnErrorMessage("失败:产品类型不存在");
	            response.getWriter().write(JSONUtil.toJSON(json));
	            return;
			}
			
			json = loanCoreService.importSingleAPSLoan(params);
		} catch (SQLException se) {
			se.printStackTrace();
			logger.error("importSingleAPSLoan方法：数据库操作失败:"+se);
			
			json = new HashMap<String, Object>();
			json.put("code",Constants.DATA_ERR_CODE);
			json.put("message","失败：未知错误");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("importSingleAPSLoan方法：失败:"+e);
			
			json = new HashMap<String, Object>();
			json.put("code",Constants.DATA_ERR_CODE);
			json.put("message","未知错误");
		}
		
        response.getWriter().write(JSONUtil.toJSON(json)); 
        return;
	}
	
	/**
	 * 根据appNo查询债券信息，生成还款计划
     * 并返回还款计划信息
	 * @param params
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/createAPSLoan")
	public void createAPSLoan(LoanVo params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("createAPSLoan生成合同接口 :接收到的参数:"+params.toString());
		
		Map<String, Object> json = null;
		
		if (Strings.isEmpty(params.getAppNo())) {
			logger.error("createAPSLoan生成合同接口:失败:缺少必要参数appNo");
            json = MessageUtil.returnErrorMessage("失败:缺少必要参数appNo");
            response.getWriter().write(JSONUtil.toJSON(json));
            return;
		}
		
		try {
			json = loanCoreService.createAPSLoan(params);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("createAPSLoan方法：失败:"+e);
			
			json = new HashMap<String, Object>();
			json.put("code",Constants.DATA_ERR_CODE);
			json.put("message","失败：生成合同失败:"+e.getMessage());
		}
		
        response.getWriter().write(JSONUtil.toJSON(json)); 
        return;
	}
	
	/**
	 * 根据idnum查询债券信息，查询债权状态
	 * @param params
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/paymentStatus")
	public void paymentStatus(PersonVo params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("paymentStatus还款状态查询接口 :接收到的参数:"+params.toString());
		
		Map<String, Object> json = null;
		
		if (Strings.isEmpty(params.getIdnum())) {
			logger.error("paymentStatus还款状态查询接口：失败： 身份证号不能为空");
            json = MessageUtil.returnErrorMessage("失败:缺少必要参数idnum");
            response.getWriter().write(JSONUtil.toJSON(json));
            return;
		}
		
		try {
			json = loanCoreService.paymentStatus(params);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("paymentStatus方法：失败:"+e);
			
			json = new HashMap<String, Object>();
			json.put("code",Constants.DATA_ERR_CODE);
			json.put("message","失败：未知错误");
		}
		
		response.getWriter().write(JSONUtil.toJSON(json)); 
        return;
	}
	
	/**
	 * 根据身份证号查询客户的所有债权信息
	 * @param params
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/loanStatus")
	public void loanStatus(PersonVo params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("loanStatus客户债权状态查询接口 :接收到的参数:"+params.toString());
		
		Map<String, Object> json = null;
		
		if (Strings.isEmpty(params.getIdnum())) {
			logger.error("loanStatus客户债权状态查询接口：失败： 身份证号不能为空");
            json = MessageUtil.returnErrorMessage("失败:缺少必要参数idnum");
            response.getWriter().write(JSONUtil.toJSON(json));
            return;
		}
		
		try {
			json = loanCoreService.loanStatus(params);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("paymentStatus方法：失败:"+e);
			
			json = new HashMap<String, Object>();
			json.put("code",Constants.DATA_ERR_CODE);
			json.put("message","失败：未知错误");
		}
		
		response.getWriter().write(JSONUtil.toJSON(json)); 
        return;
	}
	
	/**
	 * 根据申请件号查询债权状态
	 * @param params
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/queryLoanState")
	public void queryLoanState(LoanVo params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("queryLoanState查询债权状态接口 :接收到的参数:"+params.toString());
		
		Map<String, Object> json = null;
		
		if (Strings.isEmpty(params.getAppNos())) {
			logger.error("queryLoanState接口:失败:缺少必要参数appNos");
            json = MessageUtil.returnErrorMessage("失败:缺少必要参数appNos");
            response.getWriter().write(JSONUtil.toJSON(json));
            return;
		}
		
		try {
			json = loanCoreService.queryLoanState(params);
			System.out.println(JSONUtil.toJSON(json));
		} catch (Exception e) {
			logger.error("createAPSLoan方法：失败:"+e);
			
			json = new HashMap<String, Object>();
			json.put("code",Constants.DATA_ERR_CODE);
			json.put("message","失败：查询失败:"+e.getMessage());
		}
		
        response.getWriter().write(JSONUtil.toJSON(json)); 
        return;
	}
	
	
	/**
	 * 根据AppNos查询客户的债权状态
	 * @param params
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/neibupipei")
	public void neibupipei(LoanVo params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("neibupipei查询客户债权状态接口 :接收到的参数:"+params.toString());
		
		Map<String, Object> json = null;
		
		if (Strings.isEmpty(params.getAppNos())) {
			logger.error("neibupipei查询客户债权状态接口 :失败： AppNo不能为空");
            json = MessageUtil.returnErrorMessage("失败:缺少必要参数AppNos");
            response.getWriter().write(JSONUtil.toJSON(json));
            return;
		}
		
		try {
			json = loanCoreService.neibupipei(params);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("paymentStatus方法：失败:"+e);
			
			json = new HashMap<String, Object>();
			json.put("code",Constants.DATA_ERR_CODE);
			json.put("message","失败：未知错误");
		}
		
		response.getWriter().write(JSONUtil.toJSON(json)); 
        return;
	}
	
	/**
	 * 更新借款状态接口
     * "0010":合同确认
     * "0011":合同退回
     * "0020":财务审核通过
     * "0021":财务审核退回
     * "0022":财务放款退回
     * "0030":拒绝
     * "0031":取消
     * "0040":合同签订
     * 
	 * @param params
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/updateLoanState")
	public void updateLoanState(LoanVo params, HttpServletRequest request, HttpServletResponse response) throws Exception {
	    logger.info("updateLoanState接口 :接收到的参数:"+params.toString());
	    
	    Map<String, Object> json = null;
	    
	    if (Strings.isEmpty(params.getAppNo()) || Strings.isEmpty(params.getUserCode()) || Strings.isEmpty(params.getStateCode())) {
	    	logger.error("updateLoanState:失败:缺少必要参数appNo或userCode或stateCode");
	    	json = MessageUtil.returnErrorMessage("失败:缺少必要参数appNo或userCode或stateCode");
	    	response.getWriter().write(JSONUtil.toJSON(json));
	    	return;
	    }
	    try {
	    	json = loanCoreService.updateLoanState(params);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("updateLoanState方法：失败:"+e.getMessage());
			
			json = new HashMap<String, Object>();
			json.put("code",Constants.DATA_ERR_CODE);
			json.put("message","失败：更新借款状态失败");
		}
	    
	    response.getWriter().write(JSONUtil.toJSON(json)); 
	    return;
	}
	
	/**
	 * 批量更新借款状态接口
     * "0030":拒绝
     * "0031":取消
     * 
	 * @param params
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/batchUpdateLoanState")
	public void batchUpdateLoanState(LoanVo params, HttpServletRequest request, HttpServletResponse response) throws Exception {
	    logger.info("updateLoanState接口 :接收到的参数:"+params.toString());
	    
	    Map<String, Object> json = null;
	    
	    if (Strings.isEmpty(params.getAppNos()) || Strings.isEmpty(params.getStateCodes())) {
	    	logger.error("updateLoanState:失败:缺少必要参数appNos或stateCodes");
	    	json = MessageUtil.returnErrorMessage("失败:缺少必要参数appNos或stateCodes");
	    	response.getWriter().write(JSONUtil.toJSON(json));
	    	return;
	    }
	    try {
	    	json = loanCoreService.batchUpdateLoanState(params);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("updateLoanState方法：失败:"+e.getMessage());
			
			json = new HashMap<String, Object>();
			json.put("code",Constants.DATA_ERR_CODE);
			json.put("message","失败：更新借款状态失败");
		}
	    
	    response.getWriter().write(JSONUtil.toJSON(json)); 
	    return;
	}
	
	/**
	 * 贷前试算接口
	 * @param params
	 * @param request
	 * @param response
	 */
	@RequestMapping("/createLoanTrial")
	public void createLoanTrial(LoanTrialVo params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("createLoanTrial接口 :接收到的参数:"+params.toString());
	    
	    Map<String, Object> json = null;
	    
	    if (Strings.isEmpty(params.getName()) || Strings.isEmpty(params.getLoanType()) || Strings.isEmpty(params.getMoney())
	    		|| Strings.isEmpty(params.getTime()) || Strings.isEmpty(params.getFirstRepaymentDate())) {
	    	logger.error("createLoanTrial:失败:缺少必要参数name或loanType或money或time或firstRepaymentDate");
	    	json = MessageUtil.returnErrorMessage("失败:缺少必要参数name或loanType或money或time或firstRepaymentDate");
	    	response.getWriter().write(JSONUtil.toJSON(json));
	    	return;
	    }
	    
	    try {
	    	json = loanCoreService.createLoanTrial(params);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("createLoanTrial方法：失败:"+e.getMessage());
			
			json = new HashMap<String, Object>();
			json.put("code",Constants.DATA_ERR_CODE);
			json.put("message","失败：贷前试算失败");
		}
	    
	    response.getWriter().write(JSONUtil.toJSON(json)); 
	    return;
	}
	
	/**
	 * 查看借款接口
	 * @param params
	 * @param request
	 * @param response
	 */
	@RequestMapping("/queryLoan")
	public void queryLoan(LoanVo params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("queryLoan接口 :接收到的参数:"+params.toString());
	    
	    Map<String, Object> json = null;
	    
	    if ((Strings.isEmpty(params.getLoanId()) || Strings.isEmpty(params.getIdNum())) && Strings.isEmpty(params.getUserCode()) ) {
	    	logger.error("queryLoan:失败:缺少必要参数loanId或者userCode或者idNum");
	    	json = MessageUtil.returnErrorMessage("失败:缺少必要参数");
	    	response.getWriter().write(JSONUtil.toJSON(json));
	    	return;
	    }
	    try {
	    	json = loanCoreService.queryLoan(params);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("queryLoan方法：失败:"+e.getMessage());
			
			json = new HashMap<String, Object>();
			json.put("code",Constants.DATA_ERR_CODE);
			json.put("message","失败：未知错误");
		}
	    
	    response.getWriter().write(JSONUtil.toJSON(json)); 
	    return;
	}
	
	/**
	 * 积木盒子债权导出
	 * @param params
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/externalDebt")
	@ResponseBody
	public void externalDebt(ExternalDebtVO params, HttpServletRequest request, HttpServletResponse response) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 1);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        params.setMax(Long.valueOf(20000));
        Map<String,Object> paramsMap=new HashMap<String,Object>();
        paramsMap=BeanUtils.toMap(params);
        paramsMap.put("loanId", params.getLoanId());
        paramsMap.put("startQueryDate", params.getStartQueryDate());
        paramsMap.put("endQueryDate", params.getEndQueryDate());
        paramsMap.put("max", params.getMax());
        ResponseInfo responseInfo = null;
        try{
        	String fileName ="积木盒子债权信息表"+".xlsx";
        	List<Map<String, Object>> externalDebtList = vLoanInfoService.getExternalDebtList(paramsMap);
        	Assert.notCollectionsEmpty(externalDebtList,"当前导出条件查询不到数据");
        	String[] fields = new String[] {"project_no","loan_type","pact_money","grant_money","servicerate",
        		   							"loan_time","purpose","huankuanlaiyuan","sign_date","house_type","building_area",
        		   							"buy_time","houseprice","person_name", "sex","idnum","ed_level","address",
        		   							"hr_address","married","c_type","c_enter_time_year","official_rank","zonggongling",
        		   							"give_back_bank","kaihubankshengfen","gb_full_name","bank_full_name",
        		   							"enterprise_type","shareholding_ratio","premises_type","employee_amount",
        		   							"monthly_net_profit","six_month_shouru","finance_name","returneterm","lastreturneterm",
        		   							"promise_return_date","startrdate","endrdate"};

            String[] labels = new String[]  {"项目编号","产品类型","意向融资金额（万元）","融资人实收金额（万元）","证大服务费（元）",
            								"借款期限（月）","贷款用途","还款来源","签约时间","房产类型","建筑面积（平米）","购买日期（年）",
            								"房产总价（万元）","姓名","性别","身份证号","学历","通讯地址","户籍地址",
            								"婚姻状况","单位性质","本工作开始日期（年）","职位","总工龄（年）","开户银行代号","开户银行省份","开户银行地区","开户银行支行","经营主体类型",
            								"融资人持股比例","经营场所产权情况","员工数量（位）","经营实体账户月均流入金额（万元）","近6个月月均银行账户流入（万元）",
            								"融资用户名","月偿还本息数额","最后一期还本息数额","还款日","还款起日期","还款止日期"};
         // 工作表名称
            String sheetName = "Export";
            // 创建excel工作簿
            Workbook workbook = ExcelExportUtil.createExcelByMap(fileName, labels,fields, externalDebtList, sheetName);
            // 文件下载
            String downLoadError = this.downLoadExcelFile(request, response,fileName, workbook);
            if (Strings.isNotEmpty(downLoadError)) {
                throw new PlatformException(ResponseEnum.FULL_MSG, downLoadError);
            }
            return;
        } catch (PlatformException e) {
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),e.getMessage());
        } catch (Exception e) {
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
        } finally {
        }
        try {
            response.setContentType("text/html");
            response.getWriter().write(toResponseJSON(responseInfo));
        } catch (IOException e) {
        }
    }
	/**
	 * 爱特债权导出
	 * @param request 请求信息
	 * @param response 响应信息
	 */
	@RequestMapping("/eloanExport")
	@ResponseBody
	public void eloanExport(HttpServletRequest request, HttpServletResponse response, LoanExportParamsVO paramVo){
		ResponseInfo responseInfo = null;
		Map<String,Object> paramsMap=new HashMap<String,Object>();
		paramsMap=BeanUtils.toMap(paramVo);
		String lastReapyday1=DateFormatUtils.format(ToolUtils.getLastRepayDate(new Date()), "yyyy-MM-dd");
		String lastReapyday2=DateFormatUtils.format(ToolUtils.getLastRepayDate(ToolUtils.getLastRepayDate(new Date())),"yyyy-MM-dd");
		paramsMap.put("lastReapyday1", lastReapyday1);
		paramsMap.put("lastReapyday2", lastReapyday2);
		
		List<Map<String,Object>> queryResultList=vLoanInfoService.queryForLoanExport(paramsMap);
		Assert.notCollectionsEmpty(queryResultList,"当前导出条件查询不到数据！");
		String fileName="爱特债权导出表.xlsx";
		String sheetName = "爱特债权";
		String[] fields=new String[]{"id","contract_num","loan_type","aa","purpose","pact_money","approve_money","rate_sum","refer_rate","eval_rate","manage_rate","manager_rate_for_partyc","risk","time","rateEM","give_back_rate_for3term","give_back_rate_for4term","give_back_rate_after4term","overdue_penalty1day","overdue_penalty15day","startrdate","service_tel","address","postcode","signing_site","funds_sources","bb","rate","cc","dd","acct_name","borrower_sex","borrower_idnum","borrower_email","borrower_mphone","borrower_edLevel","signSalesDep_fullName","borrower_name","giveBackBank_bankName","giveBackBank_fullName","giveBackBank_account","accrualem"};
		String[] labels=new String[]{"借款ID","合同编号","产品类型","借款标题","借款用途","合同金额","审批金额","服务费","乙方咨询费","乙方评估费","乙方管理费","丙方管理费","风险金","借款期限","月利率","第三期退费","第四期退费","第四期之后退费","首次逾期1天罚息","首次逾期15天罚息","首还款日期","客服电话","现居住地址","邮政编码","合同签署地","合同来源","第三方债权id","月综合费率","是否加急","用户昵称","真实姓名","性别","身份证号","邮箱","手机号码","最高学历","放款营业部","开户人姓名","开户银行","银行分支机构","提现银行卡号","补偿利率"};
		
		try {
			Workbook workbook = ExcelExportUtil.createExcelByMap(fileName, labels,fields, queryResultList, sheetName);
			String downLoadError = this.downLoadExcelFile(request, response,fileName, workbook);
			if (StringUtils.isNotEmpty(downLoadError)) {
                throw new PlatformException(ResponseEnum.FULL_MSG, downLoadError);
            }
            return;
		} catch (PlatformException e) {
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),e.getMessage());
        } catch (Exception e) {
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
        } finally {
        }
		try {
            response.setContentType("text/html");
            response.getWriter().write(toResponseJSON(responseInfo));
        } catch (IOException e) {
        }
	}
	
	
	/**
	 * 爱特债权流水导出
	 * @param request 请求信息
	 * @param response 响应信息
	 */
	@RequestMapping("/eloanRepaymentExport")
	@ResponseBody
	public void eloanRepaymentExport(HttpServletRequest request, HttpServletResponse response, LoanExportParamsVO paramVo){
		ResponseInfo responseInfo = null;
		Map<String,Object> paramsMap=new HashMap<String,Object>();
		paramsMap.put("loanId", paramVo.getLoanId());
		
		List<Map<String,Object>> queryResultList=vLoanInfoService.queryForLoanRepayExp(paramsMap);
		Assert.notCollectionsEmpty(queryResultList,"当前导出条件查询不到数据！");
		String fileName="爱特债权还款流水导出.xlsx";
		String sheetName = "债权流水";
		String[] fields=new String[]{"id","current_term","return_date", "returneterm", "repayment_all"};
		String[] labels=new String[]{"借款ID号","期数","还款日","当期应还总额","当期一次性还款金额"};
		
		try {
			Workbook workbook = ExcelExportUtil.createExcelByMap(fileName, labels,fields, queryResultList, sheetName);
			String downLoadError = this.downLoadExcelFile(request, response,fileName, workbook);
			if (StringUtils.isNotEmpty(downLoadError)) {
                throw new PlatformException(ResponseEnum.FULL_MSG, downLoadError);
            }
            return;
		} catch (PlatformException e) {
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),e.getMessage());
        } catch (Exception e) {
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
        } finally {
        }
		try {
            response.setContentType("text/html");
            response.getWriter().write(toResponseJSON(responseInfo));
        } catch (IOException e) {
        }
		
	}
	
	
	
	
	/**
     * Excel文件下载
     * @param request
     * @param response
     * @param fileName
     * @param workbook
     * @return
     */
    private String downLoadExcelFile(HttpServletRequest request,HttpServletResponse response, String fileName, Workbook workbook) {
        OutputStream out = null;
         try {
             response.reset();
             response.setHeader("Content-Disposition", "attachment;filename="+PublicAccountInfoUtil.editFileName(request, fileName));
             response.setContentType(PublicAccountInfoUtil.CONTENT_TYPE_1);
             out = response.getOutputStream();
             workbook.write(out);
             out.flush();
         } catch (IOException e) {
             logger.error("下载文件失败：", e);
             return "下载文件失败";
         } finally {
             try {
                 if (null != out) {
                     out.close();
                 }
             } catch (IOException e) {
                 logger.error("下载文件失败：", e);
                 return "下载文件失败";
             }
         }
         return null;
    }
	/**
	 * 测试 createRepaymentDetailAfterGrantMoney 接口 无其他用处
	 * @param params
	 * @param request
	 * @param response
	 * @throws Exception
	 */
    @RequestMapping("/test")
	public void test(LoanVo params, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	LoanBase loanBase=loanBaseService.findLoanBase(params.getLoanId());
		LoanInitialInfo loanInitialInfo=loanInitialInfoDao.findByAppNo(params.getAppNo());
		LoanProduct loanProduct=LoanProductDao.findByLoanId(params.getLoanId());
		
		loanInitialInfo.setGrantMoneyDate(null);
		
		loanCoreService.createRepaymentDetailAfterGrantMoney(loanBase, loanInitialInfo, loanProduct);
    }
    
    /***
     * 同步政审修改员工密码
     * @param params
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/synApsPassword")
	public void synApsPassword(UserVo userVo, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	logger.info("synApsPassword同步政审密码接口 :接收到的参数:"+userVo.toString());
    	String password = userVo.getNewPassword();
    	String userCode = userVo.getUserCode();
    	String authKey = userVo.getAuthKey();
		Map<String, Object> json = null;
		
		if (Strings.isEmpty(userCode) || Strings.isEmpty(password) || Strings.isEmpty(authKey)) {
			logger.error("synApsPassword同步政审密码接口 :失败:缺少必要参数userCode或password或authKey");
            json = MessageUtil.returnErrorMessage("失败:缺少必要参数userCode或password或authKey");
            response.getWriter().write(JSONUtil.toJSON(json));
            return;
		}		
		
		try {		
			String authKeyCore = MD5Util.md5Hex("passwordKey")+MD5Util.md5Hex(userCode)+password;
			if(!authKeyCore.equals(authKey)){
				logger.error("synApsPassword同步政审密码接口 :失败:authKey不一致");
	            json = MessageUtil.returnErrorMessage("失败:authKey不一致");
	            response.getWriter().write(JSONUtil.toJSON(json));
	            return;
			}
			ComEmployee comEmployee = comEmployeeService.findByUserCode(userCode);
			if(null==comEmployee){
				logger.error("synApsPassword同步政审密码接口 :失败:没有该员工");
	            json = MessageUtil.returnErrorMessage("失败:没有该员工");
	            response.getWriter().write(JSONUtil.toJSON(json));
	            return;
			}
			comEmployee.setPassword(password);
			comEmployeeService.saveOrUpdate(comEmployee, true,false, false);
			json = new HashMap<String, Object>();
			json.put("code",Constants.SUCCESS_CODE);
			json.put("message","更新成功！");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("synApsPassword方法：失败:"+e);			
			json = new HashMap<String, Object>();
			json.put("code",Constants.DATA_ERR_CODE);
			json.put("message","未知错误");
		}
		
        response.getWriter().write(JSONUtil.toJSON(json)); 
        return;
    }
    
    /**
     * 新增或更新包商银行业务流水号 接口
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/saveOrUpdateBsyhBusNo")
    public void saveOrUpdateBsyhBusNo(HttpServletRequest request, HttpServletResponse response) throws Exception{
    	Map<String, Object> json = null;
    	String appNo = request.getParameter("appNo");
    	String busNumber = request.getParameter("busNumber");
    	logger.info("saveOrUpdateBsyhBusNo新增或更新包商银行业务流水号 :接收到的参数:appNo:"+appNo+";busNumber:"+busNumber);  	
    	
    	if (Strings.isEmpty(appNo)||Strings.isEmpty(busNumber)) {
			logger.error("saveOrUpdateBsyhBusNo新增或更新包商银行业务流水号：失败： appNo或busNumber为空");
            json = MessageUtil.returnErrorMessage("失败:缺少必要参数appNo或busNumber");
            response.getWriter().write(JSONUtil.toJSON(json));
            return;
		}
    	
		try {
			json = loanCoreService.saveOrUpdateBsyhBusNo(appNo,busNumber);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("saveOrUpdateBsyhBusNo方法：失败:"+e);
			json = new HashMap<String, Object>();
			json.put("code",Constants.DATA_ERR_CODE);
			json.put("message","失败：未知错误");
		}		
		response.getWriter().write(JSONUtil.toJSON(json)); 
        return;
    }

	/**
	 * 借款合同试算接口
	 * @param params
	 * @param request
	 * @param response
	 */
	@RequestMapping("/createLoanContractTrial")
	public void createLoanContractTrial(LoanContractTrialRepVo params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("createLoanContractTrial接口 :接收到的参数:" + JSONUtil.toJSON(params));

		Map<String, Object> json = null;

		if (Strings.isEmpty(params.getLoanType()) || Strings.isEmpty(params.getMoney())
				|| Strings.isEmpty(params.getTime()) || Strings.isEmpty(params.getFundsSources())) {
			logger.error("createLoanTrial:失败:缺少必要参数name或loanType或money或time或fundsSources");
			json = MessageUtil.returnErrorMessage("失败:缺少必要参数name或loanType或money或time或fundsSources");
			response.getWriter().write(JSONUtil.toJSON(json));
			return;
		}

		try {
			json = loanCoreService.createLoanContractTrial(params);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("createLoanContractTrial方法：失败:"+e.getMessage());

			json = new HashMap<String, Object>();
			json.put("code",Constants.DATA_ERR_CODE);
			json.put("message","失败：借款合同试算失败");
		}

		response.getWriter().write(JSONUtil.toJSON(json));
		return;
	}

	/**
	 * 获取剩余本金
	 * @param params
	 * @param request
	 * @param response
	 */
	@RequestMapping("/getResidualPactMoney")
	public void getResidualPactMoney(ResidualPactMoneyRepVo params, HttpServletRequest request, HttpServletResponse response)throws Exception{
		logger.info("getResidualPactMoney接口 :接收到的参数::"+ JSONUtil.toJSON(params));
		Map<String, Object> json = null;
		if (Strings.isEmpty(params.getIdNum()) || Strings.isEmpty(params.getName())) {
			logger.error("getResidualPactMoney:失败:缺少必要参数name或idNum");
			json = MessageUtil.returnErrorMessage("getResidualPactMoney:失败:缺少必要参数name或idNum\"");
			response.getWriter().write(JSONUtil.toJSON(json));
			return;
		}
		try{
			json = loanCoreService.getResidualPactMoney(params);
		}catch (Exception e){
			e.printStackTrace();
			logger.error("getResidualPactMoney：失败:"+e.getMessage());

			json = new HashMap<String, Object>();
			json.put("code",Constants.DATA_ERR_CODE);
			json.put("message", "失败：获取剩余本金失败");
		}
		response.getWriter().write(JSONUtil.toJSON(json));
		return;
	}
	
	 /**
     * 查询借款详细信息接口
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/findLoanDetails")
    public void findLoanDetails(HttpServletRequest request, HttpServletResponse response) throws Exception{
    	Map<String, Object> json = null;
    	String appNo = request.getParameter("appNo");
    	String loanNo = request.getParameter("loanNo");    	
    	logger.info("findLoanDetails查询借款详细信息接口 :接收到的参数:appNo:"+appNo+";loanNo:"+loanNo);
    	
    	if (Strings.isEmpty(appNo)&&Strings.isEmpty(loanNo)) {
			logger.error("findLoanDetails查询借款详细信息接口：失败： appNo或loanNo为空");
            json = MessageUtil.returnErrorMessage("失败:缺少必要参数appNo或loanNo");
            response.getWriter().write(JSONUtil.toJSON(json));
            return;
		}
    	if(Strings.isEmpty(appNo)){
    		appNo=loanNo;
    	}
		try {
			json = loanCoreService.findLoanDetails(appNo);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("findLoanDetails方法：失败:"+e);
			json = new HashMap<String, Object>();
			json.put("code",Constants.DATA_ERR_CODE);
			json.put("message","失败：未知错误");
		}		
		response.getWriter().write(JSONUtil.toJSON(json)); 
        return;
    }
    
    /**
	 * 根据申请件号查询债权状态
	 * @param params
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/queryLoanStateNew")
	public void queryLoanStateNew(LoanVo params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("queryLoanStateNew查询债权状态接口 :接收到的参数:"+params.toString());
		Map<String, Object> json = null;
		if (Strings.isEmpty(params.getAppNos())) {
			logger.error("queryLoanStateNew接口:失败:缺少必要参数appNos");
            json = MessageUtil.returnErrorMessage("失败:缺少必要参数appNos");
            response.getWriter().write(JSONUtil.toJSON(json));
            return;
		}
		try {
			json = loanCoreService.queryLoanStateNew(params);
			logger.info("queryLoanStateNew查询债权状态接口-返回的参数信息："+JSONUtil.toJSON(json));
		} catch (Exception e) {
			logger.error("queryLoanStateNew方法：失败:"+e);
			json = new HashMap<String, Object>();
			json.put("code",Constants.DATA_ERR_CODE);
			json.put("message","失败：查询债权状态失败:"+e.getMessage());
		}
        response.getWriter().write(JSONUtil.toJSON(json)); 
        return;
	}
}
