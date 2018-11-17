package com.zdmoney.credit.repay.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.repay.dao.pub.ILoanPreApplyRecordDao;
import com.zdmoney.credit.repay.domain.LoanPreApplyRecord;
import com.zdmoney.credit.repay.service.pub.ILoanPreApplyRecordService;

@Service
public class LoanPreApplyRecordServiceImpl implements ILoanPreApplyRecordService{
	
	@Autowired
	private ILoanPreApplyRecordDao loanPreApplyRecordDao;
	
	@Override
	public void updateByVo(LoanPreApplyRecord loanPreApplyRecord) {
		loanPreApplyRecordDao.update(loanPreApplyRecord);
	}

	@Override
	public LoanPreApplyRecord getById(Long id) {
		return loanPreApplyRecordDao.get(id);
	}

	@Override
	public LoanPreApplyRecord findByMap(Map<String, Object> params) {
		Map<String, Object> maps =new HashMap<String, Object>();
		String organization = (String) params.get("organization");
		maps.put("applyDate", Dates.getCurrDate());
		if (organization.equals("BH2")) {
			maps.put("fundsSource", "渤海2");
		}else if (organization.equals("HRBH")) {
			maps.put("fundsSource", "华瑞渤海");
		}
		return loanPreApplyRecordDao.findByMap(maps);
	}

}
