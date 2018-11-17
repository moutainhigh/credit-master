package com.zdmoney.credit.loan.vo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;


/**
 * 债权导出供（爱特）
 * @author Anfq
 *
 * 2015年9月16日
 */
public class VeloanExport {
	public String   id;
	public String contract_num;
	public String loan_type;
	public String aa;
	public String purpose;
	public BigDecimal pact_money;
	public BigDecimal approve_money;
	public BigDecimal rate_sum;
	public BigDecimal refer_rate;
	public BigDecimal eval_rate;
	public BigDecimal manage_rate;
	public BigDecimal manager_rate_for_partyc;
	public BigDecimal risk;
	public BigDecimal time;
	public BigDecimal rateEM;
	public BigDecimal give_back_rate_for3term;
	public BigDecimal give_back_rate_for4term;
	public BigDecimal give_back_rate_after4term;
	public BigDecimal overdue_penalty1day;
	public BigDecimal overdue_penalty15day;
	public Date startrdate;
	public String service_tel;
	public String address;
	public String postcode;
	public String signing_site;
	public String funds_sources;
	public String bb;
	public BigDecimal rate;
	public String cc;
	public String dd;
	public String acct_name;
	public String borrower_sex;
	public String borrower_idnum;
	public String borrower_email;
	public String borrower_mphone;
	public String borrower_edLevel;
	public String signSalesDep_fullName;
	public String borrower_name;
	public String giveBackBank_bankName;
	public String giveBackBank_fullName;
	public String giveBackBank_account;
	public BigDecimal accrualem;
	public String backTerm;
	public String batchNum;
	public String grant_money_date;
	public BigDecimal age;
	public String married;
	public String c_type;
	public String c_enter_time;
	public String official_rank;
	public String total_monthly_income;
	public String car_type;
	public BigDecimal price;
	public String house_type;
	public BigDecimal building_area;
	public String enterprise_type;
	public String time_founded;	
	public String shareholding_ratio;
	/**
	 * 营业部
	 */
	public String name;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getContract_num() {
		return contract_num;
	}
	public void setContract_num(String contract_num) {
		this.contract_num = contract_num;
	}
	public String getLoan_type() {
		return loan_type;
	}
	public void setLoan_type(String loan_type) {
		this.loan_type = loan_type;
	}
	public String getAa() {
		return aa;
	}
	public void setAa(String aa) {
		this.aa = aa;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public BigDecimal getPact_money() {
		return pact_money;
	}
	public void setPact_money(BigDecimal pact_money) {
		this.pact_money = pact_money;
	}
	public BigDecimal getApprove_money() {
		return approve_money.setScale(2,RoundingMode.HALF_UP);
	}
	public void setApprove_money(BigDecimal approve_money) {
		this.approve_money = approve_money;
	}
	public BigDecimal getRate_sum() {
		return rate_sum;
	}
	public void setRate_sum(BigDecimal rate_sum) {
		this.rate_sum = rate_sum;
	}
	public BigDecimal getRefer_rate() {
		return refer_rate;
	}
	public void setRefer_rate(BigDecimal refer_rate) {
		this.refer_rate = refer_rate;
	}
	public BigDecimal getEval_rate() {
		return eval_rate;
	}
	public void setEval_rate(BigDecimal eval_rate) {
		this.eval_rate = eval_rate;
	}
	public BigDecimal getManage_rate() {
		return manage_rate;
	}
	public void setManage_rate(BigDecimal manage_rate) {
		this.manage_rate = manage_rate;
	}
	public BigDecimal getManager_rate_for_partyc() {
		return manager_rate_for_partyc;
	}
	public void setManager_rate_for_partyc(BigDecimal manager_rate_for_partyc) {
		this.manager_rate_for_partyc = manager_rate_for_partyc;
	}
	public BigDecimal getRisk() {
		return risk;
	}
	public void setRisk(BigDecimal risk) {
		this.risk = risk;
	}
	public BigDecimal getTime() {
		return time;
	}
	public void setTime(BigDecimal time) {
		this.time = time;
	}
	public BigDecimal getRateEM() {
		return rateEM;
	}
	public void setRateEM(BigDecimal rateEM) {
		this.rateEM = rateEM;
	}
	
	
	
	
	public BigDecimal getGive_back_rate_for3term() {
		return give_back_rate_for3term;
	}
	public void setGive_back_rate_for3term(BigDecimal give_back_rate_for3term) {
		this.give_back_rate_for3term = give_back_rate_for3term;
	}
	public BigDecimal getGive_back_rate_for4term() {
		return give_back_rate_for4term;
	}
	public void setGive_back_rate_for4term(BigDecimal give_back_rate_for4term) {
		this.give_back_rate_for4term = give_back_rate_for4term;
	}
	public BigDecimal getGive_back_rate_after4term() {
		return give_back_rate_after4term;
	}
	public void setGive_back_rate_after4term(BigDecimal give_back_rate_after4term) {
		this.give_back_rate_after4term = give_back_rate_after4term;
	}
	public BigDecimal getOverdue_penalty1day() {
		return overdue_penalty1day;
	}
	public void setOverdue_penalty1day(BigDecimal overdue_penalty1day) {
		this.overdue_penalty1day = overdue_penalty1day;
	}
	public BigDecimal getOverdue_penalty15day() {
		return overdue_penalty15day;
	}
	public void setOverdue_penalty15day(BigDecimal overdue_penalty15day) {
		this.overdue_penalty15day = overdue_penalty15day;
	}
	public Date getStartrdate() {
		return startrdate;
	}
	public void setStartrdate(Date startrdate) {
		this.startrdate = startrdate;
	}
	public String getService_tel() {
		return service_tel;
	}
	public void setService_tel(String service_tel) {
		this.service_tel = service_tel;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	public String getSigning_site() {
		return signing_site;
	}
	public void setSigning_site(String signing_site) {
		this.signing_site = signing_site;
	}
	public String getFunds_sources() {
		return funds_sources;
	}
	public void setFunds_sources(String funds_sources) {
		this.funds_sources = funds_sources;
	}
	public String getBb() {
		return bb;
	}
	public void setBb(String bb) {
		this.bb = bb;
	}
	public BigDecimal getRate() {
		return rate;
	}
	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}
	public String getCc() {
		return cc;
	}
	public void setCc(String cc) {
		this.cc = cc;
	}
	public String getDd() {
		return dd;
	}
	public void setDd(String dd) {
		this.dd = dd;
	}
	public String getAcct_name() {
		return acct_name;
	}
	public void setAcct_name(String acct_name) {
		this.acct_name = acct_name;
	}
	public String getBorrower_sex() {
		return borrower_sex;
	}
	public void setBorrower_sex(String borrower_sex) {
		this.borrower_sex = borrower_sex;
	}
	public String getBorrower_idnum() {
		return borrower_idnum;
	}
	public void setBorrower_idnum(String borrower_idnum) {
		this.borrower_idnum = borrower_idnum;
	}
	public String getBorrower_email() {
		return borrower_email;
	}
	public void setBorrower_email(String borrower_email) {
		this.borrower_email = borrower_email;
	}
	public String getBorrower_mphone() {
		return borrower_mphone;
	}
	public void setBorrower_mphone(String borrower_mphone) {
		this.borrower_mphone = borrower_mphone;
	}
	public String getBorrower_edLevel() {
		return borrower_edLevel;
	}
	public void setBorrower_edLevel(String borrower_edLevel) {
		this.borrower_edLevel = borrower_edLevel;
	}
	public String getSignSalesDep_fullName() {
		return signSalesDep_fullName;
	}
	public void setSignSalesDep_fullName(String signSalesDep_fullName) {
		this.signSalesDep_fullName = signSalesDep_fullName;
	}
	public String getBorrower_name() {
		return borrower_name;
	}
	public void setBorrower_name(String borrower_name) {
		this.borrower_name = borrower_name;
	}
	public String getGiveBackBank_bankName() {
		return giveBackBank_bankName;
	}
	public void setGiveBackBank_bankName(String giveBackBank_bankName) {
		this.giveBackBank_bankName = giveBackBank_bankName;
	}
	public String getGiveBackBank_fullName() {
		return giveBackBank_fullName;
	}
	public void setGiveBackBank_fullName(String giveBackBank_fullName) {
		this.giveBackBank_fullName = giveBackBank_fullName;
	}
	public String getGiveBackBank_account() {
		return giveBackBank_account;
	}
	public void setGiveBackBank_account(String giveBackBank_account) {
		this.giveBackBank_account = giveBackBank_account;
	}
	public BigDecimal getAccrualem() {
		return accrualem;
	}
	public void setAccrualem(BigDecimal accrualem) {
		this.accrualem = accrualem;
	}
	public String getBackTerm() {
		return backTerm;
	}
	public void setBackTerm(String backTerm) {
		this.backTerm = backTerm;
	}
	public String getBatchNum() {
		return batchNum;
	}
	public void setBatchNum(String batchNum) {
		this.batchNum = batchNum;
	}
	public String getGrant_money_date() {
		return grant_money_date;
	}
	public void setGrant_money_date(String grant_money_date) {
		this.grant_money_date = grant_money_date;
	}
	public BigDecimal getAge() {
		return age;
	}
	public void setAge(BigDecimal age) {
		this.age = age;
	}
	public String getMarried() {
		return married;
	}
	public void setMarried(String married) {
		this.married = married;
	}
	public String getC_type() {
		return c_type;
	}
	public void setC_type(String c_type) {
		this.c_type = c_type;
	}
	public String getC_enter_time() {
		return c_enter_time;
	}
	public void setC_enter_time(String c_enter_time) {
		this.c_enter_time = c_enter_time;
	}
	public String getOfficial_rank() {
		return official_rank;
	}
	public void setOfficial_rank(String official_rank) {
		this.official_rank = official_rank;
	}
	public String getTotal_monthly_income() {
		return total_monthly_income;
	}
	public void setTotal_monthly_income(String total_monthly_income) {
		this.total_monthly_income = total_monthly_income;
	}
	public String getCar_type() {
		return car_type;
	}
	public void setCar_type(String car_type) {
		this.car_type = car_type;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public String getHouse_type() {
		return house_type;
	}
	public void setHouse_type(String house_type) {
		this.house_type = house_type;
	}
	public BigDecimal getBuilding_area() {
		return building_area;
	}
	public void setBuilding_area(BigDecimal building_area) {
		this.building_area = building_area;
	}
	public String getEnterprise_type() {
		return enterprise_type;
	}
	public void setEnterprise_type(String enterprise_type) {
		this.enterprise_type = enterprise_type;
	}
	public String getTime_founded() {
		return time_founded;
	}
	public void setTime_founded(String time_founded) {
		this.time_founded = time_founded;
	}
	public String getShareholding_ratio() {
		return shareholding_ratio;
	}
	public void setShareholding_ratio(String shareholding_ratio) {
		this.shareholding_ratio = shareholding_ratio;
	}
	
	
	
	
	
/*	"id" -> "借款ID"
	"contract_num" -> "合同编号"
	"loan_type" -> "产品类型"
	"aa" -> "借款标题"
	"purpose" -> "借款用途"
	"pact_money" -> "合同金额"
	"approve_money" -> "审批金额"
	"rate_sum" -> "服务费"
	"refer_rate" -> "乙方咨询费"
	"eval_rate" -> "乙方评估费"
	"manage_rate" -> "乙方管理费"
	"manager_rate_for_partyc" -> "丙方管理费"
	"risk" -> "风险金"
	"time" -> "借款期限"
	"rateEM" -> "月利率"
	"give_back_rate_for3term" -> "第三期退费"
	"give_back_rate_for4term" -> "第四期退费"
	"give_back_rate_after4term" -> "第四期之后退费"
	"overdue_penalty1day" -> "首次逾期1天罚息"
	"overdue_penalty15day" -> "首次逾期15天罚息"
	"startrdate" -> "首还款日期"
	"service_tel" -> "客服电话"
	"address" -> "现居住地址"
	"postcode" -> "邮政编码"
	"signing_site" -> "合同签署地"
	"funds_sources" -> "合同来源"
	"bb" -> "第三方债权id"
	"rate" -> "月综合费率"
	"cc" -> "是否加急"
	"dd" -> "用户昵称"
	"acct_name" -> "真实姓名"
	"borrower_sex" -> "性别"
	"borrower_idnum" -> "身份证号"
	"borrower_email" -> "邮箱"
	"borrower_mphone" -> "手机号码"
	"borrower_edLevel" -> "最高学历"
	"signSalesDep_fullName" -> "放款营业部"
	"borrower_name" -> "开户人姓名"
	"giveBackBank_bankName" -> "开户银行"
	"giveBackBank_fullName" -> "银行分支机构"
	"giveBackBank_account" -> "提现银行卡号"
	"accrualem" -> "补偿利率"
	"backTerm" -> "已还期数"
	"batchNum" -> "产品批次号"
	"grant_money_date" -> "放款日"
	"age" -> "年龄"
	"married" -> "婚姻状况"
	"c_type" -> "单位性质"
	"c_enter_time" -> "本工作开始日期"
	"official_rank" -> "职位"
	"total_monthly_income" -> "经核实月收入"
	"car_type" -> "车型"
	"price" -> "购买价格"
	"house_type" -> "房产类型"
	"building_area" -> "建筑面积"
	"enterprise_type" -> "经营主体类型"
	"time_founded" -> "成立年限"
	"shareholding_ratio" -> "融资人持股比例"*/
}
