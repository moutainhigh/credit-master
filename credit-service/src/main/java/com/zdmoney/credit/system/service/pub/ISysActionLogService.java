package com.zdmoney.credit.system.service.pub;

import java.util.List;

import com.zdmoney.credit.system.domain.SysActionLog;

/**
 * 操作日志
 * @author 00232949
 *
 */
public interface ISysActionLogService {

	/**
	 * 创建log
	 * @param userCode
	 * @param Name
	 * @param type
	 * @param logName
	 * @param content
	 * @param url 
	 * @param ip
	 *  
	 */
	void createLog(String userCode, String Name, String type,String logName, String content, String url, String ip);
	void createLog(String userCode, String Name, String type,String logName, String content, String url, String ip,String infobefor,String infoafter);
	public List<SysActionLog> findListByVo(SysActionLog sysActionLog);

}
