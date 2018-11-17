package com.zdmoney.credit.common.constant;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * 基础常量
 * @author 00232949
 *
 */
public class Const
{
	public static final String RETURNOFFER_SHEETNAME="交易回盘";
    public static final String RETURNOFFER_SUCCES="0000";//扣款成功
    public static final String RETURNOFFER_SUCCES2="000000";//扣款成功
	public static final String OFFER_BUSITYPE="19900";//代收业务类型代码
    public static final String OFFER_BUSITYPE_F="09900";//代付业务类型代码
	public static final String OFFER_MERNO="200290000004741";//商户号
    public static final String OFFER_MERNO_ZT="200290000013098";//国民信托商户号
	public static final String OFFER_S="S";//代收
    public static final String OFFER_F="F";//代付
	public static final String OFFER_VER="02";//版本号
	public static final String OFFER_EXT="xlsx";//扩展名
	
    public static final String ACCOUNT_TYPE_IN="1";//内部帐户
    public static final String ACCOUNT_TYPE_LA="2";//贷款帐户
    public static  final String FI_OFFER_BANCK_CODE="('102')"; //通过富友报盘的银行代码 //,'304','309','403','4035810'
    public static final String TRADE_CODE_NORMAL="1001"; //正常还款
    public static final String TRADE_CODE_IN="1002"; //账户存款
    public static final String TRADE_CODE_OUT="1003"; //账户取款
    public static final String TRADE_CODE_STDIN="1004"; //学生向保证金账户存款
    public static final String TRADE_CODE_STDOUT="1005"; //学生从保证金账户取款
    public static final String TRADE_CODE_ORGIN="1006"; //保证金账户存款
    public static final String TRADE_CODE_ORGOUT="1007"; //保证金账户取款
    public static final String TRADE_CODE_SETTLE="2001"; //结清处理
    public static final String TRADE_CODE_ONEOFF="3001" ;//一次性（提前还款）
    public static final String TRADE_CODE_OPENACC="4001";//个贷开户
    public static final String TRADE_CODE_OPENACC_ASC="4002";//助学贷开户
    public static final String TRADE_CODE_SPECIA="5001";//减免特殊还款
    public static final String TRADE_CODE_MARGINREAY="5002";//保证金还款
    public static final String TRADE_CODE_DRAWRISK="5003";//提风险金
    public static final String TRADE_CODE_DRAWRISK_STUDENT="5004";//提保证金
    public static final String TRADE_CODE_FILL_RISK="5005";//补足风险金
    public static final String TRADE_CODE_RETURN_RISK="5006";//风险金退回


    public static final BigDecimal PENALTY_INTEREST_RATE=new BigDecimal("0.01");//违约金利率
    public static final BigDecimal PENALTY_FINE_RATE=new BigDecimal("0.001");//罚息利率
    public static final BigDecimal PENALTY_BACK_CONSULT=new BigDecimal("0.3");//退费咨询费比例
    public static final BigDecimal PENALTY_BACK_APPRAISAL=new BigDecimal("0.3");//退费评估费比例
    public static final BigDecimal PENALTY_BACK_MANAGE=new BigDecimal("0.4");//退费管理费比例

    //科目号
    public static final String ACCOUNT_TITLE_AMOUNT="111";       //现金
    public  static final String ACCOUNT_TITLE_INTEREST_RECEI="141"; //应收利息
    public static final String ACCOUNT_TITLE_FINE_RECEI="142";//应收罚息
    public static final String ACCOUNT_TITLE_OTHER_RECEI="149";//其他应收款
    public static final String ACCOUNT_TITLE_INTEREST_PAYABLE="241";//应付利息
    public static final String ACCOUNT_TITLE_FINE_PAYABLE="242";//应付罚息
    public static final String ACCOUNT_TITLE_OTHER_PAYABLE="249";//其他应付款
    public static final String ACCOUNT_TITLE_CONSULT_INCOME="441";//咨询费收入
    public static final String ACCOUNT_TITLE_APPRAISAL_INCOME="442";//评估费收入
    public static final String ACCOUNT_TITLE_MANAGE_INCOME="443";//管理费收入
    public static final String ACCOUNT_TITLE_NONOPERAT_INCOME="449";//营业外收入
    public static final String ACCOUNT_TITLE_NONOPERAT_EXP="499";//营业外支出

