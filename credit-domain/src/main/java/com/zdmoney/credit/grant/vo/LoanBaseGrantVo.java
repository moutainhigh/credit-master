package com.zdmoney.credit.grant.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.zdmoney.credit.grant.domain.LoanBaseGrant;

/**
 * Created by ym10094 on 2016/11/9.
 */
public class LoanBaseGrantVo extends LoanBaseGrant {

    private static final long serialVersionUID = -3702361343396711492L;
    /**
     * 客户姓名
     */
    private String name;
    /**
     * 证件号码
     */
    private String idNum;
    /**
     * 合同来源
     */
    private String founsSource;
    /**
     * 合同金额
     */
    private BigDecimal pactMoney;
    /**
     * 放款金额
     */
    private BigDecimal grantMoney;
    /**
     * 银行名称
     */
    private String bankName;
    /**
     * 支行名称
     */
    private String branchBankName;
    /**
     * 银行账户
     */
    private String bankAccount;
    /**
     * 债权申请编号
     */
    private String appNo;
    /**
     * 合同编号
     */
    private String contractNum;
    /**
     * 开始还款日
     */
    private Date starTrDate;
    /**
     * 结束还款日
     */
    private Date endrDate;
    /**
     * 咨询费
     */
    private BigDecimal referRate;
    /**
     * 管理费
     */
    private BigDecimal manageRate;
    /**
     * 丙方管理费
     */
    private BigDecimal manageRateForPartyc;
    /**
     * 丁方管理费
     */
    private BigDecimal manageRateForPartyd;
    /***
     * 评估费
     */
    private BigDecimal evalRate;
    /**
     * 费用合计
     */
    private BigDecimal rateSum;
    /**
     * 风险金
     */
    private  BigDecimal risk;
    /**
     * 查询开始时间
     */
    private String beginDate;
    
    /**
     * 查询截止时间
     */
    private String endDate;
    /**
     * 操作时间
     */
    private String operate;
    /**
     * 放款营业部
     */
    private String signSalesDepName;
    /**
     * 借款产品
     */
    private String loanType;
    /**
     * 期限
     */
    private String time;
    /**
     * 签约日期
     */
    private Date signDate;
    /**
     * 批次号
     * @return
     */
    private String batchNum;
    
    public String getBatchNum() {
		return batchNum;
	}

	public void setBatchNum(String batchNum) {
		this.batchNum = batchNum;
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

    public String getFounsSource() {
        return founsSource;
    }

    public void setFounsSource(String founsSource) {
        this.founsSource = founsSource;
    }

    public BigDecimal getPactMoney() {
        return pactMoney;
    }

    public void setPactMoney(BigDecimal pactMoney) {
        this.pactMoney = pactMoney;
    }

    public BigDecimal getGrantMoney() {
        return grantMoney;
    }

    public void setGrantMoney(BigDecimal grantMoney) {
        this.grantMoney = grantMoney;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBranchBankName() {
        return branchBankName;
    }

    public void setBranchBankName(String branchBankName) {
        this.branchBankName = branchBankName;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    @Override
    public String getAppNo() {
        return appNo;
    }

    @Override
    public void setAppNo(String appNo) {
        this.appNo = appNo;
    }

    public Date getStarTrDate() {
        return starTrDate;
    }

    public void setStarTrDate(Date starTrDate) {
        this.starTrDate = starTrDate;
    }

    public Date getEndrDate() {
        return endrDate;
    }

    public void setEndrDate(Date endrDate) {
        this.endrDate = endrDate;
    }

    public BigDecimal getReferRate() {
        return referRate;
    }

    public void setReferRate(BigDecimal referRate) {
        this.referRate = referRate;
    }

    public BigDecimal getManageRate() {
        return manageRate;
    }

    public void setManageRate(BigDecimal manageRate) {
        this.manageRate = manageRate;
    }

    public BigDecimal getManageRateForPartyc() {
        return manageRateForPartyc;
    }

    public void setManageRateForPartyc(BigDecimal manageRateForPartyc) {
        this.manageRateForPartyc = manageRateForPartyc;
    }

    public BigDecimal getManageRateForPartyd() {
        return manageRateForPartyd;
    }

    public void setManageRateForPartyd(BigDecimal manageRateForPartyd) {
        this.manageRateForPartyd = manageRateForPartyd;
    }

    public BigDecimal getEvalRate() {
        return evalRate;
    }

    public void setEvalRate(BigDecimal evalRate) {
        this.evalRate = evalRate;
    }

    public BigDecimal getRateSum() {
        return rateSum;
    }

    public void setRateSum(BigDecimal rateSum) {
        this.rateSum = rateSum;
    }

    public BigDecimal getRisk() {
        return risk;
    }

    public void setRisk(BigDecimal risk) {
        this.risk = risk;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }

    public String getContractNum() {
        return contractNum;
    }

    public void setContractNum(String contractNum) {
        this.contractNum = contractNum;
    }

    public String getSignSalesDepName() {
        return signSalesDepName;
    }

    public void setSignSalesDepName(String signSalesDepName) {
        this.signSalesDepName = signSalesDepName;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Date getSignDate() {
        return signDate;
    }

    public void setSignDate(Date signDate) {
        this.signDate = signDate;
    }
}
