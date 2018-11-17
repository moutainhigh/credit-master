package com.zdmoney.credit.specialRepayment.controller;

import com.alibaba.fastjson.JSONObject;
import com.zdmoney.credit.common.constant.*;
import com.zdmoney.credit.common.constant.flow.*;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.tree.EasyUITree;
import com.zdmoney.credit.common.util.*;
import com.zdmoney.credit.common.util.coreUtil.JSONUtil;
import com.zdmoney.credit.common.util.excel.ExcelTemplet;
import com.zdmoney.credit.common.util.excel.ExcelUtil;
import com.zdmoney.credit.common.util.file.UploadFileUtil;
import com.zdmoney.credit.common.util.file.vo.UploadFile;
import com.zdmoney.credit.common.vo.AttachmentResponseInfo;
import com.zdmoney.credit.common.vo.ResponseInfo;
import com.zdmoney.credit.flow.domain.SpFlowInstance;
import com.zdmoney.credit.flow.domain.SpFlowTransitionRule;
import com.zdmoney.credit.flow.domain.SpNodeEmployee;
import com.zdmoney.credit.flow.domain.SpNodeInfo;
import com.zdmoney.credit.flow.service.pub.ISpFlowService;
import com.zdmoney.credit.flow.vo.SpFlowInstanceRequestParamVo;
import com.zdmoney.credit.flow.vo.SpFlowOperateInfoVo;
import com.zdmoney.credit.flow.vo.SpQueryFlowChartParamVo;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.loan.domain.SpecialRepaymentApply;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.*;
import com.zdmoney.credit.loan.vo.*;
import com.zdmoney.credit.offer.service.pub.IOfferInfoService;
import com.zdmoney.credit.offer.service.pub.IOfferRepayInfoService;
import com.zdmoney.credit.offer.vo.OfferRepayInfoVo;
import com.zdmoney.credit.repay.service.pub.ISalesDeptRepayInfoService;
import com.zdmoney.credit.system.domain.ComEmployee;
import com.zdmoney.credit.system.domain.PersonInfo;
import com.zdmoney.credit.system.service.pub.IComEmployeeService;
import com.zdmoney.credit.system.service.pub.IComMessageInService;
import com.zdmoney.credit.system.service.pub.IPersonInfoService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
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
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by ym10094 on 2017/6/21.
 */
@Controller
@RequestMapping("/applyReliefRepayManager")
public class ApplyReliefRepayManagerController extends BaseController {

    public static final Logger logger = LoggerFactory.getLogger(ApplyReliefRepayManagerController.class);

