package com.zdmoney.credit.loan.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.loan.dao.pub.IUploadLoanDataInfoDao;
import com.zdmoney.credit.loan.domain.UploadLoanDataInfo;

/**
 * @author 10098  2017年3月1日 上午11:28:05
 */
@Repository
public class UploadLoanDataInfoDaoImpl extends BaseDaoImpl<UploadLoanDataInfo> implements IUploadLoanDataInfoDao {

	@Override
	public List<UploadLoanDataInfo> findUploadLoanDataInfoByBatchNum(
			Map<String, Object> params) {
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".findUploadLoanDataInfoByBatchNum", params);
	}

}
