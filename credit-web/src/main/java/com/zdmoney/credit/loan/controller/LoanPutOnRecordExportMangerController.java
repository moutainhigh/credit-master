package com.zdmoney.credit.loan.controller;

import com.zdmoney.credit.common.constant.FundsSourcesTypeEnum;
import com.zdmoney.credit.common.constant.SysActionLogTypeEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.util.file.UploadFileUtil;
import com.zdmoney.credit.common.vo.ResponseInfo;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.loan.service.pub.ILoanPutOnRecordExportService;
import com.zdmoney.credit.loan.vo.LoanPutOnRecordExportVo;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 备案导出管理
 */
@Controller
@RequestMapping("/loan/put")
public class LoanPutOnRecordExportMangerController extends BaseController {
    public Logger logger= LoggerFactory.getLogger(LoanPutOnRecordExportMangerController.class);
    private String exportSZfileName = "深圳备案.xls";
    private String exportNotSZfileName = "异地备案.xls";
    @Autowired
    private ILoanPutOnRecordExportService loanPutOnRecordExportService;
    @RequestMapping("/putOnRecordExportPage")
    public ModelAndView putOnRecordExportPage(HttpServletRequest request,HttpServletResponse response){
        ModelAndView model=new ModelAndView();
        //每个月的第一天
        Date beginSignDate=  Dates.getAfterDays(Dates.getMonthLastDay(Dates.addMonths(-1)),1);
        Date endSignDate=Dates.getCurrDate();
        model.addObject("beginSignDate",beginSignDate);
        model.addObject("endSignDate", endSignDate);
        model.setViewName( "/loan/putOnRecordExportPage");
        return model;
    }

    @RequestMapping("querySZputOnRecord")
    public ModelAndView querySZputOnRecord(){
        return new ModelAndView("/loan/querySZputOnRecord");
    }
    @RequestMapping("/queryNotSZputOnRecord")
    public ModelAndView queryNotSZputOnRecord(){
        return new ModelAndView("/loan/queryNotSZputOnRecord");
    }
    @ResponseBody
    @RequestMapping("/listputOnRecordSZ")
    public String  listputOnRecordSZ(LoanPutOnRecordExportVo lporeVo,int rows, int page,HttpServletRequest request,HttpServletResponse response){
        Map<String,Object> paramMap = new HashMap<>();
        Pager pager = new Pager();
        pager.setPage(page);
        pager.setRows(rows);
        paramMap.put("contractNum", lporeVo.getContractNum());
        paramMap.put("regionType", lporeVo.getRegionType());
        paramMap.put("beginSignDate", lporeVo.getBeginSignDate());
        paramMap.put("endSignDate", lporeVo.getEndSignDate());
        paramMap.put("pager", pager);
        if (Strings.isNotEmpty(lporeVo.getFinancialorg()) && "WMXT".equals(lporeVo.getFinancialorg().substring(0, 4))) {
            paramMap.put("fundsSources", FundsSourcesTypeEnum.外贸信托.getValue());
        } else {
            paramMap.put("fundsSources",  FundsSourcesTypeEnum.外贸信托.getValue());
        }
        pager = loanPutOnRecordExportService.queryLoanPutOnRecordInfosPage(paramMap);

        return toPGJSONWithCode(pager);
    }

    @ResponseBody
    @RequestMapping("/listputOnRecordNotSZ")
    public String  listputOnRecordNotSZ(LoanPutOnRecordExportVo lporeVo,int rows, int page,HttpServletRequest request,HttpServletResponse response){
        Map<String,Object> paramMap = new HashMap<>();
        Pager pager = new Pager();
        pager.setRows(rows);
        pager.setPage(page);
        paramMap.put("contractNum", lporeVo.getContractNum());
        paramMap.put("regionType", lporeVo.getRegionType());
        paramMap.put("beginSignDate", lporeVo.getBeginSignDate());
        paramMap.put("endSignDate", lporeVo.getEndSignDate());
        paramMap.put("pager", pager);
        if (Strings.isNotEmpty(lporeVo.getFinancialorg()) && "WMXT".equals(lporeVo.getFinancialorg().substring(0, 4))) {
            paramMap.put("fundsSources", FundsSourcesTypeEnum.外贸信托.getValue());
        } else {
            paramMap.put("fundsSources", FundsSourcesTypeEnum.外贸信托.getValue());
        }
        pager = loanPutOnRecordExportService.queryLoanPutOnRecordInfosPage(paramMap);
        return toPGJSONWithCode(pager);
    }