    @Autowired
    private ISalesDeptRepayInfoService salesDeptRepayInfoService;
    @Autowired
    private ISpecialRepaymentApplyService specialRepaymentApplyService;
    @Autowired
    private IVLoanInfoService vLoanInfoService;
    @Autowired
    private ILoanBaseService loanBaseService;
    @Autowired
    private IPersonInfoService personInfoService;
    @Autowired
    private IOfferRepayInfoService offerRepayInfoService;
    @Autowired
    private ILoanRepaymentDetailService loanRepaymentDetailService;
    @Autowired
    private IAfterLoanService afterLoanService;
    @Autowired
    private ISysParamDefineService sysParamDefineService;
    @Autowired
    private ISpFlowService spFlowService;
    @Autowired
    private IComEmployeeService comEmployeeService;
    @Autowired
    IComMessageInService comMessageInServiceImpl;
    @Autowired
    IComEmployeeService comEmployeeServiceImpl;
    @Autowired
    private IOfferInfoService offerInfoService;
    /**
     * 跳转到减免申请tabs
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/applyReliefRepayment")
    public ModelAndView jumpApplyReliefRepayment(HttpServletRequest request,HttpServletResponse response){
        ModelAndView modelAndView = new ModelAndView("/specialRepayment/applyReliefRepayment");
        return modelAndView;
    }

    /**
     * 跳转到申请减免待处理页面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("applyReliefProcessed")
    public ModelAndView jumpApplyReliefProcessed(HttpServletRequest request,HttpServletResponse response){
        ModelAndView modelAndView = new ModelAndView("/specialRepayment/applyReliefProcessed");
        // 获取当前登录用户的信息
        User userInfo = UserContext.getUser();
        String empNum = userInfo.getOrgCode();
        Map<String, Object> params = new HashMap<String, Object>();
        // 管理营业部网点
        params.put("empNum", empNum);
        params.put("vLevel", ComOrganizationEnum.Level.V104.name());
        List<Map<String, Object>>  salesTeamInfoList = salesDeptRepayInfoService.getSalesDeptInfo(params);
        //减免类型
        SpecialRepaymentApplyTeyps [] applyReliefTypes = {SpecialRepaymentApplyTeyps.一般减免,SpecialRepaymentApplyTeyps.结清减免};
        List<SpNodeInfo> nodeInfos = spFlowService.querySpApplyNodeInfos();
        modelAndView.addObject("salesTeamInfoList", salesTeamInfoList);
        modelAndView.addObject("applyReliefTypes",applyReliefTypes);
        modelAndView.addObject("applyDate", Dates.getDateTime("yyyy-MM-dd"));
        modelAndView.addObject("nodeInfos",nodeInfos);
        return modelAndView;
    }

    /**
     * 跳转到申请减免完成页面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("applyReliefFinish")
    public ModelAndView jumpApplyReliefFinish(HttpServletRequest request,HttpServletResponse response){
        ModelAndView modelAndView = new ModelAndView("/specialRepayment/applyReliefFinish");
        // 获取当前登录用户的信息
        User userInfo = UserContext.getUser();
        String empNum = userInfo.getOrgCode();
        Map<String, Object> params = new HashMap<String, Object>();
        // 管理营业部网点
        params.put("empNum", empNum);
        params.put("vLevel", ComOrganizationEnum.Level.V104.name());
        List<Map<String, Object>>  salesTeamInfoList = salesDeptRepayInfoService.getSalesDeptInfo(params);
        //减免类型
        SpecialRepaymentApplyTeyps [] applyReliefTypes = {SpecialRepaymentApplyTeyps.一般减免,SpecialRepaymentApplyTeyps.结清减免};
        SpecialRepaymentApplyStatus [] applyStatuses = {SpecialRepaymentApplyStatus.取消,SpecialRepaymentApplyStatus.失效,
                SpecialRepaymentApplyStatus.完成,SpecialRepaymentApplyStatus.拒绝,SpecialRepaymentApplyStatus.生效,SpecialRepaymentApplyStatus.通过};
        SpecialReliefTypeEnum [] specialReliefTypeEnums = {SpecialReliefTypeEnum.特殊减免,SpecialReliefTypeEnum.非特殊减免};
        modelAndView.addObject("salesTeamInfoList", salesTeamInfoList);
        modelAndView.addObject("applyReliefTypes",applyReliefTypes);
        modelAndView.addObject("applyStatuses",applyStatuses);
        modelAndView.addObject("applyDate", Dates.getDateTime("yyyy-MM-dd"));
        modelAndView.addObject("specialReliefTypes",specialReliefTypeEnums);
        return modelAndView;
    }

    /**
     * 分页
     * @param rows
     * @param page
     * @param status
     * @param request
     * @param response
     * @param applyReliefVo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "applyReliefPage/{status}/{action}")
    public String applyReliefPage(int rows, int page,@PathVariable String status,@PathVariable String action,HttpServletRequest request,HttpServletResponse response,ApplyReliefVo applyReliefVo){
        Map<String,Object> params = new HashMap<>();
        Pager pager = new Pager();
        try {
            this.createLog(request, SysActionLogTypeEnum.查询, "申请减免管理", "查询申请减免的记录");
            if (Strings.isEmpty(status)) {
                throw new PlatformException(ResponseEnum.FULL_MSG,"请求路径错误！");
            }
            // 获取当前登录用户的信息
            User userInfo = UserContext.getUser();
            if (SpecialRepaymentApplyStatus.申请.getCode().equals(status)) {
                params.put("applicationStatus",SpecialRepaymentApplyStatus.申请.getCode());
                if ("approve".equals(action)) {
                    params.put("approveStep",true);
                }
                if ("apply".equals(action)) {
                    params.put("showApprpveNodeName",true);
                    params.put("nodeId",applyReliefVo.getNodeId());
                }
            }else if(Strings.isEmpty(applyReliefVo.getApplicationStatus())){
                if ("approve".equals(action)) {
                    params.put("approveFinish",true);
                }else {
                    params.put("applicationStatusList", new String[]{SpecialRepaymentApplyStatus.生效.getCode(), SpecialRepaymentApplyStatus.取消.getCode(), SpecialRepaymentApplyStatus.失效.getCode(),
                            SpecialRepaymentApplyStatus.通过.getCode(), SpecialRepaymentApplyStatus.完成.getCode(), SpecialRepaymentApplyStatus.拒绝.getCode()});
                }
            }
            if(Strings.isNotEmpty(applyReliefVo.getApplicationStatus())){
                params.put("applicationStatus",applyReliefVo.getApplicationStatus());
            }
            params.put("contractNum",applyReliefVo.getContractNum());
            params.put("name",applyReliefVo.getName());
            params.put("idNum",applyReliefVo.getIdNum());
            params.put("applyType",applyReliefVo.getApplyReliefType());
            params.put("startApplyDate",Dates.getDateTime(applyReliefVo.getStartApplyDate(),Dates.DEFAULT_DATE_FORMAT));
            params.put("endApplyDate",Dates.getDateTime(Dates.addDay(applyReliefVo.getEndApplyDate(), 1),Dates.DEFAULT_DATE_FORMAT));
            params.put("employeeId",userInfo.getId());
            params.put("isSpecial",applyReliefVo.getSpecialReliefFlag());
            //逾期管理室（余刚、寇学刘） --针对已停业的营业部

            if(isCSYQGLSRole(userInfo)) {
                if (SpecialRepaymentApplyStatus.申请.getCode().equals(status) && "apply".equals(action)) {
                    params.put("isCSYQGLSRole", true);
                }
            }else {
                 if (SpecialRepaymentApplyStatus.申请.getCode().equals(status) && "apply".equals(action)){
                    params.put("isCSYQGLSRole", false);
                }
                //管理营业部---非停业营业部
                String salesDepartmentId = applyReliefVo.getSalesDepartmentId();
                if (Strings.isEmpty(salesDepartmentId)) {

                    String orgCode = userInfo.getOrgCode();
                    params.put("orgCode", orgCode);
                } else {
                    params.put("orgId", salesDepartmentId);
                }
            }

            pager.setRows(rows);
            pager.setPage(page);
            params.put("pager",pager);
            //查询条件限制
            pager = specialRepaymentApplyService.queryAppltReliefPage(params);
        }catch (PlatformException e){
            return  JSONObject.toJSONString(new AttachmentResponseInfo<Map<String, Object>>(ResponseEnum.FULL_MSG.getCode(), e.getMessage()));
        }catch (Exception e){
            e.printStackTrace();
            return  JSONObject.toJSONString(new AttachmentResponseInfo<Map<String, Object>>(ResponseEnum.FULL_MSG.getCode(), "查询异常"));
        }
        return toPGJSONWithCode(pager);
    }

    /**
     * 校验申请减免处理
     * @param request
     * @param response
     * @param applyReliefVo
     * @return
     */
    @ResponseBody
    @RequestMapping("/applyReliefCheck")
    public String applyReliefCheck(HttpServletRequest request,HttpServletResponse response,ApplyReliefVo applyReliefVo){
        ResponseInfo responseInfo = null;
        try {
            this.createLog(request, SysActionLogTypeEnum.其他, "申请减免校验", "申请减免校验");
            logger.info("申请减免校验，获取校验参数为：{}", JSONUtil.toJSON(applyReliefVo));
            VloanPersonInfo vloanPersonInfo = specialRepaymentApplyService.checkLoanPersonInfo(applyReliefVo.getContractNum(), applyReliefVo.getIdNum(), applyReliefVo.getName());
            SpecialRepaymentApplyVo specialRepaymentApplyVo = new SpecialRepaymentApplyVo();
            specialRepaymentApplyVo.setLoanId(vloanPersonInfo.getId());
            specialRepaymentApplyVo.setApplyType(applyReliefVo.getApplyReliefType());
            specialRepaymentApplyService.checkOutSpecialRepaymentApply(specialRepaymentApplyVo);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_SUCCESS.getCode(),"减免申请校验通过");
        } catch (PlatformException pe){
            logger.error(pe.getMessage());
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),pe.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),e.getMessage());
        }
        return toResponseJSON(responseInfo);
    }

    /**
     * 申请减免页面
     * @param request
     * @param response
     * @param applyReliefVo
     * @return
     */
    @RequestMapping("/jumpApplyReliefInfo/{type}")
    public ModelAndView jumpApplyReliefInfo(HttpServletRequest request,HttpServletResponse response,ApplyReliefVo applyReliefVo,@PathVariable String type){
        ModelAndView modelAndView = new ModelAndView("/specialRepayment/applyReliefInfo");
        try {
            if (Strings.isEmpty(type) && "1,2".indexOf(type)== -1) {
                throw new PlatformException(ResponseEnum.FULL_MSG,"请求路径错误！");
            }
        /*获取基础信息*/
            Long loanId = loanBaseService.findLoanIdByContractNum(applyReliefVo.getContractNum());
            VLoanInfo vLoanInfo = vLoanInfoService.findByLoanId(loanId);
            /** 借款人信息**/
            PersonInfo personInfo = personInfoService.findById(vLoanInfo.getBorrowerId());
            vLoanInfo.setPersonInfo(personInfo);
            Date tradeDate = Dates.getCurrDate();
//        计算出罚息 逾期 罚息起始日等信息
            OfferRepayInfoVo offerRepayInfoVo = offerRepayInfoService.getRepayInfo(loanId, tradeDate);
            if (offerRepayInfoVo == null) {
                throw new PlatformException(ResponseEnum.FULL_MSG, "无借款数据或已结清");
            }
            //还款等级
            Map levelMap = new HashMap();
            levelMap.put("loanId", loanId);
            String repaymentLevel = loanRepaymentDetailService.findRepaymentLevel(levelMap);
            offerRepayInfoVo.setRepaymentLevel(repaymentLevel);
            //获取计算信息
            modelAndView.addObject("vLoanInfo", vLoanInfo);
            modelAndView.addObject("offerRepayInfo", offerRepayInfoVo);
            modelAndView.addObject("repaymentLevel", repaymentLevel);
            //已还期数
            int repayTime = Integer.valueOf(vLoanInfo.getTime().toString()) - vLoanInfo.getResidualTime();
            //已还金额
            BigDecimal repayAmount = specialRepaymentApplyService.calculateHistoryAlreadyRepayTotalMoney(vLoanInfo.getId());
            //历史逾期次数
            int overdueTime = specialRepaymentApplyService.historyOverDueTime(vLoanInfo.getId());
            //历史减免次数
            int reliefTime = specialRepaymentApplyService.historyReliefTime(vLoanInfo.getId());
            //历史减免金额
            BigDecimal reliefAmount = specialRepaymentApplyService.historyReliefAmount(vLoanInfo.getId());
            //减免类型
            String reliefType = applyReliefVo.getApplyReliefType();
            String specialReliefFlag = applyReliefVo.getSpecialReliefFlag();
            //每月应还
            BigDecimal returnem = afterLoanService.getPerReapyAmount(new Date(), vLoanInfo.getId());
            RelieCalculateAmountParamVo relieCalculateAmountParamVo = new RelieCalculateAmountParamVo();
            relieCalculateAmountParamVo.setLoanId(loanId);
            relieCalculateAmountParamVo.setTradeDate(Dates.getCurrDate());
            ReliefAmountCalculateVo reliefAmountCalculateVo = specialRepaymentApplyService.getRelieCalculateAmount(relieCalculateAmountParamVo);
            //最大减免金额
            BigDecimal maxReleifMoney;
            //规则内减免金额
            BigDecimal ruleInMaxReliefAmount = new BigDecimal(0.0);
            if (SpecialReliefTypeEnum.特殊减免.getCode().equals(specialReliefFlag)) {
                if (Strings.isEmpty(reliefType)) {
                    reliefType = afterLoanService.isOneTimeRepayment(loanId) ? SpecialRepaymentApplyTeyps.结清减免.getCode() : SpecialRepaymentApplyTeyps.一般减免.getCode();
                }
                maxReleifMoney = offerRepayInfoVo.getCurrAllAmount();
                if ((SpecialRepaymentApplyTeyps.一般减免.getCode().equals(reliefType) && LoanStateEnum.逾期.getValue().equals(vLoanInfo.getLoanState())) ||
                        (SpecialRepaymentApplyTeyps.一般减免.getCode().equals(reliefType) && LoanStateEnum.正常.getValue().equals(vLoanInfo.getLoanState())
                                &&!(tradeDate.compareTo(offerRepayInfoVo.getCurrDate()) == 0 || afterLoanService.isAdvanceRepayment(loanId)))) {
                    offerRepayInfoVo.setCurrAllAmount(offerRepayInfoVo.getOverdueAmount());
                    maxReleifMoney = offerRepayInfoVo.getOverdueAmount();
                }
            } else {
                maxReleifMoney = specialRepaymentApplyService.calculateMaxReliefMoney(reliefAmountCalculateVo, reliefType,null);
                //针对一般减免
                if (SpecialRepaymentApplyTeyps.一般减免.getCode().equals(reliefType) && tradeDate.compareTo(offerRepayInfoVo.getCurrDate()) != 0) {
                    //当前时间是否是还款日 如果不是还款日 当前应还 不含 当前
                    offerRepayInfoVo.setCurrAllAmount(offerRepayInfoVo.getOverdueAmount());
                }

                if (SpecialRepaymentApplyTeyps.结清减免.getCode().equals(reliefType)) {
                    ruleInMaxReliefAmount = specialRepaymentApplyService.calculateRuleInMaxReliefAmount(loanId);
                }
            }
            //减免类型枚举对象
            SpecialRepaymentApplyTeyps specialRepaymentApplyTeyps = EnumUtil.getEnum(SpecialRepaymentApplyTeyps.class, reliefType, "code");
            modelAndView.addObject("repayTime", repayTime);
            modelAndView.addObject("repayAmount", repayAmount);
            modelAndView.addObject("overdueTime", overdueTime);
            modelAndView.addObject("reliefTime", reliefTime);
            modelAndView.addObject("reliefAmount", reliefAmount);
            modelAndView.addObject("reliefTypeEnum", specialRepaymentApplyTeyps);
            modelAndView.addObject("maxReleifMoney", maxReleifMoney);
            modelAndView.addObject("returnem", returnem);
            modelAndView.addObject("ruleInMaxReliefAmount", ruleInMaxReliefAmount);
            modelAndView.addObject("specialReliefFlag", specialReliefFlag);
        }catch (Exception e){
            logger.info("跳转到申请减免详情页面异常：{}",e.getMessage());
            modelAndView = new ModelAndView("error/error");
            modelAndView.addObject("errMsg", e.getMessage());
        }
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping("queryApproveFlowChart")
    public String queryApproveFlowChart(HttpServletRequest request, HttpServletResponse response, SpQueryFlowChartParamVo chartParamVo) {
        AttachmentResponseInfo responseInfo = null;
        try {
            User userInfo = UserContext.getUser();
            //逾期天数
            int overDueDay = 0;
            if (Strings.isNotEmpty(chartParamVo.getOverDueDate())) {
                overDueDay = Dates.dateDiff(Dates.getCurrDate(), Dates.parse(chartParamVo.getOverDueDate(),Dates.DEFAULT_DATE_FORMAT));
            }
            String reliefType = chartParamVo.getApplyType();
            //逾期等级
            OverDueGradeEnum overDueGradeEnum = specialRepaymentApplyService.queryOverDueGrade(reliefType, overDueDay, chartParamVo.isRuleIn());
            //部门
            DepartmentTypeEnum departmentTypeEnum = getDepartmentType(userInfo);
            //减免金额等级
            ReliefAmountGradeEnum reliefAmountGradeEnum;
            if (SpecialRepaymentApplyTeyps.一般减免.getCode().equals(reliefType)) {
                reliefAmountGradeEnum = specialRepaymentApplyService.getReliefAmountGrade(chartParamVo.getApplyReliefAmount(),chartParamVo.getMaxReliefAmount(),overDueGradeEnum);
            } else {
                reliefAmountGradeEnum = specialRepaymentApplyService.getOneTimeReliefAmountGrade(chartParamVo.getApplyReliefAmount(),
                        chartParamVo.getFine(),chartParamVo.getRuleInMaxReliefAmount());
            }
            //减免类型枚举对象
            SpecialRepaymentApplyTeyps specialRepaymentApplyTeyps = EnumUtil.getEnum(SpecialRepaymentApplyTeyps.class, reliefType, "code");
            SpFlowInstanceRequestParamVo flowInstanceRequestParamVo = new SpFlowInstanceRequestParamVo();
            flowInstanceRequestParamVo.setOverDueGradeEnum(overDueGradeEnum);
            flowInstanceRequestParamVo.setDepartmentTypeEnum(departmentTypeEnum);
            flowInstanceRequestParamVo.setReliefApplyTypes(specialRepaymentApplyTeyps);
            flowInstanceRequestParamVo.setReliefAmountGradeEnum(reliefAmountGradeEnum);
            try {
                List<SpFlowTransitionRule> flowNodeList = spFlowService.queryFlowNode(flowInstanceRequestParamVo);
                logger.debug("获取到的流程：{}", JSONUtil.toJSON(flowNodeList));
                responseInfo = new AttachmentResponseInfo(ResponseEnum.SYS_SUCCESS);
                responseInfo.setAttachment(flowNodeList);
            } catch (Exception e) {
                e.printStackTrace();
                throw new PlatformException(ResponseEnum.FULL_MSG,"找不到对应的审批流程图");
            }
        } catch (Exception e) {
            e.printStackTrace();
            responseInfo = new AttachmentResponseInfo(ResponseEnum.FULL_MSG.getCode(), e.getMessage());
            return toResponseJSON(responseInfo);
        }
        return toResponseJSON(responseInfo);
    }
    /**
     * 减免详情页面
     * @param request
     * @param response
     * @param applyReliefVo
     * @return
     */
    @RequestMapping("/applyDetailInfoPage")
    public ModelAndView applyDetailInfoPage(HttpServletRequest request,HttpServletResponse response,ApplyReliefVo applyReliefVo){
        ModelAndView modelAndView = new ModelAndView("/specialRepayment/applyDetailInfoPage");
        try {
        /*获取基础信息*/
            Long loanId = loanBaseService.findLoanIdByContractNum(applyReliefVo.getContractNum());
            VLoanInfo vLoanInfo = vLoanInfoService.findByLoanId(loanId);
            /** 借款人信息**/
            PersonInfo personInfo = personInfoService.findById(vLoanInfo.getBorrowerId());
            vLoanInfo.setPersonInfo(personInfo);
            Date tradeDate = Dates.getCurrDate();
//        计算出罚息 逾期 罚息起始日等信息
            OfferRepayInfoVo offerRepayInfoVo = offerRepayInfoService.getRepayInfo(loanId, tradeDate);
            if (offerRepayInfoVo == null) {
                throw new PlatformException(ResponseEnum.FULL_MSG, "无借款数据或已结清!");
            }
            //减免生效 获取 生效减免金额 生效日期
/*            if (specialRepaymentApply.getApplicationStatus().equals(SpecialRepaymentApplyStatus.生效.getCode())) {
                Map<String, Object> effectiveParams = new HashMap<String, Object>();
                effectiveParams.put("applyId", specialRepaymentApply.getId());
                Map<String, Object> effectiveMap = specialRepaymentApplyService.queryEffectiveLoanSpecialRepayment(effectiveParams);
                modelAndView.addObject("effectiveMoney", effectiveMap.get("effectiveMoney"));
                modelAndView.addObject("effectiveDate", effectiveMap.get("effectiveDate"));
            }*/
            //还款等级
            Map levelMap = new HashMap();
            levelMap.put("loanId", loanId);
            String repaymentLevel = loanRepaymentDetailService.findRepaymentLevel(levelMap);
            offerRepayInfoVo.setRepaymentLevel(repaymentLevel);
            //获取计算信息
            modelAndView.addObject("vLoanInfo", vLoanInfo);
            modelAndView.addObject("offerRepayInfo", offerRepayInfoVo);
            modelAndView.addObject("repaymentLevel", repaymentLevel);
            //已还期数
            int repayTime = Integer.valueOf(vLoanInfo.getTime().toString()) - vLoanInfo.getResidualTime();
            //已还金额
            BigDecimal repayAmount = specialRepaymentApplyService.calculateHistoryAlreadyRepayTotalMoney(vLoanInfo.getId());
            //历史逾期次数
            int overdueTime = specialRepaymentApplyService.historyOverDueTime(vLoanInfo.getId());
            //历史减免次数
            int reliefTime = specialRepaymentApplyService.historyReliefTime(vLoanInfo.getId());
            //历史减免金额
            BigDecimal reliefAmount = specialRepaymentApplyService.historyReliefAmount(vLoanInfo.getId());
            SpecialRepaymentApply specialRepaymentApply = specialRepaymentApplyService.getSpecialRepaymentApplyById(applyReliefVo.getApplyId());
            //申请减免金额
            BigDecimal applyAmount = specialRepaymentApply.getApplyAmount();

            String reliefType = specialRepaymentApply.getApplyType();
            //每月应还
            BigDecimal returnem = afterLoanService.getPerReapyAmount(new Date(), vLoanInfo.getId());
            //审批流程
            List<SpFlowTransitionRule> flowNodeList = new ArrayList<>();
            String specialReliefFlag = applyReliefVo.getSpecialReliefFlag();
            if (SpecialReliefTypeEnum.非特殊减免.getCode().equals(specialReliefFlag)) {
                //针对一般减免
                if (SpecialRepaymentApplyTeyps.一般减免.getCode().equals(specialRepaymentApply.getApplyType()) && tradeDate.compareTo(offerRepayInfoVo.getCurrDate()) != 0) {
                    //当前时间是否是还款日 如果不是还款日 当前应还 不含 当前
                    offerRepayInfoVo.setCurrAllAmount(offerRepayInfoVo.getOverdueAmount());
                }
                try {
                    flowNodeList = spFlowService.querySpFlowTransitionRulesByApplyId(applyReliefVo.getApplyId());
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new PlatformException(ResponseEnum.FULL_MSG, Strings.format("获取审批流程异常：{0}", e.getMessage()));
                }
            }else {
                if ((SpecialRepaymentApplyTeyps.一般减免.getCode().equals(reliefType) && LoanStateEnum.逾期.getValue().equals(vLoanInfo.getLoanState())) ||
                        (SpecialRepaymentApplyTeyps.一般减免.getCode().equals(reliefType) && LoanStateEnum.正常.getValue().equals(vLoanInfo.getLoanState())
                                &&!(tradeDate.compareTo(offerRepayInfoVo.getCurrDate()) == 0 || afterLoanService.isAdvanceRepayment(loanId)))) {
                    offerRepayInfoVo.setCurrAllAmount(offerRepayInfoVo.getOverdueAmount());
                }
            }
            //申请减免后应还金额
            BigDecimal afterReliefAmount = offerRepayInfoVo.getCurrAllAmount().subtract(applyAmount);
            modelAndView.addObject("repayTime", repayTime);
            modelAndView.addObject("repayAmount", repayAmount);
            modelAndView.addObject("overdueTime", overdueTime);
            modelAndView.addObject("reliefTime", reliefTime);
            modelAndView.addObject("reliefAmount", reliefAmount);
            modelAndView.addObject("specialRepaymentApply", specialRepaymentApply);
            modelAndView.addObject("afterReliefAmount", afterReliefAmount);
            modelAndView.addObject("reliefTypeEnum", EnumUtil.getEnum(SpecialRepaymentApplyTeyps.class, reliefType, "code"));
            modelAndView.addObject("returnem", returnem);
            modelAndView.addObject("flowNodeList", flowNodeList);
            modelAndView.addObject("flowId", flowNodeList.size() > 0 ? flowNodeList.get(0).getFlowId() : 0);
            modelAndView.addObject("specialReliefFlag",specialReliefFlag);
        }catch (Exception e){
            logger.info("跳转到申请减免详情页面异常：{}",e.getMessage());
            e.printStackTrace();
            modelAndView = new ModelAndView("error/error");
            modelAndView.addObject("errMsg",e.getMessage());
        }
        return modelAndView;
    }

    /**
     * 申请减免 提交
     * @param request
     * @param response
     * @param specialRepaymentApply
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/applyReliefSubmit")
    @Transactional(propagation= Propagation.REQUIRED)
    public  String applyReliefSubmit(HttpServletRequest request, HttpServletResponse response, SpecialRepaymentApply specialRepaymentApply){
        ResponseInfo responseInfo = null;
        try{
            User userInfo = UserContext.getUser();
            BigDecimal applyAmount = specialRepaymentApply.getApplyAmount();
            Assert.notNull(applyAmount, ResponseEnum.FULL_MSG, "减免金额不能为空");
            String memo = specialRepaymentApply.getMemo();
            if(Strings.isNotEmpty(memo) && memo.length() >200){
                memo = memo.substring(0,200)+"...";
                specialRepaymentApply.setMemo(memo);
            }
                    /*获取基础信息*/
            VLoanInfo vLoanInfo = vLoanInfoService.findByLoanId(specialRepaymentApply.getLoanId());
            if (isCSYQGLSRole(userInfo)) {
                //已停业的营业部
                if (!specialRepaymentApplyService.isNoValidSalesDepartment(vLoanInfo.getSalesDepartmentId())) {
                    throw new PlatformException(ResponseEnum.FULL_MSG,"此债权的营业部管理网点未停业，不属于逾期管理室管辖");
                }
            }else if(!this.isSameSalesDepartment(userInfo,vLoanInfo)) {
                throw new PlatformException(ResponseEnum.FULL_MSG,"此债权不属于当前管理机构");
            }
            SpecialRepaymentApply sp =  specialRepaymentApplyService.addSpecialRepaymentApplyByVo(specialRepaymentApply);
            if (SpecialReliefTypeEnum.非特殊减免.getCode().equals(specialRepaymentApply.getIsSpecial())) {
                spFlowService.startFlowInstance(sp.getId(),specialRepaymentApply.getFlowId());
            }else {
                spFlowService.appleReliefPassInvokingRepayIuput(sp.getId());
            }
            responseInfo = new ResponseInfo(ResponseEnum.SYS_SUCCESS);
        } catch (PlatformException p){
            logger.error("申请减免异常：",p);
            responseInfo = new ResponseInfo(ResponseEnum.FULL_MSG.getCode(),p.getMessage());
        }catch (Exception e){
            logger.error("申请减免异常：",e);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),e.getMessage());
        }
        return toResponseJSON(responseInfo);
    }

    /**
     * 取消减免 提交
     * @param request
     * @param response
     * @param specialRepaymentApply
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/cancelApplyReliefUrl")
    @Transactional(propagation= Propagation.REQUIRED)
    public  String cancelApplyReliefUrl(HttpServletRequest request, HttpServletResponse response, SpecialRepaymentApply specialRepaymentApply){
        ResponseInfo responseInfo = null;
        try{
            Assert.notNull(specialRepaymentApply.getId(),"请选择需取消的数据");
            // 获取当前登录用户的信息
            User userInfo = UserContext.getUser();
            Long applyId = specialRepaymentApply.getId();
            SpecialRepaymentApply spl = specialRepaymentApplyService.getSpecialRepaymentApplyById(applyId);
            if(spl == null || !SpecialRepaymentApplyStatus.申请.getCode().equals(spl.getApplicationStatus())){
                throw new PlatformException(ResponseEnum.FULL_MSG, "仅可操作状态为申请中的数据");
            }
            String memo1 = specialRepaymentApply.getMemo1();
            if(memo1 == null || "".equals(memo1.trim())){
                throw new PlatformException(ResponseEnum.FULL_MSG,"取消原因不能为空");
            }
            if(Strings.isNotEmpty(memo1) && memo1.length() > 200){
                memo1 = memo1.substring(0,200)+"...";
                specialRepaymentApply.setMemo1(memo1);
            }
            if (!userInfo.getId().equals(spl.getProposerId())) {
                throw new PlatformException(ResponseEnum.FULL_MSG,"申请操作者与取消操作者必须是同一个人！");
            }
            specialRepaymentApply.setApplyAmount(null);
            specialRepaymentApply.setApplicationStatus(SpecialRepaymentApplyStatus.取消.getCode());
            specialRepaymentApplyService.updateSpecialRepaymentApply(specialRepaymentApply);
            spFlowService.cancelReliefApplyFlowInstanceByApplyId(applyId);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_SUCCESS);
        } catch (PlatformException p){
            logger.error("申请减免异常：",p);
            responseInfo = new ResponseInfo(ResponseEnum.FULL_MSG.getCode(),p.getMessage());
        }catch (Exception e){
            logger.error("申请减免异常：",e);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),e.getMessage());
        }
        return toResponseJSON(responseInfo);
    }

    /**
     * 跳转到减免审批页面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/approveReliefRepayment")
    public ModelAndView approveReliefRepayment(HttpServletRequest request,HttpServletResponse response){
        ModelAndView modelAndView = new ModelAndView("/specialRepayment/approveReliefRepayment");
        return modelAndView;
    }

    /**
     * 跳转到申请减免待处理页面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("approveReliefProcessed")
    public ModelAndView approveReliefProcessed(HttpServletRequest request,HttpServletResponse response){
        ModelAndView modelAndView = new ModelAndView("/specialRepayment/approveReliefProcessed");
        // 获取当前登录用户的信息
        User userInfo = UserContext.getUser();
        String empNum = userInfo.getOrgCode();
        Map<String, Object> params = new HashMap<String, Object>();
        // 管理营业部网点
        params.put("empNum", empNum);
        params.put("vLevel", ComOrganizationEnum.Level.V104.name());
        List<Map<String, Object>>  salesTeamInfoList = salesDeptRepayInfoService.getSalesDeptInfo(params);
        //减免类型
        SpecialRepaymentApplyTeyps [] applyReliefTypes = {SpecialRepaymentApplyTeyps.一般减免,SpecialRepaymentApplyTeyps.结清减免};
        modelAndView.addObject("salesTeamInfoList", salesTeamInfoList);
        modelAndView.addObject("applyReliefTypes",applyReliefTypes);
        modelAndView.addObject("applyDate", Dates.getDateTime("yyyy-MM-dd"));
        return modelAndView;
    }

    /**
     * 更改申请减免状态 拒绝 或者同意
     * @param request
     * @param response
     * @param specialRepaymentApply
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateApplyReliefStatus")
    public  String updateApplyReliefStatus(HttpServletRequest request, HttpServletResponse response, SpecialRepaymentApply specialRepaymentApply){
        ResponseInfo responseInfo = null;
        try{
            // 获取当前登录用户的信息
            String applyStatus = specialRepaymentApply.getApplicationStatus();
            if(applyStatus == null ||(!applyStatus.equals(SpecialRepaymentApplyStatus.拒绝.getCode()) && !applyStatus.equals(SpecialRepaymentApplyStatus.通过.getCode()))){
                throw new PlatformException(ResponseEnum.FULL_MSG, "对减免申请只能做同意或者拒绝操作");
            }
            String ids = request.getParameter("ids");
            String memo1 = specialRepaymentApply.getMemo1();
            if(Strings.isNotEmpty(memo1) && memo1.length() >200){
                memo1 = memo1.substring(0,200)+"...";
                specialRepaymentApply.setMemo1(memo1);
            }
            if(Strings.isNotEmpty(ids)){
                String idl[] = ids.split(",");
                for(String idStr:idl){
                    Long idLong = Long.parseLong(idStr);
                    SpecialRepaymentApply spl = specialRepaymentApplyService.getSpecialRepaymentApplyById(idLong);
                    if(spl == null || !SpecialRepaymentApplyStatus.申请.getCode().equals(spl.getApplicationStatus())){
                        continue;
                    }
                    dealApproveFlow(idLong,applyStatus,memo1);
                }
            }else{
                Assert.notNull(specialRepaymentApply.getId(),"请选择需操作的数据");
                Long applyId = specialRepaymentApply.getId();
                SpecialRepaymentApply spl = specialRepaymentApplyService.getSpecialRepaymentApplyById(applyId);
                if(spl == null || !SpecialRepaymentApplyStatus.申请.getCode().equals(spl.getApplicationStatus())){
                    throw new PlatformException(ResponseEnum.FULL_MSG, "仅可操作状态为申请中的数据");
                }
                dealApproveFlow(applyId,applyStatus,memo1);
            }

            responseInfo = new ResponseInfo(ResponseEnum.SYS_SUCCESS);
        } catch (PlatformException p){
            logger.error("更改申请减免状态异常：",p);
            responseInfo = new ResponseInfo(ResponseEnum.FULL_MSG.getCode(),p.getMessage());
        }catch (Exception e){
            logger.error("更改申请减免状态异常：",e);
            responseInfo = new ResponseInfo(ResponseEnum.FULL_MSG.getCode(),e.getMessage());
        }
        return toResponseJSON(responseInfo);
    }

    /**
     * 跳转到申请减免完成页面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("approveReliefFinish")
    public ModelAndView approveReliefFinish(HttpServletRequest request,HttpServletResponse response){
        ModelAndView modelAndView = new ModelAndView("/specialRepayment/approveReliefFinish");
        // 获取当前登录用户的信息
        User userInfo = UserContext.getUser();
        String empNum = userInfo.getOrgCode();
        Map<String, Object> params = new HashMap<String, Object>();
        // 管理营业部网点
        params.put("empNum", empNum);
        params.put("vLevel", ComOrganizationEnum.Level.V104.name());
        List<Map<String, Object>>  salesTeamInfoList = salesDeptRepayInfoService.getSalesDeptInfo(params);
        //减免类型
        SpecialRepaymentApplyTeyps [] applyReliefTypes = {SpecialRepaymentApplyTeyps.一般减免,SpecialRepaymentApplyTeyps.结清减免};
        SpecialRepaymentApplyStatus [] applyStatuses = {SpecialRepaymentApplyStatus.申请,SpecialRepaymentApplyStatus.取消,SpecialRepaymentApplyStatus.失效,
                SpecialRepaymentApplyStatus.完成,SpecialRepaymentApplyStatus.拒绝,SpecialRepaymentApplyStatus.生效,SpecialRepaymentApplyStatus.通过};
        SpecialReliefTypeEnum [] specialReliefTypeEnums = {SpecialReliefTypeEnum.特殊减免,SpecialReliefTypeEnum.非特殊减免};
        modelAndView.addObject("salesTeamInfoList", salesTeamInfoList);
        modelAndView.addObject("applyReliefTypes",applyReliefTypes);
        modelAndView.addObject("applyStatuses",applyStatuses);
        modelAndView.addObject("applyDate", Dates.getDateTime("yyyy-MM-dd"));
        modelAndView.addObject("specialReliefTypes",specialReliefTypeEnums);
        return modelAndView;
    }

    @RequestMapping("reliefOperateInfoPage")
    public ModelAndView reliefOperateInfoPage(HttpServletRequest request,HttpServletResponse response,ApplyReliefVo applyReliefVo){
        ModelAndView modelAndView = new ModelAndView("/specialRepayment/reliefOperateInfo");
        modelAndView.addObject("applyId",applyReliefVo.getApplyId());
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping("queryReliefOperateInfos")
    public String queryReliefOperateInfos(HttpServletRequest request,HttpServletResponse response,ApplyReliefVo applyReliefVo){
        AttachmentResponseInfo responseInfo = null;
        List<SpFlowOperateInfoVo> operateInfos = null;
        try{
            Long applyId = applyReliefVo.getApplyId();
            if (Strings.isEmpty(applyId)) {
                responseInfo = new AttachmentResponseInfo(ResponseEnum.SYS_SUCCESS);
                responseInfo.setAttachment(new ArrayList<SpFlowOperateInfoVo>());
                return toResponseJSON(responseInfo);
            }
            SpFlowInstance flowInstance = spFlowService.querySpFlowInstanceByApplyId(applyId);
            operateInfos = spFlowService.queryOperateInfos(applyId,flowInstance.getId());
            logger.info("json格式化数据：{}",JSONUtil.toJSON(operateInfos));
            responseInfo = new AttachmentResponseInfo(ResponseEnum.SYS_SUCCESS);
            responseInfo.setAttachment(operateInfos);
        }catch (PlatformException p){
            logger.info("查询文件传输流水异常：{}",p.getMessage());
            responseInfo = new AttachmentResponseInfo(ResponseEnum.FULL_MSG.getCode(),p.getMessage());
            return toResponseJSON(responseInfo);
        }catch (Exception e){
            logger.info("查询文件传输流水异常：{}",e.getMessage());
            responseInfo = new AttachmentResponseInfo(ResponseEnum.SYS_FAILD.getCode(),e.getMessage());
            return toResponseJSON(responseInfo);
        }
        logger.info("-------------:{}",toResponseJSON(responseInfo));
        return toResponseJSON(responseInfo);
    }

    /**
     * 跳转员工与环节节点关联关系管理页面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("jumpApproveNodeEmployeeManage")
    public ModelAndView jumpApproveNodeEmployeeManage(HttpServletRequest request,HttpServletResponse response){
        ModelAndView outMode = new ModelAndView("/specialRepayment/approveNodeEmployeeManage");
        return outMode;
    }

    /**
     * 分页查询员工与环节节点关系数据
     * @param rows
     * @param page
     * @param name
     * @param userCode
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping("approveNodeEmployeeManagePage")
    public String approveNodeEmployeeManagePage(int rows, int page,String name,String userCode,HttpServletRequest request,HttpServletResponse response){
        Map<String,Object> params = new HashMap<>();
        Pager pager = new Pager();
        try {
            this.createLog(request, SysActionLogTypeEnum.查询, "查询审批环节跟员工管理关系", "查询审批环节跟员工管理关系记录");
            params.put("name",name);
            params.put("userCode",userCode);
            params.put("status", NodeEmployeeEnum.有效.getCode());
            pager.setRows(rows);
            pager.setPage(page);
            params.put("pager",pager);
            //查询条件限制
            pager = spFlowService.queryNodeEmployeePage(params);
        }catch (PlatformException e){
            return  JSONObject.toJSONString(new AttachmentResponseInfo<Map<String, Object>>(ResponseEnum.FULL_MSG.getCode(), e.getMessage()));
        }catch (Exception e){
            e.printStackTrace();
            return  JSONObject.toJSONString(new AttachmentResponseInfo<Map<String, Object>>(ResponseEnum.SYS_FAILD.getCode(), "查询异常"));
        }
        return toPGJSONWithCode(pager);
    }

    /**
     * 加载所有环节节点树
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping("queryNodeTree")
    public String queryNodeTree(HttpServletRequest request,HttpServletResponse response){
        AttachmentResponseInfo responseInfo = null;
        try{
            List<SpNodeInfo> nodeInfos = spFlowService.querySpNodeInfos();
            logger.info("json格式化数据：{}",JSONUtil.toJSON(nodeInfos));
            List<EasyUITree> nodeTree = new ArrayList<>();
            for(SpNodeInfo node:nodeInfos){
                EasyUITree t = new EasyUITree(node.getId().toString(),node.getNodeName());
                nodeTree.add(t);
            }
            responseInfo = new AttachmentResponseInfo(ResponseEnum.SYS_SUCCESS);
            responseInfo.setAttachment(nodeTree);
        }catch (Exception e){
            logger.info("查询所有环节异常：{}",e.getMessage());
            responseInfo = new AttachmentResponseInfo(ResponseEnum.FULL_MSG.getCode(),e.getMessage());
            return toResponseJSON(responseInfo);
        }
        return toResponseJSON(responseInfo);
    }

    /**
     * 新增员工与审批环节节点关系
     * @param request
     * @param response
     * @param userCode
     * @param nodeIds
     * @return
     */
    @ResponseBody
    @RequestMapping("insertNodeEmployee")
    public String insertNodeEmployee(HttpServletRequest request,HttpServletResponse response,String userCode,String nodeIds){
        ResponseInfo responseInfo = null;
        try {
            if (Strings.isEmpty(userCode)) {
                throw new PlatformException(ResponseEnum.FULL_MSG, "请输入员工工号！");
            }
            if (Strings.isEmpty(nodeIds)) {
                throw new PlatformException(ResponseEnum.FULL_MSG, "请您选择需要分配的环节！");
            }
            //检查用是否存在
            ComEmployee comEmployee =comEmployeeService.findByUserCode(userCode);
            if (comEmployee == null) {
                throw new PlatformException(ResponseEnum.FULL_MSG, "您输入的员工工号不存在，请输入正确的员工工号！");
            }
            Long employeeId = comEmployee.getId();
            String[] nodeIdArrays = nodeIds.split(",");
            for (String nodeId : nodeIdArrays) {
                //检查是否存在次节点
                SpNodeEmployee nodeEmployee = spFlowService.querySpNodeEmployee(Long.valueOf(nodeId),employeeId);
                if (nodeEmployee != null) {
                    continue;
                }
                spFlowService.insertNodeEmployee(employeeId, Long.valueOf(nodeId));
            }
            responseInfo = new ResponseInfo(ResponseEnum.SYS_SUCCESS);
        } catch (PlatformException p) {
            logger.error("添加环节节点跟员工关系异常：", p);
            responseInfo = new ResponseInfo(ResponseEnum.FULL_MSG.getCode(), p.getMessage());
        } catch (Exception e) {
            logger.error("添加环节节点跟员工关系异常：", e);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(), e.getMessage());
        }
        return toResponseJSON(responseInfo);
    }

    /**
     * 删除员工与审批环节节点关系表
     * @param request
     * @param response
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("deleteNodeEmployee")
    public String deleteNodeEmployee(HttpServletRequest request,HttpServletResponse response,String id){
        ResponseInfo responseInfo = null;
        try {
            if (Strings.isEmpty(id)) {
                throw new PlatformException(ResponseEnum.FULL_MSG, "请您选择需要删除的记录");
            }
            spFlowService.deleteNodeEmployee(Long.valueOf(id));
            responseInfo = new ResponseInfo(ResponseEnum.SYS_SUCCESS);
        } catch (PlatformException p) {
            logger.error("删除环节节点跟员工关系异常：", p);
            responseInfo = new ResponseInfo(ResponseEnum.FULL_MSG.getCode(), p.getMessage());
        } catch (Exception e) {
            logger.error("删除环节节点跟员工关系异常：", e);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(), e.getMessage());
        }
        return toResponseJSON(responseInfo);
    }

    /**
     * 校验特殊申请减免处理
     * @param request
     * @param response
     * @param applyReliefVo
     * @return
     */
    @ResponseBody
    @RequestMapping("/spApplyReliefCheck")
    public String spApplyReliefCheck(HttpServletRequest request,HttpServletResponse response,ApplyReliefVo applyReliefVo){
        ResponseInfo responseInfo = null;
        try {
            this.createLog(request, SysActionLogTypeEnum.其他, "申请减免校验", "申请减免校验");
            logger.info("申请减免校验，获取校验参数为：{}", JSONUtil.toJSON(applyReliefVo));
            VloanPersonInfo vloanPersonInfo = specialRepaymentApplyService.checkLoanPersonInfo(applyReliefVo.getContractNum(), applyReliefVo.getIdNum(), applyReliefVo.getName());
            specialRepaymentApplyService.checkOutSpecialReliefApply(vloanPersonInfo.getId());
            responseInfo = new ResponseInfo(ResponseEnum.SYS_SUCCESS.getCode(),"减免申请校验通过");
        } catch (PlatformException pe){
            logger.error(pe.getMessage());
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),pe.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),e.getMessage());
        }
        return toResponseJSON(responseInfo);
    }

    @ResponseBody
    @RequestMapping("/importReliefPenaltyStateFile")
    public void importReliefPenaltyStateFile(@RequestParam(value = "uploadFile", required = false) MultipartFile file, HttpServletRequest request,
                                             HttpServletResponse response) throws IOException {
        ResponseInfo responseInfo = null;
        try {
            this.createLog(request, SysActionLogTypeEnum.导入, "特殊减免申请", "特殊减免申请导入");
            /** 登陆者信息 **/
            User user = UserContext.getUser();
            UploadFile uploadFile = new UploadFile();
            uploadFile.setFile(file);
            uploadFile.setFileType(UploadFileUtil.FILE_TYPE_EXCEL);
            uploadFile.setFileMaxSize(1024 * 1024 * 5);
            UploadFileUtil.valid(uploadFile);
            String contentType = file.getContentType();
            String fileName = file.getOriginalFilename();
            /** 跟据文件名判断是否已导入 **/
/*            List<ComMessageIn> checkFile = comMessageInServiceImpl.findByMessageTypeAndFileName("ReliefPenaltyState", fileName);
            Assert.isCollectionsEmpty(checkFile, ResponseEnum.FULL_MSG, "当前导入文件名:[" + fileName + "]已经存在!");*/
            Workbook workBook = WorkbookFactory.create(file.getInputStream());
            ExcelTemplet excelTemplet = new ExcelTemplet().new ReliefPenaltyStateInputExcel();
            List<Map<String, String>> result = ExcelUtil.getExcelData(workBook, excelTemplet);
            Assert.notCollectionsEmpty(result, ResponseEnum.FILE_EMPTY_FILE,"");
            comMessageInServiceImpl.createMsgIn("ReliefPenaltyState", user.getName(), fileName,"减免申请批量导入");
            for (int i = 0; i < result.size(); i++) {
                reliefPenaltyParseItem(result.get(i));
            }
            ExcelUtil.addResultToWorkBook(workBook, result, excelTemplet);
            /** 下载文件名 **/
            fileName = "减免申请导入结果" + Dates.getDateTime(Dates.DEFAULT_DATE_FORMAT) + ".xls";
            String enableFileName = new String(fileName.getBytes("GBK"), "ISO-8859-1");
            response.setHeader("Content-Disposition", "attachment; filename=" + enableFileName);
            response.setHeader("Content-Type",contentType);

            OutputStream outputStream = response.getOutputStream();
            workBook.write(outputStream);
            outputStream.flush();
            return;
        } catch (PlatformException pe) {
            logger.error(pe.getMessage());
            responseInfo = pe.toResponseInfo();
        } catch (Exception e) {
            /** 系统忙 **/
            logger.error(e.getMessage());
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
        }
        response.setContentType("text/html");
        response.getWriter().print(toResponseJSON(responseInfo));
    }

    /**
     * 减免查询页面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("jumpReliefQueryInfo")
    public ModelAndView jumpReliefQueryInfo(HttpServletRequest request,HttpServletResponse response){
        ModelAndView modelAndView = new ModelAndView("/specialRepayment/queryReliefInfo");
        // 获取当前登录用户的信息
        //减免类型
        SpecialRepaymentApplyTeyps [] applyReliefTypes = {SpecialRepaymentApplyTeyps.一般减免,SpecialRepaymentApplyTeyps.结清减免};
        SpecialReliefTypeEnum [] specialReliefTypeEnums = {SpecialReliefTypeEnum.特殊减免,SpecialReliefTypeEnum.非特殊减免};
        modelAndView.addObject("applyReliefTypes",applyReliefTypes);
        modelAndView.addObject("applyDate", Dates.getDateTime("yyyy-MM-dd"));
        modelAndView.addObject("specialReliefTypes",specialReliefTypeEnums);
        return modelAndView;
    }

    /***
     * 减免查询分页
     * @param rows
     * @param page
     * @param request
     * @param response
     * @param applyReliefVo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "queryReliefInfoPage")
    public String queryReliefInfoPage(int rows, int page,HttpServletRequest request,HttpServletResponse response,ApplyReliefVo applyReliefVo){
        Map<String,Object> params = new HashMap<>();
        Pager pager = new Pager();
        try {
            this.createLog(request, SysActionLogTypeEnum.查询, "减免查询", "减免查询的记录");
            params.put("contractNum",applyReliefVo.getContractNum());
            params.put("name",applyReliefVo.getName());
            params.put("idNum",applyReliefVo.getIdNum());
            params.put("applyType",applyReliefVo.getApplyReliefType());
            params.put("startApplyDate",Dates.getDateTime(applyReliefVo.getStartApplyDate(),Dates.DEFAULT_DATE_FORMAT));
            params.put("endApplyDate",Dates.getDateTime(Dates.addDay(applyReliefVo.getEndApplyDate(), 1),Dates.DEFAULT_DATE_FORMAT));
            params.put("isSpecial",applyReliefVo.getSpecialReliefFlag());
            // 获取当前登录用户的信息
            User userInfo = UserContext.getUser();
            String orgCode = userInfo.getOrgCode();
            params.put("orgCode", orgCode);
            pager.setRows(rows);
            pager.setPage(page);
            params.put("pager",pager);
            //查询条件限制
            pager = specialRepaymentApplyService.queryReliefInfoPage(params);
        }catch (PlatformException e){
            return  JSONObject.toJSONString(new AttachmentResponseInfo<Map<String, Object>>(ResponseEnum.FULL_MSG.getCode(), e.getMessage()));
        }catch (Exception e){
            e.printStackTrace();
            return  JSONObject.toJSONString(new AttachmentResponseInfo<Map<String, Object>>(ResponseEnum.FULL_MSG.getCode(), "查询异常"));
        }
        return toPGJSONWithCode(pager);
    }

    /* 导出回购债权信息
    *
    * @param request
    * @param response
    */
    @RequestMapping("/exportQueryReliefInfoReport")
    @ResponseBody
    public void exportQueryReliefInfoReport(ApplyReliefVo applyReliefVo,HttpServletRequest request, HttpServletResponse response) {
        ResponseInfo responseInfo = null;
        try {
            createLog(request, SysActionLogTypeEnum.导出, "减免查询报表", "减免查询报表信息");
            //查询条件限制
            Map<String,Object> params = new HashMap<>();
            params.put("contractNum",applyReliefVo.getContractNum());
            params.put("name",applyReliefVo.getName());
            params.put("idNum",applyReliefVo.getIdNum());
            params.put("applyType",applyReliefVo.getApplyReliefType());
            params.put("startApplyDate",Dates.getDateTime(applyReliefVo.getStartApplyDate(),Dates.DEFAULT_DATE_FORMAT));
            params.put("endApplyDate",Dates.getDateTime(Dates.addDay(applyReliefVo.getEndApplyDate(), 1),Dates.DEFAULT_DATE_FORMAT));
            params.put("isSpecial",applyReliefVo.getSpecialReliefFlag());
            // 获取当前登录用户的信息
            User userInfo = UserContext.getUser();
            String orgCode = userInfo.getOrgCode();
            params.put("orgCode", orgCode);
            // 下载文件名称编辑
            // 创建划拨申请书excel文件对象
            Workbook workbook = null;
            String fileName = "减免查询-"+Dates.getDateTime(applyReliefVo.getStartApplyDate(),Dates.DEFAULT_DATE_FORMAT)+"~"+
                    Dates.getDateTime(Dates.addDay(applyReliefVo.getEndApplyDate(), 1),Dates.DEFAULT_DATE_FORMAT)+".xls";
            workbook = specialRepaymentApplyService.getQueryReliefInfoWorkbook(params,fileName);
            if(null == workbook){
                throw new PlatformException(ResponseEnum.FULL_MSG, "无可导出数据！");
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
            logger.error("导出查询减免报表（Xls）异常：", e);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
        } finally {
        }
        try {
            response.setContentType("text/html");
            response.getWriter().write(toResponseJSON(responseInfo));
        } catch (IOException e) {
        }
    }

    private boolean isSameSalesDepartment(User userInfo,VLoanInfo vLoanInfo){
        String empNum = userInfo.getOrgCode();
        Map<String, Object> params = new HashMap<String, Object>();
        // 管理营业部网点
        params.put("empNum", empNum);
        params.put("vLevel", ComOrganizationEnum.Level.V104.name());
        List<Map<String, Object>>  salesTeamInfoList = salesDeptRepayInfoService.getSalesDeptInfo(params);
        for(Map<String,Object> map:salesTeamInfoList){
            Object id = map.get("id");
            if (id.toString().equals(vLoanInfo.getSalesDepartmentId().toString())) {
                return  true;
            }
        }
        return false;
    }

    /**
     * 用户角色是否是逾期管理室（余刚、寇学刘）
     * @param userInfo
     * @return true 是 false 不是
     */
    private boolean isCSYQGLSRole(User userInfo){
        Map<String, Object> roleMap = userInfo.getRoleMap();
        String csyqglsRoleName = sysParamDefineService.getSysParamValue("role", "csyqgls.role.name");
        for(Map.Entry<String,Object> entry:roleMap.entrySet()){
            if(entry.getValue().equals(csyqglsRoleName)){
                return true;
            }
        }
        return false;
    }

    private DepartmentTypeEnum getDepartmentType(User userInfo){
        if (isCSYQGLSRole(userInfo)) {
            //逾期管理室--申请
            return DepartmentTypeEnum.催收管理室;
        }
            //营业部--申请
        return DepartmentTypeEnum.营业部;
    }

    private void dealApproveFlow(Long applyId,String applyStatus,String memo1){
        FlowNodeTransitionStatusEnum transitionStatusEnum = applyStatus.equals(SpecialRepaymentApplyStatus.通过.getCode()) ? FlowNodeTransitionStatusEnum.审批通过 : FlowNodeTransitionStatusEnum.审批拒绝;
        SpFlowInstance flowInstance = spFlowService.querySpFlowInstanceByApplyId(applyId);
        if (flowInstance == null) {
            throw new PlatformException(ResponseEnum.FULL_MSG,"找不到对应的流程实例！");
        }
        spFlowService.moveNodeInstanceAction(flowInstance.getId(),transitionStatusEnum,memo1);
    }

    /**
     * 处理罚息减免申请 Excel文件数据
     *
     * @param values
     */
    private void reliefPenaltyParseItem(Map<String, String> values) {
        /** 登陆者信息 **/
        User user = UserContext.getUser();
        String feedBackMsg = "";
        try {
            String name = Strings.convertValue(values.get("name"), String.class);
            String idNum = Strings.convertValue(values.get("idNum"), String.class);
            String userCode = Strings.convertValue(values.get("userCode"), String.class);
            String amountPar = Strings.convertValue(values.get("amount"), String.class);
            String contractNum = Strings.convertValue(values.get("contractNum"), String.class);
            Date tradeDate = Dates.getCurrDate();

            Assert.notNullAndEmpty(name, "借款人姓名");
            Assert.notNullAndEmpty(idNum, "借款人身份证");
            Assert.notNullAndEmpty(userCode, "申请人工号");
            Assert.notNullAndEmpty(amountPar, "申请金额");
            Assert.notNullAndEmpty(contractNum, "合同编号");

            BigDecimal amount = Assert.notBigDecimal(amountPar, "申请金额");
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new PlatformException(ResponseEnum.FULL_MSG, "申请金额必须大于0");
            }

            /** 判断借款人是否存在（姓名+身份证号） **/
            VloanPersonInfo vloanPersonInfo = specialRepaymentApplyService.checkLoanPersonInfo(contractNum, idNum, name);
            /** 验证申请人工号是否存在 **/
            ComEmployee comEmployee = comEmployeeServiceImpl.findByUserCode(userCode);
            Assert.notNull(comEmployee, ResponseEnum.FULL_MSG,"申请人工号提供有误!");
            Long loanId = vloanPersonInfo.getId();
            String reliefType = afterLoanService.isOneTimeRepayment(loanId) ? SpecialRepaymentApplyTeyps.结清减免.getCode() : SpecialRepaymentApplyTeyps.一般减免.getCode();
            specialRepaymentApplyService.checkOutSpecialReliefApply(loanId);
//        计算出罚息 逾期 罚息起始日等信息
            OfferRepayInfoVo offerRepayInfoVo = offerRepayInfoService.getRepayInfo(loanId, tradeDate);
            if (offerRepayInfoVo == null) {
                throw new PlatformException(ResponseEnum.FULL_MSG, "无借款数据或已结清");
            }
            BigDecimal maxReleifMoney = offerRepayInfoVo.getCurrAllAmount();
            if ((SpecialRepaymentApplyTeyps.一般减免.getCode().equals(reliefType) && LoanStateEnum.逾期.getValue().equals(vloanPersonInfo.getLoanState())) ||
                    (SpecialRepaymentApplyTeyps.一般减免.getCode().equals(reliefType) && LoanStateEnum.正常.getValue().equals(vloanPersonInfo.getLoanState())
                            &&!(tradeDate.compareTo(offerRepayInfoVo.getCurrDate()) == 0 || afterLoanService.isAdvanceRepayment(loanId)))) {
                maxReleifMoney = offerRepayInfoVo.getOverdueAmount();
            }
            if (amount.compareTo(maxReleifMoney) == 1) {
                throw new PlatformException(ResponseEnum.FULL_MSG, "减免金额不允许大于最大减免金额");
            }
            SpecialRepaymentApply spApply = addSpReliefApply(loanId,new BigDecimal(amountPar),reliefType);
            spFlowService.appleReliefPassInvokingRepayIuput(spApply.getId());
            feedBackMsg = "申请成功!";
        } catch (PlatformException pe) {
            pe.printStackTrace();
            logger.error(pe.getMessage());
            feedBackMsg = pe.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            feedBackMsg = "系统忙：申请出错!";
        }
        values.put(ExcelTemplet.FEED_BACK_MSG, feedBackMsg);
    }

    private SpecialRepaymentApply addSpReliefApply(Long loanId,BigDecimal applyReliefAmount,String reliefType){
        //还款等级
        Map levelMap = new HashMap();
        levelMap.put("loanId", loanId);
        String repaymentLevel = loanRepaymentDetailService.findRepaymentLevel(levelMap);
        SpecialRepaymentApply specialRepaymentApply = new SpecialRepaymentApply();
        specialRepaymentApply.setLoanId(loanId);
        specialRepaymentApply.setApplyAmount(applyReliefAmount);
        specialRepaymentApply.setApplyType(reliefType);
        specialRepaymentApply.setIsSpecial(SpecialReliefTypeEnum.特殊减免.getCode());
        specialRepaymentApply.setRepayLevel(repaymentLevel);
        SpecialRepaymentApply sp =  specialRepaymentApplyService.addSpecialRepaymentApplyByVo(specialRepaymentApply);
        return sp;
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
