
package com.zdmoney.credit.job;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zdmoney.credit.bsyh.dao.pub.IEarlyRepayCalculateDao;
import com.zdmoney.credit.bsyh.dao.pub.IRepayBusLogDao;
import com.zdmoney.credit.bsyh.domain.EarlyRepayCalculate;
import com.zdmoney.credit.bsyh.domain.RepayBusLog;
import com.zdmoney.credit.bsyh.service.pub.IRepayResultNotifyLogService;
import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.common.constant.DealStateEnum;
import com.zdmoney.credit.common.constant.FundsSourcesTypeEnum;
import com.zdmoney.credit.common.constant.GatewayFuncIdEnum;
import com.zdmoney.credit.common.constant.LoanStateEnum;
import com.zdmoney.credit.common.constant.PayOffTypeEnum;
import com.zdmoney.credit.common.constant.SpecialRepaymentTypeEnum;
import com.zdmoney.credit.common.constant.SplitNotifyStateEnum;
import com.zdmoney.credit.common.constant.SplitResultStateEnum;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.gateway.GatewayUtils;
import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.HttpUtils;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.util.coreUtil.JSONUtil;
import com.zdmoney.credit.common.vo.RequestInfo;
import com.zdmoney.credit.debit.dao.pub.IDebitQueueLogDao;
import com.zdmoney.credit.debit.domain.DebitQueueLog;
import com.zdmoney.credit.debit.domain.DebitTransaction;
import com.zdmoney.credit.debit.domain.SplitQueueLog;
import com.zdmoney.credit.debit.service.pub.IDebitTransactionService;
import com.zdmoney.credit.debit.service.pub.ISplitQueueLogService;
import com.zdmoney.credit.framework.vo.bsb.input.Bsb100008Vo;
import com.zdmoney.credit.framework.vo.bsb.input.Bsb100009Vo;
import com.zdmoney.credit.framework.vo.bsb.input.Bsb100009Vo.Item;
import com.zdmoney.credit.framework.vo.bsb.input.Bsb100017Vo;
import com.zdmoney.credit.loan.dao.pub.ILoanBsbMappingDao;
import com.zdmoney.credit.loan.domain.RepayResultNotifyLog;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.IAfterLoanService;
import com.zdmoney.credit.loan.service.pub.ILoanBsbMappingService;
import com.zdmoney.credit.loan.service.pub.ILoanLedgerService;
import com.zdmoney.credit.loan.service.pub.ILoanLogService;
import com.zdmoney.credit.loan.service.pub.ILoanRepaymentDetailService;
import com.zdmoney.credit.loan.service.pub.ILoanSpecialRepaymentService;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import com.zdmoney.credit.offer.domain.OfferRepayInfo;
import com.zdmoney.credit.offer.service.pub.IOfferRepayInfoService;
import com.zdmoney.credit.payment.service.pub.IFinanceGrantService;
import com.zdmoney.credit.system.domain.PersonInfo;
import com.zdmoney.credit.system.service.pub.IPersonInfoService;
import com.zdmoney.credit.system.service.pub.ISequencesService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;
/**
 * @author YM10112  2016年12月28日
 *
 */
@Service
public class RepayResultDisposeBsyhJob {
    
