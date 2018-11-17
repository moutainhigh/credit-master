package com.zdmoney.credit.ljs.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.CompensatoryTypeEnum;
import com.zdmoney.credit.common.constant.DebitRepayTypeEnum;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.debit.domain.DebitQueueLog;
import com.zdmoney.credit.ljs.dao.pub.ICompensatoryDetailLufaxDao;
import com.zdmoney.credit.ljs.domain.CompensatoryDetailLufax;
import com.zdmoney.credit.ljs.service.pub.ICompensatoryDetailLufaxService;
import com.zdmoney.credit.loan.domain.LoanRepaymentDetail;
import com.zdmoney.credit.loan.service.pub.IAfterLoanService;
import com.zdmoney.credit.system.service.pub.ISequencesService;

/**
 * @ClassName:     CompensatoryDetailLufaxService.java
 * @Description:   
 * @author         柳云华
 * @version        V1.0  
 * @Since          JDK 1.7
 * @Date           2017年6月9日 下午6:35:41
 */
@Service
public class CompensatoryDetailLufaxServiceImpl implements ICompensatoryDetailLufaxService {
    
    @Autowired
    private ICompensatoryDetailLufaxDao compensatoryDetailLufaxDao;
    
    @Autowired
    private ISequencesService sequencesService;
    
    @Autowired
    private IAfterLoanService afterLoanService;
    
    public void saveCompensatoryDetailLufax(DebitQueueLog debitQueueLog, LoanRepaymentDetail currentDetail){
        // 保存陆金所垫付明细表记录
        CompensatoryDetailLufax lufax = new CompensatoryDetailLufax();
        if(DebitRepayTypeEnum.一次性回购.getCode().equals(debitQueueLog.getRepayType())){
            lufax.setType(CompensatoryTypeEnum.一次性回购.getCode());//垫付类型（01：逾期代偿、02：一次性回购）
            //当期本金
            BigDecimal currCorpus = currentDetail.getDeficit().subtract(currentDetail.getCurrentAccrual());
            //当期利息
            BigDecimal currAccural = currentDetail.getCurrentAccrual();
            //剩余本金
            BigDecimal principalBalance = currentDetail.getPrincipalBalance();
            //总金额
            BigDecimal totalAmount = currCorpus.add(currAccural).add(principalBalance);
            lufax.setCorpusAmount(currCorpus);
            lufax.setAccrualAmount(currAccural);
            lufax.setCleanAmount(principalBalance);
            lufax.setTotalAmount(totalAmount);
        } else{
            BigDecimal deficit = currentDetail.getDeficit();
            BigDecimal lixi = BigDecimal.ZERO;
            BigDecimal benjin = currentDetail.getReturneterm().subtract(currentDetail.getCurrentAccrual());
            if(deficit.compareTo(benjin)>0){//剩余欠款大于本金，则还有利息要还
                lixi = deficit.subtract(benjin);
            }
            BigDecimal actual_benjin = deficit.subtract(lixi);
            lufax.setType(CompensatoryTypeEnum.逾期代偿.getCode());//垫付类型（01：逾期代偿、02：一次性回购）
            lufax.setTotalAmount(deficit);
            lufax.setCorpusAmount(actual_benjin);
            lufax.setAccrualAmount(lixi);
        }
        lufax.setId(sequencesService.getSequences(SequencesEnum.COMPENSATORY_DETAIL_LUFAX));
        lufax.setTerm(currentDetail.getCurrentTerm().intValue());
        lufax.setPenaltyAmount(BigDecimal.ZERO);
        lufax.setLoanId(debitQueueLog.getLoanId());
        lufax.setTradeDate(currentDetail.getReturnDate());
        lufax.setDebitQueueId(debitQueueLog.getId());
        compensatoryDetailLufaxDao.insert(lufax);
    }
    
    public void saveCompensatoryDetailLufax(CompensatoryDetailLufax vo) {
        vo.setId(sequencesService.getSequences(SequencesEnum.COMPENSATORY_DETAIL_LUFAX));
        compensatoryDetailLufaxDao.insert(vo);
    }
}
