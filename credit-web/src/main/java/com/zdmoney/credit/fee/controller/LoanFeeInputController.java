package com.zdmoney.credit.fee.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.zdmoney.credit.common.constant.FundsSourcesTypeEnum;
import com.zdmoney.credit.common.constant.LoanFeeStateEnum;
import com.zdmoney.credit.common.constant.LoanStateEnum;
import com.zdmoney.credit.common.constant.LoanTypeEnum;
import com.zdmoney.credit.common.constant.SysActionLogTypeEnum;
import com.zdmoney.credit.common.constant.TradeTypeEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.util.Assert;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.util.excel.ExcelTemplet;
import com.zdmoney.credit.common.util.excel.ExcelUtil;
import com.zdmoney.credit.common.util.file.UploadFileUtil;
import com.zdmoney.credit.common.util.file.vo.UploadFile;
import com.zdmoney.credit.common.vo.AttachmentResponseInfo;
import com.zdmoney.credit.common.vo.ResponseInfo;
import com.zdmoney.credit.fee.domain.LoanFeeInfo;
import com.zdmoney.credit.fee.domain.LoanFeeRepayInfo;
import com.zdmoney.credit.fee.domain.vo.LoanFeeInput;
import com.zdmoney.credit.fee.service.pub.ILoanFeeInfoService;
import com.zdmoney.credit.fee.service.pub.ILoanFeeInputService;
import com.zdmoney.credit.fee.service.pub.ILoanFeeRepayInfoService;
import com.zdmoney.credit.fee.vo.RepayDealVo;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.loan.domain.LoanRepaymentDetail;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.IAfterLoanService;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import com.zdmoney.credit.system.domain.ComMessageIn;
import com.zdmoney.credit.system.domain.PersonInfo;
import com.zdmoney.credit.system.service.pub.IComMessageInService;
import com.zdmoney.credit.system.service.pub.IPersonInfoService;

/**
 * 收费录入controller类
 * @author 00236640
 * @version $Id: LoanFeeInputController.java, v 0.1 2016年7月14日 下午4:09:21 00236640 Exp $
 */
@Controller
@RequestMapping("/fee/feeInput")
public class LoanFeeInputController extends BaseController {

    protected static Log logger = LogFactory.getLog(LoanFeeInputController.class);

    @Autowired
    private ILoanFeeInputService loanFeeInputService;
    
    @Autowired
    private IComMessageInService comMessageInService;
    
    @Autowired
    private IPersonInfoService personInfoService;

    @Autowired
    private IVLoanInfoService vLoanInfoService;
    
    @Autowired
    private IAfterLoanService afterLoanService;
    
    @Autowired
    private ILoanFeeRepayInfoService loanFeeRepayInfoService;
    
    @Autowired
    private ILoanFeeInfoService loanFeeInfoService;

