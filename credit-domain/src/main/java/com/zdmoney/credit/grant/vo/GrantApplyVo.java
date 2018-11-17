package com.zdmoney.credit.grant.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by ym10094 on 2016/11/14.
 */
public class GrantApplyVo<T> implements Serializable {
    private static final long serialVersionUID = 3163417642726789126L;
    /**
     * 合作机构号
     */
    private String sysSource;
    /**
     * 合同号码;
     */
    private String pactNo;
    /**
     * 信托项目编号
     */
    private String projNo;
    /**
     * 合同金额
     */
    private BigDecimal pactAmt = new BigDecimal("0.00");
    /**
     * 趸交费总额
     */
    private BigDecimal feeTotal = new BigDecimal("0.00");
    /**
     * 利率（月）
     */
    private BigDecimal lnRate = new BigDecimal("0.00");
    /**
     * 申请地点
     */
    private String appArea;
    /**
     * 申请用途
     */
    private String appUse;
    /**
     * 合同期限（月）
     */
    private Integer termMon;
    /**
     * 合同期限（日）
     */
    private Integer termDay;
    /**
     * 到期日期 YYYYMMDD
     */
    private String endDate;
    /**
     * 扣款日类型
     */
    private String payType;
    /**
     * 扣款日期
     */
    private Integer payDay;
    /**
     * 期缴（保）费金额
     */
    private BigDecimal vouAmt = new BigDecimal("0.00");
    /**
     * 借款流程
     */
    private String loanState;
    /**
     * 借款流程状态
     */
    private String loanFlowState;
    /**
     * 所属网点(管理营业部)
     */
    private String salesDepartmentId;
    /**
     * 所属网点(放款营业部)
     */
    private String signSalesDepId;
    /**
     *申请营业部
     */
    private String applySalesDepId;
    /**
     *资金来源
     */
    private String fundsSources;
    /**
     *债权归属
     */
    private String loanBelong;
    /**
     *审批金额
     */
    private BigDecimal money = new BigDecimal("0.00");
    /**
     *借款目的
     */
    private String purpose;
    /**
     *申请日期 yyyyMMDdd
     */
    private String requestDate;
    /**
     * 月还款能力
     */
    private BigDecimal restoreem = new BigDecimal("0.00");
    /**
     *审核金额
     */
    private BigDecimal auditingMoney = new BigDecimal("0.00");
    /**
     *申请金额
     */
    private BigDecimal requestMoney = new BigDecimal("0.00");
    /**
     *放款时间YYYYMMDD
     */
    private String grantMoneyDate;
    /**
     *签约日期YYYYMMDD
     */
    private String signDate;
    /**
     *申请期限
     */
    private Integer requestTime;
    /**
     *审批日期 yyyymmdd
     */
    private String auditDate;
    /**
     *审批金额
     */
    private BigDecimal approveMoney = new BigDecimal("0.00");
    /**
     *审批期数
     */
    private Integer approveTime;
    /**
     *合同确认日期 yyyyMMdd
     */
    private String contractDate;
    /**
     * 开始还款日期 yyyyMMdd
     */
    private String startrdate;
    /**
     *结束还款日期 yyyyMMdd
     */
    private String endrdate;
    /**
     *年化利率
     */
    private  BigDecimal rateey = new BigDecimal("0.00");
    /**
     *借款期限（月数）
     */
    private Integer loanTime;
    /**
     *放款金额
     */
    private BigDecimal grantMoney = new BigDecimal("0.00");
    /**
     *风险金
     */
    private BigDecimal riskMoney = new BigDecimal("0.00");
    /**
     *罚息比率
     */
    private BigDecimal penaltyRate = new BigDecimal("0.00");
    /**
     *剩余本金
     */
    private BigDecimal residualPactMoney = new BigDecimal("0.00");
    /**
     *银行年利率均化到每月的利率,即平均每月利息
     */
    private BigDecimal accrualem = new BigDecimal("0.00");
    /**
     *预收的款项
     */
    private BigDecimal advance = new BigDecimal("0.00");
    /**
     *实际到账利率
     */
    private BigDecimal rate = new BigDecimal("0.00");
    /**
     *银行名称
     */
    private String Bank;
    /**
     *是否签订合同
     */
    private String ifPact;
    /**
     *预审批ID
     */
    private String prePactNo;
    /**
     *查证流水号
     */
    private String czPactNo;
    /**
     *工作状态
     */
    private String workSts;
    /**
     *产品号
     */
    private String prdtNo;
    /**
     *产品名称
     */
    private String prdtName;
    /**
     * 账号信息
     */
    private List<GrantAccountVo> listAc;
    /**
     * 共同借款人
     */
    private List<GrantBorrowPersonVo> listCom;
    /**
     * 借款关联人
     */
    private List<GrantBorRelaPerVo> listRel;
    /**
     * 还款计划
     */
    private List<GrantRepaymentDetailVo> listRepay;
    /**
     *借款人ID
     */
    private long borrowerId;
    /**
     * 虚拟渠道
     */
    private String cardChn;
    /**
     * 还款方式
     */
    private String repayType;

