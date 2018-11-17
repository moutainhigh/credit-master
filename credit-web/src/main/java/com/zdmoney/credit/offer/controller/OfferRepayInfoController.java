package com.zdmoney.credit.offer.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.zdmoney.credit.common.constant.FundsSourcesTypeEnum;
import com.zdmoney.credit.common.constant.LoanStateEnum;
import com.zdmoney.credit.common.constant.LoanTypeEnum;
import com.zdmoney.credit.common.constant.SysActionLogTypeEnum;
import com.zdmoney.credit.common.constant.TradeTypeEnum;
import com.zdmoney.credit.common.constant.wm.WMDebitDeductStateEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.util.Assert;
import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.util.excel.ExcelTemplet;
import com.zdmoney.credit.common.util.excel.ExcelUtil;
import com.zdmoney.credit.common.util.file.UploadFileUtil;
import com.zdmoney.credit.common.util.file.vo.UploadFile;
import com.zdmoney.credit.common.vo.AttachmentResponseInfo;
import com.zdmoney.credit.common.vo.ResponseInfo;
import com.zdmoney.credit.debit.domain.DebitOfflineOfferInfo;
import com.zdmoney.credit.debit.service.pub.IDebitOfflineOfferInfoService;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.framework.vo.wm3.entity.PaidInMoneyListEntity;
import com.zdmoney.credit.loan.domain.LoanRepaymentDetail;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.IAfterLoanService;
import com.zdmoney.credit.loan.service.pub.ILoanSpecialRepaymentService;
import com.zdmoney.credit.loan.service.pub.ILoanTransferInfoService;
import com.zdmoney.credit.loan.service.pub.ISpecialRepaymentApplyService;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import com.zdmoney.credit.offer.domain.OfferRepayInfo;
import com.zdmoney.credit.offer.service.pub.IOfferRepayInfoService;
import com.zdmoney.credit.offer.vo.RepaymentInputVo;
import com.zdmoney.credit.operation.domain.BasePublicAccountInfoWm;
import com.zdmoney.credit.operation.service.pub.IBasePublicAccountInfoWmService;
import com.zdmoney.credit.system.domain.ComMessageIn;
import com.zdmoney.credit.system.domain.PersonInfo;
import com.zdmoney.credit.system.service.pub.IComMessageInService;
import com.zdmoney.credit.system.service.pub.IPersonInfoService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;

/**
 * 账面金额Controller
 * 
 * @author Ivan
 *
 */
@Controller
@RequestMapping("/offer/repayInfo")
public class OfferRepayInfoController extends BaseController {

    @Autowired
    @Qualifier("afterLoanServiceImpl")
    IAfterLoanService afterLoanServiceImpl;

    @Autowired
    @Qualifier("loanSpecialRepaymentServiceImpl")
    ILoanSpecialRepaymentService loanSpecialRepaymentServiceImpl;

    @Autowired
    @Qualifier("personInfoServiceImpl")
    IPersonInfoService personInfoServiceImpl;

    @Autowired
    @Qualifier("VLoanInfoServiceImpl")
    IVLoanInfoService vLoanInfoServiceImpl;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    ISysParamDefineService sysParamDefineServiceImpl;

    @Autowired
    @Qualifier("comMessageInServiceImpl")
    IComMessageInService comMessageInServiceImpl;

    @Autowired
    @Qualifier("offerRepayInfoServiceImpl")
    IOfferRepayInfoService offerRepayInfoServiceImpl;
    
    @Autowired
    IBasePublicAccountInfoWmService basePublicAccountInfoWmService;

    @Autowired
    ISpecialRepaymentApplyService specialRepaymentApplyService;

    @Autowired
    IDebitOfflineOfferInfoService debitOfflineOfferInfoService;
    @Autowired
	ILoanTransferInfoService loanTransferInfoService;
    /** 还款录入Excel录入地址（车企贷） **/
    private final static String CAR_IMPORTREPAYDATA_URL = "/after/repayEntry/importRepayData";
    
