package com.zdmoney.credit.common.constant;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
/**
 * 客户相关枚举
 * @author Ivan
 *
 */
public class PersonEnum {
	
	/**
	 * 经营场所枚举
	 */
	public enum PremisesType {
		租用("租用",""),
		自有("自有","");
		
		String name;
		String code;
		
		PremisesType(String name,String code) {
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
	 * 私营企业类型枚举
	 */
	public enum EnterpriseType {
		个体户("个体户",""),
		独资("独资",""),
		合伙制("合伙制",""),
		股份制("股份制",""),
		其它("其它","");
		
		String name;
		String code;
		
		EnterpriseType(String name,String code) {
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
	 * 职业类型枚举
	 */
	public enum Profession {
		工薪("工薪",""),
		白领("白领",""),
		自营("自营",""),
		学生("学生","");
		
		String name;
		String code;
		
		Profession(String name,String code) {
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
	 * 发薪方式枚举
	 */
	public enum PayType {
		网银("网银",""),
		现金("现金",""),
		网银现金("网银+现金","");
		
		String name;
		String code;
		
		PayType(String name,String code) {
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
	 * 职位职级枚举
	 */
	public enum OfficialRank {
		法人代表("法人代表",""),
		总经理("总经理",""),
		副总经理("副总经理",""),
		部门经理("部门经理",""),
		主管("主管",""),
		职员("职员","");
		
		String name;
		String code;
		
		OfficialRank(String name,String code) {
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
	 * 单位性质枚举
	 */
	public enum CType {
		政府机构("政府机构",""),
		事业单位("事业单位",""),
		国企("国企",""),
		外资("外资",""),
		民营("民营",""),
		私营("私营",""),
		其它("其它",""),
		合资("合资",""),
		个体("个体","");
		
		String name;
		String code;
		
		CType(String name,String code) {
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
	 * 购车类型枚举
	 */
	public enum CarType {
		一手车("一手车",""),
		二手车("二手车","");
		
		String name;
		String code;
		
		CarType(String name,String code) {
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
	 * 房产类型枚举
	 */
	public enum HouseType {
		商品房("商品房",""),
		经济适用房动迁房房改房("经济适用房/动迁房/房改房",""),
		自建房("自建房","");
		
		String name;
		String code;
		
		HouseType(String name,String code) {
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
	 * 优先联系地址枚举
	 */
	public enum AddressPriority {
		户籍地址("户籍地址",""),
		住宅地址("住宅地址",""),
		单位地址("单位地址","");
		
		String name;
		String code;
		
		AddressPriority(String name,String code) {
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
	 * 学历枚举
	 */
	public enum EDLevel {
		硕士及以上("硕士及以上",""),
		本科("本科",""),
		大专("大专",""),
		中专("中专",""),
		高中("高中",""),
		初中及以下("初中及以下","");
		
		String name;
		String code;
		
		EDLevel(String name,String code) {
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
	 * 婚姻状况枚举
	 */
	public enum Married {
		未婚("未婚",""),
		已婚("已婚",""),
		离异("离异",""),
		其它("其它","");
		
		String name;
		String code;
		
		Married(String name,String code) {
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
	 * 性别枚举
	 */
	public enum Sex {
		男("男",""),
		女("女","");
		
		String name;
		String code;
		
		Sex(String name,String code) {
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
	 * 联系人-联系人类型
	 */
	public enum ContactType {
		亲属("亲属",""),
		配偶("配偶",""),
		同事("同事",""),
		紧急联系人("紧急联系人",""),
		其它("其它","");
		
		String name;
		String code;
		
		ContactType(String name,String code) {
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
	 * 联系人-关系
	 */
	public enum ContactRelation {
		本人("本人(新)",""),
		父母("父母",""),
		子女("子女",""),
		兄弟("兄弟",""),
		姐妹("姐妹",""),
		兄妹("兄妹",""),
		姐弟("姐弟",""),
		朋友("朋友",""),
		同事("同事",""),
		房东("房东",""),
		亲属("亲属",""),
		其它("其它","");
		
		String name;
		String code;
		
		ContactRelation(String name,String code) {
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
	 * 所属行业枚举
	 * @author YM10104
	 *
	 */
	public enum IndustryType {
		农等("00001","农、林、牧、渔业"),
		食品等("00002","食品、药品、工业原料、服装、日用品等制造业"),
		电力等("00003","电力、热力、燃气及水生产和供应业"),
		建筑业("00004","建筑业"),
		批发和零售业("00005","批发和零售业"),
		交通运输等("00006","交通运输、仓储和邮政业"),
		住宿等("00007","住宿、旅游、餐饮业"),
		信息传输等("00008","信息传输、软件和信息技术服务业"),
		金融业("00009","金融业"),
		房地产业("00010","房地产业"),
		租赁和商务服务业("00011","租赁和商务服务业"),
		科学研究和技术服务业("00012","科学研究和技术服务业"),
		水利等("00013","水利、环境和公共设施管理业"),
		居民服务等("00014","居民服务、修理和其他服务业"),
		教育("00015","教育、培训"),
		卫生("00016","卫生、医疗、社会保障、社会福利"),
		文化等("00017","文化、体育和娱乐业"),
		政府等("00018","政府、非赢利机构和社会组织"),
		警察等("00019","警察、消防、军人"),
		其他("00020","其他"),
		能源("00021","能源、采矿业");
		
		/** value*/
		String value;
		
		/** code*/
		String code;
		
		IndustryType(String code, String value) {
			this.code = code;
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}
		
		/**
		 * 跟据index 获得枚举
		 * @param index
		 * @return
		 */
		public static IndustryType get(String index){
			for (IndustryType industryType : IndustryType.values()) {
				if (industryType.getCode().equals(index)) {
					return industryType;
				}
			}
			return null;
		}
		
		public static String getString(String index){
			for (IndustryType industryType : IndustryType.values()) {
				if (industryType.getValue().equals(index)) {
					return industryType.getCode();
				}
			}
			return null;
		}
		
		@Override
		public String toString() {
			Map<String, String> map = new HashMap<String,String>();
			map.put("id", code);
			map.put("text", value);
			return JSONObject.toJSONString(map);
		}
	}
}
