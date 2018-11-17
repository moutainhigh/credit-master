package com.zdmoney.credit.common.constant;

/**
 * 第三方代付报盘文件状态
 * @author 00236640
 */
public enum ThirdOfferStateEnum {

    /** 报盘文件生成初始状态 **/
    未报盘("未报盘"),
    /** 已发送tpp还未回盘 */
    已报盘("已报盘"),
    /** 回盘成功 */
    扣款成功("扣款成功"),
    /** 回盘失败 */
    扣款失败("扣款失败");

    private String value;

    private ThirdOfferStateEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
