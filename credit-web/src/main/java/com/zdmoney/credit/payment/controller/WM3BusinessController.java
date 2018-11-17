package com.zdmoney.credit.payment.controller;

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
import com.zdmoney.credit.job.DebitOfferFlow4WM3Job;

/**
 * 外贸3相关业务手动处理控制类
 * @author 00236640
 */
@Controller
@RequestMapping("/wm3")
public class WM3BusinessController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(WM3BusinessController.class);

    @Autowired
    private DebitOfferFlow4WM3Job debitOfferFlow4WM3Job;

    /**
     * 同步交易系统清分数据手动触发
     * 
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping("/job/syncLoanTradeClear")
    public String syncLoanTradeClear(HttpServletRequest request, HttpServletResponse response) {
        ResponseInfo responseInfo = null;
        log.info("同步交易系统清分数据开始。。。。。。");
        try {
            // 交易清分数据查询开始时间
            String startDateStr = request.getParameter("startDate");
            // 交易清分数据查询截止时间
            String endDateStr = request.getParameter("endDate");
            if (Strings.isEmpty(startDateStr)) {
                throw new PlatformException(ResponseEnum.FULL_MSG, "开始时间不能为空！");
            }
            if (Strings.isEmpty(endDateStr)) {
                throw new PlatformException(ResponseEnum.FULL_MSG, "截止时间不能为空！");
            }
            // 查询时间格式化
            String startDate = Dates.getDateTime(Dates.parse(startDateStr, Dates.DEFAULT_DATE_FORMAT), Dates.DEFAULT_DATE_FORMAT);
            String endDate = Dates.getDateTime(Dates.parse(endDateStr, Dates.DEFAULT_DATE_FORMAT),Dates.DEFAULT_DATE_FORMAT);
            debitOfferFlow4WM3Job.syncLoanTradeClear(startDate, endDate);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_SUCCESS);
        } catch (PlatformException e) {
            logger.error("同步交易系统清分数据异常：", e);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("同步交易系统清分数据异常：", e);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
        }
        log.info("同步交易系统清分数据结束。。。。。。");
        return toResponseJSON(responseInfo);
    }

    /**
     * 推送分账信息至外贸信托
     * 
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping("/job/syncOfferToWm3")
    public String syncOfferToWm3(HttpServletRequest request, HttpServletResponse response) {
        ResponseInfo responseInfo = null;
        log.info("推送分账信息至外贸信托开始。。。。。。");
        try {
            debitOfferFlow4WM3Job.syncOfferToWm3();
            responseInfo = new ResponseInfo(ResponseEnum.SYS_SUCCESS);
        } catch (PlatformException e) {
            logger.error("推送分账信息至外贸信托异常：", e);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(), e.getMessage());
        } catch (Exception e) {
            logger.error("推送分账信息至外贸信托异常：", e);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD);
        }
        log.info("推送分账信息至外贸信托结束。。。。。。");
        return toResponseJSON(responseInfo);
    }

}
