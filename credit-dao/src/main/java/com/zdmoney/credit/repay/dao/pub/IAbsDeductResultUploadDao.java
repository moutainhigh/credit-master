package com.zdmoney.credit.repay.dao.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.repay.domain.AbsDeductResultUpload;

public interface IAbsDeductResultUploadDao extends IBaseDao<AbsDeductResultUpload> {

	/**
	 * 根据条件查找证大扣款结果信息
	 * @param params
	 * @return
	 */
	public List<AbsDeductResultUpload> findDeductResutListByMap(Map<String, Object> params);
}
