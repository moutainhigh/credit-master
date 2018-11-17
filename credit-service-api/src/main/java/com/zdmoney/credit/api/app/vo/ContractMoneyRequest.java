package com.zdmoney.credit.api.app.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author 10098  2017年4月19日 上午11:05:41
 */
public class ContractMoneyRequest implements Serializable{

	private static final long serialVersionUID = -5985095088664319621L;

	//申请金额
	private BigDecimal requestMoney;
	//产品名称
	private String prodName;
	//产品期限
	private Long term;
	//合同来源
	private String contractSource;

	public BigDecimal getRequestMoney() {
		return requestMoney;
	}

	public void setRequestMoney(BigDecimal requestMoney) {
		this.requestMoney = requestMoney;
	}

	public String getProdName() {
		return prodName;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	public Long getTerm() {
		return term;
	}

	public void setTerm(Long term) {
		this.term = term;
	}

	public String getContractSource() {
		return contractSource;
	}

	public void setContractSource(String contractSource) {
		this.contractSource = contractSource;
	}
	
	
}
