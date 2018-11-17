package com.zdmoney.credit.system.dao.pub;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;


/**
 * 获取Oracle 序列值
 * @author Ivan
 *
 */
public interface ISequencesDao extends IBaseDao {
	/**
	 * 获取Oracle 序列值
	 * @param seqName 序列名称
	 * @return
	 */
	public Object getSeq(String seqName);
}
