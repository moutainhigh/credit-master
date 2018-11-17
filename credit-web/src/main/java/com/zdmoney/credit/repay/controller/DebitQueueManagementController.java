package com.zdmoney.credit.repay.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zdmoney.credit.common.constant.DebitNotifyStateEnum;
import com.zdmoney.credit.common.constant.DebitRepayTypeEnum;
import com.zdmoney.credit.common.constant.DebitResultStateEnum;
import com.zdmoney.credit.common.constant.PayPartyEnum;
import com.zdmoney.credit.common.constant.SysActionLogTypeEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.vo.AttachmentResponseInfo;
import com.zdmoney.credit.common.vo.ResponseInfo;
import com.zdmoney.credit.debit.domain.DebitQueueLog;
import com.zdmoney.credit.debit.service.pub.IDebitQueueLogService;
import com.zdmoney.credit.framework.controller.BaseController;
import com.zdmoney.credit.repay.vo.DebitQueueManagementVo;

/**
 * 代扣管理
 * @author 10098  2017年5月18日 上午10:57:36
 */
@Controller
@RequestMapping("/debitQueue")
public class DebitQueueManagementController extends BaseController{

    private static final Logger logger = Logger.getLogger(DebitQueueManagementController.class);
    
    @Autowired
    IDebitQueueLogService debitQueueLogService;
    /**
     * 加载代扣管理页面
     * @param requests
     * @param response
     * @param modelMap
     * @return
     * @throws IOException
     */
    @RequestMapping("/debitQueueManagementPage")
    public String debitQueueManagementPage(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
        createLog(request, SysActionLogTypeEnum.查询, "代扣管理","加载代扣管理页面");
        modelMap.put("debitResultStates", DebitResultStateEnum.values());
        modelMap.put("debitNotifyStates", DebitNotifyStateEnum.values());
        modelMap.put("repayTypes", DebitRepayTypeEnum.values());
        modelMap.put("payPartys", PayPartyEnum.values());
        modelMap.put("currDate", Dates.getDateTime(Dates.DEFAULT_DATETIME_FORMAT));
        modelMap.put("currDate2", DateTime.now().minusDays(1).toString(Dates.DEFAULT_DATETIME_FORMAT));
        return "/repay/debitQueueManagementPage";
    }
    
