package com.zdmoney.credit.common.util;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;

/**
 * 字符串转换基础数据类型工具包
 * 
 * @author Ivan
 *
 */
public class Strings {
	//是否转让错误信息提示
    public static String errorMsg ="该借款合同已到期，请咨询4008 216 888电话。";
	public static final String CHARACTER_UTF =  "UTF-8";
	public static final String CHARACTER_ISO = "iso-8859-1";
	public static final String CHARACTER_GBK = "GBK";
    /**
     * 过滤空字符串
     * 
     * Strings.defaultString(null) = "" Strings.defaultString("null") = ""
     * Strings.defaultString("") = "" Strings.defaultString("123") = "123"
     * 
     * @param obj
     * @return
     */
    public static <T extends Object> String parseString(T obj) {
	if (obj == null) {
	    return StringUtils.EMPTY;
	}
	String str = obj.toString().trim();
	if (obj instanceof String) {
	    if ("null".equalsIgnoreCase(str) || "".equalsIgnoreCase(str)) {
		return StringUtils.EMPTY;
	    } else {
		return str;
	    }
	}
	return str;
    }

    /**
     * 转换到指定数据类型
     * 
     * Strings.convertValue("",String.class) = ""
     * Strings.convertValue("123",Long.class) = 123(返回Long类型)
     * Strings.convertValue("aaa",Long.class) = 抛异常
     * Strings.convertValue("123",String.class) = "123"
     * 
     * @param obj
     * @param cls
     * @return
     */
    @SuppressWarnings(value = { "unchecked" })
    public static <T extends Object> T convertValue(Object obj, Class<T> cls) {
	String value = parseString(obj);
	if (isEmpty(obj)) {
	    if (cls == String.class) {
		return (T) StringUtils.EMPTY;
	    } else {
		return null;
	    }
	} else {
	    if (cls == Integer.class) {
		return (T) Integer.valueOf(value);
	    } else if (cls == Long.class) {
		return (T) Long.valueOf(value);
	    } else if (cls == Float.class) {
		return (T) Float.valueOf(value);
	    } else if (cls == Double.class) {
		return (T) Double.valueOf(value);
	    } else if (cls == String.class) {
		return (T) String.valueOf(value);
	    }
	}
	return null;
    }

    /**
     * 空数据转默认值
     * 
     * Strings.defaultValue(null,"123") = "123"
     * Strings.defaultValue("null","123") = "123" Strings.defaultValue("","123")
     * = "123" Strings.defaultValue("str","123") = "str"
     * 
     * @param obj
     *            原值
     * @param defaultStr
     *            为空的默认值
     * @return
     */
    @SuppressWarnings(value = { "unchecked" })
    public static <T extends Object> T defaultValue(Object obj, T defaultStr) {
	if (isEmpty(obj)) {
	    return defaultStr;
	} else {
	    String value = parseString(obj);
	    if (defaultStr instanceof Integer) {
		return (T) Integer.valueOf(value);
	    } else if (defaultStr instanceof Long) {
		return (T) Long.valueOf(value);
	    } else if (defaultStr instanceof Float) {
		return (T) Float.valueOf(value);
	    } else if (defaultStr instanceof Double) {
		return (T) Double.valueOf(value);
	    } else if (defaultStr instanceof String) {
		return (T) String.valueOf(value);
	    }
	    return null;
	}
    }

