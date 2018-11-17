package com.zdmoney.credit.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.validation.Errors;

public class ValidatorUtil {
	
	/**
	 * 最大长度验证
	 * @author 00236633
	 * @param errors
	 * @param field
	 * @param fieldName
	 * @param value
	 * @param length
	 * @param require
	 */
	public static void maxLength(Errors errors,String field,String fieldName,String value,int length,boolean require){
		if(value!=null){
			if(value.trim().length()>length){
				errors.rejectValue(field, fieldName+"长度最多为"+length);
			}
		}else{
			if(require==true){
				errors.rejectValue(field, fieldName+"不能为空");
			}
		}
	}
	
	/**
	 * @author 00236633
	 * 身份证长度验证
	 * @param errors
	 * @param field
	 * @param fieldName
	 * @param value
	 * @param require
	 */
	public static void idLength(Errors errors,String field,String fieldName,String value,boolean require){
		if(value!=null){
			if(value.trim().length()==15 && value.trim().length()!=18 ){
				errors.rejectValue(field, fieldName+"长度必须为15或者18");
			}
		}else{
			if(require==true){
				errors.rejectValue(field, fieldName+"不能为空");
			}
		}
	}
	
	/**
	 * @author 00236633
	 * 日期格式验证
	 * @param errors
	 * @param field
	 * @param fieldName
	 * @param value
	 * @param require
	 */
	public static void dateFormat(Errors errors,String field,String fieldName,String value,boolean require){
		if(value!=null){
			SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD");
			try {
				dateFormat.parse(value.trim());
			} catch (ParseException e) {
				errors.rejectValue(field, fieldName+"必须是合法的日期格式");
			}
		}else{
			if(require==true){
				errors.rejectValue(field, fieldName+"不能为空");
			}
		}
	}
	
	/**
	 * 长整形数字验证
	 * @param errors
	 * @param field
	 * @param fieldName
	 * @param value
	 * @param require
	 * @param positiveNumber
	 */
	public static void longNumber(Errors errors,String field,String fieldName,String value,boolean require,boolean positiveNumber){
		if(value!=null){
			try {
				long valueLong =Long.parseLong(value.trim());
				if(positiveNumber==true){
					if(valueLong<=0){
						errors.rejectValue(field, fieldName+"必须大于0");
					}
				}
			} catch (NumberFormatException e) {
				errors.rejectValue(field, fieldName+"必须是合法的数字");
			}
			
		}else{
			if(require==true){
				errors.rejectValue(field, fieldName+"不能为空");
			}
		}
	}
	
	/**
	 * 浮点型数字验证
	 * @param errors
	 * @param field
	 * @param fieldName
	 * @param value
	 * @param require
	 * @param positiveNumber
	 */
	public static void doubleNumber(Errors errors,String field,String fieldName,String value,boolean require,boolean positiveNumber){
		if(value!=null){
			try {
				double valueLong = Double.parseDouble(value.trim());
				if(positiveNumber==true){
					if(valueLong<=0){
						errors.rejectValue(field, fieldName+"必须大于0");
					}
				}
			} catch (NumberFormatException e) {
				errors.rejectValue(field, fieldName+"必须是合法的数字");
			}
			
		}else{
			if(require==true){
				errors.rejectValue(field, fieldName+"不能为空");
			}
		}
	}
}
