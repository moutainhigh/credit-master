package com.zdmoney.credit.common.util.excel;

import java.util.HashMap;
import java.util.Map;

/**
 * Excel导入数据格式定义
 * @author Ivan
 *
 */
public class ExcelTemplet {
    
    /** 正文数据从某行读取 **/
    protected int startRow;
    /** sheet 名称 **/
    protected String sheetName;
    /** 单元格数据对照Map **/
    protected Map<String,String> columnMap = new HashMap<String,String>();
    /** 导入数据处理的结果写入的单元格字母索引 如（A,B,C....） **/
    protected String feedBackColumnName;
    /** 导入数据处理的结果 保存Map的Key **/
    public static final String FEED_BACK_MSG = "feedBackMsg";

    public int getStartRow() {
        return startRow;
    }

    public String getSheetName() {
        return sheetName;
    }

    public Map<String, String> getColumnMap() {
        return columnMap;
    }
    
    public String getFeedBackColumnName() {
        return feedBackColumnName;
    }

    /** 还款录入数据格式 **/
    public class RepaymentInputExcel extends ExcelTemplet {
    public RepaymentInputExcel() {
        sheetName = "";
        
        startRow = 2;
        
        columnMap.put("A", "name");
        columnMap.put("B", "idNum");
        columnMap.put("C", "loanType");
        columnMap.put("D", "repayDate");
        columnMap.put("E", "repayAmount");
        columnMap.put("F", "tradeType");
        columnMap.put("G", "contractNum");
        columnMap.put("H", "memo");
        columnMap.put("I", "repayNo"); 
        feedBackColumnName = "J";
    }
    }
    
    /** 管理门店变更导入数据模板格式 **/
    public class ManagerSalesDeptInputExcel extends ExcelTemplet {
        public ManagerSalesDeptInputExcel() {
            sheetName = "";
            startRow = 2;
            columnMap.put("A", "name");
            columnMap.put("B", "idNum");
            columnMap.put("C", "loanType");
            columnMap.put("D", "signDate");
            columnMap.put("E", "manageSalesDept");
            columnMap.put("F", "contractNum");
            feedBackColumnName = "G";
        }
    }
    
    /** 对公还款批量导入数据模板格式 **/
    public class PublicAccountInfoInputExcel extends ExcelTemplet {
        public PublicAccountInfoInputExcel() {
            sheetName = "";
            startRow = 2;
            columnMap.put("A", "repayDate");
            columnMap.put("B", "firstAccount");
            columnMap.put("C", "secondAccount");
            columnMap.put("D", "time");
            columnMap.put("E", "type");
            columnMap.put("F", "amount");
            columnMap.put("G", "voucherNo");
            columnMap.put("H", "secondCompany");
            columnMap.put("I", "secondBank");
            columnMap.put("J", "purpose");
            columnMap.put("K", "remark");
            columnMap.put("L", "comments");
            feedBackColumnName = "M";
        }
    }
    
    /** 罚息减免申请批量导入数据模板格式 **/
    public class ReliefPenaltyStateInputExcel extends ExcelTemplet {
        public ReliefPenaltyStateInputExcel() {
            sheetName = "";
            startRow = 2;
            /** 借款人姓名 **/
            columnMap.put("A", "name");
            /** 借款人身份证 **/
            columnMap.put("B", "idNum");
            /** 申请人工号 **/
            columnMap.put("C", "userCode");
            /** 申请金额 **/
            columnMap.put("D", "amount");
            /** 合同编号 **/
            columnMap.put("E", "contractNum");
            
            feedBackColumnName = "H";
            
        }
    }
    
    /**
     * 债权导入模板   
     */
    public class LoanReturnExcel extends ExcelTemplet {
        public LoanReturnExcel() {
            sheetName = "";
            startRow= 2;//从哪一行开始
            columnMap.put("A", "loanType");//; 借款类型
            columnMap.put("B", "borrowerName");//姓名
            columnMap.put("C", "idNum");//身份证
            columnMap.put("D", "signDate"); //签约日期
            columnMap.put("E", "importReason");  // 导入原因
            columnMap.put("F", "contractNum");//合同编号
        }
    }
    
    public class BuyBackLoanExcel extends ExcelTemplet {
    	public BuyBackLoanExcel(){
            sheetName = "";
            startRow= 2;//从哪一行开始
            columnMap.put("A", "contractNum");//; 合同编号
            columnMap.put("B", "idNum");//身份证号
            columnMap.put("C", "buyBackTime");//回购时间
            columnMap.put("D", "amount"); //回购金额
            columnMap.put("E", "fundsSources");  // 合同来源
            columnMap.put("F", "loanBelongs");// 债权去向
    	}
    }
    
