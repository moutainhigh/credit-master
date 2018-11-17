package com.zdmoney.credit.common.vo.core;

/**
 * 爱特债权导出接口参数VO
 * 
 * @author 00235304
 */
public class LoanExportParamsVO {

	/**债权编号*/
	private Long loanId;
	
	/** 营业部编号 */
	private Long salesDepartment;

	/** 姓名 */
	private String name;

	/** 手机号码 */
	private String mphone;

	/** 身份证号 */
	private String idnum;

	/** 合同编号 */
	private String eloanContractNum;

	/** 合同来源 */
	private String fundsSources;
	
	/**查询数据最大条数,默认2000*/
	private Long max=2000L;
	
	public Long getLoanId() {
		return loanId;
	}

	public void setLoanId(Long loanId) {
		this.loanId = loanId;
	}
	
	public Long getSalesDepartment() {
		return salesDepartment;
	}

	public void setSalesDepartment(Long salesDepartment) {
		this.salesDepartment = salesDepartment;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMphone() {
		return mphone;
	}

	public void setMphone(String mphone) {
		this.mphone = mphone;
	}

	public String getIdnum() {
		return idnum;
	}

	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}

	public String getEloanContractNum() {
		return eloanContractNum;
	}

	public void setEloanContractNum(String eloanContractNum) {
		this.eloanContractNum = eloanContractNum;
	}

	public String getFundsSources() {
		return fundsSources;
	}

	public void setFundsSources(String fundsSources) {
		this.fundsSources = fundsSources;
	}

	public Long getMax() {
		return max;
	}

	public void setMax(Long max) {
		this.max = max;
	}
}