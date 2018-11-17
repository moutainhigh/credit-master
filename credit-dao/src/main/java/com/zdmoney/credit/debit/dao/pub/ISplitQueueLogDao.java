package com.zdmoney.credit.debit.dao.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.common.vo.core.OneBuyBackCompensatedVO;
import com.zdmoney.credit.common.vo.core.RepayInfoLufaxVO;
import com.zdmoney.credit.common.vo.core.SplitQueueManangeVO;
import com.zdmoney.credit.debit.domain.SplitQueueLog;
import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.ljs.vo.SplitRepaymentVo;


public interface ISplitQueueLogDao  extends IBaseDao<SplitQueueLog>{
	/**
	 * 得到还款的本金211和利息451之和  
	 * @param map
	 * @return
	 */
	public  List<RepayInfoLufaxVO> findRepaymentInfoToLufax(Map<String, Object> map);

	List<SplitQueueLog> findSplitQueueLogListByParams(Map<String, Object> params);

	public List<SplitRepaymentVo> findSplitRepayment4Lufax(Map<String, Object> params);
	
	public List<RepayInfoLufaxVO> findRepayInfoLufaxList(Map<String, Object> map);
	
	public  List<RepayInfoLufaxVO> findDebitSplitQueueLogList(Map<String, Object> map);
	
	/**
	 * 查询未推送分账信息或推送分账信息失败的记录条数
	 * @param params
	 * @return
	 */
	public Integer queryPreSplitCount(Map<String, Object> params);
	
	/**
	 * 陆金所 查找还款分账信息
	 * @param params
	 * @return
	 */
	public List<SplitQueueManangeVO> findNotToThirdSplitList(Map<String, Object> params);
	
	/**
	 * 查询一次性回购和逾期代偿的借款信息
	 * @return
	 */
	public List<OneBuyBackCompensatedVO> findOneBuyBackAndCompensatedRepayInfo();
	
	/**
	 * 查询逾期还回和委托还款的分账信息
	 * @param params
	 * @return
	 */
	public List<SplitQueueLog> findOverdueEntrustSplitQueueLogList(Map<String, Object> params);
	
	/**
	 * 查询提前结清和委托还款的分账信息
	 * @param map
	 * @return
	 */
	public  List<RepayInfoLufaxVO> findAdvanceEntrustSplitQueueLogList(Map<String, Object> map);
}
