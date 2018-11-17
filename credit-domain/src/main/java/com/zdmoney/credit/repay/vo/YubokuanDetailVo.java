package com.zdmoney.credit.repay.vo;

import java.math.BigDecimal;

public class YubokuanDetailVo extends RequestManagementVo {  

	private static final long serialVersionUID = 1L;
	/**债权id**/
    private Long loanId;
    /** 信托项目简码 **/
    private String orgCode;
    /** 合同号/借款编号 **/
    private String contractNum;
    /** 申请地点 **/
    private String requestPlace;
    /** 申请号 **/
    private String requestNo;
    /** 商品类型 **/
    private String channel;
    /** 证件类型 **/
    private String idType;
    /** 证件号码 **/
    private String idnum;
    /** 姓名 **/
    private String name;
    /** 联系电话 **/
    private String contactPhone;
    /** 移动电话 **/
    private String mphone;
    /** 邮政编码 **/
    private String postcode;
    /** 通讯地址 **/
    private String address;
    /** 申请用途 **/
    private String purpose;
    /** 合同金额 **/
    private BigDecimal pactMoney;
    /** 实付金额 **/
    private BigDecimal money;
    /** 申请币种 **/
    private String currency;
    /** 申请期限（月） **/
    private Long time;
    /** 还款帐户类型 **/
    private String accountType;
    /** 还款帐号 **/
    private String backAccount;
    /** 还款方式 **/
    private String repaymentMethod;
    /** 扣款日类型 **/
    private String repaymentDayType;
    /** 扣款日类别 **/
    private String repaymentDayCategory;
    /** 扣款日期（每月应还款日） **/
    private Long promiseReturnDate;
    /** 婚姻状况 **/
    private String married;
    /** 学历 **/
    private String edLevel;
    /** 户籍 **/
    private String hrAddress;
    /** 个人月收入 **/
    private BigDecimal totalMonthlyIncome;
    /** 家庭住址 **/
    private String familyAddress;
    /** 家庭邮编 **/
    private String familyPostcode;
    /** 住宅电话 **/
    private String familyPhone;
    /** 宽限期天数 **/
    private String graceDays;
    /** 缴费方式 **/
    private String payMethod;
    /** 贷款类型 **/
    private String loanType;
    /** 借款人类型 **/
    private String customerType;
    /** 产品编号 **/
    private String productCode;
    /** 产品名称 **/
    private String productName;
    /** 手续费 **/
    private BigDecimal counterFee;
    /** 手续费率 **/
    private BigDecimal counterRate;
    /** 利率 **/
    private BigDecimal rate;
    /** 提前还款违约金比率 **/
    private BigDecimal defaultRate;
    /** 罚息率（月） **/
    private BigDecimal penaltyRate;
    /** 履行担保天数 **/
    private Long assureDays;
    /** 服务费 **/
    private BigDecimal serviceFee;
    /** 服务费率 **/
    private BigDecimal serviceFeeRate;
    /** 担保费 **/
    private String assureFee;
    /** 担保费率 **/
    private String assureFeeRate;
    /** 银行代码 **/
    private String bankCode;
    /** 开户省市 **/
    private String openAccountCity;
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
    /** 职业 **/
    private String profession;
    /** 单位名称 **/
    private String company;
    /** 单位所属行业 **/
    private String industryType;
    /** 单位地址 **/
    private String cAddress;
    /** 单位邮政编码 **/
    private String cCode;
    /** 本单位工作起始年份 **/
    private String startYear;
    /** 本人职务 **/
    private String officialRank;
    /** 本人职称 **/
    private String staff;
    /** 担保方式 **/
    private String assureMethod;
    /** 担保人姓名 **/
    private String assureName;
    /** 担保人证件类型 **/
    private String assureIdType;
    /** 担保人证件号码 **/
    private String assureIdnum;
    /** 担保金额 **/
    private BigDecimal assureAmount;
    /** 担保关系 **/
    private String assureRelation;
    /** 放款账户类型 **/
    private String lenderAcountType;
    /** 放款银行代码 **/
    private String lenderBankCode;
    /** 放款账户名称 **/
    private String lenderAcountName;
    /** 放款账户号码 **/
    private String lenderAcount;
    /** 放款账户开户支行 **/
    private String lenderBranchBank;
    /** 放款账户开户所在省 **/
    private String lenderAcountProvince;
    /** 放款账户开户所在市 **/
    private String lenderAcountCity;
    /** 划拨申请书文件名称 **/
    private String applyName;
    /** 放款日期 **/
    private String loanDate;
    /** 到期日期 **/
    private String endrdate;
    /** 利率类型 **/
    private String rateType;
    /**	起息日期 **/
    private String valueDate;
    