    /**
     *
     * true?"":"" 三元运算符 Strings.defaultValue("21", 21L, 3L) =3L
     * Strings.defaultValue("21", "", "") ="21" Strings.defaultValue("21", 22L,
     * 1L) =21L
     * 
     * @param value
     * @param ifValue
     * @param defaultValue
     * @return
     */
    @SuppressWarnings(value = { "unchecked" })
    public static <T extends Object> T defaultValue(Object value, T ifValue, T defaultValue) {
	if (isEmpty(value)) {
	    return defaultValue;
	} else {
	    String str = parseString(value);
	    if (ifValue instanceof Integer) {
		if (Integer.valueOf(str).intValue() == ((Integer) ifValue).intValue()) {
		    return defaultValue;
		} else {
		    return (T) Integer.valueOf(str);
		}
	    } else if (ifValue instanceof Long) {
		if (Long.valueOf(str).longValue() == ((Long) ifValue).longValue()) {
		    return defaultValue;
		} else {
		    return (T) Long.valueOf(str);
		}
	    } else if (ifValue instanceof Float) {
		if (Float.valueOf(str).floatValue() == ((Float) ifValue).floatValue()) {
		    return defaultValue;
		} else {
		    return (T) Float.valueOf(str);
		}
	    } else if (ifValue instanceof Double) {
		if (Double.valueOf(str).doubleValue() == ((Double) ifValue).doubleValue()) {
		    return defaultValue;
		} else {
		    return (T) Double.valueOf(str);
		}
	    } else if (ifValue instanceof String) {
		if (String.valueOf(str).equals(((String) ifValue))) {
		    return defaultValue;
		} else {
		    return (T) String.valueOf(str);
		}
	    }
	    return null;
	}
    }

    /**
     * 判断某个字符串是否存在于数组中
     * 
     * @param stringArray
     *            原数组
     * @param source
     *            查找的字符串
     * @return 是否找到
     */
    public static boolean arrayContains(String[] stringArray, String source) {
	// 转换为list
	List<String> tempList = Arrays.asList(stringArray);
	// 利用list的包含方法,进行判断
	if (tempList.contains(source)) {
	    return true;
	} else {
	    return false;
	}
    }

    /**
     * 判断是否空字符串
     * 
     * Strings.isEmpty(null) = true Strings.isEmpty("") = true
     * Strings.isEmpty("str") = false
     * 
     * @param obj
     * @return
     */
    public static <T extends Object> boolean isEmpty(T obj) {
	if (obj instanceof String) {
	    String str = parseString(obj);
	    return StringUtils.isEmpty(str);
	} else {
	    return obj == null;
	}
    }

    /**
     * 判断是否空字符串(反向判断)
     * 
     * Strings.isNotEmpty(null) = false Strings.isNotEmpty("") = false
     * Strings.isNotEmpty("str") = true
     * 
     * @param obj
     * @return
     */
    public static <T extends Object> boolean isNotEmpty(T obj) {
	return !isEmpty(obj);
    }

    /**
     * 字符串格式化
     * 
     * Strings.format("{0}-{1}-{2}","1","2","3") = "1-2-3"
     * Strings.format("{0}-{1}-{2}","") = "-{1}-{2}"
     * Strings.format("{0}-{1}-{2}",null) = "{0}-{1}-{2}"
     * 
     * @param pattern
     * @param arguments
     * @return
     */
    public static String format(String pattern, Object... arguments) {
	return MessageFormat.format(pattern, arguments);
    }

    /**
     * 字符串自增补零
     * 
     * @return
     */
    public static String aa(String str, int length) {
	if (isEmpty(str)) {
	    return "";
	}
	Long value = convertValue(str, Long.class);
	value++;
	String format = "%0" + length + "d";
	String returnStr = String.format(format, value);
	return returnStr;
    }
    
    /**
     * 字符串补0
     * @param str
     * @param length
     * @return
     */
    public static String getFileSeq4Title(String str, int length){
    	if (isEmpty(str)) {
    	   str = "1";
    	}
    	if(str.length() > length){
    		str = str.substring(str.length() - length);
    	}
    	if(isEmpty(str) || "0".equals(str)){
    		str = "1";
    	}
    	Long value = Long.valueOf(str);
    	String format = "%0" + length + "d";
    	String res = String.format(format, value);
    	return res;
    }

