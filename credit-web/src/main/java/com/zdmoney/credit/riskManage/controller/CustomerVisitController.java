package com.zdmoney.credit.riskManage.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zdmoney.credit.common.util.*;
import com.zdmoney.credit.common.util.excel.ExcelTemplet;
import com.zdmoney.credit.common.util.excel.ExcelUtil;
import com.zdmoney.credit.common.util.file.UploadFileUtil;
import com.zdmoney.credit.common.util.file.vo.UploadFile;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import com.zdmoney.credit.offer.domain.OfferRepayInfo;
import com.zdmoney.credit.system.domain.ComMessageIn;
import com.zdmoney.credit.system.domain.PersonInfo;
import com.zdmoney.credit.system.service.pub.IPersonInfoService;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.zdmoney.credit.common.constant.LoanStateEnum;
import com.zdmoney.credit.common.constant.LoanTypeEnum;
import com.zdmoney.credit.common.constant.SysActionLogTypeEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.vo.AttachmentResponseInfo;
import com.zdmoney.credit.common.vo.ResponseInfo;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.person.domain.PersonTelInfo;
import com.zdmoney.credit.person.service.pub.IPersonTelInfoService;
import com.zdmoney.credit.riskManage.service.pub.IPersonVisitService;
import com.zdmoney.credit.riskManage.vo.VPersonVisit;

@Controller
@RequestMapping("/riskManage")
public class CustomerVisitController extends BaseController {
    
    /**
     * 保存客户回访信息url
     */
    private final static String URL_SAVE_CUSTOMER_VISIT_INFO = "/riskManage/saveCustomerVisitInfo";
    
    @Autowired
    private IPersonVisitService personVisitService;
    
    @Autowired
    private IPersonTelInfoService personTelInfoService;

    @Autowired
    private IVLoanInfoService vLoanInfoService;
    @Autowired
    private IPersonInfoService personInfoService;
    
