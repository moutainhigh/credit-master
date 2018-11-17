package com.zdmoney.credit.loan.service;

import com.zdmoney.credit.common.constant.*;
import com.zdmoney.credit.common.constant.flow.OverDueGradeEnum;
import com.zdmoney.credit.common.constant.flow.ReliefAmountGradeEnum;
import com.zdmoney.credit.common.constant.system.LogLevel;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.util.*;
import com.zdmoney.credit.common.vo.core.TrailVO;
import com.zdmoney.credit.flow.dao.pub.ISpecialReliefPercentDao;
import com.zdmoney.credit.loan.dao.pub.*;
import com.zdmoney.credit.loan.domain.*;
import com.zdmoney.credit.loan.service.pub.*;
import com.zdmoney.credit.loan.vo.*;
import com.zdmoney.credit.offer.dao.pub.IOfferFlowDao;
import com.zdmoney.credit.offer.domain.OfferRepayInfo;
import com.zdmoney.credit.offer.service.pub.IOfferInfoService;
import com.zdmoney.credit.offer.service.pub.IOfferRepayInfoService;
import com.zdmoney.credit.offer.vo.RepaymentInputVo;
import com.zdmoney.credit.system.dao.pub.IComOrganizationDao;
import com.zdmoney.credit.system.domain.ComOrganization;
import com.zdmoney.credit.system.service.pub.ISequencesService;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ym10094 on 2017/5/16.
 */
@Service
public class SpecialRepaymentApplyServiceImpl implements ISpecialRepaymentApplyService {
    private static Logger logger = LoggerFactory.getLogger(SpecialRepaymentApplyServiceImpl.class);
/*    private static String reliefPercentTermRange1 = "7, 8,9";
    private static String reliefPercentTermRange2 = "10,11,12";
    private static String reliefPercentTermRange3 = "13,14,15,16,17,18";
    private static String reliefPercentTermRange4 = "19,20,21,22,23,24";
    private static String reliefPercentTermRange5 = "25,26,27,28,29,30,31,32,33,34,35";*/

    @Autowired
    private ISpecialRepaymentApplyDao specialRepaymentApplyDao;
    @Autowired
    private IVLoanInfoService vLoanInfoServiceImpl;
    @Autowired
    private IAfterLoanService afterLoanService;
    @Autowired
    private IOfferInfoService offerInfoService;
    @Autowired
    ISequencesService sequencesServiceImpl;
    @Autowired
    ILoanSpecialRepaymentService loanSpecialRepaymentServiceImpl;
    @Autowired
    IOfferRepayInfoService offerRepayInfoService;
    @Autowired
    ILoanRepaymentDetailService loanRepaymentDetailService;
    @Autowired
    private ILoanPreSettleService loanPreSettleService;
    @Autowired
    private ISpecialRepaymentRelationdDao specialRepaymentRelationdDao;
    @Autowired
    private IOfferFlowDao offerFlowDao;
    @Autowired
    private IVLoanInfoDao ivLoanInfoDao;
    @Autowired
    private ILoanSpecialRepaymentDao loanSpecialRepaymentDao;
    @Autowired
    private ISpecialTradeRelationDao specialTradeRelationDao;
    @Autowired
    private ILoanSpecialRepaymentDao specialRepaymentDao;
    @Autowired
    private ILoanBaseDao loanBaseDao;
    @Autowired
    private IComOrganizationDao comOrganizationDao;
    @Autowired
    private ISpecialReliefPercentDao specialReliefPercentDao;
    @Autowired
    private IAfterLoanService afterLoanServiceImpl;
    @Autowired 
    ILoanTransferInfoService loanTransferInfoServiceImpl;
    @Override
    public boolean isExistEffectiveSatusSpecialRepaymentApply(long loanId) {
        List<SpecialRepaymentApply> specialRepaymentApplies = this.getEffectiveSatusSpecialRepaymentApply(loanId);
        if (CollectionUtils.isEmpty(specialRepaymentApplies)) {
            return false;
        }
        return true;
    }

    @Override
    public  List<SpecialRepaymentApply> getEffectiveSatusSpecialRepaymentApply(long loanId) {
        Map<String,Object> params = new HashMap<>();
        String [] applicationStatusArrays = {SpecialRepaymentApplyStatus.申请.getCode(),SpecialRepaymentApplyStatus.通过.getCode(),SpecialRepaymentApplyStatus.生效.getCode()};
        params.put("loanId",loanId);
        params.put("applicationStatusArrays",applicationStatusArrays);
        List<SpecialRepaymentApply> specialRepaymentApplies = specialRepaymentApplyDao.querySpecialRepaymentApplyByApply(params);
        return specialRepaymentApplies;
    }

    @Override
    public SpecialRepaymentApply getApplyReliefStatusPass(long loanId) {
        List<SpecialRepaymentApply> specialRepaymentApplies = specialRepaymentApplyDao.querySpecialRepaymentApplyByApplyApplicationStatus(loanId,SpecialRepaymentApplyStatus.通过.getCode());
        if (specialRepaymentApplies.size()>0) {
            return  specialRepaymentApplies.get(0);
        }
        return null;
    }

    @Override
    public void checkOutSpecialRepaymentApply(SpecialRepaymentApplyVo specialRepaymentApplyVo) {
        long loanId = specialRepaymentApplyVo.getLoanId();
        VLoanInfo vLoanInfo = vLoanInfoServiceImpl.findByLoanId(loanId);
        //检查该借款人是否有已转让过的债权
        boolean flag = loanTransferInfoServiceImpl.isLoanTransfer(null,vLoanInfo.getId());
        if(!flag){
        	 throw new PlatformException(ResponseEnum.FULL_MSG,Strings.errorMsg);
        }
        if (vLoanInfo.getLoanState().equals(LoanStateEnum.结清.getValue())) {
            throw new PlatformException(ResponseEnum.FULL_MSG,"债权已结清不允许申请");
        }
        if (LoanStateEnum.逾期.getValue().equals(vLoanInfo.getLoanState()) && !this.isM1(loanId)) {
            throw new PlatformException(ResponseEnum.FULL_MSG,"非M1逾期,不允许申请");
        }
        if (this.isExistEffectiveSatusSpecialRepaymentApply(loanId)){
            throw new PlatformException(ResponseEnum.FULL_MSG,"有在途的申请减免未完成,不允许再申请");
        }
        String specialRepaymentApplyType = specialRepaymentApplyVo.getApplyType();
        if (SpecialRepaymentApplyTeyps.结清减免.getCode().equals(specialRepaymentApplyType)) {
            //结清减免
            /** 查看是否申请(一次性还款) **/
            if (!afterLoanService.isOneTimeRepayment(loanId)) {
                throw new PlatformException(ResponseEnum.FULL_MSG,"申请结清减免必须先申请提前结清");
            }
        }else {
            //一般减免
            if (vLoanInfo.getLoanState().equals(LoanStateEnum.正常.getValue())) {
                throw new PlatformException(ResponseEnum.FULL_MSG,"正常还款不需要申请减免");
            }
        }
    }

