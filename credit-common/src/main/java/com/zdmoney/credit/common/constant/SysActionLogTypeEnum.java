package com.zdmoney.credit.common.constant;
/**
 * 操作日志type
 * @author 00232949
 *
 */
public enum SysActionLogTypeEnum {
	登陆("登陆"),
	新增("新增"),
	删除("删除"),
	更新("更新"),
	查询("查询"),
	导出("导出"),
	导入("导入"),
	其他("其他");
	
	SysActionLogTypeEnum(String value){
		this.value=value;
	}
	private String value;
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
