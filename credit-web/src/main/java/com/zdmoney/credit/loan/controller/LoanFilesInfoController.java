package com.zdmoney.credit.loan.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zdmoney.credit.common.constant.ComOrganizationEnum;
import com.zdmoney.credit.common.constant.SysActionLogTypeEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.util.Assert;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.ExcelExportUtil;
import com.zdmoney.credit.common.util.MapUtil;
import com.zdmoney.credit.common.util.ValidatorErrorUtil;
import com.zdmoney.credit.common.vo.AttachmentResponseInfo;
import com.zdmoney.credit.common.vo.ResponseInfo;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.loan.dao.pub.ILoanFilesInfoDao;
import com.zdmoney.credit.loan.service.pub.ILoanFilesInfoService;
import com.zdmoney.credit.loan.validator.LoanFilesInfoValidator;
import com.zdmoney.credit.loan.vo.VLoanCollectionInfoList;
import com.zdmoney.credit.loan.vo.VLoanFilesInfo;
import com.zdmoney.credit.loan.vo.VLoanFilesInfoBase;
import com.zdmoney.credit.loan.vo.VLoanFilesInfoList;
import com.zdmoney.credit.loan.vo.VLoanFilesInfoUpdate;
import com.zdmoney.credit.repay.service.pub.ISalesDeptRepayInfoService;

/**
 * 前端请求处理（客户档案管理）
 * @author 00236633
 * @since 2015-7-29
 */
@Controller
@RequestMapping("/loan")
public class LoanFilesInfoController extends BaseController {
	
	protected static Log logger = LogFactory.getLog(LoanFilesInfoController.class);
	
    @Autowired
    private ISalesDeptRepayInfoService salesDeptRepayInfoService;
    
	@Autowired 
	@Qualifier("loanFilesInfoService")
	ILoanFilesInfoService loanFilesInfoService;
	
