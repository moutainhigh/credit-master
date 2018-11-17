package com.zdmoney.credit.common.domain;

import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class Log extends BaseDomain {

    private static final long serialVersionUID = -1148720890228191182L;

    /**
     * Id编号
     */
    private Long id;
    
    /**
     * 操作的对象
     */
    private String className;
    
    /**
     * 内容
     */
    private String content;
    
    /**
     * 案件开始日期
     */
    private Date dateCreated;
    
    /**
     * 错误信息
     */
    private String errorMessage;
    
    /**
     * 日志级别
     */
    private String logLevel;
    
    /**
     * 日志类型
     */
    private String logType;
    
    /**
     * 备注
     */
    private String memo;
    
    /**
     * 对象ID
     */
    private Long objectId;
    
    /**
     * 操作者
     */
    private String operator;
    
    /**
     * 日志来源
     */
    private String source;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