    @Override
    public BigDecimal calculateMaxReliefMoney(ReliefAmountCalculateVo reliefAmountCalculateVo, String specialRepaymentApplyType,String isSpecial) {
        BigDecimal maxReliefMoney = new BigDecimal(0.0);
        if (reliefAmountCalculateVo == null) {
            return  maxReliefMoney;
        }
        if (Strings.isNotEmpty(isSpecial) && SpecialReliefTypeEnum.特殊减免.getCode().equals(isSpecial)) {
            return reliefAmountCalculateVo.getShouldRepayMoney();
        }
        //一般减免
        if (specialRepaymentApplyType.equals(SpecialRepaymentApplyTeyps.一般减免.getCode())) {
            //最大申请金额 = 当前逾期已收罚息 + 当前应还罚息
            maxReliefMoney = reliefAmountCalculateVo.getAlreadyPayFine().add(reliefAmountCalculateVo.getFine());
            return maxReliefMoney;
        }
        //结清减免
        //最大减免金额 = 当前应还金额
        maxReliefMoney = reliefAmountCalculateVo.getShouldRepayMoney();
        return maxReliefMoney;
    }

    @Override
    public BigDecimal calculateAlreadyRepayTotalMoney(String currentTerm, Long loanId) {
        BigDecimal amount = offerFlowDao.getCurrenTermAlreadyRepayTotalMoney(loanId,currentTerm);
        BigDecimal accAmount = afterLoanService.getAccAmount(loanId);
        amount = amount.add(accAmount);
        return amount;
    }

    @Override
    public BigDecimal calculateHistoryAlreadyRepayTotalMoney(Long loanId) {
        BigDecimal amount = new BigDecimal(0.0);
        Map<String,Object> params = new HashMap<>();
        params.put("loanId",loanId);
        List<OfferRepayInfo> offerRepayInfoList = offerRepayInfoService.findListByMap(params);
        if (CollectionUtils.isEmpty(offerRepayInfoList)) {
            return amount;
        }
        for(OfferRepayInfo offerRepayInfo:offerRepayInfoList) {
            if (offerRepayInfo.getTradeCode().equals(Const.TRADE_CODE_ONEOFF) || offerRepayInfo.getTradeCode().equals(Const.TRADE_CODE_NORMAL)) {
                amount = amount.add(offerRepayInfo.getAmount());
            }
        }
        return amount;
    }

    @Override
    public int historyOverDueTime(Long loanId) {
        Map<String,Object> params = new HashMap<>();
        params.put("loanId",loanId);
        params.put("repaymentStates",new String[]{RepaymentStateEnum.结清.name()});
        List<LoanRepaymentDetail>  loanRepaymentDetails = loanRepaymentDetailService.findByLoanIdAndRepaymentState(params);
        if (CollectionUtils.isEmpty(loanRepaymentDetails)) {
            return 0;
        }
        int overDueTime = 0;
        for(LoanRepaymentDetail loanRepaymentDetail:loanRepaymentDetails) {
            if (loanRepaymentDetail.getFactReturnDate().compareTo(loanRepaymentDetail.getReturnDate()) == 1) {
                overDueTime = overDueTime + 1;
            }
        }
        return overDueTime;
    }

    @Override
    public ReliefAmountCalculateVo getRelieCalculateAmount(RelieCalculateAmountParamVo relieCalculateAmountParamVo) {
        long loanId = relieCalculateAmountParamVo.getLoanId();
        Date tradeDate = relieCalculateAmountParamVo.getTradeDate();
        BigDecimal repayAmount = relieCalculateAmountParamVo.getRepayAmount();
        String isPecial = relieCalculateAmountParamVo.getIsPecial();
        String reliefType = relieCalculateAmountParamVo.getApplyType();
        List<LoanRepaymentDetail> repayList= afterLoanService.getAllInterestOrLoan(tradeDate,loanId);
        if (CollectionUtils.isEmpty(repayList)) {
            throw new PlatformException(ResponseEnum.FULL_MSG,"还款正常无还款计划，不可进行减免操作");
        }
        //本次逾期期数中，最早那期的期数
        String firstOverTerm = repayList.get(0).getCurrentTerm().toString();
        VLoanInfo vLoanInfo = vLoanInfoServiceImpl.findByLoanId(loanId);
        String repayType = RepayTypeEnum.normal.name();
        if (afterLoanService.isOneTimeRepayment(loanId)) {
            repayType = RepayTypeEnum.onetime.name();
        }
        /** 调用还款试算接口 **/
        TrailVO trailVO =offerInfoService.GetRepaymentInfoForTrail(vLoanInfo,tradeDate,repayType);
        //实际还款金额
        BigDecimal realityMoney = new BigDecimal(0.0);
        //对应累计已还金额 = offfer_flow用户已还金额的钱 含当期充值金额
        BigDecimal  alreadyRepayTotalMoney = this.calculateAlreadyRepayTotalMoney(firstOverTerm,loanId);
        //历史对应累计已还金额 = offfer_flow用户已还金额的钱 不含当前充值金额
        BigDecimal historyAlreadyRepayTotalMoney = alreadyRepayTotalMoney;
        if (repayAmount != null && repayAmount.compareTo(new BigDecimal(0.0)) == 1) {
            alreadyRepayTotalMoney = alreadyRepayTotalMoney.add(repayAmount);
            realityMoney = repayAmount;
        }
        //逾期应还 = 逾期利息 + 逾期本金 + 罚息
        BigDecimal  overShouldRepayMoney = trailVO.getOverInterest().add(trailVO.getOverCorpus()).add(trailVO.getFine());
        //逾期期数
//        int overDueTime = trailVO.getOverTerm();
        //月还款额 = 期款
       BigDecimal everyMonthRepayMoney = repayList.get(0).getReturneterm();
        LoanRepaymentDetail lastRepaymentDetail = repayList.get(repayList.size()-1);
        //对应还金额 = 当前应还金额
        BigDecimal shouldRepayMoney = trailVO.getCurrAmount();
        if (Strings.isNotEmpty(isPecial) && Strings.isNotEmpty(reliefType) && SpecialReliefTypeEnum.特殊减免.getCode().equals(isPecial)
                && SpecialRepaymentApplyTeyps.一般减免.getCode().equals(reliefType)) {
            //债权状态“正常” 用户申请提前扣款
            if (LoanStateEnum.正常.getValue().equals(vLoanInfo.getLoanState()) && afterLoanService.isAdvanceRepayment(loanId)
                    && tradeDate.compareTo(lastRepaymentDetail.getReturnDate()) != 0) {
                shouldRepayMoney = lastRepaymentDetail.getDeficit().subtract(trailVO.getAccAmount());
            }
            //债权状态“逾期” 正好是端口日 不包含当期应还金额
            if (LoanStateEnum.逾期.getValue().equals(vLoanInfo.getLoanState()) && tradeDate.compareTo(lastRepaymentDetail.getReturnDate()) == 0) {
                shouldRepayMoney = overShouldRepayMoney;
            }
        }
        /** 申请减免罚息金额 **/
//        BigDecimal relief = afterLoanService.getReliefOfFine(tradeDate,loanId);
        // 应还金额不包含当前
/*        BigDecimal shouldRepayMoneyExcludeCurrentTerm = trailVO.getOverDueAmount().add(trailVO.getFine()).subtract(trailVO.getAccAmount()).subtract(relief);
        if (shouldRepayMoneyExcludeCurrentTerm.compareTo(BigDecimal.ZERO) == -1) {
            shouldRepayMoneyExcludeCurrentTerm = BigDecimal.ZERO;
        }*/
        //当前逾期已收罚息
        BigDecimal alreadyPayFine = offerFlowDao.getAlreadyPayFineAmount(loanId,firstOverTerm);
        ReliefAmountCalculateVo reliefAmountCalculateVo = new ReliefAmountCalculateVo();
//        reliefAmountCalculateVo.setAlreadyRepayTotalMoney(alreadyRepayTotalMoney);
        reliefAmountCalculateVo.setOverShouldRepayMoney(overShouldRepayMoney);
        reliefAmountCalculateVo.setEveryMonthRepayMoney(everyMonthRepayMoney);
        reliefAmountCalculateVo.setShouldRepayMoney(shouldRepayMoney);
        reliefAmountCalculateVo.setRealityMoney(realityMoney);
        reliefAmountCalculateVo.setHistoryAlreadyRepayTotalMoney(historyAlreadyRepayTotalMoney);
//        reliefAmountCalculateVo.setShouldRepayMoneyExcludeCurrentTerm(shouldRepayMoneyExcludeCurrentTerm);
        //应还罚息
        reliefAmountCalculateVo.setFine(trailVO.getFine());
        reliefAmountCalculateVo.setAlreadyPayFine(alreadyPayFine);
        return reliefAmountCalculateVo;
    }

