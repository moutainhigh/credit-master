package com.zdmoney.credit.ljs.service.pub;

import java.math.BigDecimal;

import com.zdmoney.credit.ljs.domain.PublicVirtualAccount;


/***
 * 陆金所还款结果通知处理
 * @author YM10104
 *
 */
public interface IPublicVirtualAccountService{
	
	/**
	 *  保存自扣还款结果
	 * @param repayResultNotifyLog
	 * @return
	 */
	public PublicVirtualAccount save(PublicVirtualAccount publicVirtualAccount);
	/**
	 * 
	 * @param string
	 * @param addAmt 增加的金额
	 * @return
	 */
	public int addAmtByAccountType(String accountType, BigDecimal addAmt);
	/**
	 * 通过机构类型查找账户信息
	 * @param string
	 * @return
	 */
	public PublicVirtualAccount findByAccountType(String accountType);
}
