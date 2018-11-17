package com.zdmoney.credit.fee.domain.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.zdmoney.credit.fee.domain.LoanFeeInfo;

/**
 * 收费录入实体类
 * @author 00236640
 * @version $Id: LoanFeeInput.java, v 0.1 2016年7月14日 下午4:39:43 00236640 Exp $
 */
public class LoanFeeInput extends LoanFeeInfo {

    private static final long serialVersionUID = 3047467344928692298L;
    /** 服务费编号 **/
    private Long id;
    
    /** 管理营业部名称 **/
    private String salesDepartmentName;

    /** 客户姓名 **/
    private String name;

    /** 身份证号码 **/
    private String idNum;

    /** 合同编号 **/
    private String contractNum;
    
    /** 合同来源 **/
    private String fundsSources;

    /** 借款类型 **/
    private String loanType;

    /** 合同金额 **/
    private BigDecimal pactMoney;

    /** 借款期限 **/
    private Long time;

    /** 放款日期 **/
    private Date grantMoneyDate;

    /** 放款日期（起） **/
    private String grantMoneyDateStart;

    /** 放款日期（止） **/
    private String grantMoneyDateEnd;
    
    public String getFundsSources() {
		return fundsSources;
	}

	public void setFundsSources(String fundsSources) {
		this.fundsSources = fundsSources;
	}

	public String getSalesDepartmentName() {
        return salesDepartmentName;
    }

    public void setSalesDepartmentName(String salesDepartmentName) {
        this.salesDepartmentName = salesDepartmentName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdNum() {
        return idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }

    public String getContractNum() {
        return contractNum;
    }

    public void setContractNum(String contractNum) {
        this.contractNum = contractNum;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public BigDecimal getPactMoney() {
        return pactMoney;
    }

    public void setPactMoney(BigDecimal pactMoney) {
        this.pactMoney = pactMoney;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Date getGrantMoneyDate() {
        return grantMoneyDate;
    }

    public void setGrantMoneyDate(Date grantMoneyDate) {
        this.grantMoneyDate = grantMoneyDate;
    }

    public String getGrantMoneyDateStart() {
        return grantMoneyDateStart;
    }

    public void setGrantMoneyDateStart(String grantMoneyDateStart) {
        this.grantMoneyDateStart = grantMoneyDateStart;
    }

    public String getGrantMoneyDateEnd() {
        return grantMoneyDateEnd;
    }

    public void setGrantMoneyDateEnd(String grantMoneyDateEnd) {
        this.grantMoneyDateEnd = grantMoneyDateEnd;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
    
}