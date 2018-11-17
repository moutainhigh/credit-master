package com.zdmoney.credit.repay.service.pub;

import java.util.Map;

import com.zdmoney.credit.repay.domain.LoanPreApplyRecord;


/**
 * 线上还款明细Service接口类
 * @author 00236640
 * @version $Id: IVRepaymentDetailService.java, v 0.1 2015年9月11日 下午3:42:00 00236640 Exp $
 */
public interface ILoanPreApplyRecordService {

	void updateByVo(LoanPreApplyRecord loanPreApplyRecord);

	LoanPreApplyRecord getById(Long id);

	LoanPreApplyRecord findByMap(Map<String, Object> params);

}
