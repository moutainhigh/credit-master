package com.zdmoney.credit.common.constant;

import com.zdmoney.credit.framework.vo.common.BaseParamVo;

/**
 * Created by ym10094 on 2016/11/18.
 * 网关接口常量
 */
public class GatewayConstant {
	public static final String PROJ_NO = BaseParamVo.PROJ_NO; //信托项目编号
    public static final String PROJECT_NO = "xdcore";
    public static final String SECRET_KEY = "creditgms123456";
    public static final String SYS_SOURCE = BaseParamVo.SYS_SOURCE;
    /**
     * 提起清贷推送结果接口 推送处理成功
     */
    public static  final String ALL_REPAYMENT_PUSH_DEAL_SUCC ="1";
    /**
     * 提起清贷推送结果接口 推送处理失败
     */
    public static  final String ALL_REPAYMENT_PUSH_DEAL_FAIL ="2";
    /**
     * 外贸2支付通道
     */
    public static final  String WAIMAO2PAYCHANNEL = "外贸2支付通道";
    /**
     * 网关响应成功
     */
    public static final String RESPONSE_SUCCESS = "0000";
}
