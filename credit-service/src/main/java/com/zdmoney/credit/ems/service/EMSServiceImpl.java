package com.zdmoney.credit.ems.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.zdmoney.credit.common.constant.LoanStateEnum;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.json.JackJsonUtil;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.ems.service.pub.IEMSService;
import com.zdmoney.credit.loan.dao.pub.IVLoanInfoDao;
import com.zdmoney.credit.loan.service.pub.ILoanLogService;
import com.zdmoney.credit.system.dao.pub.IBaseEmssendInfoDao;
import com.zdmoney.credit.system.dao.pub.IBaseRepayRemindDao;
import com.zdmoney.credit.system.domain.BaseEmssendInfo;
import com.zdmoney.credit.system.domain.BaseRepayRemind;
import com.zdmoney.credit.system.service.pub.ISendMailService;
import com.zdmoney.credit.system.service.pub.ISequencesService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;

@Service
public class EMSServiceImpl implements IEMSService {
	private static final Logger logger = Logger.getLogger(EMSServiceImpl.class);
	
//	@Autowired
    private CloseableHttpClient httpClient ;

    private Map<String, Object> smsContextMap;
    
    @Autowired
    private IBaseRepayRemindDao repayRemindDao;
    
    @Autowired
    private IBaseEmssendInfoDao emsSendInfoDao;
    
    @Autowired
    private ISysParamDefineService sysParamDefineService;
    
    @Autowired
    private ISequencesService sequencesService;
    
	@Autowired
	private ILoanLogService loanLogService;
	
	@Autowired
	private ISendMailService sendMailService;
	
	@Autowired
	private IVLoanInfoDao loanDao;
	
    @Autowired
    private DataSourceTransactionManager txManager;
    
    private static Map<String, String> errorCodeMsgMap = new HashMap<String, String>();
    static {
        errorCodeMsgMap.put("ER:1000", "系统异常");
		errorCodeMsgMap.put("ER:1001", "访问数据库失败");
		errorCodeMsgMap.put("ER:1100", "无法确定用户");
		errorCodeMsgMap.put("ER:1200", "定时无效（不合逻辑）");
		errorCodeMsgMap.put("ER:1300", "号码无效（格式不正确）");
		errorCodeMsgMap.put("ER:1301", "号码无效（号段不受支持）");
		errorCodeMsgMap.put("ER:1302", "号码在黑名单中");
		errorCodeMsgMap.put("ER:1400", "内容为空");
		errorCodeMsgMap.put("ER:1401", "内容存在敏感词");
		errorCodeMsgMap.put("ER:1500", "过快地向同一个号码发送重复内容");
		errorCodeMsgMap.put("ER:1600", "目标发送器不存在");
		errorCodeMsgMap.put("ER:1601", "发送器不接受");
		errorCodeMsgMap.put("ER:1602", "发送器未连接或已断开");
		errorCodeMsgMap.put("ER:1603", "发送器API返回null值");
		errorCodeMsgMap.put("ER:1604", "发送器API抛出异常");
		errorCodeMsgMap.put("169", "空号");
		errorCodeMsgMap.put("DTBLACK", "空号");
		errorCodeMsgMap.put("2:12", "空号");
		errorCodeMsgMap.put("IB:0039", "空号");
    }
	
    private void initContext(Date repayDate) {
        httpClient = HttpClients.createDefault();
        smsContextMap = new HashMap<String, Object>();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = df.format(new Date());
        smsContextMap.put("beginDateStr", dateStr+" 00:00:00");
        smsContextMap.put("endDateStr", dateStr+" 23:59:59");
        smsContextMap.put("sms.maxBatchSize", Integer.parseInt(sysParamDefineService.getSysParamValueCache("sms", "sms.maxBatchSize")));
        Calendar repayDayCal = Calendar.getInstance();
        repayDayCal.setTime(repayDate);
        smsContextMap.put("mf.repayDayDateStr",  (repayDayCal.get(Calendar.MONTH) + 1) +"月"
        + repayDayCal.get(Calendar.DAY_OF_MONTH) + "日");

        if (repayDayCal.get(Calendar.YEAR) == 2015 && repayDayCal.get(Calendar.MONTH) == 1){//2015年2月
            smsContextMap.put("mf.个贷.正常", new MessageFormat(
            		sysParamDefineService.getSysParamValueCache("sms", "sms.contentPattern.personal.normal.chunjie")));
            smsContextMap.put("mf.个贷.逾期", new MessageFormat(
            		sysParamDefineService.getSysParamValueCache("sms", "sms.contentPattern.personal.unnormal.chunjie")));
            smsContextMap.put("mf.渠道.正常", new MessageFormat(
            		sysParamDefineService.getSysParamValueCache("sms", "sms.contentPattern.channel.normal.chunjie")));
            smsContextMap.put("mf.渠道.逾期", new MessageFormat(
            		sysParamDefineService.getSysParamValueCache("sms", "sms.contentPattern.channel.unnormal.chunjie")));
        } else {
            smsContextMap.put("mf.个贷.正常", new MessageFormat(
            		sysParamDefineService.getSysParamValueCache("sms", "sms.contentPattern.personal.normal")));
            smsContextMap.put("mf.个贷.逾期", new MessageFormat(
            		sysParamDefineService.getSysParamValueCache("sms", "sms.contentPattern.personal.unnormal")));
            smsContextMap.put("mf.渠道.正常", new MessageFormat(
            		sysParamDefineService.getSysParamValueCache("sms", "sms.contentPattern.channel.normal")));
            smsContextMap.put("mf.渠道.逾期", new MessageFormat(
            		sysParamDefineService.getSysParamValueCache("sms", "sms.contentPattern.channel.unnormal")));
        }
    }
    