    public String getSysSource() {
        return sysSource;
    }

    public void setSysSource(String sysSource) {
        this.sysSource = sysSource;
    }

    public String getPactNo() {
        return pactNo;
    }

    public void setPactNo(String pactNo) {
        this.pactNo = pactNo;
    }

    public String getProjNo() {
        return projNo;
    }

    public void setProjNo(String projNo) {
        this.projNo = projNo;
    }

    public BigDecimal getPactAmt() {
        return pactAmt;
    }

    public void setPactAmt(BigDecimal pactAmt) {
        this.pactAmt = pactAmt;
    }

    public BigDecimal getFeeTotal() {
        return feeTotal;
    }

    public void setFeeTotal(BigDecimal feeTotal) {
        this.feeTotal = feeTotal;
    }

    public BigDecimal getLnRate() {
        return lnRate;
    }

    public void setLnRate(BigDecimal lnRate) {
        this.lnRate = lnRate;
    }

    public String getAppArea() {
        return appArea;
    }

    public void setAppArea(String appArea) {
        this.appArea = appArea;
    }

    public String getAppUse() {
        return appUse;
    }

    public void setAppUse(String appUse) {
        this.appUse = appUse;
    }

    public Integer getTermMon() {
        return termMon;
    }

    public void setTermMon(Integer termMon) {
        this.termMon = termMon;
    }

    public Integer getTermDay() {
        return termDay;
    }

