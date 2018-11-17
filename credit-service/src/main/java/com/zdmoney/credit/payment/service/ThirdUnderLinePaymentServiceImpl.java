package com.zdmoney.credit.payment.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.FundsSourcesTypeEnum;
import com.zdmoney.credit.common.constant.ThirdOfferFinancialTypeEnum;
import com.zdmoney.credit.common.constant.ThirdOfferStateEnum;
import com.zdmoney.credit.common.constant.offer.ThirdOfferConst;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.util.Assert;
import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.util.excel.ExcelTemplet;
import com.zdmoney.credit.core.service.pub.IFinanceCoreService;
import com.zdmoney.credit.fee.offer.LoanFeeUtil;
import com.zdmoney.credit.loan.dao.pub.ILoanBaseDao;
import com.zdmoney.credit.loan.domain.LoanBase;
import com.zdmoney.credit.payment.dao.pub.IThirdLineOfferBatchDao;
import com.zdmoney.credit.payment.dao.pub.IThirdLineOfferDao;
import com.zdmoney.credit.payment.dao.pub.IThirdLineOfferTransactionDao;
import com.zdmoney.credit.payment.dao.pub.IThirdUnderLinePaymentDao;
import com.zdmoney.credit.payment.domain.ThirdLineOffer;
import com.zdmoney.credit.payment.domain.ThirdLineOfferBatch;
import com.zdmoney.credit.payment.domain.ThirdLineOfferTransaction;
import com.zdmoney.credit.payment.service.pub.IThirdLineOfferService;
import com.zdmoney.credit.payment.service.pub.IThirdUnderLinePaymentService;
import com.zdmoney.credit.system.service.pub.ISequencesService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;

@Service
public class ThirdUnderLinePaymentServiceImpl implements IThirdUnderLinePaymentService {

    protected static Log logger = LogFactory.getLog(ThirdUnderLinePaymentServiceImpl.class);
     
    @Autowired
    private IThirdUnderLinePaymentDao thirdUnderLinePaymentDao;
    
    @Autowired
    private IThirdLineOfferBatchDao haTwoOfferBatchDao;
    
    @Autowired
    private IThirdLineOfferDao haTwoOfferDao;
    
    @Autowired
    private IThirdLineOfferTransactionDao haTwoOfferTransactionDao;
    
    @Autowired
    private IFinanceCoreService financeCoreService;
    
    @Autowired
    private ISequencesService sequencesService;
    
    @Autowired
    private IThirdLineOfferService haTwoOfferService;
    
    @Autowired
    private ISysParamDefineService sysParamDefineService;

    @Autowired
    private ILoanBaseDao loanBaseDao;

    @Autowired
    private LoanFeeUtil loanFeeUtil;
    
    public void createOffer(Map<String, Object> map) {
        // 创建报盘信息
    	List<ThirdLineOffer> offerList=haTwoOfferService.createThirdLineOffer(map);
        // 创建报盘流水和批次信息
        this.createOfferTransaction(map,offerList);
    }
    
    /**
     * 生成报盘批次和流水信息
     * @param map
     * @param offerList 
     */
    private void createOfferTransaction(Map<String, Object> map, List<ThirdLineOffer> fkbjOfferList){
        // 先查询是否有未导出的批次，没有的话，就进行以下操作
        List<ThirdLineOfferBatch> thirdLineOfferBatchs = haTwoOfferBatchDao.findHaTwoOfferBatchNotExport();
        if (CollectionUtils.isNotEmpty(thirdLineOfferBatchs)) {
            throw new PlatformException(ResponseEnum.FULL_MSG, "不能生成报盘文件，因存在未导出的报盘文件！");
        }
        // 合同来源
        String fundsSource = Strings.convertValue(map.get("fundsSource"), String.class);
        // 根据合同来源查询报盘状态为未报盘和扣款失败的放款本金报盘信息
//        List<ThirdLineOffer> fkbjOfferList = this.queryOfferList(fundsSource, ThirdOfferFinancialTypeEnum.放款本金.getValue());
        if(CollectionUtils.isEmpty(fkbjOfferList)){
            throw new PlatformException(ResponseEnum.FULL_MSG,"报盘文件不存在，不能生成批次信息！");
        }
        // 生成放款本金的报盘批次和流水信息
        this.createOfferBatchAndTransaction(fundsSource, fkbjOfferList);
    }
    
