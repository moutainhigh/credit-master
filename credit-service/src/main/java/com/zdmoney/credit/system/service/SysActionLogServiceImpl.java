package com.zdmoney.credit.system.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.system.dao.pub.ISysActionLogDao;
import com.zdmoney.credit.system.domain.SysActionLog;
import com.zdmoney.credit.system.service.pub.ISequencesService;
import com.zdmoney.credit.system.service.pub.ISysActionLogService;

@Service
@Transactional
public class SysActionLogServiceImpl implements ISysActionLogService{
	
	protected static Log logger = LogFactory.getLog(SysActionLogServiceImpl.class);
	
	@Autowired
	private ISysActionLogDao sysActionLogDao;
	@Autowired
	private ISequencesService sequencesServiceImpl;

	@Override
    @Transactional(propagation=Propagation.REQUIRES_NEW)
	public void createLog(String userCode, String name, String type,String logName, String content, String url, String ip) {
		SysActionLog loanLog = new SysActionLog();
		loanLog.setCreateTime(new Date());
        loanLog.setIp(ip);
        loanLog.setUserCode(userCode);
        loanLog.setUserName(name);
        loanLog.setActionUrl(url);
        loanLog.setLogType(type);
        loanLog.setLogName(logName);
        loanLog.setContent(content);
        loanLog.setSystemType("核心系统");
		try {
            loanLog.setId(sequencesServiceImpl.getSequences(SequencesEnum.SYS_ACTION_LOG));
            sysActionLogDao.insert(loanLog);
        } catch (Exception e) {
        	logger.error("!!!SYS_ACTION_LOG PARAM:" + JSONObject.toJSONString(loanLog));
            logger.error(e.getMessage(),e);
        }
		
	}


	@Override
	public void createLog(String userCode, String Name, String type,
			String logName, String content, String url, String ip,
			String infobefor, String infoafter) {
		// TODO Auto-generated method stub
		SysActionLog loanLog = new SysActionLog();
		loanLog.setCreateTime(new Date());
        loanLog.setIp(ip);
        loanLog.setUserCode(userCode);
        loanLog.setUserName(Name);
        loanLog.setActionUrl(url);
        loanLog.setLogType(type);
        loanLog.setLogName(logName);
        loanLog.setContent(content);
        loanLog.setMemo(infobefor+"更新为"+infoafter);
        loanLog.setSystemType("核心系统");
		try {
            loanLog.setId(sequencesServiceImpl.getSequences(SequencesEnum.SYS_ACTION_LOG));
            sysActionLogDao.insert(loanLog);
        } catch (Exception e) {
        	logger.error("!!!SYS_ACTION_LOG PARAM:" + JSONObject.toJSONString(loanLog));
            logger.error(e.getMessage(),e);
        }
	}


	@Override
	public List<SysActionLog> findListByVo(SysActionLog sysActionLog) {
		return sysActionLogDao.findListByVo(sysActionLog);
	}
	
	
}
