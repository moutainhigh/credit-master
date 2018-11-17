package com.zdmoney.credit.common.vo.core;


/**
 * 积木盒子债权导出相关接口参数
 *
 */
public class ExternalDebtVO {
	
	/**导出债权ID*/
	private String loanId;

	/**债权导出开始时间*/
	private String startQueryDate; 
	
	/**债权导出结束时间*/
	private String endQueryDate; 
	
	/**excel*/
	private String format;
	
	  /**
     * 查询最大件数
     */
    private Long max;
    
	/**
	 * @return the loanId
	 */
	public String getLoanId() {
		return loanId;
	}

	/**
	 * @param loanId the loanId to set
	 */
	public void setLoanId(String loanId) {
		this.loanId = loanId;
	}
	
	/**
	 * @return the max
	 */
	public Long getMax() {
		return max;
	}

	/**
	 * @param max the max to set
	 */
	public void setMax(Long max) {
		this.max = max;
	}

	/**
	 * @return the startQueryDate
	 */
	public String getStartQueryDate() {
		return startQueryDate;
	}

	/**
	 * @param startQueryDate the startQueryDate to set
	 */
	public void setStartQueryDate(String startQueryDate) {
		this.startQueryDate = startQueryDate;
	}

	/**
	 * @return the endQueryDate
	 */
	public String getEndQueryDate() {
		return endQueryDate;
	}

	/**
	 * @param endQueryDate the endQueryDate to set
	 */
	public void setEndQueryDate(String endQueryDate) {
		this.endQueryDate = endQueryDate;
	}

	/**
	 * @return the format
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * @param format the format to set
	 */
	public void setFormat(String format) {
		this.format = format;
	}

	/**
	 * @return the groupCode
	 */
	public String getGroupCode() {
		return groupCode;
	}

	/**
	 * @param groupCode the groupCode to set
	 */
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	/**员工机构code*/
	private String groupCode;

}
