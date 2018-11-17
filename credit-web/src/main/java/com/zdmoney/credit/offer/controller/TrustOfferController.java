package com.zdmoney.credit.offer.controller;

import com.alibaba.fastjson.JSONObject;
import com.zdmoney.credit.common.constant.FundsSourcesTypeEnum;
import com.zdmoney.credit.common.constant.SysActionLogTypeEnum;
import com.zdmoney.credit.common.constant.TradeTypeEnum;
import com.zdmoney.credit.common.constant.repay.ReqManagerFileTypeEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.service.pub.IWorkDayInfoService;
import com.zdmoney.credit.common.util.*;
import com.zdmoney.credit.common.util.file.UploadFileUtil;
import com.zdmoney.credit.common.vo.AttachmentResponseInfo;
import com.zdmoney.credit.common.vo.ResponseInfo;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.job.RefundedOfMoneyConfirmationJob;
import com.zdmoney.credit.job.TrustOfferFlowAccountingJob;
import com.zdmoney.credit.trustOffer.service.pub.ITrustOfferFlowService;
import com.zdmoney.credit.trustOffer.vo.TrustOfferFlowVo;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by ym10094 on 2016/10/31.
 */
@Controller
@RequestMapping("/offer/trustOffer")
public class TrustOfferController  extends BaseController {   
    @Autowired
    private TrustOfferFlowAccountingJob trustOfferFlowAccountingJob;
    @Autowired
    private ITrustOfferFlowService trustOfferFlowService;
    @Autowired
    private IWorkDayInfoService workDayInfoService;
    @Autowired
    private RefundedOfMoneyConfirmationJob refundedOfMoneyConfirmationJob;
    /**
     * 回款确认书JSP地址
     */
    private static final String refundedOfMonetConfirmationBookJSPUrl ="/offer/refundedOfMoneyConfirmationBook";
    @ResponseBody
    @RequestMapping("/job/executeTrustOfferFlow")
    public String executeTrustOfferFlow(HttpServletRequest request, HttpServletResponse response){
        logger.info("开始执行信托分账");
        String beginDateStr=request.getParameter("beginDate");
        String endDateStr=request.getParameter("endDate");
        String beginDate = "";
        String endDate = "";
        if(Strings.isEmpty(beginDateStr) || Strings.isEmpty(beginDateStr)){
            return "The date is not right !";
        }
        beginDate = Dates.getDateTime(Dates.parse(beginDateStr,Dates.DEFAULT_DATE_FORMAT),Dates.DEFAULT_DATE_FORMAT);
        endDate = Dates.getDateTime(Dates.parse(endDateStr, Dates.DEFAULT_DATE_FORMAT), Dates.DEFAULT_DATE_FORMAT);
        trustOfferFlowAccountingJob.histroyRepayInfosExcute(beginDate,endDate);
        return "OK!";
    }
    @ResponseBody
    @RequestMapping("/job/executeHHKQRS")
    public String executeHHKQRS(HttpServletRequest request, HttpServletResponse response){
        logger.info("开始执行回款确认书");
        String tradeDateStr=request.getParameter("tradeDate");
        Date tradeDate = new Date();
        if(Strings.isEmpty(tradeDateStr)){
            return "The date is not right !";
        }
        tradeDate = Dates.parse(tradeDateStr,Dates.DEFAULT_DATE_FORMAT);
        refundedOfMoneyConfirmationJob.excuteUploadRefundedOfMoneyConfirmationBookFile(tradeDate);
        return "OK!";
    }
    /**
     * 初始化回款确认书（页面）
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/refundedOfMoneyConfirmationBookPage")
    public ModelAndView refundedOfMoneyConfirmationBookPage(HttpServletRequest request,HttpServletResponse response){
        ModelAndView model = new ModelAndView(refundedOfMonetConfirmationBookJSPUrl);
        createLog(request, SysActionLogTypeEnum.查询,"回款确认书","回款确认书初始化页面");
        String tradeDate = Dates.getDateTime(new Date(),Dates.DEFAULT_DATE_FORMAT);
        List<Map<String,String>> loanBelongs = this.getLoanBelongs(new FundsSourcesTypeEnum[]{FundsSourcesTypeEnum.渤海2, FundsSourcesTypeEnum.渤海信托,FundsSourcesTypeEnum.华瑞渤海});
        model.addObject("tradeDate",tradeDate);
        model.addObject("loanBelongs",loanBelongs);
        return model;
    }

    private List<Map<String,String>> getLoanBelongs(FundsSourcesTypeEnum [] fundsSourcesTypeEnums){
        List<Map<String,String>> list = new ArrayList<>();
        for (FundsSourcesTypeEnum fundsSourcesTypeEnum:fundsSourcesTypeEnums){
            Map<String,String> map = new HashMap<>();
            map.put("code",fundsSourcesTypeEnum.getCode());
            map.put("value",fundsSourcesTypeEnum.getValue());
            list.add(map);
        }
        return list;
    }

    /**
     * 回款确认页面查询
     * @param request
     * @param response
     * @param offerFlowVo
     * @param rows
     * @param page
     * @return
     */
    @ResponseBody
    @RequestMapping("/refundedOfMoneyConfirmationBook/search")
    public String searchRefundedOfMoneyConfirmationBook(HttpServletRequest request,HttpServletResponse response,TrustOfferFlowVo offerFlowVo,int rows, int page){
        Map<String,Object> params = new HashMap<>();
        Pager pager = new Pager();
        try {
            pager.setRows(rows);
            pager.setPage(page);
            Date tradeDate = offerFlowVo.getTradeDate();
            String loanBelongs = offerFlowVo.getLoanBelongs();
            //查询条件限制
            this.reFundedBookQueryConditionLimit(offerFlowVo);
            Date tradeBeginDate = trustOfferFlowService.getRefundedOfMoneyConfirmationTradeBeginDate(tradeDate,loanBelongs);
            params.put("tradeDate",Dates.getDateTime(tradeDate,Dates.DEFAULT_DATE_FORMAT));
            params.put("tradeBeginDate",Dates.getDateTime(tradeBeginDate, Dates.DEFAULT_DATE_FORMAT));
            params.put("tradeEndDate",Dates.getDateTime(tradeDate, Dates.DEFAULT_DATE_FORMAT));
            params.put("loanBelongs",loanBelongs);
            params.put("pager",pager);
            pager = trustOfferFlowService.queryRefundedOfMoneyConfirmationBookPage(params);
        }catch (PlatformException e){
            return  JSONObject.toJSONString(new AttachmentResponseInfo<Map<String, Object>>(ResponseEnum.FULL_MSG.getCode(), e.getMessage()));
        }catch (Exception e){
            e.printStackTrace();
            return  JSONObject.toJSONString(new AttachmentResponseInfo<Map<String, Object>>(ResponseEnum.FULL_MSG.getCode(), "查询异常"));
        }
        return toPGJSONWithCode(pager);
    }