    /**
     * 初始化页面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/customerVisit")
    public ModelAndView jumpPage(HttpServletRequest request,HttpServletResponse response) {
        createLog(request, SysActionLogTypeEnum.查询, "客户回访查询", "加载客户回访查询页面");
        ModelAndView mav = new ModelAndView();
        mav.setViewName("riskManage/customerVisit");
        // 系统时间
        String currentDate = Dates.getDateTime(new Date(), "yyyy-MM-dd");
        mav.addObject("currentDate", currentDate);
        mav.addObject("loanTypes", LoanTypeEnum.values());
        // 回访管理页面点击回访记录按钮，跳转至客户回访查询页面，并根据返回的债权Id查询页面数据
        String loanId = request.getParameter("loanId");
        if(Strings.isNotEmpty(loanId)){
            mav.addObject("loanId", loanId);
            mav.addObject("currentDate", null);
        }
        return mav;
    }
    
    /**
     * 客户回访信息查询
     * @param performanceBelongInfo
     * @param rows
     * @param page
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/customerVisitSearch")
    @ResponseBody
    public String search(VPersonVisit personVisit, int rows,int page, HttpServletRequest request, HttpServletResponse response) {
        createLog(request, SysActionLogTypeEnum.查询, "客户回访查询", "查询客户回访信息");
        Pager pager = new Pager();
        pager.setRows(rows);
        pager.setPage(page);
        personVisit.setPager(pager);
        // 调用Service层查询客户回访信息
        pager = personVisitService.findWithPg(personVisit);
        // 将数据对象转换成JSON字符串，返回给前台
        return toPGJSONWithCode(pager);
    }
    
    /**
     * 初始化回访管理页面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/visitManage")
    public ModelAndView loadPage(HttpServletRequest request,HttpServletResponse response) {
        createLog(request, SysActionLogTypeEnum.查询, "回访管理", "加载回访管理页面");
        ModelAndView mav = new ModelAndView();
        mav.setViewName("riskManage/visitManage");
        // 获取当前登录用户的信息
        User user = UserContext.getUser();
        // 是否有保存客户回访信息的权限
        boolean isCanSaveCustomerVisitInfo = user.ifAnyGranted(URL_SAVE_CUSTOMER_VISIT_INFO);
        // 系统时间
        String currentDate = Dates.getDateTime(new Date(), "yyyy-MM-dd");
        mav.addObject("currentDate", currentDate);
        mav.addObject("loanTypes", LoanTypeEnum.values());
        mav.addObject("isCanSaveCustomerVisitInfo", String.valueOf(isCanSaveCustomerVisitInfo));
        mav.addObject("user", user);
        return mav;
    }
    
    /**
     * 查询回访管理信息
     * @param personVisit
     * @param rows
     * @param page
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/visitManageSearch")
    @ResponseBody
    public String visitManageSearch(VPersonVisit personVisit, int rows,int page, HttpServletRequest request, HttpServletResponse response) {
        createLog(request, SysActionLogTypeEnum.查询, "回访管理", "查询回访管理信息");
        personVisit.setLoanStates(new String[] { LoanStateEnum.正常.getValue(),
                LoanStateEnum.逾期.getValue(), LoanStateEnum.结清.getValue(),
                LoanStateEnum.预结清.getValue() });
        // 定义分页实例
        Pager pager = new Pager();
        pager.setRows(rows);
        pager.setPage(page);
        personVisit.setPager(pager);
        // 调用Service层查询客户回访信息
        pager = personVisitService.findVisitManageInfoWithPg(personVisit);
        // 将数据对象转换成JSON字符串，返回给前台
        return toPGJSONWithCode(pager);
    }
    
    /**
     * 加载客户回访编辑页面
     * @param loanId
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/loadEditCustomerVisitOld")
    public ModelAndView loadVisitEditPage(Long loanId,HttpServletRequest request, HttpServletResponse response) {
        createLog(request, SysActionLogTypeEnum.查询, "客户回访编辑", "加载客户回访编辑页面");
        VPersonVisit custLaon = null;
        VPersonVisit custVisit = null;
        // 获取当前登录用户的信息
        User user = UserContext.getUser();
        VPersonVisit personVisit = new VPersonVisit();
        personVisit.setLoanId(loanId);
        // 查询回访管理信息
        List<VPersonVisit> visitManageList = personVisitService.findVisitManagesByVo(personVisit);
        if (CollectionUtils.isNotEmpty(visitManageList)) {
            custLaon = visitManageList.get(0);
        }
        PersonTelInfo personTelInfo = new PersonTelInfo();
        personTelInfo.setObjectId(custLaon.getBorrowerId());
        //personTelInfo.setClassName("zdsys.Borrower");
        personTelInfo.setValid("t");
        // 查询客户电话信息
        List<PersonTelInfo> personTelInfoList = personTelInfoService.findListByVo(personTelInfo);
        // 查询客户回访记录信息
        List<VPersonVisit> personVisitList = personVisitService.findListByVO(personVisit);
        if (CollectionUtils.isNotEmpty(personVisitList)) {
            custVisit = personVisitList.get(0);
            if (CollectionUtils.isNotEmpty(personTelInfoList)){
                for(PersonTelInfo telInfo: personTelInfoList){
                    if("手机".equals(telInfo.getTelType())){
                        custVisit.setTel(telInfo.getContent());
                    }
                }
            }
        }
        ModelAndView mav = new ModelAndView();
        mav.setViewName("riskManage/editCustomerVisit");
        mav.addObject("loan", custLaon);
        mav.addObject("personVisit", custVisit);
        mav.addObject("personTelInfos", personTelInfoList);
        mav.addObject("channels", new String[]{"朋友介绍","销售电话","手机短信","网络广告","其它"});
        mav.addObject("sAttitudes", new String[]{"非常满意","满意","一般","不满意","其它"});
        mav.addObject("user", user);
        return mav;
    }
    
    /**
     * 加载客户编辑窗口
     * @param loanId
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/loadEditCustomerVisit")
    @ResponseBody
    public String loadCustVisitEditPage(Long loanId, HttpServletRequest request, HttpServletResponse response) {
        AttachmentResponseInfo<Map<String,Object>> responseInfo = new AttachmentResponseInfo<Map<String,Object>>(ResponseEnum.SYS_SUCCESS);
        Map<String,Object> resultMap = new HashMap<String,Object>();
        VPersonVisit custLaon = null;
        VPersonVisit custVisit = null;
        // 获取当前登录用户的信息
        User user = UserContext.getUser();
        VPersonVisit personVisit = new VPersonVisit();
        personVisit.setLoanId(loanId);
        // 查询回访管理信息
        List<VPersonVisit> visitManageList = personVisitService.findVisitManagesByVo(personVisit);
        if (CollectionUtils.isNotEmpty(visitManageList)) {
            custLaon = visitManageList.get(0);
        }
        PersonTelInfo personTelInfo = new PersonTelInfo();
        personTelInfo.setObjectId(custLaon.getBorrowerId());
        personTelInfo.setValid("t");
        // 查询客户电话信息
        List<PersonTelInfo> personTelInfoList = personTelInfoService.findListByVo(personTelInfo);
        // 查询客户回访记录信息
        List<VPersonVisit> personVisitList = personVisitService.findListByVO(personVisit);
        if (CollectionUtils.isNotEmpty(personVisitList)) {
            custVisit = personVisitList.get(0);
            if (CollectionUtils.isNotEmpty(personTelInfoList)){
                for(PersonTelInfo telInfo: personTelInfoList){
                    if("手机".equals(telInfo.getTelType())){
                        custVisit.setTel(telInfo.getContent());
                    }
                }
            }
        }
        resultMap.put("loan", custLaon);
        resultMap.put("personVisit", custVisit);
        resultMap.put("personTelInfos", personTelInfoList);
        resultMap.put("user", user);
        responseInfo.setAttachment(resultMap);
        return toResponseJSON(responseInfo);
    }
    
    /**
     * 保存客户回访记录
     * @param personVisit
     * @return
     */
    @RequestMapping("/saveCustomerVisitInfo")
    @ResponseBody
    public String saveCustomerVisitInfo(VPersonVisit personVisit) {
        ResponseInfo responseInfo = null;
        // 获取当前登录用户的信息
        User user = UserContext.getUser();
        personVisit.setCreatorId(user.getId());
        if(Strings.isEmpty(personVisit.getAdditionalCharges())){
            personVisit.setAdditionalCharges("f");
        }
        
        // 如果是手动输入电话号码
        if (Strings.isNotEmpty(personVisit.getDialMode())
                && Strings.isNotEmpty(personVisit.getPhone())) {
            String phone = personVisit.getPhone();
            if (phone.length() > 2 && "90".equals(phone.substring(0, 2))) {
                phone = phone.substring(2);
            } else if (phone.length() > 2 && "9".equals(phone.substring(0, 1))) {
                phone = phone.substring(1);
            }
            personVisit.setTel(phone);
        }
        try {
            if(Strings.isEmpty(personVisit.getChannel())){
                throw new PlatformException(ResponseEnum.FULL_MSG,"渠道为必选项!");
            }
            if(Strings.isEmpty(personVisit.getsAttitude())){
                throw new PlatformException(ResponseEnum.FULL_MSG,"服务态度为必选项!");
            }
            // 保存客户回访记录
            personVisitService.saveCustomerVisitInfo(personVisit);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_SUCCESS);
        } catch (PlatformException e) {
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),e.getMessage());
        } catch (Exception e) {
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
        }
        return toResponseJSON(responseInfo);
    }

    /**
     * 初始化页面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/acountLoanManagePage")
    public ModelAndView acountLoanManagePage(HttpServletRequest request,HttpServletResponse response) {
        createLog(request, SysActionLogTypeEnum.查询, "账务债权操作", "加载账务债权操作页面");
        ModelAndView mav = new ModelAndView();
        mav.setViewName("riskManage/acountLoanManagePage");
        return mav;
    }

    /**
     * 查询账务债权操作信息
     * @param personVisit
     * @param rows
     * @param page
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/acountLoanManageSearch")
    @ResponseBody
    public String acountLoanManageSearch(VPersonVisit personVisit, int rows,int page, HttpServletRequest request, HttpServletResponse response) {
        createLog(request, SysActionLogTypeEnum.查询, "账务债权操作", "查询账务债权操作信息");
        // 定义分页实例
        Pager pager = new Pager();
        pager.setRows(rows);
        pager.setPage(page);
        personVisit.setPager(pager);
        pager = vLoanInfoService.findAcountLoanManageWithPg(personVisit);
        // 将数据对象转换成JSON字符串，返回给前台
        return toPGJSONWithCode(pager);
    }

    /**
     * 账务债权操作-取消
     * @param loanId
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/cancelAssignState/{loanId}")
    @ResponseBody
    public String cancelAssignState(@PathVariable long loanId,HttpServletRequest request, HttpServletResponse response) {
        createLog(request, SysActionLogTypeEnum.查询, "账务债权操作", "取消-账务债权操作");
        ResponseInfo responseInfo = null;
        try{
            Assert.notNull(loanId,"债权编号");
            /** 登陆者信息 **/
            User user = UserContext.getUser();
            String updator = user == null ? "admin" : user.getName();
            Map<String, Object> params = new HashMap<>();
            params.put("loanId", loanId);
            params.put("updator",updator);
            params.put("assignState","0");
            vLoanInfoService.updateLoanInfExtByLoanId(params);
            responseInfo = new AttachmentResponseInfo(ResponseEnum.SYS_SUCCESS);
        }catch(PlatformException e){
            logger.error("账务债权操作取消异常",e);
            responseInfo = new AttachmentResponseInfo<>(ResponseEnum.FULL_MSG,e.getMessage());
        }catch(Exception e){
            logger.error("账务债权操作取消异常",e);
            responseInfo = new AttachmentResponseInfo<>(ResponseEnum.SYS_FAILD);
        }
        // 将数据对象转换成JSON字符串，返回给前台
        return toResponseJSON(responseInfo);
    }

    /**
     * 还款录入 - Excel文件导入（正常还款、一次性提前还款）
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/importLoanInfExtFile")
    @ResponseBody
    public void importRepayInfoFile(@RequestParam(value = "uploadFile", required = false) MultipartFile file,
                                    HttpServletRequest request, HttpServletResponse response) throws IOException {
        ResponseInfo responseInfo = null;
        try {
            createLog(request, SysActionLogTypeEnum.导入, "还款录入", "批量导入还款录入操作");

            UploadFile uploadFile = new UploadFile();
            uploadFile.setFile(file);
            uploadFile.setFileType(UploadFileUtil.FILE_TYPE_EXCEL);
            uploadFile.setFileMaxSize(1024 * 1024 * 5);

            UploadFileUtil.valid(uploadFile);
            String fileName = file.getOriginalFilename();
            String contentType = file.getContentType();

            Workbook workBook = WorkbookFactory.create(file.getInputStream());

            ExcelTemplet excelTemplet = new ExcelTemplet().new RiskAccountLoanInputExcel();
            List<Map<String, String>> result = ExcelUtil.getExcelData(workBook, excelTemplet);

            Assert.notCollectionsEmpty(result, ResponseEnum.FILE_EMPTY_FILE, "");

            for (Map<String, String> map : result) {
                this.importRiskAccountLoanItem(map);
            }

            ExcelUtil.addResultToWorkBook(workBook, result, excelTemplet);
            /** 下载文件名 **/
            fileName = "账务债权操作导入结果" + Dates.getDateTime(Dates.DEFAULT_DATE_FORMAT) + ".xls";
            String enableFileName = new String(fileName.getBytes("GBK"), "ISO-8859-1");
            response.setHeader("Content-Disposition", "attachment; filename=" + enableFileName);
            response.setHeader("Content-Type", contentType);

            OutputStream outputStream = response.getOutputStream();
            workBook.write(outputStream);
            outputStream.flush();
            return;
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

    /**
     * 账务债权操作 导入
     * @param map
     */
    private void importRiskAccountLoanItem(Map<String, String> map) {
        String feedBackMsg = "";

        try{
            String name = map.get("name");
            String mobile = map.get("mobile");
            String idNum = map.get("idNum");
            String contractNum = map.get("contractNum");
            Assert.notNull(name,"客户姓名");
            Assert.notNull(mobile,"手机号码");
            Assert.notNull(idNum,"身份证号");
            Assert.notNull(contractNum,"合同号码");
            PersonInfo personInfo = personInfoService.findByIdNumAndName(name,idNum);
            if(personInfo == null){
                throw new PlatformException(ResponseEnum.FULL_MSG,"债权姓名,身份证不一致");
            }
            if(!personInfo.getMphone().equals(mobile)){
                throw new PlatformException(ResponseEnum.FULL_MSG,"债权姓名,手机号不一致");
            }
            VLoanInfo vo = new VLoanInfo();
            vo.setContractNum(contractNum);
            List<VLoanInfo> loanInfos= vLoanInfoService.findListByVO(vo);
            if(CollectionUtils.isEmpty(loanInfos)){
                throw new PlatformException(ResponseEnum.FULL_MSG,"未找到该合同编号债权");
            }
            VLoanInfo vLoanInfo = loanInfos.get(0);
            if(!vLoanInfo.getBorrowerId().equals(personInfo.getId())){
                throw new PlatformException(ResponseEnum.FULL_MSG,"债权姓名，合同号不一致");
            }
            vLoanInfoService.addLoanInfoExtByLoanId(vLoanInfo);
            feedBackMsg = "成功";
            map.put("feedBackMsg",feedBackMsg);
        }catch (PlatformException e){
            logger.error("账务债权操作导入异常",e);
            feedBackMsg = e.getMessage();
            map.put("feedBackMsg",feedBackMsg);
        }catch (Exception e){
            logger.error("账务债权操作导入异常",e);
            feedBackMsg = "程序异常";
            map.put("feedBackMsg",feedBackMsg);
        }
    }
}
