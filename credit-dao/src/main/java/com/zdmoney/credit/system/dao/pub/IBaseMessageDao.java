package com.zdmoney.credit.system.dao.pub;

import java.util.List;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.system.domain.BaseMessage;

/**
 * 消息中心
 * @author Anfq
 *
 * 2015年7月21日
 */
public interface IBaseMessageDao  extends IBaseDao<BaseMessage>{
	public int  batchUpdateMessageState (List<Long> list);
	public int  batchUpdateMessageStateDelete (List<Long> list);
	public int  batchUpdateMessageStateView (List<Long> list);
	public int  countNoView(BaseMessage baseMessage);
	
}
