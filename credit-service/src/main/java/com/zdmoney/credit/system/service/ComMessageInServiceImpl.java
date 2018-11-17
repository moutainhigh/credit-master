package com.zdmoney.credit.system.service;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.system.dao.pub.IComMessageInDao;
import com.zdmoney.credit.system.domain.ComMessageIn;
import com.zdmoney.credit.system.service.pub.IComMessageInService;
import com.zdmoney.credit.system.service.pub.ISequencesService;

@Service
public class ComMessageInServiceImpl implements IComMessageInService{
	
	private static final Logger logger = Logger.getLogger(ComMessageInServiceImpl.class);
	
	@Autowired
	IComMessageInDao comMessageInDao;
	
	@Autowired @Qualifier("sequencesServiceImpl")
	ISequencesService sequencesServiceImpl;
	
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	@Override
	public ComMessageIn createMsgIn(String messageType, String messageId,String messageBody, String sender, String version) {
		
		try {
			ComMessageIn comMessageIn = new ComMessageIn();
			comMessageIn.setId(sequencesServiceImpl.getSequences(SequencesEnum.COM_MESSAGE_IN));
			comMessageIn.setMessageType(messageType);
			comMessageIn.setExtMessageId(messageId);
			comMessageIn.setMessageBody(messageBody);
			comMessageIn.setCreator("SYSTEM");
			comMessageIn.setSender(sender);
			comMessageIn.setCreateTime(new Date());
			comMessageIn.setVersion(version);
			return comMessageInDao.insert(comMessageIn);
			
		} catch (Exception e) {
			logger.error("保存MSGIN出错！messageBody="+messageBody, e);
		}
		return null;
		
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	@Override
	public ComMessageIn createMsgIn(String messageType, String sender,String fileName,String memo){
		ComMessageIn comMessageIn = new ComMessageIn();
		comMessageIn.setId(sequencesServiceImpl.getSequences(SequencesEnum.COM_MESSAGE_IN));
		comMessageIn.setMessageType(messageType);
		comMessageIn.setExtMessageId("");
		comMessageIn.setMessageBody("");
		comMessageIn.setCreator(sender);
		comMessageIn.setSender(sender);
		comMessageIn.setCreateTime(new Date());
		comMessageIn.setVersion("");
		comMessageIn.setFileName(fileName);
		comMessageIn.setMemo(memo);
		return comMessageInDao.insert(comMessageIn);
	}
	
	/**
	 * 跟据消息类型 + 文件名 查询结果集
	 * @param messageType 消息类型
	 * @param fileName 文件名
	 */
	@Override
	public List<ComMessageIn> findByMessageTypeAndFileName(String messageType, String fileName){
		ComMessageIn comMessageIn = new ComMessageIn();
		comMessageIn.setMessageType(messageType);
		comMessageIn.setFileName(fileName);
		return comMessageInDao.findListByVo(comMessageIn);
	}
	
}