    private static final Logger logger = Logger.getLogger(RepayResultDisposeBsyhJob.class);
    @Autowired
    private IFinanceGrantService financeGrantService;
    @Autowired
    private ILoanLogService loanLogService;
    @Autowired
    private ISysParamDefineService sysParamDefineService;
    @Autowired
    private IVLoanInfoService vLoanInfoService;
    @Autowired
    private IPersonInfoService personInfoService;
    @Autowired
    private ILoanBsbMappingDao loanBsbMappingDao;
    @Autowired
    private IRepayBusLogDao repayBusLogDao;
    @Autowired
    private ISequencesService sequencesService;
    @Autowired
    private IRepayResultNotifyLogService repayResultNotifyLogService;
    @Autowired
    private IEarlyRepayCalculateDao earlyRepayCalculateDao;
    @Autowired
    private ILoanBsbMappingService loanBsbMappingService;
    @Autowired
    private ILoanSpecialRepaymentService loanSpecialRepaymentService;
    @Autowired
    private ILoanLedgerService loanLedgerServiceImpl;
    @Autowired
    private IAfterLoanService  afterLoanService;
    @Autowired
    private IOfferRepayInfoService offerRepayInfoServiceImpl;
    @Autowired 
    private ILoanRepaymentDetailService loanRepaymentDetailServiceImpl;
    @Autowired
    private IDebitTransactionService debitTransactionService;
    @Autowired
    private IDebitQueueLogDao debitQueueLogDao;
    @Autowired
    private ISplitQueueLogService splitQueueLogService;
    @Value("${gateway.interface.url}")
    public String gatewayInterfaceUrl;
    @Value("${bstogateway.interface.url}")
    public String bstogatewayInterfaceUrl;
    
    private static final String ADVANCE_REPAY = "1";
    
    private static final String AUTO_REPAY = "3";
    
    /**
     * 外部信托机构还款通知消息记录入账、分账处理
     * 
     */
    public void repayResultDisposeBsyhExecute() {
        logger.info("当前日期为：" + DateTime.now().toString("yyyy-MM-dd HH:mm:ss")+ "，开始执行【信托机构】还款结果处置JOB！！！");
        int month = DateTime.now().getDayOfMonth();
        int hour = DateTime.now().getHourOfDay();
        if ((month == 1 || month == 16) && (hour == 20 || hour == 21)) {
            logger.warn("T日20:00:00~21:59:59需要日终跑批入账处理，不进行消费！！！");
            return;
        }
        // 查询还款结果通知记录信息
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("deductState", "t");// 划扣状态 为t成功f失败
        params.put("state", "0");// 消费状态 0未消费 1已消费
        List<RepayResultNotifyLog> repayResultLogList = repayResultNotifyLogService.findListByMap(params);
        if (CollectionUtils.isEmpty(repayResultLogList)) {
            logger.warn("【信托机构】提前还款结果通知，没有未消费的记录！");
            return;
        }
        for (RepayResultNotifyLog repayResultLog : repayResultLogList) {
            // 根据债权编号查询债权信息
            VLoanInfo loanInfo = vLoanInfoService.findByLoanId(repayResultLog.getLoanId());
            if (FundsSourcesTypeEnum.包商银行.getValue().equals(loanInfo.getLoanBelong())) {
                // 包商银行入账、分账处理
                this.executeBsyhEnterAccount(repayResultLog);
            } else if (FundsSourcesTypeEnum.外贸3.getValue().equals(loanInfo.getLoanBelong())) {
                // 外贸3入账、分账处理
                this.executeWm3EnterAccount(repayResultLog);
            } else if (FundsSourcesTypeEnum.陆金所.getValue().equals(loanInfo.getLoanBelong())) {
                // 陆金所入账、分账处理
                this.executeLufaxEnterAccount(repayResultLog);
            }
        }
        logger.info("【信托机构】消费还款通知记录结束！！！");
    }

