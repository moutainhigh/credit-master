package com.zdmoney.credit.loan.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.vo.core.LoanBaseAppVo;
import com.zdmoney.credit.loan.dao.pub.ILoanBaseDao;
import com.zdmoney.credit.loan.domain.LoanBase;
import com.zdmoney.credit.loan.service.pub.ILoanBaseService;
import com.zdmoney.credit.loan.vo.LoanImageFile;
import com.zdmoney.credit.loan.vo.TotalReturnLoanInfo;

@Service
@Transactional
public class LoanBaseServiceImpl implements ILoanBaseService {
	@Autowired
	@Qualifier("loanBaseDaoImpl")
	ILoanBaseDao loanBaseDaoImpl;
	@Override
	public Pager findLoanBaseList(Map<String, Object> map) {
		return loanBaseDaoImpl.findLoanBaseList(map);
	}
	@Override
	public Pager getLoanBaseBybatchNum(Map<String, Object> paramMap) {
		return loanBaseDaoImpl.getLoanBaseBybatchNum(paramMap);
	}
	
	@Override
	public Pager searchapprovalWithPg(Map<String, Object> paramMap) {
		return loanBaseDaoImpl.searchapprovalWithPg(paramMap);
	}
	
	@Override
	public Pager findLoanSpecialRepaymentTQKK(Map<String, Object> paramMap) {
		return loanBaseDaoImpl.findLoanSpecialRepaymentTQKK(paramMap);
	}
	@Override
	public List<LoanImageFile> findLoanImageList(Map<String, Object> map) {
		return loanBaseDaoImpl.findLoanImageList(map);
	}
	public List<LoanImageFile> findLoanImageListXsOrHt(Map<String, Object> map) {
		return loanBaseDaoImpl.findLoanImageListXsOrHt(map);
	}
	
	public List<LoanImageFile> findLoanBtchImageList(Map<String, Object> map) {
		return loanBaseDaoImpl.findLoanBtchImageList(map);
	}
	@Override
	public Pager loanReturnWithPg(Map<String, Object> paramMap) {
		return loanBaseDaoImpl.loanReturnWithPg(paramMap);
	}
	@Override
	public void updateLoanBaseByLoanReturn(LoanBase loanBase) {
		
		
		loanBaseDaoImpl.updateLoanBaseByLoanReturn(loanBase);
		
	}
	@Override
	public TotalReturnLoanInfo searchTotalReturnLoanInfo(Map<String, Object> map) {
		return loanBaseDaoImpl.searchTotalReturnLoanInfo(map);
	}
	@Override
	public List<LoanImageFile> findLoanImageListBlackName(
			Map<String, Object> map) {
		return loanBaseDaoImpl.findLoanImageListBlackName(map);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void updateLoanStateAtEndOfDay() {
		loanBaseDaoImpl.updateLoanStateAtEndOfDay();
	}
	@Override
	public LoanBaseAppVo findAppNo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return loanBaseDaoImpl.findAppNo(map);
	}
	public List<LoanBaseAppVo> findAppNoList(Map<String, Object> map){
		return loanBaseDaoImpl.findAppNoList(map);
	}

	@Override
	public Long findLoanIdByContractNum(String contractNum) {
		return loanBaseDaoImpl.findLoanIdByContractNum(contractNum);
	}
	
	@Override
	public LoanBase findLoanBase(Long id) {
		// TODO Auto-generated method stub
		return loanBaseDaoImpl.findByLoanId(id);
		
	}
	@Override
	public Pager findBuyBackLoanWithPg(Map<String, Object> paramMap) {
		return loanBaseDaoImpl.findBuyBackLoanWithPg(paramMap);
	}
	@Override
	public void updateLoanBaseByBuyBackLoan(LoanBase loanBase) {
		loanBaseDaoImpl.updateLoanBaseByBuyBackLoan(loanBase);
	}
}
