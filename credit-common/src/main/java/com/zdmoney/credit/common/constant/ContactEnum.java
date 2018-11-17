package com.zdmoney.credit.common.constant;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

/**
 * 联系人相关枚举
 * @author Ivan
 *
 */
public class ContactEnum {
	
	/**
	 * 联系人电话类型枚举
	 *
	 */
	public enum ClassName {
		Borrower("zdsys.Borrower",""),
		Contact("zdsys.Contact","");
		
		String name;
		String code;
		
		ClassName(String name,String code) {
			this.name = name;
			this.code = code;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}
		
		@Override
		public String toString() {
			Map<String, String> map = new HashMap<String,String>();
			map.put("id", name);
			map.put("text", name);
			return JSONObject.toJSONString(map);
		}
		
		public static ClassName get(String str){
			for (ClassName className : ClassName.values()) {
				if (className.getName().equalsIgnoreCase(str)) {
					return className;
				}
			}
			return null;
		}
		
	}
	
	/**
	 * 联系人电话类型枚举
	 *
	 */
	public enum TelType {
		家庭电话("家庭电话",""),
		手机("手机",""),
		公司电话("公司电话",""),
		传真("传真",""),
		其它("其它","");
		
		String name;
		String code;
		
		TelType(String name,String code) {
			this.name = name;
			this.code = code;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}
		
		@Override
		public String toString() {
			Map<String, String> map = new HashMap<String,String>();
			map.put("id", name);
			map.put("text", name);
			return JSONObject.toJSONString(map);
		}
		
		public static TelType get(String str){
			for (TelType telType : TelType.values()) {
				if (telType.getName().equalsIgnoreCase(str)) {
					return telType;
				}
			}
			return null;
		}
		
	}

	
	/**
	 * 优先级枚举
	 *
	 */
	public enum Priority {
		高("高",""),
		中("中",""),
		低("低","");
		
		String name;
		String code;
		
		Priority(String name,String code) {
			this.name = name;
			this.code = code;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}
		
		@Override
		public String toString() {
			Map<String, String> map = new HashMap<String,String>();
			map.put("id", name);
			map.put("text", name);
			return JSONObject.toJSONString(map);
		}
		
	}
	
	/**
	 * 联系人地址类型枚举
	 *
	 */
	public enum AddressType {
		家庭("家庭",""),
		公司("公司",""),
		其它("其它","");
		
		String name;
		String code;
		
		AddressType(String name,String code) {
			this.name = name;
			this.code = code;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}
		
		@Override
		public String toString() {
			Map<String, String> map = new HashMap<String,String>();
			map.put("id", name);
			map.put("text", name);
			return JSONObject.toJSONString(map);
		}
		
		public static AddressType get(String str){
			for (AddressType addressType : AddressType.values()) {
				if (addressType.getName().equalsIgnoreCase(str)) {
					return addressType;
				}
			}
			return null;
		}
		
	}
}
