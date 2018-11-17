package com.zdmoney.credit.common.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;

public class LufaxUtil {

    /**
     * 生成安硕批次号
     * @return
     */
    public static String createAnshuoBatchId() {
        return "B" + DateTime.now().toString("yyyyMMddHHmmssSSS") + RandomStringUtils.randomNumeric(6);
    }

    /**
     * 生成请求流水号 时间yyyyMMddHHmmssSS + 接口Id + 随机六位数字 例如2017050215511875 100090 314074
     * @param interId
     * @return
     */
    public static String createInterfaceReqId(String interId) {
        return DateTime.now().toString("yyyyMMddHHmmssSS") + interId + RandomStringUtils.randomNumeric(6);
    }
    
    /**
     * 生成安硕序号
     * @return
     */
    public static String createAnshuoSerialno() {
        return DateTime.now().toString("yyyyMMddHHmmssSSS") + RandomStringUtils.randomNumeric(6);
    }
    
    /**
     * 生成安硕贷款受理号
     * @param tradeNo
     * @return
     */
    public static String createAnshuoLoanAcceptId(String tradeNo) {
        if(Strings.isNotEmpty(tradeNo)){
            return tradeNo;
        }
        return "TX" + DateTime.now().toString("yyyyMMddHHmmssSSS") + "D" + RandomStringUtils.randomNumeric(6);
    }
}