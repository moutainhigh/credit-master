package com.zdmoney.credit.system.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zdmoney.credit.common.constant.ContactEnum;
import com.zdmoney.credit.common.constant.PersonEnum;


/**
 * 获取系统枚举数据
 * @author Ivan
 *
 */
@Controller
@RequestMapping("/system/enum")
public class EnumController {
	/** 枚举集合 **/
	static Map<String,Object> EnumMap = new HashMap<String,Object>();
	
	static {
		/** 性别枚举 **/
		EnumMap.put("sex", PersonEnum.Sex.values());
		/** 婚姻状况枚举 **/
		EnumMap.put("married", PersonEnum.Married.values());
		/** 学历枚举 **/
		EnumMap.put("edLevel", PersonEnum.EDLevel.values());
		/** 优先联系地址枚举 **/
		EnumMap.put("addressPriority", PersonEnum.AddressPriority.values());
		/** 经济适用房动迁房房改房 **/
		EnumMap.put("houseType", PersonEnum.HouseType.values());
		/** 购车类型枚举 **/
		EnumMap.put("carType", PersonEnum.CarType.values());
		/** 单位性质枚举 **/
		EnumMap.put("cType", PersonEnum.CType.values());
		/** 职位职级枚举 **/
		EnumMap.put("officialRank", PersonEnum.OfficialRank.values());
		/** 发薪方式枚举 **/
		EnumMap.put("payType", PersonEnum.PayType.values());
		/** 职业类型枚举 **/
		EnumMap.put("profession", PersonEnum.Profession.values());
		/** 私营企业类型枚举 **/
		EnumMap.put("enterpriseType", PersonEnum.EnterpriseType.values());
		/** 经营场所枚举 **/
		EnumMap.put("premisesType", PersonEnum.PremisesType.values());
		/** 联系人-联系人类型枚举 **/
		EnumMap.put("contactType", PersonEnum.ContactType.values());
		/** 联系人-关系枚举  **/
		EnumMap.put("contactRelation", PersonEnum.ContactRelation.values());
		/** 联系人-优先级枚举 **/
		EnumMap.put("priority", ContactEnum.Priority.values());
		/** 联系人-电话类型枚举 **/
		EnumMap.put("telType", ContactEnum.TelType.values());
		/** 联系人-地址类型枚举 **/
		EnumMap.put("addressType", ContactEnum.AddressType.values());
		/** 所属行业枚举 **/
		EnumMap.put("industryType", PersonEnum.IndustryType.values());
	}
	
	/**
	 * 跟据Key获取枚举内容
	 * 
	 * @author Ivan
	 * @return
	 */
	@RequestMapping(value = "/get/{keys}", method = RequestMethod.POST)
	@ResponseBody
	public String verifyLogin(@PathVariable String keys
			,HttpServletRequest request, HttpServletResponse response) {
		Map<String,Object> map = new HashMap<String,Object>();
		String [] enumNames = keys.split(",");
		for (int i=0;i<enumNames.length;i++) {
			String enumName = enumNames[i];
			if (EnumMap.containsKey(enumName)) {
				map.put(enumName, EnumMap.get(enumName));
			}
		}
		SerializeWriter out = new SerializeWriter();
		String jsonStr;
		JSONSerializer serializer = new JSONSerializer(out);
		serializer.config(SerializerFeature.WriteEnumUsingToString, true);
		serializer.write(map);
		jsonStr =  out.toString();
		jsonStr = jsonStr.replaceAll("\\[\"", "[");
		jsonStr = jsonStr.replaceAll("\"\\]", "]");
		jsonStr = jsonStr.replaceAll("\\}\"", "}");
		jsonStr = jsonStr.replaceAll("\"\\{", "{");
		jsonStr = jsonStr.replaceAll("\\\\", "");
		return jsonStr;
	}
}