    /**
     *  包商银行入账、分账处理
     * @param repayResultLog
     */
    private void executeBsyhEnterAccount(RepayResultNotifyLog repayResultLog) {
        boolean flag = false;
        if (ADVANCE_REPAY.equals(repayResultLog.getNotifyType()) || AUTO_REPAY.equals(repayResultLog.getNotifyType())) {
        	VLoanInfo loan = vLoanInfoService.findByLoanId(repayResultLog.getLoanId());
            if(LoanStateEnum.预结清.getValue().equalsIgnoreCase(loan.getLoanState())
            		|| LoanStateEnum.结清.getValue().equalsIgnoreCase(loan.getLoanState())){
                repayResultLog.setDealState(DealStateEnum.REFUSE.getCode());
                repayResultLog.setUpdateTime(new Date());
                repayResultLog.setState("1");//已消费 1
                repayResultNotifyLogService.update(repayResultLog);
                logger.info("【包商银行】债权loanId:"+repayResultLog.getLoanId()+",已结清-->无需入账！");
                return;
            }
        }
        //处理提前还款
        if(ADVANCE_REPAY.equals(repayResultLog.getNotifyType())){
            BigDecimal perReapyAmount = afterLoanService.getPerReapyAmount(new Date(), repayResultLog.getLoanId());
            logger.info("债权LoanId:"+repayResultLog.getLoanId()+",当期应还金额"+perReapyAmount+",正常还款入账..");
                flag = financeGrantService.createOfferRepayInfo(repayResultLog.getLoanId(),perReapyAmount,Const.TRADE_CODE_NORMAL);
                if(flag){
                    repayResultLog.setDealState(DealStateEnum.SUCCESS.getCode());
                    repayResultLog.setState("1");//已消费 1
                    repayResultLog.setUpdateTime(new Date());
                    repayResultNotifyLogService.update(repayResultLog);
                    logger.info("【包商银行】债权loanId:"+repayResultLog.getLoanId()+",提前还款入账成功,已被消费处理！");
                }
        }
        //处理T日自扣
        else if(AUTO_REPAY.equals(repayResultLog.getNotifyType())){
            //①申请过一次性结清
            if(afterLoanService.isOneTimeRepayment(repayResultLog.getLoanId())){
                //客户结清剩余应还金额
                BigDecimal totalAmt = afterLoanService.getAmount(Dates.getCurrDate(), repayResultLog.getLoanId());
                //T日自扣金额大于等于客户结清应还金额
                if(repayResultLog.getTotalamt().compareTo(totalAmt) >= 0){
                    flag = financeGrantService.createOfferRepayInfo(repayResultLog.getLoanId(),repayResultLog.getTotalamt(),Const.TRADE_CODE_ONEOFF);
                    if(flag){
                        logger.info("【包商银行】债权"+repayResultLog.getLoanId()+"，T日自扣金额大于等于客户结清应还金额，一次性还款入账成功");
                    }
                    /**
                    	advanceClearTrialInterface(repayResultLog.getLoanId());//提前结清试算接口 BYXY0017
                    	repaymentInterface(repayResultLog.getLoanId(),repayResultLog.getPeriod());//还款接口 BYXY0009
                    **/
                }else{
                    flag = financeGrantService.createOfferRepayInfo(repayResultLog.getLoanId(),repayResultLog.getTotalamt(),Const.TRADE_CODE_NORMAL);
                    if(flag){
                        logger.info("【包商银行】债权"+repayResultLog.getLoanId()+"，T日自扣金额小于客户结清应还金额，正常还款入账成功");
                    }
                }
            } 
            //②未申请过一次性结清
            else{
                flag = financeGrantService.createOfferRepayInfo(repayResultLog.getLoanId(),repayResultLog.getTotalamt(),Const.TRADE_CODE_NORMAL);
                if(flag){
                    logger.info("【包商银行】债权"+repayResultLog.getLoanId()+",没有申请过一次性还款,正常还款入账...");
                }
            }
            if(flag){
                repayResultLog.setDealState(DealStateEnum.SUCCESS.getCode());
                repayResultLog.setState("1");//已消费 1
                repayResultLog.setUpdateTime(new Date());
                repayResultNotifyLogService.update(repayResultLog);
                logger.info("【包商银行】债权loanId:"+repayResultLog.getLoanId()+",T日自扣还款入账成功,已被消费处理！");
            }else{
                repayResultLog.setDealState(DealStateEnum.FAILURE.getCode());
                repayResultLog.setState("1");//已消费 1
                repayResultLog.setUpdateTime(new Date());
                repayResultNotifyLogService.update(repayResultLog);
                logger.info("【包商银行】债权loanId:"+repayResultLog.getLoanId()+",T日自扣还款入账失败,已被消费处理！");
            }
        }
    }
    
