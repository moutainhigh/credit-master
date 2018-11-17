package com.zdmoney.credit.loan.util;

import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.offer.domain.OfferFlow;

public class OfferFlowUtil {
	
	/**
	 * 获取科目名称
	 * @param offerFlow
	 * @return
	 */
	public static String getAccountTitle(OfferFlow offerFlow){
		String itemName = "";
		
		String account = Strings.parseString(offerFlow.getAccount());
		String acctTitle = Strings.parseString(offerFlow.getAcctTitle());
		String appoDorc = Strings.parseString(offerFlow.getAppoDorc());
		switch (acctTitle) {
			case Const.ACCOUNT_TITLE_AMOUNT : 
				if (appoDorc.equals("C")) {
					itemName = "期初余额";
				} else {
					itemName = "期末余额";
				}
				break;
			case Const.ACCOUNT_TITLE_LOAN_AMOUNT : 
				itemName = "本金";
				break;
			case Const.ACCOUNT_TITLE_OTHER_PAYABLE : 
				itemName = "其他应付款";
				break;
			case Const.ACCOUNT_TITLE_INTEREST_EXP : 
				itemName = "利息";
				break;
			case Const.ACCOUNT_TITLE_FINE_EXP : 
				itemName = "罚息";
				if (account.equals(Const.ACCOUNT_GAINS)) {
					itemName += "(减免)";
				}
				break;
			case Const.ACCOUNT_TITLE_MANAGEC_EXP : 
				itemName = "丙方管理费";
				if (Strings.parseString(offerFlow.getAppoDorc()).equals("D") 
						&& Strings.parseString(offerFlow.getDorc()).equals("C")) {
					itemName += "(退费)";
				}
				break;
			case Const.ACCOUNT_TITLE_CONSULT_EXP : 
				itemName = "咨询费";
				if (Strings.parseString(offerFlow.getAppoDorc()).equals("D") 
						&& Strings.parseString(offerFlow.getDorc()).equals("C")) {
					itemName += "(退费)";
				}
				break;
			case Const.ACCOUNT_TITLE_APPRAISAL_EXP : 
				itemName = "评估费";
				if (Strings.parseString(offerFlow.getAppoDorc()).equals("D") 
						&& Strings.parseString(offerFlow.getDorc()).equals("C")) {
					itemName += "(退费)";
				}
				break;
			case Const.ACCOUNT_TITLE_MANAGE_EXP : 
				itemName = "管理费";
				if (Strings.parseString(offerFlow.getAppoDorc()).equals("D") 
						&& Strings.parseString(offerFlow.getDorc()).equals("C")) {
					itemName += "(退费)";
				}
				break;
			case Const.ACCOUNT_TITLE_PENALTY_EXP : 
				itemName = "违约金";
				break;
			default :
				
				break;
		}
		return itemName;
	}
	
	
	
	
	
}