    public Long getLoanId() {
		return loanId;
	}

	public void setLoanId(Long loanId) {
		this.loanId = loanId;
	}

	public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getContractNum() {
        return contractNum;
    }

    public void setContractNum(String contractNum) {
        this.contractNum = contractNum;
    }

    public String getRequestPlace() {
        return requestPlace;
    }

    public void setRequestPlace(String requestPlace) {
        this.requestPlace = requestPlace;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getIdnum() {
        return idnum;
    }

    public void setIdnum(String idnum) {
        this.idnum = idnum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getMphone() {
        return mphone;
    }

    public void setMphone(String mphone) {
        this.mphone = mphone;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public BigDecimal getPactMoney() {
        return pactMoney;
    }

    public void setPactMoney(BigDecimal pactMoney) {
        this.pactMoney = pactMoney;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getBackAccount() {
        return backAccount;
    }

    public void setBackAccount(String backAccount) {
        this.backAccount = backAccount;
    }

    public String getRepaymentMethod() {
        return repaymentMethod;
    }

    public void setRepaymentMethod(String repaymentMethod) {
        this.repaymentMethod = repaymentMethod;
    }

    public String getRepaymentDayType() {
        return repaymentDayType;
    }

    public void setRepaymentDayType(String repaymentDayType) {
        this.repaymentDayType = repaymentDayType;
    }

    public String getRepaymentDayCategory() {
        return repaymentDayCategory;
    }

    public void setRepaymentDayCategory(String repaymentDayCategory) {
        this.repaymentDayCategory = repaymentDayCategory;
    }

    public Long getPromiseReturnDate() {
        return promiseReturnDate;
    }

    public void setPromiseReturnDate(Long promiseReturnDate) {
        this.promiseReturnDate = promiseReturnDate;
    }

    public String getMarried() {
        return married;
    }

    public void setMarried(String married) {
        this.married = married;
    }

    public String getEdLevel() {
        return edLevel;
    }

    public void setEdLevel(String edLevel) {
        this.edLevel = edLevel;
    }

    public String getHrAddress() {
        return hrAddress;
    }

    public void setHrAddress(String hrAddress) {
        this.hrAddress = hrAddress;
    }

    public BigDecimal getTotalMonthlyIncome() {
        return totalMonthlyIncome;
    }

    public void setTotalMonthlyIncome(BigDecimal totalMonthlyIncome) {
        this.totalMonthlyIncome = totalMonthlyIncome;
    }

    public String getFamilyAddress() {
        return familyAddress;
    }

    public void setFamilyAddress(String familyAddress) {
        this.familyAddress = familyAddress;
    }

    public String getFamilyPostcode() {
        return familyPostcode;
    }

    public void setFamilyPostcode(String familyPostcode) {
        this.familyPostcode = familyPostcode;
    }

    public String getFamilyPhone() {
        return familyPhone;
    }

    public void setFamilyPhone(String familyPhone) {
        this.familyPhone = familyPhone;
    }

    public String getGraceDays() {
        return graceDays;
    }

    public void setGraceDays(String graceDays) {
        this.graceDays = graceDays;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getCounterFee() {
        return counterFee;
    }

    public void setCounterFee(BigDecimal counterFee) {
        this.counterFee = counterFee;
    }

    public BigDecimal getCounterRate() {
        return counterRate;
    }

    public void setCounterRate(BigDecimal counterRate) {
        this.counterRate = counterRate;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getDefaultRate() {
        return defaultRate;
    }

    public void setDefaultRate(BigDecimal defaultRate) {
        this.defaultRate = defaultRate;
    }

    public BigDecimal getPenaltyRate() {
        return penaltyRate;
    }

    public void setPenaltyRate(BigDecimal penaltyRate) {
        this.penaltyRate = penaltyRate;
    }

    public Long getAssureDays() {
        return assureDays;
    }

    public void setAssureDays(Long assureDays) {
        this.assureDays = assureDays;
    }

    public BigDecimal getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(BigDecimal serviceFee) {
        this.serviceFee = serviceFee;
    }

    public BigDecimal getServiceFeeRate() {
        return serviceFeeRate;
    }

    public void setServiceFeeRate(BigDecimal serviceFeeRate) {
        this.serviceFeeRate = serviceFeeRate;
    }

    public String getAssureFee() {
        return assureFee;
    }

    public void setAssureFee(String assureFee) {
        this.assureFee = assureFee;
    }

    public String getAssureFeeRate() {
        return assureFeeRate;
    }

    public void setAssureFeeRate(String assureFeeRate) {
        this.assureFeeRate = assureFeeRate;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getOpenAccountCity() {
        return openAccountCity;
    }

    public void setOpenAccountCity(String openAccountCity) {
        this.openAccountCity = openAccountCity;
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

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getIndustryType() {
        return industryType;
    }

    public void setIndustryType(String industryType) {
        this.industryType = industryType;
    }

    public String getcAddress() {
        return cAddress;
    }

    public void setcAddress(String cAddress) {
        this.cAddress = cAddress;
    }

    public String getcCode() {
        return cCode;
    }

    public void setcCode(String cCode) {
        this.cCode = cCode;
    }

    public String getStartYear() {
        return startYear;
    }

    public void setStartYear(String startYear) {
        this.startYear = startYear;
    }

    public String getOfficialRank() {
        return officialRank;
    }

    public void setOfficialRank(String officialRank) {
        this.officialRank = officialRank;
    }

    public String getStaff() {
        return staff;
    }

    public void setStaff(String staff) {
        this.staff = staff;
    }

    public String getAssureMethod() {
        return assureMethod;
    }

    public void setAssureMethod(String assureMethod) {
        this.assureMethod = assureMethod;
    }

    public String getAssureName() {
        return assureName;
    }

    public void setAssureName(String assureName) {
        this.assureName = assureName;
    }

    public String getAssureIdType() {
        return assureIdType;
    }

    public void setAssureIdType(String assureIdType) {
        this.assureIdType = assureIdType;
    }

    public String getAssureIdnum() {
        return assureIdnum;
    }

    public void setAssureIdnum(String assureIdnum) {
        this.assureIdnum = assureIdnum;
    }

    public BigDecimal getAssureAmount() {
        return assureAmount;
    }

    public void setAssureAmount(BigDecimal assureAmount) {
        this.assureAmount = assureAmount;
    }

    public String getAssureRelation() {
        return assureRelation;
    }

    public void setAssureRelation(String assureRelation) {
        this.assureRelation = assureRelation;
    }

    public String getLenderAcountType() {
        return lenderAcountType;
    }

    public void setLenderAcountType(String lenderAcountType) {
        this.lenderAcountType = lenderAcountType;
    }

    public String getLenderBankCode() {
        return lenderBankCode;
    }

    public void setLenderBankCode(String lenderBankCode) {
        this.lenderBankCode = lenderBankCode;
    }

    public String getLenderAcountName() {
        return lenderAcountName;
    }

    public void setLenderAcountName(String lenderAcountName) {
        this.lenderAcountName = lenderAcountName;
    }

    public String getLenderAcount() {
        return lenderAcount;
    }

    public void setLenderAcount(String lenderAcount) {
        this.lenderAcount = lenderAcount;
    }

    public String getLenderBranchBank() {
        return lenderBranchBank;
    }

    public void setLenderBranchBank(String lenderBranchBank) {
        this.lenderBranchBank = lenderBranchBank;
    }

    public String getLenderAcountProvince() {
        return lenderAcountProvince;
    }

    public void setLenderAcountProvince(String lenderAcountProvince) {
        this.lenderAcountProvince = lenderAcountProvince;
    }

    public String getLenderAcountCity() {
        return lenderAcountCity;
    }

    public void setLenderAcountCity(String lenderAcountCity) {
        this.lenderAcountCity = lenderAcountCity;
    }

    public String getApplyName() {
        return applyName;
    }

    public void setApplyName(String applyName) {
        this.applyName = applyName;
    }

    public String getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(String loanDate) {
        this.loanDate = loanDate;
    }

    public String getEndrdate() {
        return endrdate;
    }

    public void setEndrdate(String endrdate) {
        this.endrdate = endrdate;
    }

    public String getRateType() {
        return rateType;
    }

    public void setRateType(String rateType) {
        this.rateType = rateType;
    }

	public String getValueDate() {
		return valueDate;
	}

	public void setValueDate(String valueDate) {
		this.valueDate = valueDate;
	}
    
}