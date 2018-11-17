package com.zdmoney.credit.test.dao;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

public class ButBackEntity {

	/**合同号**/
	@NotBlank(message="合同号不能为空")
	private String pactNo;
	/**客户名称**/
	@NotBlank(message="客户名称不能为空")
	private String custName;
	/**待回购本金**/
	@NotNull(message="待回购本金不能为空")
	private BigDecimal repAmt;
	/**待回购利息**/
	@NotNull(message="待回购利息不能为空")
	private BigDecimal repIntst;
	/**回购生成日期  **/
	@Length(max = 8, message = "回购生成日期 格式为YYYYMMDD")
	private String txDate;
	
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
	public String getTxDate() {
		return txDate;
	}
	public void setTxDate(String txDate) {
		this.txDate = txDate;
	}
	
}
