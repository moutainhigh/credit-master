package com.zdmoney.credit.fee.service.pub;

import java.util.List;

import com.zdmoney.credit.fee.domain.LoanFeeOfferQueue;


/**
 * 收费报盘队列表 Service层接口，定义一些与系统业务相关的方法
 * 
 * 
 *
 */
public interface ILoanFeeOfferQueueService {

	List<LoanFeeOfferQueue> findListByVo(LoanFeeOfferQueue loanFeeOfferQueue);

	int updateByVo(LoanFeeOfferQueue vo);
	
}