    /**
     * 回款确认导出(Excel)
     * @param request
     * @param response
     * @param offerFlowVo
     */
    @ResponseBody
    @RequestMapping("/exportRefundedOfMoneyConfirmationBookExcel")
    public void exporRefundedOfMoneyConfirmationBooktExcel(HttpServletRequest request,HttpServletResponse response,TrustOfferFlowVo offerFlowVo){
        ResponseInfo responseInfo = null;
        try {
            createLog(request, SysActionLogTypeEnum.导出, "回款确认书", "导出回款确认书（Xls）文件");
            String fileName = trustOfferFlowService.getRefundedOfMoneyConfirmationBookExportFileName(offerFlowVo.getLoanBelongs(), offerFlowVo.getTradeDate());
            String hkqrsTemplatePath = trustOfferFlowService.getHkqrBookTemplatePath(offerFlowVo.getLoanBelongs(),".xls");
            Workbook workbook = trustOfferFlowService.createRefundedOfMoneyConfirmationBookWorkBook(hkqrsTemplatePath,offerFlowVo);
            if (workbook == null) {
                throw new PlatformException(ResponseEnum.FULL_MSG,"创建回款确认书workBook失败");
            }
            // 文件下载
            String downLoadError = this.downLoadFile(request, response,fileName+".xls", workbook);
            // 文件下载失败
            if (Strings.isNotEmpty(downLoadError)) {
                throw new PlatformException(ResponseEnum.FULL_MSG, downLoadError);
            }
            return;
        } catch (PlatformException e) {
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),e.getMessage());
        } catch (Exception e) {
            logger.error("导出回款确认书（Xls）异常：", e);
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
     * 分账明细表导出(Excel)
     * @param request
     * @param response
     * @param offerFlowVo
     */
    @ResponseBody
    @RequestMapping("/exportSubAccountDetailExcel")
    public void exportSubAccountDetailExcel(HttpServletRequest request,HttpServletResponse response,TrustOfferFlowVo offerFlowVo){
        ResponseInfo responseInfo = null;
        try {
            createLog(request, SysActionLogTypeEnum.导出, "分账明细表", "导出分账明细表（Xls）文件");
            String loanBelongs = offerFlowVo.getLoanBelongs();
            Date tradeDate = offerFlowVo.getTradeDate();
            String fileName = trustOfferFlowService.getReturnMoneyConfireFileName(ReqManagerFileTypeEnum.分账明细表,loanBelongs, tradeDate);
            Workbook workbook = trustOfferFlowService.getSubAccountDetailWorkbook(fileName,offerFlowVo);
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
            logger.error("导出分账明细表（Xls）异常：", e);
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
     * 其他暂收款报表导出(Excel)
     * @param request
     * @param response
     * @param offerFlowVo
     */
    @ResponseBody
    @RequestMapping("/exportTemporaryAmountExcel")
    public void exportTemporaryAmountExcel(HttpServletRequest request, HttpServletResponse response, TrustOfferFlowVo offerFlowVo) {
        ResponseInfo responseInfo = null;
        try {
            createLog(request, SysActionLogTypeEnum.导出, "回款确认书", "导出其他暂收款报表（Xls）文件");
            // 提交日期验证
            Date tradeDate = offerFlowVo.getTradeDate();
            // 创建workbook
//            String fileName = trustOfferFlowService.getTemporaryAmountExcelFileName(offerFlowVo.getLoanBelongs(), tradeDate);
            String fileName = trustOfferFlowService.getReturnMoneyConfireFileName(ReqManagerFileTypeEnum.暂其他收款,offerFlowVo.getLoanBelongs(), tradeDate);
            Workbook workbook = trustOfferFlowService.getTemporaryAmountWorkbook(fileName,offerFlowVo);
            // 文件下载
            String downLoadError = this.downLoadFile(request, response, fileName, workbook);
            // 文件下载失败
            if (Strings.isNotEmpty(downLoadError)) {
                throw new PlatformException(ResponseEnum.FULL_MSG, downLoadError);
            }
            return;
        } catch (PlatformException e) {
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("导出其他暂收款报表（Xls）异常：", e);
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
     * 回款确认导出（pdf）
     * @param request
     * @param response
     * @param offerFlowVo
     */
    @ResponseBody
    @RequestMapping("/exportRefundedOfMoneyConfirmationBookPdf")
    public void exportRefundedOfMoneyConfirmationBookPdf(HttpServletRequest request, HttpServletResponse response,TrustOfferFlowVo offerFlowVo) {
        ResponseInfo responseInfo = null;
        OutputStream os = null;
        ByteArrayOutputStream out = null;
//        FTPUtil ftpUtil = null;

        try {
            createLog(request, SysActionLogTypeEnum.导出, "申请书管理", "导出回款确认书（PDF）文件");
            String fileName = trustOfferFlowService.getRefundedOfMoneyConfirmationBookExportFileName(offerFlowVo.getLoanBelongs(), offerFlowVo.getTradeDate());
            fileName = fileName+".pdf";
            /*ftpUtil = connectBhxtFtpService.getFtpEsignatureConnect();
            if(ftpUtil == null){
                throw new PlatformException(ResponseEnum.FULL_MSG,"连接核心电子签章服务器失败！");
            }

            // 创建回款确认书pdf文件输出流对象
            ByteArrayOutputStream  byteArrayOutputStream = trustOfferFlowService.createRefundedOfMoneyConfirmationBookOutStream(reFundeOfMoneyCofirmationBookTemplatePdf,offerFlowVo);
            InputStream inputStream = requestManagementService.outStreamToInSteam(byteArrayOutputStream);

            String projectCode = trustOfferFlowService.getProjectCode(offerFlowVo.getLoanBelongs());
            out=(ByteArrayOutputStream)requestManagementService.dealEsignature(fileName,projectCode,ftpUtil,inputStream);*/
            out = (ByteArrayOutputStream)trustOfferFlowService.getRefundedOfMoneyConfirmationBookPdfFileStream(offerFlowVo,fileName);
            this.downLoadFile(request,response,fileName,out);
            if(null == out){
                throw new PlatformException(ResponseEnum.FULL_MSG, "创建回款确认书pdf文件失败！");
            }
            response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
            response.setContentType("application/octet-stream");
            os = response.getOutputStream();
            os.write(out.toByteArray());
            os.flush();
            return;
        } catch (PlatformException e) {
            e.printStackTrace();
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("导出回款确认书（PDF）异常：", e);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
        } finally {
            try {
/*                if (ftpUtil != null) {
                    ftpUtil.closeServer();
                }*/
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
    
    /**
     * 实分账明细表（页面）
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/subAccountDetailPage")
    public ModelAndView subAccountDetailPage(HttpServletRequest request,HttpServletResponse response){
        logger.info("实分账明细表页面。。。");
        ModelAndView model = new ModelAndView("/offer/subAccountDetailPage");
        String currDate = Dates.getDateTime(new Date(),Dates.DEFAULT_DATE_FORMAT);
        List<Map<String,String>> loanBelongs = this.getLoanBelongs(new FundsSourcesTypeEnum[]{FundsSourcesTypeEnum.渤海2,FundsSourcesTypeEnum.华瑞渤海});
        model.addObject("currDate", currDate);
        model.addObject("loanBelongs",loanBelongs);
        model.addObject("tradeTypes", new String[]{"快捷通","通联","银联","宝付","对公","银生宝"});
        return model;
    }
    
    /**
     * 放款管理-实分账明细表导出(Excel)
     * @param request
     * @param response
     */
    @ResponseBody
    @RequestMapping("/exportActualSubAccountDetailExcel")
    public void exportActualSubAccountDetailExcel(HttpServletRequest request,HttpServletResponse response){
        ResponseInfo responseInfo = null;
        try {
            createLog(request, SysActionLogTypeEnum.导出, "分账明细表", "导出放款管理-实分账明细表（Xls）文件");
            String loanBelongs = request.getParameter("loanBelongs");
            Date currDate = Dates.getCurrDate();
            String startDate = request.getParameter("tradeStartDate");
            String endDate = request.getParameter("tradeEndDate");
            String tradeType = request.getParameter("tradeType");
            if(Strings.isEmpty(startDate)){
                throw new PlatformException(ResponseEnum.FULL_MSG, "查询开始时间不得为空！");
            }
            if(Strings.isEmpty(endDate)){
                throw new PlatformException(ResponseEnum.FULL_MSG, "查询截止时间不得为空！");
            }
            Date tradeStartDate = Dates.getDateByString(startDate, Dates.DEFAULT_DATE_FORMAT);
            Date tradeEndDate = Dates.getDateByString(endDate, Dates.DEFAULT_DATE_FORMAT);
            if(tradeStartDate.after(tradeEndDate)){
                throw new PlatformException(ResponseEnum.FULL_MSG, "查询开始时间不得在结束时间之后！");
            }
            if(tradeEndDate.after(currDate)){
                throw new PlatformException(ResponseEnum.FULL_MSG, "查询时间超出当前日期！");
            }
            // 结束时间 查询条件中往后推一天
            endDate = Dates.getDateTime(Dates.getAfterDays(tradeEndDate, 1),Dates.DEFAULT_DATE_FORMAT);
            String tradeTypes[] = null;
            if(Strings.isNotEmpty(tradeType)){
                if("通联".equals(tradeType)){
                	tradeTypes = new String[]{TradeTypeEnum.通联代扣.getValue()};
                }else if("快捷通".equals(tradeType)){
                	tradeTypes = new String[]{TradeTypeEnum.快捷通.getValue()};
                }else if("宝付".equals(tradeType)){
                	tradeTypes = new String[]{TradeTypeEnum.宝付.getValue()};
                }else if("银生宝".equals(tradeType)){
                	tradeTypes = new String[]{TradeTypeEnum.银生宝.getValue()};
                }else if ("银联".equals(tradeType)){
                    tradeTypes = new String[]{TradeTypeEnum.上海银联代扣.getValue(),TradeTypeEnum.上海银联支付.getValue()};
                }else if ("对公".equals(tradeType)){
                    tradeTypes = new String[]{TradeTypeEnum.现金.getValue(),TradeTypeEnum.转账.getValue()};
                }
            }
             
            String fileName = trustOfferFlowService.getReturnMoneyConfireFileName(ReqManagerFileTypeEnum.实分账明细表,loanBelongs, currDate);
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("loanBelongs",loanBelongs);
            params.put("tradeStartDate", startDate);
            params.put("tradeEndDate", endDate);
            params.put("tradeType", tradeType);
            params.put("tradeTypes", tradeTypes);
            // 查询渤海信托、渤海2、华瑞渤海实分账明细数据
            List<Map<String, Object>> list = trustOfferFlowService.findSplitDetailList(params);
            if(CollectionUtils.isEmpty(list)){
                throw new PlatformException(ResponseEnum.FULL_MSG,"当前条件查询无数据！");
            }
            String labels[] = new String[]{"信托项目简码","借款编号","扣款方式","扣款类型","本期期数","实扣本金","实扣利息","利息减免","实扣罚息","罚息减免","实扣违约金","违约金减免","趸缴服务费退款","实扣手续费","手续费减免","实还担保费"
                    ,"担保费减免","实还服务费","服务费减免","扣款日期（实际）","实还其他费用一","费用一减免","实还其他费用二","费用二减免","实还其他费用三","费用三减免","三方支付流水/银行流水","趸缴服务费退款本金","趸缴服务费退款利息"};
            String fields[] = new String[]{"project_code","contract_num","trade_type","repayment_type","current_term","actual_principal","actual_interest","reduce_interest","actual_fine","reduce_fine","actual_penalty","reduce_penalty","refund_service_fee","actual_handling","reduce_handling","actual_guarantee"
                    ,"reduce_guarantee","actual_service","reduce_service","actual_trade_date","actual_fee1","reduce_fee1","actual_fee2","reduce_fee2","actual_fee3","reduce_fee3","third_pay_serial","give_back_principal","give_back_interest"};
            String sheetName = "分账明细表";
            Workbook workbook = ExcelExportUtil.createExcelByMap(fileName,labels, fields, list, sheetName);
            if (workbook == null) {
                throw new PlatformException(ResponseEnum.FULL_MSG,"创建分账明细表workBook失败");
            }
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
            logger.error("导出分账明细表（Xls）异常：", e);
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
     * 回款确认书查询条件限制
     * @param offerFlowVo
     */
    public void reFundedBookQueryConditionLimit(TrustOfferFlowVo offerFlowVo){
        Date tradeDate = offerFlowVo.getTradeDate();
        if(tradeDate.compareTo(Dates.getCurrDate()) != -1){
            throw  new PlatformException(ResponseEnum.FULL_MSG,"提交日期不能超过当天时间");
        }
        if (offerFlowVo.getLoanBelongs().equals(FundsSourcesTypeEnum.华瑞渤海.getValue())) {
            if (!workDayInfoService.isWorkDay(Dates.getCurrDate())) {
                throw new PlatformException(ResponseEnum.FULL_MSG, "非工作日不允许做查询操作");
            }
        }else {
            if (!workDayInfoService.isWorkDay(tradeDate)) {
                throw new PlatformException(ResponseEnum.FULL_MSG, "提交日期为非工作日不允许做查询操作");
            }
        }
    }
}