    public void setTermDay(Integer termDay) {
        this.termDay = termDay;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public Integer getPayDay() {
        return payDay;
    }

    public void setPayDay(Integer payDay) {
        this.payDay = payDay;
    }

    public BigDecimal getVouAmt() {
        return vouAmt;
    }

    public void setVouAmt(BigDecimal vouAmt) {
        this.vouAmt = vouAmt;
    }

    public String getLoanState() {
        return loanState;
    }

    public void setLoanState(String loanState) {
        this.loanState = loanState;
    }

    public String getLoanFlowState() {
        return loanFlowState;
    }

    public void setLoanFlowState(String loanFlowState) {
        this.loanFlowState = loanFlowState;
    }

    public String getSalesDepartmentId() {
        return salesDepartmentId;
    }

    public void setSalesDepartmentId(String salesDepartmentId) {
        this.salesDepartmentId = salesDepartmentId;
    }

    public String getSignSalesDepId() {
        return signSalesDepId;
    }

    public void setSignSalesDepId(String signSalesDepId) {
        this.signSalesDepId = signSalesDepId;
    }

    public String getApplySalesDepId() {
        return applySalesDepId;
    }

    public void setApplySalesDepId(String applySalesDepId) {
        this.applySalesDepId = applySalesDepId;
    }

    public String getFundsSources() {
        return fundsSources;
    }

    public void setFundsSources(String fundsSources) {
        this.fundsSources = fundsSources;
    }

    public String getLoanBelong() {
        return loanBelong;
    }

    public void setLoanBelong(String loanBelong) {
        this.loanBelong = loanBelong;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public BigDecimal getRestoreem() {
        return restoreem;
    }

    public void setRestoreem(BigDecimal restoreem) {
        this.restoreem = restoreem;
    }

    public BigDecimal getAuditingMoney() {
        return auditingMoney;
    }

    public void setAuditingMoney(BigDecimal auditingMoney) {
        this.auditingMoney = auditingMoney;
    }

    public BigDecimal getRequestMoney() {
        return requestMoney;
    }

    public void setRequestMoney(BigDecimal requestMoney) {
        this.requestMoney = requestMoney;
    }

    public String getGrantMoneyDate() {
        return grantMoneyDate;
    }

    public void setGrantMoneyDate(String grantMoneyDate) {
        this.grantMoneyDate = grantMoneyDate;
    }

    public String getSignDate() {
        return signDate;
    }

    public void setSignDate(String signDate) {
        this.signDate = signDate;
    }

    public Integer getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Integer requestTime) {
        this.requestTime = requestTime;
    }

    public String getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(String auditDate) {
        this.auditDate = auditDate;
    }

    public BigDecimal getApproveMoney() {
        return approveMoney;
    }

    public void setApproveMoney(BigDecimal approveMoney) {
        this.approveMoney = approveMoney;
    }

    public Integer getApproveTime() {
        return approveTime;
    }

    public void setApproveTime(Integer approveTime) {
        this.approveTime = approveTime;
    }

    public String getContractDate() {
        return contractDate;
    }

    public void setContractDate(String contractDate) {
        this.contractDate = contractDate;
    }

    public String getStartrdate() {
        return startrdate;
    }

    public void setStartrdate(String startrdate) {
        this.startrdate = startrdate;
    }

    public String getEndrdate() {
        return endrdate;
    }

    public void setEndrdate(String endrdate) {
        this.endrdate = endrdate;
    }

    public BigDecimal getRateey() {
        return rateey;
    }

    public void setRateey(BigDecimal rateey) {
        this.rateey = rateey;
    }

    public Integer getLoanTime() {
        return loanTime;
    }

    public void setLoanTime(Integer loanTime) {
        this.loanTime = loanTime;
    }

    public BigDecimal getGrantMoney() {
        return grantMoney;
    }

    public void setGrantMoney(BigDecimal grantMoney) {
        this.grantMoney = grantMoney;
    }

    public BigDecimal getRiskMoney() {
        return riskMoney;
    }

    public void setRiskMoney(BigDecimal riskMoney) {
        this.riskMoney = riskMoney;
    }

    public BigDecimal getPenaltyRate() {
        return penaltyRate;
    }

    public void setPenaltyRate(BigDecimal penaltyRate) {
        this.penaltyRate = penaltyRate;
    }

    public BigDecimal getResidualPactMoney() {
        return residualPactMoney;
    }

    public void setResidualPactMoney(BigDecimal residualPactMoney) {
        this.residualPactMoney = residualPactMoney;
    }

    public BigDecimal getAccrualem() {
        return accrualem;
    }

    public void setAccrualem(BigDecimal accrualem) {
        this.accrualem = accrualem;
    }

    public BigDecimal getAdvance() {
        return advance;
    }

    public void setAdvance(BigDecimal advance) {
        this.advance = advance;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public String getBank() {
        return Bank;
    }

    public void setBank(String bank) {
        Bank = bank;
    }

    public String getIfPact() {
        return ifPact;
    }

    public void setIfPact(String ifPact) {
        this.ifPact = ifPact;
    }

    public String getPrePactNo() {
        return prePactNo;
    }

    public void setPrePactNo(String prePactNo) {
        this.prePactNo = prePactNo;
    }

    public String getCzPactNo() {
        return czPactNo;
    }

    public void setCzPactNo(String czPactNo) {
        this.czPactNo = czPactNo;
    }

    public String getWorkSts() {
        return workSts;
    }

    public void setWorkSts(String workSts) {
        this.workSts = workSts;
    }

    public String getPrdtNo() {
        return prdtNo;
    }

    public void setPrdtNo(String prdtNo) {
        this.prdtNo = prdtNo;
    }

    public String getPrdtName() {
        return prdtName;
    }

    public void setPrdtName(String prdtName) {
        this.prdtName = prdtName;
    }

    public List<GrantAccountVo> getListAc() {
        return listAc;
    }

    public void setListAc(List<GrantAccountVo> listAc) {
        this.listAc = listAc;
    }

    public List<GrantBorrowPersonVo> getListCom() {
        return listCom;
    }

    public void setListCom(List<GrantBorrowPersonVo> listCom) {
        this.listCom = listCom;
    }

    public List<GrantBorRelaPerVo> getListRel() {
        return listRel;
    }

    public void setListRel(List<GrantBorRelaPerVo> listRel) {
        this.listRel = listRel;
    }

    public List<GrantRepaymentDetailVo> getListRepay() {
        return listRepay;
    }

    public void setListRepay(List<GrantRepaymentDetailVo> listRepay) {
        this.listRepay = listRepay;
    }

    public long getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(long borrowerId) {
        this.borrowerId = borrowerId;
    }

    public String getCardChn() {
        return cardChn;
    }

    public void setCardChn(String cardChn) {
        this.cardChn = cardChn;
    }

    public String getRepayType() {
        return repayType;
    }

    public void setRepayType(String repayType) {
        this.repayType = repayType;
    }
}

