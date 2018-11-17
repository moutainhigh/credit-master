package com.zdmoney.credit.repay.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

/**
 * 扣款结果上传表
 * @author 10098  2016年11月29日 下午8:01:00
 */
public class AbsDeductResultUpload extends BaseDomain{

	private static final long serialVersionUID = -6172303000281796662L;

	private Long id;
	
	private String batNo;
	/** 合同号 **/
    private String pactNo;
    
    /** 扣款流水号 **/
    private String serialNo;
    
    /** 扣款方式 **/
    private String deductMethod;
    
    /** 扣款类型 **/
    private String deductType;
    
    /** 减免标识 **/
    private String reductTag;
    
    /** 姓名 **/
    private String name;
    
    /** 证件类型 **/
    private String idType;
    
    /** 证件号码 **/
    private String idNo;
    
    /** 扣款账号银行代码 **/
    private String bankCode;
    
    /** 扣款账号 **/
    private String repayAcct;
    
    /** 扣款币种 **/
    private String currency;
    
    /** 本期状态 **/
    private String currStatus;
    
    /** 本期期数 **/
    private Integer cnt;
    
    /** 实扣金额 **/
    private BigDecimal repayTotal;
    
    /** 实扣本金 **/
    private BigDecimal repayAmt;
    
    /** 实扣利息 **/
    private BigDecimal repayInte;
    
    /** 实扣罚息 **/
    private BigDecimal repayOver;
    
    /** 实扣违约金 **/
    private BigDecimal deductDefaultMoney;
    
    /** 实扣手续费 **/
    private BigDecimal deductPoundage;
    
    /** 实还担保费 **/
    private BigDecimal repayGuaranteeFee;
    
    /** 实还服务费 **/
    private BigDecimal repayServiceFee;
    
    /** 逾期天数 **/
    private Integer overdueDay;
    
    /** 扣款日期（实际） **/
    private String payDate;
    
    /** 实扣费总额 **/
    private BigDecimal feeTotal;
    
    /** 费用一 **/
    private BigDecimal fee1;
    
    /** 费用二 **/
    private BigDecimal fee2;
    
    /** 费用三 **/
    private BigDecimal fee3;
    
    /** 费用四 **/
    private BigDecimal fee4;
    
    /** 费用五 **/
    private BigDecimal fee5;
    
    /** 差额划拨费用1 **/
    private BigDecimal balanceTranFee1;
    
    /** 差额划拨费用2 **/
    private BigDecimal balanceTranFee2;
    
    /** 差额划拨费用3 **/
    private BigDecimal balanceTranFee3;
    
    /** 溢缴款使用额度 **/
    private BigDecimal overflowPaymentUsage;
    /** 创建时间 **/
    private Date createTime;
    /** 更新时间 **/
    private Date updateTime;
    /** 上传状态  **/
    private String status;
    /** 错误描述 **/
    private String errDesc;
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBatNo() {
		return batNo;
	}

	public void setBatNo(String batNo) {
		this.batNo = batNo;
	}

	public String getPactNo() {
		return pactNo;
	}

