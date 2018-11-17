package com.zdmoney.credit.common.vo.core;

/**
 * 封装返回债权信息的参数
 * @author 00234770
 * @date 2015年8月28日 上午11:43:56 
 *
 */
public class ReturnLoanVo {
	
	public static final String HUAAO_REPAYMENT_WAY = "等额本息";//华澳信托：还款方式
    public static final String HUAAO_THIRD_PARTY_ID = "xintuo1@xintuo1.com";//华澳信托在畅捷支付开设的虚拟账户
    public static final String FIRST_LEGAL_REPRESENTATIVE = "吴庆斌";//国民信托：甲方对应的：法定代表人
    public static final String FIRST_ADDRESS = "上海市黄浦区中华路1600号17、18楼";//国民信托：甲方对应的：地址
    public static final String FIRST_POSTCODE = "200021";//国民信托：甲方对应的：邮编
    public static final String THIRD_LEGAL_REPRESENTATIVE = "戴卫新";//国民信托：丙方对应的：法定代表人
    public static final String THIRD_ADDRESS = "上海市浦东新区峨山路91弄120号陆家嘴软件园8号楼11楼";//国民信托：丙方对应的：地址
    public static final String THIRD_POSTCODE = "200127";//国民信托：丙方对应的：邮编
    public static final String HUAAO_CORP_NAME = "华澳国际信托有限公司";
    public static final String HUAAO_BANK_NAME = "招商银行上海分行联洋支行";
    public static final String HUAAO_BANK_ACCOUNT = "121910207710204";
    public static final String GUOMIN_CORP_NAME = "国民信托有限公司";
    public static final String GUOMIN_BANK_NAME = "中国光大银行上海分行营业部";
    public static final String GUOMIN_BANK_ACCOUNT = "36510188000853465";


    String pactMoney;   //借款金额
    String thirdPartyId;//华澳信托:甲方指定其在畅捷支付开设的虚拟账户
    String managerRateForPartyCFinance; //积木盒子：融资服务费
    String managerRateForPartyCTechnology; //积木盒子：技术服务费
    String rateEM;//贷款月利率
    Long time;//贷款期限
    String MonthlyRepayment;//月偿还本息数额

    String repaymentWay;//华澳信托:还款方式
    String huaAoThirdPartyId;//华澳信托在畅捷支付开设的虚拟账户

    String firstLegalRepresentative;
    String firstAddress;
    String firstPostcode;
    String thirdLegalRepresentative;
    String thirdAddress;
    String thirdPostcode;

    String mphone;//国民信托：乙方对应的：手机号码
    String peiOuName;//国民信托：乙方对应的：配偶姓名
    String repaymentTotal;//国民信托：需偿还的贷款本息总额
    String loanType;//国民信托：贷款类型

    String corpName;//国民信托、华澳信托 公司名称
    String corpBankName;//国民信托和华澳信托 开户银行
    String corpBankAccount;//国民信托和华澳信托 公司账号
    
    String grantMoney;//放款金额
	String accrualem ;//补偿利率
	String rateey;//年化利率
	String rate;//借款费率
    public String getAccrualem() {
		return accrualem;
	}
	public void setAccrualem(String accrualem) {
		this.accrualem = accrualem;
	}
	public String getPactMoney() {
		return pactMoney;
	}
	public void setPactMoney(String pactMoney) {
		this.pactMoney = pactMoney;
	}
	public String getThirdPartyId() {
		return thirdPartyId;
	}
	public void setThirdPartyId(String thirdPartyId) {
		this.thirdPartyId = thirdPartyId;
	}
	public String getManagerRateForPartyCFinance() {
		return managerRateForPartyCFinance;
	}
	public void setManagerRateForPartyCFinance(String managerRateForPartyCFinance) {
		this.managerRateForPartyCFinance = managerRateForPartyCFinance;
	}
	public String getManagerRateForPartyCTechnology() {
		return managerRateForPartyCTechnology;
	}
	public void setManagerRateForPartyCTechnology(String managerRateForPartyCTechnology) {
		this.managerRateForPartyCTechnology = managerRateForPartyCTechnology;
	}
	public String getRateEM() {
		return rateEM;
	}
	public void setRateEM(String rateEM) {
		this.rateEM = rateEM;
	}
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	public String getMonthlyRepayment() {
		return MonthlyRepayment;
	}
	public void setMonthlyRepayment(String monthlyRepayment) {
		MonthlyRepayment = monthlyRepayment;
	}
	public String getRepaymentWay() {
		return repaymentWay;
	}
	public void setRepaymentWay(String repaymentWay) {
		this.repaymentWay = repaymentWay;
	}
	public String getHuaAoThirdPartyId() {
		return huaAoThirdPartyId;
	}
	public void setHuaAoThirdPartyId(String huaAoThirdPartyId) {
		this.huaAoThirdPartyId = huaAoThirdPartyId;
	}
	public String getFirstLegalRepresentative() {
		return firstLegalRepresentative;
	}
	public void setFirstLegalRepresentative(String firstLegalRepresentative) {
		this.firstLegalRepresentative = firstLegalRepresentative;
	}
	public String getFirstAddress() {
		return firstAddress;
	}
	public void setFirstAddress(String firstAddress) {
		this.firstAddress = firstAddress;
	}
	public String getFirstPostcode() {
		return firstPostcode;
	}
	public void setFirstPostcode(String firstPostcode) {
		this.firstPostcode = firstPostcode;
	}
	public String getThirdLegalRepresentative() {
		return thirdLegalRepresentative;
	}
	public void setThirdLegalRepresentative(String thirdLegalRepresentative) {
		this.thirdLegalRepresentative = thirdLegalRepresentative;
	}
	public String getThirdAddress() {
		return thirdAddress;
	}
	public void setThirdAddress(String thirdAddress) {
		this.thirdAddress = thirdAddress;
	}
	public String getThirdPostcode() {
		return thirdPostcode;
	}
	public void setThirdPostcode(String thirdPostcode) {
		this.thirdPostcode = thirdPostcode;
	}
	public String getMphone() {
		return mphone;
	}
	public void setMphone(String mphone) {
		this.mphone = mphone;
	}
	public String getPeiOuName() {
		return peiOuName;
	}
	public void setPeiOuName(String peiOuName) {
		this.peiOuName = peiOuName;
	}
	public String getRepaymentTotal() {
		return repaymentTotal;
	}
	public void setRepaymentTotal(String repaymentTotal) {
		this.repaymentTotal = repaymentTotal;
	}
	public String getLoanType() {
		return loanType;
	}
	public void setLoanType(String loanType) {
		this.loanType = loanType;
	}
	public String getCorpName() {
		return corpName;
	}
	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}
	public String getCorpBankName() {
		return corpBankName;
	}
	public void setCorpBankName(String corpBankName) {
		this.corpBankName = corpBankName;
	}
	public String getCorpBankAccount() {
		return corpBankAccount;
	}
	public void setCorpBankAccount(String corpBankAccount) {
		this.corpBankAccount = corpBankAccount;
	}
	public String getGrantMoney() {
		return grantMoney;
	}
	public void setGrantMoney(String grantMoney) {
		this.grantMoney = grantMoney;
	}
	public String getRateey() {
		return rateey;
	}
	public void setRateey(String rateey) {
		this.rateey = rateey;
	}
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
	
}
