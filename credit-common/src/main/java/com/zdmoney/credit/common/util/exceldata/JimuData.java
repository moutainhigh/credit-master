package com.zdmoney.credit.common.util.exceldata;

import java.io.Serializable;


/**
 * 用于多张表联立过得的数据,参考JimuheziPaymentRisk10AMJob
 * Created by Administrator on 14-11-7.
 */
public class JimuData implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -7527340636352699522L;
	String name;
    String idnum;
    Long loanId;
    int currentTerm;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public int getCurrentTerm() {
		return currentTerm;
	}
	public void setCurrentTerm(int currentTerm) {
		this.currentTerm = currentTerm;
	}
    
    
}
