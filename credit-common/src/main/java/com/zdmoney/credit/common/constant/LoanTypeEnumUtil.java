package com.zdmoney.credit.common.constant;


/**
 * 产品类型枚举
 * @author YM10104
 *
 */
public class LoanTypeEnumUtil {
	
	/**
	 * 跟据code 获得枚举
	 * @param
	 * @return
	 */
	public static String getName(String code){
		for (LoanType loanType : LoanType.values()) {
			if (loanType.getCode().equals(code)) {
				return loanType.getName();
			}
		}
		return null;
	}
	
	
	public enum LoanType {
		随薪贷("00001","随薪贷"),
	    随意贷("00002","随意贷"),
	    随意贷A("00003","随意贷A"),
	    随意贷B("00004","随意贷B"),
	    随意贷C("00005","随意贷C"),
	    随房贷("00006","随房贷"),
	    助学贷("00007","助学贷"),
	    车贷("00008","车贷"),
	    薪生贷("00009","薪生贷"),
	    随车贷("00010","随车贷"),
	    随房贷A("00011","随房贷A"),
	    随房贷B("00012","随房贷B"),
	    公积金贷("00013","公积金贷"),
	    保单贷("00014","保单贷"),
	    网购达人贷A("00015","网购达人贷A"),
	    网购达人贷B("00020","网购达人贷B"),
	    淘宝商户贷("00016","淘宝商户贷"),
	    学历贷("00017","学历贷"),
	    卡友贷("00018","卡友贷");	
		
		LoanType(String code,String name) {
			this.code = code;
			this.name = name;	
		}
		
		private String code;
		
		private String name;

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
	}
}