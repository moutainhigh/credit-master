package com.zdmoney.credit.loan.dao.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.loan.domain.UploadFtpFileLog;

/**
 * Created by ym10094 on 2016/8/23.
 */
public interface IUploadFtpFileLogDao extends IBaseDao<UploadFtpFileLog> {
/*    *//**
     * 查询记录上传失败的记录
     * @return
     *//*
    public List<UploadFtpFileLog> findUploadFtpFileLogFails();*/

    /**
     *
     * @param contract_num
     * @return
     */
    public UploadFtpFileLog findUploadFtpFileLog(String contract_num);

    /**
     * 根据条件 查找ftp文件上传记录
     * @param params
     * @return
     */
	public List<UploadFtpFileLog> findUploadFtpFileByMap(
			Map<String, Object> params);

	public int updateUploadFtpFileLogStatus2Success(Map<String, Object> params);
}