    /**
     * 生成报盘
     */
    public void createHaTwoOffer() {
        // 先查询是否有未导出的批次，没有的话，就进行以下操作
        List<ThirdLineOfferBatch> thirdLineOfferBatchs = haTwoOfferBatchDao.findHaTwoOfferBatchNotExport();
        if (CollectionUtils.isNotEmpty(thirdLineOfferBatchs)) {
            throw new PlatformException(ResponseEnum.FULL_MSG, "不能生成报盘文件，因存在未导出的报盘文件！");
        }
        // 查询报盘状态为未报盘和扣款失败的放款本金报盘信息
        List<ThirdLineOffer> fkbjOfferList = this.queryOfferList(null, ThirdOfferFinancialTypeEnum.放款本金.getValue());
        if(CollectionUtils.isEmpty(fkbjOfferList)){
            throw new PlatformException(ResponseEnum.FULL_MSG,"报盘文件不存在，不能生成批次信息！");
        }
        // 生成放款本金的报盘批次和流水信息
        this.createOfferBatchAndTransaction(null, fkbjOfferList);
    }

    /**
     * 生成报盘批次和流水信息
     * @param offerList
     */
    private void createOfferBatchAndTransaction(String fundsSource, List<ThirdLineOffer> offerList) {
        // 生成报盘批次
        ThirdLineOfferBatch haTwoOfferBatch = new ThirdLineOfferBatch();
        haTwoOfferBatch.setId(sequencesService.getSequences(SequencesEnum.THIRD_LINE_OFFER_BATCH));
        // 交易标志（代付）
        haTwoOfferBatch.setTradeMark(ThirdOfferConst.TRADE_MARK_F);
        // 查询配置的商户ID
        String merchantId = null;
        if(FundsSourcesTypeEnum.渤海2.getValue().equals(fundsSource)){
            // 渤海2的商户号
            merchantId = sysParamDefineService.getSysParamValue("thirdOffer", "bh2MerchantId");
        }else if(FundsSourcesTypeEnum.华瑞渤海.getValue().equals(fundsSource)){
            // 华瑞渤海的商户号
            merchantId = sysParamDefineService.getSysParamValue("thirdOffer", "hrbhMerchantId");
        } else {
            // 渤海信托的商户号
            merchantId = sysParamDefineService.getSysParamValue("thirdOffer", "bhxtMerchantId");
        }
        if(Strings.isEmpty(merchantId)){
            throw new PlatformException(ResponseEnum.FULL_MSG,"商户ID不能为空！");
        }
        // 商户ID
        haTwoOfferBatch.setMerchantId(merchantId);
        // 提交日期
        haTwoOfferBatch.setCommitTime(new Date());
        // 报盘文件记录数
        haTwoOfferBatch.setRecordsTotal(offerList.size());
        // 业务类型
        haTwoOfferBatch.setBusinessType(ThirdOfferConst.BUSINESS_TYPE);
        // 创建时间
        haTwoOfferBatch.setCreateTime(new Date());
        // 版本号
        haTwoOfferBatch.setVersion(ThirdOfferConst.VERSION);
        // 是否已导出报盘文件（t：是，f：否）
        haTwoOfferBatch.setExportFile(ThirdOfferConst.EXPORT_FILE_F);
        String date = Dates.getDateTime(new Date(), "yyyyMMdd");
        // 创建当日批次号
        String dayBatchNumber = this.createBatchNumber();
        haTwoOfferBatch.setDayBatchNumber(dayBatchNumber);
        // 文件名
        StringBuilder fileName = new StringBuilder();
        fileName.append(merchantId);
        fileName.append("_");
        fileName.append(ThirdOfferConst.TRADE_MARK_F);
        fileName.append(ThirdOfferConst.VERSION);
        fileName.append(date);
        fileName.append("_");
        fileName.append(dayBatchNumber);
        haTwoOfferBatch.setFileName(fileName.toString());
        // 报盘总金额
        BigDecimal totalAmount = new BigDecimal(0.00);
        // 设置报盘总金额
        for (ThirdLineOffer offerInfo : offerList) {
            // 报盘金额累加
            totalAmount = totalAmount.add(offerInfo.getAmount());
        }
        // 批次信息记录报盘总金额
        haTwoOfferBatch.setAmountTotal(totalAmount);
        // 记录批次信息
        haTwoOfferBatchDao.insert(haTwoOfferBatch);
        
        for (int i=0; i< offerList.size(); i++) {
            ThirdLineOffer haTwoOffer = offerList.get(i);
            ThirdLineOfferTransaction transactionInstance = new ThirdLineOfferTransaction();
            transactionInstance.setId(sequencesService.getSequences(SequencesEnum.THIRD_LINE_OFFER_TRANSACTION));
            // 报盘id
            Long offerId = haTwoOffer.getId();
            transactionInstance.setTwoOfferId(offerId);
            // 记录序号
            String recordNumber = this.updateRecordNumber(i + 1);
            // 流水号：报盘类型（放款本金：'FKBJ'，手续费：'SXF'）+ 提交日期YYYYMMDD + 当日批次号 + 记录序号
            String flowNumber = haTwoOffer.getFinancialType() + Dates.getDateTime(new Date(), "yyyyMMdd") + dayBatchNumber + recordNumber;
            transactionInstance.setFlowNumber(flowNumber);
            // 批次ID
            transactionInstance.setBatchId(haTwoOfferBatch.getId());
            // 记录序号 
            transactionInstance.setRecordNumber(recordNumber);
            // 报盘状态
            transactionInstance.setState(ThirdOfferStateEnum.未报盘.getValue());
            // 记录报盘流水信息
            haTwoOfferTransactionDao.insert(transactionInstance);
        }
    }

