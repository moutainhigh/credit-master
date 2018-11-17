package com.zdmoney.credit.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 00217484 on 2015/4/2.
 */
public class DateConvertUtils {

    private static final String DEFAULE_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    // 第一次调用get将返回null
    private static ThreadLocal<Map> connThreadLocal = new ThreadLocal<Map>();

    // 获取线程的变量副本，如果不覆盖initialValue，第一次get返回null，故需要初始化一个SimpleDateFormat，并set到threadLocal中
    public static DateFormat getDateFormat(String dateFormat) {
        Map map = connThreadLocal.get();
        if(map == null){
            connThreadLocal.set(new HashMap());
        }
        DateFormat df = (DateFormat)map.get(dateFormat);
        if (df == null) {
            df = new SimpleDateFormat(dateFormat);
            map.put(dateFormat,df);
        }
        return df;
    }

    public static Date parse(String textDate) {
        Date date = null;
        try {
            DateFormat df = new SimpleDateFormat(DEFAULE_DATE_FORMAT);
            date = df.parse(textDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String format(Date date) {
        return getDateFormat(DEFAULE_DATE_FORMAT).format(date);
    }

    public static Date parse(String textDate, String dateFormat) {
        Date date = null;
        try {
            date = getDateFormat(dateFormat).parse(textDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String format(Date date, String dateFormat) {
        return getDateFormat(dateFormat).format(date);
    }
}
