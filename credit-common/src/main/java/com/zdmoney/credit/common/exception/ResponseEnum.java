package com.zdmoney.credit.common.exception;

/**
 * 返回响应状态枚举
 * @author Ivan
 *
 */
public enum ResponseEnum {
	
	/** 格式校验枚举 **/
	VALIDATE_ISNULL("100001","{0} 数据项为空!"),
	VALIDATE_FORMAT("100002","{0} 数据项格式有误!"),
	VALIDATE_RESULT_ISNULL("100003","{0} 结果集为空!"),
	VALIDATE_ENUM_ERROR("100004","{0} 枚举项解析异常! {1}"),
	VALIDATE_BEANTOMAP_ERROR("100005","Bean转换Map异常!"),
	VALIDATE_COLLECTION_ISNULL("100006","{0} 集合数据项为空!"),
	VALIDATE_ARRAY_ISNULL("100007","{0} 数组项为空!"),
	
	/** 系统枚举 **/
	SYS_SUCCESS("000000","正常"),
	SYS_FAILD("900000","系统忙"),
	SYS_EXIST("900000","已经存在，请重新输入"),
	SYS_ErrorActionCode("900001","没有操作权限"),
	SYS_SessionOutActionCode("900002","会话超时"),
	SYS_WARN("000001","可忽略异常"),
	
	/** 异常信息完全自定义 **/
	FULL_MSG("800000","{0}"),
	
	/** 员工模块枚举 **/
	EMPLOYEE_PASSWORD_WRONG("000001","工号不存在或密码错误!"),
	EMPLOYEE_PASSWORD_NOT_EQ("000002", "两次密码输入不一致！"),
	EMPLOYEE_ILLEGAL_PWD("000003", "密码不符合规则，密码必须是6位-10位字母和数字的组合"),
	
	EMPLOYEE_USERCODE_NOT_WRONG("000004", "员工工号填写错误请重新填写"),
	
	EMPLOYEE_USERCODE_NOT_EXIST("000005", "员工工号不能为空"),
	
	EMPLOYEE_EXIST("000006","该员工已经录入"),
	
	
	
	/** 员工Email为空*/
	EMAIL_NOT_EXIST("000001", "该用户邮箱未填写！请联系后线支持室更新用户信息，填写邮箱。"),
	
	/** 营业网点模块枚举 **/
	ORGANIZATION_OUT_LEVEL("000001","超出层级范围"),
	ORGANIZATION_CANNOT_DELETE("000002","存在子结点无法删除"),
	ORGANIZATION_NOT_WRONG("000003","营业部不能为空"),
	ORGANIZATION_EXIST("000004","该部门已录入"),
	
	/**债权导出模块   **/
	LOANEXTERNALDEBT_FAIL("000001","选择的部分债权已被分配到其它批次，请重新选择！"),
	
	/** 文件上传枚举 **/
//	ERROR_TIME_RANGE("000001","生成报盘前后的15分钟内不允许进行管理营业部调整，请稍后再试"),
	FILE_EMPTY_FILE("F000001","导入的文件为空，拒绝导入！"),
	FILE_SIZE_OUT_RANGE("F000002","导入的文件过大，文件大小不能超过 {0}！"),
	FILE_ERROR_TYPE("F000003","导入的文件类型错误，拒绝导入！"),
	FILE_ERROR_PARSE("F000004","解析文件出错：{0}！"),
	FILE_ERROR_WRITE("F000005","写入文件出错：{0}！"),
	FILE_ERROR_FORMAT_CONTENT("F000006","文件内容必须从第{0}行开始！"),
	FILE_NO_FILE("F000007", "所需文件未生成或已失效！"),
	
	/**	 * 实时划扣信息	 */
	REALTIMEDEDUCTPRE_SUCCESS("000000","操作成功"),
	REALTIMEDEDUCTPRE_FAILD_ERROR("000001","输入用户名和身份证不匹配"),
	REALTIMEDEDUCTPRE_FAILD_OperateERROR("000002","操作失败"),
	
	/** 第三方HTTP接口 **/
	HTTP_RESPONSE_ERROR("H000001","第三方接口{0}异常：{1}"),
	
	CREATEOFFERINFO_AMOUNT_ZERO("C000001","债权报盘金额为0，报盘文件未创建"),

	/** 网关接口响应 **/
	//GATEWAY_FINANCEGRANT_REQ_NULL("10001","请求参数为空!"),
	//GATEWAY_FINANCEGRANT_REQ_PACTN0_NOTEXIT("10002","预审批ID不存在"),
	GATEWAY_FINANCEGRANT_DIS_GRANTI_FAIL("10010","调用核心放款接口失败"),
	GATEWAY_FINANCEGRANT_DIS_GRANTI_SUCC("10011","调用核心放款接口成功"),
	GATEWAY_FINANCEGRANT_DIS_SERVERFEE_FAIL("10013","生成服务费失败"),
	GATEWAY_FINANCEGRANT_DIS_GRANTI_UPDATE("10012","更新成功"),
	GATEWAY_FINANCEGRANT_SUCC("1","成功"),
	GATEWAY_FINANCEGRANT_FAIL("2","{0}"),
	GATEWAY_ONETIMESETTLEMENT_SUCC("1","成功"),
	GATEWAY_ONETIMESETTLEMENT_FAIL("2","{0}"),
	/**待回购信息**/
	GATEWAY_LOANBACK_PUSH_REQ_DEFECT("10001","必须的请求参数缺失!"),
	GATEWAY_LOANBACK_PUSH_LOAN_NOTFOUND("10002","未找到该合同对应的债权信息！"),
	GATEWAY_LOANBACK_PUSH_GRANTI_SUCC("1","处理成功"),
	GATEWAY_LOANBACK_PUSH_GRANTI_FAIL("2","处理失败"),
	GATEWAY_LOANBACK_SUCC("10011","成功"),
	GATEWAY_LOANBACK_FAIL("10012","失败"),
	GATEWAY_LOANBACK_UPLOAD_SUCC("SUCCESS","成功"),
	GATEWAY_LOANBACK_UPLOAD_FAIL("FAIL","失败");

	private final String code;
	private final String desc;

	private ResponseEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public String getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}
	
	
	
	
}