    public void batchGroupSendUnsend(Date repayDate,Date smsDate) throws IOException{
        initContext(smsDate);
        batchGroupSend(repayDate, LoanStateEnum.正常, "个贷", "未发送", null);
        batchGroupSend(repayDate, LoanStateEnum.逾期, "个贷", "未发送", null);
        batchGroupSend(repayDate, LoanStateEnum.正常, "渠道", "未发送", null);
        batchGroupSend(repayDate, LoanStateEnum.逾期, "渠道", "未发送", null);

        //针对timeout的立刻重发
        for(int t=0; t<3 ;t++) {
            batchGroupSendFailedRetryImmediate(repayDate);
        }
        httpClient.close();
    }

    @Override
    public void batchGroupSendFailedRetry(Date repayDate,Date smsDate) throws IOException{
        initContext(smsDate);
        batchGroupSend(repayDate, LoanStateEnum.正常, "个贷", null, "发送失败");
        batchGroupSend(repayDate, LoanStateEnum.逾期, "个贷", null, "发送失败");
        batchGroupSend(repayDate, LoanStateEnum.正常, "渠道", null, "发送失败");
        batchGroupSend(repayDate, LoanStateEnum.逾期, "渠道", null, "发送失败");

        //针对timeout的立刻重发
        for(int t=0; t<3 ;t++) {
            batchGroupSendFailedRetryImmediate(repayDate);
        }
        httpClient.close();
    }

    @Override
    public void batchGroupSendFailedRetryImmediate(Date repayDate){
        batchUpdateResult(repayDate);
        batchGroupSend(repayDate, LoanStateEnum.正常, "个贷", null, "发送失败");
        batchGroupSend(repayDate, LoanStateEnum.逾期, "个贷", null, "发送失败");
        batchGroupSend(repayDate, LoanStateEnum.正常, "渠道", null, "发送失败");
        batchGroupSend(repayDate, LoanStateEnum.逾期, "渠道", null, "发送失败");
    }

    private void batchGroupSend(Date repayDate, LoanStateEnum loanState, String type,  String sendState, String deliverState){
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("repayDate", repayDate);
        param.put("loanState", loanState.toString());
        param.put("type", type);
        if (Strings.isNotEmpty(sendState)) {
        	param.put("sendState", sendState);
        }

        	param.put("deliverState", deliverState);


    	List<BigDecimal> amountList = repayRemindDao.getAmountList(param);

        for(BigDecimal amount : amountList){
            batchGroupSendByAmount(repayDate, loanState, type, amount, sendState, deliverState);
        }
    }

    // 对deliverState=''的不应该再次重复发送。。。(已发送，状态未知)
    // "未提交" + null
    // "已提交" + "发送失败"
    // "提交失败" + "发送失败"
    private void batchGroupSendByAmount(Date repayDate,LoanStateEnum loanState, String type, 
    		BigDecimal amount, String sendState, String deliverState){

        String content = buildContent(type, loanState.toString(), amount);
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("repayDate", repayDate);
        param.put("loanState", loanState.toString());
        param.put("type", type);
        param.put("amount", amount);
        if (Strings.isNotEmpty(sendState)) {
        	param.put("sendState", sendState);
        }

        	param.put("deliverState", deliverState);

        
        Pager pager = new Pager();
        pager.setRows((int) smsContextMap.get("sms.maxBatchSize"));
        
        pager.setSidx("id");
        pager.setSort(Pager.DIRECTION_ASC);
        
        param.put("pager", pager);
        
        int page = 1;
        while(true){
            @SuppressWarnings("unchecked")
			List<BaseRepayRemind> remindList  = repayRemindDao.findWithPgByMap(param).getResultList();

            if(remindList.isEmpty()) {
            	break;
            }
            List<BaseRepayRemind> list = new ArrayList<BaseRepayRemind>(new LinkedHashSet<BaseRepayRemind>(remindList));

//            long start2 = System.currentTimeMillis();
            groupSendSMS(list, content);
//            long end2 = System.currentTimeMillis();
//            System.out.println("---- sendEMS :" + (end2 - start2));
            
            page += 1;
            
            pager.setPage(page);
            
        }
    }

