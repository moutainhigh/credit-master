package com.zdmoney.credit.repay.service.pub;

import java.util.Map;

import com.zdmoney.credit.common.util.Pager;

public interface IRepayMarkService {
	public Pager searchVLoanInfoList(Map<String, Object> params);
}
