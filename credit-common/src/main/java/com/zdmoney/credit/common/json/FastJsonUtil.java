package com.zdmoney.credit.common.json;

import com.alibaba.fastjson.JSONObject;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.vo.ResponseInfo;

public class FastJsonUtil {

	public static String toJSONString(Object obj) {
		if (Strings.isEmpty(obj)) {
			return "{}";
		}
		if (obj instanceof ResponseInfo) {
			/** 内部统一响应对象 **/
			return JSONObject.toJSONString(obj);
		} else {
			return JSONObject.toJSONString(obj);
		}
	}

}
