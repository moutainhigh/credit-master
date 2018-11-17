package com.zdmoney.credit.test.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zdmoney.credit.common.util.HttpUtils;
import com.zdmoney.credit.common.util.MD5Util;
import com.zdmoney.credit.common.util.coreUtil.JSONUtil;

public class AppLoginCheckTest {

	public static void main(String[] args) {
		List arrayList = new ArrayList();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userCode", "zhaocm");
		map.put("passWord", MD5Util.md5Hex("zd123456"));
		map.put("newOrOld", "o");
		map.put("infos", JSONUtil.toJSON(arrayList));
		String res = HttpUtils.doPost("http://my.creditweb:8080/credit-web/core/loginCheckCore/loginCheckByLoan", JSONUtil.toJSON(map));
        System.out.println("sfsdtryr5:" + res);

	}

}
