package com.zdmoney.credit.payment.controller;

import com.alibaba.fastjson.JSON;
import com.zdmoney.credit.common.constant.FundsSourcesTypeEnum;
import com.zdmoney.credit.common.constant.SysActionLogTypeEnum;
import com.zdmoney.credit.common.constant.ThirdOfferStateEnum;
import com.zdmoney.credit.common.constant.offer.ThirdOfferConst;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.util.*;
import com.zdmoney.credit.common.util.excel.ExcelTemplet;
import com.zdmoney.credit.common.util.excel.ExcelUtil;
import com.zdmoney.credit.common.util.file.UploadFileUtil;
import com.zdmoney.credit.common.util.file.vo.UploadFile;
import com.zdmoney.credit.common.vo.ResponseInfo;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.loan.service.pub.ILoanBaseService;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import com.zdmoney.credit.payment.domain.*;
import com.zdmoney.credit.payment.service.pub.IThirdLineOfferBatchService;
import com.zdmoney.credit.payment.service.pub.IThirdLineOfferService;
import com.zdmoney.credit.payment.service.pub.IThirdLineOfferTransactionService;
import com.zdmoney.credit.payment.service.pub.IThirdUnderLinePaymentService;
import com.zdmoney.credit.repay.service.pub.IRequestManagementService;
import com.zdmoney.credit.system.service.pub.ISequencesService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.util.*;

/**
 * 第三方线下代付
 * @author zhangln
 */
@Controller
@RequestMapping("/payment/thirdUnderLine")
public class ThirdUnderLinePaymentController extends BaseController {

    @Autowired
    private IThirdUnderLinePaymentService thirdUnderLinePaymentService;

    @Autowired
    private IThirdLineOfferService haTwoOfferService;

    @Autowired
    private IThirdLineOfferBatchService haTwoOfferBatchService;

    @Autowired
    private IThirdLineOfferTransactionService haTwoOfferTransactionService;

    @Autowired
    private IVLoanInfoService vLoanInfoService;

    @Autowired
    private ILoanBaseService loanBaseService;

    @Autowired
    private ISysParamDefineService sysParamDefineService;
    
    @Autowired
    private ISequencesService sequencesService;

    @Autowired
    private IRequestManagementService requestManagementService;

