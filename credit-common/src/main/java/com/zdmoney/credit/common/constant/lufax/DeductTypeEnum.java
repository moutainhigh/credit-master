package com.zdmoney.credit.common.constant.lufax;

public enum DeductTypeEnum {
    
    自动代扣("1","自动代扣"), 
    重试代扣("2","重试代扣"), 
    逾期代扣("3","逾期代扣"), 
    追偿代扣("4","追偿代扣"), 
    收费代扣("5","收费代扣"),
    提前还款代扣("6","提前还款代扣"),
    手工还款代扣("7","手工还款代扣");
    
    public final String code;
    
    public final String desc;

    private DeductTypeEnum(String code,String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDesc() {
        return desc;
    }
    
    /**
     * 通过枚举<code>code</code>获得枚举
     * 
     * @param code
     * @return
     */
    public static DeductTypeEnum getByCode(String code) {
        for (DeductTypeEnum genum : values()) {
            if (genum.getCode().equals(code)) {
                return genum;
            }
        }
        return null;
    }
    
    public static DeductTypeEnum nameOf(String name){
    	DeductTypeEnum genum = null;
		if (name != null){
			for (DeductTypeEnum type : DeductTypeEnum.values()) {
				if (type.name().equalsIgnoreCase(name))
					genum = type;
			}
		}
		return genum;
	}
    
    public static DeductTypeEnum descOf(String desc){
    	DeductTypeEnum genum = null;
		if (desc != null){
			for (DeductTypeEnum type : DeductTypeEnum.values()) {
				if (type.desc.equalsIgnoreCase(desc))
					genum = type;
			}
		}
		return genum;
	}
}
