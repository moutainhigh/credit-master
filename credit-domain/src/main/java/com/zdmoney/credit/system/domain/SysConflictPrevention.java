package com.zdmoney.credit.system.domain;

import com.zdmoney.credit.framework.domain.BaseDomain;

/**
 * 防冲突表
 * @author 00232949
 *
 */
public class SysConflictPrevention extends BaseDomain {
    /**
	 * 
	 */
	private static final long serialVersionUID = 3102267440344941357L;
	
	private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key == null ? null : key.trim();
    }
}