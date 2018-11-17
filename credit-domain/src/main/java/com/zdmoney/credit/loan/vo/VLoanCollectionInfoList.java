package com.zdmoney.credit.loan.vo;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import com.zdmoney.credit.common.annotation.ConvertionCLazzType;


/**
 * 档案催收管理
 * @author 00236633
 */
public class VLoanCollectionInfoList {
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@ConvertionCLazzType(String.class)
	private Date signBeginDate;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@ConvertionCLazzType(String.class)
	private Date signEndDate;
	@Length(min=0,max=80,message="客户名称最多只能输入80个字符")
	private String borrowName;
	@Length(min=0,max=80,message="身份证号码最多只能输入18个字符")
	private String borrowIdNum;
	//合同编号
	private String contractNum;
	//放款营业部id
	private String signDepartmentId;
	//操作类型
	private String operateType;
	@NotNull(message="页不能为空")
	@Range(min=1,max=999999999,message="页必须大于0,小于1000000000")
	private Integer page;
	
	@NotNull(message="页大小不能为空")
	@Range(min=1,max=999999999,message="页大小必须大于零0,,小于1000000000")
	private Integer rows;

	public Date getSignBeginDate() {
		return signBeginDate;
	}

	public void setSignBeginDate(Date signBeginDate) {
		this.signBeginDate = signBeginDate;
	}

	public Date getSignEndDate() {
		return signEndDate;
	}

	public void setSignEndDate(Date signEndDate) {
		this.signEndDate = signEndDate;
	}

	public String getBorrowName() {
		return borrowName;
	}

	public void setBorrowName(String borrowName) {
		this.borrowName = borrowName;
	}

	public String getBorrowIdNum() {
		return borrowIdNum;
	}

	public void setBorrowIdNum(String borrowIdNum) {
		this.borrowIdNum = borrowIdNum;
	}

	public String getContractNum() {
		return contractNum;
	}

	public void setContractNum(String contractNum) {
		this.contractNum = contractNum;
	}

	public String getSignDepartmentId() {
		return signDepartmentId;
	}

	public void setSignDepartmentId(String signDepartmentId) {
		this.signDepartmentId = signDepartmentId;
	}

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
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
	
}
