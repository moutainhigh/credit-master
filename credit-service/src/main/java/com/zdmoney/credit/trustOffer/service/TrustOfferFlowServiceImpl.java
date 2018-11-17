package com.zdmoney.credit.trustOffer.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.zdmoney.credit.common.constant.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.zdmoney.credit.common.constant.repay.ReqManagerFileTypeEnum;
import com.zdmoney.credit.common.constant.repay.ReqManagerOperateTypeEnum;
import com.zdmoney.credit.common.constant.repay.RequestManagementConst;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.constant.trustOffer.TrustOfferEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.service.pub.IWorkDayInfoService;
import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.ExcelExportUtil;
import com.zdmoney.credit.common.util.FTPUtil;
import com.zdmoney.credit.common.util.NumberUtil;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.util.coreUtil.JSONUtil;
import com.zdmoney.credit.common.util.file.JschSftpUtils;
import com.zdmoney.credit.debit.domain.DebitOfferFlow;
import com.zdmoney.credit.debit.service.pub.IDebitOfferFlowService;
import com.zdmoney.credit.grant.dao.pub.IDebitAccountInfoDao;
import com.zdmoney.credit.grant.domain.DebitAccountInfo;
import com.zdmoney.credit.loan.dao.pub.ILoanRepaymentDetailDao;
import com.zdmoney.credit.loan.dao.pub.IUploadHkqrBookLogDao;
import com.zdmoney.credit.loan.domain.LoanRepaymentDetail;
import com.zdmoney.credit.loan.domain.UploadHkqrBookLog;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.IAfterLoanService;
import com.zdmoney.credit.loan.service.pub.IConnectBhxtFtpService;
import com.zdmoney.credit.loan.service.pub.ILoanLogService;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import com.zdmoney.credit.offer.dao.pub.IOfferFlowDao;
import com.zdmoney.credit.offer.dao.pub.IOfferRepayInfoDao;
import com.zdmoney.credit.offer.domain.OfferFlow;
import com.zdmoney.credit.offer.domain.OfferRepayInfo;
import com.zdmoney.credit.repay.dao.pub.IRequestFileOperateRecordDao;
import com.zdmoney.credit.repay.service.pub.IRequestManagementService;
import com.zdmoney.credit.repay.vo.RequestFileOperateRecord;
import com.zdmoney.credit.system.service.pub.ISendMailService;
import com.zdmoney.credit.system.service.pub.ISequencesService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;
import com.zdmoney.credit.trustOffer.dao.pub.ITrustOfferFlowDao;
import com.zdmoney.credit.trustOffer.domain.TrustOfferFlow;
import com.zdmoney.credit.trustOffer.service.pub.ITrustOfferFlowService;
import com.zdmoney.credit.trustOffer.vo.SubjectAmount;
import com.zdmoney.credit.trustOffer.vo.TemporaryAmountVo;
import com.zdmoney.credit.trustOffer.vo.TrustOfferFlowVo;


/**
 * Created by ym10094 on 2016/10/17.
 */
@Service
public class TrustOfferFlowServiceImpl  implements ITrustOfferFlowService {
    public Logger logger = LoggerFactory.getLogger(TrustOfferFlowServiceImpl.class);
    public static Map<String,BigDecimal>  relieflAmountMap = new ConcurrentHashMap<>();

    @Autowired
    private IOfferRepayInfoDao offerRepayInfoDao;
    @Autowired
    private IOfferFlowDao offerFlowDao;
    @Autowired
    private ITrustOfferFlowDao trustOfferFlowDao;
    @Autowired
    private ILoanRepaymentDetailDao loanRepaymentDetailDao;
    @Autowired
    private IAfterLoanService afterLoanServiceImpl;
    @Autowired
    private IVLoanInfoService vLoanInfoServiceImpl;
    @Autowired
    private ISequencesService sequencesServiceImpl;
    @Autowired
    private IRequestFileOperateRecordDao requestFileOperateRecordDao;
    @Autowired
    private IWorkDayInfoService workDayInfoService;
    @Autowired
    private IDebitOfferFlowService debitOfferFlowService;
    @Autowired
    private IDebitAccountInfoDao debitAccountInfoDao;
    
	/**外贸3报盘合同 批次数量**/
	private final static int BATCH_CONTRACT_SIZE = 100;
    @Autowired
    private IRequestManagementService requestManagementService;
    @Autowired
    private IConnectBhxtFtpService connectBhxtFtpService;
    @Autowired
    private ISysParamDefineService sysParamDefineService;
    @Autowired
    private ISendMailService sendMailService;
    @Autowired
    private IUploadHkqrBookLogDao uploadHkqrBookLogDao;
    @Autowired
    private ILoanLogService loanLogService;

    @Value("${BHXT.BhxtBook.Excel.Template.Path}")
    private String BhxtHkqrBookTemplateXls ;
    @Value("${BHXT.BhxtBook.Pdf.Template.Path}")
    private String BhxtHkqrBookTemplatePdf;
    @Value("${BH2.BhxtBook.Excel.Template.Path}")
    private String Bh2HkqrBookTemplateXls ;
    @Value("${BH2.BhxtBook.Pdf.Template.Path}")
    private String Bh2HkqrBookTemplatePdf;
    @Value("${HRBH.BhxtBook.Excel.Template.Path}")
    private String HrbhHkqrBookTemplateXls ;
    @Value("${HRBH.BhxtBook.Pdf.Template.Path}")
    private String HrbhHkqrBookTemplatePdf;
    @Override
    public List<OfferRepayInfo> getTrustRepayMentOfferRepayInfoHistroy(String beginTradeDtae, String endTradeDate) {
        Map<String,Object> paramMap = this.getTrustOfferRepayInfoBaseConditions(beginTradeDtae,endTradeDate);
        return offerRepayInfoDao.getTrustOfferRepayInfo(paramMap);
    }

    @Override
    public List<OfferRepayInfo> getTrustRepayMentOfferRepayInfoYesterDay() {
        String beginTradeDate = Dates.getDateTime(Dates.getBeforeDays(1),Dates.DEFAULT_DATE_FORMAT);
        String endTradeDate = Dates.getDateTime(Dates.getBeforeDays(1),Dates.DEFAULT_DATE_FORMAT);
        Map<String,Object> paramMap = this.getTrustOfferRepayInfoBaseConditions(beginTradeDate,endTradeDate);
        return offerRepayInfoDao.getTrustOfferRepayInfo(paramMap);
    }

    private Map<String,Object> getTrustOfferRepayInfoBaseConditions(String beginTradeDtae, String endTradeDate){
        Map<String,Object> paramMap = new HashMap();
        List<String> tradeCodes = new ArrayList();
        tradeCodes.add(Const.TRADE_CODE_NORMAL);
        tradeCodes.add(Const.TRADE_CODE_ONEOFF);
        List<String> fundsSources = new ArrayList<>();
        fundsSources.add(FundsSourcesTypeEnum.渤海信托.getValue());
        fundsSources.add(FundsSourcesTypeEnum.外贸信托.getValue());
        fundsSources.add(FundsSourcesTypeEnum.渤海2.getValue());
        fundsSources.add(FundsSourcesTypeEnum.外贸2.getValue());
        paramMap.put("beginTradeDate",beginTradeDtae);
        paramMap.put("endTradeDate",endTradeDate);
        paramMap.put("tradeCodes",tradeCodes);
        paramMap.put("fundsSources",fundsSources);
        return  paramMap;
    }

    @Override
    public List<OfferFlow> findByTradeNo(String tradeNo) {
        String[] acctTitles = {Const.ACCOUNT_TITLE_FINE_EXP,Const.ACCOUNT_TITLE_INTEREST_EXP,Const.ACCOUNT_TITLE_LOAN_AMOUNT,Const.ACCOUNT_TITLE_PENALTY_EXP};
        return  offerFlowDao.findByTradeNoaccTitleAsc(tradeNo, acctTitles);
    }

