package com.zdmoney.credit.system.dao;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.system.dao.pub.ISequencesDao;

/**
 * 获取Oracle 序列值
 * @author Ivan
 *
 */
@Repository
public class SequencesDaoImpl extends BaseDaoImpl implements ISequencesDao {
	/**
	 * 获取Oracle 序列值
	 * @param seqName 序列名称
	 * @return
	 */
	@Override
	public Object getSeq(String seqName) {
		seqName += ".NEXTVAL";
		return selectOne(seqName);
	}
}
