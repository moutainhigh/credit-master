package com.zdmoney.credit.blackList.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.blackList.dao.pub.IBlackListDao;
import com.zdmoney.credit.blacklist.domain.ComBlacklist;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;

@Repository
public class BlackListDaoImpl extends BaseDaoImpl<ComBlacklist> implements IBlackListDao {



	@Override
	public List<ComBlacklist> findLocalCustomerBlacklist(Date date) {
		Map<String,Object> map =new HashMap<String,Object>();
		map.put("canSubmitRequestDays", date);
		
		List<ComBlacklist> list = getSqlSession().selectList(getIbatisMapperNameSpace() + ".findLocalCustomerBlacklist",map);
		return list;
	}

	@Override
	public List<ComBlacklist> findLocalEnterpriseBlacklist(Date date) {
		Map<String,Object> map =new HashMap<String,Object>();
		map.put("canSubmitRequestDays", date);
		
		List<ComBlacklist> list = getSqlSession().selectList(getIbatisMapperNameSpace() + ".findLocalEnterpriseBlacklist",map);
		return list;
	}

	@Override
	public List<ComBlacklist> findAllByDateCreatedBetweenAndComeFromeNotEqual(
			Date begin, Date end, String organ, Integer[] canSubmitRequestDays) {
		Map<String,Object> map =new HashMap<String,Object>();
		map.put("begin", begin);
		map.put("end", end);
		map.put("organ", organ);
		map.put("canSubmitRequestDays", canSubmitRequestDays);

		List<ComBlacklist> list = getSqlSession().selectList(getIbatisMapperNameSpace() + ".findAllByDateCreatedBetweenAndComeFromeNotEqual",map);
		return list;
	}

}
