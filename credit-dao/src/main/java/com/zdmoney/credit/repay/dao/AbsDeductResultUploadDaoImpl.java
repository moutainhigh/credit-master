package com.zdmoney.credit.repay.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.repay.dao.pub.IAbsDeductResultUploadDao;
import com.zdmoney.credit.repay.domain.AbsDeductResultUpload;

@Repository
public class AbsDeductResultUploadDaoImpl extends BaseDaoImpl<AbsDeductResultUpload> implements IAbsDeductResultUploadDao {

	@Override
	public List<AbsDeductResultUpload> findDeductResutListByMap(Map<String, Object> params) {
		return getSqlSession().selectList(getIbatisMapperNameSpace()+".findDeductResutList2Abs", params);
	}

}
