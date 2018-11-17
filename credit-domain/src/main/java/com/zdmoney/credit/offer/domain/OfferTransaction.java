package com.zdmoney.credit.offer.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;
/**
 * 交易流水表
 * @author 00232949
 *
 */
public class OfferTransaction extends BaseDomain{
    /**
	 * 
	 */
	private static final long serialVersionUID = 5268574048435638479L;

	private Long id;

    private String trxCode;

    private String batchSerialNo;

    private String trxSerialNo;

    private Date reqTime;

    private String otrxSerialNo;

    private String otrxCode;

    private String trxState;

    private String rtnCode;

    private String rtnInfo;

    private Date rdoTime;

    private String reqSender;

    private String reqReceiver;

    private BigDecimal payAmount;

    private BigDecimal actualAmount;

    private String spt1;

    private String trxCallbackUrl;

    private Date rspReceiveTime;

    private String memo;

    private Date createTime;

    private Date updateTime;
    
    private Long offerId;

    private Long loanId;
    
    private Long reqLogId;
    
    private Long rspLogId;
    
    private String paySysNoReal;
    /**回盘商户**/
    private String merId;
    private String fundsSources;
    
    private String isThird;
    
    public String getFundsSources() {
		return fundsSources;
	}

	public void setFundsSources(String fundsSources) {
		this.fundsSources = fundsSources;
	}

	public String getMerId() {
		return merId;
	}

	public void setMerId(String merId) {
		this.merId = merId;
	}

	public String getPaySysNoReal() {
		return paySysNoReal;
	}

	public void setPaySysNoReal(String paySysNoReal) {
		this.paySysNoReal = paySysNoReal;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTrxCode() {
        return trxCode;
    }

    public void setTrxCode(String trxCode) {
        this.trxCode = trxCode == null ? null : trxCode.trim();
    }

    public String getBatchSerialNo() {
        return batchSerialNo;
    }

    public void setBatchSerialNo(String batchSerialNo) {
        this.batchSerialNo = batchSerialNo == null ? null : batchSerialNo.trim();
    }

    public String getTrxSerialNo() {
        return trxSerialNo;
    }

    public void setTrxSerialNo(String trxSerialNo) {
        this.trxSerialNo = trxSerialNo == null ? null : trxSerialNo.trim();
    }

    public Date getReqTime() {
        return reqTime;
    }

    public void setReqTime(Date reqTime) {
        this.reqTime = reqTime;
    }

    public String getOtrxSerialNo() {
        return otrxSerialNo;
    }

    public void setOtrxSerialNo(String otrxSerialNo) {
        this.otrxSerialNo = otrxSerialNo == null ? null : otrxSerialNo.trim();
    }

    public String getOtrxCode() {
        return otrxCode;
    }

    public void setOtrxCode(String otrxCode) {
        this.otrxCode = otrxCode == null ? null : otrxCode.trim();
    }

    public String getTrxState() {
        return trxState;
    }

    public void setTrxState(String trxState) {
        this.trxState = trxState == null ? null : trxState.trim();
    }

    public String getRtnCode() {
        return rtnCode;
    }

    public void setRtnCode(String rtnCode) {
        this.rtnCode = rtnCode == null ? null : rtnCode.trim();
    }

    public String getRtnInfo() {
        return rtnInfo;
    }

    public void setRtnInfo(String rtnInfo) {
        this.rtnInfo = rtnInfo == null ? null : rtnInfo.trim();
    }

    public Date getRdoTime() {
        return rdoTime;
    }

    public void setRdoTime(Date rdoTime) {
        this.rdoTime = rdoTime;
    }

    public String getReqSender() {
        return reqSender;
    }

    public void setReqSender(String reqSender) {
        this.reqSender = reqSender == null ? null : reqSender.trim();
    }

    public String getReqReceiver() {
        return reqReceiver;
    }

    public void setReqReceiver(String reqReceiver) {
        this.reqReceiver = reqReceiver == null ? null : reqReceiver.trim();
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public BigDecimal getActualAmount() {
		return actualAmount;
	}

	public void setActualAmount(BigDecimal actualAmount) {
		this.actualAmount = actualAmount;
	}

	public String getSpt1() {
        return spt1;
    }

    public void setSpt1(String spt1) {
        this.spt1 = spt1 == null ? null : spt1.trim();
    }

    public String getTrxCallbackUrl() {
        return trxCallbackUrl;
    }

    public void setTrxCallbackUrl(String trxCallbackUrl) {
        this.trxCallbackUrl = trxCallbackUrl == null ? null : trxCallbackUrl.trim();
    }

    public Date getRspReceiveTime() {
        return rspReceiveTime;
    }

    public void setRspReceiveTime(Date rspReceiveTime) {
        this.rspReceiveTime = rspReceiveTime;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


	public Long getOfferId() {
		return offerId;
	}

	public void setOfferId(Long offerId) {
		this.offerId = offerId;
	}

	public Long getLoanId() {
		return loanId;
	}

	public void setLoanId(Long loanId) {
		this.loanId = loanId;
	}

	public Long getReqLogId() {
		return reqLogId;
	}

	public void setReqLogId(Long reqLogId) {
		this.reqLogId = reqLogId;
	}

	public Long getRspLogId() {
		return rspLogId;
	}

	public void setRspLogId(Long rspLogId) {
		this.rspLogId = rspLogId;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getIsThird() {
		return isThird;
	}

	public void setIsThird(String isThird) {
		this.isThird = isThird;
	}
	
}