    @Override
    public Pager queryAppltReliefPage(Map<String, Object> params) {
        Pager pager = specialRepaymentApplyDao.queryApplyReliefPage(params);
        return pager;
    }

    @Override
    public VloanPersonInfo checkLoanPersonInfo(String contractNum, String idNum, String name) {

        Map<String,Object> params = new HashMap<>();
        params.put("contractNum",contractNum);
        params.put("idNum",idNum);
        VloanPersonInfo vloanPersonInfo = ivLoanInfoDao.findImportLoanInfo(params);
        if (vloanPersonInfo == null) {
            throw new PlatformException(ResponseEnum.FULL_MSG,"合同编号、客户姓名、证件号码不匹配！");
        }
        if (!vloanPersonInfo.getName().equals(name)) {
            throw new PlatformException(ResponseEnum.FULL_MSG,"合同编号、客户姓名、证件号码不匹配！");
        }
        return vloanPersonInfo;
    }

    @Override
    public BigDecimal historyReliefAmount(Long loanId) {
        return offerFlowDao.getHistoryReliefAmount(loanId);
    }

    @Override
    public int historyReliefTime(Long loanId) {
        return offerFlowDao.getHistoryReleifTime(loanId);
    }

    private LoanSpecialRepayment insertRelieFlowRecord(SpecialRepaymentApply specialRepaymentApply){
        Date requestDate = Dates.getCurrDate();
        this.updateSpecialRepaymentApply(specialRepaymentApply);
        VLoanInfo loanInfo = vLoanInfoServiceImpl.findByLoanId(specialRepaymentApply.getLoanId());
        if (!Strings.isEmpty(specialRepaymentApply.getApplyType())){
            if (specialRepaymentApply.getApplyType().equals(SpecialRepaymentApplyTeyps.结清减免.getCode()) && !FundsSourcesTypeEnum.陆金所.getValue().equals(loanInfo.getLoanBelong())) {
                requestDate = afterLoanService.getCurrTermReturnDate(Dates.getCurrDate(), loanInfo.getPromiseReturnDate());
            }
            if (LoanStateEnum.正常.getValue().equals(loanInfo.getLoanState()) && specialRepaymentApply.getApplyType().equals(SpecialRepaymentApplyTeyps.一般减免.getCode())
                    && !FundsSourcesTypeEnum.陆金所.getValue().equals(loanInfo.getLoanBelong())) {
                requestDate = afterLoanService.getCurrTermReturnDate(Dates.getCurrDate(), loanInfo.getPromiseReturnDate());
            }
        }
        LoanSpecialRepayment loanSpecialRepayment = this.insertLoanSpecialRepayment(specialRepaymentApply.getLoanId(),specialRepaymentApply.getEffectMoney(),specialRepaymentApply.getProposerId(),requestDate);
        if (loanSpecialRepayment != null) {
            this.insertSpecialRepaymentRelationd(specialRepaymentApply.getId(),loanSpecialRepayment.getId());
        }
        return loanSpecialRepayment;
    }

    private LoanSpecialRepayment insertLoanSpecialRepayment(Long loanId,BigDecimal moneyValue,Long proposerId,Date requestDate) {
        LoanSpecialRepayment reliefPenaltyStateObj = new LoanSpecialRepayment();
        reliefPenaltyStateObj.setId(sequencesServiceImpl.getSequences(SequencesEnum.LOAN_SPECIAL_REPAYMENT));
        reliefPenaltyStateObj.setLoanId(loanId);
        reliefPenaltyStateObj.setAmount(moneyValue);
        reliefPenaltyStateObj.setSpecialRepaymentType(SpecialRepaymentTypeEnum.减免.name());
        reliefPenaltyStateObj.setSpecialRepaymentState(SpecialRepaymentStateEnum.申请.name());
        reliefPenaltyStateObj.setProposerId(proposerId);
        reliefPenaltyStateObj.setRequestDate(requestDate);
        loanSpecialRepaymentDao.insert(reliefPenaltyStateObj);
        return reliefPenaltyStateObj;
    }
    @Override
    public SpecialRepaymentApply insertSpecialRepaymentApply(SpecialRepaymentApply specialRepaymentApply){
        specialRepaymentApply.setId(sequencesServiceImpl.getSequences(SequencesEnum.SPECIAL_REPAYMENT_APPLY));
        SpecialRepaymentApply sprapply = specialRepaymentApplyDao.insert(specialRepaymentApply);
        return sprapply;
    }
    @Override
    public int updateSpecialRepaymentApply(SpecialRepaymentApply specialRepaymentApply){
        User user = UserContext.getUser();
        if (user != null) {
            specialRepaymentApply.setUpdator(user.getName());
        }
        specialRepaymentApply.setUpdateTime(new Date());
        int count = specialRepaymentApplyDao.update(specialRepaymentApply);
        return count;
    }

    public void insertSpecialRepaymentRelationd(Long applyId,Long effectiveId){
        SpecialRepaymentRelation specialRepaymentRelation = new SpecialRepaymentRelation();
        specialRepaymentRelation.setId(sequencesServiceImpl.getSequences(SequencesEnum.SPECIAL_REPAYMENT_RELATION));
        specialRepaymentRelation.setApplyId(applyId);
        specialRepaymentRelation.setEffectiveId(effectiveId);
        specialRepaymentRelationdDao.insert(specialRepaymentRelation);
    }

