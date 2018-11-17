package com.zdmoney.credit.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.system.dao.pub.ISequencesDao;
import com.zdmoney.credit.system.service.pub.ISequencesService;

/**
 * 序列Service业务封装层
 * @author Ivan
 *
 */
@Service
@Transactional
public class SequencesServiceImpl implements ISequencesService {
	
	@Autowired @Qualifier("sequencesDaoImpl")
	ISequencesDao sequencesDaoImpl;
	
	/**
	 * 获取表序列
	 * @param sequencesEnum 序列枚举值
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.NOT_SUPPORTED,readOnly=true)
	public Long getSequences(SequencesEnum sequencesEnum) {
		Object seqValue = sequencesDaoImpl.getSeq(sequencesEnum.getSequencesName());
		return Strings.convertValue(seqValue,Long.class);
	}
}
