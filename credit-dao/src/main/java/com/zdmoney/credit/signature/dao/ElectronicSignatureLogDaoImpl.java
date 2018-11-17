package com.zdmoney.credit.signature.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.signature.dao.pub.IElectronicSignatureLogDao;
import com.zdmoney.credit.signature.domain.ElectronicSignatureLog;

/**
 * Created by ym10094 on 2016/12/6.
 */
@Repository
public class ElectronicSignatureLogDaoImpl extends BaseDaoImpl<ElectronicSignatureLog> implements IElectronicSignatureLogDao {
    @Override
    public ElectronicSignatureLog findElectronicSignatureLog(Map<String,Object> paramMap) {
        List<ElectronicSignatureLog> logList = this.findListByMap(paramMap);
        if (CollectionUtils.isEmpty(logList)) {
            return null;
        }
        return logList.get(0);
    }

	@Override
	public List<ElectronicSignatureLog> findAbsData2Upload(Map<String, Object> paramMap) {
		List<ElectronicSignatureLog> rstList = getSqlSession().selectList(getIbatisMapperNameSpace() + ".findAbsData2Upload", paramMap);
		return rstList;
	}
}