    /**
     * 跳转主页面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/jumpPage")
    public ModelAndView jumpPage(HttpServletRequest request, HttpServletResponse response) {
        createLog(request, SysActionLogTypeEnum.查询, "收费录入", "加载收费录入页面");
        User user = UserContext.getUser();
        ModelAndView mav = new ModelAndView();
        String[] fundsSources = { FundsSourcesTypeEnum.外贸信托.getValue(),
                FundsSourcesTypeEnum.龙信小贷.getValue(),
                FundsSourcesTypeEnum.外贸2.getValue(),
                FundsSourcesTypeEnum.包商银行.getValue(),
                FundsSourcesTypeEnum.渤海2.getValue(),
                FundsSourcesTypeEnum.华瑞渤海.getValue(),
                FundsSourcesTypeEnum.外贸3.getValue(),
                FundsSourcesTypeEnum.陆金所.getValue() };
        mav.setViewName("fee/feeInput");
        mav.addObject("states", LoanFeeStateEnum.values());
        mav.addObject("fundsSources", fundsSources);
        mav.addObject("user", user);
        return mav;
    }
    
    /**
     * 查询收费录入信息
     * @param loanFeeInput
     * @param rows
     * @param page
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/searchLoanFeeInputInfo")
    @ResponseBody
    public String search(LoanFeeInput loanFeeInput, int rows,int page, HttpServletRequest request, HttpServletResponse response) {
        createLog(request, SysActionLogTypeEnum.查询, "收费录入", "查询收费录入信息");
        // 定义分页实例
        Pager pager = new Pager();
        pager.setRows(rows);
        pager.setPage(page);
        loanFeeInput.setPager(pager);
        // 调用Service层查询数据信息 
        pager = loanFeeInputService.findWithPg(loanFeeInput);
        // 将数据对象转换成JSON字符串，返回给前台
        return toPGJSONWithCode(pager);
    }
    
    /**
     * 收费录入（还款）
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/singleRepayment")
    @ResponseBody
    public String repaymentInput(HttpServletRequest request, HttpServletResponse response) {
        AttachmentResponseInfo<LoanFeeRepayInfo> responseInfo = null;
        try {
            createLog(request, SysActionLogTypeEnum.更新, "收费录入", "单客户收费录入操作");
            /** 债券编号 **/
            Long loanId = Strings.convertValue(request.getParameter("loanId"), Long.class);
            /** 还款金额（前台录入） **/
            BigDecimal amount = null;
            String amountStr = request.getParameter("amount");
            if (Strings.isNotEmpty(amountStr)) {
                amount = new BigDecimal(amountStr);
            }
            /** 交易类型 **/
            String tradeType = Strings.convertValue(request.getParameter("tradeType"), String.class);
            /** 还款备注 **/
            String memo = Strings.convertValue(request.getParameter("memo"), String.class);
            Assert.notNull(loanId, "借款编号");
            Assert.notNull(amount, "还款金额");
            Assert.notNullAndEmpty(tradeType, "交易类型");
            Assert.validEnum(TradeTypeEnum.class, tradeType, "交易类型");
            /** 根据债权编号查询借款收费信息 **/
            LoanFeeInfo loanFeeInfo = loanFeeInfoService.findLoanFeeInfoByLoanId(loanId);
            Assert.notNull(loanFeeInfo, "借款收费信息");
            /** 还款录入Vo **/
            RepayDealVo repayDealVo = new RepayDealVo();
            repayDealVo.setFeeId(loanFeeInfo.getId());
            repayDealVo.setAmount(amount);
            repayDealVo.setTradeType(tradeType);
            repayDealVo.setMemo(memo);
            /** 调用核心层接口（还款） **/
            LoanFeeRepayInfo loanFeeRepayInfo = loanFeeRepayInfoService.repayDeal(repayDealVo);
            responseInfo = new AttachmentResponseInfo<LoanFeeRepayInfo>(ResponseEnum.SYS_SUCCESS);
            responseInfo.setAttachment(loanFeeRepayInfo);
        } catch (PlatformException ex) {
            logger.error(ex, ex);
            responseInfo = new AttachmentResponseInfo<LoanFeeRepayInfo>(ResponseEnum.SYS_FAILD.getCode(), ex.getMessage());
        } catch (Exception ex) {
            /** 系统忙 **/
            logger.error(ex, ex);
            responseInfo = new AttachmentResponseInfo<LoanFeeRepayInfo>(ResponseEnum.SYS_FAILD);
        }
        return toResponseJSON(responseInfo);
    }
    
    /**
     * 批量导入收费录入（还款）
     * @param file
     * @param request
     * @param response
     */
    @RequestMapping("/batchRepayment")
    @ResponseBody
    public void importRepayInfoFile(@RequestParam(value = "repayInfoFile") MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        ResponseInfo responseInfo = null;
        try {
            createLog(request, SysActionLogTypeEnum.导入, "收费录入", "批量导入收费录入操作");
            /** 登陆者信息 **/
            User user = UserContext.getUser();
            UploadFile uploadFile = new UploadFile();
            uploadFile.setFile(file);
            uploadFile.setFileType(UploadFileUtil.FILE_TYPE_EXCEL);
            uploadFile.setFileMaxSize(1024 * 1024 * 5);
            UploadFileUtil.valid(uploadFile);
            String fileName = file.getOriginalFilename();
            /** 跟据文件名判断是否已导入 **/
            List<ComMessageIn> suffix = comMessageInService.findByMessageTypeAndFileName("FeeInput", fileName);
            Assert.isCollectionsEmpty(suffix, ResponseEnum.FULL_MSG, "当前导入文件名:[" + fileName + "]已经存在!");

            Workbook workBook = WorkbookFactory.create(file.getInputStream());
            ExcelTemplet excelTemplet = new ExcelTemplet().new RepaymentInputExcel();
            List<Map<String, String>> dataList = ExcelUtil.getExcelData(workBook, excelTemplet);
            Assert.notCollectionsEmpty(dataList, ResponseEnum.FILE_EMPTY_FILE, "");
            comMessageInService.createMsgIn("FeeInput", user.getName(), fileName, "收费录入批量导入");
            for (int i = 0; i < dataList.size(); i++) {
                // 单笔收费录入
                this.importRepayInfoFileParseItem(dataList.get(i));
            }
            ExcelUtil.addResultToWorkBook(workBook, dataList, excelTemplet);
            /** 下载文件名 **/
            fileName = "收费录入导入返回结果" + Dates.getDateTime(Dates.DEFAULT_DATE_FORMAT) + ".xlsx";
            response.setHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(fileName, "UTF-8"));
            response.setContentType(file.getContentType());
            OutputStream outputStream = response.getOutputStream();
            workBook.write(outputStream);
            outputStream.flush();
            return;
        } catch (PlatformException e) {
            logger.error(e, e);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(), e.getMessage());
        } catch (Exception e) {
            /** 系统忙 **/
            logger.error(e, e);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
        }
        try {
            response.setContentType("text/html");
            response.getWriter().print(toResponseJSON(responseInfo));
        } catch (IOException e) {
        }
    }
    
    /**
     * 单笔收费录入
     * @param values
     */
    private void importRepayInfoFileParseItem(Map<String, String> values) {
        String feedBackMsg = "";
        try {
            // 客户姓名
            String name = Strings.convertValue(values.get("name"), String.class);
            // 身份证号码
            String idNum = Strings.convertValue(values.get("idNum"), String.class);
            // 借款类型
            String loanType = Strings.convertValue(values.get("loanType"), String.class);
            // 还款日期
            String repayDatePar = Strings.convertValue(values.get("repayDate"), String.class);
            // 还款金额
            String repayAmountPar = Strings.convertValue(values.get("repayAmount"), String.class);
            // 还款方式
            String tradeType = Strings.convertValue(values.get("tradeType"), String.class);
            // 备注
            String memo = Strings.convertValue(values.get("memo"), String.class);
            // 合同编号
            String contractNum = Strings.convertValue(values.get("contractNum"), String.class);
            Assert.notNullAndEmpty(name, "姓名");
            Assert.notNullAndEmpty(idNum, "身份证");
            Assert.notNullAndEmpty(loanType, "借款类型");
            Assert.notNullAndEmpty(repayDatePar, "首次还款日");
            Assert.notNullAndEmpty(repayAmountPar, "还款金额");
            Assert.notNullAndEmpty(tradeType, "还款方式");
            Assert.notNullAndEmpty(contractNum, "合同编号");
            Assert.validEnum(LoanTypeEnum.class, loanType, "借款类型");
            Assert.validEnum(TradeTypeEnum.class, tradeType, "还款方式");
            Date repayDate = Assert.notDate(repayDatePar, "首次还款日");
            BigDecimal repayAmount = Assert.notBigDecimal(repayAmountPar,"还款金额");
            
            /** 客户信息是否存在校验 **/
            PersonInfo personInfo = personInfoService.findByIdNumAndName(name, idNum);
            Assert.notNull(personInfo, ResponseEnum.FULL_MSG, "客户信息不存在");

            Long borrowerId = personInfo.getId();
            /** 根据借款人、合同编号查询借款数据 **/
            List<VLoanInfo> loanInfoList = vLoanInfoService.findByBorrowerAndContractNumAndState(borrowerId,contractNum,
                            new String[] { LoanStateEnum.正常.name(), LoanStateEnum.逾期.name() });
            Assert.notCollectionsEmpty(loanInfoList, ResponseEnum.FULL_MSG, "该笔借款已结清，系统跳过处理！");

            /** 还款当日 或 还款日+1天 在处理范围之内 **/
            Date repayDateBegin = repayDate;
            Date repayDateEnd = Dates.addDay(repayDateBegin, 1);
            /** 跟据借款人、借款类型、借款状态、 还款日期、合同编号查询借款数据 **/
            loanInfoList = vLoanInfoService.findByBorrowerAndTypeAndStateAndStartrDateAndContractNum(borrowerId, loanType,
                            new String[] { LoanStateEnum.正常.name(),LoanStateEnum.逾期.name() }, repayDateBegin,
                            repayDateEnd, contractNum);
            Assert.notCollectionsEmpty(loanInfoList, ResponseEnum.FULL_MSG, "首次还款日数据无效");

            /** 还款计划是否存在校验 **/
            Date currDate = Dates.getCurrDate();
            Long loanId = loanInfoList.get(0).getId();
            List<LoanRepaymentDetail> repaymentDetails = afterLoanService.getAllInterestOrLoan(currDate, loanId);
            Assert.notCollectionsEmpty(repaymentDetails, ResponseEnum.FULL_MSG, "未找到还款计划数据,借款编号[" + loanId + "]");

            /** 还款金额大于零校验 **/
            if (repayAmount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new PlatformException(ResponseEnum.FULL_MSG, "还款金额有误！");
            }
            
            /** 根据债权编号查询借款收费信息 **/
            LoanFeeInfo loanFeeInfo = loanFeeInfoService.findLoanFeeInfoByLoanId(loanId);
            Assert.notNull(loanFeeInfo, "借款收费信息");
            /** 还款录入Vo **/
            RepayDealVo repayDealVo = new RepayDealVo();
            repayDealVo.setFeeId(loanFeeInfo.getId());
            repayDealVo.setAmount(repayAmount);
            repayDealVo.setTradeType(tradeType);
            repayDealVo.setMemo(memo);
            /** 调用核心层接口（还款） **/
            loanFeeRepayInfoService.repayDeal(repayDealVo);
            feedBackMsg = "还款成功";
        } catch (PlatformException e) {
            logger.error(e, e);
            feedBackMsg = e.getMessage();
        } catch (Exception e) {
            logger.error(e, e);
            feedBackMsg = "系统忙：记账出错，请重新执行还款操作";
        }
        values.put(ExcelTemplet.FEED_BACK_MSG, feedBackMsg);
    }
}
