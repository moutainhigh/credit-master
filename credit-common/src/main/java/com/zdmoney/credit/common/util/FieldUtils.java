package com.zdmoney.credit.common.util;

import java.lang.reflect.Field;

import org.apache.commons.lang.ArrayUtils;

/**
 * 查询类 所有变量属性 （包含父类）
 * 
 * @param cls
 * @return
 */

public  class FieldUtils {
	
	public static Field[] getClassField(Class cls) {
		if (cls == Object.class || cls == null) {
			return new Field[0];
		}
		Field[] field1 = cls.getDeclaredFields();
		Field[] field = getClassField(cls.getSuperclass());
		return (Field[]) ArrayUtils.addAll(field, field1);
	}

}
