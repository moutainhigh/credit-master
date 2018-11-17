package com.zdmoney.credit.system.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.common.login.UserContext;
import com.zdmoney.credit.common.login.vo.User;
import com.zdmoney.credit.common.util.BeanUtils;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.system.dao.pub.IBaseEmssendInfoDao;
import com.zdmoney.credit.system.domain.BaseEmssendInfo;

@Repository
public class BaseEmssendInfoDaoImpl extends BaseDaoImpl<BaseEmssendInfo> implements IBaseEmssendInfoDao{

	@Override
	public BaseEmssendInfo getRepayRemindLastEmssendInfo(Long repayRemindId) {
		return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".getRepayRemindLastEmssendInfo", repayRemindId);
	}

	@Override
	public List<String> getDistinctSendIdByRepayDate(Date repayDate) {
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".getDistinctSendIdByRepayDate", repayDate);
	}

	@Override
	public int batchUpdateByPrimaryKeySelective(BaseEmssendInfo sendInfo,
			List<Long> ids) {
		/** 获取登陆者信息 **/
		User user = UserContext.getUser();
		if (user == null) {
			sendInfo.setUpdator("admin");
		} else {
			sendInfo.setUpdator(user.getName());
		}
		sendInfo.setUpdateTime(new Date());
		
		Map<String, Object> params = BeanUtils.toMap(sendInfo);
		params.put("ids", ids);
		return getSqlSession().update(getIbatisMapperNameSpace() + ".batchUpdateByPrimaryKeySelective", params);
	}

	@Override
	public List<BaseEmssendInfo> getSendInfoByRepayDateAndState(
			Map<String, Object> params) {
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".getSendInfoByRepayDateAndState", params);
	}

}
