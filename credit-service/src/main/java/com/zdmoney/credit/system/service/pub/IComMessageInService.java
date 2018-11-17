package com.zdmoney.credit.system.service.pub;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zdmoney.credit.system.domain.ComMessageIn;

/**
 * 系统报文接收记录表service
 * @author 00232949
 *
 */
public interface IComMessageInService {
	
	
	/**
	 * 跟据消息类型 + 文件名 查询结果集
	 * @param messageType 消息类型
	 * @param fileName 文件名
	 */
	public List<ComMessageIn> findByMessageTypeAndFileName(String messageType, String fileName);
	
	/**
	 * 创建msgin记录
	 * @param messageType
	 * @param messageId
	 * @param messageBody
	 * @param sender
	 * @param version 
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public ComMessageIn createMsgIn(String messageType, String messageId, String messageBody ,String sender, String version);
	
	/**
	 * 创建msgin记录
	 * @param messageType
	 * @param sender
	 * @param fileName
	 * @param memo
	 */
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public ComMessageIn createMsgIn(String messageType, String sender,String fileName,String memo);

}
