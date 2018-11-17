package com.zdmoney.credit.repay.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.zdmoney.credit.common.constant.FundsSourcesTypeEnum;
import com.zdmoney.credit.common.constant.SysActionLogTypeEnum;
import com.zdmoney.credit.common.constant.repay.ReqManagerFileTypeEnum;
import com.zdmoney.credit.common.constant.repay.ReqManagerOperateTypeEnum;
import com.zdmoney.credit.common.constant.repay.RequestManagementConst;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.util.Assert;
import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.FTPUtil;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.util.excel.ExcelTemplet;
import com.zdmoney.credit.common.util.excel.ExcelUtil;
import com.zdmoney.credit.common.util.file.JschSftpUtils;
import com.zdmoney.credit.common.util.file.UploadFileUtil;
import com.zdmoney.credit.common.util.file.vo.UploadFile;
import com.zdmoney.credit.common.vo.ResponseInfo;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.loan.service.ConnectBhxtFtpServiceImpl;
import com.zdmoney.credit.loan.service.pub.IConnectBhxtFtpService;
import com.zdmoney.credit.repay.domain.LoanPreApplyRecord;
import com.zdmoney.credit.repay.service.pub.ILoanPreApplyRecordService;
import com.zdmoney.credit.repay.service.pub.IRequestManagementService;
import com.zdmoney.credit.repay.service.pub.IYubokuanService;

@Controller
@RequestMapping("/repay")
public class YubokuanController extends BaseController {
    
    /** 划拨申请书导出（PDF）权限 **/
    private static final String EXPORT_APPLY_PDF_URL = "/requestManagement/exportApplyPdf";
    
    /** 划拨申请书导出（Xls）权限 **/
    private static final String EXPORT_APPLY_XLS_URL = "/requestManagement/exportApplyXls";
    
    /** 放款申请明细导出（Xls）权限 **/
    private static final String EXPORT_LOAN_APPLY_XLS_URL = "/requestManagement/exportLoanApplyXls";
    
    /** 放款申请明细导出（Txt）权限 **/
    private static final String EXPORT_LOAN_APPLY_TXT_URL = "/requestManagement/exportLoanApplyTxt";
    
    /** 还款计划导出（Xls）权限 **/
    private static final String EXPORT_PAY_PLAN_XLS_URL = "/requestManagement/exportPayPlanXls";
    
    /** 划拨申请书导入权限 **/
    private static final String IMPORT_APPLY_PDF_URL = "/requestManagement/importApplyPdfAndXls";
    
    @Autowired
    private IConnectBhxtFtpService connectBhxtFtpService;   
    @Autowired
    private IYubokuanService yubokuanService;
    @Autowired
    private ILoanPreApplyRecordService loanPreApplyRecordService;
    
    /** 预拨申请书模板 **/
    @Value("${Pre.Apply.Pdf.Template.Path}")
    private String applyPdfFilePath;
    @Value("${Pre.Apply.Excel.Template.Path}")
    private String applyExcelFilePath;
    
    /** 预拨确认书模板 **/
    @Value("${Pre.Confirm.Pdf.Template.Path}")
    private String confirmPdfFilePath;
    @Value("${Pre.Confirm.Excel.Template.Path}")
    private String confirmExcelFilePath;
    /**
     * 初始化预拨模式放款页面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/yubokuanDetail")
    public String jumpPage(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        createLog(request, SysActionLogTypeEnum.查询, "预拨申请书管理", "加载预拨申请书管理页面");
        // 合同来源
        model.addAttribute("fundsSources", this.getOrganizationList());
        return "/repay/yubokuan";
    }
    
    /**
     * 查询申请书管理信息
     * @param rows
     * @param page
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/search")
    @ResponseBody
    public String search(int rows,int page, HttpServletRequest request, HttpServletResponse response) {
        createLog(request, SysActionLogTypeEnum.查询, "申请书管理", "查询申请书管理信息");
        Map<String, Object> params =new HashMap<String, Object>();        
        // 定义分页实例
        Pager pager = new Pager();
        pager.setRows(rows);
        pager.setPage(page);
        pager.setSort("DESC");
        pager.setSidx("CREATE_TIME");
        params.put("pager", pager);
        // 调用Service层查询数据信息 
        pager = yubokuanService.queryPageList(params);
        // 将数据对象转换成JSON字符串，返回给前台
        return toPGJSONWithCode(pager);
    }
    
    /**
     * 理财机构下拉框展示
     * @return
     */
    private List<Map<String,String>> getOrganizationList(){
        List<Map<String,String>> resultList = new ArrayList<Map<String,String>>();
        Map<String,String> mapbh2 = new HashMap<String,String>();
        mapbh2.put("code", "BH2");
        mapbh2.put("value", FundsSourcesTypeEnum.渤海2.getValue());
        Map<String,String> maphrbh = new HashMap<>();
        maphrbh.put("code","HRBH");
        maphrbh.put("value",FundsSourcesTypeEnum.华瑞渤海.getValue());
        resultList.add(maphrbh);
        resultList.add(mapbh2);
        return resultList;
    }
    
