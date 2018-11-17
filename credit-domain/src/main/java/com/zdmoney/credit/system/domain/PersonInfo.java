package com.zdmoney.credit.system.domain;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.zdmoney.credit.framework.domain.BaseDomain;
import com.zdmoney.credit.person.domain.PersonCarInfo;
import com.zdmoney.credit.person.domain.PersonContactInfo;
import com.zdmoney.credit.person.domain.PersonEntrepreneurInfo;
import com.zdmoney.credit.person.domain.PersonHouseInfo;

/**
 * 借款人信息
 * @author 00232949
 *
 */
public class PersonInfo extends BaseDomain {
    /**
	 * 
	 */
	private static final long serialVersionUID = 858658306781045296L;

	private Long id;

    private String address;

    private Date birthday;

    private String cAddress;

    @DateTimeFormat(pattern="yyyy-MM-dd")  
    private Date cEnterTime;

    private String cTel;

    private String cType;

    private String company;

    private String department;

    private String duty;

    private String edLevel;

    private String email;

    private String fax;

    private String idnum;

    private String idtype;

    private String industryType;

    private String married;

    private String memo;

    private String mphone;

    private String name;

    private String postcode;

    private String sex;

    private String tel;

    private String workertype;

    private Integer childrenCount;

    private String houseType;

    private Integer livePersonCount;

    private String liveWith;

    private Integer rent;

    private String profession;

    private String cpamount;

    private String englishName;

    private Date idEndDate;

    private String idSource;

    private Date idStartDate;

    private String language;

    private String nationality;

    private Integer workYear;

    private String mothersName;

    private String financingComeFrom;

    private String investYears;

    private String investmentLore;

    private String pCode;

    private Long recorderId;

    private String realEstateLicenseCode;

    private Long salesDepartmentId;

    private Date createdDate;

    private String familyBackground;

    private String msn;

    private String qq;

    private String hasCreditCard;

    private String areaCode;

    private String cCode;

    private String comeFrom;

    private String domicilePlace;

    private String hrAddress;

    private String hrPostcode;

    private String otherComeFrom;

    private BigDecimal otherMonthlyIncome;

    private Integer payDay;

    private BigDecimal totalMonthlyIncome;

    private BigDecimal wage;

    private BigDecimal cardDeficit;

    private BigDecimal cardTotalAmount;

    private Integer creditCardCount;

    private Integer numberDependanted;

    private String addressPriority;

    private BigDecimal familyExpense;

    private String officialRank;

    private String payType;

    private BigDecimal cardMaxAmount;

    private String hasLoan;

    private Date dateCreated;

    private Date lastUpdated;

    private Long signSalesDepId;

    private String cCity;

    private Long thirdPartyAccountId;
    /** 营业网点信息 **/
    private ComOrganization comOrganization;
    /** 联系人信息 **/
    private PersonContactInfo personContactInfo;
    /** 车辆信息 **/
    private PersonCarInfo personCarInfo;
    /** 房屋信息 **/
    private PersonHouseInfo personHouseInfo;
    /** 企业信息 **/
    private PersonEntrepreneurInfo personEntrepreneurInfo;

    private String loanType;
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getcAddress() {
		return cAddress;
	}

	public void setcAddress(String cAddress) {
		this.cAddress = cAddress;
	}

	public Date getcEnterTime() {
		return cEnterTime;
	}

	public void setcEnterTime(Date cEnterTime) {
		this.cEnterTime = cEnterTime;
	}

	public String getcTel() {
		return cTel;
	}

	public void setcTel(String cTel) {
		this.cTel = cTel;
	}

	public String getcType() {
		return cType;
	}

