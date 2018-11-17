package com.zdmoney.credit.operation.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class BasePublicAccountInfoWm extends BaseDomain {
    private Long id;

    private Long loanId;

    private Date tradeDate;

    private String tradeTime;

    private BigDecimal tradeAmount;

    private String secondAccount;

    private String secondName;

    private String tradeBank;

    private String tradeType;

    private String tradeChannel;

    private String tradePurpose;

    private String tradeDesc;

    private String tradeRemark;

    private Long operatorId;

    private Long recOperatorId;

    private Date recTime;

    private String status;

    private String memo;

    private String creator;

    private Date createTime;

    private String updator;

    private Date updateTime;

    private Date breathDate;
    
    private String serialNumber;
    //内部流水号
    private String repayNo;
    //债权去向
    private String loanBelong;
    //----------------------- 
    // 以下是非数据库字段
    //-----------------------
    /**
     * 交易日期起
     */
    private Date tradeDateStart;
    
    /**
     * 交易日期止
     */
    private Date tradeDateEnd;
    
    /**
     * 认领日期(起)
     */
    private Date recTimeStart;
    
    /**
     * 认领日期(止)
     */
    private Date recTimeEnd;
    
    /**
     * 认领人工号
     */
    private String recUsercode;
    /**
     * 解锁状态 0-锁定 1-解锁
     */
    private String lockStatus;
    
    /**
     * 合同来源
     */
    private String fundsSources;
    
    /**
     * 通道来源
     */
    private String channelSources;

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

	public String getRecUsercode() {
		return recUsercode;
	}

	public void setRecUsercode(String recUsercode) {
		this.recUsercode = recUsercode;
	}

	public Date getTradeDateStart() {
		return tradeDateStart;
	}

	public void setTradeDateStart(Date tradeDateStart) {
		this.tradeDateStart = tradeDateStart;
	}

	public Date getTradeDateEnd() {
		return tradeDateEnd;
	}

	public void setTradeDateEnd(Date tradeDateEnd) {
		this.tradeDateEnd = tradeDateEnd;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Date getBreathDate() {
		return breathDate;
	}

	public void setBreathDate(Date breathDate) {
		this.breathDate = breathDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getLoanId() {
		return loanId;
	}

	public void setLoanId(Long loanId) {
		this.loanId = loanId;
	}

	public Date getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(Date tradeDate) {
        this.tradeDate = tradeDate;
    }

    public String getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(String tradeTime) {
        this.tradeTime = tradeTime == null ? null : tradeTime.trim();
    }

    public BigDecimal getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(BigDecimal tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public String getSecondAccount() {
        return secondAccount;
    }

    public void setSecondAccount(String secondAccount) {
        this.secondAccount = secondAccount == null ? null : secondAccount.trim();
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName == null ? null : secondName.trim();
    }

    public String getTradeBank() {
        return tradeBank;
    }

    public void setTradeBank(String tradeBank) {
        this.tradeBank = tradeBank == null ? null : tradeBank.trim();
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType == null ? null : tradeType.trim();
    }

    public String getTradeChannel() {
        return tradeChannel;
    }

    public void setTradeChannel(String tradeChannel) {
        this.tradeChannel = tradeChannel == null ? null : tradeChannel.trim();
    }

    public String getTradePurpose() {
        return tradePurpose;
    }

    public void setTradePurpose(String tradePurpose) {
        this.tradePurpose = tradePurpose == null ? null : tradePurpose.trim();
    }

    public String getTradeDesc() {
        return tradeDesc;
    }

    public void setTradeDesc(String tradeDesc) {
        this.tradeDesc = tradeDesc == null ? null : tradeDesc.trim();
    }

    public String getTradeRemark() {
        return tradeRemark;
    }

    public void setTradeRemark(String tradeRemark) {
        this.tradeRemark = tradeRemark == null ? null : tradeRemark.trim();
    }



    public Long getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}

	public Long getRecOperatorId() {
		return recOperatorId;
	}

	public void setRecOperatorId(Long recOperatorId) {
		this.recOperatorId = recOperatorId;
	}

	public Date getRecTime() {
        return recTime;
    }

    public void setRecTime(Date recTime) {
        this.recTime = recTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdator() {
        return updator;
    }

    public void setUpdator(String updator) {
        this.updator = updator == null ? null : updator.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

	public String getRepayNo() {
		return repayNo;
	}

	public void setRepayNo(String repayNo) {
		this.repayNo = repayNo;
	}

	public String getLoanBelong() {
		return loanBelong;
	}

	public void setLoanBelong(String loanBelong) {
		this.loanBelong = loanBelong;
	}

    public String getLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(String lockStatus) {
        this.lockStatus = lockStatus;
    }

	public String getFundsSources() {
		return fundsSources;
	}

	public void setFundsSources(String fundsSources) {
		this.fundsSources = fundsSources;
	}

	public String getChannelSources() {
		return channelSources;
	}

	public void setChannelSources(String channelSources) {
		this.channelSources = channelSources;
	}
    
}