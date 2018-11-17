package com.zdmoney.credit.core.service.pub;

import java.util.Map;

import com.zdmoney.credit.common.vo.core.FinanceVo;

/**
 * 财务类对外接口处理
 * @author 00232949
 *
 */
public interface IFinanceCoreService {

	/**
	 * 放款推送处理
	 * @param params
	 * @return
	 */
	public Map<String, Object> grantLoan(FinanceVo params);

	/**
	 * 放款撤销处理
	 * @param params
	 * @return
	 */
	public Map<String, Object> rollbackGrantLoan(FinanceVo params);

}
