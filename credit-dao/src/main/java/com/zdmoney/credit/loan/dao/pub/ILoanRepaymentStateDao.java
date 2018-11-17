package com.zdmoney.credit.loan.dao.pub;

import java.util.Date;
import java.util.List;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.loan.domain.LoanRepaymentState;

public interface ILoanRepaymentStateDao extends IBaseDao<LoanRepaymentState>{
	/**
	 * 查询所有需要记录还款状态的债权
	 * @param date 
	 * @return 
	 */
	public List<Long> findHisLoans(Date currDate);
	/**
	 * 计算统计期数
	 * @param loanId
	 * @param currDate
	 * @return 
	 */
	public Long findReportTerm(Long loanId, Date currDate);
	/**
	 * 获取结清日期
	 * @param loanId
	 * @param currDate
	 * @return
	 */
	public Date findFinishDate(Long loanId);
	/**
	 * 查询逾期起始日 
	 * @param loanId
	 * @param currDate
	 * @return
	 */
	public Date findOverdueDate(Long loanId, Date currDate);
	/**
	 * 还款期数
	 * @param loanId
	 * @param currDate
	 * @return
	 */
	public Long findRepayTerm(Long loanId, Date currDate);
	/**
	 * 删除状态记录
	 * @param reportMonth
	 */
	public int deleteByReportMonth(String reportMonth);
	
}
