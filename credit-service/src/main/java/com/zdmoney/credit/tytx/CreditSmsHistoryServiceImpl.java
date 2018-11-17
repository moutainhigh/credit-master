package com.zdmoney.credit.tytx;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.tytx.domain.CreditSmsHistory;
import com.zdmoney.credit.tytx.pub.ICreditSmsHistoryDao;
import com.zdmoney.credit.tytx.pub.ICreditSmsHistoryService;

@Service
public class CreditSmsHistoryServiceImpl implements ICreditSmsHistoryService{
	
	@Autowired
	private ICreditSmsHistoryDao icreditSmsHistoryDao;
	
	public CreditSmsHistory save(CreditSmsHistory creditSmsHistory){
		return icreditSmsHistoryDao.insert(creditSmsHistory);
	}
	
	@Override
	public List<CreditSmsHistory> findByMobile(Map<String, Object> paramMap) {
		return icreditSmsHistoryDao.findListByMap(paramMap);
	}
	
	
}
