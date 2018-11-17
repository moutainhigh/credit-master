package com.zdmoney.credit.loan.dao.pub;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.vo.core.LoanBaseAppVo;
import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.loan.domain.LoanBase;
import com.zdmoney.credit.loan.vo.LoanImageFile;
import com.zdmoney.credit.loan.vo.TotalReturnLoanInfo;

/**
 * loan主表dao
 * @author 00232949
 *
 */
public interface ILoanBaseDao  extends IBaseDao<LoanBase>{

	/**
	 * 更新到了还款日未还款的贷款户的状态
	 */
	public int updateLoanStateAtEndOfDay();

	/**
	 * 根据loanid查找
	 * @param id
	 * @return
	 */
	public LoanBase findByLoanId(Long id);

	/**
	 * 将逾期已还的贷款状态改为正常
	 * @param tradeDate 
	 * @param loanId 
	 */
	public int updatePaidoffOverdueToZC(Long loanId, Date tradeDate);

	/**
	 * 根据id 更新状态
	 * @param id
	 * @param state
	 */
	public int updateLoanState(Long id, String state);
	
	/**
	 * 费用减免申请查询
	 * 张倩倩
	 */
	public Pager findLoanBaseList(Map<String, Object> map);
	
	/**
	 * 根据isSend和state设置isSend值
	 * @param params isSendValue:set的值-t/f; states:in条件; isSendCondition:where条件中is_send的值-t/f
	 * @return
	 */
	public int updateIssendByStateAndIssend(Map<String, Object> params);
	public Pager getLoanBaseBybatchNum(Map<String, Object> paramMap);

	public Pager searchapprovalWithPg(Map<String, Object> paramMap);

	public Pager findLoanSpecialRepaymentTQKK(Map<String, Object> paramMap);
	
	public List<LoanImageFile> findLoanImageList(Map<String, Object> map);
	/**
	 * 单个下载信审或者合同
	 * @param map
	 * @return
	 */
	public List<LoanImageFile> findLoanImageListXsOrHt(Map<String, Object> map);	
	public List<LoanImageFile> findLoanBtchImageList(Map<String, Object> map) ;
	public List<LoanImageFile> findLoanImageListBlackName(Map<String, Object> map) ;
	
	public Pager loanReturnWithPg(Map<String, Object> paramMap);
	
	public int updateLoanBaseByLoanReturn(LoanBase loanBase);
	public TotalReturnLoanInfo searchTotalReturnLoanInfo(Map<String, Object> map);
	
	/**
	 * 根据id更新状态、流程状态
	 * @param id
	 * @param state
	 */
	public int updateAPSLoanState(LoanBase loanBase);
	public LoanBaseAppVo findAppNo(Map<String, Object> map);
	public List<LoanBaseAppVo> findAppNoList(Map<String, Object> map);
	/**
     * 查询所有未放款和合同来源是渤海信托的所有债权
     */
    public List<LoanBase> getAllLoanBaseInfo(Map<String, Object> map);
    
    /**
     * 分页查询债权批次信息
     * @param params
     * @return
     */
    public Pager queryBatchNumInfo(Map<String, Object> params);
    
    /**
   	* 通过客服id获取所属网点(管理营业部)
   	* */
   	public Long getSalesDepartmentId(Long newCrmId);

	public Long findLoanIdByContractNum(String contractNum);

	/**
	 * 查询某个合同来源的批次编号
	 * @param fundsSources
	 * @return
	 */
	public List<String> findBatchNumByFundsSources(String fundsSources);

	/**
	 * 分页查询债权回购信息
	 * @param paramMap
	 * @return
	 */
	public Pager findBuyBackLoanWithPg(Map<String, Object> paramMap);

	/**
	 * 债权回购 更新LoanBase
	 * @param loanBase
	 * @return
	 */
	public int updateLoanBaseByBuyBackLoan(LoanBase loanBase);
   	
	public List<Map<String, Object>> getRechargeSearchList();
	
}
