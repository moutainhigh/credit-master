package com.zdmoney.credit.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.zdmoney.credit.common.constant.FundsSourcesTypeEnum;
import com.zdmoney.credit.common.constant.GatewayFuncIdEnum;
import com.zdmoney.credit.common.constant.wm.WMDebitDeductStateEnum;
import com.zdmoney.credit.common.gateway.GatewayUtils;
import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.debit.dao.pub.IDebitOfferFlowDao;
import com.zdmoney.credit.debit.domain.DebitOfferFlow;
import com.zdmoney.credit.framework.vo.common.BaseParamVo;
import com.zdmoney.credit.framework.vo.wm3.entity.DeductMoneyDetailListEntity;
import com.zdmoney.credit.framework.vo.wm3.entity.DeductMoneyListEntity;
import com.zdmoney.credit.framework.vo.wm3.entity.FalseRecordLineDownListEntity;
import com.zdmoney.credit.framework.vo.wm3.entity.FalseRecordListEntity;
import com.zdmoney.credit.framework.vo.wm3.entity.PaidInMoneyListEntity;
import com.zdmoney.credit.framework.vo.wm3.input.WM3_2311Vo;
import com.zdmoney.credit.framework.vo.wm3.input.WM3_2312Vo;
import com.zdmoney.credit.framework.vo.wm3.output.WM3_2311OutputVo;
import com.zdmoney.credit.framework.vo.wm3.output.WM3_2312OutputVo;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;
import com.zdmoney.credit.trustOffer.service.pub.ITrustOfferFlowService;

/**
 * @author 10098  2017年3月29日 下午2:49:00
 */
@Service
public class DebitOfferFlow4WM3Job {

    private Logger logger = LoggerFactory.getLogger(TrustOfferFlowAccountingJob.class);
    @Autowired
    private ISysParamDefineService sysParamDefineService;
    @Autowired
    private ITrustOfferFlowService trustOfferFlowService;
    @Autowired
    private IDebitOfferFlowDao debitOfferFlowDao;
    
    public void executeOnlineRepayment(){
        logger.info("开始执行生成外贸3线上扣款分账流水JOB");
        
        String isDebitOfferFlow4WM3 = sysParamDefineService.getSysParamValue("sysJob", "isDebitOfferFlow4WM3");
        if(Strings.isEmpty(isDebitOfferFlow4WM3) || "0".equals(isDebitOfferFlow4WM3)){
            logger.info("isDebitOfferFlow4WM3执行关闭！");
            return ;
        }
        try{
            // 同步交易清分数据
            this.syncLoanTradeClear(null, null);
        }catch(Exception e){
            logger.error("外贸3线上扣款分账流水JOB异常", e);
        }
    }


    public void pushOnlineRepayment(){
        logger.info("开始执行推送外贸3线上扣款分账流水JOB");
        String isDebitOfferFlow4WM3 = sysParamDefineService.getSysParamValue("sysJob", "isPushDebitOfferFlow4WM3");
        if(Strings.isEmpty(isDebitOfferFlow4WM3) || "0".equals(isDebitOfferFlow4WM3)){
            logger.info("isDebitOfferFlow4WM3执行关闭！");
            return ;
        }
        try{
            // 推送分账信息给外贸信托
            this.syncOfferToWm3();
        }catch(Exception e){
            logger.error("开始执行推送外贸3线上扣款分账流水JOB", e);
        }
    }
    
    /**
     * 同步交易清分数据
     * @param startDate
     * @param endDate
     */
    public void syncLoanTradeClear(String startDate, String endDate){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("loanBelong", FundsSourcesTypeEnum.外贸3.getValue());
        if(Strings.isEmpty(startDate)){
            startDate = Dates.getDateTime(Dates.getBeforeDays(1), Dates.DEFAULT_DATE_FORMAT);
        }
        params.put("tradeDateStart", startDate);
        if(Strings.isEmpty(endDate)){
            endDate = Dates.getDateTime(Dates.DEFAULT_DATE_FORMAT);
        }
        params.put("tradeDateEnd", endDate);
        trustOfferFlowService.findDebitOfferFlowByParams(params);
    }
    