    /**
     * 外贸3入账、分账处理
     * @param repayResultLog
     */
    private void executeWm3EnterAccount(RepayResultNotifyLog repayResultLog) {
        // 第三方报盘交易流水号
        String serialNo = repayResultLog.getRepayBusNumber();
        if(Strings.isEmpty(serialNo)){
            logger.error("外贸3还款入账处理失败，交易流水号不能为空！还款通知消息队列主键Id：" + repayResultLog.getId());
            return;
        }
        // 根据交易流水号查询报盘流水信息
        DebitTransaction transaction = debitTransactionService.findDebitTransactionBySerialNo(serialNo);
        if(null == transaction){
            logger.error("外贸3还款入账处理失败，报盘流水记录不存在！交易流水号："+ serialNo);
            return;
        }
        // 第三方还款减免入账处理
        afterLoanService.accountingByTransaction(transaction);
        repayResultLog.setDealState(DealStateEnum.SUCCESS.getCode());
        // 已消费 1
        repayResultLog.setState("1");
        repayResultLog.setUpdateTime(new Date());
        repayResultNotifyLogService.update(repayResultLog);
        logger.info("【外贸3】债权loanId:"+ repayResultLog.getLoanId()+"，已被消费处理，入账成功！");
    }
    
    /**
     * 陆金所入账、分账处理
     * @param repayResultLog
     */
    private void executeLufaxEnterAccount(RepayResultNotifyLog repayResultLog) {
        logger.info("【陆金所】债权loanId:" + repayResultLog.getLoanId() + "，消费队列入账处理开始。。。。。。");
        Long loanId = repayResultLog.getLoanId();
        BigDecimal amount = repayResultLog.getTotalamt();
        String tradeCode = Const.TRADE_CODE_NORMAL;
        // 陆金所入账、分账处理
        OfferRepayInfo offerRepayInfo = financeGrantService.createOfferRepayInfoThird(loanId, amount, tradeCode);
        String tradeNo = offerRepayInfo.getTradeNo();
        String lufaxNo = repayResultLog.getOrderNo();
        String debitNo = repayResultLog.getRepayBusNumber();
        DebitQueueLog searchVo = new DebitQueueLog();
        searchVo.setDebitNo(debitNo);
        List<DebitQueueLog> debitQueueLogList = debitQueueLogDao.findListByVo(searchVo);// 根据debit_no查询一次发送的债权
        if (CollectionUtils.isEmpty(debitQueueLogList)) {
            logger.warn("查询不到对应的划扣队列信息，debitNo：" + debitNo);
            return;
        }
        for (DebitQueueLog log : debitQueueLogList) {
            log.setTradeNo(tradeNo);
            debitQueueLogDao.update(log);
        }

        SplitQueueLog splitQueueLog = new SplitQueueLog();
        splitQueueLog.setLoanId(loanId);
        splitQueueLog.setDebitNo(debitNo);// 划扣表中的 debit_queue_log.debit_no(同一天的loanId,重复)
        splitQueueLog.setSplitNotifyState(SplitNotifyStateEnum.待通知.getCode());
        splitQueueLog.setSplitResultState(SplitResultStateEnum.未分账.getCode());
        splitQueueLog.setPayOffType(PayOffTypeEnum.未结清.getCode());// 未结算
        splitQueueLog.setSplitNo(lufaxNo);// lufax还款请求号
        splitQueueLog.setBatchId(debitQueueLogList.get(0).getBatchId());// 转发代扣时的安硕批次号
        splitQueueLog.setFrozenAmount(amount);
        splitQueueLogService.createSplitQueueLog(splitQueueLog);

        repayResultLog.setDealState(DealStateEnum.SUCCESS.getCode());
        // 已消费 1
        repayResultLog.setState("1");
        repayResultNotifyLogService.update(repayResultLog);
        logger.info("【陆金所】债权loanId:" + repayResultLog.getLoanId() + "，已被消费处理，入账成功！");
    }
    
