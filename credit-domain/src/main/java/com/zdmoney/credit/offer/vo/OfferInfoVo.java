package com.zdmoney.credit.offer.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.zdmoney.credit.offer.domain.OfferInfo;

public class OfferInfoVo extends OfferInfo {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//合同编号
	private String contractNum;

	public String getContractNum() {
		return contractNum;
	}

	public void setContractNum(String contractNum) {
		this.contractNum = contractNum;
	}
	
	
	
}
