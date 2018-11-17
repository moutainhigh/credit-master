package com.zdmoney.credit.common.constant.lufax;

public enum LufaxRepayInterfaceEnum {
    
    证大发起一般还款_提前还款指令_逾期回购指令("100050","证大发起一般还款_提前还款指令_逾期回购指令"),
    证大发起一次性回购指令("800080","证大发起一次性回购指令"),
    证大同步还款计划给陆金所("100090","证大同步还款计划给陆金所"), 
    证大同步还款明细给陆金所("100060","证大同步还款明细给陆金所"), 
    证大同步还款记录给陆金所("100100","证大同步还款记录给陆金所"),
    证大借据信息给核算("800100","证大借据信息给核算");
    
    public final String code;
    
    public final String desc;

    private LufaxRepayInterfaceEnum(String code,String desc) {
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
    public static LufaxRepayInterfaceEnum getByCode(String code) {
        for (LufaxRepayInterfaceEnum genum : values()) {
            if (genum.getCode().equals(code)) {
                return genum;
            }
        }
        return null;
    }
    
    public static LufaxRepayInterfaceEnum nameOf(String name){
    	LufaxRepayInterfaceEnum genum = null;
		if (name != null){
			for (LufaxRepayInterfaceEnum type : LufaxRepayInterfaceEnum.values()) {
				if (type.name().equalsIgnoreCase(name))
					genum = type;
			}
		}
		return genum;
	}
    
    public static LufaxRepayInterfaceEnum descOf(String desc){
    	LufaxRepayInterfaceEnum genum = null;
		if (desc != null){
			for (LufaxRepayInterfaceEnum type : LufaxRepayInterfaceEnum.values()) {
				if (type.desc.equalsIgnoreCase(desc))
					genum = type;
			}
		}
		return genum;
	}
}
