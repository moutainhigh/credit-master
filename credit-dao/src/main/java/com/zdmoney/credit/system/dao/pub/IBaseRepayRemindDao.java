package com.zdmoney.credit.system.dao.pub;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.system.domain.BaseRepayRemind;

public interface IBaseRepayRemindDao extends IBaseDao<BaseRepayRemind>{

	public List<BigDecimal> getAmountList(Map<String, Object> params);
	
	/**
	 * 根据ID批量更新数据，必须包含ids（List），以及需更新的属性键值对<br>
	 * 可以使用BeanUtils的方法进行domain到map的转换
	 * @param params
	 */
	public int batchUpdateByPrimaryKeySelective(BaseRepayRemind repayRemind, List<Long> ids);
	
	/**
	 * 对应EMSService中private String buildSendResult(Date repayDate)方法
	 * @param repayDate
	 * @return
	 */
	public List<Map<String, Object>> getSendResultData(Date repayDate);
	
	/**
	 * 对应EMSService中emailSendResult方法的查询
	 * @param params
	 * @return
	 */
	public Integer getCountByRepayDateAndDeliverState(Map<String, Object> params);
	
	public Integer getCountByRepayDate(Map<String, Object> params);
	/**
	 * 对应EMSService中emailSendResult方法的查询
	 * @param repayDate
	 * @return
	 */
	public Integer getEmsCount(Date repayDate);
	
	/**
	 * 对应EMSService中public void generateData(Date repayDate, boolean forceRegernateRepayMind)的insert方法
	 * @param params
	 */
	public void emsGenerateData(Map<String, Object> params);
}
