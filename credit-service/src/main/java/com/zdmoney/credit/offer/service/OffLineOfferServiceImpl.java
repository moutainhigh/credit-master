package com.zdmoney.credit.offer.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.OfferStateEnum;
import com.zdmoney.credit.common.constant.OfferTransactionStateEnum;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.util.Assert;
import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.TPPHelper;
import com.zdmoney.credit.common.util.excel.ExcelTemplet;
import com.zdmoney.credit.loan.dao.pub.ILoanBankDao;
import com.zdmoney.credit.loan.dao.pub.ILoanBankExtDao;
import com.zdmoney.credit.loan.domain.LoanBank;
import com.zdmoney.credit.loan.domain.LoanBankExt;
import com.zdmoney.credit.loan.service.pub.IAfterLoanService;
import com.zdmoney.credit.offer.dao.pub.IOffLineOfferBankDicDao;
import com.zdmoney.credit.offer.dao.pub.IOfferExportRecordDao;
import com.zdmoney.credit.offer.dao.pub.IOfferInfoDao;
import com.zdmoney.credit.offer.dao.pub.IOfferTransactionDao;
import com.zdmoney.credit.offer.domain.OffLineOfferBankDic;
import com.zdmoney.credit.offer.domain.OfferExportRecord;
import com.zdmoney.credit.offer.domain.OfferInfo;
import com.zdmoney.credit.offer.domain.OfferTransaction;
import com.zdmoney.credit.offer.service.pub.IOffLineOfferService;
import com.zdmoney.credit.system.service.pub.ISequencesService;

@Service
public class OffLineOfferServiceImpl implements IOffLineOfferService {
    
    private static Logger logger = Logger.getLogger(OffLineOfferServiceImpl.class);
    
    /** 回盘全部成功 **/
    private static final String RESULT_SUCCESS_CODE = "FSBR0000";
    
    @Autowired
    private IAfterLoanService afterLoanService;
    
    @Autowired
    private ISequencesService sequencesService;

    @Autowired
    private IOfferInfoDao offerInfoDao;
    
    @Autowired
    private IOfferTransactionDao offerTransactionDao;
    
    @Autowired
    private IOfferExportRecordDao offerExportRecordDao;
    
    @Autowired
    private ILoanBankExtDao loanBankExtDao;
    
    @Autowired
    private ILoanBankDao loanBankDao;
    
    @Autowired
    private IOffLineOfferBankDicDao offLineOfferBankDicDao;
    
    public Pager searchOfferInfoWithPgByMap(Map<String, Object> params) {
        return offerInfoDao.findWithPgByMap(params);
    }

    public List<OfferInfo> findOffLineOfferInfoList(Map<String, Object> params) {
        return offerInfoDao.findListByMap(params);
    }

    public List<Map<String, Object>> queryOffLineOfferInfo(Map<String, Object> params) {
        return offerInfoDao.queryOffLineOfferInfo(params);
    }

    public void saveOfferExportRecord(Long transId) {
        OfferTransaction offerTransaction = offerTransactionDao.get(transId);
        if(null == offerTransaction){
            throw new PlatformException(ResponseEnum.FULL_MSG, "报盘流水不存在，流水ID："+ transId);
        }
        OfferExportRecord record = new OfferExportRecord();
        record.setId(sequencesService.getSequences(SequencesEnum.OFFER_EXPORT_RECORD));
        record.setTransId(transId);
        record.setOfferId(offerTransaction.getOfferId());
        offerExportRecordDao.insert(record);
    }

    public void closeOffer(Long offerId) {
        OfferInfo offerInfo = offerInfoDao.get(offerId);
        if (!OfferStateEnum.未报盘.getValue().equals(offerInfo.getState())
                && !OfferStateEnum.已回盘非全额.getValue().equals(offerInfo.getState())) {
            throw new PlatformException(ResponseEnum.FULL_MSG, "该债权系统报盘状态是"+ offerInfo.getState() + ",不能进行关闭操作！");
        }
        Map<String, Object> params = new HashMap<String, Object>();
        User user = UserContext.getUser();
        params.put("offerId", offerId);
        params.put("memo", "该报盘记录已经被" + user.getName() + "关闭");
        offerInfoDao.updateOfferInfo(params);
    }
    
    /**
     * 分页查询线下还款回盘信息
     * @param params
     * @return
     */
    public Pager searchOfferTransactionInfoWithPgByMap(Map<String, Object> params) {
        return offerTransactionDao.searchOfferTransactionInfoWithPgByMap(params);
    }

