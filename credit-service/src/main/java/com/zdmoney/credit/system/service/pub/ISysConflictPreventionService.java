package com.zdmoney.credit.system.service.pub;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ISysConflictPreventionService {

	/**
	 * 立即保存
	 * @param string
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void saveNow(String key);

}
