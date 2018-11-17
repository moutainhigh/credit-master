package com.zdmoney.credit.system.service.pub;

import com.zdmoney.credit.common.constant.system.SequencesEnum;


/**
 * 获取Oracle 序列值
 * @author Ivan
 *
 */
public interface ISequencesService {
	
	/**
	 * 获取表序列
	 * @param sequencesEnum 序列枚举值
	 * @return
	 */
	public Long getSequences(SequencesEnum sequencesEnum);
	
}
