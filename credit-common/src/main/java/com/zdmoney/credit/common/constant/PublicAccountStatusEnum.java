package com.zdmoney.credit.common.constant;

/**
 * 对公账户认领状态枚举类
 * @author 00236640
 */
public enum PublicAccountStatusEnum {

    未认领("未认领"),
    已认领("已认领"),
    已导出("已导出"),
    渠道确认("渠道确认"),
    被车企贷认领("被车企贷认领"),
    被车企贷无需认领("被车企贷无需认领"),
    被证方认领("被证方认领"),
    被证方无需认领("被证方无需认领");

    private String value;

    private PublicAccountStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