	private void groupSendSMS(final List<BaseRepayRemind> remindList, final String content) {
		final String mobiles = formatMobiles(remindList);
		final String sendId = groupGenerateSendId(remindList); // 时间戳+remindId；
		// 批量更新为 已发送 或 发送失败；考虑exception的log可以放到credit的log表中。

		SendResult sendResult = null;
		groupLogSuccess(remindList, sendId);
		try {
			// System.out.println(content);
			sendResult = doSendSMS(httpClient, sendId, content, mobiles);
			if (sendResult != null && sendResult.isSuccess() == false) {
				groupLogFail(remindList, sendResult, null);
			}
		} catch (Exception e) {
			groupLogFail(remindList, sendResult, e);
			logger.error("sendEMS-向平台提交短信异常失败", e);// 到这里多半是网络故障，或者数据构造错误
		}

	}

    // create BaseEmssendInfo
    private void groupLogSuccess( List<BaseRepayRemind> remindList, String sendId){
        for(BaseRepayRemind remind : remindList) {
            logSendSucess(remind, sendId);
        }
    }

    // just update BaseEmssendInfo (not create)
    private void groupLogFail(List<BaseRepayRemind> remindList, SendResult sendResult, Exception e){
        for(BaseRepayRemind remind : remindList) {
        	BaseEmssendInfo sendInfo = emsSendInfoDao.getRepayRemindLastEmssendInfo(remind.getId());
            logSendFail(sendInfo, sendResult, e);
        }
    }

    private String groupGenerateSendId(List<BaseRepayRemind> remindList){
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return df.format(new Date()) + StringUtils.leftPad(remindList.get(0).getId().toString(),10,'0'); //17+10=27
    }

    @Transactional(propagation=Propagation.REQUIRES_NEW)
    private BaseEmssendInfo logSendSucess(BaseRepayRemind repayRemindEMS,String sendId) {
        BaseEmssendInfo emsInfo = new BaseEmssendInfo();
        emsInfo.setSendState("已发送");
        emsInfo.setMobile(repayRemindEMS.getMphone());
        emsInfo.setSendId(sendId);
        emsInfo.setRepayRemindId(repayRemindEMS.getId());
        emsInfo.setId(sequencesService.getSequences(SequencesEnum.BASE_EMSSEND_INFO));
        
        emsSendInfoDao.insert(emsInfo);
        
        repayRemindEMS.setSendState("已发送");

        // important 重置deliverState为空
        repayRemindEMS.setSendStateRemark("");
        repayRemindEMS.setDeliverState("");
        repayRemindEMS.setDeliverStateRemark("");
        
        repayRemindDao.updateByPrimaryKeySelective(repayRemindEMS);
//
//        repayRemindEMS.save(flush:true)
        return emsInfo;
    }

    @Transactional(propagation=Propagation.REQUIRES_NEW)
    private void logSendFail(BaseEmssendInfo sendInfo, SendResult sendResult, Exception e) {
    	sendInfo.setSendState("发送失败");
//        sendInfo.deliverState =  '发送失败';
        if(sendResult!=null){
            sendInfo.setSendStateRemark(StringUtils.substring(sendResult.getMessage(), 0, 255));
        }

        if(e != null){
            sendInfo.setSendStateRemark("提交发送时抛出异常：" +  StringUtils.substring(e.getMessage(), 0, 255));
        }
        emsSendInfoDao.updateByPrimaryKeySelective(sendInfo);
        
        BaseRepayRemind repayRemind = new BaseRepayRemind();
        repayRemind.setId(sendInfo.getRepayRemindId());
        repayRemind.setSendState(sendInfo.getSendState());
        repayRemind.setSendStateRemark(sendInfo.getSendStateRemark());
        repayRemind.setDeliverState(sendInfo.getDeliverState());
        
        repayRemindDao.updateByPrimaryKeySelective(repayRemind);

    }

    @Override
    public void batchGroupUpdateResult(Date repayDate,Date smsDate) throws IOException {
        initContext(smsDate);

        batchUpdateResult(repayDate);
        List<String> sendIdList =  emsSendInfoDao.getDistinctSendIdByRepayDate(repayDate);
        // sendState = '已发送' &&  deliverState = null
        for(String sendId : sendIdList){
            try {
                doUpdateResult(sendId);
            }catch (Exception e){
        		loanLogService.createLog("RepayRemindEMS", "error", "查询状态报告时异常出错sendId=" + sendId, "SYSTEM");
            }
        }

        httpClient.close();
    }

