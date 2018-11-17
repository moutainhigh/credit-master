package com.zdmoney.credit.system.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.util.BeanUtils;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.system.dao.pub.IBaseRepayRemindDao;
import com.zdmoney.credit.system.domain.BaseRepayRemind;

@Repository
public class BaseRepayRemindDaoImpl extends BaseDaoImpl<BaseRepayRemind> implements IBaseRepayRemindDao{

	@Override
	public List<BigDecimal> getAmountList(Map<String, Object> params) {
		
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".getAmountList", params);
	}

	@Override
	public int batchUpdateByPrimaryKeySelective(BaseRepayRemind repayRemind, List<Long> ids) {
		/** 获取登陆者信息 **/
		User user = UserContext.getUser();
		if (user == null) {
			repayRemind.setUpdator("admin");
		} else {
			repayRemind.setUpdator(user.getName());
		}
		repayRemind.setUpdateTime(new Date());
		
		Map<String, Object> params = BeanUtils.toMap(repayRemind);
		params.put("ids", ids);
		return getSqlSession().update(getIbatisMapperNameSpace() + ".batchUpdateByPrimaryKeySelective", params);
		
	}

	@Override
	public List<Map<String, Object>> getSendResultData(Date repayDate) {
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".getSendResultData", repayDate);
	}

	@Override
	public Integer getCountByRepayDateAndDeliverState(
			Map<String, Object> params) {
		return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".getCountByRepayDateAndDeliverState", params);
	}
	
	@Override
	public Integer getCountByRepayDate(
			Map<String, Object> params) {
		return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".getCountByRepayDate", params);
	}

	@Override
	public Integer getEmsCount(Date repayDate) {
		return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".getEmsCount", repayDate);
	}

	@Override
	public void emsGenerateData(Map<String, Object> params) {
		getSqlSession().insert(getIbatisMapperNameSpace() + ".emsGenerateData", params);
		
	}

}