    /**
     * 推送分账信息给外贸信托
     */
    public void syncOfferToWm3(){
        Map<String, Object> dofParams = new HashMap<>();
        dofParams.put("states",new String[]{WMDebitDeductStateEnum.未发送.getValue(),WMDebitDeductStateEnum.扣款失败.getValue()});
        dofParams.put("debitOrder","true");
        List<DebitOfferFlow> list = debitOfferFlowDao.findListByMap(dofParams);
        trustOfferFlowService.adjustDebitOfferFlowBatNo(list);
        this.requestDeductOnlineMoney2WM3(list);
    }

    
    /**
     * 发送外贸3 线上扣款请求
     * @param list
     */
    @Transactional
    private void requestDeductOnlineMoney2WM3(List<DebitOfferFlow> list) {
        //请求批次列表
        List<WM3_2311Vo> batList = new ArrayList<WM3_2311Vo>();
        WM3_2311Vo wm3_2311Vo = new WM3_2311Vo();
        //批次下合同列表
        List<DeductMoneyListEntity> conList = null;
        //合同下分账列表
        List<DeductMoneyDetailListEntity> divideList = null;
        for(DebitOfferFlow debitOfferFlow:list){
            String batNo = debitOfferFlow.getBatNo();
            if(Strings.isEmpty(wm3_2311Vo.getBatNo()) || !wm3_2311Vo.getBatNo().equals(batNo)){
                wm3_2311Vo = new WM3_2311Vo();
                wm3_2311Vo.setBatNo(batNo);
                wm3_2311Vo.setBrNo(BaseParamVo.SYS_SOURCE_WM3);
                conList = new ArrayList<DeductMoneyListEntity>();
                wm3_2311Vo.setList(conList);
                batList.add(wm3_2311Vo);
            }
            if(CollectionUtils.isEmpty(conList) || !conList.get(conList.size()-1).getPactNo().equals(debitOfferFlow.getPactNo())){
                DeductMoneyListEntity deductMoneyListEntity = new DeductMoneyListEntity();
                deductMoneyListEntity.setAcNo(debitOfferFlow.getAcNo());
                deductMoneyListEntity.setCardChn(debitOfferFlow.getCardChn());
                deductMoneyListEntity.setPactNo(debitOfferFlow.getPactNo());
                deductMoneyListEntity.setRepayAmt(debitOfferFlow.getRepayAmt()); //实收金额  待确认？
                deductMoneyListEntity.setRepayType(debitOfferFlow.getRepyType());
                deductMoneyListEntity.setSerialNo(debitOfferFlow.getSerialNo());
                conList.add(deductMoneyListEntity);
                divideList = new ArrayList<DeductMoneyDetailListEntity>();
                deductMoneyListEntity.setListSubj(divideList);                
            }
            DeductMoneyDetailListEntity deductMoneyDetailListEntity = new DeductMoneyDetailListEntity();
            deductMoneyDetailListEntity.setCnt(debitOfferFlow.getCnt());
            deductMoneyDetailListEntity.setSubjAmt(debitOfferFlow.getSubjAmt());
            deductMoneyDetailListEntity.setSubjType(debitOfferFlow.getSubjType());
            divideList.add(deductMoneyDetailListEntity);
        }
        Map<String, String> falseMap = new HashMap<String, String>();
        for(WM3_2311Vo wm3_2311:batList){
            wm3_2311.setDataCnt(wm3_2311.getList().size());
            //发送请求
            JSONObject jsonObject = GatewayUtils.callCateWayInterface(wm3_2311, GatewayFuncIdEnum.外贸3线上扣款.getCode());
            JSONObject jsonContent = GatewayUtils.getReponseContentJSONObject(jsonObject, false);
            WM3_2311OutputVo wm3_2311OutputVo = JSONObject.toJavaObject(jsonContent, WM3_2311OutputVo.class);
            List<FalseRecordListEntity>  falseList = wm3_2311OutputVo.getList();
            if(CollectionUtils.isNotEmpty(falseList)){
                for(FalseRecordListEntity falseRecordListEntity:falseList){
                    falseMap.put(falseRecordListEntity.getPactNo(), falseRecordListEntity.getDealDesc());
                }
            }
        }
        for(DebitOfferFlow debitOfferFlow:list){
            if(falseMap.containsKey(debitOfferFlow.getPactNo())){
                debitOfferFlow.setState(WMDebitDeductStateEnum.扣款失败.getValue()); //2-响应失败
                debitOfferFlow.setMemo(falseMap.get(debitOfferFlow.getPactNo()));
            }else{
                debitOfferFlow.setState(WMDebitDeductStateEnum.扣款成功.getValue()); //3-响应成功
                debitOfferFlow.setMemo("发送成功");
            }
            debitOfferFlowDao.update(debitOfferFlow);
        }
    }
    
