package com.zdmoney.credit.common.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.zdmoney.credit.common.util.Strings;

/**
 * 正则格式验证
 * 
 * @author Ivan
 *
 */
public class RegexUtil {
	/**
	 * 验证手机号格式
	 * @param mobile
	 * @return
	 */
	public static boolean isMobile(String mobile) {
		if (Strings.isEmpty(mobile)) {
			return false;
		}
		String regExp = "^[0-9]{11}$";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(mobile);
		return m.find();
	}
}
