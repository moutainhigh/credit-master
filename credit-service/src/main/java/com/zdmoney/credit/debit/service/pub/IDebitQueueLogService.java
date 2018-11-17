package com.zdmoney.credit.debit.service.pub;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.debit.domain.DebitQueueLog;
import com.zdmoney.credit.ljs.domain.CompensatoryDetailLufax;
import com.zdmoney.credit.repay.vo.DebitQueueManagementVo;

/**
 * 
 * @author YM10112
 *
 */
public interface IDebitQueueLogService{
    
    /**
     * 业务类型
     */
    public static final String PRODUCT_TYPE = "1000500010";

	/**
	 * 调用陆金所转发代扣接口-委托代扣
	 */
	public void executeEntrustDebit(List<DebitQueueLog> debitQueueLogList);
	
	/**
	 * 调用陆金所转发代扣接口-提前结清
	 */
	public void executeAdvanceClearDebit(List<DebitQueueLog> debitQueueLogList);
	
	/**
	 * 还款日后一天挂账金额分账
	 */
	public void executeBillRepayDeal(Date date);
	
	/**
	 * 接收到陆金所的划扣结果通知后，更新debit_queue_log表中的划扣状态 debit_notify_state
	 * @param debitQueueLogList
	 */
	public void updateDebitQueueLogAfterGetDeductResultNotifyFromLufax(String debitNo,String debitResultState);

	/**
	 * 分页查找 代扣管理信息
	 * @param debitQueueManagementVo
	 * @return
	 */
	public Pager findDebitQueueManagementWithPg(DebitQueueManagementVo debitQueueManagementVo);

	/**
	 * @param list
	 */
	public void executeEntrustDebit(List<DebitQueueLog> debitQueueLogList, boolean isUpdatePayParty);


	/**
	 * @param params
	 * @return
	 */
	public List<DebitQueueLog> findDebitQueueListByMap(Map<String, Object> params);
	
	/**
	 * 保存划扣队列记录
	 * @param debitQueueLog
	 * @return
	 */
	public DebitQueueLog saveDebitQueueLog(DebitQueueLog debitQueueLog);

	/**
	 * 根据debitNo查找 实际还款时间
	 * @param debitNo
	 * @return
	 */
	public String findTradeDateByDebitNo(String debitNo);
	
	/**
	 * 保存逾期代偿（准备金）
	 */
	public void perpareOverdueCompensatory();
    
	/**
	 * 发送逾期代偿（准备金）
	 */
    public void overdueCompensatory(List<DebitQueueLog> debitQueueLogList,String type);
    
    /**
     * 批量保存DebitQueueLog
     * @param list
     */
    public void batchSaveDebitQueueLog(List<DebitQueueLog> list);
    
    /**
     * 实时划扣后，及时掉用划扣接口，发送给第三方机构
     * @param list
     */
    public void executeEntrustDebitAfterDebitOffer(List<DebitQueueLog> debitQueueLogList);

	/**
	 * 一次性逾期回购
	 */
	public void oneTimeOverdueBuyBack(List<DebitQueueLog> debitQueueLogList);
	
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
     * 根据债权编号查询一次性回购代垫明细记录
     * @param loanId
     * @return
     */
    public CompensatoryDetailLufax queryBuyBackCompensatoryDetailLufax(Long loanId);

    /**
     * 更新划扣队列数据
     * @param debitQueueLog
     */
	public void updateDebitQueueLog(DebitQueueLog debitQueueLog);
	
	/**
	 * 查找主划扣记录  
	 * @param debitQueueLogList
	 * @return
	 */
	public DebitQueueLog findPrimaryDebitQueueLog(List<DebitQueueLog> debitQueueLogList);
       
}
