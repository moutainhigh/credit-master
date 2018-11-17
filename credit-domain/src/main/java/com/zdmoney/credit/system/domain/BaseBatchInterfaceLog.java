package com.zdmoney.credit.system.domain;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class BaseBatchInterfaceLog extends BaseDomain{
    /**
	 * 
	 */
	private static final long serialVersionUID = -4329221973182050067L;

	private Long id;

    private String message;

    private String source;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message == null ? null : message.trim();
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source == null ? null : source.trim();
    }

}