    /** 还款录入Excel录入地址（证方） **/
    private final static String ZF_IMPORTREPAYDATA_URL = "/after/repayEntry/importRepayData";

    /**
     * 还款录入（正常还款、一次性提前还款）
     * 
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/repaymentInput")
    @ResponseBody
    public String repaymentInput(HttpServletRequest request, HttpServletResponse response) throws IOException {
        AttachmentResponseInfo<RepaymentInputVo> attachmentResponseInfo = null;
        try {
            createLog(request, SysActionLogTypeEnum.更新, "还款录入", "单客户还款录入操作");
            /** 登陆者信息 **/
            User user = UserContext.getUser();
            /** 还款金额（前台录入） **/
            BigDecimal amount = null;
            String amountStr = request.getParameter("amount");
            if (Strings.isNotEmpty(amountStr)) {
                amount = new BigDecimal(amountStr);
            }
            /** 借款ID **/
            Long loanId = Strings.convertValue(request.getParameter("id"), Long.class);
            /** 还款备注 **/
            String memo = Strings.convertValue(request.getParameter("memo"), String.class);
            /** 交易类型 **/
            String tradeType = Strings.convertValue(request.getParameter("tradeType"), String.class);
            Date tradeDate = Dates.getCurrDate();
            Assert.notNull(loanId, "借款编号");
            Assert.notNullAndEmpty(tradeType, "交易类型");
            Assert.notNull(amount, "还款金额");
            Assert.validEnum(TradeTypeEnum.class, tradeType, "交易类型");

            /**检查该债权是否进行过转让**/
			boolean flag = loanTransferInfoService.isLoanTransfer(null,loanId);
			if(!flag){
				attachmentResponseInfo = new AttachmentResponseInfo<RepaymentInputVo>(ResponseEnum.FULL_MSG,Strings.errorMsg);
				return toResponseJSON(attachmentResponseInfo);
			}
            
