package com.zdmoney.credit.common.constant;

/**
 * 逾期天数枚举
 * @author 00236633
 *
 */
public enum OverdueTime {
	Overdue0("30天以内",0,30),
	Overdue1("31-60天",31,60),
	Overdue2("61-90天",61,90),
	Overdue3("91-180天",91,180),
	Overdue4("180天以上",180,Integer.MIN_VALUE);
	
	OverdueTime(String value,Integer overdueTimeStart,Integer overdueTimeEnd){
		this.value=value;
		this.overdueTimeStart=overdueTimeStart;
		this.overdueTimeEnd=overdueTimeEnd;
	}
	
	private String value;
	private Integer overdueTimeStart;
	private Integer overdueTimeEnd;
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Integer getOverdueTimeStart() {
		return overdueTimeStart;
	}
	public void setOverdueTimeStart(Integer overdueTimeStart) {
		this.overdueTimeStart = overdueTimeStart;
	}
	public Integer getOverdueTimeEnd() {
		return overdueTimeEnd;
	}
	public void setOverdueTimeEnd(Integer overdueTimeEnd) {
		this.overdueTimeEnd = overdueTimeEnd;
	}
}