    @ResponseBody
    @RequestMapping("exportSZputOnRecord")
    public  void exportSZputOnRecord(LoanPutOnRecordExportVo lporeVo,HttpServletRequest request,HttpServletResponse response){
        ResponseInfo responseInfo = null;
        try {
            createLog(request, SysActionLogTypeEnum.导出, "备案导出", "导出深圳备案（Xls）文件");
            if (Strings.isNotEmpty(lporeVo.getFinancialorg()) && "WMXT".equals(lporeVo.getFinancialorg().substring(0, 4))) {
                lporeVo.setFundsSources(FundsSourcesTypeEnum.外贸信托.getValue());
            } else {
                lporeVo.setFundsSources(FundsSourcesTypeEnum.外贸信托.getValue());
            }
            String fileName = Dates.getDateTime(Dates.getCurrDate(), "yyyyMMdd") + this.exportSZfileName;
            Workbook workbook = loanPutOnRecordExportService.getSZputOnRecordWorkBook(lporeVo, fileName);
            if (workbook == null) {
                throw new PlatformException(ResponseEnum.FULL_MSG, "创建深圳备案（Xls）文件失败！");
            }
            String downLoadError = this.downLoadFile(request, response, fileName, workbook);
            if (Strings.isNotEmpty(downLoadError)) {
                throw new PlatformException(ResponseEnum.FULL_MSG, downLoadError);
            }
            return ;
        }catch (PlatformException pe){
            responseInfo = new ResponseInfo(pe.getResponseCode().getCode(),pe.getMessage());
        }catch (Exception e){
            logger.error("导出深圳备案文件异常：",e);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
        }
        try{
            response.setContentType("text/html");
            response.getWriter().write(toResponseJSON(responseInfo));
        }catch (IOException e){
        }
    }

    @ResponseBody
    @RequestMapping("/exportNotSZputOnRecord")
    public void exportNotSZputOnRecord(LoanPutOnRecordExportVo lporeVo,HttpServletRequest request,HttpServletResponse response){
        ResponseInfo responseInfo = null;
        try {
            createLog(request, SysActionLogTypeEnum.导出, "备案导出", "导出异地备案（Xls）文件");
            if (Strings.isNotEmpty(lporeVo.getFinancialorg()) && "WMXT".equals(lporeVo.getFinancialorg().substring(0, 4))) {
                lporeVo.setFundsSources(FundsSourcesTypeEnum.外贸信托.getValue());
            } else {
                lporeVo.setFundsSources(FundsSourcesTypeEnum.外贸信托.getValue());
            }
            String fileName = Dates.getDateTime(Dates.getCurrDate(), "yyyyMMdd") + this.exportNotSZfileName;
            Workbook workbook = loanPutOnRecordExportService.getNotSZputOnRecordWorkBook(lporeVo, fileName);
            if (workbook == null) {
                throw new PlatformException(ResponseEnum.FULL_MSG, "创建异地备案（Xls）文件失败！");
            }
            String downLoadError = this.downLoadFile(request, response, fileName, workbook);
            if (Strings.isNotEmpty(downLoadError)) {
                throw new PlatformException(ResponseEnum.FULL_MSG, downLoadError);
            }
            return;
        }catch (PlatformException pe){
            responseInfo = new ResponseInfo(pe.getResponseCode().getCode(),pe.getMessage());
        }catch (Exception e){
            logger.error("导出异地备案文件异常：",e);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
        }
        try{
            response.setContentType("text/html");
            response.getWriter().write(toResponseJSON(responseInfo));
        }catch (IOException e){

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
}
