package com.zdmoney.credit.offer.domain;


import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

/**
 * 报盘银行字典表
 * @author 00232949
 *
 */
public class OfferBankDic extends BaseDomain{
    /**
	 * 
	 */
	private static final long serialVersionUID = -6039845226630989844L;

	private Long id;

	private String code;

    private String name;

    private String tppBankCode;

    private String tppType;
    
    private String paySysNo;

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getTppBankCode() {
        return tppBankCode;
    }

    public void setTppBankCode(String tppBankCode) {
        this.tppBankCode = tppBankCode == null ? null : tppBankCode.trim();
    }

    public String getTppType() {
        return tppType;
    }

    public void setTppType(String tppType) {
        this.tppType = tppType == null ? null : tppType.trim();
    }

	public String getPaySysNo() {
		return paySysNo;
	}

	public void setPaySysNo(String paySysNo) {
		this.paySysNo = paySysNo;
	}
}