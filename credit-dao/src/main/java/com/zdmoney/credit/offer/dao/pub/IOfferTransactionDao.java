package com.zdmoney.credit.offer.dao.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.common.constant.OfferTransactionStateEnum;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.offer.domain.OfferTransaction;

/**
 * 
 * @author 00232949
 *
 */
public interface IOfferTransactionDao extends IBaseDao<OfferTransaction> {

	/**
	 * 根据状态和loan得到数据
	 * @param state
	 * @param loanId
	 * @return
	 */
	public List<OfferTransaction> findByStateAndLoan(OfferTransactionStateEnum state, Long loanId);

	/**
	 * 根据流水号得到报盘交易
	 * @param orderNo
	 * @return
	 */
	public OfferTransaction findByTrxSerialNo(String orderNo);
	
	/**
	 * 查询代扣回盘信息
	 * @param params
	 * @return
	 */
	public Pager searchOfferTransactionInfoWithPgByMap(Map<String, Object> params);

	/**
	 * 查询线下还款已发送且已导出的报盘流水信息
	 * @param params
	 * @return
	 */
	public OfferTransaction queryOffLineOfferTransaction(Map<String, Object> params);
	
	/**
	 * 根据银行卡账号查询报盘流水信息
	 * @param params
	 * @return
	 */
	public List<OfferTransaction> findOfferTranscationByBankAcct(Map<String, Object> params);
}
