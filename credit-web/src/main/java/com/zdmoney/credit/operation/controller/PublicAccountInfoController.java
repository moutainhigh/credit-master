package com.zdmoney.credit.operation.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLDecoder;
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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.zdmoney.credit.common.constant.PublicAccountStatusEnum;
import com.zdmoney.credit.common.constant.PublicAccountSystemTypeEnum;
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
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.loan.domain.LoanLog;
import com.zdmoney.credit.loan.service.pub.ILoanLogService;
import com.zdmoney.credit.operation.domain.BasePublicAccountInfo;
import com.zdmoney.credit.operation.service.pub.IBasePublicAccountInfoService;
import com.zdmoney.credit.system.service.pub.ISysDictionaryService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;

@Controller
@RequestMapping("/operation")
public class PublicAccountInfoController extends BaseController {
    
    /**
     * 领取权限url
     */
    private static final String CONFIRM_RECEIVE = "/operation/confirmReceive";
    
    /**
     * 渠道确认权限url
     */
    private static final String CHANNEL_CONFIRM = "/operation/channelConfirm";
    
    /**
     * 撤销权限url
     */
    private static final String CANCEL_RECEIVE = "/operation/cancelReceive";
    
    /**
     * 查看日志url
     */
    private static final String SEARCH_LOG_INFO = "/operation/searchLogInfo";
    
    /**
     * 修改领取时间url
     */
    private static final String UPDATE_RECEIVE_TIME = "/operation/updateReceiveTime";
    /***
     * 角色名字
     */
    private static final String ROLE_NAME = "门店|总部-30天外认领";
    
    @Autowired
    private IBasePublicAccountInfoService basePublicAccountInfoService;
    
    @Autowired
    private ISysParamDefineService sysParamDefineService;
    
    @Autowired
    private ISysDictionaryService sysDictionaryService;
    
    @Autowired
    private ILoanLogService loanLogService;
    
    @Autowired
    private RestTemplate restTemplate;
    
