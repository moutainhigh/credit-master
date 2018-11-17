package com.zdmoney.credit.offer.service.pub;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.zdmoney.credit.loan.domain.LoanRepaymentDetail;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.offer.domain.OfferFlow;
import com.zdmoney.credit.offer.domain.OfferRepayInfo;
import com.zdmoney.credit.repay.vo.RepayStateDetail;

/**
 * flow凭证明细 操作service
 * @author 00232949
 *
 */
public interface IOfferFlowService {

	/**
	 * 保存flow
	 * @param flowInstance
	 */
	void save(OfferFlow flowInstance);

	/**
	 * 新开户的flow账务处理
	 * @param repay
	 * @param loanInfo
	 */
	void openAccountDeal(OfferRepayInfo repay, VLoanInfo loanInfo);

	/**
	 * 账户存款的flow账务处理
	 * @param repay
	 */
	void accountDepositDeal(OfferRepayInfo repay);

	/**
	 * 保证金账户存款的flow账务处理
	 * @param repay
	 */
	void bondAccountDepositDeal(OfferRepayInfo repay);

	/**
	 * 账户取款的flow账务处理
	 * @param repay
	 */
	void accountWithdrawDeal(OfferRepayInfo repay);

	/**
	 *  保证金账户取款的flow账务处理
	 * @param repay
	 */
	void bondAccountWithdrawDeal(OfferRepayInfo repay);

	/**
	 * 一次性还款的flow账务处理
	 * @param repay
	 * @param loanInfo
	 * @param countAMT 
	 * @param overDateList 
	 * @param amount 
	 * @return 如果返回值是1，组处理已结束，不再进行下面的处理 ，如还要继续返回0
	 */
	int fullRepaymentDeal(OfferRepayInfo repay, VLoanInfo loanInfo, BigDecimal countAMT, List<LoanRepaymentDetail> overDateList);

	/**
     * 一次性还款实时的flow账务处理
     * @param repay
     * @param loanInfo
     * @param countAMT 
     * @param overDateList 
     * @param amount 
     * @return 如果返回值是1，组处理已结束，不再进行下面的处理 ，如还要继续返回0
     */
    int realtimeFullRepaymentDeal(OfferRepayInfo repay, VLoanInfo loanInfo, BigDecimal countAMT, List<LoanRepaymentDetail> overDateList);
	
	/**
	 * 金额挂账，账户充值的flow账务处理(指定金额)
	 * @param repay
	 * @param countAMT 挂账金额
	 */
	void accountRechargeDeal(OfferRepayInfo repay, BigDecimal countAMT);

	/**
	 * 挂账部分销账处理的flow账务处理
	 * @param repay
	 * @param amount 
	 */
	void accountBalancePayDeal(OfferRepayInfo repay, BigDecimal amount);

	/**
	 * 罚息减免的flow账务处理
	 * @param repay
	 * @param relief 
	 * @return
	 */
	OfferFlow reductionPenaltyDeal(OfferRepayInfo repay, BigDecimal relief);

	/**
	 * 罚息的flow账务处理
	 * @param repay
	 * @param fineAmount
	 * @param countAMT 
	 * @param relief 
	 * @param overDateList 
	 * @param reliefflow 罚息减免的flow
	 * @return 如果返回值是1，组处理已结束，不再进行下面的处理 ，如还要继续返回0
	 */
	Map<String ,Object> penaltyDeal(OfferRepayInfo repay, BigDecimal fineAmount, BigDecimal countAMT, BigDecimal relief, List<LoanRepaymentDetail> overDateList, OfferFlow reliefflow);

	/**
	 * 逾期足额还款的flow账务处理，只处理一个LoanRepaymentDetail
	 * @param repay 
	 * @param para
	 * @param loanInfo
	 * @param countAMT 
	 */
	BigDecimal overdueRepaymentDeal(OfferRepayInfo repay, LoanRepaymentDetail para, VLoanInfo loanInfo, BigDecimal countAMT);

	/**
	 * 逾期不足额还款的flow账务处理，只处理一个LoanRepaymentDetail
	 * @param repay
	 * @param para
	 * @param loanInfo
	 * @param countAMT
	 */
	void  overdueInsufficientRepaymentDeal(OfferRepayInfo repay,
			LoanRepaymentDetail para, VLoanInfo loanInfo, BigDecimal countAMT);

	/**
	 * 正常还款的flow账务处理
	 * @param repay
	 * @param loanInfo 
	 * @param lastRep
	 * @param countAMT
	 */
	BigDecimal normalRepaymentDeal(OfferRepayInfo repay, VLoanInfo loanInfo, LoanRepaymentDetail lastRep,
			BigDecimal countAMT);

	/**
	 * 记账处理<br>
	 * @param flowInstance
	 * @return
	 */
	public boolean Accounting(OfferFlow flowInstance);
	
	/**
	 * 流水帐生成<br>
	 * 原来默认：String dOrc='D',String appo_dORc='C'
	 * @param repay
	 * @param amount
	 * @param acctTitle
	 * @param memo2
	 * @param dOrc
	 * @param appo_dORc
	 * @return
	 */
	public OfferFlow FlowBuild(OfferRepayInfo repay, BigDecimal amount,
			String acctTitle, String memo2, String dOrc,
			String appo_dORc) ;
	
	/**
	 * 助学贷机构总收入
	 * @param param
	 * @return
	 */
	public BigDecimal getZhuxueOrganizationTotalIncome(Map<String, Object> param);
	
	/**
	 * 助学贷机构总支出
	 * @param param
	 * @return
	 */
	public BigDecimal getZhuxueOrganizationTotalPay(Map<String, Object> param);

	/**
	 * 学生还保证金记账
	 * @param repay
	 * @param loanInfo 
	 */
	void studentBondRepayment(OfferRepayInfo repay, VLoanInfo loanInfo);
	
	/**
	 * 跟据交易流水查询凭证明细数据
	 * @param tradeNo 交易流水
	 * @return
	 */
	public List<OfferFlow> findByTradeNo(String tradeNo);
	
	/**
	 * 陆金所结清分账
	 * @param repay
	 * @param loanInfo
	 * @param countAMT
	 * @param overDateList
	 * @param onetimeRepaymentAmount
	 * @return
	 */
	public int realtimeFullRepaymentDealLufax(OfferRepayInfo repay,VLoanInfo loanInfo, BigDecimal countAMT, List<LoanRepaymentDetail> overDateList, BigDecimal onetimeRepaymentAmount);

	/**
	 * 获取陆金所逾期还款流水
	 * @param map
	 * @return
	 */
	public List<OfferFlow> findOverDueOfferFlow4Lufax(Map<String, Object> map);
	
	/**
	 * 查询offer-flow，得到 实际还款日期  实还本金  实还利息  实还罚息 实还金额  
	 * @param paramMap
	 * @return
	 */
	public RepayStateDetail getRepayStateDetailRealValue(Map<String, Object> paramMap);
	
    /**
     * 查询某笔还款分账的当期本金
     * @param tradeNo
     * @param detail
     * @return
     */
    public BigDecimal queryNormalPrincipalAmount(String tradeNo, LoanRepaymentDetail detail);
    
    /**
     * 查询某笔还款分账的当期利息
     * @param tradeNo
     * @param detail
     * @return
     */
    public BigDecimal queryNormalInterestAmount(String tradeNo, LoanRepaymentDetail detail);
}
