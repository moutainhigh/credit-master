package com.zdmoney.credit.fee.dao;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.fee.dao.pub.ILoanFeeOfferQueueDao;
import com.zdmoney.credit.fee.domain.LoanFeeOfferQueue;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;

/**
 * 收费报盘队列表 Dao实现层，定义一些与系统业务无关的方法
 * 
 * 
 */
@Repository
public class LoanFeeOfferQueueDaoImpl extends BaseDaoImpl<LoanFeeOfferQueue> implements ILoanFeeOfferQueueDao {

}
