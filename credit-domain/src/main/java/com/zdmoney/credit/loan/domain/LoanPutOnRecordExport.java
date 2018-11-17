package com.zdmoney.credit.loan.domain;

import com.zdmoney.credit.framework.domain.BaseDomain;

/**
 *  备案导出 domain
 *  2016/9/12.
 */
public class LoanPutOnRecordExport extends BaseDomain {
    private static final long serialVersionUID = 1656828603935154486L;
    /**
     * 企业代号(商户代码)
     */
    private String enterpriseCode;
    /**
     * 费项代码
     */
    private String expenditure;
    /**
     * 行号
     */
    private String bankNo;
    /**
     * 银行行别
     */
    private String bankType;
    /**
     * 扣款账号
     */
    private String account;
    /**
     * 扣款账号名(户名)
     */
    private String accountName;
    /**
     * 备注
     */
    private String remak;
    /**
     * 合同编号
     */
    private String contractNum;
   

    public String getEnterpriseCode() {
        return enterpriseCode;
    }

    public void setEnterpriseCode(String enterpriseCode) {
        this.enterpriseCode = enterpriseCode;
    }

   

    public String getExpenditure() {
        return expenditure;
    }

    public void setExpenditure(String expenditure) {
        this.expenditure = expenditure;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    

    public String getRemak() {
        return remak;
    }

    public void setRemak(String remak) {
        this.remak = remak;
    }

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

	public String getContractNum() {
		return contractNum;
	}

	public void setContractNum(String contractNum) {
		this.contractNum = contractNum;
	}
}
