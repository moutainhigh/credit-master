package com.zdmoney.credit.operation.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.zdmoney.credit.common.constant.ComOrganizationEnum;
import com.zdmoney.credit.common.constant.SysActionLogTypeEnum;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.SpringContextUtil;
import com.zdmoney.credit.common.vo.AttachmentResponseInfo;
import com.zdmoney.credit.common.vo.ResponseInfo;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.loan.domain.LoanBase;
import com.zdmoney.credit.loan.service.pub.ILoanLogService;
import com.zdmoney.credit.operation.domain.PerformanceBelongInfo;
import com.zdmoney.credit.operation.service.pub.IPerformanceBelongInfoService;
import com.zdmoney.credit.system.domain.ComOrganization;
import com.zdmoney.credit.system.service.pub.IComOrganizationService;
import com.zdmoney.credit.system.service.pub.ISysActionLogService;

@Controller
@RequestMapping("/operation")
public class PerformanceBelongInfoController extends BaseController {
    
    @Autowired
    private IPerformanceBelongInfoService performanceBelongInfoService;
    
    @Autowired
    private IComOrganizationService comOrganizationService;
    
    @Autowired
    private ILoanLogService loanLogService;
    @Autowired
    private ISysActionLogService SysActionLog;

    /**
     * 初始化页面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/performanceBelongChange")
    public ModelAndView jumpPage(HttpServletRequest request,HttpServletResponse response) {
        createLog(request, SysActionLogTypeEnum.查询, "业绩归属变更", "加载业绩归属变更页面");
        // 获取当前登录用户的信息
        User userInfo = UserContext.getUser();
        String empNum = userInfo.getOrgCode();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("empNum", empNum);
        params.put("vLevel", ComOrganizationEnum.Level.V105.name());
        // 查询当前用户所属机构下的销售团队信息
        List<Map<String, Object>> salesTeamInfoList = performanceBelongInfoService.getSalesTeamOptionInfo(params);
        ModelAndView mav = new ModelAndView("operation/performanceBelongChange");
        mav.addObject("salesTeamInfoList", salesTeamInfoList);
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
    @RequestMapping("/searchPerformanceBelongInfo")
    @ResponseBody
    public String search(PerformanceBelongInfo performanceBelongInfo, int rows,int page, HttpServletRequest request, HttpServletResponse response) {
        createLog(request, SysActionLogTypeEnum.查询, "业绩归属变更", "查询业绩归属变更信息");
        // 获取当前登录用户的信息
        User userInfo = UserContext.getUser();
        String empNum = userInfo.getOrgCode();
        performanceBelongInfo.setEmpNum(empNum);
        // 定义分页实例
        Pager pager = new Pager();
        
        
        
        pager.setRows(rows);
        pager.setPage(page);
        performanceBelongInfo.setPager(pager);
        // 调用Service层查询各门店还款信息
        pager = performanceBelongInfoService.findWithPg(performanceBelongInfo);
        // 将数据对象转换成JSON字符串，返回给前台
        return toPGJSONWithCode(pager);
    }
    
    /**
     * 加载销售团队下拉框信息
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/loadOptionInfo")
    @ResponseBody
    public String loadOptionInfo(HttpServletRequest request, HttpServletResponse response){
        AttachmentResponseInfo<List<Map<String, Object>>> responseInfo = null;
        List<Map<String, Object>> salesTeamOptionList = null;
        // 获取当前登录用户的信息
        User userInfo = UserContext.getUser();
        String empNum = userInfo.getOrgCode();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("empNum", empNum);
        params.put("vLevel", ComOrganizationEnum.Level.V105.name());
        try{
            // 查询当前机构下的销售团队信息
            salesTeamOptionList = performanceBelongInfoService.getSalesTeamOptionInfo(params);
            responseInfo = new AttachmentResponseInfo<List<Map<String, Object>>>(ResponseEnum.SYS_SUCCESS);
            responseInfo.setAttachment(salesTeamOptionList);
        }catch(Exception e){
            responseInfo = new AttachmentResponseInfo<List<Map<String, Object>>>(ResponseEnum.SYS_FAILD);
        }
        return toResponseJSON(responseInfo);
    }
    
    /**
     * 加载录单渠道下拉框信息
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/loadOptionapply")
    @ResponseBody
    public String loadOptionapply(HttpServletRequest request, HttpServletResponse response,Long loanId){
        AttachmentResponseInfo<List<Map<String, Object>>> responseInfo = null;
      
        try{
        	Map<String, Object> salesApplyOptionMap = new HashMap<String, Object>();
        	Map<String, Object> salesApplyOptionMap2 = new HashMap<String, Object>();
            List<Map<String, Object>> salesApplyOptionList = new ArrayList<Map<String,Object>>();
        	salesApplyOptionMap.put("applyInput", "普通营业部");
        	salesApplyOptionMap2.put("applyInput", "直通车营业部");
        	salesApplyOptionList.add(salesApplyOptionMap);
        	salesApplyOptionList.add(salesApplyOptionMap2);
            responseInfo = new AttachmentResponseInfo<List<Map<String, Object>>>(ResponseEnum.SYS_SUCCESS);
            responseInfo.setAttachment(salesApplyOptionList);
        }catch(Exception e){
        	System.out.println(e);
            responseInfo = new AttachmentResponseInfo<List<Map<String, Object>>>(ResponseEnum.SYS_FAILD);
        }
        return toResponseJSON(responseInfo);
    }
    
    /**
     * 变更业绩归属
     * @param loanId
     * @param updateSalesMan
     * @param updateSalesTeam
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/updatePerformanceBelongInfo")
    @ResponseBody
    public String updatePerformanceBelongInfo(@RequestParam Long loanId,
            @RequestParam Long updateSalesMan,@RequestParam Long updateSalesTeam, 
            HttpServletRequest request,HttpServletResponse response) {
        ResponseInfo responseInfo = null;
        createLog(request, SysActionLogTypeEnum.更新, "业绩归属变更", "变更业绩归属信息");
        try {
            LoanBase loanBase = new LoanBase();
            loanBase.setId(loanId);
            loanBase.setSalesmanId(updateSalesMan);
            loanBase.setSalesTeamId(updateSalesTeam);
            // 变更业绩归属
            performanceBelongInfoService.updatePerformanceBelongInfo(loanBase);
            // 日志记录
            loanLogService.createLog(loanId, "变更业绩","PerformanceBelongInfoController", "info", null);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_SUCCESS);
        } catch (Exception e) {
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
        }
        return toResponseJSON(responseInfo);
    }
    
    /***
     *切换录单渠道按钮 
     * */
    @RequestMapping("/updatePerformanceBelongRecorded")
    @ResponseBody
    public String updatePerformanceBelongRecorded(@RequestParam Long loanId,@RequestParam String updateapplyinputflag, String applyInputFlag, 
            HttpServletRequest request,HttpServletResponse response){
    	ResponseInfo responseInfo = null;
        createLog(request, SysActionLogTypeEnum.更新, "业绩归属变更", "切换录单渠道信息",applyInputFlag,updateapplyinputflag);
        try {
        	if(updateapplyinputflag.equals("普通营业部")){
            	updateapplyinputflag="applyInput";
            }
            else if(updateapplyinputflag.equals("直通车营业部")){
            	updateapplyinputflag="directApplyInput";
            }
            LoanBase loanBase = new LoanBase();
            loanBase.setId(loanId);
            loanBase.setApplyInputFlag(updateapplyinputflag);
            
            // 切换录单渠道
            performanceBelongInfoService.updatePerformanceBelongInfo(loanBase); 
            responseInfo = new ResponseInfo(ResponseEnum.SYS_SUCCESS);
        } catch (Exception e) {
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
        }
        return toResponseJSON(responseInfo);
    	
    }
}
