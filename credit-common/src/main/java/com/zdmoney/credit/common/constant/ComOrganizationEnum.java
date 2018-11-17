package com.zdmoney.credit.common.constant;


/**
 * 营业网点层级枚举
 * @author Ivan
 *
 */
public class ComOrganizationEnum {
	
	/** 公司层级 **/
	public static final int INDEX_COMPANY = 1;
	/** 区域层级 **/
	public static final int INDEX_AREA = 2;
	/** 分部层级 **/
	public static final int INDEX_BRANCH = 3;
	/** 城市层级 **/
	public static final int INDEX_CITY = 4;
	/** 分店、营业部层级 **/
	public static final int INDEX_DEPARTMENT = 5;
	/** 团队、组层级 **/
	public static final int INDEX_TEAM = 6;
	
	/**
	 * 跟据level 获得枚举
	 * @param level
	 * @return
	 */
	public static Level get(String levelStr){
		for (Level level : Level.values()) {
			if (level.getName().equals(levelStr)) {
				return level;
			}
		}
		return null;
	}
	
	/**
	 * 跟据index 获得枚举
	 * @param index
	 * @return
	 */
	public static Level get(int index){
		for (Level level : Level.values()) {
			if (level.getIndex() == index) {
				return level;
			}
		}
		return null;
	}
	
	public enum Level {
		/** 公司 **/
		V100("V100","01",1,2),
		/** 区域 **/
		V101("V101","01",2,4),
		/** 分部 **/
		V102("V102","0001",3,8),
		/** 城市 **/
		V103("V103","0001",4,12),
		/** 分店、营业部 **/
		V104("V104","00001",5,17),
		/** 团队、组 **/
		V105("V105","00001",6,22);
		
		Level(String name,String initCode,int index,int maxCodeLen) {
			this.name = name;
			this.initCode = initCode;
			this.index = index;
			this.maxCodeLen = maxCodeLen;
		}
		
		private String initCode;
		/** 层级标识 V100,V101,V102,V103,V104,V105 **/
		private String name;
		/** 枚举索引 **/
		private int index;
		/** 完整Code总长度 **/
		private int maxCodeLen;
		public String getInitCode() {
			return initCode;
		}
		public void setInitCode(String initCode) {
			this.initCode = initCode;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		
		public int getIndex() {
			return index;
		}
		public void setIndex(int index) {
			this.index = index;
		}
		
		public int getMaxCodeLen() {
			return maxCodeLen;
		}
		public void setMaxCodeLen(int maxCodeLen) {
			this.maxCodeLen = maxCodeLen;
		}
		public Level nextLevel() {
			int index = this.getIndex();
			index++;
			return ComOrganizationEnum.get(index);
		}
		
		public Level prevLevel() {
			int index = this.getIndex();
			index--;
			return ComOrganizationEnum.get(index);
		}
	}
	
}