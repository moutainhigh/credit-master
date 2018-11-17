package com.zdmoney.credit.loan.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;
import com.zdmoney.credit.system.domain.ComEmployee;
import com.zdmoney.credit.system.domain.ComOrganization;
import com.zdmoney.credit.system.domain.PersonInfo;

/**
 * RepaymentLevelJob   还款等级出理job Vo
 *
 */
public class RepaymentLevelLoan extends BaseDomain{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

    private Long promiseReturnDate;
    
    private String idNum;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPromiseReturnDate() {
		return promiseReturnDate;
	}

	public void setPromiseReturnDate(Long promiseReturnDate) {
		this.promiseReturnDate = promiseReturnDate;
	}

    public String getIdNum() {
        return idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }
  
}