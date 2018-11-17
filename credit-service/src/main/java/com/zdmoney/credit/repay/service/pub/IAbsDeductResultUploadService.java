package com.zdmoney.credit.repay.service.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.repay.domain.AbsDeductResultUpload;

public interface IAbsDeductResultUploadService {

	/**
	 * 获取证大扣款结果列表
	 * @param params
	 * @return
	 */
	public List<AbsDeductResultUpload> getDeductResutList2Upload(Map<String, Object> params);

	/**
	 * 保存扣款结果
	 * @param list
	 */
	public void saveDeductResultList(List<AbsDeductResultUpload> list);

	/**
	 * 查询扣款结果表中信息
	 * @param map
	 * @return
	 */
	public List<AbsDeductResultUpload> getDeductResutListByStatus(Map<String, Object> map);

	/**
	 * 口款结果上传至数信
	 * @param uploadList
	 */
	public void uploadDeductList2Abs(List<AbsDeductResultUpload> uploadList);

}
