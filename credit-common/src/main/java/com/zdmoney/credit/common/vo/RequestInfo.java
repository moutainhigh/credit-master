package com.zdmoney.credit.common.vo;

import java.io.Serializable;

/**
 * Created by ym10094 on 2016/11/10.
 */
public class RequestInfo<T> implements Serializable {
    private static final long serialVersionUID = -4112845183658483896L;
    /**
     * 系统标示
     */
    private String sysId;
    /**
     * 签名
     */
    private String sign ;

    private T obj;

    /**
     * 网关接口使用--其他接口的请求看实际情况而定 业务功能号，接口的唯一标识
     */

    private String funcId;
    /**
     *  网关接口使用--其他接口的请求看实际情况而定 时间蹉，当前系统时间，可取：System.currentTimeMillis()
     */
    private String key;
    /**
     * 网关接口使用--其他接口的请求看实际情况而定
     */
    private GatewayRequest params;

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSysId() {
        return sysId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public String getFuncId() {
        return funcId;
    }

    public void setFuncId(String funcId) {
        this.funcId = funcId;
    }

    public GatewayRequest getParams() {
        return params;
    }

    public void setParams(GatewayRequest params) {
        this.params = params;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public static GatewayRequest getGatewayRequest(){
        return new GatewayRequest();
    }
    public static class GatewayRequest<E>{
        /**
         *调用接口随机产生字符串编号，建议设置成projectNo+随机生成的字符串，不能重复，如果一个请求中调用了多个接口，requestId都设置成一样
         */
        private String requestId;
        /**
         *系统编号
         */
        private String projectNo;
        /**
         *具体参数
         */
        private E reqParam;

        public String getRequestId() {
            return requestId;
        }

        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }

        public String getProjectNo() {
            return projectNo;
        }

        public void setProjectNo(String projectNo) {
            this.projectNo = projectNo;
        }

        public E getReqParam() {
            return reqParam;
        }

        public void setReqParam(E reqParam) {
            this.reqParam = reqParam;
        }
    }

}
