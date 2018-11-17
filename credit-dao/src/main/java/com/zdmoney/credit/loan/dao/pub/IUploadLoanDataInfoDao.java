package com.zdmoney.credit.loan.dao.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.loan.domain.UploadLoanDataInfo;

/**
 * @author 10098  2017年3月1日 上午11:23:48
 */
public interface IUploadLoanDataInfoDao extends IBaseDao<UploadLoanDataInfo> {

	/**
	 * @param params
	 * @return
	 */
	List<UploadLoanDataInfo> findUploadLoanDataInfoByBatchNum(Map<String, Object> params);

}