    private void batchUpdateResult(Date repayDate){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("repayDate", repayDate);
		params.put("sendState", "发送失败");
		params.put("deliverState", null);

		List<BaseEmssendInfo> sendInfoList = emsSendInfoDao.getSendInfoByRepayDateAndState(params);

		for (BaseEmssendInfo sendInfo : sendInfoList) {
			sendInfo.setDeliverState(sendInfo.getSendState());
			sendInfo.setDeliverStateRemark(sendInfo.getSendStateRemark());
			
			emsSendInfoDao.updateByPrimaryKeySelective(sendInfo);
			
			BaseRepayRemind remind = new BaseRepayRemind();
			remind.setId(sendInfo.getRepayRemindId());
			remind.setDeliverState(sendInfo.getDeliverState());
			remind.setDeliverStateRemark(sendInfo.getDeliverStateRemark());
			
			repayRemindDao.updateByPrimaryKeySelective(remind);
		}
    }

    private void doUpdateResult(String sendId) throws Exception {

        Report report = getReport(httpClient, sendId, null, (String)smsContextMap.get("beginDateStr"), 
        		(String)smsContextMap.get("endDateStr"));
        for(final SMReport smReport : report.getSmReports()) {
        	final String sendIdC = smReport.getSendId();
        	final String mobileC = smReport.getMobile();

        	try {
            	TransactionTemplate transactionTemplate = new TransactionTemplate(txManager);
            	transactionTemplate.setPropagationBehavior(Propagation.REQUIRES_NEW.value());
            	
            	transactionTemplate.execute(new TransactionCallback<Object>() {

    				@Override
    				public Object doInTransaction(TransactionStatus status) {
    					BaseEmssendInfo condition = new BaseEmssendInfo();
    					condition.setSendId(sendIdC);
    					condition.setMobile(mobileC);
    					BaseEmssendInfo sendInfo = emsSendInfoDao.get(condition);
    					
    					if (smReport.getResult().equals("0")) {
    						sendInfo.setDeliverState("发送成功");
    					} else {
    						sendInfo.setDeliverState("发送失败");
    						sendInfo.setDeliverStateRemark(smReport.getErrorCode());
    					}
    					
    					emsSendInfoDao.updateByPrimaryKeySelective(sendInfo);
    					
    					BaseRepayRemind newRemindValue = new BaseRepayRemind();
    					newRemindValue.setId(sendInfo.getRepayRemindId());
    					
    					newRemindValue.setDeliverState(sendInfo.getDeliverState());
    					newRemindValue.setDeliverStateRemark(sendInfo.getDeliverStateRemark());
    					
    					repayRemindDao.updateByPrimaryKeySelective(newRemindValue);
    					
    					return null;
    				}
            		
            	});
        	} catch (Exception e) {
        		loanLogService.createLog("RepayRemindEMS", "error", "更新状态报告时异常出错sendId=" + 
        					sendIdC + "，mobile=" + mobileC, "SYSTEM");
        	}

        }

    }

    private String buildContent(String type, String loanState, BigDecimal amount) {
        MessageFormat mf = (MessageFormat) smsContextMap.get("mf." + type +"." + loanState);
        return  mf.format(new Object[]{amount, smsContextMap.get("mf.repayDayDateStr")});
    }

//    private String buildContent(MessageFormat mf, BigDecimal amount, Date date){
//        String dateStr = "${date.getMonth()+1}月${date.getDate()}日"
//        return  mf.format([amount,dateStr] as Object[]);
//    }

