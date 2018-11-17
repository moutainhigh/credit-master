package com.zdmoney.credit.loan.domain;

import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

/**
 * 日志记录表
 * @author 00232949
 *
 */
public class LoanLog extends BaseDomain {
    
    private static final long serialVersionUID = -1131274754692059487L;

    private Long id;

    private String content;

    private Long objectId;

    private String creator;

    private Date createTime;

    private String logType;

    private String logName;

    private String memo;

    private String[] logNames;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType == null ? null : logType.trim();
    }

    public String getLogName() {
        return logName;
    }

    public void setLogName(String logName) {
        this.logName = logName;
    }

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String[] getLogNames() {
        return logNames;
    }

    public void setLogNames(String[] logNames) {
        this.logNames = logNames;
    }
}