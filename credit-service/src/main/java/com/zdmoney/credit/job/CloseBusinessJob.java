/**
 * Copyright (c) 2017, lings@yuminsoft.com All Rights Reserved. <br/>
 * Date:2017年6月2日下午1:43:12
 *
*/

package com.zdmoney.credit.job;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.operation.service.pub.ILoanCsCloseBusinessInfoService;

/**
 * 关门营业部刷新状态
 * ClassName:closeBusinessJob <br/>
 * Date:     2017年6月2日 下午1:43:12 <br/>
 * @author   lings@yuminsoft.com
 */
@Service
public class CloseBusinessJob {
	private static Logger logger = Logger.getLogger(CloseBusinessJob.class);
	
	@Autowired
	ILoanCsCloseBusinessInfoService businessInfoService;
	
	public void updateStyle(){
		logger.info("开始更新关门营业部...");
		businessInfoService.flushShutedShop();
		logger.info("更新关门营业部完成...");
	}
}