	public void setcType(String cType) {
		this.cType = cType;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getDuty() {
		return duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
	}

	public String getEdLevel() {
		return edLevel;
	}

	public void setEdLevel(String edLevel) {
		this.edLevel = edLevel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getIdnum() {
		return idnum;
	}

	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}

	public String getIdtype() {
		return idtype;
	}

	public void setIdtype(String idtype) {
		this.idtype = idtype;
	}

	public String getIndustryType() {
		return industryType;
	}

	public void setIndustryType(String industryType) {
		this.industryType = industryType;
	}

	public String getMarried() {
		return married;
	}

	public void setMarried(String married) {
		this.married = married;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getMphone() {
		return mphone;
	}

	public void setMphone(String mphone) {
		this.mphone = mphone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getWorkertype() {
		return workertype;
	}

	public void setWorkertype(String workertype) {
		this.workertype = workertype;
	}

	public Integer getChildrenCount() {
		return childrenCount;
	}

	public void setChildrenCount(Integer childrenCount) {
		this.childrenCount = childrenCount;
	}

	public String getHouseType() {
		return houseType;
	}

	public void setHouseType(String houseType) {
		this.houseType = houseType;
	}

	public Integer getLivePersonCount() {
		return livePersonCount;
	}

	public void setLivePersonCount(Integer livePersonCount) {
		this.livePersonCount = livePersonCount;
	}

	public String getLiveWith() {
		return liveWith;
	}

	public void setLiveWith(String liveWith) {
		this.liveWith = liveWith;
	}

	public Integer getRent() {
		return rent;
	}

	public void setRent(Integer rent) {
		this.rent = rent;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}

	public String getCpamount() {
		return cpamount;
	}

	public void setCpamount(String cpamount) {
		this.cpamount = cpamount;
	}

	public String getEnglishName() {
		return englishName;
	}

	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}

	public Date getIdEndDate() {
		return idEndDate;
	}

	public void setIdEndDate(Date idEndDate) {
		this.idEndDate = idEndDate;
	}

	public String getIdSource() {
		return idSource;
	}

	public void setIdSource(String idSource) {
		this.idSource = idSource;
	}

	public Date getIdStartDate() {
		return idStartDate;
	}

	public void setIdStartDate(Date idStartDate) {
		this.idStartDate = idStartDate;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public Integer getWorkYear() {
		return workYear;
	}

	public void setWorkYear(Integer workYear) {
		this.workYear = workYear;
	}

	public String getMothersName() {
		return mothersName;
	}

	public void setMothersName(String mothersName) {
		this.mothersName = mothersName;
	}

	public String getFinancingComeFrom() {
		return financingComeFrom;
	}

	public void setFinancingComeFrom(String financingComeFrom) {
		this.financingComeFrom = financingComeFrom;
	}

	public String getInvestYears() {
		return investYears;
	}

	public void setInvestYears(String investYears) {
		this.investYears = investYears;
	}

	public String getInvestmentLore() {
		return investmentLore;
	}

	public void setInvestmentLore(String investmentLore) {
		this.investmentLore = investmentLore;
	}

	public String getpCode() {
		return pCode;
	}

	public void setpCode(String pCode) {
		this.pCode = pCode;
	}

	public Long getRecorderId() {
		return recorderId;
	}

	public void setRecorderId(Long recorderId) {
		this.recorderId = recorderId;
	}

	public String getRealEstateLicenseCode() {
		return realEstateLicenseCode;
	}

	public void setRealEstateLicenseCode(String realEstateLicenseCode) {
		this.realEstateLicenseCode = realEstateLicenseCode;
	}

	public Long getSalesDepartmentId() {
		return salesDepartmentId;
	}

	public void setSalesDepartmentId(Long salesDepartmentId) {
		this.salesDepartmentId = salesDepartmentId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getFamilyBackground() {
		return familyBackground;
	}

	public void setFamilyBackground(String familyBackground) {
		this.familyBackground = familyBackground;
	}

	public String getMsn() {
		return msn;
	}

	public void setMsn(String msn) {
		this.msn = msn;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getHasCreditCard() {
		return hasCreditCard;
	}

	public void setHasCreditCard(String hasCreditCard) {
		this.hasCreditCard = hasCreditCard;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getcCode() {
		return cCode;
	}

	public void setcCode(String cCode) {
		this.cCode = cCode;
	}

	public String getComeFrom() {
		return comeFrom;
	}

	public void setComeFrom(String comeFrom) {
		this.comeFrom = comeFrom;
	}

	public String getDomicilePlace() {
		return domicilePlace;
	}

	public void setDomicilePlace(String domicilePlace) {
		this.domicilePlace = domicilePlace;
	}

	public String getHrAddress() {
		return hrAddress;
	}

	public void setHrAddress(String hrAddress) {
		this.hrAddress = hrAddress;
	}

	public String getHrPostcode() {
		return hrPostcode;
	}

	public void setHrPostcode(String hrPostcode) {
		this.hrPostcode = hrPostcode;
	}

	public String getOtherComeFrom() {
		return otherComeFrom;
	}

	public void setOtherComeFrom(String otherComeFrom) {
		this.otherComeFrom = otherComeFrom;
	}

	public BigDecimal getOtherMonthlyIncome() {
		return otherMonthlyIncome;
	}

	public void setOtherMonthlyIncome(BigDecimal otherMonthlyIncome) {
		this.otherMonthlyIncome = otherMonthlyIncome;
	}

	public Integer getPayDay() {
		return payDay;
	}

	public void setPayDay(Integer payDay) {
		this.payDay = payDay;
	}

	public BigDecimal getTotalMonthlyIncome() {
		return totalMonthlyIncome;
	}

	public void setTotalMonthlyIncome(BigDecimal totalMonthlyIncome) {
		this.totalMonthlyIncome = totalMonthlyIncome;
	}

	public BigDecimal getWage() {
		return wage;
	}

	public void setWage(BigDecimal wage) {
		this.wage = wage;
	}

	public BigDecimal getCardDeficit() {
		return cardDeficit;
	}

	public void setCardDeficit(BigDecimal cardDeficit) {
		this.cardDeficit = cardDeficit;
	}

	public BigDecimal getCardTotalAmount() {
		return cardTotalAmount;
	}

	public void setCardTotalAmount(BigDecimal cardTotalAmount) {
		this.cardTotalAmount = cardTotalAmount;
	}

	public Integer getCreditCardCount() {
		return creditCardCount;
	}

	public void setCreditCardCount(Integer creditCardCount) {
		this.creditCardCount = creditCardCount;
	}

	public Integer getNumberDependanted() {
		return numberDependanted;
	}

	public void setNumberDependanted(Integer numberDependanted) {
		this.numberDependanted = numberDependanted;
	}

	public String getAddressPriority() {
		return addressPriority;
	}

	public void setAddressPriority(String addressPriority) {
		this.addressPriority = addressPriority;
	}

	public BigDecimal getFamilyExpense() {
		return familyExpense;
	}

	public void setFamilyExpense(BigDecimal familyExpense) {
		this.familyExpense = familyExpense;
	}

	public String getOfficialRank() {
		return officialRank;
	}

	public void setOfficialRank(String officialRank) {
		this.officialRank = officialRank;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public BigDecimal getCardMaxAmount() {
		return cardMaxAmount;
	}

	public void setCardMaxAmount(BigDecimal cardMaxAmount) {
		this.cardMaxAmount = cardMaxAmount;
	}

	public String getHasLoan() {
		return hasLoan;
	}

	public void setHasLoan(String hasLoan) {
		this.hasLoan = hasLoan;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public Long getSignSalesDepId() {
		return signSalesDepId;
	}

	public void setSignSalesDepId(Long signSalesDepId) {
		this.signSalesDepId = signSalesDepId;
	}

	public String getcCity() {
		return cCity;
	}

	public void setcCity(String cCity) {
		this.cCity = cCity;
	}

	public Long getThirdPartyAccountId() {
		return thirdPartyAccountId;
	}

	public void setThirdPartyAccountId(Long thirdPartyAccountId) {
		this.thirdPartyAccountId = thirdPartyAccountId;
	}

	public ComOrganization getComOrganization() {
		return comOrganization;
	}

	public void setComOrganization(ComOrganization comOrganization) {
		this.comOrganization = comOrganization;
	}

	public PersonContactInfo getPersonContactInfo() {
		return personContactInfo;
	}

	public void setPersonContactInfo(PersonContactInfo personContactInfo) {
		this.personContactInfo = personContactInfo;
	}

	public PersonCarInfo getPersonCarInfo() {
		return personCarInfo;
	}

	public void setPersonCarInfo(PersonCarInfo personCarInfo) {
		this.personCarInfo = personCarInfo;
	}

	public PersonHouseInfo getPersonHouseInfo() {
		return personHouseInfo;
	}

	public void setPersonHouseInfo(PersonHouseInfo personHouseInfo) {
		this.personHouseInfo = personHouseInfo;
	}

	public PersonEntrepreneurInfo getPersonEntrepreneurInfo() {
		return personEntrepreneurInfo;
	}

	public void setPersonEntrepreneurInfo(
			PersonEntrepreneurInfo personEntrepreneurInfo) {
		this.personEntrepreneurInfo = personEntrepreneurInfo;
	}

   
}