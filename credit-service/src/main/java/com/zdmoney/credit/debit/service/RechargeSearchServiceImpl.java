package com.zdmoney.credit.debit.service;


import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.zdmoney.credit.bsyh.dao.pub.IRepayResultNotifyLogDao;
import com.zdmoney.credit.common.constant.FundsSourcesTypeEnum;
import com.zdmoney.credit.common.constant.GatewayFuncIdEnum;
import com.zdmoney.credit.common.constant.OfferStateEnum;
import com.zdmoney.credit.common.constant.OfferTransactionStateEnum;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.constant.tpp.ReturnCodeEnum;
import com.zdmoney.credit.common.constant.wm.WMDebitDealStsEnum;
import com.zdmoney.credit.common.constant.wm.WMOnlineFlagEnum;
import com.zdmoney.credit.common.constant.wm.WMUnderlineRepayTypeEnum;
import com.zdmoney.credit.common.gateway.GatewayUtils;
import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.debit.dao.pub.IDebitBaseInfoDao;
import com.zdmoney.credit.debit.dao.pub.IDebitOfflineOfferInfoDao;
import com.zdmoney.credit.debit.dao.pub.IDebitTransactionDao;
import com.zdmoney.credit.debit.domain.DebitBaseInfo;
import com.zdmoney.credit.debit.domain.DebitOfflineOfferInfo;
import com.zdmoney.credit.debit.domain.DebitTransaction;
import com.zdmoney.credit.debit.service.pub.IDebitOfflineOfferInfoService;
import com.zdmoney.credit.debit.service.pub.IRechargeSearchService;
import com.zdmoney.credit.framework.vo.common.BaseParamVo;
import com.zdmoney.credit.framework.vo.wm3.entity.ResponseListWm3Entiy;
import com.zdmoney.credit.framework.vo.wm3.input.WM3_2313Vo;
import com.zdmoney.credit.framework.vo.wm3.output.WM3_2313OutputVo;
import com.zdmoney.credit.loan.dao.pub.ILoanBankDao;
import com.zdmoney.credit.loan.dao.pub.ILoanBaseDao;
import com.zdmoney.credit.loan.domain.RepayResultNotifyLog;
import com.zdmoney.credit.loan.service.LoanBackServiceImpl;
import com.zdmoney.credit.offer.service.pub.IOfferTransactionService;
import com.zdmoney.credit.system.service.pub.ISequencesService;
@Service
public class RechargeSearchServiceImpl implements IRechargeSearchService {
    
    private static final Logger logger = Logger.getLogger(LoanBackServiceImpl.class);

    @Autowired
    private ILoanBaseDao loanBaseDao;
    
    @Autowired
    private ILoanBankDao loanBankDao;
    
    @Autowired
    private IRepayResultNotifyLogDao repayResultNotifyLogDao;
    
    @Autowired
    private IDebitBaseInfoDao debitBaseInfoDao;
    
    @Autowired
    private IDebitTransactionDao debitTransactionDao;  
    
    @Autowired
    private ISequencesService sequencesService;
    
    @Autowired
    private IDebitOfflineOfferInfoDao debitOfflineOfferInfoDao;
    
    @Autowired
    private IDebitOfflineOfferInfoService debitOfflineOfferInfoService;
    
    @Autowired
    private IOfferTransactionService offerTransactionServiceImpl;
    
    /**
     * 查询和处理外贸3线上还款和线下实收扣款结果
     */
    public void getSearchResult() {
        // 查询和处理外贸3线上还款扣款结果
        this.searchOnlineDebitResult();
        // 查询和处理外贸3线下实收扣款结果
        this.searchOfflineDebitResult();
    }

