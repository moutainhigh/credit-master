package com.zdmoney.credit.grant.vo;

import java.io.Serializable;

/**
 * 放款--借款人信息
 * Created by ym10094 on 2016/11/11.
 */
public class GrantBorrowPersonVo implements Serializable {
    private static final long serialVersionUID = 3163417782726789126L;
    /**
     * 借款人名称
     */
    private String comName;
    /**
     * 证件类型
     */
    private String comIdtype;
    /**
     * 证件号码
     */
    private String comIdno;
    /**
     * 联系电话
     */
    private String comTel;

    public String getComName() {
        return comName;
    }

    public void setComName(String comName) {
        this.comName = comName;
    }

    public String getComIdtype() {
        return comIdtype;
    }

    public void setComIdtype(String comIdtype) {
        this.comIdtype = comIdtype;
    }

    public String getComIdno() {
        return comIdno;
    }

    public void setComIdno(String comIdno) {
        this.comIdno = comIdno;
    }

    public String getComTel() {
        return comTel;
    }

    public void setComTel(String comTel) {
        this.comTel = comTel;
    }
}
