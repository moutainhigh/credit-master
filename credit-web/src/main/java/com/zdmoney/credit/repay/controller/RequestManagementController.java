package com.zdmoney.credit.repay.controller;

import com.zdmoney.credit.common.constant.FundsSourcesTypeEnum;
import com.zdmoney.credit.common.constant.SysActionLogTypeEnum;
import com.zdmoney.credit.common.constant.repay.ReqManagerFileTypeEnum;
import com.zdmoney.credit.common.constant.repay.ReqManagerOperateTypeEnum;
import com.zdmoney.credit.common.constant.repay.RequestManagementConst;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.util.*;
import com.zdmoney.credit.common.util.excel.ExcelTemplet;
import com.zdmoney.credit.common.util.excel.ExcelUtil;
import com.zdmoney.credit.common.util.file.JschSftpUtils;
import com.zdmoney.credit.common.util.file.UploadFileUtil;
import com.zdmoney.credit.common.util.file.vo.UploadFile;
import com.zdmoney.credit.common.vo.ResponseInfo;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.loan.service.ConnectBhxtFtpServiceImpl;
import com.zdmoney.credit.loan.service.pub.IConnectBhxtFtpService;
import com.zdmoney.credit.repay.service.pub.IRequestManagementService;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/requestManagement")
public class RequestManagementController extends BaseController {
    
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
    private IRequestManagementService requestManagementService;
    
    @Autowired
    private IConnectBhxtFtpService connectBhxtFtpService;
    
    /** 划拨申请书pdf模板文件所在目录 **/
    @Value("${Request.Pdf.Template.Path}")
    private String pdfFilePath;
    /** 渤海2 划拨申请书pdf模板文件所在目录 **/
    @Value("${BH2.Bhxt2Apply.Pdf.Template.Path}")
    private String pdfFilePathBh2;
    
    /** 划拨申请书excel模板文件所在目录 **/
    @Value("${Request.Excel.Template.Path}")
    private String excelFilePath;
    /** 渤海2 划拨申请书excel模板文件所在目录 **/
    @Value("${BH2.Bhxt2Apply.Excel.Template.Path}")
    private String excelFilePathBh2;
    /**
     * 初始化申请书管理页面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/jumpPage")
    public ModelAndView jumpPage(HttpServletRequest request,HttpServletResponse response) {
        createLog(request, SysActionLogTypeEnum.查询, "申请书管理", "加载申请书管理页面");
        User user = UserContext.getUser();
        // 划拨申请书导出（PDF）权限
        boolean isExportApplyPdf = user.ifAnyGranted(EXPORT_APPLY_PDF_URL);
        // 划拨申请书导出（Xls）权限
        boolean isExportApplyXls = user.ifAnyGranted(EXPORT_APPLY_XLS_URL);
        // 放款申请明细导出（Xls）权限
        boolean isExportLoanApplyXls = user.ifAnyGranted(EXPORT_LOAN_APPLY_XLS_URL);
        // 放款申请明细导出（Txt）权限
        boolean isExportLoanApplyTxt = user.ifAnyGranted(EXPORT_LOAN_APPLY_TXT_URL);
        // 还款计划导出（Xls）权限
        boolean isExportPayPlanXls = user.ifAnyGranted(EXPORT_PAY_PLAN_XLS_URL);
        // 划拨申请书导入权限
        boolean isImportApplyPdf = user.ifAnyGranted(IMPORT_APPLY_PDF_URL);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("repay/requestManagement");
        mav.addObject("organizationList", this.getOrganizationList());
        mav.addObject("user", user);
        mav.addObject("isExportApplyPdf", isExportApplyPdf);
        mav.addObject("isExportApplyXls", isExportApplyXls);
        mav.addObject("isExportLoanApplyXls", isExportLoanApplyXls);
        mav.addObject("isExportLoanApplyTxt", isExportLoanApplyTxt);
        mav.addObject("isExportPayPlanXls", isExportPayPlanXls);
        mav.addObject("isImportApplyPdf", isImportApplyPdf);
        return mav;
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
        // 理财机构码
        String organization = request.getParameter("organization");
        if(Strings.isNotEmpty(organization)){
            params.put("organization", organization);
        }
        // 债权批次号
        String batchNum = request.getParameter("batchNum");
        if(Strings.isNotEmpty(batchNum)){
            params.put("batchNum", batchNum);
        }
        // 是否上传
        String status = request.getParameter("status");
        if(Strings.isNotEmpty(status)){
            params.put("status", status);
        }
        // 定义分页实例
        Pager pager = new Pager();
        pager.setRows(rows);
        pager.setPage(page);
        params.put("pager", pager);
        // 调用Service层查询数据信息 
        pager = requestManagementService.queryBatchNumInfo(params);
        // 将数据对象转换成JSON字符串，返回给前台
        return toPGJSONWithCode(pager);
    }
    
    /**
     * 理财机构下拉框展示
     * @return
     */
    private List<Map<String,String>> getOrganizationList(){
        List<Map<String,String>> resultList = new ArrayList<Map<String,String>>();
        Map<String,String> map = new HashMap<String,String>();
        map.put("code", "BHXT");
        map.put("value", FundsSourcesTypeEnum.渤海信托.getValue());
        Map<String,String> mapbh2 = new HashMap<String,String>();
        mapbh2.put("code", "BH2");
        mapbh2.put("value", FundsSourcesTypeEnum.渤海2.getValue());
        Map<String,String> maphrbh = new HashMap<>();
        maphrbh.put("code","HRBH");
        maphrbh.put("value",FundsSourcesTypeEnum.华瑞渤海.getValue());
        resultList.add(maphrbh);
        resultList.add(mapbh2);
        resultList.add(map);
        return resultList;
    }
    