    /**
     * 查询和处理外贸3线上还款扣款结果
     */
    public void searchOnlineDebitResult() {
        // 查询已发送的所有批次号
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("loanBelong", FundsSourcesTypeEnum.外贸3.getValue());
        List<DebitTransaction> debitTransactionList = debitTransactionDao.queryDebitBatchNos(params);
        if (CollectionUtils.isEmpty(debitTransactionList)) {
            logger.warn("当前没有外贸3已发送的报盘划扣信息！");
            return;
        }
        WM3_2313Vo vo = new WM3_2313Vo();
        vo.setBrNo(BaseParamVo.SYS_SOURCE_WM3);
        vo.setOnLine(WMOnlineFlagEnum.线上扣款.getCode());
        for (DebitTransaction debitTransaction : debitTransactionList) {
            // 批次号
            String batNo = debitTransaction.getBatNo();
            if (Strings.isEmpty(batNo)) {
                logger.warn("批次号不能为空！");
                continue;
            }
            vo.setBatNo(batNo);
            // 调用外贸信托扣款查询接口，返回查询结果对象
            WM3_2313OutputVo wM3_2313OutputVo = this.searchDebitResult(vo);
            // 处理线上扣款查询结果
            this.handleOnlineDebitResult(wM3_2313OutputVo);
        }
    }
    
    /**
     * 查询和处理外贸3线下实收扣款结果
     */
    public void searchOfflineDebitResult() {
        Map<String, Object> map = new HashMap<String, Object>();
        // （01-线下正常扣款、02-线下提前清贷、03-线下溢缴款充值）'
        map.put("repyType", WMUnderlineRepayTypeEnum.线下溢缴款充值.getCode());
        map.put("states",  new String[]{OfferTransactionStateEnum.未发送.getValue(), OfferTransactionStateEnum.扣款失败.getValue()});
        List<DebitOfflineOfferInfo> debitOfflineOfferInfoList = debitOfflineOfferInfoService.findListByMap(map);
        if (CollectionUtils.isEmpty(debitOfflineOfferInfoList)) {
            logger.info("【外贸3】没有线下实收扣款待查询记录！");
            return;
        }
        logger.info("debit_offline_offer_info状态为已发送且的数量：" + debitOfflineOfferInfoList.size());
        searchOfflineDebitResult(debitOfflineOfferInfoList);
    }
    
    /**
     * 记录还款入账消费队列
     * @param rentity
     */
    private void insertRepayNotifyLog(ResponseListWm3Entiy rentity) {
         RepayResultNotifyLog repayResultNotifyLog = new RepayResultNotifyLog() ;
         repayResultNotifyLog.setId(sequencesService.getSequences(SequencesEnum.REPAY_RESULT_NOTIFY_LOG));
         // 根据合同号查询 loanId
         Long loanId = loanBaseDao.findLoanIdByContractNum(rentity.getPactNo());
         repayResultNotifyLog.setLoanId(loanId);
         //划扣状态t成功f失败
         repayResultNotifyLog.setDeductState("t");
         // 银行卡号
         repayResultNotifyLog.setBankCardNo(loanBankDao.findNumByLoanId(loanId));
         // 根据批次号和合同编号查询报盘流水记录
         DebitTransaction debitTransaction = this.queryDebitTransaction(rentity.getBatNo(), rentity.getPactNo());
         if(null == debitTransaction){
             logger.warn("查询不存在报盘流水记录，批次号："+rentity.getBatNo()+"，合同编号："+rentity.getPactNo());
             return;
         }
         // 设置划扣的金额
         repayResultNotifyLog.setTotalamt(debitTransaction.getPayAmount());
         
         repayResultNotifyLog.setState("0");//消费状态 0未消费 1已消费
         repayResultNotifyLog.setNotifyType("3");////1提前扣款2结清代偿3自扣还款
         repayResultNotifyLog.setPayResult("t");
         //货币类型
         repayResultNotifyLog.setCurrency("156");//中国的货币名称是人民币元,字母代码是CNY,数字代码是156
         //交易日期
         repayResultNotifyLog.setScheduleDate(Dates.getDateTime(new Date(), "yyyyMMdd"));
         //交易流水号保存到还款业务申请流水号
         repayResultNotifyLog.setRepayBusNumber(debitTransaction.getSerialNo());
         repayResultNotifyLogDao.insert(repayResultNotifyLog);
    }
    
