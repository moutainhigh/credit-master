package com.zdmoney.credit.abs.service.pub;

import java.util.List;

import com.zdmoney.credit.loan.domain.UploadFtpFileLog;


/**
 * 数信 相关service层
 * @author 10098  2016年12月7日 下午3:58:41
 */
public interface IAbsCommonService {

	/**
	 * 向ftp上传日志表中插入数据
	 * @param params
	 */
	public boolean pushUploadFtpFileLog();

	/**
	 *查找待上传至数信的数据
	 * @return
	 */
	public List<UploadFtpFileLog> findWmxt2UploadFtpLog2Abs();

	/**
	 * 更改上传状态
	 * @param contractNos
	 * @return
	 */
	public void uploadUploadFileLogStatus(List<String> contractNos);
}
