package com.zdmoney.credit.repay.controller;

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
import org.springframework.web.servlet.ModelAndView;

import com.zdmoney.credit.common.constant.LoanStateEnum;
import com.zdmoney.credit.common.constant.SysActionLogTypeEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.util.Assert;
import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.ExcelExportUtil;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.util.excel.ExcelTemplet;
import com.zdmoney.credit.common.util.excel.ExcelUtil;
import com.zdmoney.credit.common.util.file.UploadFileUtil;
import com.zdmoney.credit.common.util.file.vo.UploadFile;
import com.zdmoney.credit.common.vo.AttachmentResponseInfo;
import com.zdmoney.credit.common.vo.ResponseInfo;
import com.zdmoney.credit.fee.service.pub.ILoanFeeInfoService;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.loan.domain.LoanLog;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.ILoanLogService;
import com.zdmoney.credit.loan.service.pub.ILoanTransferInfoService;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import com.zdmoney.credit.repay.domain.BasePrivateAccountInfo;
import com.zdmoney.credit.repay.service.pub.IBasePrivateAccountInfoService;
import com.zdmoney.credit.system.domain.PersonInfo;
import com.zdmoney.credit.system.service.pub.IPersonInfoService;
import com.zdmoney.credit.system.service.pub.ISysDictionaryService;

@Controller
@RequestMapping("/repayment")
public class PrivateAccountInfoController extends BaseController {
    
    /**
     * 领取权限url
     */
    private static final String CONFIRM_RECEIVE = "/repayment/confirmReceive";
    
    /**
     * 渠道确认权限url
     */
    private static final String CHANNEL_CONFIRM = "/repayment/channelConfirm";
    
    /**
     * 撤销权限url
     */
    private static final String CANCEL_RECEIVE = "/repayment/cancelReceive";
    
    /**
     * 查看日志url
     */
    private static final String SEARCH_LOG_INFO = "/repayment/searchLogInfo";
    
    /***
     * 角色名字
     */
    private static final String ROLE_NAME = "门店|总部-30天外认领";
    
    /***
     * 角色名字2
     */
    private static final String ROLE_NAME_TWO = "催收-全营业部停催";
    
    @Autowired
    private IBasePrivateAccountInfoService basePrivateAccountInfoService;
    
    @Autowired
    private ISysDictionaryService sysDictionaryService;
    
    @Autowired
    private ILoanLogService loanLogService;
    
    @Autowired
    private IVLoanInfoService vLoanInfoService;
    
    @Autowired
    private IPersonInfoService personInfoService;
    
    @Autowired
    private ILoanFeeInfoService loanFeeInfoService;
    
    @Autowired 
    ILoanTransferInfoService loanTransferInfoServiceImpl;
    
//    @Autowired
//	private ILoanSpecialRepaymentService loanSpecialRepaymentService;
//    
//    @Autowired
//    private ILoanRepaymentDetailService loanRepaymentDetailService;
    