    //自动查询包商银行还款结果  每天23:00
    public void repayResultSearchBsyhExecute(){
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("applyState","t");//申请成功 t
        paramMap.put("deductState", "Y");//划扣状态为空
        List<RepayBusLog> repayBusLogList = repayBusLogDao.findListByMap(paramMap);//申请成功  划扣状态为空 的数据
        if(CollectionUtils.isNotEmpty(repayBusLogList)){
            logger.info("【包商银行】当前日期为："+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"，开始执行还款结果查询JOB");
            for(RepayBusLog repayBusLog : repayBusLogList){
                String repayBusNumber = repayBusLog.getRepayBusNumber();
                searchRepayResultInterface(repayBusNumber);
            }
        }else{
            logger.info("【包商银行】还款结果查询,暂无数据！");
        }
    }
    
    /**
     * 掉用包银还款接口  BYXY0009  T日掉用
     * @param loan
     */
    public void repaymentInterface(Long loanId,Integer period) {
        logger.info("【包商银行】掉用还款接口BYXY0009,发送结清代偿,债权id:"+loanId);
        VLoanInfo loan = vLoanInfoService.findByLoanId(loanId);
        PersonInfo personInfo = personInfoService.findById(loan.getBorrowerId());
        Bsb100009Vo vo = new Bsb100009Vo();
        vo.setIdNo(personInfo.getIdnum());
        vo.setIdType("01");
        vo.setCustName(personInfo.getName());
        vo.setMobNo(personInfo.getMphone());
        vo.setTrantype("04");//01:违约，02:当期提前还款，03:部分提前还款，04:提前结清, 
        vo.setDebitType("D01");// D01:客户银行卡，证大必传
        vo.setCallbackUrl(bstogatewayInterfaceUrl);
        List<Item> itemList = new ArrayList<Item>();
        Item item = new Item();
        String orderNo = loanBsbMappingDao.getByLoanId(loanId).getOrderNo();
        item.setOrderNo(orderNo);
        itemList.add(item);
        vo.setPayList(itemList);
        RequestInfo requestInfoVo = null;
        String rsultStr = "";
        String url = gatewayInterfaceUrl;
        if (Strings.isEmpty(url)) {
            throw new PlatformException(ResponseEnum.FULL_MSG,"【包商银行】接口路径不存在");
        }
        try{
            requestInfoVo = GatewayUtils.getSendGatewayRequestVo(vo, GatewayFuncIdEnum.包商银行还款);
            logger.info("【包商银行】还款接口url:"+url+",参数："+JSONUtil.toJSON(requestInfoVo));
            rsultStr = HttpUtils.doPost(url, JSONUtil.toJSON(requestInfoVo));
            rsultStr = URLDecoder.decode(rsultStr, "UTF-8");
            logger.info("【包商银行】还款接口url:"+url+",响应："+JSONUtil.toJSON(rsultStr));
        }catch (Exception e){
            throw new PlatformException(ResponseEnum.FULL_MSG,"掉用【包商银行】还款接口异常");
        }
        JSONObject jsonObject = JSON.parseObject(rsultStr);
        logger.info("【包商银行】还款接口返回的json数据："+jsonObject);
        if ("0000".equals(jsonObject.get("resCode").toString())) {
            JSONObject grantResut = jsonObject.getJSONObject("infos").getJSONObject("data");
            if("0500".endsWith(grantResut.getString("respcd"))){//此时的结果是 申请处理中
                logger.info("【包商银行】调用还款接口成功，返回码："+grantResut.getString("respcd")+",返回信息："+grantResut.getString("resptx"));
            }else{
                logger.info("【包商银行】调用还款接口失败，原因："+grantResut.getString("resptx"));
            }
            saveRepayBusLog(jsonObject,loanId,period);
        }else{
            logger.info("【包商银行】还款接口,调用网关返回的失败信息："+jsonObject.get("respDesc").toString());
        }
    }
    
