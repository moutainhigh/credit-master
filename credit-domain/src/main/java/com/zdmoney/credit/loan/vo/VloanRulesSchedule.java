package com.zdmoney.credit.loan.vo;

/**
 * @ClassName:     VloanRulesSchedule.java
 * @Description:   TODO
 * @author         liuyh
 * @version        V1.0  
 * @Date           2016年11月17日 下午5:54:57
 */
@Deprecated
public class VloanRulesSchedule {
	
	/** 期数*/
	private int period;
	
	/** 应还款日（日期格式：yyyy-MM-dd）*/
	private String repayDateTheory;
	
	/** 实际还款日（日期格式：yyyy-MM-dd）*/
	private String repayDatePractice;
	
	/** 当月还款状态 0:结清 1:逾期 2：未还款*/
	private String repayStatus;

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public String getRepayDateTheory() {
		return repayDateTheory;
	}

	public void setRepayDateTheory(String repayDateTheory) {
		this.repayDateTheory = repayDateTheory;
	}

	public String getRepayDatePractice() {
		return repayDatePractice;
	}

	public void setRepayDatePractice(String repayDatePractice) {
		this.repayDatePractice = repayDatePractice;
	}

	public String getRepayStatus() {
		return repayStatus;
	}

	public void setRepayStatus(String repayStatus) {
		this.repayStatus = repayStatus;
	}
	
}