    public class LoanReturnWCExcel extends ExcelTemplet {
        public LoanReturnWCExcel() {
            sheetName = "";
            startRow= 2;//从哪一行开始
            columnMap.put("A", "id");//; 
            columnMap.put("B", "borrowerName");//姓名
            columnMap.put("C", "idNum");//身份证
            columnMap.put("D", "zrMoney"); //本次转让债权价值
            columnMap.put("E", "payMoney");  // 需支付对价
            columnMap.put("F", "workInfo");  // 借款人职业情况
            columnMap.put("G", "purpose");  //借款人借款用途
            columnMap.put("H", "startRDate");  //还款起始日期
            columnMap.put("I", "time");  // 还款期限（月）
            columnMap.put("J", "residualTime");  // 剩余还款月数
            columnMap.put("K", "rateEY");  // 预计债权收益率（年)
            columnMap.put("L", "contractNum");  // 合同编号
//            columnMap.put("M", "result");  // 结果

        }
    }
    /**
     * 随手记excel模板
     * @author 00235528
     *
     */
    public class LoanSsjExcel extends ExcelTemplet {
        public LoanSsjExcel() {
            sheetName = "";
            startRow= 2;//从哪一行开始
            columnMap.put("A", "id");//; 
            columnMap.put("B", "borrowerName");//借款人姓名
            columnMap.put("C", "idNum");//借款人身份证号码
            columnMap.put("D", "zrMoney"); //本次转让债权价值
            columnMap.put("E", "payMoney");  // 需支付对价
            columnMap.put("F", "workInfo");  // 借款人职业情况
            columnMap.put("G", "purpose");  //借款人借款用途
            columnMap.put("H", "startDate");  //还款起始日期
            columnMap.put("I", "time");  // 还款期限（月）
            columnMap.put("J", "residualTime");  // 剩余还款月数
            columnMap.put("K", "rateEY");  // 预计债权收益率（年)
            
            feedBackColumnName = "L";
            
        }
    }
    /**  黑名单导入   **/
    public class blackNameInfoExcel extends ExcelTemplet {
        public blackNameInfoExcel() {
             sheetName = "";
             startRow= 2;//从哪一行开始
             columnMap.put("A", "loanType");//; 
             columnMap.put("B", "borrowerName");//姓名
             columnMap.put("C", "idNum");//身份证
             columnMap.put("D", "signDate"); //签约日期
        }
    }
    
    
    /** 入职批量导入 **/
    public class entryImportInputExcel extends ExcelTemplet {
        public entryImportInputExcel() {
            sheetName = "";
            startRow = 2;
            columnMap.put("A", "userCode");
            columnMap.put("B", "name");
            columnMap.put("H", "areaName");
            columnMap.put("I", "cityName");
            columnMap.put("J", "workName");
            columnMap.put("K", "group");
            columnMap.put("L", "job");
            columnMap.put("M", "mobile");
            columnMap.put("N", "email");
            feedBackColumnName = "O";
        }
    }
    
    /** 离职批量导入 **/
    public class quitImportInputExcel extends ExcelTemplet {
        public quitImportInputExcel() {
            sheetName = "";
            startRow = 2;
            columnMap.put("A", "userCode");
            columnMap.put("B", "name");
            columnMap.put("H", "areaName");
            columnMap.put("I", "cityName");
            columnMap.put("J", "workName");
            columnMap.put("K", "group");
            columnMap.put("L", "job");
            columnMap.put("M", "quitTime");
            feedBackColumnName = "N";
        }
    }
    
    /** 对私还款批量导入数据模板格式 **/
    public class PrivateAccountInfoInputExcel extends ExcelTemplet {
        public PrivateAccountInfoInputExcel() {
            sheetName = "";
            startRow = 4;
            columnMap.put("A", "tradeDate");
            columnMap.put("B", "tradeTime");
            columnMap.put("C", "tradeAmount");
//            columnMap.put("D", "costAmount");
            columnMap.put("D", "currentBalance");
            columnMap.put("E", "secondName");
            columnMap.put("F", "secondAccount");
            columnMap.put("G", "tradeBank");
            columnMap.put("H", "tradeChannel");
            columnMap.put("I", "tradeType");
            columnMap.put("J", "tradePurpose");
            columnMap.put("K", "tradeRemark");
//            columnMap.put("L", "tradeDesc");
            feedBackColumnName = "N";
        }
    }
    
