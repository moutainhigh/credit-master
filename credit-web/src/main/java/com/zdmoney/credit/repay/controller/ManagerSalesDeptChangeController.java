package com.zdmoney.credit.repay.controller;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
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

import com.zdmoney.credit.common.constant.ComOrganizationEnum;
import com.zdmoney.credit.common.constant.LoanStateEnum;
import com.zdmoney.credit.common.constant.LoanTypeEnum;
import com.zdmoney.credit.common.constant.SysActionLogTypeEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.util.Assert;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.util.excel.ExcelTemplet;
import com.zdmoney.credit.common.util.excel.ExcelUtil;
import com.zdmoney.credit.common.util.file.UploadFileUtil;
import com.zdmoney.credit.common.util.file.vo.UploadFile;
import com.zdmoney.credit.common.vo.ResponseInfo;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import com.zdmoney.credit.repay.domain.SalesDeptRepayInfo;
import com.zdmoney.credit.repay.service.pub.ISalesDeptRepayInfoService;
import com.zdmoney.credit.system.domain.ComEmployee;
import com.zdmoney.credit.system.domain.ComOrganization;
import com.zdmoney.credit.system.service.pub.IComEmployeeService;
import com.zdmoney.credit.system.service.pub.IComOrganizationService;

@Controller
@RequestMapping("/repay")
public class ManagerSalesDeptChangeController extends BaseController {
    
    @Autowired
    private ISalesDeptRepayInfoService salesDeptRepayInfoService;
    
    @Autowired
    private IVLoanInfoService vLoanInfoService;
    
    @Autowired
    private IComEmployeeService comEmployeeService;
    
    @Autowired
    private IComOrganizationService comOrganizationService;
    
