package com.zdmoney.credit.operation.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.zdmoney.credit.common.constant.SysActionLogTypeEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.vo.ResponseInfo;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.loan.service.pub.ILoanLogService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;

@Controller
@RequestMapping("/operation")
public class MonthSignSettingController extends BaseController {
    
    @Autowired
    private ISysParamDefineService sysParamDefineService;
    
    @Autowired
    private ILoanLogService loanLogService;
    
    /**
     * 初始化页面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/monthSignSetting")
    public ModelAndView jumpPage(HttpServletRequest request,HttpServletResponse response) {
        createLog(request, SysActionLogTypeEnum.查询, "月中/月末签单设置", "加载月中/月末签单设置页面");
        String[] executeFlags = new String[]{"不执行","执行"};
        String paramKey = "end_of_month_opened";
        String magicType = "codeHelper";
        String endOfMonthOpened = sysParamDefineService.getSysParamValueCache(magicType, paramKey);
        String executeFlag = executeFlags[1];
        if("f".equals(endOfMonthOpened)){
            executeFlag = executeFlags[0];
        }
        String nowMonth = new SimpleDateFormat("yyyy-MM").format(new Date());
        ModelAndView mav = new ModelAndView();
        mav.setViewName("operation/monthSignSetting");
        mav.addObject("executeFlags", executeFlags);
        mav.addObject("executeFlag", executeFlag);
        mav.addObject("nowMonth", nowMonth);
        return mav;
    }
    
    /**
     * 月末签约特殊处理
     * @param nowMonth
     * @param executeFlag
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/monthSignSetUp")
    @ResponseBody
    public String update(String nowMonth, String executeFlag,HttpServletRequest request, HttpServletResponse response) {
        ResponseInfo responseInfo = null;
        String paramKey = "end_of_month_opened";
        String magicType = "codeHelper";
        createLog(request, SysActionLogTypeEnum.更新, "月中/月末签单设置", "变更月中/月末签单设置");
        try {
            String endOfMonthOpened = sysParamDefineService.getSysParamValueCache(magicType, paramKey);
            Calendar date = Calendar.getInstance();
            // 往前三天的日期
            date.add(Calendar.DATE, 3);
            int day = date.get(Calendar.DATE);
            // 签单调整的设置日期
            String signSettingDays = sysParamDefineService.getSysParamValueCache(magicType, "signSettingDays");
            List<String> dayList = new ArrayList<String>();
            try {
                dayList = Arrays.asList(signSettingDays.split(","));
            } catch (Exception e) {
                dayList = Arrays.asList(new String[] { "1", "2", "3", "16", "17", "18" });
            }
            // 未到签单调整设置日期
            if(!dayList.contains(String.valueOf(day))){
                responseInfo = new ResponseInfo(ResponseEnum.FULL_MSG.getCode(), "设置失败，未到签单调整设置日期!");
                return toResponseJSON(responseInfo);
            }
            String paramValue = "f";
            if ("执行".equals(executeFlag)) {
                paramValue = "t";
            }
            String content = endOfMonthOpened + "-->" + paramValue;
            // 签单设置
            sysParamDefineService.updateSysParamValue(magicType, paramKey,paramValue);
            // 日志记录
            loanLogService.createLog(null, content,"MonthSignSettingController", "info", "月中/月末签单设置");
            responseInfo = new ResponseInfo(ResponseEnum.SYS_SUCCESS);
        } catch (PlatformException e) {
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(),e.getMessage());
        } catch (Exception e) {
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
        }
        return toResponseJSON(responseInfo);
    }
}
