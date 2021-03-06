package com.zdmoney.credit.operation.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.zdmoney.credit.common.constant.LoanStateEnum;
import com.zdmoney.credit.common.constant.SysActionLogTypeEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.util.Assert;
import com.zdmoney.credit.common.util.ExcelExportUtil;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.vo.ResponseInfo;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.loan.domain.LoanBase;
import com.zdmoney.credit.loan.service.pub.ILoanLogService;
import com.zdmoney.credit.operation.domain.LoanContractInfo;
import com.zdmoney.credit.operation.service.pub.ILoanContractInfoService;

@Controller
@RequestMapping("/operation")
public class LoanContractCustomerServiceController extends BaseController {
    
    private static final String CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    @Autowired
    private ILoanContractInfoService loanContractInfoService;
    
    @Autowired
    private ILoanLogService loanLogService;
    
    /**
     * 初始化页面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/loanContractCustomerServiceChange")
    public ModelAndView jumpPage(HttpServletRequest request,HttpServletResponse response) {
        createLog(request, SysActionLogTypeEnum.查询, "借款合同客服变更", "加载借款合同客服变更页面");
        ModelAndView mav = new ModelAndView();
        mav.setViewName("operation/loanContractCustomerServiceInfo");
        // 编辑借款状态
        List<String> loanStates = this.getLoanStates();
        mav.addObject("loanStates", loanStates);
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
    @RequestMapping("/searchLoanContractCustomerServiceInfo")
    @ResponseBody
    public String search(LoanContractInfo loanContractInfo, int rows,int page, HttpServletRequest request, HttpServletResponse response) {
        createLog(request, SysActionLogTypeEnum.查询, "借款合同客服变更", "查询借款合同客服信息");
        // 获取当前登录用户的信息
        User userInfo = UserContext.getUser();
        String empNum = userInfo.getOrgCode();
        loanContractInfo.setEmpNum(empNum);
        // 编辑借款状态
        List<String> loanStates = this.getLoanStates();
        loanContractInfo.setLoanStates(loanStates);
        // 定义分页实例
        Pager pager = new Pager();
        pager.setRows(rows);
        pager.setPage(page);
        loanContractInfo.setPager(pager);
        // 调用Service层查询各门店还款信息
        pager = loanContractInfoService.findWithPg(loanContractInfo);
        // 将数据对象转换成JSON字符串，返回给前台
        return toPGJSONWithCode(pager);
    }
    
    /**
     * 客服变更
     * @param loanIds
     * @param newCrmId
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/batchUpdateLoanContractCustomerServiceInfo")
    @ResponseBody
    public String batchUpdateCustomerServiceInfo(String loanIds, Long newCrmId,
            HttpServletRequest request, HttpServletResponse response) {
        ResponseInfo responseInfo = null;
        createLog(request, SysActionLogTypeEnum.更新, "借款合同客服变更", "变更借款合同客服信息");
        try {
            String[] loanIdArr = loanIds.split(",");
            for (int i = 0; i < loanIdArr.length; i++) {
                LoanBase loanBase = new LoanBase();
                Long  salesDepartmentId=loanContractInfoService.getSalesDepartmentId(newCrmId);
                System.out.println("营业厅网点ID~~~~~~~~~~:::"+salesDepartmentId);
                Long loanId = Long.valueOf(loanIdArr[i]);
                loanBase.setId(loanId);
                loanBase.setCrmId(newCrmId);//设置客服ID
                loanBase.setSalesDepartmentId(salesDepartmentId);//设置管理营业部ID
                // 变更客服
                loanContractInfoService.updateLoanContractCrmInfo(loanBase);
                // 记录日志
                loanLogService.createLog(loanId, "客服变更", "LoanContractCustomerServiceController", "info", null);
            }
            responseInfo = new ResponseInfo(ResponseEnum.SYS_SUCCESS);
        } catch (Exception e) {
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
        }
        return toResponseJSON(responseInfo);
    }
    
    /**
     * excel文件导出
     * @param loanContractInfo
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/exportLoanContractCustomerServiceInfo")
    @ResponseBody
    public void exportLoanContractInfo(LoanContractInfo loanContractInfo, HttpServletRequest request,HttpServletResponse response) {
        ResponseInfo responseInfo = null;
        // 获取当前登录用户的信息
        User userInfo = UserContext.getUser();
        String empNum = userInfo.getOrgCode();
        loanContractInfo.setEmpNum(empNum);
        // 编辑借款状态
        List<String> loanStates = this.getLoanStates();
        loanContractInfo.setLoanStates(loanStates);
        // 最大导出20000条
        loanContractInfo.setMax(Long.valueOf(20000));
        
        createLog(request, SysActionLogTypeEnum.导出, "借款合同客服变更", "导出借款合同客服信息");
        Workbook workbook = null;
        OutputStream out = null;
        try {
            List<LoanContractInfo> loanContractInfoList = loanContractInfoService.findListByVo(loanContractInfo);
            // 当前导出条件查询不到数据
            Assert.notCollectionsEmpty(loanContractInfoList, "当前导出条件查询不到数据");
            for (LoanContractInfo contractInfo : loanContractInfoList) {
                contractInfo.setInActive("t".equals(contractInfo.getInActive()) ? "是" : "否");
            }
            // excel文件的表头显示字段名
            List<String> labels = this.getLabels();
            // excel文件的数据字段名
            List<String> fields = this.getFields();
            // 文件名
            String fileName = "客服变更查询表"+ new SimpleDateFormat("yy-MM-dd").format(new Date()) + ".xls";
            // 工作表名称
            String sheetName = "Export";
            // 创建excel工作簿对象
            workbook = ExcelExportUtil.createExcelByVo(fileName, labels,fields, loanContractInfoList, sheetName);
            // 文件下载
            //response.reset();
            response.setHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(fileName, "UTF-8"));
            response.setContentType(CONTENT_TYPE);
            out = response.getOutputStream();
            workbook.write(out);
            out.flush();
            return;
        } catch (PlatformException e) {
            logger.error("导出excel文件失败：", e);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),e.getMessage());
        } catch (Exception e) {
            logger.error("导出excel文件失败：", e);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
        } finally {
            try {
                if (null != out) {
                    out.close();
                }
                if (null != workbook) {
                    workbook.close();
                }
            } catch (IOException e) {
                responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
                logger.error("导出excel文件失败：", e);
            }
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
    private List<String> getLabels(){
        List<String> labels = new ArrayList<String>();
        labels.add("借款人");
        labels.add("证件号码");
        labels.add("借款类型");
        labels.add("申请金额");
        labels.add("审批金额");
        labels.add("合同金额");
        labels.add("放款金额");
        labels.add("借款期限");
        labels.add("申请日期");
        labels.add("借款状态");
        labels.add("当前客服");
        labels.add("是否在职");
        labels.add("合同编号");
        return labels;
    }
    
    /**
     * excel文件的数据字段名
     * @return
     */
    private List<String> getFields(){
        List<String> fields = new ArrayList<String>();
        fields.add("name");
        fields.add("idNum");
        fields.add("loanType");
        fields.add("requestMoney");
        fields.add("money");
        fields.add("pactMoney");
        fields.add("grantMoney");
        fields.add("time");
        fields.add("requestDate");
        fields.add("loanState");
        fields.add("crmName");
        fields.add("inActive");
        fields.add("contractNum");
        return fields;
    }
    
    /**
     * 编辑借款状态
     * @return
     */
    private List<String> getLoanStates(){
        List<String> loanStates = new ArrayList<String>();
        loanStates.add(LoanStateEnum.申请.getValue());
        loanStates.add(LoanStateEnum.审核中.getValue());
        loanStates.add(LoanStateEnum.通过.getValue());
        loanStates.add(LoanStateEnum.正常.getValue());
        loanStates.add(LoanStateEnum.逾期.getValue());
        loanStates.add(LoanStateEnum.退回.getValue());
        return loanStates;
    }
}
