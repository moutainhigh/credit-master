package com.zdmoney.credit.loan.controller;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.vo.ResponseInfo;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.loan.service.pub.ILoanRepaymentStateService;

/**
 * 借款每月还款状态初始化控制类
 * @author 00236640
 */
@Controller
@RequestMapping("/loanRepaymentState")
public class LoanRepaymentStateController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(LoanRepaymentStateController.class);

    @Autowired
    private ILoanRepaymentStateService loanRepaymentStateService;

    /**
     * 手动初始化每月债权还款状态数据
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping("/job/createLoanRepaymentState")
    public String createLoanRepaymentState(HttpServletRequest request, HttpServletResponse response) {
        ResponseInfo responseInfo = null;
        log.info("初始化每月债权还款状态数据开始。。。。。。");
        try {
            // 起始统计月份
            String startMonth = request.getParameter("startMonth");
            // 截止统计月份
            String endMonth = request.getParameter("endMonth");
            if (Strings.isEmpty(startMonth)) {
                throw new PlatformException(ResponseEnum.FULL_MSG, "起始统计月份不能为空！");
            }
            if (Strings.isEmpty(endMonth)) {
                throw new PlatformException(ResponseEnum.FULL_MSG, "截止统计月份不能为空！");
            }
            // 转换为date日期类型
            Date startDate = Dates.parse(startMonth + "-01", Dates.DEFAULT_DATE_FORMAT);
            Date endDate = Dates.parse(endMonth + "-01", Dates.DEFAULT_DATE_FORMAT);
            if(startDate.compareTo(endDate) > 0){
                throw new PlatformException(ResponseEnum.FULL_MSG, "起始统计月份不能大于截止统计月份！");
            }
            Calendar c1 = Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();
            c1.setTime(startDate);
            c2.setTime(endDate);
            Date tradeDate = null;
            String tradeMonth = "";
            while (c1.compareTo(c2) <= 0) {
                tradeDate = c1.getTime();
                tradeMonth = Dates.getDateTime(tradeDate, "yyyy-MM");
                long startTime = System.currentTimeMillis();
                log.info("初始化【"+ tradeMonth + "】债权还款状态数据开始。。。。。。");
                loanRepaymentStateService.createLoanRepaymentState(tradeDate);
                log.info("初始化【"+ tradeMonth + "】债权还款状态数据结束。。。。。。耗时：" + ((System.currentTimeMillis() - startTime)/1000) + "秒");
                // 开始日期加一个月直到大于结束日期为止
                c1.add(Calendar.MONTH, 1);
            }
            responseInfo = new ResponseInfo(ResponseEnum.SYS_SUCCESS);
        } catch (PlatformException e) {
            logger.error("初始化每月债权还款状态数据异常：", e);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("初始化每月债权还款状态数据异常：", e);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
        }
        log.info("初始化每月债权还款状态数据结束。。。。。。");
        return toResponseJSON(responseInfo);
    }
}
