package com.zdmoney.credit.common.vo.core;

import java.util.Arrays;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 还款试算接口参数VO类
 * 
 * @author 00235304
 */
public class RepayTrailParamsVO {

	/** 姓名 */
	private String name;

	/** 身份证号 */
	private String idnum;

	/** 债权编号 */
	private String[] loanIds;

	/** 手机号码 */
	private String mphone;

	/** 还款方式 */
	private String repayType;

	/** 还款日期 */
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date repayDate;

	/** 操作员编号 */
	private String userCode;

	/** 显示指定页数,默认值是：10 */
	private Long offset = 10L;

	/** 每页的记录条数, 默认是：0 */
	private Long max = 0L;

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

	public String[] getLoanIds() {
		return loanIds;
	}

	public void setLoanIds(String[] loanIds) {
		this.loanIds = loanIds;
	}

	public String getMphone() {
		return mphone;
	}

	public void setMphone(String mphone) {
		this.mphone = mphone;
	}

	public String getRepayType() {
		return repayType;
	}

	public void setRepayType(String repayType) {
		this.repayType = repayType;
	}

	public Date getRepayDate() {
		return repayDate;
	}

	public void setRepayDate(Date repayDate) {
		this.repayDate = repayDate;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public Long getOffset() {
		return offset;
	}

	public void setOffset(Long offset) {
		this.offset = offset;
	}

	public Long getMax() {
		return max;
	}

	public void setMax(Long max) {
		this.max = max;
	}

	@Override
	public String toString() {
		return "RepayTrailParamsVO [name=" + name + ", idnum=" + idnum
				+ ", loanIds=" + Arrays.toString(loanIds) + ", mphone="
				+ mphone + ", repayType=" + repayType + ", repayDate="
				+ repayDate + ", userCode=" + userCode + ", offset=" + offset
				+ ", max=" + max + "]";
	}
}