    @Override
    public SpecialRepaymentApply calculateEffectTotalMoney(ReliefAmountCalculateVo reliefAmountCalculateVo,SpecialRepaymentApply specialRepaymentApply) {
        BigDecimal realityMoney = reliefAmountCalculateVo.getRealityMoney();
        BigDecimal applyAmount = specialRepaymentApply.getApplyAmount();
        BigDecimal effectiveAmount = applyAmount;
        //记录下生效是的 应该总金额
        specialRepaymentApply.setRepayAmount(reliefAmountCalculateVo.getShouldRepayMoney());
        //一般减免 生效规则
        if (SpecialRepaymentApplyTeyps.一般减免.getCode().equals(specialRepaymentApply.getApplyType()) && SpecialReliefTypeEnum.非特殊减免.getCode().equals(specialRepaymentApply.getIsSpecial())) {
            //临时金额 = 期款（每期应还的本金利息） - 已收罚息 - 已收期款（已收本金利息） - 挂账
            BigDecimal tempAmount = reliefAmountCalculateVo.getEveryMonthRepayMoney().subtract(reliefAmountCalculateVo.getHistoryAlreadyRepayTotalMoney());
            if (realityMoney.compareTo(tempAmount) != -1) {
                specialRepaymentApply.setEffectMoney(effectiveAmount);
                specialRepaymentApply.setApplicationStatus(SpecialRepaymentApplyStatus.生效.getCode());
                return specialRepaymentApply;
            }
            return specialRepaymentApply;
        }
/*        if (SpecialRepaymentApplyTeyps.一般减免.getCode().equals(specialRepaymentApply.getApplyType()) && SpecialReliefTypeEnum.特殊减免.getCode().equals(specialRepaymentApply.getIsSpecial())) {
            //申请多少生效多少：
            specialRepaymentApply.setEffectMoney(effectiveAmount);
            specialRepaymentApply.setApplicationStatus(SpecialRepaymentApplyStatus.生效.getCode());
            return specialRepaymentApply;
        }*/
        //结清减免生效规则
        BigDecimal shouldRepayMoney = reliefAmountCalculateVo.getShouldRepayMoney();
        if (realityMoney.add(applyAmount).compareTo(shouldRepayMoney) != -1) {
            specialRepaymentApply.setEffectMoney(effectiveAmount);
            specialRepaymentApply.setApplicationStatus(SpecialRepaymentApplyStatus.生效.getCode());
            return specialRepaymentApply;
        }

        return specialRepaymentApply;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED)
    public OfferRepayInfo dealReliefMoneyInputAndRepaymentInput(SpecialRepaymentApply specialRepaymentApply, RepaymentInputVo repaymentInputVo, ReliefAmountCalculateVo reliefAmountCalculateVo) {
        OfferRepayInfo offerRepayInfo = new OfferRepayInfo();
        Assert.notNull(repaymentInputVo, "还款数据录入对象不能为空！");
        Assert.notNull(specialRepaymentApply, "申请减免数据对象不能为空！");
        Long loanId = specialRepaymentApply.getLoanId();
        //是否生效
        boolean isEffect = specialRepaymentApply.getApplicationStatus().equals(SpecialRepaymentApplyStatus.生效.getCode()) && specialRepaymentApply.getEffectMoney().compareTo(new BigDecimal(0.0)) == 1;
        if (!isEffect) {
            //申请状态为 失效 或者 通过的
            if (repaymentInputVo.getAmount().compareTo(new BigDecimal(0.0)) == 1) {
                //若 交易金额小于 或等 0 不调用核心还款接口 入账处理
                /** 调用核心层接口（还款） **/
                offerRepayInfo = offerRepayInfoService.repaymentInputCore(repaymentInputVo);
            }
            if (specialRepaymentApply.getApplicationStatus().equals(SpecialRepaymentApplyStatus.失效.getCode())) {
                //失效 把申请减免置为失效
                this.updateSpecialRepaymentApply(specialRepaymentApply);
            }
            return offerRepayInfo;
        }
//        if (!afterLoanService.isOneTimeRepayment(loanId)) {
        if(SpecialRepaymentApplyTeyps.一般减免.getCode().equals(specialRepaymentApply.getApplyType())){
            //未申请过一次性结清金额(一般减免)
            offerRepayInfo = this.dealGeneralReliefMoneyInputAndRepaymentInput(specialRepaymentApply, repaymentInputVo);
            return offerRepayInfo;
        }
        //申请过一次性结清金额(结清减免)
        VLoanInfo vLoanInfo = vLoanInfoServiceImpl.findByLoanId(loanId);
        if (!LoanStateEnum.逾期.getValue().equals(vLoanInfo.getLoanState())) {
            //非逾期一次性结清申请减免(正常一次性结清)
/*            if (Strings.isEmpty(specialRepaymentApply.getApplyType())) {
                //催收 正常一次性结清
                specialRepaymentApply.setApplyType(SpecialRepaymentApplyTeyps.结清减免.getCode());
            }*/
            offerRepayInfo = this.dealGeneralReliefMoneyInputAndRepaymentInput(specialRepaymentApply, repaymentInputVo);
            if (FundsSourcesTypeEnum.陆金所.getValue().equals(vLoanInfo.getLoanBelong())) {
                return offerRepayInfo;
            }
            loanPreSettleService.creartPreSettle(vLoanInfo, repaymentInputVo.getTradeDate());
            loanBaseDao.updateLoanState(specialRepaymentApply.getLoanId(), LoanStateEnum.预结清.getValue());
            return offerRepayInfo;
        }
        //逾期一次性结清申请减免
        BigDecimal overShouldRepayMoney = reliefAmountCalculateVo.getOverShouldRepayMoney();//逾期应还金额
        BigDecimal effectMoney = specialRepaymentApply.getEffectMoney();//生效金额
        if (overShouldRepayMoney.compareTo(effectMoney) != -1) {
            specialRepaymentApply.setApplyType(SpecialRepaymentApplyTeyps.一般减免.getCode());
            offerRepayInfo = this.dealGeneralReliefMoneyInputAndRepaymentInput(specialRepaymentApply, repaymentInputVo);
            return offerRepayInfo;
        }
        //申请是的总结清减免 拆分成 一般 跟 结清
        SpecialRepaymentApply specialToTalSettle = new SpecialRepaymentApply();
        org.springframework.beans.BeanUtils.copyProperties(specialRepaymentApply,specialToTalSettle);
        specialToTalSettle.setApplicationStatus(SpecialRepaymentApplyStatus.完成.getCode());
        specialToTalSettle.setFlag("1");//不显示
        this.updateSpecialRepaymentApply(specialToTalSettle);
        //一般减免
        SpecialRepaymentApply specialGeneral = new SpecialRepaymentApply();
        org.springframework.beans.BeanUtils.copyProperties(specialRepaymentApply,specialGeneral);
        specialGeneral.setApplyType(SpecialRepaymentApplyTeyps.一般减免.getCode());
        specialGeneral.setEffectMoney(overShouldRepayMoney);
        this.insertSpecialRepaymentApply(specialGeneral);
        offerRepayInfo = this.dealGeneralReliefMoneyInputAndRepaymentInput(specialGeneral, repaymentInputVo);
        //结清减免
        //一次性结清减免生效金额 = 减免生效金额 – 对应逾期金额
        effectMoney = effectMoney.subtract(overShouldRepayMoney);
        SpecialRepaymentApply specialSettle = new SpecialRepaymentApply();
        org.springframework.beans.BeanUtils.copyProperties(specialRepaymentApply,specialSettle);
        specialSettle.setEffectMoney(effectMoney);
        specialSettle.setApplyType(SpecialRepaymentApplyTeyps.结清减免.getCode());
        this.insertSpecialRepaymentApply(specialSettle);
        this.insertRelieFlowRecord(specialSettle);
        if (FundsSourcesTypeEnum.陆金所.getValue().equals(vLoanInfo.getLoanBelong())) {
            repaymentInputVo.setAmount(new BigDecimal(0.00));
            /** 调用核心层接口（还款） **/
            offerRepayInfo = offerRepayInfoService.repaymentInputCore(repaymentInputVo);
            return offerRepayInfo;
        }
        loanPreSettleService.creartPreSettle(vLoanInfo, repaymentInputVo.getTradeDate());
        loanBaseDao.updateLoanState(specialSettle.getLoanId(), LoanStateEnum.预结清.getValue());
        return offerRepayInfo;
    }

