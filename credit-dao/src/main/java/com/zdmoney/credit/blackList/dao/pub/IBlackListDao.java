package com.zdmoney.credit.blackList.dao.pub;

import java.util.Date;
import java.util.List;

import com.zdmoney.credit.blacklist.domain.ComBlacklist;
import com.zdmoney.credit.framework.dao.pub.IBaseDao;

/**
 * 黑名单
 * @author 00232949
 *
 */
public interface IBlackListDao  extends IBaseDao<ComBlacklist>{

	/**
	 * 得到本系统添加的人员黑名单，用于同步给总部
	 * @param date
	 * @return
	 */
	List<ComBlacklist> findLocalCustomerBlacklist(Date date);

	/**
	 * 得到本系统添加企业黑名单，用于同步给总部
	 * @param date
	 * @return
	 */
	List<ComBlacklist> findLocalEnterpriseBlacklist(Date date);
	
	List<ComBlacklist> findAllByDateCreatedBetweenAndComeFromeNotEqual(Date begin,Date end,String organ, Integer[] canSubmitRequestDays);

}
