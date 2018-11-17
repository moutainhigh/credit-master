package com.zdmoney.credit.bsyh.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.bsyh.dao.pub.IRepayResultNotifyLogDao;
import com.zdmoney.credit.bsyh.service.pub.IRepayResultNotifyLogService;
import com.zdmoney.credit.loan.domain.RepayResultNotifyLog;

/**
 * 包商银行 自扣还款结果通知接口
 * @author YM10112  2017年1月17日
 *
 */
@Service
public class RepayResultNotifyLogServiceImpl  implements IRepayResultNotifyLogService{

	@Autowired
	IRepayResultNotifyLogDao autoDebitRepayResultDao;
	
	@Override
	public RepayResultNotifyLog save(RepayResultNotifyLog autoDebitRepayResult) {
		return autoDebitRepayResultDao.insert(autoDebitRepayResult);
	}
	
	@Override
	public int update(RepayResultNotifyLog autoDebitRepayResult) {
		return autoDebitRepayResultDao.update(autoDebitRepayResult);
	}
	
	@Override
	public List<RepayResultNotifyLog> findListByMap(Map<String, Object> map) {
		return autoDebitRepayResultDao.findListByMap(map);
	}

	@Override
	public RepayResultNotifyLog get(Long id) {
		return autoDebitRepayResultDao.get(id);
	}



	

}