    /**
     *  新增减免生效金额记录和还款录入处理（针对的减免申请状态必然是已经生效的）
     * @param specialRepaymentApply
     * @param repaymentInputVo
     */
    public OfferRepayInfo dealGeneralReliefMoneyInputAndRepaymentInput(SpecialRepaymentApply specialRepaymentApply, RepaymentInputVo repaymentInputVo){
        OfferRepayInfo offerRepayInfo = new OfferRepayInfo();
        LoanSpecialRepayment loanSpecialRepayment = this.insertRelieFlowRecord(specialRepaymentApply);
        //判断是否应该实时入账
        if (Dates.getCurrDate().compareTo(loanSpecialRepayment.getRequestDate()) == 0 || repaymentInputVo.getAmount().compareTo(new BigDecimal(0.0)) == 1) {
            /** 调用核心层接口（还款） **/
            offerRepayInfo = offerRepayInfoService.repaymentInputCore(repaymentInputVo);
        }
        return offerRepayInfo;
    }

    @Override
    public SpecialTradeRelation insetSpecialTradeRelation(String tradeNo,Long loanId) {
        List<SpecialRepaymentRelation> specialRepaymentRelationList = specialRepaymentRelationdDao.querySpecialRepaymentRelation(loanId, SpecialRepaymentApplyStatus.生效.getCode());
        if (CollectionUtils.isEmpty(specialRepaymentRelationList)){
            //不存在生效的申请减免 既用户没有申请减免 或者 申请减免不生效
            return null;
        }
       if (specialRepaymentRelationList.size()>1) {
            throw new PlatformException(ResponseEnum.FULL_MSG,"申请减免操作有误请按照规则操作，一次减免只能有一条生效申请操作");
        }
/*        List<LoanSpecialRepayment> loanSpecialRepayments = loanSpecialRepaymentDao.findReleifApplyEffect(specialRepaymentRelationList.get(0).getApplyId());
        Long effectiveId = specialRepaymentRelationList.get(0).getEffectiveId();
        for(LoanSpecialRepayment loanSpecialRepayment:loanSpecialRepayments) {
            if (loanSpecialRepayment.getSpecialRepaymentState().equals(SpecialRepaymentStateEnum.申请.getValue()) && loanSpecialRepayment.getSpecialRepaymentType().equals(SpecialRepaymentTypeEnum.减免.getValue())) {
                effectiveId = loanSpecialRepayment.getId();
            }
        }*/
        SpecialTradeRelation specialTradeRelation = new SpecialTradeRelation();
        specialTradeRelation.setId(sequencesServiceImpl.getSequences(SequencesEnum.SPECIAL_TRADE_RELATION));
        specialTradeRelation.setEffectiveId(specialRepaymentRelationList.get(0).getEffectiveId());
        specialTradeRelation.setTradeNo(tradeNo);
        specialTradeRelation.setCreateTime(new Date());
        specialTradeRelation.setUpdateTime(new Date());
        specialTradeRelationDao.insert(specialTradeRelation);
        return specialTradeRelation;
    }

    @Override
    public void updateSpecialRepaymentApplyFinish(Long effectiveId) {
        SpecialRepaymentApply specialRepaymentApply = specialRepaymentApplyDao.querySpecialRepaymentApplyByEffectiveid(effectiveId);
        specialRepaymentApply.setApplicationStatus(SpecialRepaymentApplyStatus.完成.getCode());
        this.updateSpecialRepaymentApply(specialRepaymentApply);
    }

    @Override
    public void updateSpecialRepaymentApplyPass(Long effectiveId) {
        SpecialRepaymentApply specialRepaymentApply = specialRepaymentApplyDao.querySpecialRepaymentApplyByEffectiveid(effectiveId);
        specialRepaymentApply.setApplicationStatus(SpecialRepaymentApplyStatus.通过.getCode());
        this.updateSpecialRepaymentApply(specialRepaymentApply);
    }

    @Override
    public void updateSpecialRepaymentApply(Long applyId,String applyStatus,String memo) {
        SpecialRepaymentApply specialRepaymentApply = new SpecialRepaymentApply();
        specialRepaymentApply.setId(applyId);
        specialRepaymentApply.setApplicationStatus(applyStatus);
        specialRepaymentApply.setMemo1(memo);
        this.updateSpecialRepaymentApply(specialRepaymentApply);
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED)
    public void reliefRepayDealFinish(OfferRepayInfo repay) {
        SpecialTradeRelation specialTradeRelation =this.insetSpecialTradeRelation(repay.getTradeNo(), repay.getLoanId());
        if (specialTradeRelation == null) {
            return;
        }
        this.updateSpecialRepaymentApplyFinish(specialTradeRelation.getEffectiveId());
        return;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED)
    public void offOneTimeSettle(Long loanId) {
        List<SpecialRepaymentApply> specialRepaymentApplys = this.getEffectiveSatusSpecialRepaymentApply(loanId);
        if (CollectionUtils.isEmpty(specialRepaymentApplys)) {
            return;
        }
        if (specialRepaymentApplys.size() >1) {
            throw new PlatformException(ResponseEnum.FULL_MSG,"存在多条减免不需取消申请！");
        }
        SpecialRepaymentApply specialRepaymentApply = specialRepaymentApplys.get(0);
        this.offReliefApply(specialRepaymentApply);
       List<LoanSpecialRepayment> loanSpecialRepayments = specialRepaymentDao.findReleifApplyEffect(specialRepaymentApply.getId());
        for (LoanSpecialRepayment loanSpecialRepayment:loanSpecialRepayments) {
            if (loanSpecialRepayment.getSpecialRepaymentState().equals(SpecialRepaymentStateEnum.申请.getValue()) &&
                    loanSpecialRepayment.getSpecialRepaymentType().equals(SpecialRepaymentTypeEnum.减免.getValue())) {
                loanSpecialRepayment.setSpecialRepaymentState(SpecialRepaymentStateEnum.取消.getValue());
                specialRepaymentDao.update(loanSpecialRepayment);
            }
        }
    }

    @Override
    public void offReliefApply(SpecialRepaymentApply specialRepaymentApply) {
        if (specialRepaymentApply.getApplicationStatus().equals(SpecialRepaymentApplyStatus.申请.getCode())) {
            specialRepaymentApply.setApplicationStatus(SpecialRepaymentApplyStatus.取消.getCode());
            specialRepaymentApply.setMemo1("取消提前结清，减免自动取消");
        }else if (specialRepaymentApply.getApplicationStatus().equals(SpecialRepaymentApplyStatus.通过.getCode())) {
            specialRepaymentApply.setApplicationStatus(SpecialRepaymentApplyStatus.失效.getCode());
            specialRepaymentApply.setMemo1("取消提前结清，减免自动失效");
        }else if (specialRepaymentApply.getApplicationStatus().equals(SpecialRepaymentApplyStatus.生效.getCode())) {
            specialRepaymentApply.setApplicationStatus(SpecialRepaymentApplyStatus.失效.getCode());
        }
        this.updateSpecialRepaymentApply(specialRepaymentApply);
    }
    @Override
    public SpecialRepaymentApply addSpecialRepaymentApplyByVo(SpecialRepaymentApply specialRepaymentApply) {
        User user = UserContext.getUser();
        if (user != null) {
            specialRepaymentApply.setProposerId(user.getId());
        }
        if (SpecialReliefTypeEnum.特殊减免.getCode().equals(specialRepaymentApply.getIsSpecial())) {
            specialRepaymentApply.setApplicationStatus(SpecialRepaymentApplyStatus.通过.getCode());
        }else {
            specialRepaymentApply.setApplicationStatus(SpecialRepaymentApplyStatus.申请.getCode());
        }
        specialRepaymentApply.setApplyNo("");
        specialRepaymentApply.setFlag("0");
        specialRepaymentApply.setRepayLevel(specialRepaymentApply.getRepayLevel());
        specialRepaymentApply.setId(sequencesServiceImpl.getSequences(SequencesEnum.SPECIAL_REPAYMENT_APPLY));
        return specialRepaymentApplyDao.insert(specialRepaymentApply);
    }