    /** 对公还款批量导入数据模板格式（新模板） **/
    public class NewPublicAccountInfoInputExcel extends ExcelTemplet {
        public NewPublicAccountInfoInputExcel() {
            sheetName = "";
            startRow = 2;
            columnMap.put("A", "repayDate");
            columnMap.put("B", "time");
            columnMap.put("C", "debitAmount");
            columnMap.put("D", "amount");
            columnMap.put("E", "accountBalance");
            columnMap.put("F", "secondAccount");
            columnMap.put("G", "secondCompany");
            columnMap.put("H", "voucherNo");
            columnMap.put("I", "tradeNo");
            columnMap.put("J", "remark");
            feedBackColumnName = "K";
        }
    }
    
    /**第三方线下代付导入回盘文件**/
    public class importHaTwoOfferExcel extends ExcelTemplet {
        public importHaTwoOfferExcel(){
            sheetName = "";
            startRow = 3;
            columnMap.put("A", "recordNumber");
            columnMap.put("B", "tlPaymentNumber");
            columnMap.put("C", "bankCode");
            columnMap.put("D", "accountNumberType");
            columnMap.put("E", "accountNumber");
            columnMap.put("F", "accountName");
            columnMap.put("G", "bankProvince");
            columnMap.put("H", "bankCity");
            columnMap.put("I", "bankName");
            columnMap.put("J", "accountType");
            columnMap.put("K", "amount");
            columnMap.put("L", "currencyType");
            columnMap.put("M", "protocolNumber");
            columnMap.put("N", "protocolUserNumber");
            columnMap.put("O", "certificateType");
            columnMap.put("P", "licenseNumber");
            columnMap.put("Q", "telNumber");
            columnMap.put("R", "customUserNumber");
            columnMap.put("S", "remark");
            columnMap.put("T", "feedbackCode");
            columnMap.put("U", "reason");
            feedBackColumnName = "V";
        }
    }
    
    /**第三方线下放款excel导入*/
    public class importOffLineExcel extends ExcelTemplet {
        public importOffLineExcel(){
            sheetName = "";
            startRow = 4;
            columnMap.put("A", "no");//序号
            columnMap.put("B", "contractNum");//债权号
            columnMap.put("C", "name");//姓名
            columnMap.put("D", "idNum");//身份证
            columnMap.put("E", "branchName");//来源营业部
            columnMap.put("F", "grantMoneyDate");//放款日期
            columnMap.put("G", "loanTime");//贷款期限
            columnMap.put("H", "pactMoney");//合同金额
            columnMap.put("I", "result");//结果
            columnMap.put("J", "memo");//备注
//            columnMap.put("K", "feedbackCode");//反馈码
//            columnMap.put("L", "reason");//原因
            feedBackColumnName = "K";
        }
    }
    
    /** 对私还款批量导入数据模板格式 **/
    public class PublicAccountInfoWMInputExcel extends ExcelTemplet {
        public PublicAccountInfoWMInputExcel() {
            sheetName = "";
            startRow = 14;
            columnMap.put("A", "tradeDate");
            columnMap.put("B", "tradeTime");
            columnMap.put("C", "breathDate");
            columnMap.put("D", "tradeType");
            columnMap.put("E", "borrowerAmount");
            columnMap.put("F", "tradeAmount");
            columnMap.put("G", "amount");
            columnMap.put("H", "tradeRemark");
            columnMap.put("I", "serialNumber");
            columnMap.put("J", "processNumber");
            columnMap.put("K", "businessName");
            columnMap.put("L", "tradePurpose");
            columnMap.put("M", "businessParamNumber");
            columnMap.put("N", "businessRemark");
            columnMap.put("O", "otherRemark");
            columnMap.put("P", "tradeBank");
            columnMap.put("Q", "secondName");
            columnMap.put("R", "secondAccount");
            columnMap.put("S", "secondOpenAccount");
            columnMap.put("T", "secondOpenName");
            columnMap.put("U", "secondOpenAddress");
            columnMap.put("V", "companyAccountName");
            columnMap.put("W", "companyAccount");
            columnMap.put("X", "companyName");
            columnMap.put("Y", "informationSign");
            columnMap.put("Z", "isAttachment");
            columnMap.put("AA", "balanceSign");
            columnMap.put("AB", "extensionRemark");
            columnMap.put("AC", "tradeAnalysisCode");
            columnMap.put("AD", "billNumber");
            columnMap.put("AE", "payNumber");
            columnMap.put("AF", "internalNumber");
            
            feedBackColumnName = "AG";
        }
    }
    
