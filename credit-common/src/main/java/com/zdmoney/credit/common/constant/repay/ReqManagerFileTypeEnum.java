package com.zdmoney.credit.common.constant.repay;

/**
 * Created by ym10094 on 2016/9/1.
 */
public enum ReqManagerFileTypeEnum {
    划拨申请书pdf("01","_apply_",".pdf"),
    划拨申请书xls("02","_apply_",".xls"),
    放款申请书txt("03","_loanapply_",".txt"),
    放款申请书xls("04","_loanapply_",".xls"),
    还款计划xls("05","_payplan_",".xls"),
    放款明细xls("06","_loandetail_",".xls"),
    放款明细txt("07","_loandetail_",".txt"),
    还款计划导出txt("08","_RetuPlan",".txt"),
    债权导出供理财txt("08","_LoanApply",".txt"),
    回款确认书("09","_paylogsum_",".xls"),
    分账明细表("10","_paylog_",".xls"),
    实分账明细表("11","_paylogdetail_",".xls"),
    回款确认书pdf("12","_paylogsum_",".pdf"),
    暂其他收款("13","_shortreceipt_",".xls"),
    划拨确认书pdf("14","_confirm_",".pdf"),
    划拨确认书xls("15","_confirm_",".xls");
    /**
     * 文件类型
     */
    private String fileType;
    /**
     * 文件编码
     */
    private String fileCode;
    /**
     * 文件后缀
     */
    private String fileExtension;

    ReqManagerFileTypeEnum(String fileType, String fileCode,String fileExtension) {
        this.fileType = fileType;
        this.fileCode = fileCode;
        this.fileExtension = fileExtension;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileCode() {
        return fileCode;
    }

    public void setFileCode(String fileCode) {
        this.fileCode = fileCode;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }
}

