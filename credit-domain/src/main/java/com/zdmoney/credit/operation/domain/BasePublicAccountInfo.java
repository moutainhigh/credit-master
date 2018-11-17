package com.zdmoney.credit.operation.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class BasePublicAccountInfo extends BaseDomain{
    
    private static final long serialVersionUID = -17763303526324701L;

    /**
     * 主键Id
     */
    private Long id;

    /**
     * 还款金额
     */
    private BigDecimal amount;

    /**
     * 附言
     */
    private String comments;

    /**
     * 本方账户
     */
    private String firstAccount;

    /**
     * 债权Id
     */
    private Long loanId;

    /**
     * 上传人
     */
    private Long operatorId;

    /**
     * 用途
     */
    private String purpose;

    /**
     * 认领人
     */
    private BigDecimal recOperatorId;

    /**
     * 摘要
     */
    private String remark;

    /**
     * 还款日期
     */
    private Date repayDate;

    /**
     * 对方账号
     */
    private String secondAccount;

    /**
     * 对方行号
     */
    private String secondBank;

    /**
     * 对方单位
     */
    private String secondCompany;

    /**
     * 认领状态（未认领 已认领 已导出 渠道确认）
     */
    private String status;

    /**
     * 还款时间
     */
    private String time;

    /**
     * 借贷类型（借/贷）
     */
    private String type;

    /**
     * 凭证号
     */
    private String voucherNo;

    /**
     * 认领时间
     */
    private Date recTime;
    
    /**
     * 认领日期(起)
     */
    private Date recTimeStart;
    
    /**
     * 认领日期(止)
     */
    private Date recTimeEnd;
    
    /**
     * 还款日期起
     */
    private Date repayDateStart;
    
    /**
     * 还款日期止
     */
    private Date repayDateEnd;
    
    /**
     * 查询最大件数
     */
    private Long max;
    
    /**
     * 认领者工号
     */
    private String recUsercode;
    
    /**
     * 线下还款内部流水号
     */
    private String repayNo;

    /**
     * 解冻状态
     */
    private String lockStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getFirstAccount() {
        return firstAccount;
    }

    public void setFirstAccount(String firstAccount) {
        this.firstAccount = firstAccount;
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public BigDecimal getRecOperatorId() {
        return recOperatorId;
    }

    public void setRecOperatorId(BigDecimal recOperatorId) {
        this.recOperatorId = recOperatorId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getRepayDate() {
        return repayDate;
    }

    public void setRepayDate(Date repayDate) {
        this.repayDate = repayDate;
    }

    public String getSecondAccount() {
        return secondAccount;
    }

    public void setSecondAccount(String secondAccount) {
        this.secondAccount = secondAccount;
    }

    public String getSecondBank() {
        return secondBank;
    }

    public void setSecondBank(String secondBank) {
        this.secondBank = secondBank;
    }

    public String getSecondCompany() {
        return secondCompany;
    }

    public void setSecondCompany(String secondCompany) {
        this.secondCompany = secondCompany;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public Date getRecTime() {
        return recTime;
    }

    public void setRecTime(Date recTime) {
        this.recTime = recTime;
    }

    public Date getRecTimeStart() {
        return recTimeStart;
    }

    public void setRecTimeStart(Date recTimeStart) {
        this.recTimeStart = recTimeStart;
    }

    public Date getRecTimeEnd() {
        return recTimeEnd;
    }

    public void setRecTimeEnd(Date recTimeEnd) {
        this.recTimeEnd = recTimeEnd;
    }

    public Date getRepayDateStart() {
        return repayDateStart;
    }

    public void setRepayDateStart(Date repayDateStart) {
        this.repayDateStart = repayDateStart;
    }

    public Date getRepayDateEnd() {
        return repayDateEnd;
    }

    public void setRepayDateEnd(Date repayDateEnd) {
        this.repayDateEnd = repayDateEnd;
    }

    public Long getMax() {
        return max;
    }

    public void setMax(Long max) {
        this.max = max;
    }

    public String getRecUsercode() {
        return recUsercode;
    }

    public void setRecUsercode(String recUsercode) {
        this.recUsercode = recUsercode;
    }

	public String getRepayNo() {
		return repayNo;
	}

	public void setRepayNo(String repayNo) {
		this.repayNo = repayNo;
	}

    public String getLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(String lockStatus) {
        this.lockStatus = lockStatus;
    }
}