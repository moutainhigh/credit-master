package com.zdmoney.credit.offer.service.pub;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.zdmoney.credit.common.vo.core.FinanceVo;
import com.zdmoney.credit.debit.domain.DebitTransaction;
import com.zdmoney.credit.framework.vo.wm3.entity.PaidInMoneyListEntity;
import com.zdmoney.credit.loan.domain.LoanLedger;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.offer.domain.OfferRepayInfo;
import com.zdmoney.credit.offer.domain.OfferTransaction;
import com.zdmoney.credit.offer.vo.OfferRepayInfoVo;
import com.zdmoney.credit.offer.vo.RepaymentInputVo;
import com.zdmoney.credit.system.domain.ComEmployee;

/**
 * 交易凭证的service
 * @author 00232949
 *
 */
public interface IOfferRepayInfoService {

	/**
	 * 创建报回盘交易凭证
	 * @param offerTransaction
	 * @return
	 */
	public OfferRepayInfo builderRepayInfo(OfferTransaction offerTransaction);


	/**
	 * 根据有挂账的loan分账，创建日终处理的还款日挂账部分账务处理
	 * @param loanLedger
	 * @param tradeDate
	 */
	public void createAccountingByRepaymentDayDeal(LoanLedger loanLedger,
			Calendar tradeDate);
	
	/***
	 * 跟据Map查询结果集
	 * @param map 参数集合
	 * @return
	 */
	public List<OfferRepayInfo> findListByMap(Map paramMap);

	/**
	 * 财务放款的交易凭证创建
	 * @param loan
	 * @param employee
	 */
	public void builderRepayInfoFinancing(VLoanInfo loan, ComEmployee employee);


	/**
	 * 放款接口，查询还款明细
	 * @param params
	 * @return
	 */
	public List<OfferRepayInfo> findListByFinanceVo(FinanceVo params);
	
	/***
	 * 查询还款汇总信息
	 * @return
	 */
	public OfferRepayInfoVo getRepayInfo(Long loanId,Date tradeDate);
	
	/**
	 * afterLoanService中，drawJimuRisk方法获取repayInfo第一条数据的查询
	 * @param params loanId，tradeCodes列表，tradeDate排序
	 * @return
	 */
	public List<OfferRepayInfo> getDrawJimuRiskRepayInfo(Map<String, Object> params);

	/**
	 * 保存
	 * @param repay
	 */
	public void save(OfferRepayInfo repay);


	/**
	 * 取最后一次提风险金的记录
	 * @param loanid 
	 * @return
	 */
	public OfferRepayInfo getLoanlastDrawRisk(Long loanid);
	
	/**
	 * 根据loanid和tradecode，获取最后一条OfferRepayInfo记录
	 * @param loanId
	 * @param tradeCode
	 * @return
	 */
	public OfferRepayInfo getLoanLastRepayInfoByTradeCode(Long loanId, String tradeCode);
	
	/***
	 * 还款录入处理
	 * @param repaymentInputVo 录入Vo信息
	 * @return
	 */
	public OfferRepayInfo repaymentInputCore(RepaymentInputVo repaymentInputVo);
	
	/**
	 * 结清证明打印时 根据合同id查询，根据id desc 取最后一条
	 */
	public OfferRepayInfo getLoanLastRepayInfoById(Long loanId);
	
	/**
	 * 掉用外贸3 线下实收接口 
	 * @param paidInMoneyList
	 */
	public void callOfflineReceiveInterface(List<PaidInMoneyListEntity> paidInMoneyList);

	/**
	 * 处理还款录入
	 * @param repaymentInputVo
	 */
	public OfferRepayInfo dealRepaymentInput(RepaymentInputVo repaymentInputVo);

	public RepaymentInputVo builderRepaymentInputVo(OfferTransaction offerTransaction);

	/**
	 * 创建第三方还款减免入账参数
	 * @param transaction
	 * @return
	 */
	public RepaymentInputVo createRepaymentInputVo(DebitTransaction transaction);
}
