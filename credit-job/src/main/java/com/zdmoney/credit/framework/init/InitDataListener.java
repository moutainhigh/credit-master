package com.zdmoney.credit.framework.init;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ServletContextAware;

import com.zdmoney.credit.core.service.pub.ILoanCoreService;
import com.zdmoney.credit.offer.service.pub.IOfferInfoService;

public class InitDataListener implements InitializingBean, ServletContextAware {

	@Autowired
	ILoanCoreService loanCoreService;
	
	@Autowired
	private IOfferInfoService offerInfoService;
	
	@Override
	public void setServletContext(ServletContext arg0) {
		// TODO Auto-generated method stub
		
		/**初始化核心接口配置信息**/
		loanCoreService.initFieldMapper();
		/** 初始化银行关系字典表 **/
		offerInfoService.initBankRelateTppMap();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
	}

}
