package com.zdmoney.credit.system.service.pub;

import java.util.List;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.system.domain.BaseMessage;

/**
 * 消息中心
 * @author Anfq
 *
 * 2015年7月21日
 */
public interface IBaseMessageService {
	
	public List<BaseMessage> getList(BaseMessage baseMessage);
	
	public BaseMessage get(BaseMessage baseMessage);
	
	
	public void inserBaseMessage(BaseMessage baseMessage);
	
	public Pager findWithPg(BaseMessage baseMessage);
	
	public int update(BaseMessage baseMessage);
	
	public int batchUpdateBaseMessageState(List<Long> list);
	public int batchUpdateBaseMessageDelete(List<Long> list);
	public int batchUpdateBaseMessageView(List<Long> list);
	
	public BaseMessage get(Long id);
	
	public int countNoView(BaseMessage baseMessage);

	
}
