package com.zdmoney.credit.loan.dao;

import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.loan.dao.pub.IUploadHkqrBookLogDao;
import com.zdmoney.credit.loan.domain.UploadHkqrBookLog;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Created by ym10094 on 2017/4/7.
 */
@Repository
public class UploadHkqrBookLogDaoImpl extends BaseDaoImpl<UploadHkqrBookLog> implements IUploadHkqrBookLogDao {
    @Override
    public List<UploadHkqrBookLog> queryUploadFailLog() {
        Map<String,Object> params = new HashMap<>();
        params.put("status","0");//失败状态
        List<UploadHkqrBookLog> logs = this.findListByMap(params);
        if (CollectionUtils.isEmpty(logs)) {
            return Collections.emptyList();
        }
        return logs;
    }

    @Override
    public UploadHkqrBookLog queryUploadLog(String fundsSource, Date tradeDate) {
        Map<String,Object> params = new HashMap<>();
        params.put("fundssource",fundsSource);
        params.put("accountDate",tradeDate);
        List<UploadHkqrBookLog> logs = this.findListByMap(params);
        if (CollectionUtils.isEmpty(logs)) {
            return null;
        }
        return logs.get(0);
    }
}