    /**
     * 获取代扣管理分页信息
     * @param debitQueueManagementVo
     * @param rows
     * @param page
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping("/searchQueueManagementList")
    public String searchQueueManagementList(DebitQueueManagementVo debitQueueManagementVo, int rows,int page,HttpServletRequest request, HttpServletResponse response){
        createLog(request, SysActionLogTypeEnum.查询, "代扣管理", "查询代扣管理信息");
        // 定义分页实例
        Pager pager = new Pager();
        pager.setRows(rows);
        pager.setPage(page);
        pager.setSidx("l.id");
        pager.setSort("desc");
        debitQueueManagementVo.setPager(pager);
        pager = debitQueueLogService.findDebitQueueManagementWithPg(debitQueueManagementVo);
        // 将数据对象转换成JSON字符串，返回给前台
        return toPGJSONWithCode(pager);
    }
    
    /**
     * 重发代扣
     * @param id
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping("/debitQueueLogResend")
    public String debitQueueLogResend(@RequestParam String debitIds, HttpServletRequest request, HttpServletResponse response){
        AttachmentResponseInfo<Map<String, Object>> responseInfo = null;
        try{
            if(Strings.isEmpty(debitIds)){
                throw new PlatformException(ResponseEnum.FULL_MSG, "请选择需划扣的记录！");
            }
            Map<String, Object> params = new HashMap<String, Object>();
            String[] nos = debitIds.split(",");
            params.put("debitIds", nos);
            List<DebitQueueLog> list = debitQueueLogService.findDebitQueueListByMap(params);
            if(CollectionUtils.isEmpty(list)){
                throw new PlatformException(ResponseEnum.FULL_MSG, "查询不到待划扣的记录！");
            }
            debitQueueLogService.executeEntrustDebit(list, Boolean.FALSE);
            responseInfo = new AttachmentResponseInfo<>(ResponseEnum.SYS_SUCCESS.getCode(),"发送成功");
        }catch(PlatformException p){
            logger.info("【陆金所】重发代扣异常p"+p.getMessage(),p);
            responseInfo = new AttachmentResponseInfo<>(ResponseEnum.FULL_MSG, p.getMessage());
        }catch(Exception e){
            logger.info("【陆金所】重发代扣异常e"+e.getMessage(),e);
            responseInfo = new AttachmentResponseInfo<>(ResponseEnum.FULL_MSG, "重发代扣异常"+e.getMessage());
        }
        return toResponseJSON(responseInfo);
    }
    
    /**
     * 保证金垫付（包括逾期代偿和一次性回购）
     * @param id
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping("/marginPay")
    public String marginPay(@RequestParam String debitIds, HttpServletRequest request, HttpServletResponse response){
        AttachmentResponseInfo<Map<String, Object>> responseInfo = null;
        try{
            if(Strings.isEmpty(debitIds)){
                throw new PlatformException(ResponseEnum.FULL_MSG, "请选择需划扣的记录");
            }
            Map<String, Object> params = new HashMap<String, Object>();
            String[] ids = debitIds.split(",");
            params.put("ids", ids);
            logger.info("陆金所保证金逾期代偿或一次性回购发起的debit_queue_id为：" + debitIds);
            List<DebitQueueLog> list = debitQueueLogService.findDebitQueueListByMap(params);
            if(CollectionUtils.isEmpty(list)){
                throw new PlatformException(ResponseEnum.FULL_MSG, "查询不到待划扣的记录！");
            }
            debitQueueLogService.executeEntrustDebit(list, Boolean.TRUE);
            responseInfo = new AttachmentResponseInfo<>(ResponseEnum.SYS_SUCCESS.getCode(),"发送成功");
        }catch(PlatformException p){
            logger.info("【陆金所】保证金逾期代偿异常p:"+p.getMessage(),p);
            responseInfo = new AttachmentResponseInfo<>(ResponseEnum.FULL_MSG, p.getMessage());
        }catch(Exception e){
            logger.info("【陆金所】保证金逾期代偿异常e:"+e.getMessage(),e);
            responseInfo = new AttachmentResponseInfo<>(ResponseEnum.FULL_MSG, "保证金垫付异常"+e.getMessage());
        }
        return toResponseJSON(responseInfo);
    }
    
    /**
     * 一键代扣
     * @param id
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping("/oneKeyResendDebitQueueLog")
    public String oneKeyResendDebitQueueLog(HttpServletRequest request, HttpServletResponse response){
        AttachmentResponseInfo<Map<String, Object>> responseInfo = null;
        try{
            Map<String, Object> params = new HashMap<String, Object>();
            String toyday = Dates.getDateTime(Dates.DEFAULT_DATE_FORMAT);
            params.put("createDate", toyday);// 得到昨天的时间 今天的不发送代扣请求，时间<今天
            params.put("repayTypes", new String[]{DebitRepayTypeEnum.委托还款.getCode(),DebitRepayTypeEnum.提前结清.getCode()});
            params.put("debitResultStates",new String[] { DebitResultStateEnum.未划扣.getCode(), DebitResultStateEnum.划扣失败.getCode() });
            List<DebitQueueLog> list = debitQueueLogService.findDebitQueueListByMap(params);

            //获取当天需要逾期代偿 一次性回购的 划扣信息
            Map<String, Object> compensatoryParams = new HashMap<String, Object>();
            compensatoryParams.put("createTime", Dates.getCurrDate());
            compensatoryParams.put("repayTypes", new String[]{DebitRepayTypeEnum.逾期代偿.getCode(), DebitRepayTypeEnum.一次性回购.getCode()});
            compensatoryParams.put("debitResultStates",new String[] { DebitResultStateEnum.未划扣.getCode(), DebitResultStateEnum.划扣失败.getCode() });
            List<DebitQueueLog> compensatoryList = debitQueueLogService.findDebitQueueListByMap(compensatoryParams);
            if(CollectionUtils.isNotEmpty(compensatoryList)){
                list.addAll(compensatoryList);
            }
            debitQueueLogService.executeEntrustDebit(list, Boolean.FALSE);
            responseInfo = new AttachmentResponseInfo<>(ResponseEnum.SYS_SUCCESS.getCode(),"发送成功");
        }catch(PlatformException p){
            logger.info("【陆金所】发送一键代扣异常p:"+p.getMessage(),p);
            responseInfo = new AttachmentResponseInfo<>(ResponseEnum.FULL_MSG, p.getMessage());
        }catch(Exception e){
            logger.info("【陆金所】发送一键代扣异常e:"+e.getMessage(),e);
            responseInfo = new AttachmentResponseInfo<>(ResponseEnum.FULL_MSG, "一键代扣异常"+e.getMessage());
        }
        return toResponseJSON(responseInfo);
    }
    
    /**
     * 陆金所挂账金额分账处理
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping("/accAmountRepayDeal")
    public String accAmountRepayDeal(HttpServletRequest request, HttpServletResponse response) {
        ResponseInfo responseInfo = null;
        try {
            // 分账时间
            String tradeDate = request.getParameter("tradeDate");
            if (Strings.isEmpty(tradeDate)) {
                throw new PlatformException(ResponseEnum.FULL_MSG, "分账时间不能为空！");
            }
            Date date = Dates.parse(tradeDate, Dates.DEFAULT_DATE_FORMAT);
            debitQueueLogService.executeBillRepayDeal(date);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_SUCCESS);
        } catch (PlatformException e) {
            logger.error("【陆金所】挂账金额分账异常:", e);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(), "挂账金额分账异常：" + e.getMessage());
        } catch (Exception e) {
            logger.error("【陆金所】挂账金额分账异常:", e);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(), e.getMessage());
        }
        return toResponseJSON(responseInfo);
    }
    
    /**
     * 更新划扣队列数据
     * @param debitQueueLog
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping("/updateDebitQueue")
    public String updateDebitQueue(DebitQueueLog debitQueueLog, HttpServletRequest request, HttpServletResponse response) {
        ResponseInfo responseInfo = null;
        try {
            if (Strings.isEmpty(debitQueueLog.getId())) {
                throw new PlatformException(ResponseEnum.FULL_MSG, "划扣队列Id参数不能为空！");
            }
            // 更新划扣队列数据
            debitQueueLogService.updateDebitQueueLog(debitQueueLog);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_SUCCESS.getCode(), "操作成功！");
        } catch (PlatformException e) {
            logger.error("更新划扣队列数据异常:", e);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(), "更新划扣队列数据异常：" + e.getMessage());
        } catch (Exception e) {
            logger.error("更新划扣队列数据异常:", e);
            responseInfo = new ResponseInfo(ResponseEnum.SYS_FAILD.getCode(), e.getMessage());
        }
        return toResponseJSON(responseInfo);
    }
}
