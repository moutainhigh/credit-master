package com.zdmoney.credit.loan.vo;


public class LoanImageFile {
    
    /**
     * 债权Id
     */
    private Long loanId;
    /**
     * 应用号
     */
    private String appNo;
    /**
     * 身份证号
     */
    private String idNum;
    /**
     * 客户姓名
     */
    private String name;
    /**
     * 图片类型
     */
    private String imageType;
    /**
     * 图片名称
     */
    private String imageName;
    /**
     * 文件路径
     */
    private String filepath;
    /**
     * 样式图片
     */
    private String smallIcon;
    /**
     * 文件名称
     */
    private String fileName;

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public String getAppNo() {
        return appNo;
    }

    public void setAppNo(String appNo) {
        this.appNo = appNo;
    }

    public String getIdNum() {
        return idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getSmallIcon() {
        return smallIcon;
    }

    public void setSmallIcon(String smallIcon) {
        this.smallIcon = smallIcon;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    

}