    @Override
    public void generateData(Date repayDate, boolean forceRegernateRepayMind){
        if(forceRegernateRepayMind == false){
        	Map<String, Object> params = new HashMap<String, Object>();
        	params.put("repayDate", repayDate);
            int nums = repayRemindDao.getCountByRepayDateAndDeliverState(params);
            //RepayRemind.executeQuery("select count(*) from RepayRemind where repayDate=:repayDate",[repayDate:repayDate]);
            if(nums >0)  return;
        }
        Date createDate = new Date();
        String todayStr = new SimpleDateFormat("yyyy-MM-dd").format(createDate);
        
        Calendar repayDayCal = Calendar.getInstance();
        repayDayCal.setTime(repayDate);
        long promiseReturnDay = repayDayCal.get(Calendar.DAY_OF_MONTH);
        
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("repayDate", repayDate);
        params.put("createDate", createDate);
        params.put("todayStr", todayStr);
        params.put("promiseReturnDay", promiseReturnDay);
        
        repayRemindDao.emsGenerateData(params);
                
//        Sql sql = new Sql(dataSource);
//        String sqlStr = """insert into repay_remind (id, version, date_created, last_updated, send_state,  repay_date, 
//        loan_id, mphone,loan_state, type, amount)
//          select nextval('repay_remind_id_seq'), 0, '${createDate}', '${createDate}', '未发送', '${repayDate}', 
//        t2.loan_id, t2.mphone, t2.loan_state,TYPE, t2.quanbuyinghuan
//          from (
//          select
//            t1.loan_id, t1.mphone, t1.loan_state,
//            T1.department_type AS TYPE,
//             CASE
//            WHEN T1.department_type <> '渠道' THEN
//                T1.yuqifaxi + T1.benxi - T1.guazhang
//            ELSE
//                T1.dangqiyinghuanbenxi  - T1.guazhang
//            END AS quanbuyinghuan
//            FROM
//                (
//                    SELECT
//                        A .loan_id,
//                        p.mphone,
//                        ba.department_type,
//                        CASE
//                    WHEN DATE '${todayStr}'- MIN (penalty_date) <= 0 THEN
//                        0
//                    ELSE
//                        CASE
//                    WHEN b.penalty_rate = 0.001 THEN
//                        round(
//                            (
//                                SELECT
//                                    SUM (deficit)
//                                FROM
//                                    repayment_detail
//                                WHERE
//                                    loan_id = A .loan_id
//                                AND repayment_State <> '结清'
//                            ) * (
//                                DATE '${todayStr}' - MIN (penalty_date)
//                            ) * 0.001,
//                            2
//                        )
//                    ELSE
//                        round(
//                            b.residual_pact_money * (
//                                DATE '${todayStr}' - MIN (penalty_date)
//                            ) * b.penalty_rate,
//                            2
//                        )
//                    END
//                    END yuqifaxi,
//                    SUM (
//                        CASE
//                        WHEN (
//                            return_date - DATE '${repayDate}'
//                        ) <= 0 THEN
//                            deficit
//                        ELSE
//                            0
//                        END
//                    ) benxi,
//                    SUM (
//                        CASE
//                        WHEN (
//                            return_date - DATE '${repayDate}'
//                        ) >= 0 THEN
//                            deficit
//                        ELSE
//                            0
//                        END
//                    ) dangqiyinghuanbenxi,
//                    SUM (
//                        CASE
//                        WHEN (
//                            return_date - DATE '${repayDate}'
//                        ) < 0 THEN
//                            deficit
//                        ELSE
//                            0
//                        END
//                    ) yuqiyinghuanbenxi,
//                    COALESCE (
//                       (
//                        SELECT
//                         lr_amount
//                        FROM
//                         ledger
//                        WHERE
//                         lr_account = A .loan_id || ''
//                       ),
//                       0
//                      ) guazhang,
//                    b.loan_state,
//                    b.plan_id
//                FROM
//                    repayment_detail A
//                INNER JOIN loan b ON A .loan_id = b. ID
//                INNER JOIN base_area ba on b.sales_department_id = ba.id
//                INNER JOIN person p ON b.borrower_id = p.id
//                WHERE
//                    b.promise_return_date = ${promiseReturnDay}
//                AND b.loan_state IN ('正常', '逾期')
//                AND return_date <= DATE '${repayDate}'
//                AND repayment_state IN (
//                    '未还款',
//                    '不足额还款',
//                    '不足罚息'
//                )
//                GROUP BY
//                    A .loan_id,
//                    p.mphone,
//                    penalty_rate,
//                    residual_pact_money,
//                    b.loan_state,
//                    b.plan_id,
//                    ba.department_type
//                ) T1
//          where not exists( select 1 from repay_remind where loan_id = t1.loan_id and repay_date = '${repayDate}' )
//            order by type desc, t1.loan_state desc,  quanbuyinghuan desc ) T2
//          where  t2.quanbuyinghuan >0
//       """
//        sql.execute(sqlStr);

    }

