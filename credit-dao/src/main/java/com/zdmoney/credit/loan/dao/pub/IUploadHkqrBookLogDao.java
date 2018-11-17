package com.zdmoney.credit.loan.dao.pub;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.loan.domain.UploadHkqrBookLog;

import java.util.Date;
import java.util.List;

/**
 * Created by ym10094 on 2017/4/7.
 */
public interface IUploadHkqrBookLogDao extends IBaseDao<UploadHkqrBookLog> {

    public List<UploadHkqrBookLog> queryUploadFailLog();

    /**
     * 查询莫一天是否已经上传成功过
     * @param fundsSource
     * @param tradeDate
     * @return
     */
    public UploadHkqrBookLog queryUploadLog(String fundsSource,Date tradeDate);
}
