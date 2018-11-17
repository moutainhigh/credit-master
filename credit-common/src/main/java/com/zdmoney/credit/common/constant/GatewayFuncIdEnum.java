package com.zdmoney.credit.common.constant;

/**
 * Created by ym10094 on 2016/11/18.
 * 网关接口业务工单Id
 */
public enum GatewayFuncIdEnum {
    放款申请业务功能号("abs100002","放款申请",""),
    一次性提前清贷业务功能号("abs100005","一次性提前清贷",""),
    扣款结果上传("abs100008","扣款结果上传","http://10.8.30.28:8080/creditGateway-web/preRequest"),
    上传待回购信息("abs100006","上传待回购信息","http://10.8.30.28:8080/creditGateway-web/preRequest"),
    影像资料上传("abs1000110","影像资料上传",""),
    对公还款("abs100007","对公还款","http://10.8.30.48:8080/creditGateway-web/preRequest"),
    包商银行放款申请业务功能号("bsb100005","包银放款申请","http://10.8.30.48:8080/creditGateway-web/preRequest"),
    包商银行放款试算业务功能号("bsb100001","包银放款试算","http://10.8.30.48:8080/creditGateway-web/preRequest"),
 	包商银行查询协议接口("bsb100011","包商银行查询协议接口","http://10.8.30.48:8080/creditGateway-web/preRequest"),
	包商银行还款("bsb100009","包银还款","http://10.8.30.48:8080/creditGateway-web/preRequest"),
    包商银行还款结果查询("bsb100008","包银还款结果查询","http://10.8.30.48:8080/creditGateway-web/preRequest"),
    包商银行提前结清试算接口("bsb100017","包银提前结清试算接口","http://10.8.30.48:8080/creditGateway-web/preRequest"),
    电话号码变更("bsb100019","电话号码变更","http://10.8.30.48:8080/creditGateway-web/preRequest"),
    外贸3放款申请("wm3_2101","外贸3放款申请",""),
    外贸3放款申请结果查询("wm3_2102","外贸3放款申请结果查询",""),
    外贸3还款计划上传("wm3_2201","外贸3还款计划上传",""),
    外贸3扣款信息上传("wm3_2301","外贸3扣款信息上传",""),
    外贸3线上扣款("wm3_2311","外贸3线上扣款",""),
    外贸3线下实收("wm3_2312","外贸3线下实收",""),
    外贸3扣款查询("wm3_2313","外贸3扣款查询",""),
    外贸3溢缴款账户开户("wm3_5101","外贸3溢缴款账户开户",""),
    外贸3溢缴款账户充值("wm3_5102","外贸3溢缴款账户充值",""),
    外贸3溢缴款账户充值结果查询("wm3_5104","外贸3溢缴款账户充值结果查询",""),
    外贸3账户开户("wm3_5201","外贸3账户开户",""),
    外贸3还款账号单笔维护("wm3_2501","外贸3还款账号单笔维护",""),
    陆金所确认合同接口("lufax100003","陆金所确认合同接口",""),
    陆金所证大推送还款计划和资金分配计划("lufax100007","陆金所证大推送还款计划和资金分配计划",""),
    陆金所扣款指令("lufax100009","陆金所扣款指令",""),
    证大同步还款计划给陆金所("lufax100010","证大同步还款计划给陆金所",""),
    证大同步还款明细给陆金所("lufax100011","证大同步还款明细给陆金所",""),
    证大同步还款记录给陆金所("lufax100012","证大同步还款记录给陆金所",""),
    证大调用对公虚拟户充值接口("lufax100014","证大调用对公虚拟户充值接口",""),
    证大调用对公虚拟户提现请求接口("lufax100015","证大调用对公虚拟户提现请求接口",""),
    证大同步借据信息给交易核算接口("lufax100016","证大同步借据信息给交易核算接口",""),
    证大同步正式还款计划借据接口("lufax100017","证大同步正式还款计划借据接口",""),
    陆金所一次性回购("lufax100018","陆金所一次性回购",""),
    证大同步追偿还款记录接口("lufax100019","证大同步追偿还款记录接口",""),
    证大同步追偿还款计划接口("lufax100020","证大同步追偿还款计划接口","");
    private String code;
    private String value;
    private String url;
    
    GatewayFuncIdEnum(String code, String value, String url) {
        this.code = code;
        this.value = value;
        this.url  = url;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
