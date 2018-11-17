package com.zdmoney.credit.bsyh.service.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.loan.domain.RepayResultNotifyLog;


/**
 * 包商银行 自扣还款结果通知接口
 * @author YM10112  2017年1月17日
 *
 */
public interface IRepayResultNotifyLogService{
	
	/**
	 *  保存自扣还款结果
	 * @param repayResultNotifyLog
	 * @return
	 */
	public RepayResultNotifyLog save(RepayResultNotifyLog repayResultNotifyLog);
	
	
	/**
	 *  更新自扣还款结果
	 * @param repayResultNotifyLog
	 * @return
	 */
	public int update(RepayResultNotifyLog repayResultNotifyLog);
	
	
	/**
	 *  更新自扣还款结果
	 * @param repayResultNotifyLog
	 * @return
	 */
	public RepayResultNotifyLog get(Long id);
	
	/**
	 * 查询 包银返回自扣还款结果日志
	 * @param map
	 * @return
	 */
	public List<RepayResultNotifyLog> findListByMap(Map<String, Object> map);
	

}
