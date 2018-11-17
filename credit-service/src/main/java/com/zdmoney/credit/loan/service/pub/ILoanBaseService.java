package com.zdmoney.credit.loan.service.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.vo.core.LoanBaseAppVo;
import com.zdmoney.credit.loan.domain.LoanBase;
import com.zdmoney.credit.loan.vo.LoanImageFile;
import com.zdmoney.credit.loan.vo.TotalReturnLoanInfo;

public interface ILoanBaseService {
	/**
	 * 费用减免申请查询
	 * 张倩倩
	 */
	public Pager findLoanBaseList(Map<String, Object> map);
	
	public Pager getLoanBaseBybatchNum(Map<String, Object> paramMap) ;

	public Pager searchapprovalWithPg(Map<String, Object> paramMap);

	public Pager findLoanSpecialRepaymentTQKK(Map<String, Object> paramMap);
	
	
	
	/**
	 * 信审或者合同、批量下载附件
	 * @param loanId
	 * @return
	 */
	public List<LoanImageFile> findLoanImageList(Map<String, Object> map);
	public List<LoanImageFile> findLoanImageListXsOrHt(Map<String, Object> map);
	
	public List<LoanImageFile> findLoanBtchImageList(Map<String, Object> map);
	/**
	 * 黑名单下载附件
	 * @param map
	 * @return
	 */
	public List<LoanImageFile> findLoanImageListBlackName(Map<String, Object> map);
	
	public Pager loanReturnWithPg(Map<String, Object> paramMap);
	
	public void updateLoanBaseByLoanReturn(LoanBase loanBase);
	
	public TotalReturnLoanInfo searchTotalReturnLoanInfo(Map<String, Object> map);

	/**
	 * 还款日逾期修改债权状态
	 */
	public void updateLoanStateAtEndOfDay();
	
	
	public LoanBaseAppVo findAppNo(Map<String, Object> map);
	public List<LoanBaseAppVo> findAppNoList(Map<String, Object> map);
	
	public LoanBase findLoanBase(Long id);
	/**
	 * 通过合同编号查询债权id
	 * @param contractNum
	 * @return
	 */
	public Long findLoanIdByContractNum(String contractNum);

	/**
	 * 分页查询 债权回购信息
	 * @param paramMap
	 * @return
	 */
	public Pager findBuyBackLoanWithPg(Map<String, Object> paramMap);

	/**
	 * 债权回购 更新LoanBase
	 * @param loanBase
	 * @return
	 */
	public void updateLoanBaseByBuyBackLoan(LoanBase loanBase);
}
