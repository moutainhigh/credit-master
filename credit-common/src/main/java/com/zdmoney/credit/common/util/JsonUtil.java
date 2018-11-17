package com.zdmoney.credit.common.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;

import com.alibaba.fastjson.JSONObject;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.Strings;

public class JsonUtil {

	/**
	 * 将JSON对象转换成 URL参数拼接(a=a&b=b)
	 * 
	 * @param jsonObj
	 * @return
	 */
	public static String toUrlParam(JSONObject jsonObj) {
		List<String> params = new ArrayList<String>();

		Iterator iter = jsonObj.keySet().iterator();
		while (iter.hasNext()) {
			String key = Strings.parseString(iter.next());
			Object value = jsonObj.get(key);
			if (value instanceof Date) {
				/** 将日期格式转换成 yyyy-MM-dd **/
				value = Dates.getDateTime((Date) value, Dates.DEFAULT_DATE_FORMAT);
			} else {
				value = Strings.parseString(value);
			}
			params.add(key + "=" + value);
		}
		return StringUtils.join(params.toArray(), "&");
	}

}