    /**
     * 初始化页面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/publicAccountInfo")
    public ModelAndView jumpPage(HttpServletRequest request,HttpServletResponse response) {
        createLog(request, SysActionLogTypeEnum.查询, "对公账户还款", "加载对公账户还款页面");
        User user = UserContext.getUser();
        // 是否有领取权限
        boolean isCanConfirmReceive = user.ifAnyGranted(CONFIRM_RECEIVE);
        // 是否有渠道确认权限
        boolean isCanChannelConfirm = user.ifAnyGranted(CHANNEL_CONFIRM);
        // 是否有撤销权限
        boolean isCancelReceive = user.ifAnyGranted(CANCEL_RECEIVE);
        // 是否有查看日志权限
        boolean isSearchLog = user.ifAnyGranted(SEARCH_LOG_INFO);
        // 是否有修改对公领取时间权限
        boolean isUpdateReceiveTime = user.ifAnyGranted(UPDATE_RECEIVE_TIME);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("operation/publicAccountInfo");
        mav.addObject("statusList", PublicAccountStatusEnum.values());
        mav.addObject("user", user);
        mav.addObject("isCanConfirmReceive", isCanConfirmReceive);
        mav.addObject("isCanChannelConfirm", isCanChannelConfirm);
        mav.addObject("isCancelReceive", isCancelReceive);
        mav.addObject("isSearchLog", isSearchLog);
        mav.addObject("isUpdateReceiveTime", isUpdateReceiveTime);
        return mav;
    }
    
    /**
     * 查询
     * @param performanceBelongInfo
     * @param rows
     * @param page
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/searchPublicAccountInfo")
    @ResponseBody
    public String search(BasePublicAccountInfo basePublicAccountInfo, int rows,int page, HttpServletRequest request, HttpServletResponse response) {
        createLog(request, SysActionLogTypeEnum.查询, "对公账户还款", "查询对公账户还款信息");
        // 定义分页实例
        Pager pager = new Pager();
        pager.setRows(rows);
        pager.setPage(page);
        basePublicAccountInfo.setPager(pager);
        // 调用Service层查询对公还款信息 
        pager = basePublicAccountInfoService.findWithPg(basePublicAccountInfo);
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
    @RequestMapping("/batchImportPublicAccountInfo")
    @ResponseBody
    public void batchImportPublicAccountInfo(@RequestParam(value = "publicAccountfile") MultipartFile file,
            HttpServletRequest request, HttpServletResponse response) {
        createLog(request, SysActionLogTypeEnum.导入, "对公账户还款", "批量导入对公账户还款信息");
        // 创建导入数据模板（对公还款批量导入数据模板格式）
        ExcelTemplet excelTemplet = new ExcelTemplet().new PublicAccountInfoInputExcel();
        // 批量导入对公还款信息
        this.importAccountInfoCommon(file, excelTemplet, request, response);
    }
    
    /**
     * 校验和保存导入数据
     * @param sheetDataList
     */
    private void validateAndSaveData(List<Map<String, String>> sheetDataList) {
        String pattern_1 = "yyyy-MM-dd";
        String pattern_2 = "HH:mm:ss";
        for (Map<String, String> map : sheetDataList) {
            // 还款日期
            String repayDate = map.get("repayDate");
            // 还款时间
            String time = map.get("time");
            // 还款金额
            String amount = map.get("amount");
            try {
                Assert.notNullAndEmpty(time, "还款时间");
                Assert.notNullAndEmpty(amount, "还款金额");
                if(Strings.isNotEmpty(repayDate)){
                    Assert.notDate(repayDate, pattern_1, "还款日期格式错误，应类似2014-07-01");
                }else{
                    // 如果为空，则设置为当前日期
                    map.put("repayDate", Dates.getDateTime(new Date(), pattern_1));
                }
                // 还款时间，主要为了将银行类似于20.30.30的时间转换为20:30:30格式
                time = time.replaceAll("\\D", ":");
                Date tempDate = Assert.notDate(time, pattern_2, "还款时间格式错误，应类似20:30:30或20.30.30");
                map.put("time", Dates.getDateTime(tempDate, pattern_2));
                try {
                    new BigDecimal(amount);
                } catch (Exception e) {
                    throw new PlatformException(ResponseEnum.FULL_MSG,"还款金额类型不正确");
                }
                // 还款金额必须大于零的校验
                if(new BigDecimal(amount).doubleValue() <= 0){
                    throw new PlatformException(ResponseEnum.FULL_MSG,"还款金额必须大于零");
                }
                // 保存导入数据
                basePublicAccountInfoService.savePublicAccountImportData(map);
            } catch (PlatformException e) {
                map.put(ExcelTemplet.FEED_BACK_MSG, e.getMessage());
            } catch (Exception e) {
                map.put(ExcelTemplet.FEED_BACK_MSG, "批量导入对公还款信息异常");
            }
        }
    }
    
    /**
     * 车企贷批量导入
     * @param file
     * @param request
     * @param response
     * @return
     * @throws IOException 
     */
    @RequestMapping("/batchImportCarLoanAccountInfo")
    @ResponseBody
    public void batchImportCarLoanAccountInfo(@RequestParam(value = "carLoanAccountfile") MultipartFile file,
            HttpServletRequest request, HttpServletResponse response) {
        createLog(request, SysActionLogTypeEnum.导入, "对公账户还款", "批量导入车企贷对公账户还款信息");
        // 请求路径
        String url = sysParamDefineService.getSysParamValueCache("sysCar","cqdUrl");
        url += "/after/businessAccount/importBusinessData";
        // 对公还款信息批量导入车企贷系统
        this.importCarLoanAccountInfoCommon(file, url, PublicAccountSystemTypeEnum.车企贷.getValue(), request, response);
    }

