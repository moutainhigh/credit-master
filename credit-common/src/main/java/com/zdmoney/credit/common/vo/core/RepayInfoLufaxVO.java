package com.zdmoney.credit.common.vo.core;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 得到本金221和利息451
 */
public class RepayInfoLufaxVO {

	private Long splitId;
	private Long loanId; 			// 借款ID
	private BigDecimal capital = BigDecimal.ZERO; 			// 本金221
	private BigDecimal aint = BigDecimal.ZERO; 	//利息
	private String debitNo;
	private Date tradeDate;
	private String serialno;	//安硕序号
	private String anshuo_loan_accept_id;//安硕贷款受理号
	private String lufax_loan_req_id;//借款申请ID
	private String anshuobatchid;	//安硕批次号
	private String frozen_amount;//成功冻结金额
	private String lufax_repay_req_no;	//lufax还款请求号
	private String tradeNo;
	private String payParty;
    private String repayType;
    private String debitType;
    private Long term;
	public Long getSplitId() {
		return splitId;
	}

	public void setSplitId(Long splitId) {
		this.splitId = splitId;
	}

	public Long getLoanId() {
		return loanId;
	}

	public void setLoanId(Long loanId) {
		this.loanId = loanId;
	}

	public BigDecimal getCapital() {
		return capital;
	}

	public void setCapital(BigDecimal capital) {
		this.capital = capital;
	}

	public BigDecimal getAint() {
		return aint;
	}

	public void setAint(BigDecimal aint) {
		this.aint = aint;
	}

	public String getDebitNo() {
		return debitNo;
	}

	public void setDebitNo(String debitNo) {
		this.debitNo = debitNo;
	}

	public Date getTradeDate() {
		return tradeDate;
	}

	public void setTradeDate(Date tradeDate) {
		this.tradeDate = tradeDate;
	}

	public String getAnshuobatchid() {
		return anshuobatchid;
	}

	public void setAnshuobatchid(String anshuobatchid) {
		this.anshuobatchid = anshuobatchid;
	}

	public String getSerialno() {
		return serialno;
	}

	public void setSerialno(String serialno) {
		this.serialno = serialno;
	}

	public String getLufax_repay_req_no() {
		return lufax_repay_req_no;
	}

	public void setLufax_repay_req_no(String lufax_repay_req_no) {
		this.lufax_repay_req_no = lufax_repay_req_no;
	}

	public String getAnshuo_loan_accept_id() {
		return anshuo_loan_accept_id;
	}

	public void setAnshuo_loan_accept_id(String anshuo_loan_accept_id) {
		this.anshuo_loan_accept_id = anshuo_loan_accept_id;
	}

	public String getLufax_loan_req_id() {
		return lufax_loan_req_id;
	}

	public void setLufax_loan_req_id(String lufax_loan_req_id) {
		this.lufax_loan_req_id = lufax_loan_req_id;
	}

	public String getFrozen_amount() {
		return frozen_amount;
	}

	public void setFrozen_amount(String frozen_amount) {
		this.frozen_amount = frozen_amount;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public String getPayParty() {
		return payParty;
	}

	public void setPayParty(String payParty) {
		this.payParty = payParty;
	}

	public String getRepayType() {
		return repayType;
	}

	public void setRepayType(String repayType) {
		this.repayType = repayType;
	}

	public String getDebitType() {
		return debitType;
	}

	public void setDebitType(String debitType) {
		this.debitType = debitType;
	}

	public Long getTerm() {
		return term;
	}

	public void setTerm(Long term) {
		this.term = term;
	}

}
