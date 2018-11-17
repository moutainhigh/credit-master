package com.zdmoney.credit.fee.domain;

import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

/**
 *  收费报盘队列表
 * @author YM10104
 *
 */
public class LoanFeeOfferQueue extends BaseDomain {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 主键 **/
	private Long id;
	/** 债权编号 **/
	private Long loanId;
	/** 收费主表编号 **/
	private Long feeId;
	/** 处理状态（01：未处理、02：已处理） **/
	private String state;
	/** 预留备注信息 **/
	private String memo;
	/** 创建人 **/
	private String creator;
	/** 创建时间 **/
	private Date createTime;
	/** 修改人 **/
	private String updator;
	/** 修改时间 **/
	private Date updateTime;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getLoanId() {
		return loanId;
	}
	public void setLoanId(Long loanId) {
		this.loanId = loanId;
	}
	public Long getFeeId() {
		return feeId;
	}
	public void setFeeId(Long feeId) {
		this.feeId = feeId;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getUpdator() {
		return updator;
	}
	public void setUpdator(String updator) {
		this.updator = updator;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}	
}