    /**
     * 掉用还款结果查询接口  BYXY0008
     * @param repayBusNumber
     */
    @SuppressWarnings("rawtypes")
    public void searchRepayResultInterface(String repayBusNumber) {
        logger.info("【包商银行】掉用还款结果查询接口BYXY0008...");
        Map<String, Object> json = new HashMap<String, Object>();
        Bsb100008Vo vo = new Bsb100008Vo();
        vo.setRepayBusNumber(repayBusNumber);
        RequestInfo requestInfoVo = null;
        String rsultStr = "";
        String url = gatewayInterfaceUrl;
        try{
            requestInfoVo = GatewayUtils.getSendGatewayRequestVo(vo, GatewayFuncIdEnum.包商银行还款结果查询);
            logger.info("【包商银行】还款结果查询接口url:"+url+",参数："+JSONUtil.toJSON(requestInfoVo));
            rsultStr = HttpUtils.doPost(url, JSONUtil.toJSON(requestInfoVo));
            //rsultStr = "{\"infos\":{\"comm\":{\"encrmd\":\"01\",\"invktm\":\"20170526 23:00:01\",\"signtp\":\"001\",\"signtx\":\"75f8808e52985a2865af42220d772f09\",\"chanltp\":\"C-00\",\"chanlno\":\"0101020011\",\"txncd\":\"BYXY0008\",\"ordrno\":\"01010200112017052623000010374936\",\"encodg\":\"UTF-8\"},\"data\":{\"respcd\":\"3700\",\"resptx\":\"[3701]交易记录不存在\"}},\"resCode\":\"0000\",\"respDesc\":\"操作成功\"}";
            rsultStr = URLDecoder.decode(rsultStr, "UTF-8");
            logger.info("【包商银行】还款结果查询接口url:"+url+",响应："+JSONUtil.toJSON(rsultStr));
        }catch (Exception e){
            throw new PlatformException(ResponseEnum.FULL_MSG,"【包商银行】还款结果查询接口异常");
        }
        JSONObject jsonObject = JSON.parseObject(rsultStr);
        logger.info("【包商银行】还款结果查询接口返回的json数据："+jsonObject);
        if ("0000".equals(jsonObject.get("resCode").toString())) {
            JSONObject grantResut = jsonObject.getJSONObject("infos").getJSONObject("data");
            if("3700".endsWith(grantResut.getString("respcd"))){//交易记录不存在
                logger.info("调用【包商银行】还款结果查询接口成功,返回码："+grantResut.getString("respcd")+",返回信息："+grantResut.getString("resptx"));
                return;
            }
            if("0000".endsWith(grantResut.getString("respcd"))){//交易成功
                logger.info("调用【包商银行】还款结果查询接口成功，返回码："+grantResut.getString("respcd")+",返回信息："+grantResut.getString("resptx"));
            }else{
                logger.info("调用【包商银行】还款结果查询接口失败：原因："+grantResut.getString("resptx"));
            }
            updateRepayBusLog(jsonObject,repayBusNumber);//更新划扣状态
            try {
                debitTransactionService.callbackNotify(repayBusNumber, grantResut.getString("respcd"), 
                        grantResut.getString("resptx"), "1");
            } catch (Exception e) {
                logger.error(e.getMessage(),e);
            }
        }else{
            logger.info("【包商银行】还款结果查询接口,调用网关返回的失败信息："+jsonObject.get("respDesc").toString());
        }
    }
    