    @Override
    public String emailSendResult(Date repayDate,Date smsDate) throws IOException{
        initContext(smsDate);

		loanLogService.createLog("RepayRemindEMS", "info", "开始生成还款提醒短信结果报告........", "SYSTEM");
        Calendar repayDayCal = Calendar.getInstance();
        repayDayCal.setTime(repayDate);
        String filePath = buildSendResult(repayDate);
        String title = (repayDayCal.get(Calendar.MONTH)+1) + "月" + repayDayCal.get(Calendar.DAY_OF_MONTH) + "日还款日还款提醒短信反馈";
        int totalCount = 0;
        int emscount = 0;
        int deliverSucceed = 0;
        int deliverUnknown = 0;
        int deliverFailed = 0;

        String[] tos = sysParamDefineService.getSysParamValueCache("sms", "sms.email.sendResult.to").split(";") ;
        String[] ccs = sysParamDefineService.getSysParamValueCache("sms", "sms.email.sendResult.cc").split(";");

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("repayDate", repayDate);
        List<String> states = new ArrayList<String>();

        totalCount = repayRemindDao.getCountByRepayDate(params);
        emscount = repayRemindDao.getEmsCount(repayDate);
        states.add("发送成功");
        params.put("states", states);
        deliverSucceed = repayRemindDao.getCountByRepayDateAndDeliverState(params);
        states.clear();
        states.add("发送失败");
        deliverFailed = repayRemindDao.getCountByRepayDateAndDeliverState(params);
        states.clear();
        params.remove("states");
        deliverUnknown = repayRemindDao.getCountByRepayDateAndDeliverState(params);

        String content = MessageFormat.format("本次发送的为{0}应还款客户的提醒短信，应还款客户{1}个，"
        		+ "一共发送{2}条短信，发送成功的短信{3}条，发送失败的短信{4}条，状态不明的短信{5}条",
                repayDayCal.get(Calendar.YEAR) + "年" + 
        		(repayDayCal.get(Calendar.MONTH)+1) + "月" + 
                repayDayCal.get(Calendar.DAY_OF_MONTH) + "日",
                totalCount, emscount, deliverSucceed, deliverFailed, deliverUnknown);
        sendMailService.sendMail(tos,ccs,title,content,new String[]{filePath});
		loanLogService.createLog("RepayRemindEMS", "info", "完成发送还款提醒短信结果报告........", "SYSTEM");

        return filePath;
    }

    private String buildSendResult(Date repayDate) throws IOException{
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("new sheet");

        //区域、营业部、客户姓名、客户身份证、客户电话号码、账户状态（是否逾期）、每月应还款额
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("区域");
        headerRow.createCell(1).setCellValue("营业部");
        headerRow.createCell(2).setCellValue("客户姓名");
        headerRow.createCell(3).setCellValue("身份证号");
        headerRow.createCell(4).setCellValue("电话号码");
        headerRow.createCell(5).setCellValue("借款状态");
        headerRow.createCell(6).setCellValue("每月应还款额");
        headerRow.createCell(7).setCellValue("发送状态备注");
        headerRow.createCell(8).setCellValue("接收状态备注");

        List<Map<String, Object>> list = repayRemindDao.getSendResultData(repayDate);

        for (int i = 0; i < list.size(); i++) {
        	Map<String, Object> data = list.get(i);
            Row row = sheet.createRow(i+1);
            row.createCell(0).setCellValue((String)data.get("AREA"));
            row.createCell(1).setCellValue((String)data.get("SALES_DEP"));
            row.createCell(2).setCellValue((String)data.get("NAME"));
            row.createCell(3).setCellValue((String)data.get("IDNUM"));
            row.createCell(4).setCellValue((String)data.get("MPHONE"));
            row.createCell(5).setCellValue((String)data.get("LOAN_STATE"));
            BigDecimal eterm = (BigDecimal)data.get("RETURNETERM");
            if (null == eterm) {
            	eterm = BigDecimal.ZERO;
            }
            row.createCell(6).setCellValue(eterm.toPlainString());
            if(data.get("SEND_STATE_REMARK") == null || ((String)data.get("SEND_STATE_REMARK")).trim().equals("")){
                row.createCell(7).setCellValue((String)data.get("SEND_STATE_REMARK"));
            } else {
                row.createCell(7).setCellValue(parseErrorCode((String)data.get("SEND_STATE_REMARK")));
            }
            if(data.get("DELIVER_STATE_REMARK") == null || ((String)data.get("DELIVER_STATE_REMARK")).trim().equals("")){
                row.createCell(8).setCellValue("已发送，成功与否未知");
            } else {
                row.createCell(8).setCellValue(parseErrorCode((String)data.get("DELIVER_STATE_REMARK")));
            }
        }

        
        Calendar repayDayCal = Calendar.getInstance();
        repayDayCal.setTime(repayDate);
        String fileName = 
        		"还款日"+(repayDayCal.get(Calendar.MONTH)+1)+"月"+repayDayCal.get(Calendar.DAY_OF_MONTH)
        		+"日还款提醒短信发送结果" + Dates.getDateTime(new Date(), "yyyyMMddHHmmss")+".xlsx";   // ".jpg" //uploadedFile.contentType)
        String dir = sysParamDefineService.getSysParamValue("sms", "sms.uploads");
        File dirPath = new File(dir);
        dirPath.mkdirs();
        File f= new File(dirPath,fileName);
        FileOutputStream fileOut = new FileOutputStream(f);
        wb.write(fileOut);
        wb.close();
        
        fileOut.close();

        return f.getAbsolutePath();
    }
    
    private String parseErrorCode(String errorCode) {
        String msg = errorCodeMsgMap.get(errorCode);
        return msg!=null ? msg : errorCode;
    }

