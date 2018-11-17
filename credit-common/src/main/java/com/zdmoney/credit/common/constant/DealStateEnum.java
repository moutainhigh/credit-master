package com.zdmoney.credit.common.constant;

/**
 * @ClassName:     DealStateEnum.java
 * @Description:   
 * @author         柳云华
 * @version        V1.0  
 * @Since          JDK 1.7
 * @Date           2018年1月25日 下午5:56:45
 */
public enum DealStateEnum {
    /* 划扣失败 */
    NONE("0","无需入账"),
    /* 默认 */
    PREPARE("1","待入账"),
    /* 划扣成功，入账成功 */
    SUCCESS("2","已入账"),
    /* 划扣成功，入账失败 */
    FAILURE("3","入账失败"),
    /* 预结清或结清 */
    REFUSE("4","拒绝入账");
    
    public final String code;
    
    public final String desc;

    private DealStateEnum(String code,String desc) {
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
    public static DealStateEnum getByCode(String code) {
        for (DealStateEnum genum : values()) {
            if (genum.getCode().equals(code)) {
                return genum;
            }
        }
        return null;
    }
    
    public static DealStateEnum nameOf(String name){
        DealStateEnum genum = null;
        if (name != null){
            for (DealStateEnum type : DealStateEnum.values()) {
                if (type.name().equalsIgnoreCase(name))
                    genum = type;
            }
        }
        return genum;
    }
    
    public static DealStateEnum descOf(String desc){
        DealStateEnum genum = null;
        if (desc != null){
            for (DealStateEnum type : DealStateEnum.values()) {
                if (type.desc.equalsIgnoreCase(desc))
                    genum = type;
            }
        }
        return genum;
    }

}