	@Autowired 
	@Qualifier("loanFilesInfoDao")
	private ILoanFilesInfoDao loanFilesInfoDao;
	/**
	 * 添加验证器
	 * @author 00236633
	 * @param binder
	 */
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
    	binder.addValidators(new LoanFilesInfoValidator());
    }
	
	/**
	 * 加载客户档案列表页面
	 * @author 00236633
	 * @return
	 */
	@RequestMapping(value = "/loanFilesInfoListPage", method = RequestMethod.GET)
	public void listPage(Model model,HttpServletRequest request) {
    	this.createLog(request, SysActionLogTypeEnum.查询, "客户档案管理", "加载客户档案列表页面");
		Map<String,Object> serviceResult = loanFilesInfoService.listPage();
		
		Map<String,Object> response = new HashMap<String,Object>();
		response.put("businessEmployeeList", serviceResult.get("businessEmployeeList"));
		response.put("resCode",ResponseEnum.SYS_SUCCESS.getCode());
		response.put("resMsg",ResponseEnum.SYS_SUCCESS.getDesc());
		
		model.addAllAttributes(response);
	}
	
	/**
	 * 查询客户档案列表
	 * @author 00236633
	 * @return
	 */
	@RequestMapping(value = "/loanFilesInfoList", method = RequestMethod.POST)
	@ResponseBody
	public String list(@Valid VLoanFilesInfoList vLoanFilesInfoList, BindingResult error,HttpServletRequest request) {
    	this.createLog(request, SysActionLogTypeEnum.查询, "客户档案管理", "查询客户档案列表");
    	AttachmentResponseInfo<Map<String,Object>> response = new AttachmentResponseInfo<Map<String,Object>>();
		Map<String,Object> data = new HashMap<String,Object>();
		response.setAttachment(data);
		
		try {
			if(!error.hasErrors()){
				Map<String,Object> serviceResult = loanFilesInfoService.list(vLoanFilesInfoList);
				data.put("rows", serviceResult.get("loanFilesInfoList"));
				data.put("total", serviceResult.get("loanFilesInfoCount"));
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
			logger.error("path:/loan/loanFilesInfoList",e);
			response.setResCode(ResponseEnum.SYS_FAILD.getCode());
			response.setResMsg("查询失败");
		}
		
		return toResponseJSON(response);
	}
	
	/**
	 * 加载添加借款客户档案页面
	 * @param vLoanFilesInfoBase
	 * @param error
	 * @return
	 */
	@RequestMapping(value = "/loanFilesInfoAddPage", method = RequestMethod.GET)
	public void addPage(@Valid VLoanFilesInfoBase vLoanFilesInfoBase, BindingResult error,Model model,HttpServletRequest request) {
    	this.createLog(request, SysActionLogTypeEnum.查询, "客户档案管理", "加载添加借款客户档案页面");
    	Map<String,Object> response = new HashMap<String,Object>();
		
		if(!error.hasErrors()){
			Map<String,Object> serviceResult = loanFilesInfoService.addPage(vLoanFilesInfoBase);
			response.put("loanBaseInfo", serviceResult.get("loanBaseInfo"));
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
				response.put("resMsg","打开添加客户档案信息页面参数不合法");
			}
		}
		
		model.addAllAttributes(response);
	}
	
	/**
	 * 添加借款客户档案
	 * @author 00236633
	 * @param vLoanFilesInfo
	 * @param error
	 * @return
	 */
	@RequestMapping(value = "/loanFilesInfoAdd", method = RequestMethod.POST)
	@ResponseBody
	public String add(@Valid VLoanFilesInfo vLoanFilesInfo, BindingResult error,HttpServletRequest request) {
    	this.createLog(request, SysActionLogTypeEnum.查询, "客户档案管理", "添加借款客户档案");
    	AttachmentResponseInfo<Map<String,Object>> response = new AttachmentResponseInfo<Map<String,Object>>();
		Map<String,Object> data = new HashMap<String,Object>();
		response.setAttachment(data);

		try {
			if(!error.hasErrors()){
				Map<String,Object> serviceResult = loanFilesInfoService.add(vLoanFilesInfo);
				if((Boolean)serviceResult.get("success")){
					response.setResCode(ResponseEnum.SYS_SUCCESS.getCode());
					response.setResMsg(ResponseEnum.SYS_SUCCESS.getDesc());
				}else{
					response.setResCode(ResponseEnum.SYS_FAILD.getCode());
					response.setResMsg(serviceResult.get("message")+"");
				}
			}else{
				response.setResCode(ResponseEnum.SYS_FAILD.getCode());
				String errorInfo = ValidatorErrorUtil.getErrorInfoString(error.getAllErrors());
				if(errorInfo.length()>0){
					response.setResMsg(errorInfo);
				}else{
					response.setResMsg("添加客户档案信息参数不合法");
				}
			}
		} catch (Exception e) {
			logger.error("path:/loan/loanFilesInfoAdd",e);
			response.setResCode(ResponseEnum.SYS_FAILD.getCode());
			response.setResMsg("添加客户档案信息失败");
		}
		
		return toResponseJSON(response);
	}
	
	/**
	 * 加载更新客户档案页面
	 * @author 00236633
	 * @param vLoanFilesInfoBase
	 * @param error
	 * @return
	 */
	@RequestMapping(value = "/loanFilesInfoUpdatePage", method = RequestMethod.GET)
	public void updatePage(@Valid VLoanFilesInfoBase vLoanFilesInfoBase, BindingResult error,Model model,HttpServletRequest request) {
    	this.createLog(request, SysActionLogTypeEnum.查询, "客户档案管理", "加载更新客户档案页面");
    	Map<String,Object> response = new HashMap<String,Object>();
		
		if(!error.hasErrors()){
			Map<String,Object> serviceResult = loanFilesInfoService.updatePage(vLoanFilesInfoBase);
			response.put("loanBaseInfo", serviceResult.get("loanBaseInfo"));
			response.put("loanFilesInfo", serviceResult.get("loanFilesInfo"));
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
				response.put("resMsg","打开更新客户档案信息页面参数不合法");
			}
		}
		
		model.addAllAttributes(response);
	}
	
	/**
	 * 更新借款客户档案
	 * @param vLoanFilesInfoUpdate
	 * @param error
	 * @return
	 */
	@RequestMapping(value = "/loanFilesInfoUpdate", method = RequestMethod.POST)
	@ResponseBody
	public String update(@Valid VLoanFilesInfoUpdate vLoanFilesInfoUpdate, BindingResult error,HttpServletRequest request) {
    	this.createLog(request, SysActionLogTypeEnum.查询, "客户档案管理", "更新借款客户档案");
    	AttachmentResponseInfo<Map<String,Object>> response = new AttachmentResponseInfo<Map<String,Object>>();
		Map<String,Object> data = new HashMap<String,Object>();
		response.setAttachment(data);
		
		try {
			if(!error.hasErrors()){
				Map<String,Object> serviceResult = loanFilesInfoService.update(vLoanFilesInfoUpdate);
				if((Boolean)serviceResult.get("success")){
					response.setResCode(ResponseEnum.SYS_SUCCESS.getCode());
					response.setResMsg(ResponseEnum.SYS_SUCCESS.getDesc());
				}else{
					response.setResCode(ResponseEnum.SYS_FAILD.getCode());
					response.setResMsg(serviceResult.get("message")+"");
				}
			}else{
				response.setResCode(ResponseEnum.SYS_FAILD.getCode());
				String errorInfo = ValidatorErrorUtil.getErrorInfoString(error.getAllErrors());
				if(errorInfo.length()>0){
					response.setResMsg(errorInfo);
				}else{
					response.setResMsg("更新客户档案信息参数不合法");
				}
			}
		} catch (Exception e) {
			logger.error("path:/loan/loanFilesInfoUpdate",e);
			response.setResCode(ResponseEnum.SYS_FAILD.getCode());
			response.setResMsg("更新客户档案信息失败");
		}
		
		return toResponseJSON(response);
	}
	
	/**
	 * 查询单个借款的客户档案
	 * @author 00236633
	 * @param vLoanFilesInfoBase
	 * @param error
	 * @return
	 */
	@RequestMapping(value = "/loanFilesInfoDetailPage", method = RequestMethod.GET)
	public void detailPage(@Valid VLoanFilesInfoBase vLoanFilesInfoBase, BindingResult error,Model model,HttpServletRequest request) {
    	this.createLog(request, SysActionLogTypeEnum.查询, "客户档案管理", "查询单个借款的客户档案页面处理");
    	Map<String,Object> response = new HashMap<String,Object>();
		
		if(!error.hasErrors()){
			response.put("loanId", vLoanFilesInfoBase.getLoanId());
			response.put("resCode",ResponseEnum.SYS_SUCCESS.getCode());
			response.put("resMsg",ResponseEnum.SYS_SUCCESS.getDesc());
		}else{
			response.put("resCode",ResponseEnum.SYS_FAILD.getCode());
			String errorInfo = ValidatorErrorUtil.getErrorInfoString(error.getAllErrors());
			if(errorInfo.length()>0){
				response.put("resMsg",errorInfo);
			}else{
				response.put("resMsg","打开查看客户档案信息页面参数不合法");
			}
		}
		
		model.addAllAttributes(response);
	}
	
	/**
	 * 查询单个借款的客户档案
	 * @author 00236633
	 * @param vLoanFilesInfoBase
	 * @param error
	 * @return
	 */
	@RequestMapping(value = "/loanFilesInfoDetail", method = RequestMethod.GET)
	public void detail(VLoanFilesInfoBase vLoanFilesInfoBase, BindingResult error,Model model,HttpServletRequest request) {
    	this.createLog(request, SysActionLogTypeEnum.查询, "客户档案管理", "查询单个借款的客户档案");
    	Map<String,Object> response = new HashMap<String,Object>();
		
		if(!error.hasErrors()){
			Map<String,Object> serviceResult = loanFilesInfoService.detail(vLoanFilesInfoBase);
			response.put("loanBaseInfo", serviceResult.get("loanBaseInfo"));
			response.put("loanFilesInfo", serviceResult.get("loanFilesInfo"));
			response.put("resCode",ResponseEnum.SYS_SUCCESS.getCode());
			response.put("resMsg",ResponseEnum.SYS_SUCCESS.getDesc());
		}else{
			response.put("resCode",ResponseEnum.SYS_FAILD.getCode());
			String errorInfo = ValidatorErrorUtil.getErrorInfoString(error.getAllErrors());
			if(errorInfo.length()>0){
				response.put("resMsg",errorInfo);
			}else{
				response.put("resMsg","打开查看客户档案信息页面参数不合法");
			}
		}
		model.addAllAttributes(response);
	}
	

	/**
	 * 张倩倩
	 * 借款查询---客户经理下拉列表
	 */
	@RequestMapping(value = "findKhjl")
	@ResponseBody
	public String findKhjl(HttpServletRequest request, HttpServletResponse response) {
		AttachmentResponseInfo<List> attachmentResponseInfo = null;
		
		try {
			Map<String,Object> serviceResult = loanFilesInfoService.listPage();
			List list=(List) serviceResult.get("businessEmployeeList");
			attachmentResponseInfo = new AttachmentResponseInfo<List>(
					ResponseEnum.SYS_SUCCESS.getCode(),
					ResponseEnum.SYS_SUCCESS.getDesc(), "");
			attachmentResponseInfo.setAttachment(list);
		} catch (Exception ex) {
			// 系统忙
			ex.printStackTrace();
			logger.error(ex);
			attachmentResponseInfo = new AttachmentResponseInfo<List>(
					ResponseEnum.SYS_FAILD.getCode(),
					ResponseEnum.SYS_FAILD.getDesc(), "");
		}
		return toResponseJSON(attachmentResponseInfo);
		
	}

	/**
	 * 加载档案催收管理页面
	 * @author 00236633
	 * @return
	 */
	@RequestMapping(value = "/loanCollectionManagePage", method = RequestMethod.GET)
	public void loanCollectionManagePage(Model model,HttpServletRequest request) {
    	this.createLog(request, SysActionLogTypeEnum.查询, "档案催收管理", "加载档案催收管理页面");
        // 获取当前登录用户的信息
        User userInfo = UserContext.getUser();
        String empNum = userInfo.getOrgCode();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("empNum", empNum);
        params.put("vLevel", ComOrganizationEnum.Level.V104.name());
        // 查询当前用户所属机构下的所有营业网点信息
        List<Map<String, Object>> signDeptInfoList = salesDeptRepayInfoService.getSalesDeptInfo(params);
		Map<String,Object> response = new HashMap<String,Object>();
		String operateTypes[] = new String[]{"已提交","未提交"};
		response.put("signDeptInfoList", signDeptInfoList);
		response.put("operateTypes", operateTypes);
		response.put("resCode",ResponseEnum.SYS_SUCCESS.getCode());
		response.put("resMsg",ResponseEnum.SYS_SUCCESS.getDesc());
        String currentDate = Dates.getDateTime("yyyy-MM-dd");
        response.put("currentDate", currentDate);
        String monthStartDate = currentDate.substring(0, 7)+"-01";
        response.put("monthStartDate", monthStartDate);
		model.addAllAttributes(response);
	}
	
	
	/**
	 * 查询档案催收管理列表
	 * @author 00236633
	 * @return
	 */
	@RequestMapping(value = "/loanCollectionManageList", method = RequestMethod.POST)
	@ResponseBody
	public String loanCollectionManageList(@Valid VLoanCollectionInfoList vLoanCollectionInfoList,BindingResult error,HttpServletRequest request) {
    	this.createLog(request, SysActionLogTypeEnum.查询, "档案催收管理", "查询档案催收管理");
    	AttachmentResponseInfo<Map<String,Object>> response = new AttachmentResponseInfo<Map<String,Object>>();
		Map<String,Object> data = new HashMap<String,Object>();
		response.setAttachment(data);
		try {
			if(!error.hasErrors()){
				if(vLoanCollectionInfoList.getSignEndDate() != null && vLoanCollectionInfoList.getSignBeginDate() != null){
					if(vLoanCollectionInfoList.getSignBeginDate().after(vLoanCollectionInfoList.getSignEndDate())){
						response.setResMsg("查询起始日期不得大于终止日期！");
						response.setResCode(ResponseEnum.SYS_FAILD.getCode());
						return toResponseJSON(response);
					}
				}
				Map<String,Object> serviceResult = loanFilesInfoService.getLoanCollectionManageList(vLoanCollectionInfoList);
				data.put("rows", serviceResult.get("loanCollectionManageList"));
				data.put("total", serviceResult.get("loanCollectionManageCount"));
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
			logger.error("path:/loan/loanCollectionManageList",e);
			response.setResCode(ResponseEnum.SYS_FAILD.getCode());
			response.setResMsg("查询失败");
		}
		
		return toResponseJSON(response);
	}
	
	@RequestMapping(value = "/updateCollectionByStatus", method = RequestMethod.GET)
	@ResponseBody
	public String updateCollectionByStatus(@RequestParam("ids") String[] ids, @RequestParam("status") String status,  HttpServletRequest request){
    	this.createLog(request, SysActionLogTypeEnum.更新, "档案催收管理", "更新档案信息");
    	AttachmentResponseInfo<Map<String,Object>> response = new AttachmentResponseInfo<Map<String,Object>>();
        if(null == ids || ids.length == 0){
			response.setResCode(ResponseEnum.SYS_FAILD.getCode());
			response.setResMsg("无档案编号，参数非法");	
            return toResponseJSON(response);
        }
        User userInfo = UserContext.getUser();
		Map<String,Object> data = new HashMap<String,Object>();
		response.setAttachment(data);
		try{
	        String operateType = "";
	        if(status != null && "y".equals(status)){
	        	operateType = "已提交";
	        }else{
	        	operateType = "未提交";
	        }
	        List<Long> idList = new ArrayList<Long>();
	        for(String id : ids){
	        	idList.add(Long.parseLong(id));
	        }
	        Map<String,Object> params = new HashMap<String, Object>();
	        params.put("ids", idList);
	        params.put("operateType", operateType);
	        params.put("updator", userInfo.getName());
	        params.put("operateTime",Dates.getDateTime("yyyy-MM-dd HH:mm:ss"));
	        loanFilesInfoService.checkCollectionExist(idList);
	        loanFilesInfoService.updateCollectionByIds(params);
	        response.setResCode(ResponseEnum.SYS_SUCCESS.getCode());
			response.setResMsg(ResponseEnum.SYS_SUCCESS.getDesc());
		}catch(Exception e){
			logger.error("path:/loan/updateCollectionByStatus",e);
			response.setResCode(ResponseEnum.SYS_FAILD.getCode());
			response.setResMsg("查询失败");			
		}
		return toResponseJSON(response);
	}
	
	 /**
     * excel文件导出
     * @param loanContractInfo
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/exportLoanCollectionRecord")
    @ResponseBody
    public void exportLoanCollectionRecord(@Valid VLoanCollectionInfoList vLoanCollectionInfoList,BindingResult error, HttpServletRequest request,HttpServletResponse response) {
        ResponseInfo responseInfo = null;   
        createLog(request, SysActionLogTypeEnum.导出, "档案催收管理", "导出档案催收管理");
        Workbook workbook = null;
        OutputStream out = null;
        try {
    		Map<String,Object> params = new HashMap<String,Object>(); 		
    		MapUtil.vObjectConvertToMap(vLoanCollectionInfoList, params, false);
    		if(vLoanCollectionInfoList.getSignEndDate() != null && vLoanCollectionInfoList.getSignBeginDate() != null){
				if(vLoanCollectionInfoList.getSignBeginDate().after(vLoanCollectionInfoList.getSignEndDate())){
					throw new PlatformException("查询起始日期不得大于终止日期！");
				}
			}
    		params.put("startRow", "1");
    		params.put("endRow", "2000");
        	List<Map<String,Object>> loanCollectionManageList = loanFilesInfoDao.findLoanCollectionManageListByMap(params);
            // 当前导出条件查询不到数据
            Assert.notCollectionsEmpty(loanCollectionManageList, "当前导出条件查询不到数据");
            String[] labels = new String[]{"姓名",  "身份证", "签约日期", "合同编号", "营业部", "提交状态"} ;
            String[] fields = new String[] {"borrowName","borrowIdNum","signDate","contractNum","signDeptName","operateType"};
            // 文件名
            String fileName = "客服催收管理"+ new SimpleDateFormat("yy-MM-dd").format(new Date()) + ".xls";
            // 工作表名称
            String sheetName = "Export";
            // 创建excel工作簿对象
            workbook = ExcelExportUtil.createExcelByMap(fileName, labels,fields, loanCollectionManageList, sheetName);
            // 文件下载
            //response.reset();
            response.setHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(fileName, "UTF-8"));
            out = response.getOutputStream();
            workbook.write(out);
            out.flush();
            return;
        } catch (PlatformException e) {
            logger.error("导出excel文件失败：", e);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),e.getMessage());
        } catch (Exception e) {
            logger.error("导出excel文件失败：", e);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
        } finally {
            try {
                if (null != out) {
                    out.close();
                }
                if (null != workbook) {
                    workbook.close();
                }
            } catch (IOException e) {
                responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
                logger.error("导出excel文件失败：", e);
            }
        }
        try {
            response.setContentType("text/html");
            response.getWriter().write(toResponseJSON(responseInfo));
        } catch (IOException e) {
        }
    }
}