    //just sql query and do things in memory, not insert db
    @Override
    public String emailMPhone(Date repayDate) throws IOException{
        initContext(repayDate);
        Calendar repayDayCal = Calendar.getInstance();
        repayDayCal.setTime(repayDate);
        String filePath = buildWillSendPhone4WhiteList(repayDate);

        String[] tos = sysParamDefineService.getSysParamValueCache("sms","sms.email.sendPhoneList.to").split(";") ;
        String[] ccs = sysParamDefineService.getSysParamValueCache("sms","sms.email.sendPhoneList.cc").split(";");

        String content = MessageFormat.format("证大财富{0}应还款客户的提醒短信对应的手机号码，请尽快添加白名单",
        		repayDayCal.get(Calendar.YEAR) + "年" + (repayDayCal.get(Calendar.MONTH)+1) + "月" 
        		+ repayDayCal.get(Calendar.DAY_OF_MONTH) + "日");

        sendMailService.sendMail(tos, ccs, content,content,new String[]{filePath});

        return filePath;
    }

    private String buildWillSendPhone4WhiteList(Date repayDate) throws IOException{
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("new sheet");
        Calendar repayDayCal = Calendar.getInstance();
        repayDayCal.setTime(repayDate);

        int returnDate = repayDayCal.get(Calendar.DAY_OF_MONTH);
        List<String> lists = loanDao.getWillSendPhone4WhiteList(returnDate);

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("手机号码");

        for(int i = 0;i<lists.size();i++){
            Row row = sheet.createRow(i+1);
            row.createCell(0).setCellValue(lists.get(i));
        }

        String fileName = "还款日"+(repayDayCal.get(Calendar.MONTH) + 1) +"月"+repayDayCal.get(Calendar.DAY_OF_MONTH) + 
        		"日待发送还款提醒短信-号码" + Dates.getDateTime(new Date(), "yyyyMMddHHmmss")+".xlsx";
        		
        String dir = sysParamDefineService.getSysParamValue("sms", "sms.uploads");
        File dirPath = new File(dir);
        
        dirPath.mkdirs();
        File f= new File(dirPath,fileName);

        FileOutputStream fileOut = new FileOutputStream(f);
        wb.write(fileOut);
        wb.close();
        
        fileOut.close();

        return f.getAbsolutePath();
    }


    /*******************************************************************************************************************************************/

    private SendResult doSendSMS(HttpClient httpClient, String sendId, String msg, String... mobiles) throws Exception {
        return sendByPost(httpClient, sendId, msg, mobiles);
    }

    private SendResult sendByPost(HttpClient httpClient, String token, String msg, String... mobiles) throws Exception{
        HttpPost post = new HttpPost(sysParamDefineService.getSysParamValueCache("sms", "sms.serverUrl"));

        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("cmd", "sendSm"));
        params.add(new BasicNameValuePair("username", sysParamDefineService.getSysParamValueCache("sms", "sms.uid")));
        params.add(new BasicNameValuePair("password", sysParamDefineService.getSysParamValueCache("sms", "sms.pwd")));
        String mobs = formatMobileArray(mobiles);
        params.add(new BasicNameValuePair("smMtMessage", buildSendJson(
        		token,  mobs, msg, sysParamDefineService.getSysParamValueCache("sms", "beginDateStr"), "0")));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();//设置请求和传输超时时间
        post.setConfig(requestConfig);
        post.setEntity(entity);

