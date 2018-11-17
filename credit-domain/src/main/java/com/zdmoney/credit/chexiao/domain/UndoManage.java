package com.zdmoney.credit.chexiao.domain;


import com.zdmoney.credit.framework.domain.BaseDomain;

public class UndoManage extends BaseDomain {
    
	private static final long serialVersionUID = 1L;

	
    private String contract_num;
    
	
	public String getContract_num() {
		return contract_num;
	}


	public void setContract_num(String contract_num) {
		this.contract_num = contract_num;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}