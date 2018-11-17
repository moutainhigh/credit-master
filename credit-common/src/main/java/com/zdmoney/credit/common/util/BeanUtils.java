package com.zdmoney.credit.common.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;

/***
 * 实体类公共方法
 * @author Ivan
 *
 */
public class BeanUtils {
	/***
	 * Domain 、 Vo 转换成Map
	 * @param bean
	 * @return
	 */
	public static Map<String,Object> toMap(Object bean) {
		try {
			Class type = bean.getClass();
			Map<String,Object> returnMap = new HashMap<String,Object>();
			BeanInfo beanInfo = Introspector.getBeanInfo(type);
			PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors();
			for (int i = 0; i< propertyDescriptors.length; i++) {
				PropertyDescriptor descriptor = propertyDescriptors[i];
				String propertyName = descriptor.getName();
				if (!propertyName.equals("class")) {
					Method readMethod = descriptor.getReadMethod();
					Object result = readMethod.invoke(bean, new Object[0]);
					if (result != null) {
						returnMap.put(propertyName, result);
					} else {
//						returnMap.put(propertyName, "");
					}
				}
			}
			return returnMap;
		} catch(Exception ex) {
			throw new PlatformException(ResponseEnum.VALIDATE_BEANTOMAP_ERROR,ex);
		}
	}
}