    /**
     * 线下还款回盘导入
     * @param sheetDataList
     */
    public void importReturnOffer(List<Map<String, String>> sheetDataList) {
        for (Map<String, String> map : sheetDataList) {
            // 合同号
            String contractNum = map.get("contractNum");
            // 扣款帐号户名
            String name = map.get("name");
            // 扣款金额
            String offerAmount = map.get("offerAmount");
            // 银行返回码
            String returnCode = map.get("returnCode");
            //扣款银行卡账号
            String bankAcct = map.get("bankAcct");
            try {
                Assert.notNullAndEmpty(contractNum, "合同号");
                Assert.notNullAndEmpty(bankAcct, "扣款银行卡号");
                Assert.notNullAndEmpty(name, "扣款帐号户名");
                Assert.notNullAndEmpty(offerAmount, "扣款金额");
                Assert.notNullAndEmpty(returnCode, "银行返回码");
                try {
                    new BigDecimal(offerAmount);
                } catch (Exception e) {
                    throw new PlatformException(ResponseEnum.FULL_MSG,"扣款金额类型不正确！");
                }
                // 交易金额必须大于零的校验
                if(new BigDecimal(offerAmount).doubleValue() <= 0){
                    throw new PlatformException(ResponseEnum.FULL_MSG,"扣款金额必须大于零！");
                }
                // 更新报盘记录并实时记账
                this.updateOffer(map);
                map.put(ExcelTemplet.FEED_BACK_MSG, "回盘处理成功！");
            } catch (PlatformException e) {
                map.put(ExcelTemplet.FEED_BACK_MSG, e.getMessage());
            } catch (Exception e) {
                map.put(ExcelTemplet.FEED_BACK_MSG, "回盘处理异常："+ e.getMessage());
            }
        }
    }

    /**
     * 更新报盘记录并实时记账
     * @param map
     */
    private void updateOffer(Map<String, String> map) {
        // 合同号（交易流水号）
        String contractNum = map.get("contractNum");
        // 扣款金额
        String offerAmount = map.get("offerAmount");
      //扣款银行卡账号
        String bankAcct = map.get("bankAcct");
        // 银行返回码
        String returnCode = map.get("returnCode");
        // 扣款结果
        String returnMsg = map.get("returnMsg");
        // 查询线下还款已发送且已导出的报盘流水信息
        Map<String,Object> params = new HashMap<String,Object>();
        OfferTransaction offerTransaction = null;
        User user = UserContext.getUser();
        params.put("bankAcct", bankAcct);
        List<OfferTransaction> otList = offerTransactionDao.findOfferTranscationByBankAcct(params);
        if(otList == null || otList.size() == 0){
          throw new PlatformException(ResponseEnum.FULL_MSG,"扣款银行卡号：" + bankAcct +  "未找到已发送的报盘流水记录，回盘处理失败！");
        }
        if(otList.size() > 1){
            throw new PlatformException(ResponseEnum.FULL_MSG,"扣款银行卡号：" + bankAcct +  "存在多条报盘流水记录，回盘处理失败");
        }
        offerTransaction = otList.get(0);        
        if(OfferTransactionStateEnum.扣款成功.getValue().equals(offerTransaction.getTrxState()) ||
                OfferTransactionStateEnum.扣款失败.getValue().equals(offerTransaction.getTrxState())){
            throw new PlatformException(ResponseEnum.FULL_MSG,"合同号:" + contractNum + "已经回盘完成，不再处理！");
        }
        // 查询报盘信息
        OfferInfo offerInfo = offerInfoDao.get(offerTransaction.getOfferId());
        if(null == offerInfo){
            throw new PlatformException(ResponseEnum.FULL_MSG,"合同号:" + contractNum + "未找到对应的报盘记录，回盘处理失败！");
        }
        // 成功代扣金额
        BigDecimal successAmount = null;
        if (RESULT_SUCCESS_CODE.equals(returnCode)) { // 划扣全部成功
            returnCode = TPPHelper.RESULT_SUCCESS_CODE;
            successAmount = new BigDecimal(offerAmount);
            offerTransaction.setActualAmount(successAmount);
            offerTransaction.setTrxState(OfferTransactionStateEnum.扣款成功.getValue());
            offerInfo.setState(OfferStateEnum.已回盘全额.getValue());
        } /*else if (RESULT_BFSUCCESS_CODE.equals(returnCode)) { // 划扣部分成功
            returnCode = TPPHelper.RESULT_BFSUCCESS_CODE;
            successAmount = new BigDecimal(offerAmount);
            offerTransaction.setActualAmount(successAmount);
            offerTransaction.setTrxState(OfferTransactionStateEnum.扣款成功.getValue());
            offerInfo.setState(OfferStateEnum.已回盘非全额.getValue());
        }*/ else { // 划扣失败
            returnCode = TPPHelper.RESULT_FAIL_CODE;
            // 失败时记录零
            successAmount = new BigDecimal(0);
            offerTransaction.setActualAmount(successAmount);
            offerTransaction.setTrxState(OfferTransactionStateEnum.扣款失败.getValue());
            offerInfo.setState(OfferStateEnum.已回盘非全额.getValue());
        }
        offerTransaction.setRspReceiveTime(new Date());
        offerTransaction.setUpdateTime(offerTransaction.getRspReceiveTime());
        offerTransaction.setRdoTime(offerTransaction.getRspReceiveTime());
        offerTransaction.setRtnCode(returnCode);
        offerTransaction.setRtnInfo(returnMsg);
        offerTransaction.setMemo("");
        offerTransaction.setPaySysNoReal(offerInfo.getPaySysNo());
        // 如果是划扣全部成功或部分成功，需实时记账
        if (TPPHelper.RESULT_SUCCESS_CODE.equals(returnCode)) {
            try {
                // 实时记账
                afterLoanService.accountingByTransaction(offerTransaction);
            } catch (Exception e) {
                logger.error("合同号:" + contractNum + "，记账处理失败！", e);
                throw new PlatformException(ResponseEnum.FULL_MSG, "合同号:" + contractNum + "，记账处理失败！");
            }
        }
        // 更新报盘流水信息
        offerTransactionDao.update(offerTransaction);
        
        offerInfo.setReturnCode(returnCode);
        offerInfo.setCause(returnMsg);
        // 更新报盘金额 报盘金额=报盘金额-本次成功扣款金额
        offerInfo.setOfferAmount(offerInfo.getOfferAmount().subtract(successAmount));
        // 更新成功扣款总金额
        offerInfo.setActualAmount(offerInfo.getActualAmount().add(successAmount));
        // 更新报盘记录信息
        offerInfoDao.updateByPrimaryKeySelective(offerInfo);
    }
    
