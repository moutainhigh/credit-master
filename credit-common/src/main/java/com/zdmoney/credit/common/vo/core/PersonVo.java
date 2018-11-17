package com.zdmoney.credit.common.vo.core;

/**
 * 封装征审中债权相关接口参数
 * 
 * @author 00234770
 * 
 */
public class PersonVo {

    /** 用户code */
    private String idnum;

    public String getIdnum() {
	return idnum;
    }

    public void setIdnum(String idnum) {
	this.idnum = idnum;
    }

    @Override
    public String toString() {
	return "PersonVo[idnum=" + idnum + "]";
    }
}