    /**
     * 生成划拨申请书
     * @param request
     * @param response
     */
    @RequestMapping("/createApplication")
    public void createApplication(HttpServletRequest request, HttpServletResponse response) {
    	 createLog(request, SysActionLogTypeEnum.新增, "预拨申请书管理", "生成划拨申请书");
    	 response.setContentType("text/html");
    	 ResponseInfo responseInfo = null;
    	 ByteArrayOutputStream bos=null;//申请书pdf流
    	 ByteArrayOutputStream out=null;//申请书pdf签章流
    	 Workbook applyExcelWorkbook=null;
    	 ByteArrayOutputStream applyExcelOut=null;//申请书excel 流   	 
    	 
    	 ByteArrayOutputStream lastConfirmPdfBoss=null;//确认书pdf流
		 Workbook lastConfirmXlsWb=null;
		 ByteArrayOutputStream lastConfirmXlsOut =null;//确认书xls流
		 Workbook lastGrantDetailWb=null;
		 ByteArrayOutputStream lastGrantDetailOut=null;//放款明细xls流
		 Workbook lastPayPlanWb=null;//还款计划xls流
		 ByteArrayOutputStream lastPayPlanlOut=null;//还款计划xls流
         Map<String, Object> params =new HashMap<String, Object>();        
         // 理财机构码
         String organization = request.getParameter("organization");
         if(Strings.isNotEmpty(organization)){
             params.put("organization", organization);
         }
         // 预拨资金
         String yuboAmt = request.getParameter("yuboAmt");
         if(Strings.isNotEmpty(yuboAmt)){
             params.put("yuboAmt", yuboAmt);
         }
         //同一天一个来源职能预拨一笔       
         LoanPreApplyRecord lpar=loanPreApplyRecordService.findByMap(params);
         if(lpar!=null){
	    	 try {
	             responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),"每天只能申请一次划拨申请书！");
	             response.getWriter().write(toResponseJSON(responseInfo));
	             return;
	         } catch (IOException e) {
	         }
         }
         //连接存放盖章文件的ftp服务器
         FTPUtil ftpUtil = connectBhxtFtpService.getFtpEsignatureConnect();
         if(ftpUtil == null){
	    	 try {
	             responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),"连接存放盖章文件的服务器失败！");
	             response.getWriter().write(toResponseJSON(responseInfo));
	             return;
	         } catch (IOException e) {
	         }
         }
		 try {
			//查询最近一次的预拨申请记录
			LoanPreApplyRecord loanPreApplyRecord=yubokuanService.findLastRecord(params);
			String lastConfirmPdfFileName=null;
			String lastConfirmXlsFileName=null;
			String lastGrantDetailFileName=null;
			String lastPayPlanFileName=null;
			
        	if(null!=loanPreApplyRecord){
        		String projectCode = yubokuanService.getProjectCode(organization);
        		lastConfirmPdfFileName=projectCode+"_apply_"+Dates.getDateTime(loanPreApplyRecord.getApplyDate(), "yyyyMMdd")+"_001_confirm_001_.pdf";
        		lastConfirmXlsFileName=projectCode+"_apply_"+Dates.getDateTime(loanPreApplyRecord.getApplyDate(), "yyyyMMdd")+"_001_confirm_001_.xls";
        		lastGrantDetailFileName=projectCode+"_loandetail_"+Dates.getDateTime(loanPreApplyRecord.getApplyDate(), "yyyyMMdd")+"_001.xls";
        		lastPayPlanFileName=projectCode+"_payplan_"+Dates.getDateTime(loanPreApplyRecord.getApplyDate(), "yyyyMMdd")+"_001.xls";
				//生成最近一次的预拨确认书文件（PDF）及预拨确认书文件（XLS）
        		loanPreApplyRecord.setUpdateTime(Dates.getNow());//当前时间设置成更新时间
        		lastConfirmPdfBoss = yubokuanService.getConfirmPdfOutput(loanPreApplyRecord, confirmPdfFilePath);
				lastConfirmXlsWb = yubokuanService.getConfirmWorkbook(loanPreApplyRecord,confirmExcelFilePath);//获取划拨确认书excel，并填充数据
				//生成最近一次预拨申请记录中放款成功的放款明细文件（XLS）
				lastGrantDetailWb = yubokuanService.createGrantXls(loanPreApplyRecord,lastGrantDetailFileName);
				//生成最近一次还款计划文件（XLS）
				lastPayPlanWb = yubokuanService.createPayPlanXls(loanPreApplyRecord,lastPayPlanFileName);
				//上传预拨确认书文件（PDF）及预拨确认书文件（XLS）和放款明细文件（XLS）和还款计划文件（XLS）到渤海信托的FTP服务器				
				//更新最近一次的预拨申请记录 
				loanPreApplyRecord.setStatus2("2");
				loanPreApplyRecord.setStatus3("2");
				loanPreApplyRecord.setEndDate(Dates.getNow());
				loanPreApplyRecordService.updateByVo(loanPreApplyRecord);
	        }
                
	        //获取划拨申请书PDF，并填充数据
        	bos = yubokuanService.getApplyPdfOutput(params, applyPdfFilePath);
	        params.put("applyDate", String.valueOf(Dates.getCurrentYear())+"年"+String.valueOf(Dates.getCurrentMonth())+"月"+String.valueOf(Dates.getCurrentDay())+"日");
	        applyExcelWorkbook = yubokuanService.getApplyWorkbook(params,applyExcelFilePath);//获取划拨申请书excel，并填充数据
	        //上传申请书PDF进行签章         
	        String projectCode = yubokuanService.getProjectCode(organization);
	        String pdfFileName=projectCode+"_apply_"+Dates.getDateTime("yyyyMMdd")+"_001.pdf";
	        String excelFileName=projectCode+"_apply_"+Dates.getDateTime("yyyyMMdd")+"_001.xls";
        	String esignatureFtpFilePathName = yubokuanService.isExistEsignatureFtpFile(ConnectBhxtFtpServiceImpl.esignatureFtpDownloadPath+"/"+projectCode,pdfFileName,ftpUtil);
        	if(Strings.isEmpty(esignatureFtpFilePathName)){   
        		String httpFilePath=yubokuanService.uploadEsignatureFile(new ByteArrayInputStream(bos.toByteArray()),ConnectBhxtFtpServiceImpl.esignatureFtpUploadPath, pdfFileName, projectCode, ftpUtil);
        		if(Strings.isEmpty(httpFilePath)){
        			responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),"上传签章文件到服务器失败！");
   	             	response.getWriter().write(toResponseJSON(responseInfo));
   	             	return;
        		}
        		String httpUrl =yubokuanService.dealEsignature(httpFilePath, pdfFileName);
        		if (Strings.isEmpty(httpUrl)) {
        			responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),"签章失败！");
   	             	response.getWriter().write(toResponseJSON(responseInfo));
   	             	return;
        		}
        		out = yubokuanService.downloadEsignatureFileByHttpUrl(httpUrl);	        	
        	}else{
        		out=yubokuanService.downloadEsignatureFileByFilePath(esignatureFtpFilePathName,ftpUtil);
        	}
	    	yubokuanService.uploadEsignatureFile(new ByteArrayInputStream(out.toByteArray()),ConnectBhxtFtpServiceImpl.esignatureFtpDownloadPath,pdfFileName,projectCode,ftpUtil);
	    	//pdf签章文件以及EXCEL格式的文件上传至渤海2的FTP服务器
	    	JschSftpUtils jschSftpUtils = connectBhxtFtpService.getFtpBhxtConnectJsch();
	    	if(!jschSftpUtils.login()){
	    		responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),"上传失败！服务器连接不可连接！");
             	response.getWriter().write(toResponseJSON(responseInfo));
             	return;
	    	}	        
	        Map<String,InputStream> inputStreamMap=new HashMap<>();
	        inputStreamMap.put(pdfFileName, new ByteArrayInputStream(out.toByteArray()));//pdf签章文件
	        applyExcelOut = new ByteArrayOutputStream();
	        applyExcelWorkbook.write(applyExcelOut);
	        inputStreamMap.put(excelFileName,new ByteArrayInputStream(applyExcelOut.toByteArray()));//EXCEL格式的文件
	        if(lastConfirmPdfFileName!=null){//确认书pdf
	        	inputStreamMap.put(lastConfirmPdfFileName,new ByteArrayInputStream(lastConfirmPdfBoss.toByteArray()));
	        }
	        if(lastConfirmXlsFileName!=null){//确认书xls
	        	lastConfirmXlsOut = new ByteArrayOutputStream();
	        	lastConfirmXlsWb.write(lastConfirmXlsOut);
	        	inputStreamMap.put(lastConfirmXlsFileName,new ByteArrayInputStream(lastConfirmXlsOut.toByteArray()));
	        }
	        if(lastGrantDetailFileName!=null){//放款明细
	        	lastGrantDetailOut = new ByteArrayOutputStream();
	        	lastGrantDetailWb.write(lastGrantDetailOut);
	        	inputStreamMap.put(lastGrantDetailFileName,new ByteArrayInputStream(lastGrantDetailOut.toByteArray()));
	        }
	        if(lastPayPlanFileName!=null){//还款计划xls
	        	lastPayPlanlOut = new ByteArrayOutputStream();
	        	lastPayPlanWb.write(lastPayPlanlOut);
	        	inputStreamMap.put(lastPayPlanFileName,new ByteArrayInputStream(lastPayPlanlOut.toByteArray()));
	        }
	        yubokuanService.uploadFtpBHXT(jschSftpUtils, inputStreamMap, projectCode);
	    	//插入预拨申请记录表
	        yubokuanService.createYubokuanRecord(organization, Dates.getNow(), yuboAmt, 1L,ConnectBhxtFtpServiceImpl.esignatureFtpDownloadPath+"/"+projectCode+"/"+pdfFileName);
	        responseInfo = new ResponseInfo(ResponseEnum.SYS_SUCCESS.getCode(),"操作成功！");
        } catch (PlatformException e) {
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),e.getMessage());
        } catch (Exception e) {
            logger.error("生成划拨申请书异常：", e);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
        } finally {
            try {               
                if(null != bos){
                	bos.close();
                }
                if(null != out){
                	out.close();
                }
                if(null != applyExcelOut){
                	applyExcelOut.close();
                }
                if(null != lastConfirmPdfBoss){
                	lastConfirmPdfBoss.close();
                }
                if(null != lastConfirmXlsOut){
                	lastConfirmXlsOut.close();
                }
                if(null != lastGrantDetailOut){
                	lastGrantDetailOut.close();
                }
                if (ftpUtil != null) {
                    ftpUtil.closeServer();
                }
                if(null != lastPayPlanlOut){
                	lastPayPlanlOut.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            response.getWriter().write(toResponseJSON(responseInfo));
        } catch (IOException e) {
        }
    }
    
    /**
     * 划拨申请书导出（Pdf）
     * @param batchNum
     * @param request
     * @param response
     */
    @RequestMapping("/exportApplyPdf")
    public void exportApplyPdf(@RequestParam Long id, HttpServletRequest request, HttpServletResponse response) {
        ByteArrayOutputStream out = null;
        try {
	    	LoanPreApplyRecord loanPreApplyRecord=loanPreApplyRecordService.getById(id);
	    	String filePath = loanPreApplyRecord.getFilePath();
	    	String fileName = filePath.substring(filePath.lastIndexOf("/")+1);
	    	//连接存放盖章文件的ftp服务器
	        FTPUtil ftpUtil = connectBhxtFtpService.getFtpEsignatureConnect();
	        if(ftpUtil == null){
	            throw new PlatformException(ResponseEnum.FULL_MSG,"连接存放盖章文件的服务器失败！");
	        }
			out = yubokuanService.downloadEsignatureFileByFilePath(filePath, ftpUtil);
			if(null == out){
                throw new PlatformException(ResponseEnum.FULL_MSG, "下载划拨申请书pdf文件失败！");
			}
			String downLoadError = this.downLoadFile(request, response, fileName, out);
			// 文件下载失败
            if (Strings.isNotEmpty(downLoadError)) {
                throw new PlatformException(ResponseEnum.FULL_MSG, downLoadError);
            }
            return;
		} catch (IOException e) {
			logger.error("导出划拨申请书（PDF）异常：", e);
            ResponseInfo responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
		}
    }
    
    /**
     * 划拨申请书导出（Xls）
     * @param batchNum
     * @param request
     * @param response
     */
    @RequestMapping("/exportApplyXls")
    public void exportApplyXls(@RequestParam Long id, HttpServletRequest request, HttpServletResponse response) {
        ResponseInfo responseInfo = null;
        Map<String, Object> params =new HashMap<String, Object>();
        try {
        	LoanPreApplyRecord loanPreApplyRecord=loanPreApplyRecordService.getById(id);
        	String fileName = loanPreApplyRecord.getFilePath().substring(loanPreApplyRecord.getFilePath().lastIndexOf("/")+1).replace(".pdf", ".xls");
        	String fundsSource = loanPreApplyRecord.getFundsSource();
        	
        	params.put("organization", fundsSource);// 理财机构码
            params.put("yuboAmt", loanPreApplyRecord.getApplyAmount());// 预拨资金
            Date applyDate = loanPreApplyRecord.getApplyDate();
            Calendar c = Calendar.getInstance();
            c.setTime(applyDate);
            params.put("applyDate", String.valueOf(c.get(Calendar.YEAR))+"年"+String.valueOf(c.get(Calendar.MONTH)+1)+"月"+String.valueOf(c.get(Calendar.DAY_OF_MONTH))+"日");
        	Workbook workbook = yubokuanService.getApplyWorkbook(params,applyExcelFilePath);//获取划拨申请书excel，并填充数据
        	if(null == workbook){
                throw new PlatformException(ResponseEnum.FULL_MSG, "下载划拨申请书（Xls）文件失败！");
            }
        	String downLoadError = this.downLoadFile(request, response, fileName, workbook);
        	// 文件下载失败
            if (Strings.isNotEmpty(downLoadError)) {
                throw new PlatformException(ResponseEnum.FULL_MSG, downLoadError);
            }
            return;
        } catch (Exception e) {
            logger.error("导出划拨申请书（Xls）异常：", e);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
        }
    }
    
    /**
     * 划拨确认书导出（Pdf）
     * @param batchNum
     * @param request
     * @param response
     */
    @RequestMapping("/exportConfirmPdf")
    public void exportConfirmPdf(@RequestParam Long id, HttpServletRequest request, HttpServletResponse response) {
    	try {
    		LoanPreApplyRecord loanPreApplyRecord=loanPreApplyRecordService.getById(id);
    		String fileName = loanPreApplyRecord.getFilePath().substring(loanPreApplyRecord.getFilePath().lastIndexOf("/")+1).replace(".pdf", "_confirm_001.pdf");
    		//生成最近一次的预拨确认书文件（PDF）及预拨确认书文件（XLS）
    		ByteArrayOutputStream out = yubokuanService.getConfirmPdfOutput(loanPreApplyRecord, confirmPdfFilePath);
    		String downLoadError = this.downLoadFile(request, response, fileName, out);
    		// 文件下载失败
    		if (Strings.isNotEmpty(downLoadError)) {
    			throw new PlatformException(ResponseEnum.FULL_MSG, downLoadError);
    		}    		
    	}catch(Exception e) {
            logger.error("导出划拨确认书导出（Pdf）异常：", e);
            ResponseInfo responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
    	}
    }
    /**
     * 划拨确认书导出（Xls）
     * @param batchNum
     * @param request
     * @param response
     */
    @RequestMapping("/exportConfirmXls")
    public void exportConfirmXls(@RequestParam Long id, HttpServletRequest request, HttpServletResponse response) {    
    	try {
    		LoanPreApplyRecord loanPreApplyRecord=loanPreApplyRecordService.getById(id);
    		String fileName = loanPreApplyRecord.getFilePath().substring(loanPreApplyRecord.getFilePath().lastIndexOf("/")+1).replace(".pdf", "_confirm_001.xls");
    		//获取划拨确认书excel，并填充数据
        	Workbook wookbook = yubokuanService.getConfirmWorkbook(loanPreApplyRecord,confirmExcelFilePath);
    		String downLoadError = this.downLoadFile(request, response, fileName, wookbook);
    		// 文件下载失败
    		if (Strings.isNotEmpty(downLoadError)) {
    			throw new PlatformException(ResponseEnum.FULL_MSG, downLoadError);
    		}    		
    	}catch(Exception e) {
            logger.error("导出划拨确认书导出（Xls）异常：", e);
            ResponseInfo responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
    	}
    }
    /**
     * 放款申请明细导出（Xls）
     * @param batchNum
     * @param request
     * @param response
     */
    @RequestMapping("/exportGrantDetailXls")
    public void exportGrantDetailXls(@RequestParam Long id, HttpServletRequest request, HttpServletResponse response) {
        ResponseInfo responseInfo = null;
        try {
        	LoanPreApplyRecord loanPreApplyRecord=loanPreApplyRecordService.getById(id);
        	String fileName = loanPreApplyRecord.getFilePath().substring(loanPreApplyRecord.getFilePath().lastIndexOf("/")+1).replace(".pdf", ".xls").replace("apply", "loandetail");
        	Workbook workbook = yubokuanService.createGrantXls(loanPreApplyRecord,fileName);
        	if(null == workbook){
                throw new PlatformException(ResponseEnum.FULL_MSG, "下载划拨申请书（Xls）文件失败！");
            }
        	String downLoadError = this.downLoadFile(request, response, fileName, workbook);
        	// 文件下载失败
            if (Strings.isNotEmpty(downLoadError)) {
                throw new PlatformException(ResponseEnum.FULL_MSG, downLoadError);
            }
            return;
        }catch (PlatformException e) {
            logger.error("导出放款申请明细异常：", e);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),e.getMessage());
        } catch (Exception e) {
            logger.error("导出放款申请明细导出（Xls）异常：", e);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
        }
        try {
            response.setContentType("text/html");
            response.getWriter().write(toResponseJSON(responseInfo));
        } catch (IOException e) {
        }
    }
   
    /**
     * 文件下载
     * @param request
     * @param response
     * @param fileName
     * @param workbook
     * @return
     */
    private String downLoadFile(HttpServletRequest request,HttpServletResponse response, String fileName, Workbook workbook) {
        OutputStream out = null;
         try {
             response.setHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(fileName, "UTF-8"));
             response.setContentType(UploadFileUtil.FILE_TYPE_EXCEL[0]);
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

    private String downLoadFile(HttpServletRequest request,HttpServletResponse response, String fileName, ByteArrayOutputStream byteArrayOutputStream) {
        OutputStream out = null;
        try {
            response.setHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(fileName, "UTF-8"));
            response.setContentType("application/pdf");
            out = response.getOutputStream();
            byteArrayOutputStream.writeTo(out);
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
}
