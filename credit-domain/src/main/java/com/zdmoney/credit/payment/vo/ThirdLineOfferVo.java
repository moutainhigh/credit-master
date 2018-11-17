package com.zdmoney.credit.payment.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.zdmoney.credit.payment.domain.ThirdLineOffer;


/**
 *  第三方线下代付 导出报盘文件
 * @author 00235528
 *
 */
public class ThirdLineOfferVo extends ThirdLineOffer{
	
	private String tradeMark ;
	
	private Date commitTime ;
	
	private int recordsTotal;
	
	private BigDecimal amountTotal ;
	
	private String businessType;
	 
	private String fileName;
	
	private String dayBatchNumber;
	
	private String exportFile;
	
	private String version;

	public String getTradeMark() {
		return tradeMark;
	}

	public void setTradeMark(String tradeMark) {
		this.tradeMark = tradeMark;
	}

	public Date getCommitTime() {
		return commitTime;
	}

	public void setCommitTime(Date commitTime) {
		this.commitTime = commitTime;
	}

	public int getRecordsTotal() {
		return recordsTotal;
	}

	public void setRecordsTotal(int recordsTotal) {
		this.recordsTotal = recordsTotal;
	}

	public BigDecimal getAmountTotal() {
		return amountTotal;
	}

	public void setAmountTotal(BigDecimal amountTotal) {
		this.amountTotal = amountTotal;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getDayBatchNumber() {
		return dayBatchNumber;
	}

	public void setDayBatchNumber(String dayBatchNumber) {
		this.dayBatchNumber = dayBatchNumber;
	}

	public String getExportFile() {
		return exportFile;
	}

	public void setExportFile(String exportFile) {
		this.exportFile = exportFile;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
    
}
