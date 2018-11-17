package com.zdmoney.credit.system.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.system.dao.pub.IBaseMessageDao;
import com.zdmoney.credit.system.domain.BaseMessage;
@Repository
public class BaseMessageDaoImpl extends BaseDaoImpl<BaseMessage> implements IBaseMessageDao{

	/**
	 * 批量更新
	 */
	@Override
	public int batchUpdateMessageState(List<Long> list) {
		int affectNum = 0;
		affectNum=getSqlSession().update(getIbatisMapperNameSpace() + ".batchUpdateBaseMessageState", list);
		return affectNum;	
	}

	/**
	 * 批量更新
	 */
	@Override
	public int batchUpdateMessageStateDelete(List<Long> list) {
		int affectNum = 0;
		affectNum=getSqlSession().update(getIbatisMapperNameSpace() + ".batchUpdateMessageStateDelete", list);
		return affectNum;	
	}
	/**
	 * 表示已查看
	 */
	public int batchUpdateMessageStateView(List<Long> list) {
		int affectNum = 0;
		affectNum=getSqlSession().update(getIbatisMapperNameSpace() + ".batchUpdateMessageStateView", list);
		return affectNum;	
	}
	
	public int countNoView(BaseMessage baseMessage){
		int affectNum = 0;
		affectNum=getSqlSession().selectOne(getIbatisMapperNameSpace() + ".countNoView",baseMessage);
		return affectNum;	
	}
}