    /**
     * 初始化第三方线下代付页面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/thirdUnderLinePaymentPage")
    public ModelAndView loanReturnDefault(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("thirdLine/thirdUnderLinePaymentPage");
        String sysdate = Dates.getDateTime(new Date(), "yyyyMMdd");
        mav.addObject("sysdate", sysdate);
        mav.addObject("states", ThirdOfferStateEnum.values());
        mav.addObject("fundsSources", new String[]{FundsSourcesTypeEnum.渤海信托.getValue(), FundsSourcesTypeEnum.渤海2.getValue(),FundsSourcesTypeEnum.华瑞渤海.getValue()});
        return mav;
    }

    /**
     * 查询第三方线下代付信息
     * @param rows
     * @param page
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping("/listThirdUnderLinePayment")
    public String listRepayTrailNomal(int rows, int page, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = new HashMap<String, Object>();
        // 客户姓名
        String name = request.getParameter("name");
        if (Strings.isNotEmpty(name)) {
            params.put("name", name);
        }
        // 身份证号码
        String idnum = request.getParameter("idnum");
        if (Strings.isNotEmpty(idnum)) {
            params.put("idnum", idnum);
        }
        // 报盘状态
        String state = request.getParameter("state");
        if (Strings.isNotEmpty(state)) {
            params.put("state", state);
        }
        // 起始报盘日期
        String offerTimeStart = request.getParameter("offerTimeStart");
        if (Strings.isNotEmpty(offerTimeStart)) {
            params.put("offerTimeStart", offerTimeStart);
        }
        // 截止报盘日期
        String offerTimeEnd = request.getParameter("offerTimeEnd");
        if (Strings.isNotEmpty(offerTimeEnd)) {
            params.put("offerTimeEnd", offerTimeEnd);
        }
        // 批次号
        String batchNum = request.getParameter("batchNum");
        if (Strings.isNotEmpty(batchNum)) {
            params.put("batchNum", batchNum);
        }
        // 合同来源
        String fundsSource = request.getParameter("fundsSource");
        if (Strings.isNotEmpty(fundsSource)) {
            params.put("fundsSource", fundsSource);
        }
        Pager pager = new Pager();
        pager.setPage(page);
        pager.setRows(rows);
        params.put("pager", pager);
        pager = haTwoOfferService.searchOfferInfoWithPg(params);
        // 将数据对象转换成JSON字符串，返回给前台
        return toPGJSONWithCode(pager);
    }

    /**
     * 生成报盘文件
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/createHaTwoOffer")
    @ResponseBody
    public String createHaTwoOffer(HttpServletRequest request, HttpServletResponse response) {
        ResponseInfo responseInfo = null;
        try {
            // 查询是否有未导出报盘文件的批次信息
            List<ThirdLineOfferBatch> haTwoOfferBatchs = haTwoOfferBatchService.findHaTwoOfferBatch();
            if(CollectionUtils.isNotEmpty(haTwoOfferBatchs)){
                // 不能生成报盘文件，因为有未导出报盘文件的批次
                throw new PlatformException(ResponseEnum.FULL_MSG, "不能生成报盘文件，因存在未导出的报盘文件！");
            }
            Map<String, Object> map = new HashMap<String, Object>();
            // 批次号
            String batchNum = request.getParameter("batchNum");
            if (Strings.isNotEmpty(batchNum)) {
                map.put("batchNum", batchNum);
            }
            // 合同来源
            String fundsSource = request.getParameter("fundsSource");
            if (Strings.isEmpty(fundsSource)) {
                throw new PlatformException(ResponseEnum.FULL_MSG, "生成报盘记录时必须选择一个合同来源！");
            }
            map.put("fundsSource", fundsSource);
            requestManagementService.previousBatchNumIsALLBackOffer(batchNum,fundsSource);
            // 生成报盘相关信息、包括报盘信息、报盘流水信息、批次信息
            thirdUnderLinePaymentService.createOffer(map);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_SUCCESS);
        } catch (PlatformException e) {
            logger.error(e.getMessage(), e);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            String errorMsg = e.getMessage().length() > 100 ? e.getMessage().substring(0, 100) : e.getMessage();
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(), errorMsg);
        }
        return toResponseJSON(responseInfo);
    }

    /**
     * 导出报盘文件
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/exportHaTwoOffer")
    public void exportHaTwoOffer(HttpServletRequest request,HttpServletResponse response) {
        ResponseInfo responseInfo = null;
        Workbook workbook = null;
        try {
            // 合同来源
            String fundsSource = request.getParameter("fundsSource");
            if (Strings.isEmpty(fundsSource)) {
                throw new PlatformException(ResponseEnum.FULL_MSG, "导出报盘文件时必须选择一个合同来源！");
            }
            // 查询待导出的批次信息
            ThirdLineOfferBatch searchVo = new ThirdLineOfferBatch();
            searchVo.setExportFile(ThirdOfferConst.EXPORT_FILE_F);
            List<ThirdLineOfferBatch> offerBatchList = haTwoOfferBatchService.findListByVo(searchVo);
            if(CollectionUtils.isEmpty(offerBatchList)){
                throw new PlatformException(ResponseEnum.FULL_MSG, "【" + fundsSource + "】合同来源下没有待导出的报盘信息！");
            }
            ThirdLineOfferBatch batchInfo = offerBatchList.get(0);
            Long batchId = batchInfo.getId();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("batchId", batchId);
            map.put("state", ThirdOfferStateEnum.未报盘.getValue());
            // 批次号
            String batchNum = request.getParameter("batchNum");
            if (Strings.isNotEmpty(batchNum)) {
                map.put("batchNum", batchNum);
            }
            map.put("fundsSource", fundsSource);
            // 根据批次号相关条件查询报盘流水表信息
            List<ThirdLineOfferTransaction> haTwoOfferTransactions = haTwoOfferTransactionService.findOfferTransactionListByMap(map);
            if(CollectionUtils.isEmpty(haTwoOfferTransactions)){
                String message = "【" + fundsSource + "】合同来源下没有待导出的报盘流水信息！";
                if (Strings.isNotEmpty(batchNum)) {
                    message = message + "债权批次号："+ batchNum;
                }
                throw new PlatformException(ResponseEnum.FULL_MSG, message);
            }
            // 导出总金额
            BigDecimal totalAmount = new BigDecimal(0);
            // 导出总件数
            int totalCount = haTwoOfferTransactions.size();
            // 报盘信息
            List<ThirdLineOffer> thirdLineOffers = new ArrayList<ThirdLineOffer>();
            for (int i=0; i< totalCount; i++) {
                ThirdLineOfferTransaction transaction = haTwoOfferTransactions.get(i);
                Long offerId = transaction.getTwoOfferId();
                // 更新报盘表的状态为 已报盘
                haTwoOfferService.updateState(offerId);
                // 更新报盘流水表状态为已报盘
                haTwoOfferTransactionService.updateState(transaction.getId());
                // 查询报盘信息
                ThirdLineOffer thirdLineOffer = haTwoOfferService.findById(offerId);
                // 金额以分为最小单位
                thirdLineOffer.setAmount(thirdLineOffer.getAmount().multiply(new BigDecimal(100)));
                // 金额累加、最小单位是分
                totalAmount = totalAmount.add(thirdLineOffer.getAmount());
                // 设置记录号
                thirdLineOffer.setRecordNumber(this.updateRecordNumber((i+1)));
                // 备注设置流水号
                thirdLineOffer.setRemark(transaction.getFlowNumber());
                thirdLineOffers.add(thirdLineOffer);
            }
            StringBuilder buildFileName = new StringBuilder();
            buildFileName.append(batchInfo.getMerchantId());
            buildFileName.append("_");
            buildFileName.append(ThirdOfferConst.TRADE_MARK_F);
//            buildFileName.append("_");
            buildFileName.append(ThirdOfferConst.VERSION);
            buildFileName.append(Dates.getDateTime("yyyyMMdd"));
            buildFileName.append("_");
            buildFileName.append(batchInfo.getDayBatchNumber());
            buildFileName.append(".xls");
            // 下载文件名称
            String fileName = buildFileName.toString();
            buildFileName = null;
            // 获取当日最大的日批次号
            String batchNumber = thirdUnderLinePaymentService.createBatchNumber();
            batchInfo.setDayBatchNumber(batchNumber);
            // 如果批次下所有的报盘数据都已经导出，则更新导出状态
            if (!this.isExistNoExportOffer(batchId)) {
                batchInfo.setExportFile("t");
            }
            // 更新批次为已导出报盘
            haTwoOfferBatchService.update(batchInfo);
            // 工作表名称
            String sheetName = "报盘导出信息";
            // 创建workbook对象
            workbook = ExcelExportUtil.createWorkbook(fileName, sheetName);
            // 编辑报盘文件头部汇总信息
            List<ThirdLineOfferBatch> batchList = this.editBatchList(totalCount,totalAmount,batchInfo.getMerchantId());
            // 往excel文件写数据（从第一行写，不写标题头部）
            ExcelExportUtil.createExcelByVo(workbook, this.getSummaryLabels(), this.getSummaryFields(), batchList, false, 1);
            // 往excel文件写数据（从第二行写，写标题头部）
            ExcelExportUtil.createExcelByVo(workbook, this.getLabels(), this.getFields(), thirdLineOffers, true, 2);
            // 文件下载
            String downLoadError = this.downLoadFile(request, response, fileName, workbook);
            // 文件下载失败
            if (Strings.isNotEmpty(downLoadError)) {
                throw new PlatformException(ResponseEnum.FULL_MSG, downLoadError);
            }
            return;
        } catch (PlatformException e) {
            logger.error("导出报盘文件失败：", e);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("导出报盘文件失败：", e);
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
     * 编辑报盘文件头部汇总信息
     * @param totalCount
     * @param totalAmount
     * @param merchantId
     * @return
     */
    private List<ThirdLineOfferBatch> editBatchList(int totalCount, BigDecimal totalAmount, String merchantId) {
        List<ThirdLineOfferBatch> offerBatchList = new ArrayList<ThirdLineOfferBatch>();
        ThirdLineOfferBatch offerBatch = new ThirdLineOfferBatch();
        offerBatch.setTradeMark(ThirdOfferConst.TRADE_MARK_F);
        offerBatch.setMerchantId(merchantId);
        offerBatch.setSubmitDate(Dates.getDateTime("yyyyMMdd"));
        offerBatch.setRecordsTotal(totalCount);
        offerBatch.setAmountTotal(totalAmount);
        offerBatch.setBusinessType(ThirdOfferConst.BUSINESS_TYPE);
        offerBatchList.add(offerBatch);
        return offerBatchList;
    }

