package com.zdmoney.credit.system.dao.pub;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.system.domain.BaseEmssendInfo;

public interface IBaseEmssendInfoDao extends IBaseDao<BaseEmssendInfo>{
	
	public BaseEmssendInfo getRepayRemindLastEmssendInfo(Long repayRemindId);
	
	public List<String> getDistinctSendIdByRepayDate(Date repayDate);
	
	/**
	 * 根据ID批量更新数据，必须包含ids（List），以及需更新的属性键值对<br>
	 * 可以使用BeanUtils的方法进行domain到map的转换
	 * @param params
	 */
	public int batchUpdateByPrimaryKeySelective(BaseEmssendInfo sendInfo, List<Long> ids);
	
	/**
	 * 
	 * @param params repayDate, deliverState, sendState
	 * @return
	 */
	public List<BaseEmssendInfo> getSendInfoByRepayDateAndState(Map<String, Object> params);
}