        HttpResponse response  = httpClient.execute(post);
        String result = EntityUtils.toString(response.getEntity());
        return parseSendResultJson(result);
//        System.out.println(result);
    }

    private String formatMobiles(List<BaseRepayRemind> repayRemindList){
        int offset = repayRemindList.size() - 1;
        StringBuilder sb = new StringBuilder(repayRemindList.size()*3);
        for( int i = 0; i < offset; i++ )
        {
        		sb.append(repayRemindList.get(i).getMphone()).append(", ");
        }
        sb.append(repayRemindList.get(offset).getMphone());

        return sb.toString();
    }

    private String formatMobileArray(String... mobiles){
        int offset = mobiles.length - 1;
        StringBuilder sb = new StringBuilder(mobiles.length*3);
        for( int i = 0; i < offset; i++ )
        {
                sb.append(mobiles[i]).append(", ");
        }
        sb.append(mobiles[offset]);

        return sb.toString();
    }

    private Report getReport(HttpClient httpClient,String token, String mobile, String beginDateStr, String endDateStr) throws Exception {
        String resultStr = getReportJSONByPost(httpClient, token, mobile, beginDateStr, endDateStr);
        return parseReportJson(resultStr);
    }

    @SuppressWarnings("unused")
	private String getReportJSONByGet(HttpClient httpClient,String token, String mobile, 
			String beginDateStr, String endDateStr) throws Exception{
        URI uri = new URIBuilder()
                .setScheme("http")
                .setHost("117.135.144.27")
                .setPort(8083)
                .setPath("/mdao-emap8/webservice/http/service")
                .setParameter("cmd", "getSmReport")
                .setParameter("username", "zdcf")
                .setParameter("password", "zdcf_1210")
                .setParameter("token",token)
                .setParameter("mobile",mobile)
                .setParameter("beginDate",beginDateStr)
                .setParameter("endDate", endDateStr)
                .build();

        HttpGet get = new HttpGet(uri);
        HttpResponse response  = httpClient.execute(get);
        String result = EntityUtils.toString(response.getEntity());
//        System.out.println(result);
        return result;
    }

    private String getReportJSONByPost(HttpClient httpClient, String token, String mobile, 
    		String beginDateStr, String endDateStr) throws Exception{
        HttpPost post = new HttpPost(sysParamDefineService.getSysParamValueCache("sms", "sms.serverUrl"));

        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("cmd", "getSmReport"));
        params.add(new BasicNameValuePair("username", sysParamDefineService.getSysParamValueCache("sms","sms.uid")));
        params.add(new BasicNameValuePair("password", sysParamDefineService.getSysParamValueCache("sms","sms.pwd")));
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("mobile", mobile));
        params.add(new BasicNameValuePair("beginDate", beginDateStr));
        params.add(new BasicNameValuePair("endDate", endDateStr));

        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();//设置请求和传输超时时间
        post.setConfig(requestConfig);
        post.setEntity(entity);
        HttpResponse response  = httpClient.execute(post);

        String result = EntityUtils.toString(response.getEntity());
//        System.out.println(result);
        return result;
    }

    //如果支持定时？那么可以半夜发送？？
    private static String buildSendJson(String sendId, String mobsStr, String content,String beginDateStr, String dailyBeginTimeStr) {
        Map<String, String> m = new HashMap<String, String>();
        m.put("token", sendId);
        m.put("mobiles", mobsStr);
        m.put("content", content);
//        m.put("dailyBeginTime", dailyBeginTimeStr);
//        m.put("beginDate", beginDateStr);
//        Gson gson = new Gson();
        
        String json = "";
		try {
			json = JackJsonUtil.objToStr(m);
		} catch (JsonGenerationException e) {
			logger.error(e.getMessage());
		} catch (JsonMappingException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}

        return json;
    }

    private static Report parseReportJson(String reportJson){

        Report r = null;
		try {
			r = (Report) JackJsonUtil.strToObj(reportJson, Report.class);
		} catch (JsonParseException e) {
			logger.error(e.getMessage());
		} catch (JsonMappingException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
        return r;
    }

    private static SendResult parseSendResultJson(String resultJson) {
//        String json ="{\"message\":\"未提供短信内容：content=\",\"success\":false}";
//        Gson gson = new Gson();
        SendResult r = null;;
		try {
			r = (SendResult) JackJsonUtil.strToObj(resultJson, SendResult.class);
		} catch (JsonParseException e) {
			logger.error(e.getMessage());
		} catch (JsonMappingException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}

        return r;
    }

    static  class SendResult{
        Boolean success;
        String message;

        public Boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    static class Report{
        String message;
        Boolean success;
        List<SMReport> smReports ;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Boolean getSuccess() {
            return success;
        }

        public void setSuccess(Boolean success) {
            this.success = success;
        }

        public List<SMReport> getSmReports() {
            return smReports;
        }

        public void setSmReports(List<SMReport> smReports) {
            this.smReports = smReports;
        }
    }

    static class SMReport {
        String result;
        String mobile;
        String sendId;
        String errorCode;
        String sendTime;
        String receiveTime;

        public SMReport(){}

        public SMReport(String sendId, String mobile, String result){
            this.sendId = sendId;
            this.mobile = mobile;
            this.result = result;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getSendId() {
            return sendId;
        }

        public void setSendId(String sendId) {
            this.sendId = sendId;
        }

        public String getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(String errorCode) {
            this.errorCode = errorCode;
        }

        public String getSendTime() {
            return sendTime;
        }

        public void setSendTime(String sendTime) {
            this.sendTime = sendTime;
        }

        public String getReceiveTime() {
            return receiveTime;
        }

        public void setReceiveTime(String receiveTime) {
            this.receiveTime = receiveTime;
        }

        @Override
        public String toString() {
            return "SMReport{" +
                    "result='" + result + '\'' +
                    ", mobile='" + mobile + '\'' +
                    ", sendId='" + sendId + '\'' +
                    ", errorCode='" + errorCode + '\'' +
                    ", sendTime=" + sendTime +
                    ", receiveTime=" + receiveTime +
                    '}';
        }
    }

	
}
