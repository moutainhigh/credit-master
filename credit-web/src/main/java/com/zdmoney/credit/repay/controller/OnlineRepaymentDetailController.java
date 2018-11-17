package com.zdmoney.credit.repay.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.zdmoney.credit.common.constant.ComOrganizationEnum;
import com.zdmoney.credit.common.constant.SysActionLogTypeEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.util.Assert;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.ExcelExportUtil;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.vo.ResponseInfo;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.repay.service.pub.ISalesDeptRepayInfoService;
import com.zdmoney.credit.repay.service.pub.IVRepaymentDetailService;
import com.zdmoney.credit.repay.vo.VRepaymentDetail;

@Controller
@RequestMapping("/repay")
public class OnlineRepaymentDetailController extends BaseController {
    
    /**
     * 文件类型
     */
    public static final String CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    
    @Autowired
    private IVRepaymentDetailService vRepaymentDetailService;
    
    @Autowired
    private ISalesDeptRepayInfoService salesDeptRepayInfoService;
    
    /**
     * 初始化线上还款明细页面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/onlineRepaymentDetail")
    public ModelAndView jumpPage(HttpServletRequest request,HttpServletResponse response) {
        createLog(request, SysActionLogTypeEnum.查询, "线上还款明细", "加载线上还款明细页面");
        // 获取当前登录用户的信息
        User userInfo = UserContext.getUser();
        String empNum = userInfo.getOrgCode();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("empNum", empNum);
        params.put("vLevel", ComOrganizationEnum.Level.V104.name());
        // 查询当前用户所属机构下的所有营业网点信息
        List<Map<String, Object>> salesDeptInfoList = salesDeptRepayInfoService.getSalesDeptInfo(params);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("repay/onlineRepaymentDetail");
        mav.addObject("salesDeptInfoList", salesDeptInfoList);
        return mav;
    }
    
    /**
     * 线上还款明细查询处理
     * @param repaymentDetail
     * @param rows
     * @param page
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/searchOnlineRepaymentDetail")
    @ResponseBody
    public String search(VRepaymentDetail repaymentDetail, int rows,int page, HttpServletRequest request, HttpServletResponse response) {
        createLog(request, SysActionLogTypeEnum.查询, "线上还款明细", "查询线上还款明细数据");
        // 定义分页实例
        Pager pager = new Pager();
        pager.setRows(rows);
        pager.setPage(page);
        repaymentDetail.setPager(pager);
        // 调用Service层查询线上还款明细
        pager = vRepaymentDetailService.findWithPg(repaymentDetail);
        // 将数据对象转换成JSON字符串，返回给前台
        return toPGJSONWithCode(pager);
    }
    
    /**
     * 导出在线还款明细信息
     * @param repaymentDetail
     * @param request
     * @param response
     */
    @RequestMapping("/exportOnlineRepaymentDetail")
    @ResponseBody
    public void exportOnlineRepaymentDetail(VRepaymentDetail repaymentDetail,HttpServletRequest request, HttpServletResponse response){
        ResponseInfo responseInfo = null;
        // 查询最大件数s
        repaymentDetail.setMax(Long.valueOf(20000));
        createLog(request, SysActionLogTypeEnum.导出, "线上还款明细", "导出线上还款明细数据");
        try {
            // 查询线上还款明细信息，最大件数不超过20000件
            List<VRepaymentDetail> repaymentDetailList = vRepaymentDetailService.queryRepaymentDetailList(repaymentDetail);
            Assert.notCollectionsEmpty(repaymentDetailList,"当前导出条件查询不到数据");
            String fileName = "线上还款明细表" + Dates.getDateTime(new Date(), "yy-MM-dd") + ".xls";
            String[] labels = new String[] { "身份证号", "交易金额", "罚息", "减免金额","风险金", "利息", "本金", "借款人", "交易日期", "管理网点ID", "借款状态", "借款类型","合同编号"};
            String[] fields = new String[] { "idnum", "amount", "fx","jmfx", "fxj", "lx", "bj", "name", "tradeDate", "salesDepartmentId", "loanState", "loanType","contractNum"};
            // 工作表名称
            String sheetName = "Export";
            // 创建工作簿
            Workbook workbook = ExcelExportUtil.createExcelByVo(fileName, labels,fields, repaymentDetailList, sheetName);
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
             //response.reset();
             response.setHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(fileName, "UTF-8"));
             response.setContentType(CONTENT_TYPE);
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
