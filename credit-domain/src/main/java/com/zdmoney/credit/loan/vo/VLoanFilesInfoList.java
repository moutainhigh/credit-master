package com.zdmoney.credit.loan.vo;

import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import com.zdmoney.credit.common.annotation.ConvertionCLazzType;

/**
 * 客户档案信息
 * @author 00236633
 */
public class VLoanFilesInfoList {

	@Length(min=0,max=80,message="客户名称最多只能输入80个字符")
	private String borrowName;
	
	@Length(min=0,max=80,message="手机号码最多只能输入11个字符")
	private String borrowMphone;
	
	@Length(min=0,max=80,message="手机号码最多只能输入18个字符")
	private String borrowIdNum;
	
	@Range(min=1,max=999999999999999999l,message="客户经理id必须大于0,小于1000000000000000000")
	private Long salesmanId;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@ConvertionCLazzType(String.class)
	private Date grantMoneyDateStart;
	//合同编号
	private String contractNum;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@ConvertionCLazzType(String.class)
	private Date grantMoneyDateEnd;
	
	@NotNull(message="页不能为空")
	@Range(min=1,max=999999999,message="页必须大于0,小于1000000000")
	private Integer page;
	
	@NotNull(message="页大小不能为空")
	@Range(min=1,max=999999999,message="页大小必须大于零0,,小于1000000000")
	private Integer rows;
    
	public String getBorrowName() {
		return borrowName;
	}

	public void setBorrowName(String borrowName) {
		this.borrowName = borrowName;
	}

	public String getBorrowMphone() {
		return borrowMphone;
	}

	public void setBorrowMphone(String borrowMphone) {
		this.borrowMphone = borrowMphone;
	}

	public String getBorrowIdNum() {
		return borrowIdNum;
	}

	public void setBorrowIdNum(String borrowIdNum) {
		this.borrowIdNum = borrowIdNum;
	}

	public Long getSalesmanId() {
		return salesmanId;
	}

	public void setSalesmanId(Long salesmanId) {
		this.salesmanId = salesmanId;
	}

	public Date getGrantMoneyDateStart() {
		return grantMoneyDateStart;
	}

	public void setGrantMoneyDateStart(Date grantMoneyDateStart) {
		this.grantMoneyDateStart = grantMoneyDateStart;
	}

	public Date getGrantMoneyDateEnd() {
		return grantMoneyDateEnd;
	}

	public void setGrantMoneyDateEnd(Date grantMoneyDateEnd) {
		this.grantMoneyDateEnd = grantMoneyDateEnd;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	public String getContractNum() {
		return contractNum;
	}

	public void setContractNum(String contractNum) {
		this.contractNum = contractNum;
	}
	
}