    /**
     * 发送外贸3 线下扣款请求
     * @param list
     */
    @Transactional
    private void requestDeductOfflineMoney2WM3(List<DebitOfferFlow> list) {
        //请求批次列表
        List<WM3_2312Vo> batList = new ArrayList<WM3_2312Vo>();
        WM3_2312Vo wm3_2312Vo = new WM3_2312Vo();
        //批次下合同列表
        List<PaidInMoneyListEntity> conList = null;
        //合同下分账列表
        List<DeductMoneyDetailListEntity> divideList = null;
        for(DebitOfferFlow debitOfferFlow:list){
            String batNo = debitOfferFlow.getBatNo();
            if(Strings.isEmpty(wm3_2312Vo.getBatNo()) || !wm3_2312Vo.getBatNo().equals(batNo)){
                wm3_2312Vo = new WM3_2312Vo();
                wm3_2312Vo.setBatNo(batNo);
                wm3_2312Vo.setBrNo(BaseParamVo.SYS_SOURCE_WM3);
                conList = new ArrayList<PaidInMoneyListEntity>();
                wm3_2312Vo.setList(conList);
                batList.add(wm3_2312Vo);
            }
            if(CollectionUtils.isEmpty(conList) || !conList.get(0).getPactNo().equals(debitOfferFlow.getPactNo())){
                PaidInMoneyListEntity paidInMoneyListEntity = new PaidInMoneyListEntity();
                paidInMoneyListEntity.setPactNo(debitOfferFlow.getPactNo());
                paidInMoneyListEntity.setRepayAmt(debitOfferFlow.getRepayAmt());
                paidInMoneyListEntity.setRepayType(debitOfferFlow.getRepyType());
                conList.add(paidInMoneyListEntity);
                divideList = new ArrayList<DeductMoneyDetailListEntity>();
                paidInMoneyListEntity.setListSubj(divideList);                
            }
            DeductMoneyDetailListEntity deductMoneyDetailListEntity = new DeductMoneyDetailListEntity();
            deductMoneyDetailListEntity.setCnt(debitOfferFlow.getCnt());
            deductMoneyDetailListEntity.setSubjAmt(debitOfferFlow.getSubjAmt());
            deductMoneyDetailListEntity.setSubjType(debitOfferFlow.getSubjType());
            divideList.add(deductMoneyDetailListEntity);
        }
        Map<String, String> falseMap = new HashMap<String, String>();
        for(WM3_2312Vo wm3_2312:batList){
            wm3_2312.setDataCnt(String.valueOf(wm3_2312.getList().size()));
            //发送请求
            JSONObject jsonObject = GatewayUtils.callCateWayInterface(wm3_2312, GatewayFuncIdEnum.外贸3线下实收.getCode());
            JSONObject jsonContent = GatewayUtils.getReponseContentJSONObject(jsonObject, false);
            WM3_2312OutputVo wm3_2312OutputVo = JSONObject.toJavaObject(jsonContent, WM3_2312OutputVo.class);
            List<FalseRecordLineDownListEntity>  falseList = wm3_2312OutputVo.getList();
            if(CollectionUtils.isNotEmpty(falseList)){
                for(FalseRecordLineDownListEntity falseRecordListEntity:falseList){
                    falseMap.put(falseRecordListEntity.getPactNo(), falseRecordListEntity.getDealDesc());
                }
            }
        }
        for(DebitOfferFlow debitOfferFlow:list){
            if(falseMap.containsKey(debitOfferFlow.getPactNo())){
                debitOfferFlow.setState(WMDebitDeductStateEnum.扣款失败.getValue()); //2-响应失败
                debitOfferFlow.setMemo(falseMap.get(debitOfferFlow.getPactNo()));
            }else{
                debitOfferFlow.setState(WMDebitDeductStateEnum.扣款成功.getValue()); //3-响应成功
                debitOfferFlow.setMemo("发送成功");
            }
            debitOfferFlowDao.update(debitOfferFlow);
        }
    }
}
