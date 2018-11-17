package com.zdmoney.credit.fee.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.fee.dao.pub.ILoanFeeOfferQueueDao;
import com.zdmoney.credit.fee.domain.LoanFeeOfferQueue;
import com.zdmoney.credit.fee.service.pub.ILoanFeeOfferQueueService;

/**
 * 收费报盘队列表 Service实现层，定义一些与系统业务相关的方法
 * 
 * 
 *
 */
@Service
public class LoanFeeOfferQueueServiceImpl implements ILoanFeeOfferQueueService {
	protected static Log logger = LogFactory.getLog(LoanFeeOfferQueueServiceImpl.class);
	@Autowired
	private ILoanFeeOfferQueueDao loanFeeOfferQueueDao;
	@Override
	public List<LoanFeeOfferQueue> findListByVo(
			LoanFeeOfferQueue loanFeeOfferQueue) {
		List<LoanFeeOfferQueue> list=loanFeeOfferQueueDao.findListByVo(loanFeeOfferQueue);
		return list;
	}
	@Override
	public int updateByVo(LoanFeeOfferQueue vo) {
		return  loanFeeOfferQueueDao.update(vo);		
	}
}