    public Pager queryOffLineLoanInfo(Map<String, Object> params) {
        return loanBankExtDao.queryOffLineLoanInfo(params);
    }

    /**
     * 设置银行卡行别、行号
     */
    public void setRegionType(String[] bankIdArr, String regionType) {
        for(String bankId : bankIdArr){
            // 查询银行账户表信息
            LoanBank loanBank = loanBankDao.get(Long.valueOf(bankId));
            if(null == loanBank){
                throw new PlatformException(ResponseEnum.FULL_MSG, "债权对应的银行账户信息不存在，bankId："+ bankId);
            }
            // 查询行别、行号字典配置信息
            OffLineOfferBankDic offLineOfferBankDic = this.findOffLineOfferBankDic(loanBank, regionType);
            if(null == offLineOfferBankDic){
                throw new PlatformException(ResponseEnum.FULL_MSG, "没有配置银行卡所属地区信息，银行账户："+ loanBank.getAccount());
            }
            // 查询已经保存的行别、行号配置信息
            LoanBankExt loanBankExt = this.findLoanBankExtByBankId(loanBank.getId());
            if(null == loanBankExt){
                // 保存行别、行号配置信息
                loanBankExt = new LoanBankExt();
                loanBankExt.setId(sequencesService.getSequences(SequencesEnum.LOAN_BANK_EXT));
                loanBankExt.setLoanBankId(Long.valueOf(bankId));
                loanBankExt.setOfferBankId(offLineOfferBankDic.getId());
                loanBankExtDao.insert(loanBankExt);
            } else {
                // 更新行别、行号配置信息
                loanBankExt.setOfferBankId(offLineOfferBankDic.getId());
                loanBankExtDao.update(loanBankExt);
            }
        }
    }

    /**
     * 查询行别、行号字典配置信息
     * @param loanBank
     * @param regionType
     * @return
     */
    private OffLineOfferBankDic findOffLineOfferBankDic(LoanBank loanBank, String regionType) {
        // 查询行别、行号字典配置信息
        String bankCode = loanBank.getBankCode();
        OffLineOfferBankDic searchVo = new OffLineOfferBankDic();
        searchVo.setCode(bankCode);
        searchVo.setRegionType(regionType);
        List<OffLineOfferBankDic> resultList = offLineOfferBankDicDao.findListByVo(searchVo);
        if(CollectionUtils.isNotEmpty(resultList)){
            return resultList.get(0);
        }
        return null;
    }

    /**
     * 查询已经保存的行别、行号配置信息
     * @param bankId
     * @return
     */
    private LoanBankExt findLoanBankExtByBankId(Long bankId) {
        LoanBankExt searchVo = new LoanBankExt();
        searchVo.setLoanBankId(bankId);
        List<LoanBankExt> resultList = loanBankExtDao.findListByVo(searchVo);
        if(CollectionUtils.isNotEmpty(resultList)){
            return resultList.get(0);
        }
        return null;
    }

    /**
     * 判断线下还款报盘文件是否已导出
     */
    public boolean isExportOffer(Long offerId) {
        OfferExportRecord searchVo = new OfferExportRecord();
        searchVo.setOfferId(offerId);
        List<OfferExportRecord> offerExportRecordList = offerExportRecordDao.findListByVo(searchVo);
        if(CollectionUtils.isNotEmpty(offerExportRecordList)){
            return true;
        }
        return false;
    }
}
