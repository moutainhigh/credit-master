package com.zdmoney.credit.grant.vo;

import java.io.Serializable;

/**
 * 放款--借款联系人
 * Created by ym10094 on 2016/11/11.
 */
public class GrantBorRelaPerVo implements Serializable {
    private static final long serialVersionUID = 3163417682726789126L;
    /**
     * 借款人名称
     */
    private String relName;
    /**
     * 证件类型
     */
    private String relIdtype;
    /**
     * 证件号码
     */
    private String relIdno;
    /**
     * 联系电话
     */
    private String relTel;

    public String getRelName() {
        return relName;
    }

    public void setRelName(String relName) {
        this.relName = relName;
    }

    public String getRelIdtype() {
        return relIdtype;
    }

    public void setRelIdtype(String relIdtype) {
        this.relIdtype = relIdtype;
    }

    public String getRelIdno() {
        return relIdno;
    }

    public void setRelIdno(String relIdno) {
        this.relIdno = relIdno;
    }

    public String getRelTel() {
        return relTel;
    }

    public void setRelTel(String relTel) {
        this.relTel = relTel;
    }
}
