package com.zdmoney.credit.repay.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.repay.dao.pub.IRequestFileOperateRecordDao;
import com.zdmoney.credit.repay.vo.RequestFileOperateRecord;

/**
 * 申请书管理菜单文件上传下载操作
 */
@Repository
public class RequestFileOperateRecordDaoImpl extends BaseDaoImpl<RequestFileOperateRecord> implements IRequestFileOperateRecordDao {
    @Override
    public String findRequestFileBatchNum(Map<String, Object> params) {
        return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".findRequestFileBatchNum",params);
    }

    @Override
    public List<RequestFileOperateRecord> findRequestFileOperateRecord(RequestFileOperateRecord requestFileOperateRecord) {
        return getSqlSession().selectList(getIbatisMapperNameSpace() +".findRequestFileOperateRecord",requestFileOperateRecord);
    }

	@Override
	public String findFileSeqByParam(Map<String, Object> paramMap) {
		return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".findFileSeqByParam",paramMap);
	}

    @Override
    public List<RequestFileOperateRecord> queryCurrentDayRequestFileOperateRecordByAsc(Map<String,Object> paramMap) {
        return getSqlSession().selectList(getIbatisMapperNameSpace() + ".queryCurrentDayRequestFileOperateRecordByAsc",paramMap);
    }
}