    /**
     * 初始化对私还款页面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/privateAccountInfo")
    public ModelAndView jumpPage(HttpServletRequest request,HttpServletResponse response) {
        createLog(request, SysActionLogTypeEnum.查询, "对私还款", "加载对私还款页面");
        // 认领状态
        String[] statusArr = new String[]{"未认领","已认领","已导出","渠道确认"};
        User user = UserContext.getUser();
        // 是否有领取权限
        boolean isCanConfirmReceive = user.ifAnyGranted(CONFIRM_RECEIVE);
        // 是否有渠道确认权限
        boolean isCanChannelConfirm = user.ifAnyGranted(CHANNEL_CONFIRM);
        // 是否有撤销权限
        boolean isCancelReceive = user.ifAnyGranted(CANCEL_RECEIVE);
        // 是否有查看日志权限
        boolean isSearchLog = user.ifAnyGranted(SEARCH_LOG_INFO);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("repay/privateAccountInfo");
        mav.addObject("statusList", statusArr);
        mav.addObject("user", user);
        mav.addObject("isCanConfirmReceive", isCanConfirmReceive);
        mav.addObject("isCanChannelConfirm", isCanChannelConfirm);
        mav.addObject("isCancelReceive", isCancelReceive);
        mav.addObject("isSearchLog", isSearchLog);
        return mav;
    }
    
    /**
     * 对私还款信息查询
     * @param basePrivateAccountInfo
     * @param rows
     * @param page
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/searchPrivateAccountInfo")
    @ResponseBody
    public String search(BasePrivateAccountInfo basePrivateAccountInfo, int rows,int page, HttpServletRequest request, HttpServletResponse response) {
        createLog(request, SysActionLogTypeEnum.查询, "对私还款", "查询对私还款信息");
        // 定义分页实例
        Pager pager = new Pager();
        pager.setRows(rows);
        pager.setPage(page);
        basePrivateAccountInfo.setPager(pager);
        // 调用Service层查询数据信息 
        pager = basePrivateAccountInfoService.findWithPg(basePrivateAccountInfo);
        // 将数据对象转换成JSON字符串，返回给前台
        return toPGJSONWithCode(pager);
    }
    
    /**
     * 批量导入
     * @param file
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/importPrivateAccountInfo")
    @ResponseBody
    public void importPrivateAccountInfo(@RequestParam(value = "privateAccountfile") MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        OutputStream outputStream = null;
        ResponseInfo responseInfo = null;
        // 导入文件名
        String fileName = file.getOriginalFilename();
        try {
            createLog(request, SysActionLogTypeEnum.导入, "对私还款", "批量导入对私还款信息");
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
            basePrivateAccountInfoService.savePrivateAccountInfo(sheetDataList);
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
            logger.error("对私还款批量导入异常：", e);
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
     * 对私还款导出已认领结果
     * @param basePrivateAccountInfo
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/exportPrivateAccountReceiveInfo")
    public void exportPrivateAccountReceiveInfo(BasePrivateAccountInfo basePrivateAccountInfo, HttpServletRequest request, HttpServletResponse response) {
        ResponseInfo responseInfo = null;
        basePrivateAccountInfo.setStatus("已认领");
        try {
            createLog(request, SysActionLogTypeEnum.导出, "对私还款", "导出对私还款已认领结果");
            // 查询对私还款已认领结果
            List<Map<String, Object>> privateAccountReceiveInfoList = basePrivateAccountInfoService.findPrivateAccountReceiveInfo(basePrivateAccountInfo);
            Assert.notCollectionsEmpty(privateAccountReceiveInfoList,"当前导出条件查询不到数据");
            // 更新状态为已导出
            List<Long> ids = new ArrayList<Long>();
            for(Map<String, Object> privateAccountReceiveInfo:privateAccountReceiveInfoList){
                ids.add(Long.valueOf(privateAccountReceiveInfo.get("id").toString()));
            }
            // 批量更新对私还款信息
            this.updateAccountInfo(ids);
            // 下载导出文件
            String fileName = "对私账户已认领导出文件" + Dates.getDateTime(new Date(), "yyyy-MM-ddHHmmss") + ".xlsx";
            String[] labels = new String[] { "姓名", "身份证", "借款类型", "首次还款日","还款金额", "还款方式","合同编号", "备注","内部流水号","交易摘要"};
            String[] fields = new String[] { "name", "idNum", "loanType","firstRepaymentDate", "tradeAmount","tradeType","contractNum", "memo","repayNo","tradeRemark"};
            // 工作表名称
            String sheetName = "对私账户已认领结果";
            // 创建工作簿
            Workbook workbook = ExcelExportUtil.createExcelByMap(fileName, labels,fields, privateAccountReceiveInfoList, sheetName);
            // 文件下载
            String downLoadError = this.downLoadFile(request, response,fileName, workbook);
            // 文件下载失败
            if (Strings.isNotEmpty(downLoadError)) {
                throw new PlatformException(ResponseEnum.FULL_MSG, downLoadError);
            }
            return;
        } catch (PlatformException e) {
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),e.getMessage());
        } catch (Exception e) {
            logger.error("对私还款已认领结果导出异常：", e);
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
     * 批量更新对私还款信息
     * @param ids
     */
    private void updateAccountInfo(List<Long> ids) {
        List<Long> subIds = null;
        // 每一千条更新一批
        int batchSize = 1000;
        // 更新数据量
        int size = ids.size();
        // 更新次数
        int batchNum = size % batchSize == 0 ? size / batchSize : size / batchSize + 1;
        for (int i = 0; i < batchNum; i++) {
            if (i < batchNum - 1) {
                subIds = ids.subList(i * batchSize, (i + 1) * batchSize);
            } else {
                subIds = ids.subList(i * batchSize, size);
            }
            // 批量更新对私还款信息（更新状态为：已导出）
            this.updateSubAccountInfo(subIds);
        }
    }

