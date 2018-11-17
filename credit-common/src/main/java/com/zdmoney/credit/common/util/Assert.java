package com.zdmoney.credit.common.util;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;

/**
 * 常规数据项检验
 * 
 * @author Ivan
 *
 */
public class Assert {

    /***
     * 判断值对象是否为NULL （为NULL时抛出异常）
     * 
     * @param object
     *            值对象
     * @param message
     *            异常补充信息
     */
    public static void notNull(Object object, Object... arguments) {
    notNull(object, ResponseEnum.VALIDATE_ISNULL, arguments);
    }

    /***
     * 判断值对象是否为NULL （为NULL时抛出异常）
     * 
     * @param object
     *            值对象
     * @param responseEnum
     *            异常代码
     * @param message
     *            异常补充信息
     */
    public static void notNull(Object object, ResponseEnum responseEnum, Object... arguments) {
    if (object == null) {
        throw new PlatformException(responseEnum, arguments);
    }
    }
    
    /***
     * 判断值对象是否为NULL （不为NULL时抛出异常）
     * 
     * @param object
     *            值对象
     * @param responseEnum
     *            异常代码
     * @param message
     *            异常补充信息
     */
    public static void isNull(Object object, ResponseEnum responseEnum, Object... arguments) {
    if (object != null) {
        throw new PlatformException(responseEnum, arguments);
    }
    }

    /***
     * 判断值对象是否为空 （为空时抛出异常）
     * 
     * @param object
     *            值对象
     * @param message
     *            异常补充信息
     */
    public static String notEmpty(Object object, String message) {
    if (object == null) {

    } else if (Strings.isEmpty(object)) {
        throw new PlatformException(ResponseEnum.VALIDATE_ISNULL, message);
    }
    return Strings.convertValue(object, String.class);
    }

    /***
     * 判断值对象是否为NULL 或 空 （为NULL或空时抛出异常）
     * 
     * @param object
     *            值对象
     * @param message
     *            异常补充信息
     */
    public static String notNullAndEmpty(Object object, String message) {
    return notNullAndEmpty(object, ResponseEnum.VALIDATE_ISNULL, message);
    }
    
    /***
     * 判断值对象是否为NULL 或 空 （为NULL或空时抛出异常）
     * 
     * @param object
     *            值对象
     * @param message
     *            异常补充信息
     */
    public static String notNullAndEmpty(Object object,ResponseEnum responseEnum, String message) {
    if (Strings.isEmpty(object)) {
        throw new PlatformException(responseEnum, message);
    }
    return Strings.convertValue(object, String.class);
    }
    
    /***
     * 判断值对象是否为NULL 或 空 （不为NULL并且不为空时抛出异常）
     * 
     * @param object
     *            值对象
     * @param message
     *            异常补充信息
     */
    public static String isNullAndEmpty(Object object,ResponseEnum responseEnum, String message) {
    if (!Strings.isEmpty(object)) {
        throw new PlatformException(responseEnum, message);
    }
    return Strings.convertValue(object, String.class);
    }

    /**
     * 判断是否整数
     * 
     * @param object
     * @param message
     */
    public static Long notNumber(Object object, String message) {
    String str = Strings.convertValue(object, String.class);
    if (!Strings.isNumber(str)) {
        throw new PlatformException(ResponseEnum.VALIDATE_FORMAT, message);
    }
    return Strings.convertValue(object, Long.class);
    }

    /**
     * 判断是否小数
     * 
     * @param object
     * @param message
     */
    public static BigDecimal notBigDecimal(Object object, String message) {
    String str = Strings.convertValue(object, String.class);
    if (!Strings.isDecimal(str)) {
        throw new PlatformException(ResponseEnum.VALIDATE_FORMAT, message);
    }
    return new BigDecimal(str);
    }

    /**
     * 判断是否为日期格式
     * 
     * @param object
     * @param message
     */
    public static Date notDate(Object object, String message) {
    String str = Strings.convertValue(object, String.class);
    Date date = Dates.parse(str, "");
    if (date == null) {
        throw new PlatformException(ResponseEnum.VALIDATE_FORMAT, message);
    }
    return date;
    }

    /***
     * 判断空集合(为空时抛异常)
     * 
     * @param coll
     *            集合对象
     * @param message
     *            异常补充信息
     */
    public static void notCollectionsEmpty(Collection coll, String message) {
    	notCollectionsEmpty(coll,ResponseEnum.VALIDATE_COLLECTION_ISNULL,message);
    }
    
    /***
     * 判断空集合(为空时抛异常)
     * 
     * @param coll
     *            集合对象
     * @param message
     *            异常补充信息
     */
    public static void notCollectionsEmpty(Collection coll,ResponseEnum responseEnum, String message) {
	    if (CollectionUtils.isEmpty(coll)) {
	        throw new PlatformException(responseEnum, message);
	    }
    }
    
    /***
     * 判断空集合(不为空时抛异常)
     * 
     * @param coll
     *            集合对象
     * @param message
     *            异常补充信息
     */
    public static void isCollectionsEmpty(Collection coll,ResponseEnum responseEnum, String message) {
	    if (!CollectionUtils.isEmpty(coll)) {
	        throw new PlatformException(responseEnum, message);
	    }
    }

    /***
     * 判断空数组
     * 
     * @param arr
     *            数组对象
     * @param message
     *            异常补充信息
     */
    public static void notArrayEmpty(Object[] arr, String message) {
    if (arr == null || arr.length == 0) {
        throw new PlatformException(ResponseEnum.VALIDATE_ARRAY_ISNULL, message);
    }
    }

    /***
     * 验证枚举值数据项
     * 
     * @param enumType
     *            枚举类
     * @param enumName
     *            值名称
     * @param message
     *            异常补充信息
     */
    public static <T extends Enum<T>> T validEnum(Class<T> enumType, String enumName, String message) {
    try {
        return Enum.valueOf(enumType, enumName);
    } catch (Exception ex) {
        ex.printStackTrace();
        throw new PlatformException(ResponseEnum.VALIDATE_ENUM_ERROR, message,enumType + ">" + enumName);
    }
    }
    
    /**
     * 判断是否为正确格式的日期
     * 
     * @param object 校验日期
     * @param pattern 格式
     * @param message 提示信息
     * @return
     */
    public static Date notDate(Object object, String pattern, String message) {
        String str = Strings.convertValue(object, String.class);
        Date date = null;
        try {
            DateFormat format = new SimpleDateFormat(pattern);
            // 设置此条件（严格解析）、则转换2015-02-29这样的格式的字符串日期将抛出异常
            format.setLenient(false);
            date = format.parse(str);
        } catch (ParseException e) {
            throw new PlatformException(ResponseEnum.VALIDATE_FORMAT, message);
        }
        if (null==date) {
            throw new PlatformException(ResponseEnum.VALIDATE_FORMAT, message);
        }
        return date;
    }

}
