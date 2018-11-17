package com.zdmoney.credit.common.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.dao.pub.IOperateLogDao;
import com.zdmoney.credit.common.domain.OperateLog;
import com.zdmoney.credit.common.service.pub.IOperateLogService;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.system.service.pub.ISequencesService;

/**
 *  外贸3 操作日志
 * @author YM10112  
 *
 */
@Service
public class OperateLogServiceImpl  implements IOperateLogService{

	@Autowired
	IOperateLogDao operateLogDao;
	@Autowired
	ISequencesService sequencesService;
	@Override
	public OperateLog save(OperateLog operateLog) {
		return operateLogDao.insert(operateLog);
	}
	
	@Override
	public int update(OperateLog operateLog) {
		return operateLogDao.update(operateLog);
	}
	
	@Override
	public List<OperateLog> findListByMap(Map<String, Object> map) {
		return operateLogDao.findListByMap(map);
	}


	@Override
	public OperateLog addOperateLog(Long loanId, String operateType, String status) {
		OperateLog operateLog = new OperateLog();
		operateLog.setLoanId(loanId);
		operateLog.setOperateDate(Dates.getCurrDate());
		operateLog.setOperateType(operateType);
		operateLog.setStatus(status);
		operateLog.setId(sequencesService.getSequences(SequencesEnum.OPERATE_LOG));
		return operateLogDao.insert(operateLog);
	}

	
	@Override
	public List<OperateLog> findOperateLogList2DownLoad(Map<String, Object> params) {
		return operateLogDao.findOperateLogList2DownLoad(params);
	}

	@Override
	public List<Long> findLoanIds4FormalRepayPlans2Lufax() {
		return operateLogDao.findLoanIds4FormalRepayPlans2Lufax();
	}
}
