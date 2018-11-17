package com.zdmoney.credit.common.util.coreUtil;


/**
 * Created by xzhao on 15-4-7.
 */
public final class Constants {

    public static final String SUCCESS_CODE = "000000";//成功代码
    public static final String PARAM_ERR_CODE = "010001";//请求参数错误代码
    public static final String DATA_ERR_CODE = "020001";//请求数据错误代码
    public static final String SYS_ERR_CODE = "030001";//账务系统错误代码
    public static final String LOGIN_PERMISSION_NOT = "040001";//其它未知错误代码
    public static final String OTHER_ERR_CODE = "990001";//其它未知错误代码
    public static final String TYTX_SUCCESS_CODE="900000";//统一通信平台成功码
    		
    public static final String URLCORE_REG = "/[^/]*Core/[^/]*";//对外提供接口的URL

    private Constants() {
    }
}
