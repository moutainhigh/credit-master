package com.zdmoney.credit.common.dao.pub;

import java.util.Map;

import com.zdmoney.credit.common.domain.WorkDayInfo;
import com.zdmoney.credit.framework.dao.pub.IBaseDao;

public interface IWorkDayInfoDao extends IBaseDao<WorkDayInfo> {

	/**
	 * 通过Map参数查找WorkDayInfo
	 * @param params
	 * @return
	 */
	public WorkDayInfo getWorkDayInfoByParamsMap(Map<String, Object> params);

	/**
	 * 通过Map参数查找上一个工作日信息
	 * @param params
	 * @return
	 */
	public WorkDayInfo getPreviousWorkDayInfoByParams(Map<String, Object> params);

	/**
	 * 通过Map参数查找下一个工作日信息
	 * @param params
	 * @return
	 */
	public WorkDayInfo getAfterWorkDayInfoByParams(Map<String, Object> params);
	
	/**
	 * 获取两个时间之间的工作日天数
	 * @param params
	 * @return
	 */
	public Integer getWorkDaysInRegion(Map<String, Object> params);
}