    /** 线下还款回盘导入数据模板格式 **/
    public class OffLineReturnOfferInputExcel extends ExcelTemplet {
        public OffLineReturnOfferInputExcel() {
            sheetName = "";
            startRow = 2;
            columnMap.put("A", "contractNum");
            columnMap.put("B", "bankType");
            columnMap.put("C", "bankNo");
            columnMap.put("D", "bankAcct");
            columnMap.put("E", "name");
            columnMap.put("F", "offerAmount");
            columnMap.put("G", "returnCode");
            columnMap.put("H", "returnMsg");
            feedBackColumnName = "I";
        }
    }
    
    /** 对公还款批量导入数据模板格式（华瑞渤海） **/
    public class NewPublicAccountHRBHInfoInputExcel extends ExcelTemplet {
        public NewPublicAccountHRBHInfoInputExcel() {
            sheetName = "";
            startRow = 3;
            columnMap.put("A", "repayDate");
            columnMap.put("B", "debitAmount");
            columnMap.put("C", "amount");
            columnMap.put("D", "accountBalance");
            columnMap.put("E", "remark");
            columnMap.put("F", "secondAccount");
            columnMap.put("G", "secondCompany");
            feedBackColumnName = "H";
        }
    }
    
    /** 对公还款-机构（陆金所）批量导入数据模板格式 **/
    public class PublicAccountInfoLufaxInputExcel extends ExcelTemplet {
        public PublicAccountInfoLufaxInputExcel() {
            sheetName = "";
            startRow = 3;
            columnMap.put("A", "tradeDate");
            columnMap.put("B", "selfAccount");
            columnMap.put("C", "loanAmount");
            columnMap.put("D", "tradeAmount");
            columnMap.put("E", "balance");
            columnMap.put("F", "secondAccount");
            columnMap.put("G", "secondName");
            columnMap.put("H", "serialNumber");
            columnMap.put("I", "companySettleCard");
            columnMap.put("J", "tradeRemark");
            columnMap.put("K", "tradePurpose");
            feedBackColumnName = "L";
        }
    }
    
    public class LoanTransferExcel extends ExcelTemplet {
    	public LoanTransferExcel(){
            sheetName = "";
            startRow= 2;//从哪一行开始
            columnMap.put("A", "manageDepartment");
            columnMap.put("B", "loanType");
            columnMap.put("C", "customerName");
            columnMap.put("D", "idNum");
            columnMap.put("E", "signDate");
            columnMap.put("F", "promiseReturnDate");
            columnMap.put("G", "pactMoney");
            columnMap.put("H", "overdueStartDate");
            columnMap.put("I", "overdueTerm");
            columnMap.put("J", "overdueDay");
            columnMap.put("K", "surplusCapital");
            columnMap.put("L", "overdueCapital");
            columnMap.put("M", "overdueAint");
            columnMap.put("N", "fineStartDate");
            columnMap.put("O", "fineAmount");
            columnMap.put("P", "returnTotalAmount");
            columnMap.put("Q", "lastReturnDate");
            columnMap.put("R", "fundsSources");
            columnMap.put("S", "loanBelong");
            columnMap.put("T", "contractNum");
            columnMap.put("U", "transferBatch");
            
            feedBackColumnName = "V";
    	}
    }
    
    public class LoanEvaluateExcel extends ExcelTemplet {
    	public LoanEvaluateExcel(){
            sheetName = "";
            startRow= 2;//从哪一行开始
            columnMap.put("A", "contractNum");
            columnMap.put("B", "name");
            columnMap.put("C", "idNum");
            columnMap.put("D", "isEval");//是否评估
            
            feedBackColumnName = "E";
    	}
    }

    /** 线下还款回盘导入数据模板格式 **/
    public class RiskAccountLoanInputExcel extends ExcelTemplet {
        public RiskAccountLoanInputExcel() {
            sheetName = "";
            startRow = 2;
            columnMap.put("A", "name");
            columnMap.put("B", "loanType");
            columnMap.put("C", "contractNum");
            columnMap.put("D", "idNum");
            columnMap.put("E", "mobile");
            feedBackColumnName = "f";
        }
    }

    public class LoanSettleExcel extends ExcelTemplet {
        public LoanSettleExcel(){
            sheetName = "";
            startRow= 2;//从哪一行开始
            columnMap.put("A", "name");
            columnMap.put("B", "idNum");
            columnMap.put("C", "contractNum");
            columnMap.put("D", "transferState");
            columnMap.put("E", "memo");
            feedBackColumnName = "F";
        }
    }
}
