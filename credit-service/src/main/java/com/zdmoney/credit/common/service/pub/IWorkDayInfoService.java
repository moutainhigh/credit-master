package com.zdmoney.credit.common.service.pub;

import java.util.Date;

import com.zdmoney.credit.common.domain.WorkDayInfo;

import java.util.Map;

public interface IWorkDayInfoService {

	/**
	 * 通过参数 查找工作日信息
	 * @param params
	 * @return
	 */
	public WorkDayInfo getWorkDayInfoByParams(Map<String, Object> params);

	/**
	 * 查找上一个 工作日信息
	 * @param params
	 * @return
	 */
	public WorkDayInfo getPreviousWorkDayInfoByParams( Map<String, Object> params);

	/**
	 * 是否工作日
	 * @param currentDate
	 * @return true 工作日 false 节假日
	 */
	public boolean isWorkDay(Date currentDate);
	/**
	 * 查找上一个 工作时间
	 * @param currentDate
	 * @return
	 */
	public Date getPreviousWorkDayInfoByParams(Date currentDate);
	/**
	 * 查找下一个 工作日信息
	 * @param params
	 * @return
	 */
	public WorkDayInfo getAfterWorkDayInfoByParams( Map<String, Object> params);
	/**
	 * 查找下一个 工作日时间
	 * @param currentDate
	 * @return
	 */
	public Date getAfterWorkDayInfoByParams( Date currentDate);

	/**
	 * 是否是每个节假日之后的第一个工作日
	 * @param currentDate
	 * @return true 第一个工作日 false 非第一个工作日
	 */
	public boolean isFirstWorkDay(Date currentDate);
	
	/**
	 * 获取两个时间之间的工作日天数
	 * @param params
	 * @return
	 */
	public Integer getWorkDaysInRegion(Date beginDate, Date endDate);
}
