package com.zdmoney.credit.offer.domain;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class OffLineOfferBankDic extends BaseDomain {

    private static final long serialVersionUID = 5266702780147057052L;

    private Long id;

    private String code;

    private String name;

    private String bankType;

    private String bankNo;

    private String regionType;

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

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType == null ? null : bankType.trim();
    }

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo == null ? null : bankNo.trim();
    }

    public String getRegionType() {
        return regionType;
    }

    public void setRegionType(String regionType) {
        this.regionType = regionType == null ? null : regionType.trim();
    }
}