    /**
     * 对公账户导出已认领结果
     * @param basePublicAccountInfo
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/exportPublicAccountReceiveInfo")
    public void exportPublicAccountReceiveInfo(BasePublicAccountInfo basePublicAccountInfo, HttpServletRequest request, HttpServletResponse response) {
        ResponseInfo responseInfo = null;
        basePublicAccountInfo.setStatus(PublicAccountStatusEnum.已认领.getValue());
        try {
            createLog(request, SysActionLogTypeEnum.导出, "对公账户还款", "导出对公账户还款已认领结果");
            List<Map<String, Object>> publicAccountReceiveInfoList = basePublicAccountInfoService.findPublicAccountReceiveInfo(basePublicAccountInfo);
            Assert.notCollectionsEmpty(publicAccountReceiveInfoList,"当前导出条件查询不到数据");
            // 更新状态为已导出
            List<Long> ids = new ArrayList<Long>();
            for(Map<String, Object> publicAccountReceiveInfo:publicAccountReceiveInfoList){
                ids.add(Long.valueOf(publicAccountReceiveInfo.get("id").toString()));
            }
            // 批量更新对公账户信息
            this.updateAccountInfo(ids);
            // 下载导出文件
            String fileName = "对公账户已认领导出文件" + Dates.getDateTime(new Date(), "yyyy-MM-ddHHmmss") + ".xlsx";
            String[] labels = new String[] { "姓名", "身份证", "借款类型", "首次还款日","还款金额", "还款方式","合同编号", "备注","内部流水号","摘要" };
            String[] fields = new String[] { "name", "idNum", "loanType","createTime", "amount", "repayMethod","contractNum", "memo", "repayNo","remark"};
            // 工作表名称
            String sheetName = "Export";
            // 创建工作簿
            Workbook workbook = ExcelExportUtil.createExcelByMap(fileName, labels,fields, publicAccountReceiveInfoList, sheetName);
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
            logger.error("对公已认领结果导出异常：", e);
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
     * 批量更新对公账户信息
     * @param ids
     */
    private void updateAccountInfo(List<Long> ids) {
        // 每一千条更新一批
        int batchSize = 1000;
        // 更新数据量
        int size = ids.size();
        // 更新次数
        int batchNum = size % batchSize == 0 ? size / batchSize : size / batchSize + 1;
        for (int i = 0; i < batchNum; i++) {
            if (i < batchNum - 1) {
                // 批量更新对公账户信息（更新状态为：已导出）
                this.updateSubAccountInfo(ids.subList(i * batchSize, (i + 1) * batchSize));
            } else {
                // 批量更新对公账户信息（更新状态为：已导出）
                this.updateSubAccountInfo(ids.subList(i * batchSize, size));
            }
        }
    }

