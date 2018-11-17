package com.zdmoney.credit.common.constant.repay;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RequestManagementConst {
    
    /** 信托项目简码 **/
    public final static String TRUST_PROJECT_CODE = "ZDCF01";
  
    /** 外贸信托 默认项目号 **/
    public final static String WMXT_PROJECT_CODE = "016";
    
    /** 信托项目简码 **/
    public final static String TRUST_PROJECT_CODE2 = "ZDCF02";
    /** 信托项目简码 **/
    public final static String TRUST_PROJECT_CODE3 = "ZDCF03";
    
    /** 外贸信托 默认机构代码 **/
    public final static String WMXT_ORG_CODE = "01603";
    /** 文件生成的临时目录 */
    public static final String FILE_TEMP_PATH = "tmp" + File.separator +"request";
    
    /** 文件编码格式 */
    public static final String STRING_CHARSET = "GBK";
    
    /** 转译分隔符（|+|） **/
    public static final String SEPARATOR = "|+|";
    
    /** 交易标志：代付 **/
    public final static String TRADE_MARK_F = "F";
    /** 交易标志：代收 **/
    public final static String TRADE_MARK_S = "S";
    /** 商户号 **/
    public final static String MERCHANT_NO = "   ";
    /** 业务类型 **/
    public final static String BUSINESS_TYPE = "09900";
    /** 版本号 **/
    public final static String VERSION = "02";
    /** 未导出报盘文件 **/
    public final static String EXPORT_FILE_F = "f";
    /** 已导出报盘文件 **/
    public final static String EXPORT_FILE_T = "t";
    /** 记录号起始位 **/
    public final static String START_RECORD_NO = "00001";
    /** 通联支付用户编号 **/
    public final static String TL_PAYMENT_NUMBER = "0000000001";
    /** 账号类型 **/
    public final static String ACCOUNT_NUMBER_TYPE = "00";
    /** 账户类型 **/
    public final static String ACCOUNT_TYPE = "0";
    /** 公司账户：银行代码 **/
    public final static String ZD_BANK_CODE = "303";
    /** 公司账户：开户行名称 **/
    public final static String ZD_BANK_NAME = "光大银行上海松江支行";
    /** 公司账户：账号 **/
    public final static String ZD_ACCOUNT_NUMBER = "36620188000268095";
    /** 公司账户：账户名称 **/
    public final static String ZD_ACCOUNT_NAME = "上海证大投资咨询有限公司";
    /** 回盘成功反馈码 **/
    public final static String SUCCESS_BACK_CODE = "0000";
    /** 回盘失败反馈码（余额不足） **/
    public final static String FAILURE_BACK_CODE = "3008";

    public final static  String FLAG_FILE_NAME="flag.ok";

    public final static String ESIGNATURE_SUCCESS = "0000"; //签章成功标志

    public final static String ESIGNATURE_REQUEST_CODE = "020001"; //签章请求码

//    public final static String APPLY_COMMPAY_CHOP_NAME = "上海证大投资咨询有限公司（公章/财务章/业务章）" ;//划拨申请书公章名
    public final static String APPLY_COMMPAY_CHOP_NAME = "上海证大投资咨询有限公司业务公章" ;//划拨申请书公章名
    public final static String ESIGNATURE_REQUEST_SYSID = "credit";//签章系统请求码
    /**
     * 编辑放款申请明细标题字段
     * @return
     */
    public static List<String> getLoanApplyDetailLabels(){
        List<String> resultList = new ArrayList<String>();
        resultList.add("信托项目简码");
        resultList.add("合同号/借款编号");
        resultList.add("申请地点");
        resultList.add("申请号");
        resultList.add("渠道来源");
        resultList.add("证件类型");
        resultList.add("证件号码");
        resultList.add("姓名");
        resultList.add("联系电话");
        resultList.add("移动电话");
        resultList.add("邮政编码");
        resultList.add("通讯地址");
        resultList.add("申请用途");
        resultList.add("合同金额");
        resultList.add("实付金额");
        resultList.add("申请币种");
        resultList.add("申请期限(月)");
        resultList.add("还款帐户类型");
        resultList.add("还款帐号");
        resultList.add("还款方式");
        resultList.add("扣款日类型");
        resultList.add("扣款日类别");
        resultList.add("扣款日期");
        resultList.add("婚姻状况");
        resultList.add("学历");
        resultList.add("户籍");
        resultList.add("个人月收入");
        resultList.add("家庭住址");
        resultList.add("家庭邮编");
        resultList.add("住宅电话");
        resultList.add("经办机构");
            resultList.add("缴费方式");
        resultList.add("贷款类型");
        resultList.add("借款人类型");
        resultList.add("产品编号");
        resultList.add("产品名称");
        resultList.add("手续费");
        resultList.add("手续费率");
        resultList.add("利率(月)");
        resultList.add("提前还款违约金比率");
        resultList.add("罚息率(月)");
        resultList.add("履行担保天数");
        resultList.add("服务费");
        resultList.add("服务费率");
        resultList.add("担保费");
        resultList.add("担保费率");
        resultList.add("银行代码");
        resultList.add("开户省市");
        resultList.add("费用一");
        resultList.add("费用二");
        resultList.add("费用三");
        resultList.add("费用四");
        resultList.add("费用五");
        resultList.add("职业");
        resultList.add("单位名称");
        resultList.add("单位所属行业");
        resultList.add("单位地址");
        resultList.add("单位邮政编码");
        resultList.add("本单位工作起始年份");
        resultList.add("本人职务");
        resultList.add("本人职称");
        resultList.add("担保方式");
        resultList.add("担保人姓名");
        resultList.add("担保人证件类型");
        resultList.add("担保人证件号码");
        resultList.add("担保金额");
        resultList.add("担保关系");
        resultList.add("放款账户类型(*)");
        resultList.add("放款银行代码(*)");
        resultList.add("放款账户名称(*)");
        resultList.add("放款账户号码(*)");
        resultList.add("放款账户开户支行");
        resultList.add("放款账户开户所在省");
        resultList.add("放款账户开户所在市");
        resultList.add("划拨申请书文件名称");
        resultList.add("放款日期");
        resultList.add("到期日期");
        resultList.add("利率类型");
        resultList.add("起息日期");
        return resultList;
    }
    
    /**
     * 编辑放款申请明细明细字段
     * @return
     */
    public static List<String> getLoanApplyDetailFields(){
        List<String> resultList = new ArrayList<String>();
        resultList.add("creditCode");
        resultList.add("contractNum");
        resultList.add("requestPlace");
        resultList.add("requestNo");
        resultList.add("channel");
        resultList.add("idType");
        resultList.add("idnum");
        resultList.add("name");
        resultList.add("contactPhone");
        resultList.add("mphone");
        resultList.add("postcode");
        resultList.add("address");
        resultList.add("purpose");
        resultList.add("pactMoney");
        resultList.add("money");
        resultList.add("currency");
        resultList.add("time");
        resultList.add("accountType");
        resultList.add("backAccount");
        resultList.add("repaymentMethod");
        resultList.add("repaymentDayType");
        resultList.add("repaymentDayCategory");
        resultList.add("promiseReturnDate");
        resultList.add("married");
        resultList.add("edLevel");
        resultList.add("hrAddress");
        resultList.add("totalMonthlyIncome");
        resultList.add("familyAddress");
        resultList.add("familyPostcode");
        resultList.add("familyPhone");
        resultList.add("handleOrg");
        resultList.add("payMethod");
        resultList.add("loanType");
        resultList.add("customerType");
        resultList.add("productCode");
        resultList.add("productName");
        resultList.add("counterFee");
        resultList.add("counterRate");
        resultList.add("rate");
        resultList.add("defaultRate");
        resultList.add("penaltyRate");
        resultList.add("assureDays");
        resultList.add("serviceFee");
        resultList.add("serviceFeeRate");
        resultList.add("assureFee");
        resultList.add("assureFeeRate");
        resultList.add("bankCode");
        resultList.add("openAccountCity");
        resultList.add("fee1");
        resultList.add("fee2");
        resultList.add("fee3");
        resultList.add("fee4");
        resultList.add("fee5");
        resultList.add("profession");
        resultList.add("company");
        resultList.add("industryType");
        resultList.add("cAddress");
        resultList.add("cCode");
        resultList.add("startYear");
        resultList.add("officialRank");
        resultList.add("staff");
        resultList.add("assureMethod");
        resultList.add("assureName");
        resultList.add("assureIdType");
        resultList.add("assureIdnum");
        resultList.add("assureAmount");
        resultList.add("assureRelation");
        resultList.add("lenderAcountType");
        resultList.add("lenderBankCode");
        resultList.add("lenderAcountName");
        resultList.add("lenderAcount");
        resultList.add("lenderBranchBank");
        resultList.add("lenderAcountProvince");
        resultList.add("lenderAcountCity");
        resultList.add("applyName");
        resultList.add("loanDate");
        resultList.add("endrdate");
        resultList.add("rateType");
        resultList.add("valueDate");
        return resultList;
    }
    
    /**
     * 编辑还款计划细标题字段
     * @return
     */
    public static List<String> getPayPlanLabels(){
        List<String> resultList = new ArrayList<String>();
        resultList.add("信托项目简码");
        resultList.add("借款编号");
        resultList.add("还款类型");
        resultList.add("期次");
        resultList.add("应还款日期");
        resultList.add("应还本金");
        resultList.add("应还利息");
        resultList.add("应还手续费");
        resultList.add("应还担保费");
        resultList.add("应还服务费");
        resultList.add("应还其他费1");
        resultList.add("应还其他费2");
        resultList.add("应还其他费3");
        resultList.add("剩余本金");
        resultList.add("一次性结清应还金额");
        return resultList;
    }
    
    /**
     * 编辑还款计划细标题字段
     * @return
     */
    public static List<String> getPayPlanFields(){
        List<String> resultList = new ArrayList<String>();
        resultList.add("creditCode");
        resultList.add("contractNum");
        resultList.add("repaymentType");
        resultList.add("currentTerm");
        resultList.add("returnDate");
        resultList.add("currentPrincipal");
        resultList.add("currentAccrual");
        resultList.add("counterFee");
        resultList.add("assureFee");
        resultList.add("serviceFee");
        resultList.add("fee1");
        resultList.add("fee2");
        resultList.add("fee3");
        resultList.add("principalBalance");
        resultList.add("repaymentAll");
        return resultList;
    }
    
    /**
     * 编辑还款计划导出字段
     */
    public static List<String> getRetuPlanFields(){
    	List<String> resultList = new ArrayList<String>();
    	resultList.add("orgCode"); //机构代码
    	resultList.add("contractNum"); //合同号
    	resultList.add("currentTerm");//期次
    	resultList.add("returnDateStr");//应还款日期
    	resultList.add("returnBalance");//应还本金
    	resultList.add("currentAccrual"); //应还利息
    	resultList.add("principalBalance"); // 剩余本金
    	return resultList;
    }
    
    /**
     * 债权导出供理财 字段
     */
    public static List<String> getDebtInfoFields(){
    	List<String> list = new ArrayList<String>();
    	list.add("orgCode");
    	list.add("contractNum");
    	list.add("applyAreaCode");
    	list.add("applyNum");
    	list.add("channelSource");
    	list.add("idType");
    	list.add("idNum");
    	list.add("borrowName");
    	list.add("telephone");
    	list.add("mobilePhone");
    	list.add("postCode");
    	list.add("address");
    	list.add("purpose");
    	list.add("contractMoney");
    	list.add("actualMoney");
    	list.add("currency");
    	list.add("applyTerm");
    	list.add("repayAccountType");
    	list.add("repayAccountNum");
    	list.add("repayType");
    	list.add("repayDayType");
    	list.add("repayDayCategory");
    	list.add("repayDay");
    	list.add("marryStatus");
    	list.add("edLevel");
    	list.add("hrAddressCode");
    	list.add("personMonthIncome");
    	list.add("familyAddress");
    	list.add("familyTelephone");
    	list.add("familyPostCode");
    	list.add("handleOrgName");
    	list.add("paymentMethod");
    	list.add("loanType");
    	list.add("borrowerType");
    	list.add("productNum");
    	list.add("productType");
    	list.add("handlingCharge");
    	list.add("chargeRate");
    	list.add("accrualEM");
    	list.add("prepayPenalRate");
    	list.add("penaltyRate");
    	list.add("guaranteeDays");
    	list.add("serviceMoney");
    	list.add("serviceRate");
    	list.add("guaranteeMoney");
    	list.add("guaranteeRate");
    	list.add("repayBankCode");
    	list.add("bankCityCode");
    	list.add("fare1");
    	list.add("fare2");
    	list.add("fare3");
    	list.add("fare4");
    	list.add("fare5");
    	list.add("lendDate");
    	list.add("lendAccountType");
    	list.add("lendBankCode");
    	list.add("lendAccountName");
    	list.add("lendAccountNum");
    	return list;
    }
    
    
    /**
     * 编辑清分分账信息标题字段
     * @return
     */
    public static List<String> getRepayStateDetailFields(){
        List<String> resultList = new ArrayList<String>();
        resultList.add("projectCode");//信托项目简码
        resultList.add("contractNum");//合同编号/借款编号
        resultList.add("term");//期次
        resultList.add("returnDate");//应还款日期
        resultList.add("lastDay");//宽限期最后一天
        resultList.add("returnCorpus");//应还本金
        resultList.add("returnAccrual");//应还利息 	
        resultList.add("returnCorpusOine");//应还本金罚息
        resultList.add("returnAccrualOine");//应还利息罚息
        resultList.add("realReturnDate");//实际还款日期
        resultList.add("realCorpus");//实还本金
        resultList.add("realAccrual");//实还利息
        resultList.add("realCorpusOine");//实还本金罚息
        resultList.add("realAccrualOine");//实还利息罚息
        resultList.add("currentPayOffFlag");//本期结清标志
        resultList.add("update");//更新日期
        resultList.add("accountState");//账户状态
        resultList.add("payOffDate");//结清日期
        resultList.add("overdueCorpus");//逾期本金
        resultList.add("overdueAccrual");//逾期利息
        return resultList;
    }
}
