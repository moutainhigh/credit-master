package com.zdmoney.credit.loan.dao.pub;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.zdmoney.credit.bsyh.vo.BsyhSpecialRepayVo;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.vo.core.SpecialRepayParamsVO;
import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.loan.domain.LoanSpecialRepayment;

public interface ILoanSpecialRepaymentDao extends IBaseDao<LoanSpecialRepayment>{

	/**
	 * 通过债权ID、日期、类型、状态查询特殊还款
	 * @param map
	 * @return
	 */
	public LoanSpecialRepayment findByLoanIdAndDateAndTypeAndState(Map<String,Object> map);

	/**
	 * 
	 * @param promiseReturnDate1
	 */
	public int updateSpecialRepaymentStateAtEndOfDay(int promiseReturnDate1);
	
	/**
	 * 通过债权ID、状态、类型查询特殊还款
	 * @param params
	 * @return
	 */
	public LoanSpecialRepayment findSpecialRepaymentByLoanIdAndTypeAndState(SpecialRepayParamsVO params);
	
	 public Pager findSpecialRepaymentList(Map<String, Object> map);
	 
	 public int findSpecialRepaymentByLoanId(Long loanId);
	 
	 public int findLoanByUserIdAndLoanState(String userId);
	 
	 public Pager ZBfindSpecialRepaymentList(Map<String, Object> map);
	 public int findLoanSpecialRepaymentByStateAndLoanId(Long loanId) ;

	public void updateFYJM(LoanSpecialRepayment loanSpecialRepayment);

	public Pager findListByMapLog(Map<String, Object> map);

	public List findSpecialRepaymentByUserAndDate(String idnum);

	public List<BsyhSpecialRepayVo> findBsyhSpecialRepay(Date date);

	public List<BsyhSpecialRepayVo> findBsyhSpecialRepayAll();

	/**
	 * 查询生效的减免申请
	 * @param applyId
	 * @return
	 */
	public List<LoanSpecialRepayment> findReleifApplyEffect(Long applyId);
}