    @Override
    public boolean isOverdue(List<OfferFlow> offerFlows) {
        for(OfferFlow offerFlow:offerFlows){
            if(Const.ACCOUNT_TITLE_FINE_EXP.equals(offerFlow.getAcctTitle()) &&  !Const.ACCOUNT_GAINS.equals(offerFlow.getAccount())){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isRelief(List<OfferFlow> offerFlows) {
        for (OfferFlow offerFlow:offerFlows){
            if(Const.ACCOUNT_TITLE_FINE_EXP.equals(offerFlow.getAcctTitle()) && Const.ACCOUNT_GAINS.equals(offerFlow.getAccount())){
                return true;
            }
        }
        return false;
    }

    /**
     * 逾期罚息
     * @param offerFlows
     * @return
     */
    @Override
    public BigDecimal getfineInterest(List<OfferFlow> offerFlows) {
        BigDecimal fineAccrualAmount = new BigDecimal("0.00");
        for (OfferFlow offerFlow:offerFlows) {
            if (Const.ACCOUNT_TITLE_FINE_EXP.equals(offerFlow.getAcctTitle()) && !Const.ACCOUNT_GAINS.equals(offerFlow.getAccount())){
                fineAccrualAmount = fineAccrualAmount.add(offerFlow.getTradeAmount());
            }
        }
        return fineAccrualAmount;
    }

    @Override
    public BigDecimal getAlreaDyFineInterest(Long loanId, Long currentTerm) {
        BigDecimal fineInterestAmount = new BigDecimal("0.00");
        List<TrustOfferFlow> trustOfferFlows = trustOfferFlowDao.getSplitAccountingsByLoanId(loanId,currentTerm);
        String [] fineAcctitles = {TrustOfferEnum.ACCOUNT_TITLE_FINE_EXP.getSubjectCode(),TrustOfferEnum.ACCOUNT_TITLE_FINE_EXP_A.getSubjectCode()};
        fineInterestAmount = this.getAcctTitleAmount(trustOfferFlows,Arrays.asList(fineAcctitles));
        return fineInterestAmount;
    }

    /**
     * 获取减免金额
     * @param offerFlows 可以为null
     * @param isOverdue
     * @param  repayInfo
     * @return
     */
    @Override
    public BigDecimal getReliefAmount(List<OfferFlow> offerFlows, boolean isOverdue,OfferRepayInfo repayInfo) {
        BigDecimal relieflAmount = new BigDecimal("0.00");
        if(isOverdue) {
            //逾期获取减免金额
            for (OfferFlow offerFlow : offerFlows) {
                if(Const.ACCOUNT_TITLE_FINE_EXP.equals(offerFlow.getAcctTitle()) && Const.ACCOUNT_GAINS.equals(offerFlow.getAccount())){
                    relieflAmount = relieflAmount.add(offerFlow.getTradeAmount());
                }
            }
//            return relieflAmount;
        }else {
            List<OfferFlow> oflows = offerFlowDao.findByTradeNoaccTitleAsc(repayInfo.getTradeNo(), new String[]{Const.ACCOUNT_TITLE_FINE_EXP});
            for (OfferFlow flow : oflows) {
                if (Const.ACCOUNT_GAINS.equals(flow.getAccount())) {
                    relieflAmount = relieflAmount.add(flow.getTradeAmount());
                }
            }
        }
        if (relieflAmount.compareTo(new BigDecimal("0.00")) == 1) {
            BigDecimal userRelieflAmount = relieflAmountMap.get(repayInfo.getTradeNo());
            if (userRelieflAmount != null) {
                relieflAmount = relieflAmount.subtract(userRelieflAmount);
            }
        }
        return relieflAmount;
    }

/*    @Override
    public BigDecimal getRecordAmount(List<OfferFlow> offerFlows) {
        BigDecimal recordAmount = new BigDecimal("0.00");
        for(OfferFlow offerFlow:offerFlows) {
            if (Const.ACCOUNT_TITLE_AMOUNT.equals(offerFlow.getAcctTitle()) && "D".equals(offerFlow.getAppoDorc()) && "D".equals(offerFlow.getDorc())) {
                recordAmount = recordAmount.add(offerFlow.getTradeAmount());
            }
        }
        return recordAmount;
    }*/

/*    @Override
    public BigDecimal getCancelAmount(List<OfferFlow> offerFlows) {
        BigDecimal recordAmount = new BigDecimal("0.00");
        for(OfferFlow offerFlow:offerFlows) {
            if (Const.ACCOUNT_TITLE_AMOUNT.equals(offerFlow.getAcctTitle()) && "C".equals(offerFlow.getAppoDorc()) && "C".equals(offerFlow.getDorc())) {
                recordAmount = recordAmount.add(offerFlow.getTradeAmount());
            }
        }
        return recordAmount;
    }*/

/*    @Override
    public Map<String,Object>  getPenaltyAmount(List<OfferFlow> offerFlows,OfferRepayInfo repayInfo) {
        BigDecimal penaltyAmount = new BigDecimal("0.00");
        Map<String,Object> returnMap = new HashMap<>();
        for(OfferFlow offerFlow:offerFlows) {
            if (Const.ACCOUNT_TITLE_PENALTY_EXP.equals(offerFlow.getAcctTitle())) {
                penaltyAmount = penaltyAmount.add(offerFlow.getTradeAmount());
                returnMap.put("penaltyAmount",penaltyAmount);
                returnMap.put("currentTerm",offerFlow.getMemo2());
                return returnMap;
            }
        }
        VLoanInfo vLoanInfo = vLoanInfoServiceImpl.findByLoanId(repayInfo.getLoanId());
        Date CurrTermReturnDate= afterLoanServiceImpl.getCurrTermReturnDate(repayInfo.getTradeDate(), vLoanInfo.getPromiseReturnDate());
        Map<String,Object> map = new HashMap<>();
        map.put("CurrTermReturnDate", CurrTermReturnDate);
        map.put("loanId", repayInfo.getLoanId());
        List<LoanRepaymentDetail> list = loanRepaymentDetailDao.findTrustRepaymentState(map);
        if(CollectionUtils.isEmpty(list)){
            //已经到最后一期 一次性结清 违约金取0
            returnMap.put("penaltyAmount",penaltyAmount);
            returnMap.put("currentTerm",0);
            return returnMap;
        }
        penaltyAmount = list.get(0).getPenalty();
        returnMap.put("penaltyAmount",penaltyAmount);
        returnMap.put("currentTerm",list.get(0).getCurrentTerm());
        return returnMap;
    }*/

    @Override
    public BigDecimal getAcctTitleAmount(List<TrustOfferFlow> trustOfferFlows, List<String> accTitles) {
        BigDecimal amount = new BigDecimal("0.00");
        if (CollectionUtils.isEmpty(trustOfferFlows) || CollectionUtils.isEmpty(accTitles)) {
            return amount;
        }
        for (TrustOfferFlow trustOfferFlow:trustOfferFlows) {
            if (accTitles.contains(trustOfferFlow.getAcctTitle()) && trustOfferFlow.getTradeAmount() != null) {
                amount = amount.add(trustOfferFlow.getTradeAmount());
            }
        }
        return amount;
    }

    /**
     * 记账流水对象
     * @param currenTerm
     * @param acctTitle
     * @param tradeAmount
     * @param memo
     * @return
     */
    public TrustOfferFlow createTrustOfferFlow(Long currenTerm,String acctTitle,BigDecimal tradeAmount,String memo,OfferRepayInfo repayInfo){
        String tradeType = TrustOfferEnum.正常还款.getTradeType();
        if(repayInfo.isOverdue()){
            tradeType = TrustOfferEnum.逾期还款.getTradeType();
            if (!this.isOverdue(repayInfo,currenTerm)) {
                if (Const.TRADE_CODE_ONEOFF.equals(repayInfo.getTradeCode())) {
                    tradeType = TrustOfferEnum.一次结清.getTradeType();
                }
                tradeType = TrustOfferEnum.正常还款.getTradeType();
            }
        }
        else if(!repayInfo.isOverdue() && Const.TRADE_CODE_ONEOFF.equals(repayInfo.getTradeCode())){
            tradeType = TrustOfferEnum.一次结清.getTradeType();
        }
        if (TrustOfferEnum.ACCOUNT_TITLE_INTEREST_EXP_A.getSubjectCode().equals(acctTitle) || TrustOfferEnum.ACCOUNT_TITLE_FINE_EXP_A.getSubjectCode().equals(acctTitle) ||
                TrustOfferEnum.ACCOUNT_TITLE_LOAN_AMOUNT_A.getSubjectCode().equals(acctTitle) || TrustOfferEnum.ACCOUNT_TITLE_PENALTY_EXP_A.getSubjectCode().equals(acctTitle)) {
            BigDecimal reliflAmount = relieflAmountMap.get(repayInfo.getTradeNo());
            if (reliflAmount == null) {
                relieflAmountMap.put(repayInfo.getTradeNo(), tradeAmount);
            } else {
                reliflAmount = reliflAmount.add(tradeAmount);
                relieflAmountMap.put(repayInfo.getTradeNo(),reliflAmount);
            }
        }
        TrustOfferFlow trustOfferFlow = new TrustOfferFlow();
        trustOfferFlow.setId(sequencesServiceImpl.getSequences(SequencesEnum.TRUST_OFFER_FLOW));
        trustOfferFlow.setLoanId(repayInfo.getLoanId());
        trustOfferFlow.setTradeDate(repayInfo.getTradeDate());
        trustOfferFlow.setCurrentTerm(currenTerm);
        trustOfferFlow.setAcctTitle(acctTitle);
        trustOfferFlow.setTradeCode(repayInfo.getTradeCode());
        trustOfferFlow.setTradeNo(repayInfo.getTradeNo());
        trustOfferFlow.setTradeMode(repayInfo.getTradeType());
        trustOfferFlow.setTradeType(tradeType);
        trustOfferFlow.setTradeAmount(tradeAmount);
        trustOfferFlow.setMemo(memo);
        trustOfferFlow.setUpdateTime(new Date());
        logger.info("交易流水号{}生成记账流水单：{}",repayInfo.getTradeNo(), JSONUtil.toJSON(trustOfferFlow));
        return  trustOfferFlow;
    }

    /**
     * 是否逾期 true 逾 false 没有
     * @param repayInfo
     * @param currenTerm
     * @return
     */
    public boolean isOverdue(OfferRepayInfo repayInfo,Long currenTerm){
        LoanRepaymentDetail loanRepaymentDetail = loanRepaymentDetailDao.findByLoanIdAndCurrentTerm(repayInfo.getLoanId(),currenTerm);
        if (loanRepaymentDetail != null && Dates.compareTo(repayInfo.getTradeDate(),loanRepaymentDetail.getReturnDate()) == 1) {
            //交易时间 大于 还款日期
            return true;
        }
        return false;
    }
    /**
     * 记账
     * @param trustOfferFlow
     * @return
     */
    @Transactional
    public void accounting(TrustOfferFlow trustOfferFlow) throws Exception{
        if(trustOfferFlow.getTradeAmount().compareTo(new BigDecimal("0.00")) == 1){
            trustOfferFlowDao.insert(trustOfferFlow);
        }
    };

    /**
     * 获取开始逾期期数
     * @param offerFlows
     * @param isOverdue
     * @param repayInfo
     * @return
     */
    @Override
    public List<Long> getbeginOverdueCurrentTerm(List<OfferFlow> offerFlows, boolean isOverdue, OfferRepayInfo repayInfo) {
        List<Long>  currenTerms = new ArrayList<>() ;
        Long currentTerm = 0L;
        for(OfferFlow offerFlow:offerFlows){
            if (!Const.ACCOUNT_GAINS.equals(offerFlow.getAccount())) {
                if (Strings.isNotEmpty(offerFlow.getMemo2()) && !currentTerm.equals(Long.valueOf(offerFlow.getMemo2()))) {
                    LoanRepaymentDetail loanRepaymentDetail = loanRepaymentDetailDao.findByLoanIdAndCurrentTerm(offerFlow.getLoanId(), Long.valueOf(offerFlow.getMemo2()));
                    if (loanRepaymentDetail != null && Dates.compareTo(offerFlow.getTradeDate(), loanRepaymentDetail.getReturnDate()) == 1) {
                        currentTerm = Long.valueOf(offerFlow.getMemo2());
                        currenTerms.add(currentTerm);
                    }
                }
            }
        }
        return currenTerms;
    }

    /**
     * 获取当前期数
     * @param offerFlows
     * @param repayInfo
     * @return
     */
    @Override
    public Long getCurrentTerm(List<OfferFlow> offerFlows, OfferRepayInfo repayInfo) {
        Long currentTerm = 0L;
        Long tempCurrentTerm = 0L;
        for(OfferFlow offerFlow:offerFlows){
            if (!Const.ACCOUNT_GAINS.equals(offerFlow.getAccount())){
                tempCurrentTerm = Long.valueOf(offerFlow.getMemo2());
                if (tempCurrentTerm > currentTerm) {
                    currentTerm = tempCurrentTerm;
                }
            }
        }
        return currentTerm;
    }

    @Override
    public Long getCurrentTerm(Long loanId) {
        //获取已经在trust_offer_flow 表中分账的最大期数
        Long maxTerm = trustOfferFlowDao.getAlreadySplitAccountingMaxTerm(loanId);
        if (this.isCurrentTermSettle(loanId,maxTerm)) {
            //最大期数是否结清
            maxTerm = maxTerm + 1;
            return maxTerm;
        }
        return maxTerm;
    }

    @Override
    public List<Long> getTerm(List<TrustOfferFlow> trustOfferFlows) {
        List<Long>  terms = new ArrayList<>() ;
        Long term = 0L;
        for(TrustOfferFlow trustOfferFlow:trustOfferFlows){
                if (Strings.isNotEmpty(trustOfferFlow.getCurrentTerm()) && !term.equals(Long.valueOf(trustOfferFlow.getCurrentTerm()))) {
                    term = Long.valueOf(trustOfferFlow.getCurrentTerm());
                    terms.add(term);
                }
        }
        return terms;
    }

    @Override
    public List<OfferFlow> getRecordAccounts(String tradeNo) {
        return offerFlowDao.findRecordAccountsFlowsBytradeNo(tradeNo);
    }

/*    @Override
    public List<OfferFlow> getCancelAccounts(String tradeNo) {
        return offerFlowDao.findCancelAccountsFlowsBytradeNo(tradeNo);
    }*/

    /**
     * 分账记账
     * @param reliefAmount  减免金额
     * @param fineInterestAmount 罚息金额
     * @param penaltyAmount 违约金额
     * @param principalAmount 本金金额
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    private void splitAccounting(BigDecimal reliefAmount,BigDecimal fineInterestAmount,BigDecimal penaltyAmount,BigDecimal principalAmount,BigDecimal interestAmount,LoanRepaymentDetail repaymentDetail,OfferRepayInfo repayInfo){
        BigDecimal amount = repayInfo.getAmount();
        BigDecimal tempAmount = new BigDecimal("0.00");
        if (repayInfo.getAmount().compareTo(new BigDecimal("0.00")) !=1) {
            return;
        }
       //减免金额 按照 罚违利本 计算 减免金额 = 减免罚息 + 减免违约金 + 减免利息 +减免本金
        //减免罚息
        if(fineInterestAmount.compareTo(new BigDecimal("0.00")) ==1 ){
            //存在罚息
            if(fineInterestAmount.compareTo(reliefAmount)==1){

                //减免不够交罚息
                    try{
                    accounting(createTrustOfferFlow(repaymentDetail.getCurrentTerm(), TrustOfferEnum.ACCOUNT_TITLE_FINE_EXP_A.getSubjectCode(), reliefAmount, "", repayInfo)) ;
                    }catch (Exception e) {
                        e.printStackTrace();
                        throw new PlatformException("减免罚息记账流水处理失败！交易失败！");
                    }
                 tempAmount = fineInterestAmount.subtract(reliefAmount);
                //减免全交罚息了 所以没有减免金额了 清零
                reliefAmount = new BigDecimal("0.00");
                if(amount.compareTo(tempAmount)==1) {
                    //充值金额比实扣罚息多 充值金额足够交罚息
                    try{
                        accounting(createTrustOfferFlow(repaymentDetail.getCurrentTerm(), TrustOfferEnum.ACCOUNT_TITLE_FINE_EXP.getSubjectCode(), tempAmount, "", repayInfo));
                        amount = amount.subtract(tempAmount);
                    }catch(Exception e){
                        e.printStackTrace();
                        throw new PlatformException("实扣罚息记账流水处理失败！交易失败！");
                    }
                }
                 else{
                    //交易充值金额不过交罚息或刚刚够
                    try {
                        accounting(createTrustOfferFlow(repaymentDetail.getCurrentTerm(), TrustOfferEnum.ACCOUNT_TITLE_FINE_EXP.getSubjectCode(), amount, "", repayInfo));
                        repayInfo.setAmount(new BigDecimal("0.00"));
                        return ;
                    }catch (Exception e){
                        e.printStackTrace();
                         throw new PlatformException("实扣罚息记账流水处理失败！交易失败！");
                    }
              }
          }
            else{
                //不生成实扣罚息记账流水 减免够多
                 try{
                    accounting(createTrustOfferFlow(repaymentDetail.getCurrentTerm(), TrustOfferEnum.ACCOUNT_TITLE_FINE_EXP_A.getSubjectCode(),fineInterestAmount,"",repayInfo));
                     //交了罚息还是减免金额
                     reliefAmount = reliefAmount.subtract(fineInterestAmount);
                 }catch (Exception e){
                     e.printStackTrace();
                     throw new PlatformException("减免罚息记账流水处理失败！交易失败！");
                 }
             }
        }
        //减免违约金
        if (penaltyAmount.compareTo(new BigDecimal("0.00")) ==1 ) {
            //存在违约金
            if ( penaltyAmount.compareTo(reliefAmount) == 1) {
                //减免不够交违约金
                try {
                    accounting(createTrustOfferFlow(repaymentDetail.getCurrentTerm(), TrustOfferEnum.ACCOUNT_TITLE_PENALTY_EXP_A.getSubjectCode(), reliefAmount, "", repayInfo));
                }catch (Exception e){
                        e.printStackTrace();
                        throw new PlatformException("减免违约金记账流水处理失败！交易失败！");
                    }
                tempAmount = penaltyAmount.subtract(reliefAmount);
                //减免金额全交违约金了 减免金额清零
                reliefAmount = new BigDecimal("0.00");
                if (amount.compareTo(tempAmount) == 1) {
                    //充值金额比实扣违约金多
                    try {
                        accounting(createTrustOfferFlow(repaymentDetail.getCurrentTerm(), TrustOfferEnum.ACCOUNT_TITLE_PENALTY_EXP.getSubjectCode(), tempAmount, "", repayInfo));
                        amount = amount.subtract(tempAmount);
                    }catch (Exception e){
                        e.printStackTrace();
                        throw new PlatformException("实扣违约金记账流水处理失败！交易失败！");
                    }
                } else {
                    //充值金额不够或只够还违约金
                    try {
                        accounting(createTrustOfferFlow(repaymentDetail.getCurrentTerm(), TrustOfferEnum.ACCOUNT_TITLE_PENALTY_EXP.getSubjectCode(), amount, "", repayInfo));
                        repayInfo.setAmount(new BigDecimal("0.00"));
                        return ;
                    }catch (Exception e){
                        e.printStackTrace();
                        throw new PlatformException("实扣违约金记账流水处理失败！交易失败！");
                    }
                }

            }
            else{
                //减免金 够交违约金
                try{
                    accounting(createTrustOfferFlow(repaymentDetail.getCurrentTerm(), TrustOfferEnum.ACCOUNT_TITLE_PENALTY_EXP_A.getSubjectCode(), penaltyAmount, "", repayInfo));
                    //交完违约金 还剩减免金额
                    reliefAmount = reliefAmount.subtract(penaltyAmount);
                }catch (Exception e){
                    throw new PlatformException("减免违约金记账流水处理失败！交易失败！");
                }
            }
        }

        //利息;
        if (interestAmount.compareTo(new BigDecimal("0.00")) == 1){
            //存在利息
            if(interestAmount.compareTo(reliefAmount) ==1){
                //减免不够利息
                try{
                    accounting(createTrustOfferFlow(repaymentDetail.getCurrentTerm(), TrustOfferEnum.ACCOUNT_TITLE_INTEREST_EXP_A.getSubjectCode(), reliefAmount, "", repayInfo));
                }catch (Exception e){
                    e.printStackTrace();
                    throw new PlatformException("减免利息记账流水处理失败！交易失败！");
                }
                tempAmount = interestAmount.subtract(reliefAmount);
                //减免金额全交利息 减免金额清零
                reliefAmount = new BigDecimal("0.00");
                if (amount.compareTo(tempAmount) == 1) {
                    //充值金额比实扣利息金多
                    try {
                        accounting(createTrustOfferFlow(repaymentDetail.getCurrentTerm(), TrustOfferEnum.ACCOUNT_TITLE_INTEREST_EXP.getSubjectCode(), tempAmount, "", repayInfo));
                        amount = amount.subtract(tempAmount);
                    }catch (Exception e){
                        e.printStackTrace();
                        throw new PlatformException("实扣利息记账流水处理失败！交易失败！");
                    }
                } else {
                    //充值金额不够或只够还利息
                    try {
                        accounting(createTrustOfferFlow(repaymentDetail.getCurrentTerm(), TrustOfferEnum.ACCOUNT_TITLE_INTEREST_EXP.getSubjectCode(), amount, "", repayInfo));
                        repayInfo.setAmount(new BigDecimal("0.00"));
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new PlatformException("实扣违约金记账流水处理失败！交易失败！");
                    }
                }
            }
            else{
                //减免金额 够还利息
                try{
                    accounting(createTrustOfferFlow(repaymentDetail.getCurrentTerm(), TrustOfferEnum.ACCOUNT_TITLE_INTEREST_EXP_A.getSubjectCode(), interestAmount, "", repayInfo));
                    //交完利息 还剩减免金额
                    reliefAmount = reliefAmount.subtract(interestAmount);
                }catch (Exception e){
                    throw new PlatformException("减免利息记账流水处理失败！交易失败！");
                }
            }
        }

        //本金
        if(principalAmount.compareTo(new BigDecimal("0.00")) ==1){
            //存在本金
            if(principalAmount.compareTo(reliefAmount) ==1){
                //减免不够本金
                try{
                    accounting(createTrustOfferFlow(repaymentDetail.getCurrentTerm(), TrustOfferEnum.ACCOUNT_TITLE_LOAN_AMOUNT_A.getSubjectCode(), reliefAmount, "", repayInfo));
                }catch (Exception e){
                    e.printStackTrace();
                    throw new PlatformException("减免利息记账流水处理失败！交易失败！");
                }
                tempAmount = principalAmount.subtract(reliefAmount);
                //减免金额全交本金 减免金额清零
                reliefAmount = new BigDecimal("0.00");
                if (amount.compareTo(tempAmount) == 1) {
                    //充值金额比实扣本金多
                    try {
                            accounting(createTrustOfferFlow(repaymentDetail.getCurrentTerm(), TrustOfferEnum.ACCOUNT_TITLE_LOAN_AMOUNT.getSubjectCode(), tempAmount, "", repayInfo));
                            amount = amount.subtract(tempAmount);
                    }catch (Exception e){
                        e.printStackTrace();
                        throw new PlatformException("实扣利息记账流水处理失败！交易失败！");
                    }
                } else {
                    //充值金额不够或只够还本金
                    try {
                        accounting(createTrustOfferFlow(repaymentDetail.getCurrentTerm(), TrustOfferEnum.ACCOUNT_TITLE_LOAN_AMOUNT.getSubjectCode(), amount, "", repayInfo));
                        repayInfo.setAmount(new BigDecimal("0.00"));
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new PlatformException("实扣违约金记账流水处理失败！交易失败！");
                    }
                }
            }
            else{
                //减免金额 够还本金
                try{
                    accounting(createTrustOfferFlow(repaymentDetail.getCurrentTerm(), TrustOfferEnum.ACCOUNT_TITLE_LOAN_AMOUNT_A.getSubjectCode(), principalAmount, "", repayInfo));
                    //减免金额全交本金 还剩减免金额
                    reliefAmount = reliefAmount.subtract(principalAmount);
                }catch (Exception e){
                    throw new PlatformException("减免利息记账流水处理失败！交易失败！");
                }
            }
        }
        repayInfo.setAmount(amount);
    }

    /**
     * 逾期拆分记账
     * @param offerFlows
     * @param  repayInfo
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void overdueSplitAccounting(List<OfferFlow> offerFlows,OfferRepayInfo repayInfo) {
        logger.info("逾期拆分tradeNo：{}",repayInfo.getTradeNo());
        //减免金额
        BigDecimal reliefAmount = this.getReliefAmount(offerFlows,true,repayInfo);
        //违约金
        BigDecimal penaltyAmount = new BigDecimal("0.00");
        //获取当前开始逾期期数
        List<Long> currentTerms = this.getbeginOverdueCurrentTerm(offerFlows,true,repayInfo);
        if (CollectionUtils.isEmpty(currentTerms)) {
                //正常还款 减免
            VLoanInfo vLoanInfo = vLoanInfoServiceImpl.findByLoanId(repayInfo.getLoanId());
            repayInfo.setIsOverdue(false);
            List<LoanRepaymentDetail> repaymentDetails = this.getNormalNotRepayDetail(repayInfo.getTradeDate(), repayInfo.getLoanId(), vLoanInfo.getPromiseReturnDate());
            if (Const.TRADE_CODE_ONEOFF.equals(repayInfo.getTradeCode())) {
                this.onetimeSlitAccounting(repayInfo, repaymentDetails);
                return;
            } else if (Const.TRADE_CODE_NORMAL.equals(repayInfo.getTradeCode())){
                this.normalSplitAccounting(repayInfo, repaymentDetails);
                return;
            }
            return;
        }
        //应付罚息
        BigDecimal fineInterestAmount = offerFlowDao.getMoreTermFineAmount(repayInfo.getLoanId(), currentTerms);
        //获取还款计划记录
        List<LoanRepaymentDetail> repaymentDetails = loanRepaymentDetailDao.findEqualCurrentTerm(repayInfo.getLoanId(), currentTerms);
        if (CollectionUtils.isEmpty(repaymentDetails)) {
            logger.info("债权{}逾期拆分找不到逾期{}期的还款计划！",repayInfo.getLoanId(),JSONUtil.toJSON(currentTerms));
            return ;
        }
        //逾期拆分 跟正常拆分
        //利息
        BigDecimal interestAmount = repaymentDetails.get(0).getCurrentAccrual();
        //本金
        BigDecimal principalAmount =  repaymentDetails.get(0).getReturneterm().subtract(interestAmount);
        //针对上期挂账 已经在新trustOfferFlow 中记录下分账流水的需要判断是否在上期挂账中 结算清楚
        //当期已经分账金额
        SubjectAmount subjectAmount = new SubjectAmount();
        subjectAmount.setInterestAmount(interestAmount);
        subjectAmount.setPenaltyAmount(penaltyAmount);
        subjectAmount.setPrincipalAmount(principalAmount);
        this.statisticsCurrentAccTitleDeficitAmount(subjectAmount,repayInfo.getLoanId(),currentTerms.get(0));
        interestAmount = subjectAmount.getInterestAmount();
        penaltyAmount = subjectAmount.getPenaltyAmount();
        principalAmount = subjectAmount.getPrincipalAmount();
        //实际罚息 =  应付罚息 -已付罚息
        fineInterestAmount = fineInterestAmount.subtract(this.getAlreaDyFineInterest(repayInfo.getLoanId(),currentTerms.get(0)));
        //当前逾期期数的拆分记账
        this.splitAccounting(reliefAmount,fineInterestAmount,penaltyAmount,principalAmount,interestAmount,repaymentDetails.get(0),repayInfo);
        Long currentTerm = repaymentDetails.get(repaymentDetails.size()-1).getCurrentTerm();
        if(new BigDecimal("0.00").compareTo(repayInfo.getAmount()) == -1){
            fineInterestAmount = new BigDecimal("0.00");
            penaltyAmount = new BigDecimal("0.00");
            repaymentDetails.remove(0);
            for(LoanRepaymentDetail repaymentDetail:repaymentDetails){
                reliefAmount = this.getReliefAmount(offerFlows,true,repayInfo);
                interestAmount = repaymentDetail.getCurrentAccrual();
                principalAmount = repaymentDetail.getReturneterm().subtract(interestAmount);
                this.splitAccounting(reliefAmount,fineInterestAmount,penaltyAmount,principalAmount,interestAmount,repaymentDetail,repayInfo);
                currentTerm = repaymentDetail.getCurrentTerm();
            }
        }
        //逾期挂账
        if (new BigDecimal("0.00").compareTo(repayInfo.getAmount()) == -1) {
                this.realTimeSurplusAmountSplitAccounting(repayInfo, currentTerm);
        }

    }

    /**
     * 正常拆分记账
     * @param repayInfo
     * @param repaymentDetails
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void normalSplitAccounting(OfferRepayInfo repayInfo, List<LoanRepaymentDetail> repaymentDetails) {
        if(CollectionUtils.isEmpty(repaymentDetails)){
            return;
        }
        for(LoanRepaymentDetail repaymentDetail:repaymentDetails){
            //计算当前还款的这一期是否还过部分 还过获取还过部分的钱

            //减免金额
            BigDecimal reliefAmount = this.getReliefAmount(null,false,repayInfo);
            if (repayInfo.isOverdue()) {
                //逾期调用到正常拆分说明 是剩余金额 既然完成了当前应还 那么剩余的钱肯定是正常还款状态了 不能算逾期还款状态了
                repayInfo.setIsOverdue(false);
            }
            //应付罚息  正常拆分 执行昨天的充值记录 对于逾期的 系统原有分账是实时分账的 所以正常拆分记账 没有罚息 因为原有分账已经生产了记录 直接取原有分账流水就好
            BigDecimal fineInterestAmount = new BigDecimal("0.00");
            //违约金 正常拆分 无违约金
            BigDecimal penaltyAmount = new BigDecimal("0.00");
            //利息
            BigDecimal interestAmount = repaymentDetail.getCurrentAccrual();
            //本金
            BigDecimal principalAmount = repaymentDetail.getReturneterm().subtract(interestAmount);
            //当期已经分账金额
            SubjectAmount subjectAmount = new SubjectAmount();
            subjectAmount.setInterestAmount(interestAmount);
            subjectAmount.setPenaltyAmount(penaltyAmount);
            subjectAmount.setPrincipalAmount(principalAmount);
            this.statisticsCurrentAccTitleDeficitAmount(subjectAmount,repayInfo.getLoanId(),repaymentDetail.getCurrentTerm());
            interestAmount = subjectAmount.getInterestAmount();
            penaltyAmount = subjectAmount.getPenaltyAmount();
            principalAmount = subjectAmount.getPrincipalAmount();
            if(new BigDecimal("0.00").compareTo(repayInfo.getAmount()) != -1){
                return ;
            }
            this.splitAccounting(reliefAmount,fineInterestAmount,penaltyAmount,principalAmount,interestAmount,repaymentDetail,repayInfo);
        }
        //其他费用
/*        BigDecimal otherAmount = repayInfo.getAmount();
        if(otherAmount.compareTo(new BigDecimal("0.00")) ==1){
            try{
                accounting(createTrustOfferFlow(repaymentDetails.get(repaymentDetails.size()-1).getCurrentTerm(),TrustOfferEnum.ACCOUNT_TITLE_OTHER_RECEI.getSubjectCode(),otherAmount,"",repayInfo));
            }catch (Exception e){
                e.printStackTrace();
                throw new PlatformException("正常结清生成其他费用失败！，交易未完成");
            }
        }*/
        this.otherAccttleAccounting(repaymentDetails.get(repaymentDetails.size()-1).getCurrentTerm(),repayInfo);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void onetimeSlitAccounting(OfferRepayInfo repayInfo,List<LoanRepaymentDetail> repaymentDetails) {
        if(CollectionUtils.isEmpty(repaymentDetails)){
            logger.info("一次性结算没有未还款的计划！");
            return;
        }
        //减免金额
        BigDecimal reliefAmount = this.getReliefAmount(null,false,repayInfo);
        //应付罚息
        BigDecimal fineInterestAmount = new BigDecimal("0.00");
        //违约金
        BigDecimal penaltyAmount =  repaymentDetails.get(0).getPenalty();
        //利息
        BigDecimal interestAmount = repaymentDetails.get(0).getCurrentAccrual();
        //一次性结清本金
        BigDecimal principalAmount = repaymentDetails.get(0).getRepaymentAll().subtract(penaltyAmount).subtract(interestAmount);
        //本期一次性结清总金额
        BigDecimal oneTimeTotalAmont =  repaymentDetails.get(0).getRepaymentAll();

        if (repayInfo.isOverdue()){
            //逾期调用到正常一次性拆分说明 是剩余金额 既然完成了当前应还 那么剩余的钱肯定是正常一次还款状态了 不能算逾期还款状态了
            repayInfo.setIsOverdue(false);
        }
        //是否在上一次提前结清中还了部分违约金
        if (this.isRepaymentPenalty(repayInfo.getLoanId())) {
            this.oneTimepenaltyAccttleAccounting(repaymentDetails.get(0).getCurrentTerm(),repayInfo);
            return;
        }
        //一次性结清已还金额
        SubjectAmount subjectAmount = new SubjectAmount();
        subjectAmount.setInterestAmount(interestAmount);
        subjectAmount.setPenaltyAmount(penaltyAmount);
        subjectAmount.setPrincipalAmount(principalAmount);
        this.statisticsCurrentAccTitleDeficitAmount(subjectAmount,repayInfo.getLoanId(),repaymentDetails.get(0).getCurrentTerm());
        interestAmount = subjectAmount.getInterestAmount();
        penaltyAmount = subjectAmount.getPenaltyAmount();
        principalAmount = subjectAmount.getPrincipalAmount();
        BigDecimal otherAmount = new BigDecimal("0.00");

        //当期已还总金额（实时分账---新分账）的
        BigDecimal currenTermRepayTotalAmont = subjectAmount.getRepayTotalAmount();
        //非当前实时分账（新分账）的一次性结清已分总金额 已经 期数
        Map<String,Object> excludeCurrenTermMap = this.realAccounOneTimeExcludeCurrenTermToTal(repayInfo.getLoanId(), repaymentDetails.get(0).getCurrentTerm());
        //非当前实时分账（新分账）的一次性结清已分总金额
        BigDecimal excludeCurrenTermToTalAmount = (BigDecimal) excludeCurrenTermMap.get("excludeCurrenTermToTal");
        //一次性结清的非当前 期数（当前存在过数期一次性分账申请）
        List<Long> excludeCurrenTerms = (List<Long>) excludeCurrenTermMap.get("excludeCurrenTerms");
        //旧分账已还款的总金额
        BigDecimal repayTotalAmont =new BigDecimal("0.00");
        if (CollectionUtils.isNotEmpty(excludeCurrenTerms)) {
            repayTotalAmont = this.getRepayTotalAmount(repayInfo.getLoanId(),excludeCurrenTerms);
        }
        //多余的 = 当前已还的所有金额- 时间所还的金额
        BigDecimal moreAmount =excludeCurrenTermToTalAmount.subtract(repayTotalAmont);

        principalAmount = principalAmount.subtract(moreAmount);
        boolean isEnoughOneTimeSettle = true;
        if (repayInfo.getAmount().add(reliefAmount).add(currenTermRepayTotalAmont).add(moreAmount).compareTo(oneTimeTotalAmont) == -1) {
            penaltyAmount = new BigDecimal("0.00");
            isEnoughOneTimeSettle = false;
        }
        this.splitAccounting(reliefAmount, fineInterestAmount, penaltyAmount, principalAmount, interestAmount, repaymentDetails.get(0), repayInfo);
        /* otherAmount =repayInfo.getAmount();
        if (otherAmount.compareTo(new BigDecimal("0.00")) == 1) {
            //一次性结清有剩余
            //其他费用
            try{
                accounting(createTrustOfferFlow(repaymentDetails.get(0).getCurrentTerm(),TrustOfferEnum.ACCOUNT_TITLE_OTHER_RECEI.getSubjectCode(),otherAmount,"",repayInfo));
            }catch (Exception e){
                e.printStackTrace();
                throw new PlatformException("一次性结清生成其他费用失败！，交易未完成");
            }
        }*/
        if (!isEnoughOneTimeSettle) {
            this.oneTimepenaltyAccttleAccounting(repaymentDetails.get(0).getCurrentTerm(),repayInfo);
            return;
        }
        this.otherAccttleAccounting(repaymentDetails.get(0).getCurrentTerm(),repayInfo);
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void copyOfferFlowAccounting(List<OfferFlow> offerFlows, OfferRepayInfo repayInfo) {
        //入库的金额
        BigDecimal amount = new BigDecimal("0.00");
        for(OfferFlow offerFlow:offerFlows) {
            if (!repayInfo.isOverdue()) {
                this.setIsOverdue(offerFlow,repayInfo);
            }
            amount = amount.add(offerFlow.getTradeAmount());
            if (Const.ACCOUNT_TITLE_FINE_EXP.equals(offerFlow.getAcctTitle())) {
                //实扣罚息支出
                try {
                    Long currentTerm = Long.valueOf(offerFlow.getMemo2());
                    BigDecimal fineAmount = offerFlow.getTradeAmount().subtract(this.getAlreaDyFineInterest(repayInfo.getLoanId(),currentTerm));
                    accounting(createTrustOfferFlow(currentTerm, TrustOfferEnum.ACCOUNT_TITLE_FINE_EXP.getSubjectCode(),fineAmount, "", repayInfo));
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new PlatformException("生成实扣罚息流水失败！交易未完成！");
                }
            }
            if (Const.ACCOUNT_TITLE_INTEREST_EXP.equals(offerFlow.getAcctTitle())) {
                //实扣利息支出
                try {
                    accounting(createTrustOfferFlow(Long.valueOf(offerFlow.getMemo2()), TrustOfferEnum.ACCOUNT_TITLE_INTEREST_EXP.getSubjectCode(), offerFlow.getTradeAmount(), "",repayInfo));
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new PlatformException("生成实扣利息流水失败！交易未完成！");
                }
            }
            if (Const.ACCOUNT_TITLE_LOAN_AMOUNT.equals(offerFlow.getAcctTitle())) {
                //实扣本金支出
                try {
                    accounting(createTrustOfferFlow(Long.valueOf(offerFlow.getMemo2()), TrustOfferEnum.ACCOUNT_TITLE_LOAN_AMOUNT.getSubjectCode(), offerFlow.getTradeAmount(), "", repayInfo));
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new PlatformException("生成实扣本金流水失败！交易未完成！");
                }
            }
            if (Const.ACCOUNT_TITLE_PENALTY_EXP.equals(offerFlow.getAcctTitle())) {
                //实扣违约金支出
                try {
                    accounting(createTrustOfferFlow(Long.valueOf(offerFlow.getMemo2()), TrustOfferEnum.ACCOUNT_TITLE_PENALTY_EXP.getSubjectCode(), offerFlow.getTradeAmount(), "", repayInfo));
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new PlatformException("生成实扣违约金流水失败！交易未完成！");
                }
            }
        }
        //入库之后还剩金额 一般为0
        repayInfo.setAmount(repayInfo.getAmount().subtract(amount));
    }

    @Override
    public boolean isNotEnoughFineInterest(OfferRepayInfo repayInfo,VLoanInfo vLoanInfo) {
        //当前期数
        Long currenTerm = this.getCurrentTerm(repayInfo.getLoanId());
        LoanRepaymentDetail repaymentDetail = loanRepaymentDetailDao.findByLoanIdAndCurrentTerm(repayInfo.getLoanId(),currenTerm);
        int overdueDay = 0;
        if (repaymentDetail != null) {
            //逾期天数
            overdueDay =  this.getOverdueDay(repayInfo.getTradeDate(),repaymentDetail.getReturnDate());
        }
        String[] principaAccttiles ={TrustOfferEnum.ACCOUNT_TITLE_LOAN_AMOUNT.getSubjectCode(),TrustOfferEnum.ACCOUNT_TITLE_LOAN_AMOUNT_A.getSubjectCode()};
        //已还本金
        BigDecimal alreadyPrincipalAmount = trustOfferFlowDao.getSplitAccountingAmountByAccttitle(repayInfo.getLoanId(),principaAccttiles,null);
        //剩余本金
        BigDecimal balancePrincipalAmount =  vLoanInfo.getPactMoney().subtract(alreadyPrincipalAmount);
        //已还罚息
        BigDecimal alreadyFineAmount = this.getAlreaDyFineInterest(repayInfo.getLoanId(),currenTerm);
        BigDecimal fineAmount = balancePrincipalAmount.multiply(new BigDecimal(overdueDay)).multiply(vLoanInfo.getPenaltyRate()).subtract(alreadyFineAmount);
        if (repayInfo.getAmount().compareTo(fineAmount) == -1) {
            return  true;
        }
        return false;
    }

    @Override
    public List<LoanRepaymentDetail> getNormalNotRepayDetail(Date currDate,Long loanId,Long promiseReturnDate) {
        Map<String,Object> map = new HashMap<String, Object>();
        Date CurrTermReturnDate= afterLoanServiceImpl.getCurrTermReturnDate(currDate,promiseReturnDate);
        map.put("CurrTermReturnDate", CurrTermReturnDate);
        map.put("loanId", loanId);
        map.put("repaymentStates", new String[]{RepaymentStateEnum.结清.name(),RepaymentStateEnum.不足额还款.name(),RepaymentStateEnum.未还款.name()});
        List<LoanRepaymentDetail> list = loanRepaymentDetailDao.findTrustRepaymentState(map);
        return list;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void trustRepayService(OfferRepayInfo repayInfo) {
        logger.info("开始对还款记录:{}",JSONUtil.toJSON(repayInfo));
        String tradeNo = repayInfo.getTradeNo();
        List<OfferFlow> offerFlows = this.findByTradeNo(tradeNo);
        if(CollectionUtils.isNotEmpty(offerFlows)){
            //处理历史还款记账流水  包括（历史正常，历史逾期，历史一次性结算） 跟 实时逾期 实时逾期一次性结算
            logger.debug("还款交易流水:{}在OfferFlow有{}条分账流水记录",repayInfo.getTradeNo(),offerFlows.size());
            Boolean isOverdue = this.isOverdue(offerFlows);
            if(isOverdue){
                //逾期
                repayInfo.setIsOverdue(true);
                Boolean isRelief =this.isRelief(offerFlows);
                if(isRelief){
                    repayInfo.setIsRelief(isRelief);
                    //逾期而且减免 对在OfferFlow中的分账流水 以更小的颗粒度拆分
                    this.overdueSplitAccounting(offerFlows, repayInfo);
                    return;
                }
                //逾期无减免的不需要重新拆分 copy原有分账流水就好
                this.historyAadtaskReminderOfferFlowsExcute(offerFlows, repayInfo);
                return;
            }
            this.historyAadtaskReminderOfferFlowsExcute(offerFlows, repayInfo);
            return;
        }

        VLoanInfo vLoanInfo = vLoanInfoServiceImpl.findByLoanId(repayInfo.getLoanId());
        //不足罚息 不记账
        if( !Const.TRADE_CODE_ONEOFF.equals(repayInfo.getTradeCode()) && this.isNotEnoughFineInterest(repayInfo,vLoanInfo)){
            logger.info("交易流水号：{}不足罚息不记账！",tradeNo);
            this.notEnoughFineInterestAccounting(repayInfo);
            return;
        }
        //非历史数据 非逾期 正常还款记账
        //正常还款
        if(Const.TRADE_CODE_NORMAL.equals(repayInfo.getTradeCode())){
            List<LoanRepaymentDetail> repaymentDetails = this.getNormalNotRepayDetail(repayInfo.getTradeDate(), repayInfo.getLoanId(),vLoanInfo.getPromiseReturnDate());
            this.normalSplitAccounting(repayInfo, repaymentDetails);
            return;
        }
        //一次性结算
        if (Const.TRADE_CODE_ONEOFF.equals(repayInfo.getTradeCode())) {
//            List<LoanRepaymentDetail> repaymentDetails = this.getNormalNotRepayDetail(repayInfo.getTradeDate(), repayInfo.getLoanId(),vLoanInfo.getPromiseReturnDate());
            List<LoanRepaymentDetail> repaymentDetails = this.getOnetimeRepayDetail(repayInfo.getTradeDate(), repayInfo.getLoanId());
            this.onetimeSlitAccounting(repayInfo,repaymentDetails);
            return;
        }
    }

  /*  @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void recordAccountsSplitAccounting(List<OfferFlow> offerFlows, OfferRepayInfo repayInfo) {
        VLoanInfo vLoanInfo = vLoanInfoServiceImpl.findByLoanId(repayInfo.getLoanId());
        Date CurrTermReturnDate= afterLoanServiceImpl.getCurrTermReturnDate(repayInfo.getTradeDate(), vLoanInfo.getPromiseReturnDate());
        List<OfferFlow> raFlows= offerFlowDao.findRecordAccountsFlows(repayInfo.getLoanId(),CurrTermReturnDate);
        //存在历史数据copy
        if(CollectionUtils.isNotEmpty(raFlows)){
            Long currentTerm = this.getCurrentTerm(raFlows,repayInfo);
            BigDecimal curAccountAmount = trustOfferFlowDao.getCurrentTermSplitAccountingAmount(repayInfo.getLoanId(),currentTerm);
            if(curAccountAmount.compareTo(new BigDecimal("0.00")) ==1){
                //说明此日终分账已存在 不再重复生成
                return;
            }
            Boolean relief = this.isRelief(raFlows);
            //存在减免
            if(relief){
                this.realTimeSurplusAmountSplitAccounting(repayInfo,currentTerm);
                return;
            }
            this.copyOfferFlowAccounting(raFlows,repayInfo);
            return;
        }
        if(Const.TRADE_CODE_NORMAL.equals(repayInfo.getTradeCode())) {
            //正常拆分
            List<LoanRepaymentDetail> repaymentDetails = this.getNormalNotRepayDetail(repayInfo.getTradeDate(), repayInfo.getLoanId(), vLoanInfo.getPromiseReturnDate());
            this.normalSplitAccounting(repayInfo, repaymentDetails);
            return;
        }
        //一次性结算
        if (Const.TRADE_CODE_ONEOFF.equals(repayInfo.getTradeCode())) {
            List<LoanRepaymentDetail> repaymentDetails = this.getNormalNotRepayDetail(repayInfo.getTradeDate(), repayInfo.getLoanId(), vLoanInfo.getPromiseReturnDate());
            this.onetimeSlitAccounting(repayInfo,repaymentDetails);
            return;
        }
    }*/

   /* public void historyRecordAccountsOfferFlows(OfferRepayInfo repayInfo){
        //是否有挂账
        List<OfferFlow> recordAccountsOfferFlows = this.getRecordAccounts(repayInfo.getTradeNo());
        if (CollectionUtils.isEmpty(recordAccountsOfferFlows)) {
            return;
        }
        //有挂账 查询交易本个日终结算是否产生 利息本金的 offerFlow流水
        VLoanInfo vLoanInfo = vLoanInfoServiceImpl.findByLoanId(repayInfo.getLoanId());
        Date CurrTermReturnDate= afterLoanServiceImpl.getCurrTermReturnDate(repayInfo.getTradeDate(), vLoanInfo.getPromiseReturnDate());
        //是否找到挂账流水
        List<OfferFlow> offerFlows= offerFlowDao.findRecordAccountsFlows(repayInfo.getLoanId(),CurrTermReturnDate);
        if (CollectionUtils.isEmpty(offerFlows)) {
            if (!repayInfo.isRelief()) {
                //挂账金额
                BigDecimal recordAmount = this.getRecordAmount(recordAccountsOfferFlows);
                repayInfo.setAmount(recordAmount);
            }
            if (Const.TRADE_CODE_NORMAL.equals(repayInfo.getTradeCode())) {
                //正常拆分
                List<LoanRepaymentDetail> repaymentDetails = this.getNormalNotRepayDetail(repayInfo.getTradeDate(), repayInfo.getLoanId(), vLoanInfo.getPromiseReturnDate());
                this.normalSplitAccounting(repayInfo, repaymentDetails);
                return;
            }
            //一次性结算
            if (Const.TRADE_CODE_ONEOFF.equals(repayInfo.getTradeCode())) {
                List<LoanRepaymentDetail> repaymentDetails = this.getNormalNotRepayDetail(repayInfo.getTradeDate(), repayInfo.getLoanId(), vLoanInfo.getPromiseReturnDate());
                this.onetimeSlitAccounting(repayInfo, repaymentDetails);
                return;
            }
            return;
        }
        Long currentTerm = this.getCurrentTerm(offerFlows,repayInfo);
        BigDecimal curAccountAmount = trustOfferFlowDao.getCurrentTermSplitAccountingAmount(repayInfo.getLoanId(),currentTerm);
        if(curAccountAmount.compareTo(new BigDecimal("0.00")) ==1){
            //说明此日终分账已存在 不再重复生成
            return;
        }
        //挂账的原因 是因为 用户所还的金额 够还款 还有剩余 所以对挂账部分分账的交易状态为“正常还款”
            this.copyOfferFlowAccounting(offerFlows, repayInfo);
            if (Const.TRADE_CODE_ONEOFF.equals(repayInfo.getTradeCode())) {
                //一次性结算 有剩余的期末余额 挂账
                List<OfferFlow> taskReminderecordAccountsOfferFlows = this.getRecordAccounts(offerFlows.get(0).getTradeNo());
                if (CollectionUtils.isNotEmpty(taskReminderecordAccountsOfferFlows)) {
                    try {
                        //挂账金额
                        BigDecimal recordAmount = this.getRecordAmount(taskReminderecordAccountsOfferFlows);
                        accounting(createTrustOfferFlow(this.getCurrentTerm(offerFlows, repayInfo), TrustOfferEnum.ACCOUNT_TITLE_OTHER_RECEI.getSubjectCode(), recordAmount, "", repayInfo));
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new PlatformException("一次性结清生成其他费用失败！，交易未完成");
                    }
                }
            }
    }*/

    /**
     * 针对完成本次交易所还金额的剩余金额实时分账
     * @param repayInfo
     * @param currentTerm
     */
    public void realTimeSurplusAmountSplitAccounting(OfferRepayInfo repayInfo,Long currentTerm){
        List<LoanRepaymentDetail> repaymentDetails = loanRepaymentDetailDao.findBigCurrentTerm(repayInfo.getLoanId(),currentTerm);
        //正常拆分
        if(Const.TRADE_CODE_NORMAL.equals(repayInfo.getTradeCode())) {
            //正常拆分
            this.normalSplitAccounting(repayInfo, repaymentDetails);
            return;
        }
        //一次性结算
        if (Const.TRADE_CODE_ONEOFF.equals(repayInfo.getTradeCode())) {
            this.onetimeSlitAccounting(repayInfo,repaymentDetails);
            return;
        }
    }

    /**
     * 针对之前的历史数据进行处理 针对日终那天产生的正常还款进行处理 日终正常还款按照新想信托拆分生成流水记账
     * @param offerFlows
     * @param repayInfo
     */
    public void historyAadtaskReminderOfferFlowsExcute(List<OfferFlow> offerFlows,OfferRepayInfo repayInfo){
        if(!repayInfo.isOverdue()) {
            //获取逾期流水 OfferFlow
            List<OfferFlow> overDueOfferFlow = this.getOverDueOfferFlow(offerFlows,repayInfo);
            if (CollectionUtils.isNotEmpty(overDueOfferFlow)) {
                Boolean isRelief =this.isRelief(offerFlows);
                if(isRelief){
                    repayInfo.setIsOverdue(true);
                    repayInfo.setIsRelief(isRelief);
                    //逾期而且减免 对在OfferFlow中的分账流水 以更小的颗粒度拆分
                    this.overdueSplitAccounting(offerFlows, repayInfo);
                    return;
                }
                this.copyOfferFlowAccounting(overDueOfferFlow,repayInfo);
            }
            //正常拆分
            if (Const.TRADE_CODE_NORMAL.equals(repayInfo.getTradeCode())) {
                //正常拆分
                VLoanInfo vLoanInfo = vLoanInfoServiceImpl.findByLoanId(repayInfo.getLoanId());
                List<LoanRepaymentDetail> repaymentDetails = this.getNormalNotRepayDetail(repayInfo.getTradeDate(), repayInfo.getLoanId(), vLoanInfo.getPromiseReturnDate());
                this.normalSplitAccounting(repayInfo, repaymentDetails);
                return;
            }
            //一次性结算
            if (Const.TRADE_CODE_ONEOFF.equals(repayInfo.getTradeCode())) {
                VLoanInfo vLoanInfo = vLoanInfoServiceImpl.findByLoanId(repayInfo.getLoanId());
//                List<LoanRepaymentDetail> repaymentDetails = this.getNormalNotRepayDetail(repayInfo.getTradeDate(), repayInfo.getLoanId(), vLoanInfo.getPromiseReturnDate());
                List<LoanRepaymentDetail> repaymentDetails = this.getOnetimeRepayDetail(repayInfo.getTradeDate(), repayInfo.getLoanId());
                this.onetimeSlitAccounting(repayInfo, repaymentDetails);
                return;
            }
        }else if(repayInfo.isOverdue()){
            this.copyOfferFlowAccounting(offerFlows,repayInfo);
            //逾期 无减免 挂账 非历史数据
            //是否有挂账
            List<OfferFlow> recordAccountsOfferFlows = this.getRecordAccounts(repayInfo.getTradeNo());
            if (CollectionUtils.isEmpty(recordAccountsOfferFlows)) {
                return;
            }
            List<Long> currentTerms = this.getbeginOverdueCurrentTerm(offerFlows,true,repayInfo);
            Long currentTerm = currentTerms.size()>0?currentTerms.get(currentTerms.size()-1):0L;
            this.realTimeSurplusAmountSplitAccounting(repayInfo,currentTerm);
        }
    }

    @Override
    public void setIsOverdue(OfferFlow offerFlow, OfferRepayInfo repayInfo) {
        LoanRepaymentDetail loanRepaymentDetail = loanRepaymentDetailDao.findByLoanIdAndCurrentTerm(repayInfo.getLoanId(),Long.valueOf(offerFlow.getMemo2()));
        if (loanRepaymentDetail !=null && Dates.compareTo(repayInfo.getTradeDate(),loanRepaymentDetail.getReturnDate()) == 1) {
            repayInfo.setIsOverdue(true);
        }
    }

    @Override
    public List<OfferFlow> getOverDueOfferFlow(List<OfferFlow> offerFlows,OfferRepayInfo repayInfo) {
        List<OfferFlow> overDueOfferFlows = new ArrayList<>();
        for (OfferFlow offerFlow:offerFlows) {
            if (!Const.ACCOUNT_GAINS.equals(offerFlow.getAccount())) {
                LoanRepaymentDetail loanRepaymentDetail = loanRepaymentDetailDao.findByLoanIdAndCurrentTerm(repayInfo.getLoanId(), Long.valueOf(offerFlow.getMemo2()));
                if (loanRepaymentDetail != null && Dates.compareTo(repayInfo.getTradeDate(), loanRepaymentDetail.getReturnDate()) == 1) {
                    overDueOfferFlows.add(offerFlow);
                }
            }
        }
        return overDueOfferFlows;
    }

    @Override
    public void statisticsCurrentAccTitleDeficitAmount(SubjectAmount subjectAmount,Long loanId, Long currentTerm) {
        String [] penaltyAcctitles = {TrustOfferEnum.ACCOUNT_TITLE_PENALTY_EXP_A.getSubjectCode(),TrustOfferEnum.ACCOUNT_TITLE_PENALTY_EXP.getSubjectCode()};
        String [] interestAcctitles = {TrustOfferEnum.ACCOUNT_TITLE_INTEREST_EXP_A.getSubjectCode(),TrustOfferEnum.ACCOUNT_TITLE_INTEREST_EXP.getSubjectCode()};
        String [] principalAcctitles = {TrustOfferEnum.ACCOUNT_TITLE_LOAN_AMOUNT_A.getSubjectCode(),TrustOfferEnum.ACCOUNT_TITLE_LOAN_AMOUNT.getSubjectCode()};
        BigDecimal repaymentPenaltyAmount = new BigDecimal("0.00");
        BigDecimal repaymentInterestAmount = new BigDecimal("0.00");
        BigDecimal repaymentPrincipalAmount = new BigDecimal("0.00");
        List<TrustOfferFlow> trustOfferFlows = trustOfferFlowDao.getSplitAccountingsByLoanId(loanId,currentTerm);
//        if (Const.TRADE_CODE_NORMAL.equals(tradeType)) {
//        }else {
//            trustOfferFlows = trustOfferFlowDao.getSplitAccountingsByTradeType(loanId, new String[]{TrustOfferEnum.一次结清.getTradeType()});
//            subjectAmount.setTerms(this.getTerm(trustOfferFlows));
//        }
        repaymentPenaltyAmount = this.getAcctTitleAmount(trustOfferFlows,Arrays.asList(penaltyAcctitles));
        repaymentInterestAmount = this.getAcctTitleAmount(trustOfferFlows,Arrays.asList(interestAcctitles));
        repaymentPrincipalAmount = this.getAcctTitleAmount(trustOfferFlows,Arrays.asList(principalAcctitles));
//        if (Const.TRADE_CODE_ONEOFF.equals(tradeType)) {
            subjectAmount.setRepayTotalAmount(repaymentPenaltyAmount.add(repaymentInterestAmount).add((repaymentPrincipalAmount)));
//        }
        if (repaymentPenaltyAmount.compareTo(subjectAmount.getPenaltyAmount()) == -1) {
            subjectAmount.setPenaltyAmount(subjectAmount.getPenaltyAmount().subtract(repaymentPenaltyAmount));
        }else {
            subjectAmount.setPenaltyAmount(new BigDecimal("0.00"));
        }
        if (repaymentInterestAmount.compareTo(subjectAmount.getInterestAmount()) == -1) {
            subjectAmount.setInterestAmount(subjectAmount.getInterestAmount().subtract(repaymentInterestAmount));
        } else {
            subjectAmount.setInterestAmount(new BigDecimal("0.00"));
        }
        if (repaymentPrincipalAmount.compareTo(subjectAmount.getPrincipalAmount()) == -1) {
            subjectAmount.setPrincipalAmount(subjectAmount.getPrincipalAmount().subtract(repaymentPrincipalAmount));
        } else {
            subjectAmount.setPrincipalAmount(new BigDecimal("0.00"));
        }
    }

    @Override
    public List<LoanRepaymentDetail> getOnetimeRepayDetail(Date currDate, Long loanId) {
         List<LoanRepaymentDetail> repaymentDetails = new ArrayList<>();
        Long currenTerm = this.getCurrentTerm(loanId);
        LoanRepaymentDetail repaymentDetail = loanRepaymentDetailDao.findByLoanIdAndCurrentTerm(loanId,currenTerm);
        repaymentDetails.add(repaymentDetail);
        return repaymentDetails;
    }

    /**
     * 获取已经还了的金额
     * @param loanId
     * @param terms
     * @return
     */
    public BigDecimal getRepayTotalAmount(Long loanId,List<Long> terms){
        BigDecimal totalAmount = new BigDecimal("0.00");
        List<LoanRepaymentDetail> loanRepaymentDetails = loanRepaymentDetailDao.findEqualCurrentTerm(loanId,terms);
        if (CollectionUtils.isEmpty(loanRepaymentDetails)){
            return totalAmount;
        }
        for (LoanRepaymentDetail repaymentDetail:loanRepaymentDetails) {
            totalAmount = totalAmount.add(repaymentDetail.getReturneterm());
        }
        //罚息
        totalAmount = totalAmount.add(offerFlowDao.getMoreTermFineAmount(loanId,terms));
        return totalAmount;
    }

    /**
     * 获取除currentTerm期之外的 一次性结清金额 已还金额 已经期数
     * @param loanId
     * @param currentTerm
     * @return
     */
    public Map<String,Object> realAccounOneTimeExcludeCurrenTermToTal(Long loanId,Long currentTerm){
        Map<String,Object> map = new HashMap<>();
        //非currentTerm期数集合
        List<Long> excludeCurrenTerms= new ArrayList<>();
        BigDecimal amount = new BigDecimal("0.00");
        Long firstOneTimeTerm = 0L;
        Long tempTerm = 0L;

        List<TrustOfferFlow> trustOfferFlows = trustOfferFlowDao.getSplitAccountingsByTradeType(loanId, new String[]{TrustOfferEnum.一次结清.getTradeType()});
        for (TrustOfferFlow trustOfferFlow:trustOfferFlows) {
            if (!currentTerm.equals(trustOfferFlow.getCurrentTerm())) {
                amount = amount.add(trustOfferFlow.getTradeAmount());
               /* if (!excludeCurrenTerms.contains(trustOfferFlow.getCurrentTerm())) {
                    excludeCurrenTerms.add(trustOfferFlow.getCurrentTerm());
                }*/
            }
        }
        if (amount.compareTo(new BigDecimal("0.00")) == 1) {
            firstOneTimeTerm = trustOfferFlows.get(0).getCurrentTerm();
            tempTerm = firstOneTimeTerm;
            excludeCurrenTerms.add(firstOneTimeTerm);
            Long  differTerm =currentTerm-firstOneTimeTerm;
            for(int i = 1;i< differTerm;i++){
                tempTerm = tempTerm + 1;
                excludeCurrenTerms.add(tempTerm);
            }
        }
        if (tempTerm > 0L) {
            String[] tradeTypes ={TrustOfferEnum.正常还款.getTradeType(),TrustOfferEnum.逾期还款.getTradeType()};
            BigDecimal tempAmount = trustOfferFlowDao.getSplitAccountingAmountByTradetype(loanId,tradeTypes,firstOneTimeTerm,currentTerm);
            amount = amount.add(tempAmount);
        }
        map.put("excludeCurrenTermToTal",amount);
        map.put("excludeCurrenTerms",excludeCurrenTerms);
        map.put("firstOneTimeTerm",firstOneTimeTerm);
        return map;
    }

    @Override
    public boolean isCurrentTermSettle(Long loanId, Long currentTerm) {
        //取还款计划表的currentTerm期应还金额
        LoanRepaymentDetail loanRepaymentDetail = loanRepaymentDetailDao.findByLoanIdAndCurrentTerm(loanId,currentTerm);
        return this.isCurrentTermSettle(loanRepaymentDetail);
    }

    @Override
    public boolean isCurrentTermSettle(LoanRepaymentDetail loanRepaymentDetail) {
        String[] accttiles = {TrustOfferEnum.ACCOUNT_TITLE_INTEREST_EXP_A.getSubjectCode(),TrustOfferEnum.ACCOUNT_TITLE_INTEREST_EXP.getSubjectCode(),
                TrustOfferEnum.ACCOUNT_TITLE_LOAN_AMOUNT_A.getSubjectCode(),TrustOfferEnum.ACCOUNT_TITLE_LOAN_AMOUNT.getSubjectCode()};
        BigDecimal totalAmount = trustOfferFlowDao.getSplitAccountingAmountByAccttitle(loanRepaymentDetail.getLoanId(), accttiles,loanRepaymentDetail.getCurrentTerm());
        if(totalAmount.compareTo(loanRepaymentDetail.getReturneterm()) == 0){
            return true;
        }
        return false;
    }

    @Override
    public int getOverdueDay(Date currDate, Date penaltyDate) {
        int result=0;
            result = (int)Dates.diffDays(currDate,penaltyDate);
        return result<0?0:result;
    }

    @Override
    public void notEnoughFineInterestAccounting(OfferRepayInfo repayInfo) {
        repayInfo.setIsOverdue(true);
        Long currentTerm = this.getCurrentTerm(repayInfo.getLoanId());
        try{
            accounting(createTrustOfferFlow(currentTerm,TrustOfferEnum.ACCOUNT_TITLE_FINE_EXP.getSubjectCode(),repayInfo.getAmount(),"",repayInfo));
        }catch (Exception e){
            e.printStackTrace();
            throw new PlatformException("不足罚息记实扣罚息失败！，交易未完成");
        }
    }

    @Override
    public void otherAccttleAccounting(Long term, OfferRepayInfo repayInfo) {
        //其他费用
        BigDecimal otherAmount = repayInfo.getAmount();
        if(otherAmount.compareTo(new BigDecimal("0.00")) ==1){
            try{
                accounting(createTrustOfferFlow(term,TrustOfferEnum.ACCOUNT_TITLE_OTHER_RECEI.getSubjectCode(),otherAmount,"",repayInfo));
            }catch (Exception e){
                e.printStackTrace();
                throw new PlatformException("结清生成其他费用失败！，交易未完成");
            }
        }
    }

    @Override
    public boolean isRepaymentPenalty(Long loanId) {
        String [] accttitles = {TrustOfferEnum.ACCOUNT_TITLE_PENALTY_EXP.getSubjectCode(),TrustOfferEnum.ACCOUNT_TITLE_PENALTY_EXP_A.getSubjectCode()};
        BigDecimal amount = trustOfferFlowDao.getSplitAccountingAmountByAccttitle(loanId, accttitles, null);
        if (amount.compareTo(new BigDecimal("0.00")) == 1) {
            return true;
        }
        return false;
    }

    @Override
    public void oneTimepenaltyAccttleAccounting(Long term,OfferRepayInfo repayInfo) {
        BigDecimal penaltyAmount = repayInfo.getAmount();
        if(penaltyAmount.compareTo(new BigDecimal("0.00")) ==1) {
            try {
                accounting(createTrustOfferFlow(term, TrustOfferEnum.ACCOUNT_TITLE_PENALTY_EXP.getSubjectCode(), penaltyAmount, "", repayInfo));
            } catch (Exception e) {
                e.printStackTrace();
                throw new PlatformException("一次性结清实扣违约金记账流水处理失败！交易失败！");
            }
        }
    }

    @Override
    public Pager queryRefundedOfMoneyConfirmationBookPage(Map<String, Object> params) {
        return trustOfferFlowDao.queryRefundedOfMoneyConfirmationBookPage(params);
    }

    @Override
    public String getRefundedOfMoneyConfirmationBookExportFileName(String fundsSources, Date tradeDate) {
        String projectCode = this.getProjectCode(fundsSources);
        String fileDate = Dates.getDateTime(tradeDate, Dates.DATAFORMAT_YYYYMMDD);
        String fileName =String.format("%s%s%s", projectCode, ReqManagerFileTypeEnum.回款确认书.getFileCode(),fileDate);
        String fileSeq = "001";
        return String.format("%s_%s", fileName,fileSeq);
    }

    private String getBatchNum(String fundsSources,Date tradeDate) {
        StringBuffer batchNum = new StringBuffer();
        String temp = Dates.getDateTime(tradeDate,Dates.DATAFORMAT_YYYYMMDD);
        if (Strings.isEmpty(fundsSources)) {
            throw new PlatformException(ResponseEnum.FULL_MSG,"合同来源不存在");
        }
        if(fundsSources.equals(FundsSourcesTypeEnum.渤海信托.getValue()))
            return batchNum.append("BHXT").append(temp).toString();
        if(fundsSources.equals(FundsSourcesTypeEnum.渤海2.getValue()))
            return batchNum.append("BH2_").append(temp).toString();
        return batchNum.toString();
    }

    @Override
    public String getProjectCode(String fundsSources) {
        if (Strings.isEmpty(fundsSources)) {
            throw new PlatformException(ResponseEnum.FULL_MSG,"合同来源不存在");
        }
        if (fundsSources.equals(FundsSourcesTypeEnum.渤海2.getValue())) {
            return RequestManagementConst.TRUST_PROJECT_CODE2;
        }else if (fundsSources.equals(FundsSourcesTypeEnum.渤海信托.getValue())) {
            return  RequestManagementConst.TRUST_PROJECT_CODE;
        }else if (fundsSources.equals(FundsSourcesTypeEnum.华瑞渤海.getValue())) {
            return RequestManagementConst.TRUST_PROJECT_CODE3;
        }
            throw new PlatformException(ResponseEnum.FULL_MSG,"找不到合同号对应的项目简码");
    }

    /**
     * 还款确认书文件时间
     * @param tradeDate
     * @return
     */
    public String refundedOfMoneyConfirmationBookFileDate(Date tradeDate){
        Date fileDate = Dates.addDay(Dates.format(tradeDate, Dates.DATAFORMAT_YYYYMMDD),1);
        boolean isWoerkDay = this.isWorkDay(fileDate);
        if (isWoerkDay) {
            return Dates.getDateTime(fileDate,Dates.DATAFORMAT_YYYYMMDD);
        }
        return this.getNextMonday(tradeDate);
    }
    public boolean isWorkDay(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week = cal.get(Calendar.DAY_OF_WEEK);
        if (week > 1 && week < 7) {
            return true;
        }
        return false;
    }
    public String getNextMonday(Date date){
        Calendar cal =  Calendar.getInstance();
        cal.setTime(date);
        int week = cal.get(Calendar.DAY_OF_WEEK);
        switch (week){
            case Calendar.SATURDAY :
                return Dates.getDateTime(Dates.addDay(date,2),Dates.DATAFORMAT_YYYYMMDD);
            case Calendar.FRIDAY :
                return Dates.getDateTime(Dates.addDay(date,3),Dates.DATAFORMAT_YYYYMMDD);
            case Calendar.THURSDAY :
                return Dates.getDateTime(Dates.addDay(date,4),Dates.DATAFORMAT_YYYYMMDD);
            case Calendar.WEDNESDAY :
                return Dates.getDateTime(Dates.addDay(date,5),Dates.DATAFORMAT_YYYYMMDD);
            case Calendar.TUESDAY :
                return Dates.getDateTime(Dates.addDay(date,6),Dates.DATAFORMAT_YYYYMMDD);
            case Calendar.MONDAY:
                return Dates.getDateTime(Dates.addDay(date,7),Dates.DATAFORMAT_YYYYMMDD);
            default:
                return Dates.getDateTime(Dates.addDay(date,1),Dates.DATAFORMAT_YYYYMMDD);
        }
    }
    private String getMaxFileSeq(){
        Map<String,Object> params = new HashMap<>();
        params.put("operateType",ReqManagerOperateTypeEnum.downLoad.getOperateType());
        params.put("fileType", ReqManagerFileTypeEnum.回款确认书.getFileCode());
        String maxFileSeq = requestFileOperateRecordDao.findFileSeqByParam(params);
        if (Strings.isEmpty(maxFileSeq)) {
            return "0";
        }
        return maxFileSeq;
    }

    public void createFileSeqLog(String batchNum,String fileSeq) {
        RequestFileOperateRecord record = new RequestFileOperateRecord();
        record.setId(new BigDecimal(sequencesServiceImpl.getSequences(SequencesEnum.REQUEST_FILE_OPERATE_RECORD)));
        record.setBatchNum(batchNum);
        record.setOperateType(ReqManagerOperateTypeEnum.downLoad.getOperateType());
        record.setFileType(ReqManagerFileTypeEnum.回款确认书.getFileType());
        record.setOperateDate(new Date());
        record.setTimes(1);
        record.setFileSeq(fileSeq);
        record.setUpdateTime(new Date());
        requestFileOperateRecordDao.insert(record);
    }
    @Override
    public Workbook createRefundedOfMoneyConfirmationBookWorkBook(String path, TrustOfferFlowVo offerFlowVo) throws Exception{
        Workbook workbook = null;
        InputStream in = null;
        try {
            if(Strings.isEmpty(path)){
                throw new PlatformException(ResponseEnum.FULL_MSG, "没有配置回款确认书excel模板文件的存放路径！");
            }
            File file = new File(path);
            if(!file.exists()){
                throw new PlatformException(ResponseEnum.FULL_MSG, "回款确认书excel模板文件不存在！");
            }
            if(!file.isFile()){
                throw new PlatformException(ResponseEnum.FULL_MSG, "回款确认书excel模板文件不能是目录！");
            }
            // 回款确认书xls模板文件名称
            String pdfFileName = file.getName();
            // xls文件后缀名称
            String suffixName = pdfFileName.substring(pdfFileName.lastIndexOf(".") + 1);
            if(!"xls".equalsIgnoreCase(suffixName) && !"xlsx".equalsIgnoreCase(suffixName)){
                throw new PlatformException(ResponseEnum.FULL_MSG, "回款确认书excel模板文件格式不正确！");
            }
            // 统计回款确认书金额
            SubjectAmount subjectAmount = null;
            Map<String,Object> bh2CountResultMap = null;
            String loanBelongs = offerFlowVo.getLoanBelongs();
            Date tradeDate = offerFlowVo.getTradeDate();
            if (FundsSourcesTypeEnum.渤海信托.getValue().equals(loanBelongs)) {
                 subjectAmount = this.countRefundedOfMoneyConfirmationBookMoney(loanBelongs,Dates.getDateTime(tradeDate,Dates.DEFAULT_DATE_FORMAT));
                if(null == subjectAmount){
                    throw new PlatformException(ResponseEnum.FULL_MSG, "查询不到回款确认书的统计信息！");
                }
            }else{
                //渤海2  华瑞渤海 的回款确认书统计
                bh2CountResultMap = this.queryBH2RefundedOfMoneyConfirmationBookData(offerFlowVo);
            }
            // 模板文件输入流
            in = new FileInputStream(file);
            workbook = new HSSFWorkbook(in);
            // 填充excel模板数据
            if(FundsSourcesTypeEnum.渤海信托.getValue().equals(loanBelongs)) {
                if (!this.fillRefundedOfMoneyConfirmationBookExcelFile(workbook, subjectAmount, tradeDate)) {
                    throw new PlatformException(ResponseEnum.FULL_MSG, "填充excel模板数据异常！");
                }
            }else {
                //渤海2  华瑞渤海
                if(!this.fillBH2RefundedOfMoneyConfirmationBookExcelFile(workbook,bh2CountResultMap,tradeDate)){
                    throw new PlatformException(ResponseEnum.FULL_MSG, "填充excel模板数据异常！");
                }
            }
        } finally {
            try {
                if(null != in){
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return workbook;
    }

    /**
     * 计算回款确认书的金额
     * @param loanBelongs
     * @param tradeDate
     * @return
     */
    private SubjectAmount countRefundedOfMoneyConfirmationBookMoney(String loanBelongs,String tradeDate){
        String [] interestAcctitles = {TrustOfferEnum.ACCOUNT_TITLE_INTEREST_EXP.getSubjectCode()};
        String [] principalAcctitles = {TrustOfferEnum.ACCOUNT_TITLE_LOAN_AMOUNT.getSubjectCode()};
        String [] fineAcctitles = {TrustOfferEnum.ACCOUNT_TITLE_FINE_EXP.getSubjectCode(),TrustOfferEnum.ACCOUNT_TITLE_PENALTY_EXP.getSubjectCode(),
                TrustOfferEnum.ACCOUNT_TITLE_OTHER_RECEI.getSubjectCode()};
        String [] tradeTypes = {TrustOfferEnum.正常还款.getTradeType(),TrustOfferEnum.一次结清.getTradeType()};
        String [] specialAccount ={TrustOfferEnum.ACCOUNT_TITLE_INTEREST_EXP.getSubjectCode(),TrustOfferEnum.ACCOUNT_TITLE_LOAN_AMOUNT.getSubjectCode(),
                TrustOfferEnum.ACCOUNT_TITLE_FINE_EXP.getSubjectCode(),TrustOfferEnum.ACCOUNT_TITLE_PENALTY_EXP.getSubjectCode(),
                TrustOfferEnum.ACCOUNT_TITLE_OTHER_RECEI.getSubjectCode()};
        Date previousWorkDayDate = workDayInfoService.getPreviousWorkDayInfoByParams(Dates.parse(tradeDate,Dates.DEFAULT_DATE_FORMAT));
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("tradeBeginDate", Dates.getDateTime(previousWorkDayDate, Dates.DEFAULT_DATE_FORMAT));
        paramMap.put("tradeEndDate", tradeDate);
        paramMap.put("loanBelongs",loanBelongs);
        List<TrustOfferFlow> trustOfferFlows = trustOfferFlowDao.getSplitAccountingsByFundSources(paramMap);
        if (CollectionUtils.isEmpty(trustOfferFlows)) {
            throw new PlatformException(ResponseEnum.FULL_MSG,"统计结果为零");
        }
        List<TrustOfferFlow> trustOfferFlowList = this.getTrustOfferFlowByTradeMode(trustOfferFlows, TradeTypeEnum.通联代扣.getValue());
        if (CollectionUtils.isEmpty(trustOfferFlowList)) {
            throw new PlatformException(ResponseEnum.FULL_MSG,"无通联代扣的交易");
        }
        SubjectAmount subjectAmount = new SubjectAmount();
        subjectAmount.setSpecialAccountTotalAmount(getAcctTitleAmount(trustOfferFlowList, Arrays.asList(specialAccount)));
        subjectAmount.setPrincipalAmount(this.getAmountByAccTitleTradeType(trustOfferFlowList, Arrays.asList(principalAcctitles),Arrays.asList(tradeTypes)));
        subjectAmount.setInterestAmount(this.getAmountByAccTitleTradeType(trustOfferFlowList, Arrays.asList(interestAcctitles),Arrays.asList(tradeTypes)));
        subjectAmount.setFineInterestAmount(this.getAmountByAccTitleTradeType(trustOfferFlowList, Arrays.asList(fineAcctitles),Arrays.asList(tradeTypes)));
        subjectAmount.setRepayTotalAmount(subjectAmount.getPrincipalAmount().add(subjectAmount.getInterestAmount()).add(subjectAmount.getFineInterestAmount()));
        return subjectAmount;
    }

    private List<TrustOfferFlow> getTrustOfferFlowByTradeMode(List<TrustOfferFlow> trustOfferFlows,String tradeMode){
        List<TrustOfferFlow> offerFlowList = new ArrayList<>();
        for(TrustOfferFlow trustOfferFlow:trustOfferFlows){
            if (trustOfferFlow.getTradeMode().equals(tradeMode)){
                offerFlowList.add(trustOfferFlow);
            }
        }
        return offerFlowList;
    }

    public Map<String,Object> queryBH2RefundedOfMoneyConfirmationBookData(TrustOfferFlowVo trustOfferFlowVo){
        Map<String,Object> result = new HashMap<>();
        Map<String,Object> params = new HashMap<>();
        Date tradeDate = trustOfferFlowVo.getTradeDate();
        String loanBelong = trustOfferFlowVo.getLoanBelongs();
        Date tradeBeginDate = getRefundedOfMoneyConfirmationTradeBeginDate(tradeDate,loanBelong);
        params.put("loanBelong", loanBelong);
        params.put("tradeBeginDate", Dates.getDateTime(tradeBeginDate, Dates.DEFAULT_DATE_FORMAT));
        params.put("tradeEndDate", Dates.getDateTime(tradeDate, Dates.DEFAULT_DATE_FORMAT));
        if (!loanBelong.equals(FundsSourcesTypeEnum.华瑞渤海.getValue())) {
            params.put("tradeType", TradeTypeEnum.通联代扣.getValue());
        }
        if (loanBelong.equals(FundsSourcesTypeEnum.华瑞渤海.getValue())) {
            params.put("tradeTypes", new String[]{TradeTypeEnum.通联代扣.getValue(),TradeTypeEnum.快捷通.getValue()});
        }
        BigDecimal rechargeTotalAmount = new BigDecimal(0.0);
        SubjectAmount trustSubjectAmount = new SubjectAmount();
        SubjectAmount repaymentSubjectAmount = new SubjectAmount();
        if (workDayInfoService.isWorkDay(tradeDate)) {
            //统计划扣的专户资金流入总额
            rechargeTotalAmount = trustOfferFlowDao.queryRechargeTotalAmount(params);
            //统计划扣的专户资金流入总额 当天还款 当天分账（本金、利息、罚息）的金额
            trustSubjectAmount = trustOfferFlowDao.queryTrustSubjectAmount(params);
            //统计划扣的还款明细表 还款入账分账（本金、利息、罚息）的金额
            repaymentSubjectAmount = trustOfferFlowDao.queryRepaymentSubjectAmount(params);
        }
        if (loanBelong.equals(FundsSourcesTypeEnum.华瑞渤海.getValue())){
            params.put("tradeBeginDate", Dates.getDateTime(tradeDate, Dates.DEFAULT_DATE_FORMAT));
            params.put("tradeEndDate", Dates.getDateTime(Dates.getAfterDays(tradeDate,1), Dates.DEFAULT_DATE_FORMAT));
            //统计对公的专户资金流入总额
            rechargeTotalAmount = rechargeTotalAmount.add(trustOfferFlowDao.queryPublicRechargeTotalAmount(params));
            //统计对公的专户资金流入总额 当天还款 当天分账（本金、利息、罚息）的金额
            SubjectAmount trustPublicSubjectAmount = trustOfferFlowDao.queryPublicTrustSubjectAmount(params);
            trustSubjectAmount = this.addSubjectAmount(trustSubjectAmount,trustPublicSubjectAmount);
            //统计对公的还款明细表 还款入账分账（本金、利息、罚息）的金额
            SubjectAmount repaymentPublicSubjectAmount = trustOfferFlowDao.queryPulicRepaymentSubjectAmount(params);
            repaymentSubjectAmount = this.addSubjectAmount(repaymentSubjectAmount,repaymentPublicSubjectAmount);
        }
        //总金额
        BigDecimal repayTotalAmount = repaymentSubjectAmount.getPrincipalAmount().add(repaymentSubjectAmount.getInterestAmount()).add(repaymentSubjectAmount.getFineInterestAmount());
        if (rechargeTotalAmount.compareTo(new BigDecimal("0")) == 0 && repayTotalAmount.compareTo(new BigDecimal("0")) == 0) {
            throw  new PlatformException(ResponseEnum.FULL_MSG,"当前条件查询无数据！");
        }
        result.put("rechargeTotalAmount", rechargeTotalAmount);
        result.put("trustSubjectAmount", trustSubjectAmount);
        result.put("repaymentSubjectAmount", repaymentSubjectAmount);
        return result;
    }

    private SubjectAmount addSubjectAmount(SubjectAmount ... subjectAmount){
        SubjectAmount subject = new SubjectAmount();
        BigDecimal reliefAmount = new BigDecimal("0.00");
        BigDecimal fineInterestAmount = new BigDecimal("0.00");
        BigDecimal penaltyAmount = new BigDecimal("0.00");
        BigDecimal interestAmount = new BigDecimal("0.00");
        BigDecimal principalAmount = new BigDecimal("0.00");
        BigDecimal repayTotalAmount = new BigDecimal("0.00");
        for(SubjectAmount sub:subjectAmount){
            reliefAmount = reliefAmount.add(sub.getReliefAmount());
            fineInterestAmount = fineInterestAmount.add(sub.getFineInterestAmount());
            penaltyAmount = penaltyAmount.add(sub.getPenaltyAmount());
            interestAmount = interestAmount.add(sub.getInterestAmount());
            principalAmount = principalAmount.add(sub.getPrincipalAmount());
            repayTotalAmount = repayTotalAmount.add(sub.getRepayTotalAmount());
        }
        subject.setReliefAmount(reliefAmount);
        subject.setFineInterestAmount(fineInterestAmount);
        subject.setPenaltyAmount(penaltyAmount);
        subject.setInterestAmount(interestAmount);
        subject.setPrincipalAmount(principalAmount);
        subject.setRepayTotalAmount(repayTotalAmount);
        return subject;
    }
    /**
     * 填充回款确认书excel模板数据
     * @param workbook
     * @param subjectAmount
     */
    private boolean fillRefundedOfMoneyConfirmationBookExcelFile(Workbook workbook, SubjectAmount subjectAmount,Date tradeDate) {
        // 总额
        BigDecimal totalAmount = subjectAmount.getRepayTotalAmount();
        //本金
        BigDecimal principalAmount = subjectAmount.getPrincipalAmount();
        //利息
        BigDecimal interestAmount = subjectAmount.getInterestAmount();
        //罚息
        BigDecimal fineInterestAmount = subjectAmount.getFineInterestAmount();
        //专户总额
        BigDecimal specialAccountTotalAmount = subjectAmount.getSpecialAccountTotalAmount();
        //暂收款
        BigDecimal tempAmount = specialAccountTotalAmount.subtract(principalAmount).subtract(interestAmount).subtract(fineInterestAmount);
        // 系统日期
        Calendar ca = Calendar.getInstance();
        int year = ca.get(Calendar.YEAR);
        int month = ca.get(Calendar.MONTH) + 1;
        int day = ca.get(Calendar.DAY_OF_MONTH);
        String stampDate = year + "年" + month + "月" + day + "日";
//        String accountDate = this.refundedOfMoneyConfirmationBookFileDate(tradeDate);
        ca.setTime(Dates.parse(Dates.getDateTime(tradeDate,Dates.DATAFORMAT_YYYYMMDD), Dates.DATAFORMAT_YYYYMMDD));
        int tradeYear = ca.get(Calendar.YEAR);
        int tradeMonth = ca.get(Calendar.MONTH) + 1;
        int tradeDay = ca.get(Calendar.DAY_OF_MONTH);
        String tradeDayStr = tradeYear + "年" + tradeMonth + "月" + tradeDay + "日";

        try {
            /** excel单元格数据填充 **/
            Sheet sheet = workbook.getSheetAt(0);
            // 设置到账时间
            Cell cell = sheet.getRow(2).getCell(0);
            cell.setCellValue(tradeDayStr);
            // 设置专户资金流入总额
            cell = sheet.getRow(8).getCell(2);
            cell.setCellValue(NumberUtil.formatAmount(specialAccountTotalAmount));
            // 设置本金
            cell = sheet.getRow(8).getCell(3);
            cell.setCellValue(NumberUtil.formatAmount(principalAmount));
            // 设置利息
            cell = sheet.getRow(8).getCell(4);
            cell.setCellValue(NumberUtil.formatAmount(interestAmount));
            // 设置罚息
            cell = sheet.getRow(8).getCell(5);
            cell.setCellValue(NumberUtil.formatAmount(fineInterestAmount));

            // 设置总额
            cell = sheet.getRow(11).getCell(2);
            cell.setCellValue(NumberUtil.formatAmount(totalAmount));
            // 设置本金
            cell = sheet.getRow(11).getCell(3);
            cell.setCellValue(NumberUtil.formatAmount(principalAmount));
            // 设置利息
            cell = sheet.getRow(11).getCell(4);
            cell.setCellValue(NumberUtil.formatAmount(interestAmount));
            // 设置罚息
            cell = sheet.getRow(11).getCell(5);
            cell.setCellValue(NumberUtil.formatAmount(fineInterestAmount));
            // 设置差异金额
            cell = sheet.getRow(15).getCell(2);
            cell.setCellValue(NumberUtil.formatAmount(tempAmount));
            // 设置暂收
            cell = sheet.getRow(15).getCell(5);
            cell.setCellValue(NumberUtil.formatAmount(tempAmount));
            // 设置其他暂收
            cell = sheet.getRow(15).getCell(6);
            cell.setCellValue(NumberUtil.formatAmount(new BigDecimal("0.00")));
            // 设置盖章时间
            cell = sheet.getRow(20).getCell(2);
            cell.setCellValue(stampDate);
            // 设置复核确认时间
/*            cell = sheet.getRow(32).getCell(2);
            cell.setCellValue(stampDate);*/
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("填充excel模板数据异常：", e);
            return false;
        }
    }

    /**
     * 填充回款确认书excel模板数据
     * @param workbook
     * @param map
     */
    private boolean fillBH2RefundedOfMoneyConfirmationBookExcelFile(Workbook workbook,Map<String,Object> map,Date tradeDate) {
        BigDecimal rechargeTotalAmount = (BigDecimal)map.get("rechargeTotalAmount");
        SubjectAmount trustSubjectAmount = (SubjectAmount) map.get("trustSubjectAmount");
        SubjectAmount repaymentSubjectAmount = (SubjectAmount) map.get("repaymentSubjectAmount");
        //信托专户
        // 总额
        BigDecimal totalAmount = rechargeTotalAmount;
        //本金
        BigDecimal principalAmount = trustSubjectAmount.getPrincipalAmount();
        //利息
        BigDecimal interestAmount = trustSubjectAmount.getInterestAmount();
        //罚息
        BigDecimal fineInterestAmount = trustSubjectAmount.getFineInterestAmount();
        //还款明细
        //本金
        BigDecimal repayPrincipalAmount = repaymentSubjectAmount.getPrincipalAmount();
        //利息
        BigDecimal repayInterestAmount = repaymentSubjectAmount.getInterestAmount();
        //罚息
        BigDecimal repayFineAmount = repaymentSubjectAmount.getFineInterestAmount();
        //总金额
        BigDecimal repayTotalAmount = repayPrincipalAmount.add(repayInterestAmount).add(repayFineAmount);
        //差异金额
        BigDecimal differenceAmount = totalAmount.subtract(repayTotalAmount);
        //其他暂收金额 = 资金流入总金额 - （本金+利息+罚息）
        BigDecimal qitazhanshoukuan = totalAmount.subtract(principalAmount.add(interestAmount).add(fineInterestAmount));
        //冲其他暂收款（本金） 还款明细本金 - 信托专户本金
        BigDecimal zhanshoukuanbenjin = repayPrincipalAmount.subtract(principalAmount);
        //冲其他暂收款（利息） 还款明细利息 - 信托专户利息
        BigDecimal zhanshoukuanlixi = repayInterestAmount.subtract(interestAmount);
        //冲其他暂收款（罚息）还款明细罚息 - 信托专户罚息 违约金 当罚息计算
        BigDecimal zhanshoukuanfaxi = repayFineAmount.subtract(fineInterestAmount);
        // 系统日期
        Calendar ca = Calendar.getInstance();
        int year = ca.get(Calendar.YEAR);
        int month = ca.get(Calendar.MONTH) + 1;
        int day = ca.get(Calendar.DAY_OF_MONTH);
        String stampDate = year + "年" + month + "月" + day + "日";
//        String accountDate = this.refundedOfMoneyConfirmationBookFileDate(tradeDate);
        ca.setTime(Dates.parse(Dates.getDateTime(tradeDate,Dates.DATAFORMAT_YYYYMMDD), Dates.DATAFORMAT_YYYYMMDD));
        int tradeYear = ca.get(Calendar.YEAR);
        int tradeMonth = ca.get(Calendar.MONTH) + 1;
        int tradeDay = ca.get(Calendar.DAY_OF_MONTH);
        String tradeDayStr = tradeYear + "年" + tradeMonth + "月" + tradeDay + "日";

        try {
            /** excel单元格数据填充 **/
            Sheet sheet = workbook.getSheetAt(0);
            // 设置到账时间
            Cell cell = sheet.getRow(2).getCell(0);
            cell.setCellValue(tradeDayStr);
            // 设置专户资金流入总额
            cell = sheet.getRow(8).getCell(2);
            cell.setCellValue(NumberUtil.formatAmount(totalAmount));
            // 设置本金
            cell = sheet.getRow(8).getCell(3);
            cell.setCellValue(NumberUtil.formatAmount(principalAmount));
            // 设置利息
            cell = sheet.getRow(8).getCell(4);
            cell.setCellValue(NumberUtil.formatAmount(interestAmount));
            // 设置罚息
            cell = sheet.getRow(8).getCell(5);
            cell.setCellValue(NumberUtil.formatAmount(fineInterestAmount));

            // 设置总额
            cell = sheet.getRow(11).getCell(2);
            cell.setCellValue(NumberUtil.formatAmount(repayTotalAmount));
            // 设置本金
            cell = sheet.getRow(11).getCell(3);
            cell.setCellValue(NumberUtil.formatAmount(repayPrincipalAmount));
            // 设置利息
            cell = sheet.getRow(11).getCell(4);
            cell.setCellValue(NumberUtil.formatAmount(repayInterestAmount));
            // 设置罚息
            cell = sheet.getRow(11).getCell(5);
            cell.setCellValue(NumberUtil.formatAmount(repayFineAmount));
            // 设置差异金额
           cell = sheet.getRow(15).getCell(2);
            cell.setCellValue(NumberUtil.formatAmount(differenceAmount));
            // 设置其他暂收
            cell = sheet.getRow(15).getCell(6);
            cell.setCellValue(NumberUtil.formatAmount(qitazhanshoukuan));
            //设置冲其他暂收款（本金）
            cell = sheet.getRow(15).getCell(7);
            cell.setCellValue(NumberUtil.formatAmount(zhanshoukuanbenjin));
            //设置冲其他暂收款（利息）
            cell = sheet.getRow(15).getCell(8);
            cell.setCellValue(NumberUtil.formatAmount(zhanshoukuanlixi));
            //设置冲其他暂收款（罚息）
            cell = sheet.getRow(15).getCell(10);
            cell.setCellValue(NumberUtil.formatAmount(zhanshoukuanfaxi));
            // 设置盖章时间
            cell = sheet.getRow(20).getCell(2);
            cell.setCellValue(stampDate);
            // 设置复核确认时间
/*            cell = sheet.getRow(32).getCell(2);
            cell.setCellValue(stampDate);*/
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("填充excel模板数据异常：", e);
            return false;
        }
    }
    @Override
    public ByteArrayOutputStream createRefundedOfMoneyConfirmationBookOutStream(String path, TrustOfferFlowVo offerFlowVo) throws Exception{
        PdfReader reader = null;
        ByteArrayOutputStream bos = null;
        PdfStamper stamper = null;
        try {
            if(Strings.isEmpty(path)){
                throw new PlatformException(ResponseEnum.FULL_MSG, "没有配置回款确认书pdf模板文件的存放路径！");
            }
            File file = new File(path);
            if(!file.exists()){
                throw new PlatformException(ResponseEnum.FULL_MSG, "回款确认书pdf模板文件不存在！");
            }
            if(!file.isFile()){
                throw new PlatformException(ResponseEnum.FULL_MSG, "回款确认书pdf模板文件不能是目录！");
            }
            // 划拨申请书pdf模板文件名称
            String pdfFileName = file.getName();
            // pdf文件后缀名称
            String suffixName = pdfFileName.substring(pdfFileName.lastIndexOf(".") + 1);
            if(!"pdf".equalsIgnoreCase(suffixName)){
                throw new PlatformException(ResponseEnum.FULL_MSG, "回款确认书pdf模板文件格式不正确！");
            }
            // 统计回款确认书金额
            SubjectAmount subjectAmount = null;
            Map<String,Object> bh2CountResultMap = null;
            String loanBelongs = offerFlowVo.getLoanBelongs();
            Date tradeDate = offerFlowVo.getTradeDate();
            if (FundsSourcesTypeEnum.渤海信托.getValue().equals(loanBelongs)) {
                subjectAmount = this.countRefundedOfMoneyConfirmationBookMoney(loanBelongs, Dates.getDateTime(tradeDate, Dates.DEFAULT_DATE_FORMAT));
                if (null == subjectAmount) {
                    throw new PlatformException(ResponseEnum.FULL_MSG, "统计回款确认书金额不存在");
                }
            }else {
                //渤海2 华瑞渤海 的回款确认书统计
                bh2CountResultMap = this.queryBH2RefundedOfMoneyConfirmationBookData(offerFlowVo);
            }
            // 读取pdf模板文件
            reader = new PdfReader(path);
            bos = new ByteArrayOutputStream();
            stamper = new PdfStamper(reader, bos);
            AcroFields form = stamper.getAcroFields();
            if (FundsSourcesTypeEnum.渤海信托.getValue().equals(loanBelongs)) {
                this.fillRefundedOfMoneyConfirmationBookPdfFile(form, subjectAmount, tradeDate);
            }else {
                //渤海2 华瑞渤海
                this.fillBH2RefundedOfMoneyConfirmationBookPdfFile(form,bh2CountResultMap,tradeDate);
            }
            // 如果为false那么生成的PDF文件还能编辑，一定要设为true
            stamper.setFormFlattening(true);
        } finally {
            try {
                if(null!= stamper){
                    stamper.close();
                }
                if(null!= bos){
                    bos.close();
                }
                if(null!= reader){
                    reader.close();
                }
            } catch (DocumentException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bos;
    }

    private void fillRefundedOfMoneyConfirmationBookPdfFile(AcroFields form, SubjectAmount subjectAmount,Date tradeDate) throws IOException, DocumentException {
        // 总额
        BigDecimal totalAmount = subjectAmount.getRepayTotalAmount();
        //本金
        BigDecimal principalAmount = subjectAmount.getPrincipalAmount();
        //利息
        BigDecimal interestAmount = subjectAmount.getInterestAmount();
        //罚息
        BigDecimal fineInterestAmount = subjectAmount.getFineInterestAmount();
        //专户总额
        BigDecimal specialAccountTotalAmount = subjectAmount.getSpecialAccountTotalAmount();
        //暂收款
        BigDecimal tempAmount = specialAccountTotalAmount.subtract(principalAmount).subtract(interestAmount).subtract(fineInterestAmount);
        // 系统日期
        Calendar ca = Calendar.getInstance();
        int year = ca.get(Calendar.YEAR);
        int month = ca.get(Calendar.MONTH) + 1;
        int day = ca.get(Calendar.DAY_OF_MONTH);
        String stampDate = year + "年" + month + "月" + day + "日";

//        String accountDate = this.refundedOfMoneyConfirmationBookFileDate(tradeDate);
        ca.setTime(Dates.parse(Dates.getDateTime(tradeDate,Dates.DATAFORMAT_YYYYMMDD), Dates.DATAFORMAT_YYYYMMDD));
        int tradeYear = ca.get(Calendar.YEAR);
        int tradeMonth = ca.get(Calendar.MONTH) + 1;
        int tradeDay = ca.get(Calendar.DAY_OF_MONTH);
        String tradeDayStr = tradeYear + "年" + tradeMonth + "月" + tradeDay + "日";
        form.setField("repayTotalAmount",NumberUtil.formatAmount(totalAmount));
        form.setField("repayPrincipalAmount",NumberUtil.formatAmount(principalAmount));
        form.setField("repayInterestAmount",NumberUtil.formatAmount(interestAmount));
        form.setField("repayFineAmount",NumberUtil.formatAmount(fineInterestAmount));
        form.setField("totalAmount",NumberUtil.formatAmount(specialAccountTotalAmount));
        form.setField("principalAmount",NumberUtil.formatAmount(principalAmount));
        form.setField("interestAmount",NumberUtil.formatAmount(interestAmount));
        form.setField("fineAmount",NumberUtil.formatAmount(fineInterestAmount));
        form.setField("differenceAmount",NumberUtil.formatAmount(tempAmount));
        form.setField("zhanshoukuan",NumberUtil.formatAmount(tempAmount));
        form.setField("qitazhanshoukuan",NumberUtil.formatAmount(new BigDecimal("0.00")));

        form.setField("tradeDay", tradeDayStr);
        form.setField("stampDate", stampDate);
//        form.setField("reviewDate", stampDate);



    }

    private void fillBH2RefundedOfMoneyConfirmationBookPdfFile(AcroFields form, Map<String,Object> map,Date tradeDate) throws IOException, DocumentException {
        BigDecimal rechargeTotalAmount = (BigDecimal)map.get("rechargeTotalAmount");
        SubjectAmount trustSubjectAmount = (SubjectAmount) map.get("trustSubjectAmount");
        SubjectAmount repaymentSubjectAmount = (SubjectAmount) map.get("repaymentSubjectAmount");
        //信托专户
        // 总额
        BigDecimal totalAmount = rechargeTotalAmount;
        //本金
        BigDecimal principalAmount = trustSubjectAmount.getPrincipalAmount();
        //利息
        BigDecimal interestAmount = trustSubjectAmount.getInterestAmount();
        //罚息
        BigDecimal fineInterestAmount = trustSubjectAmount.getFineInterestAmount();
        //还款明细
        //本金
        BigDecimal repayPrincipalAmount = repaymentSubjectAmount.getPrincipalAmount();
        //利息
        BigDecimal repayInterestAmount = repaymentSubjectAmount.getInterestAmount();
        //罚息
        BigDecimal repayFineAmount = repaymentSubjectAmount.getFineInterestAmount();
        //总金额
        BigDecimal repayTotalAmount = repayPrincipalAmount.add(repayInterestAmount).add(repayFineAmount);
        //差异金额
        BigDecimal differenceAmount = totalAmount.subtract(repayTotalAmount);
        //其他暂收金额 = 资金流入总金额 - （本金+利息+罚息）
        BigDecimal qitazhanshoukuan = totalAmount.subtract(principalAmount.add(interestAmount).add(fineInterestAmount));
        //冲其他暂收款（本金） 还款明细本金 - 信托专户本金
        BigDecimal zhanshoukuanbenjin = repayPrincipalAmount.subtract(principalAmount);
        //冲其他暂收款（利息） 还款明细利息 - 信托专户利息
        BigDecimal zhanshoukuanlixi = repayInterestAmount.subtract(interestAmount);
        //冲其他暂收款（罚息）还款明细罚息 - 信托专户罚息 违约金 当罚息计算
        BigDecimal zhanshoukuanfaxi = repayFineAmount.subtract(fineInterestAmount);
        // 系统日期
        Calendar ca = Calendar.getInstance();
        int year = ca.get(Calendar.YEAR);
        int month = ca.get(Calendar.MONTH) + 1;
        int day = ca.get(Calendar.DAY_OF_MONTH);
        String stampDate = year + "年" + month + "月" + day + "日";

//        String accountDate = this.refundedOfMoneyConfirmationBookFileDate(tradeDate);
        ca.setTime(Dates.parse(Dates.getDateTime(tradeDate,Dates.DATAFORMAT_YYYYMMDD), Dates.DATAFORMAT_YYYYMMDD));
        int tradeYear = ca.get(Calendar.YEAR);
        int tradeMonth = ca.get(Calendar.MONTH) + 1;
        int tradeDay = ca.get(Calendar.DAY_OF_MONTH);
        String tradeDayStr = tradeYear + "年" + tradeMonth + "月" + tradeDay + "日";
        form.setField("repayTotalAmount",NumberUtil.formatAmount(repayTotalAmount));
        form.setField("repayPrincipalAmount",NumberUtil.formatAmount(repayPrincipalAmount));
        form.setField("repayInterestAmount",NumberUtil.formatAmount(repayInterestAmount));
        form.setField("repayFineAmount",NumberUtil.formatAmount(repayFineAmount));
        form.setField("totalAmount",NumberUtil.formatAmount(totalAmount));
        form.setField("principalAmount",NumberUtil.formatAmount(principalAmount));
        form.setField("interestAmount",NumberUtil.formatAmount(interestAmount));
        form.setField("fineAmount",NumberUtil.formatAmount(fineInterestAmount));
        form.setField("differenceAmount",NumberUtil.formatAmount(differenceAmount));
        form.setField("qitazhanshoukuan",NumberUtil.formatAmount(qitazhanshoukuan));
        form.setField("zhanshoukuanbenjin",NumberUtil.formatAmount(zhanshoukuanbenjin));
        form.setField("zhanshoukuanlixi",NumberUtil.formatAmount(zhanshoukuanlixi));
        form.setField("zhanshoukuanfaxi",NumberUtil.formatAmount(zhanshoukuanfaxi));
        form.setField("tradeDay", tradeDayStr);
        form.setField("stampDate", stampDate);
//        form.setField("reviewDate", stampDate);

    }
	@Override
	public String getReturnMoneyConfireFileName(ReqManagerFileTypeEnum fileTypeEnum, String loanBelongs, Date tradeDate) {
		StringBuffer fileName = new StringBuffer();
		fileName.append(getProjectCode(loanBelongs));
		fileName.append(fileTypeEnum.getFileCode());
		fileName.append(Dates.getDateTime(tradeDate, Dates.DATAFORMAT_YYYYMMDD));
		fileName.append("_001");
		fileName.append(fileTypeEnum.getFileExtension());
		return fileName.toString();
	}

	@Override
	public List<Map<String, Object>> findSubAccountDetailList(Map<String, Object> params) {
		String loanBelongs = (String)params.get("loanBelongs");
		if(FundsSourcesTypeEnum.渤海2.getValue().equals(loanBelongs) || FundsSourcesTypeEnum.华瑞渤海.getValue().equals(loanBelongs)){
            Date tradeDate = (Date)params.get("tradeDate");
            List<Map<String,Object>> deatailList = new ArrayList<>();
            if (workDayInfoService.isWorkDay(tradeDate)) {
                //统计划扣分账明细
                deatailList = offerRepayInfoDao.findSubAccountDetailList(params);
            }
            if (FundsSourcesTypeEnum.华瑞渤海.getValue().equals(loanBelongs)) {
                params.put("tradeStartDate", Dates.getDateTime(tradeDate, Dates.DEFAULT_DATE_FORMAT));
                params.put("tradeEndDate", Dates.getDateTime(Dates.getAfterDays(tradeDate,1), Dates.DEFAULT_DATE_FORMAT));
                //统计对公分账明细
                List<Map<String,Object>> deatailPublicList = offerRepayInfoDao.findPublicSubAccountDetailList(params);
                if (CollectionUtils.isNotEmpty(deatailPublicList)) {
                    deatailList.addAll(deatailPublicList);
                    Collections.sort(deatailList,new SortByContractNumAndTradate());
                }
            }
            return deatailList;
		}else if(FundsSourcesTypeEnum.渤海信托.getValue().equals(loanBelongs)){
			return offerRepayInfoDao.findSubAccountDetailList4BHXT(params);
		}else{
			return null;
		}
	}

	/**
	 * 获取其他暂收款报表导出的文件名
	 */
	@Override
	public String getTemporaryAmountExcelFileName(String loanBelongs,Date tradeDate) {
		//文件名信息获取
		String projectCode = this.getProjectCode(loanBelongs);
		String fileDate = Dates.getDateTime(tradeDate,Dates.DATAFORMAT_YYYYMMDD);
		String fileName = String.format("%s%s%s", projectCode, "_shortreceipt_",
				fileDate);
		String fileSeq = "001";
		return String.format("%s_%s", fileName, fileSeq);
	}

	/**
	 * 获取其他暂收款报表导出的workbook对象
	 */
	@Override
	public Workbook createTemporaryAmountExcelWorkBook(TrustOfferFlowVo offerFlowVo,
			List<TemporaryAmountVo> temporaryAmountList, String fileName) {
		try {

			// 转换实际还款时间与实际分账时间
			Date tradeDate = new Date();
			Date offDate = new Date();
			for (TemporaryAmountVo _temporaryAmountVo : temporaryAmountList) {
                tradeDate = _temporaryAmountVo.getTradeDate();
                offDate = _temporaryAmountVo.getOffDate();

				tradeDate = workDayInfoService.getAfterWorkDayInfoByParams(tradeDate);
				offDate = workDayInfoService.getAfterWorkDayInfoByParams(offDate);
				_temporaryAmountVo.setTradeDateS(Dates.getDateTime(tradeDate, Dates.DATAFORMAT_SLASHDATE));
				_temporaryAmountVo.setOffDateS(Dates.getDateTime(offDate, Dates.DATAFORMAT_SLASHDATE));
                if ("isPublicRepayment".equals(_temporaryAmountVo.getMemo()) && RequestManagementConst.TRUST_PROJECT_CODE3.equals(_temporaryAmountVo.getProjectCode())) {
                    _temporaryAmountVo.setOffDateS(Dates.getDateTime(Dates.parse(fileName.substring("ZDCF03_shortreceipt_".length(), fileName.indexOf("_001.xls")),Dates.DATAFORMAT_YYYYMMDD),Dates.DATAFORMAT_SLASHDATE));
                    _temporaryAmountVo.setTradeDateS(Dates.getDateTime(_temporaryAmountVo.getTradeDate(), Dates.DATAFORMAT_SLASHDATE));

                }
			}

			//创建工作簿准备
			String[] labels = new String[] { "信托项目简码", "合同编号/借款编号", "发生日期", "其他暂收款本金",
					"其他暂收款利息", "其他暂收罚息", "其他暂收款暂收", "冲账日期" };

			String[] fields = new String[] { "projectCode", "contractNum", "tradeDateS", "temporaryIncome",
					"temporaryInterest", "temporaryPenalty", "temporaryOtherAmount", "offDateS" };

			// 工作表名称
			String sheetName = "其他暂收款报表信息";

			// 创建工作簿
			Workbook workbook = ExcelExportUtil.createExcelByVo(fileName+".xls", labels, fields, temporaryAmountList, sheetName);

			return workbook;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 查询其他暂收款报表信息
	 */
	@Override
	public List<TemporaryAmountVo> queryTemporaryAmount(TrustOfferFlowVo offerFlowVo) {
        String loanBelongs = offerFlowVo.getLoanBelongs();
		// 收集请求参数
		Map<String, Object> params = new HashMap<String, Object>();
        // 找出上个工作日
        Date tradeDate = offerFlowVo.getTradeDate();
        Date startDate = this.getRefundedOfMoneyConfirmationTradeBeginDate(tradeDate,offerFlowVo.getLoanBelongs());
		// 查询条件限制
		params.put("loanBelong", loanBelongs);
		params.put("startDate", Dates.getDateTime(startDate, Dates.DEFAULT_DATE_FORMAT));
		params.put("endDate", Dates.getDateTime(tradeDate, Dates.DEFAULT_DATE_FORMAT));
        if (!loanBelongs.equals(FundsSourcesTypeEnum.华瑞渤海.getValue())) {
            params.put("tradeType", TradeTypeEnum.通联代扣.getValue());
        }
        if (loanBelongs.equals(FundsSourcesTypeEnum.华瑞渤海.getValue())) {
            params.put("tradeTypes", new String[]{TradeTypeEnum.通联代扣.getValue(),TradeTypeEnum.快捷通.getValue()});
        }
        List<TemporaryAmountVo> temporaryAmountVoList = new ArrayList<>();
        if (workDayInfoService.isWorkDay(tradeDate)) {
            //划扣暂其他明细
            temporaryAmountVoList = trustOfferFlowDao.queryTemporaryAmount(params);
        }
        if (loanBelongs.equals(FundsSourcesTypeEnum.华瑞渤海.getValue())) {
            params.put("tradeStartDate", Dates.getDateTime(tradeDate, Dates.DEFAULT_DATE_FORMAT));
            params.put("tradeEndDate", Dates.getDateTime(Dates.getAfterDays(tradeDate,1), Dates.DEFAULT_DATE_FORMAT));
            //对公暂其他明细
            List<TemporaryAmountVo> temporaryPublicAmountVoList = trustOfferFlowDao.queryPublicTemporaryAmount(params);
            if (CollectionUtils.isNotEmpty(temporaryPublicAmountVoList)) {
                temporaryAmountVoList.addAll(temporaryPublicAmountVoList);
                Collections.sort(temporaryAmountVoList,new SortByContractNum());
            }
        }
        return temporaryAmountVoList;
	}

	@Override
	@Transactional
	public List<DebitOfferFlow> findDebitOfferFlowByParams(Map<String, Object> params) {
		List<DebitOfferFlow> list = new ArrayList<DebitOfferFlow>();
		List<Map<String, Object>> loanList = trustOfferFlowDao.findDebitTermInfo4Wm3(params);
		if(CollectionUtils.isEmpty(loanList)){
			return list;
		}
		String batNo = "wm3_" + System.currentTimeMillis();
		for(int i =0;i<loanList.size();i++){
			Map<String, Object> map = loanList.get(i);
			Long loanId = Long.parseLong((String)map.get("loanId"));
			DebitAccountInfo debitAccountInfo = new DebitAccountInfo();
			debitAccountInfo.setLoanId(loanId);
			debitAccountInfo = debitAccountInfoDao.findListByVo(debitAccountInfo).get(0);
			String accNo = debitAccountInfo.getAccountNo();
			params.put("loanId", loanId);
			List<Map<String, Object>> debitFlowInfoList = trustOfferFlowDao.findDebitFlowInfo4Wm3(params);
			if(CollectionUtils.isEmpty(debitFlowInfoList)){
				continue;
			}
			for(Map<String, Object> debitFlowInfo:debitFlowInfoList){
				String tradeDate = (String)debitFlowInfo.get("tradeDate");
				Object receivePrincipal = debitFlowInfo.get("receivePrincipal");
				String currentTerm = (String)debitFlowInfo.get("currentTerm");
                boolean isAdvanceRepay =  isAdvanceRepay(loanId, Strings.isEmpty(currentTerm) ? 0l : Long.valueOf(currentTerm));
				if(Strings.isNotEmpty(receivePrincipal) && new BigDecimal(receivePrincipal.toString()).compareTo(new BigDecimal(0)) > 0){
					DebitOfferFlow debitOfferFlow = debitOfferFlowService.createDebitOfferFlow(map, batNo, SubjTypeWm3Enum.应收本金.getCode(), new BigDecimal(receivePrincipal.toString()),tradeDate,accNo,currentTerm, isAdvanceRepay);
					list.add(debitOfferFlow);
				}
				Object receiveInterest = debitFlowInfo.get("receiveInterest");
				if(Strings.isNotEmpty(receiveInterest) && new BigDecimal(receiveInterest.toString()).compareTo(new BigDecimal(0)) > 0){
					DebitOfferFlow debitOfferFlow = debitOfferFlowService.createDebitOfferFlow(map, batNo, SubjTypeWm3Enum.应收利息.getCode(), new BigDecimal(receiveInterest.toString()),tradeDate,accNo,currentTerm, isAdvanceRepay);
					list.add(debitOfferFlow);
				}
				Object receiveFine = debitFlowInfo.get("receiveFine");
				if(Strings.isNotEmpty(receiveFine) && new BigDecimal(receiveFine.toString()).compareTo(new BigDecimal(0)) > 0){
					DebitOfferFlow debitOfferFlow = debitOfferFlowService.createDebitOfferFlow(map, batNo, SubjTypeWm3Enum.应收罚息.getCode(), new BigDecimal(receiveFine.toString()),tradeDate,accNo,currentTerm, isAdvanceRepay);
					list.add(debitOfferFlow);
				}
				
				Object actualPrincipal = debitFlowInfo.get("actualPrincipal");
				if(Strings.isNotEmpty(actualPrincipal) && new BigDecimal(actualPrincipal.toString()).compareTo(new BigDecimal(0)) > 0){
					DebitOfferFlow debitOfferFlow = debitOfferFlowService.createDebitOfferFlow(map, batNo, SubjTypeWm3Enum.实收本金.getCode(), new BigDecimal(actualPrincipal.toString()),tradeDate,accNo,currentTerm, isAdvanceRepay);
					list.add(debitOfferFlow);
				}
				Object actualInterest = debitFlowInfo.get("actualInterest");
				if(Strings.isNotEmpty(actualInterest) && new BigDecimal(actualInterest.toString()).compareTo(new BigDecimal(0)) > 0){
					DebitOfferFlow debitOfferFlow = debitOfferFlowService.createDebitOfferFlow(map, batNo, SubjTypeWm3Enum.实收利息.getCode(), new BigDecimal(actualInterest.toString()),tradeDate,accNo,currentTerm, isAdvanceRepay);
					list.add(debitOfferFlow);
				}
				Object actualFine = debitFlowInfo.get("actualFine");
				if(Strings.isNotEmpty(actualFine) && new BigDecimal(actualFine.toString()).compareTo(new BigDecimal(0)) > 0){
					DebitOfferFlow debitOfferFlow = debitOfferFlowService.createDebitOfferFlow(map, batNo, SubjTypeWm3Enum.实收罚息.getCode(), new BigDecimal(actualFine.toString()),tradeDate,accNo,currentTerm, isAdvanceRepay);
					list.add(debitOfferFlow);
				}
				Object reducePrincipal = debitFlowInfo.get("reducePrincipal");
				if(Strings.isNotEmpty(reducePrincipal) && new BigDecimal(reducePrincipal.toString()).compareTo(new BigDecimal(0)) > 0){
					DebitOfferFlow debitOfferFlow = debitOfferFlowService.createDebitOfferFlow(map, batNo, SubjTypeWm3Enum.减免本金.getCode(), new BigDecimal(reducePrincipal.toString()),tradeDate,accNo,currentTerm, isAdvanceRepay);
					list.add(debitOfferFlow);
				}
				Object reduceInterest = debitFlowInfo.get("reduceInterest");
				if(Strings.isNotEmpty(reduceInterest) && new BigDecimal(reduceInterest.toString()).compareTo(new BigDecimal(0)) > 0){
					DebitOfferFlow debitOfferFlow = debitOfferFlowService.createDebitOfferFlow(map, batNo, SubjTypeWm3Enum.减免利息.getCode(), new BigDecimal(reduceInterest.toString()),tradeDate,accNo,currentTerm, isAdvanceRepay);
					list.add(debitOfferFlow);
				}
				Object reduceFine = debitFlowInfo.get("reduceFine");
				if(Strings.isNotEmpty(reduceFine) && new BigDecimal(reduceFine.toString()).compareTo(new BigDecimal(0)) > 0){
					DebitOfferFlow debitOfferFlow = debitOfferFlowService.createDebitOfferFlow(map, batNo, SubjTypeWm3Enum.减免罚息.getCode(), new BigDecimal(reduceFine.toString()),tradeDate,accNo,currentTerm, isAdvanceRepay);
					list.add(debitOfferFlow);
				}
				Object actualPenalty = debitFlowInfo.get("actualPenalty");
				if(Strings.isNotEmpty(actualPenalty) && new BigDecimal(actualPenalty.toString()).compareTo(new BigDecimal(0)) > 0){
					DebitOfferFlow debitOfferFlow = debitOfferFlowService.createDebitOfferFlow(map, batNo, SubjTypeWm3Enum.代收违约金.getCode(), new BigDecimal(actualPenalty.toString()),tradeDate,accNo,currentTerm, isAdvanceRepay);
					list.add(debitOfferFlow);
				}				
			}
		}
		return list;
	}

    /**
     * 判断是否提前清贷
     * @param loanId
     * @param currentTerm
     * @return
     */
    private boolean isAdvanceRepay(Long loanId, Long currentTerm) {
        boolean flag = false;
        VLoanInfo vLoanInfo = vLoanInfoServiceImpl.findByLoanId(loanId);
        LoanRepaymentDetail loanRepaymentDetail = loanRepaymentDetailDao.findByLoanIdAndCurrentTerm(loanId, currentTerm);
        if(loanRepaymentDetail == null){
            return false;
        }
        if(LoanStateEnum.结清.getValue().equals(vLoanInfo.getLoanState()) && vLoanInfo.getTime() > currentTerm){
            flag = true;
        }
        return flag;
    }

    @Override
    public Workbook getSubAccountDetailWorkbook(String fileName, TrustOfferFlowVo offerFlowVo) {
        Workbook workbook = null;
        try {
            Date tradeDate = offerFlowVo.getTradeDate();
            String loanBelongs = offerFlowVo.getLoanBelongs();
            Date tradeStartDate = this.getRefundedOfMoneyConfirmationTradeBeginDate(tradeDate,loanBelongs);
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("loanBelongs", loanBelongs);
            params.put("tradeStartDate", Dates.getDateTime(tradeStartDate, Dates.DEFAULT_DATE_FORMAT));
            params.put("tradeEndDate", Dates.getDateTime(tradeDate, Dates.DEFAULT_DATE_FORMAT));
            params.put("tradeDate",tradeDate);
            if (!loanBelongs.equals(FundsSourcesTypeEnum.华瑞渤海.getValue())) {
                params.put("tradeType", TradeTypeEnum.通联代扣.getValue());
            }
            if (loanBelongs.equals(FundsSourcesTypeEnum.华瑞渤海.getValue())) {
                params.put("tradeTypes", new String[]{TradeTypeEnum.通联代扣.getValue(),TradeTypeEnum.快捷通.getValue()});
            }
            List<Map<String, Object>> list = this.findSubAccountDetailList(params);
            if (CollectionUtils.isEmpty(list)) {
                throw new PlatformException(ResponseEnum.FULL_MSG, "当前条件查询无数据！");
            }
            String labels[] = new String[]{"信托项目简码", "借款编号", "扣款方式", "扣款类型", "本期期数", "实扣本金", "实扣利息", "利息减免", "实扣罚息", "罚息减免", "实扣违约金", "违约金减免", "趸缴服务费退款", "实扣手续费", "手续费减免", "实还担保费"
                    , "担保费减免", "实还服务费", "服务费减免", "扣款日期（实际）", "实还其他费用一", "费用一减免", "实还其他费用二", "费用二减免", "实还其他费用三", "费用三减免", "三方支付流水/银行流水", "趸缴服务费退款本金", "趸缴服务费退款利息"};
            String fields[] = new String[]{"project_code", "contract_num", "trade_type", "repayment_type", "current_term", "actual_principal", "actual_interest", "reduce_interest", "actual_fine", "reduce_fine", "actual_penalty", "reduce_penalty", "refund_service_fee", "actual_handling", "reduce_handling", "actual_guarantee"
                    , "reduce_guarantee", "actual_service", "reduce_service", "actual_trade_date", "actual_fee1", "reduce_fee1", "actual_fee2", "reduce_fee2", "actual_fee3", "reduce_fee3", "third_pay_serial", "give_back_principal", "give_back_interest"};
            String sheetName = "分账明细表";
            workbook = ExcelExportUtil.createExcelByMap(fileName, labels, fields, list, sheetName);
            if (workbook == null) {
                throw new PlatformException(ResponseEnum.FULL_MSG, "创建分账明细表workBook失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new PlatformException(ResponseEnum.FULL_MSG,e.getMessage());
        }
        return workbook;
    }

    @Override
    public Workbook getTemporaryAmountWorkbook(String fileName, TrustOfferFlowVo offerFlowVo) {

        // 查询其他暂收款报表信息
        List<TemporaryAmountVo> temporaryAmountList = this.queryTemporaryAmount(offerFlowVo);
        if (CollectionUtils.isEmpty(temporaryAmountList)) {
            throw new PlatformException(ResponseEnum.FULL_MSG, "当前条件查询无数据！");
        }
        Workbook workbook = this.createTemporaryAmountExcelWorkBook(offerFlowVo, temporaryAmountList, fileName);
        if (workbook == null) {
            throw new PlatformException(ResponseEnum.FULL_MSG,"创建其他暂收款表workBook失败！");
        }
        return workbook;
    }

    @Override
    public Boolean uploadRefundedOfMoneyConfirmationFileToBh(String fundSources,Date tradeDate,JschSftpUtils jschSftpUtils){
        String projectCode = this.getProjectCode(fundSources);
        ReqManagerFileTypeEnum [] reqManagerFileTypeEnums = this.getReqManagerFileTypes(projectCode);
        TrustOfferFlowVo offerFlowVo = new TrustOfferFlowVo();
        offerFlowVo.setFundsSources(fundSources);
        offerFlowVo.setLoanBelongs(fundSources);
        offerFlowVo.setTradeDate(tradeDate);
        Map<String,InputStream> inputStreamMap = this.getUploadBhFtpInputStreams(offerFlowVo,reqManagerFileTypeEnums);
        if (inputStreamMap.isEmpty()) {
            throw new PlatformException(ResponseEnum.FULL_MSG,"回款确认书文件上传job没有需要上传的文件！");
        }
        boolean statues = requestManagementService.uploadFtpBHXT(jschSftpUtils,inputStreamMap,projectCode);
        return statues;
    }

    public ReqManagerFileTypeEnum[] getReqManagerFileTypes(String projectCode){
        ReqManagerFileTypeEnum [] reqManagerFileTypeEnums = null;
        if (projectCode.equals(RequestManagementConst.TRUST_PROJECT_CODE)) {
            reqManagerFileTypeEnums = new ReqManagerFileTypeEnum[]{ReqManagerFileTypeEnum.回款确认书,ReqManagerFileTypeEnum.回款确认书pdf,ReqManagerFileTypeEnum.分账明细表};
            return reqManagerFileTypeEnums;
        }
        if (projectCode.equals(RequestManagementConst.TRUST_PROJECT_CODE2)) {
            reqManagerFileTypeEnums = new ReqManagerFileTypeEnum[]{ReqManagerFileTypeEnum.回款确认书,ReqManagerFileTypeEnum.回款确认书pdf,ReqManagerFileTypeEnum.分账明细表,ReqManagerFileTypeEnum.暂其他收款};
            return reqManagerFileTypeEnums;
        }
        if (projectCode.equals(RequestManagementConst.TRUST_PROJECT_CODE3)) {
            reqManagerFileTypeEnums = new ReqManagerFileTypeEnum[]{ReqManagerFileTypeEnum.回款确认书,ReqManagerFileTypeEnum.回款确认书pdf,ReqManagerFileTypeEnum.分账明细表,ReqManagerFileTypeEnum.暂其他收款};
            return reqManagerFileTypeEnums;
        }
        if (reqManagerFileTypeEnums == null) {
            throw new PlatformException(ResponseEnum.FULL_MSG,"没有为项目简码分配文件类型！");
        }
        return reqManagerFileTypeEnums;
    }

    /**
     * 获取文件上传流
     * @param offerFlowVo
     * @param fileTypeEnums
     * @return
     * @throws Exception
     */
    public Map<String,InputStream> getUploadBhFtpInputStreams(TrustOfferFlowVo offerFlowVo,ReqManagerFileTypeEnum[] fileTypeEnums){
        Map<String,InputStream> inputStreamMap = new HashMap<>();
        for(ReqManagerFileTypeEnum fileTypeEnum:fileTypeEnums) {
            try {
                String fileName = this.getReturnMoneyConfireFileName(fileTypeEnum,offerFlowVo.getLoanBelongs(),offerFlowVo.getTradeDate());
                if (!fileTypeEnum.getFileType().equals(ReqManagerFileTypeEnum.回款确认书pdf.getFileType())) {
                    Workbook workbook = this.getWorkbookByFileType(fileTypeEnum,fileName, offerFlowVo);
                    InputStream inputStream = requestManagementService.outStreamToInSteam(workbook);
                    inputStreamMap.put(fileName,inputStream);
                    continue;
                }
                OutputStream pdfOutputStream = this.getRefundedOfMoneyConfirmationBookPdfFileStream(offerFlowVo,fileName);
                InputStream inputStream = requestManagementService.outStreamToInSteam(pdfOutputStream);
                inputStreamMap.put(fileName,inputStream);
            } catch (Exception e) {
                logger.info("获取回款确认书上传数据：{}",e.getMessage());
                loanLogService.createLog("getUploadBhFtpInputStreams", "info", Strings.format("提取日{0}获取{1}文件到渤海异常：{2}===============",
                        Dates.getDateTime(offerFlowVo.getTradeDate(),Dates.DEFAULT_DATE_FORMAT) ,fileTypeEnum.name(),e.getMessage()), "SYSTEM");
            }
        }
        return inputStreamMap;
    }

    /**
     * 获取excel workBook
     * @param fileTypeEnum
     * @param fileName
     * @param offerFlowVo
     * @return
     * @throws Exception
     */
    public Workbook getWorkbookByFileType(ReqManagerFileTypeEnum fileTypeEnum,String fileName,TrustOfferFlowVo offerFlowVo)throws Exception{
        Workbook workbook = null;
        if (fileTypeEnum.getFileCode().equals(ReqManagerFileTypeEnum.回款确认书.getFileCode())) {
            String hkqrsTemplatePath = this.getHkqrBookTemplatePath(offerFlowVo.getLoanBelongs(),".xls");
            return this.createRefundedOfMoneyConfirmationBookWorkBook(hkqrsTemplatePath, offerFlowVo);
        }
        if (fileTypeEnum.getFileCode().equals(ReqManagerFileTypeEnum.分账明细表.getFileCode())) {
            return this.getSubAccountDetailWorkbook(fileName,offerFlowVo);
        }
        if (fileTypeEnum.getFileCode().equals(ReqManagerFileTypeEnum.暂其他收款.getFileCode())) {
            return this.getTemporaryAmountWorkbook(fileName,offerFlowVo);
        }
        if (workbook == null) {
            throw new PlatformException(ResponseEnum.FULL_MSG,"找不到对应文件的workbook");
        }
        return workbook;
    }

    public OutputStream getRefundedOfMoneyConfirmationBookPdfFileStream(TrustOfferFlowVo offerFlowVo,String fileName){
        OutputStream out = null;
        FTPUtil ftpUtil = null;
        try {
            ftpUtil = connectBhxtFtpService.getFtpEsignatureConnect();
            if(ftpUtil == null){
                throw new PlatformException(ResponseEnum.FULL_MSG,"连接核心电子签章服务器失败！");
            }
            String hkqrsTemplatePath = this.getHkqrBookTemplatePath(offerFlowVo.getLoanBelongs(),".pdf");
            ByteArrayOutputStream byteArrayOutputStream = this.createRefundedOfMoneyConfirmationBookOutStream(hkqrsTemplatePath, offerFlowVo);
            InputStream inputStream = requestManagementService.outStreamToInSteam(byteArrayOutputStream);
            String projectCode = this.getProjectCode(offerFlowVo.getLoanBelongs());
            out = requestManagementService.dealEsignature(fileName, projectCode, ftpUtil, inputStream);
        }catch (Exception e){
            e.printStackTrace();
            throw new PlatformException(ResponseEnum.FULL_MSG,e.getMessage());
        }finally {
            try {
                if (ftpUtil != null) {
                    ftpUtil.closeServer();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return out;
    }

    @Override
    public boolean checkOutUploadFileData(String fundSources, Date tradeDate) {
        if (fundSources.equals(FundsSourcesTypeEnum.渤海信托.getValue())) {
            boolean isSend = this.isSendEmail(fundSources, tradeDate);
            if (isSend) {
                this.sendEmail(tradeDate);
                logger.info("渤海信托分账明细数据校验有误发送通知相关人员！");
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isSendEmail(String fundSources, Date tradeDate) {
        BigDecimal subAccountAmount = this.getSubAccountActualRepayTotalAmount(fundSources,tradeDate);
        BigDecimal repayFlowAmount = this.getRepayFlowReportActualRepayTotalAmount(fundSources,tradeDate);
        if (subAccountAmount.compareTo(repayFlowAmount) != 0) {
            return true;
        }
        return false;
    }

    /**
     * 计算分账明细中的实际还款总金额
     * @param fundSources
     * @param tradeDate
     * @return
     */
    public BigDecimal getSubAccountActualRepayTotalAmount(String fundSources, Date tradeDate){
        Date previousWorkDayDate = workDayInfoService.getPreviousWorkDayInfoByParams(tradeDate);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("loanBelongs", fundSources);
        params.put("tradeStartDate", Dates.getDateTime(previousWorkDayDate, Dates.DEFAULT_DATE_FORMAT));
        params.put("tradeEndDate", Dates.getDateTime(tradeDate, Dates.DEFAULT_DATE_FORMAT));
        params.put("tradeType", TradeTypeEnum.通联代扣.getValue());
        List<Map<String, Object>> list = this.findSubAccountDetailList(params);
        if (CollectionUtils.isEmpty(list)) {
            throw new PlatformException(ResponseEnum.FULL_MSG, "当前条件查询无数据！");
        }
        BigDecimal amonut = new BigDecimal("0.00");
        for(Map<String,Object> map : list){
            //实扣本金 + 实扣利息 + 实扣罚息 +实扣违约金 + 实还其他费用—
            amonut = amonut.add((BigDecimal)map.get("actual_principal")).add((BigDecimal)map.get("actual_interest"))
                    .add((BigDecimal)map.get("actual_fine")).add((BigDecimal)map.get("actual_penalty")).add((BigDecimal)map.get("actual_fee1"));
        }
        return amonut;
    }

    /**
     * 计算还款流水报表中实际还款总金额
     * @param fundSources
     * @param tradeDate
     * @return
     */
    public BigDecimal getRepayFlowReportActualRepayTotalAmount(String fundSources, Date tradeDate){
        BigDecimal amonut = new BigDecimal("0.00");
        Date previousWorkDayDate = workDayInfoService.getPreviousWorkDayInfoByParams(tradeDate);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("loanBelongs", fundSources);
        params.put("tradeStartDate", Dates.getDateTime(previousWorkDayDate, Dates.DEFAULT_DATE_FORMAT));
        params.put("tradeEndDate", Dates.getDateTime(tradeDate, Dates.DEFAULT_DATE_FORMAT));
        params.put("tradeType", TradeTypeEnum.通联代扣.getValue());
        amonut = trustOfferFlowDao.queryRepayFlowReportActualRepayTotalAmount(params);
        return amonut;
    }

    @Override
    public void sendEmail(Date tradeDate) {
       String email =  sysParamDefineService.getSysParamValueCache("huikuanquerenshu", "hui.kuan.que.ren.shu.email");
        String text = Strings.format("渤海信托{0}待提交的回款文件对账不成功，请查收。",Dates.getDateTime(tradeDate,Dates.DATAFORMAT_SLASHDATE));
        String content = Strings.format("渤海信托{0}待提交的回款文件对账不成功，请查收。",Dates.getDateTime(tradeDate,Dates.DATAFORMAT_SLASHDATE));
        try {
            sendMailService.sendTextMail(email, text, content);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public String getHkqrBookTemplatePath(String fundsSources, String filePostfix) {
        String templatePath = "";
        if (".pdf".equals(filePostfix)) {
            if (FundsSourcesTypeEnum.渤海信托.getValue().equals(fundsSources)){
                return BhxtHkqrBookTemplatePdf;
            }
            if (FundsSourcesTypeEnum.渤海2.getValue().equals(fundsSources)) {
                return Bh2HkqrBookTemplatePdf;
            }
            if (FundsSourcesTypeEnum.华瑞渤海.getValue().equals(fundsSources)) {
                return HrbhHkqrBookTemplatePdf;
            }
        }else if(".xls".equals(filePostfix)) {
            if (FundsSourcesTypeEnum.渤海信托.getValue().equals(fundsSources)) {
                return BhxtHkqrBookTemplateXls;
            }
            if (FundsSourcesTypeEnum.渤海2.getValue().equals(fundsSources)) {
                return Bh2HkqrBookTemplateXls;
            }
            if (FundsSourcesTypeEnum.华瑞渤海.getValue().equals(fundsSources)) {
                return HrbhHkqrBookTemplateXls;
            }
        }
        if (Strings.isEmpty(templatePath)) {
            throw new PlatformException(ResponseEnum.FULL_MSG,"找不到回款确认书模板！");
        }
        return templatePath;
    }

    @Override
    public void againUploadFailIsHHQRSToBh(JschSftpUtils jschSftpUtils) {
        List<UploadHkqrBookLog> uploadHkqrBookLogs = uploadHkqrBookLogDao.queryUploadFailLog();
        for (UploadHkqrBookLog uploadHkqrBookLog:uploadHkqrBookLogs){
            String fundSources = uploadHkqrBookLog.getFundssource();
            try {
                boolean status = this.uploadRefundedOfMoneyConfirmationFileToBh(uploadHkqrBookLog.getFundssource(), uploadHkqrBookLog.getAccountDate(), jschSftpUtils);
                if (status) {
                    this.updateUploadHKQRSLog(status,uploadHkqrBookLog);
                    loanLogService.createLog("excuteUploadRefundedOfMoneyConfirmationBookFileJob", "info", Strings.format("上传{0}回款确认书文件到渤海成功！===============", fundSources), "SYSTEM");
                    logger.info("excuteUploadRefundedOfMoneyConfirmationBookFileJob--上传{}回款确认书文件到渤海成功！===============", fundSources);
                }
            }catch (Exception e){
                e.printStackTrace();
                loanLogService.createLog("excuteUploadRefundedOfMoneyConfirmationBookFileJob", "info", Strings.format("{0}再一次上传回款确认书文件到渤海异常：{1}===============", fundSources,e.getMessage()), "SYSTEM");
                logger.info("excuteUploadRefundedOfMoneyConfirmationBookFileJob--{}上再一次传回款确认书文件到渤海异常：{}===============", fundSources,e.getMessage());
            }
        }
    }

    @Override
    public void insertUploadHKQRSLog(boolean status,String fundSources,Date tradeDate) {
        UploadHkqrBookLog uploadHkqrBookLog = new UploadHkqrBookLog();
        uploadHkqrBookLog.setId(sequencesServiceImpl.getSequences(SequencesEnum.UPLOAD_HKQR_BOOK_LOG));
        uploadHkqrBookLog.setAccountDate(tradeDate);
        uploadHkqrBookLog.setFundssource(fundSources);
        uploadHkqrBookLog.setStatus(new Short("0"));
        if (status) {
            uploadHkqrBookLog.setStatus(new Short("1"));
        }
        uploadHkqrBookLog.setUpdateTime(new Date());
        uploadHkqrBookLogDao.insert(uploadHkqrBookLog);
    }

    public int updateUploadHKQRSLog(boolean status,UploadHkqrBookLog uploadHkqrBookLog) {
        uploadHkqrBookLog.setStatus(new Short("0"));
        if (status) {
            uploadHkqrBookLog.setStatus(new Short("1"));
        }
        uploadHkqrBookLog.setUpdateTime(new Date());
        int count = uploadHkqrBookLogDao.update(uploadHkqrBookLog);
        return count;
    }

    public Date getRefundedOfMoneyConfirmationTradeBeginDate(Date tradeDate,String loanBelongs){
        Date tradeBeginDate = null;
        if (loanBelongs.equals(FundsSourcesTypeEnum.华瑞渤海.getValue())) {
            if (workDayInfoService.isFirstWorkDay(tradeDate)){
                tradeBeginDate = workDayInfoService.getPreviousWorkDayInfoByParams(tradeDate);
            }else {
                tradeBeginDate = Dates.getBeforeDays(tradeDate, 1);
            }
        }else {
            tradeBeginDate = workDayInfoService.getPreviousWorkDayInfoByParams(tradeDate);
        }
        return tradeBeginDate;
    }

    @Override
    public void firstWorkDayUploadRefundedOfMoneyConfirmationFileToBh(String fundSources, Date tradeDate, JschSftpUtils jschSftpUtils) {
        Date previousWorkDay = workDayInfoService.getPreviousWorkDayInfoByParams(tradeDate);
        int nonWorkDays = Dates.dateDiff(previousWorkDay,tradeDate);
        for(int i = 0;i <= nonWorkDays;i++){
            try {
                Date submitDay = Dates.addDay(previousWorkDay,i);
                if (this.fundsSourceIsUpload(fundSources, submitDay)) {
                    continue;
                }
                boolean status = this.uploadRefundedOfMoneyConfirmationFileToBh(fundSources, submitDay, jschSftpUtils);
                this.insertUploadHKQRSLog(status, fundSources, submitDay);
                if (status) {
                    loanLogService.createLog("firstWorkDayUploadRefundedOfMoneyConfirmationBook", "info", Strings.format("上传{0}回款确认书文件到渤海成功！===============", fundSources), "SYSTEM");
                    logger.info("firstWorkDayUploadRefundedOfMoneyConfirmationBook--上传{}回款确认书文件到渤海成功！===============", fundSources);
                    continue;
                }
                loanLogService.createLog("firstWorkDayUploadRefundedOfMoneyConfirmationBook", "info", Strings.format("上传{0}回款确认书文件到渤海失败！===============", fundSources), "SYSTEM");
                logger.info("firstWorkDayUploadRefundedOfMoneyConfirmationBook--上传{}回款确认书文件到渤海失败！===============", fundSources);
            }catch (Exception e){
                e.printStackTrace();
                loanLogService.createLog("firstWorkDayUploadRefundedOfMoneyConfirmationBook", "info", Strings.format("{0}上传回款确认书文件到渤海异常：{1}===============", fundSources,e.getMessage()), "SYSTEM");
                logger.info("firstWorkDayUploadRefundedOfMoneyConfirmationBook--{}上传回款确认书文件到渤海异常：{}===============", fundSources,e.getMessage());
            }
        }
    }

    @Override
    public boolean fundsSourceIsUpload(String fundsSource, Date tradeDate) {
        UploadHkqrBookLog uploadHkqrBookLog = uploadHkqrBookLogDao.queryUploadLog(fundsSource, tradeDate);
        if (uploadHkqrBookLog == null) {
            return false;
        }
        return true;
    }

    class SortByContractNumAndTradate implements Comparator{
        @Override
        public int compare(Object o1, Object o2) {
            Map<String,Object> o1Map = (Map<String, Object>) o1;
            Map<String,Object> o2Map = (Map<String, Object>) o2;
            String contract_num1 = (String)o1Map.get("contract_num");
            String contract_num2 = (String)o2Map.get("contract_num");
            if (contract_num1.compareTo(contract_num2)==0) {
                String tradate1 = (String) o1Map.get("actual_trade_date");
                String tradate2 = (String)o2Map.get("actual_trade_date");
                return  tradate1.compareTo(tradate2);
            }else {
                return contract_num1.compareTo(contract_num2);
            }
        }
    }
    class SortByContractNum implements Comparator{
        @Override
        public int compare(Object o1, Object o2) {
            TemporaryAmountVo temporaryAmountVo1 = (TemporaryAmountVo) o1;
            TemporaryAmountVo temporaryAmountVo2 = (TemporaryAmountVo) o2;
            String contract_num1 = temporaryAmountVo1.getContractNum();
            String contract_num2 = temporaryAmountVo2.getContractNum();
            if (contract_num1.compareTo(contract_num2)==0) {
                Date tradate1 = temporaryAmountVo1.getTradeDate();
                Date tradate2 = temporaryAmountVo2.getTradeDate();
                return  tradate1.compareTo(tradate2);
            }else {
                return contract_num1.compareTo(contract_num2);
            }
        }
    }
    
    public List<Map<String, Object>> findSplitDetailList(Map<String, Object> params) {
        String loanBelongs = (String) params.get("loanBelongs");
        if (FundsSourcesTypeEnum.渤海2.getValue().equals(loanBelongs) || FundsSourcesTypeEnum.华瑞渤海.getValue().equals(loanBelongs)) {
            return offerRepayInfoDao.findSubAccountDetailList(params);
        } else if (FundsSourcesTypeEnum.渤海信托.getValue().equals(loanBelongs)) {
            return offerRepayInfoDao.findSubAccountDetailList4BHXT(params);
        } 
        return null;
    }

    @Override
    public void adjustDebitOfferFlowBatNo(List<DebitOfferFlow> list) {
        List<Long> loanIds = new ArrayList<>();
        Map<Long, BigDecimal> amtMap = new HashMap<>();
        Map<Long, String> batMap = new HashMap<>();
        for(DebitOfferFlow debitOfferFlow : list){
            String subjType = debitOfferFlow.getSubjType();
            Long loanId = debitOfferFlow.getLoanId();
            if(!amtMap.containsKey(loanId)){
                amtMap.put(loanId,new BigDecimal(0.00));
            }
            if(!loanIds.contains(loanId)){
                loanIds.add(loanId);
            }
            if(subjType.equals(SubjTypeWm3Enum.实收本金.getCode()) || subjType.equals(SubjTypeWm3Enum.实收利息.getCode())
                    || subjType.equals(SubjTypeWm3Enum.实收罚息.getCode()) || subjType.equals(SubjTypeWm3Enum.代收违约金.getCode())){
                BigDecimal  repayAmt = amtMap.get(loanId);
                repayAmt = repayAmt.add(debitOfferFlow.getSubjAmt());
                amtMap.put(loanId,repayAmt);
            }
        }
        String batNo = "";
        for(int i=0;i<loanIds.size();i++){
            if(i%BATCH_CONTRACT_SIZE == 0){
                batNo = "wm3_" + System.currentTimeMillis()+i;
            }
            batMap.put(loanIds.get(i),batNo);
        }
        for(DebitOfferFlow debitOfferFlow : list){
            debitOfferFlow.setRepayAmt(amtMap.get(debitOfferFlow.getLoanId()));
            debitOfferFlow.setBatNo(batMap.get(debitOfferFlow.getLoanId()));
        }
    }

    @Override
    public BigDecimal getAmountByAccTitleTradeType(List<TrustOfferFlow> trustOfferFlows, List<String> accTitles, List<String> tradeTypes) {
        BigDecimal amount = new BigDecimal("0.00");
        if (CollectionUtils.isEmpty(trustOfferFlows) || CollectionUtils.isEmpty(accTitles)) {
            return amount;
        }
        for (TrustOfferFlow trustOfferFlow:trustOfferFlows) {
            if (accTitles.contains(trustOfferFlow.getAcctTitle()) && trustOfferFlow.getTradeAmount() != null && tradeTypes.contains(trustOfferFlow.getTradeType())) {
                amount = amount.add(trustOfferFlow.getTradeAmount());
            }
        }
        return amount;
    }
}
