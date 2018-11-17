/**
 * 
 */
package com.zdmoney.credit.ljs.service.pub;

import com.zdmoney.credit.debit.domain.DebitQueueLog;
import com.zdmoney.credit.ljs.domain.CompensatoryDetailLufax;
import com.zdmoney.credit.loan.domain.LoanRepaymentDetail;

/**
 * @ClassName:     ICompensatoryDetailLufaxService.java
 * @Description:   
 * @author         柳云华
 * @version        V1.0  
 * @Since          JDK 1.7
 * @Date           2017年6月9日 下午6:34:49
 */
public interface ICompensatoryDetailLufaxService {
    
    /**
     * 保存陆金所垫付明细数据
     * @param debitQueueLog
     * @param currentDetail
     */
    public void saveCompensatoryDetailLufax(DebitQueueLog debitQueueLog, LoanRepaymentDetail currentDetail);
    
    /**
     * 保存陆金所垫付明细数据
     * @param vo
     */
    public void saveCompensatoryDetailLufax(CompensatoryDetailLufax vo);
}