    /**
     * 根据合同来源查询报盘状态为未报盘和扣款失败的放款本金报盘信息
     * @param financialType
     * @return
     */
    private List<ThirdLineOffer> queryOfferList(String fundsSource, String financialType) {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("financialType", financialType);
        params.put("states", new String[]{ThirdOfferStateEnum.未报盘.getValue(), ThirdOfferStateEnum.扣款失败.getValue()});
        if(Strings.isNotEmpty(fundsSource)){
            params.put("fundsSource", fundsSource);
        }
        return haTwoOfferDao.findListByMap(params);
    }
    
    /**
     * 创建当天批次号
     * @return
     */
    public String createBatchNumber() {
        ThirdLineOfferBatch searchVo = new ThirdLineOfferBatch();
        searchVo.setCommitTime(new Date());
        List<ThirdLineOfferBatch> batchList = haTwoOfferBatchDao.findListByVo(searchVo);
        if(CollectionUtils.isEmpty(batchList)){
            return ThirdOfferConst.START_RECORD_NO;
        }
        int maxBathNumber = 0;
        int bathNumber = 0;
        for(ThirdLineOfferBatch batchInfo : batchList){
            bathNumber = Integer.parseInt(batchInfo.getDayBatchNumber());
            if(maxBathNumber < bathNumber){
                maxBathNumber = bathNumber;
            }
        }
        return StringUtils.leftPad(String.valueOf(maxBathNumber + 1), 5, "0");
    }
    
    /**
     * 记录序号  同一债权，从000001开始累加
     * @param paramMap
     */
    private String updateRecordNumber(int index){
        return StringUtils.leftPad(String.valueOf(index), 6, "0");
    }
    
