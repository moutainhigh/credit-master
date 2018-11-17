package com.zdmoney.credit.offer.dao.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.offer.domain.FuiouOfferJobEntry;

public interface IFuiouOfferJobEntryDao extends IBaseDao<FuiouOfferJobEntry>{
	
	/**
	 * 获取 FuiouOfferJob所需数据（22时的定时批处理）
	 * @param dateStr
	 * @return
	 */
	public List<FuiouOfferJobEntry> getFuiouOffer22(String dateStr);
	
	/**
	 * 获取 FuiouOfferJob所需数据（除22时）
	 * @param params 包含参数：dateStr-日期yyyy-mm-dd，hour-小时数0-23
	 * @return
	 */
	public List<FuiouOfferJobEntry> getFuiouOffer(Map<String, Object> params);

}
