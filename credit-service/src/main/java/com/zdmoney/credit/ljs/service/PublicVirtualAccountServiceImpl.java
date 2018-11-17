package com.zdmoney.credit.ljs.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.ljs.dao.pub.IPublicAccountDetailDao;
import com.zdmoney.credit.ljs.dao.pub.IPublicVirtualAccountDao;
import com.zdmoney.credit.ljs.domain.PublicAccountDetail;
import com.zdmoney.credit.ljs.domain.PublicVirtualAccount;
import com.zdmoney.credit.ljs.service.pub.IPublicAccountDetailService;
import com.zdmoney.credit.ljs.service.pub.IPublicVirtualAccountService;

/***
 * 陆金所还款结果通知接口
 * @author YM10104
 *
 */
@Service
public class PublicVirtualAccountServiceImpl  implements IPublicVirtualAccountService{

	@Autowired
	IPublicVirtualAccountDao publicVirtualAccountDao;
	
	@Override
	public PublicVirtualAccount save(PublicVirtualAccount publicVirtualAccount) {
		return publicVirtualAccountDao.insert(publicVirtualAccount);
	}

	@Override
	public int addAmtByAccountType(String accountType, BigDecimal addAmt) {
		
		return publicVirtualAccountDao.addAmtByAccountType(accountType,addAmt);
	}

	@Override
	public PublicVirtualAccount findByAccountType(String accountType) {
		return publicVirtualAccountDao.findByAccountType(accountType);
	}	

}
