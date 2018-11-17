package com.zdmoney.credit.common.vo.core;

public class BlackListVo {
	/**风险发现时间*/
	private String riskTime;
	/**organ*/
	private String organ;
	/**name*/
	private String name;
	/**idnum*/
	private String idnum;
	/**mphone*/
	private String mphone;
	/**tel*/
	private String tel;
	/**company*/
	private String company;
	/**memo*/
	private String memo;
	/**rejectDate*/
	private String rejectDate;
	
	public String getRiskTime() {
		return riskTime;
	}
	public void setRiskTime(String riskTime) {
		this.riskTime = riskTime;
	}
	public String getOrgan() {
		return organ;
	}
	public void setOrgan(String organ) {
		this.organ = organ;
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
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getRejectDate() {
		return rejectDate;
	}
	public void setRejectDate(String rejectDate) {
		this.rejectDate = rejectDate;
	}
	
	@Override
	public String toString() {
		return "BlackListVo [riskTime=" + riskTime + ", organ=" + organ + ", name=" + name
				+ ", idnum=" + idnum + ", mphone=" + mphone + ", tel=" + tel 
				+ ", company=" + company + ", memo=" + memo + ", rejectDate="
				+ rejectDate + "]";
	}
}
