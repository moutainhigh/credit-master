package com.zdmoney.credit.common.service.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.common.domain.OperateLog;


/**
 * 外贸3 操作日志
 * @author YM10112 
 *
 */
public interface IOperateLogService{
	
	/**
	 *  保存日志
	 * @param 
	 * @return
	 */
	public OperateLog save(OperateLog operateLog);
	
	
	/**
	 *  更新日志
	 * @param operateLog
	 * @return
	 */
	public int update(OperateLog operateLog);
	
	
	/**
	 * 查询 包银返回自扣还款结果日志
	 * @param map
	 * @return
	 */
	public List<OperateLog> findListByMap(Map<String, Object> map);


	/**
	 * 添加操作记录
	 * @param id
	 * @param string
	 * @param string2
	 */
	public OperateLog addOperateLog(Long loanId, String operateType, String status);


	/**
	 *  获取待下载的记录
	 * @return
	 */
	public List<OperateLog> findOperateLogList2DownLoad(Map<String, Object> params);

	/**
	 * 陆金所 获取需同步 的正式还款计划与债权信息
	 * @return
	 */
	public List<Long> findLoanIds4FormalRepayPlans2Lufax();
}
