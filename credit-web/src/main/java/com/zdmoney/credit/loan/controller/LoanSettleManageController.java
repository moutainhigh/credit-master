package com.zdmoney.credit.loan.controller;

import com.zdmoney.credit.common.constant.SysActionLogTypeEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.util.Assert;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.excel.ExcelTemplet;
import com.zdmoney.credit.common.util.excel.ExcelUtil;
import com.zdmoney.credit.common.util.file.UploadFileUtil;
import com.zdmoney.credit.common.util.file.vo.UploadFile;
import com.zdmoney.credit.common.vo.ResponseInfo;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.loan.domain.LoanSettleInfo;
import com.zdmoney.credit.loan.service.pub.ILoanEvaluateInfoService;
import com.zdmoney.credit.loan.service.pub.ILoanSettleInfoService;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;


/**
 * 债权结清管理
 */

@Controller
@RequestMapping(value = "/loanSettle")
public class LoanSettleManageController extends BaseController {
    private static final String CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    @Autowired
    IVLoanInfoService vLoanInfoService;
    @Autowired
    ILoanSettleInfoService loanSettleInfoService;
    @Autowired
    ILoanEvaluateInfoService loanEvaluateInfoService;

    /**
     * 跳转到债权结清管理页面
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/loanSettleManagePage")
    public String loanSettleManagePage(HttpServletRequest request, HttpServletResponse response) {
        return "/loanTransfer/loanSettleManagePage";
    }

    /**
     * 债权转让导入
     *
     * @param file
     * @param request
     * @param response
     * @throws IOException
     */
    //@ResponseBody
    @RequestMapping("/loanSettleImport")
    public void loanSettleImport(@RequestParam(value = "uploadFile", required = false) MultipartFile file,
                                 HttpServletRequest request, HttpServletResponse response) throws IOException {
        createLog(request, SysActionLogTypeEnum.导入, "债权结清管理", "导入债权结清信息");
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
            ExcelTemplet excelTemplet = new ExcelTemplet().new LoanSettleExcel();
            List<Map<String, String>> result = ExcelUtil.getExcelData(workbook, excelTemplet);
            Assert.notCollectionsEmpty(result, ResponseEnum.FILE_EMPTY_FILE, "");
            for (Map<String, String> map : result) {
                bulidLoanSettleEvalManageInfo(map);
            }
            ExcelUtil.addResultToWorkBook(workbook, result, excelTemplet);
            fileName = "债权结清导入结果" + Dates.getDateTime(Dates.DEFAULT_DATE_FORMAT) + ".xls";/** 下载文件名 **/
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
     * 保存债权结清
     *
     * @param map
     */
    private void bulidLoanSettleEvalManageInfo(Map<String, String> map) {
        String feedBackMsg = "";
        String str = "";
        try {
            LoanSettleInfo loanSettleInfo = loanSettleInfoService.bulidLoanSettleInfo(map);
            str = "债权结清导入";
            feedBackMsg = loanSettleInfo.getNote();

        } catch (PlatformException ex) {
            logger.error(ex, ex);
            feedBackMsg = ex.getMessage();
        } catch (Exception ex) {
            logger.error(ex, ex);
            feedBackMsg = "系统忙：" + str + "出错，请重新执行导入操作";
        }
        map.put(ExcelTemplet.FEED_BACK_MSG, feedBackMsg);
    }
}