    /**
     * 外贸3报盘、报盘流水状态信息更新
     * @param rentity
     */
    private void updateDebit(ResponseListWm3Entiy rentity) {
        // 根据批次号和合同编号查询报盘流水记录
        DebitTransaction debitTransaction = this.queryDebitTransaction(rentity.getBatNo(), rentity.getPactNo());
        if(null == debitTransaction){
            logger.warn("查询不存在报盘流水记录，批次号："+rentity.getBatNo()+"，合同编号："+rentity.getPactNo());
            return;
        }
        // 查询报盘流水对应的报盘记录
        DebitBaseInfo debitBaseInfo = debitBaseInfoDao.get(debitTransaction.getDebitId());
        if(null == debitBaseInfo){
            logger.warn("查询不存在报盘记录，debitId："+ debitTransaction.getDebitId());
            return;
        }
        debitBaseInfo.setMemo(rentity.getDealDesc());// 处理说明
        debitTransaction.setRtnInfo(rentity.getDealDesc());
        debitTransaction.setResTime(new Date());// 响应时间
        if (WMDebitDealStsEnum.处理成功.getCode().equals(rentity.getDealSts())) {
            debitBaseInfo.setState(OfferStateEnum.已回盘全额.getValue());
            BigDecimal actualAmount=debitTransaction.getPayAmount();
            debitBaseInfo.setActualAmount(actualAmount);//实际划扣金额
            debitTransaction.setState(OfferTransactionStateEnum.扣款成功.getValue());
            debitTransaction.setRtnCode(ReturnCodeEnum.交易成功.getCode());// 返回信息码 成功为000000 失败 为111111
            debitTransaction.setActualAmount(actualAmount);//实际划扣金额
        } else {
            debitBaseInfo.setState(OfferStateEnum.已回盘非全额.getValue());
            debitTransaction.setState(OfferTransactionStateEnum.扣款失败.getValue());
            debitTransaction.setRtnCode(ReturnCodeEnum.交易失败.getCode());
        }
        // 更新第三方划扣信息表
        debitBaseInfoDao.update(debitBaseInfo);
        debitTransactionDao.update(debitTransaction);
        
        //跨日回盘 添加到消息中心
        offerTransactionServiceImpl.addBaseMessage(debitBaseInfo.getOfferDate(), debitBaseInfo.getLoanId());
    }
    
    /**
     * 根据批次号和合同编号查询报盘流水记录
     * @param batNo
     * @param contractNum
     * @return
     */
    private DebitTransaction queryDebitTransaction(String batNo, String contractNum){
        // 根据批次号和合同编号查询报盘流水记录
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("batNo", batNo);
        params.put("contractNum", contractNum);
        return debitTransactionDao.queryDebitTransactionByConditions(params);
    }
    
    /**
     * 外贸3线下实收扣款结果查询和处理
     */
    private void searchOfflineDebitResult(List<DebitOfflineOfferInfo> debitOfflineOfferInfoList) {
        logger.info("【外贸3】线下实收扣款结果查询开始...");
        WM3_2313Vo vo = new WM3_2313Vo();
        // 合作机构号 005
        vo.setBrNo(BaseParamVo.SYS_SOURCE_WM3);
        // 线上标示 01-线上扣款  02-线下实收
        vo.setOnLine(WMOnlineFlagEnum.线下实收.getCode());
        for(DebitOfflineOfferInfo offline : debitOfflineOfferInfoList){
            // 批次号
            String batNo = offline.getBatNo();
            vo.setBatNo(batNo);
            // 调用外贸信托扣款查询接口，返回查询结果对象
            WM3_2313OutputVo wM3_2313OutputVo = this.searchDebitResult(vo);
            // 处理线下实收扣款查询结果
            this.handleOfflineDebitResult(wM3_2313OutputVo);
        }
    }
    
