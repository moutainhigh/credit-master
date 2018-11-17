package com.zdmoney.credit.common.vo.core;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 封装贷前试算接口参数
 * @author 00234770
 * @date 2015年10月21日 上午9:44:22 
 *
 */
public class LoanTrialVo {

	/**客户姓名**/
	private String name;
	/**贷款类型**/
	private String loanType;
	/**借款金额**/
	private BigDecimal money;
	/**借款期数**/
	private Long time;
	/**预计首次还款日**/
	private Date firstRepaymentDate;
	/**合同来源（渠道）**/
	private  String fundsSources;
	/**是否是费率优惠客户y，n**/
	private String isRatePreferLoan;
	
	public String getIsRatePreferLoan() {
		return isRatePreferLoan;
	}

	public void setIsRatePreferLoan(String isRatePreferLoan) {
		this.isRatePreferLoan = isRatePreferLoan;
	}

	public String getFundsSources() {
		return fundsSources == null ? "00001" : fundsSources;
	}

	public void setFundsSources(String fundsSources) {
		this.fundsSources = fundsSources;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLoanType() {
		return loanType;
	}

	public void setLoanType(String loanType) {
		this.loanType = loanType;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public Date getFirstRepaymentDate() {
		return firstRepaymentDate;
	}

	public void setFirstRepaymentDate(Date firstRepaymentDate) {
		this.firstRepaymentDate = firstRepaymentDate;
	}
	
	@Override
	public String toString() {
		return "LoanTrialVo [name=" + name + ", loanType=" + loanType + ", money=" + money + ", time=" + time + ", firstRepaymentDate=" + firstRepaymentDate + ",fundsSources="+fundsSources+",isRatePreferLoan="+isRatePreferLoan+"]";
	}
}
