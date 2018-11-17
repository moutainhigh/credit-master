package com.zdmoney.credit.common.constant;

/**
 * 合同来源
 * @author 00232949
 *
 */
public enum FundsSourcesTypeEnum {
	
	证大P2P("00001","证大P2P"), 
	证大爱特("00003","证大爱特"), 
	证大爱特2("00006","证大爱特2"), 
	积木盒子("00007","积木盒子"), 
	向上360("00002","向上360P"), 
	华澳信托("00005","华澳信托"), 
	国民信托("00004","国民信托"),
	挖财2("00008","挖财2"),
	海门小贷("00011","海门小贷"),
	渤海信托("00012","渤海信托"),
	龙信小贷("00013","龙信小贷"),
	外贸信托("00014","外贸信托"),
	渤海2("00015","渤海2"),
	捞财宝("00016","捞财宝"),
	外贸2("00017","外贸2"),
	包商银行("00018","包商银行"),
	外贸3("00019","外贸3"),
	华瑞渤海("00020","华瑞渤海"),
	陆金所("00021","陆金所");

	
	/** value*/
	private String value;
	
	/** code*/
	private String code;
	
	private FundsSourcesTypeEnum(String code, String value) {
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