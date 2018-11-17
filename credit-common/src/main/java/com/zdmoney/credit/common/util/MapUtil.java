package com.zdmoney.credit.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.zdmoney.credit.common.annotation.ConvertionCLazzType;


/**
 * Map工具类
 * @author 00236633
 *
 */
public class MapUtil {
	public static DecimalFormat decimalFormat = new DecimalFormat();
	
	public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
	
	/**
	 * 将一个对象转化为Map
	 * @param o
	 * @param result
	 * @param isAddNullValue
	 */
	public static void vObjectConvertToMap(Object o,Map<String,Object> result,boolean isAddNullValue){
		Field[] fields = o.getClass().getDeclaredFields();
		for (Field filed : fields) {
			try {
				String fieldName = filed.getName();
				String getMethodName = "get"+fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1);
				Object value = o.getClass().getDeclaredMethod(getMethodName).invoke(o);
				if(value!=null){
					ConvertionCLazzType convertionCLazzType = filed.getAnnotation(ConvertionCLazzType.class);
					if(convertionCLazzType!=null){
						Class clazz = convertionCLazzType.value();
						String format = convertionCLazzType.format();
						if(clazz.isAssignableFrom(Integer.class)){
							result.put(fieldName, Integer.parseInt(value.toString().trim()));
						}else if(clazz.isAssignableFrom(Long.class)){
							result.put(fieldName, Long.parseLong(value.toString().trim()));
						}else if(clazz.isAssignableFrom(Double.class)){
							if(format!=null&&!format.equals("")){
								decimalFormat.applyPattern(format);
								result.put(fieldName, decimalFormat.parse(value.toString().trim()).doubleValue());
							}else{
								result.put(fieldName,Double.parseDouble(value.toString().trim()));
							}
						}else if(clazz.isAssignableFrom(Date.class)){
							if(filed.getType().isAssignableFrom(Date.class)){
								result.put(fieldName, value);
							}else if(filed.getType().isAssignableFrom(java.sql.Date.class)){
								result.put(fieldName, new Date (((java.sql.Date)value).getTime()));
							}else{
								if(format==null||format.equals("")){
									format="yyyy-MM-dd";
								}
								simpleDateFormat.applyPattern(format);
								result.put(fieldName, simpleDateFormat.parse(value.toString().trim()));
							}
						}else if(clazz.isAssignableFrom(java.sql.Date.class)){
							if(filed.getType().isAssignableFrom(Date.class)){
								result.put(fieldName, new java.sql.Date (((Date)value).getTime()));
							}else if(filed.getType().isAssignableFrom(java.sql.Date.class)){
								result.put(fieldName, value);
							}else{
								if(format==null||format.equals("")){
									format="yyyy-MM-dd";
								}
								simpleDateFormat.applyPattern(format);
								result.put(fieldName,  new java.sql.Date (simpleDateFormat.parse(value.toString().trim()).getTime()));
							}
						}else if(clazz.isAssignableFrom(String.class)){
							if( filed.getType().isAssignableFrom(Date.class) || filed.getType().isAssignableFrom(java.sql.Date.class) ){
								if(format==null||format.equals("")){
									format="yyyy-MM-dd";
								}
								simpleDateFormat.applyPattern(format);
								result.put(fieldName, simpleDateFormat.format( (Date)value));
							}else{
								result.put(fieldName, value.toString().trim());
							}
						}else{
							result.put(fieldName, value);
						}
					}else{
						result.put(fieldName, value);
					}
				}else{
					if(isAddNullValue==true){
						result.put(fieldName, value);
					}
				}
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException
					| SecurityException | ParseException e) {
				System.out.println(e);
			}
		}
	}
}