    /**
     * 划拨申请书导出（PDF）
     * @param batchNum
     * @param request
     * @param response
     */
    @RequestMapping("/exportApplyPdf")
    public void exportApplyPdf(@RequestParam String batchNum, HttpServletRequest request, HttpServletResponse response) {
        ResponseInfo responseInfo = null;
        OutputStream os = null;
        ByteArrayOutputStream out = null;
        try {
            createLog(request, SysActionLogTypeEnum.导出, "申请书管理", "导出划拨申请书（PDF）文件");
            //获取项目简码
            String projectCode = requestManagementService.getProjectCode(batchNum);
            //获取当天文件下载批次号
            String fileBatchNum=requestManagementService.getRequestManagerFileToDayBatchNum(ReqManagerFileTypeEnum.划拨申请书pdf.getFileType(),batchNum);
            // 下载文件名称编辑
            String fileName=requestManagementService.getRequestManagementFileName(ReqManagerFileTypeEnum.划拨申请书pdf,fileBatchNum,projectCode);
            // 创建划拨申请书pdf文件输出流对象
            if (RequestManagementConst.TRUST_PROJECT_CODE.equals(projectCode)) {
                out = requestManagementService.getApplyPdfOutput(batchNum, pdfFilePath);
            }else if (RequestManagementConst.TRUST_PROJECT_CODE2.equals(projectCode) || RequestManagementConst.TRUST_PROJECT_CODE3.equals(projectCode)) {
                out = requestManagementService.getApplyPdfOutput(batchNum, pdfFilePathBh2);
            }
            if(null == out){
                throw new PlatformException(ResponseEnum.FULL_MSG, "创建划拨申请书pdf文件失败！");
            }
            requestManagementService.createRequestManagerOperateRecord(batchNum,"", ReqManagerOperateTypeEnum.downLoad.getOperateType(),fileBatchNum);
            response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
            response.setContentType("application/octet-stream");
            os = response.getOutputStream();
            os.write(out.toByteArray());
            os.flush();
            return;
        } catch (PlatformException e) {
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),e.getMessage());
        } catch (Exception e) {
            logger.error("导出划拨申请书（PDF）异常：", e);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
        } finally {
            try {
                if(null != os){
                    os.close();
                }
                if(null != out){
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            response.setContentType("text/html");
            response.getWriter().write(toResponseJSON(responseInfo));
        } catch (IOException e) {
        }
    }
    
    /**
     * 划拨申请书导出（Xls）
     * @param batchNum
     * @param request
     * @param response
     */
    @RequestMapping("/exportApplyXls")
    public void exportApplyXls(@RequestParam String batchNum, HttpServletRequest request, HttpServletResponse response) {
        ResponseInfo responseInfo = null;
        try {
            createLog(request, SysActionLogTypeEnum.导出, "申请书管理", "导出划拨申请书（Xls）文件");
            //获取当天划拨申请书导出xls的批次号
            String fileBatchNum=requestManagementService.getRequestManagerFileToDayBatchNum(ReqManagerFileTypeEnum.划拨申请书xls.getFileType(),batchNum);
            String projectCode=requestManagementService.getProjectCode(batchNum);
            // 下载文件名称编辑
            String fileName = requestManagementService.getRequestManagementFileName(ReqManagerFileTypeEnum.划拨申请书xls,fileBatchNum,projectCode);
            // 创建划拨申请书excel文件对象
            Workbook workbook = null;
//            workbook = requestManagementService.getApplyWorkbook(batchNum,fileName, excelFilePathBh2);
            if(RequestManagementConst.TRUST_PROJECT_CODE.equals(projectCode)){
                workbook = requestManagementService.getApplyWorkbook(batchNum,fileName, excelFilePath);
            }else if (RequestManagementConst.TRUST_PROJECT_CODE2.equals(projectCode) || RequestManagementConst.TRUST_PROJECT_CODE3.equals(projectCode)) {
                workbook = requestManagementService.getApplyWorkbook(batchNum,fileName, excelFilePathBh2);
            }
            if(null == workbook){
                throw new PlatformException(ResponseEnum.FULL_MSG, "创建划拨申请书（Xls）文件失败！");
            }
            // 文件下载
            String downLoadError = this.downLoadFile(request, response,fileName, workbook);
            // 文件下载失败
            if (Strings.isNotEmpty(downLoadError)) {
                throw new PlatformException(ResponseEnum.FULL_MSG, downLoadError);
            }
            //添加下载记录
            requestManagementService.createRequestManagerOperateRecord(batchNum,"",ReqManagerOperateTypeEnum.downLoad.getOperateType(),fileBatchNum);
            return;
        } catch (PlatformException e) {
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),e.getMessage());
        } catch (Exception e) {
            logger.error("导出划拨申请书（Xls）异常：", e);
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
     * 放款申请明细导出（Xls）
     * @param batchNum
     * @param request
     * @param response
     */
    @RequestMapping("/exportLoanApplyXls")
    public void exportLoanApplyXls(@RequestParam String batchNum, HttpServletRequest request, HttpServletResponse response) {
        ResponseInfo responseInfo = null;
        try {
            createLog(request, SysActionLogTypeEnum.导出, "申请书管理", "导出放款申请明细（Xls）文件");
            //获取放款申请明细导出xls当天的文件批次号
            String fileBatchNum =requestManagementService.getRequestManagerFileToDayBatchNum(ReqManagerFileTypeEnum.放款申请书xls.getFileType(),batchNum);
            String projectCode = requestManagementService.getProjectCode(batchNum);
            // 下载文件名称编辑
            String fileName = requestManagementService.getRequestManagementFileName(ReqManagerFileTypeEnum.放款申请书xls,fileBatchNum,projectCode);
            // 创建放款申请明细excel文件对象
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("apply", "apply");
            Workbook workbook = requestManagementService.getLoanApplyWorkbook(batchNum, fileName, map);
            if(null == workbook){
                throw new PlatformException(ResponseEnum.FULL_MSG, "创建放款申请明细excel文件失败！");
            }
            // 文件下载
            String downLoadError = this.downLoadFile(request, response,fileName, workbook);
            // 文件下载失败
            if (Strings.isNotEmpty(downLoadError)) {
                throw new PlatformException(ResponseEnum.FULL_MSG, downLoadError);
            }
            //添加下载记录
            requestManagementService.createRequestManagerOperateRecord(batchNum,"",ReqManagerOperateTypeEnum.downLoad.getOperateType(),fileBatchNum);
            return;
        } catch (PlatformException e) {
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),e.getMessage());
        } catch (Exception e) {
            logger.error("导出放款申请明细（Xls）异常：", e);
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
     * 放款申请明细导出（Txt）
     * @param batchNum
     * @param request
     * @param response
     */
    @RequestMapping("/exportLoanApplyTxt")
    public void exportLoanApplyTxt(@RequestParam String batchNum, HttpServletRequest request, HttpServletResponse response) {
        ResponseInfo responseInfo = null;
        OutputStream os = null;
        OutputStream out = null;
        try {
            createLog(request, SysActionLogTypeEnum.导出, "申请书管理", "导出放款申请明细（Txt）文件");
            //获取当天放款申请明细导出txt的文件批次号
            String fileBatchNum=requestManagementService.getRequestManagerFileToDayBatchNum(ReqManagerFileTypeEnum.放款申请书txt.getFileType(),batchNum);
            String projectCode = requestManagementService.getProjectCode(batchNum);
            // 下载文件名称编辑
            String fileName = requestManagementService.getRequestManagementFileName(ReqManagerFileTypeEnum.放款申请书txt,fileBatchNum,projectCode);
            // 创建放款申请明细TXT文件输出流对象
            out = requestManagementService.getLoanApplyTxtOutput(batchNum, fileName);
            if(null == out){
                throw new PlatformException(ResponseEnum.FULL_MSG, "创建放款申请明细TXT文件失败！");
            }
            response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
            response.setContentType("application/octet-stream");
            os = response.getOutputStream();
            os.write(out.toString().getBytes());
            os.flush();
            //添加下载记录
            requestManagementService.createRequestManagerOperateRecord(batchNum,"",ReqManagerOperateTypeEnum.downLoad.getOperateType(),fileBatchNum);
            return;
        } catch (PlatformException e) {
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),e.getMessage());
        } catch (Exception e) {
            logger.error("导出放款申请明细（Txt）异常：", e);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
        } finally {
            try {
                if(out != null){
                    out.close();
                }
                if(os != null){
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            response.setContentType("text/html");
            response.getWriter().write(toResponseJSON(responseInfo));
        } catch (IOException e) {
        }
    }
    
    /**
     * 还款计划导出（Xls）
     * @param batchNum
     * @param request
     * @param response
     */
    @RequestMapping("/exportPayPlanXls")
    public void exportPayPlanXls(@RequestParam String batchNum, HttpServletRequest request, HttpServletResponse response) {
        ResponseInfo responseInfo = null;
        try {
            createLog(request, SysActionLogTypeEnum.导出, "申请书管理", "导出还款计划（Xls）文件");
            //获取当天还款计划导出xls文件批次号
            String fileBatchNum=requestManagementService.getRequestManagerFileToDayBatchNum(ReqManagerFileTypeEnum.还款计划xls.getFileType(),batchNum);
            String projectCode = requestManagementService.getProjectCode(batchNum);
            // 下载文件名称编辑
            String fileName = requestManagementService.getRequestManagementFileName(ReqManagerFileTypeEnum.还款计划xls,fileBatchNum,projectCode);
            // 创建还款计划excel文件对象
            Workbook workbook = requestManagementService.getPayPlanWorkbook(batchNum, fileName);
            if(null == workbook){
                throw new PlatformException(ResponseEnum.FULL_MSG, "创建还款计划excel文件失败！");
            }
            // 文件下载
            String downLoadError = this.downLoadFile(request, response,fileName, workbook);
            // 文件下载失败
            if (Strings.isNotEmpty(downLoadError)) {
                throw new PlatformException(ResponseEnum.FULL_MSG, downLoadError);
            }
            //添加下载记录
            requestManagementService.createRequestManagerOperateRecord(batchNum,"",ReqManagerOperateTypeEnum.downLoad.getOperateType(),fileBatchNum);
            return;
        } catch (PlatformException e) {
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),e.getMessage());
        } catch (Exception e) {
            logger.error("导出还款计划（Xls）异常：", e);
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
     * 划拨申请书导入
     * @param file
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/importApplyPdf")
    @ResponseBody
    public void importApplyPdf(@RequestParam(value = "privateAccountfile") MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        OutputStream outputStream = null;
        ResponseInfo responseInfo = null;
        // 导入文件名
        String fileName = file.getOriginalFilename();
        try {
            createLog(request, SysActionLogTypeEnum.导入, "申请书管理", "导入划拨申请书（PDF）");
            // 文件校验
            UploadFile uploadFile = new UploadFile();
            uploadFile.setFile(file);
            uploadFile.setFileType(UploadFileUtil.FILE_TYPE_EXCEL);
            uploadFile.setFileMaxSize(1024 * 1024 * 10);
            UploadFileUtil.valid(uploadFile);
            // 创建excel工作簿
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            // 创建导入数据模板
            ExcelTemplet excelTemplet = new ExcelTemplet().new PrivateAccountInfoInputExcel();
            // 文件数据转换为List集合
            List<Map<String, String>> sheetDataList = ExcelUtil.getExcelData(workbook, excelTemplet);
            if(CollectionUtils.isEmpty(sheetDataList)){
                throw new PlatformException(ResponseEnum.FULL_MSG,"导入文件不能为空！");
            }
            // 保存导入数据
            //basePrivateAccountInfoService.savePrivateAccountInfo(sheetDataList);
            // 在excel文件的每行末尾追加单元格提示信息
            ExcelUtil.addResultToWorkBook(workbook, sheetDataList, excelTemplet);
            response.setHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(fileName, "UTF-8"));
            response.setContentType(file.getContentType());
            outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            return;
        } catch (PlatformException e) {
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),e.getMessage());
        } catch (Exception e) {
            logger.error("导入划拨申请书（PDF）异常：", e);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
        }finally{
            if(null!=outputStream){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
                }
            }
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

    @ResponseBody
    @RequestMapping("/importApplyPdfAndXls")
    public String importApplyPdfAndXls(@RequestParam(value="applyFile") MultipartFile[] files,@RequestParam(value = "hksqBatchNum") String batchNum,HttpServletRequest request, HttpServletResponse response){
        ResponseInfo responseInfo = null;
        JschSftpUtils jschSftpUtils = null;
        try {
            if (files == null || files.length == 0) {
                throw new PlatformException(ResponseEnum.FULL_MSG, "查询数据不存在！");
            }
            Assert.notNull(batchNum,"导入批次号不能为空！");
            Map<String,InputStream> inputStreamMap=new HashMap<>();
            String fileBatchNum="";
            //获取导入的划拨申请书 文件
            for (MultipartFile multipartFile : files) {
                if(multipartFile.isEmpty()){
                throw new PlatformException(ResponseEnum.FULL_MSG,"划拨申请书不存在！");
                }
                String applyFileName=multipartFile.getOriginalFilename();
                InputStream applyFileInputStream=multipartFile.getInputStream();
                if(applyFileName.indexOf(".pdf")!=-1){
                        fileBatchNum=applyFileName.substring(applyFileName.lastIndexOf(".")-12,applyFileName.lastIndexOf(".pdf"));
                }
                inputStreamMap.put(applyFileName,applyFileInputStream);
            }
            //获取当前批次号的上一个批次的 还款计划exce 放款申请明细TXT 放款申请明细excel 输入流
            Map<String,InputStream> exculeApplyPdfXlsInputMap= requestManagementService.getUploadApplyFileInputMap(batchNum,fileBatchNum);
            inputStreamMap.putAll(exculeApplyPdfXlsInputMap);
            //连接渤海信托ftp服务器
            if(!connectBhxtFtpService.getConnectionParam()){
                throw new PlatformException(ResponseEnum.FULL_MSG,"获取渤海ftp登录参数失败！");
            }
            if(Strings.isEmpty(ConnectBhxtFtpServiceImpl.applyUploadDirBH)||Strings.isEmpty(ConnectBhxtFtpServiceImpl.loanapplyUploadDirBH)||Strings.isEmpty(ConnectBhxtFtpServiceImpl.payplanUploadDirBH)){
                throw new PlatformException(ResponseEnum.FULL_MSG,"未初始化上传目录！");
            }
            jschSftpUtils=connectBhxtFtpService.getFtpBhxtConnectJsch();
           if(!jschSftpUtils.login()){
               throw new PlatformException(ResponseEnum.FULL_MSG,"上传失败！服务器连接不可连接！");
           }
            String projectCode = requestManagementService.getProjectCode(batchNum);
            boolean uploadStatus=requestManagementService.uploadFtpBHXT(jschSftpUtils,inputStreamMap,projectCode);
            if(!uploadStatus){
                throw new PlatformException(ResponseEnum.FULL_MSG,"上传失败！");
            }
            //添加上传记录
            requestManagementService.createRequestManagerOperateRecord(batchNum,"",ReqManagerOperateTypeEnum.upload.getOperateType(),fileBatchNum);
            if (RequestManagementConst.TRUST_PROJECT_CODE2.equals(projectCode) || RequestManagementConst.TRUST_PROJECT_CODE3.equals(projectCode)) {
                requestManagementService.createApplyBookInfos(batchNum, fileBatchNum);
            }
            responseInfo = new ResponseInfo(ResponseEnum.SYS_SUCCESS.getCode(),"上传成功！");
        }catch (PlatformException e){
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),e.getMessage());
        } catch (Exception e){
            logger.error("导入划拨申请书（PDF）异常："+ e.getMessage());
            responseInfo=new ResponseInfo(ResponseEnum.SYS_FAILD);
        }finally {
            if (jschSftpUtils!=null) {
                jschSftpUtils.logout();
            }
        }
        return toResponseJSON(responseInfo);
    }
    @ResponseBody
    @RequestMapping("/applyPdfEsignature")
    public void applyPdfEsignature(@RequestParam(value = "applyEsignaturePdf")MultipartFile multipartfile,HttpServletRequest request,HttpServletResponse response){
        ResponseInfo responseInfo = null;
        ByteArrayOutputStream out = null;
        FTPUtil ftpUtil = null;
        try {
            if(multipartfile.isEmpty()){
                logger.info("上传文件不存在");
                throw new PlatformException(ResponseEnum.FULL_MSG,"划拨申请书原件不存在！");
            }
            String fileName = multipartfile.getOriginalFilename();
            InputStream in = multipartfile.getInputStream();
            ftpUtil = connectBhxtFtpService.getFtpEsignatureConnect();
            if(ftpUtil == null){
                throw new PlatformException(ResponseEnum.FULL_MSG,"连接服务器失败！");
            }
            String projectCode = fileName.substring(0,fileName.indexOf("_"));
            String esignatureFtpFilePathName = requestManagementService.isExistEsignatureFtpFile(ConnectBhxtFtpServiceImpl.esignatureFtpDownloadPath+"/"+projectCode,fileName,ftpUtil);
            if(!Strings.isEmpty(esignatureFtpFilePathName)){
                out=requestManagementService.downloadEsignatureFileByFilePath(esignatureFtpFilePathName,ftpUtil);
                this.downLoadFile(request,response,fileName,out);
                return ;
            }
            String  httpFilePath=requestManagementService.uploadEsignatureFile(in,ConnectBhxtFtpServiceImpl.esignatureFtpUploadPath,fileName,projectCode,ftpUtil);
            if(Strings.isEmpty(httpFilePath)){
                throw new PlatformException(ResponseEnum.FULL_MSG,"上传签章文件到服务器失败！");
            }
            String httpUrl =requestManagementService.dealEsignature(httpFilePath, fileName);
            if (Strings.isEmpty(httpUrl)) {
                throw new PlatformException(ResponseEnum.FULL_MSG,"签章失败！");
            }
            out = requestManagementService.downloadEsignatureFileByHttpUrl(httpUrl);
            requestManagementService.uploadEsignatureFile(new ByteArrayInputStream(out.toByteArray()),ConnectBhxtFtpServiceImpl.esignatureFtpDownloadPath,fileName,projectCode,ftpUtil);
            this.downLoadFile(request,response,fileName,out);
            return ;
        } catch (PlatformException e) {
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),e.getMessage());
        } catch (Exception e) {
            logger.error("划拨申请书（PDF）签章异常：", e);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
        }finally {
            try {
                if (ftpUtil != null) {
                    ftpUtil.closeServer();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            response.setContentType("text/html");
            response.getWriter().write(toResponseJSON(responseInfo));
        } catch (IOException e) {
        }
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
