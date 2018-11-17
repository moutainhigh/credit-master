package com.zdmoney.credit.repay.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.loan.dao.pub.IVLoanInfoDao;
import com.zdmoney.credit.repay.service.pub.IRepayMarkService;
@Service
public class RepayMarkServiceImpl implements IRepayMarkService {

	@Autowired
    private IVLoanInfoDao vLoanInfoDao;
	
	@Override
	public Pager searchVLoanInfoList(Map<String, Object> params) {
		return vLoanInfoDao.searchVLoanInfoListWithPg(params);
	}

}
