package com.zdmoney.credit.common.service;

import com.zdmoney.credit.common.dao.pub.IWorkDayInfoDao;
import com.zdmoney.credit.common.domain.WorkDayInfo;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.service.pub.IWorkDayInfoService;
import com.zdmoney.credit.common.util.Dates;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class WorkDayInfoServiceImpl implements IWorkDayInfoService {

	@Autowired
	private IWorkDayInfoDao workDayInfoDao; 
	
	public WorkDayInfo getWorkDayInfoByParams(Map<String, Object> params) {
		return workDayInfoDao.getWorkDayInfoByParamsMap(params);
	}

	@Override
	public WorkDayInfo getPreviousWorkDayInfoByParams(Map<String, Object> params) {
		return workDayInfoDao.getPreviousWorkDayInfoByParams(params);
	}

	public boolean isWorkDay(Date currentDate){
		Map<String, Object> workDayParams = new HashMap<String, Object>();
		workDayParams.put("currDate", Dates.getDateTime(currentDate, Dates.DEFAULT_DATE_FORMAT));
		WorkDayInfo workDayInfo = this.getWorkDayInfoByParams(workDayParams);
		if(workDayInfo == null || "0".equals(workDayInfo.getIsWorkDay())){
			//非工作日
			return false;
		}
		//工作日
		return true;
	}

	@Override
	public Date getPreviousWorkDayInfoByParams(Date currentDate) {
		Map<String, Object> workDayParams = new HashMap<String, Object>();
		workDayParams.put("currDate", Dates.getDateTime(currentDate,Dates.DEFAULT_DATE_FORMAT));
		WorkDayInfo workDayInfo = this.getPreviousWorkDayInfoByParams(workDayParams);
		if (workDayInfo == null) {
			throw new PlatformException(ResponseEnum.FULL_MSG,"请配置工作日时间");
		}
		return workDayInfo.getCurrDate();
	}
	@Override
	public WorkDayInfo getAfterWorkDayInfoByParams(Map<String, Object> params) {
		return workDayInfoDao.getAfterWorkDayInfoByParams(params);
	}

	@Override
	public Date getAfterWorkDayInfoByParams(Date currentDate) {
		Map<String, Object> workDayParams = new HashMap<String, Object>();
		workDayParams.put("currDate", Dates.getDateTime(currentDate,Dates.DEFAULT_DATE_FORMAT));
		WorkDayInfo workDayInfo = this.getAfterWorkDayInfoByParams(workDayParams);
		if (workDayInfo == null) {
			throw new PlatformException(ResponseEnum.FULL_MSG,"请配置工作日时间");
		}
		return workDayInfo.getCurrDate();
	}

	@Override
	public boolean isFirstWorkDay(Date currentDate) {
		if (isWorkDay(currentDate)) {
			Date previousWorkDay = this.getPreviousWorkDayInfoByParams(currentDate);
			if (Dates.dateDiff(previousWorkDay,currentDate) > 1) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Integer getWorkDaysInRegion(Date beginDate, Date endDate) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("beginDate", beginDate);
		params.put("endDate", endDate);
		return workDayInfoDao.getWorkDaysInRegion(params);
	}
}