    @Override
    public SpecialRepaymentApply getSpecialRepaymentApplyById(Long id) {
        return specialRepaymentApplyDao.get(id);
    }

    @Override
    public void executeApplyReliefStatusUpdate() {
        Map<String, Object> params = new HashMap<>();
        params.put("currDate", Dates.getDateTime(Dates.getBeforeDays(1),Dates.DEFAULT_DATE_FORMAT));
        params.put("applicationStatusArrays", new String[]{SpecialRepaymentApplyStatus.申请.getCode(),SpecialRepaymentApplyStatus.通过.getCode()});
        List<SpecialRepaymentApply> list  = specialRepaymentApplyDao.findListByMap(params);
        if(CollectionUtils.isEmpty(list)){
            return;
        }
        for(SpecialRepaymentApply specialRepaymentApply: list){
            if (specialRepaymentApply.getApplicationStatus().equals(SpecialRepaymentApplyStatus.申请.getCode())) {
                specialRepaymentApply.setApplicationStatus(SpecialRepaymentApplyStatus.取消.getCode());
                specialRepaymentApply.setMemo1("审批超时，系统自动取消");
            }else if (specialRepaymentApply.getApplicationStatus().equals(SpecialRepaymentApplyStatus.通过.getCode())) {
                specialRepaymentApply.setApplicationStatus(SpecialRepaymentApplyStatus.失效.getCode());
                specialRepaymentApply.setMemo1("当日实还金额不足，系统自动失效");
            }
            this.updateSpecialRepaymentApply(specialRepaymentApply);
        }
    }

    @Override
    public boolean isM1(Long loanId) {
        Date tradeDate = Dates.getCurrDate();
        List<LoanRepaymentDetail> rdList = afterLoanService.getAllInterestOrLoan(tradeDate, loanId);
        Date overdueStartDate = rdList.get(0).getReturnDate();
        if (tradeDate.compareTo(Dates.addDay(overdueStartDate,30)) != 1) {
            return true;
        }
        return false;
    }

    @Override
    public Map<String, Object> queryEffectiveLoanSpecialRepayment(Map<String, Object> params) {
        return specialRepaymentApplyDao.queryEffectiveLoanSpecialRepayment(params);
    }

    @Override
    public boolean isNoValidSalesDepartment(Long salesDepartmentId) {
        ComOrganization comOrganization = comOrganizationDao.getNoValidSalesDepartment(salesDepartmentId);
        if (comOrganization != null) {
            return true;
        }
        return false;
    }

    @Override
    public BigDecimal getReleifAmount(Long loanId) {
        List<SpecialRepaymentApply> specialRepaymentApplies = this.getEffectiveSatusSpecialRepaymentApply(loanId);
        if (CollectionUtils.isNotEmpty(specialRepaymentApplies)) {
            SpecialRepaymentApply specialRepaymentApply = specialRepaymentApplies.get(0);

            if (SpecialRepaymentApplyStatus.通过.getCode().equals(specialRepaymentApply.getApplicationStatus())) {
                //这个债权存在通过的申请减免
                 return specialRepaymentApply.getApplyAmount();
            }
            if (SpecialRepaymentApplyStatus.生效.getCode().equals(specialRepaymentApply.getApplicationStatus())) {
                Map<String, Object> effectiveParams = new HashMap<String, Object>();
                effectiveParams.put("applyId", specialRepaymentApply.getId());
                Map<String, Object> effectiveMap = this.queryEffectiveLoanSpecialRepayment(effectiveParams);
                return (BigDecimal) effectiveMap.get("effectiveMoney");
            }
        }
        return new BigDecimal(0.00);
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED)
    public void releifNotEnoughFine(Long effectiveId) {
        this.updateSpecialRepaymentApplyPass(effectiveId);
        specialRepaymentRelationdDao.deleteByEffectiveId(effectiveId);
    }

    @Override
    public OverDueGradeEnum queryOverDueGrade(String reliefType, int overDueDay, boolean isRuleIn) {
        if (SpecialRepaymentApplyTeyps.一般减免.getCode().equals(reliefType)) {
            if (1<= overDueDay && overDueDay <= 15) {
                return OverDueGradeEnum.A;
            }
            if (15 <= overDueDay && overDueDay <= 31) {
                return OverDueGradeEnum.B;
            }
        }
        if (isRuleIn) {
            return OverDueGradeEnum.D;
        }
        return OverDueGradeEnum.E;
    }