    public static final String  ACCOUNT_TITLE_OTHER_INCOME="448";//其他营业收入
    public static final String  ACCOUNT_TITLE_OTHER_EXP="498";//其他营业支出
    public static final String  ACCOUNT_TITLE_CONSULT_EXP="491";//咨询费支出
    public static final String  ACCOUNT_TITLE_APPRAISAL_EXP="492";//评估费支出
    public static final String  ACCOUNT_TITLE_MANAGE_EXP="493";//管理费支出
    public static final String  ACCOUNT_TITLE_MANAGEC_EXP="481";//丙方管理费支出

    public  static final String  ACCOUNT_TITLE_FINE_INCOME="412";//罚息收入
    public static final String  ACCOUNT_TITLE_FINE_EXP="452";//罚息支出

    public static final String  ACCOUNT_TITLE_INTEREST_INCOME="411";//利息收入
    public static final String  ACCOUNT_TITLE_INTEREST_EXP="451";//利息支出

    public static final String  ACCOUNT_TITLE_LOAN_AMOUNT="211";//贷款本金
    public static final String  ACCOUNT_TITLE_FINANCING="161";//理财户 居间人

    public static final String  ACCOUNT_TITLE_PENALTY_INCOME="444"; //违约金收入
    public static final String  ACCOUNT_TITLE_PENALTY_EXP="494";     //违约金支出

    public static final String  ACCOUNT_TITLE_RISK_INCOME="445";//风险金收入
    public static final String  ACCOUNT_TITLE_RISK_EXP="495";//风险金支出


    //帐户号
    public static final String  ACCOUNT_REPAYMENT="ZD0000001090000001";//还款账户
    public static final String  ACCOUNT_GAINS="ZD0000001090000002";//收益账户
    public static final String  ACCOUNT_RISK ="ZD0000001090000003";//风险金账户
    public static final String  ACCOUNT_INVESTMENT="ZD0000001090000004";//投资咨询公司
    public static final String  ACCOUNT_FORTUNE="ZD0000001090000005";//财富账户
    public static final String  ACCOUNT_INTERJACENT="ZD0000001030000001";//居间人账户
    public static final String  ACCOUNT_LENDER="ZD0000001020000001";//出借人账户
    public static final String  ACCOUNT_JIMUHEZIRISK="JMHZ00001090000003";//积木盒子风险金账户
    public static final String  ACCOUNT_JIMUHEZIREPAY="JMHZ00001030000001";//居间人账户

    //外贸账户号
    public static final String ACCOUNT_REPAYMENT_WM1 = "955108480000011"; // 外贸信托 对公还款账户
    public static final String ACCOUNT_REPAYMENT_WM2 = ""; //外贸2 对公还款账户
    public static final String ACCOUNT_REPAYMENT_WM3 = "955107120000015"; //外贸3 对公还款银行账户（待确认）
    public static final int[] PROMISERETURNDATE={1,16};//约定还款日
    public static final String ENDOFDAY_TELLER="A0001";//日终处理柜员号
    public static final String FILLRISK_TELLER="A0003";//积木盒子补足风险金处理柜员号
    public static final String JIMU_REPAYALL_TELLER="A0004";//积木盒子一次性结清处理柜员号
    
	public static final String isClosing = "0";//0是关闭，只要不是0就执行

	public static final String isOpening = "1";//1是执行，只要不是1就关闭


    public enum DorC{D,C};//D-借 C-贷

    public static final String OFFER_BUSITYPE_RECEIVE_FI="AC01";//富友代收业务类型代码
    public static final String RETURNOFFER_SUCCES_FI="扣款成功";//扣款成功
    public static int OFFER_FILE_BUFFER_SIZE=2048;

