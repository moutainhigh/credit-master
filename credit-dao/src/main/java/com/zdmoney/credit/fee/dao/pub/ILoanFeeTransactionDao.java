package com.zdmoney.credit.fee.dao.pub;

import java.util.Map;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.fee.domain.LoanFeeTransaction;
import com.zdmoney.credit.framework.dao.pub.IBaseDao;

/**
 * 借款收费回盘表 Dao层接口，定义一些与系统业务无关的方法
 * 
 * @author Ivan
 */
public interface ILoanFeeTransactionDao extends IBaseDao<LoanFeeTransaction> {
	/**
	 * 判断是否存在有报回盘记录
	 * 
	 * @param feeId
	 *            服务费编号
	 * @param trxState
	 *            报回盘状态
	 * 
	 * @return
	 */
	public boolean isExistsByFeeIdAndTrxState(Long feeId, String trxState);

	/**
	 * 查询服务费回盘数据
	 * 
	 * @param param
	 * @return
	 */
	public Pager searchFeeOfferTransactionList(Map<String, Object> param);
}
