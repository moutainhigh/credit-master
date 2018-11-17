package com.zdmoney.credit.common.constant.repay;

import java.util.ArrayList;
import java.util.List;

public class YubokuanConst {
    /**
     * 编辑放款申请明细标题字段
     * @return
     */
    public static List<String> getLoanApplyDetailLabels(){
        List<String> resultList = new ArrayList<String>();
        resultList.add("信托项目简码");
        resultList.add("合同号/借款编号");
        resultList.add("申请地点");//使用开户省市码表
        resultList.add("申请号");//取合同号
        resultList.add("商品类型");//默认为信托
        resultList.add("证件类型");
        resultList.add("证件号码");
        resultList.add("姓名");
        resultList.add("联系电话");
        resultList.add("移动电话");
        resultList.add("邮政编码");
        resultList.add("通讯地址");
        resultList.add("申请用途");//默认消费贷款
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
        resultList.add("宽限期天数");
            resultList.add("缴费方式");
        resultList.add("贷款类型");
        resultList.add("借款人类型");
        resultList.add("产品编号");
        resultList.add("产品名称");
        resultList.add("手续费");
        resultList.add("手续费率");
        resultList.add("利率");
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
        resultList.add("划拨申请书文件名称");//取前一次划拨申请书文件名称
        resultList.add("放款日期");//实际放款日期
        resultList.add("到期日期");//最后一期还款日
        resultList.add("利率类型");
        resultList.add("起息日期");//取放款日期
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
        resultList.add("channel");//默认为信托
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
        resultList.add("graceDays");//宽限天数
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
}
