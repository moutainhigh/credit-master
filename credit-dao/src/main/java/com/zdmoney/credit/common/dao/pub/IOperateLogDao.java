package com.zdmoney.credit.common.dao.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.common.domain.OperateLog;
import com.zdmoney.credit.framework.dao.pub.IBaseDao;

public interface IOperateLogDao extends IBaseDao<OperateLog>{

	/**
	 * 获取待下载的记录
	 * @return
	 */
	List<OperateLog> findOperateLogList2DownLoad(Map<String, Object> params);

	/**
	 *陆金所 获取需同步 的正式还款计划与债权信息
	 * @return
	 */
	List<Long> findLoanIds4FormalRepayPlans2Lufax();
}
