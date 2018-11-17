package com.zdmoney.credit.ljs.dao.pub;

import java.math.BigDecimal;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.ljs.domain.PublicVirtualAccount;

/**
 * 
 * @author YM10104
 *
 */
public interface IPublicVirtualAccountDao  extends IBaseDao<PublicVirtualAccount>{

	int addAmtByAccountType(String accountType, BigDecimal addAmt);

	PublicVirtualAccount findByAccountType(String accountType);

	int subAmtByAccountType(String string, BigDecimal amount);
	
}
