package com.zdmoney.credit.signature.dao.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.signature.domain.ElectronicSignatureLog;

/**
 * Created by ym10094 on 2016/12/6.
 * 电子签章 记录
 */
public interface IElectronicSignatureLogDao extends IBaseDao<ElectronicSignatureLog> {

    public ElectronicSignatureLog findElectronicSignatureLog(Map<String,Object> paramMap);
    
	/**
	 * 跟据Map查询 需要插入数信ftp上传日志表 的数据
	 */
	public List<ElectronicSignatureLog> findAbsData2Upload(Map<String,Object> paramMap);
}
