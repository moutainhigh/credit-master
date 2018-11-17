package com.zdmoney.credit.api.app.vo;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * 接口Vo对象
 * 
 * @author Ivan
 *
 */
public class SendMobileCodeVo {
	@NotEmpty(message = "数据类型参数为空")
	@Length(max = 1, message = "数据类型参数超过1个字符")
	private String dataType;

	@NotEmpty(message = "渠道参数为空")
	@Length(max = 1, message = "渠道参数超过1个字符")
	private String channel;

	@NotEmpty(message = "数据信息参数为空")
	@Length(max = 30, message = "数据信息参数超过30个字符")
	private String dataInfo;

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getDataInfo() {
		return dataInfo;
	}

	public void setDataInfo(String dataInfo) {
		this.dataInfo = dataInfo;
	}
}
