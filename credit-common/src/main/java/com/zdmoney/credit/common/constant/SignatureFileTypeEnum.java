package com.zdmoney.credit.common.constant;

/**
 * Created by ym10094 on 2016/12/9. 签章文件类型
 */
public enum SignatureFileTypeEnum {
    咨询服务协议("001","咨询服务协议");

    private String code;
    private String name;

    SignatureFileTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
