package com.zdmoney.credit.common.vo.core;

import java.math.BigDecimal;


/**
 * 特殊还款接口参数VO
 * 
 * @author 00235304
 */
public class SpecialRepayParamsVO {

	/** 债权编号 */
	private Long loanId;

	/** 操作员编号 */
	private String userCode;

	/** 生效日期 */
	private String effectiveDate;

	/** 特殊还款类型 */
	private String spRepayType;
	
	/** 特殊还款状态 */
	private String spRepayState;
	
	/** 减免金额*/
	private BigDecimal reliefAmount;
	
	/**姓名*/
	private String name;
	
	/**身份证号*/
	private String idnum;
	
	/**手机号*/
	private String mphone;
	
	/**客户经理ID*/
	private Long salesmanId;
	
	/**每页显示条数，默认为10*/
	private Long max;
	
	/**显示指定的页数*/
	private Long offset;

	private boolean isSpRelief = false;
	
	public Long getLoanId() {
		return loanId;
	}

	public void setLoanId(Long loanId) {
		this.loanId = loanId;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public String getSpRepayType() {
		return spRepayType;
	}

	public void setSpRepayType(String spRepayType) {
		this.spRepayType = spRepayType;
	}
	
	public String getSpRepayState() {
		return spRepayState;
	}

	public void setSpRepayState(String spRepayState) {
		this.spRepayState = spRepayState;
	}
	
	public BigDecimal getReliefAmount() {
		return reliefAmount;
	}

	public void setReliefAmount(BigDecimal reliefAmount) {
		this.reliefAmount = reliefAmount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdnum() {
		return idnum;
	}

	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}

	public String getMphone() {
		return mphone;
	}

	public void setMphone(String mphone) {
		this.mphone = mphone;
	}

	public Long getSalesmanId() {
		return salesmanId;
	}

	public void setSalesmanId(Long salesmanId) {
		this.salesmanId = salesmanId;
	}

	public Long getMax() {
		return max;
	}

	public void setMax(Long max) {
		this.max = max;
	}

	public Long getOffset() {
		return offset;
	}

	public void setOffset(Long offset) {
		this.offset = offset;
	}

	public boolean isSpRelief() {
		return isSpRelief;
	}

	public void setIsSpRelief(boolean isSpRelief) {
		this.isSpRelief = isSpRelief;
	}

	@Override
	public String toString() {
		return "SpecialRepayParamsVO [loanId=" + loanId + ", userCode="
				+ userCode + ", effectiveDate=" + effectiveDate
				+ ", spRepayType=" + spRepayType + ", spRepayState="
				+ spRepayState + ", reliefAmount=" + reliefAmount + ", name="
				+ name + ", idnum=" + idnum + ", mphone=" + mphone
				+ ", salesmanId=" + salesmanId + ", max=" + max + ", offset="
				+ offset +",isSpRelief="+ isSpRelief + "]";
	}
	
}