            /** 还款录入Vo **/
            RepaymentInputVo repaymentInputVo = new RepaymentInputVo();
            repaymentInputVo.setLoanId(loanId);
            repaymentInputVo.setAmount(amount);
            repaymentInputVo.setMemo(memo);
            repaymentInputVo.setOrgan(user.getOrgCode());
            repaymentInputVo.setTeller(user.getUserCode());
            repaymentInputVo.setTradeDate(tradeDate);
            repaymentInputVo.setTradeType(tradeType);
//            /** 调用核心层接口（还款） **/
//            offerRepayInfoServiceImpl.repaymentInputCore(repaymentInputVo);
            offerRepayInfoServiceImpl.dealRepaymentInput(repaymentInputVo);
            attachmentResponseInfo = new AttachmentResponseInfo<RepaymentInputVo>(ResponseEnum.SYS_SUCCESS.getCode(),ResponseEnum.SYS_SUCCESS.getDesc(), "");
            attachmentResponseInfo.setAttachment(repaymentInputVo);
            
            
        } catch (PlatformException ex) {
            logger.error(ex, ex);
            ResponseEnum responseCode = ex.getResponseCode();
            if (responseCode != null) {
                attachmentResponseInfo = new AttachmentResponseInfo<RepaymentInputVo>(responseCode.getCode(),
                        ex.getMessage(), "");
            } else {
                attachmentResponseInfo = new AttachmentResponseInfo<RepaymentInputVo>(ResponseEnum.FULL_MSG,
                        ex.getMessage());
            }
        } catch (Exception ex) {
            /** 系统忙 **/
            logger.error(ex, ex);
            attachmentResponseInfo = new AttachmentResponseInfo<RepaymentInputVo>(ResponseEnum.SYS_FAILD.getCode(),
                    ResponseEnum.SYS_FAILD.getDesc(), "");
        }
        return toResponseJSON(attachmentResponseInfo);
    }

    /**
     * 还款录入 - Excel文件导入（正常还款、一次性提前还款）
     * 
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/importRepayInfoFile")
    @ResponseBody
    public void importRepayInfoFile(@RequestParam(value = "uploadFile", required = false) MultipartFile file,
            HttpServletRequest request, HttpServletResponse response) throws IOException {
        ResponseInfo responseInfo = null;
        try {
            createLog(request, SysActionLogTypeEnum.导入, "还款录入", "批量导入还款录入操作");
            /** 登陆者信息 **/
            User user = UserContext.getUser();

            UploadFile uploadFile = new UploadFile();
            uploadFile.setFile(file);
            uploadFile.setFileType(UploadFileUtil.FILE_TYPE_EXCEL);
            uploadFile.setFileMaxSize(1024 * 1024 * 5);

            UploadFileUtil.valid(uploadFile);
            String fileName = file.getOriginalFilename();
            String contentType = file.getContentType();

            /** 跟据文件名判断是否已导入 **/
            List<ComMessageIn> checkFile = comMessageInServiceImpl.findByMessageTypeAndFileName("RepaymentInput",
                    fileName);
            Assert.isCollectionsEmpty(checkFile, ResponseEnum.FULL_MSG, "当前导入文件名:[" + fileName + "]已经存在!");

            String isCarCredit = fileName.substring(fileName.lastIndexOf("_") + 1, fileName.lastIndexOf("."));
            if (!isCarCredit.equalsIgnoreCase("car")) {
                Workbook workBook = WorkbookFactory.create(file.getInputStream());

                ExcelTemplet excelTemplet = new ExcelTemplet().new RepaymentInputExcel();
                List<Map<String, String>> result = ExcelUtil.getExcelData(workBook, excelTemplet);

                Assert.notCollectionsEmpty(result, ResponseEnum.FILE_EMPTY_FILE, "");

                comMessageInServiceImpl.createMsgIn("RepaymentInput", user.getName(), fileName, "还款录入批量导入");
                
                for (Map<String, String> map : result) {
                    // 处理还款录入 Excel文件数据
                    OfferRepayInfo offerRepayInfo = this.importRepayInfoFileParseItem(map);
                    // 封装收集接口参数信息
                    //this.packageParams(map, offerRepayInfo, paidInMoneyList);
                    this.createDebitOfflineOfferInfo(map, offerRepayInfo);
                }
                
                ExcelUtil.addResultToWorkBook(workBook, result, excelTemplet);

                // response.reset();
                /** 下载文件名 **/
                fileName = "还款录入导入结果" + Dates.getDateTime(Dates.DEFAULT_DATE_FORMAT) + ".xls";
                String enableFileName = new String(fileName.getBytes("GBK"), "ISO-8859-1");
                response.setHeader("Content-Disposition", "attachment; filename=" + enableFileName);
                response.setHeader("Content-Type", contentType);

                OutputStream outputStream = response.getOutputStream();
                workBook.write(outputStream);
                outputStream.flush();
                // 调用【外贸3】线下实收接口
                //this.callOfflineReceiveInterface(paidInMoneyList);
                return;
            } else {
                throw new PlatformException(ResponseEnum.FULL_MSG, "文件不为【个贷】认领结果");
            }
        } catch (PlatformException ex) {
            logger.error(ex, ex);
            responseInfo = ex.toResponseInfo();
        } catch (Exception ex) {
            /** 系统忙 **/
            logger.error(ex, ex);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
        }
        response.setContentType("text/html");
        response.getWriter().print(toResponseJSON(responseInfo));
    }

    //外贸3 创建线下还款 流水信息
    private void createDebitOfflineOfferInfo(Map<String, String> map, OfferRepayInfo offerRepayInfo) {
        // 线下还款金额
        String repayAmountPar = Strings.convertValue(map.get("repayAmount"),String.class);
        // 合同编号
        String contractNum = Strings.convertValue(map.get("contractNum"),String.class);
        // 还款录入反馈信息
        String feedBackMsg = Strings.convertValue(map.get("feedBackMsg"),String.class);
        //内部流水号
        String repayNo = Strings.convertValue(map.get("repayNo"), String.class);
        // 根据合同编号查询债权信息
        VLoanInfo searchVo = new VLoanInfo();
        searchVo.setContractNum(contractNum);
        List<VLoanInfo> loanInfoList = vLoanInfoServiceImpl.findListByVO(searchVo);
        if (CollectionUtils.isEmpty(loanInfoList)) {
            logger.warn("查询债权信息不存在，合同编号：" + contractNum);
            return;
        }
        VLoanInfo loanInfo = loanInfoList.get(0);
        if(!FundsSourcesTypeEnum.外贸3.getValue().equals(loanInfo.getLoanBelong())){
            logger.info("该笔对公还款不是外贸3的对公还款！");
            return;
        }
        if(!"还款成功".equals(feedBackMsg)){
            logger.info("该笔对公还款入账失败！");
            return;
        }
        BasePublicAccountInfoWm basePublicAccountInfoWm = new BasePublicAccountInfoWm();
        basePublicAccountInfoWm.setRepayNo(repayNo);
        basePublicAccountInfoWm.setLoanId(loanInfo.getId());
        List<BasePublicAccountInfoWm> list = basePublicAccountInfoWmService.findListByVo(basePublicAccountInfoWm);
        if(CollectionUtils.isEmpty(list)){
            logger.warn("外贸3线下还款异常 合同号为：" + contractNum + " 未找到专户银行流水号");
            return;
        }
        DebitOfflineOfferInfo debitOfflineOfferInfo = new DebitOfflineOfferInfo();
        debitOfflineOfferInfo.setLoanId(loanInfo.getId());
        debitOfflineOfferInfo.setBankNo(list.get(0).getSerialNumber());
        debitOfflineOfferInfo.setPactNo(contractNum);
        debitOfflineOfferInfo.setRepayAmt(new BigDecimal(repayAmountPar));
        debitOfflineOfferInfo.setRepayNo(repayNo);
        debitOfflineOfferInfo.setRepyType("03"); //溢缴款
        debitOfflineOfferInfo.setState(WMDebitDeductStateEnum.未发送.getValue());
        debitOfflineOfferInfo.setTradeDate((offerRepayInfo == null || offerRepayInfo.getTradeDate() == null) ? Dates.getCurrDate() : offerRepayInfo.getTradeDate());
        debitOfflineOfferInfo.setTradeNo(offerRepayInfo==null?null:offerRepayInfo.getTradeNo());
        debitOfflineOfferInfoService.createDebitOfflineOfferInfo(debitOfflineOfferInfo);
    }

    /**
     * 处理还款录入 Excel文件数据
     * 
     * @param values
     */
    private OfferRepayInfo importRepayInfoFileParseItem(Map<String, String> values) {
        /** 登陆者信息 **/
        User user = UserContext.getUser();
        String feedBackMsg = "";
        OfferRepayInfo offerRepayInfo = new OfferRepayInfo();
        try {
            String name = Strings.convertValue(values.get("name"), String.class);
            String idNum = Strings.convertValue(values.get("idNum"), String.class);
            String loanType = Strings.convertValue(values.get("loanType"), String.class);
            String repayDatePar = Strings.convertValue(values.get("repayDate"), String.class);
            String repayAmountPar = Strings.convertValue(values.get("repayAmount"), String.class);
            String tradeType = Strings.convertValue(values.get("tradeType"), String.class);
            String memo = Strings.convertValue(values.get("memo"), String.class);
            //合同编号
            String contractNum = Strings.convertValue(values.get("contractNum"), String.class);
            String repayNo = Strings.convertValue(values.get("repayNo"), String.class);
            
            Assert.notNullAndEmpty(name, "姓名");
            Assert.notNullAndEmpty(idNum, "身份证");
            Assert.notNullAndEmpty(loanType, "借款类型");
            Assert.notNullAndEmpty(repayDatePar, "首次还款日");
            Assert.notNullAndEmpty(repayAmountPar, "还款金额");
            Assert.notNullAndEmpty(tradeType, "还款方式");
            Assert.notNullAndEmpty(contractNum, "合同编号");
            Assert.notNullAndEmpty(repayNo, "内部流水号");
            Assert.validEnum(LoanTypeEnum.class, loanType, "借款类型");
            Assert.validEnum(TradeTypeEnum.class, tradeType, "还款方式");
            Date repayDate = Assert.notDate(repayDatePar, "首次还款日");
            BigDecimal repayAmount = Assert.notBigDecimal(repayAmountPar, "还款金额");
            PersonInfo personInfo = personInfoServiceImpl.findByIdNumAndName(name, idNum);
            Assert.notNull(personInfo, ResponseEnum.FULL_MSG, "客户信息不存在");

            Long borrowerId = personInfo.getId();

            /*List<VLoanInfo> loan = vLoanInfoServiceImpl.findByBorrowerAndState(borrowerId, new String[] {
                    LoanStateEnum.正常.name(), LoanStateEnum.逾期.name() });*/
            /** 根据借款人、合同编号查询借款数据**/
            List<VLoanInfo> loan = vLoanInfoServiceImpl.findByBorrowerAndContractNumAndState(borrowerId, contractNum, new String[] {
                    LoanStateEnum.正常.name(), LoanStateEnum.逾期.name() });
            Assert.notCollectionsEmpty(loan, ResponseEnum.FULL_MSG, "该笔借款已结清，系统跳过处理！");

            /** 还款当日 或 还款日+1天 在处理范围之内 **/
            Date repayDateBegin = repayDate;
            Date repayDateEnd = Dates.addDay(repayDateBegin, 1);
            /** 跟据借款人、借款类型、借款状态、 还款日期、合同编号查询借款数据 **/
            /*loan = vLoanInfoServiceImpl.findByBorrowerAndTypeAndStateAndStartrDate(borrowerId, loanType, new String[] {
                    LoanStateEnum.正常.name(), LoanStateEnum.逾期.name() }, repayDateBegin, repayDateEnd);*/
            loan = vLoanInfoServiceImpl.findByBorrowerAndTypeAndStateAndStartrDateAndContractNum(borrowerId, loanType, new String[] {
                    LoanStateEnum.正常.name(), LoanStateEnum.逾期.name() }, repayDateBegin, repayDateEnd, contractNum);
            Assert.notCollectionsEmpty(loan, ResponseEnum.FULL_MSG, "首次还款日数据无效");

            Date currDate = Dates.getCurrDate();
            Long loanId = loan.get(0).getId();
            /****/
            List<LoanRepaymentDetail> repaymentDetails = afterLoanServiceImpl.getAllInterestOrLoan(currDate, loanId);
            Assert.notCollectionsEmpty(repaymentDetails, ResponseEnum.FULL_MSG, "未找到还款计划数据,借款编号[" + loanId + "]");
            
            /**检查该债权是否进行过转让**/
			boolean flag = loanTransferInfoService.isLoanTransfer(null,loanId);
			if(!flag){
				 throw new PlatformException(ResponseEnum.FULL_MSG, Strings.errorMsg);
			}
            
            /** 查询一次性还款金额 **/
            // BigDecimal repaymentAmount =
            // afterLoanServiceImpl.getOnetimeRepaymentAmount(repaymentDetails,
            // currDate);
            /** 查询逾期总额 不包含罚息 **/
            // BigDecimal overdueAmount =
            // afterLoanServiceImpl.getOverdueAmount(repaymentDetails,
            // currDate);
            /** 根据当前还款日期、查询逾期罚息 **/
            // BigDecimal fine = afterLoanServiceImpl.getFine(repaymentDetails,
            // currDate);
            /** 总计 **/
            // BigDecimal maxRepayMoney =
            // repaymentAmount.add(overdueAmount).add(fine);
            /** 查询账户中余额（即挂账部分） **/
            // BigDecimal accAmount = afterLoanServiceImpl.getAccAmount(loanId);

            if (repayAmount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new PlatformException(ResponseEnum.FULL_MSG, "还款金额有误");
            }
            // if ((repayAmount.add(accAmount)).compareTo(maxRepayMoney) > 0) {
            // throw new PlatformException(ResponseEnum.FULL_MSG, "还款金额：" +
            // repayAmount + ",账户余额：" + accAmount
            // + " 超过应还总额： " + maxRepayMoney);
            // }

            RepaymentInputVo repaymentInputVo = new RepaymentInputVo();
            repaymentInputVo.setLoanId(loanId);
            repaymentInputVo.setAmount(repayAmount);
            repaymentInputVo.setMemo(memo);
            repaymentInputVo.setOrgan(user.getOrgCode());
            repaymentInputVo.setTeller(user.getUserCode());
            repaymentInputVo.setTradeDate(currDate);
            repaymentInputVo.setTradeType(tradeType);
            repaymentInputVo.setRepayNo(repayNo);
            /** 调用核心层接口（还款） **/
            //offerRepayInfo = offerRepayInfoServiceImpl.repaymentInputCore(repaymentInputVo);
            offerRepayInfo = offerRepayInfoServiceImpl.dealRepaymentInput(repaymentInputVo);
            feedBackMsg = "还款成功";
        } catch (PlatformException ex) {
            logger.error(ex, ex);
            feedBackMsg = ex.getMessage();
        } catch (Exception ex) {
            logger.error(ex, ex);
            feedBackMsg = "系统忙：记账出错，请重新执行还款操作";
        }
        values.put(ExcelTemplet.FEED_BACK_MSG, feedBackMsg);
        return offerRepayInfo;
    }

    @RequestMapping("/importCarRepayInfoFile")
    @ResponseBody
    public void importCarRepayInfoFile(@RequestParam(value = "uploadFile", required = false) MultipartFile file,
            HttpServletRequest request, HttpServletResponse response) throws IOException {
        ResponseInfo responseInfo = null;
        try {
            createLog(request, SysActionLogTypeEnum.导入, "还款录入", "批量导入还款录入操作（车企贷系统）");
            /** 登陆者信息 **/
            User user = UserContext.getUser();

            UploadFile uploadFile = new UploadFile();
            uploadFile.setFile(file);
            uploadFile.setFileType(UploadFileUtil.FILE_TYPE_EXCEL);
            uploadFile.setFileMaxSize(1024 * 1024 * 5);

            UploadFileUtil.valid(uploadFile);
            String fileName = file.getOriginalFilename();
            String contentType = file.getContentType();

            /** 跟据文件名判断是否已导入 **/
            List<ComMessageIn> checkFile = comMessageInServiceImpl.findByMessageTypeAndFileName("RepaymentInputCar",
                    fileName);
            Assert.isCollectionsEmpty(checkFile, ResponseEnum.FULL_MSG, "当前导入文件名:[" + fileName + "]已经存在!");

            String isCarCredit = fileName.substring(fileName.lastIndexOf("_") + 1, fileName.lastIndexOf("."));
            if (isCarCredit.equals("car")) {
                comMessageInServiceImpl.createMsgIn("RepaymentInputCar", user.getName(), fileName, "还款录入批量导入(车企贷)");

                MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<String, Object>();
                paramMap.add("file\"; filename=\"" + fileName, file.getBytes());
                paramMap.add("fileName", fileName);
                /** 车企贷Excel导入地址 **/
                String url = sysParamDefineServiceImpl.getSysParamValueCache("sysCar", "cqdUrl");
                Assert.notNullAndEmpty(url, ResponseEnum.FULL_MSG, "缺少地址信息");
                url += CAR_IMPORTREPAYDATA_URL;
                ResponseEntity<byte[]> responseEntity = null;
                /** 调用远程方法、获取数据 **/
                responseEntity = restTemplate.postForEntity(url, paramMap, byte[].class);
                String result = Strings.convertValue(responseEntity.getHeaders().get("result"), String.class);
                /** 按UTF-8进行解码 **/
                result = URLDecoder.decode(result, "UTF-8");
                if (Strings.isEmpty(result)) {
                    /** 响应正常返回 **/
                    // response.reset();
                    /** 下载文件名 **/
                    fileName = "车企贷导出已认领结果" + Dates.getDateTime(Dates.DEFAULT_DATE_FORMAT) + ".xlsx";
                    String enableFileName = new String(fileName.getBytes("GBK"), "ISO-8859-1");
                    response.setHeader("Content-Disposition", "attachment; filename=" + enableFileName);
                    response.setHeader("Content-Type", contentType);
                    OutputStream outputStream = response.getOutputStream();
                    outputStream.write(responseEntity.getBody());
                    outputStream.flush();
                    return;
                } else {
                    /** 响应异常返回 **/
                    throw new PlatformException(ResponseEnum.FULL_MSG, result);
                }
            } else {
                /** 文件名必须以"_car"结尾 **/
                throw new PlatformException(ResponseEnum.FULL_MSG, "文件不为【车企贷】认领结果");
            }
        } catch (PlatformException ex) {
            logger.error(ex, ex);
            responseInfo = new ResponseInfo(ex.getResponseCode(), ex.getArguments());
        } catch (Exception ex) {
            /** 系统忙 **/
            logger.error(ex, ex);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
        }
        response.setContentType("text/html");
        response.getWriter().print(toResponseJSON(responseInfo));
    }
    
    /**
     * 还款录入批量导入（证方系统）
     * @param file
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/importZfRepayInfoFile")
    @ResponseBody
    public void importZfRepayInfoFile(@RequestParam(value = "zfRepayInfoFile") MultipartFile file, HttpServletRequest request, HttpServletResponse response){
        ResponseInfo responseInfo = null;
        try {
            createLog(request, SysActionLogTypeEnum.导入, "还款录入", "批量导入还款录入操作（证方系统）");
            UploadFile uploadFile = new UploadFile();
            uploadFile.setFile(file);
            uploadFile.setFileType(UploadFileUtil.FILE_TYPE_EXCEL);
            uploadFile.setFileMaxSize(1024 * 1024 * 5);
            UploadFileUtil.valid(uploadFile);
            String fileName = file.getOriginalFilename();
            /** 跟据文件名判断是否已导入 **/
            List<ComMessageIn> fileMessages = comMessageInServiceImpl.findByMessageTypeAndFileName("RepaymentInputZf",fileName);
            Assert.isCollectionsEmpty(fileMessages, ResponseEnum.FULL_MSG, "当前导入文件名:[" + fileName + "]已经存在！");
            String suffix = fileName.substring(fileName.lastIndexOf("_") + 1, fileName.lastIndexOf("."));
            if (!"zf".equals(suffix)) {
                /** 文件名必须以"_zf"结尾 **/
                throw new PlatformException(ResponseEnum.FULL_MSG, "导入文件不是证方已认领结果文件！");
            }
            /** 登陆者信息 **/
            User user = UserContext.getUser();
            /** 记录导入消息日志 **/
            comMessageInServiceImpl.createMsgIn("RepaymentInputZf", user.getName(), fileName, "还款录入批量导入（证方）");
            MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<String, Object>();
            paramMap.add("file\"; filename=\"" + fileName, file.getBytes());
            paramMap.add("fileName", fileName);
            /** 证方还款录入导入地址 **/
            String url = sysParamDefineServiceImpl.getSysParamValueCache("sysCar", "zfUrl");
            Assert.notNullAndEmpty(url, ResponseEnum.FULL_MSG, "缺少请求地址信息！");
            url += ZF_IMPORTREPAYDATA_URL;
            /** 调用远程方法、获取数据 **/
            ResponseEntity<byte[]> responseEntity = restTemplate.postForEntity(url, paramMap, byte[].class);
            String result = Strings.convertValue(responseEntity.getHeaders().get("result"), String.class);
            /** 按UTF-8进行解码 **/
            result = URLDecoder.decode(result, "UTF-8");
            if (Strings.isNotEmpty(result)) {
                /** 响应异常返回 **/
                throw new PlatformException(ResponseEnum.FULL_MSG, result);
            }
            /** 响应正常返回 **/
            fileName = "证方还款录入批量导入返回结果" + Dates.getDateTime(Dates.DEFAULT_DATE_FORMAT) + ".xlsx";
            response.setHeader("Content-disposition","attachment;filename="+ URLEncoder.encode(fileName, "UTF-8"));
            response.setContentType(file.getContentType());
            OutputStream outputStream = response.getOutputStream();
            outputStream.write(responseEntity.getBody());
            outputStream.flush();
            return;
        } catch (PlatformException ex) {
            logger.error(ex, ex);
            responseInfo = new ResponseInfo(ex.getResponseCode(), ex.getArguments());
        } catch (Exception ex) {
            /** 系统忙 **/
            logger.error(ex, ex);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
        }
        try {
            response.setContentType("text/html");
            response.getWriter().print(toResponseJSON(responseInfo));
        } catch (IOException e) {
            logger.error("", e);
        }
    }
    
    /**
     * 封装收集接口参数信息
     * 
     * @param map
     * @param offerRepayInfo
     */
    public void packageParams(Map<String, String> map, OfferRepayInfo offerRepayInfo, List<PaidInMoneyListEntity> dataList) {
        // 线下还款金额
        String repayAmountPar = Strings.convertValue(map.get("repayAmount"),String.class);
        // 合同编号
        String contractNum = Strings.convertValue(map.get("contractNum"),String.class);
        // 还款录入反馈信息
        String feedBackMsg = Strings.convertValue(map.get("feedBackMsg"),String.class);
        //内部流水号
        String repayNo = Strings.convertValue(map.get("repayNo"), String.class);
        // 根据合同编号查询债权信息
        VLoanInfo searchVo = new VLoanInfo();
        searchVo.setContractNum(contractNum);
        List<VLoanInfo> loanInfoList = vLoanInfoServiceImpl.findListByVO(searchVo);
        if (CollectionUtils.isEmpty(loanInfoList)) {
            logger.warn("查询债权信息不存在，合同编号：" + contractNum);
            return;
        }
        VLoanInfo loanInfo = loanInfoList.get(0);
        if(!FundsSourcesTypeEnum.外贸3.getValue().equals(loanInfo.getLoanBelong())){
            logger.info("该笔对公还款不是外贸3的对公还款！");
            return;
        }
        if(!"还款成功".equals(feedBackMsg)){
            logger.info("该笔对公还款入账失败！");
            return;
        }
        BasePublicAccountInfoWm basePublicAccountInfoWm = new BasePublicAccountInfoWm();
        basePublicAccountInfoWm.setRepayNo(repayNo);
        basePublicAccountInfoWm.setLoanId(offerRepayInfo.getLoanId());
        //basePublicAccountInfoWm.setLoanBelong(FundsSourcesTypeEnum.外贸3.getValue());
        List<BasePublicAccountInfoWm> list = basePublicAccountInfoWmService.findListByVo(basePublicAccountInfoWm);
        if(CollectionUtils.isEmpty(list)){
            logger.warn("外贸3线下还款异常 合同号为：" + contractNum + " 未找到专户银行流水号");
            return;
        }
        // 需要查询外贸3对公还款的专户银行流水号、待实现
        PaidInMoneyListEntity paidInMoney = new PaidInMoneyListEntity();
        paidInMoney.setRepayAmt(new BigDecimal(repayAmountPar));
        paidInMoney.setPactNo(contractNum);
        dataList.add(paidInMoney);
    }
    
    /**
     * 调用【外贸3】线下实收接口
     * @param dataList
     */
    public void callOfflineReceiveInterface(List<PaidInMoneyListEntity> dataList) {
        if (CollectionUtils.isNotEmpty(dataList)) {
            try {
                logger.info("OfferRepayInfoController调用【外贸3】线下实收接口开始-批量导入");
                offerRepayInfoServiceImpl.callOfflineReceiveInterface(dataList);
                logger.info("OfferRepayInfoController调用【外贸3】线下实收接口成功-批量导入");
            } catch (Exception e) {
                logger.error("OfferRepayInfoController调用【外贸3】线下实收接口失败-批量导入 "+ e.getMessage());
            }
        }
    }
}
