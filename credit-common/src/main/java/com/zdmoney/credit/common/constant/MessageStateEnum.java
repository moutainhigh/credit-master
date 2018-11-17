package com.zdmoney.credit.common.constant;

public enum MessageStateEnum {
	已读("已读"),未读("未读"),已删除("已删除");
    
    /** value*/
    private String value;
    
    private MessageStateEnum( String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
