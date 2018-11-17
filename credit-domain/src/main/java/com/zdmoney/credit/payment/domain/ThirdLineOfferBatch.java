package com.zdmoney.credit.payment.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

/**
 * 第三方线下代付 报盘批次表
 * @author 00235528
 *
 */
public class ThirdLineOfferBatch extends BaseDomain {

    private static final long serialVersionUID = -8010611323390367493L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 交易标志
     */
    private String tradeMark;

    /**
     * 商户号
     */
    private String merchantId;

    /**
     * 提交日期
     */
    private Date commitTime;

    /**
     * 总记录数
     */
    private int recordsTotal;

    /**
     * 总金额
     */
    private BigDecimal amountTotal;
    /**
     * 业务类型
     */
    private String businessType;
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 当日批次号
     */
    private String dayBatchNumber;
    /**
     * 版本号
     */
    private String version;
    /**
     * 是否导出文件
     */
    private String exportFile;
    
    /**
     * 提交日期（YYYYMMDD）
     */
    private String submitDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTradeMark() {
        return tradeMark;
    }

    public void setTradeMark(String tradeMark) {
        this.tradeMark = tradeMark;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getDayBatchNumber() {
        return dayBatchNumber;
    }

    public void setDayBatchNumber(String dayBatchNumber) {
        this.dayBatchNumber = dayBatchNumber;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getExportFile() {
        return exportFile;
    }

    public void setExportFile(String exportFile) {
        this.exportFile = exportFile;
    }

    public String getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(String submitDate) {
        this.submitDate = submitDate;
    }
}
