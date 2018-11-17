package com.zdmoney.credit.loan.vo;

import com.zdmoney.credit.loan.domain.LoanPutOnRecordExport;

/**
 * 备案导出 vo
 * Created by  on 2016/9/26.
 */
public class LoanPutOnRecordExportVo extends LoanPutOnRecordExport {
    /**
     * 协议签约时间查询开始时间
     */
    private  String beginSignDate;
    /**
     * 协议签约时间查询结束时间
     */
    private String endSignDate;
    /**
     * 理财机构
     */
    private String financialorg;
    /**
     * 银行所属地区
     */
    private String regionType;
    /**
     * 合同来源
     */
    private String fundsSources;
   
    public String getBeginSignDate() {
        return beginSignDate;
    }

    public void setBeginSignDate(String beginSignDate) {
        this.beginSignDate = beginSignDate;
    }

    public String getEndSignDate() {
        return endSignDate;
    }

    public void setEndSignDate(String endSignDate) {
        this.endSignDate = endSignDate;
    }

    public String getFinancialorg() {
        return financialorg;
    }

    public void setFinancialorg(String financialorg) {
        this.financialorg = financialorg;
    }

    public String getRegionType() {
        return regionType;
    }

    public void setRegionType(String regionType) {
        this.regionType = regionType;
    }

    public String getFundsSources() {
        return fundsSources;
    }

    public void setFundsSources(String fundsSources) {
        this.fundsSources = fundsSources;
    }

	
}
