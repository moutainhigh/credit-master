package com.zdmoney.credit.loan.util;

import com.zdmoney.credit.common.constant.Const;
import com.zdmoney.credit.common.util.Strings;

public class OfferRepayUtil {
	/**
	 * 获取科目名称
	 * @param offerFlow
	 * @return
	 */
	public static String getTradeCodeName(String tradeCode){
		String tradeCodeName = "";
		tradeCode = Strings.parseString(tradeCode);
		switch (tradeCode) {
			case Const.TRADE_CODE_NORMAL :
				tradeCodeName = "账户还款";
				break;
			case Const.TRADE_CODE_IN :
				tradeCodeName = "账户存款";
				break;
			case Const.TRADE_CODE_OUT :
				tradeCodeName = "账户取款";
				break;
			case Const.TRADE_CODE_STDIN :
				tradeCodeName = "学生向保证金账户存款";
				break;
			case Const.TRADE_CODE_STDOUT :
				tradeCodeName = "学生从保证金账户取款";
				break;
			case Const.TRADE_CODE_ORGIN :
				tradeCodeName = "保证金账户存款";
				break;
			case Const.TRADE_CODE_ORGOUT :
				tradeCodeName = "保证金账户取款";
				break;
			case Const.TRADE_CODE_SETTLE :
				tradeCodeName = "结清处理";
				break;
			case Const.TRADE_CODE_ONEOFF :
				tradeCodeName = "一次性（提前还款）";
				break;
			case Const.TRADE_CODE_OPENACC :
				tradeCodeName = "个贷开户";
				break;
			case Const.TRADE_CODE_OPENACC_ASC :
				tradeCodeName = "助学贷开户";
				break;
			case Const.TRADE_CODE_SPECIA :
				tradeCodeName = "减免特殊还款";
				break;
			case Const.TRADE_CODE_MARGINREAY :
				tradeCodeName = "保证金还款";
				break;
			default :
				break;
		}
		return tradeCodeName;
	}
}