	public void setPactNo(String pactNo) {
		this.pactNo = pactNo;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getDeductMethod() {
		return deductMethod;
	}

	public void setDeductMethod(String deductMethod) {
		this.deductMethod = deductMethod;
	}

	public String getDeductType() {
		return deductType;
	}

	public void setDeductType(String deductType) {
		this.deductType = deductType;
	}

	public String getReductTag() {
		return reductTag;
	}

	public void setReductTag(String reductTag) {
		this.reductTag = reductTag;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getRepayAcct() {
		return repayAcct;
	}

	public void setRepayAcct(String repayAcct) {
		this.repayAcct = repayAcct;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCurrStatus() {
		return currStatus;
	}

	public void setCurrStatus(String currStatus) {
		this.currStatus = currStatus;
	}

	public Integer getCnt() {
		return cnt;
	}

	public void setCnt(Integer cnt) {
		this.cnt = cnt;
	}

	public BigDecimal getRepayTotal() {
		return repayTotal;
	}

	public void setRepayTotal(BigDecimal repayTotal) {
		this.repayTotal = repayTotal;
	}

	public BigDecimal getRepayAmt() {
		return repayAmt;
	}

	public void setRepayAmt(BigDecimal repayAmt) {
		this.repayAmt = repayAmt;
	}

	public BigDecimal getRepayInte() {
		return repayInte;
	}

	public void setRepayInte(BigDecimal repayInte) {
		this.repayInte = repayInte;
	}

	public BigDecimal getRepayOver() {
		return repayOver;
	}

	public void setRepayOver(BigDecimal repayOver) {
		this.repayOver = repayOver;
	}

	public BigDecimal getDeductDefaultMoney() {
		return deductDefaultMoney;
	}

	public void setDeductDefaultMoney(BigDecimal deductDefaultMoney) {
		this.deductDefaultMoney = deductDefaultMoney;
	}

	public BigDecimal getDeductPoundage() {
		return deductPoundage;
	}

	public void setDeductPoundage(BigDecimal deductPoundage) {
		this.deductPoundage = deductPoundage;
	}

	public BigDecimal getRepayGuaranteeFee() {
		return repayGuaranteeFee;
	}

	public void setRepayGuaranteeFee(BigDecimal repayGuaranteeFee) {
		this.repayGuaranteeFee = repayGuaranteeFee;
	}

	public BigDecimal getRepayServiceFee() {
		return repayServiceFee;
	}

	public void setRepayServiceFee(BigDecimal repayServiceFee) {
		this.repayServiceFee = repayServiceFee;
	}

	public Integer getOverdueDay() {
		return overdueDay;
	}

	public void setOverdueDay(Integer overdueDay) {
		this.overdueDay = overdueDay;
	}

	public String getPayDate() {
		return payDate;
	}

	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}

	public BigDecimal getFeeTotal() {
		return feeTotal;
	}

	public void setFeeTotal(BigDecimal feeTotal) {
		this.feeTotal = feeTotal;
	}

	public BigDecimal getFee1() {
		return fee1;
	}

	public void setFee1(BigDecimal fee1) {
		this.fee1 = fee1;
	}

	public BigDecimal getFee2() {
		return fee2;
	}

	public void setFee2(BigDecimal fee2) {
		this.fee2 = fee2;
	}

	public BigDecimal getFee3() {
		return fee3;
	}

	public void setFee3(BigDecimal fee3) {
		this.fee3 = fee3;
	}

	public BigDecimal getFee4() {
		return fee4;
	}

	public void setFee4(BigDecimal fee4) {
		this.fee4 = fee4;
	}

	public BigDecimal getFee5() {
		return fee5;
	}

	public void setFee5(BigDecimal fee5) {
		this.fee5 = fee5;
	}

	public BigDecimal getBalanceTranFee1() {
		return balanceTranFee1;
	}

	public void setBalanceTranFee1(BigDecimal balanceTranFee1) {
		this.balanceTranFee1 = balanceTranFee1;
	}

	public BigDecimal getBalanceTranFee2() {
		return balanceTranFee2;
	}

	public void setBalanceTranFee2(BigDecimal balanceTranFee2) {
		this.balanceTranFee2 = balanceTranFee2;
	}

	public BigDecimal getBalanceTranFee3() {
		return balanceTranFee3;
	}

	public void setBalanceTranFee3(BigDecimal balanceTranFee3) {
		this.balanceTranFee3 = balanceTranFee3;
	}

	public BigDecimal getOverflowPaymentUsage() {
		return overflowPaymentUsage;
	}

	public void setOverflowPaymentUsage(BigDecimal overflowPaymentUsage) {
		this.overflowPaymentUsage = overflowPaymentUsage;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getErrDesc() {
		return errDesc;
	}

	public void setErrDesc(String errDesc) {
		this.errDesc = errDesc;
	}
	
}