    /**
     * 判断指定批次下是否存在未导出的报盘数据
     * @param batchId
     * @return
     */
    private boolean isExistNoExportOffer(Long batchId){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("batchId", batchId);
        map.put("state", ThirdOfferStateEnum.未报盘.getValue());
        // 根据批次号相关条件查询报盘流水表信息
        List<ThirdLineOfferTransaction> haTwoOfferTransactions = haTwoOfferTransactionService.findOfferTransactionListByMap(map);
        if(CollectionUtils.isNotEmpty(haTwoOfferTransactions)){
            return true;
        }
        return false;
    }

    /**
     * 记录序号  同一债权，从000001开始累加
     * @param index
     * @return
     */
    private String updateRecordNumber(int index){
        return StringUtils.leftPad(String.valueOf(index), 6, "0");
    }
    
    /**
     * 导入回盘文件
     * @param file
     * @param request
     * @param response
     */
    @RequestMapping("/importHaTwoOffer")
    @ResponseBody
    private void importHaTwoOffer(@RequestParam(value = "noHaTwoOfferFile") MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        ResponseInfo responseInfo = null;
        // 导入文件名
        String fileName = file.getOriginalFilename();
        try {
            // 文件校验
            UploadFile uploadFile = new UploadFile();
            uploadFile.setFile(file);
            uploadFile.setFileType(UploadFileUtil.FILE_TYPE_EXCEL);
            uploadFile.setFileMaxSize(UploadFileUtil.FILE_SIZE_DEFAULT_MAX);
            UploadFileUtil.valid(uploadFile);
            // 创建excel工作簿
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            // 创建导入数据模板
            ExcelTemplet excelTemplet = new ExcelTemplet().new importHaTwoOfferExcel();
            // 文件数据转换为List集合
            List<Map<String, String>> sheetDataList = ExcelUtil.getExcelData(workbook, excelTemplet);
            // 校验和更新导入数据
            thirdUnderLinePaymentService.updateHaTwoOffer(sheetDataList);
            // 在excel文件的每行末尾追加单元格提示信息
            ExcelUtil.addResultToWorkBook(workbook, sheetDataList, excelTemplet);
            // 文件下载
            String downLoadError = this.downLoadFile(request, response, fileName, workbook);
            // 文件下载失败
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
     * excel文件的表头显示名
     * @return
     */
    private String[] getSummaryLabels() {
        String[] labels = { "交易标志", "商户ID", "提交日期", "总记录数", "总金额", "业务类型"};
        return labels;
    }

    /**
     * excel文件的数据字段名
     * @return
     */
    private String[] getSummaryFields() {
        String[] fields = { "tradeMark", "merchantId", "submitDate", "recordsTotal", "amountTotal", "businessType"};
        return fields;
    }

    /**
     * excel文件的表头显示名
     * @return
     */
    private String[] getLabels() {
        String[] labels = { "记录序号", "通联支付用户编号", "银行代码", "帐号类型", "账号", "户名",
                "开户行所在省", "开户行所在市", "开户行名称", "账户类型", "金额", "货币类型", "协议号",
                "协议用户编号", "开户证件类型", "证件号", "手机号/小灵通", "自定义用户号", "备注", "反馈码",
                "原因" };
        return labels;
    }

    /**
     * excel文件的数据字段名
     * @return
     */
    private String[] getFields() {
        String[] fields = { "recordNumber", "tlPaymentNumber", "bankCode",
                "accountNumberType", "accountNumber", "accountName",
                "bankProvince", "bankCity", "bankName", "accountType",
                "amount", "currencyType", "protocolNumber",
                "protocolUserNumber", "certificateType", "licenseNumber",
                "telNumber", "customUserNumber", "remark", "feedbackCode",
                "reason" };
        return fields;
    }

    /**
     * 查询放款流水
     * @param offerId
     * @param rows
     * @param page
     * @return
     */
    @ResponseBody
    @RequestMapping("/listLoanOrder")
    public String listLoanOrder(String offerId, int rows, int page) {
        logger.info("------进入查询客户放款流水---------");
        ResponseInfo responseInfo = null;
        if (Strings.isEmpty(offerId)) {
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),"参数不能为空");
            return toResponseJSON(responseInfo);
        }
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("offerId", offerId);
            Pager pager = new Pager();
            pager.setPage(page);
            pager.setRows(rows);
            pager.setSidx("OFFER_TIME");
            pager.setSort("desc");
            params.put("pager", pager);
            pager = vLoanInfoService.listLoanOrder(params);
            // 将数据对象转换成JSON字符串，返回给前台
            return toPGJSONWithCode(pager);
        } catch (Exception e) {
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(), "查询放款流水出错");
            return toResponseJSON(responseInfo);
        }
    }

    /**
     * 附件查看
     * @param loanId
     */
    @ResponseBody
    @RequestMapping("/AttachLoaninfo")
    public String AttachLoaninfo(String loanId) {
        logger.info("------进入附件查看---------");
        ResponseInfo responseInfo = null;
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            String strUrl = sysParamDefineService.getSysParamValueCache("system.thirdparty", "system.credit.cs.url");
            if (Strings.isEmpty(strUrl)) {
                responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(), "获取参数失败");
                return toResponseJSON(responseInfo);
            }
            String resultUrL = strUrl + "/video/jumpIndex/" + loanId;
            resultMap.put("resultUrL", resultUrL);
            return JSONObject.valueToString(resultMap);
        } catch (Exception e) {
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),"查询放款流水出错");
            return toResponseJSON(responseInfo);
        }
    }

    @RequestMapping("/exportHaTwoGuoMinXinTouOffer")
    public void exportHaTwoGuoMinXinTouOffer(HttpServletRequest request,HttpServletResponse response){
        ResponseInfo responseInfo = null;
        Workbook workbook=null;
        StringBuilder fileName=null;
        OfferExportBatch offerExportBatch=null;
        boolean isCurrDateFirst=true;
        String seq_no="00001";
        BigInteger totalNumber=new BigInteger("0");
        try {
            createLog(request, SysActionLogTypeEnum.导出, "第三方线下代付", "导出国民信托");
          String  merchantId =sysParamDefineService.getSysParamValueCache("merchantid", "guominxintou.export.merchantid");
            if(StringUtils.isBlank(merchantId)){
                throw new PlatformException(ResponseEnum.FULL_MSG, "没有指定具体的商户号！");
            }

            List<OfferExportInfo> offerExportInfoList=haTwoOfferTransactionService.getGuoMinXinTuoThirdLine();
            logger.debug("获取国民信托详细信息："+JSON.toJSONString(offerExportInfoList));
            BigDecimal totalAmount=haTwoOfferTransactionService.getGuoMinXinTuoThirdLineTotalAmount();
            logger.debug("获取国民信详细总金额："+JSON.toJSONString(totalAmount));
            if(CollectionUtils.isEmpty(offerExportInfoList)){
                throw new PlatformException(ResponseEnum.FULL_MSG, "没有待导出的信息！");
            }
            logger.debug("获取国民信详细总记录数："+JSON.toJSONString(totalAmount));
            totalNumber=new BigInteger( String.valueOf(offerExportInfoList.size()));

            offerExportBatch=new OfferExportBatch();
            offerExportBatch.setExportTime(Dates.getCurrDate());
            offerExportBatch=haTwoOfferTransactionService.findOfferExportBatch(offerExportBatch);
            logger.debug("获取国民信托批次信息："+ JSON.toJSONString(offerExportBatch));
            if(offerExportBatch != null){
                //今天已经导出数据
                isCurrDateFirst=false;
                seq_no=offerExportBatch.getSeqNo();
                seq_no=Strings.aa(String.valueOf(Integer.parseInt(seq_no)),5);
            }else {
                offerExportBatch=new OfferExportBatch();
            }

            List<OfferExportBatch> batchList = new ArrayList<OfferExportBatch>();
                offerExportBatch.setCurrentDay(Dates.getDateTime("yyyyMMdd"));
                offerExportBatch.setBusinessType(ThirdOfferConst.BUSINESS_TYPE);
                offerExportBatch.setTotalAmount(totalAmount.multiply(new BigDecimal(100)));
                offerExportBatch.setTotalNumber(totalNumber);
                offerExportBatch.setMerchantId(merchantId);
                offerExportBatch.setTradeMark("F");
                batchList.add(offerExportBatch);
            // 下载文件名称 200290000013098_F0220160607_00001
            fileName=new StringBuilder("200290000013098_F02");
            fileName.append(Dates.getDateTime("yyyyMMdd")+"_");
            fileName.append(seq_no);
            fileName.append(".xlsx");
            // 工作表名称
            String sheetName = "信息";
            // 创建workbook对象
            workbook = ExcelExportUtil.createWorkbook(fileName.toString(), sheetName);
            // 往excel文件写数据（从第一行写，不写标题头部）
            ExcelExportUtil.createExcelByVo(workbook, this.getOfferExportSummaryLabels(), this.getOfferExportSummaryFields(), batchList, false, 1);
            // 往excel文件写数据（从第二行写，写标题头部）
            ExcelExportUtil.createExcelByVo(workbook, this.getOfferExportLabels(), this.getOfferExportFields(),offerExportInfoList , true, 2);
            // 文件下载
            String downLoadError = this.downLoadFile(request,response,fileName.toString(),workbook);
            // 文件下载失败
            if (Strings.isNotEmpty(downLoadError)) {
                throw new PlatformException(ResponseEnum.FULL_MSG, downLoadError);
            }
            //if(isCurrDateFirst){
                offerExportBatch.setId(sequencesService.getSequences(SequencesEnum.OFFER_EXPORT_BATCH));
                offerExportBatch.setExportTime(Dates.getCurrDate());
                offerExportBatch.setSeqNo(seq_no);
                haTwoOfferTransactionService.insertOfferExportBatch(offerExportBatch);
           /* }else{
                offerExportBatch.setSeqNo(seq_no);
                haTwoOfferTransactionService.insertOfferExportBatch(offerExportBatch);
            }*/
            return;
        }catch (PlatformException e) {
            logger.error("国民信托导出报盘文件失败：", e);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(), e.getMessage());
        }  catch (Exception e) {
            e.printStackTrace();
            logger.error("第三方线下代付导出异常：", e);
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
                if (null != workbook) {
                    workbook.close();
                }
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
     * excel文件的表头显示名
     * @return
     */
    private String[] getOfferExportSummaryLabels() {
        String[] labels = { "交易标志", "商户ID", "导出日期", "总记录数", "总金额", "业务类型"};
        return labels;
    }

    /**
     * excel文件的数据字段名
     * @return
     */
    private String[] getOfferExportSummaryFields() {
        String[] fields = { "tradeMark", "merchantId", "currentDay", "totalNumber", "totalAmount", "businessType"};
        return fields;
    }

    /**
     * excel文件的表头显示名
     * @return
     */
    private String[] getOfferExportLabels() {
        String[] labels = { "记录序号", "通联支付用户编号", "银行代码", "帐号类型", "账号", "账户名",
                "开户行所在省", "开户行所在市", "开户行名称", "账户类型", "金额", "货币类型", "协议号",
                "协议用户编号", "开户证件类型", "证件号", "手机号/小灵通", "自定义用户号", "备注", "反馈码",
                "原因" };
        return labels;
    }

    /**
     * excel文件的数据字段名
     * @return
     */
    private String[] getOfferExportFields() {
        String[] fields = { "recSeqId", "tlPaymentNumber", "bankCode",
                "accType", "account", "accountName",
                "bankProvince", "bankCity", "bankName", "accountType",
                "grantMoney", "currencyType", "protocolNumber",
                "protocolUserNumber", "cardType", "cardId",
                "telNumber", "customUserNumber", "remark", "feedbackCode",
                "reason" };
        return fields;
    }
}
