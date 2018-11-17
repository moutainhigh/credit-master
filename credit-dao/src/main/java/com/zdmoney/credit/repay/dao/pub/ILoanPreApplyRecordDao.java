package com.zdmoney.credit.repay.dao.pub;

import java.util.Map;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.repay.domain.LoanPreApplyRecord;

/**
 * 线上还款明细DAO接口类
 * @author 00236640
 * @version $Id: IVRepaymentDetailDao.java, v 0.1 2015年9月11日 下午3:41:04 00236640 Exp $
 */
public interface ILoanPreApplyRecordDao extends IBaseDao<LoanPreApplyRecord>{

	LoanPreApplyRecord findByMap(Map<String, Object> maps);
	
}
