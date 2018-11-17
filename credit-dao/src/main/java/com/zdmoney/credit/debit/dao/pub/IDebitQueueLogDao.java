package com.zdmoney.credit.debit.dao.pub;


import java.util.List;
import java.util.Map;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.debit.domain.DebitQueueLog;
import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.repay.vo.DebitQueueManagementVo;


public interface IDebitQueueLogDao  extends IBaseDao<DebitQueueLog>{
	/**
	 * 接收到陆金所的划扣结果通知后，更新debit_queue_log表中的划扣状态 debit_notify_state
	 * @param debitQueueLogList
	 */
	public void updateDebitQueueLogAfterGetDeductResultNotifyFromLufax(String debitNo,String debitResultState);

	/**
	 * 查找代扣管理分页信息
	 * @param debitQueueManagementVo
	 * @return
	 */
	public Pager findDebitQueueManagementWithPg(DebitQueueManagementVo debitQueueManagementVo);

	/**
	 * 根据条件查询还款日期
	 * @param map
	 * @return
	 */
	public Map<String,Object> findTradeDateByDebitNo(Map<String, Object> map);
	
	/**
	 * 查询一笔还款当期所在期数
	 * @param params
	 * @return
	 */
	public Long getRepaymentCurrentTerm(Map<String, Object> params);
	
	/**
	 * 查询逾期还款对应的起始期数和截止期数，不包含当期
	 * @param params
	 * @return
	 */
	public Map<String, Object> getOverdueRepaymentTerm(Map<String, Object> params);
	
	/**
	 * 查询还款类型为委托代扣01和逾期代偿03的划扣记录
	 * @param params
	 * @return
	 */
	public List<DebitQueueLog> findEntrustAndOverdueDebit(Map<String, Object> params);
	
	/**
	 * 查询还款类型为委托代扣01和提前结清05的划扣记录
	 * @param params
	 * @return
	 */
	public List<DebitQueueLog> findEntrustAndAdvanceClearDebit();
}
