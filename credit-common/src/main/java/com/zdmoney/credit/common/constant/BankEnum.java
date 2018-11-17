package com.zdmoney.credit.common.constant;

public enum BankEnum {
    
    ICBC("102","中国工商银行"), ABC("103","中国农业银行"), BOC("104","中国银行"), CCB("105","中国建设银行"),
    
    PAB("154","平安银行"),
    
    CDB("201","国家开发银行"), EXIMB("202","中国进出口银行"), ADBC("203","中国农业发展银行"),
    
    BOCO("301","交通银行"),CITIC("302","中信银行"),CEB("303","中国光大银行"),HXB("304","华夏银行"),
    
    CMBC("305","中国民生银行"),CGB("306","广东发展银行"),SDB("307","深圳发展银行"),CMB("308","招商银行"),
    
    CIB("309","兴业银行"),SPDB("310","上海浦东发展银行"),EGB("315","恒丰银行"),HSB("315","徽商银行"),
    
    PSBC("403","中国邮政储蓄银行");
    
    
    public final String code;
    
    public final String desc;

    //构造器默认也只能是private, 从而保证构造函数只能在内部使用
    private BankEnum(String code,String desc) {
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
    public static BankEnum getByCode(String code) {
        for (BankEnum bank : values()) {
            if (bank.getCode().equals(code)) {
                return bank;
            }
        }
        return null;
    }
    
    public static BankEnum nameOf(String name){
    	BankEnum bank = null;
		if (name != null){
			for (BankEnum type : BankEnum.values()) {
				if (type.name().equalsIgnoreCase(name))
					bank = type;
			}
		}
		return bank;
	}
}