    /**
     * 提前结清试算接口  BYXY0017
     * @param loanId
     */
    public void advanceClearTrialInterface(Long loanId){
        logger.info("【包商银行】提前结清试算接口被调用，债权id:"+loanId);
        Map<String, Object> json = new HashMap<String, Object>();
        Bsb100017Vo vo = new Bsb100017Vo();
        vo.setOrderNo(loanBsbMappingService.getByLoanId(loanId).getOrderNo());//借据号
        //vo.setOrderNo("C021702160002590001");
        vo.setProdSubNo("400001");//产品小类编号
        RequestInfo requestInfoVo = null;
        String rsultStr = "";
        String url = gatewayInterfaceUrl;
        try{
            requestInfoVo = GatewayUtils.getSendGatewayRequestVo(vo, GatewayFuncIdEnum.包商银行提前结清试算接口);
            logger.info("【包商银行】提前结清试算接口url:"+url+",参数："+JSONUtil.toJSON(requestInfoVo));
            rsultStr = HttpUtils.doPost(url, JSONUtil.toJSON(requestInfoVo));
            rsultStr = URLDecoder.decode(rsultStr, "UTF-8");
            logger.info("【包商银行】提前结清试算接口url:"+url+",响应："+JSONUtil.toJSON(rsultStr));
        }catch (Exception e){
            throw new PlatformException(ResponseEnum.FULL_MSG,"【包商银行】提前结清试算接口异常");
        }
        JSONObject jsonObject = JSON.parseObject(rsultStr);
        logger.info("【包商银行】提前结清试算接口返回的json数据："+jsonObject);
        if ("0000".equals(jsonObject.get("resCode").toString())) {
            JSONObject grantResut = jsonObject.getJSONObject("infos").getJSONObject("data");
            if("0000".endsWith(grantResut.getString("respcd"))){//交易成功 0000
                logger.info("调用【包商银行】提前结清试算接口成功，返回码："+grantResut.getString("respcd")+",返回信息："+grantResut.getString("resptx"));
                saveEarlyRepayCalculate(grantResut);//只有试算返回成功码，才保存数据试算返回的数据
            }else{
                logger.info("调用【包商银行】提前结清试算接口接口失败：原因："+grantResut.getString("resptx"));
            }
        }else{
            logger.info("【包商银行】提前结清试算接口,调用网关返回的失败信息："+jsonObject.get("respDesc").toString());
        }
    }
    
    
    //T日掉用  apply_type 2提前结清代偿
    private void saveRepayBusLog(JSONObject jsonObject,Long loanId,Integer period) {
        JSONObject grantResut = jsonObject.getJSONObject("infos").getJSONObject("data");
        String respcd = grantResut.getString("respcd");
        String resptx = grantResut.getString("resptx");
        String repayBusNumber = grantResut.getString("repayBusNumber");
        RepayBusLog repayLog = new RepayBusLog();
        if ("0500".endsWith(grantResut.getString("respcd"))) {// 此时的结果是 申请处理中
            repayLog.setApplyState("t");// 包银申请状态t成功，f失败
        } else {
            repayLog.setApplyState("f");// 包银申请状态t成功，f失败
        }
        repayLog.setId(sequencesService.getSequences(SequencesEnum.REPAY_BUS_LOG));
        repayLog.setRespCd(respcd);
        repayLog.setReternMsg(resptx);
        repayLog.setCurrentTerm(String.valueOf(period));// 申请提前还款期数
        repayLog.setLoanId(loanId);
        repayLog.setRepayBusNumber(repayBusNumber);
        repayLog.setApplyType((long)2);// 1提前扣款申请  2提前结清代偿
        repayLog.setCreateTime(new Date());
        repayBusLogDao.insert(repayLog);
    }
    /**
     * 更新repay_bug_log包银还款申请日志表，
     * 保存repay_result_notify_log还款结果通知表
     * @param jsonObject
     * @param repayBusNumber
     */
    public void updateRepayBusLog(JSONObject jsonObject,String repayBusNumber){
        JSONObject grantResut = jsonObject.getJSONObject("infos").getJSONObject("data");
        String respcd = grantResut.getString("respcd");
        String resptx = grantResut.getString("resptx");
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("repayBusNumber",repayBusNumber);
        List<RepayBusLog> repayBusLogList = repayBusLogDao.findListByMap(paramMap);
        RepayBusLog repayLog = repayBusLogList.get(0);
        if ("0000".endsWith(grantResut.getString("respcd"))) {
            repayLog.setDeductState("t");// 包银划扣状态t成功，f失败
        } else {
            repayLog.setDeductState("f");// 包银划扣状态t成功，f失败
            if(repayLog.getApplyType() == 1){//结束 提前扣款申请
                logger.info("【包商银行】自动查询还款结果接口划扣失败，债权id为："+repayLog.getLoanId()+",更新特殊还款表状态为结束！");
                loanSpecialRepaymentService.updateSpecialRepaymentToEnd(repayLog.getLoanId(), SpecialRepaymentTypeEnum.提前扣款.getValue(), "申请");
            }
        }
        repayLog.setId(repayLog.getId());
        repayLog.setRespCd(respcd);
        repayLog.setReternMsg(resptx);
        repayLog.setUpdateTime(new Date());
        repayBusLogDao.update(repayLog);
        
        RepayResultNotifyLog repayResultNotify = new RepayResultNotifyLog();
        repayResultNotify.setId(sequencesService.getSequences(SequencesEnum.REPAY_RESULT_NOTIFY_LOG));
        repayResultNotify.setLoanId(repayLog.getLoanId());
        repayResultNotify.setRepayBusNumber(repayBusNumber);
        repayResultNotify.setRespcd(respcd);
        repayResultNotify.setResptx(resptx);
        repayResultNotify.setState("0");//消费状态 0未消费 1已消费
        repayResultNotify.setDeductState(repayLog.getDeductState());//划扣状态
        repayResultNotify.setNotifyType(repayLog.getApplyType().toString());//1提前扣款2结清代偿3自扣还款
        repayResultNotifyLogService.save(repayResultNotify);
    }
    /**
     * 保存 early_repay_calculate 包银提前结清试算表
     * @param grantResut
     */
    @SuppressWarnings("unused")
    private void saveEarlyRepayCalculate(JSONObject grantResut) {
        logger.info("【包商银行】提前结清试算成功,保存业务数据！");
        EarlyRepayCalculate earlyRepayCalculate = new EarlyRepayCalculate();
        String respcd = grantResut.getString("respcd");
        String resptx = grantResut.getString("resptx");
        String orderNo = grantResut.getString("orderNo");//还款申请业务流水号
        BigDecimal installTotalAmt = grantResut.getBigDecimal("installTotalAmt");
        BigDecimal repayBaseAmt = grantResut.getBigDecimal("repayBaseAmt");
        BigDecimal repyam = grantResut.getBigDecimal("repyam");
        BigDecimal installTotalInterest = grantResut.getBigDecimal("installTotalInterest");
        BigDecimal disCountInterest = grantResut.getBigDecimal("disCountInterest");
        Long repayPeriod = grantResut.getLong("repayPeriod");
        Long remainRepayTimes = grantResut.getLong("remainRepayTimes");
        BigDecimal liqdaRatio = grantResut.getBigDecimal("liqdaRatio");
        BigDecimal liqdaAmount = grantResut.getBigDecimal("liqdaAmount");
        BigDecimal earlyRepaymentAmt = grantResut.getBigDecimal("earlyRepaymentAmt");
        
        earlyRepayCalculate.setId(sequencesService.getSequences(SequencesEnum.EARLY_REPAY_CALCULATE));
        earlyRepayCalculate.setRespcd(respcd);
        earlyRepayCalculate.setResptx(resptx);
        earlyRepayCalculate.setOrderNo(orderNo);
        earlyRepayCalculate.setInstallTotalAmt(installTotalAmt);
        earlyRepayCalculate.setRepayBaseAmt(repayBaseAmt);
        earlyRepayCalculate.setRepyam(repyam);
        earlyRepayCalculate.setInstallTotalInterest(installTotalInterest);
        earlyRepayCalculate.setDisCountInterest(disCountInterest);
        earlyRepayCalculate.setRepayPeriod(repayPeriod);
        earlyRepayCalculate.setRemainRepayTimes(remainRepayTimes);
        earlyRepayCalculate.setLiqdaRatio(liqdaRatio);
        earlyRepayCalculate.setLiqdaAmount(liqdaAmount);
        earlyRepayCalculate.setEarlyRepaymentAmt(earlyRepaymentAmt);
        earlyRepayCalculateDao.insert(earlyRepayCalculate);
    }
}
