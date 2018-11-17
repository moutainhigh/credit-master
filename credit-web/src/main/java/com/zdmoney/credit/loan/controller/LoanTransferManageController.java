package com.zdmoney.credit.loan.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.zdmoney.credit.common.constant.SysActionLogTypeEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.util.Assert;
import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.ExcelExportUtil;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.util.coreUtil.Constants;
import com.zdmoney.credit.common.util.coreUtil.JSONUtil;
import com.zdmoney.credit.common.util.coreUtil.MessageUtil;
import com.zdmoney.credit.common.util.excel.ExcelTemplet;
import com.zdmoney.credit.common.util.excel.ExcelUtil;
import com.zdmoney.credit.common.util.file.UploadFileUtil;
import com.zdmoney.credit.common.util.file.vo.UploadFile;
import com.zdmoney.credit.common.vo.ResponseInfo;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.loan.service.pub.ILoanEvaluateInfoService;
import com.zdmoney.credit.loan.service.pub.ILoanTransferInfoService;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;


/**
 * @author YM10112 2017年10月17日 下午2:00:40
 */
 
@Controller
@RequestMapping(value="/loanTransfer")
public class LoanTransferManageController extends BaseController {
	private static final String CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	private static final String LOAN_TRANSFER = "transfer";
	private static final String LOAN_EVALUATE = "evaluate";
	@Autowired
	IVLoanInfoService vLoanInfoService;
	@Autowired
	ILoanTransferInfoService loanTransferInfoService;
	@Autowired
	ILoanEvaluateInfoService loanEvaluateInfoService;
	/**
     * 跳转到债权转让管理页面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/loanTransferManagePage")
    public String loanTransferManagePage(HttpServletRequest request, HttpServletResponse response) {
		return "/loanTransfer/loanTransferManagePage";
    }
    
    /**
     * 债权转让导入
     * @param file
     * @param request
     * @param response
     * @throws IOException
     */
    //@ResponseBody
	@RequestMapping("/loanTransferImport")
	public void loanTransferImport(@RequestParam(value="uploadFile",required = false) MultipartFile file,  
			HttpServletRequest request, HttpServletResponse response) throws IOException {
    	createLog(request, SysActionLogTypeEnum.导入, "债权转让管理", "导入债权转让信息");
        ResponseInfo responseInfo = null;
        Workbook workbook = null;
        OutputStream outputStream = null;
        try {
            // 文件校验
            UploadFile uploadFile = new UploadFile();
            uploadFile.setFile(file);
            uploadFile.setFileType(UploadFileUtil.FILE_TYPE_EXCEL);
            uploadFile.setFileMaxSize(1024 * 1024 * 50);
            UploadFileUtil.valid(uploadFile);
            String fileName = file.getOriginalFilename();
            String contentType = file.getContentType();
            workbook = WorkbookFactory.create(file.getInputStream());
            ExcelTemplet excelTemplet = new ExcelTemplet().new LoanTransferExcel();
            List<Map<String, String>> result = ExcelUtil.getExcelData(workbook, excelTemplet);
            Assert.notCollectionsEmpty(result, ResponseEnum.FILE_EMPTY_FILE, "");
            for(Map<String, String> map : result){
            	bulidLoanTransferEvalManageInfo(map,LOAN_TRANSFER);
            }
            ExcelUtil.addResultToWorkBook(workbook, result, excelTemplet);
            fileName = "债权转让导入结果" + Dates.getDateTime(Dates.DEFAULT_DATE_FORMAT) + ".xls";/** 下载文件名 **/
            String enableFileName = new String(fileName.getBytes("GBK"), "ISO-8859-1");
            response.setHeader("Content-Disposition", "attachment; filename=" + enableFileName);
            response.setHeader("Content-Type", contentType);
            outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            return;
        } catch (PlatformException ex) {
            logger.error(ex, ex);
            responseInfo = ex.toResponseInfo();
        } catch (Exception ex) {
            logger.error(ex, ex);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
        }
        response.setContentType("text/html");
        response.getWriter().print(toResponseJSON(responseInfo));
    }
    
    
    /**
     * 导出债权转让文件
     * @param request
     * @param response
     * @throws IOException 
     */
    @RequestMapping("/loanTransferExport")
    public void loanTransferExport(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	createLog(request, SysActionLogTypeEnum.导出, "债权转让管理", "导出债权转让信息");
        ResponseInfo responseInfo = null;
		Workbook workbook = null;
		OutputStream out = null;
        try {
        	String contractNum = request.getParameter("contractNum");
        	String transferBatch = request.getParameter("transferBatch");
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("contractNum",contractNum);
            params.put("transferBatch",transferBatch);
            List<Map<String, Object>> loanTransferInfoList = loanTransferInfoService.findLoanTransferInfoList(params);
            if(CollectionUtils.isEmpty(loanTransferInfoList)){
            	logger.info("根据查询条件查出来的数据为空,合同编号："+contractNum+",批次号："+transferBatch);
                throw new PlatformException(ResponseEnum.FULL_MSG, "根据查询条件查出来的数据为空！");
            }
 			List<String> labels = this.getLabels();
 			List<String> fields = this.getFields();
 			String fileName = "债权转让" + Dates.getDateTime(new Date(), "yyyyMMdd") + ".xls";
 			String sheetName = "Export";
 			workbook = ExcelExportUtil.createExcelByMap(fileName, labels, fields, loanTransferInfoList, sheetName);
 			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
 			response.setContentType(CONTENT_TYPE);
 			out = response.getOutputStream();
 			workbook.write(out);
 			out.flush();
 			return;
        }  catch (PlatformException e) {
			logger.error("导出债权转让信息异常：", e);
			responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("导出债权转让信息异常：", e);
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
				logger.error("导出债权转让信息异常：", e);
			}
		}
		response.setContentType("text/html");
		response.getWriter().write(toResponseJSON(responseInfo));
    }
    
    
    
    /**
	 * 获取唯一的批次号信息
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/getLoanTransferBatchList")
	@ResponseBody
	public String getLoanTransferBatchList(HttpServletRequest request, HttpServletResponse response) throws IOException {
		List<Map<String, Object>> transferBatchList = loanTransferInfoService.findLoanTransferBatchList();
		List<Map> resultList = new ArrayList<Map>();
		for (int i=0;i<transferBatchList.size();i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", transferBatchList.get(i).get("batchs"));
			map.put("text", transferBatchList.get(i).get("batchs"));
			resultList.add(map);
		}
		return JSONObject.toJSONString(resultList);
	}
    
	/**
     * 跳转到债权转让管理页面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/loanEvaluateManagePage")
    public String loanEvaluateManagePage(HttpServletRequest request, HttpServletResponse response) {
		return "/loanTransfer/loanEvaluateManagePage";
    }
	
    
    /**
     * 债权评估导入
     * @param file
     * @param request
     * @param response
     * @throws IOException
     */
    @ResponseBody
	@RequestMapping("/loanEvaluateImport")
	public void loanEvaluateImport(@RequestParam(value="uploadFileEval",required = false) MultipartFile file,  
			HttpServletRequest request, HttpServletResponse response) throws IOException {
    	createLog(request, SysActionLogTypeEnum.导入, "债权转让管理", "导入债权评估信息");
        ResponseInfo responseInfo = null;
        Workbook workbook = null;
        OutputStream outputStream = null;
        try {
            // 文件校验
            UploadFile uploadFile = new UploadFile();
            uploadFile.setFile(file);
            uploadFile.setFileType(UploadFileUtil.FILE_TYPE_EXCEL);
            uploadFile.setFileMaxSize(1024 * 1024 * 50);
            UploadFileUtil.valid(uploadFile);
            String fileName = file.getOriginalFilename();
            String contentType = file.getContentType();
            workbook = WorkbookFactory.create(file.getInputStream());
            ExcelTemplet excelTemplet = new ExcelTemplet().new LoanEvaluateExcel();
            List<Map<String, String>> result = ExcelUtil.getExcelData(workbook, excelTemplet);
            Assert.notCollectionsEmpty(result, ResponseEnum.FILE_EMPTY_FILE, "");
            for(Map<String, String> map : result){
            	bulidLoanTransferEvalManageInfo(map,LOAN_EVALUATE);
            }
            ExcelUtil.addResultToWorkBook(workbook, result, excelTemplet);
            fileName = "债权评估导入结果" + Dates.getDateTime(Dates.DEFAULT_DATE_FORMAT) + ".xls";
            String enableFileName = new String(fileName.getBytes("GBK"), "ISO-8859-1");
            response.setHeader("Content-Disposition", "attachment; filename=" + enableFileName);
            response.setHeader("Content-Type", contentType);
            outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            return;
        } catch (PlatformException ex) {
            logger.error(ex, ex);
            responseInfo = ex.toResponseInfo();
        } catch (Exception ex) {
            logger.error(ex, ex);
            responseInfo = new ResponseInfo(ResponseEnum.FULL_MSG);
        }
        response.setContentType("text/html");
        response.getWriter().print(toResponseJSON(responseInfo));
    }
    
    
    
    /**
     * 导出债权评估文件
     * @param request
     * @param response
     * @throws IOException 
     */
    @ResponseBody
    @RequestMapping("/loanEvaluateExport")
    public void loanEvaluateExport(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	createLog(request, SysActionLogTypeEnum.导出, "债权转让管理", "导出债权评估信息");
        ResponseInfo responseInfo = null;
		Workbook workbook = null;
		OutputStream out = null;
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            List<Map<String, Object>> loanEvaluateInfoList = loanEvaluateInfoService.findLoanEvaluateInfoList(params);
            if(CollectionUtils.isEmpty(loanEvaluateInfoList)){
                throw new PlatformException(ResponseEnum.FULL_MSG, "债权评估数据为空！");
            }
 			List<String> labels = this.getEvalLabels();
 			List<String> fields = this.getEvalFields();
 			String fileName = "债权评估" + Dates.getDateTime(new Date(), "yyyyMMdd") + ".xls";
 			String sheetName = "Export";
 			workbook = ExcelExportUtil.createExcelByMap(fileName, labels, fields, loanEvaluateInfoList, sheetName);
 			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
 			response.setContentType(CONTENT_TYPE);
 			out = response.getOutputStream();
 			workbook.write(out);
 			out.flush();
 			return;
        }  catch (PlatformException e) {
			logger.error("导出债权评估信息异常：", e);
			responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(), e.getMessage());
		} catch (Exception e) {
			logger.error("导出债权评估信息异常：", e);
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
				logger.error("导出债权评估信息异常：", e);
			}
		}
		response.setContentType("text/html");
		response.getWriter().write(toResponseJSON(responseInfo));
    }
    
    
    /**
     * 保存债权转让和评估的数据
     * @param map
     */
    private void bulidLoanTransferEvalManageInfo(Map<String, String> map,String type) {
    	String feedBackMsg = "";
    	String str = "";
		try{
			if(LOAN_TRANSFER.equals(type)){
				loanTransferInfoService.bulidLoanTransferInfo(map);
				str = "债权转让导入";
				feedBackMsg = "债权转让导入成功";
			}else if(LOAN_EVALUATE.equals(type)){
				loanEvaluateInfoService.bulidLoanEvaluateInfo(map);
				str = "债权评估导入";
				feedBackMsg = "债权评估导入成功";
			}
		}catch (PlatformException ex) {
            logger.error(ex, ex);
            feedBackMsg = ex.getMessage();
        } catch (Exception ex) {
            logger.error(ex, ex);
            feedBackMsg = "系统忙："+str+"出错，请重新执行导入操作";
        }
        map.put(ExcelTemplet.FEED_BACK_MSG, feedBackMsg);	
	}
    
    
    /**
     * 更新债权评估标记，供催收系统调用
     * @param loanId
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/updateLoanEvaluateSign")
	public void updateLoanEvaluateSign(String loanId, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	logger.info("updateLoanEvaluateSign更新债权评估标记接口 :接收到的参数:"+loanId);
    	Map<String, Object> json = new HashMap<String, Object>();
		if (Strings.isEmpty(loanId)) {
			logger.error("updateLoanEvaluateSign更新债权评估标记接口:失败:缺少必要参数loanId");
            json = MessageUtil.returnErrorMessage("失败:缺少必要的参数loanId！");
            response.getWriter().write(JSONUtil.toJSON(json));
            return;
		}
		try {
			json = loanEvaluateInfoService.updateLoanEvaluateSign(loanId);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("updateLoanEvaluateSign方法：失败:" + e);
			json.put("code", Constants.DATA_ERR_CODE);
			json.put("message", "失败：更新债权评估标记失败:" + e.getMessage());
		}
		response.getWriter().write(JSONUtil.toJSON(json)); 
        return;
    }
    
    /**
	 * excel文件的表头显示名
	 * @return
	 */
	private List<String> getLabels() {
		List<String> labels = new ArrayList<String>();
		labels.add("管理营业部");
		labels.add("贷款种类");
		labels.add("客户姓名");
		labels.add("身份证号码");
		labels.add("签约日期");
		labels.add("还款日");
		labels.add("合同金额");
		labels.add("逾期起始日");
		labels.add("逾期期数");
		labels.add("逾期天数");
		labels.add("剩余本金");
		labels.add("逾期本金(A1)");
		labels.add("逾期利息(A2)");
		labels.add("罚息起算日");
		labels.add("罚息金额(B)");
		labels.add("应还总金额(A1+A2+B)");
		labels.add("最后一期应还款日期");
		labels.add("合同来源");
		labels.add("债权去向");
		labels.add("合同编号");
		labels.add("转让批次");
		return labels;
	}
	
	/* excel文件的数据字段名
	 * @return
	 */
	private List<String> getFields() {
		List<String> fields = new ArrayList<String>();
		fields.add("MANAGE_DEPARTMENT");
		fields.add("LOAN_TYPE");
		fields.add("CUSTOMER_NAME");
		fields.add("ID_NUM");
		fields.add("SIGN_DATE");
		fields.add("PROMISE_RETURN_DATE");
		fields.add("PACT_MONEY");
		fields.add("OVERDUE_START_DATE");
		fields.add("OVERDUE_TERM");
		fields.add("OVERDUE_DAY");
		fields.add("SURPLUS_CAPITAL");
		fields.add("OVERDUE_CAPITAL");
		fields.add("OVERDUE_AINT");
		fields.add("FINE_START_DATE");
		fields.add("FINE_AMOUNT");
		fields.add("RETURN_TOTAL_AMOUNT");
		fields.add("LAST_RETURN_DATE");
		fields.add("FUNDS_SOURCES");
		fields.add("LOAN_BELONG");
		fields.add("CONTRACT_NUM");
		fields.add("TRANSFER_BATCH");
		return fields;
	}
	
	/**
	 * excel文件的表头显示名
	 * @return
	 */
	private List<String> getEvalLabels() {
		List<String> labels = new ArrayList<String>();
		labels.add("合同编号");
		labels.add("姓名");
		labels.add("身份证号");
		labels.add("是否在催");
		labels.add("退案日期");
		labels.add("是否评估");
		return labels;
	}
	
	/**
	 * excel文件的数据字段名
	 * @return
	 */
	private List<String> getEvalFields() {
		List<String> fields = new ArrayList<String>();
		fields.add("CONTRACT_NUM");
		fields.add("NAME");
		fields.add("ID_NUM");
		fields.add("IS_CS");//是否在催
		fields.add("AUTO_CLOSE_DATE");//退案日期 精确到日即可
		fields.add("IS_EVAL");//是否评估
		return fields;
	}
}
