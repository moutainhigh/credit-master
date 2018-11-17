package com.zdmoney.credit.core.service.pub;

import java.util.Date;
import java.util.Map;

import com.zdmoney.credit.common.vo.core.SpecialRepayParamsVO;
import com.zdmoney.credit.system.domain.ComEmployee;

public interface ISpecialRepaymentCoreService {

	/**
	 * 提前扣款时间更新
	 * @param paramsVo 特殊还款参数VO
	 * @return
	 */
	public Map<String,Object> updateSpecialRepayment(SpecialRepayParamsVO paramsVo);
	
	
	/**
	 * 特殊还款操作方法，用途：提前结清、提前扣款、费用减免
	 * @param paramsVo 特殊还款参数VO
	 * @return
	 */
	public Map<String,Object> handleSpecialRepayment(SpecialRepayParamsVO paramsVo,ComEmployee employee);
	
	
	/**
	 * 校验指定日期是否早于当前日期或晚于还款日
	 * @param loanId 债权编号ID
	 * @param assginDate 指定日期
	 * @return 
	 */
	public String validDateForLateOrearly(Long loanId,Date assginDate);
	
	
	/**
	 * 特殊还款查询
	 * @param params 查询参数集合
	 * @return
	 */
	public Map<String, Object> querySpecialRepayment(SpecialRepayParamsVO paramsVo);
	
	/**
	 * 罚息减免试算查询
	 * @param params 查询参数集合
	 * @return
	 */
	public Map<String, Object> queryReliefPenalty(SpecialRepayParamsVO paramsVo);
}
