package com.zdmoney.credit.loan.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;
/**
 * 待回购信息
 * @author user
 *
 */
public class LoanBack extends BaseDomain{
	
	
	private static final long serialVersionUID = -3175114729555304645L;

		private Long id;
		
	 	private Long loanId;
		
	 	private String pactNo;
		
		private String custName;
		
		private String batNo;
		
		private BigDecimal repAmt;
		
		private BigDecimal repIntst;
		
		private Date txDate;
		
		private Date repDate;

		private int bbState;
		
		private int pState;
		
		private String memo;
		
		private Date createTime;
		
		private Date updateTime;
		
		private String creator;
		
		private String updator;
		
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public Long getLoanId() {
			return loanId;
		}
		public void setLoanId(Long loanId) {
			this.loanId = loanId;
		}
		public String getPactNo() {
			return pactNo;
		}
		public void setPactNo(String pactNo) {
			this.pactNo = pactNo;
		}
		public String getCustName() {
			return custName;
		}
		public void setCustName(String custName) {
			this.custName = custName;
		}
		public BigDecimal getRepAmt() {
			return repAmt;
		}
		public void setRepAmt(BigDecimal repAmt) {
			this.repAmt = repAmt;
		}
		public BigDecimal getRepIntst() {
			return repIntst;
		}
		public void setRepIntst(BigDecimal repIntst) {
			this.repIntst = repIntst;
		}
		public Date getTxDate() {
			return txDate;
		}
		public void setTxDate(Date txDate) {
			this.txDate = txDate;
		}
	
		public String getMemo() {
			return memo;
		}
		public void setMemo(String memo) {
			this.memo = memo;
		}
		public Date getCreateTime() {
			return createTime;
		}
		public void setCreateTime(Date createTime) {
			this.createTime = createTime;
		}
		public Date getUpdateTime() {
			return updateTime;
		}
		public void setUpdateTime(Date updateTime) {
			this.updateTime = updateTime;
		}
		public String getCreator() {
			return creator;
		}
		public void setCreator(String creator) {
			this.creator = creator;
		}
		public String getUpdator() {
			return updator;
		}
		public void setUpdator(String updator) {
			this.updator = updator;
		}
		public String getBatNo() {
			return batNo;
		}
		public void setBatNo(String batNo) {
			this.batNo = batNo;
		}
		public Date getRepDate() {
			return repDate;
		}
		public void setRepDate(Date repDate) {
			this.repDate = repDate;
		}
		public int getBbState() {
			return bbState;
		}
		public void setBbState(int bbState) {
			this.bbState = bbState;
		}
		public int getpState() {
			return pState;
		}
		public void setpState(int pState) {
			this.pState = pState;
		}
 }

