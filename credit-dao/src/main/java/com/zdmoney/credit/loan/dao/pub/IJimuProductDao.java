package com.zdmoney.credit.loan.dao.pub;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.loan.domain.JimuProduct;

public interface IJimuProductDao extends IBaseDao<JimuProduct> {
	
	/**
	 * 通过time查找
	 * @param time
	 * @return
	 */
	public JimuProduct findByTime(Long time);

}