    /**
     * 批量更新对公账户信息（更新状态为：已导出）
     * @param subIds
     */
    private void updateSubAccountInfo(List<Long> subIds) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("ids", subIds);
        params.put("status", "已导出");
        params.put("updateTime", new Date());
        basePublicAccountInfoService.updateAccountInfoForExport(params);
    }

    /**
     * 导出车企贷已认领结果
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/exportCarLoanAccountReceiveInfo")
    public void exportCarLoanAccountReceiveInfo(HttpServletRequest request, HttpServletResponse response) {
        ResponseInfo responseInfo = null;
        // 导出文件名
        String fileName = "对车企贷已认领导出文件"+ Dates.getDateTime(new Date(), "yyyy-MM-ddHHmmss") + "_car.xlsx";
        createLog(request, SysActionLogTypeEnum.导出, "对公账户还款", "导出车企贷已认领结果");
        try {
            // 请求路径
            String url = sysParamDefineService.getSysParamValueCache("sysCar","cqdUrl");
            url += "/after/businessAccount/exportReceiveData";
            // 调用远程方法、获取数据
            ResponseEntity<byte[]> responseEntity = this.remoteInvoke(null, url);
            if (Strings.isEmpty(responseEntity)) {
                throw new PlatformException(ResponseEnum.FULL_MSG, "调用车企贷导出数据接口失败");
            }
            List<String> resultList = responseEntity.getHeaders().get("result");
            if (CollectionUtils.isNotEmpty(resultList)) {
                String result = URLDecoder.decode(resultList.get(0), "utf-8");
                throw new PlatformException(ResponseEnum.FULL_MSG, result);
            }
            // 下载文件
            String downLoadError = this.downLoadFile(request, response,fileName, responseEntity);
            if (Strings.isNotEmpty(downLoadError)) {
                throw new PlatformException(ResponseEnum.FULL_MSG, downLoadError);
            }
            return;
        } catch (PlatformException e) {
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),e.getMessage());
        } catch (Exception e) {
            logger.error("对公导出车企贷已认领结果异常：", e);
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
     * 导出对公账户还款认领情况表
     * @param request
     * @param response
     * @param basePublicAccountInfo
     * @return
     */
    @RequestMapping("/exportPublicAccountRepayReceiveInfo")
    public void exportPublicAccountRepayReceiveInfo(BasePublicAccountInfo basePublicAccountInfo,
            HttpServletRequest request, HttpServletResponse response) {
        createLog(request, SysActionLogTypeEnum.导出, "对公账户还款", "导出对公账户还款认领情况表");
        String fileName = "对公账户还款情况导出文件" + Dates.getDateTime(new Date(), "yyyy-MM-ddHHmmss") + ".xlsx";
        if (Strings.isEmpty(basePublicAccountInfo.getRecTimeStart()) && Strings.isEmpty(basePublicAccountInfo.getRecTimeEnd())) {
            Date sysdate = Dates.parse(Dates.getDateTime(new Date(), "yyyy-MM-dd"), "yyyy-MM-dd");
            basePublicAccountInfo.setRecTimeStart(sysdate);
            basePublicAccountInfo.setRecTimeEnd(sysdate);
        }
        basePublicAccountInfo.setStatus(PublicAccountStatusEnum.已认领.getValue());
        ResponseInfo responseInfo = null;
        try {
            // 查询对公账户还款认领情况表
            List<Map<String, Object>> publicAccountRepayReceiveInfoList = basePublicAccountInfoService.findPublicAccountRepayReceiveInfo(basePublicAccountInfo);
            Assert.notCollectionsEmpty(publicAccountRepayReceiveInfoList,"当前导出条件查询不到数据");
            String[] labels = new String[] { "交易日期", "交易时间", "对方账号", "金额","对方单位", "认领操作人", "操作时间", "领取人姓名", "领取人证件号码","合同编号" };
            String[] fields = new String[] { "repayDate", "time","secondAccount", "amount", "secondCompany", "recOperator","recTime", "name", "idNum","contractNum" };
            // 工作表名称
            String sheetName = "Export";
            // 创建excel工作簿
            Workbook workbook = ExcelExportUtil.createExcelByMap(fileName, labels,fields, publicAccountRepayReceiveInfoList, sheetName);
            // 文件下载
            String downLoadError = this.downLoadFile(request, response,fileName, workbook);
            if (Strings.isNotEmpty(downLoadError)) {
                throw new PlatformException(ResponseEnum.FULL_MSG, downLoadError);
            }
            return;
        } catch (PlatformException e) {
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),e.getMessage());
        } catch (Exception e) {
            logger.error("导出对公账户还款认领情况表异常：", e);
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
     * 导出查询结果
     * @param request
     * @param response
     * @param basePublicAccountInfo
     * @return
     */
    @RequestMapping("/exportAccountSearchResult")
    public void exportAccountSearchResult(BasePublicAccountInfo basePublicAccountInfo,HttpServletRequest request, HttpServletResponse response){
        String fileName = "对公账户查询结果导出文件" + Dates.getDateTime(new Date(), "yyyy-MM-ddHHmmss") + ".xlsx";
        ResponseInfo responseInfo = null;
        createLog(request, SysActionLogTypeEnum.导出, "对公账户还款", "导出查询结果");
        try {
            // 查询对公账户信息
//            List<BasePublicAccountInfo> queryResultList = basePublicAccountInfoService.findQueryResultList(basePublicAccountInfo);
            List<Map<String, Object>> queryResultList = basePublicAccountInfoService.findQueryResultMapList(basePublicAccountInfo);
            Assert.notCollectionsEmpty(queryResultList,"当前导出条件查询不到数据");
            String[] labels = new String[] { "交易日期", "本方账号", "对方账号", "交易时间","借/贷", "金额", "凭证号",
                    "对方单位", "对方行号", "用途", "摘要", "附言", "状态", "借款人姓名", "身份证号", "合同编号", "首期还款日", "管理营业部","放款日期" };
            String[] fields = new String[] { "repayDate", "firstAccount","secondAccount", "time", "type", "amount", "voucherNo",
                    "secondCompany", "secondBank", "purpose", "remark","comments", "status", "name", "idNum", "contractNum", "firstRepayDate", "orgName","grantMoneyDate"};
            // 工作表名称
            String sheetName = "Export";
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
            logger.error("对公导出查询结果异常：", e);
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
     * 调用远程方法、获取数据
     * @param paramMap
     * @param url
     * @return
     */
    private ResponseEntity<byte[]> remoteInvoke(MultiValueMap<String, Object> paramMap, String url) {
        ResponseEntity<byte[]> responseEntity = null;
        try{
            // 调用远程方法
            responseEntity = restTemplate.postForEntity(url, paramMap, byte[].class);
        }catch(Exception e){
            logger.error("调用远程方法获取数据失败：", e);
        }
        return responseEntity;
    }
    
    /**
     * 下载文件
     * @param request
     * @param response
     * @param newFileName
     * @param responseEntity
     * @return
     */
    private String downLoadFile(HttpServletRequest request,
            HttpServletResponse response, String fileName,
            ResponseEntity<byte[]> responseEntity) {
        // 文件下载
        OutputStream out = null;
        try{
            //response.reset();
            response.setHeader("Content-disposition","attachment;filename="+ URLEncoder.encode(fileName, "UTF-8"));
            response.setContentType(UploadFileUtil.FILE_TYPE_EXCEL[0]);
            out=response.getOutputStream();
            out.write(responseEntity.getBody());
            out.flush();
        }catch(Exception e){
            logger.error("下载文件失败：", e);
            return "下载文件失败";
        }finally{
            if(null!=out){
                try {
                    out.close();
                } catch (IOException e) {
                    logger.error("下载文件失败：", e);
                    return "下载文件失败";
                }
            }
        }
        return null;
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
             //response.reset();
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
     * 领取校验
     * @param id
     * @return
     */
    @RequestMapping("/publicAccountReceiveInfo")
    @ResponseBody
    public String receiveCheck(Long id) {
        ResponseInfo responseInfo = null;
        User user = UserContext.getUser();
        try {
            // 校验对公还款领取状态，不能领取则返回提示信息
            String message = basePublicAccountInfoService.checkReceiveStatus();
            if (Strings.isNotEmpty(message)) {
                throw new PlatformException(ResponseEnum.FULL_MSG, message);
            }
            // 查询对公账户信息
            BasePublicAccountInfo publicAccountInfo = basePublicAccountInfoService.get(id);
            if (Strings.isEmpty(publicAccountInfo)) {
                throw new PlatformException(ResponseEnum.FULL_MSG,"查询不到对应的对公账户还款信息");
            }
            Date repayDate = publicAccountInfo.getRepayDate();
            if(!user.ifAnyRole(ROLE_NAME)){
            	 if(Dates.getAfterDays(repayDate,30).before(Dates.getCurrDate())){
                 	throw new PlatformException(ResponseEnum.FULL_MSG,"该笔存款超过30天，无法领取");
                 }
            }            
            responseInfo = new ResponseInfo(ResponseEnum.SYS_SUCCESS);
        } catch (PlatformException e) {
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),e.getMessage());
        } catch (Exception e) {
            logger.error("对公领取时间校验异常：", e);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
        }
        return toResponseJSON(responseInfo);
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
        createLog(request, SysActionLogTypeEnum.查询, "对公账户还款", "查询对公账户还款操作日志信息");
        // 定义分页实例
        Pager pager = new Pager();
        pager.setRows(rows);
        pager.setPage(page);
        pager.setSidx("ID");
        pager.setSort("ASC");
        loanLog.setPager(pager);
        // 调用Service层查询各门店还款信息 
        pager = loanLogService.findWithPg(loanLog);
        // 将数据对象转换成JSON字符串，返回给前台
        return toPGJSONWithCode(pager);
    }
    
    /**
     * 加载领取时间
     * @param id
     * @return
     */
    @RequestMapping("/loadReceiveTime")
    @ResponseBody
    public String loadReceiveTime() {
        AttachmentResponseInfo<Map<String,Object>> responseInfo = null;
        Map<String,Object> result = null;
        try {
            // 查询对公账户还款领取时间范围信息
            result = basePublicAccountInfoService.findBusinessAccountReceiveTime();
            responseInfo = new AttachmentResponseInfo<Map<String,Object>>(ResponseEnum.SYS_SUCCESS);
            responseInfo.setAttachment(result);
        } catch (PlatformException e) {
            responseInfo = new AttachmentResponseInfo<Map<String,Object>>(ResponseEnum.SYS_FAILD.getCode(),e.getMessage());
        } catch (Exception e) {
            logger.error("对公获取领取时间异常：", e);
            responseInfo = new AttachmentResponseInfo<Map<String,Object>>(ResponseEnum.SYS_FAILD);
        }
        return toResponseJSON(responseInfo);
    }
    
    /**
     * 修改对公账户还款领取时间
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
            // 更新对公账户还款领取时间范围信息
            basePublicAccountInfoService.updateBusinessAccountReceiveTime(params);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_SUCCESS);
        } catch (PlatformException e) {
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),e.getMessage());
        } catch (Exception e) {
            logger.error("修改对公账户还款领取时间异常：", e);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
        }
        return toResponseJSON(responseInfo);
    }
    
    /**
     * 对公还款信息批量导入（新模板）
     * @param file
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/newTemplateImportPublicAccountInfo")
    @ResponseBody
    public void newTemplateImportPublicAccountInfo(@RequestParam(value = "newPublicAccountfile") MultipartFile file,
            HttpServletRequest request, HttpServletResponse response) {
        createLog(request, SysActionLogTypeEnum.导入, "对公账户还款","批量导入对公账户还款信息（新模板）");
        // 创建导入数据模板、对公还款批量导入数据模板格式（新模板）
        ExcelTemplet excelTemplet = new ExcelTemplet().new NewPublicAccountInfoInputExcel();
        // 批量导入对公还款信息
        this.importAccountInfoCommon(file, excelTemplet, request, response);
    }

    /**
     * 对公还款信息批量导入通用方法
     * @param file
     * @param excelTemplet
     * @param request
     * @param response
     */
    private void importAccountInfoCommon(MultipartFile file, ExcelTemplet excelTemplet,
            HttpServletRequest request, HttpServletResponse response) {
        OutputStream outputStream = null;
        ResponseInfo responseInfo = null;
        // 导入文件名
        String fileName = file.getOriginalFilename();
        try {
            // 文件校验
            UploadFile uploadFile = new UploadFile();
            uploadFile.setFile(file);
            uploadFile.setFileType(UploadFileUtil.FILE_TYPE_EXCEL);
            uploadFile.setFileMaxSize(1024 * 1024 * 10);
            UploadFileUtil.valid(uploadFile);
            // 创建excel工作簿
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            // 文件数据转换为List集合
            List<Map<String, String>> sheetDataList = ExcelUtil.getExcelData(workbook, excelTemplet);
            if(CollectionUtils.isEmpty(sheetDataList)){
                throw new PlatformException(ResponseEnum.FULL_MSG,"导入文件不能为空！");
            }
            // 校验和保存导入数据
            this.validateAndSaveData(sheetDataList);
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
            logger.error("对公账户批量导入异常：", e);
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
     * 车企贷批量导入（新模板）
     * @param file
     * @param request
     * @param response
     * @return
     * @throws IOException 
     */
    @RequestMapping("/newTemplateImportCarLoanAccountInfo")
    @ResponseBody
    public void newTemplateImportCarLoanAccountInfo(@RequestParam(value = "newCarLoanAccountfile") MultipartFile file,
            HttpServletRequest request, HttpServletResponse response) {
        createLog(request, SysActionLogTypeEnum.导入, "对公账户还款", "批量导入车企贷对公账户还款信息（新模板）");
        // 请求路径
        String url = sysParamDefineService.getSysParamValueCache("sysCar","cqdUrl");
        url += "/after/businessAccount/importBusinessData";
        // 对公还款信息批量导入车企贷系统
        this.importCarLoanAccountInfoCommon(file, url, PublicAccountSystemTypeEnum.车企贷.getValue(), request, response);
    }
    
    /**
     * 证方批量导入（新模板）
     * @param file
     * @param request
     * @param response
     */
    @RequestMapping("/newTemplateImportBusinessAccountInfo")
    @ResponseBody
    public void newTemplateImportBusinessAccountInfo(@RequestParam(value = "newBusinessfile") MultipartFile file,
            HttpServletRequest request, HttpServletResponse response) {
        createLog(request, SysActionLogTypeEnum.导入, "对公账户还款", "证方批量导入（新模板）");
        // 请求路径
        String url = sysParamDefineService.getSysParamValueCache("sysCar","zfUrl");
        url += "/after/businessAccount/importBusinessData";
        // 对公还款信息批量导入车企贷系统
        this.importCarLoanAccountInfoCommon(file, url, PublicAccountSystemTypeEnum.证方.getValue(), request, response);
    }
    
    /**
     * 导出证方系统已认领结果
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/exportBusinessAccountReceiveInfo")
    public void exportBusinessAccountReceiveInfo(HttpServletRequest request, HttpServletResponse response) {
        ResponseInfo responseInfo = null;
        // 导出文件名
        String fileName = "证方已认领导出文件"+ Dates.getDateTime(new Date(), "yyyy-MM-ddHHmmss") + "_zf.xlsx";
        createLog(request, SysActionLogTypeEnum.导出, "对公账户还款", "导出证方系统已认领结果");
        try {
            // 请求路径
            String url = sysParamDefineService.getSysParamValueCache("sysCar","zfUrl");
            url += "/after/businessAccount/exportReceiveData";
            // 调用远程方法、获取数据
            ResponseEntity<byte[]> responseEntity = this.remoteInvoke(null, url);
            if (Strings.isEmpty(responseEntity)) {
                throw new PlatformException(ResponseEnum.FULL_MSG, "调用证方导出数据接口失败");
            }
            List<String> resultList = responseEntity.getHeaders().get("result");
            if (CollectionUtils.isNotEmpty(resultList)) {
                String result = URLDecoder.decode(resultList.get(0), "utf-8");
                throw new PlatformException(ResponseEnum.FULL_MSG, result);
            }
            // 下载文件
            String downLoadError = this.downLoadFile(request, response,fileName, responseEntity);
            if (Strings.isNotEmpty(downLoadError)) {
                throw new PlatformException(ResponseEnum.FULL_MSG, downLoadError);
            }
            return;
        } catch (PlatformException e) {
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),e.getMessage());
        } catch (Exception e) {
            logger.error("对公导出证方已认领结果异常：", e);
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
     * 对公还款信息车企贷批量导入通用方法
     * @param file
     * @param url
     * @param request
     * @param response
     */
    private void importCarLoanAccountInfoCommon(MultipartFile file, String url, String systemType, HttpServletRequest request, HttpServletResponse response) {
        OutputStream out = null;
        ResponseInfo responseInfo = null;
        String systemName = null;
        try {
            // 文件校验
            UploadFile uploadFile = new UploadFile();
            uploadFile.setFile(file);
            uploadFile.setFileType(UploadFileUtil.FILE_TYPE_EXCEL);
            uploadFile.setFileMaxSize(1024 * 1024 * 10);
            UploadFileUtil.valid(uploadFile);
            // 导入文件名
            String fileName = file.getOriginalFilename();
            // 下载文件名
            String newFileName = "";
            if(PublicAccountSystemTypeEnum.车企贷.getValue().equals(systemType)){
                newFileName = "车企贷批量导入返回结果" + Dates.getDateTime(new Date(), "yyyy-MM-ddHHmmss") + ".xlsx";
                systemName = PublicAccountSystemTypeEnum.车企贷.name();
            }else if(PublicAccountSystemTypeEnum.证方.getValue().equals(systemType)){
                newFileName = "证方批量导入返回结果" + Dates.getDateTime(new Date(), "yyyy-MM-ddHHmmss") + ".xlsx";
                systemName = PublicAccountSystemTypeEnum.证方.name();
            }
            // 请求参数
            MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<String, Object>();
            paramMap.add("fileName", fileName);
            paramMap.add("file\"; filename=\"" + fileName, file.getBytes());
            // 请求响应信息
            ResponseEntity<byte[]> responseEntity = null;
            // 调用远程方法、获取数据
            responseEntity = this.remoteInvoke(paramMap, url);
            if (Strings.isEmpty(responseEntity)) {
                throw new PlatformException(ResponseEnum.FULL_MSG,"调用" + systemName + "导入数据接口失败");
            }
            List<String> resultList = responseEntity.getHeaders().get("result");
            if (CollectionUtils.isNotEmpty(resultList)) {
                String result = URLDecoder.decode(resultList.get(0), "utf-8");
                throw new PlatformException(ResponseEnum.FULL_MSG, result);
            }
            // 下载文件
            response.setHeader("Content-disposition","attachment;filename="+ URLEncoder.encode(newFileName, "UTF-8"));
            response.setContentType(file.getContentType());
            out=response.getOutputStream();
            out.write(responseEntity.getBody());
            out.flush();
            return;
        } catch (PlatformException e) {
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),e.getMessage());
        } catch (Exception e) {
            logger.error(systemName + "批量导入异常：", e);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
        } finally {
            if (null != out) {
                try {
                    out.close();
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
}
