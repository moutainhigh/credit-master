package com.zdmoney.credit.offer.domain;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.zdmoney.credit.framework.domain.BaseDomain;
/**
 * 报盘信息表
 * @author 00232949
 */
public class OfferInfo extends BaseDomain{
    
    private static final long serialVersionUID = -8678925468848029790L;

    private Long id;

    private String acctType;

    private BigDecimal amount;

    private BigDecimal offerAmount;

    private BigDecimal actualAmount;

    private String bankAcct;

    private String bankCode;

    private String bankName;

    private String cause;

    private String currencyType;

    private String custId;

    private String idnum;

    private Long loanId;

    private String memo;

    private String name;

    private String offerNo;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date offerDate;

    private Long offerFileId;

    private String organ;

    private String protocol;

    private String protocolCust;

    private String returnCode;

    private Long returnFileId;

    private String state;

    private String tel;

    private String teller;

    private String tppType;

    private String type;

    private Integer priority;
    
    private String tppSysNum;
    
    private String infoCategoryCode;
    
    private String bizSysNo;
    
    private String paySysNo;
    
    private String spt1;
    
    private String spt2;
    
    private String offerRange;
    
    private OfferTransaction offerTransaction;
    
    private String oldState;
    
    private String rtnInfo;
    
    private String paySysNoReal;
    
    private String isThird;
    
    public String getPaySysNoReal() {
		return paySysNoReal;
	}

	public void setPaySysNoReal(String paySysNoReal) {
		this.paySysNoReal = paySysNoReal;
	}

	public String getOfferRange() {
		return offerRange;
	}

	public void setOfferRange(String offerRange) {
		this.offerRange = offerRange;
	}

	public String getRtnInfo() {
		return rtnInfo;
	}

	public void setRtnInfo(String rtnInfo) {
		this.rtnInfo = rtnInfo;
	}

	/**
     * 回盘导入日期
     */
    private Date updateTime;
    
    /**
     * 合同来源
     */
    private String fundsSources;
    
    /**
     * 回盘日期
     */
    private Date rdoTime;
    
    public String getOldState() {
        return oldState;
    }

    public void setOldState(String oldState) {
        this.oldState = oldState;
    }

    public String getTppSysNum() {
        return tppSysNum;
    }

    public void setTppSysNum(String tppSysNum) {
        this.tppSysNum = tppSysNum;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAcctType() {
        return acctType;
    }

    public void setAcctType(String acctType) {
        this.acctType = acctType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getOfferAmount() {
        return offerAmount;
    }

    public void setOfferAmount(BigDecimal offerAmount) {
        this.offerAmount = offerAmount;
    }

    public BigDecimal getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(BigDecimal actualAmount) {
        this.actualAmount = actualAmount;
    }

    public String getBankAcct() {
        return bankAcct;
    }

    public void setBankAcct(String bankAcct) {
        this.bankAcct = bankAcct;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getIdnum() {
        return idnum;
    }

    public void setIdnum(String idnum) {
        this.idnum = idnum;
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOfferNo() {
        return offerNo;
    }

    public void setOfferNo(String offerNo) {
        this.offerNo = offerNo;
    }

    public Date getOfferDate() {
        return offerDate;
    }

    public void setOfferDate(Date offerDate) {
        this.offerDate = offerDate;
    }

    public Long getOfferFileId() {
        return offerFileId;
    }

    public void setOfferFileId(Long offerFileId) {
        this.offerFileId = offerFileId;
    }

    public String getOrgan() {
        return organ;
    }

    public void setOrgan(String organ) {
        this.organ = organ;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getProtocolCust() {
        return protocolCust;
    }

    public void setProtocolCust(String protocolCust) {
        this.protocolCust = protocolCust;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public Long getReturnFileId() {
        return returnFileId;
    }

    public void setReturnFileId(Long returnFileId) {
        this.returnFileId = returnFileId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getTeller() {
        return teller;
    }

    public void setTeller(String teller) {
        this.teller = teller;
    }

    public String getTppType() {
        return tppType;
    }

    public void setTppType(String tppType) {
        this.tppType = tppType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        
        this.priority = priority;
    }

    public OfferTransaction getOfferTransaction() {
        return offerTransaction;
    }

    public void setOfferTransaction(OfferTransaction offerTransaction) {
        this.offerTransaction = offerTransaction;
    }

    public String getInfoCategoryCode() {
        return infoCategoryCode;
    }

    public void setInfoCategoryCode(String infoCategoryCode) {
        this.infoCategoryCode = infoCategoryCode;
    }

    public String getBizSysNo() {
        return bizSysNo;
    }

    public void setBizSysNo(String bizSysNo) {
        this.bizSysNo = bizSysNo;
    }

    public String getPaySysNo() {
        return paySysNo;
    }

    public void setPaySysNo(String paySysNo) {
        this.paySysNo = paySysNo;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getFundsSources() {
        return fundsSources;
    }

    public void setFundsSources(String fundsSources) {
        this.fundsSources = fundsSources;
    }

    public String getSpt1() {
        return spt1;
    }

    public void setSpt1(String spt1) {
        this.spt1 = spt1;
    }

    public String getSpt2() {
        return spt2;
    }

    public void setSpt2(String spt2) {
        this.spt2 = spt2;
    }

    public Date getRdoTime() {
        return rdoTime;
    }

    public void setRdoTime(Date rdoTime) {
        this.rdoTime = rdoTime;
    }

	public String getIsThird() {
		return isThird;
	}

	public void setIsThird(String isThird) {
		this.isThird = isThird;
	}
    
}