    /**
     * 判断是否整形数字
     * 
     * @param number
     * @return
     */
    public static boolean isNumber(String number) {
	if (Strings.isEmpty(number)) {
	    return false;
	}
	return StringUtils.isNumeric(number);
    }

    /**
     * 判断是否小数
     * 
     * @param number
     * @return
     */
    public static boolean isDecimal(String decimal) {
	if (Strings.isEmpty(decimal)) {
	    return false;
	}
	int index = decimal.indexOf(".");
	if (index < 0) {
	    return StringUtils.isNumeric(decimal);
	} else {
	    String num1 = decimal.substring(0, index);
	    String num2 = decimal.substring(index + 1);
	    return StringUtils.isNumeric(num1) && StringUtils.isNumeric(num2);
	}
    }

	/**
	 * LIST集合转化a,b,c字符串形式
	 * @param stringList
	 * @return
	 */
	public static String listToString(List<String> stringList){ 
		 if (stringList==null) { 
			 return null;        }  
		 StringBuilder result=new StringBuilder();    
		 boolean flag=false;      
		 for (String string : stringList) {  
			 if (flag) {            
				 result.append(",");     
				 }else {             
					 flag=true;      
					 }          
			 result.append(string);    
			 }     
		 return result.toString();
       } 
	
	/**
	 * 通过反射获取当前对象所有的字段名
	 * @param t
	 * @return
	 */
	public static <T> List<String> getFields(T t){
		List<String> list = new ArrayList<String>();
		if(t != null){
			Field[] fields = t.getClass().getDeclaredFields();
			for(Field f : fields){
				String fieldName = f.getName();
				if(fieldName.equals("serialVersionUID")){
					continue;
				}else{
					list.add(fieldName);
				}
			}
		}
		return list;
	}

	/**
	 * 去除左边的特殊字符
	 * @param str
	 * @param ch
	 * @return
	 */
	public static String leftExcludeChar(String str,char ch){
		if(Strings.isEmpty(str)){
			return null;
		}
		int len = str.length();//取得字符串的长度
		int index = 0;//预定义第一个非零字符串的位置

		char strs[] = str.toCharArray();// 将字符串转化成字符数组
		for(int i=0; i<len; i++){
			if(ch !=strs[i]){
				index=i;// 找到非零字符串并跳出
				break;
			}
		}
		String strLast = str.substring(index, len);// 截取字符串
		return strLast;
	}

	/**
	 * 通过反射获取所有字段的值
	 * @param t
	 * @param padChar 拼接参数
	 * @return
	 */
	public static <T> String reflectionToString(T t, String padChar){
		StringBuilder strBuilder = new StringBuilder();
		try {
			Field[] fields = t.getClass().getDeclaredFields();
			for (short i = 0; i < fields.length; i++) {
				Field field = fields[i];
				String fieldName = field.getName();
				if(fieldName.equals("serialVersionUID")){
					continue;
				}
				//拼接get方法名
				String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				Class<? extends Object> tCls = t.getClass();
				Method getMethod = tCls.getMethod(getMethodName, new Class[] {});//获取方法
				Object value = getMethod.invoke(t, new Object[] {});//调用方法返回值
				
				if(i == fields.length -1){
					strBuilder.append(value==null ? "" : value.toString());
				}else{
					strBuilder.append(value==null ? "" + padChar : value.toString() + padChar);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return setEncoding(strBuilder.toString(), "gbk");
	}
   
	/**
	 * 设置字符编码
	 * @param str
	 * @param encoding
	 * @return
	 */
	public static String setEncoding(String str, String encoding){
		try {
			//使用传递的编码格式获取字节，设置新的字符编码
			return new String(str.getBytes(encoding), encoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static String getRandomString(int length) { //length表示生成字符串的长度
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	/**
	 * 字符串 转换字符格式
	 * @param str
	 * @param orig
	 * @param dest
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getCharacterTransfer(String str, String orig, String dest) throws UnsupportedEncodingException{
		return new String(str.getBytes(orig), dest);
	}
}
