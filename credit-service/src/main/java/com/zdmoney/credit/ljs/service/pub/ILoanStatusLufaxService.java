package com.zdmoney.credit.ljs.service.pub;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.zdmoney.credit.common.vo.core.LoanVo;
import com.zdmoney.credit.framework.vo.lufax.entity.LoanDetailLufax;
import com.zdmoney.credit.ljs.domain.LoanStatusLufax;


/**
 * @author 10098  2017年5月15日 上午9:56:51
 */
public interface ILoanStatusLufaxService {

	/**
	 * @param params
	 * @return
	 */
	public List<LoanDetailLufax> findLoanDetailLufaxList(Map<String, Object> params);

	/**
	 * 添加陆金所债权状态
	 * @param id
	 * @param string
	 * @return
	 */
	public LoanStatusLufax addLoanStatusLufax(Long loanId, String loanStatus);

	/**
	 * 证大推送正式还款计划借据给陆金所
	 * @param loanVo
	 * @return
	 */
	public JSONObject pushRepayPlanAndLoanInfo2Lufax(LoanVo loanVo);

	/**
	 * 查找待推送给陆金所的 借据信息
	 * @param params
	 * @return
	 */
	public List<LoanDetailLufax> findLoanDetailLufaxList2Transmit(Map<String, Object> params);
	
	/**
	 * 同步借据信息后记录操作日志
	 * @param loanDetailLufaxList
	 */
	public void createOperateLog(List<LoanDetailLufax> loanDetailLufaxList);

	/**
	 * 更新逾期的陆金所状态
	 */
	public void updateLufuxStatusToYuqi();
}
