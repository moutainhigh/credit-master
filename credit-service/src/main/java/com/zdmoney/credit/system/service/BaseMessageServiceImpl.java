package com.zdmoney.credit.system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.system.dao.pub.IBaseMessageDao;
import com.zdmoney.credit.system.domain.BaseMessage;
import com.zdmoney.credit.system.service.pub.IBaseMessageService;
import com.zdmoney.credit.system.service.pub.ISequencesService;

@Service
public class BaseMessageServiceImpl implements IBaseMessageService{
	@Autowired @Qualifier("sequencesServiceImpl")
	ISequencesService sequencesServiceImpl;
	
	@Autowired
	IBaseMessageDao baseMessageDaoImpl;
	
	@Override
	public List<BaseMessage> getList(BaseMessage baseMessage) {
		// TODO Auto-generated method stub
		return baseMessageDaoImpl.findListByVo(baseMessage);
	}

	@Override
	public BaseMessage get(BaseMessage baseMessage) {
		// TODO Auto-generated method stub
		return baseMessageDaoImpl.get(baseMessage);
	}

	@Override
	public void inserBaseMessage(BaseMessage baseMessage) {
		baseMessage.setId(sequencesServiceImpl.getSequences(SequencesEnum.BASE_MESSAGE));
		baseMessageDaoImpl.insert(baseMessage);
	}

	@Override
	public Pager findWithPg(BaseMessage baseMessage) {
		// TODO Auto-generated method stub
		return baseMessageDaoImpl.findWithPg(baseMessage);
	}

	@Override
	public int update(BaseMessage baseMessage) {
		// TODO Auto-generated method stub
		return baseMessageDaoImpl.update(baseMessage);
	}

	@Override
	public int batchUpdateBaseMessageState(List<Long> list) {
		// TODO Auto-generated method stub
		return baseMessageDaoImpl.batchUpdateMessageState(list);
	}

	@Override
	public int batchUpdateBaseMessageDelete(List<Long> list) {
		// TODO Auto-generated method stub
		return baseMessageDaoImpl.batchUpdateMessageStateDelete(list);
	}

	@Override
	public int batchUpdateBaseMessageView(List<Long> list) {
		// TODO Auto-generated method stub
		return baseMessageDaoImpl.batchUpdateMessageStateView(list);
	}

	@Override
	public BaseMessage get(Long id) {
		// TODO Auto-generated method stub
		return baseMessageDaoImpl.get(id);
	}

	@Override
	public int countNoView(BaseMessage baseMessage) {
		// TODO Auto-generated method stub
		return baseMessageDaoImpl.countNoView(baseMessage);
	}







	
}