    @Override
    public BigDecimal calculateRuleInMaxReliefAmount(Long loanId) {
      /*  LoanBase loanBase = loanBaseDao.findByLoanId(loanId);
        Assert.notNull(loanBase, ResponseEnum.FULL_MSG, "未找到债权数据loanId:" + loanId);
        LoanInitialInfo loanInitialInfo = loanInitialInfoDao.findByLoanId(loanId);
        LoanProduct loanProduct = loanProductDao.findByLoanId(loanInitialInfo.getLoanId());
        VLoanInfo vLoanInfo = vLoanInfoServiceImpl.findByLoanId(loanId);
        /** 获取计算器实例 **//*
        ICalculator calculatorInstace = CalculatorFactoryImpl.createCalculator(vLoanInfo);
        List<LoanRepaymentDetail> loanRepaymentDetails = calculatorInstace.createLoanTrial(loanBase,loanInitialInfo,loanProduct);
        Date tradeDate = Dates.getCurrDate();
        List<LoanRepaymentDetail> repayListOld = afterLoanService.getAllInterestOrLoan(tradeDate,loanId);
        Long overTerm = repayListOld.get(0).getCurrentTerm();
        Long currenTerm =  repayListOld.get(repayListOld.size() - 1).getCurrentTerm();
        List<LoanRepaymentDetail> repayListNew = createNewAllInterestOrLoan(loanRepaymentDetails,overTerm,currenTerm);
        logger.info("新创建的逾期到当前部分的还款计划：{}", JSONUtil.toJSON(repayListNew));
        //已还期数
        int alreadyRepayTerm = currenTerm.intValue() == 1 ? 0 : currenTerm.intValue() - 1;
        //费率差额 = 当前一次性减免金额（旧）-当前一次性结清金额（新）+[每期还款（旧）-每期还款（新）]*已还期数
        BigDecimal rateDiffAmount = calculatorInstace.getOnetimeRepaymentAmount(loanId,tradeDate,repayListOld).subtract(calculatorInstace.getOnetimeRepaymentAmount(loanId,tradeDate,repayListNew))
                .add(repayListOld.get(0).getReturneterm().subtract(repayListNew.get(0).getReturneterm()).multiply(new BigDecimal(alreadyRepayTerm)));
        String repayGrade = queryRepayGrade(loanId);
        int beginNumber = getBeginNumber(String.valueOf(currenTerm));
        int endNumber = getEedNumber(String.valueOf(currenTerm));
        BigDecimal reliefPrecent = specialReliefPercentDao.queryReliefPercent(repayGrade, beginNumber, endNumber);
        BigDecimal ruleInMaxReliefAmount ;
        BigDecimal residualPactMoney = vLoanInfo.getResidualPactMoney();
        if (currenTerm.intValue() >= 12) {
            // 当期期数<=12的，最大减免 = 剩余本金×4%×减免比例 + 费率差额
            ruleInMaxReliefAmount = residualPactMoney.multiply(new BigDecimal(0.04)).multiply(reliefPrecent).add(rateDiffAmount);
        }else {
            // 当期期数>=13的，最大减免 = 剩余本金×3%×减免比例 + 费率差额
            ruleInMaxReliefAmount = residualPactMoney.multiply(new BigDecimal(0.03)).multiply(reliefPrecent).add(rateDiffAmount);
        }*/
        Date tradeDate = Dates.getCurrDate();
        List<LoanRepaymentDetail> repayList = afterLoanService.getAllInterestOrLoan(tradeDate,loanId);
        if (CollectionUtils.isEmpty(repayList)) {
            return new BigDecimal(0.00);
        }
        LoanRepaymentDetail lastRepaymentDetail = repayList.get(repayList.size() - 1);
        Long currentTerm =  lastRepaymentDetail.getCurrentTerm();
//        VLoanInfo vLoanInfo = vLoanInfoServiceImpl.findByLoanId(loanId);
//        获取计算器实例
//        ICalculator calculatorInstace = CalculatorFactoryImpl.createCalculator(vLoanInfo);
        String repayGrade = queryRepayGrade(loanId);
        BigDecimal reliefPrecent = specialReliefPercentDao.queryReliefPercent(repayGrade, currentTerm);
        //最大规则内减免金额 =  "一次性结清金额"*"减免比例"
        BigDecimal ruleInMaxReliefAmount = lastRepaymentDetail.getRepaymentAll().multiply(reliefPrecent);
        return ruleInMaxReliefAmount.setScale(2,BigDecimal.ROUND_DOWN);
    }

/*    public List<LoanRepaymentDetail> createNewAllInterestOrLoan( List<LoanRepaymentDetail> loanRepaymentDetails,Long overTerm,Long currenTerm){
        List<LoanRepaymentDetail> loanRepaymentDetailList = new ArrayList<>();
        boolean flag = false;
        for(LoanRepaymentDetail repaymentDetail:loanRepaymentDetails) {
            if (repaymentDetail.getCurrentTerm().equals(overTerm)) {
                loanRepaymentDetailList.add(repaymentDetail);
                flag =true;
            }
            if (flag) {
                loanRepaymentDetailList.add(repaymentDetail);
            }
            if (repaymentDetail.getCurrentTerm().equals(currenTerm)) {
                flag = false;
            }
        }
        return loanRepaymentDetailList;
    }*/
/*    private int getBeginNumber(String currentTerm) {
        if (reliefPercentTermRange1.indexOf(currentTerm) > 0) {
            return 6;
        }
        if (reliefPercentTermRange2.indexOf(currentTerm) > 0) {
            return 9;
        }
        if (reliefPercentTermRange3.indexOf(currentTerm) > 0){
            return 12;
        }
        if (reliefPercentTermRange4.indexOf(currentTerm) > 0) {
            return 18;
        }
        if(reliefPercentTermRange5.indexOf(currentTerm) > 0){
            return 24;
        }
        return 0;
    }*/

/*    private int getEedNumber(String currentTerm){
        if (reliefPercentTermRange1.indexOf(currentTerm) > 0) {
            return 9;
        }
        if (reliefPercentTermRange2.indexOf(currentTerm) > 0) {
            return 12;
        }
        if (reliefPercentTermRange3.indexOf(currentTerm) > 0){
            return 18;
        }
        if (reliefPercentTermRange4.indexOf(currentTerm) > 0) {
            return 24;
        }
        if(reliefPercentTermRange5.indexOf(currentTerm) > 0){
            return 35;
        }
        return 0;
    }*/

    private String queryRepayGrade(Long loanId){
        Map levelMap = new HashMap();
        levelMap.put("loanId",loanId);
        String repaymentLevel =  loanRepaymentDetailService.findRepaymentLevel(levelMap);
        return  Strings.isEmpty(repaymentLevel) ? "":repaymentLevel.substring(0,1);
    }

    @Override
    public BigDecimal getMaxReliefAmount(MaxReliefAmountParamVo maxReliefAmountParamVo) {
        RelieCalculateAmountParamVo relieCalculateAmountParamVo = new RelieCalculateAmountParamVo();
        relieCalculateAmountParamVo.setLoanId(maxReliefAmountParamVo.getLoanId());
        relieCalculateAmountParamVo.setTradeDate(maxReliefAmountParamVo.getTradeDate());
        ReliefAmountCalculateVo reliefAmountCalculateVo = this.getRelieCalculateAmount(relieCalculateAmountParamVo);
        //最大减免金额
        BigDecimal maxReleifAmount = this.calculateMaxReliefMoney(reliefAmountCalculateVo, maxReliefAmountParamVo.getReliefType(),maxReliefAmountParamVo.getIsPecial());
        return maxReleifAmount;
    }

    @Override
    public ReliefAmountGradeEnum getReliefAmountGrade(BigDecimal applyReliefAmount, BigDecimal maxReliefAmount,OverDueGradeEnum overDueGradeEnum) {
        //0＜申请减免金额≤最大申请金额*100%
        if (OverDueGradeEnum.A.getCode().equals(overDueGradeEnum.getCode()) && new BigDecimal(0).compareTo(applyReliefAmount) == -1 && applyReliefAmount.compareTo(maxReliefAmount) != 1) {
            return ReliefAmountGradeEnum.A;
        }
        //0＜申请减免金额≤最大申请金额*80%
        if (new BigDecimal(0).compareTo(applyReliefAmount) == -1 && applyReliefAmount.compareTo(maxReliefAmount.multiply(new BigDecimal(0.8))) != 1) {
            return ReliefAmountGradeEnum.B;
        }
        //最大申请金额×80% ＜申请减免金额≤最大申请金额
        if (applyReliefAmount.compareTo(maxReliefAmount.multiply(new BigDecimal(0.8))) == 1 && applyReliefAmount.compareTo(maxReliefAmount) != 1) {
            return ReliefAmountGradeEnum.C;
        }
        return null;
    }

    @Override
    public ReliefAmountGradeEnum getOneTimeReliefAmountGrade(BigDecimal applyReliefAmount, BigDecimal fine, BigDecimal ruleInMaxReliefAmount) {
        //申请减免金额<=罚息 第一档
        if (applyReliefAmount.compareTo(fine) != 1) {
            return  ReliefAmountGradeEnum.D;
        }
        //罚息  < 申请减免金额 <= 罚息 + 规则内最大减免金额 第二档
        if (applyReliefAmount.compareTo(fine.add(ruleInMaxReliefAmount)) != 1) {
            return ReliefAmountGradeEnum.E;
        }
        return ReliefAmountGradeEnum.F;
    }

