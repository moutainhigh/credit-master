package com.zdmoney.credit.flow.vo;

import com.zdmoney.credit.flow.domain.SpNodeEmployee;

/**
 * Created by ym10094 on 2017/8/17.
 */
public class SpNodeEmployeeVo extends SpNodeEmployee {

    private String name;

    private String nodeName;

    private String userCode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }
}