    /**
     * 初始化页面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/managerSalesDeptChange")
    public ModelAndView jumpPage(HttpServletRequest request,HttpServletResponse response) {
        createLog(request, SysActionLogTypeEnum.查询, "管理门店变更", "加载管理门店变更页面");
        // 获取当前登录用户的信息
        User userInfo = UserContext.getUser();
        String empNum = userInfo.getOrgCode();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("empNum", empNum);
        params.put("vLevel", ComOrganizationEnum.Level.V104.name());
        // 查询当前用户所属机构下的所有营业网点信息
        List<Map<String, Object>> salesDeptInfoList = salesDeptRepayInfoService.getSalesDeptInfo(params);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("repay/managerSalesDeptChange");
        mav.addObject("salesDeptInfoList", salesDeptInfoList);
        mav.addObject("loanTypes", LoanTypeEnum.values());
        return mav;
    }
    
    /**
     * 查询处理
     * @param salesDeptRepayInfo
     * @param rows
     * @param page
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/searchSalesDeptRepayInfo")
    @ResponseBody
    public String search(SalesDeptRepayInfo salesDeptRepayInfo, int rows,int page, HttpServletRequest request, HttpServletResponse response) {
        createLog(request, SysActionLogTypeEnum.查询, "管理门店变更", "查询管理门店列表信息");
        // 获取当前登录用户的信息
        User userInfo = UserContext.getUser();
        String empNum = userInfo.getOrgCode();
        salesDeptRepayInfo.setEmpNum(empNum);
        salesDeptRepayInfo.setLoanStates(this.getLoanStates());
        // 定义分页实例
        Pager pager = new Pager();
        pager.setRows(rows);
        pager.setPage(page);
        salesDeptRepayInfo.setPager(pager);
        // 调用Service层查询各门店还款信息
        pager = salesDeptRepayInfoService.findWithPg(salesDeptRepayInfo);
        // 将数据对象转换成JSON字符串，返回给前台
        return toPGJSONWithCode(pager);
    }
    
    /**
     * 批量导入文件处理
     * @param file
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/batchImportSalesDeptRepayInfo")
    @ResponseBody
    public void importManageSalesDeptChangeInfo(@RequestParam(value = "uploadfile") MultipartFile file,
            HttpServletRequest request, HttpServletResponse response) {
        OutputStream out = null;
        ResponseInfo responseInfo = null;
        createLog(request, SysActionLogTypeEnum.导入, "管理门店变更", "变更管理门店");
        try {
            // 文件校验
            UploadFile uploadFile = new UploadFile();
            uploadFile.setFile(file);
            uploadFile.setFileType(UploadFileUtil.FILE_TYPE_EXCEL);
            uploadFile.setFileMaxSize(1024 * 1024 * 10);
            UploadFileUtil.valid(uploadFile);
            // 复制文件
            //File newFile = ManagerSalesDeptChangeUtil.createFile(file, request);
            // 创建excel工作表
            InputStream in = new BufferedInputStream(file.getInputStream());
            Workbook workbook = WorkbookFactory.create(in);
            // 创建导入数据模板
            ExcelTemplet excelTemplet = new ExcelTemplet().new ManagerSalesDeptInputExcel();
            // 文件数据转换为List集合
            List<Map<String, String>> sheetDataList = ExcelUtil.getExcelData(workbook, excelTemplet);
            // 导入文件的数据为空则提示错误信息
            Assert.notCollectionsEmpty(sheetDataList, ResponseEnum.FILE_EMPTY_FILE,"");
            // 循环校验、变更管理营业部
            for (Map<String, String> sheetData:sheetDataList) {
                this.processOneLine(sheetData);
            }
            // 在excel文件的每行末尾追加单元格提示信息
            ExcelUtil.addResultToWorkBook(workbook, sheetDataList, excelTemplet);
            //response.reset();
            response.setHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(file.getOriginalFilename(), "UTF-8"));
            response.setContentType(file.getContentType());
            out = response.getOutputStream();
            workbook.write(out);
            out.flush();
            return;
        } catch (PlatformException e) {
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),e.getMessage());
        } catch (Exception e) {
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
        }finally {
            /*if(null!=out){
                try {
                    out.close();
                } catch (IOException e) {
                    responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
                }
            }*/
        }
        try {
            response.setContentType("text/html");
            response.getWriter().write(toResponseJSON(responseInfo));
        } catch (IOException e) {
        }
    }
    
    /**
     * 校验导入文件数据、更新债权相关信息
     * @param map
     */
    private void processOneLine(Map<String, String> map) {
        // 校验一条记录的各个字段是否合法
        Map<String,Object> checkResultMap = this.praseAndCheckValue(map);
        // 校验返回的错误信息
        String errorMsg = map.get(ExcelTemplet.FEED_BACK_MSG);
        if (Strings.isNotEmpty(errorMsg)) {
            return;
        }
        
        // 查询借款人的债权信息
        VLoanInfo loanInfo = (VLoanInfo) checkResultMap.get("loanInfo");
        // 营业机构信息
        ComOrganization organization = (ComOrganization) checkResultMap.get("organization");
        // 更新债权信息、记录门店变更日志
        this.updateLoanManageSalesDept(loanInfo,organization,map);
        
        errorMsg = map.get(ExcelTemplet.FEED_BACK_MSG);
        if (Strings.isNotEmpty(errorMsg)) {
            return;
        }
        map.put(ExcelTemplet.FEED_BACK_MSG, "变更成功");
    }

    /**
     * 
     * 校验上传文件的各个字段的值是否合法
     * @param map
     * @return
     */
    private Map<String, Object> praseAndCheckValue(Map<String, String> map) {
        // 借款人姓名
        String name = Strings.convertValue(map.get("name"), String.class);
        // 借款人身份证号码
        String idNum = Strings.convertValue(map.get("idNum"), String.class);
        // 借款类型
        String loanType = Strings.convertValue(map.get("loanType"),String.class);
        // 签约日期
        String signDate = Strings.convertValue(map.get("signDate"),String.class);
        // 营业机构名称
        String manageSalesDepName = Strings.convertValue(map.get("manageSalesDept"), String.class);
        //合同编号
        String contractNum = Strings.convertValue(map.get("contractNum"), String.class);
        VLoanInfo loanInfo = null;
        ComOrganization organization = null;
        try {
            // 导入字段非空校验
            Assert.notNullAndEmpty(name, "姓名");
            Assert.notNullAndEmpty(idNum, "身份证");
            Assert.notNullAndEmpty(loanType, "借款类型");
            Assert.notNullAndEmpty(signDate, "签约日期");
            Assert.notNullAndEmpty(manageSalesDepName, "管理营业部");
            Assert.notNullAndEmpty(contractNum, "合同编号");

            // 是否是合法的借款类型
            Assert.validEnum(LoanTypeEnum.class, loanType, "借款类型有误");

            // 是否是正确的日期格式
            Assert.notDate(signDate, "yyyy-MM-dd", "签约时间格式错误，应类似2014-07-01");

            // 查询借款人债权信息
            Map<String, Object> loanParams = new HashMap<String, Object>();
            loanParams.put("idNum", idNum.trim());
            loanParams.put("name", name.trim());
            loanParams.put("loanType", loanType.trim());
            loanParams.put("signDate", signDate.trim());
            loanParams.put("contractNum", contractNum.trim());
            loanInfo = vLoanInfoService.getLoanByIdnumAndName(loanParams);
            // 是否存在借款信息
            Assert.notNull(loanInfo, "无法找到指定借款信息");

            // 根据营业机构名称查询营业机构信息
            ComOrganization comOrganization = new ComOrganization();
            comOrganization.setName(manageSalesDepName.trim());
            List<ComOrganization> organizationList = comOrganizationService.findListByVo(comOrganization);
            // 是否存在指定的营业部
            Assert.notCollectionsEmpty(organizationList, "无法找到指定营业部");

            // 至少存在一个营业机构，就取第一个，正常情况下按名字查询营业机构最多存在一个营业机构
            organization = organizationList.get(0);
            /*if ("电销".equals(organization.getDepartmentType())) {
                throw new PlatformException(ResponseEnum.FULL_MSG,"无法变更为电销的营业部");
            }*/
            
            // 借款状态
            String loanStatus = loanInfo.getLoanState();
            if (!LoanStateEnum.正常.getValue().equals(loanStatus)
                    && !LoanStateEnum.逾期.getValue().equals(loanStatus)
                    && !LoanStateEnum.结清.getValue().equals(loanStatus)
                    && !LoanStateEnum.预结清.getValue().equals(loanStatus)
                    && !LoanStateEnum.坏账.getValue().equals(loanStatus)) {
                throw new PlatformException(ResponseEnum.FULL_MSG,"借款必须处于正常、逾期、结清、预结清、坏账状态");
            }
        } catch (PlatformException e) {
            map.put(ExcelTemplet.FEED_BACK_MSG, e.getMessage());
        } catch (Exception e) {
            map.put(ExcelTemplet.FEED_BACK_MSG, "发生了不可预知的异常");
        }
        return editCheckResultInfo(map, loanInfo, organization);
    }
    
    /**
     * 编辑校验结果信息
     * @param map
     * @param borrowerLoanInfoMap
     * @param organization
     * @return
     */
    private Map<String, Object> editCheckResultInfo(Map<String,String> map,VLoanInfo loanInfo,ComOrganization organization) {
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("paramMap", map);
        resultMap.put("loanInfo", loanInfo);
        resultMap.put("organization", organization);
        return resultMap;
    }
    
    /**
     * 更新债权信息、记录门店变更日志
     * @param loanInfo
     * @param organization
     * @param map
     * @return
     * @throws Exception
     */
    private void updateLoanManageSalesDept(VLoanInfo loanInfo,ComOrganization organization, Map<String, String> map){
        // 营业机构ID
        Long orgId = organization.getId();
        // 机构匹配码
        String orgCode = organization.getOrgCode();
        // 根据营业机构Id和角色名查询客服信息
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("orgCode", orgCode);
        params.put("employeeType", "管理人员");
        try {
            ComEmployee adminEmployee = comEmployeeService.queryEmployeeByOrgIdAndRoleName(params);
            // 指定的营业部是否有管理人员
            Assert.notNull(adminEmployee, "新指定的管理营业部没有管理人员,无法自动分配客服");
            // 设置新的管理部门
            loanInfo.setSalesDepartmentId(orgId);
            // 暂存旧的客服
            Long oldCrmId = loanInfo.getCrmId();
            // 设置新的客服
            loanInfo.setCrmId(adminEmployee.getId());
            /**
             * 更新借款人债权信息（包括更新债权信息所属的管理营业部及借款人所属的管理营业部）
             * 更新最近一条loan_manage_sales_dep_log表的记录（结束时间修改为当前时间）并记录此表的一条记录
             */
            boolean isSuccess = salesDeptRepayInfoService.updateBorrowerLoanInfo(loanInfo,oldCrmId);
            if (!isSuccess) {
                throw new PlatformException(ResponseEnum.FULL_MSG, "借款信息更新失败");
            }
        } catch (PlatformException e) {
            map.put(ExcelTemplet.FEED_BACK_MSG, e.getMessage());
        } catch (Exception e) {
            map.put(ExcelTemplet.FEED_BACK_MSG, "发生了不可预知的异常");
        }
    }
    
    /**
     * 编辑借款状态
     * @return
     */
    private List<String> getLoanStates(){
        List<String> loanStates = new ArrayList<String>();
        loanStates.add(LoanStateEnum.正常.getValue());
        loanStates.add(LoanStateEnum.逾期.getValue());
        loanStates.add(LoanStateEnum.结清.getValue());
        loanStates.add(LoanStateEnum.预结清.getValue());
        loanStates.add(LoanStateEnum.坏账.getValue());
        return loanStates;
    }
}
