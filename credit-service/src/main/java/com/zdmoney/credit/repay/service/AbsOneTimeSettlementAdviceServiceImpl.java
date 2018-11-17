package com.zdmoney.credit.repay.service;

import com.zdmoney.credit.common.constant.FundsSourcesTypeEnum;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.framework.vo.abs.input.Abs100005Vo;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.ILoanSpecialRepaymentService;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import com.zdmoney.credit.repay.dao.AbsOneTimeSettlementAdvicedDaoImpl;
import com.zdmoney.credit.repay.dao.pub.IAbsOneTimeSettlementAdvicedDao;
import com.zdmoney.credit.repay.domain.AbsOneTimeSettlementAdvice;
import com.zdmoney.credit.repay.service.pub.IAbsOneTimeSettlementAdviceService;
import com.zdmoney.credit.trustOffer.dao.pub.ITrustOfferFlowDao;
import com.zdmoney.credit.trustOffer.vo.TrustOfferFlowVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ym10094 on 2016/12/2.
 */
@Service
public class AbsOneTimeSettlementAdviceServiceImpl implements IAbsOneTimeSettlementAdviceService {
    private static Logger log = LoggerFactory.getLogger(AbsOneTimeSettlementAdvicedDaoImpl.class);
    @Autowired
    private IAbsOneTimeSettlementAdvicedDao absOneTimeSettlementAdvicedDao;
    @Autowired
    private ITrustOfferFlowDao  trustOfferFlowDao;
    @Autowired
    private IVLoanInfoService ivLoanInfoService;
    @Autowired
    private ILoanSpecialRepaymentService loanSpecialRepaymentService;
    /**
     *通知状态 01 未通知  02 已通知 03 通知失败
     */
    public static final String[] adviceState ={"01","02","03"};
    @Override
    public void adviceAbsOneTimeSettlementFinish() {
        try {
            String [] fundsSources = {FundsSourcesTypeEnum.外贸2.getValue()};
            Date queryDate = new Date();
            List<TrustOfferFlowVo> trustOfferFlowVoList = trustOfferFlowDao.queryOneTimeSettlementAccountingFlow(fundsSources,queryDate);
            List<AbsOneTimeSettlementAdvice> absOneTimeSettlementAdviceList = this.trustOfferFlowListConvertAbsOneTimeSettlementAdviceList(trustOfferFlowVoList);
            absOneTimeSettlementAdvicedDao.insertBatch(absOneTimeSettlementAdviceList);
            List<AbsOneTimeSettlementAdvice> sendOneTimeSettlementAdviceList = absOneTimeSettlementAdvicedDao.queryAbsOneTimeSettlementAdviceByStates(new String[]{adviceState[0],adviceState[2]});
            this.sendOneTimeSettlementAdviceGiveAbs(sendOneTimeSettlementAdviceList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * trustOfferFlowVoList 转换成  absOneTimeSettlementAdviceList
     * @param trustOfferFlowVoList
     * @return
     */
    private List<AbsOneTimeSettlementAdvice> trustOfferFlowListConvertAbsOneTimeSettlementAdviceList(List<TrustOfferFlowVo> trustOfferFlowVoList){
        List<AbsOneTimeSettlementAdvice> absOneTimeSettlementAdviceList = new ArrayList<>();
        for (TrustOfferFlowVo trustOfferFlowVo:trustOfferFlowVoList) {
            AbsOneTimeSettlementAdvice  absOneTimeSettlementAdvice = this.trustOfferFlowVoConvertabsOneTimeSettlementAdvice(trustOfferFlowVo);
            absOneTimeSettlementAdviceList.add(absOneTimeSettlementAdvice);
        }
        return absOneTimeSettlementAdviceList;
    }

    /**
     * trustOfferFlowVo对象  转换成  absOneTimeSettlementAdvice对象
     * @param trustOfferFlow
     * @return
     */
    private AbsOneTimeSettlementAdvice trustOfferFlowVoConvertabsOneTimeSettlementAdvice(TrustOfferFlowVo trustOfferFlow){
        String serialno =trustOfferFlow.getTradeNo();
        serialno = serialno.substring(serialno.indexOf("D")+1);
        AbsOneTimeSettlementAdvice absOneTimeSettlementAdvice = new AbsOneTimeSettlementAdvice();
        absOneTimeSettlementAdvice.setLoanId(trustOfferFlow.getLoanId()!=null?trustOfferFlow.getLoanId():0L);
        absOneTimeSettlementAdvice.setAdviceState(adviceState[0]);//为通知
        absOneTimeSettlementAdvice.setSerialno(serialno);
        absOneTimeSettlementAdvice.setPayover(trustOfferFlow.getFineInterest().add(trustOfferFlow.getReliefFineInterest()));
        absOneTimeSettlementAdvice.setRepaytotal(trustOfferFlow.getAmount());
        absOneTimeSettlementAdvice.setRepayamt(trustOfferFlow.getPrincipal());
        absOneTimeSettlementAdvice.setRepayinte(trustOfferFlow.getInterest());
        absOneTimeSettlementAdvice.setRepayover(trustOfferFlow.getFineInterest());
        absOneTimeSettlementAdvice.setCreateTime(new Date());
        absOneTimeSettlementAdvice.setUpdateTime(new Date());
        absOneTimeSettlementAdvice.setCreator("admin");
        absOneTimeSettlementAdvice.setUpdator("admin");
        absOneTimeSettlementAdvice.setLastTerm(trustOfferFlow.getLastTerm());
        absOneTimeSettlementAdvice.setStartTerm(trustOfferFlow.getStartTerm());
        return absOneTimeSettlementAdvice;
    }

    public void sendOneTimeSettlementAdviceGiveAbs(List<AbsOneTimeSettlementAdvice> adviceList) {
        for (AbsOneTimeSettlementAdvice absOneTimeSettlementAdvice : adviceList) {
            try {
                VLoanInfo vLoanInfo = ivLoanInfoService.findByLoanId(absOneTimeSettlementAdvice.getLoanId());
                if (vLoanInfo == null) {
                    log.info("找不到{}这个债权",absOneTimeSettlementAdvice.getLoanId());
                    continue;
                }
                Abs100005Vo abs100005Vo = loanSpecialRepaymentService.abs100005VoPackage(vLoanInfo, absOneTimeSettlementAdvice);
                boolean callState = loanSpecialRepaymentService.callOneTimeSettlementApplyInterface(abs100005Vo);
                if (callState) {
                    this.updateAbsOneTimeSettlementAdviceStateSucc(absOneTimeSettlementAdvice);
                    continue;
                }
                this.updateAbsOneTimeSettlementAdviceStateFail(absOneTimeSettlementAdvice);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送一次结算通知给数信成功
     * @param absOneTimeSettlementAdvice
     */
    public void updateAbsOneTimeSettlementAdviceStateSucc(AbsOneTimeSettlementAdvice absOneTimeSettlementAdvice){
        absOneTimeSettlementAdvice.setAdviceState(adviceState[1]);
        absOneTimeSettlementAdvice.setUpdateTime(new Date());
        absOneTimeSettlementAdvicedDao.update(absOneTimeSettlementAdvice);
    }

    /**
     * 发送一次性结算通知给数信失败
     * @param absOneTimeSettlementAdvice
     */
    public void updateAbsOneTimeSettlementAdviceStateFail(AbsOneTimeSettlementAdvice absOneTimeSettlementAdvice){
        absOneTimeSettlementAdvice.setAdviceState(adviceState[2]);
        absOneTimeSettlementAdvice.setUpdateTime(new Date());
        absOneTimeSettlementAdvicedDao.update(absOneTimeSettlementAdvice);
    }
}
