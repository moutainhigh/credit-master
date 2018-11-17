package com.zdmoney.credit.debit.service.pub;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.vo.core.RepayInfoLufaxVO;
import com.zdmoney.credit.debit.domain.SplitQueueLog;
import com.zdmoney.credit.framework.vo.lufax.input.Lufax100010Vo;
import com.zdmoney.credit.framework.vo.lufax.output.Lufax100018OutputVo;
import com.zdmoney.credit.ljs.vo.SplitRepaymentVo;
import com.zdmoney.credit.loan.domain.VLoanInfo;

/**
 * 
 * @author YM10112
 *
 */
public interface ISplitQueueLogService{

	/**
	 * 查询分页
	 * @param paramMap
	 * @return
	 */
	public Pager findWithPgByMap(Map<String, Object> paramMap);
	
	/**
	 * 创建分账队列 保存数据
	 * @param map
	 * @return
	 */
	public SplitQueueLog createSplitQueueLog(Long loanId,String debitNo,String lufaxNo,String batchId, BigDecimal frozenAmount);
	
	/**
	 * 委托还款  推送还款计划、还款明细、还款记录 
	 */
	public void executeEntrustRepaymentInfo(HashSet<String> splitIds);
	
	/**
	 * 机构还款 推送  还款计划，还款明细，还款记录
	 * @param list
	 */
	public void executeOrganRepaymentInfo(HashSet<String> splitIds);
	
	/**
	 * 提前结清还款  推送还款计划、还款明细、还款记录 
	 */
	public void executeAdvanceClearRepaymentInfo(HashSet<String> splitIds);
	
    /**
     * 判断是否可以进行还款、入账操作
     * @param loanId
     * @return
     */
    public boolean isCanRepayment(Long loanId);
    
    /**
     * 判断是否可以进行还款、入账操作
     * @param loanId
     * @return
     */
    public boolean isCanRepayment(Long loanId, String[] repayTypes);
    
    /**
	 * 把代扣结果给的值，设置到vo里面
	 * @param repayInfo
	 * @return
	 */
	public void disposeDeductResultNotifyFromLufax(List<RepayInfoLufaxVO> repayInfoList);
	
	/**
	 * 创建分账队列 
	 * @param splitQueueLog
	 * @return
	 */
	public SplitQueueLog createSplitQueueLog(SplitQueueLog splitQueueLog);

	/**
	 * 根据条件查询分账队列信息
	 * @param params
	 * @return
	 */
	public List<SplitQueueLog> findSplitQueueLogListByParams(Map<String, Object> params);
	
	/**
	 * 逾期代偿明细数据（还款计划、还款明细、还款记录）推送
	 */
	public void syncOverdueCompensatoryInfo(HashSet<String> splitIds);

	/**
	 * 陆金所 逾期还回，提前还款推送 还款计划、还款明细
	 * @param list
	 */
	public void executeOverdueRepaymentInfo(HashSet<String> splitIds);
	
	/**
     * @param jsonArray
     */
    public void dealOverdueCompensatory(JSONArray jsonArray);
    
    /**
     * @param jsonArray
     */
    public void marginAuditResultNotify(JSONArray jsonArray);

	/**
	 * 陆金所 查找还款分账信息
	 * @param params
	 * @return
	 */
	public List<SplitRepaymentVo> findSplitRepayment4Lufax(Map<String, Object> params);
	
	/**
	 * 根据逾期换回划扣队列 更新债权状态与 还款计划
	 * @param splitQueueLog
	 * @return
	 */
	public boolean updateRepaymentDetailAndLoanStatus4Lufax(SplitQueueLog splitQueueLog);

	public Lufax100010Vo getLufax100010VoByRepaymentPlan(List<VLoanInfo> list);
	
	/**
	 * 投资时间通知
	 * @param loanId
	 * @return
	 */
	public Lufax100018OutputVo investTimeNotify(long loanId,String investTime);

	/**
	 * 证大推送正式还款计划借据给陆金所
	 * @param loanId
	 */
	public void pushRepayPlanAndLoanInfo2Lufax(Long loanId);
	
	/**
	 * 一次性回购（还款计划、还款明细、还款记录）推送
	 */
	public void executeOneBuyBackRepaymentInfo(HashSet<String> splitIds);
	
	/**
	 * 证大同步一次性代偿、最后一期代偿追偿的还款记录和还款计划
	 */
	public void syncOneBuyBackAndCompensatedRepayInfo();
	
	/**
     * 查询逾期代偿或者一次性回购超过三个宽限日所垫付的罚息
     * @param debitQueueLog
     * @return
     */
	public BigDecimal queryCompensatoryPenaltyAmount(Long loanId, Long term);
	
	/**
	 * 单独同步还款计划给陆金所
	 * @param vo
	 */
	public void syncRepaymentPlan(RepayInfoLufaxVO vo);
	
	/**
	 * 单独同步还款记录给陆金所
	 * @param vo
	 */
	public void syncRepaymentRecord(RepayInfoLufaxVO vo);
	
	/**
	 * 单独同步还款明细给陆金所
	 * @param vo
	 */
	public void syncRepaymentDetail(RepayInfoLufaxVO vo);

	/**
	 * 更新分账队列数据
	 * @param vo
	 */
	public void updateSplitQueue(SplitQueueLog vo);
	
    /**
     * 判断是否可以进行还款、入账操作
     * @param loanId
     * @return
     */
    public boolean isCanRepayment(Long loanId, String repayType);
	
	 /**
     * 判断是否可以进行对公还款认领、入账操作
     * @param loanId
     * @return
     */
    public boolean isCanPublicAccountRepayment(Long loanId);
    
    /**
     * 查询逾期还回和委托还款的分账信息
     * @param params
     * @return
     */
    public List<SplitQueueLog> findOverdueEntrustSplitQueueLogList(Map<String, Object> params);
}