    @Override
    public void checkOutSpecialReliefApply(Long loanId) {
        VLoanInfo vLoanInfo = vLoanInfoServiceImpl.findByLoanId(loanId);
        //检查该借款人是否有已转让过的债权
        boolean flag = loanTransferInfoServiceImpl.isLoanTransfer(null,vLoanInfo.getId());
        if(!flag){
        	 throw new PlatformException(ResponseEnum.FULL_MSG,Strings.errorMsg);
        }        
        if (LoanStateEnum.结清.getValue().equals(vLoanInfo.getLoanState()) || LoanStateEnum.预结清.getValue().equals(vLoanInfo.getLoanState())) {
            throw new PlatformException(ResponseEnum.FULL_MSG,"债权已结清或预结清不允许申请！");
        }
        if (this.isExistEffectiveSatusSpecialRepaymentApply(loanId)){
            throw new PlatformException(ResponseEnum.FULL_MSG,"有在途的申请减免未完成,不允许再申请");
        }
        if(FundsSourcesTypeEnum.包商银行.getValue().equals(vLoanInfo.getLoanBelong())){
            // 得到特殊还款记录表信息
            LoanSpecialRepayment specialRepayment = loanSpecialRepaymentServiceImpl.findByLoanIdAndRequestDateAndTypesAndState(vLoanInfo.getId(),
                    null,
                    new String[]{SpecialRepaymentTypeEnum.提前扣款.getValue(),SpecialRepaymentTypeEnum.一次性还款.getValue()},
                    SpecialRepaymentStateEnum.申请.getValue());
            //获取当期还款明细
            Date currTermReturnDate=afterLoanServiceImpl.getCurrTermReturnDate(Dates.getCurrDate(),vLoanInfo.getPromiseReturnDate());
            LoanRepaymentDetail loanRepaymentDetail = loanRepaymentDetailService.findByLoanAndReturnDate(vLoanInfo,currTermReturnDate);
            //当提前申请并未生效，而且也没有逾期，不能申请减免
            if(specialRepayment == null && loanRepaymentDetail.getRepaymentState().equals("结清")){
                throw new PlatformException("包商银行提前还款/结清申请未生效，而且也没有逾期，不能申请减免").applyLogLevel(LogLevel.ERROR);
            }
        }
    }

    @Override
    public Pager queryReliefInfoPage(Map<String, Object> params) {
        return specialRepaymentApplyDao.queryReliefInfoPage(params);
    }

    @Override
    public Workbook getQueryReliefInfoWorkbook(Map<String, Object> params,String fileName) {
        Workbook workbook = null;
        String sheetName = "服务费收费";
        try {
            List<ReliefQueryReportVo> reportVos = specialRepaymentApplyDao.queryReliefQueryReport(params);
            for (ReliefQueryReportVo reportVo:reportVos) {
                editReliefQueryReport(reportVo);
            }
            // 创建workbook对象
            workbook = ExcelExportUtil.createExcelByVo(fileName, this.getLabels(),this.getFields(),reportVos, sheetName);
        }catch (Exception e){
            e.printStackTrace();
        }
        return workbook;
    }

    @Override
    public void checkRemitAmountRequestState(Long loanId, BigDecimal remitAmount, boolean isSpecail) {
        VLoanInfo vLoanInfo = vLoanInfoServiceImpl.findByLoanId(loanId);
        Assert.notNull(vLoanInfo, ResponseEnum.FULL_MSG, "未找到债权数据loanId:" + loanId);
        if(this.isExistEffectiveSatusSpecialRepaymentApply(loanId)){
            throw new PlatformException(ResponseEnum.FULL_MSG,"该笔债权已存在在途的减免申请，不能重复申请！").applyLogLevel(LogLevel.WARN);
        }
        if (new BigDecimal(0.01).compareTo(remitAmount)==1) {
            throw new PlatformException(ResponseEnum.FULL_MSG,"减免金额不能小于0.01！").applyLogLevel(LogLevel.WARN);
        }
        boolean allCleanSpecialState = afterLoanService.isOneTimeRepayment(loanId);
        String reliefType = allCleanSpecialState ? SpecialRepaymentApplyTeyps.结清减免.getCode() : SpecialRepaymentApplyTeyps.一般减免.getCode();
        //最大减免金额
        MaxReliefAmountParamVo maxReliefAmountParamVo = new MaxReliefAmountParamVo();
        maxReliefAmountParamVo.setLoanId(loanId);
        maxReliefAmountParamVo.setTradeDate(Dates.getCurrDate());
        maxReliefAmountParamVo.setReliefType(reliefType);
        maxReliefAmountParamVo.setIsPecial(isSpecail ? SpecialReliefTypeEnum.特殊减免.getCode() : SpecialReliefTypeEnum.非特殊减免.getCode());
        BigDecimal maxReleifAmount = this.getMaxReliefAmount(maxReliefAmountParamVo);

        if (remitAmount.compareTo(maxReleifAmount) > 0) {
            throw new PlatformException(ResponseEnum.FULL_MSG, "减免金额不能大于最大减免金额").applyLogLevel(LogLevel.WARN);
        }
    }

    /**
     * excel文件的表头显示名
     * @return
     */
    private String[] getLabels() {
        String[] labels = { "合同编号", "管理营业部","客户姓名", "身份证号", "借款类型","借款期限", "放款日期", "合同金额",
                "应还总额", "减免总额", "减免罚息", "减免违约金", "减免利息","减免本金", "减免日期","减免类型", "账号类别",
                "申请类型", "是否特殊减免"};
        return labels;
    }
    /**
     * excel文件的数据字段名
     * @return
     */
    private String[] getFields() {
        String[] fields = {"contractNum","salesDepartmentName","name","idNum","loanType","time","grantDate","pactMoney",
                "repayAmount","reliefAmonut", "releifFine","reliefPenalty","reliefInterest","releifPrincipal","releifDate",
                "applyType","grantType", "applySource","isSpecial"};
        return fields;
    }

    private void editReliefQueryReport(ReliefQueryReportVo reportVo){
        String isSpecial = reportVo.getIsSpecial();
        reportVo.setIsSpecial(SpecialReliefTypeEnum.非特殊减免.getCode().equals(isSpecial) ? "否":"是");
        reportVo.setApplyType(SpecialRepaymentApplyTeyps.getValueByCode(reportVo.getApplyType()).getValue());
        reportVo.setApplySource(SpecialReliefTypeEnum.非特殊减免.getCode().equals(isSpecial)&&"0".equals(reportVo.getFlag()) ? "门店":"总部");
        BigDecimal reliefAmount = reportVo.getReliefAmonut();
        Map<String,Object> acounts = specialRepaymentApplyDao.queryAccount(reportVo.getTradeNo());
        //罚息
        BigDecimal fine = (BigDecimal)acounts.get("fine");
        //违约金
        BigDecimal penalty = (BigDecimal)acounts.get("penalty");
        //利息
        BigDecimal interest = (BigDecimal)acounts.get("interest");
        //本金
        BigDecimal principal = (BigDecimal)acounts.get("principal");
        if (new BigDecimal(0.0).compareTo(fine) == -1) {
            if (reliefAmount.compareTo(fine) != 1) {
                reportVo.setReleifFine(reliefAmount);
                return;
            }
            reportVo.setReleifFine(fine);
            reliefAmount = reliefAmount.subtract(fine);
        }
        if (new BigDecimal(0.0).compareTo(penalty) == -1) {
            if (reliefAmount.compareTo(penalty) != 1) {
                reportVo.setReliefPenalty(reliefAmount);
                return;
            }
            reportVo.setReliefPenalty(penalty);
            reliefAmount = reliefAmount.subtract(penalty);
        }
        if (new BigDecimal(0.0).compareTo(interest) == -1) {
            if (reliefAmount.compareTo(interest) != 1) {
                reportVo.setReliefInterest(reliefAmount);
                return;
            }
            reportVo.setReliefInterest(interest);
            reliefAmount = reliefAmount.subtract(interest);
        }
        reportVo.setReleifPrincipal(reliefAmount);
    }
}
