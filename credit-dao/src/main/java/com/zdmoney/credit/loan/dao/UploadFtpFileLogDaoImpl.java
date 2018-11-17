package com.zdmoney.credit.loan.dao;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.zdmoney.credit.common.util.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.loan.dao.pub.IUploadFtpFileLogDao;
import com.zdmoney.credit.loan.domain.UploadFtpFileLog;

/**
 * Created by ym10094 on 2016/8/23.
 */
@Repository
public class UploadFtpFileLogDaoImpl extends BaseDaoImpl<UploadFtpFileLog> implements IUploadFtpFileLogDao {

/*    @Override
    public List<UploadFtpFileLog> findUploadFtpFileLogFails() {
        List<UploadFtpFileLog> list=getSqlSession().selectList(getIbatisMapperNameSpace() + ".findUploadFtpFileLogFails");
        return list;
    }*/

    @Override
    public UploadFtpFileLog findUploadFtpFileLog(String contract_num) {
        List<UploadFtpFileLog> uploadFtpFileLogs = getSqlSession().selectList(getIbatisMapperNameSpace() + ".findUploadFtpFileLog",contract_num);
        if (CollectionUtils.isEmpty(uploadFtpFileLogs)) {
            return null;
        }
        return uploadFtpFileLogs.get(0);
    }

	@Override
	public List<UploadFtpFileLog> findUploadFtpFileByMap(Map<String, Object> params) {
		// TODO Auto-generated method stub
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".findUploadFtpFileByMap", params);
	}

	@Override
	public int updateUploadFtpFileLogStatus2Success(Map<String, Object> params) {
		return getSqlSession().update(getIbatisMapperNameSpace() + ".updateUploadFtpFileLogStatus2Success", params);
	}
}