    /**
     * 批量更新对私还款信息（更新状态为：已导出）
     * @param subIds
     */
    private void updateSubAccountInfo(List<Long> subIds) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ids", subIds);
        params.put("status", "已导出");
        params.put("updator", UserContext.getUser().getName());
        params.put("updateTime", new Date());
        basePrivateAccountInfoService.updateAccountInfoForExport(params);
    }

    /**
     * 对私还款导出查询结果
     * @param request
     * @param response
     * @param basePrivateAccountInfo
     * @return
     */
    @RequestMapping("/exportPrivateAccountSearchResult")
    public void exportPrivateAccountSearchResult(BasePrivateAccountInfo basePrivateAccountInfo,HttpServletRequest request, HttpServletResponse response){
        String fileName = "对私账户查询结果导出文件" + Dates.getDateTime(new Date(), "yyyy-MM-ddHHmmss") + ".xlsx";
        ResponseInfo responseInfo = null;
        createLog(request, SysActionLogTypeEnum.导出, "对私还款", "导出对私还款查询结果");
        try {
            // 查询对私还款信息
            //List<BasePrivateAccountInfo> queryResultList = basePrivateAccountInfoService.findQueryResultList(basePrivateAccountInfo);
            List<Map<String, Object>> queryResultList = basePrivateAccountInfoService.findQueryResultMapList(basePrivateAccountInfo);
            Assert.notCollectionsEmpty(queryResultList,"当前导出条件查询不到数据");
            String[] labels = new String[] { "交易日期", "交易时间", "交易金额","本次余额","对方户名","对方账号", "交易行","交易渠道","交易类型","交易用途","交易摘要", "状态"
                    , "借款人姓名", "身份证号", "合同编号", "首期还款日", "管理营业部","放款日期" };
            String[] fields = new String[] { "tradeDate", "tradeTime", "tradeAmount","currentBalance", "secondName", "secondAccount", "tradeBank", "tradeChannel",
                    "tradeType", "tradePurpose", "tradeRemark", "status", "name", "idNum", "contractNum", "firstRepayDate", "orgName","grantMoneyDate"};
            // 工作表名称
            String sheetName = "对私账户查询结果";
            // 创建excel工作簿
            Workbook workbook = ExcelExportUtil.createExcelByMap(fileName, labels, fields, queryResultList, sheetName);
            // 文件下载
            String downLoadError = this.downLoadFile(request, response,fileName, workbook);
            if (Strings.isNotEmpty(downLoadError)) {
                throw new PlatformException(ResponseEnum.FULL_MSG, downLoadError);
            }
            return;
        } catch (PlatformException e) {
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),e.getMessage());
        } catch (Exception e) {
            logger.error("对私还款导出查询结果异常：", e);
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
    
    /**
     * 对私还款加载领取时间
     * @param id
     * @return
     */
    @RequestMapping("/loadReceiveTime")
    @ResponseBody
    public String loadReceiveTime() {
        AttachmentResponseInfo<Map<String,Object>> responseInfo = null;
        Map<String,Object> result = null;
        try {
            // 查询对私还款领取时间范围信息
            result = basePrivateAccountInfoService.findAccountReceiveTime();
            responseInfo = new AttachmentResponseInfo<Map<String,Object>>(ResponseEnum.SYS_SUCCESS);
            responseInfo.setAttachment(result);
        } catch (PlatformException e) {
            responseInfo = new AttachmentResponseInfo<Map<String,Object>>(ResponseEnum.SYS_FAILD.getCode(),e.getMessage());
        } catch (Exception e) {
            logger.error("获取对私还款领取时间异常：", e);
            responseInfo = new AttachmentResponseInfo<Map<String,Object>>(ResponseEnum.SYS_FAILD);
        }
        return toResponseJSON(responseInfo);
    }
    
    /**
     * 修改对私还款领取时间
     * @param startDictionaryId
     * @param endDictionaryId
     * @param startTime
     * @param endTime
     * @return
     */
    @RequestMapping("/updateReceiveTime")
    @ResponseBody
    public String updateReceiveTime(@RequestParam Long startDictionaryId,@RequestParam Long endDictionaryId, 
            @RequestParam String startTime,@RequestParam String endTime) {
        ResponseInfo responseInfo = null;
        Map<String, Object> params = new HashMap<String, Object>();
        try {
            Date start = Assert.notDate(startTime, "HH:mm", "领取开始时间");
            Date end = Assert.notDate(endTime, "HH:mm", "领取结束时间");
            if(start.getTime() > end.getTime()){
                throw new PlatformException(ResponseEnum.FULL_MSG,"领取开始时间不能大于领取结束时间！");
            }
            params.put("startDictionaryId", startDictionaryId);
            params.put("endDictionaryId", endDictionaryId);
            params.put("startTime", startTime);
            params.put("endTime", endTime);
            // 更新对私还款领取时间范围信息
            basePrivateAccountInfoService.updateAccountReceiveTime(params);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_SUCCESS);
        } catch (PlatformException e) {
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),e.getMessage());
        } catch (Exception e) {
            logger.error("修改对私还款领取时间异常：", e);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
        }
        return toResponseJSON(responseInfo);
    }
    
    /**
     * 领取校验
     * @param id
     * @return
     */
    @RequestMapping("/privateAccountReceiveCheck")
    @ResponseBody
    public String receiveCheck(Long id) {
        ResponseInfo responseInfo = null;
        User user = UserContext.getUser();
        try {
            // 校验对私还款领取状态，不能领取则返回提示信息
            String message = basePrivateAccountInfoService.checkReceiveStatus();
            if (Strings.isNotEmpty(message)) {
                throw new PlatformException(ResponseEnum.FULL_MSG, message);
            }
            // 查询对私还款信息
            BasePrivateAccountInfo privateAccountInfo = basePrivateAccountInfoService.get(id);
            if (Strings.isEmpty(privateAccountInfo)) {
                throw new PlatformException(ResponseEnum.FULL_MSG,"查询不到对应的对私还款信息！");
            }
            // 状态不是未认领、不能领取
            if(!"未认领".equals(privateAccountInfo.getStatus())){
            	throw new PlatformException(ResponseEnum.FULL_MSG,"该笔存款超过30天，无法领取");           		
            }
            Date repayDate = privateAccountInfo.getTradeDate();
            if(!user.ifAnyRole(ROLE_NAME)){
           	 	if(Dates.getAfterDays(repayDate,30).before(Dates.getCurrDate())){
                	throw new PlatformException(ResponseEnum.FULL_MSG,"该笔存款超过30天，无法领取");
                }
            }
            responseInfo = new ResponseInfo(ResponseEnum.SYS_SUCCESS);
        } catch (PlatformException e) {
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),e.getMessage());
        } catch (Exception e) {
            logger.error("对私还款领取时间校验异常：", e);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
        }
        return toResponseJSON(responseInfo);
    }
    
    /**
     * 查询被领取人债权信息
     * @param rows
     * @param page
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/searchPrivateAccountReceiveInfo")
    @ResponseBody
    public String searchPrivateAccountReceiveInfo(int rows,int page,HttpServletRequest request, HttpServletResponse response) {
        createLog(request, SysActionLogTypeEnum.查询, "对私还款", "查询对私还款被领取人信息");
        Map<String,Object> params = new HashMap<String,Object>();
        // 借款人姓名
        String name = request.getParameter("name");
        if(Strings.isNotEmpty(name)){
            params.put("name", name);
        }
        // 联系电话
        String contractPhone = request.getParameter("contractPhone");
        if(Strings.isNotEmpty(contractPhone)){
            params.put("contractPhone", contractPhone);
        }
        // 身份证号码
        String idNum = request.getParameter("idNum");
        if(Strings.isNotEmpty(idNum)){
            params.put("idNum", idNum);
        }
        //合同编号
        String contractNum = request.getParameter("contractNum");
        if(Strings.isNotEmpty(contractNum)){
            params.put("contractNum", contractNum);
        }
        // 借款状态
        String[] loanStates = {LoanStateEnum.正常.getValue(),LoanStateEnum.逾期.getValue()};
        params.put("loanStates", loanStates);
        User user = UserContext.getUser();
        params.put("orgCode", user.getOrgCode());
        // 定义分页实例
        Pager pager = new Pager();
        pager.setRows(rows);
        pager.setPage(page);
        params.put("pager", pager);
        // 调用Service层查询数据信息 
        pager = basePrivateAccountInfoService.findWithPgByMap(params);
        // 将数据对象转换成JSON字符串，返回给前台
        return toPGJSONWithCode(pager);
    }
    
    /**
     * 对私还款确认领取
     * @param id
     * @param loanId
     * @param borrowerName
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/confirmReceive")
    @ResponseBody
    public String confirmReceive(@RequestParam Long id, @RequestParam Long loanId, HttpServletRequest request, HttpServletResponse response) {
        ResponseInfo responseInfo = null;
        createLog(request, SysActionLogTypeEnum.更新, "对私还款", "对私还款确认领取");
        User user = UserContext.getUser();
        // 如果没有领取权限
        if(!user.ifAnyGranted(CONFIRM_RECEIVE)){
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),"您无领取操作权限！");
            return toResponseJSON(responseInfo);
        }
        
        //检查该借款是否有已转让
  		boolean flag = loanTransferInfoServiceImpl.isLoanTransfer(null,loanId);
  		if(!flag){
  			 responseInfo = new ResponseInfo(ResponseEnum.FULL_MSG.getCode(),Strings.errorMsg);
  	         return toResponseJSON(responseInfo);
  		}
  		
        // 对私还款领取状态校验，不能认领则返回提示信息
        String message = basePrivateAccountInfoService.checkReceiveStatus();
        if(Strings.isNotEmpty(message)){
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),message);
            return toResponseJSON(responseInfo);
        }
        // 针对龙信小贷、外贸信托、外贸2、包商银行，，判断是否已经完成划扣服务费，如果没有，则不能认领
        if(!loanFeeInfoService.isAlreadyDebitServiceCharge(loanId)){
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),"没有完成划扣服务费，不能认领！");
            return toResponseJSON(responseInfo);
        }
       /* VLoanInfo vLoanInfo = vLoanInfoService.findByLoanId(id);
	    if(FundsSourcesTypeEnum.包商银行.getValue().equals(vLoanInfo.getLoanBelong())){
        	// 得到特殊还款记录表信息
            LoanSpecialRepayment specialRepayment = loanSpecialRepaymentService.findByLoanIdAndRequestDateAndTypesAndState(id,
         				null,
         				new String[]{SpecialRepaymentTypeEnum.提前扣款.getValue(),SpecialRepaymentTypeEnum.一次性还款.getValue()}, 
         				SpecialRepaymentStateEnum.申请.getValue());
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("loanId", id);
            map.put("CurrTermReturnDate", Dates.getCurrDate());
            List<LoanRepaymentDetail> planlist = loanRepaymentDetailService.findByLoanIdAndRepaymentState(map);
            LoanRepaymentDetail last = planlist.get(planlist.size()-1);
            //当提前申请并未生效，而且也没有逾期，不能认领
            if(specialRepayment == null && last.getRepaymentState().equals("结清")){
            	responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),"包商银行提前还款/结清申请未生效，而且也没有逾期，不能认领");
                return toResponseJSON(responseInfo);
            }
        }*/
        // 查询对私账户信息
        BasePrivateAccountInfo privateAccountInfo = basePrivateAccountInfoService.get(id);
        if(Strings.isEmpty(privateAccountInfo)){
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),"查询不到待认领的对私还款记录！");
            return toResponseJSON(responseInfo);
        }
        // 如果状态不是未认领，则不能领取
        if(!"未认领".equals(privateAccountInfo.getStatus())){
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),"状态不是未认领，不能领取！");
            return toResponseJSON(responseInfo);
        }

        String dateStr = Dates.getDateTime(privateAccountInfo.getTradeDate(),Dates.DEFAULT_DATE_FORMAT);
        String timeStr =privateAccountInfo.getTradeTime().trim();
        Date tradeDate = Dates.getDateByString(dateStr+" "+timeStr, Dates.DEFAULT_DATETIME_FORMAT);
        //判断存款日期是否小于放款日期
        VLoanInfo vLoanInfo = vLoanInfoService.findByLoanId(loanId);
        Date repayDate = privateAccountInfo.getTradeDate();
        if(!user.ifAnyRole(ROLE_NAME_TWO)){//有这个角色 则可以进行认领
        	if(Dates.getAfterDays(repayDate,30).before(Dates.getCurrDate())){//该笔存款超过30天
        		if(!user.ifAnyRole(ROLE_NAME)){
        			responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),"该笔存款超过30天，无法领取");
        			return toResponseJSON(responseInfo);     		
        		}
        	}else{//该笔存款未超过30天
        		if(user.ifAnyRole(ROLE_NAME)){
        			if(vLoanInfo.getGrantMoneyDate() != null && !tradeDate.before(vLoanInfo.getGrantMoneyDate())){//存款时间大于等于申请件的放款时间
        				responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),"该数据在权限范围以外");
        				return toResponseJSON(responseInfo);
        			}
        		}else{
        			if(vLoanInfo.getGrantMoneyDate() != null && tradeDate.before(vLoanInfo.getGrantMoneyDate())){//存款时间小于申请件的放款时间
        				responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),"该笔存款时间小于申请件放款时间");
        				return toResponseJSON(responseInfo);
        			}
        		}
        	}
        }
        try {
            BasePrivateAccountInfo accountInfo = new BasePrivateAccountInfo();
            accountInfo.setId(id);
            accountInfo.setStatus("已认领");
            accountInfo.setLoanId(loanId);
            accountInfo.setRecOperatorId(user.getId());
            accountInfo.setRecTime(new Date());
            String content = "认领";
            // 查询借款人信息
            PersonInfo personInfo = this.queryPersonInfo(loanId);
            if(Strings.isNotEmpty(personInfo)){
                content = content + "（"+personInfo.getName()+"："+personInfo.getIdnum()+"）";
            }
            // 日志记录
            loanLogService.createLog(privateAccountInfo.getId(), content,"PrivateAccountInfoController", "info", "对私账户认领");
            // 更新对私还款信息
            basePrivateAccountInfoService.updatePrivateAccountInfo(accountInfo);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_SUCCESS.getCode(),"对私账户【"+ id +"】领取成功！");
        } catch (Exception e) {
            logger.error("对私认领异常：", e);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),"领取失败！");
        }
        return toResponseJSON(responseInfo);
    }
    
    /**
     * 渠道确认
     * @param id
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/channelConfirm")
    @ResponseBody
    public String channelConfirm(Long id,HttpServletRequest request, HttpServletResponse response) {
        ResponseInfo responseInfo = null;
        User user = UserContext.getUser();
        BasePrivateAccountInfo privateAccountInfo = basePrivateAccountInfoService.get(id);
        if(Strings.isEmpty(privateAccountInfo)){
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),"查询不到待认领的对私还款记录！");
            return toResponseJSON(responseInfo);
        }
        // 如果状态不是未认领，则不能渠道确认
        if(!"未认领".equals(privateAccountInfo.getStatus())){
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),"状态不是未认领，不能渠道确认");
            return toResponseJSON(responseInfo);
        }
        try {
            BasePrivateAccountInfo accountInfo = new BasePrivateAccountInfo();
            accountInfo.setId(id);
            accountInfo.setStatus("渠道确认");
            accountInfo.setRecOperatorId(user.getId());
            accountInfo.setRecTime(new Date());
            // 日志记录
            loanLogService.createLog(privateAccountInfo.getId(), "渠道确认","PrivateAccountInfoController", "info", "对私还款渠道确认");
            // 更新对私还款信息
            basePrivateAccountInfoService.updatePrivateAccountInfo(accountInfo);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_SUCCESS.getCode(),"渠道确认成功！");
        } catch (Exception e) {
            logger.error("渠道确认异常：", e);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),"渠道确认失败！");
        }
        return toResponseJSON(responseInfo);
    }
    
    /**
     * 撤销领取（包括认领和渠道确认）
     * @param id
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/cancelReceive")
    @ResponseBody
    public String cancelReceive(Long id,HttpServletRequest request, HttpServletResponse response) {
        ResponseInfo responseInfo = null;
        User user = UserContext.getUser();
        BasePrivateAccountInfo privateAccountInfo = basePrivateAccountInfoService.get(id);
        if(Strings.isEmpty(privateAccountInfo)){
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),"查询不到待撤销的对私还款记录！");
            return toResponseJSON(responseInfo);
        }
        // 如果状态不是已认领或者渠道确认，则不能撤销
        String status = privateAccountInfo.getStatus();
        if(!"已认领".equals(status) && !"渠道确认".equals(status)){
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),"状态不是已认领或者渠道确认，不能撤销");
            return toResponseJSON(responseInfo);
        }
        try {
            BasePrivateAccountInfo accountInfo = new BasePrivateAccountInfo();
            accountInfo.setId(id);
            accountInfo.setStatus("未认领");
            accountInfo.setRecOperatorId(null);
            accountInfo.setRecTime(null);
            accountInfo.setLoanId(null);
            accountInfo.setUpdator(user.getName());
            accountInfo.setUpdateTime(new Date());
            
            String content = "撤销";
            if("已认领".equals(status) && Strings.isNotEmpty(privateAccountInfo.getLoanId())){
                // 查询借款人信息
                PersonInfo personInfo = this.queryPersonInfo(privateAccountInfo.getLoanId());
                if(Strings.isNotEmpty(personInfo)){
                    content = content + "（"+personInfo.getName()+"："+personInfo.getIdnum()+"）";
                }
            }
            // 日志记录
            loanLogService.createLog(privateAccountInfo.getId(), content,"PrivateAccountInfoController", "info", "对私账户撤销");
            // 更新对私还款信息（撤销领取或撤销渠道确认）
            basePrivateAccountInfoService.updateAccountInfoForCancel(accountInfo);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_SUCCESS.getCode(),"对私账户【"+ id +"】撤销成功！");
        } catch (Exception e) {
            logger.error("撤销失败：", e);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),"撤销失败！");
        }
        return toResponseJSON(responseInfo);
    }
    
    /**
     * 查询借款人信息
     * @param loanId
     * @return
     */
    private PersonInfo queryPersonInfo(Long loanId) {
        if(Strings.isEmpty(loanId)){
            return null;
        }
        // 查询债权信息
        VLoanInfo loanInfo = vLoanInfoService.findByLoanId(loanId);
        if(Strings.isEmpty(loanInfo)){
            return null;
        }
        // 查询客户信息
        PersonInfo personInfo = personInfoService.findById(loanInfo.getBorrowerId());
        if(Strings.isEmpty(personInfo)){
            return null;
        }
        return personInfo;
    }
    
    /**
     * 查询日志信息
     * @param loanLog
     * @param rows
     * @param page
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/searchLogInfo")
    @ResponseBody
    public String searchLogInfo(LoanLog loanLog, int rows,int page, HttpServletRequest request, HttpServletResponse response) {
        createLog(request, SysActionLogTypeEnum.查询, "对私还款", "查询对私还款操作日志信息");
        // 定义分页实例
        Pager pager = new Pager();
        pager.setRows(rows);
        pager.setPage(page);
        pager.setSidx("ID");
        pager.setSort("ASC");
        loanLog.setPager(pager);
        // 调用Service层查询操作日志信息 
        pager = loanLogService.findWithPg(loanLog);
        // 将数据对象转换成JSON字符串，返回给前台
        return toPGJSONWithCode(pager);
    }
}
