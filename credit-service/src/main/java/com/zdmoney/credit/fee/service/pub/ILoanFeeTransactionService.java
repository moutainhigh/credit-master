package com.zdmoney.credit.fee.service.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.vo.TppCallBackData20;
import com.zdmoney.credit.fee.domain.LoanFeeOffer;
import com.zdmoney.credit.fee.domain.LoanFeeTransaction;

/**
 * 借款收费回盘表 Service层接口，定义一些与系统业务相关的方法
 * 
 * @author Ivan
 *
 */
public interface ILoanFeeTransactionService {

	/**
	 * PK换实体
	 * 
	 * @param id
	 */
	public LoanFeeTransaction findById(Long id);

	/**
	 * 跟据实体类查询
	 * 
	 * @param loanFeeTransaction
	 */
	public List<LoanFeeTransaction> findListByVo(LoanFeeTransaction loanFeeTransaction);

	/**
	 * 生成回盘流水
	 * 
	 * @param loanFeeOffer
	 *            报盘信息
	 * @return
	 */
	public LoanFeeTransaction buildTransaction(LoanFeeOffer loanFeeOffer);

	/**
	 * 更新报表状态 已发送 （批量）
	 * 
	 * @param transactionList
	 */
	public void updateTransactionToSendding(List<LoanFeeTransaction> transactionList);

	/**
	 * 龙信小贷 服务费划扣 回盘
	 * 
	 * @param tppCallBackData20
	 *            回盘信息
	 * @return
	 */
	public boolean callBackTransaction(TppCallBackData20 tppCallBackData20);

	/**
	 * 跟据报盘流水号查询
	 * 
	 * @param serialNo
	 *            报盘流水号
	 * @return
	 */
	public LoanFeeTransaction findBySerialNo(String serialNo);

	/**
	 * ' 查询债权服务费报盘是否有未回盘的情况
	 * 
	 * @param loanId
	 *            债权编号
	 * @return true:有报盘未回 false:均已回盘
	 */
	public boolean isOfferSendding(Long loanId);

	/**
	 * 查询服务费回盘数据
	 * 
	 * @param param
	 * @return
	 */
	public Pager searchFeeOfferTransactionList(Map<String, Object> param);

}