    /**
     * 导入回盘文件
     */
    public void updateHaTwoOffer(List<Map<String, String>> dataList) {
        User user = UserContext.getUser();
        String userCode = null;
        if(null != user){
            userCode = user.getUserCode();
        }
        for (Map<String, String> map : dataList) {
            // 备注
            String remark = map.get("remark");
            // 反馈码
            String feedbackCode = map.get("feedbackCode");
            // 原因
            String reason = map.get("reason");
            
            ThirdLineOffer haTwoOffer = new ThirdLineOffer();
            ThirdLineOfferTransaction haTwoOfferTransaction = new ThirdLineOfferTransaction();
            Long loanId = null;
            try {
                Assert.notNullAndEmpty(remark, "备注");
                Assert.notNullAndEmpty(feedbackCode, "反馈码");
                // 根据流水号和报盘状态（必须是：已报盘）查询报盘流水信息
                Map<String, Object> paramMap = new HashMap<String, Object>();
                paramMap.put("flowNumber", remark);
                paramMap.put("state", ThirdOfferStateEnum.已报盘.getValue());
                haTwoOfferTransaction = haTwoOfferTransactionDao.findHaTwoOfferTransactionByMap(paramMap);
                if (null == haTwoOfferTransaction) {
                    throw new PlatformException(ResponseEnum.FULL_MSG,"查询不到待更新的报盘流水信息，请检查回盘文件是否有误！");
                }
                
                String state = ThirdOfferStateEnum.扣款失败.getValue();
                if(ThirdOfferConst.SUCCESS_BACK_CODE.equals(feedbackCode)){
                    state = ThirdOfferStateEnum.扣款成功.getValue();
                }
                // 更变报盘状态表信息
                haTwoOfferTransaction.setState(state);
                // 设置反馈码
                haTwoOfferTransaction.setFeedbackCode(feedbackCode);
                // 设置反馈原因
                haTwoOfferTransaction.setReason(reason);
                // 设置回盘时间
                haTwoOfferTransaction.setReturnTime(new Date());
                // 更新报盘流水信息
                haTwoOfferTransactionDao.update(haTwoOfferTransaction);
                
                // 查询报盘信息
                haTwoOffer = haTwoOfferDao.get(haTwoOfferTransaction.getTwoOfferId());
                if (null == haTwoOffer) {
                    throw new PlatformException(ResponseEnum.FULL_MSG,"查询不到待更新的报盘信息，请检查回盘文件是否有误！");
                }
                if(!ThirdOfferStateEnum.已报盘.getValue().equals(haTwoOffer.getState())){
                    throw new PlatformException(ResponseEnum.FULL_MSG,"报盘状态不是已报盘，不能更新报盘信息！");
                }
                // 更新报盘信息
                haTwoOffer.setState(state);
                haTwoOfferDao.update(haTwoOffer);
                
                // 债权编号
                loanId = haTwoOffer.getLoanId();
                // 判断某一笔债权的放款本金和手续费是否均回盘成功，成功则调用放款接口
                if(this.isSuccessDebit(loanId)){
                    // 调用放款接口
//                    FinanceVo financeVo = new FinanceVo();
//                    financeVo.setUserCode(userCode);
//                    financeVo.setIds(loanId.toString());
                    Map<String,Object> result = null;
                    String code = null;
                    String message = null;
                    try{
                        LoanBase loanBase = loanBaseDao.findByLoanId(loanId);
                        loanFeeUtil.createFee(loanId,loanBase.getFundsSources());
//                        result = financeCoreService.grantLoan(financeVo);
//                        if(null == result){
//                            throw new PlatformException(ResponseEnum.FULL_MSG,"调用放款接口异常！");
//                        }
//                        code =  Strings.convertValue(result.get("code"), String.class);
//                        if(Constants.SUCCESS_CODE.equals(code)){
                            map.put(ExcelTemplet.FEED_BACK_MSG, "导入回盘成功！");
//                        } else {
//                            message = Strings.convertValue(result.get("message"), String.class);
//                            map.put(ExcelTemplet.FEED_BACK_MSG, "调用放款接口失败，失败原因是：" + message);
//                        }
                    }catch(Exception e){
                        logger.error("调用放款接口失败，失败原因是：" + e.getMessage());
                        map.put(ExcelTemplet.FEED_BACK_MSG, "调用放款接口异常！");
                    }
                } else {
                    map.put(ExcelTemplet.FEED_BACK_MSG, "导入回盘成功！");
                }
            } catch (PlatformException e) {
                map.put(ExcelTemplet.FEED_BACK_MSG, e.getMessage());
            } catch (Exception e) {
                logger.error("导入回盘文件异常，异常信息：" + e.getMessage());
                map.put(ExcelTemplet.FEED_BACK_MSG, "导入回盘文件异常！");
            }
        }
    }
    
    /**
     * 判断某一笔债权的放款本金和手续费是否都回盘成功
     * @param loanId
     * @return
     */
    private boolean isSuccessDebit(Long loanId) {
        ThirdLineOffer searchVo = new ThirdLineOffer();
        searchVo.setLoanId(loanId);
        searchVo.setState(ThirdOfferStateEnum.扣款成功.getValue());
        searchVo.setFinancialType(ThirdOfferFinancialTypeEnum.放款本金.getValue());
        List<ThirdLineOffer> offerList = haTwoOfferDao.findListByVo(searchVo);
        if(CollectionUtils.isEmpty(offerList)){
            return false;
        }
        return true;
    }
    
    /**
     * 债权是否可以退回
     */
    public boolean isLoanBack(Long loanId) {
        ThirdLineOffer searchVo = new ThirdLineOffer();
        searchVo.setLoanId(loanId);
        searchVo.setFinancialType(ThirdOfferFinancialTypeEnum.放款本金.getValue());
        List<ThirdLineOffer> offerList = haTwoOfferDao.findListByVo(searchVo);
        if(CollectionUtils.isEmpty(offerList)){
            return true;
        }
        ThirdLineOffer offer = offerList.get(0);
        if(ThirdOfferStateEnum.未报盘.getValue().equals(offer.getState()) 
            || ThirdOfferStateEnum.已报盘.getValue().equals(offer.getState())){
            return false;
        }
        return true;
    }
}