    public static int task_limit = 5; //自动分单上限
    //通联
   /*  static  Map CONFIG_AIP_OFFER_COLUMN_FIRSTROW_MAP={
            'A': 'Flag',
            'B': 'No',
            'C': 'Date',
            'D': 'Count',
            'E': 'Amount',
            'F':'Type'

    };
     static Map CONFIG_AIP_OFFER_COLUMN_TITLEROW_MAP={
            'A':'A',
            'B':'B',
            'C':'C',
            'D':'D',
            'E':'E',
            'F':'F',
            'G':'G',
            'H':'H',
            'I':'I',
            'J':'J',
            'K':'K',
            'L':'L',
            'M':'M',
            'N':'N',
            'O':'O',
            'P':'P',
            'Q':'Q',
            'R':'R',
            'S':'S',
            'T':'T',
            'U':'U'
     };

     static Map CONFIGE_AIP_TITLEROW_VALUE_MAP=[
            'A':"记录序号",
            'B':"通联支付用户编号",
            'C':"银行代码",
            'D':"帐号类型",
            'E':"账号",
            'F':"户名",
            'G':"开户行所在省",
            'H':"开户行所在市",
            'I':"开户行名称",
            'J':"账户类型",
            'K':"金额",
            'L':"货币类型",
            'M':"协议号",
            'N':"协议用户编号",
            'O':"开户证件类型",
            'P':"证件号",
            'Q':"手机号/小灵通",
            'R':"自定义用户号",
            'S':"备注",
            'T':"反馈码",
            'U':"原因"
    ]

    //富友
     static Map CONFIG_FI_OFFER_COLUMN_TITLEROW_MAP=[
            'A':'A',
            'B':'B',
            'C':'C',
            'D':'D',
            'E':'E',
            'F':'F',
            'G':'G',
            'H':'H'
    ]
     static Map CONFIGE_FI_TITLEROW_VALUE_MAP=[
            'A':"序号",
            'B':"开户行",
            'C':"扣款人银行账号",
            'D':"户名",
            'E':"金额(单位:元)",
            'F':"企业流水号",
            'G':"备注",
            'H':"手机号"
    ]
     static  Map HIGH_STATE=[
            10:'签订回退',
            7:'终审回退',
            4:'门店重提',
            1:'普通申请'

    ]*/

    public final static  Map<String,String> SPECIAL_REPAYMENT_TYPE=new HashMap<String,String>();
    static{
    	SPECIAL_REPAYMENT_TYPE.put("onetime", SpecialRepaymentTypeEnum.一次性还款.name());
    	SPECIAL_REPAYMENT_TYPE.put("reduction", SpecialRepaymentTypeEnum.减免.name());
    	SPECIAL_REPAYMENT_TYPE.put("advanceDeduct", SpecialRepaymentTypeEnum.提前扣款.name());
    	SPECIAL_REPAYMENT_TYPE.put(SpecialRepaymentTypeEnum.一次性还款.name(), "onetime");
    	SPECIAL_REPAYMENT_TYPE.put(SpecialRepaymentTypeEnum.减免.name(), "reduction");
    	SPECIAL_REPAYMENT_TYPE.put(SpecialRepaymentTypeEnum.提前扣款.name(), "advanceDeduct");
    }
    
    public final static  Map<String,String> SPECIAL_REPAYMENT_STATE=new HashMap<String,String>();
    static{
    	SPECIAL_REPAYMENT_STATE.put("application", SpecialRepaymentStateEnum.申请.name());
    	SPECIAL_REPAYMENT_STATE.put("cancel", SpecialRepaymentStateEnum.取消.name());
    	SPECIAL_REPAYMENT_STATE.put(SpecialRepaymentStateEnum.申请.name(), "application");
    	SPECIAL_REPAYMENT_STATE.put(SpecialRepaymentStateEnum.取消.name(), "cancel");
    }
    
    public final static  Map<String,String> OFFER_STATE=new HashMap<String,String>();
    static{
    	OFFER_STATE.put("returned", OfferStateEnum.已回盘.name());
    	OFFER_STATE.put("reported", OfferStateEnum.已报盘.name());
    	OFFER_STATE.put("unreport", OfferStateEnum.未报盘.name());
    	OFFER_STATE.put(OfferStateEnum.已回盘.name(), "returned");
    	OFFER_STATE.put(OfferStateEnum.已报盘.name(), "reported");
    	OFFER_STATE.put(OfferStateEnum.未报盘.name(), "unreport");
    }
}