    /**
     * 调用外贸信托扣款查询接口，返回查询结果对象
     * @param vo
     * @return
     */
    private WM3_2313OutputVo searchDebitResult(WM3_2313Vo vo) {
        JSONObject jsonObject = null;
        WM3_2313OutputVo wM3_2313OutputVo = null;
        // 发送请求 调用接口
        try {
            jsonObject = GatewayUtils.callCateWayInterface(vo, GatewayFuncIdEnum.外贸3扣款查询.getCode());
        } catch (Exception e) {
            logger.error("外贸3扣款结果查询接口调用接口失败！异常信息：" + e.getMessage(), e);
            return wM3_2313OutputVo;
        }
        if (null == jsonObject) {
            logger.error("扣款结果查询接口返回结果为空！");
            return wM3_2313OutputVo;
        }
        // 接口返回码
        String respCode = jsonObject.getString("respCode");
        // 接口返回描述
        String respDesc = jsonObject.getString("respDesc");
        if (!"0000".equals(respCode)) {
            logger.warn("外贸3线上扣款结果查询失败，原因是：" + respDesc);
            return wM3_2313OutputVo;
        }
        // 接口返回响应结果数据
        String content = jsonObject.get("content").toString();
        logger.info("接口返回响应结果数据：" + content);
        JSONObject result = JSONObject.parseObject(content);
        // json字符串转换成java对象
        wM3_2313OutputVo = JSONObject.toJavaObject(result, WM3_2313OutputVo.class);
        logger.info("响应数据" + wM3_2313OutputVo.toString());
        return wM3_2313OutputVo;
    }
    
    /**
     * 处理线上扣款查询结果
     * @param wM3_2313OutputVo
     */
    private void handleOnlineDebitResult(WM3_2313OutputVo wM3_2313OutputVo) {
        if(null == wM3_2313OutputVo){
            logger.warn("线上扣款查询结果为空！");
            return;
        }
        // 插入还款结果通知记录表(消费队列表)
        for (ResponseListWm3Entiy rentity : wM3_2313OutputVo.getList()) {
            String dealSts = rentity.getDealSts();
            if (!WMDebitDealStsEnum.处理成功.getCode().equals(dealSts)
                    && !WMDebitDealStsEnum.处理失败.getCode().equals(dealSts)
                    && !WMDebitDealStsEnum.服务异常.getCode().equals(dealSts)) {
                logger.info("扣款结果未明确，处理结束！");
                continue;
            }
            if (WMDebitDealStsEnum.处理成功.getCode().equals(dealSts)) {// 扣款成功
                this.insertRepayNotifyLog(rentity);
            }
            // 更新表
            this.updateDebit(rentity);
        }
    }
    
    /**
     * 处理线下实收扣款查询结果
     * @param wM3_2313OutputVo
     */
    private void handleOfflineDebitResult(WM3_2313OutputVo wM3_2313OutputVo) {
        if(null == wM3_2313OutputVo){
            logger.warn("线下实收查询结果为空！");
            return;
        }
        for (ResponseListWm3Entiy rentity : wM3_2313OutputVo.getList()) {
            // 批次号
            String batNo = rentity.getBatNo();
            // 合同编号
            String pactNo = rentity.getPactNo();
            // 处理状态 01未处理 、02处理中、 03处理成功、 04处理失败
            String dealSts = rentity.getDealSts();
            // 处理说明
            String dealDesc = rentity.getDealDesc();
            if (!WMDebitDealStsEnum.处理成功.getCode().equals(dealSts)
                    && !WMDebitDealStsEnum.处理失败.getCode().equals(dealSts)
                    && !WMDebitDealStsEnum.服务异常.getCode().equals(dealSts)) {
                logger.info("扣款结果未明确，处理结束！");
                continue;
            }
            // 查询线下实收记录
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("batNo", batNo);
            params.put("pactNo", pactNo);
            // 批次号+合同号定位唯一一条线下实收记录
            List<DebitOfflineOfferInfo> offlineOfferInfoList = debitOfflineOfferInfoDao.findListByMap(params);
            if (CollectionUtils.isEmpty(offlineOfferInfoList)) {
                logger.warn("查询不存在线下还款流水记录，批次号："+ batNo +"，合同编号："+ pactNo);
                continue;
            }
            DebitOfflineOfferInfo info = offlineOfferInfoList.get(0);
            
            if (WMDebitDealStsEnum.处理成功.getCode().equals(dealSts)) {
                info.setState(OfferTransactionStateEnum.扣款成功.getValue());
            } else {
                info.setState(OfferTransactionStateEnum.扣款失败.getValue());
            }
            info.setMemo(dealDesc);
            debitOfflineOfferInfoDao.update(info);
        }
    }
}
