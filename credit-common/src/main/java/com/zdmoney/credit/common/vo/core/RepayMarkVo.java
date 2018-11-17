package com.zdmoney.credit.common.vo.core;

public class RepayMarkVo {
	
	String contractNum;
	String fundsSources;
	String idNum;
	String name;
	String loanState;
	Long id;
	String memo;
	
	public String getContractNum() {
		return contractNum;
	}
	public void setContractNum(String contractNum) {
		this.contractNum = contractNum;
	}
	public String getFundsSources() {
		return fundsSources;
	}
	public void setFundsSources(String fundsSources) {
		this.fundsSources = fundsSources;
	}
	public String getIdNum() {
		return idNum;
	}
	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLoanState() {
		return loanState;
	}
	public void setLoanState(String loanState) {
		this.loanState = loanState;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	@Override
	public String toString() {
		return "RepayMarkVo[contractNum="+contractNum+", fundsSources="+fundsSources+", idNum="+idNum+", name="+name+", loanState="+loanState + ", id="+id+", memo="+memo+"]";
	}
}
