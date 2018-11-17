package com.zdmoney.credit.ljs.dao.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.ljs.domain.PublicAccountDetail;

/**
 * 
 * @author YM10104
 *
 */
public interface IPublicAccountDetailDao  extends IBaseDao<PublicAccountDetail>{

	PublicAccountDetail getLastAccountDetail(String accountType);

	List<PublicAccountDetail> findByMap(Map<String, Object> paramMap);

	void updateAccDetailByBatchNo(PublicAccountDetail publicAccountDetail);

	PublicAccountDetail findByBatchNo(String batchNo);

	
}
