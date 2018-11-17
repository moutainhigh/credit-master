package com.zdmoney.credit.common.constant.trustOffer;

/**
 * Created by ym10094 on 2016/10/17.
 */
public enum TrustOfferEnum {
    ACCOUNT_TITLE_PENALTY_EXP("494","实扣违约金支出"),
    ACCOUNT_TITLE_PENALTY_EXP_A("494A","减免违约金支出"),
    ACCOUNT_TITLE_FINE_EXP("452","实扣罚息支出"),
    ACCOUNT_TITLE_FINE_EXP_A("452A","减免罚息支出"),
    ACCOUNT_TITLE_INTEREST_EXP("451","实扣利息支出"),
    ACCOUNT_TITLE_INTEREST_EXP_A("451A","减免利息支出"),
    ACCOUNT_TITLE_LOAN_AMOUNT("211","实扣本金"),
    ACCOUNT_TITLE_LOAN_AMOUNT_A("211A","减免本金"),
    ACCOUNT_TITLE_OTHER_RECEI("149","实还其他费用"),
    正常还款("正常还款"),逾期还款("逾期还款"),一次结清("一次性结清");

    private String subjectCode;

    private String subjectName;

    private String tradeType;

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    TrustOfferEnum(String subjectCode,String subjectName){
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
    }
    TrustOfferEnum(String tradeType){
        this.tradeType = tradeType;
    }
}
