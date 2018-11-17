/**
 * 
 */
package com.zdmoney.credit.debit.service.pub;

import java.math.BigDecimal;

import com.zdmoney.credit.debit.domain.DebitBaseInfo;
import com.zdmoney.credit.loan.domain.VLoanInfo;

/**
 * @ClassName:     IDebitBaseInfoService.java
 * @Description:   
 * @author         柳云华
 * @version        V1.0  
 * @Since          JDK 1.7
 * @Date           2017年3月22日 下午4:24:57
 */
public interface IDebitBaseInfoService {
	
	/**
	 * 创建第三方划扣信息(报盘信息)
	 * @param loaninfo
	 * @return
	 */
	public DebitBaseInfo createThirdOffer(VLoanInfo loan, BigDecimal offerAmount);
	
	/**
	 * 创建第三方划扣信息(报盘信息)
	 * @param loaninfo
	 * @return
	 */
	public void createCallbackThirdOffer(VLoanInfo loan, BigDecimal offerAmount, boolean flag, String failReason);


}
