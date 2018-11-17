package com.zdmoney.credit.common.constant;

/**
 * @author 10098  2017年5月17日 上午10:12:06
 */
public enum LoanStatueLufaxEnum {
	正常("04","正常"), // 还款计划无逾期、无当期代偿
	逾期("05","逾期"), // 还款计划出现逾期，借款人未还款，包括逾期代偿、一次性回购
	结清("06","结清"), // 最后一期正常结清或者提前结清
	/*代偿("07","代偿"),*/
	追偿结清("10","追偿结清"),// 最后一期准备金代偿、准备金一次性回购、最后一期保证金代偿、保证金一次性回购，后续客户完全结清此笔借款
	准备金代偿结清("11","准备金代偿结清"),// 最后一期准备金代偿完成或者准备金一次性回购完成
	保证金代偿结清("12","保证金代偿结清");// 最后一期保证金代偿完成或者保证金一次性回购完成
	
	private String code;
	private String value;
	private LoanStatueLufaxEnum(String code, String value){
		this.code = code;
		this.value = value;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
